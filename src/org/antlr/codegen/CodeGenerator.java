/*
[The "BSD licence"]
Copyright (c) 2005 Terence Parr
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.antlr.codegen;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRewriteEngine;
import antlr.TokenWithIndex;
import antlr.collections.AST;
import org.antlr.Tool;
import org.antlr.analysis.*;
import org.antlr.misc.*;
import org.antlr.runtime.CharStream;
import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplateGroupLoader;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
import org.antlr.tool.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** ANTLR's code generator.
 *
 *  Generate recognizers derived from grammars.  Language independence
 *  achieved through the use of StringTemplateGroup objects.  All output
 *  strings are completely encapsulated in the group files such as Java.stg.
 *  Some computations are done that are unused by a particular language.
 *  This generator just computes and sets the values into the templates;
 *  the templates are free to use or not use the information.
 *
 *  To make a new code generation target, define X.stg for language X
 *  by copying from existing Y.stg most closely releated to your language;
 *  e.g., to do CSharp.stg copy Java.stg.  The template group file has a
 *  bunch of templates that are needed by the code generator.  You can add
 *  a new target w/o even recompiling ANTLR itself.  The language=X option
 *  in a grammar file dictates which templates get loaded/used.
 *
 *  Some language like C need both parser files and header files.  Java needs
 *  to have a separate file for the cyclic DFA as ANTLR generates bytecodes
 *  directly (which cannot be in the generated parser Java file).  To facilitate
 *  this,
 *
 * cyclic can be in same file, but header, output must be searpate.  recognizer
 *  is in outptufile.
 */
public class CodeGenerator {
	/** When generating SWITCH statements, some targets might need to limit
	 *  the size (based upon the number of case labels).  Generally, this
	 *  limit will be hit only for lexers where wildcard in a UNICODE
	 *  vocabulary environment would generate a SWITCH with 65000 labels.
	 */
	public int MAX_SWITCH_CASE_LABELS = 300;
	public int MIN_SWITCH_ALTS = 3;
	public boolean GENERATE_SWITCHES_WHEN_POSSIBLE = true;

	public String classpathTemplateRootDirectoryName =
		"org/antlr/codegen/templates";

	/** Which grammar are we generating code for?  Each generator
	 *  is attached to a specific grammar.
	 */
	protected Grammar grammar;

	/** What language are we generating? */
	protected String language;

	/** The target specifies how to write out files and do other language
	 *  specific actions.
	 */
	public Target target = null;

	/** Where are the templates this generator should use to generate code? */
	protected StringTemplateGroup templates;

	protected StringTemplate recognizerST;
	protected StringTemplate outputFileST;
	protected StringTemplate headerFileST;

	/** Used to create unique labels */
	protected int uniqueLabelNumber = 1;

	/** A reference to the ANTLR tool so we can learn about output directories
	 *  and such.
	 */
	protected Tool tool;

	/** Generate debugging event method calls */
	protected boolean debug;

	/** Create a Tracer object and make the recognizer invoke this. */
	protected boolean trace;

	/** Track runtime parsing information about decisions etc...
	 *  This requires the debugging event mechanism to work.
	 */
	protected boolean profile;

	/** Cache rule invocation results during backtracking */
	protected boolean memoize = true;

	/** I have factored out the generation of acyclic DFAs to separate class */
	protected ACyclicDFACodeGenerator acyclicDFAGenerator =
		new ACyclicDFACodeGenerator(this);

	/** I have factored out the generation of cyclic DFAs to separate class */
	protected CyclicDFACodeGenerator cyclicDFAGenerator =
		new CyclicDFACodeGenerator(this);

	public static final String VOCAB_FILE_EXTENSION = ".tokens";
	protected final String vocabFilePattern =
		"<tokens:{<attr.name>=<attr.type>\n}>" +
		"<literals:{<attr.name>=<attr.type>\n}>";

	public CodeGenerator(Tool tool, Grammar grammar, String language) {
		this.tool = tool;
		this.grammar = grammar;
		this.language = language;
		loadLanguageTarget(language);
	}

	protected void loadLanguageTarget(String language) {
		String targetName = "org.antlr.codegen."+language+"Target";
		try {
			Class c = Class.forName(targetName);
			target = (Target)c.newInstance();
		}
		catch (ClassNotFoundException cnfe) {
			target = new Target(); // use default
		}
		catch (InstantiationException ie) {
			ErrorManager.error(ErrorManager.MSG_CANNOT_CREATE_TARGET_GENERATOR,
							   targetName,
							   ie);
		}
		catch (IllegalAccessException cnfe) {
			ErrorManager.error(ErrorManager.MSG_CANNOT_CREATE_TARGET_GENERATOR,
							   targetName,
							   cnfe);
		}
	}

	/** load the main language.stg template group file */
	protected void loadTemplates(String language) {
		// get a group loader containing main templates dir and target subdir
		String templateDirs =
			classpathTemplateRootDirectoryName+":"+
			classpathTemplateRootDirectoryName+"/"+language;
		//System.out.println("targets="+templateDirs.toString());
		StringTemplateGroupLoader loader =
			new CommonGroupLoader(templateDirs.toString(),
							  ErrorManager.getStringTemplateErrorListener());
		StringTemplateGroup.registerGroupLoader(loader);
		StringTemplateGroup.registerDefaultLexer(AngleBracketTemplateLexer.class);

		// first load main language template
		StringTemplateGroup coreTemplates =
			StringTemplateGroup.loadGroup(language);
		if ( coreTemplates==null ) {
			ErrorManager.error(ErrorManager.MSG_MISSING_CODE_GEN_TEMPLATES,
							   language);
			return;
		}

		// dynamically add subgroups that act like filters to apply to
		// their supergroup.  E.g., Java:Dbg:AST:ASTDbg.
		String outputOption = (String)grammar.getOption("output");
		if ( outputOption!=null && outputOption.equals("AST") ) {
			if ( debug && grammar.type!=Grammar.LEXER ) {
				StringTemplateGroup dbgTemplates =
					StringTemplateGroup.loadGroup("Dbg", coreTemplates);
				StringTemplateGroup astTemplates =
					StringTemplateGroup.loadGroup("AST",dbgTemplates);
				StringTemplateGroup astDbgTemplates =
					StringTemplateGroup.loadGroup("ASTDbg", astTemplates);
				templates = astDbgTemplates;
			}
			else {
				templates = StringTemplateGroup.loadGroup("AST", coreTemplates);
			}
		}
		else if ( outputOption!=null && outputOption.equals("template") ) {
			if ( debug && grammar.type!=Grammar.LEXER ) {
				StringTemplateGroup dbgTemplates =
					StringTemplateGroup.loadGroup("Dbg", coreTemplates);
				StringTemplateGroup stTemplates =
					StringTemplateGroup.loadGroup("ST",dbgTemplates);
				/*
				StringTemplateGroup astDbgTemplates =
					StringTemplateGroup.loadGroup("STDbg", astTemplates);
				*/
				templates = stTemplates;
			}
			else {
				templates = StringTemplateGroup.loadGroup("ST", coreTemplates);
			}
		}
		else if ( debug && grammar.type!=Grammar.LEXER ) {
			templates = StringTemplateGroup.loadGroup("Dbg", coreTemplates);
		}
		else {
			templates = coreTemplates;
		}
	}

	/** Given the grammar to which we are attached, walk the AST associated
	 *  with that grammar to create NFAs.  Then create the DFAs for all
	 *  decision points in the grammar by converting the NFAs to DFAs.
	 *  Finally, walk the AST again to generate code.
	 *
	 *  Either 1 or 2 files are written:
	 *
	 * 		recognizer: the main parser/lexer/treewalker item
	 * 		header file: language like C/C++ need extern definitions
	 *
	 *  The target, such as JavaTarget, dictates which files get written.
	 */
	public void genRecognizer() {
		// LOAD OUTPUT TEMPLATES
		loadTemplates(language);
		if ( templates==null ) {
			return;
		}

		// CREATE NFA FROM GRAMMAR, CREATE DFA FROM NFA
		target.performGrammarAnalysis(this, grammar);

		// OPTIMIZE DFA
		DFAOptimizer optimizer = new DFAOptimizer(grammar);
		optimizer.optimize();

		// OUTPUT FILE (contains recognizerST)
		outputFileST = templates.getInstanceOf("outputFile");

		// HEADER FILE
		if ( templates.isDefined("headerFile") ) {
			headerFileST = templates.getInstanceOf("headerFile");
		}
		else {
			// create a dummy to avoid null-checks all over code generator
			headerFileST = new StringTemplate(templates,"");
			headerFileST.setName("dummy-header-file");
		}

		// Ok, the only two possible output files are available now.
		// Verify action scopes are ok for target and dump actions into output
		// Templates can say <actions.parser.header> for example.
		verifyActionScopesOkForTarget(grammar.getActions());
		// translate $x::y references
		translateActionAttributeReferences(grammar.getActions());
		headerFileST.setAttribute("actions", grammar.getActions());
		outputFileST.setAttribute("actions", grammar.getActions());

		boolean filterMode = grammar.getOption("filter")!=null &&
						 	 grammar.getOption("filter").equals("true");
		boolean canBacktrack = grammar.getSyntacticPredicates()!=null ||
							   filterMode;
		outputFileST.setAttribute("backtracking", new Boolean(canBacktrack));
		headerFileST.setAttribute("backtracking", new Boolean(canBacktrack));
		outputFileST.setAttribute("memoize", new Boolean(memoize&&canBacktrack));
		headerFileST.setAttribute("memoize", new Boolean(memoize&&canBacktrack));
		Set synpredNames = null;
		if ( grammar.getSyntacticPredicates()!=null ) {
			synpredNames = grammar.getSyntacticPredicates().keySet();
		}
		outputFileST.setAttribute("synpreds", synpredNames);
		headerFileST.setAttribute("synpreds", synpredNames);

		outputFileST.setAttribute("trace", new Boolean(trace));
		headerFileST.setAttribute("trace", new Boolean(trace));

		// RECOGNIZER
		if ( grammar.type==Grammar.LEXER ) {
			recognizerST = templates.getInstanceOf("lexer");
			outputFileST.setAttribute("LEXER", new Boolean(true));
			headerFileST.setAttribute("LEXER", new Boolean(true));
			recognizerST.setAttribute("filterMode",
									  new Boolean(filterMode));
		}
		else if ( grammar.type==Grammar.PARSER ||
			grammar.type==Grammar.COMBINED )
		{
			recognizerST = templates.getInstanceOf("parser");
			outputFileST.setAttribute("PARSER", new Boolean(true));
			headerFileST.setAttribute("PARSER", new Boolean(true));
		}
		else {
			recognizerST = templates.getInstanceOf("treeParser");
			outputFileST.setAttribute("TREE_PARSER", new Boolean(true));
			headerFileST.setAttribute("TREE_PARSER", new Boolean(true));
		}
		outputFileST.setAttribute("recognizer", recognizerST);
		outputFileST.setAttribute("actionScope",
								  grammar.getDefaultActionScope(grammar.type));

		outputFileST.setAttribute("fileName", grammar.getFileName());
		outputFileST.setAttribute("ANTLRVersion", Tool.VERSION);
		headerFileST.setAttribute("ANTLRVersion", Tool.VERSION);
		outputFileST.setAttribute("generatedTimestamp", Tool.getCurrentTimeStamp());
		headerFileST.setAttribute("fileName", grammar.getFileName());
		headerFileST.setAttribute("generatedTimestamp", Tool.getCurrentTimeStamp());

		// GENERATE RECOGNIZER
		// Walk the AST holding the input grammar, this time generating code
		// Decisions are generated by using the precomputed DFAs
		// Fill in the various templates with data
		CodeGenTreeWalker gen = new CodeGenTreeWalker();
		try {
			gen.grammar((AST)grammar.getGrammarTree(),
						grammar,
						recognizerST,
						outputFileST,
						headerFileST);
		}
		catch (RecognitionException re) {
			ErrorManager.error(ErrorManager.MSG_BAD_AST_STRUCTURE,
							   re);
		}
		genTokenTypeConstants(recognizerST);
		genTokenTypeConstants(outputFileST);
		genTokenTypeConstants(headerFileST);

		if ( grammar.type!=Grammar.LEXER ) {
			genTokenTypeNames(recognizerST);
			genTokenTypeNames(outputFileST);
			genTokenTypeNames(headerFileST);
		}

		// WRITE FILES
		try {
			target.genRecognizerFile(tool,this,grammar,outputFileST);
			target.genRecognizerHeaderFile(tool,this,grammar,headerFileST);
			// write out the vocab interchange file; used by antlr,
			// does not change per target
			StringTemplate tokenVocabSerialization = genTokenVocabOutput();
			write(tokenVocabSerialization, getVocabFileName());
			//System.out.println(outputFileST.toStructureString());
		}
		catch (IOException ioe) {
			ErrorManager.error(ErrorManager.MSG_CANNOT_WRITE_FILE,
							   getVocabFileName(),
							   ioe);
		}
		/*
		System.out.println("num obj.prop refs: "+ ASTExpr.totalObjPropRefs);
		System.out.println("num reflection lookups: "+ ASTExpr.totalReflectionLookups);
		*/
	}

	/** Some targets will have some extra scopes like C++ may have
	 *  '@headerfile:name {action}' or something.  Make sure the
	 *  target likes the scopes in action table.
	 */
	protected void verifyActionScopesOkForTarget(Map actions) {
		Set actionScopeKeySet = actions.keySet();
		for (Iterator it = actionScopeKeySet.iterator(); it.hasNext();) {
			String scope = (String)it.next();
			if ( !target.isValidActionScope(grammar.type, scope) ) {
				// get any action from the scope to get error location
				Map scopeActions = (Map)actions.get(scope);
				GrammarAST actionAST =
					(GrammarAST)scopeActions.values().iterator().next();
				ErrorManager.grammarError(
					ErrorManager.MSG_INVALID_ACTION_SCOPE,grammar,
					actionAST.getToken(),scope,
					Grammar.grammarTypeToString[grammar.type]);
			}
		}
	}

	/** Actions may reference $x::y attributes, call translateAction on
	 *  each action and replace that action in the Map.
	 */
	protected void translateActionAttributeReferences(Map actions) {
		Set actionScopeKeySet = actions.keySet();
		for (Iterator it = actionScopeKeySet.iterator(); it.hasNext();) {
			String scope = (String)it.next();
			Map scopeActions = (Map)actions.get(scope);
			translateActionAttributeReferencesForSingleScope(null,scopeActions);
		}
	}

	/** Use for translating rule @init{...} actions that have no scope */
	protected void translateActionAttributeReferencesForSingleScope(
		Rule r,
		Map scopeActions)
	{
		String ruleName=null;
		if ( r!=null ) {
			ruleName = r.name;
		}
		Set actionNameSet = scopeActions.keySet();
		for (Iterator nameIT = actionNameSet.iterator(); nameIT.hasNext();) {
			String name = (String) nameIT.next();
			GrammarAST actionAST = (GrammarAST)scopeActions.get(name);
			String action = translateAction(ruleName,actionAST);
			scopeActions.put(name, action); // replace with translation
		}
	}

	/*
	protected void fillFOLLOWSets(StringTemplate recognizerST,
	StringTemplate outputFileST,
	StringTemplate headerFileST)
	{
	long start = System.currentTimeMillis();
	for (Iterator itr = grammar.getRules().iterator(); itr.hasNext();) {
	Grammar.Rule r = (Grammar.Rule) itr.next();
	LookaheadSet follow = grammar.FOLLOW(r.name);
	//LookaheadSet follow = new LookaheadSet();
	System.out.println("FOLLOW("+r.name+")="+follow.toString(grammar));
	// TODO: not sending in EOF for FOLLOW sets! (might not need;
	// consume until will know to stop at EOF)
	long[] words = null;
	if ( follow.tokenTypeSet==null ) {
	words = new long[1];
	}
	else {
	BitSet bits = BitSet.of(follow.tokenTypeSet);
	//bits.remove(Label.EOF);
	words = bits.toPackedArray();
	}
	recognizerST.setAttribute("bitsets.{name,inName,bits}",
	r.name,
	words);
	outputFileST.setAttribute("bitsets.{name,inName,bits}",
	r.name,
	words);
	headerFileST.setAttribute("bitsets.{name,inName,bits}",
	r.name,
	words);
	}
	long stop = System.currentTimeMillis();
	System.out.println("FOLLOW sets computed in "+(int)(stop-start)+" ms");
	}
	*/

	/** Error recovery in ANTLR recognizers.
	 *
	 *  Based upon original ideas:
	 *
	 *  Algorithms + Data Structures = Programs by Niklaus Wirth
	 *
	 *  and
	 *
	 *  A note on error recovery in recursive descent parsers:
	 *  http://portal.acm.org/citation.cfm?id=947902.947905
	 *
	 *  Later, Josef Grosch had some good ideas:
	 *  Efficient and Comfortable Error Recovery in Recursive Descent Parsers:
	 *  ftp://www.cocolab.com/products/cocktail/doca4.ps/ell.ps.zip
	 *
	 *  Like Grosch I implemented local FOLLOW sets that are combined at run-time
	 *  upon error to avoid parsing overhead.
	 */
	public void generateLocalFOLLOW(GrammarAST referencedElementNode,
									String referencedElementName,
									String enclosingRuleName)
	{
		NFAState followingNFAState = referencedElementNode.followingNFAState;
		int i = ((TokenWithIndex)referencedElementNode.getToken()).getIndex();
		/*
		System.out.print("compute FOLLOW "+referencedElementNode.toString()+
		" for "+referencedElementName+"#"+i+" in "+
		enclosingRuleName);
		*/
		LookaheadSet follow = grammar.LOOK(followingNFAState);
		//System.out.println(" "+follow);

		long[] words = null;
		if ( follow==null || follow.tokenTypeSet==null ) {
			words = new long[1];
		}
		else {
			BitSet bits = BitSet.of(follow.tokenTypeSet);
			words = bits.toPackedArray();
		}
		// use the target to convert to hex strings (typically)
		String[] wordStrings = new String[words.length];
		for (int j = 0; j < words.length; j++) {
			long w = words[j];
			wordStrings[j] = target.getTarget64BitStringFromValue(w);
		}
		recognizerST.setAttribute("bitsets.{name,inName,bits,tokenIndex}",
								  referencedElementName,
								  enclosingRuleName,
								  wordStrings,
								  new Integer(i));
		outputFileST.setAttribute("bitsets.{name,inName,bits,tokenIndex}",
								  referencedElementName,
								  enclosingRuleName,
								  wordStrings,
								  new Integer(i));
		headerFileST.setAttribute("bitsets.{name,inName,bits,tokenIndex}",
								  referencedElementName,
								  enclosingRuleName,
								  wordStrings,
								  new Integer(i));
	}

	// L O O K A H E A D  D E C I S I O N  G E N E R A T I O N

	/** Generate code that computes the predicted alt given a DFA.  The
	 *  recognizerST can be either the main generated recognizerTemplate
	 *  for storage in the main parser file or a separate file.  It's up to
	 *  the code that ultimately invokes the codegen.g grammar rule.
	 *
	 *  Regardless, the output file and header file get a copy of the DFAs.
	 */
	public StringTemplate genLookaheadDecision(StringTemplate recognizerST,
											   DFA dfa)
	{
		StringTemplate decisionST;
		if ( !dfa.isCyclic() /* TODO: or too big */ ) {
			decisionST =
				acyclicDFAGenerator.genFixedLookaheadDecision(getTemplates(), dfa);
		}
		else {
			StringTemplate dfaST =
				cyclicDFAGenerator.genCyclicLookaheadDecision(templates,
															  dfa);
			recognizerST.setAttribute("cyclicDFAs", dfaST);
			outputFileST.setAttribute("cyclicDFAs", dfaST);
			headerFileST.setAttribute("cyclicDFAs", dfaST);
			decisionST = templates.getInstanceOf("dfaDecision");
			String description = dfa.getNFADecisionStartState().getDescription();
			description = target.getTargetStringLiteralFromString(description);
			if ( description!=null ) {
				decisionST.setAttribute("description", description);
			}
			decisionST.setAttribute("decisionNumber",
									new Integer(dfa.getDecisionNumber()));
		}
		return decisionST;
	}

	/** Generate an expression for traversing an edge. */
	protected StringTemplate genLabelExpr(StringTemplateGroup templates,
										  Transition edge,
										  int k)
	{
		Label label = edge.label;
		if ( label.isSemanticPredicate() ) {
			return genSemanticPredicateExpr(templates, edge);
		}
		if ( label.isSet() ) {
			return genSetExpr(templates, label.getSet(), k, true);
		}
		// must be simple label
		StringTemplate eST = templates.getInstanceOf("lookaheadTest");
		eST.setAttribute("atom", getTokenTypeAsTargetLabel(label.getAtom()));
		eST.setAttribute("atomAsInt", new Integer(label.getAtom()));
		eST.setAttribute("k", new Integer(k));
		return eST;
	}

	protected StringTemplate genSemanticPredicateExpr(StringTemplateGroup templates,
													  Transition edge)
	{
		Label label = edge.label;
		SemanticContext semCtx = label.getSemanticContext();
		return semCtx.genExpr(this,templates,((DFAState)edge.target).dfa);
	}

	/** For intervals such as [3..3, 30..35], generate an expression that
	 *  tests the lookahead similar to LA(1)==3 || (LA(1)>=30&&LA(1)<=35)
	 */
	public StringTemplate genSetExpr(StringTemplateGroup templates,
									 IntSet set,
									 int k,
									 boolean partOfDFA)
	{
		IntervalSet iset = (IntervalSet)set;
		if ( !(iset instanceof IntervalSet) ) {
			throw new IllegalArgumentException("unable to generate expressions for non IntervalSet objects");
		}
		if ( iset.getIntervals()==null || iset.getIntervals().size()==0 ) {
			StringTemplate emptyST = new StringTemplate(templates, "");
			emptyST.setName("empty-set-expr");
			return emptyST;
		}
		String testSTName = "lookaheadTest";
		String testRangeSTName = "lookaheadRangeTest";
		if ( !partOfDFA ) {
			testSTName = "isolatedLookaheadTest";
			testRangeSTName = "isolatedLookaheadRangeTest";
		}
		StringTemplate setST = templates.getInstanceOf("setTest");
		Iterator iter = iset.getIntervals().iterator();
		int rangeNumber = 1;
		while (iter.hasNext()) {
			Interval I = (Interval) iter.next();
			int a = I.a;
			int b = I.b;
			StringTemplate eST;
			if ( a==b ) {
				eST = templates.getInstanceOf(testSTName);
				eST.setAttribute("atom", getTokenTypeAsTargetLabel(a));
				eST.setAttribute("atomAsInt", new Integer(a));
				//eST.setAttribute("k",new Integer(k));
			}
			else {
				eST = templates.getInstanceOf(testRangeSTName);
				eST.setAttribute("lower",getTokenTypeAsTargetLabel(a));
				eST.setAttribute("lowerAsInt", new Integer(a));
				eST.setAttribute("upper",getTokenTypeAsTargetLabel(b));
				eST.setAttribute("upperAsInt", new Integer(b));
				eST.setAttribute("rangeNumber",new Integer(rangeNumber));
			}
			eST.setAttribute("k",new Integer(k));
			setST.setAttribute("ranges", eST);
			rangeNumber++;
		}
		return setST;
	}

	// T O K E N  D E F I N I T I O N  G E N E R A T I O N

	/** Set attributes tokens and literals attributes in the incoming
	 *  code template.  This is not the token vocab interchange file, but
	 *  rather a list of token type ID needed by the recognizer.
	 */
	protected void genTokenTypeConstants(StringTemplate code) {
		// make constants for the token types
		Iterator tokenIDs = grammar.getTokenIDs().iterator();
		while (tokenIDs.hasNext()) {
			String tokenID = (String) tokenIDs.next();
			int tokenType = grammar.getTokenType(tokenID);
			if ( tokenType==Label.EOF ||
				 tokenType>=Label.MIN_TOKEN_TYPE )
			{
				// don't do FAUX labels 'cept EOF
				code.setAttribute("tokens.{name,type}", tokenID, new Integer(tokenType));
			}
		}
	}

	/** Generate a token names table that maps token type to a printable
	 *  name: either the label like INT or the literal like "begin".
	 */
	protected void genTokenTypeNames(StringTemplate code) {
		for (int t=Label.MIN_TOKEN_TYPE; t<=grammar.getMaxTokenType(); t++) {
			String tokenName = grammar.getTokenDisplayName(t);
			if ( tokenName!=null ) {
				tokenName=target.getTargetStringLiteralFromString(tokenName, true);
				code.setAttribute("tokenNames", tokenName);
			}
		}
	}

	/** Get a meaningful name for a token type useful during code generation.
	 *  Literals without associated names are converted to the string equivalent
	 *  of their integer values. Used to generate x==ID and x==34 type comparisons
	 *  etc...  Essentially we are looking for the most obvious way to refer
	 *  to a token type in the generated code.  If in the lexer, return the
	 *  char literal translated to the target language.  For example, ttype=10
	 *  will yield '\n' from the getTokenDisplayName method.  That must
	 *  be converted to the target languages literals.  For most C-derived
	 *  languages no translation is needed.
	 */
	public String getTokenTypeAsTargetLabel(int ttype) {
		String name = grammar.getTokenDisplayName(ttype);
		if ( grammar.type==Grammar.LEXER ) {
			return target.getTargetCharLiteralFromANTLRCharLiteral(this,name);
		}
		// If name is a literal, return the token type instead
		if ( name.charAt(0)=='\'' ) {
			return String.valueOf(ttype);
		}
		if ( ttype==Label.EOF ) {
			return String.valueOf(CharStream.EOF);
		}
		return name;
	}

	/** Generate a token vocab file with all the token names/types.  For example:
	 *  ID=7
	 *  FOR=8
	 *  This is independent of the target language; used by antlr internally
	 */
	protected StringTemplate genTokenVocabOutput() {
		StringTemplate vocabFileST =
			new StringTemplate(vocabFilePattern,
							   AngleBracketTemplateLexer.class);
		vocabFileST.setName("vocab-file");
		// make constants for the token names
		Iterator tokenIDs = grammar.getTokenIDs().iterator();
		while (tokenIDs.hasNext()) {
			String tokenID = (String) tokenIDs.next();
			int tokenType = grammar.getTokenType(tokenID);
			if ( tokenType>=Label.MIN_TOKEN_TYPE ) {
				vocabFileST.setAttribute("tokens.{name,type}", tokenID, new Integer(tokenType));
			}
		}

		// now dump the strings
		Iterator literals = grammar.getStringLiterals().iterator();
		while (literals.hasNext()) {
			String literal = (String) literals.next();
			int tokenType = grammar.getTokenType(literal);
			if ( tokenType>=Label.MIN_TOKEN_TYPE ) {
				vocabFileST.setAttribute("tokens.{name,type}", literal, new Integer(tokenType));
			}
		}

		return vocabFileST;
	}

	public String translateAction(String ruleName,
								  GrammarAST actionTree)
	{
		ActionTranslator translator = new ActionTranslator(this);
		return translator.translate(ruleName,actionTree);
	}

	/** Given a template constructor action like %foo(a={...}) in
	 *  an action, translate it to the appropriate template constructor
	 *  from the templateLib.
	 *
	 *  This translates a *piece* of the action and hence this routine
	 *  returns the index of the char at which it stopped parsing.
	 *  This is a real mess in java.  blech.
	 */
	public String translateTemplateConstructor(String ruleName,
											   GrammarAST actionTree,
											   int startIndex,
											   MutableInteger nextCharIndexI)
	{
		// first, parse with antlr.g
		String actionText = actionTree.getText();
		// silly, but I need to create my own counter...
		class CountingStringReader extends StringReader {
			public CountingStringReader(String text) {
				super(text);
			}
			public int n = 0; // how many chars are read
			public int read() throws IOException {
				n++;
				return super.read();
			}
		};
		CountingStringReader sr = new CountingStringReader(actionText);
		try {
			sr.skip(startIndex);
		}
		catch (IOException ioe) {
			ErrorManager.internalError("problems reading template action", ioe);
		}
		ANTLRLexer lexer = new ANTLRLexer(sr);
		lexer.setFilename(grammar.getFileName());
		// use the rewrite engine because we want to buffer up all tokens
		// in case they have a merged lexer/parser, send lexer rules to
		// new grammar.
		lexer.setTokenObjectClass("antlr.TokenWithIndex");
		TokenStreamRewriteEngine tokenBuffer = new TokenStreamRewriteEngine(lexer);
		tokenBuffer.discard(ANTLRParser.WS);
		tokenBuffer.discard(ANTLRParser.ML_COMMENT);
		tokenBuffer.discard(ANTLRParser.COMMENT);
		tokenBuffer.discard(ANTLRParser.SL_COMMENT);
		ANTLRParser parser = new ANTLRParser(tokenBuffer);
		parser.setFilename(grammar.getFileName());
		parser.setASTNodeClass("org.antlr.tool.GrammarAST");
		try {
			parser.rewrite_template();
		}
		catch (RecognitionException re) {
			System.err.println("can't parse template action");
		}
		catch (TokenStreamException tse) {
			System.err.println("can't parse template action");
		}
		GrammarAST rewriteTree = (GrammarAST)parser.getAST();

		// then translate via codegen.g
		CodeGenTreeWalker gen = new CodeGenTreeWalker();
		gen.init(grammar);
		gen.currentRuleName = ruleName;
		gen.outerAltNum = actionTree.outerAltNum;
		String code = null;
		try {
			StringTemplate st = gen.rewrite_template((AST)rewriteTree);
			code = st.toString();
		}
		catch (RecognitionException re) {
			ErrorManager.error(ErrorManager.MSG_BAD_AST_STRUCTURE,
							   re);
		}

		nextCharIndexI.value = startIndex+sr.n; // what char to match next

		return code;
	}

	// M I S C

	public StringTemplateGroup getTemplates() {
		return templates;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		// for now turn on profile so we have a listener for the dbg events
		//this.profile = true;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public void setProfile(boolean profile) {
		this.profile = profile;
		if ( profile ) {
			setDebug(true); // requires debug events
		}
	}

	public void setMemoize(boolean memoize) {
		this.memoize = memoize;
	}

	/** During early-access release, this distinguishes between
	 *  forced profiling and -profile option
	public void setDumpProfile(boolean dumpProfile) {
		this.dumpProfile = dumpProfile;
	}
	 */

	public String getRecognizerFileName() {
		StringTemplate extST = templates.getInstanceOf("codeFileExtension");
		return grammar.name+extST.toString();
	}

	public String getVocabFileName() {
		return grammar.name+VOCAB_FILE_EXTENSION;
	}

	/** TODO: add the package to the name; language sensitive? */
	public String getClassName() {
		return grammar.name;
	}

	public void write(StringTemplate code, String fileName) throws IOException {
		Writer w = tool.getOutputFile(grammar, fileName);
		w.write(code.toString());
		w.close();
	}

	/** You can generate a switch rather than if-then-else for a DFA state
	 *  if there are no semantic predicates and the number of edge label
	 *  values is small enough; e.g., don't generate a switch for a state
	 *  containing an edge label such as 20..52330 (the resulting byte codes
	 *  would overflow the method 65k limit probably).
	 */
	protected boolean canGenerateSwitch(DFAState s) {
		if ( !GENERATE_SWITCHES_WHEN_POSSIBLE ) {
			return false;
		}
		int size = 0;
		for (int i = 0; i < s.getNumberOfTransitions(); i++) {
			Transition edge = (Transition) s.transition(i);
			if ( edge.label.isSemanticPredicate() ) {
				return false;
			}
			if ( ((DFAState)edge.target).getGatedPredicatesInNFAConfigurations()!=null ) {
				// can't do a switch if the edges are going to required gated predicates
				return false;
			}
			size += edge.label.getSet().size();
		}
		if ( s.getNumberOfTransitions()<MIN_SWITCH_ALTS || size>MAX_SWITCH_CASE_LABELS ) {
			return false;
		}
		return true;
	}

	/** Create a label to track a token / rule reference's result.
	 *  Technically, this is a place where I break model-view separation
	 *  as I am creating a variable name that could be invalid in a
	 *  target language, however, label ::= <ID><INT> is probably ok in
	 *  all languages we care about.
	 */
	public String createUniqueLabel(String name) {
		return new StringBuffer()
			.append(name).append(uniqueLabelNumber++).toString();
	}
}

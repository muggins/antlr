package org.antlr.tool;

import org.antlr.Tool;
import org.antlr.misc.Utils;
import org.antlr.codegen.CodeGenerator;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

/** Given a grammar file, show the dependencies on .tokens etc...
 *  Using ST, emit a simple "make compatible" list of dependencies.
 *  For example, combined grammar T.g (no token import) generates:
 *
 *		TParser.java : T.g
 * 		T.tokens : T.g
 * 		T__g : T.g
 *
 *  For tree grammar TP with import of T.tokens:
 *
 * 		TP.g : T.tokens
 * 		TP.java : TP.g
 *
 *  If "-lib libdir" is used on command-line with -depend, then include the
 *  path like
 *
 * 		TP.g : libdir/T.tokens
 *
 *  Pay attention to -o as well:
 *
 * 		outputdir/TParser.java : T.g
 *
 *  So this output shows what the grammar depends on *and* what it generates.
 *
 *  Operate on one grammar file at a time.  If given a list of .g on the
 *  command-line with -depend, just emit the dependencies.  The grammars
 *  may depend on each other, but the order doesn't matter.  Build tools,
 *  reading in this output, will know how to organize it.
 *
 *  This is a wee bit slow probably because the code generator has to load
 *  all of its template files in order to figure out the file extension
 *  for the generated recognizer.
 *
 *  This code was obvious until I removed redundant "./" on front of files
 *  and had to escape spaces in filenames :(
 */
public class BuildDependencyGenerator {
	protected String grammarFileName;
	protected Tool tool;
	protected Grammar grammar;
	protected CodeGenerator generator;
	protected StringTemplateGroup templates;

	public BuildDependencyGenerator(Tool tool, String grammarFileName)
		throws IOException, antlr.TokenStreamException, antlr.RecognitionException
	{
		this.tool = tool;
		this.grammarFileName = grammarFileName;
		grammar = tool.getRootGrammar(grammarFileName);
		String language = (String)grammar.getOption("language");
		generator = new CodeGenerator(tool, grammar, language);
		generator.loadTemplates(language);
	}

	/** From T.g return a list of File objects that
	 *  names files ANTLR will emit from T.g.
	 */
	public List getGeneratedFileList() {
		List files = new ArrayList();
		File outputDir = tool.getOutputDirectory(grammarFileName);
		if ( outputDir.getName().equals(".") ) {
			outputDir = null;
		}
		else if ( outputDir.getName().indexOf(' ')>=0 ) { // has spaces?
			String escSpaces = Utils.replace(outputDir.toString(),
											 " ",
											 "\\ ");
			outputDir = new File(escSpaces);
		}
		// add generated recognizer; e.g., TParser.java
		String recognizer =
			generator.getRecognizerFileName(grammar.name, grammar.type);
		files.add(new File(outputDir, recognizer));
		// add output vocab file; e.g., T.tokens
		files.add(new File(outputDir, generator.getVocabFileName()));
		// are we generating a .h file?
		StringTemplate headerExtST = null;
		if ( generator.getTemplates().isDefined("headerFile") ) {
			headerExtST     = generator.getTemplates().getInstanceOf("headerFileExtension");
            String suffix   = Grammar.grammarTypeToFileNameSuffix[grammar.type];
            String fileName = grammar.name + suffix + headerExtST.toString();
			files.add(new File(outputDir,fileName));
		}
		if ( grammar.type==Grammar.COMBINED ) {
			// add autogenerated lexer; e.g., TLexer.java TLexer.h TLexer.tokens
			// don't add T__.g (just a temp file)
            StringTemplate  extST   = generator.getTemplates().getInstanceOf("codeFileExtension");
            String          suffix  = Grammar.grammarTypeToFileNameSuffix[Grammar.LEXER];
			String          lexer   = grammar.name + suffix + extST.toString();
			files.add(new File(outputDir,lexer));
            
			// TLexer.h
			if ( headerExtST !=null ) {
				String header =	 grammar.name+suffix+headerExtST.toString();
				files.add(new File(outputDir,header));
			}
			// for combined, don't generate TLexer.tokens
		}

		if ( files.size()==0 ) {
			return null;
		}
		return files;
	}

	/** Return a list of File objects that name files ANTLR will read
	 *  to process T.g; for now, this can only be .tokens files and only
	 *  if they use the tokenVocab option.
	 */
	public List getDependenciesFileList() {
		List files = new ArrayList();
		String vocabName = (String)grammar.getOption("tokenVocab");
		if ( vocabName == null ) {
			return null;
		}
		File vocabFile = grammar.getImportedVocabFileName(vocabName);
		File outputDir = vocabFile.getParentFile();
		if ( outputDir.getName().equals(".") ) {
			files.add(vocabFile.getName());
		}
		else if ( outputDir.getName().indexOf(' ')>=0 ) { // has spaces?
			String escSpaces = Utils.replace(outputDir.toString(),
											 " ",
											 "\\ ");
			outputDir = new File(escSpaces);
			files.add(new File(outputDir, vocabFile.getName()));
		}
		else {
			files.add(vocabFile);
		}

		if ( files.size()==0 ) {
			return null;
		}
		return files;
	}

	public StringTemplate getDependencies() {
		loadDependencyTemplates();
		StringTemplate dependenciesST = templates.getInstanceOf("dependencies");
		dependenciesST.setAttribute("in", getDependenciesFileList());
		dependenciesST.setAttribute("out", getGeneratedFileList());
		dependenciesST.setAttribute("grammarFileName", grammar.fileName);
		return dependenciesST;
	}

	public void loadDependencyTemplates() {
		if ( templates!=null ) {
			return;
		}
		String fileName = "org/antlr/tool/templates/depend.stg";
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(fileName);
		if ( is==null ) {
			cl = ErrorManager.class.getClassLoader();
			is = cl.getResourceAsStream(fileName);
		}
		if ( is==null ) {
			ErrorManager.internalError("Can't load dependency templates: "+fileName);
			return;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			templates = new StringTemplateGroup(br,
												AngleBracketTemplateLexer.class);
			br.close();
		}
		catch (IOException ioe) {
			ErrorManager.internalError("error reading dependency templates file "+fileName, ioe);
		}
		finally {
			if ( br!=null ) {
				try {
					br.close();
				}
				catch (IOException ioe) {
					ErrorManager.internalError("cannot close dependency templates file "+fileName, ioe);
				}
			}
		}
	}
}

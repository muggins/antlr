header {
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
	package org.antlr.tool;
	import java.util.*;
}

/** Print out a grammar (no pretty printing).
 *
 *  Terence Parr
 *  University of San Francisco
 *  August 19, 2003
 */
class ANTLRTreePrinter extends TreeParser;

options {
	importVocab = ANTLR;
	ASTLabelType = "GrammarAST";
    codeGenBitsetTestThreshold=999;
}

{
	protected Grammar grammar;
	protected boolean showActions;
    protected StringBuffer buf = new StringBuffer(300);

    public void out(String s) {
        buf.append(s);
    }

    public void reportError(RecognitionException ex) {
		Token token = null;
		if ( ex instanceof MismatchedTokenException ) {
			token = ((MismatchedTokenException)ex).token;
		}
		else if ( ex instanceof NoViableAltException ) {
			token = ((NoViableAltException)ex).token;
		}
        ErrorManager.syntaxError(
            ErrorManager.MSG_SYNTAX_ERROR,
            token,
            "antlr.print: "+ex.toString(),
            ex);
    }

	/** Normalize a grammar print out by removing all double spaces
	 *  and trailing/beginning stuff.  FOr example, convert
	 *
	 *  ( A  |  B  |  C )*
	 *
	 *  to
	 *
	 *  ( A | B | C )*
	 */
	public static String normalize(String g) {
	    StringTokenizer st = new StringTokenizer(g, " ", false);
		StringBuffer buf = new StringBuffer();
		while ( st.hasMoreTokens() ) {
			String w = st.nextToken();
			buf.append(w);
			buf.append(" ");
		}
		return buf.toString().trim();
	}
}

/** Call this to figure out how to print */
toString[Grammar g, boolean showActions] returns [String s=null]
{
grammar = g;
this.showActions = showActions;
}
    :   (   grammar
        |   rule
        |   alternative
        |   element
        |	single_rewrite
        |   EOR {s="EOR";}
        )
        {return normalize(buf.toString());}
    ;

// --------------

grammar
    :   (headerSpec)*
	    ( #( LEXER_GRAMMAR grammarSpec["lexer " ] )
	    | #( PARSER_GRAMMAR grammarSpec["parser "] )
	    | #( TREE_GRAMMAR grammarSpec["tree "] )
	    | #( COMBINED_GRAMMAR grammarSpec[""] )
	    )
     ;

headerSpec
    :   #( "header" (ID)? a:ACTION {out("header {"+#a.getText()+"}\n");} )
    ;

attrScope
	:	#( "scope" ID ACTION )
	;

grammarSpec[String gtype]
	:	 id:ID {out(gtype+"grammar "+#id.getText());}
        (cmt:DOC_COMMENT {out(#cmt.getText()+"\n");} )?
        (optionsSpec)? {out(";\n");}
        (tokensSpec)?
        (attrScope)*
        (ACTION {if ( showActions ) out(#ACTION.getText());} )?
        rules
    ;

optionsSpec
    :   #( OPTIONS {out("options {");}
    	   (option {out("; ");})+
    	   {out("} : ");}
    	 )
    ;

option
    :   #( ASSIGN id:ID {out(#id.getText()+"=");} optionValue )
    ;

optionValue
	:	id:ID            {out(#id.getText());}
	|   s:STRING_LITERAL {out(#s.getText());}
	|	c:CHAR_LITERAL   {out(#c.getText());}
	|	i:INT            {out(#i.getText());}
//	|   charSet
	;

/*
charSet
	:   #( CHARSET charSetElement )
	;

charSetElement
	:   c:CHAR_LITERAL {out(#c.getText());}
	|   #( OR c1:CHAR_LITERAL c2:CHAR_LITERAL )
	|   #( RANGE c3:CHAR_LITERAL c4:CHAR_LITERAL )
	;
*/

tokensSpec
	:	#( TOKENS ( tokenSpec )+ )
	;

tokenSpec
	:	TOKEN_REF
	|	#( ASSIGN TOKEN_REF (STRING_LITERAL|CHAR_LITERAL) )
	;

rules
    :   ( rule )+
    ;

rule
    :   #( RULE id:ID
           (modifier)?
           {out(#id.getText());}
           #(ARG (arg:ARG_ACTION {out("["+#arg.getText()+"]");} )? )
           #(RET (ret:ARG_ACTION {out(" returns ["+#ret.getText()+"]");} )? )
           {out(" : ");}
           (optionsSpec)?
           (ruleScopeSpec)?
           #( INITACTION (ia:ACTION {if ( showActions ) out(#ia.getText());} )? )
           b:block[false] EOR {out(";\n");}
         )
    ;

modifier
{out(#modifier.getText()); out(" ");}
	:	"protected"
	|	"public"
	|	"private"
	|	"fragment"
	;

ruleScopeSpec
 	:	#( "scope" (ACTION)? ( ID )* )
 	;

block[boolean forceParens]
{
int numAlts = countAltsForBlock(#block);
}
    :   #(  BLOCK {if ( forceParens||numAlts>1 ) out(" (");}
            (optionsSpec)?
            alternative rewrite ( {out(" | ");} alternative rewrite )*
            EOB   {if ( forceParens||numAlts>1 ) out(")");}
         )
    ;

countAltsForBlock returns [int n=0]
    :   #( BLOCK (OPTIONS)? (ALT (REWRITE)* {n++;})+ EOB )
	;

alternative
    :   #( ALT (element)+ EOA )
    ;

single_rewrite
	:	#( REWRITE {out(" ->");} (SEMPRED {out(" {"+#SEMPRED.getText()+"}?");})?
	       ( alternative | rewrite_template | ACTION {out(" {"+#ACTION.getText()+"}");})
	     )
	;

rewrite_template
	:	#( TEMPLATE id:ID {out(" "+#id.getText());}
	       #( ARGLIST
	       	  ( #( ARG arg:ID {out(#arg.getText()+"=");}
	               a:ACTION   {out(#a.getText());}
	             )
	          )*
	        )
		   ( STRING_LITERAL {out(" "+#STRING_LITERAL.getText());}
		   | DOUBLE_ANGLE_STRING_LITERAL {out(" "+#DOUBLE_ANGLE_STRING_LITERAL.getText());}
		   )?
	     )
	;

rewrite
	:	(single_rewrite)*
	;

element
    :   atom
    |   #(NOT {out("~");} atom) 
    |   #(RANGE atom {out("..");} atom)
    |   #(CHAR_RANGE atom {out("..");} atom)
    |	#(ASSIGN id:ID {out(#id.getText()+"=");} atom) 
    |	#(PLUS_ASSIGN id2:ID {out(#id2.getText()+"+=");} atom)
    |   ebnf
    |   tree
    |   #( SYNPRED block[true] ) {out("=>");}
    |   a:ACTION  {if ( showActions ) {out("{"); out(a.getText()); out("}");}}
    |   pred:SEMPRED
    	{
    	if ( showActions ) {out("{"); out(pred.getText()); out("}?");}
    	else {out("{...}?");}
    	}
    |   EPSILON {out(" /* epsilon */ ");}
    ;

ebnf:   block[true] {out(" ");}
    |   #( OPTIONAL block[true] ) {out("? ");}
    |   #( CLOSURE block[true] )  {out("* ");}
    |   #( POSITIVE_CLOSURE block[true] ) {out("+ ");}
    ;

tree:   #(TREE_BEGIN {out(" ^(");} element (element)* {out(") ");} )
    ;

atom
{out(" ");}
    :   (	#( RULE_REF		{out(#atom.toString());}
			   (rarg:ARG_ACTION	{out("["+#rarg.toString()+"]");})?
			   (ast_suffix)?
             )
		|   #( TOKEN_REF		{out(#atom.toString());} 
			   (targ:ARG_ACTION	{out("["+#targ.toString()+"]");} )?
			   (ast_suffix)?
             )
		|   #( CHAR_LITERAL	{out(#atom.toString());}
			   (ast_suffix)?
             )
		|   #( STRING_LITERAL	{out(#atom.toString());}
			   (ast_suffix)?
             )
		|   #( WILDCARD		{out(#atom.toString());}
			   (ast_suffix)?
             )
		)
		{out(" ");}
    |	LABEL {out(" $"+#LABEL.getText());} // used in -> rewrites
    |   set
    ;

ast_suffix
	:	ROOT {out("^");}
	|	RULEROOT {out("^^");}
	|	BANG  {out("!");}
	;

set
    :   #( SET
    	   {out("(");} setElement ({out("|");} setElement)* {out(")");}
           (ast_suffix)?
    	 )
    ;

setElement
    :   (	CHAR_LITERAL    {out(#setElement.toString());}
		|   TOKEN_REF		{out(#setElement.toString());}
		|   STRING_LITERAL	{out(#setElement.toString());}
		)
    |	#(CHAR_RANGE c1:CHAR_LITERAL c2:CHAR_LITERAL)
    	{out(#c1.getText()+
    	     ".."+
    	     #c2.getText());
    	}
    ;

/** \file
 *  This OBJC source file was generated by $ANTLR version 3.3.1-SNAPSHOT Jan 30, 2011 08:28:24
 *
 *     -  From the grammar source file : PolyPrinter.g
 *     -                            On : 2011-01-30 08:45:32
 *     -           for the tree parser : PolyPrinterTreeParser *
 * Editing it, at least manually, is not wise. 
 *
 * ObjC language generator and runtime by Alan Condit, acondit|hereisanat|ipns|dotgoeshere|com.
 *
 *
*/
// [The "BSD licence"]
// Copyright (c) 2010 Alan Condit
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. The name of the author may not be used to endorse or promote products
//    derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
// OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
// IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
// THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

// $ANTLR 3.3.1-SNAPSHOT Jan 30, 2011 08:28:24 PolyPrinter.g 2011-01-30 08:45:32

/* -----------------------------------------
 * Include the ANTLR3 generated header file.
 */
#import <ST4/ST.h>
#import <ST4/STGroup.h>
#import "PolyPrinter.h"
/* ----------------------------------------- */


/* ============================================================================= */

/* =============================================================================
 * Start of recognizer
 */



#pragma mark Bitsets
static ANTLRBitSet *FOLLOW_8_in_poly43;
static const unsigned long long FOLLOW_8_in_poly43_data[] = { 0x0000000000000004LL};
static ANTLRBitSet *FOLLOW_poly_in_poly48;
static const unsigned long long FOLLOW_poly_in_poly48_data[] = { 0x0000000000000370LL};
static ANTLRBitSet *FOLLOW_poly_in_poly52;
static const unsigned long long FOLLOW_poly_in_poly52_data[] = { 0x0000000000000008LL};
static ANTLRBitSet *FOLLOW_MULT_in_poly74;
static const unsigned long long FOLLOW_MULT_in_poly74_data[] = { 0x0000000000000004LL};
static ANTLRBitSet *FOLLOW_poly_in_poly78;
static const unsigned long long FOLLOW_poly_in_poly78_data[] = { 0x0000000000000370LL};
static ANTLRBitSet *FOLLOW_poly_in_poly82;
static const unsigned long long FOLLOW_poly_in_poly82_data[] = { 0x0000000000000008LL};
static ANTLRBitSet *FOLLOW_9_in_poly104;
static const unsigned long long FOLLOW_9_in_poly104_data[] = { 0x0000000000000004LL};
static ANTLRBitSet *FOLLOW_poly_in_poly109;
static const unsigned long long FOLLOW_poly_in_poly109_data[] = { 0x0000000000000370LL};
static ANTLRBitSet *FOLLOW_poly_in_poly113;
static const unsigned long long FOLLOW_poly_in_poly113_data[] = { 0x0000000000000008LL};
static ANTLRBitSet *FOLLOW_INT_in_poly134;
static const unsigned long long FOLLOW_INT_in_poly134_data[] = { 0x0000000000000002LL};
static ANTLRBitSet *FOLLOW_ID_in_poly148;
static const unsigned long long FOLLOW_ID_in_poly148_data[] = { 0x0000000000000002LL};


#pragma mark Dynamic Global Scopes

#pragma mark Dynamic Rule Scopes

#pragma mark Rule return scopes start
@implementation PolyPrinter_poly_return /* returnScope */
 /* start of synthesize -- OBJC-Line 1837 */
+ (PolyPrinter_poly_return *)newPolyPrinter_poly_return
{
    return [[[PolyPrinter_poly_return alloc] init] retain];
}

- (id) getTemplate { return st; }

//public StringTemplate st;
//public Object getTemplate() { return st; }
//public String toString() { return st==null?null:st.toString(); }

@end /* end of returnScope implementation */



@implementation PolyPrinter  // line 637

+ (void) initialize
{
    #pragma mark Bitsets
    FOLLOW_8_in_poly43 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_8_in_poly43_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly48 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly48_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly52 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly52_data Count:(NSUInteger)1] retain];
    FOLLOW_MULT_in_poly74 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_MULT_in_poly74_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly78 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly78_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly82 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly82_data Count:(NSUInteger)1] retain];
    FOLLOW_9_in_poly104 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_9_in_poly104_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly109 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly109_data Count:(NSUInteger)1] retain];
    FOLLOW_poly_in_poly113 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_poly_in_poly113_data Count:(NSUInteger)1] retain];
    FOLLOW_INT_in_poly134 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_INT_in_poly134_data Count:(NSUInteger)1] retain];
    FOLLOW_ID_in_poly148 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_ID_in_poly148_data Count:(NSUInteger)1] retain];

    [BaseRecognizer setTokenNames:[[NSArray arrayWithObjects:@"<invalid>", @"<EOR>", @"<DOWN>", @"<UP>", 
 @"MULT", @"INT", @"ID", @"WS", @"'+'", @"'^'", nil] retain]];
    [BaseRecognizer setGrammarFileName:@"PolyPrinter.g"];
}

+ (PolyPrinter *)newPolyPrinter:(id<TreeNodeStream>)aStream
{

    return [[PolyPrinter alloc] initWithStream:aStream];

}


- (id) initWithStream:(id<TreeNodeStream>)aStream
{
    if ((self = [super initWithStream:aStream State:[[RecognizerSharedState newRecognizerSharedStateWithRuleLen:1+1] retain]]) != nil) {


        /* start of actions-actionScope-init */
        /* start of init */
    }
    return self;
}

- (void) dealloc
{
    [super dealloc];
}

/* members */
 

/* start actions.actionScope.methods */
/* start methods() */
/*protected StringTemplateGroup templateLib = new StringTemplateGroup("PolyPrinterTemplates", AngleBracketTemplateLexer.class); */
STGroup *templateLib = [STGroup newSTGroup];

//public void setTemplateLib(StringTemplateGroup templateLib) {
//  this.templateLib = templateLib;
//}
//public StringTemplateGroup getTemplateLib() {
//  return templateLib;
//}
- (void) setTemplateLib:(STGroup *)aTemplateLib { templateLib = aTemplateLib; } 
- (STGroup *)getTemplateLib { return templateLib; } 
/** allows convenient multi-value initialization:
 *  "new STAttrMap().put(...).put(...)"
 */
/*
public static class STAttrMap extends HashMap {
  public STAttrMap put(String attrName, Object value) {
    super.put(attrName, value);
    return this;
  }
  public STAttrMap put(String attrName, int value) {
    super.put(attrName, new Integer(value));
    return this;
  }
}
 */
// start rules
/*
 * $ANTLR start poly
 * PolyPrinter.g:9:1: poly : ( ^( '+' a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a>+<b>\" | ^( MULT a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a><b>\" | ^( '^' a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a>^<b>\" | INT -> {%{$INT.text}} | ID -> {%{$ID.text}});
 */
- (PolyPrinter_poly_return *) poly
{
    /* ruleScopeSetUp */

    PolyPrinter_poly_return * retval = [PolyPrinter_poly_return newPolyPrinter_poly_return];
    [retval setStart:[input LT:1]];

    @try {
        CommonTree *INT1 = nil;
        CommonTree *ID2 = nil;
        PolyPrinter_poly_return * a = nil;

        PolyPrinter_poly_return * b = nil;


        // PolyPrinter.g:9:5: ( ^( '+' a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a>+<b>\" | ^( MULT a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a><b>\" | ^( '^' a= poly b= poly ) -> template(a=$a.stb=$b.st) \"<a>^<b>\" | INT -> {%{$INT.text}} | ID -> {%{$ID.text}}) //ruleblock
        NSInteger alt1=5;
        switch ([input LA:1]) {
            case 8: ;
                {
                alt1=1;
                }
                break;
            case MULT: ;
                {
                alt1=2;
                }
                break;
            case 9: ;
                {
                alt1=3;
                }
                break;
            case INT: ;
                {
                alt1=4;
                }
                break;
            case ID: ;
                {
                alt1=5;
                }
                break;

        default: ;
            NoViableAltException *nvae = [NoViableAltException newException:1 state:0 stream:input];
            @throw nvae;
        }

        switch (alt1) {
            case 1 : ;
                // PolyPrinter.g:9:7: ^( '+' a= poly b= poly ) // alt
                {
                [self match:input TokenType:8 Follow:FOLLOW_8_in_poly43]; 

                    [self match:input TokenType:DOWN Follow:nil]; 
                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly48];
                    a = [self poly];

                    [self popFollow];


                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly52];
                    b = [self poly];

                    [self popFollow];



                    [self match:input TokenType:UP Follow:nil]; 


                // TEMPLATE REWRITE
                // 9:29: -> template(a=$a.stb=$b.st) \"<a>+<b>\"
                {
                    retval.st = new StringTemplate(templateLib, "<a>+<b>",
                  new STAttrMap().put("a", (a!=nil?[a st]:nil)).put("b", (b!=nil?[b st]:nil)));
                }


                }
                break;
            case 2 : ;
                // PolyPrinter.g:10:4: ^( MULT a= poly b= poly ) // alt
                {
                [self match:input TokenType:MULT Follow:FOLLOW_MULT_in_poly74]; 

                    [self match:input TokenType:DOWN Follow:nil]; 
                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly78];
                    a = [self poly];

                    [self popFollow];


                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly82];
                    b = [self poly];

                    [self popFollow];



                    [self match:input TokenType:UP Follow:nil]; 


                // TEMPLATE REWRITE
                // 10:26: -> template(a=$a.stb=$b.st) \"<a><b>\"
                {
                    retval.st = new StringTemplate(templateLib, "<a><b>",
                  new STAttrMap().put("a", (a!=nil?[a st]:nil)).put("b", (b!=nil?[b st]:nil)));
                }


                }
                break;
            case 3 : ;
                // PolyPrinter.g:11:4: ^( '^' a= poly b= poly ) // alt
                {
                [self match:input TokenType:9 Follow:FOLLOW_9_in_poly104]; 

                    [self match:input TokenType:DOWN Follow:nil]; 
                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly109];
                    a = [self poly];

                    [self popFollow];


                    /* ruleRef */
                    [self pushFollow:FOLLOW_poly_in_poly113];
                    b = [self poly];

                    [self popFollow];



                    [self match:input TokenType:UP Follow:nil]; 


                // TEMPLATE REWRITE
                // 11:26: -> template(a=$a.stb=$b.st) \"<a>^<b>\"
                {
                    retval.st = [ST newST:@"<a>^<b>"]
                    [retval.st add:@"a" value:@"b"];
                }


                }
                break;
            case 4 : ;
                // PolyPrinter.g:12:4: INT // alt
                {
                INT1=(CommonTree *)[self match:input TokenType:INT Follow:FOLLOW_INT_in_poly134]; 


                // TEMPLATE REWRITE
                // 12:13: -> {%{$INT.text}}
                {
                    retval.st = [ST newST:(INT1!=nil?INT1.text:nil)];
                }


                }
                break;
            case 5 : ;
                // PolyPrinter.g:13:4: ID // alt
                {
                ID2=(CommonTree *)[self match:input TokenType:ID Follow:FOLLOW_ID_in_poly148]; 


                // TEMPLATE REWRITE
                // 13:12: -> {%{$ID.text}}
                {
                    retval.st = [ST newST:ID2!=nil?[ID2.text]:nil];
                }


                }
                break;

        }
        // token+rule list labels

    }
    @catch (RecognitionException *re) {
        [self reportError:re];
        [self recover:input Exception:re];
    }
    @finally {
    }
    return retval;
}
/* $ANTLR end poly */

@end /* end of PolyPrinter implementation line 692 */


/* End of code
 * =============================================================================
 */

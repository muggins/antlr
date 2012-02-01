/** \file
 *  This OBJC source file was generated by $ANTLR version 3.2 Aug 20, 2010 15:00:19
 *
 *     -  From the grammar source file : /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g
 *     -                            On : 2010-08-20 15:03:14
 *     -                for the parser : TreeRewriteParserParser *
 * Editing it, at least manually, is not wise. 
 *
 * C language generator and runtime by Jim Idle, jimi|hereisanat|idle|dotgoeshere|ws.
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

// $ANTLR 3.2 Aug 20, 2010 15:00:19 /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g 2010-08-20 15:03:14

/* -----------------------------------------
 * Include the ANTLR3 generated header file.
 */
#import "TreeRewriteParser.h"
/* ----------------------------------------- */


/* ============================================================================= */

/* =============================================================================
 * Start of recognizer
 */



#pragma mark Bitsets
static ANTLRBitSet *FOLLOW_INT_in_rule26;

const unsigned long long FOLLOW_INT_in_rule26_data[] = { 0x0000000000000010LL};

static ANTLRBitSet *FOLLOW_subrule_in_rule28;

const unsigned long long FOLLOW_subrule_in_rule28_data[] = { 0x0000000000000002LL};

static ANTLRBitSet *FOLLOW_INT_in_subrule53;

const unsigned long long FOLLOW_INT_in_subrule53_data[] = { 0x0000000000000002LL};



#pragma mark Dynamic Global Scopes

#pragma mark Dynamic Rule Scopes

#pragma mark Rule return scopes start
@implementation TreeRewriteParser_rule_return
@synthesize tree;
+ (TreeRewriteParser_rule_return *)newTreeRewriteParser_rule_return
{
    return [[[TreeRewriteParser_rule_return alloc] init] retain];
}

// returnScope.methods
- (CommonTree *)getTree
{
    return tree;
}

- (void) setTree:(CommonTree *)aTree
{
    if ( tree != aTree ) {
        if ( tree ) [tree release];
        if ( aTree ) [aTree retain];
        tree = aTree;
    }
}

- (void) dealloc
{
#ifdef DEBUG_DEALLOC
    NSLog( @"called dealloc in TreeRewriteParser_rule_return" );
#endif
    if ( tree ) [tree release];
    [super dealloc];
}

@end 

@implementation TreeRewriteParser_subrule_return
@synthesize tree;
+ (TreeRewriteParser_subrule_return *)newTreeRewriteParser_subrule_return
{
    return [[[TreeRewriteParser_subrule_return alloc] init] retain];
}

// returnScope.methods
- (CommonTree *)getTree
{
    return tree;
}

- (void) setTree:(CommonTree *)aTree
{
    if (tree != aTree) {
        if (tree != nil) [tree release];
        if (aTree != nil) [aTree retain];
        tree = aTree;
    }
}

- (void) dealloc
{
#ifdef DEBUG_DEALLOC
    NSLog( @"called dealloc in TreeRewriteParser_subrule_return" );
#endif
    if ( tree ) [tree release];
    [super dealloc];
}

@end 



@implementation TreeRewriteParser  // line 610

+ (void) initialize
{
    FOLLOW_INT_in_rule26 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_INT_in_rule26_data Count:(NSUInteger)1] retain];
    FOLLOW_subrule_in_rule28 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_subrule_in_rule28_data Count:(NSUInteger)1] retain];
    FOLLOW_INT_in_subrule53 = [[ANTLRBitSet newBitSetWithBits:(const unsigned long long *)FOLLOW_INT_in_subrule53_data Count:(NSUInteger)1] retain];

    [BaseRecognizer setTokenNames:[[[NSArray alloc] initWithObjects:@"<invalid>", @"<EOR>", @"<DOWN>", @"<UP>", 
 @"INT", @"WS", nil] retain]];
}

+ (TreeRewriteParser *)newTreeRewriteParser:(id<TokenStream>)aStream
{
    return [[TreeRewriteParser alloc] initWithTokenStream:aStream];

}

- (id) initWithTokenStream:(id<TokenStream>)aStream
{
    if ((self = [super initWithTokenStream:aStream State:[[RecognizerSharedState newRecognizerSharedStateWithRuleLen:2+1] retain]]) != nil) {


                        
        // start of actions-actionScope-init
        // start of init
        // genericParser.init
        [self setTreeAdaptor:[[CommonTreeAdaptor newCommonTreeAdaptor] retain]];
    }
    return self;
}

- (void) dealloc
{
#ifdef DEBUG_DEALLOC
    NSLog( @"called dealloc in TreeRewriteParser" );
#endif
    if ( treeAdaptor ) [treeAdaptor release];
    [super dealloc];
}

// start actions.actionScope.methods
// start methods()
// genericParser.methods
// parserMethods
- (id<TreeAdaptor>) getTreeAdaptor
{
	return treeAdaptor;
}

- (void) setTreeAdaptor:(id<TreeAdaptor>)aTreeAdaptor
{
	if (aTreeAdaptor != treeAdaptor) {
		treeAdaptor = aTreeAdaptor;
	}
}
// start rules
/*
 * $ANTLR start rule
 * /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:8:1: rule : INT subrule -> ^( subrule INT ) ;
 */
- (TreeRewriteParser_rule_return *) rule
{
    // ruleScopeSetUp

    // ruleDeclarations
    TreeRewriteParser_rule_return * retval = [TreeRewriteParser_rule_return newTreeRewriteParser_rule_return];
    [retval setStart:[input LT:1]];

    CommonTree *root_0 = nil;

    @try {
        // ruleLabelDefs
        id<Token> INT1 = nil;
        TreeRewriteParser_subrule_return * subrule2 = nil;


        CommonTree *INT1_tree=nil;
        RewriteRuleTokenStream *stream_INT = 
            [[RewriteRuleTokenStream newRewriteRuleTokenStream:treeAdaptor
                                                             description:@"token INT"] retain];
        RewriteRuleSubtreeStream *stream_subrule = 
            [[RewriteRuleSubtreeStream newRewriteRuleSubtreeStream:treeAdaptor
                                                                description:@"rule subrule"] retain];
        // /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:8:5: ( INT subrule -> ^( subrule INT ) ) // ruleBlockSingleAlt
        // /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:8:7: INT subrule // alt
        {
        INT1=(id<Token>)[self match:input TokenType:INT Follow:FOLLOW_INT_in_rule26];  
            [stream_INT addElement:INT1];
          /* element() */
        [self pushFollow:FOLLOW_subrule_in_rule28];
        subrule2 = [self subrule];
        [self popFollow];


        [stream_subrule addElement:[subrule2 getTree]];  /* element() */
         /* elements */

        // AST REWRITE
        // elements: INT, subrule
        // token labels: 
        // rule labels: retval
        // token list labels: 
        // rule list labels: 
        // wildcard labels: 
         [retval setTree:root_0];

        retval.tree = root_0;

        RewriteRuleSubtreeStream *stream_retval =
            [[RewriteRuleSubtreeStream newRewriteRuleSubtreeStream:treeAdaptor
                                                                description:@"token retval"
                                                                    element:retval!=nil?[retval getTree]:nil] retain];

        root_0 = (CommonTree *)[[[treeAdaptor class] newEmptyTree] retain];

        // 8:19: -> ^( subrule INT )
        {
            // /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:8:22: ^( subrule INT )
            {
                CommonTree *root_1 = (CommonTree *)[[[treeAdaptor class] newEmptyTree] retain];
                root_1 = (CommonTree *)[treeAdaptor becomeRoot:(id<Tree>)[stream_subrule nextNode]
                                                                         old:root_1];

                 // TODO: args: 
                [treeAdaptor addChild:[stream_INT nextNode] toTree:root_1];

                [treeAdaptor addChild:root_1 toTree:root_0];
            }

        }

        retval.tree = root_0;

        }

        // token+rule list labels
        [retval setStop:[input LT:-1]];

        [stream_INT release];
        [stream_subrule release];

        retval.tree = (CommonTree *)[treeAdaptor rulePostProcessing:root_0];
        [treeAdaptor setTokenBoundaries:retval.tree From:retval.startToken To:retval.stopToken];

    }
    @catch (RecognitionException *re) {
        [self reportError:re];
        [self recover:input Exception:re];
        retval.tree = (CommonTree *)[treeAdaptor errorNode:input From:retval.startToken To:[input LT:-1] Exception:re];

    }    @finally {
    }
    return retval;
}
/* $ANTLR end rule */
/*
 * $ANTLR start subrule
 * /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:11:1: subrule : INT ;
 */
- (TreeRewriteParser_subrule_return *) subrule
{
    // ruleScopeSetUp

    // ruleDeclarations
    TreeRewriteParser_subrule_return * retval = [TreeRewriteParser_subrule_return newTreeRewriteParser_subrule_return];
    [retval setStart:[input LT:1]];

    CommonTree *root_0 = nil;

    @try {
        // ruleLabelDefs
        id<Token> INT3 = nil;

        CommonTree *INT3_tree=nil;

        // /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:12:5: ( INT ) // ruleBlockSingleAlt
        // /usr/local/ANTLR3-ObjC2.0-Runtime/Framework/examples/treerewrite/TreeRewrite.g:12:9: INT // alt
        {
        root_0 = (CommonTree *)[[[treeAdaptor class] newEmptyTree] retain];

        INT3=(id<Token>)[self match:input TokenType:INT Follow:FOLLOW_INT_in_subrule53]; 
        INT3_tree = (CommonTree *)[[treeAdaptor createTree:INT3] retain];
        [treeAdaptor addChild:INT3_tree  toTree:root_0];
          /* element() */
         /* elements */
        }

        // token+rule list labels
        [retval setStop:[input LT:-1]];


        retval.tree = (CommonTree *)[treeAdaptor rulePostProcessing:root_0];
        [treeAdaptor setTokenBoundaries:retval.tree From:retval.startToken To:retval.stopToken];

    }
    @catch (RecognitionException *re) {
        [self reportError:re];
        [self recover:input Exception:re];
        retval.tree = (CommonTree *)[treeAdaptor errorNode:input From:retval.startToken To:[input LT:-1] Exception:re];

    }    @finally {
    }
    return retval;
}
/* $ANTLR end subrule */

@end /* end of TreeRewriteParser implementation line 669 */


/* End of code
 * =============================================================================
 */

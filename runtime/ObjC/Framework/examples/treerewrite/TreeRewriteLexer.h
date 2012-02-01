// $ANTLR ${project.version} ${buildNumber} TreeRewrite.g 2011-06-20 13:57:09

/* =============================================================================
 * Standard antlr3 OBJC runtime definitions
 */
#import <Foundation/Foundation.h>
#import <ANTLR/ANTLR.h>
/* End of standard antlr3 runtime definitions
 * =============================================================================
 */

/* Start cyclicDFAInterface */

#pragma mark Rule return scopes Interface start
#pragma mark Rule return scopes Interface end
#pragma mark Tokens
#ifdef EOF
#undef EOF
#endif
#define EOF -1
#define INT 4
#define WS 5
/* interface lexer class */
@interface TreeRewriteLexer : Lexer { // line 283
/* ObjC start of actions.lexer.memVars */
/* ObjC end of actions.lexer.memVars */
}
+ (void) initialize;
+ (TreeRewriteLexer *)newTreeRewriteLexerWithCharStream:(id<CharStream>)anInput;
/* ObjC start actions.lexer.methodsDecl */
/* ObjC end actions.lexer.methodsDecl */
- (void) mINT ; 
- (void) mWS ; 
- (void) mTokens ; 

@end /* end of TreeRewriteLexer interface */


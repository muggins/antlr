// $ANTLR 3.0b5 /Users/kroepke/Projects/antlr3/code/antlr/main/lib/ObjC/Framework/examples/simplecTreeParser/SimpleCTP.g 2006-11-12 20:38:47

#import <Cocoa/Cocoa.h>
#import <ANTLR/ANTLR.h>



#pragma mark Cyclic DFA

#pragma mark Tokens
#define SimpleCTPTreeParser_FUNC_DEF	8
#define SimpleCTPTreeParser_WS	20
#define SimpleCTPTreeParser_CHAR	15
#define SimpleCTPTreeParser_EQ	11
#define SimpleCTPTreeParser_FUNC_HDR	6
#define SimpleCTPTreeParser_LT	18
#define SimpleCTPTreeParser_ARG_DEF	5
#define SimpleCTPTreeParser_EQEQ	17
#define SimpleCTPTreeParser_BLOCK	9
#define SimpleCTPTreeParser_INT	12
#define SimpleCTPTreeParser_EOF	-1
#define SimpleCTPTreeParser_VOID	16
#define SimpleCTPTreeParser_FOR	13
#define SimpleCTPTreeParser_PLUS	19
#define SimpleCTPTreeParser_FUNC_DECL	7
#define SimpleCTPTreeParser_INT_TYPE	14
#define SimpleCTPTreeParser_ID	10
#define SimpleCTPTreeParser_VAR_DEF	4

#pragma mark Dynamic Global Scopes

#pragma mark Dynamic Rule Scopes

#pragma mark Rule Return Scopes


@interface SimpleCTPTreeParser : ANTLRTreeParser {

													

 }


- (void) program;
- (void) declaration;
- (void) variable;
- (void) declarator;
- (void) functionHeader;
- (void) formalParameter;
- (void) type;
- (void) block;
- (void) stat;
- (void) forStat;
- (void) expr;
- (void) atom;



@end
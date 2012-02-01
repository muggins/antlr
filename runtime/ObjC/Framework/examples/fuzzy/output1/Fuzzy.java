// $ANTLR 3.4 /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g 2012-01-31 20:38:37

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class Fuzzy extends Lexer {
    public static final int EOF=-1;
    public static final int ARG=4;
    public static final int CALL=5;
    public static final int CHAR=6;
    public static final int CLASS=7;
    public static final int COMMENT=8;
    public static final int ESC=9;
    public static final int FIELD=10;
    public static final int ID=11;
    public static final int IMPORT=12;
    public static final int METHOD=13;
    public static final int QID=14;
    public static final int QIDStar=15;
    public static final int RETURN=16;
    public static final int SL_COMMENT=17;
    public static final int STAT=18;
    public static final int STRING=19;
    public static final int TYPE=20;
    public static final int WS=21;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public Fuzzy() {} 
    public Fuzzy(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public Fuzzy(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g"; }

    public Token nextToken() {
        while (true) {
            if ( input.LA(1)==CharStream.EOF ) {
                Token eof = new CommonToken((CharStream)input,Token.EOF,
                                            Token.DEFAULT_CHANNEL,
                                            input.index(),input.index());
                eof.setLine(getLine());
                eof.setCharPositionInLine(getCharPositionInLine());
                return eof;
            }
            state.token = null;
    	state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
    	state.text = null;
            try {
                int m = input.mark();
                state.backtracking=1; 
                state.failed=false;
                mTokens();
                state.backtracking=0;
                if ( state.failed ) {
                    input.rewind(m);
                    input.consume(); 
                }
                else {
                    emit();
                    return state.token;
                }
            }
            catch (RecognitionException re) {
                // shouldn't happen in backtracking mode, but...
                reportError(re);
                recover(re);
            }
        }
    }

    public void memoize(IntStream input,
    		int ruleIndex,
    		int ruleStartIndex)
    {
    if ( state.backtracking>1 ) super.memoize(input, ruleIndex, ruleStartIndex);
    }

    public boolean alreadyParsedRule(IntStream input, int ruleIndex) {
    if ( state.backtracking>1 ) return super.alreadyParsedRule(input, ruleIndex);
    return false;
    }
    // $ANTLR start "IMPORT"
    public final void mIMPORT() throws RecognitionException {
        try {
            int _type = IMPORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:5:2: ( 'import' WS name= QIDStar ( WS )? ';' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:5:4: 'import' WS name= QIDStar ( WS )? ';'
            {
            match("import"); if (state.failed) return ;



            mWS(); if (state.failed) return ;


            int nameStart31 = getCharIndex();
            int nameStartLine31 = getLine();
            int nameStartCharPos31 = getCharPositionInLine();
            mQIDStar(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart31, getCharIndex()-1);
            name.setLine(nameStartLine31);
            name.setCharPositionInLine(nameStartCharPos31);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:5:29: ( WS )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0 >= '\t' && LA1_0 <= '\n')||LA1_0==' ') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:5:29: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            match(';'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IMPORT"

    // $ANTLR start "RETURN"
    public final void mRETURN() throws RecognitionException {
        try {
            int _type = RETURN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:10:2: ( 'return' ( options {greedy=false; } : . )* ';' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:10:4: 'return' ( options {greedy=false; } : . )* ';'
            {
            match("return"); if (state.failed) return ;



            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:10:13: ( options {greedy=false; } : . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==';') ) {
                    alt2=2;
                }
                else if ( ((LA2_0 >= '\u0000' && LA2_0 <= ':')||(LA2_0 >= '<' && LA2_0 <= '\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:10:38: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            match(';'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RETURN"

    // $ANTLR start "CLASS"
    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:2: ( 'class' WS name= ID ( WS )? ( 'extends' WS QID ( WS )? )? ( 'implements' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )? '{' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:4: 'class' WS name= ID ( WS )? ( 'extends' WS QID ( WS )? )? ( 'implements' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )? '{'
            {
            match("class"); if (state.failed) return ;



            mWS(); if (state.failed) return ;


            int nameStart81 = getCharIndex();
            int nameStartLine81 = getLine();
            int nameStartCharPos81 = getCharPositionInLine();
            mID(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart81, getCharIndex()-1);
            name.setLine(nameStartLine81);
            name.setCharPositionInLine(nameStartCharPos81);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:23: ( WS )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||LA3_0==' ') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:23: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:27: ( 'extends' WS QID ( WS )? )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='e') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:28: 'extends' WS QID ( WS )?
                    {
                    match("extends"); if (state.failed) return ;



                    mWS(); if (state.failed) return ;


                    mQID(); if (state.failed) return ;


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:45: ( WS )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0==' ') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:14:45: WS
                            {
                            mWS(); if (state.failed) return ;


                            }
                            break;

                    }


                    }
                    break;

            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:3: ( 'implements' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='i') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:4: 'implements' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )*
                    {
                    match("implements"); if (state.failed) return ;



                    mWS(); if (state.failed) return ;


                    mQID(); if (state.failed) return ;


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:24: ( WS )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||LA6_0==' ') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:24: WS
                            {
                            mWS(); if (state.failed) return ;


                            }
                            break;

                    }


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:28: ( ',' ( WS )? QID ( WS )? )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==',') ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:29: ',' ( WS )? QID ( WS )?
                    	    {
                    	    match(','); if (state.failed) return ;

                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:33: ( WS )?
                    	    int alt7=2;
                    	    int LA7_0 = input.LA(1);

                    	    if ( ((LA7_0 >= '\t' && LA7_0 <= '\n')||LA7_0==' ') ) {
                    	        alt7=1;
                    	    }
                    	    switch (alt7) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:33: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    mQID(); if (state.failed) return ;


                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:41: ( WS )?
                    	    int alt8=2;
                    	    int LA8_0 = input.LA(1);

                    	    if ( ((LA8_0 >= '\t' && LA8_0 <= '\n')||LA8_0==' ') ) {
                    	        alt8=1;
                    	    }
                    	    switch (alt8) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:15:41: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    }
                    break;

            }


            match('{'); if (state.failed) return ;

            if ( state.backtracking==1 ) {NSLog(@"found class %@", (name!=null?name.getText():null));}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CLASS"

    // $ANTLR start "METHOD"
    public final void mMETHOD() throws RecognitionException {
        try {
            int _type = METHOD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:5: ( TYPE WS name= ID ( WS )? '(' ( ARG ( WS )? ( ',' ( WS )? ARG ( WS )? )* )? ')' ( WS )? ( 'throws' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )? '{' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:9: TYPE WS name= ID ( WS )? '(' ( ARG ( WS )? ( ',' ( WS )? ARG ( WS )? )* )? ')' ( WS )? ( 'throws' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )? '{'
            {
            mTYPE(); if (state.failed) return ;


            mWS(); if (state.failed) return ;


            int nameStart158 = getCharIndex();
            int nameStartLine158 = getLine();
            int nameStartCharPos158 = getCharPositionInLine();
            mID(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart158, getCharIndex()-1);
            name.setLine(nameStartLine158);
            name.setCharPositionInLine(nameStartCharPos158);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:25: ( WS )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||LA11_0==' ') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:25: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            match('('); if (state.failed) return ;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:33: ( ARG ( WS )? ( ',' ( WS )? ARG ( WS )? )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0 >= 'A' && LA16_0 <= 'Z')||LA16_0=='_'||(LA16_0 >= 'a' && LA16_0 <= 'z')) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:35: ARG ( WS )? ( ',' ( WS )? ARG ( WS )? )*
                    {
                    mARG(); if (state.failed) return ;


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:39: ( WS )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( ((LA12_0 >= '\t' && LA12_0 <= '\n')||LA12_0==' ') ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:39: WS
                            {
                            mWS(); if (state.failed) return ;


                            }
                            break;

                    }


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:43: ( ',' ( WS )? ARG ( WS )? )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==',') ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:44: ',' ( WS )? ARG ( WS )?
                    	    {
                    	    match(','); if (state.failed) return ;

                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:48: ( WS )?
                    	    int alt13=2;
                    	    int LA13_0 = input.LA(1);

                    	    if ( ((LA13_0 >= '\t' && LA13_0 <= '\n')||LA13_0==' ') ) {
                    	        alt13=1;
                    	    }
                    	    switch (alt13) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:48: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    mARG(); if (state.failed) return ;


                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:56: ( WS )?
                    	    int alt14=2;
                    	    int LA14_0 = input.LA(1);

                    	    if ( ((LA14_0 >= '\t' && LA14_0 <= '\n')||LA14_0==' ') ) {
                    	        alt14=1;
                    	    }
                    	    switch (alt14) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:56: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }


            match(')'); if (state.failed) return ;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:69: ( WS )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0 >= '\t' && LA17_0 <= '\n')||LA17_0==' ') ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:20:69: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:8: ( 'throws' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )* )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0=='t') ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:9: 'throws' WS QID ( WS )? ( ',' ( WS )? QID ( WS )? )*
                    {
                    match("throws"); if (state.failed) return ;



                    mWS(); if (state.failed) return ;


                    mQID(); if (state.failed) return ;


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:25: ( WS )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0 >= '\t' && LA18_0 <= '\n')||LA18_0==' ') ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:25: WS
                            {
                            mWS(); if (state.failed) return ;


                            }
                            break;

                    }


                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:29: ( ',' ( WS )? QID ( WS )? )*
                    loop21:
                    do {
                        int alt21=2;
                        int LA21_0 = input.LA(1);

                        if ( (LA21_0==',') ) {
                            alt21=1;
                        }


                        switch (alt21) {
                    	case 1 :
                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:30: ',' ( WS )? QID ( WS )?
                    	    {
                    	    match(','); if (state.failed) return ;

                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:34: ( WS )?
                    	    int alt19=2;
                    	    int LA19_0 = input.LA(1);

                    	    if ( ((LA19_0 >= '\t' && LA19_0 <= '\n')||LA19_0==' ') ) {
                    	        alt19=1;
                    	    }
                    	    switch (alt19) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:34: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    mQID(); if (state.failed) return ;


                    	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:42: ( WS )?
                    	    int alt20=2;
                    	    int LA20_0 = input.LA(1);

                    	    if ( ((LA20_0 >= '\t' && LA20_0 <= '\n')||LA20_0==' ') ) {
                    	        alt20=1;
                    	    }
                    	    switch (alt20) {
                    	        case 1 :
                    	            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:21:42: WS
                    	            {
                    	            mWS(); if (state.failed) return ;


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop21;
                        }
                    } while (true);


                    }
                    break;

            }


            match('{'); if (state.failed) return ;

            if ( state.backtracking==1 ) {NSLog(@"found method %@", (name!=null?name.getText():null));}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "METHOD"

    // $ANTLR start "FIELD"
    public final void mFIELD() throws RecognitionException {
        try {
            int _type = FIELD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:5: ( TYPE WS name= ID ( '[]' )? ( WS )? ( ';' | '=' ) )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:9: TYPE WS name= ID ( '[]' )? ( WS )? ( ';' | '=' )
            {
            mTYPE(); if (state.failed) return ;


            mWS(); if (state.failed) return ;


            int nameStart261 = getCharIndex();
            int nameStartLine261 = getLine();
            int nameStartCharPos261 = getCharPositionInLine();
            mID(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart261, getCharIndex()-1);
            name.setLine(nameStartLine261);
            name.setCharPositionInLine(nameStartCharPos261);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:25: ( '[]' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0=='[') ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:25: '[]'
                    {
                    match("[]"); if (state.failed) return ;



                    }
                    break;

            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:31: ( WS )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( ((LA24_0 >= '\t' && LA24_0 <= '\n')||LA24_0==' ') ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:26:31: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            if ( input.LA(1)==';'||input.LA(1)=='=' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( state.backtracking==1 ) {NSLog(@"found var %@", (name!=null?name.getText():null));}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FIELD"

    // $ANTLR start "STAT"
    public final void mSTAT() throws RecognitionException {
        try {
            int _type = STAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:5: ( ( 'if' | 'while' | 'switch' | 'for' ) ( WS )? '(' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:7: ( 'if' | 'while' | 'switch' | 'for' ) ( WS )? '('
            {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:7: ( 'if' | 'while' | 'switch' | 'for' )
            int alt25=4;
            switch ( input.LA(1) ) {
            case 'i':
                {
                alt25=1;
                }
                break;
            case 'w':
                {
                alt25=2;
                }
                break;
            case 's':
                {
                alt25=3;
                }
                break;
            case 'f':
                {
                alt25=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }

            switch (alt25) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:8: 'if'
                    {
                    match("if"); if (state.failed) return ;



                    }
                    break;
                case 2 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:13: 'while'
                    {
                    match("while"); if (state.failed) return ;



                    }
                    break;
                case 3 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:21: 'switch'
                    {
                    match("switch"); if (state.failed) return ;



                    }
                    break;
                case 4 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:30: 'for'
                    {
                    match("for"); if (state.failed) return ;



                    }
                    break;

            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:37: ( WS )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0 >= '\t' && LA26_0 <= '\n')||LA26_0==' ') ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:30:37: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            match('('); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STAT"

    // $ANTLR start "CALL"
    public final void mCALL() throws RecognitionException {
        try {
            int _type = CALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:33:5: (name= QID ( WS )? '(' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:33:9: name= QID ( WS )? '('
            {
            int nameStart326 = getCharIndex();
            int nameStartLine326 = getLine();
            int nameStartCharPos326 = getCharPositionInLine();
            mQID(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart326, getCharIndex()-1);
            name.setLine(nameStartLine326);
            name.setCharPositionInLine(nameStartCharPos326);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:33:18: ( WS )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( ((LA27_0 >= '\t' && LA27_0 <= '\n')||LA27_0==' ') ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:33:18: WS
                    {
                    mWS(); if (state.failed) return ;


                    }
                    break;

            }


            match('('); if (state.failed) return ;

            if ( state.backtracking==1 ) {/*ignore if this/super */ NSLog(@"found call %@",(name!=null?name.getText():null));}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CALL"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:38:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:38:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); if (state.failed) return ;



            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:38:14: ( options {greedy=false; } : . )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0=='*') ) {
                    int LA28_1 = input.LA(2);

                    if ( (LA28_1=='/') ) {
                        alt28=2;
                    }
                    else if ( ((LA28_1 >= '\u0000' && LA28_1 <= '.')||(LA28_1 >= '0' && LA28_1 <= '\uFFFF')) ) {
                        alt28=1;
                    }


                }
                else if ( ((LA28_0 >= '\u0000' && LA28_0 <= ')')||(LA28_0 >= '+' && LA28_0 <= '\uFFFF')) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:38:41: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);


            match("*/"); if (state.failed) return ;



            if ( state.backtracking==1 ) {NSLog(@"found comment %@", [self text]);}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "SL_COMMENT"
    public final void mSL_COMMENT() throws RecognitionException {
        try {
            int _type = SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:43:5: ( '//' ( options {greedy=false; } : . )* '\\n' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:43:9: '//' ( options {greedy=false; } : . )* '\\n'
            {
            match("//"); if (state.failed) return ;



            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:43:14: ( options {greedy=false; } : . )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0=='\n') ) {
                    alt29=2;
                }
                else if ( ((LA29_0 >= '\u0000' && LA29_0 <= '\t')||(LA29_0 >= '\u000B' && LA29_0 <= '\uFFFF')) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:43:41: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop29;
                }
            } while (true);


            match('\n'); if (state.failed) return ;

            if ( state.backtracking==1 ) {NSLog(@"found // comment %@", [self text]);}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SL_COMMENT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:48:2: ( '\"' ( options {greedy=false; } : ESC | . )* '\"' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:48:4: '\"' ( options {greedy=false; } : ESC | . )* '\"'
            {
            match('\"'); if (state.failed) return ;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:48:8: ( options {greedy=false; } : ESC | . )*
            loop30:
            do {
                int alt30=3;
                int LA30_0 = input.LA(1);

                if ( (LA30_0=='\"') ) {
                    alt30=3;
                }
                else if ( (LA30_0=='\\') ) {
                    int LA30_2 = input.LA(2);

                    if ( (LA30_2=='\"') ) {
                        alt30=1;
                    }
                    else if ( (LA30_2=='\\') ) {
                        alt30=1;
                    }
                    else if ( (LA30_2=='\'') ) {
                        alt30=1;
                    }
                    else if ( ((LA30_2 >= '\u0000' && LA30_2 <= '!')||(LA30_2 >= '#' && LA30_2 <= '&')||(LA30_2 >= '(' && LA30_2 <= '[')||(LA30_2 >= ']' && LA30_2 <= '\uFFFF')) ) {
                        alt30=2;
                    }


                }
                else if ( ((LA30_0 >= '\u0000' && LA30_0 <= '!')||(LA30_0 >= '#' && LA30_0 <= '[')||(LA30_0 >= ']' && LA30_0 <= '\uFFFF')) ) {
                    alt30=2;
                }


                switch (alt30) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:48:34: ESC
            	    {
            	    mESC(); if (state.failed) return ;


            	    }
            	    break;
            	case 2 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:48:40: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            match('\"'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:52:2: ( '\\'' ( options {greedy=false; } : ESC | . )* '\\'' )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:52:4: '\\'' ( options {greedy=false; } : ESC | . )* '\\''
            {
            match('\''); if (state.failed) return ;

            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:52:9: ( options {greedy=false; } : ESC | . )*
            loop31:
            do {
                int alt31=3;
                int LA31_0 = input.LA(1);

                if ( (LA31_0=='\'') ) {
                    alt31=3;
                }
                else if ( (LA31_0=='\\') ) {
                    int LA31_2 = input.LA(2);

                    if ( (LA31_2=='\'') ) {
                        alt31=1;
                    }
                    else if ( (LA31_2=='\\') ) {
                        alt31=1;
                    }
                    else if ( (LA31_2=='\"') ) {
                        alt31=1;
                    }
                    else if ( ((LA31_2 >= '\u0000' && LA31_2 <= '!')||(LA31_2 >= '#' && LA31_2 <= '&')||(LA31_2 >= '(' && LA31_2 <= '[')||(LA31_2 >= ']' && LA31_2 <= '\uFFFF')) ) {
                        alt31=2;
                    }


                }
                else if ( ((LA31_0 >= '\u0000' && LA31_0 <= '&')||(LA31_0 >= '(' && LA31_0 <= '[')||(LA31_0 >= ']' && LA31_0 <= '\uFFFF')) ) {
                    alt31=2;
                }


                switch (alt31) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:52:35: ESC
            	    {
            	    mESC(); if (state.failed) return ;


            	    }
            	    break;
            	case 2 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:52:41: .
            	    {
            	    matchAny(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            match('\''); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:55:5: ( ( ' ' | '\\t' | '\\n' )+ )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:55:9: ( ' ' | '\\t' | '\\n' )+
            {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:55:9: ( ' ' | '\\t' | '\\n' )+
            int cnt32=0;
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( ((LA32_0 >= '\t' && LA32_0 <= '\n')||LA32_0==' ') ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)==' ' ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt32 >= 1 ) break loop32;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(32, input);
                        throw eee;
                }
                cnt32++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "QID"
    public final void mQID() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:59:5: ( ID ( '.' ID )* )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:59:7: ID ( '.' ID )*
            {
            mID(); if (state.failed) return ;


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:59:10: ( '.' ID )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0=='.') ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:59:11: '.' ID
            	    {
            	    match('.'); if (state.failed) return ;

            	    mID(); if (state.failed) return ;


            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "QID"

    // $ANTLR start "QIDStar"
    public final void mQIDStar() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:2: ( ID ( '.' ID )* ( '.*' )? )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:4: ID ( '.' ID )* ( '.*' )?
            {
            mID(); if (state.failed) return ;


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:7: ( '.' ID )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0=='.') ) {
                    int LA34_1 = input.LA(2);

                    if ( ((LA34_1 >= 'A' && LA34_1 <= 'Z')||LA34_1=='_'||(LA34_1 >= 'a' && LA34_1 <= 'z')) ) {
                        alt34=1;
                    }


                }


                switch (alt34) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:8: '.' ID
            	    {
            	    match('.'); if (state.failed) return ;

            	    mID(); if (state.failed) return ;


            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:17: ( '.*' )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0=='.') ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:68:17: '.*'
                    {
                    match(".*"); if (state.failed) return ;



                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "QIDStar"

    // $ANTLR start "TYPE"
    public final void mTYPE() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:72:5: ( QID ( '[]' )? )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:72:9: QID ( '[]' )?
            {
            mQID(); if (state.failed) return ;


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:72:13: ( '[]' )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0=='[') ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:72:13: '[]'
                    {
                    match("[]"); if (state.failed) return ;



                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TYPE"

    // $ANTLR start "ARG"
    public final void mARG() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:76:5: ( TYPE WS ID )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:76:9: TYPE WS ID
            {
            mTYPE(); if (state.failed) return ;


            mWS(); if (state.failed) return ;


            mID(); if (state.failed) return ;


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ARG"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:80:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:80:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:80:33: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( ((LA37_0 >= '0' && LA37_0 <= '9')||(LA37_0 >= 'A' && LA37_0 <= 'Z')||LA37_0=='_'||(LA37_0 >= 'a' && LA37_0 <= 'z')) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "ESC"
    public final void mESC() throws RecognitionException {
        try {
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:84:5: ( '\\\\' ( '\"' | '\\'' | '\\\\' ) )
            // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:84:7: '\\\\' ( '\"' | '\\'' | '\\\\' )
            {
            match('\\'); if (state.failed) return ;

            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC"

    public void mTokens() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:39: ( IMPORT | RETURN | CLASS | METHOD | FIELD | STAT | CALL | COMMENT | SL_COMMENT | STRING | CHAR | WS )
        int alt38=12;
        switch ( input.LA(1) ) {
        case 'i':
            {
            int LA38_1 = input.LA(2);

            if ( (synpred1_Fuzzy()) ) {
                alt38=1;
            }
            else if ( (synpred4_Fuzzy()) ) {
                alt38=4;
            }
            else if ( (synpred5_Fuzzy()) ) {
                alt38=5;
            }
            else if ( (synpred6_Fuzzy()) ) {
                alt38=6;
            }
            else if ( (synpred7_Fuzzy()) ) {
                alt38=7;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 1, input);

                throw nvae;

            }
            }
            break;
        case 'r':
            {
            int LA38_7 = input.LA(2);

            if ( (synpred2_Fuzzy()) ) {
                alt38=2;
            }
            else if ( (synpred4_Fuzzy()) ) {
                alt38=4;
            }
            else if ( (synpred5_Fuzzy()) ) {
                alt38=5;
            }
            else if ( (synpred7_Fuzzy()) ) {
                alt38=7;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 7, input);

                throw nvae;

            }
            }
            break;
        case 'c':
            {
            int LA38_9 = input.LA(2);

            if ( (synpred3_Fuzzy()) ) {
                alt38=3;
            }
            else if ( (synpred4_Fuzzy()) ) {
                alt38=4;
            }
            else if ( (synpred5_Fuzzy()) ) {
                alt38=5;
            }
            else if ( (synpred7_Fuzzy()) ) {
                alt38=7;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 9, input);

                throw nvae;

            }
            }
            break;
        case 'f':
        case 's':
        case 'w':
            {
            int LA38_11 = input.LA(2);

            if ( (synpred4_Fuzzy()) ) {
                alt38=4;
            }
            else if ( (synpred5_Fuzzy()) ) {
                alt38=5;
            }
            else if ( (synpred6_Fuzzy()) ) {
                alt38=6;
            }
            else if ( (synpred7_Fuzzy()) ) {
                alt38=7;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 11, input);

                throw nvae;

            }
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
        case 'a':
        case 'b':
        case 'd':
        case 'e':
        case 'g':
        case 'h':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 't':
        case 'u':
        case 'v':
        case 'x':
        case 'y':
        case 'z':
            {
            int LA38_12 = input.LA(2);

            if ( (synpred4_Fuzzy()) ) {
                alt38=4;
            }
            else if ( (synpred5_Fuzzy()) ) {
                alt38=5;
            }
            else if ( (synpred7_Fuzzy()) ) {
                alt38=7;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 12, input);

                throw nvae;

            }
            }
            break;
        case '/':
            {
            int LA38_13 = input.LA(2);

            if ( (synpred8_Fuzzy()) ) {
                alt38=8;
            }
            else if ( (synpred9_Fuzzy()) ) {
                alt38=9;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 13, input);

                throw nvae;

            }
            }
            break;
        case '\"':
            {
            alt38=10;
            }
            break;
        case '\'':
            {
            alt38=11;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt38=12;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 38, 0, input);

            throw nvae;

        }

        switch (alt38) {
            case 1 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:41: IMPORT
                {
                mIMPORT(); if (state.failed) return ;


                }
                break;
            case 2 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:48: RETURN
                {
                mRETURN(); if (state.failed) return ;


                }
                break;
            case 3 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:55: CLASS
                {
                mCLASS(); if (state.failed) return ;


                }
                break;
            case 4 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:61: METHOD
                {
                mMETHOD(); if (state.failed) return ;


                }
                break;
            case 5 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:68: FIELD
                {
                mFIELD(); if (state.failed) return ;


                }
                break;
            case 6 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:74: STAT
                {
                mSTAT(); if (state.failed) return ;


                }
                break;
            case 7 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:79: CALL
                {
                mCALL(); if (state.failed) return ;


                }
                break;
            case 8 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:84: COMMENT
                {
                mCOMMENT(); if (state.failed) return ;


                }
                break;
            case 9 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:92: SL_COMMENT
                {
                mSL_COMMENT(); if (state.failed) return ;


                }
                break;
            case 10 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:103: STRING
                {
                mSTRING(); if (state.failed) return ;


                }
                break;
            case 11 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:110: CHAR
                {
                mCHAR(); if (state.failed) return ;


                }
                break;
            case 12 :
                // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:115: WS
                {
                mWS(); if (state.failed) return ;


                }
                break;

        }

    }

    // $ANTLR start synpred1_Fuzzy
    public final void synpred1_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:41: ( IMPORT )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:41: IMPORT
        {
        mIMPORT(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred1_Fuzzy

    // $ANTLR start synpred2_Fuzzy
    public final void synpred2_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:48: ( RETURN )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:48: RETURN
        {
        mRETURN(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred2_Fuzzy

    // $ANTLR start synpred3_Fuzzy
    public final void synpred3_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:55: ( CLASS )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:55: CLASS
        {
        mCLASS(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred3_Fuzzy

    // $ANTLR start synpred4_Fuzzy
    public final void synpred4_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:61: ( METHOD )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:61: METHOD
        {
        mMETHOD(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred4_Fuzzy

    // $ANTLR start synpred5_Fuzzy
    public final void synpred5_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:68: ( FIELD )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:68: FIELD
        {
        mFIELD(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred5_Fuzzy

    // $ANTLR start synpred6_Fuzzy
    public final void synpred6_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:74: ( STAT )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:74: STAT
        {
        mSTAT(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred6_Fuzzy

    // $ANTLR start synpred7_Fuzzy
    public final void synpred7_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:79: ( CALL )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:79: CALL
        {
        mCALL(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred7_Fuzzy

    // $ANTLR start synpred8_Fuzzy
    public final void synpred8_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:84: ( COMMENT )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:84: COMMENT
        {
        mCOMMENT(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred8_Fuzzy

    // $ANTLR start synpred9_Fuzzy
    public final void synpred9_Fuzzy_fragment() throws RecognitionException {
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:92: ( SL_COMMENT )
        // /Users/acondit/source/antlr/code/antlr/runtime/ObjC/Framework/examples/fuzzy/Fuzzy.g:1:92: SL_COMMENT
        {
        mSL_COMMENT(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred9_Fuzzy

    public final boolean synpred9_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_Fuzzy() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_Fuzzy_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

}
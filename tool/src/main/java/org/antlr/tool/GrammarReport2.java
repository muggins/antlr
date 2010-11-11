package org.antlr.tool;

/** Simplifying report dramatically for LL(*) paper.  Old results were
 *  wrong anyway it seems.  We need:
 *
 * 		percent decisions that potentially backtrack
 *  	histogram of regular lookahead depth (int k or *)
 */
public class GrammarReport2 {
	public static final String newline = System.getProperty("line.separator");

	public Grammar root;

	public GrammarReport2(Grammar rootGrammar) {
		this.root = rootGrammar;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		stats(root, buf);
		CompositeGrammar composite = root.composite;
		for (Grammar g : composite.getDelegates(root)) {
			stats(g, buf);
		}
		return buf.toString();
	}

	void stats(Grammar g, StringBuilder buf) {
		int numDec = g.getNumberOfDecisions();
		for (int decision=1; decision<=numDec; decision++) {
			Grammar.Decision d = g.getDecision(decision);
			if ( d.dfa==null ) { // unusued decisions in auto synpreds
				//System.err.println("no decision "+decision+" dfa for "+d.blockAST.toStringTree());
				continue;
			}
			int k = d.dfa.getMaxLookaheadDepth();
			Rule enclosingRule = d.dfa.decisionNFAStartState.enclosingRule;
			if ( enclosingRule.isSynPred ) continue; // don't count synpred rules
			buf.append(g.name+"."+enclosingRule.name+":" +
					   "");
			GrammarAST decisionAST =
				d.dfa.decisionNFAStartState.associatedASTNode;
			buf.append(decisionAST.getLine());
			buf.append(":");
			buf.append(decisionAST.getColumn());
			buf.append(" decision "+decision+":");
			
			if ( d.dfa.isCyclic() ) buf.append(" cyclic");
			if ( k!=Integer.MAX_VALUE ) buf.append(" k="+k); // fixed, no sempreds
			if ( d.dfa.hasSynPred() ) buf.append(" backtracks"); // isolated synpred not gated
			if ( d.dfa.hasSemPred() ) buf.append(" sempred"); // user-defined sempred
//			else {
//				buf.append("undefined");
//				FASerializer serializer = new FASerializer(g);
//				String result = serializer.serialize(d.dfa.startState);
//				System.err.println(result);
//			}
			nl(buf);
		}
	}

	void nl(StringBuilder buf) {
		buf.append(newline);
	}
}

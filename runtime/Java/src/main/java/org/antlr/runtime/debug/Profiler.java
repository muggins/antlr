/*
 [The "BSD license"]
 Copyright (c) 2005-2009 Terence Parr
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
package org.antlr.runtime.debug;

import org.antlr.runtime.*;
import org.antlr.runtime.misc.DoubleKeyMap;
import org.antlr.runtime.misc.Stats;

import java.io.IOException;
import java.util.*;

/** Using the debug event interface, track what is happening in the parser
 *  and record statistics about the runtime.
 */
public class Profiler extends BlankDebugEventListener {
	public static final String DATA_SEP = "\t";

	public static class ProfileStats {
		public String Version;
		public String name;
		public int numRuleInvocations;
		public int numUniqueRulesInvoked;
		public int numDecisionEvents;
		public int maxRuleInvocationDepth;
		public int numFixedDecisions;
		public int minDecisionMaxFixedLookaheads;
		public int maxDecisionMaxFixedLookaheads;
		public int avgDecisionMaxFixedLookaheads;
		public int stddevDecisionMaxFixedLookaheads;
		public int numCyclicDecisions;
		public int minDecisionMaxCyclicLookaheads;
		public int maxDecisionMaxCyclicLookaheads;
		public int avgDecisionMaxCyclicLookaheads;
		public int stddevDecisionMaxCyclicLookaheads;
		public int numBacktrackDecisions;
//		int Stats.min(toArray(decisionMaxSynPredLookaheads);
//		int Stats.max(toArray(decisionMaxSynPredLookaheads);
//		int Stats.avg(toArray(decisionMaxSynPredLookaheads);
//		int Stats.stddev(toArray(decisionMaxSynPredLookaheads);
		public int numSemanticPredicates;
		public int numTokens;
		public int numHiddenTokens;
		public int numCharsMatched;
		public int numHiddenCharsMatched;
		public int numReportedErrors;
		public int numMemoizationCacheHits;
		public int numMemoizationCacheMisses;
		public int numGuessingRuleInvocations;
		public int numMemoizationCacheEntries;
	}

	public static class DecisionDescriptor {
		public int decision;
		public String fileName;
		public String ruleName;
		public int line;
		public int pos;

		public int n;
		public int maxk;
		public int numSynPredEvals;
		public int numSemPredEvals;
	}

	// all about a specific exec of a single decision
	public static class DecisionEvent {
		public DecisionDescriptor decision;
		public int startIndex;
		public int maxk;
		public boolean evalSynPred;
		public boolean evalSemPred;
		public long startTime;
		public long stopTime;
		public int numMemoizationCacheHits;
		public int numMemoizationCacheMisses;
	}

	/** Because I may change the stats, I need to track that for later
	 *  computations to be consistent.
	 */
	public static final String Version = "3";
	public static final String RUNTIME_STATS_FILENAME = "runtime.stats";

	public DebugParser parser = null;

	// working variables

	protected int ruleLevel = 0;
	//protected int decisionLevel = 0;
	protected Token lastRealTokenTouchedInDecision;
	protected Set<String> uniqueRules = new HashSet<String>();
	protected Stack<String> currentGrammarFileName = new Stack();
	protected Stack<String> currentRuleName = new Stack();
	protected Stack<Integer> currentLine = new Stack();
	protected Stack<Integer> currentPos = new Stack();

	// Vector<DecisionStats>
	//protected Vector decisions = new Vector(200); // need setSize
	protected DoubleKeyMap<String,Integer, DecisionDescriptor> decisions =
		new DoubleKeyMap<String,Integer, DecisionDescriptor>();

	// Record a DecisionData for each decision we hit while parsing
	protected List<DecisionEvent> decisionEvents = new ArrayList<DecisionEvent>();
	protected Stack<DecisionEvent> decisionStack = new Stack<DecisionEvent>();

	ProfileStats stats = new ProfileStats();

	public Profiler() {
	}

	public Profiler(DebugParser parser) {
		this.parser = parser;
	}

	public void enterRule(String grammarFileName, String ruleName) {
		//System.out.println("enterRule "+grammarFileName+":"+ruleName);
		ruleLevel++;
		stats.numRuleInvocations++;
		uniqueRules.add(grammarFileName+":"+ruleName);
		stats.maxRuleInvocationDepth = Math.max(stats.maxRuleInvocationDepth, ruleLevel);
		currentGrammarFileName.push( grammarFileName );
		currentRuleName.push( ruleName );
	}

	public void exitRule(String grammarFileName, String ruleName) {
		ruleLevel--;
		currentGrammarFileName.pop();
		currentRuleName.pop();
	}

	/** Track memoization; this is not part of standard debug interface
	 *  but is triggered by profiling.  Code gen inserts an override
	 *  for this method in the recognizer, which triggers this method.
	 */
	public void examineRuleMemoization(IntStream input,
									   int ruleIndex,
									   String ruleName)
	{
		//System.out.println("examine memo "+ruleName);
		int stopIndex = parser.getRuleMemoization(ruleIndex, input.index());
		if ( stopIndex==BaseRecognizer.MEMO_RULE_UNKNOWN ) {
			//System.out.println("rule "+ruleIndex+" missed @ "+input.index());
			stats.numMemoizationCacheMisses++;
			stats.numGuessingRuleInvocations++; // we'll have to enter
			currentDecision().numMemoizationCacheMisses++;
		}
		else {
			// regardless of rule success/failure, if in cache, we have a cache hit
			//System.out.println("rule "+ruleIndex+" hit @ "+input.index());
			stats.numMemoizationCacheHits++;
			currentDecision().numMemoizationCacheHits++;
		}
	}

	public void memoize(IntStream input,
						int ruleIndex,
						int ruleStartIndex,
						String ruleName)
	{
		// count how many entries go into table
		//System.out.println("memoize "+ruleName);
		stats.numMemoizationCacheEntries++;
	}

	@Override
	public void location(int line, int pos) {
		currentLine.push(line);
		currentPos.push(pos);
	}

	public void enterDecision(int decisionNumber) {
		lastRealTokenTouchedInDecision = null;
		stats.numDecisionEvents++;
		int startingLookaheadIndex = parser.getTokenStream().index();
		TokenStream input = parser.getTokenStream();
		System.out.println("enterDecision " + decisionNumber + " depth " + decisionStack.size() +
						   " @ " + input.get(input.index()) +
						   " rule " +locationDescription());
		String g = (String) currentGrammarFileName.peek();
		DecisionDescriptor descriptor = decisions.get(g, decisionNumber);
		if ( descriptor ==null ) {
			descriptor = new DecisionDescriptor();
			decisions.put(g, decisionNumber, descriptor);
			descriptor.decision = decisionNumber;
			descriptor.fileName = (String)currentGrammarFileName.peek();
			descriptor.ruleName = (String)currentRuleName.peek();
			descriptor.line = (Integer)currentLine.peek();
			descriptor.pos = (Integer)currentPos.peek();
		}
		descriptor.n++;

		DecisionEvent d = new DecisionEvent();
		decisionStack.push(d);
		d.decision = descriptor;
		d.startTime = System.currentTimeMillis();
		d.startIndex = startingLookaheadIndex;
	}

	public void exitDecision(int decisionNumber) {
		DecisionEvent d = decisionStack.pop();
		d.stopTime = System.currentTimeMillis();

		int lastTokenIndex = lastRealTokenTouchedInDecision.getTokenIndex();
		int numHidden = getNumberOfHiddenTokens(d.startIndex, lastTokenIndex);
		int depth = lastTokenIndex - d.startIndex - numHidden + 1; // +1 counts consuming start token as 1
		d.maxk = depth;
		d.decision.maxk = Math.max(d.decision.maxk, depth);

		System.out.println("exitDecision "+decisionNumber+" in "+d.decision.ruleName+
						   " depth "+d.maxk+" max token "+lastRealTokenTouchedInDecision);
		decisionEvents.add(d); // done with decision; track all
	}

	public void consumeToken(Token token) {
		//System.out.println("consume token "+token);
		if ( !inDecision() ) return;
		if ( lastRealTokenTouchedInDecision==null ||
			 lastRealTokenTouchedInDecision.getTokenIndex() < token.getTokenIndex() )
		{
			lastRealTokenTouchedInDecision = token;
		}
		DecisionEvent d = currentDecision();
		// compute lookahead depth
		int thisRefIndex = token.getTokenIndex();
		int numHidden = getNumberOfHiddenTokens(d.startIndex, thisRefIndex);
		int depth = thisRefIndex - d.startIndex - numHidden + 1; // +1 counts consuming start token as 1
		//d.maxk = Math.max(d.maxk, depth);
		System.out.println("consume to "+thisRefIndex+" "+depth+" tokens ahead in "+
						   d.decision.ruleName+"-"+d.decision.decision+" start index "+d.startIndex);		
	}

	/** The parser is in a decision if the decision depth > 0.  This
	 *  works for backtracking also, which can have nested decisions.
	 */
	public boolean inDecision() {
		return decisionStack.size()>0;
	}

	public void consumeHiddenToken(Token token) {
		//System.out.println("consume hidden token "+token);
	}

	/** Track refs to lookahead if in a fixed/nonfixed decision.
	 */
	public void LT(int i, Token t) {
		if ( inDecision() && i>0 ) {
			if ( lastRealTokenTouchedInDecision==null ||
				 lastRealTokenTouchedInDecision.getTokenIndex() < t.getTokenIndex() )
			{
				lastRealTokenTouchedInDecision = t;
				System.out.println("last token "+lastRealTokenTouchedInDecision);
			}
			DecisionEvent d = currentDecision();
			System.out.println("LT("+i+")="+t+" index "+t.getTokenIndex()+" relative to "+d.decision.ruleName+"-"+
							   d.decision.decision+" start index "+d.startIndex);			
			// get starting index off stack
//			int stackTop = lookaheadStack.size()-1;
//			Integer startingIndex = (Integer)lookaheadStack.get(stackTop);
//			// compute lookahead depth
//			int thisRefIndex = parser.getTokenStream().index();
//			int numHidden =
//				getNumberOfHiddenTokens(startingIndex.intValue(), thisRefIndex);
//			int depth = i + thisRefIndex - startingIndex.intValue() - numHidden;
//			/*
//			System.out.println("LT("+i+") @ index "+thisRefIndex+" is depth "+depth+
//				" max is "+maxLookaheadInCurrentDecision);
//			*/
//			if ( depth>maxLookaheadInCurrentDecision ) {
//				maxLookaheadInCurrentDecision = depth;
//			}
//			d.maxk = currentDecision()/
		}
	}

	/** Track backtracking decisions.  You'll see a fixed or cyclic decision
	 *  and then a backtrack.
	 *
	 * 		enter rule
	 * 		...
	 * 		enter decision
	 * 		LA and possibly consumes (for cyclic DFAs)
	 * 		begin backtrack level
	 * 		mark m
	 * 		rewind m
	 * 		end backtrack level, success
	 * 		exit decision
	 * 		...
	 * 		exit rule
	 */
	public void beginBacktrack(int level) {
		System.out.println("enter backtrack "+level);
		stats.numBacktrackDecisions++;
		DecisionEvent d = currentDecision();
		d.evalSynPred = true;
		d.decision.numSynPredEvals++;
	}

	/** Successful or not, track how much lookahead synpreds use */
	public void endBacktrack(int level, boolean successful) {
		System.out.println("exit backtrack "+level+": "+successful);
	}

	@Override
	public void mark(int i) {
		System.out.println("mark "+i);
	}

	@Override
	public void rewind(int i) {
		System.out.println("rewind "+i);
	}

	@Override
	public void rewind() {
		System.out.println("rewind");
	}

	protected DecisionEvent currentDecision() {
		return decisionStack.peek();
	}

	public void recognitionException(RecognitionException e) {
		stats.numReportedErrors++;
	}

	public void semanticPredicate(boolean result, String predicate) {
		stats.numSemanticPredicates++;
		if ( inDecision() ) {
			DecisionEvent d = currentDecision();
			d.evalSemPred = true;
			d.decision.numSemPredEvals++;
			System.out.println("eval "+predicate+" in "+d.decision.ruleName+"-"+
							   d.decision.decision);
		}
	}

	public void terminate() {
		String stats = toNotifyString();
		try {
			Stats.writeReport(RUNTIME_STATS_FILENAME,stats);
		}
		catch (IOException ioe) {
			System.err.println(ioe);
			ioe.printStackTrace(System.err);
		}
		System.out.println(toString());
	}

	public void setParser(DebugParser parser) {
		this.parser = parser;
	}

	// R E P O R T I N G

	public String toNotifyString() {
		StringBuffer buf = new StringBuffer();
		buf.append(Version);
		buf.append('\t');
		buf.append(parser.getClass().getName());
//		buf.append('\t');
//		buf.append(numRuleInvocations);
//		buf.append('\t');
//		buf.append(maxRuleInvocationDepth);
//		buf.append('\t');
//		buf.append(numFixedDecisions);
//		buf.append('\t');
//		buf.append(Stats.min(decisionMaxFixedLookaheads));
//		buf.append('\t');
//		buf.append(Stats.max(decisionMaxFixedLookaheads));
//		buf.append('\t');
//		buf.append(Stats.avg(decisionMaxFixedLookaheads));
//		buf.append('\t');
//		buf.append(Stats.stddev(decisionMaxFixedLookaheads));
//		buf.append('\t');
//		buf.append(numCyclicDecisions);
//		buf.append('\t');
//		buf.append(Stats.min(decisionMaxCyclicLookaheads));
//		buf.append('\t');
//		buf.append(Stats.max(decisionMaxCyclicLookaheads));
//		buf.append('\t');
//		buf.append(Stats.avg(decisionMaxCyclicLookaheads));
//		buf.append('\t');
//		buf.append(Stats.stddev(decisionMaxCyclicLookaheads));
//		buf.append('\t');
//		buf.append(numBacktrackDecisions);
//		buf.append('\t');
//		buf.append(Stats.min(toArray(decisionMaxSynPredLookaheads)));
//		buf.append('\t');
//		buf.append(Stats.max(toArray(decisionMaxSynPredLookaheads)));
//		buf.append('\t');
//		buf.append(Stats.avg(toArray(decisionMaxSynPredLookaheads)));
//		buf.append('\t');
//		buf.append(Stats.stddev(toArray(decisionMaxSynPredLookaheads)));
//		buf.append('\t');
//		buf.append(numSemanticPredicates);
//		buf.append('\t');
//		buf.append(parser.getTokenStream().size());
//		buf.append('\t');
//		buf.append(numHiddenTokens);
//		buf.append('\t');
//		buf.append(numCharsMatched);
//		buf.append('\t');
//		buf.append(numHiddenCharsMatched);
//		buf.append('\t');
//		buf.append(numberReportedErrors);
//		buf.append('\t');
//		buf.append(numMemoizationCacheHits);
//		buf.append('\t');
//		buf.append(numMemoizationCacheMisses);
//		buf.append('\t');
//		buf.append(numGuessingRuleInvocations);
//		buf.append('\t');
//		buf.append(numMemoizationCacheEntries);
		return buf.toString();
	}

	public String toString() {
		return toString(getReport());
	}

	public ProfileStats getReport() {
		TokenStream input = parser.getTokenStream();
		for (int i=0; i<input.size()&& lastRealTokenTouchedInDecision !=null&&i<= lastRealTokenTouchedInDecision.getTokenIndex(); i++) {
			Token t = input.get(i);
			if ( t.getChannel()!=Token.DEFAULT_CHANNEL ) {
				stats.numHiddenTokens++;
				stats.numHiddenCharsMatched += t.getText().length();
			}
		}
		stats.Version = Version;
		stats.name = parser.getClass().getName();
		stats.numUniqueRulesInvoked = uniqueRules.size();
		//stats.numCharsMatched = lastTokenConsumed.getStopIndex() + 1;
		return stats;
	}

	public DoubleKeyMap getDecisionStats() {
		return decisions;
	}

	public List getDecisionEvents() {
		return decisionEvents;
	}

	public static String toString(ProfileStats stats) {
		StringBuffer buf = new StringBuffer();
		buf.append("ANTLR Runtime Report; Profile Version ");
		buf.append(stats.Version);
		buf.append('\n');
		buf.append("parser name ");
		buf.append(stats.name);
		buf.append('\n');
		buf.append("Number of rule invocations ");
		buf.append(stats.numRuleInvocations);
		buf.append('\n');
		buf.append("Number of unique rules visited ");
		buf.append(stats.numUniqueRulesInvoked);
		buf.append('\n');
		buf.append("Number of decision events ");
		buf.append(stats.numDecisionEvents);
		buf.append('\n');
		buf.append("Number of rule invocations while backtracking ");
		buf.append(stats.numGuessingRuleInvocations);
		buf.append('\n');
		buf.append("max rule invocation nesting depth ");
		buf.append(stats.maxRuleInvocationDepth);
		buf.append('\n');
//		buf.append("number of fixed lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("min lookahead used in a fixed lookahead decision ");
//		buf.append();
//		buf.append('\n');
//		buf.append("max lookahead used in a fixed lookahead decision ");
//		buf.append();
//		buf.append('\n');
//		buf.append("average lookahead depth used in fixed lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("standard deviation of depth used in fixed lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("number of arbitrary lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("min lookahead used in an arbitrary lookahead decision ");
//		buf.append();
//		buf.append('\n');
//		buf.append("max lookahead used in an arbitrary lookahead decision ");
//		buf.append();
//		buf.append('\n');
//		buf.append("average lookahead depth used in arbitrary lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("standard deviation of depth used in arbitrary lookahead decisions ");
//		buf.append();
//		buf.append('\n');
//		buf.append("number of evaluated syntactic predicates ");
//		buf.append();
//		buf.append('\n');
//		buf.append("min lookahead used in a syntactic predicate ");
//		buf.append();
//		buf.append('\n');
//		buf.append("max lookahead used in a syntactic predicate ");
//		buf.append();
//		buf.append('\n');
//		buf.append("average lookahead depth used in syntactic predicates ");
//		buf.append();
//		buf.append('\n');
//		buf.append("standard deviation of depth used in syntactic predicates ");
//		buf.append();
//		buf.append('\n');
		buf.append("rule memoization cache size ");
		buf.append(stats.numMemoizationCacheEntries);
		buf.append('\n');
		buf.append("number of rule memoization cache hits ");
		buf.append(stats.numMemoizationCacheHits);
		buf.append('\n');
		buf.append("number of rule memoization cache misses ");
		buf.append(stats.numMemoizationCacheMisses);
		buf.append('\n');
//		buf.append("number of evaluated semantic predicates ");
//		buf.append();
//		buf.append('\n');
		buf.append("number of tokens ");
		buf.append(stats.numTokens);
		buf.append('\n');
		buf.append("number of hidden tokens ");
		buf.append(stats.numHiddenTokens);
		buf.append('\n');
		buf.append("number of char ");
		buf.append(stats.numCharsMatched);
		buf.append('\n');
		buf.append("number of hidden char ");
		buf.append(stats.numHiddenCharsMatched);
		buf.append('\n');
		buf.append("number of syntax errors ");
		buf.append(stats.numReportedErrors);
		buf.append('\n');
		return buf.toString();
	}

	public String getDecisionStatsDump() {
		StringBuffer buf = new StringBuffer();
		buf.append("location");
		buf.append(DATA_SEP);
		buf.append("n");
		buf.append(DATA_SEP);
		buf.append("maxk");
		buf.append(DATA_SEP);
		buf.append("synpred");
		buf.append(DATA_SEP);
		buf.append("sempred");
		buf.append("\n");
		for (String fileName : decisions.keySet()) {
			for (int d : decisions.keySet(fileName)) {
				DecisionDescriptor s = decisions.get(fileName, d);
				buf.append(s.decision);
				buf.append("@");
				buf.append(locationDescription(s.fileName,s.ruleName,s.line,s.pos)); // decision number
				buf.append(DATA_SEP);
				buf.append(s.n);
				buf.append(DATA_SEP);
				buf.append(s.maxk);
				buf.append(DATA_SEP);
				buf.append(s.numSynPredEvals);
				buf.append(DATA_SEP);
				buf.append(s.numSemPredEvals);
				buf.append('\n');
			}
		}
		return buf.toString();
	}

	protected int[] trim(int[] X, int n) {
		if ( n<X.length ) {
			int[] trimmed = new int[n];
			System.arraycopy(X,0,trimmed,0,n);
			X = trimmed;
		}
		return X;
	}

	protected int[] toArray(List a) {
		int[] x = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			Integer I = (Integer) a.get(i);
			x[i] = I.intValue();
		}
		return x;
	}

	/** Get num hidden tokens between i..j inclusive */
	public int getNumberOfHiddenTokens(int i, int j) {
		int n = 0;
		TokenStream input = parser.getTokenStream();
		for (int ti = i; ti<input.size() && ti <= j; ti++) {
			Token t = input.get(ti);
			if ( t.getChannel()!=Token.DEFAULT_CHANNEL ) {
				n++;
			}
		}
		return n;
	}

	protected String locationDescription() {
		return locationDescription(
			currentGrammarFileName.peek(),
			currentRuleName.peek(),
			currentLine.peek(),
			currentPos.peek());
	}

	protected String locationDescription(String file, String rule, int line, int pos) {
		return file+":"+line+":"+pos+"(" + rule + ")";
	}
}

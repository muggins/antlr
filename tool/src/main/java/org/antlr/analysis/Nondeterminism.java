/*
 * [The "BSD license"]
 *  Copyright (c) 2010 Terence Parr
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.antlr.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.misc.IntSet;
import org.antlr.runtime.Token;
import org.antlr.tool.Grammar;
import org.antlr.tool.GrammarNonDeterminismMessage;

/** So Edgar Espina can get ambig paths in grammar */
public class Nondeterminism {

	public Nondeterminism() {
	}

	public void manageNonDeterminism(GrammarNonDeterminismMessage message) {

		System.out.println("**************** NON-DETERMINISM ****************");

		DecisionProbe decisionProbe = message.probe;

		DFA dfa = decisionProbe.dfa;

		MachineProbe probe = new MachineProbe(dfa);

		findPaths(decisionProbe, probe, message.problemState);
	}

	@SuppressWarnings("unchecked")
	private void findPaths(DecisionProbe decisionProbe,
			MachineProbe probe, DFAState ambiguousState) {
		List<Integer> alts = decisionProbe
				.getNonDeterministicAltsForState(ambiguousState);

		Collections.sort(alts);
		System.out.println("ambig alts=" + alts);

		List<DFAState> dfaStates = probe.getAnyDFAPathToTarget(ambiguousState);
		System.out.print("DFA path =");
		for (DFAState dfaState : dfaStates) {
			System.out.print(" " + dfaState.stateNumber);
		}
		System.out.println("");

		List<IntSet> labels = probe.getEdgeLabels(ambiguousState);

		Grammar g = ambiguousState.dfa.nfa.grammar;

		System.out
				.println("labels=" + probe.getInputSequenceDisplay(g, labels));

		for (int alt : alts) {
			List<Set<NFAState>> nfaStates = collectNFAState(dfaStates, alt);
			System.out.println("NFAConfigs per state: " + nfaStates);
			List<Token> path = probe.getGrammarLocationsForInputSequence(
					nfaStates, labels);
			System.out.println("alt " + alt + " path = " + path);
		}
	}

	private List<Set<NFAState>> collectNFAState(Collection<DFAState> dfaStates,
			int alt) {
		List<Set<NFAState>> states = new ArrayList<Set<NFAState>>();
		for (DFAState dfaState : dfaStates) {
			states.add(collectNFAState(dfaState, alt));
		}
		return states;
	}

	private Set<NFAState> collectNFAState(DFAState dfaState, int alt) {
		NFA nfa = dfaState.dfa.nfa;
		Set<NFAState> states = new HashSet<NFAState>();
		for (Object object : dfaState.nfaConfigurations) {
			NFAConfiguration nfaConfig = (NFAConfiguration) object;
			if (nfaConfig.alt == alt) {
				NFAState state = nfa.getState(nfaConfig.state);
				states.add(state);
			}
		}
		return states;
	}
}

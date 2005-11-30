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
package org.antlr.codegen;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.analysis.*;
import org.antlr.tool.GrammarAST;
import org.antlr.tool.ANTLRParser;
import org.antlr.misc.Utils;

import java.util.List;

public class ACyclicDFACodeGenerator {
	protected CodeGenerator parent;

	public ACyclicDFACodeGenerator(CodeGenerator parent) {
		this.parent = parent;
	}

	public StringTemplate genFixedLookaheadDecision(StringTemplateGroup templates,
													DFA dfa)
	{
		return walkFixedDFAGeneratingStateMachine(templates, dfa, dfa.startState, 1);
	}

	protected StringTemplate walkFixedDFAGeneratingStateMachine(
			StringTemplateGroup templates,
			DFA dfa,
			DFAState s,
			int k)
	{
		/*
		System.out.println("walkFixedDFAGeneratingStateMachine DFA.state "+
						   dfa.decisionNumber+"."+s.stateNumber);
		*/
		if ( s.isAcceptState() ) {
			StringTemplate dfaST = templates.getInstanceOf("dfaAcceptState");
			dfaST.setAttribute("alt", new Integer(s.getUniquelyPredictedAlt()));
			return dfaST;
		}

		// the default templates for generating a state and its edges
		// can be an if-then-else structure or a switch
		String dfaStateName = "dfaState";
		String dfaLoopbackStateName = "dfaLoopbackState";
		String dfaOptionalBlockStateName = "dfaOptionalBlockState";
		String dfaEdgeName = "dfaEdge";
		if ( parent.canGenerateSwitch(s) ) {
			dfaStateName = "dfaStateSwitch";
			dfaLoopbackStateName = "dfaLoopbackStateSwitch";
			dfaOptionalBlockStateName = "dfaOptionalBlockStateSwitch";
			dfaEdgeName = "dfaEdgeSwitch";
		}

		/*
		int oldMax = parent.decisionToMaxLookaheadDepth[dfa.getDecisionNumber()];
		if( k > oldMax ) {
			// track max (don't count the accept state)
			parent.decisionToMaxLookaheadDepth[dfa.getDecisionNumber()] = k;
		}
		*/
		StringTemplate dfaST = templates.getInstanceOf(dfaStateName);
		if ( dfa.getNFADecisionStartState().decisionStateType==NFAState.LOOPBACK ) {
			dfaST = templates.getInstanceOf(dfaLoopbackStateName);
		}
		else if ( dfa.getNFADecisionStartState().decisionStateType==NFAState.OPTIONAL_BLOCK_START ) {
			dfaST = templates.getInstanceOf(dfaOptionalBlockStateName);
		}
		dfaST.setAttribute("k", new Integer(k));
		dfaST.setAttribute("stateNumber", new Integer(s.stateNumber));
		String description = dfa.getNFADecisionStartState().getDescription();
		description = parent.target.getTargetStringLiteralFromString(description);
		//System.out.println("DFA: "+description+" associated with AST "+decisionASTNode);
		if ( description!=null ) {
			dfaST.setAttribute("description", description);
		}
		int EOTPredicts = NFA.INVALID_ALT_NUMBER;
		DFAState EOTTarget = null;
		//System.out.println("DFA state "+s.stateNumber);
		for (int i = 0; i < s.getNumberOfTransitions(); i++) {
			Transition edge = (Transition) s.transition(i);
			//System.out.println("edge label "+edge.label.toString());
			if ( edge.label.getAtom()==Label.EOT ) {
				// don't generate a real edge for EOT; track alt EOT predicts
				// generate that prediction in the else clause as default case
				EOTTarget = (DFAState)edge.target;
				EOTPredicts = EOTTarget.getUniquelyPredictedAlt();
				/*
				System.out.println("DFA s"+s.stateNumber+" EOT goes to s"+
								   edge.target.stateNumber+" predicates alt "+
								   EOTPredicts);
				*/
				// TODO: BUG; preds after EOT are NOT generated!
				// do the EOT edge last as ELSE clause and therefore no
				// error clause should be generated.
				// Actually, we can get away with adding all the edges
				// emanating from the EOT target state as edges on this
				// state as long as they are *after* everything else.
				// The EOT arc is like a default wildcard clause
				continue;
			}
			StringTemplate edgeST = templates.getInstanceOf(dfaEdgeName);
			// If the template wants all the label values delineated, do that
			if ( edgeST.getFormalArgument("labels")!=null ) {
				List labels = edge.label.getSet().toList();
				for (int j = 0; j < labels.size(); j++) {
					Integer vI = (Integer) labels.get(j);
					String label =
						parent.getTokenTypeAsTargetLabel(vI.intValue());
					labels.set(j, label); // rewrite List element to be name
				}
				edgeST.setAttribute("labels", labels);
			}
			else { // else create an expression to evaluate (the general case)
				edgeST.setAttribute("labelExpr",
								parent.genLabelExpr(templates,edge,k));
			}
			if ( true ) {
				DFAState target = (DFAState)edge.target;
				//System.out.println("preds="+target.getGatedPredicatesInNFAConfigurations());
				edgeST.setAttribute("predicates",
									((DFAState)edge.target).getGatedPredicatesInNFAConfigurations());
			}

			StringTemplate targetST =
				walkFixedDFAGeneratingStateMachine(templates,
												   dfa,
												   (DFAState)edge.target,
												   k+1);
			edgeST.setAttribute("targetState", targetST);
			dfaST.setAttribute("edges", edgeST);
			/*
			System.out.println("back to DFA "+
							   dfa.decisionNumber+"."+s.stateNumber);
							   */
		}

		// HANDLE EOT EDGE
		if ( EOTPredicts!=NFA.INVALID_ALT_NUMBER ) {
			// EOT unique predicts an alt
			dfaST.setAttribute("eotPredictsAlt", new Integer(EOTPredicts));
		}
		else if ( EOTTarget!=null && EOTTarget.getNumberOfTransitions()>0 ) {
			// EOT state has transitions so must split on predicates.
			// Generate predicate else-if clauses and then generate
			// NoViableAlt exception as else clause.
			// Note: these predicates emanate from the EOT target state
			// rather than the current DFAState s so the error message
			// might be slightly misleading if you are looking at the
			// state number.  Predicates emanating from EOT targets are
			// hoisted up to the state that has the EOT edge.
			for (int i = 0; i < EOTTarget.getNumberOfTransitions(); i++) {
				Transition predEdge = (Transition)EOTTarget.transition(i);
				StringTemplate edgeST = templates.getInstanceOf(dfaEdgeName);
				edgeST.setAttribute("labelExpr",
							parent.genSemanticPredicateExpr(templates,predEdge));
				// the target must be an accept state
				StringTemplate targetST =
					walkFixedDFAGeneratingStateMachine(templates,
													   dfa,
													   (DFAState)predEdge.target,
													   k+1);
				edgeST.setAttribute("targetState", targetST);
				dfaST.setAttribute("edges", edgeST);
			}
		}
		return dfaST;
	}
}


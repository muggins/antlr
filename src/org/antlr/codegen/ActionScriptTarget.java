/*
 [The "BSD licence"]
 Copyright (c) 2005-2006 Terence Parr
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

import org.antlr.Tool;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.tool.Grammar;

public class ActionScriptTarget extends Target {

    public String getTargetCharLiteralFromANTLRCharLiteral(
            CodeGenerator generator,
            String literal) {
        String newVal = "" + (int) literal.charAt(1);
        return newVal;
    }

    public String getTokenTypeAsTargetLabel(CodeGenerator generator,
                                            int ttype) {
        // use ints for predefined types;
        // <invalid> <EOR> <DOWN> <UP>
        if (ttype >= 0 && ttype <= 3) {
            return String.valueOf(ttype);
        }

        String name = generator.grammar.getTokenDisplayName(ttype);

        // If name is a literal, return the token type instead
        if (name.charAt(0) == '\'') {
            return String.valueOf(ttype);
        }

        return name;
    }

    /** Convert long to two 32-bit numbers separted by a comma.
     *  ActionScript does not support 64-bit numbers, so we need to break
     *  the number into two 32-bit literals to give to the Bit.  A number like
     *  0xHHHHHHHHLLLLLLLL is broken into the following string:
     *  "0xLLLLLLLL, 0xHHHHHHHH"
	 *  Note that the low order bits are first, followed by the high order bits.
     *  This is to match how the BitSet constructor works, where the bits are
     *  passed in in 32-bit chunks with low-order bits coming first.
	 */
	public String getTarget64BitStringFromValue(long word) {
		StringBuffer buf = new StringBuffer(22); // enough for the two "0x", "," and " "
		buf.append("0x");
        writeHexWithPadding(buf, Integer.toHexString((int)(word & 0x00000000ffffffffL)));
        buf.append(", 0x");
        writeHexWithPadding(buf, Integer.toHexString((int)(word >> 32)));

        return buf.toString();
	}

    private void writeHexWithPadding(StringBuffer buf, String digits) {
       digits = digits.toUpperCase();
		int padding = 8 - digits.length();
		// pad left with zeros
		for (int i=1; i<=padding; i++) {
			buf.append('0');
		}
		buf.append(digits);
    }

    protected StringTemplate chooseWhereCyclicDFAsGo(Tool tool,
                                                     CodeGenerator generator,
                                                     Grammar grammar,
                                                     StringTemplate recognizerST,
                                                     StringTemplate cyclicDFAST) {
        return recognizerST;
    }
}


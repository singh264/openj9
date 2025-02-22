/*******************************************************************************
 * Copyright IBM Corp. and others 2001
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package j9vm.test.ddrext.util;

import j9vm.test.ddrext.Constants;

import java.io.OutputStream;
import java.io.PrintStream;

public class DDROutputStream extends PrintStream {
	private StringBuffer outBuff;

	public DDROutputStream(OutputStream out) {
		super(out);
		outBuff = new StringBuffer();
	}

	@Override
	public void print(String x) {
		outBuff.append(x);
	}

	@Override
	public void println() {
		print(Constants.NL);
	}

	public void println(String x) {
		print(x + Constants.NL);
	}

	@Override
	public PrintStream printf(String format, Object... args) {
		outBuff.append(String.format(format, args));
		return this;
	}
	
	public StringBuffer getOutBuffer() {
		return outBuff;
	}

	public void clear() {
		outBuff.delete(0, outBuff.length());
	}

	@Override
	public PrintStream append(char c) {
		outBuff.append(c);
		return this;
	}
}

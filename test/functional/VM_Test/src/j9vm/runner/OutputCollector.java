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
package j9vm.runner;

import java.io.*;


public class OutputCollector extends Thread {
	private BufferedInputStream myStream;
	private ByteArrayOutputStream bout;

	public OutputCollector(BufferedInputStream myStream) {
		this.myStream = myStream;
		this.bout = new ByteArrayOutputStream();
	}

	public void run() {
		byte [] buf = new byte[100];
		try {
			int length = this.myStream.read(buf);
			while (length >= 0)  {
				System.out.write(buf, 0, length);
				bout.write(buf, 0, length);
				length = this.myStream.read(buf);
			}
		} catch(IOException e) {
			System.out.println("------------------- BEGIN bogus output from test? ----------------");
			e.printStackTrace();
			System.out.println("------------------- END bogus output from test? ----------------");		
		}
	}

	public byte[] getOutputAsByteArray() {
		return bout.toByteArray();
	}
}

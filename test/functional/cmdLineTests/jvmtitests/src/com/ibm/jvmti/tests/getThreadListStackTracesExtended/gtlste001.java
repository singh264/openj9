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
package com.ibm.jvmti.tests.getThreadListStackTracesExtended;

public class gtlste001 
{
	/**
	 * Check the correct type of frames in jvmtiGetThreadListStackTracesExtended()
	 * 
 	 * The testcase checks for at least 1 jitted frame 
	 * after relatively large number of iterations
	 * 
	 * @return true on pass
	 */

	static long    ii;
	static int     iter         = 10000;   /* enough iterations to jit methods*/
	static int     reached_iter = 0;
	static boolean passed       = false;

	public String helpGetThreadListStackTracesExtended()
	{
		return "Check the jvmtiGetThreaListdStackTracesExtended API for at least 1 jitted frame after a relatively large number of iterations " +
		       "Added as a unit test for J9 VM design ID 771";		
	}			
	
	public boolean testGetThreadListStackTracesExtended()
	{
		while (reached_iter < iter) {
			reached_iter++;
			myA();
		}
		return passed;
	}

	/* Native method to test jvmtiGetThreadListStackTracesExtended */
	public static native int anyJittedFrame(int high);
	
	static void myA()
	{
		myB();
	}

	static void myB()
	{
		int ret;
		
		for (int j = 0; j < 1000; j++) {
			ii++;
		}
		/* call anyJittedFrame() in the last iteration, 
		   when at least one method should be jitted 
		 */
		if (reached_iter == iter) {
			/* Let the JIT finish compiling */
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
						
			ret = anyJittedFrame(1);
			if (ret == 2) {
				myB();
			}
			
			if (ret == 0) {
				passed = false;
			} else {
				passed = true;			
			}
		}
	}
}

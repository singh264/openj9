/*******************************************************************************
 * Copyright IBM Corp. and others 2013
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
package com.ibm.j9.security;

import java.security.AccessControlException;

import com.ibm.j9.security.bootpath.CodeTrusted;

public class TestACdoPrivilegedWithCombiner {
	final static String	PROP_JAVA_HOME = "java.home";
	
	public static void main(String[] args) {
		new TestACdoPrivilegedWithCombiner().testAll();
	}
	
	void testAll() {
		System.setSecurityManager(new SecurityManager());
		final CodeTrusted		ct = new CodeTrusted();

		testPR83842(ct);
		
		System.out.println("ALL TESTS FINISHED");
	} 

	void testPR83842(final CodeTrusted ct) {
		try {
			String propValue = ct.getPropertyPrivDC(PROP_JAVA_HOME);
			System.out.println("PASS: property value returned: " + propValue);
		} catch (AccessControlException ace) {
			System.out.println("FAILED: AccessControlException NOT expected");
			ace.printStackTrace();
		}			
	}
}

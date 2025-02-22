/*******************************************************************************
 * Copyright IBM Corp. and others 1998
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
package org.openj9.test.java.lang;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

@Test(groups = { "level.sanity" })
public class Test_InstantiationError {
	/**
	 * @tests java.lang.InstantiationError#InstantiationError()
	 */
	@Test
	public void test_Constructor() {
		try {
			if (true)
				throw new InstantiationError();
		} catch (InstantiationError e) {
			AssertJUnit.assertTrue("Error not instantiated correctly: " + e.toString(),
					e.toString().equals("java.lang.InstantiationError"));
		} catch (Exception e) {
			AssertJUnit.assertTrue("Exception occured during test." + e.toString(), false);
		}
	}

	/**
	 * @tests java.lang.InstantiationError#InstantiationError(java.lang.String)
	 */
	@Test
	public void test_Constructor2() {
		try {
			if (true)
				throw new InstantiationError("test messsage InstantiationError");
		} catch (InstantiationError e) {
			AssertJUnit.assertTrue("Error not instantiated correctly: " + e.toString(),
					e.getMessage().equals("test messsage InstantiationError"));
		} catch (Exception e) {
			AssertJUnit.assertTrue("Exception occured during test." + e.toString(), false);
		}
	}
}

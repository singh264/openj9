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
package jit.test.jitt.misc;

import org.testng.annotations.Test;
import org.testng.Assert;

@Test(groups = { "level.sanity","component.jit" })
public class SanityS1_Strings extends jit.test.jitt.Test {

int tstString(String pString,int pi) {
	//S.printString(pString);//works with this call
	if (pi != 0)
	  {S localS = new S();
	   localS.printStringV(pString);
	  }
	return 1;
}
	@Test
	public void testSanityS1_Strings() {
	  int li=0;
				for (int i = 0; i < sJitThreshold - 2; i++)
				  li = tstString("myString",0);
				for (int i = 0; i < 4; i++)
				  li = tstString("myString",1);
				  if (li == 0)
				    {
				    Assert.fail("string error");
					};
	
	}
}

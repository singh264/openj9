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
// Test file: IntegerMultiply.java
// Testing the multiplication operation among integer type operand variables, and constants.

package jit.test.jitt.math2;

import org.testng.annotations.Test;
import org.testng.Assert;

@Test(groups = { "level.sanity","component.jit" })
public class IntegerMultiply extends jit.test.jitt.Test {

   @Test
   public void testIntegerMultiply() {

        int A, B;
        long C;
        int D;

        A = 1;
        B = 32767;
        D = tstdoMult(A,B);
        if (D != 32767)
                Assert.fail("IntegerMultiply->run: Incorrect multiplication for test #1!");



        A = 32767;
        B = 32767;
        D = tstdoMult(A,B);
        if (D != 1073676289)
                Assert.fail("IntegerMultiply->run: Incorrect multiplication for test #2!");



        A = 32767;
        D = tstdoIdentMult(A);
        if (D != -32767L)
                Assert.fail("IntegerMultiply->run: Incorrect multiplication for test #3!");


        A = 32767;
        C = tstdoShortConstMult(A);
        if (C != 33553408)
                Assert.fail("IntegerMultiply->run: Incorrect multiplication for test #4!");



        A = 32767;
        C = tstdoIntConstMult(A);
        if (C != 65536)
                Assert.fail("IntegerMultiply->run: Incorrect multiplication for test #5!");


   }

   static int tstdoMult(int A, int B){
    int C;
    C=A*B;

    return C;
   }

   static long tstdoNullMult(int A){
    long C;
    C=A*(0);

    return C;
   }

   static int tstdoIdentMult(int A){
    int C;
    C=A*(-1);

    return C;
   }

   static long tstdoShortConstMult(int A){
    long C;
    C=A*(1024);

    return C;
   }

   static long tstdoIntConstMult(int A){
    long C;
    C=A*(2147418112);

    return C;
   }

}




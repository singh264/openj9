/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2006
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
package com.ibm.dtfj.corereaders.zos.le;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/* This class was generated automatically by com.ibm.dtfj.corereaders.zos.util.Xml2Java */

public final class CeexcibhTemplate {

    public static int length() {
        return 2816;
    }

    public static long getCibh_back(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 4);
        return inputStream.readUnsignedInt() & 0xffffffffL;
    }
    public static int getCibh_back$offset() {
        return 4;
    }
    public static int getCibh_back$length() {
        return 32;
    }
    public static long getCibh_ptr_cib(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 16);
        return inputStream.readUnsignedInt() & 0xffffffffL;
    }
    public static int getCibh_ptr_cib$offset() {
        return 16;
    }
    public static int getCibh_ptr_cib$length() {
        return 32;
    }
    public static long getCibh_in_use(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 20);
        inputStream.setBitOffset(1);
        long result = inputStream.readBits(1);
        result <<= 63;
        result >>= 63;
        return result;
    }
    public static int getCibh_in_use$offset() {
        return 20;
    }
    public static int getCibh_in_use$length() {
        return 1;
    }
}

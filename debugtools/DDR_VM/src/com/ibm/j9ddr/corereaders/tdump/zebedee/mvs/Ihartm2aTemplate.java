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
package com.ibm.j9ddr.corereaders.tdump.zebedee.mvs;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/* This class was generated automatically by com.ibm.zebedee.util.Xml2Java */

public final class Ihartm2aTemplate {

    public static int length() {
        return 1744;
    }

    public static long getRtm2ereg(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 60);
        throw new Error("request for long value for field rtm2ereg which has length of 64");
    }
    public static int getRtm2ereg$offset() {
        return 60;
    }
    public static int getRtm2ereg$length() {
        return 512;
    }
    public static long getRtm2apsw(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 124);
        throw new Error("request for long value for field rtm2apsw which has length of 16");
    }
    public static int getRtm2apsw$offset() {
        return 124;
    }
    public static int getRtm2apsw$length() {
        return 128;
    }
    public static long getRtm2g64h(ImageInputStream inputStream, long address) throws IOException {
        inputStream.seek(address + 1480);
        throw new Error("request for long value for field rtm2g64h which has length of 64");
    }
    public static int getRtm2g64h$offset() {
        return 1480;
    }
    public static int getRtm2g64h$length() {
        return 512;
    }
}

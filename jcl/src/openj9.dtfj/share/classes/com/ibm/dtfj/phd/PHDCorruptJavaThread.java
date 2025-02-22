/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2008
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
package com.ibm.dtfj.phd;

import java.util.Collections;
import java.util.Iterator;

import com.ibm.dtfj.image.CorruptData;
import com.ibm.dtfj.image.CorruptDataException;
import com.ibm.dtfj.image.DataUnavailable;
import com.ibm.dtfj.image.ImageAddressSpace;
import com.ibm.dtfj.image.ImagePointer;
import com.ibm.dtfj.image.ImageSection;
import com.ibm.dtfj.image.ImageThread;
import com.ibm.dtfj.java.JavaObject;
import com.ibm.dtfj.java.JavaStackFrame;
import com.ibm.dtfj.java.JavaThread;

/**
 * @author ajohnson
 */
public class PHDCorruptJavaThread extends PHDCorruptData implements JavaThread {

	PHDCorruptJavaThread(ImageAddressSpace space, CorruptData cd) {
		super(space, cd);
	}

	public ImageThread getImageThread() throws CorruptDataException,
			DataUnavailable {
		throw new CorruptDataException(this);
	}

	public ImagePointer getJNIEnv() throws CorruptDataException {
		throw new CorruptDataException(this);
	}

	public String getName() throws CorruptDataException {
		throw new CorruptDataException(this);
	}

	public JavaObject getObject() throws CorruptDataException {
		throw new CorruptDataException(this);
	}

	public int getPriority() throws CorruptDataException {
		throw new CorruptDataException(this);
	}

	public Iterator<JavaStackFrame> getStackFrames() {
		return Collections.<JavaStackFrame>emptyList().iterator();
	}

	public Iterator<ImageSection> getStackSections() {
		return Collections.<ImageSection>emptyList().iterator();
	}

	public int getState() throws CorruptDataException {
		throw new CorruptDataException(this);
	}

	public JavaObject getBlockingObject() throws CorruptDataException {
		throw new CorruptDataException(this);
	}
}

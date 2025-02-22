/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2011
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
package com.ibm.java.diagnostics.utils;

import com.ibm.dtfj.image.Image;
import com.ibm.dtfj.image.ImageAddressSpace;
import com.ibm.dtfj.image.ImageProcess;
import com.ibm.dtfj.java.JavaRuntime;

/**
 * It is possible that if a core file is sufficiently corrupt, or that an invalid type of
 * file is passed to jdmpview, then at least one context needs to be created so that the
 * user can exit the tool
 * 
 * @author adam
 *
 */
public class EmptyDTFJContext extends DTFJContext {

	public EmptyDTFJContext(int major, int minor, Image image, ImageAddressSpace space, ImageProcess proc, JavaRuntime rt) {
		super(major, minor, image, space, proc, rt);
	}

	@Override
	public void refresh() {
		commands.clear();
		//these are the only hardwired commands that are always available within any context
		addGlobalCommandsToContext();	
	}
}

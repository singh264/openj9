/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2012
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
package com.ibm.jvm.dtfjview.tools.utils;

import java.util.LinkedList;

/**
 * This class first caches up to a specified number of lines before the matching is found.
 * The lines will be released whenever it is asked to do so.
 * <p>
 * @author Manqing Li, IBM.
 *
 */
public class MaxLinesPrematchHandle implements IPrematchHandle {
	public MaxLinesPrematchHandle(int maxPrematchLines) {
		this.maxPrematchLines = maxPrematchLines > 0 ? maxPrematchLines : 0;
		cached = new LinkedList<String>();
	}
	
	public void process(String s) {
		cached.addLast(s);
		if (maxPrematchLines < cached.size()) {
			cached.removeFirst();
		}
	}
	
	public String release() {
		StringBuffer sb = new StringBuffer();
		for (String line : cached) {
			sb.append(line);
		}
		cached = new LinkedList<String>();
		return sb.toString();
	}

	private final int maxPrematchLines;
	private LinkedList<String> cached;
}

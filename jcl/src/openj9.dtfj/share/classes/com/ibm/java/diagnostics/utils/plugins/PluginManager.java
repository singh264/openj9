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

package com.ibm.java.diagnostics.utils.plugins;

import java.net.URL;
import java.util.Set;

import com.ibm.java.diagnostics.utils.commands.CommandException;

/**
 * Used so that the DTFJPluginClassloader will not be referenced directly in code,
 * but only loaded through reflection, using a dedicated classloader (in DTFJContext)
 * @author blazejc
 *
 */
public interface PluginManager {
	public void refreshSearchPath();
	public void scanForClassFiles() throws CommandException;
	public Container getCache();
	/**
	 * Add a listener to the manager. If the listener already
	 * exists as determined by a call to equals() then the 
	 * existing instance is replace by the new one. This allows
	 * clients to install a single type of handler but update
	 * the configuration or it's methods of operation without retaining
	 * a handle to original listener.
	 * 
	 * @param listener listener to add
	 * @return result from the standard Set.add() method
	 */
	public boolean addListener(ClassListener listener);
	public boolean removeListener(ClassListener listener);
	public Set<ClassListener> getListeners();
	public URL[] getClasspath();
}

/*[INCLUDE-IF JAVA_SPEC_VERSION >= 8]*/
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
package java.lang.ref;

/**
 * SoftReference objects are used to detect referents which
 * are no longer visible and who's memory is to be reclaimed.
 *
 * @author		OTI
 * @version		initial
 * @since		1.2
 */	
public
/*[IF JAVA_SPEC_VERSION >= 19]*/
non-sealed
/*[ENDIF] JAVA_SPEC_VERSION >= 19 */
class SoftReference<T> extends Reference<T> {
	/*[PR 124242] SoftReference.get() should reset age */
	private volatile int age;

/**
 * Constructs a new instance of this class.
 *
 * @param		r	referent to track.
 * @param		q	queue to register to the reference object with.
 */	
public SoftReference(T r, ReferenceQueue<? super T> q) {
	initReference(r, q);
}

/**
 * Constructs a new instance of this class.
*
 * @param		r	referent to track.
 */	
public SoftReference(T r) {
	initReference(r);
}

/**
 * Return the referent of the reference object.
 *
 * @return	Referent to which reference refers,
 *			or null if object has been cleared.
 */	
public T get () {
	/*[PR 124242] SoftReference.get() should reset age*/
	if (age != 0) {
		age = 0;
	}
	return super.get();
}
}

/*******************************************************************************
 * Copyright IBM Corp. and others 2019
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
package org.openj9.test.javaagenttest.util;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM7;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;

public class FieldAdder extends CheckClassAdapter
{
	private final FieldNode _fn;
	private final String _className;
	private final String _methodName;

	public FieldAdder(ClassVisitor cv, FieldNode fn, String className, String methodName)
	{
		super(ASM7, cv, true);
		_fn = fn;
		_className = className;
		_methodName = methodName;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		if (name.equals(_methodName)) {
			mv = new MethodInstrumentAdapter_InitializeNewStatic(access, name, desc, mv, _className);
		}
		return mv;
	}

	@Override
	public void visitEnd()
	{
		_fn.accept(cv);
		super.visitEnd();
	}
}

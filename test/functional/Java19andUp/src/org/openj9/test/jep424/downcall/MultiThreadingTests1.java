/*******************************************************************************
 * Copyright IBM Corp. and others 2021
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
package org.openj9.test.jep424.downcall;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.AssertJUnit;

import java.lang.invoke.MethodHandle;
import java.lang.foreign.Addressable;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.SegmentAllocator;
import static java.lang.foreign.ValueLayout.*;

/**
 * Test cases for JEP 424: Foreign Linker API (Preview) for primitive types in downcall, which
 * verifies the downcalls with the same downcall handlder (cached as soft reference in OpenJDK)
 * in multithreading.
 */
@Test(groups = { "level.sanity" })
public class MultiThreadingTests1 implements Thread.UncaughtExceptionHandler {
	private volatile Throwable initException;
	private static Linker linker = Linker.nativeLinker();

	static {
		System.loadLibrary("clinkerffitests");
	}
	private static final SymbolLookup nativeLibLookup = SymbolLookup.loaderLookup();

	@Test(enabled=false)
	@Override
	public void uncaughtException(Thread thr, Throwable t) {
		initException =  t;
	}

	@Test
	public void test_twoThreadsWithSameFuncDesc_SameDowncallHandler() throws Throwable {
		Thread thr1 = new Thread(){
			public void run() {
				try {
					FunctionDescriptor fd = FunctionDescriptor.of(JAVA_INT, JAVA_INT, ADDRESS);
					Addressable functionSymbol = nativeLibLookup.lookup("addIntAndIntFromPointer").get();
					MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

					try (MemorySession session = MemorySession.openConfined()) {
						SegmentAllocator allocator = SegmentAllocator.newNativeArena(session);
						MemorySegment intSegmt = allocator.allocate(JAVA_INT, 215);
						int result = (int)mh.invoke(321, intSegmt);
						Assert.assertEquals(result, 536);
					}
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}
		};

		Thread thr2 = new Thread(){
			public void run() {
				try {
					FunctionDescriptor fd = FunctionDescriptor.of(JAVA_INT, JAVA_INT, ADDRESS);
					Addressable functionSymbol = nativeLibLookup.lookup("addIntAndIntFromPointer").get();
					MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

					try (MemorySession session = MemorySession.openConfined()) {
						SegmentAllocator allocator = SegmentAllocator.newNativeArena(session);
						MemorySegment intSegmt = allocator.allocate(JAVA_INT, 215);
						int result = (int)mh.invoke(322, intSegmt);
						Assert.assertEquals(result, 537);
					}
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}
		};

		thr1.setUncaughtExceptionHandler(this);
		thr2.setUncaughtExceptionHandler(this);

		thr1.start();
		thr2.start();

		thr1.join();
		thr2.join();

		if (initException != null){
			throw new RuntimeException(initException);
		}
	}
}

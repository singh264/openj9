################################################################################
# Copyright IBM Corp. and others 2017
#
# This program and the accompanying materials are made available under
# the terms of the Eclipse Public License 2.0 which accompanies this
# distribution and is available at https://www.eclipse.org/legal/epl-2.0/
# or the Apache License, Version 2.0 which accompanies this distribution and
# is available at https://www.apache.org/licenses/LICENSE-2.0.
#
# This Source Code may also be made available under the following
# Secondary Licenses when the conditions for such availability set
# forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
# General Public License, version 2 with the GNU Classpath
# Exception [1] and GNU General Public License, version 2 with the
# OpenJDK Assembly Exception [2].
#
# [1] https://www.gnu.org/software/classpath/license.html
# [2] https://openjdk.org/legal/assembly-exception.html
#
# SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
################################################################################

set(OMR_ENHANCED_WARNINGS OFF)

j9vm_add_library(j9dbgext STATIC
	dbgpool.c
	j9dbgext.c
)

target_include_directories(j9dbgext
	PUBLIC
		.
)

target_compile_definitions(j9dbgext
	PRIVATE
		J9VM_INTERP_STACKWALK_TRACING
		J9VM_INTERP_LINEAR_STACKWALK_TRACING
		UT_TRACE_OVERHEAD=0
)

target_link_libraries(j9dbgext
	PRIVATE
		j9vm_interface
		j9vm_compiler_defines
)

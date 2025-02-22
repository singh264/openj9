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

j9vm_add_executable(vmLifecycleTests
	main.c
)

target_link_libraries(vmLifecycleTests
	PRIVATE
		j9vm_interface
		j9vm_gc_includes
		j9vm_main_wrapper

		j9util
		j9utilcore
		j9avl
		j9hashtable
		j9pool
		j9thr
		j9prt
		j9exelib
		j9zip
		j9hookable
)

install(
	TARGETS vmLifecycleTests
	RUNTIME DESTINATION ${j9vm_SOURCE_DIR}
)

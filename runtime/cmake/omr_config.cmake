################################################################################
# Copyright IBM Corp. and others 2018
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

# Various config options we apply to OMR

# Default to OMR_DDR=OFF, but the set(CACHE) command is ignored if the variable already exists in the cache
# (unless 'FORCE' is specified, or type is 'INTERNAL') eg. because it was set on the command line with a '-D'
set(OMR_DDR OFF CACHE BOOL "")
set(OMR_EXAMPLE OFF CACHE INTERNAL "")
set(OMR_FVTEST OFF CACHE INTERNAL "")
set(OMR_GC ON CACHE INTERNAL "")
set(OMR_SEPARATE_DEBUG_INFO ON CACHE BOOL "")
set(OMRPORT_OMRSIG_SUPPORT ON CACHE INTERNAL "")

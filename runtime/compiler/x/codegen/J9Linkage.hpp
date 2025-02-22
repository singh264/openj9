/*******************************************************************************
 * Copyright IBM Corp. and others 2000
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

#ifndef J9_X86_LINKAGE_INCL
#define J9_X86_LINKAGE_INCL

/*
 * The following #define and typedef must appear before any #includes in this file
 */
#ifndef J9_LINKAGE_CONNECTOR
#define J9_LINKAGE_CONNECTOR
namespace J9 { namespace X86 { class Linkage; } }
namespace J9 { typedef J9::X86::Linkage LinkageConnector; }
#endif

#include "codegen/OMRLinkage.hpp"

namespace J9
{

namespace X86
{

class OMR_EXTENSIBLE Linkage : public OMR::LinkageConnector
   {
   public:
   Linkage(TR::CodeGenerator *cg) : OMR::LinkageConnector(cg) {}

   void alignOffset(uint32_t &stackIndex, int32_t localObjectAlignment);

   void alignLocalObjectWithCollectedFields(uint32_t & stackIndex);
   void alignLocalObjectWithoutCollectedFields(uint32_t & stackIndex);
   };

} // namespace X86

} // namespace J9

#endif

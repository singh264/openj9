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

#ifndef J9_ARM_CODEGENERATOR_INCL
#define J9_ARM_CODEGENERATOR_INCL

/*
 * The following #define and typedef must appear before any #includes in this file
 */
#ifndef J9_CODEGENERATOR_CONNECTOR
#define J9_CODEGENERATOR_CONNECTOR
namespace J9 { namespace ARM { class CodeGenerator; } }
namespace J9 { typedef J9::ARM::CodeGenerator CodeGeneratorConnector; }
#else
#error J9::ARM::CodeGenerator expected to be a primary connector, but a J9 connector is already defined
#endif


#include "compiler/codegen/J9CodeGenerator.hpp"
#include "codegen/LinkageConventionsEnum.hpp"

namespace TR { class Recompilation; }

namespace J9
{

namespace ARM
{

class OMR_EXTENSIBLE CodeGenerator : public J9::CodeGenerator
   {

protected:

   CodeGenerator(TR::Compilation *comp);

public:

   void initialize();

   TR::Recompilation *allocateRecompilationInfo();

   TR::Linkage *createLinkage(TR_LinkageConventions lc);

   void doBinaryEncoding();

   };

}

}

#endif

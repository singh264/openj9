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

#include "codegen/AheadOfTimeCompile.hpp"
#include "codegen/ARMAOTRelocation.hpp"
#include "codegen/ARMJNILinkage.hpp"
#include "codegen/ARMPrivateLinkage.hpp"
#include "compile/SymbolReferenceTable.hpp"
#include "codegen/CodeGenerator.hpp"
#include "codegen/CodeGenerator_inlines.hpp"
#include "codegen/Linkage.hpp"
#include "codegen/Linkage_inlines.hpp"
#include "codegen/PrivateLinkage.hpp"
#include "arm/codegen/ARMSystemLinkage.hpp"
#include "arm/codegen/ARMRecompilation.hpp"
#include "env/OMRMemory.hpp"
#include "codegen/ARMInstruction.hpp"
#include "codegen/GenerateInstructions.hpp"
#include "env/VMJ9.h"


extern void TEMPORARY_initJ9ARMTreeEvaluatorTable(TR::CodeGenerator *cg);

J9::ARM::CodeGenerator::CodeGenerator(TR::Compilation *comp) :
      J9::CodeGenerator(comp)
   {
   /**
    * Do not add CodeGenerator initialization logic here.
    * Use the \c initialize() method instead.
    */
   }

void
J9::ARM::CodeGenerator::initialize()
   {
   self()->J9::CodeGenerator::initialize();

   /*
    * "Statically" initialize the FE-specific tree evaluator functions.
    * This code only needs to execute once per JIT lifetime.
    */
   static bool initTreeEvaluatorTable = false;
   if (!initTreeEvaluatorTable)
      {
      TEMPORARY_initJ9ARMTreeEvaluatorTable(self());
      initTreeEvaluatorTable = true;
      }
   }

static int32_t identifyFarConditionalBranches(int32_t estimate, TR::CodeGenerator *cg)
   {
   TR_Array<TR::ARMConditionalBranchInstruction *> candidateBranches(cg->trMemory(), 256);
   TR::Instruction *cursorInstruction = cg->getFirstInstruction();

   while (cursorInstruction)
      {
      TR::ARMConditionalBranchInstruction *branch = cursorInstruction->getARMConditionalBranchInstruction();
      if (branch != NULL)
         {
         if (abs(branch->getEstimatedBinaryLocation() - branch->getLabelSymbol()->getEstimatedCodeLocation()) > 16384)
            {
            candidateBranches.add(branch);
            }
         }
      cursorInstruction = cursorInstruction->getNext();
      }

   // The following heuristic algorithm penalizes backward branches in
   // estimation, since it should be rare that a backward branch needs
   // a far relocation.

   for (int32_t i=candidateBranches.lastIndex(); i>=0; i--)
      {
      int32_t myLocation=candidateBranches[i]->getEstimatedBinaryLocation();
      int32_t targetLocation=candidateBranches[i]->getLabelSymbol()->getEstimatedCodeLocation();
      int32_t  j;

      if (targetLocation >= myLocation)
         {
         for (j=i+1; j<candidateBranches.size() &&
                 targetLocation>
                 candidateBranches[j]->getEstimatedBinaryLocation();
              j++)
            ;
         if ((targetLocation-myLocation + (j-i-1)*4) >= 32768)
            {
            candidateBranches[i]->setFarRelocation(true);
            }
         else
            {
            candidateBranches.remove(i);
            }
         }
      else    // backward branch
         {
         for (j=i-1; j>=0 && targetLocation<=
                 candidateBranches[j]->getEstimatedBinaryLocation();
              j--)
            ;
         if ((myLocation-targetLocation + (i-j-1)*4) > 32768)
            {
            candidateBranches[i]->setFarRelocation(true);
            }
         else
            {
            candidateBranches.remove(i);
            }
         }
      }
      return(estimate+4*candidateBranches.size());
   }

void J9::ARM::CodeGenerator::doBinaryEncoding()
   {
   TR::Compilation *comp = self()->comp();
   TR::CodeGenerator *cg = self();
   int32_t estimate = 0;
   TR::Recompilation *recomp = comp->getRecompilationInfo();
   TR::Instruction *tempInstruction;
   TR::Instruction *cursorInstruction = cg->getFirstInstruction();
   TR::Instruction *i2jEntryInstruction;
   TR::Instruction *j2jEntryInstruction;
   TR::ResolvedMethodSymbol *methodSymbol  = comp->getMethodSymbol();
   bool  isPrivateLinkage = (methodSymbol->getLinkageConvention() == TR_Private);

   if (methodSymbol->isJNI())
      {
      // leave space for the JNI target address
      cursorInstruction = cursorInstruction->getNext();
      }

   if (isPrivateLinkage)
      {
      j2jEntryInstruction = cursorInstruction->getNext();
      self()->getLinkage()->loadUpArguments(cursorInstruction);
      i2jEntryInstruction = cursorInstruction->getNext();
      }
   else
      {
      i2jEntryInstruction = j2jEntryInstruction = cursorInstruction;

      // TODO: Probably bogus; what does loadUpArguments do when cursorInstruction == NULL?
      cursorInstruction = NULL;
      self()->getLinkage()->loadUpArguments(cursorInstruction);
      }

   if (recomp != NULL)
      {
      recomp->generatePrePrologue();
      }

   cursorInstruction = cg->getFirstInstruction();

   while (cursorInstruction && cursorInstruction->getOpCodeValue() != TR::InstOpCode::proc)
      {
      estimate          = cursorInstruction->estimateBinaryLength(estimate);
      cursorInstruction = cursorInstruction->getNext();
      }
   tempInstruction = cursorInstruction;

   if ((recomp != NULL) && (!recomp->useSampling()))
      {
      tempInstruction = recomp->generatePrologue(tempInstruction);
      }

   self()->getLinkage()->createPrologue(tempInstruction);
   bool skipOneReturn = false;
   while (cursorInstruction)
      {
      if (cursorInstruction->getOpCodeValue() == TR::InstOpCode::retn)
         {
         if (skipOneReturn == false)
            {
            TR::Instruction *temp = cursorInstruction->getPrev();
            self()->getLinkage()->createEpilogue(temp);
            cursorInstruction = temp->getNext();
            skipOneReturn     = true;
            }
         else
            {
            skipOneReturn = false;
            }
         }
      estimate          = cursorInstruction->estimateBinaryLength(estimate);
      cursorInstruction = cursorInstruction->getNext();
      }
   estimate = self()->setEstimatedLocationsForSnippetLabels(estimate);
   if (estimate > 32768)
      {
      estimate = identifyFarConditionalBranches(estimate, self());
      }

   self()->setEstimatedWarmLength(estimate);

   cursorInstruction = cg->getFirstInstruction();
   uint8_t *coldCode = NULL;
   uint8_t *temp = self()->allocateCodeMemory(self()->getEstimatedWarmLength(), 0, &coldCode);

   self()->setBinaryBufferStart(temp);
   self()->setBinaryBufferCursor(temp);

   while (cursorInstruction)
      {
#ifdef DEBUG
      uint32_t estLen = cursorInstruction->estimateBinaryLength((int32_t)0);
#endif
      self()->setBinaryBufferCursor(cursorInstruction->generateBinaryEncoding());
      self()->addToAtlas(cursorInstruction);
      if (cursorInstruction->getNext() == i2jEntryInstruction)
         {
         self()->setPrePrologueSize(self()->getBinaryBufferCursor() - self()->getBinaryBufferStart());
         comp->getSymRefTab()->findOrCreateStartPCSymbolRef()->getSymbol()->getStaticSymbol()->setStaticAddress(self()->getBinaryBufferCursor());
         }
#ifdef DEBUG
      uint32_t binLen;
      binLen = cursorInstruction->getBinaryLength();
      if(binLen > estLen)
         {
         TR_ASSERT(0, "bin length estimated too small");
         }
#endif

      cursorInstruction = cursorInstruction->getNext();
      if (isPrivateLinkage && cursorInstruction == j2jEntryInstruction)
         {
         uint32_t linkageInfoWord = self()->initializeLinkageInfo(_returnTypeInfoInstruction->getBinaryEncoding());
         ((TR::ARMImmInstruction *)_returnTypeInfoInstruction)->setSourceImmediate(linkageInfoWord);
         }
      }
   // Create exception table entries for outlined instructions.
   //
   ListIterator<TR_ARMOutOfLineCodeSection> oiIterator(&self()->getARMOutOfLineCodeSectionList());
   TR_ARMOutOfLineCodeSection *oiCursor = oiIterator.getFirst();

   while (oiCursor)
      {
      uint32_t startOffset = oiCursor->getFirstInstruction()->getBinaryEncoding() - self()->getCodeStart();
      uint32_t endOffset   = oiCursor->getAppendInstruction()->getBinaryEncoding() - self()->getCodeStart();

      TR::Block * block = oiCursor->getBlock();
      bool needsETE = oiCursor->getFirstInstruction()->getNode()->getOpCode().hasSymbolReference() &&
                        oiCursor->getFirstInstruction()->getNode()->getSymbolReference() &&
                        oiCursor->getFirstInstruction()->getNode()->getSymbolReference()->canCauseGC();

      if (needsETE && block && !block->getExceptionSuccessors().empty())
         block->addExceptionRangeForSnippet(startOffset, endOffset);

      oiCursor = oiIterator.getNext();
      }
   }

// Get or create the TR::Linkage object that corresponds to the given linkage
// convention.
// Even though this method is common, its implementation is machine-specific.
//
TR::Linkage *J9::ARM::CodeGenerator::createLinkage(TR_LinkageConventions lc)
   {
   TR::Linkage *linkage;
   switch (lc)
      {
//    case TR_InterpretedStatic:
//       linkage = new (self()->trHeapMemory()) TR::ARMInterpretedStaticLinkage(this);
//       break;
      case TR_Private:
         linkage = new (self()->trHeapMemory()) J9::ARM::PrivateLinkage(self());
         break;
      case TR_System:
         linkage = new (self()->trHeapMemory()) TR::ARMSystemLinkage(self());
         break;
//    case TR_AllRegister:
//       linkage = new (self()->trHeapMemory()) TR::ARMAllRegisterLinkage(this);
//       break;
      case TR_CHelper:
      case TR_Helper:
         linkage = new (self()->trHeapMemory()) J9::ARM::HelperLinkage(self());
         break;
      case TR_J9JNILinkage:
         linkage = new (self()->trHeapMemory()) J9::ARM::JNILinkage(self());
         break;
      default :
         TR_ASSERT(0, "using system linkage for unrecognized convention %d\n", lc);
         linkage = new (self()->trHeapMemory()) TR::ARMSystemLinkage(self());
      }
   self()->setLinkage(lc, linkage);
   return linkage;
   }

TR::Recompilation *J9::ARM::CodeGenerator::allocateRecompilationInfo()
   {
   return TR_ARMRecompilation::allocate(TR::comp());
   }

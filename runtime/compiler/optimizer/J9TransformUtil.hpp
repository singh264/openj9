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

#ifndef J9_TRANSFORMUTIL_INCL
#define J9_TRANSFORMUTIL_INCL

/*
 * The following #define and typedef must appear before any #includes in this file
 */
#ifndef J9_TRANSFORMUTIL_CONNECTOR
#define J9_TRANSFORMUTIL_CONNECTOR
namespace J9 { class TransformUtil; }
namespace J9 { typedef J9::TransformUtil TransformUtilConnector; }
#endif

#include "optimizer/OMRTransformUtil.hpp"
#include "optimizer/Optimization.hpp"
#include "control/RecompilationInfo.hpp"

namespace TR { class Compilation; }
namespace TR { class Node; }
namespace TR { class Block; }

namespace J9
{

class OMR_EXTENSIBLE TransformUtil : public OMR::TransformUtilConnector
   {
public:
   static TR::TreeTop *generateRetranslateCallerWithPrepTrees(TR::Node *node, TR_PersistentMethodInfo::InfoBits reason, TR::Compilation *comp);
   static int32_t getLoopNestingDepth(TR::Compilation *comp, TR::Block *block);
   static bool foldFinalFieldsIn(TR_OpaqueClassBlock *clazz, const char *className, int32_t classNameLength, bool isStatic, TR::Compilation *comp);

   /**
    * \brief
    *    Determine whether to avoid folding a final instance field of an
    *    instance of a trusted JCL type.
    *
    *    This determination can depend on the particular instance, not only the
    *    field. For example, a field might be foldable only once its value is
    *    non-null, or it might be foldable in some subclasses but not others.
    *
    *    Because it may be dependent on the object's state, this determination
    *    should be made \em before reading the field in question. Otherwise
    *    there is a risk of incorrect folding:
    *    1. The compiler reads a value from field f that should not be folded.
    *    2. A concurrent modification changes the value of f and puts the
    *       object into a state that allows f to be folded.
    *    3. avoidFoldingInstanceField() returns false.
    *    4. The compiler incorrectly folds the value from step 1 instead of the
    *       new value from step 2.
    *
    *    This method requires the caller to hold VM access.
    *
    * \parm object
    *    The address of the Java object to load from
    *
    * \parm field
    *    The shadow symbol of the final field to load from \p object
    *
    * \parm cpIndex
    *    The field reference CP index (-1 for fabricated references)
    *
    * \parm owningMethod
    *    The owning method of the field reference
    *
    * \parm comp
    *    The compilation object
    *
    * \return
    *    True if folding must be avoided, false if folding is allowed.
    */
   static bool avoidFoldingInstanceField(
      uintptr_t object,
      TR::Symbol *field,
      uint32_t fieldOffset,
      int cpIndex,
      TR_ResolvedMethod *owningMethod,
      TR::Compilation *comp);

   static bool avoidFoldingInstanceField(
      uintptr_t object,
      TR::SymbolReference *symRef,
      TR::Compilation *comp);

   static TR::Node *generateArrayElementShiftAmountTrees(
         TR::Compilation *comp,
         TR::Node *object);

   static bool transformDirectLoad(TR::Compilation *, TR::Node *node);

   /**
    * \brief
    *    Fold direct load of a reliable static final field. A reliable static final field
    *    is a field on which modification after initialization is not expected because it's
    *    initial value critical to VM functionality and performance.
    *
    *    See J9::TransformUtil::canFoldStaticFinalField for the list of reliable static final
    *    field.
    *
    * \parm comp
    *    The compilation object
    *
    * \parm node
    *    The node which is a direct load
    *
    * \return
    *    True if the field has been folded
    */

   static bool foldReliableStaticFinalField(TR::Compilation *, TR::Node *node);
   /**
    * \brief
    *    Fold direct load of a static final field assuming there is protection for the folding.
    *    This will fold both the reliable ones as well as those we have no prior knowledge about.
    *    Folding of the latter requires protection in order to get out of the jitted body when
    *    the field gets modified. The onus is on the caller to ensure such protection is added.
    *
    * \parm comp
    *    The compilation object
    *
    * \parm node
    *    The node which is a direct load
    *
    * \return
    *    True if the field has been folded
    */
   static bool foldStaticFinalFieldAssumingProtection(TR::Compilation *, TR::Node *node);

   /** \brief
    *     Try to fold var handle static final field with protection
    *
    *  \param opt
    *     The current optimization object.
    *
    *  \param currentTree
    *     The tree with the load of static final field.
    *
    *  \param node
    *     The node which is a load of a static final field.
    */
   static bool attemptVarHandleStaticFinalFieldFolding(TR::Optimization* opt, TR::TreeTop * currentTree, TR::Node *node);

   /** \brief
    *     Try to fold generic static final field with protection
    *
    *  \param opt
    *     The current optimization object.
    *
    *  \param currentTree
    *     The tree with the load of static final field.
    *
    *  \param node
    *     The node which is a load of a static final field.
    */
   static bool attemptGenericStaticFinalFieldFolding(TR::Optimization* opt, TR::TreeTop * currentTree, TR::Node *node);

   /**
    * \brief
    *    Answers if a static final field can be folded
    *
    * \parm comp
    *    The compilation object
    *
    * \parm node
    *    The node which is a load direct of static final field
    *
    * \return
    *    TR_yes    If the final field is reliable, then we can trust it. Modifying such field
    *              after initialization is not expected and is very likely to cause VM
    *              functional or performance issue.
    *    TR_no     If the final field is known to change, e.g. fields in java/lang/System
    *    TR_maybe  If we don't have any prior knowledge about this field, it can be folded
    *              with guard
    */
   static TR_YesNoMaybe canFoldStaticFinalField(TR::Compilation *comp, TR::Node *node);

   static bool transformIndirectLoadChain(TR::Compilation *, TR::Node *node, TR::Node *baseExpression, TR::KnownObjectTable::Index baseKnownObject, TR::Node **removedNode);
   static bool transformIndirectLoadChainAt(TR::Compilation *, TR::Node *node, TR::Node *baseExpression, uintptr_t *baseReferenceLocation, TR::Node **removedNode);
   static bool transformIndirectLoadChainImpl( TR::Compilation *, TR::Node *node, TR::Node *baseExpression, void *baseAddress, int32_t baseStableArrayRank, TR::Node **removedNode);

   static bool fieldShouldBeCompressed(TR::Node *node, TR::Compilation *comp);

   static TR::Block *insertNewFirstBlockForCompilation(TR::Compilation *comp);
   /**
    * \brief
    *    Calculate offset for an array element given its index based on `type`
    *
    * \param index
    *    The index into the array
    *
    * \param type
    *    The data type of the array element
    *
    * \return The Int64 value representing the offset into an array of the specified type given the index
    */
   static TR::Node *calculateOffsetFromIndexInContiguousArray(TR::Compilation *, TR::Node * index, TR::DataType type);
   /**
    * \brief
    *    Calculate offset for an array element given its index based on `elementStride`
    *
    * \param index
    *    The index into the array
    *
    * \param elementStride
    *    The size of the array element
    *
    * \return The Int64 value representing the offset into an array of the specified element size given the index
    */
   static TR::Node *calculateOffsetFromIndexInContiguousArrayWithElementStride(TR::Compilation *, TR::Node * index, int32_t elementStride);
   static TR::Node *calculateElementAddress(TR::Compilation *, TR::Node *array, TR::Node * index, TR::DataType type);
   static TR::Node *calculateElementAddressWithElementStride(TR::Compilation *, TR::Node *array, TR::Node * index, int32_t elementStride);
   static TR::Node *calculateIndexFromOffsetInContiguousArray(TR::Compilation *, TR::Node * offset, TR::DataType type);

   static TR::Node* saveNodeToTempSlot(TR::Compilation* comp, TR::Node* node, TR::TreeTop* insertTreeTop);
   static void createTempsForCall(TR::Optimization* opt, TR::TreeTop *callTree);
   static void createDiamondForCall(TR::Optimization* opt, TR::TreeTop *callTree, TR::TreeTop *compareTree, TR::TreeTop *ifTree, TR::TreeTop *elseTree, bool changeBlockExtensions = false, bool markCold = false);

   static void prohibitOSROverRange(TR::Compilation* comp, TR::TreeTop* start, TR::TreeTop* end);
   static void removePotentialOSRPointHelperCalls(TR::Compilation* comp, TR::TreeTop* start, TR::TreeTop* end);
   /**
    *   \brief
    *       Move NULLCHK to a separate tree
    *
    *   \param comp  The compilation Object
    *   \param tree  The tree containing the null check
    *   \param trace Bool to enable tracing
    */
   static void separateNullCheck(TR::Compilation* comp, TR::TreeTop* tree, bool trace = false);
   /*
    * \brief
    *    Generate a tree to report modification to a static final field after initialization.
    *
    * \param node The java/lang/Class object
    *
    * \return Tree with the call to `jitReportFinalFieldModified`
    */
   static TR::TreeTop* generateReportFinalFieldModificationCallTree(TR::Compilation *comp, TR::Node *node);

   /*
    * \brief
    *    Truncate boolean for Unsafe get/put APIs.
    *    The boolean loaded or written by Unsafe APIs can only have value zero or one. The spec rules on
    *    Unsafe behavior regarding boolean is to check `(0 != value)`.
    *
    *   \param comp  The compilation Object
    *   \param tree  The tree containing Unsafe call that reads/writes a boolean
    *
    */
   static void truncateBooleanForUnsafeGetPut(TR::Compilation *comp, TR::TreeTop* tree);

   /*
    * \brief
    *    Speclize `MethodHandle.invokeExact` with a known receiver handle to a thunk archetype specimen.
    *    This will allow inliner inline the call.
    *
    *   \param comp  The compilation Object
    *   \param callNode The node with `MethodHandle.invokeExact` call
    *   \param methodHandleLocation A pointer to the MethodHandle object reference
    *
    *   \return True if specialization succeeds, false otherwise
    */
   static bool specializeInvokeExactSymbol(TR::Compilation *comp, TR::Node *callNode, uintptr_t *methodHandleLocation);

   /*
    * \brief
    *    Refine `MethodHandle.invokeBasic` with a known receiver handle
    */
   static bool refineMethodHandleInvokeBasic(TR::Compilation* comp, TR::TreeTop* treetop, TR::Node* node, TR::KnownObjectTable::Index mhIndex, bool trace = false);
   /*
    * \brief
    *    Refine `MethodHandle.linkTo*` with a known MemberName argument (the last argument)
    *    Doesn't support `MethodHandle.linkToInterface` right now
    */
   static bool refineMethodHandleLinkTo(TR::Compilation* comp, TR::TreeTop* treetop, TR::Node* node, TR::KnownObjectTable::Index mnIndex, bool trace = false);

   /*
    * \brief
    *    Determine the known-object index to use for a reference-typed final
    *    static field that is foldable in the walker.
    *
    *    The caller must check in advance that the field is a final reference.
    *    This method does not verify.
    *
    * \param comp the compilation object
    * \param owningMethod the owning method of the static field reference
    * \param cpIndex the constant pool index of the static field reference
    * \param dataAddress The static field address from \c staticAttributes()
    *
    * \return the known object index, or \c UNKNOWN if disallowed
    */
   static TR::KnownObjectTable::Index knownObjectFromFinalStatic(
      TR::Compilation *comp,
      TR_ResolvedMethod *owningMethod,
      int32_t cpIndex,
      void *dataAddress);

protected:
   /**
    * \brief
    *    Fold a load of static final field to a constant or improve its symbol reference
    *
    * \parm comp
    *    The compilation object
    *
    * \parm node
    *    The node which is a load direct of static final field
    *
    * \return
    *    True if the field is folded
    */
   static bool foldStaticFinalFieldImpl(TR::Compilation *, TR::Node *node);

   /** \brief
    *     Try to fold static final field with protection
    *
    *  \param opt
    *     The current optimization object.
    *
    *  \param currentTree
    *     The tree with the load of static final field.
    *
    *  \param node
    *     The node which is a load of a static final field.
    *
    *  \param varHandleOnly
    *     True if only folding varHandle static final fields.
    *     Faslse if folding all static final fileds.
    */
   static bool attemptStaticFinalFieldFoldingImpl(TR::Optimization* opt, TR::TreeTop * currentTree, TR::Node *node, bool varHandleOnly);
   };

}

#endif

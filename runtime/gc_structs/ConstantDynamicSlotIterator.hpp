
/*******************************************************************************
 * Copyright IBM Corp. and others 1991
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

/**
 * @file
 * @ingroup GC_Structs
 */

#if !defined(CONSTANTDYNAMICSLOTITERATOR_HPP_)
#define CONSTANTDYNAMICSLOTITERATOR_HPP_

#include "j9.h"
#include "ModronAssertions.h"


/**
 * Iterate over a constant dynamic object reference within the constant pool of a class.
 * 
 * @see GC_ConstantPoolClassSlotIterator
 * @ingroup GC_Structs
 */
class GC_ConstantDynamicSlotIterator {
private:
	typedef enum {
		condy_slot_value,
		condy_slot_exception,
		condy_slot_terminator
	} CondySlotState;
	CondySlotState _condySlotState;
    
public:
	GC_ConstantDynamicSlotIterator() :
		_condySlotState(condy_slot_value)
	{ };

	j9object_t *nextSlot(j9object_t *slotPtr);
};
#endif /* CONSTANTDYNAMICSLOTITERATOR_HPP_ */

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
 * @ingroup GC_Check
 */

#if !defined(CHECKUNFINALIZEDLIST_HPP_)
#define CHECKUNFINALIZEDLIST_HPP_

#include "j9.h"
#include "j9cfg.h"

#if defined(J9VM_GC_FINALIZATION)

#include "Check.hpp"

/**
 * 
 */
class GC_CheckUnfinalizedList : public GC_Check
{
private:
	virtual void check(); /**< run the check */
	virtual void print(); /**< dump the check structure to tty */

public:
	static GC_Check *newInstance(J9JavaVM *javaVM, GC_CheckEngine *engine);
	virtual void kill();

	virtual const char *getCheckName() { return "UNFINALIZED"; };

	GC_CheckUnfinalizedList(J9JavaVM *javaVM, GC_CheckEngine *engine) :
		GC_Check(javaVM, engine)
	{}
};

#endif /* J9VM_GC_FINALIZATION */

#endif /* CHECKUNFINALIZEDLIST_HPP_ */

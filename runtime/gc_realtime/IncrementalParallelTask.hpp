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
 * @ingroup GC_Realtime
 */

#if !defined(INCREMENTAL_PARALLEL_TASK_HPP_)
#define INCREMENTAL_PARALLEL_TASK_HPP_

#include "omr.h"
#include "omrcfg.h"
#include "omrthread.h"

#include "ParallelTask.hpp"
#include "YieldCollaborator.hpp"

/**
 * @todo Provide class documentation
 * @ingroup GC_Realtime
 */
class MM_IncrementalParallelTask : public MM_ParallelTask
{
private:
	uintptr_t _entryCount;
	MM_YieldCollaborator _yieldCollaborator;

protected:
	virtual void synchronizeGCThreads(MM_EnvironmentBase *env, const char *id);
	virtual bool synchronizeGCThreadsAndReleaseMain(MM_EnvironmentBase *env, const char *id);
	virtual bool synchronizeGCThreadsAndReleaseSingleThread(MM_EnvironmentBase *env, const char *id);
	virtual void releaseSynchronizedGCThreads(MM_EnvironmentBase *env);
public:
	
	
	MM_YieldCollaborator *getYieldCollaborator() { return &_yieldCollaborator; }	
	
	MM_IncrementalParallelTask(MM_EnvironmentBase *env, MM_ParallelDispatcher *dispatcher) :
		MM_ParallelTask(env, dispatcher),
		_entryCount(0),
		_yieldCollaborator(&_synchronizeMutex, &_synchronizeCount, MM_YieldCollaborator::IncrementalParallelTask)
	{
		_typeId = __FUNCTION__;
	};
};

#endif /* INCREMENTAL_PARALLEL_TASK_HPP_ */

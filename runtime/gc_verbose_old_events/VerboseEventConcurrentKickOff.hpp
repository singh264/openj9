
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

#if !defined(EVENT_CON_KICKOFF_HPP_)
#define EVENT_CON_KICKOFF_HPP_

#include "j9.h"
#include "j9cfg.h"
#include "j9port.h"
#include "mmhook.h"

#include "VerboseEvent.hpp"

/**
 * Stores the data relating to the kickoff of a concurrent collection.
 * @ingroup GC_verbose_events
 */
class MM_VerboseEventConcurrentKickOff : public MM_VerboseEvent
{
private:
	/* Passed Data */
	UDATA	_nurseryFreeBytes; /**< number of bytes free in the nursery */
	UDATA	_tenureFreeBytes; /**< number of bytes free in the tenure area */
	UDATA	_traceTarget; /**< the targetted number of bytes to be concurrently traced */
	UDATA	_kickOffThreshold; /**< the number of bytes free at which concurrent gc will begin */
	UDATA	_kickOffReason; /**< the reason for kickoff */
	UDATA	_languageKickOffReason; /**< the reason for kickoff */
	
	I_64 _timeInMilliSeconds;
	
	void initialize(void);
	const char *getKickoffReasonAsString(UDATA reason, UDATA languageReason);

public:

	static MM_VerboseEvent *newInstance(MM_ConcurrentKickoffEvent *event, J9HookInterface** hookInterface);
	
	virtual void consumeEvents();
	virtual void formattedOutput(MM_VerboseOutputAgent *agent);

	MMINLINE virtual bool definesOutputRoutine() { return true; };
	MMINLINE virtual bool endsEventChain() { return true; };
		
	MM_VerboseEventConcurrentKickOff(MM_ConcurrentKickoffEvent *event, J9HookInterface** hookInterface) :
	MM_VerboseEvent(event->currentThread, event->timestamp, event->eventid, hookInterface),
	_nurseryFreeBytes(event->commonData->nurseryFreeBytes),
	_tenureFreeBytes(event->commonData->tenureFreeBytes),
	_traceTarget(event->traceTarget),
	_kickOffThreshold(event->kickOffThreshold),
	_kickOffReason(event->reason),
	_languageKickOffReason(event->languageReason)
	{};
};

#endif /* EVENT_CON_KICKOFF_HPP_ */

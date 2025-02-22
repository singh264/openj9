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

set(gc_realtime_sources
	AllocationContextRealtime.cpp
	ConfigurationRealtime.cpp
	ContinuationObjectBufferRealtime.cpp
	EnvironmentRealtime.cpp
	GlobalAllocationManagerRealtime.cpp
	HeapRegionDescriptorRealtime.cpp
	IncrementalOverflow.cpp
	IncrementalParallelTask.cpp
	MemorySubSpaceMetronome.cpp
	MetronomeAlarm.cpp
	MetronomeAlarmThread.cpp
	OSInterface.cpp
	OwnableSynchronizerObjectBufferRealtime.cpp
	ProcessorInfo.cpp
	RealtimeAccessBarrier.cpp
	RealtimeGC.cpp
	RealtimeMarkingScheme.cpp
	RealtimeMarkTask.cpp
	RealtimeRootScanner.cpp
	RealtimeSweepTask.cpp
	ReferenceObjectBufferRealtime.cpp
	Scheduler.cpp
	SweepSchemeRealtime.cpp
	Timer.cpp
	UnfinalizedObjectBufferRealtime.cpp
	UtilizationTracker.cpp
	WorkPacketsRealtime.cpp
	YieldCollaborator.cpp
)

j9vm_add_library(j9realtime STATIC
	${gc_realtime_sources}
)

target_link_libraries(j9realtime
	PRIVATE
		j9vm_interface

		omrgc
		j9gcbase
)

if(OMR_OS_WINDOWS)
	target_link_libraries(j9realtime PRIVATE winmm.lib)
endif()

if(OMR_MIXED_REFERENCES_MODE_STATIC)
	j9vm_add_library(j9realtime_full STATIC
		${gc_realtime_sources}
	)

	target_link_libraries(j9realtime_full
		PRIVATE
			j9vm_interface

			omrgc_full
			j9gcbase_full
	)

	if(OMR_OS_WINDOWS)
		target_link_libraries(j9realtime_full PRIVATE winmm.lib)
	endif()
endif()

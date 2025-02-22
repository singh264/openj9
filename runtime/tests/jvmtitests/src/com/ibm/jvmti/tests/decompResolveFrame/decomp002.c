/*******************************************************************************
 * Copyright IBM Corp. and others 2001
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

#include "jvmti_test.h"
#include <string.h>


static agentEnv * env;
 
static int steps = 0; 

#define STEP_COUNT 10000000
 
static void JNICALL
cbSingleStep(jvmtiEnv *jvmti_env, JNIEnv* jni_env, jthread thread, jmethodID method, jlocation location)
{
	steps++;
}


jint JNICALL
decomp002(agentEnv * _env, char * args)
{
	JVMTI_ACCESS_FROM_AGENT(_env);
	jvmtiError err;
	jvmtiEventCallbacks callbacks;
	jvmtiCapabilities capabilities;

	env = _env;

	if (!ensureVersion(env, JVMTI_VERSION_1_1)) {
		return JNI_ERR;
	}


	memset(&capabilities, 0, sizeof(jvmtiCapabilities));
	capabilities.can_generate_single_step_events = 1;
	err = (*jvmti_env)->AddCapabilities(jvmti_env, &capabilities);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Failed to add capabilities");
		return JNI_ERR;
	}	
	
	memset(&callbacks, 0, sizeof(jvmtiEventCallbacks));
	callbacks.SingleStep = cbSingleStep;
	err = (*jvmti_env)->SetEventCallbacks(jvmti_env, &callbacks, sizeof(jvmtiEventCallbacks));
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Failed to set callback for SingleStep events");
		return JNI_ERR;
	}	

	return JNI_OK;
}



jboolean JNICALL
Java_com_ibm_jvmti_tests_decompResolveFrame_decomp002_singleStep(JNIEnv *jni_env, jclass cls)
{
	JVMTI_ACCESS_FROM_AGENT(env);
	jvmtiError err;
	int decompileCount = 0;

	printf("\tStep %d times\n", STEP_COUNT);  
		
	while (1) {

		err = (*jvmti_env)->SetEventNotificationMode(jvmti_env, JVMTI_ENABLE, JVMTI_EVENT_SINGLE_STEP, NULL);
		if ( err != JVMTI_ERROR_NONE ) {
			error(env, err, "Enable SetEventNotificationMode single step failed");
			return JNI_FALSE;
		}
		
        jvmtitest_usleep(100);
 
		err = (*jvmti_env)->SetEventNotificationMode(jvmti_env, JVMTI_DISABLE, JVMTI_EVENT_SINGLE_STEP, NULL);
		if ( err != JVMTI_ERROR_NONE ) {
			error(env, err, "Disable SetEventNotificationMode single step failed");
			return JNI_FALSE;
		}
 		
 		if (steps > STEP_COUNT) {
 			break;
 		}
 		
 		decompileCount++;
 				
		jvmtitest_usleep(100);
	}

	printf("\tDecompile cycles: %d\n", decompileCount);

	return JNI_TRUE;
}


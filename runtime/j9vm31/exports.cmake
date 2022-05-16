################################################################################
# Copyright (c) 2021, 2022 IBM Corp. and others
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
# [2] http://openjdk.java.net/legal/assembly-exception.html
#
# SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
################################################################################

omr_add_exports(jvm31
	JNI_CreateJavaVM
	JNI_GetCreatedJavaVMs
	JNI_GetDefaultJavaVMInitArgs
	DestroyJavaVM
	AttachCurrentThread
	DetachCurrentThread
	GetEnv
	AttachCurrentThreadAsDaemon
	GetVersion
	DefineClass
	FindClass
	FromReflectedMethod
	FromReflectedField
	ToReflectedMethod
	GetSuperclass
	IsAssignableFrom
	ToReflectedField
	Throw
	ThrowNew
	ExceptionOccurred
	ExceptionDescribe
	ExceptionClear
	FatalError
	PushLocalFrame
	PopLocalFrame
	NewGlobalRef
	DeleteGlobalRef
	DeleteLocalRef
	IsSameObject
	NewLocalRef
	EnsureLocalCapacity
	AllocObject
	NewObject
	NewObjectV
	NewObjectA
	GetObjectClass
	IsInstanceOf
	GetMethodID
	CallObjectMethod
	CallObjectMethodV
	CallObjectMethodA
	CallBooleanMethod
	CallBooleanMethodV
	CallBooleanMethodA
	CallByteMethod
	CallByteMethodV
	CallByteMethodA
	CallCharMethod
	CallCharMethodV
	CallCharMethodA
	CallShortMethod
	CallShortMethodV
	CallShortMethodA
	CallIntMethod
	CallIntMethodV
	CallIntMethodA
	CallLongMethod
	CallLongMethodV
	CallLongMethodA
	CallFloatMethod
	CallFloatMethodV
	CallFloatMethodA
	CallDoubleMethod
	CallDoubleMethodV
	CallDoubleMethodA
	CallVoidMethod
	CallVoidMethodV
	CallVoidMethodA
	CallNonvirtualObjectMethod
	CallNonvirtualObjectMethodV
	CallNonvirtualObjectMethodA
	CallNonvirtualBooleanMethod
	CallNonvirtualBooleanMethodV
	CallNonvirtualBooleanMethodA
	CallNonvirtualByteMethod
	CallNonvirtualByteMethodV
	CallNonvirtualByteMethodA
	CallNonvirtualCharMethod
	CallNonvirtualCharMethodV
	CallNonvirtualCharMethodA
	CallNonvirtualShortMethod
	CallNonvirtualShortMethodV
	CallNonvirtualShortMethodA
	CallNonvirtualIntMethod
	CallNonvirtualIntMethodV
	CallNonvirtualIntMethodA
	CallNonvirtualLongMethod
	CallNonvirtualLongMethodV
	CallNonvirtualLongMethodA
	CallNonvirtualFloatMethod
	CallNonvirtualFloatMethodV
	CallNonvirtualFloatMethodA
	CallNonvirtualDoubleMethod
	CallNonvirtualDoubleMethodV
	CallNonvirtualDoubleMethodA
	CallNonvirtualVoidMethod
	CallNonvirtualVoidMethodV
	CallNonvirtualVoidMethodA
	GetFieldID
	GetObjectField
	GetBooleanField
	GetByteField
	GetCharField
	GetShortField
	GetIntField
	GetLongField
	GetFloatField
	GetDoubleField
	SetObjectField
	SetBooleanField
	SetByteField
	SetCharField
	SetShortField
	SetIntField
	SetLongField
	SetFloatField
	SetDoubleField
	GetStaticMethodID
	CallStaticObjectMethod
	CallStaticObjectMethodV
	CallStaticObjectMethodA
	CallStaticBooleanMethod
	CallStaticBooleanMethodV
	CallStaticBooleanMethodA
	CallStaticByteMethod
	CallStaticByteMethodV
	CallStaticByteMethodA
	CallStaticCharMethod
	CallStaticCharMethodV
	CallStaticCharMethodA
	CallStaticShortMethod
	CallStaticShortMethodV
	CallStaticShortMethodA
	CallStaticIntMethod
	CallStaticIntMethodV
	CallStaticIntMethodA
	CallStaticLongMethod
	CallStaticLongMethodV
	CallStaticLongMethodA
	CallStaticFloatMethod
	CallStaticFloatMethodV
	CallStaticFloatMethodA
	CallStaticDoubleMethod
	CallStaticDoubleMethodV
	CallStaticDoubleMethodA
	CallStaticVoidMethod
	CallStaticVoidMethodV
	CallStaticVoidMethodA
	GetStaticFieldID
	GetStaticObjectField
	GetStaticBooleanField
	GetStaticByteField
	GetStaticCharField
	GetStaticShortField
	GetStaticIntField
	GetStaticLongField
	GetStaticFloatField
	GetStaticDoubleField
	SetStaticObjectField
	SetStaticBooleanField
	SetStaticByteField
	SetStaticCharField
	SetStaticShortField
	SetStaticIntField
	SetStaticLongField
	SetStaticFloatField
	SetStaticDoubleField
	NewString
	GetStringLength
	GetStringChars
	ReleaseStringChars
	NewStringUTF
	GetStringUTFLength
	GetStringUTFChars
	ReleaseStringUTFChars
	GetArrayLength
	NewObjectArray
	GetObjectArrayElement
	SetObjectArrayElement
	NewBooleanArray
	NewByteArray
	NewCharArray
	NewShortArray
	NewIntArray
	NewLongArray
	NewFloatArray
	NewDoubleArray
	GetBooleanArrayElements
	GetByteArrayElements
	GetCharArrayElements
	GetShortArrayElements
	GetIntArrayElements
	GetLongArrayElements
	GetFloatArrayElements
	GetDoubleArrayElements
	ReleaseBooleanArrayElements
	ReleaseByteArrayElements
	ReleaseCharArrayElements
	ReleaseShortArrayElements
	ReleaseIntArrayElements
	ReleaseLongArrayElements
	ReleaseFloatArrayElements
	ReleaseDoubleArrayElements
	GetBooleanArrayRegion
	GetByteArrayRegion
	GetCharArrayRegion
	GetShortArrayRegion
	GetIntArrayRegion
	GetLongArrayRegion
	GetFloatArrayRegion
	GetDoubleArrayRegion
	SetBooleanArrayRegion
	SetByteArrayRegion
	SetCharArrayRegion
	SetShortArrayRegion
	SetIntArrayRegion
	SetLongArrayRegion
	SetFloatArrayRegion
	SetDoubleArrayRegion
	RegisterNatives
	UnregisterNatives
	MonitorEnter
	MonitorExit
	GetJavaVM
	GetStringRegion
	GetStringUTFRegion
	GetPrimitiveArrayCritical
	ReleasePrimitiveArrayCritical
	GetStringCritical
	ReleaseStringCritical
	NewWeakGlobalRef
	DeleteWeakGlobalRef
	ExceptionCheck
	NewDirectByteBuffer
	GetDirectBufferAddress
	GetDirectBufferCapacity
	GetObjectRefType
)

if(NOT JAVA_SPEC_VERSION LESS 11)
	omr_add_exports(jvm31 GetModule)
endif()

if(NOT JAVA_SPEC_VERSION LESS 19)
	omr_add_exports(jvm31 IsVirtualThread)
endif()

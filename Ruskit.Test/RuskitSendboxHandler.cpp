#include "io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler.h"
#include <iostream>
#include <string>
#include <cstdlib>

using namespace std;

/*
* Class:     io_github_ruskonert_ruskit_test_RuskitSendboxHandler
* Method:    Test0
* Signature: (Ljava/lang/String;)V
*/
JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_test_RuskitSendboxHandler_Test0
(JNIEnv *env, jclass java_class, jstring java_string)
{
	const char *nativeString = env->GetStringUTFChars(java_string, JNI_FALSE);
	cout << "JNI Executed: \"" << nativeString << "\", REF -> (0x" << java_string << ")" << endl;
}

/*
* Class:     io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler
* Method:    ConsoleClear0
* Signature: ()V
*/
JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler_ConsoleClear0(JNIEnv *env, jclass java_class)
{
	system("cls");
}

/*
* Class:     io_github_ruskonert_ruskit_test_RuskitSendboxHandler
* Method:    Test1
* Signature: (I)V
*/
JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_test_RuskitSendboxHandler_Test1
(JNIEnv *env, jclass java_class, jint java_int)
{
	return;
}

/*
* Class:     io_github_ruskonert_ruskit_test_RuskitSendboxHandler
* Method:    Test2
* Signature: ([B)V
*/
JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_test_RuskitSendboxHandler_Test2
(JNIEnv *env, jclass java_class, jbyteArray Java_byte_arr)
{
	return;
}
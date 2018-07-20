#include <iostream>
#include <string>
#include <cstdlib>
#include <fstream>

#include <windows.h>

#include "io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler.h"

#pragma comment(lib, "Winmm.lib")
using namespace std;

JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler_Test0(JNIEnv *env, jclass java_class, jstring java_string)
{
	const char *nativeString = env->GetStringUTFChars(java_string, JNI_FALSE);
	cout << "JNI Executed: \"" << nativeString << "\", REF -> (0x" << java_string << ")" << endl;
}

JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler_ConsoleClear0(JNIEnv *env, jclass java_class)
{
	system("cls");
}


JNIEXPORT void JNICALL Java_io_github_ruskonert_ruskit_sendbox_RuskitSendboxHandler_PlaySoundA(JNIEnv *env, jclass java_class, jbyteArray java_byte_arr)
{
	const char* file_array = as_unsigned_char_array(env, java_byte_arr);
	std::ofstream file("myfile.bin", std::ios::binary);
	file.write(file_array, strnlen(file_array, 0xFFFF));
    //PlaySoundW()
}

char* as_unsigned_char_array(JNIEnv *env, jbyteArray array)
{
	int len = env->GetArrayLength(array);
	char* buf = new char[len];
	env->GetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte*>(buf));
	return buf;
}
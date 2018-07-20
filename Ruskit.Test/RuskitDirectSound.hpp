#pragma once
#pragma comment(lib, "dsound.lib")
#pragma comment(lib, "dxguid.lib")
#pragma comment(lib, "winmm.lib")

#include <windows.h>
#include <mmsystem.h>
#include <dsound.h>
#include <iostream>
#include <string>

namespace Ruskit
{
	class SoundFramework
	{
	private:
		WaveHeaderType waveHeaderType;
		IDirectSound8* m_DirectSound;
		IDirectSoundBuffer* m_primaryBuffer;
		IDirectSoundBuffer8* m_primaryBuffer8;

		bool InitializeDirectSound(HWND);
		void ShutdownDirectSound();

		bool LoadWave(std::string*, IDirectSound3DBuffer8**);
		void ShutdownWave(IDirectSound3DBuffer8**);

		bool Play();

	public:
		SoundFramework();
		SoundFramework(const SoundFramework&);
		~SoundFramework();
		bool Initialize(HWND);
		void Shutdown();
	};

	class WaveHeaderType
	{
		char chunkId[4];
		unsigned long chunkSize;
		char format[4];
		char subChunkId[4];
		unsigned long subChunkSize;
		unsigned short audioFormat;
		unsigned short numChannels;
		unsigned long sampleRate;
		unsigned long bytesPerSecond;
		unsigned short blockAlign;
		unsigned short bitsPerSample;
		char dataChunkId[4];
		unsigned long dataSize;
	};
}
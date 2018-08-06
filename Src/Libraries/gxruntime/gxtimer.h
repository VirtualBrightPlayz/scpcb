
#ifndef GXTIMER_H
#define GXTIMER_H

#include <Windows.h>

class gxRuntime;

class gxTimer{
public:
	gxTimer( gxRuntime *rt,int hertz );
	~gxTimer();

#ifdef _WIN64
	static void _cdecl timerCallback( UINT id,UINT msg,DWORD_PTR user,DWORD_PTR dw1,DWORD_PTR dw2 );
#else
    static void CALLBACK timerCallback(UINT id, UINT msg, DWORD_PTR user, DWORD_PTR dw1, DWORD_PTR dw2);
#endif

private:
	gxRuntime *runtime;
	HANDLE event;
	MMRESULT timerID;
	int ticks_put,ticks_get;

	/***** GX INTERFACE *****/
public:
	int wait();
};

#endif
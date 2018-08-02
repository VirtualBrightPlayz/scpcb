
/*

Platform neutral runtime library.

To be statically linked with an appropriate gxruntime driver.

*/

#ifndef BBRUNTIME_H
#define BBRUNTIME_H

#include "../gxruntime/gxruntime.h"

bool bbruntime_create(HINSTANCE hinst);

bool bbruntime_destroy();

void bbDelay(int ms);

void bbruntime_panic( const char *err );

void bbEnd();
void bbStop();
void bbRuntimeError(class BBStr* str);
int bbExecFile(class BBStr* f);
void bbDelay(int ms);
int bbMilliSecs();
class BBStr* bbCommandLine();
class BBStr* bbSystemProperty(class BBStr* p);
class gxTimer* bbCreateTimer(int hertz);
int bbWaitTimer(class gxTimer* t);
void bbFreeTimer(class gxTimer* t);
void bbDebugLog(class BBStr* t);

#endif

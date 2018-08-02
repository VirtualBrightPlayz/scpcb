
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

void bbAppTitle(struct BBStr *ti, struct BBStr *cp);
void bbEnd();
void bbStop();
void bbRuntimeError(struct BBStr* str);
int bbExecFile(struct BBStr* f);
void bbDelay(int ms);
int bbMilliSecs();
struct BBStr* bbCommandLine();
struct BBStr* bbSystemProperty(struct BBStr* p);
class gxTimer* bbCreateTimer(int hertz);
int bbWaitTimer(class gxTimer* t);
void bbFreeTimer(class gxTimer* t);
void bbDebugLog(struct BBStr* t);

#endif

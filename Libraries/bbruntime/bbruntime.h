
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

void bbAppTitle(const String& ti, const String& cp="");
void bbEnd();
void bbStop();
void bbRuntimeError(const String& str);
int bbExecFile(const String& f);
int bbMilliSecs();
String bbCommandLine();
String bbSystemProperty(const String& p);
class gxTimer* bbCreateTimer(int hertz);
int bbWaitTimer(class gxTimer* t);
void bbFreeTimer(class gxTimer* t);

#endif

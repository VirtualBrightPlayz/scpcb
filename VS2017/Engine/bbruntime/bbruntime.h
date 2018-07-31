
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

#endif

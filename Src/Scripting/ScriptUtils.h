#ifndef SCRIPT_UTILS_H_INCLUDED
#define SCRIPT_UTILS_H_INCLUDED

#include <angelscript.h>

#ifdef _WIN32
#define asVIRTUAL_METHOD(c,m) asMETHOD(c,m)
#else
#define asVIRTUAL_METHOD(c,m) asSMethodPtr<sizeof(&c::m)>::Convert((void*)(&c::m))
#endif

#endif // SCRIPT_UTILS_H_INCLUDED
#ifndef SCRIPT_UTILS_H_INCLUDED
#define SCRIPT_UTILS_H_INCLUDED

#define asVIRTUAL_METHOD(c,m) asSMethodPtr<sizeof(&c::m)>::Convert((void*)(&c::m))

#endif // SCRIPT_UTILS_H_INCLUDED
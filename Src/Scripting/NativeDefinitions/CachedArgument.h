#ifndef CACHEDARGUMENT_H_INCLUDED
#define CACHEDARGUMENT_H_INCLUDED

#include <Misc/String.h>
#include "../Type.h"

struct CachedArgument {
    PGE::String name;
    Type* type;
    union Value {
        Value();
        int32_t i32;
        uint32_t u32;
        float f;
        double d;
        void* ptr;
    } value;
    CachedArgument() { }
    CachedArgument(const PGE::String& nm, Type* t);
};

#endif

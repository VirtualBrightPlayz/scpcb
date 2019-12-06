#ifndef TYPE_H_INCLUDED
#define TYPE_H_INCLUDED

#include <Misc/String.h>

enum class Type {
    Int32,
    UInt32,
    Float,
    Double,
    String
};

extern PGE::String typeToString(Type type);

#endif

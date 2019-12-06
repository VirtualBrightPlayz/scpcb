#include "Type.h"

PGE::String typeToString(Type type) {
    switch (type) {
        case Type::Int32: {
            return "int32";
        } break;
        case Type::UInt32: {
            return "uint32";
        } break;
        case Type::Float: {
            return "float";
        } break;
        case Type::Double: {
            return "double";
        } break;
        case Type::String: {
            return "string";
        } break;
    }

    return "<unknown>";
}

#include "Type.h"

const Type Type::Int32 = Type("int32");
const Type Type::UInt32 = Type("uint32");
const Type Type::Float = Type("float");
const Type Type::Double = Type("double");
const Type Type::String = Type("string");

Type::Type() {
    typeName = "<unknown>";
}

Type::Type(const PGE::String& name) {
    typeName = name;
}

Type::~Type() {}

bool Type::equals(const Type& other) const {
    return !typeName.equals("<unknown>") && other.toString().equals(typeName);
}

PGE::String Type::toString() const {
    return typeName;
}


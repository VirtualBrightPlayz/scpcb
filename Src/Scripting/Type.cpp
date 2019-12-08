#include "Type.h"

const Type Type::int32Private = Type("int32");
const Type Type::uint32Private = Type("uint32");
const Type Type::floatPrivate = Type("float");
const Type Type::doublePrivate = Type("double");
const Type Type::stringPrivate = Type("string");
const Type Type::voidPrivate = Type("void");

const Type* const Type::Int32 = &Type::int32Private;
const Type* const Type::UInt32 = &Type::uint32Private;
const Type* const Type::Float = &Type::floatPrivate;
const Type* const Type::Double = &Type::doublePrivate;
const Type* const Type::String = &Type::stringPrivate;
const Type* const Type::Void = &Type::voidPrivate;

Type::Type() {
    typeName = "<unknown>";
}

Type::Type(const PGE::String& name) {
    typeName = name;
    refType = new RefType(this);
}

Type::~Type() {}

PGE::String Type::getName() const {
    return typeName;
}

const RefType* Type::asRef() const {
    return refType;
}

RefType::RefType(const Type* type) {
    baseType = type;
    refType = nullptr;
}

PGE::String RefType::getName() const {
    return PGE::String(baseType->getName(), "@");
}

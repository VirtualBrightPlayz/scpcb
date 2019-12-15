#include "Type.h"

Type Type::int32Private = Type("int32");
Type Type::uint32Private = Type("uint32");
Type Type::floatPrivate = Type("float");
Type Type::doublePrivate = Type("double");
Type Type::stringPrivate = Type("string");
Type Type::voidPrivate = Type("void");
Type Type::unspecifiedArrayPrivate = Type("array<?>");

Type* const Type::Int32 = &Type::int32Private;
Type* const Type::UInt32 = &Type::uint32Private;
Type* const Type::Float = &Type::floatPrivate;
Type* const Type::Double = &Type::doublePrivate;
Type* const Type::String = &Type::stringPrivate;
Type* const Type::Void = &Type::voidPrivate;
Type* const Type::UnspecifiedArray = &Type::unspecifiedArrayPrivate;

Type::Type() {
    typeName = "<unknown>";
}

Type::Type(const PGE::String& name) {
    typeName = name;
    refType = new RefType(this);
    arrayType = nullptr;
}

Type::~Type() {}

PGE::String Type::getName() const {
    return typeName;
}

bool Type::isRef() const {
    return false;
}

ArrayType* Type::getArrayType() {
    if (arrayType == nullptr) {
        arrayType = new ArrayType(this);
    }
    return arrayType;
}

RefType* Type::asRef() const {
    return refType;
}

ArrayType::ArrayType(Type* type) {
    elementType = type;
}

PGE::String ArrayType::getName() const {
    return "array<" + elementType->getName() + ">";
}

Type* ArrayType::getElementType() const {
    return elementType;
}

RefType::RefType(Type* type) {
    baseType = type;
    refType = nullptr;
}

PGE::String RefType::getName() const {
    return PGE::String(baseType->getName(), "@");
}

bool RefType::isRef() const {
    return true;
}

Type* RefType::getBaseType() const {
    return baseType;
}

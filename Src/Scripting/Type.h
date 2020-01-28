#ifndef TYPE_H_INCLUDED
#define TYPE_H_INCLUDED

#include <Misc/String.h>

class ArrayType;
class RefType;
class Type {
    private:
        static Type int32Private;
        static Type uint32Private;
        static Type floatPrivate;
        static Type doublePrivate;
        static Type stringPrivate;
        static Type voidPrivate;
        static Type unsupportedPrivate;
    protected:
        PGE::String typeName;
        RefType* refType;
        ArrayType* arrayType;
    public:
        Type();
        Type(const PGE::String& name);
        ~Type();

        virtual PGE::String getName() const;
        virtual ArrayType* getArrayType();
        virtual RefType* asRef() const;
        virtual bool isRef() const;

        static Type* const Int32;
        static Type* const UInt32;
        static Type* const Float;
        static Type* const Double;
        static Type* const String;
        static Type* const Void;
        static Type* const Unsupported;
};

class ArrayType : public Type {
    private:
        Type* elementType;
    public:
        ArrayType(Type* type);

        PGE::String getName() const override;
        Type* getElementType() const;
};

class RefType : public Type {
    private:
        Type* baseType;
    public:
        RefType(Type* type);

        PGE::String getName() const override;
        bool isRef() const override;
        Type* getBaseType() const;
};

#endif

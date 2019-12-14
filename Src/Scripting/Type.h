#ifndef TYPE_H_INCLUDED
#define TYPE_H_INCLUDED

#include <Misc/String.h>

class RefType;
class Type {
    private:
        static const Type int32Private;
        static const Type uint32Private;
        static const Type floatPrivate;
        static const Type doublePrivate;
        static const Type stringPrivate;
        static const Type voidPrivate;
    protected:
        PGE::String typeName;
        const RefType* refType;
    public:
        Type();
        Type(const PGE::String& name);
        ~Type();

        virtual PGE::String getName() const;
        virtual const RefType* asRef() const;
        virtual bool isRef() const;

        static const Type* const Int32;
        static const Type* const UInt32;
        static const Type* const Float;
        static const Type* const Double;
        static const Type* const String;
        static const Type* const Void;
};

class RefType : public Type {
    private:
        const Type* baseType;
    public:
        RefType(const Type* type);

        PGE::String getName() const override;
        bool isRef() const override;
        const Type* getBaseType() const;
};

#endif

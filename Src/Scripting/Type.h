#ifndef TYPE_H_INCLUDED
#define TYPE_H_INCLUDED

#include <Misc/String.h>

class Type {
    private:
        static const Type int32Private;
        static const Type uint32Private;
        static const Type floatPrivate;
        static const Type doublePrivate;
        static const Type stringPrivate;
    protected:
        PGE::String typeName;
    public:
        Type();
        Type(const PGE::String& name);
        ~Type();

        virtual PGE::String getName() const;

        static const Type* const Int32;
        static const Type* const UInt32;
        static const Type* const Float;
        static const Type* const Double;
        static const Type* const String;
};

#endif

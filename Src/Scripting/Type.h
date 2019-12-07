#ifndef TYPE_H_INCLUDED
#define TYPE_H_INCLUDED

#include <Misc/String.h>

class Type {
    protected:
        PGE::String typeName;
    public:
        Type();
        Type(const PGE::String& name);
        ~Type();

        virtual bool equals(const Type& other) const;
        virtual PGE::String toString() const;

        static const Type Int32;
        static const Type UInt32;
        static const Type Float;
        static const Type Double;
        static const Type String;
};

#endif

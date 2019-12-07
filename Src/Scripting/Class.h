#ifndef CLASS_H_INCLUDED
#define CLASS_H_INCLUDED

#include "Type.h"
#include "Script.h"
#include <Misc/String.h>
#include <vector>
#include <angelscript.h>

class ScriptClass : public Type {
    public:
        enum class Visibility {
            Private,
            Protected,
            Public
        };

        struct Property {
            PGE::String name;
            //Type type; //TODO: convert from typeId to Type
            int offset;
            int typeId;
            bool isReference;
            Visibility visibility;
        };
    private:
        asITypeInfo* angelScriptTypeInfo;

        std::vector<Property> properties;

    public:
        ScriptClass(Script* script, const PGE::String& className);

        const std::vector<Property>& getProperties() const;
};

#endif

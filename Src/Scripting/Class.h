#ifndef CLASS_H_INCLUDED
#define CLASS_H_INCLUDED

#include "Type.h"
#include <Misc/String.h>
#include <vector>
#include <angelscript.h>

class Script;
class ScriptFunction;
class ScriptObject;

class ScriptClass : public Type {
    public:
        enum class Visibility {
            Private,
            Protected,
            Public
        };

        class Property {
            private:
                PGE::String name;
                int offset;
                int typeId;
                const Type* type;
                bool isRef;
                Visibility visibility;
            public:
                Property(const PGE::String& n, int off, int tId, bool ref, Visibility vis);

                void determineType(Script* script);

                PGE::String getName() const;
                int getOffset() const;
                int getTypeId() const;
                bool isReference() const;
                Visibility getVisibility() const;
        };
    private:
        asITypeInfo* angelScriptTypeInfo;

        std::vector<Property> properties;
        std::vector<ScriptFunction*> methods;
        std::vector<ScriptFunction*> constructors;

        Script* script;

    public:
        ScriptClass(Script* scrpt, asITypeInfo* tInfo);

        const std::vector<Property>& getProperties() const;
        Script* getScript() const;
        int getTypeId() const;

        void finalizeInitialization();
        ScriptObject* createNewObject();
};

#endif

#ifndef CLASS_H_INCLUDED
#define CLASS_H_INCLUDED

#include "Type.h"
#include <Misc/String.h>
#include <vector>
#include <angelscript.h>

class ScriptModule;
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
                Type* type;
                bool isRef;
                Visibility visibility;
                bool isNonSerialize;
                int arrayDims;
            public:
                Property(const PGE::String& n, int off, int tId, bool ref, Visibility vis, bool isNonSerial);

                void determineType(ScriptModule* scriptModule);

                PGE::String getName() const;
                int getOffset() const;
                int getTypeId() const;
                bool isReference() const;
                Visibility getVisibility() const;
                bool isNonSerializable() const;
                int getArrayDims() const;
        };
    private:
        asITypeInfo* angelScriptTypeInfo;

        std::vector<Property> properties;
        std::vector<ScriptFunction*> methods;
        std::vector<ScriptFunction*> constructors;

        ScriptModule* scriptModule;

    public:
        ScriptClass(ScriptModule* module, asITypeInfo* tInfo);

        const std::vector<Property>& getProperties() const;
        ScriptModule* getScriptModule() const;
        int getTypeId() const;

        void finalizeInitialization();
        ScriptObject* createNewObject();
};

#endif

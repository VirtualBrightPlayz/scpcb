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
                bool isSerialize;
                int arrayDims;
            public:
                Property(const PGE::String& n, int off, int tId, bool ref, Visibility vis, bool isSerial);

                void determineType(ScriptModule* scriptModule);

                PGE::String getName() const;
                int getOffset() const;
                int getTypeId() const;
                bool isReference() const;
                Visibility getVisibility() const;
                bool isSerializable() const;
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

        bool isClassType() const override;

        const std::vector<Property>& getProperties() const;
        ScriptModule* getScriptModule() const;
        int getTypeId() const;
        int getSize() const;

        ScriptFunction* getMethod(const PGE::String& funcName) const;
        bool implements(asITypeInfo* info) const;

        void finalizeInitialization();
        ScriptObject* createNewObject();
};

#endif

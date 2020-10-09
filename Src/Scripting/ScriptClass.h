#ifndef CLASS_H_INCLUDED
#define CLASS_H_INCLUDED

#include <vector>

#include <angelscript.h>

#include <Misc/String.h>

#include "Type.h"

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
        };

    private:
        asITypeInfo* angelScriptTypeInfo;

        std::vector<Property> properties;
        std::vector<ScriptFunction*> methods;
        std::vector<ScriptFunction*> constructors;

        std::vector<ScriptClass*> derivedClasses;

        ScriptModule* scriptModule;
        ScriptClass* parentClass;

    public:
        ScriptClass(ScriptModule* module, asITypeInfo* tInfo, ScriptClass* prntClass);
        ~ScriptClass();

        bool isClassType() const override;

        const std::vector<Property>& getProperties() const;
        const std::vector<ScriptFunction*>& getConstructors() const;
        ScriptModule* getScriptModule() const;
        asITypeInfo* getAngelScriptTypeInfo() const;
        int getTypeId() const;

        void registerDerivedClass(ScriptClass* clss);
        const std::vector<ScriptClass*>& getDerivedClasses() const;

        void finalizeInitialization();
};

#endif // CLASS_H_INCLUDED

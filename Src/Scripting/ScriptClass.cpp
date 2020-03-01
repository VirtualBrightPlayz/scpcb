#include "Script.h"
#include "ScriptFunction.h"
#include "ScriptClass.h"
#include "ScriptObject.h"

ScriptClass::ScriptClass(Script* scrpt, asITypeInfo* tInfo) {
    script = scrpt;

    typeName = tInfo->GetName();
    refType = new RefType(this);

    asIScriptModule* module = script->getAngelScriptModule();

    angelScriptTypeInfo = tInfo;

    int propertyCount = angelScriptTypeInfo->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        const char* name;
        bool isPrivate;
        bool isProtected;
        bool isNonSerialize;
        int typeId;
        int offset;
        bool isReference;
        Visibility visibility;
        angelScriptTypeInfo->GetProperty(i, &name, &typeId, &isPrivate, &isProtected, &isNonSerialize, &offset, &isReference);

        if (isPrivate) {
            visibility = Visibility::Private;
        } else if (isProtected) {
            visibility = Visibility::Protected;
        } else {
            visibility = Visibility::Public;
        }

        Property newProperty = Property(name, offset, typeId, isReference, visibility, isNonSerialize);

        properties.push_back(newProperty);
    }
}

const std::vector<ScriptClass::Property>& ScriptClass::getProperties() const {
    return properties;
}

Script* ScriptClass::getScript() const {
    return script;
}

int ScriptClass::getTypeId() const {
    return angelScriptTypeInfo->GetTypeId();
}

void ScriptClass::finalizeInitialization() {
    int methodCount = angelScriptTypeInfo->GetMethodCount();
    for (int i = 0; i < methodCount; i++) {
        ScriptFunction* newFunction = new ScriptFunction(script,
                                                         angelScriptTypeInfo->GetMethodByIndex(i, true),
                                                         angelScriptTypeInfo->GetMethodByIndex(i, false));

        PGE::String decl = newFunction->getSignature().toString();

        methods.push_back(newFunction);
    }

    int factoryCount = angelScriptTypeInfo->GetFactoryCount();

    for (int i = 0; i < factoryCount; i++) {
        ScriptFunction* newFunction = new ScriptFunction(script, angelScriptTypeInfo->GetFactoryByIndex(i));

        PGE::String decl = newFunction->getSignature().toString();

        constructors.push_back(newFunction);
    }

    for (int i = 0; i < properties.size(); i++) {
        properties[i].determineType(script);
    }
}

ScriptObject* ScriptClass::createNewObject() {
    //TODO: don't hardcode use of first constructor
    constructors[0]->execute();
    return constructors[0]->getReturnObject();
}

ScriptClass::Property::Property(const PGE::String& n, int off, int tId, bool ref, ScriptClass::Visibility vis, bool isNonSerial) {
    name = n; offset = off; typeId = tId; isRef = ref; visibility = vis; isNonSerialize = isNonSerial;
}

PGE::String ScriptClass::Property::getName() const {
    return name;
}

int ScriptClass::Property::getOffset() const {
    return offset;
}

int ScriptClass::Property::getTypeId() const {
    return typeId;
}

bool ScriptClass::Property::isReference() const {
    return isRef;
}

ScriptClass::Visibility ScriptClass::Property::getVisibility() const {
    return visibility;
}

bool ScriptClass::Property::isNonSerializable() const {
    return isNonSerialize;
}

void ScriptClass::Property::determineType(Script* script) {
    type = script->typeFromTypeId(typeId);
}

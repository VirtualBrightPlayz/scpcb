#include "Script.h"
#include "Class.h"

ScriptClass::ScriptClass(Script* script, asITypeInfo* tInfo) {
    typeName = tInfo->GetName();

    asIScriptModule* module = script->getAngelScriptModule();

    angelScriptTypeInfo = tInfo;

    int propertyCount = angelScriptTypeInfo->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        const char* name;
        bool isPrivate;
        bool isProtected;
        int typeId;
        int offset;
        bool isReference;
        Visibility visibility;
        angelScriptTypeInfo->GetProperty(i, &name, &typeId, &isPrivate, &isProtected, &offset, &isReference);

        if (isPrivate) {
            visibility = Visibility::Private;
        } else if (isProtected) {
            visibility = Visibility::Protected;
        } else {
            visibility = Visibility::Public;
        }

        Property newProperty = Property(name, offset, typeId, isReference, visibility);

        properties.push_back(newProperty);
    }
}

const std::vector<ScriptClass::Property>& ScriptClass::getProperties() const {
    return properties;
}

int ScriptClass::getTypeId() const {
    return angelScriptTypeInfo->GetTypeId();
}

ScriptClass::Property::Property(const PGE::String& n, int off, int tId, bool ref, ScriptClass::Visibility vis) {
    name = n; offset = off; typeId = tId; isRef = ref; visibility = vis;
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



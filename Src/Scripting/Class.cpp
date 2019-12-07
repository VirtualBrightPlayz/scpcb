#include "Class.h"

ScriptClass::ScriptClass(Script* script, const PGE::String& className) {
    typeName = className;

    asIScriptModule* module = script->getAngelScriptModule();

    angelScriptTypeInfo = module->GetTypeInfoByName(className.cstr());

    int propertyCount = angelScriptTypeInfo->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        const char* name;
        bool isPrivate; bool isProtected;
        Property newProperty;
        angelScriptTypeInfo->GetProperty(i, &name, &newProperty.typeId, &isPrivate, &isProtected, &newProperty.offset, &newProperty.isReference);

        if (isPrivate) {
            newProperty.visibility = Visibility::Private;
        } else if (isProtected) {
            newProperty.visibility = Visibility::Protected;
        } else {
            newProperty.visibility = Visibility::Public;
        }

        newProperty.name = name;

        properties.push_back(newProperty);
    }
}

const std::vector<ScriptClass::Property>& ScriptClass::getProperties() const {
    return properties;
}

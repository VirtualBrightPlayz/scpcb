#include "ScriptObject.h"

#include <vector>

#include "ScriptClass.h"
#include "ScriptModule.h"

ScriptObject::ScriptObject(ScriptClass* clss, asIScriptObject* asObj) {
    scriptClass = clss;
    angelScriptObject = asObj;

    angelScriptObject->AddRef();
}

ScriptObject::~ScriptObject() {
    angelScriptObject->Release();
}

asIScriptObject* ScriptObject::getAngelScriptObject() const {
    return angelScriptObject;
}

int ScriptObject::getPropertyIndex(const PGE::String& name) const {
    int propertyCount = angelScriptObject->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        if (name.equals(angelScriptObject->GetPropertyName(i))) { return i; }
    }
    return -1;
}

PGE::String ScriptObject::getPropertyString(const PGE::String& propertyName) const {
    int index = getPropertyIndex(propertyName);

    void* obj = angelScriptObject->GetAddressOfProperty(index);
    return *((PGE::String*)obj);
}

void ScriptObject::setProperty(const PGE::String& propertyName, const PGE::String& val) const {
    int index = getPropertyIndex(propertyName);

    void* obj = angelScriptObject->GetAddressOfProperty(index);
    *((PGE::String*)obj) = val;
}

void ScriptObject::saveXML(tinyxml2::XMLElement* element, tinyxml2::XMLDocument& doc, const ScriptModule* module) const {
    int propertyCount = angelScriptObject->GetPropertyCount();
    for (int i = 0; i < propertyCount; i++) {
        // TODO: Check for empty properties.
        // TODO: Check for serializable properties.
        tinyxml2::XMLElement* propertyElement = doc.NewElement(angelScriptObject->GetPropertyName(i));
        element->InsertEndChild(propertyElement);

        int typeID = angelScriptObject->GetPropertyTypeId(i);
        Type* type = module->typeFromTypeId(typeID);

        module->saveXML(angelScriptObject->GetAddressOfProperty(i), type, propertyElement, doc);
    }
}




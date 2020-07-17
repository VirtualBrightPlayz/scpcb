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

ScriptFunction* ScriptObject::getFunction(const PGE::String& name) const {
    return scriptClass->getMethod(name);
}

void ScriptObject::saveXML(tinyxml2::XMLElement* element, const ScriptModule* module) const {
    int propertyCount = angelScriptObject->GetPropertyCount();

    for (int i = 0; i < propertyCount; i++) {
        if (!angelScriptObject->IsPropertySerializable(i)) { continue; }

        tinyxml2::XMLElement* propertyElement = element->GetDocument()->NewElement(angelScriptObject->GetPropertyName(i));
        int typeID = angelScriptObject->GetPropertyTypeId(i);
        Type* type = module->typeFromTypeId(typeID);

        module->saveXML(angelScriptObject->GetAddressOfProperty(i), type, propertyElement);
        if (!propertyElement->IsEmpty()) {
            element->InsertEndChild(propertyElement);
        }
    }
}

void ScriptObject::loadXML(tinyxml2::XMLElement* element, const ScriptModule* module) const {
    for (tinyxml2::XMLElement* propertyElement = element->FirstChildElement(); element != nullptr; element = element->NextSiblingElement()) {
        PGE::String propName = propertyElement->Name();
        int index = getPropertyIndex(propName);

        if (!angelScriptObject->IsPropertySerializable(index)) { continue; }

        int typeID = angelScriptObject->GetPropertyTypeId(index);
        Type* type = module->typeFromTypeId(typeID);

        module->loadXML(angelScriptObject->GetAddressOfProperty(index), type, propertyElement);
    }
}




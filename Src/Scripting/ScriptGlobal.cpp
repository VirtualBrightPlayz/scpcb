#include "ScriptGlobal.h"

#include "ScriptModule.h"
#include "ScriptObject.h"
#include "ScriptClass.h"

ScriptGlobal::ScriptGlobal(ScriptModule* module, int index) {
    this->module = module;
    this->index = index;
    const char* outName;
    const char* outNamespace;
    int outTypeID;
    bool outIsSerialize;

    module->getAngelScriptModule()->GetGlobalVar(index, &outName, &outNamespace, &outTypeID, nullptr, &outIsSerialize);
    name = outName;
    varNamespace = outNamespace;
    isSerialize = outIsSerialize;
    type = module->typeFromTypeId(outTypeID);

    object = nullptr; //TODO: add support for primitive types
    if (type->isClassType()) {
        object = new ScriptObject((ScriptClass*)type, (asIScriptObject*)module->getAngelScriptModule()->GetAddressOfGlobalVar(index));
    }
}

ScriptGlobal::~ScriptGlobal() {
    if (object != nullptr) { delete object; }
}

PGE::String ScriptGlobal::getName() const {
    return name;
}

PGE::String ScriptGlobal::getNamespace() const {
    return varNamespace;
}

bool ScriptGlobal::isSerializable() const {
    return isSerialize;
}

ScriptObject* ScriptGlobal::getObject() const {
    return object;
}

void ScriptGlobal::saveXML(tinyxml2::XMLElement* parent) const {
    tinyxml2::XMLElement* element = parent->GetDocument()->NewElement(name);
    if (!varNamespace.isEmpty()) {
        element->SetAttribute("namespace", varNamespace);
    }
    
    module->saveXML(module->getAngelScriptModule()->GetAddressOfGlobalVar(index), type, element);
    if (!element->IsEmpty()) {
        parent->InsertEndChild(element);
    }
}

void ScriptGlobal::loadXML(tinyxml2::XMLElement* element) const {
    void* ref = module->getAngelScriptModule()->GetAddressOfGlobalVar(index);

    module->loadXML(ref, type, element);
}
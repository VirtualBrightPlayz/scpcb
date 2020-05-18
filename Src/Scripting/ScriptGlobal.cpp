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
}

bool ScriptGlobal::isSerializable() const {
    return isSerialize;
}

void ScriptGlobal::saveXML(tinyxml2::XMLElement* parent, tinyxml2::XMLDocument& doc) const {
    tinyxml2::XMLElement* element = doc.NewElement(name);
    if (!varNamespace.isEmpty()) {
        element->SetAttribute("namespace", varNamespace);
    }
    parent->InsertEndChild(element);
    
    module->saveXML(module->getAngelScriptModule()->GetAddressOfGlobalVar(index), type, element, doc);
}
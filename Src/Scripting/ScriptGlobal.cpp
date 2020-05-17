#include "ScriptGlobal.h"

#include "ScriptModule.h"
#include "ScriptObject.h"

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
	bool isClass;
	type = module->typeFromTypeId(outTypeID, isClass);
	isClassType = isClass;
}

void ScriptGlobal::SaveXML(tinyxml2::XMLElement* element) const {
	void* ref = module->getAngelScriptModule()->GetAddressOfGlobalVar(index);

	if (!isClassType) {
		if (type == Type::String) {

		} else {
			int size = 0;
			if (type == Type::Int32) {
				size = 4;
			}
			int ivalue = 0;
			memcpy(&ivalue, ref, size);
		}
	} 
	else {
		void* ref = module->getAngelScriptModule()->GetAddressOfGlobalVar(index);
		asIScriptObject* obj = (asIScriptObject*)ref;
		ScriptClass* clss = (ScriptClass*)type;

		ScriptObject classObject = ScriptObject(clss, obj);
	}
}
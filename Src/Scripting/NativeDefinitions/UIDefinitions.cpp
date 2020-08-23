#include "UIDefinitions.h"

#include "../../Graphics/UIMesh.h"
#include "../ScriptManager.h"

UIDefinitions::UIDefinitions(ScriptManager* mgr, UIMesh* uiMesh) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("UI");
    engine->RegisterGlobalFunction("void setColor(const Color&in col)", asMETHOD(UIMesh, setColor), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void setTextureless()", asMETHOD(UIMesh, setTextureless), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void setTextured(const string&in textureName, bool tile)", asMETHODPR(UIMesh, setTextured, (const PGE::String&, bool), void), asCALL_THISCALL_ASGLOBAL, uiMesh);
    engine->RegisterGlobalFunction("void addRect(const Rectanglef&in rect)", asMETHOD(UIMesh, addRect), asCALL_THISCALL_ASGLOBAL, uiMesh);
}

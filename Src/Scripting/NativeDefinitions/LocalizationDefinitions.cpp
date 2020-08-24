#include "LocalizationDefinitions.h"

#include "../ScriptManager.h"
#include "../../Utils/LocalizationManager.h"

LocalizationDefinitions::LocalizationDefinitions(ScriptManager* mgr, LocalizationManager* lm) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("Local");
    engine->RegisterGlobalFunction("string getTxt(const string&in key)", asMETHOD(LocalizationManager, getLocalTxt), asCALL_THISCALL_ASGLOBAL, lm);
}

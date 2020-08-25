#include "WorldDefinitions.h"

#include "../ScriptManager.h"
#include "../../World/World.h"

WorldDefinitions::WorldDefinitions(ScriptManager* mgr, World* w) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("World");

    engine->RegisterGlobalProperty("bool paused", &w->paused);
    engine->RegisterGlobalFunction("void quit()", asMETHOD(World, quit), asCALL_THISCALL_ASGLOBAL, w);
}

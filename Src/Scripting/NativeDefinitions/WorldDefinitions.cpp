#include "WorldDefinitions.h"

#include "../ScriptManager.h"

#include "../../World/World.h"

WorldDefinitions::WorldDefinitions(ScriptManager* mgr, World* w) :
#ifdef WINDOWS
    platform(0)
#elif defined __APPLE__
    platform(1)
#elif defined LINUX
    platform(2)
#endif
    {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("World");
    engine->RegisterGlobalProperty("bool paused", &w->paused);
    engine->RegisterGlobalFunction("void quit()", asMETHOD(World, quit), asCALL_THISCALL_ASGLOBAL, w);

    engine->RegisterEnum("Platform");
    engine->RegisterEnumValue("Platform", "Windows", 0);
    engine->RegisterEnumValue("Platform", "Apple", 1);
    engine->RegisterEnumValue("Platform", "Linux", 2);

    engine->SetDefaultNamespace("World::Platform");
    engine->RegisterGlobalProperty("const Platform active", (void*)&platform);
}

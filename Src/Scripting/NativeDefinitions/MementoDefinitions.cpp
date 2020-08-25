#include "MementoDefinitions.h"

#include "../../Menus/GUI/MementoManager.h"
#include "../ScriptManager.h"

static MementoManager* createMM(int mementoMaxMemSize) {
	return new MementoManager(mementoMaxMemSize);
}

static void destroyMM(MementoManager* mm) {
	delete mm;
}

MementoDefinitions::MementoDefinitions(ScriptManager* mgr) {
	engine = mgr->getAngelScriptEngine();

	engine->RegisterObjectType("MementoManager", sizeof(MementoManager), asOBJ_REF | asOBJ_NOCOUNT);

	engine->RegisterObjectMethod("MementoManager", "string execute(const string&in txt, int pos, int&out posOut, bool undo)", asMETHOD(MementoManager, execute), asCALL_THISCALL);
	engine->RegisterObjectMethod("MementoManager", "void clear()", asMETHOD(MementoManager, clear), asCALL_THISCALL);
	engine->RegisterObjectMethod("MementoManager", "void push(int startPos, const string&in content, bool write, bool linked = false)", asMETHOD(MementoManager, push), asCALL_THISCALL);
	
	engine->SetDefaultNamespace("MementoManager");
	engine->RegisterGlobalFunction("MementoManager@ create(int maxMemSize)", asFUNCTION(createMM), asCALL_CDECL);
	engine->RegisterGlobalFunction("void destroy(MementoManager@ mm)", asFUNCTION(destroyMM), asCALL_CDECL);
}

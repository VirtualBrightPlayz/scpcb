#include "MessageDefinitions.h"

#include "../../Utils/MessageManager.h"

MessageDefinitions::MessageDefinitions(ScriptManager* mgr, MessageManager* mm) {
	asIScriptEngine* engine = mgr->getAngelScriptEngine();
	engine->SetDefaultNamespace("Msg");
	engine->RegisterGlobalFunction("void set(const string&in msg, float seconds=5.0, Color color=Color::White)", asMETHOD(MessageManager, setMsg), asCALL_THISCALL_ASGLOBAL, mm);
}

#include "RM2Definitions.h"
#include <fstream>

void RM2Definitions::loadRM2(PGE::String filename) {
    std::ofstream output; output.open("aaaa.txt", std::ios_base::out);

    output.write(filename.cstr(), filename.size());

    output.close();
}

void RM2Definitions::registerToEngine(ScriptManager* mgr) {
    asIScriptEngine* engine = mgr->getAngelScriptEngine();
    engine->RegisterGlobalFunction("void LoadRM2(string filename)", asMETHOD(RM2Definitions, loadRM2), asCALL_THISCALL_ASGLOBAL, this);
}

void RM2Definitions::cleanup() {
    //TODO: implement
}

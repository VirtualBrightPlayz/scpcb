#include "ScriptManager.h"
#include "Script.h"

#include <fstream>

Script::Script(ScriptManager* scriptMgr, const PGE::FileName& filename, const PGE::String& moduleName) {
    scriptManager = scriptMgr;

    PGE::String scriptContents = "";

    char* readBuf = new char[1024];

    std::ifstream inFile;
    inFile.open(filename.cstr(), std::ios_base::in);

    bool reachedEndOfFile = inFile.eof();
    while (!reachedEndOfFile) {
        inFile.read(readBuf,1023);
        readBuf[inFile.gcount()]='\0';
        scriptContents = PGE::String(scriptContents,readBuf);

        reachedEndOfFile = inFile.eof();
    }

    inFile.close();

    delete[] readBuf;

    PGE::String sectionName;
    sectionName = filename.str().substr(filename.str().findLast("/") + 1);
    sectionName = sectionName.substr(0, sectionName.findFirst("."));

    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();

    scriptModule = engine->GetModule(moduleName.size()>0 ? moduleName.cstr() : nullptr, asGM_CREATE_IF_NOT_EXISTS);

    int errorCode = scriptModule->AddScriptSection(sectionName.cstr(), scriptContents.cstr(), scriptContents.size());
    if (errorCode<0) { throw std::exception("kablooey!"); }
    
    errorCode = scriptModule->Build();
    //if (errorCode<0) { throw std::exception("whabammy!"); }
}

Script::~Script() {
    //do nothing
}

asIScriptModule* Script::getAngelScriptModule() const {
    return scriptModule;
}

#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <angelscript.h>
#include <Misc/FileName.h>
#include <Misc/String.h>

class Script {
    private:
        ScriptManager* scriptManager;
        asIScriptModule* scriptModule;
    public:
        Script(ScriptManager* scriptMgr, const PGE::FileName& filename, const PGE::String& moduleName);
        ~Script();
};

#endif

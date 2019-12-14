#ifndef SCRIPT_H_INCLUDED
#define SCRIPT_H_INCLUDED

#include <angelscript.h>
#include <Misc/FileName.h>
#include <Misc/String.h>
#include <map>
#include <inttypes.h>
#include "Type.h"

class ScriptManager;
class ScriptFunction;
class ScriptClass;

class Script {
    private:
        ScriptManager* scriptManager;
        asIScriptModule* scriptModule;

        std::vector<ScriptFunction*> functions;
        std::vector<ScriptClass*> classes;
    public:
        Script(ScriptManager* scriptMgr, const PGE::FileName& filename, const PGE::String& moduleName);
        ~Script();

        asIScriptModule* getAngelScriptModule() const;
        ScriptManager* getScriptManager() const;

        ScriptClass* getClassByName(const PGE::String& name) const;
        ScriptClass* getClassByTypeId(int typeId) const;

        ScriptFunction* getFunctionByName(const PGE::String& name) const;

        const Type* typeFromTypeId(int typeId) const;
        const Type* typeFromTypeId(int typeId, bool& isClssType) const;
};

#endif

#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>
#include <scriptbuilder/scriptbuilder.h>

class ScriptManager {
    private:
        asIScriptEngine* engine;
        asIScriptContext* context;
};

#endif

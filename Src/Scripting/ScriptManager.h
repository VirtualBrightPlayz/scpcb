#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>

class ScriptManager {
    private:
        asIScriptEngine* engine;
        asIStringFactory* stringFactory;
    public:
        ScriptManager();
        ~ScriptManager();

        void messageCallback(const asSMessageInfo* msg, void* param);

        asIScriptEngine* getAngelScriptEngine() const;
};

#endif

#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>
#include <Misc/String.h>

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

struct StringPoolEntry {
    PGE::String str;
    int refCount;

    StringPoolEntry(const PGE::String& s) {
        str = s;
        refCount = 0;
    }
};

#endif

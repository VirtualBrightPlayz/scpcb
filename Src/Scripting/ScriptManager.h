#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>
#include <Misc/String.h>
#include <vector>

class Script;
class ScriptClass;
class ScriptFunction;

class ScriptManager {
    public:
        struct LogEntry {
            enum class Type {
                AngelScriptError,
                AngelScriptWarning,
                AngelScriptInfo
            };

            Type type;
            PGE::String message;

            PGE::String section;
            int row;
            int col;
        };
    private:
        asIScriptEngine* engine;
        asIStringFactory* stringFactory;

        std::vector<ScriptClass*> sharedClasses;
        std::vector<ScriptFunction*> scriptFunctions;

        std::vector<LogEntry> log;
    public:
        ScriptManager();
        ~ScriptManager();

        void messageCallback(const asSMessageInfo* msg, void* param);

        asIScriptEngine* getAngelScriptEngine() const;

        ScriptClass* getSharedClassByTypeId(int typeId) const;
        void registerSharedClass(ScriptClass* clss);

        bool isArrayTypeId(int typeId) const;

        const std::vector<LogEntry>& getLog() const;
};

struct StringPoolEntry {
    PGE::String str;
    int refCount;

    StringPoolEntry() { refCount = 0; }

    StringPoolEntry(const PGE::String& s) {
        str = s;
        refCount = 0;
    }
};

#endif

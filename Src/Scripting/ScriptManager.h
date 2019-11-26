#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>
#include <Misc/String.h>
#include <vector>

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
        std::vector<LogEntry> log;
    public:
        ScriptManager();
        ~ScriptManager();

        void messageCallback(const asSMessageInfo* msg, void* param);

        asIScriptEngine* getAngelScriptEngine() const;

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

#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <vector>

#include <angelscript.h>

#include <String/String.h>

class Script;
class ScriptClass;
class ScriptFunction;
class ScriptModule;
class NativeFunction;

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
        std::vector<ScriptModule*> scriptModules;

        std::vector<LogEntry> log;
    public:
        ScriptManager();
        ~ScriptManager();

        void messageCallback(const asSMessageInfo* msg, void* param);
        void contextExceptionCallback(asIScriptContext* ctx);

        asIScriptEngine* getAngelScriptEngine() const;

        ScriptClass* getSharedClassByTypeId(int typeId) const;
        ScriptClass* getClassByTypeId(int typeId) const;
        void registerSharedClass(ScriptClass* clss);
        const std::vector<ScriptModule*>& getScriptModules() const;
        void registerScriptModule(ScriptModule* mdl);


        bool isArrayTypeId(int typeId) const;
};

#endif // SCRIPTMANAGER_H_INCLUDED

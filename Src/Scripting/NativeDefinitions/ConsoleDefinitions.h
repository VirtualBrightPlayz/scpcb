#ifndef CONSOLEDEFINITIONS_H_INCLUDED
#define CONSOLEDEFINITIONS_H_INCLUDED

#include <map>

#include "../NativeDefinition.h"
#include "../ScriptGlobal.h"

class ScriptManager;

class ConsoleDefinitions : public NativeDefinition {
    private:
        enum class LogType {
            Log,
            Warning,
            Error
        };

        struct Command {
            asIScriptFunction* func;
            PGE::String name;
            PGE::String helpText;
            bool duplicateName;
        };
        std::map<long long, Command> commands;

        asIScriptContext* scriptContext;
        asIScriptContext* addMsgCtx;

        void registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId);
        void registerCommandNoHelp(const PGE::String& name, void* f, int typeId);
        void executeCommand(const PGE::String& in);

        void internalLog(void* ref, int typeId, LogType type) const;

        void log(void* ref, int typeId) const;
        void warning(void* ref, int typeId) const;
        void error(void* ref, int typeId) const;

    public:
        ConsoleDefinitions(ScriptManager* mgr);
        ~ConsoleDefinitions();

        void setUp(ScriptManager* mgr);
};

#endif // CONSOLEDEFINITIONS_H_INCLUDED

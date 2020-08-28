#ifndef CONSOLEDEFINITIONS_H_INCLUDED
#define CONSOLEDEFINITIONS_H_INCLUDED

#include <map>

#include <Color/Color.h>

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
        };
        std::map<long long, Command> commands;

        asIScriptContext* scriptContext;
        asIScriptContext* msgContext;
        asIScriptObject* consoleInstance;
        asIScriptFunction* addConsoleMsgFunc;

        void printHelpList();
        void printHelpCommand(const PGE::String& command);

        void registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId);
        void registerCommandNoHelp(const PGE::String& name, void* f, int typeId);
        void executeCommand(const PGE::String& in);

        void addConsoleMessage(const PGE::String& msg, const PGE::Color& color);

        void internalLog(void* ref, int typeId, LogType type);

        void log(void* ref, int typeId);
        void warning(void* ref, int typeId);
        void error(void* ref, int typeId);

    public:
        ConsoleDefinitions(ScriptManager* mgr);
        ~ConsoleDefinitions();

        void setUp(ScriptManager* mgr);
};

#endif // CONSOLEDEFINITIONS_H_INCLUDED

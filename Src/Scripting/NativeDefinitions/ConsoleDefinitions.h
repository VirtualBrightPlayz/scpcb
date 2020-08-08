#ifndef CONSOLEDEFINITIONS_H_INCLUDED
#define CONSOLEDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class Console;

class ConsoleDefinitions : public NativeDefinition {
    private:
        enum class LogType {
            Log,
            Warning,
            Error
        };
        
        Console* console;

        asIScriptContext* scriptContext;

        void registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId) const;
        void registerCommandNoHelp(const PGE::String& name, void* f, int typeId) const;

        void internalLog(void* ref, int typeId, LogType type) const;

        void log(void* ref, int typeId) const;
        void warning(void* ref, int typeId) const;
        void error(void* ref, int typeId) const;
    public:
        ConsoleDefinitions(ScriptManager* mgr, Console* con);
        ~ConsoleDefinitions();
};

#endif // CONSOLEDEFINITIONS_H_INCLUDED

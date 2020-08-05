#ifndef CONSOLEDEFINITIONS_H_INCLUDED
#define CONSOLEDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class Console;

class ConsoleDefinitions : public NativeDefinition {
    private:
        Console* console;

        asIScriptContext* scriptContext;

        void registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId);
        void registerCommandNoHelp(const PGE::String& name, void* f, int typeId);

        void log(const PGE::String& content);
        void warning(const PGE::String& content);
        void error(const PGE::String& content);
    public:
        ConsoleDefinitions(ScriptManager* mgr, Console* con);
};

#endif // CONSOLEDEFINITIONS_H_INCLUDED

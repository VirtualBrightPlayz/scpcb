#ifndef GENERALDEFINITIONS_H_INCLUDED
#define GENERALDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class Console;

class ConsoleDefinition : public NativeDefinition {
    private:
        Console* console;

        asIScriptContext* scriptContext;

        void registerCommand(const PGE::String& name, const PGE::String& helpText, bool caseSensitive, void* f, int typeId);
        void registerCommandCaseInsensitive(const PGE::String& name, const PGE::String& helpText, void* f, int typeId);
        void registerCommandNoHelp(const PGE::String& name, void* f, int typeId);

        void log(const PGE::String& content);
        void warning(const PGE::String& content);
        void error(const PGE::String& content);
    public:
        ConsoleDefinition(ScriptManager* mgr, Console* con);
};

#endif

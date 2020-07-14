#ifndef GENERALDEFINITIONS_H_INCLUDED
#define GENERALDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class Console;

class ConsoleDefinition : public NativeDefinition {
    private:
        Console* console;

        void log(const PGE::String& content);
        void warning(const PGE::String& content);
        void error(const PGE::String& content);
    public:
        ConsoleDefinition(ScriptManager* mgr, Console* con);
};

#endif

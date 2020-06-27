#ifndef INPUTDEFINITIONS_H_INCLUDED
#define INPUTDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"
#include "../ScriptManager.h"

class KeyBinds;

class InputDefinitions : public NativeDefinition {
    private:
        KeyBinds* keyBinds;
    public:
        InputDefinitions(ScriptManager* mgr, KeyBinds* kb);
};

#endif

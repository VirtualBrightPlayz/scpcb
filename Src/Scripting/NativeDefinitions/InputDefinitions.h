#ifndef INPUTDEFINITIONS_H_INCLUDED
#define INPUTDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"
#include "../ScriptManager.h"

class KeyBinds;
class MouseData;

class InputDefinitions : public NativeDefinition {
    private:
        KeyBinds* keyBinds;

        int getClickCount() const;
        bool isHit() const;
        bool isDown() const;

    public:
        InputDefinitions(ScriptManager* mgr, KeyBinds* kb, MouseData* md);
};

#endif // INPUTDEFINITIONS_H_INCLUDED

#ifndef INPUTDEFINITIONS_H_INCLUDED
#define INPUTDEFINITIONS_H_INCLUDED

#include <PGE/Input/InputManager.h>

#include "../NativeDefinition.h"

class ScriptManager;
class KeyBinds;
class MouseData;

class InputDefinitions : public NativeDefinition {
    private:
        KeyBinds* keyBinds;

        void registerKey(const PGE::String& name, PGE::KeyboardInput* input);

        int getClickCount() const;
        bool isHit() const;
        bool isDown() const;

    public:
        InputDefinitions(ScriptManager* mgr, KeyBinds* kb, MouseData* md, PGE::InputManager* inputManager);
};

#endif // INPUTDEFINITIONS_H_INCLUDED

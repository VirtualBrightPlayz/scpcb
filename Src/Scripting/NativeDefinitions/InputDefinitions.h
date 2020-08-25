#ifndef INPUTDEFINITIONS_H_INCLUDED
#define INPUTDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"
#include "../ScriptManager.h"

#include <IO/IO.h>

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
        InputDefinitions(ScriptManager* mgr, KeyBinds* kb, MouseData* md, PGE::IO* io);
};

#endif // INPUTDEFINITIONS_H_INCLUDED

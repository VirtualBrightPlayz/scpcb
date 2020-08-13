#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <map>

#include <IO/IO.h>
#include <UserInput/UserInput.h>

#include "Input.h"

class KeyBinds {
    private:
        // Inputs that are down for this tick.
        Input downInputs;

        // Inputs that are in the hit state for this tick.
        Input hitInputs;

        struct UserInput {
                PGE::UserInput* input;
                int code; // Can be mouse button, controller button or key code.
        };

        PGE::IO* io;

        typedef std::multimap<Input, UserInput> UserInputMap;
        UserInputMap bindings;
        //std::multimap<PGE::String, UserInput> consoleBindings; // TODO.

        void bindInput(Input input, UserInput key);
        void unbindInput(Input input, PGE::UserInput::DEVICE device, int key);

    public:
        KeyBinds(PGE::IO* inIo);
        ~KeyBinds();

        PGE::MouseInput* mouse1;
        PGE::MouseInput* mouse2;
        PGE::KeyboardInput* escape;

        // Text input keys.
        PGE::KeyboardInput* leftArrow;
        PGE::KeyboardInput* rightArrow;
        PGE::KeyboardInput* upArrow;
        PGE::KeyboardInput* downArrow;
        PGE::KeyboardInput* leftShift;
        PGE::KeyboardInput* rightShift;
        // Checks if any of the shift keys are held down.
        bool anyShiftDown() const;
        PGE::KeyboardInput* backspace;
        PGE::KeyboardInput* del;
        PGE::KeyboardInput* enter;

        // Shortcuts.
        PGE::KeyboardInput* leftShortcutKey;
        PGE::KeyboardInput* rightShortcutKey;
        // Checks if any of the shortcut keys are held down.
        bool anyShortcutDown() const;
        PGE::KeyboardInput* keyA;
        PGE::KeyboardInput* keyC;
        PGE::KeyboardInput* keyX;
        PGE::KeyboardInput* keyV;
        PGE::KeyboardInput* keyZ;
#ifndef __APPLE__
        PGE::KeyboardInput* keyY;
#endif

        bool copyIsHit() const;
        bool cutIsHit() const;
        bool pasteIsHit() const;
        bool undoIsHit() const;
        bool redoIsHit() const;
        bool selectAllIsHit() const;

        void bindInput(Input input, PGE::MouseInput::BUTTON key);
        void bindInput(Input input, PGE::KeyboardInput::KEYCODE key);
        void bindInput(Input input, PGE::ControllerInput::BUTTON key);
        void unbindInput(Input input, PGE::MouseInput::BUTTON key);
        void unbindInput(Input input, PGE::KeyboardInput::KEYCODE key);
        void unbindInput(Input input, PGE::ControllerInput::BUTTON key);

        // Iterate through the keybinds and find out which ones fired this tick.
        void update();
        Input getDownInputs() const;
        Input getHitInputs() const;
};

#endif // KEYBINDS_H_INCLUDED

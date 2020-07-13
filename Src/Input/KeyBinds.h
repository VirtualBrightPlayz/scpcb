#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <map>

#include <IO/IO.h>
#include <UserInput/UserInput.h>

#include "Input.h"

class KeyBinds {
    private:
        /// <summary>
        /// Inputs that are down for this tick.
        /// </summary>
        Input downInputs;

        /// <summary>
        /// Inputs that are in the hit state for this tick.
        /// </summary>
        Input hitInputs;

        class UserInput {
            public:
                PGE::UserInput* input;
                PGE::MouseInput::BUTTON mouseButton;
                PGE::KeyboardInput::SCANCODE scancode;
                PGE::ControllerInput::BUTTON controllerButton;
        };

        PGE::IO* io;
        std::map<Input, std::vector<UserInput>> bindings;
        // TODO: Separate bindings map for console commands.

        void bindInput(Input input, UserInput key);

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

        void bindInput(Input input, PGE::MouseInput::BUTTON key);
        void bindInput(Input input, PGE::KeyboardInput::SCANCODE key);
        void bindInput(Input input, PGE::ControllerInput::BUTTON key);
        void unbindInput(Input input, PGE::MouseInput::BUTTON key);
        void unbindInput(Input input, PGE::KeyboardInput::SCANCODE key);
        void unbindInput(Input input, PGE::ControllerInput::BUTTON key);

        // Iterate through the keybinds and find out which ones fired this tick.
        void update();
        Input getDownInputs() const;
        Input getHitInputs() const;
};

#endif // KEYBINDS_H_INCLUDED

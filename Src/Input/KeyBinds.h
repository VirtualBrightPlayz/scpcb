#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <map>

#include <IO/IO.h>
#include <UserInput/UserInput.h>

#include "Input.h"

class KeyBinds {
    private:
        // Current inputs for this tick.
        Input currInputs;

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

        void bindInput(Input input, PGE::MouseInput::BUTTON key);
        void bindInput(Input input, PGE::KeyboardInput::SCANCODE key);
        void bindInput(Input input, PGE::ControllerInput::BUTTON key);
        void unbindInput(Input input, PGE::MouseInput::BUTTON key);
        void unbindInput(Input input, PGE::KeyboardInput::SCANCODE key);
        void unbindInput(Input input, PGE::ControllerInput::BUTTON key);

        // Iterate through the keybinds and find out which ones fired this tick.
        void update();
        Input getFiredInputs() const;
};

#endif // KEYBINDS_H_INCLUDED

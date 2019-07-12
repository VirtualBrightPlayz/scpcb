#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <IO/IO.h>
#include <UserInput/UserInput.h>

class KeyBinds {
    private:
        std::vector<PGE::UserInput*> miscBindings;

    public:
        PGE::MouseInput* mouse1;
        PGE::MouseInput* mouse2;

        PGE::KeyboardInput* escape;

        void update();
};

#endif // KEYBINDS_H_INCLUDED

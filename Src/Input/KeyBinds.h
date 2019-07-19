#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <IO/IO.h>
#include <UserInput/UserInput.h>

class KeyBinds {
    private:
        PGE::IO* io;
    public:
        KeyBinds(PGE::IO* inIo);
        ~KeyBinds();

        PGE::MouseInput* mouse1;
        PGE::MouseInput* mouse2;

        PGE::KeyboardInput* escape;
};

#endif // KEYBINDS_H_INCLUDED

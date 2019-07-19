#include "KeyBinds.h"

KeyBinds::KeyBinds(PGE::IO* inIo) {
    io = inIo;

    mouse1 = new PGE::MouseInput(PGE::MouseInput::BUTTON::LEFT);
    mouse2 = new PGE::MouseInput(PGE::MouseInput::BUTTON::RIGHT);

    escape = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::ESCAPE);

    io->trackInput(mouse1);
    io->trackInput(mouse2);
    io->trackInput(escape);
}

KeyBinds::~KeyBinds() {
    io->untrackInput(mouse1);
    io->untrackInput(mouse2);
    io->untrackInput(escape);

    delete mouse1; delete mouse2; delete escape;
}

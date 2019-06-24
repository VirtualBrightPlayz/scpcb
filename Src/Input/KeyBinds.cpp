#include "KeyBinds.h"

KeyBinds keyBinds = KeyBinds();

void KeyBinds::initialize() {
    keyBinds.mouse1 = Binding::createMouseBind(PGE::MouseInput::BUTTON::LEFT);
    keyBinds.mouse2 = Binding::createMouseBind(PGE::MouseInput::BUTTON::RIGHT);

    keyBinds.escape = Binding::createKeyboardBind(PGE::KeyboardInput::SCANCODE::ESCAPE);
}

void KeyBinds::cleanup() {
    keyBinds = KeyBinds();
}

void KeyBinds::update() {
    mouse1.update();
    mouse2.update();

    escape.update();
}
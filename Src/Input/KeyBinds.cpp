#include "KeyBinds.h"

KeyBinds::KeyBinds(PGE::IO* inIo) {
    io = inIo;

    mouse1 = new PGE::MouseInput(PGE::MouseInput::BUTTON::LEFT);
    mouse2 = new PGE::MouseInput(PGE::MouseInput::BUTTON::RIGHT);

    escape = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::ESCAPE);
    leftArrow = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::LEFT);
    rightArrow = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::RIGHT);
    leftShift = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::LSHIFT);
    rightShift = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::RSHIFT);
    backspace = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::BACKSPACE);
    del = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::DELETE);

#ifdef __APPLE__
    leftShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::LGUI);
    rightShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::RGUI);
#else
    leftShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::LCTRL);
    rightShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::RCTRL);
#endif
    keyC = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::C);
    keyX = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::X);
    keyV = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::V);
    keyZ = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::Z);
#ifndef __APPLE__
    keyY = new PGE::KeyboardInput(PGE::KeyboardInput::SCANCODE::Y);
#endif

    io->trackInput(mouse1);
    io->trackInput(mouse2);
    io->trackInput(escape);
    io->trackInput(leftArrow);
    io->trackInput(rightArrow);
    io->trackInput(leftShift);
    io->trackInput(rightShift);
    io->trackInput(backspace);
    io->trackInput(del);
    io->trackInput(leftShortcutKey);
    io->trackInput(rightShortcutKey);
    io->trackInput(keyC);
    io->trackInput(keyX);
    io->trackInput(keyV);
    io->trackInput(keyZ);
#ifndef __APPLE__
    io->trackInput(keyY);
#endif
}

KeyBinds::~KeyBinds() {
    io->untrackInput(mouse1);
    io->untrackInput(mouse2);
    io->untrackInput(escape);
    io->untrackInput(leftArrow);
    io->untrackInput(rightArrow);
    io->untrackInput(leftShift);
    io->untrackInput(rightShift);
    io->untrackInput(backspace);
    io->untrackInput(del);
    io->untrackInput(leftShortcutKey);
    io->untrackInput(rightShortcutKey);
    io->untrackInput(keyC);
    io->untrackInput(keyX);
    io->untrackInput(keyV);
    io->untrackInput(keyZ);
#ifndef __APPLE__
    io->untrackInput(keyY);
#endif

    delete mouse1; delete mouse2; delete escape;
    delete leftArrow; delete rightArrow; delete leftShift; delete rightShift;
    delete backspace; delete del;
    delete leftShortcutKey; delete rightShortcutKey;
    delete keyC; delete keyX; delete keyV; delete keyZ;
#ifndef __APPLE__
    delete keyY;
#endif

    // Untrack and delete bindings map.
    std::map<Input, std::vector<UserInput>>::iterator it;
    for (it = bindings.begin(); it != bindings.end(); it++) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            io->untrackInput(it->second[i].input);
            delete it->second[i].input;
        }
        it->second.clear();
    }
}

bool KeyBinds::anyShiftDown() const {
    return leftShift->isDown() || rightShift->isDown();
}

bool KeyBinds::copyIsHit() const {
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && keyC->isHit();
}
bool KeyBinds::cutIsHit() const {
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && keyX->isHit();
}
bool KeyBinds::pasteIsHit() const {
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && keyV->isHit();
}
bool KeyBinds::undoIsHit() const {
#ifdef __APPLE__
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && !anyShiftDown() && keyZ->isHit();
#else
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && keyZ->isHit();
#endif
}
bool KeyBinds::redoIsHit() const {
#ifdef __APPLE__
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && anyShiftDown() && keyZ->isHit();
#else
    return (leftShortcutKey->isDown() || rightShortcutKey->isDown()) && keyY->isHit();
#endif
}

void KeyBinds::update() {
    currInputs = Input::None;

    std::map<Input, std::vector<UserInput>>::const_iterator it;
    for (it = bindings.begin(); it != bindings.end(); it++) {
        // Check if any of the assigned inputs are down.
        for (int i = 0; i < (int)it->second.size(); i++) {
            // If one of them is down then the input is active.
            if (it->second[i].input->isDown()) {
                currInputs = currInputs | it->first;
                break;
            }
        }
    }
}

Input KeyBinds::getFiredInputs() const {
    return currInputs;
}

void KeyBinds::bindInput(Input input, UserInput key) {
    io->trackInput(key.input);

    // Does the key exist already?
    std::map<Input, std::vector<UserInput>>::iterator it = bindings.find(input);
    if (it != bindings.end()) {
        it->second.push_back(key);
    } else {
        // Create it otherwise.
        std::vector<UserInput> vect;
        vect.push_back(key);
        bindings[input] = vect;
    }
}

void KeyBinds::bindInput(Input input, PGE::MouseInput::BUTTON key) {
    UserInput wrapKey;
    wrapKey.input = new PGE::MouseInput(key);
    wrapKey.mouseButton = key;

    bindInput(input, wrapKey);
}

void KeyBinds::bindInput(Input input, PGE::KeyboardInput::SCANCODE key) {
    UserInput wrapKey;
    wrapKey.input = new PGE::KeyboardInput(key);
    wrapKey.scancode = key;

    bindInput(input, wrapKey);
}

void KeyBinds::bindInput(Input input, PGE::ControllerInput::BUTTON key) {
    UserInput wrapKey;
    wrapKey.input = new PGE::ControllerInput(key);
    wrapKey.controllerButton = key;

    bindInput(input, wrapKey);
}

void KeyBinds::unbindInput(Input input, PGE::MouseInput::BUTTON key) {
    std::map<Input, std::vector<UserInput>>::iterator it = bindings.find(input);
    if (it != bindings.end()) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            if (it->second[i].input->getDevice() == PGE::UserInput::DEVICE::MOUSE && it->second[i].mouseButton == key) {
                io->untrackInput(it->second[i].input);
                delete it->second[i].input;
                it->second.erase(it->second.begin() + i);
                return;
            }
        }
    }
}

void KeyBinds::unbindInput(Input input, PGE::KeyboardInput::SCANCODE key) {
    std::map<Input, std::vector<UserInput>>::iterator it = bindings.find(input);
    if (it != bindings.end()) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            if (it->second[i].input->getDevice() == PGE::UserInput::DEVICE::KEYBOARD && it->second[i].scancode == key) {
                io->untrackInput(it->second[i].input);
                delete it->second[i].input;
                it->second.erase(it->second.begin() + i);
                return;
            }
        }
    }
}

void KeyBinds::unbindInput(Input input, PGE::ControllerInput::BUTTON key) {
    std::map<Input, std::vector<UserInput>>::iterator it = bindings.find(input);
    if (it != bindings.end()) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            if (it->second[i].input->getDevice() == PGE::UserInput::DEVICE::CONTROLLER && it->second[i].controllerButton == key) {
                io->untrackInput(it->second[i].input);
                delete it->second[i].input;
                it->second.erase(it->second.begin() + i);
                return;
            }
        }
    }
}

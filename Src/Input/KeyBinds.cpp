#include "KeyBinds.h"

KeyBinds::KeyBinds(PGE::IO* inIo) {
    downInputs = Input::None;
    hitInputs = Input::None;

    io = inIo;

    mouse1 = new PGE::MouseInput(PGE::MouseInput::BUTTON::LEFT);
    mouse2 = new PGE::MouseInput(PGE::MouseInput::BUTTON::RIGHT);

    escape = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::ESCAPE);
    leftArrow = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::LEFT);
    rightArrow = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::RIGHT);
    upArrow = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::UP);
    downArrow = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::DOWN);
    leftShift = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::LSHIFT);
    rightShift = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::RSHIFT);
    backspace = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::BACKSPACE);
    del = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::DELETE);
    enter = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::RETURN);

#ifdef __APPLE__
    leftShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::LGUI);
    rightShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::RGUI);
#else
    leftShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::LCTRL);
    rightShortcutKey = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::RCTRL);
#endif
    keyC = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::C);
    keyX = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::X);
    keyV = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::V);
    keyZ = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::Z);
#ifndef __APPLE__
    keyY = new PGE::KeyboardInput(PGE::KeyboardInput::KEYCODE::Y);
#endif

    io->trackInput(mouse1);
    io->trackInput(mouse2);
    io->trackInput(escape);
    io->trackInput(leftArrow);
    io->trackInput(rightArrow);
    io->trackInput(upArrow);
    io->trackInput(downArrow);
    io->trackInput(leftShift);
    io->trackInput(rightShift);
    io->trackInput(backspace);
    io->trackInput(del);
    io->trackInput(enter);
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
    io->untrackInput(upArrow);
    io->untrackInput(downArrow);
    io->untrackInput(leftShift);
    io->untrackInput(rightShift);
    io->untrackInput(backspace);
    io->untrackInput(del);
    io->untrackInput(enter);
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
    delete leftArrow; delete rightArrow; delete upArrow; delete downArrow; delete leftShift; delete rightShift;
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

bool KeyBinds::anyShortcutDown() const {
    return leftShortcutKey->isDown() || rightShortcutKey->isDown();
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
    downInputs = Input::None;
    hitInputs = Input::None;

    std::map<Input, std::vector<UserInput>>::const_iterator it;
    for (it = bindings.begin(); it != bindings.end(); it++) {
        // Check if any of the assigned inputs are down.
        for (int i = 0; i < (int)it->second.size(); i++) {
            // If one of them is down/hit then the input is active.
            if (it->second[i].input->isDown()) {
                downInputs = downInputs | it->first;
            }
            
            if (it->second[i].input->isHit()) {
                hitInputs = hitInputs | it->first;
            }
        }
    }
}

Input KeyBinds::getDownInputs() const {
    return downInputs;
}

Input KeyBinds::getHitInputs() const {
    return hitInputs;
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

void KeyBinds::bindInput(Input input, PGE::KeyboardInput::KEYCODE key) {
    UserInput wrapKey;
    wrapKey.input = new PGE::KeyboardInput(key);
    wrapKey.keyCode = key;

    bindInput(input, wrapKey);
}

void KeyBinds::bindInput(Input input, PGE::ControllerInput::BUTTON key) {
    UserInput wrapKey;
    wrapKey.input = new PGE::ControllerInput(io->getController(0),key);
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

void KeyBinds::unbindInput(Input input, PGE::KeyboardInput::KEYCODE key) {
    std::map<Input, std::vector<UserInput>>::iterator it = bindings.find(input);
    if (it != bindings.end()) {
        for (int i = 0; i < (int)it->second.size(); i++) {
            if (it->second[i].input->getDevice() == PGE::UserInput::DEVICE::KEYBOARD && it->second[i].keyCode == key) {
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

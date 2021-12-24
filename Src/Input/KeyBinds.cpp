#include "KeyBinds.h"

#include "../Scripting/NativeDefinitions/ConsoleDefinitions.h"

KeyBinds::KeyBinds(PGE::InputManager* inIo) {
    downInputs = Input::NONE;
    hitInputs = Input::NONE;

    inputManager = inIo;

    mouse1 = new PGE::MouseInput(PGE::MouseButton::LEFT);
    mouse2 = new PGE::MouseInput(PGE::MouseButton::RIGHT);

    escape = new PGE::KeyboardInput(PGE::Keycode::ESCAPE);
    leftArrow = new PGE::KeyboardInput(PGE::Keycode::LEFT);
    rightArrow = new PGE::KeyboardInput(PGE::Keycode::RIGHT);
    upArrow = new PGE::KeyboardInput(PGE::Keycode::UP);
    downArrow = new PGE::KeyboardInput(PGE::Keycode::DOWN);
    leftShift = new PGE::KeyboardInput(PGE::Keycode::LSHIFT);
    rightShift = new PGE::KeyboardInput(PGE::Keycode::RSHIFT);
    backspace = new PGE::KeyboardInput(PGE::Keycode::BACKSPACE);
    del = new PGE::KeyboardInput(PGE::Keycode::DELETE);
    enter = new PGE::KeyboardInput(PGE::Keycode::RETURN);

#ifdef __APPLE__
    leftShortcutKey = new PGE::KeyboardInput(PGE::Keycode::LGUI);
    rightShortcutKey = new PGE::KeyboardInput(PGE::Keycode::RGUI);
#else
    leftShortcutKey = new PGE::KeyboardInput(PGE::Keycode::LCTRL);
    rightShortcutKey = new PGE::KeyboardInput(PGE::Keycode::RCTRL);
#endif
    keyA = new PGE::KeyboardInput(PGE::Keycode::A);
    keyC = new PGE::KeyboardInput(PGE::Keycode::C);
    keyX = new PGE::KeyboardInput(PGE::Keycode::X);
    keyV = new PGE::KeyboardInput(PGE::Keycode::V);
    keyZ = new PGE::KeyboardInput(PGE::Keycode::Z);
#ifndef __APPLE__
    keyY = new PGE::KeyboardInput(PGE::Keycode::Y);
#endif

    inputManager->trackInput(mouse1);
    inputManager->trackInput(mouse2);
    inputManager->trackInput(escape);
    inputManager->trackInput(leftArrow);
    inputManager->trackInput(rightArrow);
    inputManager->trackInput(upArrow);
    inputManager->trackInput(downArrow);
    inputManager->trackInput(leftShift);
    inputManager->trackInput(rightShift);
    inputManager->trackInput(backspace);
    inputManager->trackInput(del);
    inputManager->trackInput(enter);
    inputManager->trackInput(leftShortcutKey);
    inputManager->trackInput(rightShortcutKey);
    inputManager->trackInput(keyA);
    inputManager->trackInput(keyC);
    inputManager->trackInput(keyX);
    inputManager->trackInput(keyV);
    inputManager->trackInput(keyZ);
#ifndef __APPLE__
    inputManager->trackInput(keyY);
#endif

    // TODO: Add more | change style?
    registerInputString("key_q", PGE::Keycode::Q);
    registerInputString("key_w", PGE::Keycode::W);
    registerInputString("key_e", PGE::Keycode::E);
    registerInputString("key_r", PGE::Keycode::R);
    registerInputString("key_t", PGE::Keycode::T);
    registerInputString("key_z", PGE::Keycode::Z);
    registerInputString("key_u", PGE::Keycode::U);
    registerInputString("key_i", PGE::Keycode::I);
    registerInputString("key_o", PGE::Keycode::O);
    registerInputString("key_p", PGE::Keycode::P);
    registerInputString("key_a", PGE::Keycode::A);
    registerInputString("key_s", PGE::Keycode::S);
    registerInputString("key_d", PGE::Keycode::D);
    registerInputString("key_f", PGE::Keycode::F);
    registerInputString("key_g", PGE::Keycode::G);
    registerInputString("key_h", PGE::Keycode::H);
    registerInputString("key_j", PGE::Keycode::J);
    registerInputString("key_k", PGE::Keycode::K);
    registerInputString("key_l", PGE::Keycode::L);
    registerInputString("key_y", PGE::Keycode::Y);
    registerInputString("key_x", PGE::Keycode::X);
    registerInputString("key_c", PGE::Keycode::C);
    registerInputString("key_v", PGE::Keycode::V);
    registerInputString("key_b", PGE::Keycode::B);
    registerInputString("key_n", PGE::Keycode::N);
    registerInputString("key_m", PGE::Keycode::M);
}

KeyBinds::~KeyBinds() {
    inputManager->untrackInput(mouse1);
    inputManager->untrackInput(mouse2);
    inputManager->untrackInput(escape);
    inputManager->untrackInput(leftArrow);
    inputManager->untrackInput(rightArrow);
    inputManager->untrackInput(upArrow);
    inputManager->untrackInput(downArrow);
    inputManager->untrackInput(leftShift);
    inputManager->untrackInput(rightShift);
    inputManager->untrackInput(backspace);
    inputManager->untrackInput(del);
    inputManager->untrackInput(enter);
    inputManager->untrackInput(leftShortcutKey);
    inputManager->untrackInput(rightShortcutKey);
    inputManager->untrackInput(keyA);
    inputManager->untrackInput(keyC);
    inputManager->untrackInput(keyX);
    inputManager->untrackInput(keyV);
    inputManager->untrackInput(keyZ);
#ifndef __APPLE__
    inputManager->untrackInput(keyY);
#endif

    delete mouse1; delete mouse2; delete escape;
    delete leftArrow; delete rightArrow; delete upArrow; delete downArrow; delete leftShift; delete rightShift;
    delete backspace; delete del;
    delete leftShortcutKey; delete rightShortcutKey;
    delete keyA; delete keyC; delete keyX; delete keyV; delete keyZ;
#ifndef __APPLE__
    delete keyY;
#endif

    // Untrack and delete bindings map.
    UserInputMap::iterator it;
    for (it = bindings.begin(); it != bindings.end(); it++) {
        inputManager->untrackInput(it->second);
        delete it->second;
    }

    for (const auto& it : inputStrings) {
        delete it.second;
    }
}

void KeyBinds::setConsoleDefinitions(ConsoleDefinitions* inConDef) {
    conDef = inConDef;
}

void KeyBinds::registerInputString(const PGE::String& string, PGE::Keycode key) {
    inputStrings.emplace(string, new PGE::KeyboardInput(key));
}

void KeyBinds::registerInputString(const PGE::String& string, PGE::MouseButton key) {
    inputStrings.emplace(string, new PGE::MouseInput(key));
}

void KeyBinds::registerInputString(const PGE::String& string, PGE::ControllerButton key) {
    inputStrings.emplace(string, new PGE::ControllerInput(inputManager->getController(0), key));
}

bool KeyBinds::anyShiftDown() const {
    return leftShift->isDown() || rightShift->isDown();
}

bool KeyBinds::anyShortcutDown() const {
    return leftShortcutKey->isDown() || rightShortcutKey->isDown();
}

bool KeyBinds::copyIsHit() const {
    return keyC->isHit() && anyShortcutDown();
}
bool KeyBinds::cutIsHit() const {
    return keyX->isHit() && anyShortcutDown();
}
bool KeyBinds::pasteIsHit() const {
    return keyV->isHit() && anyShortcutDown();
}
bool KeyBinds::undoIsHit() const {
#ifdef __APPLE__
    return keyZ->isHit() && anyShortcutDown() && !anyShiftDown();
#else
    return keyZ->isHit() && anyShortcutDown();
#endif
}
bool KeyBinds::redoIsHit() const {
#ifdef __APPLE__
    return keyZ->isHit() && anyShortcutDown() && anyShiftDown();
#else
    return keyY->isHit() && anyShortcutDown();
#endif
}
bool KeyBinds::selectAllIsHit() const {
    return keyA->isHit() && anyShortcutDown();
}

void KeyBinds::update() {
    downInputs = Input::NONE;
    hitInputs = Input::NONE;

    // TODO: This could be faster when the map is switched.
    for (auto it = bindings.begin(); it != bindings.end(); it++) {
        // Check if any of the assigned inputs are down.
        // If one of them is down/hit then the input is active.
        if (it->second->isDown()) {
            downInputs = downInputs | it->first;
        }
            
        if (it->second->isHit()) {
            hitInputs = hitInputs | it->first;
        }
    }

    // TODO: Disable when key capture is enabled.
    for (ConsoleBindingsMap::const_iterator it = consoleBindings.begin(); it != consoleBindings.end(); it++) {
        if (it->second->isHit()) {
            conDef->executeCommand(it->first.str);
        }
    }
}

Input KeyBinds::getDownInputs() const {
    return downInputs;
}

Input KeyBinds::getHitInputs() const {
    return hitInputs;
}

void KeyBinds::bindInput(Input input, PGE::Input* key) {
    inputManager->trackInput(key);
    bindings.emplace(input, key);
}

void KeyBinds::bindInput(Input input, PGE::MouseButton key) {
    bindInput(input, new PGE::MouseInput(key));
}

void KeyBinds::bindInput(Input input, PGE::Keycode key) {
    bindInput(input, new PGE::KeyboardInput(key));
}

void KeyBinds::bindInput(Input input, PGE::ControllerButton key) {
    bindInput(input, new PGE::ControllerInput(inputManager->getController(0), key));
}

void KeyBinds::unbindInput(Input input, PGE::Device Device, int key) {
    // Iterate over all values of the given key
    std::pair<UserInputMap::iterator, UserInputMap::iterator> bindingsRange = bindings.equal_range(input);
    for (UserInputMap::iterator it = bindingsRange.first; it != bindingsRange.second; it++) {
        if (it->second->getDevice() == Device && it->second->getKey() == key) {
            inputManager->untrackInput(it->second);
            delete it->second;
            bindings.erase(it);
            return;
        }
    }
}

void KeyBinds::unbindInput(Input input, PGE::MouseButton key) {
    unbindInput(input, PGE::Device::MOUSE, (int)key);
}

void KeyBinds::unbindInput(Input input, PGE::Keycode key) {
    unbindInput(input, PGE::Device::KEYBOARD, (int)key);
}

void KeyBinds::unbindInput(Input input, PGE::ControllerButton key) {
    unbindInput(input, PGE::Device::CONTROLLER, (int)key);
}

void KeyBinds::bindCommand(PGE::String command, PGE::Input* key) {
    inputManager->trackInput(key);
    consoleBindings.emplace(command, key);
}

void KeyBinds::unbindCommand(PGE::String command, PGE::Input* key) {
    // Iterate over all values of the given key
    std::pair<ConsoleBindingsMap::iterator, ConsoleBindingsMap::iterator> bindingsRange = consoleBindings.equal_range(command);
    for (ConsoleBindingsMap::iterator it = bindingsRange.first; it != bindingsRange.second; it++) {
        if (it->second->getDevice() == key->getDevice() && it->second->getKey() == key->getKey()) {
            inputManager->untrackInput(it->second);
            consoleBindings.erase(it);
            return;
        }
    }
}

PGE::Input* KeyBinds::stringToInput(const PGE::String& key) const {
    auto find = inputStrings.find(key.toLower());
    if (find == inputStrings.end()) {
        return nullptr;
    }
    return find->second;
}

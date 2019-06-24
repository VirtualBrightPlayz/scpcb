#include "Binding.h"

std::map<PGE::UserInput*, int> Binding::cpyTracker = std::map<PGE::UserInput*, int>();

IO Binding::io = IO();

void Binding::initialize(const IO& io) {
    Binding::io = io;
}

void Binding::increment() const {
    if (deviceInput != nullptr) {
        cpyTracker[deviceInput] = cpyTracker[deviceInput] + 1;
    }
}

bool Binding::decrement() const {
    if (deviceInput == nullptr) { return false; }
    int newCount = cpyTracker[deviceInput] = cpyTracker[deviceInput] - 1;
    return newCount <= 0;
}

Binding::Binding(const Binding& cpy) {
    device = cpy.device;
    deviceInput = cpy.deviceInput;
    increment();
}

Binding& Binding::operator=(const Binding& other) {
    if (&other == this) return *this;

    // Wipe current reference.
    bool remove = decrement();

    if (remove && deviceInput != nullptr) {
        io->untrackInput(deviceInput);
        delete deviceInput;
    }
    deviceInput = nullptr;

    // Add new reference.
    device = other.device;
    deviceInput = other.deviceInput;
    increment();

    return *this;
}

Binding::~Binding() {
    bool remove = decrement();

    if (remove && deviceInput != nullptr) {
        io->untrackInput(deviceInput);
        delete deviceInput;
    }
}

Binding Binding::createMouseBind(PGE::MouseInput::BUTTON button) {
    Binding retval;
    retval.device = PGE::UserInput::DEVICE::MOUSE;
    retval.deviceInput = new PGE::MouseInput(button);
    io->trackInput(retval.deviceInput);
    cpyTracker.insert(std::pair<PGE::UserInput*, int>(retval.deviceInput, 1));

    return retval;
}

Binding Binding::createKeyboardBind(PGE::KeyboardInput::SCANCODE code) {
    Binding retval;
    retval.device = PGE::UserInput::DEVICE::KEYBOARD;
    retval.deviceInput = new PGE::KeyboardInput(code);
    io->trackInput(retval.deviceInput);
    cpyTracker.insert(std::pair<PGE::UserInput*, int>(retval.deviceInput, 1));

    return retval;
}

Binding Binding::createControllerBind(PGE::GamepadInput::CONTROLLER_BUTTON button) {
    Binding retval;
    retval.device = PGE::UserInput::DEVICE::GAMEPAD;
    retval.deviceInput = new PGE::GamepadInput(button);
    io->trackInput(retval.deviceInput);
    cpyTracker.insert(std::pair<PGE::UserInput*, int>(retval.deviceInput, 1));

    return retval;
}

void Binding::update() {
    inputDown = deviceInput->isDown();

    // Check if this down state is a hit.
    inputHit = inputDown && !prevInputDown;

    // Update the previous tick's down state.
    prevInputDown = inputDown;
}

bool Binding::isDown() const {
    return inputDown;
}

bool Binding::isUp() const {
    return !inputDown;
}

bool Binding::isHit() const  {
    return inputHit;
}

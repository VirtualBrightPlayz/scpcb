#ifndef BINDING_H_INCLUDED
#define BINDING_H_INCLUDED

#include <vector>
#include <UserInput/UserInput.h>

#include "../PlayerController/PlayerController.h"
#include "../Wrap/IO.h"

class Binding {
private:
    static IO io;

    static std::map<PGE::UserInput*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::UserInput* deviceInput = nullptr;
    PGE::UserInput::DEVICE device;

    // Whether the tracked input was held down in the previous tick or not.
    bool prevInputDown = false;
    // Whether the tracked input is down.
    bool inputDown = false;
    // Whether the tracked input first entered the down state this tick.
    bool inputHit = false;

    // TODO: Add vector of console commands.
    PlayerController::INPUT playerInput = PlayerController::INPUT::NONE;

public:
    static void initialize(const IO& io);
    static void cleanup();

    Binding()=default;
    Binding(const Binding& cpy);
    Binding& operator=(const Binding& other);
    ~Binding();

    static Binding createMouseBind(PGE::MouseInput::BUTTON button);
    static Binding createKeyboardBind(PGE::KeyboardInput::SCANCODE code);
    static Binding createControllerBind(PGE::GamepadInput::CONTROLLER_BUTTON button);

    void bindInput(PlayerController::INPUT input);
    void unbind();

    void update();

    bool isDown() const;
    bool isUp() const;
    bool isHit() const;
};

#endif // BINDING_H_INCLUDED

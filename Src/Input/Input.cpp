#include "Input.h"

PGE::String getBindingName(const Input& input) {
    switch (input) {
        case Input::Forward: {
            return PGE::String("Forward");
        } break;
        case Input::Backward: {
            return PGE::String("Backward");
        } break;
        case Input::Left: {
            return PGE::String("Left");
        } break;
        case Input::Right: {
            return PGE::String("Right");
        } break;
        case Input::Sprint: {
            return PGE::String("Sprint");
        } break;
        case Input::Crouch: {
            return PGE::String("Crouch");
        } break;
        case Input::Blink: {
            return PGE::String("Blink");
        } break;
        case Input::Interact: {
            return PGE::String("Interact");
        } break;
        case Input::Inventory: {
            return PGE::String("Inventory");
        } break;
        default: {
            return PGE::String("None");
        }
    }
}

const Input operator&(const Input& a, const Input& b) {
    return (Input)((int)a & (int)b);
}

const Input operator|(const Input& a, const Input& b) {
    return (Input)((int)a | (int)b);
}

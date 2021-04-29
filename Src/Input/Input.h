#ifndef INPUT_H_INCLUDED
#define INPUT_H_INCLUDED

#include <String/String.h>

enum class Input {
    None = 0x0,
    Forward = 0x1,
    Backward = 0x2,
    Left = 0x4,
    Right = 0x8,
    Sprint = 0x10,
    Crouch = 0x20,
    Blink = 0x40,
    Interact = 0x80,

    // Inventory Hotkeys.
    Inventory = 0x100,
    ToggleSiteNavigator = 0x200,
    ToggleRadio = 0x400,

    ToggleConsole = 0x800
};

PGE::String getBindingName(const Input& input);
bool inputWasFired(const Input& input, const Input& firedInput);

const Input operator&(const Input& a, const Input& b);
const Input operator|(const Input& a, const Input& b);

#endif // INPUT_H_INCLUDED

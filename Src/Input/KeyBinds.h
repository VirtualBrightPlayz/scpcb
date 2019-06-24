#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include "Binding.h"

class KeyBinds {
private:
    std::vector<Binding> miscBindings;

public:
    static void initialize();
    static void cleanup();

    // -- Definite bindings.

    Binding mouse1;
    Binding mouse2;

    Binding escape;

    void update();
};

extern KeyBinds keyBinds;

#endif // KEYBINDS_H_INCLUDED

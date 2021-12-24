#ifndef KEYBINDS_H_INCLUDED
#define KEYBINDS_H_INCLUDED

#include <unordered_map>

#include <PGE/Input/InputManager.h>
#include <PGE/Input/Input.h>
#include <PGE/String/Key.h>

#include "Input.h"

class ConsoleDefinitions;

class KeyBinds {
    private:
        // Inputs that are down for this tick.
        Input downInputs;

        // Inputs that are in the hit state for this tick.
        Input hitInputs;

        PGE::InputManager* inputManager;
        ConsoleDefinitions* conDef;

        typedef std::unordered_multimap<Input, PGE::Input*> UserInputMap;
        UserInputMap bindings;

        typedef std::unordered_multimap<PGE::String::RedundantKey, PGE::Input*> ConsoleBindingsMap;
        ConsoleBindingsMap consoleBindings;

        void bindInput(Input input, PGE::Input* key);
        void unbindInput(Input input, PGE::Device device, int key);

        std::unordered_map<PGE::String::Key, PGE::Input*> inputStrings;
        void registerInputString(const PGE::String& string, PGE::Keycode key);
        void registerInputString(const PGE::String& string, PGE::MouseButton key);
        void registerInputString(const PGE::String& string, PGE::ControllerButton key);

    public:
        KeyBinds(PGE::InputManager* inIo);
        ~KeyBinds();

        void setConsoleDefinitions(ConsoleDefinitions* inConDef);

        PGE::MouseInput* mouse1;
        PGE::MouseInput* mouse2;
        PGE::KeyboardInput* escape;

        // Text input keys.
        PGE::KeyboardInput* leftArrow;
        PGE::KeyboardInput* rightArrow;
        PGE::KeyboardInput* upArrow;
        PGE::KeyboardInput* downArrow;
        PGE::KeyboardInput* leftShift;
        PGE::KeyboardInput* rightShift;
        // Checks if any of the shift keys are held down.
        bool anyShiftDown() const;
        PGE::KeyboardInput* backspace;
        PGE::KeyboardInput* del;
        PGE::KeyboardInput* enter;

        // Shortcuts.
        PGE::KeyboardInput* leftShortcutKey;
        PGE::KeyboardInput* rightShortcutKey;
        // Checks if any of the shortcut keys are held down.
        bool anyShortcutDown() const;
        PGE::KeyboardInput* keyA;
        PGE::KeyboardInput* keyC;
        PGE::KeyboardInput* keyX;
        PGE::KeyboardInput* keyV;
        PGE::KeyboardInput* keyZ;
#ifndef __APPLE__
        PGE::KeyboardInput* keyY;
#endif

        bool copyIsHit() const;
        bool cutIsHit() const;
        bool pasteIsHit() const;
        bool undoIsHit() const;
        bool redoIsHit() const;
        bool selectAllIsHit() const;

        void bindInput(Input input, PGE::MouseButton key);
        void bindInput(Input input, PGE::Keycode key);
        void bindInput(Input input, PGE::ControllerButton key);
        void unbindInput(Input input, PGE::MouseButton key);
        void unbindInput(Input input, PGE::Keycode key);
        void unbindInput(Input input, PGE::ControllerButton key);

        void bindCommand(PGE::String command, PGE::Input* key);
        void unbindCommand(PGE::String command, PGE::Input* key);

        // Iterate through the keybinds and find out which ones fired this tick.
        void update();
        Input getDownInputs() const;
        Input getHitInputs() const;

        PGE::Input* stringToInput(const PGE::String& key) const;
};

#endif // KEYBINDS_H_INCLUDED

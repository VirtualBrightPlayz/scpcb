#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED

#include <IO/IO.h>

#include "Menu.h"

class UIMesh;
class Font;
class KeyBinds;
class Config;
class TxtManager;
class GUIFrame;
class GUITextInput;

class Console;

class InternalCommand {
public:
    virtual PGE::String getName() const = 0;
    virtual PGE::String getHelpText() const = 0;
    virtual void execute(Console* console, const std::vector<PGE::String>& params) const = 0;
};

class Console : public Menu {
private:

    GUIFrame* frame;
    GUITextInput* input;

    UIMesh* uiMesh;
    Font* font;
    KeyBinds* keyBinds;

    std::vector<InternalCommand*> interCommands;
    void registerInternalCommands();

    void executeCommand(const PGE::String& com, const std::vector<PGE::String>& params);

public:
    Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io);
    ~Console();

    void update(const PGE::Vector2f& mousePosition) override;
    void render() const override;

    void todo_test();
};

#endif // CONSOLE_H_INCLUDED

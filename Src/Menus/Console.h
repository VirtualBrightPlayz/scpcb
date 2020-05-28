#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED

#include <IO/IO.h>

#include "Menu.h"

class UIMesh;
class Font;
class KeyBinds;
class Config;
class TxtManager;

class Console : Menu {
private:
    class InternalCommand {
    public:
        virtual PGE::String getHelpText() const = 0;
        virtual void execute(Console* console) const = 0;
    };

public:
    Console(World* wrld, UIMesh* um, Font* font, KeyBinds* kb, Config* con, TxtManager* tm, PGE::IO* io);
    ~Console() = default;

    /*void update(const PGE::Vector2f& mousePosition) override;
    void render() const override;*/

private:
    class TestCommand : InternalCommand {
      /*  PGE::String getHelpText() const override;
        void execute(Console* console) const override;*/
    };
};

#endif // CONSOLE_H_INCLUDED

#ifndef CONSOLECOMMANDS_H_INCLUDED
#define CONSOLECOMMANDS_H_INCLUDED
#include <vector>
#include <StringType.h>

#include "Console.h"

namespace CBN {

class ConsoleCmd {
public:
    virtual void execute(std::vector<String> args)=0;

    String name;
    std::vector<String> aliases;
    String description;

    static void generateCommands();
    static void clearCommands();
    static void executeCommand(const String& name, std::vector<String> args);
};

extern std::vector<ConsoleCmd*> commandList;

class Cmd_Status : public ConsoleCmd {
public:
    Cmd_Status() {
        name = "status";
        description = "Prints player, camera, and room information.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_DebugHUD : public ConsoleCmd {
public:
    Cmd_DebugHUD() {
        name = "debug";
        aliases = { "debughud" };
        description = "Displays various debug states of the game's properties.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Noclip : public ConsoleCmd {
public:
    Cmd_Noclip() {
        name = "noclip";
        description = "Disables collision detection for the player.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_GodMode : public ConsoleCmd {
public:
    Cmd_GodMode() {
        name = "godmode";
        aliases = { "god" };
        description = "Makes the player invincible.";
    }
    virtual void execute(std::vector<String> args);
};

}
#endif // CONSOLECOMMANDS_H_INCLUDED

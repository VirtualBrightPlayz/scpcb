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
protected:
    String name = "status";
    String description = "Prints player, camera, and room information.";
public:
    virtual void execute(std::vector<String> args);
};

class Cmd_DebugHUD : public ConsoleCmd {
public:
    String name = "debug";
    std::vector<String> aliases = { "debughud" };
    String description = "Displays various debug states of the game's properties.";
    virtual void execute(std::vector<String> args);
};

class Cmd_Noclip : public ConsoleCmd {
public:
    String name = "noclip";
    String description = "Disables collision detection for the player.";
    virtual void execute(std::vector<String> args);
};

class Cmd_GodMode : public ConsoleCmd {
public:
    String name = "godmode";
    std::vector<String> aliases = { "god" };
    String description = "Makes the player invincible.";
    virtual void execute(std::vector<String> args);
};

}
#endif // CONSOLECOMMANDS_H_INCLUDED

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
    std::vector<String> helpDesc;

    static void generateCommands();
    static void clearCommands();
    static void executeCommand(const String& name, std::vector<String> args);
};

extern std::vector<ConsoleCmd*> commandList;

class Cmd_Help : public ConsoleCmd {
public:
    Cmd_Help() {
        name = "help";
        helpDesc = { "Prints help information for each console command.",
            "Type \"help\" to see a list of available console commands.",
            "Type \"help [command]\" to see a description of a particular command. (e.g. help teleport)" };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Status : public ConsoleCmd {
public:
    Cmd_Status() {
        name = "status";
        helpDesc = { "Prints player, camera, and room information." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_DebugHUD : public ConsoleCmd {
public:
    Cmd_DebugHUD() {
        name = "debug";
        aliases = { "debughud" };
        helpDesc = { "Displays various debug states of the game's properties." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Wireframe : public ConsoleCmd {
public:
    Cmd_Wireframe() {
        name = "wireframe";
        helpDesc = { "Only renders the edges of polygons." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Noclip : public ConsoleCmd {
public:
    Cmd_Noclip() {
        name = "noclip";
        aliases = { "fly" };
        helpDesc = { "Disables collision detection for the player." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_GodMode : public ConsoleCmd {
public:
    Cmd_GodMode() {
        name = "godmode";
        aliases = { "god" };
        helpDesc = { "Makes the player invincible." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_NoTarget : public ConsoleCmd {
public:
    Cmd_NoTarget() {
        name = "notarget";
        helpDesc = { "Makes the player unable to be targeted by NPCs." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Heal : public ConsoleCmd {
public:
    Cmd_Heal() {
        name = "heal";
        helpDesc = { "Removes all bloodloss and injuries." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Teleport : public ConsoleCmd {
public:
    Cmd_Teleport() {
        name = "teleport";
        aliases = { "tele" };
        helpDesc = { "Teleports the player to the specified room." };
    }
    virtual void execute(std::vector<String> args);
};

// TODO: Itemlist command.
class Cmd_SpawnItem : public ConsoleCmd {
public:
    Cmd_SpawnItem() {
        name = "spawnitem";
        aliases = { "giveitem, item" };
        helpDesc = { "Creates the specified item at the player's position." };
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Omni : public ConsoleCmd {
public:
    Cmd_Omni() {
        name = "spawnomni";
        aliases = { "omni" };
        helpDesc = { "Creates an omni card." };
    }
    virtual void execute(std::vector<String> args);
};

}
#endif // CONSOLECOMMANDS_H_INCLUDED

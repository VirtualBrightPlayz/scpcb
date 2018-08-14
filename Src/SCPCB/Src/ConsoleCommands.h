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

class Cmd_Help : public ConsoleCmd {
public:
    Cmd_Help() {
        name = "help";
        description = String("Prints help information for each console command. ")
            + "Type \"help\" to see a list of available console commands. "
            + "You can also type \"help teleport\" to see a description of a particular command.";
    }
    virtual void execute(std::vector<String> args);
};

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

class Cmd_Wireframe : public ConsoleCmd {
private:
    bool wireframeState = false;
public:
    Cmd_Wireframe() {
        name = "wireframe";
        description = "Only renders the edges of polygons.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Noclip : public ConsoleCmd {
public:
    Cmd_Noclip() {
        name = "noclip";
        aliases = { "fly" };
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

class Cmd_Heal : public ConsoleCmd {
public:
    Cmd_Heal() {
        name = "heal";
        description = "Removes all bloodloss and injuries.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Teleport : public ConsoleCmd {
public:
    Cmd_Teleport() {
        name = "teleport";
        aliases = { "tele" };
        description = "Teleports the player to the specified room.";
    }
    virtual void execute(std::vector<String> args);
};

// TODO: Itemlist command.
class Cmd_SpawnItem : public ConsoleCmd {
public:
    Cmd_SpawnItem() {
        name = "spawnitem";
        aliases = { "giveitem, item" };
        description = "Creates the specified item at the player's position.";
    }
    virtual void execute(std::vector<String> args);
};

class Cmd_Omni : public ConsoleCmd {
public:
    Cmd_Omni() {
        name = "spawnomni";
        aliases = { "omni" };
        description = "Creates an omni card.";
    }
    virtual void execute(std::vector<String> args);
};

}
#endif // CONSOLECOMMANDS_H_INCLUDED

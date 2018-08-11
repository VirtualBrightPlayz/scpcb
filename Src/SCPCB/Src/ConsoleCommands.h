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
};

extern std::vector<ConsoleCmd*> commandList;

void generateConsoleCommands();
void clearConsoleCommands();
void executeConsoleCommand(const String& name, std::vector<String> args);

class Cmd_Status : public ConsoleCmd {
public:
    String name = "status";
    String description = "Prints player, camera, and room information.";
    virtual void execute(std::vector<String> args);
};

}
#endif // CONSOLECOMMANDS_H_INCLUDED

#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct ConsoleMsg {
private:
    ConsoleMsg();
public:
    ~ConsoleMsg();
    static std::vector<ConsoleMsg*> list;
    static void create(const String& txt, int r = -1, int g = -1, int b = -1, bool isCommand = false);

    String txt;
    bool isCommand;
    int r;
    int g;
    int b;
};

// Globals.
extern String ConsoleInput;
extern float ConsoleScroll;
extern int ConsoleScrollDragging;
extern int ConsoleMouseMem;
extern int ConsoleReissue;
extern int ConsoleR;
extern int ConsoleG;
extern int ConsoleB;
extern bool DebugHUD;

// Functions.
void UpdateConsole();

void DrawConsole();

}
#endif // CONSOLE_H_INCLUDED

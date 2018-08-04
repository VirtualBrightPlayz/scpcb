#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct ConsoleMsg {
private:
    static std::vector<ConsoleMsg*> list;

public:
    ConsoleMsg();
    ~ConsoleMsg();
    static int getListSize();
    static ConsoleMsg* getObject(int index);

    String txt;
    int isCommand;
    int r;
    int g;
    int b;
};

// Globals.
extern String ConsoleInput;
extern float ConsoleScroll;
extern int ConsoleScrollDragging;
extern int ConsoleMouseMem;
extern ConsoleMsg* ConsoleReissue;
extern int ConsoleR;
extern int ConsoleG;
extern int ConsoleB;
extern int ConsoleFlush;
extern int ConsoleFlushSnd;
extern int ConsoleMusFlush;
extern int DebugHUD;

// Functions.
void CreateConsoleMsg(String txt, int r = -1, int g = -1, int b = -1, int isCommand = false);

void DrawConsole();

void UpdateConsole();

}
#endif // CONSOLE_H_INCLUDED

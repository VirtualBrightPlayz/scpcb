#ifndef CONSOLE_H_INCLUDED
#define CONSOLE_H_INCLUDED
#include <vector>

namespace CBN {

class Console {
private:
    String input;
    float scroll;
    bool scrollDragging;
    int mouseMem;
    int reissue;

public:
    int msgR;
    int msgG;
    int msgB;

public:
    Console();
    ~Console();

    std::vector<ConsoleMsg*> msgList;
    void update();
    void draw();

    // Stuff that can only be toggled through the console.
    bool debugHUD;
    bool wireframeState;
    bool halloween173Tex;
    bool noTarget;
};

// Structs.
struct ConsoleMsg {
private:
    ConsoleMsg();
public:
    ~ConsoleMsg();
    static void create(const String& txt, int r = -1, int g = -1, int b = -1, bool isCommand = false);

    String txt;
    bool isCommand;
    int r;
    int g;
    int b;
};

extern Console* console;

}
#endif // CONSOLE_H_INCLUDED

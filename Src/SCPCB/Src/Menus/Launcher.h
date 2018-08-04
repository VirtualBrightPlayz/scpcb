#ifndef LAUNCHER_H_INCLUDED
#define LAUNCHER_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Launcher {
private:
    static std::vector<Launcher*> list;

public:
    Launcher();
    ~Launcher();
    static int getListSize();
    static Launcher* getObject(int index);

    int width;
    int height;

    int background;

    IntArray* resWidths;
    IntArray* resHeights;

    int selectedGFXMode;
};

// Globals.
extern Launcher* launcher;

// Functions.
Launcher* CreateLauncher();

void DestroyLauncher(Launcher* launch);

void UpdateLauncher();

void DrawLauncher();

}
#endif // LAUNCHER_H_INCLUDED

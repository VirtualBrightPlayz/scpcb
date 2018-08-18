#ifndef LAUNCHER_H_INCLUDED
#define LAUNCHER_H_INCLUDED
#include <vector>
#include "GUI/GUIButton.h"
#include "GUI/GUITick.h"

namespace CBN {

struct Launcher {
private:
    GUIButton btnLaunch;
    GUIButton btnExit;
    GUITick tckUseLauncher;
    GUITick tckFullscreen;
    void verifyResolution();
public:
    Launcher();
    ~Launcher();
    void update();
    void draw();

    int width;
    int height;

    class bbImage* background;

    std::vector<int> resWidths;
    std::vector<int> resHeights;

    int selectedGFXMode;
};
extern Launcher* launcher;

}
#endif // LAUNCHER_H_INCLUDED

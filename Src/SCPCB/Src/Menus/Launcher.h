#ifndef LAUNCHER_H_INCLUDED
#define LAUNCHER_H_INCLUDED
#include <vector>

namespace CBN {

class GUIButton;

struct Launcher {
private:
    GUIButton* btn_launch;
    GUIButton* btn_exit;
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

// Globals.
extern Launcher* launcher;

}
#endif // LAUNCHER_H_INCLUDED

#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include <StringType.h>
#include "GUIFrame.h"

namespace CBN {

class GUIButton : public GUIFrame {
private:
    String text;
    bool bigFont;

public:
    GUIButton(int x, int y, int width, int height, const String& txt, bool bigFont = false, bool relative = true);
    ~GUIButton();

    void update();
    void draw();
};

}
#endif // GUIBUTTON_H_INCLUDED

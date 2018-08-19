#ifndef GUIINPUT_H_INCLUDED
#define GUIINPUT_H_INCLUDED

#include <StringType.h>
#include "GUIButtonBase.h"

namespace CBN {

class GUIInput : public GUIButtonBase {
private:
    String displayText;
    int textOffset;
    int caretPos;
    bool selected;
    int caretTimer;

public:
    GUIInput();
    GUIInput(int x, int y, int width, int height, const String& displayTxt, int txtOffset, const String& defaultTxt = "", bool relative = true);

    String input;

    virtual void update();
    virtual void draw();
};

}
#endif // GUIINPUT_H_INCLUDED

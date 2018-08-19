#ifndef GUIINPUT_H_INCLUDED
#define GUIINPUT_H_INCLUDED

#include <StringType.h>
#include "GUIButtonBase.h"

namespace CBN {

class GUIInput : public GUIButtonBase {
private:
    String displayText;
    int textOffset;
    String input;
    int caretPos;
    int caretTimer;
    int charLimit;

public:
    GUIInput();
    GUIInput(int x, int y, int width, int height, const String& displayTxt, int txtOffset, int limit, const String& defaultTxt = "", bool relative = true);

    bool selected;

    void toLower();
    void setInput(const String& txt);
    String getInput();
    void clear();
    virtual void update();
    virtual void draw();
};

}
#endif // GUIINPUT_H_INCLUDED

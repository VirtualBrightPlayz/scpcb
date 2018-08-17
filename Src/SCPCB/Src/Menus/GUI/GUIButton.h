#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include <StringType.h>
#include "GUIButtonBase.h"

namespace CBN {

class GUIButton : public GUIButtonBase {
private:
    bool bigFont;

public:
    GUIButton(int x, int y, int width, int height, const String& txt, bool bigFont = false, bool relative = true);

    virtual void update();
    virtual void draw();
};

}
#endif // GUIBUTTON_H_INCLUDED

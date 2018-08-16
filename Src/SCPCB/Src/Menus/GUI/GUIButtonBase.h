#ifndef GUIBUTTONBASE_H_INCLUDED
#define GUIBUTTONBASE_H_INCLUDED

#include <StringType.h>
#include "GUIFrame.h"

namespace CBN {

class GUIButtonBase : public GUIFrame {
protected:
    String text;

public:
    GUIButtonBase(int x, int y, int width, int height, const String& txt, bool relative = true);

    void update();
    virtual void draw() = 0;
};

}
#endif // GUIBUTTONBASE_H_INCLUDED

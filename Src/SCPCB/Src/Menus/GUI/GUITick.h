#ifndef GUITICK_H_INCLUDED
#define GUITICK_H_INCLUDED

#include "GUIButton.h"

namespace CBN {

class GUITick : public GUIButtonBase {
public:
    GUITick(int x, int y, const String& txt, bool ticked = false, bool relative = true);

    bool ticked;
    bool locked;

    virtual void update();
    virtual void draw();
};

}
#endif // GUITICK_H_INCLUDED

#ifndef GUIBUTTONBASE_H_INCLUDED
#define GUIBUTTONBASE_H_INCLUDED

#include <StringType.h>
#include "GUIBase.h"

namespace CBN {

class GUIButtonBase : public GUIBase {
protected:
    String text;

public:
    GUIButtonBase(int x, int y, int width, int height, const String& txt, bool relative = true);

    virtual void update();
    virtual void draw();
};

}
#endif // GUIBUTTONBASE_H_INCLUDED

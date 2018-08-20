#ifndef GUIBUTTONBASE_H_INCLUDED
#define GUIBUTTONBASE_H_INCLUDED

#include <StringType.h>
#include "GUIBase.h"

namespace CBN {

class GUIButtonBase : public GUIBase {
public:
    GUIButtonBase(int x, int y, int width, int height, bool relative = true);

    bool locked;

    virtual void update();
    virtual void draw();
};

}
#endif // GUIBUTTONBASE_H_INCLUDED

#include <bbgraphics.h>

#include "GUIButtonBase.h"
#include "GUIBase.h"
#include "../../Assetmgmt/Audio.h"
#include "../../Assetmgmt/Assets.h"

namespace CBN {

GUIButtonBase::GUIButtonBase(int x, int y, int width, int height, bool relative)
    : GUIBase(x, y, width, height, relative) {
    locked = false;
}

void GUIButtonBase::update() {
    GUIBase::update();
    if (isMouseHit()) {
        PlaySound_SM(sndMgmt->button);
    }
}

void GUIButtonBase::draw() {
    if (isMouseHover()) {
        bbColor(30, 30, 30);
    }
    else
    {
        bbColor(0, 0, 0);
    }

    int coordOff = getRelativeVal(4);
    int sizeOff = getRelativeVal(-8);
    bbRect(getX() + coordOff, getY() + coordOff, getWidth() + sizeOff, getHeight() + sizeOff);
    bbColor(255, 255, 255);
}

}
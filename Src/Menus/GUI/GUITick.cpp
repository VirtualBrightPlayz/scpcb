#include <bbgraphics.h>

#include "GUITick.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../AssetMgmt/TextMgmt.h"

namespace CBN {

GUITick::GUITick() : GUITick(0, 0, "", 0, 0) { };

GUITick::GUITick(int x, int y, const String& txt, int txtOffset, bool ticked, bool relative)
    : GUIButtonBase(x, y, 20, 20, relative) {
    text = txt;
    textOffset = txtOffset;

    this->ticked = ticked;
}

void GUITick::update() {
    if (!visible || locked) { return; }

    GUIButtonBase::update();
    if (isMouseHit()) {
        ticked = !ticked;
    }
}

void GUITick::draw() {
    if (!visible) { return; }

    // White border and black inside.
    DrawTiledImageRect(uiAssets->tileWhite, (getX() % 256), (getY() % 256), 512, 512, getX(), getY(), getWidth(), getHeight());
    bbColor(0, 0, 0);
    int coordOff = 2;
    int sizeOff = -4;
    bbRect(getX() + coordOff, getY() + coordOff, getWidth() + sizeOff, getHeight() + sizeOff);
    bbColor(255, 255, 255);

    GUIButtonBase::draw();

    if (ticked) {
        coordOff = 4;
        sizeOff = -8;
        DrawTiledImageRect(uiAssets->tileWhite, (getX() % 256), (getY() % 256), 512, 512, getX() + coordOff, getY() + coordOff, getWidth() + sizeOff, getHeight() + sizeOff);
    }

    if (!text.isEmpty()) {
        if (locked) {
            bbColor(37, 37, 38);
        }
        bbText(getX() + getRelativeVal(textOffset), getY(), txtMgmt->getLocalTxt(text));
        bbColor(255, 255, 255);
    }
}

}
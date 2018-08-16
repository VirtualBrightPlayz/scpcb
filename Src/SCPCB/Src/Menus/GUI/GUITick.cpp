#include "GUITick.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"

namespace CBN {

GUITick::GUITick(int x, int y, const String& txt, bool ticked, bool relative)
    : GUIButtonBase(x, y, 0, 0, txt, relative) {
    this->width = 20 * (relative ? (int)MenuScale : 1);
    this->height = 20 * (relative ? (int)MenuScale : 1);

    this->ticked = ticked;
    locked = false;
}

void GUITick::update() {
    if (!locked) {
        GUIButtonBase::update();
        if (isMouseHit()) {
            ticked = !ticked;
        }
    }
}

void GUITick::draw() {
    GUIButtonBase::draw();
    if (ticked) {
        DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x + 4, y + 4, width - 8, height - 8);
    }
}

}
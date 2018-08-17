#include <bbgraphics.h>

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
    bbColor(0, 0, 0);
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);

    bbRect(x + 2, y + 2, width - 4, height - 4);
    bbColor(255, 255, 255);

    GUIButtonBase::draw();

    if (ticked) {
        DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x + 4, y + 4, width - 8, height - 8);
    }
}

}
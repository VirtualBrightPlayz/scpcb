#include <bbgraphics.h>

#include "GUITick.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"

namespace CBN {

GUITick::GUITick() : GUITick(0, 0, "", 0, 0) { };

GUITick::GUITick(int x, int y, const String& txt, int txtOffset, bool ticked, bool relative)
    : GUIButtonBase(x, y, 20, 20, relative) {
    text = txt;
    textOffset = relative ? (int)(txtOffset * MenuScale) : txtOffset;

    this->ticked = ticked;
    locked = false;
}

void GUITick::update() {
    if (!visible) { return; }
    if (!locked) {
        GUIButtonBase::update();
        if (isMouseHit()) {
            ticked = !ticked;
        }
    }
}

void GUITick::draw() {
    if (!visible) { return; }

    // White border and black inside.
    DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x, y, width, height);
    bbColor(0, 0, 0);
    bbRect(x + 2, y + 2, width - 4, height - 4);
    bbColor(255, 255, 255);

    GUIButtonBase::draw();

    if (ticked) {
        DrawTiledImageRect(uiAssets->tileWhite, (x % 256), (y % 256), 512, 512, x + 4, y + 4, width - 8, height - 8);
    }

    if (!text.isEmpty()) {
        bbText(x + textOffset, y, text);
    }
}

}
#include <bbgraphics.h>

#include "GUIButton.h"
#include "../../Assetmgmt/Assets.h"
#include "../Menu.h"

namespace CBN {

GUIButton::GUIButton() : GUIButton(0, 0, 0, 0, "") { };

GUIButton::GUIButton(int x, int y, int width, int height, const String& txt, bool bigFont, bool relative)
    : GUIButtonBase(x, y, width, height, relative) {
    text = txt;
    this->bigFont = bigFont;
}

void GUIButton::update() {
    if (!visible || locked) { return; }
    GUIButtonBase::update();
}

void GUIButton::draw() {
    if (!visible) { return; }

    // White border and black inside.
    DrawTiledImageRect(uiAssets->tileWhite, 0, (y % 256), 512, 512, x, y, width, height);
    DrawTiledImageRect(uiAssets->tileBlack, 0, (y % 256), 512, 512, (int)(x + 3 * MenuScale), (int)(y + 3 * MenuScale), (int)(width - 6 * MenuScale), (int)(height - 6 * MenuScale));
    GUIButtonBase::draw();

    if (bigFont) {
        bbSetFont(uiAssets->font[1]);
    } else {
        bbSetFont(uiAssets->font[0]);
    }

    if (locked) {
        bbColor(37, 37, 38);
    }

    bbText(x + width / 2, y + height / 2, text, true, true);
    bbSetFont(uiAssets->font[0]);
    bbColor(255, 255, 255);
}

}
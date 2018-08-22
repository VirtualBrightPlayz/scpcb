#include <bbgraphics.h>

#include "GUIButton.h"
#include "../../AssetMgmt/Assets.h"
#include "../../AssetMgmt/TextMgmt.h"
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
    int coordOff = 3;
    int sizeOff = -6;
    DrawTiledImageRect(uiAssets->tileWhite, 0, (getY() % 256), 512, 512, getX(), getY(), getWidth(), getHeight());
    DrawTiledImageRect(uiAssets->tileBlack, 0, (getY() % 256), 512, 512, getX() + coordOff, getY() + coordOff, getWidth() + sizeOff, getHeight() + sizeOff);
    GUIButtonBase::draw();

    if (text.isEmpty()) { return; }
    if (bigFont) {
        bbSetFont(uiAssets->font[1]);
    } else {
        bbSetFont(uiAssets->font[0]);
    }

    if (locked) {
        bbColor(37, 37, 38);
    }

    bbText(getX() + getWidth() / 2, getY() + getHeight() / 2, txtMgmt->lang[text], true, true);
    bbSetFont(uiAssets->font[0]);
    bbColor(255, 255, 255);
}

}
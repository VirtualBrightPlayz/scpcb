#include <bbgraphics.h>

#include "GUIButton.h"
#include "../../Assetmgmt/Assets.h"
#include "../Menu.h"

namespace CBN {

GUIButton::GUIButton(int x, int y, int width, int height, const String& txt, bool bigFont, bool relative)
    : GUIButtonBase(x, y, width, height, txt, relative) {
    this->bigFont = bigFont;
}

void GUIButton::update() {
    GUIButtonBase::update();
}

void GUIButton::draw() {
    GUIButtonBase::draw();
    int scale = relative ? (int)MenuScale : 1;
    DrawTiledImageRect(uiAssets->tileWhite, 0, (y % 256), 512, 512, x, y, width, height);
    DrawTiledImageRect(uiAssets->tileBlack, 0, (y % 256), 512, 512, x + 3 * scale, y + 3 * scale, width - 6 * scale, height - 6 * scale);

    bbColor(255, 255, 255);
    if (bigFont) {
        bbSetFont(uiAssets->font[1]);
    } else {
        bbSetFont(uiAssets->font[0]);
    }

    bbText(x + width / 2, y + height / 2, text, true, true);
    bbSetFont(uiAssets->font[0]);
}

}
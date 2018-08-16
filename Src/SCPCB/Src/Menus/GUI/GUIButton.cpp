#include <bbgraphics.h>

#include "GUIButton.h"
#include "GUIFrame.h"
#include "../../Assetmgmt/Assets.h"

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
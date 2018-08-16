#include <bbgraphics.h>

#include "GUIButton.h"
#include "GUIFrame.h"
#include "../../Assetmgmt/Audio.h"
#include "../../Assetmgmt/Assets.h"

namespace CBN {

GUIButton::GUIButton(int x, int y, int width, int height, const String& txt, bool bigFont, bool relative = true)
    : GUIFrame(x, y, width, height, 0, 0, relative) {
    text = txt;
    this->bigFont = bigFont;
}

void GUIButton::update() {
    GUIFrame::update();
    if (isMouseHit()) {
        PlaySound_SM(sndMgmt->button);
    }
}

void GUIButton::draw() {
    GUIFrame::draw();
    if (isMouseHover()) {
        bbColor(30, 30, 30);
        bbRect(x + 4, y + 4, width - 8, height - 8);
    }

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
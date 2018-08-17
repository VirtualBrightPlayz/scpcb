#include <bbgraphics.h>

#include "GUIButtonBase.h"
#include "GUIBase.h"
#include "../../Assetmgmt/Audio.h"
#include "../../Assetmgmt/Assets.h"

namespace CBN {

GUIButtonBase::GUIButtonBase(int x, int y, int width, int height, const String& txt, bool relative)
    : GUIBase(x, y, width, height, relative) {
    text = txt;
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

    bbRect(x + 4, y + 4, width - 8, height - 8);
    bbColor(255, 255, 255);
}

}
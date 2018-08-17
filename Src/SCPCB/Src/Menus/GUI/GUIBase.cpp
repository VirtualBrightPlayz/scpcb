#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIBase.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../GameMain.h"

namespace CBN {

GUIBase::GUIBase(int x, int y, int width, int height, bool relative) {
    this->x = x * (relative ? (int)MenuScale : 1);
    this->y = y * (relative ? (int)MenuScale : 1);
    this->width = width * (relative ? (int)MenuScale : 1);
    this->height = height * (relative ? (int)MenuScale : 1);
    this->relative = relative;

    mouseHover = false;
    mouseHit = false;
    mouseUp = false;
}

bool GUIBase::isMouseHover() {
    return mouseHover;
}

bool GUIBase::isMouseHit() {
    return mouseHit;
}

bool GUIBase::isMouseUp() {
    return mouseUp;
}

void GUIBase::update() {
    mouseHover = false;
    mouseHit = false;
    mouseUp = false;

    if (MouseOn(x, y, width, height)) {
        mouseHover = true;
        if (MouseHit1) {
            mouseHit = true;
        } else if (MouseUp1) {
            mouseUp = true;
        }
    }
}

}
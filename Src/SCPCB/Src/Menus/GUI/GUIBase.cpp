#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIBase.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../GameMain.h"

namespace CBN {

GUIBase::GUIBase(int x, int y, int width, int height, bool relative) {
    this->x = relative ? (int)(x * MenuScale) : x;
    this->y = relative ? (int)(y * MenuScale) : y;
    this->width = relative ? (int)(width * MenuScale) : width;
    this->height = relative ? (int)(height * MenuScale) : height;
    this->relative = relative;

    mouseHover = false;
    mouseHit = false;
    mouseUp = false;
    visible = true;
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
    if (!visible) { return; }
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
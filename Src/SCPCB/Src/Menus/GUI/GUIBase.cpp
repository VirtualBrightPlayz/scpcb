#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIBase.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../GameMain.h"

namespace CBN {

GUIBase::GUIBase(int x, int y, int width, int height, bool relative) {
    this->x = x;
    this->y = y;
    this->width =  width;
    this->height = height;
    this->relative = relative;

    mouseHover = false;
    mouseHit = false;
    mouseUp = false;
    visible = true;
}

int GUIBase::getRelativeVal(int val) {
    return relative ? (int)(val * MenuScale) : val;
}

int GUIBase::getX() {
    return getRelativeVal(x);
}

int GUIBase::getY() {
    return getRelativeVal(y);
}

int GUIBase::getWidth() {
    return getRelativeVal(width);
}

int GUIBase::getHeight() {
    return getRelativeVal(height);
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
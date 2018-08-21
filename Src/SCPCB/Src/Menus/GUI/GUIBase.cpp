#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIBase.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../Config/Options.h"
#include "../../GameMain.h"

namespace CBN {

float GUIBase::getScreenWidthScale() {
    return userOptions->screenWidth / 1024.f;
}

GUIBase::GUIBase(int x, int y, int width, int height, bool relative) {
    this->x = x;
    this->y = y;
    this->width =  width;
    this->height = height;
    this->relative = relative;
    this->xRelativeToScreenWidth = false;
    this->widthRelativeToScreenWidth = false;

    mouseHover = false;
    mouseHit = false;
    mouseUp = false;
    visible = true;
}

int GUIBase::getRelativeVal(int val) {
    return relative ? (int)(val * MenuScale) : val;
}

int GUIBase::getX() {
    return xRelativeToScreenWidth
        ? (int)(x * getScreenWidthScale())
        : getRelativeVal(x);
}

int GUIBase::getY() {
    return getRelativeVal(y);
}

int GUIBase::getWidth() {
    return widthRelativeToScreenWidth
        ? (int)(width * getScreenWidthScale())
        : getRelativeVal(width);
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

    if (MouseOn(getX(), getY(), getWidth(), getHeight())) {
        mouseHover = true;
        if (MouseHit1) {
            mouseHit = true;
        } else if (MouseUp1) {
            mouseUp = true;
        }
    }
}

}
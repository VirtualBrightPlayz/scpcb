#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIFrame.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../GameMain.h"

namespace CBN {

GUIFrame::GUIFrame(int x, int y, int width, int height, bool relative) {
    this->x = x * (relative ? (int)MenuScale : 1);
    this->y = y * (relative ? (int)MenuScale : 1);
    this->width = width * (relative ? (int)MenuScale : 1);
    this->height = height * (relative ? (int)MenuScale : 1);
    this->xoffset = 0;
    this->yoffset = 0;
    this->relative = relative;

    mouseHover = false;
    mouseHit = false;
    mouseUp = false;
}

bool GUIFrame::isMouseHover() {
    return mouseHover;
}

bool GUIFrame::isMouseHit() {
    return mouseHit;
}

bool GUIFrame::isMouseUp() {
    return mouseUp;
}

void GUIFrame::update() {
    mouseHover = false;
    mouseHit = false;
    mouseUp = false;

    if (MouseOn(x, y, width, height)) {
        mouseHover = true;
        if (MouseHit1) {
            mouseHit = true;
        }
        else if (MouseUp1) {
            mouseUp = true;
        }
    }
}

void GUIFrame::draw() {
    int scale = relative ? (int)MenuScale : 1;
    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, xoffset, (y % 256), 512, 512, x, y, width, height);

    DrawTiledImageRect(uiAssets->tileBlack, yoffset, (y % 256), 512, 512, x+3 * scale, y+3 * scale, width-6 * scale, height-6 * scale);
}

}
#include <bbgraphics.h>
#include <bbinput.h>

#include "GUIFrame.h"
#include "../Menu.h"
#include "../../AssetMgmt/Assets.h"
#include "../../GameMain.h"

namespace CBN {

GUIFrame::GUIFrame(int x, int y, int width, int height, int xoffset, int yoffset, bool relative) {
    this->x = x * (relative ? MenuScale : 1);
    this->y = y * (relative ? MenuScale : 1);
    this->width = width * (relative ? MenuScale : 1);
    this->height = height * (relative ? MenuScale : 1);
    this->xoffset = xoffset;
    this->yoffset = yoffset;

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
    bbColor(255, 255, 255);
    DrawTiledImageRect(uiAssets->tileWhite, xoffset, (y % 256), 512, 512, x, y, width, height);

    DrawTiledImageRect(uiAssets->tileBlack, yoffset, (y % 256), 512, 512, x, y, width, height);
}

}
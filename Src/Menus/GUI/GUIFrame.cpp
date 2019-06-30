#include "GUIFrame.h"
#include "../UIAssets.h"
#include "../../Graphics/UIMesh.h"

GUIFrame::GUIFrame() : GUI(0.f, 0.f, 0.f, 0.f, Alignment::CenterXY) { }

GUIFrame::GUIFrame(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment)
: GUI(x, y, width, height, alignment) {
    background = assets->menuWhite->createSlice(x, y, width, height);

    float fgOffset = 0.5f;
    foreground = assets->menuBlack->createSlice(x + fgOffset, y + fgOffset, width - fgOffset * 2, height - fgOffset * 2);
}

void GUIFrame::updateVisibility(bool visible) {
    background->visible = visible;
    foreground->visible = visible;
}

void GUIFrame::internalUpdate(PGE::Vector2f mousePos) { }

void GUIFrame::activeUpdate(PGE::Vector2f mousePos) { }

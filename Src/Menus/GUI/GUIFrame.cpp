#include "GUIFrame.h"
#include "../UIAssets.h"
#include "../../Graphics/UIMesh.h"

GUIFrame::GUIFrame(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, const UIAssets* assets, Alignment alignment)
: GUIComponent(um, kb, x, y, width, height, alignment) {
    float fgOffset = 0.5f;
}

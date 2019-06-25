#include "UIAssets.h"
#include "../Graphics/UIMesh.h"

UIAssets::UIAssets(const Graphics& gfx) {
    menuWhite = new UIMesh(gfx, "GFX/Menu/menuwhite.jpg", true);
    menuBlack = new UIMesh(gfx, "GFX/Menu/menublack.jpg", true);
}

UIAssets::~UIAssets() {
    delete menuWhite;
    delete menuBlack;
}

void UIAssets::update() {
    menuWhite->bake();
    menuBlack->bake();
}

void UIAssets::draw() const {
    menuBlack->render();
    menuWhite->render();
}

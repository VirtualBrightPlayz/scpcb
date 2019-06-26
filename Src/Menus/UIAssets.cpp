#include "UIAssets.h"
#include "../Graphics/UIMesh.h"

UIAssets::UIAssets(const Graphics& gfx) {
    menuWhite = new UIMesh(gfx, "GFX/Menu/menuwhite.jpg", true);
    menuBlack = new UIMesh(gfx, "GFX/Menu/menublack.jpg", true);

    PGE::Color grey = PGE::Color(30, 30, 30, 127);
    hoverLightUp = new UIMesh(gfx, grey);
}

UIAssets::~UIAssets() {
    delete menuWhite;
    delete menuBlack;
    delete hoverLightUp;
}

void UIAssets::update() {
    menuWhite->bake();
    menuBlack->bake();
    hoverLightUp->bake();
}

void UIAssets::draw() const {
    hoverLightUp->render();
    menuBlack->render();
    menuWhite->render();
}

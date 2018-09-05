#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <cmath>

#include "FirstAid.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../AssetMgmt/Assets.h"
#include "../Config/Options.h"
#include "../GameMain.h"

using namespace CBN;

FirstAid::FirstAid() : Item("GFX/Items/firstaid/firstaid.b3d", 10.f, ItemPickSound::Heafty, WornItemSlot::Hand) {
    type = "firstaid";
}

FirstAid::FirstAid(float x, float y, float z) : FirstAid() {
    bbPositionEntity(collider, x, y, z);
}

String FirstAid::getType() {
    return type;
}

String FirstAid::getInvName() {
    return txtMgmt->lang["it_firstaid"];
}

void FirstAid::updateUse() {
    timer -= timing->tickDuration;
}

void FirstAid::drawUse() {
    // Render icon if one isn't there (somehow).
    generateInvImg();

    int x = userOptions->screenWidth / 2;
    int y = userOptions->screenHeight / 2;
    bbDrawImage(invImg, x, y);

    int width = 300;
    int height = 20;
    x = userOptions->screenWidth / 2 - width / 2;
    y = userOptions->screenHeight / 2 + 30 - 100;

    bbRect(x, y, width + 4, height, false);
    // TODO: wtf
    for (int i = 1; i <= (int)std::ceil((width - 2) * (timer / 100.f) / 10); i++) {
        bbDrawImage(uiAssets->blinkBar, x + 3 + 10 * (i - 1), y + 3);
    }


}
#include <bbblitz3d.h>
#include <bbgraphics.h>
#include <cmath>

#include "FirstAid.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../AssetMgmt/Assets.h"
#include "../Config/Options.h"
#include "../Player/Player.h"
#include "../GameMain.h"

using namespace CBN;

String type = "firstaid";

FirstAid::FirstAid() : Item("GFX/Items/firstaid/firstaid.b3d", 10.f, ItemPickSound::Heafty, WornItemSlot::Hand) {
    timer = DEFAULT_TIME;
}

FirstAid* FirstAid::spawn(float x, float y, float z) {
    FirstAid* it = new FirstAid();
    it->setCoords(x, y, z);
    return it;
}

String FirstAid::getType() {
    return type;
}

String FirstAid::getInvName() {
    return txtMgmt->lang["it_firstaid"];
}

void FirstAid::onUse() {
    if (mainPlayer->isEquipped(type)) {
        mainPlayer->disableControls = true;
        prevCrouch = mainPlayer->crouching;
        mainPlayer->crouching = true;
    } else {
        mainPlayer->disableControls = false;
        mainPlayer->crouching = prevCrouch;
        timer = DEFAULT_TIME;
    }
}

void FirstAid::updateUse() {
    timer -= timing->tickDuration;
    mainPlayer->disableControls = true;
    // TODO: Messages and injuries.
    if (timer < 0) {
        mainPlayer->disableControls = false;
        mainPlayer->crouching = prevCrouch;
        markedForRemoval = true;
        txtMgmt->setMsg("Test.");
    }
}

void FirstAid::drawUse() {
    // Render icon if one isn't there (somehow).
    generateInvImg();

    int x = userOptions->screenWidth / 2;
    int y = userOptions->screenHeight / 2;
    bbDrawImage(invImg, x, y);

    int width = 300;
    int height = 20;
    x -= width / 2;
    y -= 70;

    bbRect(x, y, width + 4, height, false);
    // TODO: wtf
    for (int i = 1; i <= (int)std::ceil((width - 2) * (timer / 100.f) / 10); i++) {
        bbDrawImage(uiAssets->blinkBar, x + 3 + 10 * (i - 1), y + 3);
    }


}
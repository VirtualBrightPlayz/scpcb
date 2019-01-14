#include <bbblitz3d.h>

#include "Gasmask.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../Player/Player.h"

using namespace CBN;

String type = "gasmask";

GasMask::GasMask() : Item("GFX/Items/gasmask/gasmask.b3d", 6.f, ItemPickSound::Heafty, WornItemSlot::Head) { }

GasMask* GasMask::spawn(float x, float y, float z) {
    GasMask* it = new GasMask();
    it->setCoords(x, y, z);
    return it;
}

String GasMask::getType() {
    return type;
}

String GasMask::getInvName() {
    return txtMgmt->lang["it_gasmask"];
}

void GasMask::onPick() {
    txtMgmt->setMsg("TOUCH");
}

void GasMask::onUse() {
    if (mainPlayer->isEquipped(type)) {
        txtMgmt->setMsg(txtMgmt->lang["it_gasmask_on"]);
    } else {
        txtMgmt->setMsg(txtMgmt->lang["it_gasmask_off"]);
    }
}
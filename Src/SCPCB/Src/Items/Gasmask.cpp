#include "Gasmask.h"
#include "../AssetMgmt/TextMgmt.h"
#include "../Player/Player.h"

namespace CBN {

GasMask::GasMask() : Item("", ItemPickSound::Heafty, WornItemSlot::Head) {
    type = "gasmask";
}

String GasMask::getType() {
    return type;
}

String GasMask::getInvName() {
    return;
}

void GasMask::onPick() {
    txtMgmt->setMsg("TOUCH");
}

void GasMask::onUse() {
    if (mainPlayer->isEquipped(type)) {

    } else {

    }
}

}
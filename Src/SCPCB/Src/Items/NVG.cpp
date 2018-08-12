#include "NVG.h"
#include "Items.h"
#include "../Player.h"
#include "../MathUtils/MathUtils.h"
#include "../GameMain.h"

namespace CBN {

// Functions.
void UpdateNVG() {
    Item* wornItem = mainPlayer->inventory->items[WORNITEM_SLOT_HEAD];

    if (wornItem != nullptr) {
        if (!wornItem->itemTemplate->name.equals("nvgoggles") && !wornItem->itemTemplate->name.equals("supernv")) {
            wornItem = nullptr;
        }
    }

    //TODO: idfk
    float decayMultiplier = 0.02;
    if (wornItem!=nullptr) {
        wornItem->state = Max(-1.f,wornItem->state - (timing->tickDuration * decayMultiplier));
    }
}

}

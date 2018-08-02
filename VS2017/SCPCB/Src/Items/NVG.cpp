#include "NVG.h"
#include "include.h"

namespace CBN {

// Functions.
void UpdateNVG() {
    Item* wornItem = mainPlayer->inventory->items[WORNITEM_SLOT_HEAD];

    if (wornItem != nullptr) {
        if (wornItem->template->name != "nvgoggles" & wornItem->template->name != "supernv") {
            wornItem = nullptr;
        }
    }

    //TODO: idfk
    float decayMultiplier = 0.02;
    if (wornItem!=nullptr) {
        wornItem->state = Max(-1.0,wornItem->state - (timing->tickDuration * decayMultiplier));
    }
}

}

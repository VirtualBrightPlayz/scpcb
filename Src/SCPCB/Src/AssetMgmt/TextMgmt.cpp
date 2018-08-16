#include <bbgraphics.h>

#include "TextMgmt.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../Config/Options.h"
#include "../Player/Player.h"
#include "../Items/Items.h"

namespace CBN {

TxtManager::TxtManager() {
    displayMsg = "";
    displayTimer = 0.f;
    deathMsg = "";
}

void TxtManager::setMsg(const String& msg, float time) {
    displayMsg = msg;
    displayTimer = time;
}

void TxtManager::setDeathMsg(const String& msg) {
    deathMsg = msg;
}

String TxtManager::getDeathMsg() {
    return deathMsg;
}

void TxtManager::update() {
    if (displayTimer > 0) {
        bool paperSelected = false;
        if (CurrGameState != GAMESTATE_INVENTORY) {
            if (mainPlayer->selectedItem != nullptr) {
                // TODO: Replace with getType().
                if (mainPlayer->selectedItem->itemTemplate->name.equals("paper") || mainPlayer->selectedItem->itemTemplate->name.equals("oldpaper")) {
                    paperSelected = true;
                }
            }
        }

        // If a paper is being looked at then move the text down.
        if (!paperSelected) {
            bbColor(0,0,0);
            bbText((userOptions->screenWidth / 2)+1, (userOptions->screenHeight / 2) + 201, displayMsg, true, false);
            bbColor(255,255,255);
            bbText((userOptions->screenWidth / 2), (userOptions->screenHeight / 2) + 200, displayMsg, true, false);
        } else {
            bbColor(0,0,0);
            bbText((userOptions->screenWidth / 2)+1, (int)((userOptions->screenHeight * 0.94f) + 1), displayMsg, true, false);
            bbColor(255,255,255);
            bbText((userOptions->screenWidth / 2), (int)((userOptions->screenHeight * 0.94f)), displayMsg, true, false);
        }
        displayTimer -= timing->tickDuration;
    }
}

TxtManager* txtMgmt;
}
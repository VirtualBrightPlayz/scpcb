#include <bbgraphics.h>
#include <bbfilesystem.h>

#include "TextMgmt.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../Config/Options.h"
#include "../Config/INI.h"
#include "../Player/Player.h"
#include "../Items/Item.h"
#include "../MathUtils/MathUtils.h"

namespace CBN {

TxtManager* txtMgmt;

TxtManager::TxtManager(const String& langCode) {
    displayMsg = "";
    displayTimer = 0.f;
    deathMsg = "";

    changeLocalization(langCode);
}

void TxtManager::setMsg(const String& local, float time) {
    displayMsg = getLocalTxt(local);
    displayTimer = time * 70;
}

void TxtManager::setDeathMsg(const String& local) {
    deathMsg = getLocalTxt(local);
}

String TxtManager::getDeathMsg() {
    return deathMsg;
}

void TxtManager::updateMsg() {
    if (displayTimer <= 0) { return; }
    displayTimer -= timing->tickDuration;
}

void TxtManager::drawMsg() {
    if (displayTimer <= 0) { return; }

    bool paperSelected = false;
    if (CurrGameState != GAMESTATE_INVENTORY && mainPlayer->selectedItem != nullptr) {
        if (mainPlayer->selectedItem->getType().equals("paper")) {
            paperSelected = true;
        }
    }

    // TODO: Re-implement.
    // If a paper is being looked at then move the text down.
    int x, y;
    if (!paperSelected) {
        x = userOptions->screenWidth / 2;
        y = (userOptions->screenHeight / 2) + (int)(200 * MenuScale);
    } else {
        //bbText((userOptions->screenWidth / 2)+1, (int)((userOptions->screenHeight * 0.94f) + 1), displayMsg, true, false);
        //bbColor(255,255,255);
        //bbText((userOptions->screenWidth / 2), (int)((userOptions->screenHeight * 0.94f)), displayMsg, true, false);
    }
    bbColor(0, 0, 0);
    bbText(x + 1, y + 1, displayMsg, true);
    bbColor((int)Min(displayTimer / 2, 255), (int)Min(displayTimer / 2, 255), (int)Min(displayTimer / 2, 255));
    bbText(x, y, displayMsg, true);
}

String TxtManager::getLocalTxt(const String& key) {
    // TODO: Throw a console error (or some other kind) if an invalid key is requested.
    std::map<String, String>::iterator it = lang.find(key);
    String retVal;
    if (it != lang.end()) {
        return it->second;
    }

    return key;
}

void TxtManager::changeLocalization(const String& langCode) {
    lang.clear();

    String path = "Data/lang/" + langCode + "/text.ini";
    if (bbFileType(path) != 1) {
        throw "Language file \"" + path + "\" not found!";
    }

    lang = getINISection(path, "text");
}

}
#include <bbgraphics.h>
#include <bbfilesystem.h>

#include "TextMgmt.h"
#include "../GameMain.h"
#include "../Menus/Menu.h"
#include "../Config/Options.h"
#include "../Config/INI.h"
#include "../Player/Player.h"
#include "../Items/Items.h"

namespace CBN {

TxtManager* txtMgmt;

TxtManager::TxtManager(const String& langCode) {
    displayMsg = "";
    displayTimer = 0.f;
    deathMsg = "";

    changeLocalization(langCode);
}

void TxtManager::setMsg(const String& msg, float time) {
    displayMsg = msg;
    displayTimer = time * 70;
}

void TxtManager::setDeathMsg(const String& msg) {
    deathMsg = msg;
}

String TxtManager::getDeathMsg() {
    return deathMsg;
}

void TxtManager::updateMsg() {
    if (displayTimer <= 0) { return; }

    displayTimer -= timing->tickDuration;
    std::cout << String(displayTimer) + "\n";
}

void TxtManager::drawMsg() {
    if (displayTimer <= 0) { return; }

    bool paperSelected = false;
    if (CurrGameState != GAMESTATE_INVENTORY && mainPlayer->selectedItem != nullptr) {
        // TODO: Replace with getType().
        if (mainPlayer->selectedItem->itemTemplate->name.equals("paper") || mainPlayer->selectedItem->itemTemplate->name.equals("oldpaper")) {
            paperSelected = true;
        }
    }

    // If a paper is being looked at then move the text down.
    bbColor(0,0,0);
    if (!paperSelected) {
        bbText((userOptions->screenWidth / 2)+1, (userOptions->screenHeight / 2) + 201, displayMsg, true, false);
        bbColor(255,255,255);
        bbText((userOptions->screenWidth / 2), (userOptions->screenHeight / 2) + 200, displayMsg, true, false);
    } else {
        bbText((userOptions->screenWidth / 2)+1, (int)((userOptions->screenHeight * 0.94f) + 1), displayMsg, true, false);
        bbColor(255,255,255);
        bbText((userOptions->screenWidth / 2), (int)((userOptions->screenHeight * 0.94f)), displayMsg, true, false);
    }
}

void TxtManager::changeLocalization(const String& langCode) {
    String path = "Data/lang" + langCode + ".ini";
    if (bbFileType(path) != 1) {
        throw "Language file \"" + path + "\" not found!";
    }

    // launcher
    lang["lch_start"] = GetINIString(path, "launcher", "start");
    lang["lch_quit"] = GetINIString(path, "launcher", "quit");
    lang["lch_fullscreen"] = GetINIString(path, "launcher", "fullscreen");
    lang["lch_launcher"] = GetINIString(path, "launcher", "launcher");

    // menu
    lang["mnu_new"] = GetINIString(path, "menu", "new");
    lang["mnu_load"] = GetINIString(path, "menu", "load");
    lang["mnu_opt"] = GetINIString(path, "menu", "options");
    lang["mnu_quit"] = GetINIString(path, "menu", "quit");
    lang["mnu_back"] = GetINIString(path, "menu", "back");

    lang["mnu_start"] = GetINIString(path, "menu", "start");
    lang["mnu_loadmap"] = GetINIString(path, "menu", "loadmap");
    lang["mnu_intro"] = GetINIString(path, "menu", "intro");
    lang["mnu_name"] = GetINIString(path, "menu", "name");
    lang["mnu_seed"] = GetINIString(path, "menu", "seed");

    lang["pau_resume"] = GetINIString(path, "menu", "resume");
    lang["pau_load"] = GetINIString(path, "menu", "pau_load");
    lang["pau_opt"] = GetINIString(path, "menu", "pau_opt");
    lang["pau_quit"] = GetINIString(path, "menu", "pau_quit");
}
}
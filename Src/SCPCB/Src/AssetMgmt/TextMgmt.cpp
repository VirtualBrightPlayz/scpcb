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
}

void TxtManager::drawMsg() {
    if (displayTimer <= 0) { return; }

    bool paperSelected = false;
    if (CurrGameState != GAMESTATE_INVENTORY && mainPlayer->selectedItem != nullptr) {
        // TODO: Replace with getType().
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

void TxtManager::changeLocalization(const String& langCode) {
    lang.clear();

    String path = "Data/lang/" + langCode + ".ini";
    if (bbFileType(path) != 1) {
        throw "Language file \"" + path + "\" not found!";
    }

    // launcher
    lang["lch_start"] = GetINIString(path, "launcher", "start");
    lang["lch_quit"] = GetINIString(path, "launcher", "quit");
    lang["lch_fullscreen"] = GetINIString(path, "launcher", "fullscreen");
    lang["lch_launcher"] = GetINIString(path, "launcher", "launcher");
    lang["lch_res"] = GetINIString(path, "launcher", "resolution");
    lang["lch_graphics"] = GetINIString(path, "launcher", "graphics");

    // menu
    lang["mnu_new"] = GetINIString(path, "menu", "new");
    lang["mnu_load"] = GetINIString(path, "menu", "load");
    lang["mnu_opt"] = GetINIString(path, "menu", "options");
    lang["mnu_quit"] = GetINIString(path, "menu", "quit");
    lang["mnu_loadmap"] = GetINIString(path, "menu", "loadmap");
    lang["mnu_back"] = GetINIString(path, "menu", "back");

    lang["mnu_start"] = GetINIString(path, "menu", "start");
    lang["mnu_loadmap_btn"] = GetINIString(path, "menu", "loadmap_btn");
    lang["mnu_intro"] = GetINIString(path, "menu", "intro");
    lang["mnu_name"] = GetINIString(path, "menu", "name");
    lang["mnu_seed"] = GetINIString(path, "menu", "seed");

    lang["mnu_nosav"] = GetINIString(path, "menu", "nosav");
    lang["mnu_nomap"] = GetINIString(path, "menu", "nomap");

    lang["pau_title"] = GetINIString(path, "menu", "pau_title");
    lang["pau_dead"] = GetINIString(path, "menu", "pau_dead");
    lang["pau_resume"] = GetINIString(path, "menu", "resume");
    lang["pau_load"] = GetINIString(path, "menu", "pau_load");
    lang["pau_opt"] = GetINIString(path, "menu", "pau_opt");
    lang["pau_quit"] = GetINIString(path, "menu", "pau_quit");

    lang["mnu_loadimg"] = GetINIString(path, "menu", "loading");
    lang["mnu_loaddone"] = GetINIString(path, "menu", "loaddone");

    // items
    lang["inv_full"] = GetINIString(path, "items", "inv_full");
    lang["inv_cantequip"] = GetINIString(path, "items", "inv_cantequip");
    lang["inv_cantcombine"] = GetINIString(path, "items", "inv_cantcombine");
    lang["inv_alreadyequip"] = GetINIString(path, "items", "inv_alreadyequip");
    lang["dor_locked"] = GetINIString(path, "items", "locked");
    lang["dor_keyinsert"] = GetINIString(path, "items", "keyinsert");
    lang["dor_keylocked"] = GetINIString(path, "items", "keylocked");
    lang["dor_keyhigher"] = GetINIString(path, "items", "keyhigher");
    lang["dor_keyneeded"] = GetINIString(path, "items", "keyneeded");

    lang["it_gasmask"] = GetINIString(path, "items", "gasmask");
    lang["it_gasmask_on"] = GetINIString(path, "items", "gasmask_on");
    lang["it_gasmask_off"] = GetINIString(path, "items", "gasmask_off");

    lang["it_firstaid"] = GetINIString(path, "items", "firstaid");
}

}
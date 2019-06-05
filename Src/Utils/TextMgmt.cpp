#include <iostream>

#include "TextMgmt.h"
#include "INI.h"
#include "FileUtil.h"

namespace CBN {

TxtManager* txtMgmt;

TxtManager::TxtManager(const PGE::String& langCode) {
    displayMsg = "";
    displayTimer = 0.f;
    deathMsg = "";

    changeLocalization(langCode);
}

void TxtManager::setMsg(const PGE::String& local, float time) {
    displayMsg = getLocalTxt(local);
    displayTimer = time * 70;
}

void TxtManager::setDeathMsg(const PGE::String& local) {
    deathMsg = getLocalTxt(local);
}

PGE::String TxtManager::getDeathMsg() {
    return deathMsg;
}

void TxtManager::updateMsg() {
    if (displayTimer <= 0) { return; }
    //    displayTimer -= timing->tickDuration; // TODO: Re-implement.
}

void TxtManager::drawMsg() {
    // TODO: Implement.
}

PGE::String TxtManager::getLocalTxt(const PGE::String& key) {
    // TODO: Throw a console error (or some other kind) if an invalid key is requested.
    std::map<PGE::String, PGE::String>::iterator it = lang.find(key);
    PGE::String retVal;
    if (it != lang.end()) {
        return it->second;
    }

    return key;
}

void TxtManager::changeLocalization(const PGE::String& langCode) {
    lang.clear();

    PGE::String path = "Data/lang/" + langCode + "/text.ini";
    if (!FileUtil::exists(path.resourcePath())) {
        throw "Language file \"" + path + "\" not found!";
    }

    lang = getINISection(path, "text");
}

}

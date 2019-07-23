#include <iostream>
#include <Misc/FileUtil.h>
#include <Misc/FileName.h>

#include "TextMgmt.h"
#include "INI.h"

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

    PGE::FileName path = PGE::FileName::create("Data/lang/" + langCode + "/text.ini");
    if (!PGE::FileUtil::exists(path.str())) {
        throw "Language file \"" + path.str() + "\" not found!";
    }

    lang = getINISection(path.str(), "text");
}

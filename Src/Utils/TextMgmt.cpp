#include <iostream>
#include <exception>
#include <Misc/FileUtil.h>
#include <Misc/FileName.h>

#include "TextMgmt.h"

Language::Language(std::vector<INIFile::Section*> iniFile) {
    for (int i = 0; i < (int)iniFile.size(); i++) {
        INIFile::Section* currSection = iniFile[i];

        if (currSection->names[0].equals("info")) {
            for (int j = 0; j < (int)currSection->keys.size(); j++) {
                if (currSection->keys[j].equals("name")) {
                    name = currSection->values[j];
                    break;
                }
            }
        } else {
            PGE::String prefix = currSection->names[0] + ".";

            for (int j = 0; j < (int)currSection->keys.size(); j++) {
                PGE::String key = currSection->keys[j];
                PGE::String value = currSection->values[j];
                whyyy[prefix + key] = value;
            }
        }
    }
}

PGE::String Language::getText(const PGE::String& code) const {
    // TODO: Throw an error if an invalid key is requested.
    std::map<PGE::String, PGE::String>::const_iterator it = whyyy.find(code);
    PGE::String retVal;
    if (it != whyyy.end()) {
        return it->second;
    }

    return code;
}

TxtManager::TxtManager(const PGE::String& langCode) {
    currentLanguage = nullptr;
    displayMsg = "";
    displayTimer = 0.f;
    deathMsg = "";

    changeLocalization(langCode);
}

void TxtManager::setMsg(const PGE::String& local, float seconds) {
    displayMsg = getLocalTxt(local);
    displayTimer = seconds;
}

void TxtManager::setDeathMsg(const PGE::String& local) {
    deathMsg = getLocalTxt(local);
}

PGE::String TxtManager::getDeathMsg() {
    return deathMsg;
}

void TxtManager::updateMsg(float timestep) {
    if (displayTimer <= 0) { return; }
    displayTimer -= timestep;
}

void TxtManager::drawMsg() {
    // TODO: Implement.
}

PGE::String TxtManager::getLocalTxt(const PGE::String& key) const {
    return currentLanguage->getText(key);
}

void TxtManager::changeLocalization(const PGE::String& langCode) {
    if (currentLanguage != nullptr) {
        delete currentLanguage;
    }

    PGE::FileName path = PGE::FileName::create("Data/lang/" + langCode + "/text.ini");
    if (!PGE::FileUtil::exists(path.str())) {
        throw std::runtime_error(PGE::String("Language file \"" + path.str() + "\" not found!").cstr());
    }

    std::vector<INIFile::Section*> langINI = getINISections(path.str());
    currentLanguage = new Language(langINI);
}

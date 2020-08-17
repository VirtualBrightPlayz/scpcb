#include "LocalizationManager.h"

#include <iostream>
#include <exception>
#include <Misc/FileUtil.h>
#include <Misc/FilePath.h>

#include "INI.h"

LocalizationManager::Language::Language(const PGE::String& langCode) :
    code(langCode) {
    PGE::FilePath path = PGE::FilePath::fromStr("Data/lang/" + langCode + "/text.ini");
    if (!PGE::FileUtil::exists(path)) {
        throw std::runtime_error(PGE::String("Language file \"" + path.str() + "\" not found!").cstr());
    }

    std::vector<INIFile::Section*> iniFile = getINISections(path.str());

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
                map[prefix + key] = value;
            }
        }
    }
}

PGE::String LocalizationManager::Language::getText(const PGE::String& code) const {
    // TODO: Throw an error if an invalid key is requested.
    std::map<PGE::String, PGE::String>::const_iterator it = map.find(code);
    PGE::String retVal;
    if (it != map.end()) {
        return it->second;
    }

    return code;
}

LocalizationManager::LocalizationManager(const PGE::String& langCode) {
    currentLanguage = nullptr;
    setLocalization(langCode);
}

LocalizationManager::~LocalizationManager() {
    delete currentLanguage;
}

void LocalizationManager::setLocalization(const PGE::String& langCode) {
    if (currentLanguage != nullptr) {
        if (langCode == currentLanguage->code) {
            return; // We don't need to do anything, that language is already selected!
        }
        delete currentLanguage;
    }

    currentLanguage = new Language(langCode);
}

PGE::String LocalizationManager::getLocalTxt(const PGE::String& key) const {
    return currentLanguage->getText(key);
}

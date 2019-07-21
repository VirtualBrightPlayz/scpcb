#include <iostream>
#include <fstream>
#include <Misc/String.h>
#include <Misc/FileUtil.h>

#include "INI.h"

std::vector<INIFile*> INIFile::list;
INIFile::INIFile(const PGE::String& filename) {
    list.push_back(this);

    name = filename;

    Section* currSection = nullptr;

    std::ifstream file(filename.cstr());

    if (file.good()) {
        PGE::String currLine;
        while (!file.eof()) {
            std::string cppStr;
            getline(file, cppStr);
            currLine = PGE::String(cppStr);

            if (currLine.charAt(0) == '[') {
                if (currSection != nullptr) {
                    sections.push_back(currSection);
                }
                currSection = new Section();
                currLine = currLine.substr(1,currLine.size()-2).trim();
                currSection->names = currLine.split('|', true);
            } else if (currLine.charAt(0) != ';') {
                if (currSection != nullptr) {
                    std::vector<PGE::String> split = currLine.split('=', false);
                    if (split.size() >= 2) {
                        currSection->keys.push_back(split[0].trim());

                        // If the value itself had a '=' in it then recover those.
                        split.erase(split.begin());
                        PGE::String value = PGE::String::join(split, '=');
                        currSection->values.push_back(value.trim());
                    }
                }
            }
        }
        if (currSection != nullptr) {
            sections.push_back(currSection);
        }
    }
    file.close();
}
INIFile::~INIFile() {
    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
    for (int i = 0; i < (int)sections.size(); i++) {
        delete sections[i];
    }
}
int INIFile::getListSize() {
    return (int)list.size();
}
INIFile* INIFile::getObject(int index) {
    return list[index];
}

PGE::String INIFile::getName() {
    return name;
}

PGE::String INIFile::getValue(const PGE::String& section, const PGE::String& key, const PGE::String& defaultValue) {
    for (int i = 0; i < (int)sections.size(); i++) {
        for (int j = 0; j < (int)sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j].toLower())) {
                for (int k = 0; k < (int)sections[i]->keys.size(); k++) {
                    if (sections[i]->keys[k].toLower().equals(key.toLower())) {
                        return sections[i]->values[k];
                    }
                }
                return defaultValue;
            }
        }
    }
    return defaultValue;
}

void INIFile::setValue(const PGE::String& section, const PGE::String& key, const PGE::String& value) {
    for (int i = 0; i < (int)sections.size(); i++) {
        for (int j = 0; j < (int)sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j])) {
                for (int k = 0; k < (int)sections[i]->keys.size(); k++) {
                    if (sections[i]->keys[k].toLower().equals(key.toLower())) {
                        sections[i]->values[k] = value;
                        return;
                    }
                }
                
                // Key doesn't exist, make it.
                sections[i]->keys.push_back(key.toLower());
                sections[i]->values.push_back(value);
                return;
            }
        }
    }

    // Section doesn't exist, make one.
    Section* sec = new Section();
    sec->names.push_back(section);
    sec->keys.push_back(key.toLower());
    sec->values.push_back(value);

    sections.push_back(sec);
}

std::map<PGE::String, PGE::String> INIFile::getSection(const PGE::String& section) {
    for (int i = 0; i < (int)sections.size(); i++) {
        for (int j = 0; j < (int)sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j])) {
                std::map<PGE::String, PGE::String> retVal;

                for (int k = 0; k < (int)sections[i]->keys.size(); k++) {
                    PGE::String key = sections[i]->keys[k];
                    PGE::String value = sections[i]->values[k];
                    retVal[key] = value;
                }
                return retVal;
            }
        }
    }

    return std::map<PGE::String, PGE::String>();
}

void INIFile::save() {
    std::ofstream f(name.cstr());

    for (int i = 0; i < (int)sections.size(); i++) {
        PGE::String secName = sections[i]->names[0];
        for (int j = 1; j < (int)sections[i]->names.size(); j++) {
            secName = secName + "|" + sections[i]->names[j];
        }
        f << "[" << secName << "]" << std::endl;
        for (int j = 0; j < (int)sections[i]->keys.size(); j++) {
            f << sections[i]->keys[j] << " = " << sections[i]->values[j] << std::endl;
        }
        f << std::endl;
    }
    f.close();
}

PGE::String getINIString(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, const PGE::String& defaultValue) {
    for (int i = 0; i < INIFile::getListSize(); i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            return iniFile->getValue(section, parameter, defaultValue);
        }
    }
    INIFile* newFile = new INIFile(file);
    return newFile->getValue(section, parameter, defaultValue);
}

int getINIInt(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, int defaultvalue) {
    PGE::String txt = getINIString(file, section, parameter, PGE::String(defaultvalue));
    return txt.toInt();
}

bool getINIBool(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, bool defaultvalue) {
    int val = getINIInt(file, section, parameter, (int)defaultvalue);
    return !!val;
}

float getINIFloat(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, float defaultvalue) {
    return getINIString(file, section, parameter, PGE::String(defaultvalue)).toFloat();
}

void putINIValue(const PGE::String& file, const PGE::String& section, const PGE::String& key, const PGE::String& value) {
    INIFile* targetFile = nullptr;
    for (int i = 0; i < INIFile::getListSize(); i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            targetFile = iniFile;
            break;
        }
    }
    if (targetFile == nullptr) {
        targetFile = new INIFile(file);
    }

    targetFile->setValue(section,key,value);
    targetFile->save();
}

std::map<PGE::String, PGE::String> getINISection(const PGE::String& file, const PGE::String& section) {
    for (int i = 0; i < INIFile::getListSize(); i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            return iniFile->getSection(section);
        }
    }
    INIFile* newFile = new INIFile(file);
    return newFile->getSection(section);
}

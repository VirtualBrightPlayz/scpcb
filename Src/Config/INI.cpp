#include <iostream>
#include <bbfilesystem.h>

#include "INI.h"

namespace CBN {

// Structs.
std::vector<INIFile*> INIFile::list;
INIFile::INIFile(const String& filename) {
    list.push_back(this);

    name = filename;

    Section* currSection = nullptr;

    bbFile* file = bbReadFile(filename);
    String currLine;
    while (!bbEof(file)) {
        currLine = bbReadLine(file);
        if (currLine.charAt(0) == '[') {
            if (currSection != nullptr) {
                sections.push_back(currSection);
            }
            currSection = new Section();
            currLine = currLine.substr(1,currLine.size()-2).trim();
            currSection->names = currLine.split('|');
        } else if (currLine.charAt(0) != ';') {
            if (currSection != nullptr) {
                std::vector<String> split = currLine.split('=');
                if (split.size() >= 2) {
                    currSection->keys.push_back(split[0].trim());

                    // If the value itself had a '=' in it then recover those.
                    split.erase(split.begin());
                    String value = String::join(split, '=');
                    currSection->values.push_back(value.trim());
                }
            }
        }
    }
    if (currSection != nullptr) {
        sections.push_back(currSection);
    }
    bbCloseFile(file);
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

String INIFile::getName() {
    return name;
}

String INIFile::getValue(const String& section, const String& key, const String& defaultValue) {
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

void INIFile::setValue(const String& section, const String& key, const String& value) {
    for (int i = 0; i < (int)sections.size(); i++) {
        for (int j = 0; j < (int)sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j])) {
                for (int k = 0; k < (int)sections[i]->keys.size(); k++) {
                    if (sections[i]->keys[k].toLower().equals(key.toLower())) {
                        sections[i]->values[k] = value;
                        return;
                    }
                }
                return;
            }
        }
    }
}

std::map<String, String> INIFile::getSection(const String& section) {
    for (int i = 0; i < (int)sections.size(); i++) {
        for (int j = 0; j < (int)sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j])) {
                std::map<String, String> retVal;

                for (int k = 0; k < (int)sections[i]->keys.size(); k++) {
                    String key = sections[i]->keys[k];
                    String value = sections[i]->values[k];
                    retVal[key] = value;
                }
                return retVal;
            }
        }
    }

    return std::map<String, String>();
}

void INIFile::save() {
    bbFile* f = bbWriteFile(name);
    for (int i = 0; i < (int)sections.size(); i++) {
        String secName = sections[i]->names[0];
        for (int j = 1; j < (int)sections[i]->names.size(); j++) {
            secName = secName + "|" + sections[i]->names[j];
        }
        bbWriteLine(f,"["+secName+"]");
        for (int j = 0; j < (int)sections[i]->keys.size(); j++) {
            bbWriteLine(f, sections[i]->keys[j]+" = "+sections[i]->values[j]);
        }
        bbWriteLine(f, "");
    }
    bbCloseFile(f);
}

// Functions.
String getINIString(const String& file, const String& section, const String& parameter, const String& defaultValue) {
    for (int i = 0; i < INIFile::getListSize(); i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            return iniFile->getValue(section, parameter, defaultValue);
        }
    }
    INIFile* newFile = new INIFile(file);
    return newFile->getValue(section, parameter, defaultValue);
}

int getINIInt(const String& file, const String& section, const String& parameter, int defaultvalue) {
    String txt = getINIString(file, section, parameter, String(defaultvalue));
    if (txt.toLower().equals("true")) {
        return 1;
    } else if (txt.toLower().equals("false")) {
        return 0;
    } else {
        return txt.toInt();
    }
}

float getINIFloat(const String& file, const String& section, const String& parameter, float defaultvalue) {
    return getINIString(file, section, parameter, String(defaultvalue)).toFloat();
}

void putINIValue(const String& file, const String& section, const String& key, const String& value) {
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

std::map<String, String> getINISection(const String& file, const String& section) {
    for (int i = 0; i < INIFile::getListSize(); i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            return iniFile->getSection(section);
        }
    }
    INIFile* newFile = new INIFile(file);
    return newFile->getSection(section);
}

}

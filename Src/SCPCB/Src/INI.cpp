#include <iostream>
#include <bbbank.h>
#include <bbfilesystem.h>
#include <StringType.h>
#include <bbstring.h>

#include "INI.h"
#include "MathUtils/MathUtils.h"

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
        if (currLine.charAt(0)=='[') {
            if (currSection != nullptr) {
                sections.push_back(currSection);
            }
            currSection = new Section();
            currLine = currLine.substr(1,currLine.size()-2).trim();
            currSection->names = currLine.split("|");
        } else if (currLine.charAt(0)!=';') {
            if (currSection != nullptr) {
                std::vector<String> split = currLine.split("=");
                if (split.size()>=2) {
                    currSection->keys.push_back(split[0].trim());
                    String value = "";
                    for (int i=1;i<split.size();i++) {
                        value = split[i];
                    }
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
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
    for (int i=0;i<sections.size();i++) {
        delete sections[i];
    }
}
int INIFile::getListSize() {
    return list.size();
}
INIFile* INIFile::getObject(int index) {
    return list[index];
}

String INIFile::getName() {
    return name;
}

String INIFile::getValue(const String& section, String key, String defaultValue) {
    for (int i = 0; i<sections.size(); i++) {
        for (int j = 0; j<sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j].toLower())) {
                for (int k = 0; k<sections[i]->keys.size(); k++) {
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

void INIFile::setValue(const String& section, String key, String value) {
    for (int i = 0; i<sections.size(); i++) {
        for (int j = 0; j<sections[i]->names.size(); j++) {
            if (section.toLower().equals(sections[i]->names[j])) {
                for (int k = 0; k<sections[i]->keys.size(); k++) {
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

void INIFile::save() {
    bbFile* f = bbWriteFile(name);
    for (int i = 0; i<sections.size(); i++) {
        String secName = sections[i]->names[0];
        for (int j = 1; j<sections[i]->names.size(); j++) {
            secName = secName+"|"+sections[i]->names[j];
        }
        bbWriteLine(f,"["+secName+"]");
        for (int j=0;j<sections[i]->keys.size();j++) {
            bbWriteLine(f, sections[i]->keys[j]+" = "+sections[i]->values[j]);
        }
        bbWriteLine(f,"");
    }
    bbCloseFile(f);
}
// Functions.
String GetINIString(const String& file, String section, String parameter, String defaultValue) {
    for (int i=0;i<INIFile::getListSize();i++) {
        INIFile* iniFile = INIFile::getObject(i);

        if (iniFile->getName().toLower().equals(file.toLower())) {
            return iniFile->getValue(section, parameter, defaultValue);
        }
    }
    INIFile* newFile = new INIFile(file);
    return newFile->getValue(section, parameter, defaultValue);
}

int GetINIInt(const String& file, String section, String parameter, int defaultvalue) {
    String txt = GetINIString(file, section, parameter, String(defaultvalue));
    if (txt.toLower().equals("true")) {
        return 1;
    } else if (txt.toLower().equals("false")) {
        return 0;
    } else {
        return txt.toInt();
    }
}

float GetINIFloat(const String& file, String section, String parameter, float defaultvalue) {
    return GetINIString(file, section, parameter, String(defaultvalue)).toFloat();
}

void PutINIValue(const String& file, String section, String key, String value) {
    INIFile* targetFile = nullptr;
    for (int i = 0; i<INIFile::getListSize(); i++) {
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

}

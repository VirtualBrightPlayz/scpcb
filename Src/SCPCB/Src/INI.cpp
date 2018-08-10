#include <iostream>
#include <bbbank.h>
#include <bbfilesystem.h>
#include <StringType.h>
#include <bbstring.h>

#include "INI.h"
#include "MathUtils/MathUtils.h"

namespace CBN {
// TODO: Replace this with simpleini.
// Structs.
std::vector<INIFile*> INIFile::list;
INIFile::INIFile() {
    list.push_back(this);
}
INIFile::~INIFile() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int INIFile::getListSize() {
    return list.size();
}
INIFile* INIFile::getObject(int index) {
    return list[index];
}

// Functions.
String ReadINILine(INIFile* file) {
    int rdbyte;
    bool firstbyte = true;
    int offset = file->bankOffset;
    bbBank* bank = file->bank;
    String retStr = "";
    rdbyte = bbPeekByte(bank,offset);
    while (firstbyte || ((rdbyte!=13) && (rdbyte!=10)) && (offset < file->size)) {
        rdbyte = bbPeekByte(bank,offset);
        if ((rdbyte!=13) && (rdbyte!=10)) {
            firstbyte = false;
            retStr = retStr+((char)rdbyte);
        }
        offset = offset+1;
    }
    file->bankOffset = offset;
    return retStr;
}

void UpdateINIFile(String filename) {
    INIFile* file = nullptr;

    INIFile* k;
    for (int iterator62 = 0; iterator62 < INIFile::getListSize(); iterator62++) {
        k = INIFile::getObject(iterator62);

        if (k->name.equals(filename.toLower())) {
            file = k;
        }
    }

    if (file == nullptr) {
        return;
    }

    if (file->bank!=0) {
        bbFreeBank(file->bank);
    }
    bbFile* f = bbReadFile(file->name);
    int fleSize = 1;
    while (fleSize<bbFileSize(file->name)) {
        fleSize = fleSize*2;
    }
    file->bank = bbCreateBank(fleSize);
    file->size = 0;
    while (!bbEof(f)) {
        bbPokeByte(file->bank,file->size,bbReadByte(f));
        file->size = file->size+1;
    }
    bbCloseFile(f);
}

String GetINIString(String file, String section, String parameter, String defaultvalue) {
    String temporaryString = "";
    String strtemp;

    INIFile* lfile = nullptr;

    INIFile* k;
    for (int i = 0; i < INIFile::getListSize(); i++) {
        k = INIFile::getObject(i);

        if (k->name.equals(file.toLower())) {
            lfile = k;
        }
    }

    if (lfile == nullptr) {
        std::cout << "CREATE BANK FOR "+file;
        lfile = new INIFile();
        lfile->name = file.toLower();
        lfile->bank = 0;
        UpdateINIFile(lfile->name);
    }

    lfile->bankOffset = 0;

    section = section.toLower();

    std::cout<<"INISTR\n";
    //While (Not Eof(f))
    while (lfile->bankOffset<lfile->size) {
        strtemp = ReadINILine(lfile);
        if (strtemp.charAt(0) == '[') {
            strtemp = strtemp.toLower();
            if (strtemp.substr(1, strtemp.size()-2).toLower().equals(section)) {
                do {
                    temporaryString = ReadINILine(lfile);
                    String leftPart = temporaryString.substr(0, (int)(Max(temporaryString.findFirst("="), 0)));
                    if (leftPart.trim().toLower().equals(parameter.toLower())) {
                        //CloseFile(f)
                        return temporaryString.substr(leftPart.size()+1).trim();
                    }
                } while (temporaryString.charAt(0) != '[' && lfile->bankOffset < lfile->size);

                //CloseFile(f)
                return defaultvalue;
            }
        }
    }

    return defaultvalue;
}

int GetINIInt(String file, String section, String parameter, int defaultvalue) {
    String txt = GetINIString(file, section, parameter, String(defaultvalue));
    if (txt.toLower().equals("true")) {
        return 1;
    } else if (txt.toLower().equals("false")) {
        return 0;
    } else {
        return txt.toInt();
    }
}

float GetINIFloat(String file, String section, String parameter, float defaultvalue) {
    return GetINIString(file, section, parameter, String(defaultvalue)).toFloat();
}

String GetINIString2(String file, int start, String parameter, String defaultvalue) {
    String temporaryString = "";
    String strTemp;
    bbFile* f = bbReadFile(file);

    int n = 0;
    while (!bbEof(f)) {
        strTemp = bbReadLine(f);
        n++;
        if (n==start) {
            do {
                temporaryString = bbReadLine(f);
                if (temporaryString.substr(0, (int)(Max(temporaryString.findFirst("="), 0))).trim().toLower().equals(parameter.toLower())) {
                    bbCloseFile(f);
                    return bbRight(temporaryString, temporaryString.size()-temporaryString.findFirst(" = ")+1).trim();
                }
            } while (temporaryString.charAt(0) == '[' || bbEof(f));
            bbCloseFile(f);
            return defaultvalue;
        }
    }

    bbCloseFile(f);

    return defaultvalue;
}

int GetINIInt2(String file, int start, String parameter, String defaultvalue) {
    String txt = GetINIString2(file, start, parameter, String(defaultvalue));
    if (txt.toLower().equals("true")) {
        return 1;
    } else if (txt.toLower().equals("false")) {
        return 0;
    } else {
        return txt.toInt();
    }
}

int GetINISectionLocation(String file, String section) {
    int temp;
    String strTemp;
    bbFile* f = bbReadFile(file);

    section = section.toLower();

    int n = 0;
    while (!bbEof(f)) {
        strTemp = bbReadLine(f);
        n = n+1;
        if (strTemp.charAt(0) == '[') {
            strTemp = strTemp.toLower();
            temp = strTemp.findFirst(section);
            if (temp>=0) {
                if (bbMid(strTemp, temp, 1).equals('[') || bbMid(strTemp, temp, 1).equals('|')) {
                    bbCloseFile(f);
                    return n;
                }
            }
        }
    }

    bbCloseFile(f);
}

int PutINIValue(String file, String INI_sSection, String INI_sKey, String INI_sValue) {

    // Returns: True (Success) Or False (Failed)

    INI_sSection = "[" + INI_sSection.trim() + "]";
    String INI_sUpperSection = INI_sSection.toUpper();
    INI_sKey = INI_sKey.trim();
    INI_sValue = INI_sValue.trim();
    String INI_sFilename = file;

    // Retrieve the INI Data (If it exists)

    String INI_sContents = INI_FileToString(INI_sFilename);

    // (Re)Create the INI file updating/adding the SECTION, KEY And VALUE

    int INI_bWrittenKey = false;
    int INI_bSectionFound = false;
    String INI_sCurrentSection = "";

    bbFile* INI_lFileHandle = bbWriteFile(INI_sFilename);
    // Create file failed!
    if (INI_lFileHandle == 0) {
        return false;
    }

    int INI_lOldPos = 1;
    int INI_lPos = INI_sContents.findFirst('\0');
    String INI_sTemp;
    int lEqualsPos;

    while (INI_lPos != 0) {

        INI_sTemp = bbMid(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos));

        if (!INI_sTemp.isEmpty()) {

            if (INI_sTemp.charAt(0) == '[' && bbRight(INI_sTemp, 1).equals(']')) {

                // Process SECTION

                if (INI_sCurrentSection.equals(INI_sUpperSection) && INI_bWrittenKey == false) {
                    INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue);
                }
                INI_sCurrentSection = INI_CreateSection(INI_lFileHandle, INI_sTemp).toUpper();
                if (INI_sCurrentSection.equals(INI_sUpperSection)) {
                    INI_bSectionFound = true;
                }

            } else {
                if (INI_sTemp.charAt(0) == ':' || INI_sTemp.charAt(0) == ';') {
                    bbWriteLine(INI_lFileHandle, INI_sTemp);
                } else {
                    // KEY=VALUE
                    lEqualsPos = INI_sTemp.findFirst("=");
                    if (lEqualsPos != 0) {
                        if (INI_sCurrentSection.equals(INI_sUpperSection) && INI_sTemp.substr(0, (lEqualsPos - 1)).trim().toUpper().equals(INI_sKey.toUpper())) {
                            if (!INI_sValue.isEmpty()) {
                                INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue);
                            }
                            INI_bWrittenKey = true;
                        } else {
                            bbWriteLine(INI_lFileHandle, INI_sTemp);
                        }
                    }
                }

            }

        }

        // Move through the INI file...

        INI_lOldPos = INI_lPos + 1;
        INI_lPos = INI_sContents.findFirst('\0', INI_lOldPos);

    }

    // KEY wasn;t found in the INI file - Append a New SECTION If required And create our KEY=VALUE Line

    if (INI_bWrittenKey == false) {
        if (INI_bSectionFound == false) {
            INI_CreateSection(INI_lFileHandle, INI_sSection);
        }
        INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue);
    }

    bbCloseFile(INI_lFileHandle);

    // Success
    return true;

}

String INI_FileToString(String INI_sFilename) {

    String INI_sString = "";
    bbFile* INI_lFileHandle = bbReadFile(INI_sFilename);
    if (INI_lFileHandle != 0) {
        while (!bbEof(INI_lFileHandle)) {
            INI_sString = INI_sString + bbReadLine(INI_lFileHandle) + '\0';
        }
        bbCloseFile(INI_lFileHandle);
    }
    return INI_sString;

}

String INI_CreateSection(bbFile* INI_lFileHandle, String INI_sNewSection) {

    // Blank Line between sections)
    if (bbFilePos(INI_lFileHandle) != 0) {
        bbWriteLine(INI_lFileHandle, "");
    }
    bbWriteLine(INI_lFileHandle, INI_sNewSection);
    return INI_sNewSection;

}

int INI_CreateKey(bbFile* INI_lFileHandle, String INI_sKey, String INI_sValue) {

    bbWriteLine(INI_lFileHandle, INI_sKey + " = " + INI_sValue);
    return true;

}

}

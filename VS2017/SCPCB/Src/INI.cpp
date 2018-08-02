#include "INI.h"
#include "include.h"

namespace CBN {

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
    int firstbyte = true;
    int offset = file->bankOffset;
    int bank = file->bank;
    String retStr = "";
    rdbyte = bbPeekByte(bank,offset);
    while ((firstbyte) | ((rdbyte!=13) & (rdbyte!=10))) & (offset<file->size) {
        rdbyte = bbPeekByte(bank,offset);
        if ((rdbyte!=13) & (rdbyte!=10)) {
            firstbyte = false;
            retStr = retStr+bbChr(rdbyte);
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

        if (k->name == bbLower(filename)) {
            file = k;
        }
    }

    if (file == nullptr) {
        return;
    }

    if (file->bank!=0) {
        bbFreeBank(file->bank);
    }
    int f = bbReadFile(file->name);
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

String GetINIString(String file, String section, String parameter, String defaultvalue = "") {
    String TemporaryString = "";
    String strtemp;

    INIFile* lfile = nullptr;

    INIFile* k;
    for (int iterator63 = 0; iterator63 < INIFile::getListSize(); iterator63++) {
        k = INIFile::getObject(iterator63);

        if (k->name == bbLower(file)) {
            lfile = k;
        }
    }

    if (lfile == nullptr) {
        bbDebugLog("CREATE BANK FOR "+file);
        lfile = new INIFile();
        lfile->name = bbLower(file);
        lfile->bank = 0;
        UpdateINIFile(lfile->name);
    }

    lfile->bankOffset = 0;

    section = bbLower(section);

    //While (Not Eof(f))
    while (lfile->bankOffset<lfile->size) {
        strtemp = ReadINILine(lfile);
        if (bbLeft(strtemp,1) == "[") {
            strtemp = bbLower(strtemp);
            if (bbMid(strtemp, 2, bbLen(strtemp)-2)==section) {
                do {
                    TemporaryString = ReadINILine(lfile);
                    if (bbLower(bbTrim(bbLeft(TemporaryString, Int(Max(bbInstr(TemporaryString, "=") - 1, 0))))) = bbLower(parameter)) {
                        //CloseFile(f)
                        Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString," = ")) );
                    }
                } while (Left(TemporaryString, 1) = "[" | (lfile->bankOffset>=lfile->size));

                //CloseFile(f)
                return defaultvalue;
            }
        }
    }

    return defaultvalue;
}

int GetINIInt(String file, String section, String parameter, int defaultvalue = 0) {
    String txt = GetINIString(file, section, parameter, String(defaultvalue));
    if (bbLower(txt) == "true") {
        return 1;
    } else if ((bbLower(txt) == "false")) {
        return 0;
    } else {
        return Int(txt);
    }
}

float GetINIFloat(String file, String section, String parameter, float defaultvalue = 0.0) {
    return Float(GetINIString(file, section, parameter, String(defaultvalue)));
}

String GetINIString2(String file, int start, String parameter, String defaultvalue = "") {
    String TemporaryString = "";
    String strTemp;
    int f = bbReadFile(file);

    int n = 0;
    while (!bbEof(f)) {
        strTemp = bbReadLine(f);
        n++;
        if (n==start) {
            do {
                TemporaryString = bbReadLine(f);
                if (bbLower(bbTrim(bbLeft(TemporaryString, Int(Max(bbInstr(TemporaryString, "=") - 1, 0))))) = bbLower(parameter)) {
                    bbCloseFile(f);
                    Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString," = ")) );
                }
            } while (Left(TemporaryString, 1) = "[" | bbEof(f));
            bbCloseFile(f);
            return defaultvalue;
        }
    }

    bbCloseFile(f);

    return defaultvalue;
}

int GetINIInt2(String file, int start, String parameter, String defaultvalue = "") {
    String txt = GetINIString2(file, start, parameter, String(defaultvalue));
    if (bbLower(txt) == "true") {
        return 1;
    } else if ((bbLower(txt) == "false")) {
        return 0;
    }
    return Int(txt);
}

int GetINISectionLocation(String file, String section) {
    int Temp;
    String strTemp;
    int f = bbReadFile(file);

    section = bbLower(section);

    int n = 0;
    while (!bbEof(f)) {
        strTemp = bbReadLine(f);
        n = n+1;
        if (bbLeft(strTemp,1) == "[") {
            strTemp = bbLower(strTemp);
            Temp = bbInstr(strTemp, section);
            if (Temp>0) {
                if (bbMid(strTemp, Temp-1, 1)=="[" | bbMid(strTemp, Temp-1, 1)=="|") {
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

    INI_sSection = "[" + bbTrim$(INI_sSection) + "]";
    String INI_sUpperSection = bbUpper$(INI_sSection);
    INI_sKey = bbTrim$(INI_sKey);
    INI_sValue = bbTrim$(INI_sValue);
    String INI_sFilename = file;

    // Retrieve the INI Data (If it exists)

    String INI_sContents = INI_FileToString(INI_sFilename);

    // (Re)Create the INI file updating/adding the SECTION, KEY And VALUE

    int INI_bWrittenKey = false;
    int INI_bSectionFound = false;
    String INI_sCurrentSection = "";

    int INI_lFileHandle = bbWriteFile(INI_sFilename);
    // Create file failed!
    if (INI_lFileHandle == 0) {
        return false;
    }

    int INI_lOldPos = 1;
    int INI_lPos = bbInstr(INI_sContents, bbChr$(0));
    String INI_sTemp;
    int lEqualsPos;

    while (INI_lPos != 0) {

        INI_sTemp = bbMid$(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos));

        if (INI_sTemp != "") {

            if (bbLeft$(INI_sTemp, 1) == "[" & bbRight$(INI_sTemp, 1) == "]") {

                // Process SECTION

                if (INI_sCurrentSection == INI_sUpperSection) & (INI_bWrittenKey == false) {
                    INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue);
                }
                INI_sCurrentSection = bbUpper$(INI_CreateSection(INI_lFileHandle, INI_sTemp));
                if (INI_sCurrentSection == INI_sUpperSection) {
                    INI_bSectionFound = true;
                }

            } else {
                if (bbLeft(INI_sTemp, 1) == ":") | (bbLeft(INI_sTemp, 1) == ";") {
                    bbWriteLine(INI_lFileHandle, INI_sTemp);
                } else {
                    // KEY=VALUE
                    lEqualsPos = bbInstr(INI_sTemp, "=");
                    if (lEqualsPos != 0) {
                        if (INI_sCurrentSection == INI_sUpperSection) & (bbUpper$(bbTrim$(bbLeft$(INI_sTemp, (lEqualsPos - 1)))) == bbUpper$(INI_sKey)) {
                            if (INI_sValue != "") {
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
        INI_lPos = bbInstr(INI_sContents, bbChr$(0), INI_lOldPos);

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
    int INI_lFileHandle = bbReadFile(INI_sFilename);
    if (INI_lFileHandle != 0) {
        while (Not(bbEof(INI_lFileHandle))) {
            INI_sString = INI_sString + bbReadLine$(INI_lFileHandle) + bbChr$(0);
        }
        bbCloseFile(INI_lFileHandle);
    }
    return INI_sString;

}

String INI_CreateSection(int INI_lFileHandle, String INI_sNewSection) {

    // Blank Line between sections)
    if (bbFilePos(INI_lFileHandle) != 0) {
        bbWriteLine(INI_lFileHandle, "");
    }
    bbWriteLine(INI_lFileHandle, INI_sNewSection);
    return INI_sNewSection;

}

int INI_CreateKey(int INI_lFileHandle, String INI_sKey, String INI_sValue) {

    WriteLine(INI_lFileHandle, INI_sKey + " = " + INI_sValue);
    return true;

}

}

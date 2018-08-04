#ifndef INI_H_INCLUDED
#define INI_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct INIFile {
private:
    static std::vector<INIFile*> list;

public:
    INIFile();
    ~INIFile();
    static int getListSize();
    static INIFile* getObject(int index);

    String name;
    int bank;
    int bankOffset = 0;
    int size;
};

// Functions.
String ReadINILine(INIFile* file);

void UpdateINIFile(String filename);

String GetINIString(String file, String section, String parameter, String defaultvalue = "");

int GetINIInt(String file, String section, String parameter, int defaultvalue = 0);

float GetINIFloat(String file, String section, String parameter, float defaultvalue = 0.0);

String GetINIString2(String file, int start, String parameter, String defaultvalue = "");

int GetINIInt2(String file, int start, String parameter, String defaultvalue = "");

int GetINISectionLocation(String file, String section);

int PutINIValue(String file, String INI_sSection, String INI_sKey, String INI_sValue);

String INI_FileToString(String INI_sFilename);

String INI_CreateSection(int INI_lFileHandle, String INI_sNewSection);

int INI_CreateKey(int INI_lFileHandle, String INI_sKey, String INI_sValue);

}
#endif // INI_H_INCLUDED

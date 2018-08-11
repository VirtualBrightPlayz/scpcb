#ifndef INI_H_INCLUDED
#define INI_H_INCLUDED
#include <StringType.h>
#include <vector>

class bbBank;
class bbFile;

namespace CBN {

// Structs.
class INIFile {
private:
    static std::vector<INIFile*> list;

    struct Section
    {
        std::vector<String> names;
        std::vector<String> keys;
        std::vector<String> values;
    };

    std::vector<Section*> sections;

    String name;
public:
    INIFile(const String& filename);
    ~INIFile();
    static int getListSize();
    static INIFile* getObject(int index);

    String getName();

    String getValue(const String& section, const String& key, const String& defaultValue);
    void setValue(const String& section, const String& key, const String& value);

    void save();

    int bankOffset = 0;
    int size;
};

// Functions.
String GetINIString(const String& file, const String& section, const String& parameter, const String& defaultValue = "");

int GetINIInt(const String& file, const String& section, const String& parameter, int defaultvalue = 0);

float GetINIFloat(const String& file, const String& section, const String& parameter, float defaultvalue = 0.0);

void PutINIValue(const String& file, const String& INI_sSection, const String& INI_sKey, const String& INI_sValue);

}
#endif // INI_H_INCLUDED

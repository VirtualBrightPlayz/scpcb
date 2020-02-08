#ifndef INI_H_INCLUDED
#define INI_H_INCLUDED
#include <Misc/String.h>
#include <vector>
#include <map>

class INIFile {
    public:
        struct Section {
            std::vector<PGE::String> names;
            std::vector<PGE::String> keys;
            std::vector<PGE::String> values;
        };
    private:
        static std::vector<INIFile*> list;

        std::vector<Section*> sections;

        PGE::String name;
    public:
        INIFile(const PGE::String& filename);
        ~INIFile();
        static int getListSize();
        static INIFile* getObject(int index);

        PGE::String getName();

        PGE::String getValue(const PGE::String& section, const PGE::String& key, const PGE::String& defaultValue);
        void setValue(const PGE::String& section, const PGE::String& key, const PGE::String& value);

        std::vector<INIFile::Section*> getAllSections();

        void save();

        int bankOffset = 0;
        int size;
};

PGE::String getINIString(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, const PGE::String& defaultValue = "");

int getINIInt(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, int defaultvalue = 0);

bool getINIBool(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, bool defaultvalue = false);

float getINIFloat(const PGE::String& file, const PGE::String& section, const PGE::String& parameter, float defaultvalue = 0.0);

void putINIValue(const PGE::String& file, const PGE::String& INI_sSection, const PGE::String& INI_sKey, const PGE::String& INI_sValue);

std::vector<INIFile::Section*> getINISections(const PGE::String& file);

#endif // INI_H_INCLUDED

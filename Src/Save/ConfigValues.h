#ifndef CONFIGVALUES_H_INCLUDED
#define CONFIGVALUES_H_INCLUDED

#include <vector>

#include <String/String.h>

class INIFile;

class ConfigValue {
    protected:
        INIFile* const file;
        const PGE::String section;
        const PGE::String name;

    public:
        ConfigValue(INIFile* file, const PGE::String& section, const PGE::String& name);

        virtual void saveValue() const = 0;
        virtual void loadValue() = 0;
};

class IntConfigValue : public ConfigValue {
    public:
        int value;

        IntConfigValue(INIFile* file, const PGE::String& section, const PGE::String& name, int defaultValue);
        
        void saveValue() const override;
        void loadValue() override;
};

class BoolConfigValue : public ConfigValue {
    public:
        bool value;

        BoolConfigValue(INIFile* file, const PGE::String& section, const PGE::String& name, bool defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

class StringConfigValue : public ConfigValue {
    public:
        PGE::String value;

        StringConfigValue(INIFile* file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

class ArrayConfigValue : public ConfigValue {
    public:
        std::vector<PGE::String> value;

        ArrayConfigValue(INIFile* file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

#endif // CONFIGVALUES_H_INCLUDED

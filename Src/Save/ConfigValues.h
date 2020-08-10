#ifndef CONFIGVALUES_H_INCLUDED
#define CONFIGVALUES_H_INCLUDED

#include<vector>

#include <Misc/String.h>

class ConfigValue {
    protected:
        const PGE::String file;
        const PGE::String section;
        const PGE::String name;

    public:
        ConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name);

        virtual void saveValue() const = 0;
        virtual void loadValue() = 0;
};

class IntConfigValue : public ConfigValue {
    public:
        int value;

        IntConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, int defaultValue);
        
        void saveValue() const override;
        void loadValue() override;
};

class BoolConfigValue : public ConfigValue {
    public:
        bool value;

        BoolConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, bool defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

class StringConfigValue : public ConfigValue {
    public:
        PGE::String value;

        StringConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

class ArrayConfigValue : public ConfigValue {
    public:
        std::vector<PGE::String> value;

        ArrayConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue);

        void saveValue() const override;
        void loadValue() override;
};

#endif // CONFIGVALUES_H_INCLUDED

#include "ConfigValues.h"

#include "../Utils/INI.h"

ConfigValue::ConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name) :
    file(file), section(section), name(name) {}

IntConfigValue::IntConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, int defaultValue)
    : ConfigValue(file, section, name) {
    value = defaultValue;
}

void IntConfigValue::saveValue() const {
    putINIValue(file, section, name, value);
}

void IntConfigValue::loadValue() {
    value = getINIInt(file, section, name, value);
}

BoolConfigValue::BoolConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, bool defaultValue)
    : ConfigValue(file, section, name) {
    value = defaultValue;
}

void BoolConfigValue::saveValue() const {
    putINIValue(file, section, name, value ? "true" : "false");
}

void BoolConfigValue::loadValue() {
    value = getINIBool(file, section, name, value);
}

StringConfigValue::StringConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue)
    : ConfigValue(file, section, name) {
    value = defaultValue;
}

void StringConfigValue::saveValue() const {
    putINIValue(file, section, name, value);
}

void StringConfigValue::loadValue() {
    value = getINIString(file, section, name, value);
}

ArrayConfigValue::ArrayConfigValue(const PGE::String& file, const PGE::String& section, const PGE::String& name, const PGE::String& defaultValue)
    : ConfigValue(file, section, name) {
    value = defaultValue.split('|', true);
}

void ArrayConfigValue::saveValue() const {
    putINIValue(file, section, name, PGE::String::join(value, '|'));
}

void ArrayConfigValue::loadValue() {
    PGE::String newVal = getINIString(file, section, name, "");
    if (!newVal.isEmpty()) {
        value = newVal.split('|', true);
    }
}

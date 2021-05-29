#include <PGE/File/FilePath.h>

#include "IntProperty.h"

IntProperty::IntProperty(const PGE::String& name, int defaultVal) : XMLProperty(name) {
    value = defaultVal;
}

void IntProperty::setValue(int val) {
    value = val;
}

int IntProperty::getValue() const {
    return value;
}

void IntProperty::readXML(const tinyxml2::XMLElement* element) {
    tinyxml2::XMLError result = element->QueryIntAttribute("value", &value);
    if (result != tinyxml2::XML_SUCCESS) {
        // TODO: Console error.
    }
}

void IntProperty::writeXML(tinyxml2::XMLElement* element) const {
    element->SetAttribute("value", PGE::String::fromInt(value).cstr());
}

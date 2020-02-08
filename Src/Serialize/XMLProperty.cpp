#include "XMLProperty.h"

XMLProperty::XMLProperty(const PGE::String& name) {
    this->xmlName = name;
}

PGE::String XMLProperty::getXMLName() const {
    return xmlName;
}

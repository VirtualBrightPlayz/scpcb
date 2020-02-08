#ifndef INTPROPERTY_H_INCLUDED
#define INTPROPERTY_H_INCLUDED

#include "XMLProperty.h"

class IntProperty : public XMLProperty {
private:
    int value;

public:
    IntProperty(const PGE::String& name, int defaultVal = 0);
    
    void setValue(int val);
    int getValue() const;

    virtual void readXML(const tinyxml2::XMLElement* element) override;
    virtual void writeXML(tinyxml2::XMLElement* element) const override;
};

#endif // INTPROPERTY_H_INCLUDED

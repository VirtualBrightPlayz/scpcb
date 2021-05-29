#ifndef XMLPROPERTY_H_INCLUDED
#define XMLPROPERTY_H_INCLUDED

#include <tinyxml2.h>
#include <PGE/String/String.h>

class XMLProperty {
private:
    PGE::String xmlName;
    
protected:
    XMLProperty(const PGE::String& name);
    ~XMLProperty()=default;
    
public:
    PGE::String getXMLName() const;
    
    virtual void readXML(const tinyxml2::XMLElement* element)=0;
    virtual void writeXML(tinyxml2::XMLElement* element) const=0;
};

#endif // XMLPROPERTY_H_INCLUDED

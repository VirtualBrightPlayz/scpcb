#ifndef XMLPROPERTY_H_INCLUDED
#define XMLPROPERTY_H_INCLUDED

#include <Misc/String.h>

class XMLProperty {
private:
    PGE::String name;
    
protected:
    PGE::String value;
    
    XMLProperty(const PGE::String& name);
};

#endif // XMLPROPERTY_H_INCLUDED

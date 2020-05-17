#ifndef GLOBAL_H_INCLUDED
#define GLOBAL_H_INCLUDED

#include <Misc/String.h>
#include <angelscript.h>
#include <tinyxml2.h>

class ScriptModule;
class Type;

class ScriptGlobal {
private:
    const ScriptModule* module;

    int index;
    PGE::String name;
    PGE::String varNamespace;

    Type* type;
    bool isClassType;

public:
    ScriptGlobal(ScriptModule* module, int index);
    ~ScriptGlobal()=default;

    void SaveXML(tinyxml2::XMLElement* element) const;
};

#endif // GLOBAL_H_INCLUDED

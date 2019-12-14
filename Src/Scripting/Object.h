#ifndef OBJECT_H_INCLUDED
#define OBJECT_H_INCLUDED

#include <Misc/String.h>
#include <angelscript.h>

class ScriptClass;

class ScriptObject {
    private:
        ScriptClass* scriptClass;
        asIScriptObject* angelScriptObject;

        int getPropertyIndex(const PGE::String& name) const;
    public:
        ScriptObject(ScriptClass* clss, asIScriptObject* asObj);
        ~ScriptObject();

        asIScriptObject* getAngelScriptObject() const;

        PGE::String getPropertyString(const PGE::String& propertyName) const;
        //PGE::String setPropertyString(const PGE::String& propertyName) const;
};

#endif

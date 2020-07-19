#ifndef REFLECTIONDEFINITIONS_H_INCLUDED
#define REFLECTIONDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"

class CScriptArray;
class ScriptClass;
class Reflection;
class ReflectionDefinitions : NativeDefinition {
    private:
        ScriptManager* scriptManager;
    public:
        ReflectionDefinitions(ScriptManager* mgr);

        CScriptArray* getDerivedNames(Reflection* reflection);
        void setConstructorArgInt(int index, int val, Reflection* reflection);
        void setConstructorArgObj(int index, void* val, int typeId, Reflection* reflection);
};

#endif

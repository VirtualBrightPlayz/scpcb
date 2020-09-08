#ifndef REFLECTIONDEFINITIONS_H_INCLUDED
#define REFLECTIONDEFINITIONS_H_INCLUDED

#include "../NativeDefinition.h"

class ScriptManager;
class CScriptArray;
class ScriptClass;
class Reflection;
class ReflectionDefinitions : NativeDefinition {
    private:
        ScriptManager* scriptManager;

        void reflectionConstructor(asITypeInfo* typeInfo, void* memory);
        void reflectionDestructor(void* memory);
    public:
        ReflectionDefinitions(ScriptManager* mgr);
};

#endif

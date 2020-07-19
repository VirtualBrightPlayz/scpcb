#include "ReflectionDefinitions.h"
#include "../ScriptClass.h"
#include "../ScriptModule.h"
#include "CachedArgument.h"

class Reflection {
    private:
        bool initialized;
        ScriptClass* scriptClass;
        ScriptManager* scriptManager;
        asITypeInfo* typeInfo;
        struct ConstructorParam {
            Type* type;
            void* value;
        };

        int selectedDerivedIndex;
        std::vector<CachedArgument> arguments;

        void allocArguments(int index) {
            arguments.resize(index+1);
        }
    public:
        Reflection(asITypeInfo* ti) {
            typeInfo = ti->GetSubType();
            PGE::String typeName = typeInfo->GetName();
            initialized = false;
            scriptClass = nullptr;
            scriptManager = nullptr;
            selectedDerivedIndex = 0;
        }

        void initialize(ScriptManager* mgr) {
            if (initialized) { return; }

            initialized = true;

            scriptManager = mgr;

            PGE::String typeName = typeInfo->GetName();
            int typeId = typeInfo->GetTypeId();

            scriptClass = mgr->getClassByTypeId(typeId);
        }

        int getDerivedCount() {
            return scriptClass->getDerivedClasses().size();
        }

        PGE::String getDerivedName(int index) {
            return scriptClass->getDerivedClasses()[index]->getName();
        }

        void clearArguments() {
            arguments.clear();
        }

        void setConstructorArgument(int index, int val) {
            allocArguments(index);
            arguments[index].type = Type::Int32;
            arguments[index].value.i32 = val;
        }

        void setConstructorArgument(int index, unsigned int val) {
            allocArguments(index);
            arguments[index].type = Type::Int32;
            arguments[index].value.u32 = val;
        }

        void setConstructorArgument(int index, float val) {
            allocArguments(index);
            arguments[index].type = Type::Float;
            arguments[index].value.f = val;
        }

        void setConstructorArgument(int index, double val) {
            allocArguments(index);
            arguments[index].type = Type::Double;
            arguments[index].value.d = val;
        }

        void setConstructorArgument(int index, void* val, int typeId) {
            ScriptClass* type = scriptManager->getClassByTypeId(typeId);
            
            allocArguments(index);
            arguments[index].type = type;
            arguments[index].value.ptr = val;
        }

        void* callConstructor() {
            //TODO: implement
            return nullptr;
        }
};


void reflectionConstructor(asITypeInfo* typeInfo, void* memory) {
    new(memory) Reflection(typeInfo);
}

void reflectionDestructor(void* memory) {
    ((Reflection*)memory)->~Reflection();
}

ReflectionDefinitions::ReflectionDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();
    scriptManager = mgr;

    engine->RegisterObjectType("Reflection<class T>", sizeof(Reflection), asOBJ_VALUE | asOBJ_APP_CLASS_ALLINTS | asOBJ_TEMPLATE | asGetTypeTraits<Reflection>());
    engine->RegisterObjectBehaviour("Reflection<T>", asBEHAVE_CONSTRUCT, "void f(int&in)", asFUNCTION(reflectionConstructor), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("Reflection<T>", asBEHAVE_DESTRUCT, "void f()", asFUNCTION(reflectionDestructor), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("Reflection<T>", "int getDerivedCount()", asMETHOD(ReflectionDefinitions, getDerivedCount), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectMethod("Reflection<T>", "string getDerivedName(int index)", asMETHOD(ReflectionDefinitions, getDerivedName), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectMethod("Reflection<T>", "void setConstructorArgument(int index, int val)", asMETHOD(ReflectionDefinitions, setConstructorArgInt), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectMethod("Reflection<T>", "void setConstructorArgument(int index, ?&in val)", asMETHOD(ReflectionDefinitions, setConstructorArgObj), asCALL_THISCALL_OBJLAST, this);
}

int ReflectionDefinitions::getDerivedCount(Reflection* reflection) {
    reflection->initialize(scriptManager);
    return reflection->getDerivedCount();
}

PGE::String ReflectionDefinitions::getDerivedName(int index, Reflection* reflection) {
    reflection->initialize(scriptManager);
    return reflection->getDerivedName(index);
}

void ReflectionDefinitions::setConstructorArgInt(int index, int val, Reflection* reflection) {
    reflection->initialize(scriptManager);
    reflection->setConstructorArgument(index, val);
}

void ReflectionDefinitions::setConstructorArgObj(int index, void* val, int typeId, Reflection* reflection) {
    reflection->initialize(scriptManager);
    reflection->setConstructorArgument(index, val, typeId);
}

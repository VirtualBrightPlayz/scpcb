#ifndef EVENTDEFINITION_H_INCLUDED
#define EVENTDEFINITION_H_INCLUDED

#include <angelscript.h>
#include <map>
#include <Misc/String.h>
#include "../ScriptFunction.h"
#include "../NativeDefinition.h"

class EventDefinition : public NativeDefinition {
    private:
        PGE::String name;
        ScriptFunction::Signature signature;
        std::vector<asIScriptFunction*> registeredCallbacks;
        asIScriptContext* scriptContext;
        int latestCallbackId;

        void registerCallback(asIScriptFunction* f);
        void unregisterCallback(asIScriptFunction* f);

        struct Argument {
            PGE::String name;
            Type* type;
            union Value {
                Value();
                int32_t i32;
                uint32_t u32;
                float f;
                double d;
            } value;
            Argument(const PGE::String& nm, Type* t);
        };
        std::vector<Argument> arguments;
    public:
        EventDefinition(ScriptManager* mgr, const PGE::String& nm, const ScriptFunction::Signature& sgntr);

        void setArgument(const PGE::String& argument, int32_t i32);
        void setArgument(const PGE::String& argument, uint32_t u32);
        void setArgument(const PGE::String& argument, float f);
        void setArgument(const PGE::String& argument, double d);
        /*void setArgument(const PGE::String& argument, const PGE::String& s);
        void setArgument(const PGE::String& argument, ScriptObject* srcObj);
        void setArgumentNative(const PGE::String& argument, void* natObj);*/
        void execute();
};

#endif

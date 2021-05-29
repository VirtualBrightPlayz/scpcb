#ifndef EVENTDEFINITION_H_INCLUDED
#define EVENTDEFINITION_H_INCLUDED

#include <map>

#include <angelscript.h>

#include <PGE/String/String.h>

#include "../CachedArgument.h"
#include "../NativeDefinition.h"
#include "../ScriptFunction.h"

class ScriptManager;

class EventDefinition : public NativeDefinition {
    private:
        PGE::String name;
        ScriptFunction::Signature signature;
        std::vector<ScriptFunction*> registeredCallbacks;
        int latestCallbackId;

        void registerCallback(asIScriptFunction* f);
        void unregisterCallback(asIScriptFunction* f);

        std::vector<CachedArgument> arguments;

        ScriptManager* scriptManager;

    public:
        EventDefinition(ScriptManager* mgr, const PGE::String& nm, std::vector<ScriptFunction::Signature::Argument> argList);

        void setArgument(const PGE::String& argument, int32_t i32);
        void setArgument(const PGE::String& argument, uint32_t u32);
        void setArgument(const PGE::String& argument, float f);
        void setArgument(const PGE::String& argument, double d);
        // TODO
        /*void setArgument(const PGE::String& argument, const PGE::String& s);
        void setArgument(const PGE::String& argument, ScriptObject* srcObj);
        void setArgumentNative(const PGE::String& argument, void* natObj);*/
        void execute();
};

#endif // EVENTDEFINITION_H_INCLUDED

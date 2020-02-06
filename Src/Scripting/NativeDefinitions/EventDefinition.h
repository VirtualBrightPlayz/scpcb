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
    public:
        EventDefinition(const PGE::String& nm, const ScriptFunction::Signature& sgntr);

        void setArgument(const PGE::String& argument, int32_t i32);
        void setArgument(const PGE::String& argument, uint32_t u32);
        void setArgument(const PGE::String& argument, float f);
        void setArgument(const PGE::String& argument, double d);
        void setArgument(const PGE::String& argument, const PGE::String& s);
        void setArgument(const PGE::String& argument, ScriptObject* obj);
        void setArgumentNative(const PGE::String& argument, void* obj);
        void execute();

        void registerToEngine(ScriptManager* mgr) override;
        void cleanup() override;
};

#endif

#ifndef SCRIPTMANAGER_H_INCLUDED
#define SCRIPTMANAGER_H_INCLUDED

#include <angelscript.h>

#include <PGE/String/String.h>
#include <PGE/ResourceManagement/Resource.h>

#include "StringFactory.h"

class ScriptClass;
class ScriptModule;

class ScriptManager {
    private:
        // We're relying on member destruction order here
        // 1. Engine 2. StringFactory 3. log
        std::vector<asSMessageInfo> log;
    
        std::unique_ptr<StringFactory> stringFactory;

        class RaiiEngine : public PGE::Resource<asIScriptEngine*> {
            public:
                RaiiEngine() { resource = asCreateScriptEngine(); }
                ~RaiiEngine() { resource->ShutDownAndRelease(); }
        } engine;

        std::vector<ScriptClass*> sharedClasses;
        std::vector<ScriptModule*> scriptModules;

    public:
        ScriptManager();

        void messageCallback(const asSMessageInfo* msg, void* param);
        void contextExceptionCallback(asIScriptContext* ctx);

        asIScriptEngine* getAngelScriptEngine() const;

        ScriptClass* getSharedClassByTypeId(int typeId) const;
        ScriptClass* getClassByTypeId(int typeId) const;
        void registerSharedClass(ScriptClass* clss);
        const std::vector<ScriptModule*>& getScriptModules() const;
        void registerScriptModule(ScriptModule* mdl);
};

#endif // SCRIPTMANAGER_H_INCLUDED

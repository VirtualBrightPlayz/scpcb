#include "ScriptManager.h"

#include "ScriptClass.h"
#include "ScriptModule.h"

#include <iostream>
#include <scriptarray/scriptarray.h>

using namespace PGE;

static std::unordered_map<asIScriptEngine*, std::vector<asIScriptContext*>> contextPools;
static asIScriptContext* requestContextCallback(asIScriptEngine* engine, void* param) {
    asIScriptContext* ctx;
    if(contextPools[engine].size() > 0) {
        ctx = contextPools[engine].front();
        contextPools[engine].pop_back();
    } else {
        ctx = engine->CreateContext();
    }
    ctx->SetExceptionCallback(asMETHOD(ScriptManager, contextExceptionCallback), param, asCALL_THISCALL);

    return ctx;
}

static void returnContextToPool(asIScriptEngine* engine, asIScriptContext* ctx, void* param) {
    contextPools[engine].push_back(ctx);

    ctx->Unprepare();
}

ScriptManager::ScriptManager() {
    contextPools.emplace(engine, std::vector<asIScriptContext*>());

    engine->SetContextCallbacks(requestContextCallback, returnContextToPool, this);

    engine->SetMessageCallback(asMETHOD(ScriptManager, messageCallback), this, asCALL_THISCALL);

    stringFactory = std::make_unique<StringFactory>(engine);

    RegisterScriptArray(engine, true);
}

void ScriptManager::contextExceptionCallback(asIScriptContext* context) {
    asSMessageInfo info;
    info.message = context->GetExceptionString();
    info.row = context->GetExceptionLineNumber(&info.col, &info.section);
    info.type = asEMsgType::asMSGTYPE_ERROR;
    messageCallback(&info, nullptr);
}

void ScriptManager::messageCallback(const asSMessageInfo* msg, void* param) {
    String typeStr;
    switch (msg->type) {
        case asEMsgType::asMSGTYPE_ERROR: {
            typeStr = "ERROR";
        } break;
        case asEMsgType::asMSGTYPE_WARNING: {
            typeStr = "WARN";
        } break;
        case asEMsgType::asMSGTYPE_INFORMATION: {
            typeStr = "INFO";
        } break;
    }
    std::cout << msg->section << "(" << msg->row << ", " << msg->col << "): " << typeStr << ": " << msg->message << std::endl;
    log.push_back(*msg);
}

asIScriptEngine* ScriptManager::getAngelScriptEngine() const {
    engine->SetDefaultNamespace("");
    return engine;
}

ScriptClass* ScriptManager::getSharedClassByTypeId(int typeId) const {
    for (ScriptClass* sc : sharedClasses) {
        if (sc->getTypeId() == typeId) { return sc; }
    }
    return nullptr;
}

ScriptClass* ScriptManager::getClassByTypeId(int typeId) const {
    ScriptClass* scriptClass = getSharedClassByTypeId(typeId);

    if (scriptClass != nullptr) { return scriptClass; }

    for (ScriptModule* m : scriptModules) {
        scriptClass = m->getClassByTypeId(typeId);
        if (scriptClass != nullptr) { return scriptClass; }
    }

    return nullptr;
}

void ScriptManager::registerSharedClass(ScriptClass* clss) {
    sharedClasses.push_back(clss);
}

const std::vector<ScriptModule*>& ScriptManager::getScriptModules() const {
    return scriptModules;
}

void ScriptManager::registerScriptModule(ScriptModule* mdl) {
    scriptModules.push_back(mdl);
}

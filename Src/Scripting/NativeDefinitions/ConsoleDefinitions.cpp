#include "ConsoleDefinitions.h"
#ifdef DEBUG
    #include <iostream>
#endif

#include "../../Utils/MathUtil.h"
#include "../ScriptManager.h"
#include "../ScriptModule.h"
#include "../ScriptObject.h"

ConsoleDefinitions::ConsoleDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();
    scriptContext = engine->CreateContext();
    //addMsgCtx = engine->CreateContext();

    engine->SetDefaultNamespace("Console");
    engine->RegisterGlobalFunction("void register(const string&in name, const string&in helpText, function&in command)", asMETHOD(ConsoleDefinitions, registerCommand), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void register(const string&in name, function&in command)", asMETHOD(ConsoleDefinitions, registerCommandNoHelp), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void execute(const string&in)", asMETHOD(ConsoleDefinitions, executeCommand), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void log(?&in content)", asMETHOD(ConsoleDefinitions, log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void warning(?&in content)", asMETHOD(ConsoleDefinitions, warning), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void error(?&in content)", asMETHOD(ConsoleDefinitions, error), asCALL_THISCALL_ASGLOBAL, this);
}

ConsoleDefinitions::~ConsoleDefinitions() {
    scriptContext->Release();
    addMsgCtx->Release();
}

void ConsoleDefinitions::setUp(ScriptManager* mgr) {
    std::vector<ScriptModule*> modules = mgr->getScriptModules();
    for (const auto& mod : modules) {
        if (mod->getAngelScriptModule()->GetName() == "RootScript") {
            addMsgCtx->Prepare(engine->GetModule("RootScript")->GetTypeInfoByName("ConsoleMenu")->GetMethodByName("addConsoleMessage"));
            std::cout << "POGGERS " << mod->getGlobalByName("ConsoleMenu::instance") << std::endl;
            addMsgCtx->SetObject(mod->getGlobalByName("ConsoleMenu::instance")->getObject()->getAngelScriptObject());
            break;
        }
    }
}

void ConsoleDefinitions::registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId) {
    asIScriptFunction* func = (asIScriptFunction*)(((typeId & asTYPEID_OBJHANDLE) != 0) ? *((void**)f) : f);
    for (unsigned int i = 0; i < func->GetParamCount(); i++) {
        int paramTypeId;
        if (func->GetParam(i, &paramTypeId) < 0) {
            throw std::runtime_error("ptooey?");
        } else {
            if (paramTypeId != asTYPEID_BOOL && paramTypeId != asTYPEID_INT32 && paramTypeId != asTYPEID_FLOAT && paramTypeId != engine->GetStringFactoryReturnTypeId()) {
                throw std::runtime_error("STINKY INVALID TYPE");
            }
        }
    }
    // TODO: Maybe reconsider map, performance increase when executing, longer load times.
    Command newCommand = { func, name, helpText, false };
    for (auto comSet : commands) {
        Command otherCommand = comSet.second;
        // This will also catch a command that already has had its name changed.
        if (otherCommand.name.equalsIgnoreCase(otherCommand.duplicateName ? PGE::String(otherCommand.func->GetModuleName()) + ":" + name : name)) {
            if (!otherCommand.duplicateName) {
                otherCommand.duplicateName = true;
                commands.erase(commands.find(otherCommand.name.getHashCode()));
                commands.emplace((PGE::String(otherCommand.func->GetModuleName()) + ":" + otherCommand.name).getHashCode(), otherCommand);
            }
            
            newCommand.duplicateName = true;
            if (otherCommand.name.equalsIgnoreCase(newCommand.name)) { throw std::runtime_error("lol double command"); }
        }
    }
    commands.emplace((newCommand.duplicateName ? PGE::String(func->GetModuleName()) + ":" + name : name).getHashCode(), newCommand);
}

void ConsoleDefinitions::registerCommandNoHelp(const PGE::String& name, void* f, int typeId) {
    registerCommand(name, "", f, typeId);
}

void ConsoleDefinitions::executeCommand(const PGE::String& in) {
    std::vector<PGE::String> params = in.split(" ", true);
    std::map<long long, Command>::iterator find = commands.find(params[0].getHashCode());
    if (find != commands.end()) {
        asIScriptFunction* func = find->second.func;
        params.erase(params.begin());
        if (func->GetParamCount() != params.size()) {
            //console->addConsoleMessage("ARGUMENT SIZE MISMATCH", PGE::Color::Red);
            return;
        }
        if (scriptContext->Prepare(func) < 0) { throw std::runtime_error("ptooey! 2"); }
        PGE::String errMsg;
        for (unsigned int i = 0; i < func->GetParamCount(); i++) {
            int paramTypeId;
            if (func->GetParam(i, &paramTypeId) >= 0) {
                if (paramTypeId == asTYPEID_BOOL) {
                    if (params[i].toLower().equals("true") || params[i].equals("1")) {
                        scriptContext->SetArgByte(i, 1);
                    } else if (params[i].toLower().equals("false") || params[i].equals("0")) {
                        scriptContext->SetArgByte(i, 0);
                    } else {
                        errMsg = "NOT BOOL";
                        break;
                    }
                } else if (paramTypeId == asTYPEID_INT32) {
                    bool success;
                    int arg = params[i].toInt(success);
                    if (!success) {
                        errMsg = "NOT INT";
                        break;
                    }

                    // If the user enters a float.
                    if (!MathUtil::equalFloats((float)arg, params[i].toFloat())) {
                        //console->addConsoleMessage("Loss of data!", PGE::Color::Yellow);
                    }

                    scriptContext->SetArgDWord(i, arg);
                } else if (paramTypeId == asTYPEID_FLOAT) {
                    bool success;
                    float arg = params[i].toFloat(success);
                    if (!success) {
                        errMsg = "NOT FLOAT";
                        break;
                    }

                    scriptContext->SetArgFloat(i, arg);
                } else if (paramTypeId == scriptContext->GetEngine()->GetStringFactoryReturnTypeId()) {
                    scriptContext->SetArgObject(i, (void*)&(params[i]));
                }
            } else {
                throw std::runtime_error("ptooey! 3");
            }
        }

        if (errMsg.isEmpty()) {
            scriptContext->Execute();
        } else {
            //console->addConsoleMessage(errMsg, PGE::Color::Red);
        }
        scriptContext->Unprepare();
    } else {
        //addConsoleMessage("No command found", PGE::Color::Red);
    }
}

void ConsoleDefinitions::internalLog(void* ref, int typeId, LogType type) const {
    PGE::String typeString;
    switch (type) {
        case LogType::Log: {
            typeString = "Log";
        } break;
        case LogType::Warning: {
            typeString = "Warn";
        } break;
        case LogType::Error: {
            typeString = "Err";
        } break;
    }
    PGE::String content;
    if (typeId == engine->GetStringFactoryReturnTypeId()) {
        content = *(PGE::String*)ref;
    } else {
        switch (typeId) {
            case asTYPEID_VOID: { // This should never happen.
                throw new std::runtime_error(("VOID PARAMETER! " + PGE::String((uint64_t)ref, true)).cstr());
            } break;
            case asTYPEID_BOOL: {
                content = (*(bool*)ref) ? "True" : "False";
            } break;
            case asTYPEID_INT8: {
                content = PGE::String((int32_t) * (int8_t*)ref);
            } break;
            case asTYPEID_INT16: {
                content = PGE::String((int32_t) * (int16_t*)ref);
            } break;
            case asTYPEID_INT32: {
                content = PGE::String(*(int32_t*)ref);
            } break;
            case asTYPEID_INT64: {
                content = PGE::String(*(int64_t*)ref);
            } break;
            case asTYPEID_UINT8: {
                content = PGE::String((uint32_t) * (uint8_t*)ref);
            } break;
            case asTYPEID_UINT16: {
                content = PGE::String((uint32_t) * (uint16_t*)ref);
            } break;
            case asTYPEID_UINT32: {
                content = PGE::String(*(uint32_t*)ref);
            } break;
            case asTYPEID_UINT64: {
                content = PGE::String(*(uint64_t*)ref);
            } break;
            case asTYPEID_FLOAT: {
                content = PGE::String(*(float*)ref);
            } break;
            case asTYPEID_DOUBLE: {
                content = PGE::String(*(double*)ref);
            } break;
            default: { // Object.
                asITypeInfo* typeInfo = engine->GetTypeInfoById(typeId);
                asIScriptFunction* toString = typeInfo->GetMethodByName("toString");
                if (toString != nullptr) {
                    asIScriptContext* context = engine->RequestContext();
                    context->Prepare(toString);
                    context->SetObject(ref);
                    context->Execute();
                    void* ret = context->GetAddressOfReturnValue();
                    if (ret != nullptr) {
                        content = *(PGE::String*)ret;
                        engine->ReturnContext(context);
                        break;
                    }
                }
                content = PGE::String(typeInfo->GetName()) + "@" + PGE::String((uint64_t)ref, true);
            } break;
        }
    }
#ifdef DEBUG
    std::ostream& out = (type == LogType::Error ? std::cerr : std::cout);
    out << "Debug::" << typeString << ": " << content << std::endl;
#endif
    //console->addConsoleMessage("Debug::" + typeString + ": " + content);
    if (type == LogType::Error) {
        throw new std::runtime_error(("ERROR! " + content).cstr());
    }
}

void ConsoleDefinitions::log(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Log);
}

void ConsoleDefinitions::warning(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Warning);
}

void ConsoleDefinitions::error(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Error);
}

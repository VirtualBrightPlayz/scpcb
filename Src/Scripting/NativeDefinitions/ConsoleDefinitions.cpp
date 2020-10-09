#include "ConsoleDefinitions.h"
#ifdef DEBUG
    #include <iostream>
#endif

#include "../../Utils/MathUtil.h"
#include "../../Input/KeyBinds.h"
#include "../ScriptManager.h"
#include "../ScriptModule.h"
#include "../ScriptObject.h"
#include "../ScriptGlobal.h"

void ConsoleDefinitions::helpInternal(std::vector<PGE::String> params) {
    if (params.size() == 2) {
        std::map<long long, Command>::iterator find = commands.find(params[1].getHashCode());
        if (find == commands.end()) {
            addConsoleMessage("That command doesn't exist", PGE::Color::Yellow);
        } else {
            if (find->second.helpText.isEmpty()) {
                addConsoleMessage("There is no help available for that command.", PGE::Color::Yellow);
            } else {
                addConsoleMessage(find->second.helpText, PGE::Color::Blue);
            }
        }
    } else if (params.size() == 1) {
        for (const auto& comSet : commands) {
            Command command = comSet.second;
            addConsoleMessage(command.name, PGE::Color::Cyan);
        }
    } else {
        addConsoleMessage("ARGUMENT SIZE MISMATCH", PGE::Color::Red);
    }
}

void ConsoleDefinitions::bindInternal(std::vector<PGE::String> params) {
    params.erase(params.begin());
    PGE::UserInput* key = keyBinds->stringToInput(params.back());
    if (key == nullptr) {
        addConsoleMessage("That key doesn't exist.", PGE::Color::Red);
    } else {
        params.pop_back();
        keyBinds->bindCommand(PGE::String::join(params, " "), key);
    }
}

void ConsoleDefinitions::unbindInternal(std::vector<PGE::String> params) {
    params.erase(params.begin());
    PGE::UserInput* key = keyBinds->stringToInput(params.back());
    if (key == nullptr) {
        addConsoleMessage("That key doesn't exist.", PGE::Color::Red);
    } else {
        params.pop_back();
        keyBinds->unbindCommand(PGE::String::join(params, " "), key);
    }
}

ConsoleDefinitions::ConsoleDefinitions(ScriptManager* mgr, KeyBinds* kb) {
    keyBinds = kb;

    engine = mgr->getAngelScriptEngine();
    scriptContext = engine->CreateContext();
    msgContext = engine->CreateContext();

    engine->SetDefaultNamespace("Console");
    engine->RegisterGlobalFunction("void register(const string&in name, const string&in helpText, function&in command)", asMETHOD(ConsoleDefinitions, registerCommand), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void register(const string&in name, function&in command)", asMETHOD(ConsoleDefinitions, registerCommandNoHelp), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void execute(const string&in)", asMETHOD(ConsoleDefinitions, executeCommand), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void log(?&in content)", asMETHOD(ConsoleDefinitions, log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void warning(?&in content)", asMETHOD(ConsoleDefinitions, warning), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void error(?&in content)", asMETHOD(ConsoleDefinitions, error), asCALL_THISCALL_ASGLOBAL, this);

    registerCommandInternal("help", "Provides a description of a specified command. Use it with no parameters to list all commands.", &ConsoleDefinitions::helpInternal);
    registerCommandInternal("bind", "Binds a command to a key.", &ConsoleDefinitions::bindInternal);
    registerCommandInternal("unbind", "Unbinds the given command from the key.", &ConsoleDefinitions::unbindInternal);
}

ConsoleDefinitions::~ConsoleDefinitions() {
    scriptContext->Release();
    msgContext->Release();
}

void ConsoleDefinitions::setUp(ScriptManager* mgr) {
    std::vector<ScriptModule*> modules = mgr->getScriptModules();
    for (const auto& mod : modules) {
        if (PGE::String(mod->getAngelScriptModule()->GetName()) == "RootScript") {
            consoleInstance = mod->getGlobalByName("instance", "ConsoleMenu")->getObject()->getAngelScriptObject();
            addConsoleMsgFunc = engine->GetModule("RootScript")->GetTypeInfoByName("ConsoleMenu")->GetMethodByName("addConsoleMessage");
            break;
        }
    }
}

void ConsoleDefinitions::registerCommandInternal(const PGE::String& name, const PGE::String& helpText, void(ConsoleDefinitions::*nativFunc)(std::vector<PGE::String>)) {
    Command c = { nullptr, nativFunc, name, helpText };
    commands.emplace(name.getHashCode(), c);
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
    Command newCommand = { func, nullptr, name, helpText };
    commands.emplace(name.getHashCode(), newCommand);
    std::cout << "Command: " << name << std::endl;
}

void ConsoleDefinitions::registerCommandNoHelp(const PGE::String& name, void* f, int typeId) {
    registerCommand(name, "", f, typeId);
}

void ConsoleDefinitions::executeCommand(const PGE::String& in) {
    std::vector<PGE::String> params = in.split(" ", true);
    std::map<long long, Command>::iterator find = commands.find(params[0].getHashCode());
    if (find != commands.end()) {
        if (find->second.func == nullptr) {
            (this->*find->second.nativFunc)(params);
        } else {
            asIScriptFunction* func = find->second.func;
            params.erase(params.begin());
            // TODO: Do we need to check all AS funcs or none?
            if (scriptContext->Prepare(func) < 0) { throw std::runtime_error("ptooey! 2"); }
            if (func->GetParamCount() != params.size()) {
                const char* defaultParam;
                func->GetParam((asUINT)params.size(), nullptr, nullptr, nullptr, &defaultParam);
                if (defaultParam == nullptr) {
                    scriptContext->Unprepare();
                    addConsoleMessage("ARGUMENT SIZE MISMATCH", PGE::Color::Red);
                    return;
                }
            }
            PGE::String errMsg;
            for (unsigned int i = 0; i < params.size(); i++) {
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
                            addConsoleMessage("Loss of data!", PGE::Color::Yellow);
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
                addConsoleMessage(errMsg, PGE::Color::Red);
            }
            scriptContext->Unprepare();
        }
    } else {
        addConsoleMessage("No command found", PGE::Color::Red);
    }
}

void ConsoleDefinitions::addConsoleMessage(const PGE::String& msg, const PGE::Color& color) {
    if (msgContext->Prepare(addConsoleMsgFunc) < 0) { throw std::runtime_error("prepare fail"); };
    if (msgContext->SetObject(consoleInstance) < 0) { throw std::runtime_error("setobj fail"); };
    if (msgContext->SetArgObject(0, (void*)&msg) < 0) { throw std::runtime_error("setarg0 fail"); };
    if (msgContext->SetArgObject(1, (void*)&color) < 0) { throw std::runtime_error("setarg1 fail"); };
    if (msgContext->Execute() < 0) { throw std::runtime_error("execute fail"); };
    if (msgContext->Unprepare() < 0) { throw std::runtime_error("unprepare fail"); };
}

void ConsoleDefinitions::internalLog(void* ref, int typeId, LogType type) {
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
                throw std::runtime_error("VOID PARAMETER!");
            } break;
            case asTYPEID_BOOL: {
                content = (*(bool*)ref) ? "True" : "False";
            } break;
            case asTYPEID_INT8: {
                content = PGE::String::fromInt((int) *(int8_t*)ref);
            } break;
            case asTYPEID_INT16: {
                content = PGE::String::fromInt((int) *(int16_t*)ref);
            } break;
            case asTYPEID_INT32: {
                // TODO: We assume that an int holds 32 bits here.
                content = PGE::String::format(*(int32_t*)ref, "%li");
            } break;
            case asTYPEID_INT64: {
                content = PGE::String::format(*(int64_t*)ref, "%lli");
            } break;
            case asTYPEID_UINT8: {
                content = PGE::String::format(*(uint8_t*)ref, "%u");
            } break;
            case asTYPEID_UINT16: {
                content = PGE::String::format(*(uint16_t*)ref, "%u");
            } break;
            case asTYPEID_UINT32: {
                content = PGE::String::format(*(uint32_t*)ref, "%lu");
            } break;
            case asTYPEID_UINT64: {
                content = PGE::String::format(*(uint64_t*)ref, "%llu");
            } break;
            case asTYPEID_FLOAT: {
                content = PGE::String::fromFloat(*(float*)ref);
            } break;
            case asTYPEID_DOUBLE: {
                content = PGE::String::format(*(double*)ref, "%lf");
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
                content = PGE::String(typeInfo->GetName()) + "@" + PGE::String::format((long long) ref, "%#010llx");
            } break;
        }
    }
#ifdef DEBUG
    std::ostream& out = (type == LogType::Error ? std::cerr : std::cout);
    out << "Debug::" << typeString << ": " << content << std::endl;
#endif
    if (type == LogType::Error) {
        throw std::runtime_error(("ERROR! " + content).cstr());
    }
}

void ConsoleDefinitions::log(void* ref, int typeId) {
    internalLog(ref, typeId, LogType::Log);
}

void ConsoleDefinitions::warning(void* ref, int typeId) {
    internalLog(ref, typeId, LogType::Warning);
}

void ConsoleDefinitions::error(void* ref, int typeId) {
    internalLog(ref, typeId, LogType::Error);
}

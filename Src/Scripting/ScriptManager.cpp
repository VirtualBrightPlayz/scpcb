#include "ScriptManager.h"
#include "ScriptClass.h"
#include <angelscript.h>
#include <Misc/String.h>
#include <map>
#include <stdlib.h>
#include <inttypes.h>
#include "AngelScriptAddons/scriptarray/scriptarray.h"

class StringFactory : public asIStringFactory {
    private:
        std::map<long long, StringPoolEntry> strPool;
    public:
        const void* GetStringConstant(const char* data, asUINT length) {
            asAcquireExclusiveLock();

            char* tempBuf = new char[(uint64_t)length + 1];
            memcpy(tempBuf, data, sizeof(char) * length);
            tempBuf[length] = '\0';

            PGE::String tempStr = tempBuf;
            delete[] tempBuf;

            std::map<long long, StringPoolEntry>::iterator poolEntry = strPool.find(tempStr.getHashCode());
            if (poolEntry != strPool.end()) {
                poolEntry->second.refCount++;
            } else {
                StringPoolEntry newEntry = StringPoolEntry(tempStr);
                newEntry.refCount = 1;
                strPool.emplace(tempStr.getHashCode(), newEntry);
                poolEntry = strPool.find(tempStr.getHashCode());
            }
            
            asReleaseExclusiveLock();

            return &(poolEntry->second.str);
        }

        int ReleaseStringConstant(const void* str) {
            int retVal = asSUCCESS;

            asAcquireExclusiveLock();

            PGE::String* deref = ((PGE::String*)str);
            std::map<long long, StringPoolEntry>::iterator poolEntry = strPool.find(deref->getHashCode());
            if (poolEntry != strPool.end()) {
                poolEntry->second.refCount--;
                if (poolEntry->second.refCount <= 0) {
                    strPool.erase(deref->getHashCode());
                }
            }

            asReleaseExclusiveLock();

            return retVal;
        }

        int GetRawStringData(const void* str, char* data, asUINT* length) const {
            if (str == nullptr) { return asERROR; }

            PGE::String deref = *((PGE::String*)str);
            if (length != nullptr) {
                asUINT& lengthRef = *length;
                lengthRef = deref.size();
            }
            if (data != nullptr) {
                memcpy(data, deref.cstr(), sizeof(char)*deref.size());
            }

            return asSUCCESS;
        }
};

static void constructString(PGE::String* thisPointer) {
    new(thisPointer) PGE::String();
}

static void copyConstructString(const PGE::String& other, PGE::String* thisPointer) {
    new(thisPointer) PGE::String(other);
}

static void destructString(PGE::String* thisPointer) {
    thisPointer->~String();
}

static PGE::String& assignString(const PGE::String& str, PGE::String& dest) {
    dest = str;
    return dest;
}

static PGE::String& addAssignString(const PGE::String& str, PGE::String& dest) {
    dest = PGE::String(dest, str);
    return dest;
}

static bool stringEquals(const PGE::String& lhs, const PGE::String& rhs) {
    return lhs.equals(rhs);
}

static PGE::String stringAdd(const PGE::String& rhs, const PGE::String& lhs) {
    return PGE::String(lhs, rhs);
}

static int stringLength(const PGE::String& str) {
    return str.size();
}

static long long stringGetHashCode(const PGE::String& str) {
    return str.getHashCode();
}

static PGE::String stringSubstrStartLen(int start, int count, const PGE::String& str) {
    return str.substr(start, count);
}

static char stringCharAt(int index, const PGE::String& str) {
    return str.charAt(index);
}

ScriptManager::ScriptManager() {
    engine = asCreateScriptEngine();

    engine->SetMessageCallback(asMETHOD(ScriptManager, messageCallback), this, asCALL_THISCALL);

    stringFactory = new StringFactory();

    engine->RegisterObjectType("string", sizeof(PGE::String), asOBJ_VALUE | asOBJ_APP_CLASS_CDAK);

    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,"void f()",asFUNCTION(constructString), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,"void f(const string& in)",asFUNCTION(copyConstructString), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectBehaviour("string",asBEHAVE_DESTRUCT,"void f()",asFUNCTION(destructString), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("string","string &opAssign(const string& in)",asFUNCTION(assignString), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("string","string &opAddAssign(const string& in)",asFUNCTION(addAssignString), asCALL_CDECL_OBJLAST);

    engine->RegisterObjectMethod("string","bool opEquals(const string& in) const",asFUNCTION(stringEquals), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("string","string opAdd(const string& in) const",asFUNCTION(stringAdd), asCALL_CDECL_OBJLAST);

    engine->RegisterObjectMethod("string", "uint length() const", asFUNCTION(stringLength), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("string", "uint64 getHashCode() const", asFUNCTION(stringGetHashCode), asCALL_CDECL_OBJLAST);
    engine->RegisterObjectMethod("string","string substr(int start, int end=-1) const",asFUNCTION(stringSubstrStartLen), asCALL_CDECL_OBJLAST);

    engine->RegisterObjectMethod("string","uint8 opIndex(uint) const",asFUNCTION(stringCharAt), asCALL_CDECL_OBJLAST);

    engine->RegisterStringFactory("string", stringFactory);

    RegisterScriptArray(engine, true);
}

ScriptManager::~ScriptManager() {
    engine->ShutDownAndRelease();
    delete ((StringFactory*)stringFactory);
}

void ScriptManager::messageCallback(const asSMessageInfo* msg, void* param) {
    LogEntry newLogEntry;
    switch (msg->type) {
        case asEMsgType::asMSGTYPE_ERROR: {
            newLogEntry.type = LogEntry::Type::AngelScriptError;
        } break;
        case asEMsgType::asMSGTYPE_WARNING: {
            newLogEntry.type = LogEntry::Type::AngelScriptWarning;
        } break;
        case asEMsgType::asMSGTYPE_INFORMATION: {
            newLogEntry.type = LogEntry::Type::AngelScriptInfo;
        } break;
    }
    newLogEntry.message = msg->message;
    newLogEntry.section = msg->section;
    newLogEntry.row = msg->row;
    newLogEntry.col = msg->col;

    log.push_back(newLogEntry);
}

asIScriptEngine* ScriptManager::getAngelScriptEngine() const {
    return engine;
}

ScriptClass* ScriptManager::getSharedClassByTypeId(int typeId) const {
    for (int i = 0; i < sharedClasses.size(); i++) {
        if (sharedClasses[i]->getTypeId() == typeId) { return sharedClasses[i]; }
    }
    return nullptr;
}

void ScriptManager::registerSharedClass(ScriptClass* clss) {
    sharedClasses.push_back(clss);
}

bool ScriptManager::isArrayTypeId(int typeId) const {
    return IsScriptArrayType(engine->GetTypeInfoById(typeId));
}

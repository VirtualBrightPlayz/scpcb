#include "ScriptManager.h"
#include <angelscript.h>
#include <Misc/String.h>
#include <map>
#include <stdlib.h>
#include <inttypes.h>

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

            return &(poolEntry->second);
        }

        int ReleaseStringConstant(const void* str) {
            int retVal = asSUCCESS;

            asAcquireExclusiveLock();

            StringPoolEntry deref = *((StringPoolEntry*)str);
            std::map<long long, StringPoolEntry>::iterator poolEntry = strPool.find(deref.str.getHashCode());
            if (poolEntry != strPool.end()) {
                poolEntry->second.refCount--;
                if (poolEntry->second.refCount <= 0) {
                    strPool.erase(deref.str.getHashCode());
                }
            }

            asReleaseExclusiveLock();

            return retVal;
        }

        int GetRawStringData(const void* str, char* data, asUINT* length) const {
            if (str == nullptr) { return asERROR; }

            StringPoolEntry deref = *((StringPoolEntry*)str);
            if (length != nullptr) {
                asUINT& lengthRef = *length;
                lengthRef = deref.str.size();
            }
            if (data != nullptr) {
                memcpy(data, deref.str.cstr(), sizeof(char)*deref.str.size());
            }

            return asSUCCESS;
        }
};

static void localMessageCallback(ScriptManager* scriptManager, const asSMessageInfo* msg, void* param) {
    scriptManager->messageCallback(msg, param);
}

static void ConstructStringGeneric(asIScriptGeneric* gen) {
    new (gen->GetObject()) StringPoolEntry();
}

static void CopyConstructStringGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* a = static_cast<StringPoolEntry*>(gen->GetArgObject(0));
    new (gen->GetObject()) StringPoolEntry(*a);
}

static void DestructStringGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* ptr = static_cast<StringPoolEntry*>(gen->GetObject());
    ptr->~StringPoolEntry();
}

ScriptManager::ScriptManager() {
    engine = asCreateScriptEngine();

    engine->SetMessageCallback(asFUNCTION(localMessageCallback), this, asCALL_CDECL_OBJFIRST);

    stringFactory = new StringFactory();

    engine->RegisterObjectType("string", sizeof(StringPoolEntry), asOBJ_VALUE | asOBJ_APP_CLASS_CDAK);

    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,  "void f()",asFUNCTION(ConstructStringGeneric), asCALL_GENERIC);
    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,  "void f(const string &in)",asFUNCTION(CopyConstructStringGeneric), asCALL_GENERIC);
    engine->RegisterObjectBehaviour("string",asBEHAVE_DESTRUCT,"void f()",asFUNCTION(DestructStringGeneric),asCALL_GENERIC);

    engine->RegisterStringFactory("string", stringFactory);
}

ScriptManager::~ScriptManager() {
    engine->ShutDownAndRelease();
    delete ((StringFactory*)stringFactory);
}

void ScriptManager::messageCallback(const asSMessageInfo* msg, void* param) {
    const char *type = "ERR ";
    if (msg->type==asMSGTYPE_WARNING) {
        type = "WARN";
    } else if (msg->type==asMSGTYPE_INFORMATION) {
        type = "INFO";
    }

    if (msg->type==asMSGTYPE_ERROR) {
        throw msg->message;
    }
}

asIScriptEngine* ScriptManager::getAngelScriptEngine() const {
    return engine;
}

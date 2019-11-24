#include "ScriptManager.h"
#include <angelscript.h>
#include <Misc/String.h>
#include <map>
#include <stdlib.h>
#include <inttypes.h>

class StringFactory : public asIStringFactory {
    private:
        struct PoolEntry {
            PGE::String str;
            int refCount;

            PoolEntry() {
                refCount = 0;
            }
        };
        std::map<long long, PoolEntry> strPool;
    public:
        const void* GetStringConstant(const char* data, asUINT length) {
            asAcquireExclusiveLock();

            char* tempBuf = new char[(uint64_t)length + 1];
            memcpy(tempBuf, data, sizeof(char) * length);
            tempBuf[length] = '\0';

            PGE::String tempStr = tempBuf;
            delete[] tempBuf;

            std::map<long long, PoolEntry>::iterator poolEntry = strPool.find(tempStr.getHashCode());
            if (poolEntry != strPool.end()) {
                poolEntry->second.refCount++;
            } else {
                PoolEntry newEntry;
                newEntry.refCount = 1;
                newEntry.str = tempStr;
                strPool.emplace(tempStr.getHashCode(), newEntry);
                poolEntry = strPool.find(tempStr.getHashCode());
            }
            
            asReleaseExclusiveLock();

            return &(poolEntry->first);
        }

        int ReleaseStringConstant(const void* str) {
            int retVal = asSUCCESS;

            asAcquireExclusiveLock();

            PGE::String temp = (const char*)str;
            std::map<long long, PoolEntry>::iterator poolEntry = strPool.find(temp.getHashCode());
            if (poolEntry != strPool.end()) {
                poolEntry->second.refCount--;
                if (poolEntry->second.refCount <= 0) {
                    strPool.erase(temp.getHashCode());
                }
            } else {
                retVal = asERROR;
            }

            asReleaseExclusiveLock();

            return retVal;
        }

        int GetRawStringData(const void* str, char* data, asUINT* length) const {
            if (str == nullptr) { return asERROR; }

            long long hashCode = *((long long*)str);

            std::map<long long, PoolEntry>::const_iterator poolEntry = strPool.find(hashCode);
            if (length != nullptr) {
                asUINT& lengthRef = *length;
                lengthRef = poolEntry->second.str.size();
            }
            if (data != nullptr) {
                memcpy(data, poolEntry->second.str.cstr(), sizeof(char)*poolEntry->second.str.size());
            }

            return asSUCCESS;
        }
};

static void localMessageCallback(ScriptManager* scriptManager, const asSMessageInfo* msg, void* param) {
    scriptManager->messageCallback(msg, param);
}

ScriptManager::ScriptManager() {
    engine = asCreateScriptEngine();

    engine->SetMessageCallback(asFUNCTION(localMessageCallback), this, asCALL_CDECL_OBJFIRST);

    stringFactory = new StringFactory();

    engine->RegisterStringFactory("string", stringFactory);
}

ScriptManager::~ScriptManager() {
    engine->ShutDownAndRelease();
    delete (StringFactory*)stringFactory;
}

void ScriptManager::messageCallback(const asSMessageInfo* msg, void* param) {
    //TODO: implement
}

asIScriptEngine* ScriptManager::getAngelScriptEngine() const {
    return engine;
}

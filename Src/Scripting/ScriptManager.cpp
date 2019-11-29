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

static void AssignStringGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* a = static_cast<StringPoolEntry*>(gen->GetArgObject(0));
    StringPoolEntry* self = static_cast<StringPoolEntry*>(gen->GetObject());
    self->str = a->str;
    gen->SetReturnAddress(self);
}

static void AddAssignStringGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* a = static_cast<StringPoolEntry*>(gen->GetArgObject(0));
    StringPoolEntry* self = static_cast<StringPoolEntry*>(gen->GetObject());
    self->str = PGE::String(self->str, a->str);
    gen->SetReturnAddress(self);
}

static void StringEqualsGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* a = static_cast<StringPoolEntry*>(gen->GetObject());
    StringPoolEntry* b = static_cast<StringPoolEntry*>(gen->GetArgAddress(0));
    *(bool*)gen->GetAddressOfReturnLocation() = ((*a).str.equals((*b).str));
}

static void StringAddGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* a = static_cast<StringPoolEntry*>(gen->GetObject());
    StringPoolEntry* b = static_cast<StringPoolEntry*>(gen->GetArgAddress(0));
    StringPoolEntry retVal;
    retVal.str = a->str+b->str;
    gen->SetReturnObject(&retVal);
}

static void StringLengthGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* self = static_cast<StringPoolEntry*>(gen->GetObject());
    *static_cast<asUINT*>(gen->GetAddressOfReturnLocation()) = (asUINT)self->str.size();
}

static void StringIsEmptyGeneric(asIScriptGeneric* gen) {
    StringPoolEntry* self = reinterpret_cast<StringPoolEntry*>(gen->GetObject());
    *reinterpret_cast<bool*>(gen->GetAddressOfReturnLocation()) = self->str.size()==0;
}

static void StringSubStringStartLenGeneric(asIScriptGeneric *gen) {
    // Get the arguments
    StringPoolEntry* str = (StringPoolEntry*)gen->GetObject();
    asUINT start = *(int*)gen->GetAddressOfArg(0);
    int count = *(int*)gen->GetAddressOfArg(1);

    // Return the substring
    new(gen->GetAddressOfReturnLocation()) StringPoolEntry(str->str.substr(start,count));
}

static void StringCharAtGeneric(asIScriptGeneric* gen) {
    unsigned int index = gen->GetArgDWord(0);
    StringPoolEntry* self = static_cast<StringPoolEntry*>(gen->GetObject());

    if (index < 0 || index >= self->str.size()) {
        // Set a script exception
        asIScriptContext *ctx = asGetActiveContext();
        ctx->SetException("Out of range");

        gen->SetReturnAddress(0);
    } else {
        *static_cast<char*>(gen->GetAddressOfReturnLocation()) = self->str.charAt(index);
    }
}

ScriptManager::ScriptManager() {
    engine = asCreateScriptEngine();

    engine->SetMessageCallback(asMETHOD(ScriptManager, messageCallback), this, asCALL_THISCALL);

    stringFactory = new StringFactory();

    engine->RegisterObjectType("string", sizeof(StringPoolEntry), asOBJ_VALUE | asOBJ_APP_CLASS_CDAK);

    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,"void f()",asFUNCTION(ConstructStringGeneric), asCALL_GENERIC);
    engine->RegisterObjectBehaviour("string", asBEHAVE_CONSTRUCT,"void f(const string &in)",asFUNCTION(CopyConstructStringGeneric),asCALL_GENERIC);
    engine->RegisterObjectBehaviour("string",asBEHAVE_DESTRUCT,"void f()",asFUNCTION(DestructStringGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","string &opAssign(const string &in)",asFUNCTION(AssignStringGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","string &opAddAssign(const string &in)",asFUNCTION(AddAssignStringGeneric),asCALL_GENERIC);

    engine->RegisterObjectMethod("string","bool opEquals(const string &in) const",asFUNCTION(StringEqualsGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","string opAdd(const string &in) const",asFUNCTION(StringAddGeneric),asCALL_GENERIC);
    
    engine->RegisterObjectMethod("string","uint length() const",asFUNCTION(StringLengthGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","bool isEmpty() const",asFUNCTION(StringIsEmptyGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","string substr(int start, int end=-1) const",asFUNCTION(StringSubStringStartLenGeneric),asCALL_GENERIC);

    engine->RegisterObjectMethod("string","uint8 &opIndex(uint)",asFUNCTION(StringCharAtGeneric),asCALL_GENERIC);
    engine->RegisterObjectMethod("string","const uint8 &opIndex(uint) const",asFUNCTION(StringCharAtGeneric),asCALL_GENERIC);
    
    engine->RegisterStringFactory("string", stringFactory);
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

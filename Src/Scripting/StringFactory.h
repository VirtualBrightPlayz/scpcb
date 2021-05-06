#ifndef STRINGFACTORY_H_INCLUDED
#define STRINGFACTORY_H_INCLUDED

#include <unordered_map>

#include <angelscript.h>

#include <String/Key.h>

struct StringPoolEntry {
    PGE::String str;
    int refCount;

    StringPoolEntry() { refCount = 0; }

    StringPoolEntry(const PGE::String& s) {
        str = s;
        refCount = 0;
    }
};

class StringFactory : public asIStringFactory {
    private:
        // String key safe here?
        std::unordered_map<PGE::String::Key, StringPoolEntry> strPool;

    public:
        StringFactory(asIScriptEngine* engine);
        const void* GetStringConstant(const char* data, asUINT length);
        int ReleaseStringConstant(const void* str);
        int GetRawStringData(const void* str, char* data, asUINT* length) const;
};

#endif // STRINGFACTORY_H_INCLUDED

#ifndef STRING_H_INCLUDED
#define STRING_H_INCLUDED

#include <string>

#ifdef __APPLE__
#include <Carbon/Carbon.h>
#include <CoreFoundation/CFBundle.h>
#include <CoreFoundation/CFString.h>
#endif

typedef wchar_t wchar;

struct String {
    public:
        ~String();
        String();
        String(const String& a);
        String(const char* cstr);
        String(const std::string& cppstr);
        String(const wchar* wstr);
        String(const std::wstring& cppwstr);
        String(const String& a,const String& b);
        String(char c);
        String(wchar w);
        String(int i);
        String(float f);

        const char* cstr() const;
        const wchar* wstr() const;
        int toInt() const;
        float toFloat() const;

        int size() const;

        int findLast(const String& fnd,int from=-1) const;

        String substr(int start,int cnt=-1) const;
        String replace(const String& fnd,const String& rplace) const;
        String toLower() const;
        String unHex() const;
        String resourcePath() const;

        String& operator=(const String& other);
        static String operator+(const String& a,const String& b);

        bool equals(const String& other) const;
    protected:
        enum class DOMINANT_BUFFER {
            C, W
        } dominantBuffer;

        char* cbuffer = nullptr;
        wchar* wbuffer = nullptr;
        int capacity = 16;
        int strSize = 0;

        void syncBuffers();
};

#endif

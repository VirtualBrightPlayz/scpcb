/**
Copyright (c) 2018 Juan Pablo Arce
This software is provided 'as-is', without any express or implied
warranty. In no event will the authors be held liable for any damages
arising from the use of this software.
Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
**/

#ifndef STRING_H_INCLUDED
#define STRING_H_INCLUDED

#include <string>
#include <iostream>
#include <vector>

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

        int findFirst(const String& fnd,int from=-1) const;
        int findLast(const String& fnd,int from=-1) const;

        String substr(int start,int cnt=-1) const;
        char charAt(int pos) const;
        String replace(const String& fnd,const String& rplace) const;
        String toUpper() const;
        String toLower() const;
        String trim() const;

        String& operator=(const String& other);

        bool equals(const String& other) const;
        bool equals(const char* other) const;
        bool isEmpty() const;

        std::vector<String> split(const String& needle, bool removeEmptyEntries = false) const;
        static String join(const std::vector<String>& vect, const String& separator = "");
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

String operator+(const String& a, const String& b);
String operator+(const char* a, const String& b);
bool operator<(const String& a, const String& b);
bool operator>(const String& a, const String& b);
std::ostream& operator<<(std::ostream &os, const String& s);

#endif

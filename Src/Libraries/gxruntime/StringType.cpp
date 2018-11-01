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

#include "StringType.h"

#include <cstdio>
#include <cstring>
#include <cwchar>
#include <cstdlib>
#include <cwctype>

String::~String() {
    if (cbuffer != nullptr) { delete[] cbuffer; }
    if (wbuffer != nullptr) { delete[] wbuffer; }
}

String::String() {
    cbuffer = new char[24];
    capacity = 24;
    cbuffer[0] = '\0';
    strSize = 0;
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String::String(const String& a) {
    int len = a.size();
    wbuffer = new wchar[len + 1];
    capacity = len + 1;
    wcscpy(wbuffer, a.wstr());
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::W;
    syncBuffers();
}

String::String(const char* cstr) {
    int len = strlen(cstr);
    cbuffer = new char[len + 1];
    capacity = len + 1;
    strcpy(cbuffer, cstr);
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String::String(const std::string& cppstr) {
    int len = cppstr.size();
    cbuffer = new char[len + 1];
    capacity = len + 1;
    strcpy(cbuffer, cppstr.c_str());
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String::String(const wchar* wstr) {
    int len = wcslen(wstr);
    wbuffer = new wchar[len + 1];
    capacity = len + 1;
    wcscpy(wbuffer, wstr);
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::W;
    syncBuffers();
}

String::String(const std::wstring& cppwstr) {
    int len = cppwstr.size();
    wbuffer = new wchar[len + 1];
    capacity = len + 1;
    wcscpy(wbuffer, cppwstr.c_str());
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::W;
    syncBuffers();
}

String::String(const String& a, const String& b) {
    int len = a.size() + b.size();
    wbuffer = new wchar[len + 1];
    capacity = len + 1;
    wcscpy(wbuffer, a.wstr());
    wcscpy(wbuffer + a.size(), b.wstr());
    strSize = len;
    dominantBuffer = DOMINANT_BUFFER::W;
    syncBuffers();
}

String::String(char c) {
    cbuffer = new char[2];
    capacity = 2;
    cbuffer[0] = c; cbuffer[1] = '\0';
    strSize = 1;
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String::String(wchar w) {
    wbuffer = new wchar[2];
    capacity = 2;
    wbuffer[0] = w; wbuffer[1] = L'\0';
    strSize = 1;
    dominantBuffer = DOMINANT_BUFFER::W;
    syncBuffers();
}

String::String(int i) {
    cbuffer = new char[32];
    capacity = 32;
    sprintf(cbuffer, "%d", i);
    strSize = strlen(cbuffer);
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String::String(float f) {
    cbuffer = new char[32];
    capacity = 32;
    sprintf(cbuffer, "%f", f);
    strSize = strlen(cbuffer);
    dominantBuffer = DOMINANT_BUFFER::C;
    syncBuffers();
}

String& String::operator=(const String& other) {
    if (equals(other)) { return *this; }

    if (wbuffer != nullptr) { delete[] wbuffer; }
    if (cbuffer != nullptr) { delete[] cbuffer; }
    wbuffer = new wchar[other.capacity];
    cbuffer = new char[other.capacity];
    wcscpy(wbuffer, other.wstr());
    strcpy(cbuffer, other.cstr());
    capacity = other.capacity;
    strSize = other.size();
    return *this;
}

String operator+(const String& a, const String& b) {
    return String(a, b);
}

String operator+(const char* a, const String& b) {
    return String(String(a), b);
}

bool operator<(const String& a, const String& b) {
    return strcmp(a.cstr(), b.cstr()) < 0;
}

bool operator>(const String& a, const String& b) {
    return strcmp(a.cstr(), b.cstr()) > 0;
}

std::ostream& operator<<(std::ostream& os, const String& s) {
	return os << s.cstr();
}

bool String::equals(const String& other) const {
    return strcmp(cbuffer, other.cstr()) == 0;
}

bool String::equals(const char* other) const {
    return strcmp(cbuffer,other) == 0;
}

bool String::isEmpty() const {
    return strSize == 0;
}

void String::syncBuffers() {
    //TODO: do UTF-8<->WCHAR conversion
    if (dominantBuffer == DOMINANT_BUFFER::C) {
        if (wbuffer != nullptr) { delete[] wbuffer; }
        wbuffer = new wchar[capacity];
        for (int i = 0; i<size() + 1; i++) {
            wbuffer[i] = cbuffer[i];
        }
    }
    else if (dominantBuffer == DOMINANT_BUFFER::W) {
        if (cbuffer != nullptr) { delete[] cbuffer; }
        cbuffer = new char[capacity];
        for (int i = 0; i<size() + 1; i++) {
            cbuffer[i] = (char)wbuffer[i];
        }
    }
}

int String::size() const {
    return strSize;
}

int String::findFirst(const String& fnd, int from) const {
    if (fnd.size() == 0) { return -1; }
    if (from<0) { from = 0; }
    for (int i = from; i <= size()-fnd.size(); i++) {
        if (memcmp(fnd.wstr(), wstr() + i, fnd.size() * sizeof(wchar)) == 0) { return i; }
    }
    return -1;
}

int String::findLast(const String& fnd, int from) const {
    if (fnd.size() == 0) { return -1; }
    if (from<0) { from = size(); }
    for (int i = from - fnd.size(); i >= 0; i--) {
        if (memcmp(fnd.wstr(), wstr() + i, fnd.size() * sizeof(wchar)) == 0) { return i; }
    }
    return -1;
}

const char* String::cstr() const {
    return cbuffer;
}

const wchar* String::wstr() const {
    return wbuffer;
}

int String::toInt() const {
    return atoi(cbuffer);
}

float String::toFloat() const {
    return (float)atof(cbuffer);
}

String String::substr(int start, int cnt) const {
    if (cnt<0) {
        cnt = size() - start;
    }

    wchar* newBuf = new wchar[cnt + 1];
    newBuf[cnt] = L'\0';
    memcpy(newBuf, wstr() + start, cnt * sizeof(wchar));

    String retVal(newBuf);
    delete[] newBuf;
    return retVal;
}

char String::charAt(int pos) const {
    return (char)(wstr()[pos]);
}

String String::replace(const String& fnd, const String& rplace) const {
    if (fnd.size() == 0) { return *this; }

    int newSize = strSize;
    for (int i = 0; i<strSize - fnd.size() + 1;) {
        if (memcmp(fnd.wstr(), wstr() + i, fnd.size() * sizeof(wchar)) == 0) {
            newSize += rplace.size() - fnd.size();
            i += fnd.size();
        } else {
            i++;
        }
    }

    wchar* newBuf = new wchar[newSize + 1];
    int i = 0; int j = 0;
    while (i<strSize) {
        bool found = i<strSize - fnd.size() + 1;
        if (found) {
            found = memcmp(fnd.wstr(), wstr() + i, fnd.size() * sizeof(wchar)) == 0;
        }
        if (found) {
            memcpy(newBuf + j, rplace.wstr(), rplace.size() * sizeof(wchar));
            i += fnd.size(); j += rplace.size();
        }
        else {
            newBuf[j] = wstr()[i];
            i++; j++;
        }
    }
    newBuf[j] = L'\0';

    String retVal(newBuf);
    delete[] newBuf;
    return retVal;
}

String String::toUpper() const {
    wchar* newBuf = new wchar[capacity];
    for (int i = 0; i<strSize; i++) {
        newBuf[i] = towupper(wbuffer[i]);
    }
    newBuf[strSize] = L'\0';

    String retVal(newBuf);
    delete[] newBuf;
    return retVal;
}

String String::toLower() const {
    wchar* newBuf = new wchar[capacity];
    for (int i = 0; i<strSize; i++) {
        newBuf[i] = towlower(wbuffer[i]);
    }
    newBuf[strSize] = L'\0';

    String retVal(newBuf);
    delete[] newBuf;
    return retVal;
}

String String::trim() const {
    if (size()==0) { return ""; }

    wchar* newBuf = new wchar[capacity];
    int leadingPos = 0;
    while (charAt(leadingPos) == ' ' || charAt(leadingPos) == '\t') {
        leadingPos++;
        if (leadingPos>=size()) {
            return *this;
        }
    }
    int trailingPos = size()-1;
    while (charAt(trailingPos) == ' ' || charAt(trailingPos) == '\t') {
        trailingPos--;
        if (trailingPos<0) {
            return *this;
        }
    }

    int newLength = 1;
    newBuf[0] = charAt(leadingPos);
    for (int i=leadingPos+1;i<=trailingPos;i++) {
        if (charAt(i-1)!=' ' || charAt(i)!=' ') {
            newBuf[newLength]=charAt(i);
            newLength++;
        }
    }
    newBuf[newLength]=L'\0';

    String retVal = newBuf;
    delete[] newBuf;
    return retVal;
}

std::vector<String> String::split(const String& needle, bool removeEmptyEntries) const {
	std::vector<String> retVal;
	String haystack = String(*this);
	while (haystack.findFirst(needle) > -1) {
		String adder = haystack.substr(0, haystack.findFirst(needle));
		retVal.push_back(adder);
		haystack = haystack.substr(adder.size()+needle.size());
	}
	// Add the rest of the string to the vector.
	retVal.push_back(haystack);

    if (removeEmptyEntries) {
        for (int i = 0; i < retVal.size(); i++) {
            if (retVal[i].isEmpty()) {
                retVal.erase(retVal.begin() + i);
                i--;
            }
        }
    }

	return retVal;
}

String String::join(const std::vector<String>& vect, const String& separator) {
    if (vect.size() <= 0) {
        return String("");
    }

    String retVal = vect[0];
    for (int i = 1; i < vect.size(); i++) {
        retVal = retVal + separator + vect[i];
    }

    return retVal;
}

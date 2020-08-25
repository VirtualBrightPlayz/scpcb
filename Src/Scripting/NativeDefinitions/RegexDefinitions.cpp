#include "RegexDefinitions.h"

#include <string>

#include "../ScriptManager.h"

static bool matchRegexAny(std::regex* regex, const PGE::String& testStr) {
    return std::regex_match(testStr.cstr(), *regex, std::regex_constants::match_any);
}

static int matchRegexPosition(std::regex* regex, const PGE::String& testStr) {
    std::cmatch matches = testStr.regexMatch(*regex);
    return (int)(matches[1].matched ? matches.position(1) : matches.position(2));
}

RegexDefinitions::RegexDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr) {
    engine = mgr->getAngelScriptEngine();

    refCounterManager = rcMgr;

    engine->RegisterObjectType("Regex", sizeof(std::regex), asOBJ_REF);
    
    engine->RegisterObjectBehaviour("Regex", asBEHAVE_FACTORY, "Regex@ f(const string&in str)", asMETHOD(RegexDefinitions, regexFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("Regex", asBEHAVE_ADDREF, "void f()", asMETHOD(RegexDefinitions, addRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("Regex", asBEHAVE_RELEASE, "void f()", asMETHOD(RegexDefinitions, release), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterObjectMethod("Regex", "bool matchAny(const string&in testStr) const", asFUNCTION(matchRegexAny), asCALL_CDECL_OBJFIRST);
    engine->RegisterObjectMethod("Regex", "int matchPosition(const string&in testStr) const", asFUNCTION(matchRegexPosition), asCALL_CDECL_OBJFIRST);
}

std::regex* RegexDefinitions::regexFactory(const PGE::String& str) {
    std::regex* regex = new std::regex(str.cstr());
    refCount.emplace(regex, 1);
    refCounterManager->linkPtrToCounter(regex, this);
    return regex;
}

void RegexDefinitions::addRef(void* ptr) {
    std::regex* pickable = (std::regex*)ptr;
    if (refCount.find(pickable) == refCount.end()) { return; }
    refCount[pickable]++;
}

void RegexDefinitions::release(void* ptr) {
    std::regex* regex = (std::regex*)ptr;
    if (refCount.find(regex) == refCount.end()) { return; }
    refCount[regex]--;

    if (refCount[regex] <= 0) {
        refCount.erase(regex);
        refCounterManager->unlinkPtr(regex);
        delete regex;
    }
}

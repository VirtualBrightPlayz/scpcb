#ifndef TEXTMGMT_H_INCLUDED
#define TEXTMGMT_H_INCLUDED

#include <Misc/String.h>
#include <map>

#include "INI.h"

class Language {
private:
    PGE::String name;
    std::map<PGE::String, PGE::String> whyyy;

public:
    Language(std::vector<INIFile::Section*> iniFile);

    PGE::String getText(const PGE::String& code) const;
};

// TODO: Localization stuff here. Also DeathMSG.
class TxtManager {
private:
    Language* currentLanguage;

    PGE::String displayMsg;
    float displayTimer;
    PGE::String deathMsg;

public:
    TxtManager(const PGE::String& langCode);

    void setMsg(const PGE::String& local, float seconds = 5.f);

    void setDeathMsg(const PGE::String& local);
    PGE::String getDeathMsg();
    void updateMsg(float timestep);
    void drawMsg();

    PGE::String getLocalTxt(const PGE::String& key) const;
    void changeLocalization(const PGE::String& langCode);
};

#endif // TEXTMGMT_H_INCLUDED

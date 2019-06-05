#ifndef TEXTMGMT_H_INCLUDED
#define TEXTMGMT_H_INCLUDED

#include <Misc/String.h>
#include <map>

namespace CBN {

// TODO: Localization stuff here. Also DeathMSG.
class TxtManager {
private:
    PGE::String displayMsg;
    float displayTimer;
    PGE::String deathMsg;

    std::map<PGE::String, PGE::String> lang;

public:
    TxtManager(const PGE::String& langCode);

    void setMsg(const PGE::String& local, float time = 5.f);

    void setDeathMsg(const PGE::String& local);
    PGE::String getDeathMsg();
    void updateMsg();
    void drawMsg();

    PGE::String getLocalTxt(const PGE::String& key);
    void changeLocalization(const PGE::String& langCode);
};
extern TxtManager* txtMgmt;

}
#endif // TEXTMGMT_H_INCLUDED

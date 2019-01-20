#ifndef TEXTMGMT_H_INCLUDED
#define TEXTMGMT_H_INCLUDED

#include <StringType.h>
#include <map>

namespace CBN {

// TODO: Localization stuff here. Also DeathMSG.
class TxtManager {
private:
    String displayMsg;
    float displayTimer;
    String deathMsg;

    std::map<String, String> lang;

public:
    TxtManager(const String& langCode);

    void setMsg(const String& local, float time = 5.f);

    void setDeathMsg(const String& local);
    String getDeathMsg();
    void updateMsg();
    void drawMsg();

    String getLocalTxt(const String& key);
    void changeLocalization(const String& langCode);
};
extern TxtManager* txtMgmt;

}
#endif // TEXTMGMT_H_INCLUDED

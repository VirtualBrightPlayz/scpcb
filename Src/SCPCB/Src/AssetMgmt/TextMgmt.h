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

public:
    TxtManager(const String& langCode);

    void setMsg(const String& msg, float time = 70 * 5);
    void setDeathMsg(const String& msg);
    String getDeathMsg();
    void updateMsg();
    void drawMsg();

    std::map<String, String> lang;
    void changeLocalization(const String& langCode);
};
extern TxtManager* txtMgmt;

}
#endif // TEXTMGMT_H_INCLUDED

#ifndef TEXTMGMT_H_INCLUDED
#define TEXTMGMT_H_INCLUDED

#include <StringType.h>

namespace CBN {

// TODO: Localization stuff here. Also DeathMSG,
class TxtManager {
private:
    String displayMsg;
    float displayTimer;
    String deathMsg;

public:
    TxtManager();
    ~TxtManager();

    void setMsg(const String& msg, float time = 70 * 5);
    void setDeathMsg(const String& msg);
    String getDeathMsg();
    void update();
};
extern TxtManager* txtManager;

}
#endif // TEXTMGMT_H_INCLUDED

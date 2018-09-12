#ifndef FIRSTAID_H_INCLUDE
#define FIRSTAID_H_INCLUDE

#include "Item.h"

namespace CBN {

class FirstAid : public Item {
private:
    String type;
    FirstAid();

    const float DEFAULT_TIME = 10 * 70;
    float timer = DEFAULT_TIME;

    void updateUse();
    void drawUse();
public:
    static FirstAid* spawn(float x, float y, float z);

    String getType();
    String getInvName();
};

}

#endif // FIRSTAID_H_INCLUDE
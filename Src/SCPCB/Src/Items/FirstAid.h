#ifndef FIRSTAID_H_INCLUDE
#define FIRSTAID_H_INCLUDE

#include "Item.h"

namespace CBN {

class FirstAid : public Item {
private:
    FirstAid();

    // Stores the player's crouch state prior to using the first aid.
    bool prevCrouch;

    const float DEFAULT_TIME = 10 * 70;
    float timer;

public:
    static FirstAid* spawn(float x, float y, float z);

    String getType() override;
    String getInvName() override;

    void onUse() override;
    void updateUse() override;
    void drawUse() override;
};

}

#endif // FIRSTAID_H_INCLUDE
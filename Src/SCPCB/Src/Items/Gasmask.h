#ifndef GASMASK_H_INCLUDED
#define GASMASK_H_INCLUDED
#include "Item.h"

namespace CBN {

class GasMask : public Item {
private:
    String type;
    GasMask();
public:
    GasMask(float x, float y, float z);

    String getType();
    String getInvName();

    void onPick();
    void onUse();
};

}
#endif // GASMASK_H_INCLUDED

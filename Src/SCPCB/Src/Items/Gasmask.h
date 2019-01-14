#ifndef GASMASK_H_INCLUDED
#define GASMASK_H_INCLUDED
#include "Item.h"

namespace CBN {

class GasMask : public Item {
private:
    GasMask();

public:
    static GasMask* spawn(float x, float y, float z);

    String getType() override;
    String getInvName() override;

    void onPick() override;
    void onUse() override;
};

}
#endif // GASMASK_H_INCLUDED

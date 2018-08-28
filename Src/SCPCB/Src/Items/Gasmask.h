#ifndef GASMASK_H_INCLUDED
#define GASMASK_H_INCLUDED
#include "Item.h"

namespace CBN {

class GasMask : public Item {
private:
    String type;
public:
    GasMask();
    virtual String getType();
    virtual String getInvName();

    virtual void onPick();
    virtual void onUse();
};

}
#endif // GASMASK_H_INCLUDED

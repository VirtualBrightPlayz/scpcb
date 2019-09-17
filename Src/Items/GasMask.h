#ifndef GASMASK_H_INCLUDED
#define GASMASK_H_INCLUDED

#include "Item.h"

class GasMask : public Item {
private:
    GasMask(GraphicsResources* gfx, TxtManager* tm);
    
public:
    GasMask* spawn(GraphicsResources* gfx, TxtManager* tm, float x, float y, float z);
    
    virtual PGE::String getType() const override;
    virtual PGE::String getInvName() const override;
};

#endif // GASMASK_H_INCLUDED

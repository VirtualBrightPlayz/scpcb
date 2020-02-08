#include <Misc/FileName.h>

#include "GasMask.h"
#include "../Utils/TextMgmt.h"

GasMask::GasMask(GraphicsResources* gfx, TxtManager* tm)
: Item(gfx, tm, PGE::FileName::create("GFX/Items/gasmask/gasmask.b3d"), 6.f, ItemPickSound::Heafty) { }

GasMask* GasMask::spawn(GraphicsResources* gfx, TxtManager* tm, float x, float y, float z) {
    GasMask* it = new GasMask(gfx, tm);
    it->setPosition(x, y, z);
    return it;
}

PGE::String GasMask::getType() const {
    return "gasmask";
}

PGE::String GasMask::getInvName() const {
    return txtM->getLocalTxt("Items.GasMask.Name");
}

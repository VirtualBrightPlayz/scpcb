#include "Item.h"

#include "../Graphics/GraphicsResources.h"
#include "../Models/Model.h"
#include "../Menus/Inventory.h"

/*void Item::onPicked() {
    if (inventory->addItem(this)) {
        picked = true;
        pickMan->deactivatePickable(this);
    }
}

Item::Item(const PGE::String& filename, const PGE::String& icon, int x, GraphicsResources* gr, PickableManager* pm, Inventory* inv)
: Pickable(PickType::Grab) {
    gfxRes = gr;
    model = gfxRes->getModelInstance(filename);
    model->setPosition(PGE::Vector3f(x, 0.f, 0.f));
    pickMan = pm;
    pickMan->activatePickable(this);

    invIcon = gr->getTexture(icon);

    inventory = inv;

    picked = false;
}

Item::~Item() {
    gfxRes->dropModelInstance(model);
    gfxRes->dropTexture(invIcon);
    pickMan->deactivatePickable(this);
}

void Item::render() const {
    if (!picked) {
        model->render();
    }
}

PGE::Texture* Item::getInvIcon() const {
    return invIcon;
}
*/
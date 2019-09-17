#include "Item.h"
#include "../Graphics/GraphicsResources.h"
#include "../Graphics/Camera.h"
#include "../Utils/MathUtil.h"

Item::Item(GraphicsResources* gfx, TxtManager* tm, const PGE::FileName& path, float scale, ItemPickSound ips) {
    dropSpeed = 0.f;
    inventoryIcon = nullptr;
    markedForRemoval = false;
    needsInventoryIcon = false;
    
    gfxRes = gfx;
    txtM = tm;
    
    this->scale = scale;
}

Item::~Item() {
    if (inventoryIcon != nullptr) {
        delete inventoryIcon;
    }
}

void Item::setPosition(float x, float y, float z) {
    position = PGE::Vector3f(x, y, z);
}

void Item::generateInventoryIcon() {
    if (!needsInventoryIcon) { return; }
    
    Camera* tempCam = new Camera(gfxRes, 1, MathUtil::degToRad(70.f));
    
    tempCam->update();
    
    needsInventoryIcon = false;
}

void Item::onPick() { }
void Item::onUse() { }

void Item::update() {
    updateInternal();
}

void Item::updateInternal() { }

void Item::render() const {
    // TODO:
}

#include <bbblitz3d.h>
#include <bbgraphics.h>

#include "Item.h"

namespace CBN {

Item::Item(const String& meshPath, ItemPickSound sound, WornItemSlot slot) : meshPath(meshPath) {
    list.push_back(this);

    mesh = bbLoadMesh(meshPath);
    pickSound = sound;
    wornSlot = slot;
    wornOnly = false;
    needInvImg = true;
    invImg = nullptr;
    scale = 0.f;
    dist = 0.f;
    picked = false;
    dropped = true;

    collider = bbCreatePivot();
    wontColl = false;
    xSpeed = 0.f;
    zSpeed = 0.f;
    dropSpeed = 0.f;
}

Item::~Item() {
    bbFreeEntity(mesh);
    if (invImg != nullptr) {
        bbFreeImage(invImg);
    }
    bbFreeEntity(collider);

    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}

void Item::assignTag(const String& tag) {
    if (hasTag(tag)) { return; }
    tags.push_back(tag);
}

bool Item::hasTag(const String& tag) {
    return std::find(tags.begin(), tags.end(), tag) != tags.end();
}

void Item::removeTag(const String& tag) {
    bool found = false;
    for (int i = 0; i < tags.size(); i++) {
        if (tags[i].equals(tag)) {
            found = true;
            tags.erase(tags.begin() + i);
            return;
        }
    }

    if (!found) {
        throw ("Removed non-existant tag: " + getType() + ", tag: " + tag);
    }
}

}
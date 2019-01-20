#include <StringType.h>
#include <bbblitz3d.h>
#include <bbmath.h>
#include <string.h>

#include "Decals.h"
#include "../AssetMgmt/Assets.h"
#include "../GameMain.h"
#include "../MathUtils/MathUtils.h"

namespace CBN {

// Structs.
std::vector<Decal*> Decal::list;
Decal::Decal() {
    memset(this, 0, sizeof(Decal));
    list.push_back(this);
}
Decal::~Decal() {
    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Decal::getListSize() {
    return (int)list.size();
}
Decal* Decal::getObject(int index) {
    return list[index];
}

// Globals.
String decalList[DECAL_COUNT];

// Functions.
void LoadDecals() {
    decalList[DECAL_CORROSION] = "corrosion.png";
    decalList[DECAL_CRACKS] = "cracks.png";
    decalList[DECAL_BLOOD_SPREAD] = "blood_spread.png";
    decalList[DECAL_BLOOD_SPLATTER] = "blood_splatter.png";
    decalList[DECAL_BLOOD_POOL] = "blood_pool.png";
    decalList[DECAL_PD1] = "decal_pd1.jpg";
    decalList[DECAL_PD2] = "decal_pd2.jpg";
    decalList[DECAL_PD3] = "decal_pd3.jpg";
    decalList[DECAL_PD4] = "decal_pd4.jpg";
    decalList[DECAL_PD5] = "decal_pd5.jpg";
    decalList[DECAL_PD6] = "decal_pd6.jpg";
    decalList[DECAL_BULLET_HOLE1] = "bullet_hole1.jpg";
    decalList[DECAL_BULLET_HOLE2] = "bullet_hole2.jpg";
    decalList[DECAL_BLOOD_DROP1] = "blood_drop1.png";
    decalList[DECAL_BLOOD_DROP2] = "blood_drop2.png";
}

Decal* CreateDecal(int id, float x, float y, float z, float pitch, float yaw, float roll) {
    TextureAssetWrap* tex = TextureAssetWrap::grab("GFX/Decals/" + decalList[id], 1+2);
    Decal* d = new Decal();

    d->x = x;
    d->y = y;
    d->z = z;
    d->pitch = pitch;
    d->yaw = yaw;
    d->roll = roll;

    d->maxSize = 1.f;

    d->alpha = 1.f;
    d->size = 1.f;
    d->obj = bbCreateSprite();
    d->blendmode = 1;

    bbEntityTexture(d->obj, tex->getTexture());
    bbEntityFX(d->obj, 0);
    bbSpriteViewMode(d->obj, 2);
    bbPositionEntity(d->obj, x, y, z);
    bbRotateEntity(d->obj, pitch, yaw, roll);
    tex->drop();

    d->id = id;

    if (tex == nullptr || d->obj == nullptr) {
        return nullptr;
    }

    return d;
}

void UpdateDecals() {
    for (int i = 0; i < Decal::getListSize(); i++) {
        Decal* d = Decal::getObject(i);

        // FIXME
        if (d->sizeChange != 0) {
            d->size = d->size + d->sizeChange * timing->tickDuration;
            bbScaleSprite(d->obj, d->size, d->size);

            switch (d->id) {
                case DECAL_CORROSION: {
                    if (d->timer <= 0) {
                        float angle = bbRnd(360);
                        float temp = bbRnd(d->size);
                        Decal* d2 = CreateDecal(DECAL_CRACKS, bbEntityX(d->obj) + bbCos(angle) * temp, bbEntityY(d->obj) - 0.0005f, bbEntityZ(d->obj) + bbSin(angle) * temp, bbEntityPitch(d->obj), bbRnd(360), bbEntityRoll(d->obj));
                        d2->size = bbRnd(0.1f, 0.5f);
                        bbScaleSprite(d2->obj, d2->size, d2->size);
                        //TODO: fix, and not attach this to the decal
                        //PlayRangedSound(DecaySFX(Rand(1, 3)), mainPlayer\cam, d2\obj, 10.f, Rnd(0.1f, 0.5f))
                        //d\timer = d\timer + Rand(50,150)
                        d->timer = (float)bbRand(50, 100);
                    } else {
                        d->timer = d->timer-timing->tickDuration;
                    }
                    //Case 6
                    //	EntityBlend(d\obj, 2)
                } break;
            }

            if (d->size >= d->maxSize) {
                d->sizeChange = 0;
                d->size = d->maxSize;
            }
        }

        // FIXME
        if (d->alphaChange != 0) {
            d->alpha = Min(d->alpha + timing->tickDuration * d->alphaChange, 1.f);
            bbEntityAlpha(d->obj, d->alpha);
        }

        if (d->lifetime > 0) {
            d->lifetime = Max(d->lifetime-timing->tickDuration,5);
        }

        // FIXME
        if (d->size <= 0 || d->alpha <= 0 || d->lifetime==5.f ) {
            bbFreeEntity(d->obj);
            delete d;
        }
    }
}

}

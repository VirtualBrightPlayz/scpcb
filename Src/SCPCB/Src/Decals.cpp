#include "Decals.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Decal*> Decal::list;
Decal::Decal() {
    list.push_back(this);
}
Decal::~Decal() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Decal::getListSize() {
    return list.size();
}
Decal* Decal::getObject(int index) {
    return list[index];
}

// Constants.
const int DECAL_COUNT = 15;
const int DECAL_CORROSION = 0;
const int DECAL_CRACKS = 1;
const int DECAL_BLOOD_SPREAD = 2;
const int DECAL_BLOOD_SPLATTER = 3;
const int DECAL_BLOOD_POOL = 4;
const int DECAL_PD1 = 5;
const int DECAL_PD2 = 6;
const int DECAL_PD3 = 7;
const int DECAL_PD4 = 8;
const int DECAL_PD5 = 9;
const int DECAL_PD6 = 10;
const int DECAL_BULLET_HOLE1 = 11;
const int DECAL_BULLET_HOLE2 = 12;
const int DECAL_BLOOD_DROP1 = 13;
const int DECAL_BLOOD_DROP2 = 14;

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
    int tex = GrabTexture("GFX/Decals/" + decalList[id], 1+2);
    Decal* d = new Decal();

    d->x = x;
    d->y = y;
    d->z = z;
    d->pitch = pitch;
    d->yaw = yaw;
    d->roll = roll;

    d->maxSize = 1.0;

    d->alpha = 1.0;
    d->size = 1.0;
    d->obj = bbCreateSprite();
    d->blendmode = 1;

    bbEntityTexture(d->obj, tex);
    bbEntityFX(d->obj, 0);
    bbSpriteViewMode(d->obj, 2);
    bbPositionEntity(d->obj, x, y, z);
    bbRotateEntity(d->obj, pitch, yaw, roll);
    DropAsset(tex);

    d->id = id;

    if (tex == 0 | d->obj == 0) {
        return nullptr;
    }

    return d;
}

void UpdateDecals() {
    float angle;
    float temp;

    Decal* d;
    Decal* d2;
    for (int iterator54 = 0; iterator54 < Decal::getListSize(); iterator54++) {
        d = Decal::getObject(iterator54);

        if (d->sizeChange != 0) {
            d->size = d->size + d->sizeChange * timing->tickDuration;
            bbScaleSprite(d->obj, d->size, d->size);

            switch (d->id) {
                case DECAL_CORROSION: {
                    if (d->timer <= 0) {
                        angle = bbRand(360);
                        temp = bbRnd(d->size);
                        d2 = CreateDecal(DECAL_CRACKS, bbEntityX(d->obj) + bbCos(angle) * temp, bbEntityY(d->obj) - 0.0005, bbEntityZ(d->obj) + bbSin(angle) * temp, bbEntityPitch(d->obj), bbRnd(360), bbEntityRoll(d->obj));
                        d2->size = bbRnd(0.1, 0.5);
                        bbScaleSprite(d2->obj, d2->size, d2->size);
                        //TODO: fix, and not attach this to the decal
                        //PlayRangedSound(DecaySFX(Rand(1, 3)), mainPlayer\cam, d2\obj, 10.0, Rnd(0.1, 0.5))
                        //d\timer = d\timer + Rand(50,150)
                        d->timer = bbRand(50, 100);
                    } else {
                        d->timer = d->timer-timing->tickDuration;
                    }
                    //Case 6
                    //	EntityBlend(d\obj, 2)
                }
            }

            if (d->size >= d->maxSize) {
                d->sizeChange = 0;
                d->size = d->maxSize;
            }
        }

        if (d->alphaChange != 0) {
            d->alpha = Min(d->alpha + timing->tickDuration * d->alphaChange, 1.0);
            bbEntityAlpha(d->obj, d->alpha);
        }

        if (d->lifetime > 0) {
            d->lifetime = Max(d->lifetime-timing->tickDuration,5);
        }

        if (d->size <= 0 | d->alpha <= 0 | d->lifetime==5.0 ) {
            bbFreeEntity(d->obj);
            delete d;
        }
    }
}

}

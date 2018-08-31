#include <bbblitz3d.h>
#include <bbmath.h>
#include <string.h>

#include "Particles.h"
#include "../AssetMgmt/Assets.h"
#include "../GameMain.h"
#include "../MathUtils/MathUtils.h"
#include "../AssetMgmt/Audio.h"
#include "MapSystem.h"
#include "../Player/Player.h"
#include "../Menus/Menu.h"
#include "../Items/Item.h"

namespace CBN {

// Structs.
std::vector<Particle*> Particle::list;
Particle::Particle() {
    memset(this,0,sizeof(Particle));

    list.push_back(this);
}
Particle::~Particle() {
    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Particle::getListSize() {
    return list.size();
}
Particle* Particle::getObject(int index) {
    return list[index];
}

std::vector<Emitter*> Emitter::list;
Emitter::Emitter() {
    memset(this,0,sizeof(Emitter));

    list.push_back(this);
}
Emitter::~Emitter() {
    for (int i = 0; i < (int)list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Emitter::getListSize() {
    return list.size();
}
Emitter* Emitter::getObject(int index) {
    return list[index];
}

// Globals.
String particleList[PARTICLE_COUNT];
int InSmoke;

// Functions.
void LoadParticles() {
    particleList[PARTICLE_DUST] = "Sprites/dust.jpg";
    particleList[PARTICLE_SMOKE_BLACK] = "Sprites/smoke_black.png";
    particleList[PARTICLE_SMOKE_WHITE] = "Sprites/smoke_white.png";
    particleList[PARTICLE_FLASH] = "Sprites/flash.jpg";
    particleList[PARTICLE_SMOKE_WHITE] = "Sprites/smoke_white.png";
    particleList[PARTICLE_SPARK] = "Sprites/spark.jpg";
    particleList[PARTICLE_BLOOD] = "Sprites/blood.png";
    particleList[PARTICLE_SUN] = "Map/Textures/sun.jpg";
    particleList[PARTICLE_HG] = "General/hg.jpg";
}

Particle* CreateParticle(float x, float y, float z, int image, float size, float gravity, int lifetime) {
    TextureAssetWrap* tex = TextureAssetWrap::grab("GFX/" + particleList[image], 1+2);
    Particle* p = new Particle();
    p->lifetime = (float)lifetime;

    p->sprite = bbCreateSprite();
    bbPositionEntity(p->sprite, x, y, z, true);
    bbEntityTexture(p->sprite, tex->getTexture());
    bbRotateEntity(p->sprite, 0, 0, bbRnd(360));
    bbEntityFX(p->sprite, 1 + 8);
    tex->drop();

    bbSpriteViewMode(p->sprite, 3);

    switch (image) {
        case 0:
        case 5:
        case 6: {
            bbEntityBlend(p->sprite, 1);
        } break;
        case 1:
        case 2:
        case 3:
        case 4:
        case 7: {
            bbEntityBlend(p->sprite, BLEND_ADD);
        } break;
    }

    p->pvt = bbCreatePivot();
    bbPositionEntity(p->pvt, x, y, z, true);

    p->image = image;
    p->gravity = gravity * 0.004f;
    p->r = 255;
    p->g = 255;
    p->b = 255;
    p->a = 1.f;
    p->size = size;
    bbScaleSprite(p->sprite, p->size, p->size);
    return p;
}

void UpdateParticles() {
    for (int i = 0; i < Particle::getListSize(); i++) {
        Particle* p = Particle::getObject(i);

        bbMoveEntity(p->pvt, 0, 0, p->speed * timing->tickDuration);
        if (p->gravity != 0) {
            p->yspeed = p->yspeed - p->gravity * timing->tickDuration;
        }
        bbTranslateEntity(p->pvt, 0, p->yspeed * timing->tickDuration, 0, true);

        bbPositionEntity(p->sprite, bbEntityX(p->pvt,true), bbEntityY(p->pvt,true), bbEntityZ(p->pvt,true), true);

        //TurnEntity(p\obj, 0, 0, timing\tickDuration)

        if (p->aChange != 0) {
            p->a = Min(Max(p->a+p->aChange * timing->tickDuration,0.f),1.f);
            bbEntityAlpha(p->sprite, p->a);
        }

        if (p->sizeChange != 0) {
            p->size = p->size+p->sizeChange * timing->tickDuration;
            bbScaleSprite(p->sprite, p->size, p->size);
        }

        bbShowEntity(p->sprite);
        bbEntityAlpha(p->sprite,1.f);

        p->lifetime = p->lifetime-timing->tickDuration;
        if (p->lifetime <= 0 || p->size < 0.00001f || p->a <= 0) {
            RemoveParticle(p);
        }
    }
}

void RemoveParticle(Particle* p) {
    bbFreeEntity(p->sprite);
    bbFreeEntity(p->pvt);
    delete p;
}

void UpdateEmitters() {
    InSmoke = false;
    Particle* p;
    float dist;

    for (int i = 0; i < Emitter::getListSize(); i++) {
        Emitter* e = Emitter::getObject(i);

        if (timing->tickDuration > 0 && (mainPlayer->currRoom == e->room || e->room->dist < 8)) {
            //If (EntityDistance(mainPlayer\cam, e\obj) < 6.f) Then
            p = CreateParticle(bbEntityX(e->obj, true), bbEntityY(e->obj, true), bbEntityZ(e->obj, true), PARTICLE_SMOKE_WHITE, e->size, e->gravity, e->lifeTime);
            p->speed = e->speed;
            bbRotateEntity(p->pvt, bbEntityPitch(e->obj, true), bbEntityYaw(e->obj, true), bbEntityRoll(e->obj, true), true);
            bbTurnEntity(p->pvt, bbRnd(-e->randAngle, e->randAngle), bbRnd(-e->randAngle, e->randAngle), 0);

            bbTurnEntity(p->sprite, 0,0,bbRnd(360));

            p->sizeChange = e->sizeChange;

            p->aChange = e->aChange;

            e->soundCHN = LoopRangedSound_SM(sndMgmt->hiss, e->soundCHN, mainPlayer->cam, e->obj);

            if (InSmoke == false) {
                if (mainPlayer->isEquipped("gasmask")) {
                    dist = Distance(bbEntityX(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true), bbEntityX(e->obj, true), bbEntityZ(e->obj, true));
                    if (dist < 0.8f) {
                        if (abs(bbEntityY(mainPlayer->cam, true)-bbEntityY(e->obj,true))<5.f) {
                            InSmoke = true;
                        }
                    }
                }
            }
            //EndIf
        }
    }

    if (InSmoke) {
        if (mainPlayer->blinkEffect > (70 * 6)) {
            mainPlayer->blurTimer = Max(mainPlayer->blurTimer, (mainPlayer->blinkEffect - (70 * 6)) / (70.f * 24.f));
        }
        if (mainPlayer->blinkEffect > (70 * 24)) {
            //DeathMSG = "Subject D-9341 found dead in [DATA REDACTED]. Cause of death: Suffocation due to decontamination gas.";
            mainPlayer->kill();
        }

        if (!mainPlayer->dead) {
            if (bbRand(150) == 1) {
                //If (Not bbChannelPlaying(CoughCHN)) Then CoughCHN = PlaySound2(mainPlayer\coughSFX[Rand(0, 2)))
            }
        }

        mainPlayer->blinkEffect = mainPlayer->blinkEffect+timing->tickDuration * 4;
    }
}

Emitter* CreateEmitter(float x, float y, float z, int emittertype) {
    Emitter* e = new Emitter();

    e->obj = bbCreatePivot();
    bbPositionEntity(e->obj, x, y, z, true);

    switch (emittertype) {
        case 0: {
            e->size = 0.03f;
            e->gravity = -0.2f;
            e->lifeTime = 200;
            e->sizeChange = 0.005f;
            e->speed = 0.004f;
            e->randAngle = 20;
            e->aChange = -0.008f;
        } break;
        case 1: {
            e->size = 0.03f;
            e->gravity = -0.2f;
            e->lifeTime = 200;
            e->sizeChange = 0.008f;
            e->speed = 0.004f;
            e->randAngle = 40;
            e->aChange = -0.01f;
        } break;
    }

    for (int i = 0; i < Room::getListSize(); i++) {
        Room* r = Room::getObject(i);

        if (abs(bbEntityX(e->obj) - bbEntityX(r->obj)) < 4.f && abs(bbEntityZ(e->obj) - bbEntityZ(r->obj)) < 4.f) {
            e->room = r;
        }
    }

    return e;

}

}

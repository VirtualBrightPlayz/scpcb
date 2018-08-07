#include "Particles.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Particle*> Particle::list;
Particle::Particle() {
    list.push_back(this);
}
Particle::~Particle() {
    for (int i = 0; i < list.size(); i++) {
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
    list.push_back(this);
}
Emitter::~Emitter() {
    for (int i = 0; i < list.size(); i++) {
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

// Constants.
const int PARTICLE_COUNT = 8;
const int PARTICLE_DUST = 0;
const int PARTICLE_SMOKE_BLACK = 1;
const int PARTICLE_SMOKE_WHITE = 2;
const int PARTICLE_FLASH = 3;
const int PARTICLE_SPARK = 4;
const int PARTICLE_BLOOD = 5;
const int PARTICLE_SUN = 6;
const int PARTICLE_HG = 7;

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

Particle* CreateParticle(float x, float y, float z, int image, float size, float gravity = 1.0, int lifetime = 200) {
    int tex = GrabTexture("GFX/" + particleList[image], 1+2);
    Particle* p = new Particle();
    p->lifetime = lifetime;

    p->obj = bbCreateSprite();
    bbPositionEntity(p->obj, x, y, z, true);
    bbEntityTexture(p->obj, tex);
    bbRotateEntity(p->obj, 0, 0, bbRnd(360));
    bbEntityFX(p->obj, 1 + 8);
    DropAsset(tex);

    bbSpriteViewMode(p->obj, 3);

    switch (image) {
        case 0,5,6: {
            bbEntityBlend(p->obj, 1);
        } break;
        case 1,2,3,4,7: {
            bbEntityBlend(p->obj, BLEND_ADD);
        } break;
    }

    p->pvt = bbCreatePivot();
    bbPositionEntity(p->pvt, x, y, z, true);

    p->image = image;
    p->gravity = gravity * 0.004;
    p->r = 255;
    p->g = 255;
    p->b = 255;
    p->a = 1.0;
    p->size = size;
    bbScaleSprite(p->obj, p->size, p->size);
    return p;
}

void UpdateParticles() {
    Particle* p;
    for (int iterator99 = 0; iterator99 < Particle::getListSize(); iterator99++) {
        p = Particle::getObject(iterator99);

        bbMoveEntity(p->pvt, 0, 0, p->speed * timing->tickDuration);
        if (p->gravity != 0) {
            p->yspeed = p->yspeed - p->gravity * timing->tickDuration;
        }
        bbTranslateEntity(p->pvt, 0, p->yspeed * timing->tickDuration, 0, true);

        bbPositionEntity(p->obj, bbEntityX(p->pvt,true), bbEntityY(p->pvt,true), bbEntityZ(p->pvt,true), true);

        //TurnEntity(p\obj, 0, 0, timing\tickDuration)

        if (p->aChange != 0) {
            p->a = Min(Max(p->a+p->aChange * timing->tickDuration,0.0),1.0);
            bbEntityAlpha(p->obj, p->a);
        }

        if (p->sizeChange != 0) {
            p->size = p->size+p->sizeChange * timing->tickDuration;
            bbScaleSprite(p->obj, p->size, p->size);
        }

        bbShowEntity(p->obj);
        bbEntityAlpha(p->obj,1.0);

        p->lifetime = p->lifetime-timing->tickDuration;
        if (p->lifetime <= 0 | p->size < 0.00001 | p->a <= 0) {
            RemoveParticle(p);
        }
    }
}

void RemoveParticle(Particle* p) {
    bbFreeEntity(p->obj);
    bbFreeEntity(p->pvt);
    delete p;
}

void UpdateEmitters() {
    InSmoke = false;
    Emitter* e;
    Particle* p;
    float dist;

    for (int iterator100 = 0; iterator100 < Emitter::getListSize(); iterator100++) {
        e = Emitter::getObject(iterator100);

        if (timing->tickDuration > 0 & (mainPlayer->currRoom == e->room | e->room->dist < 8)) {
            //If (EntityDistance(mainPlayer\cam, e\obj) < 6.0) Then
            p = CreateParticle(bbEntityX(e->obj, true), bbEntityY(e->obj, true), bbEntityZ(e->obj, true), PARTICLE_SMOKE_WHITE, e->size, e->gravity, e->lifeTime);
            p->speed = e->speed;
            bbRotateEntity(p->pvt, bbEntityPitch(e->obj, true), bbEntityYaw(e->obj, true), bbEntityRoll(e->obj, true), true);
            bbTurnEntity(p->pvt, bbRnd(-e->randAngle, e->randAngle), bbRnd(-e->randAngle, e->randAngle), 0);

            bbTurnEntity(p->obj, 0,0,bbRnd(360));

            p->sizeChange = e->sizeChange;

            p->aChange = e->aChange;

            e->soundCHN = LoopRangedSound_SM(sndManager->hiss, e->soundCHN, mainPlayer->cam, e->obj);

            if (InSmoke == false) {
                if (IsPlayerWearingItem(mainPlayer,"gasmask") & IsPlayerWearingItem(mainPlayer,"hazmatsuit")) {
                    dist = Distance(bbEntityX(mainPlayer->cam, true), bbEntityZ(mainPlayer->cam, true), bbEntityX(e->obj, true), bbEntityZ(e->obj, true));
                    if (dist < 0.8) {
                        if (Abs(bbEntityY(mainPlayer->cam, true)-bbEntityY(e->obj,true))<5.0) {
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
            mainPlayer->blurTimer = Max(mainPlayer->blurTimer, (mainPlayer->blinkEffect - (70 * 6)) / (70.0 * 24.0));
        }
        if (mainPlayer->blinkEffect > (70 * 24)) {
            DeathMSG = "Subject D-9341 found dead in [DATA REDACTED]. Cause of death: Suffocation due to decontamination gas.";
            Kill(mainPlayer);
        }

        if (!mainPlayer->dead) {
            if (bbRand(150) == 1) {
                //If (Not IsChannelPlaying(CoughCHN)) Then CoughCHN = PlaySound2(mainPlayer\coughSFX[Rand(0, 2)))
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
            e->size = 0.03;
            e->gravity = -0.2;
            e->lifeTime = 200;
            e->sizeChange = 0.005;
            e->speed = 0.004;
            e->randAngle = 20;
            e->aChange = -0.008;
        } break;
        case 1: {
            e->size = 0.03;
            e->gravity = -0.2;
            e->lifeTime = 200;
            e->sizeChange = 0.008;
            e->speed = 0.004;
            e->randAngle = 40;
            e->aChange = -0.01;
        } break;
    }

    Room* r;
    for (int iterator101 = 0; iterator101 < Room::getListSize(); iterator101++) {
        r = Room::getObject(iterator101);

        if (Abs(bbEntityX(e->obj) - bbEntityX(r->obj)) < 4.0 & Abs(bbEntityZ(e->obj) - bbEntityZ(r->obj)) < 4.0) {
            e->room = r;
        }
    }

    return e;

}

}

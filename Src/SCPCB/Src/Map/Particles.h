#ifndef PARTICLES_H_INCLUDED
#define PARTICLES_H_INCLUDED
#include <vector>

class Sprite;
class Pivot;
class gxChannel;

namespace CBN {

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

// Structs.
struct Particle {
private:
    static std::vector<Particle*> list;

public:
    Particle();
    ~Particle();
    static int getListSize();
    static Particle* getObject(int index);

    Sprite* sprite;
    Pivot* pvt;
    int image;

    float r;
    float g;
    float b;
    float a;
    float size;
    float speed;
    float yspeed;
    float gravity;
    float rChange;
    float gChange;
    float bChange;
    float aChange;
    float sizeChange;

    float lifetime;
};

struct Emitter {
private:
    static std::vector<Emitter*> list;

public:
    Emitter();
    ~Emitter();
    static int getListSize();
    static Emitter* getObject(int index);

    Pivot* obj;

    float size;
    float gravity;
    int lifeTime;

    bool disable;

    struct Room* room;

    gxChannel* soundCHN;

    float speed;
    float randAngle;
    float sizeChange;
    float aChange;
};

// Globals.
extern String particleList[PARTICLE_COUNT];
extern int InSmoke;

// Functions.
void LoadParticles();

Particle* CreateParticle(float x, float y, float z, int image, float size, float gravity = 1.0, int lifetime = 200);

void UpdateParticles();

void RemoveParticle(Particle* p);

void UpdateEmitters();

Emitter* CreateEmitter(float x, float y, float z, int emittertype);

}
#endif // PARTICLES_H_INCLUDED

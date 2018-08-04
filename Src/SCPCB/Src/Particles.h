#ifndef PARTICLES_H_INCLUDED
#define PARTICLES_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Particle {
private:
    static std::vector<Particle*> list;

public:
    Particle();
    ~Particle();
    static int getListSize();
    static Particle* getObject(int index);

    int obj;
    int pvt;
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

    int obj;

    float size;
    float gravity;
    int lifeTime;

    int disable;

    struct Room* room;

    int soundCHN;

    float speed;
    float randAngle;
    float sizeChange;
    float aChange;
};

// Constants.
extern const int PARTICLE_COUNT;
extern const int PARTICLE_DUST;
extern const int PARTICLE_SMOKE_BLACK;
extern const int PARTICLE_SMOKE_WHITE;
extern const int PARTICLE_FLASH;
extern const int PARTICLE_SPARK;
extern const int PARTICLE_BLOOD;
extern const int PARTICLE_SUN;
extern const int PARTICLE_HG;

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

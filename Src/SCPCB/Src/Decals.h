#ifndef DECALS_H_INCLUDED
#define DECALS_H_INCLUDED
#include <vector>

class Sprite;

namespace CBN {

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

// Structs.
struct Decal {
private:
    static std::vector<Decal*> list;

public:
    Decal();
    ~Decal();
    static int getListSize();
    static Decal* getObject(int index);

    Sprite* obj;
    float sizeChange;
    float size;
    float maxSize;
    float alphaChange;
    float alpha;
    int blendmode;
    int fx;
    int id;
    float timer;

    float lifetime;

    float x;
    float y;
    float z;
    float pitch;
    float yaw;
    float roll;
};

// Globals.
extern String decalList[DECAL_COUNT];

// Functions.
void LoadDecals();

Decal* CreateDecal(int id, float x, float y, float z, float pitch, float yaw, float roll);

void UpdateDecals();

}
#endif // DECALS_H_INCLUDED

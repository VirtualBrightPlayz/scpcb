#ifndef DECALS_H_INCLUDED
#define DECALS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Decal {
private:
    static std::vector<Decal*> list;

public:
    Decal();
    ~Decal();
    static int getListSize();
    static Decal* getObject(int index);

    int obj;
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

// Constants.
extern const int DECAL_COUNT;
extern const int DECAL_CORROSION;
extern const int DECAL_CRACKS;
extern const int DECAL_BLOOD_SPREAD;
extern const int DECAL_BLOOD_SPLATTER;
extern const int DECAL_BLOOD_POOL;
extern const int DECAL_PD1;
extern const int DECAL_PD2;
extern const int DECAL_PD3;
extern const int DECAL_PD4;
extern const int DECAL_PD5;
extern const int DECAL_PD6;
extern const int DECAL_BULLET_HOLE1;
extern const int DECAL_BULLET_HOLE2;
extern const int DECAL_BLOOD_DROP1;
extern const int DECAL_BLOOD_DROP2;

// Globals.
extern String decalList[DECAL_COUNT];

// Functions.
void LoadDecals();

Decal* CreateDecal(int id, float x, float y, float z, float pitch, float yaw, float roll);

void UpdateDecals();

}
#endif // DECALS_H_INCLUDED

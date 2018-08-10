#ifndef ROOM_TEST_860_2_H_INCLUDED
#define ROOM_TEST_860_2_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
const int gridsize = 10;
const int deviation_chance = 40;
const int branch_chance = 65;
const int branch_max_life = 4;
const int branch_die_chance = 18;
const int max_deviation_distance = 3;
const int return_chance = 27;
const int center = 5;

// Structs.
struct Forest {
private:
    static std::vector<Forest*> list;

public:
    Forest();
    ~Forest();
    static int getListSize();
    static Forest* getObject(int index);

    MeshModel* tileMesh[6];
    MeshModel* detailMesh[6];
    int grid[(gridsize*gridsize)+11];
    MeshModel* tileEntities[(gridsize*gridsize)+1];
    Pivot* forest_Pivot;

    MeshModel* door[2];
    MeshModel* detailEntities[2];

    int id;
};

// Globals.
extern int LastForestID;

// Functions.
void FillRoom_test_860_2(struct Room* r);

void UpdateEvent_test_860_2(struct Event* e);

int move_forward(int dir, int pathx, int pathy, int retval = 0);

int chance(int chanc);

int turn_if_deviating(int max_deviation_distance_, int pathx, int center_, int dir, int retval = 0);

void GenForestGrid(Forest* fr);

void PlaceForest(Forest* fr, float x, float y, float z, Room* r);

void DestroyForest(Forest* fr);

void UpdateForest(Forest* fr, Object* ent);

MeshModel* load_terrain(bbImage* hmap, float yscale, Texture* t1, Texture* t2, Texture* mask);

}
#endif // ROOM_TEST_860_2_H_INCLUDED

#ifndef ROOM_TEST_860_2_H_INCLUDED
#define ROOM_TEST_860_2_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Forest {
private:
    static std::vector<Forest*> list;

public:
    Forest();
    ~Forest();
    static int getListSize();
    static Forest* getObject(int index);

    int tileMesh[6];
    int detailMesh[6];
    int tileTexture[10];
    int grid[(gridsize*gridsize)+11];
    int tileEntities[(gridsize*gridsize)+1];
    int forest_Pivot;

    int door[2];
    int detailEntities[2];

    int id;
};

// Constants.
extern const int gridsize;
extern const int deviation_chance;
extern const int branch_chance;
extern const int branch_max_life;
extern const int branch_die_chance;
extern const int max_deviation_distance;
extern const int return_chance;
extern const int center;

// Globals.
extern int LastForestID;

// Functions.
void FillRoom_test_860_2(Room* r);

void UpdateEvent_test_860_2(Event* e);

int move_forward(int dir, int pathx, int pathy, int retval = 0);

int chance(int chanc);

int turn_if_deviating(int max_deviation_distance_, int pathx, int center_, int dir, int retval = 0);

void GenForestGrid(Forest* fr);

void PlaceForest(Forest* fr, float x, float y, float z, Room* r);

void DestroyForest(Forest* fr);

void UpdateForest(Forest* fr, int ent);

int load_terrain(int hmap, float yscale = 0.7, int t1, int t2, int mask);

}
#endif // ROOM_TEST_860_2_H_INCLUDED

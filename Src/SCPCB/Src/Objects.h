#ifndef OBJECTS_H_INCLUDED
#define OBJECTS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Lever {
private:
    static std::vector<Lever*> list;

public:
    Lever();
    ~Lever();
    static int getListSize();
    static Lever* getObject(int index);

    bool succ = false;
    bool locked = false;
    class MeshModel* obj;
    class MeshModel* baseObj;
};

struct ElevatorObj {
private:
    static std::vector<ElevatorObj*> list;

public:
    ElevatorObj();
    ~ElevatorObj();
    static int getListSize();
    static ElevatorObj* getObject(int index);

    int obj;
    int inFacility;
    struct Door* door;
};

// Functions.
class MeshModel* CreateButton(float x, float y, float z, float pitch, float yaw, float roll = 0);

void UpdateButton(class MeshModel* obj);

Lever* CreateLever();

void UpdateLevers();

float UpdateElevators(float state, struct Door* door1, struct Door* door2, int room1, int room2, struct Event* event);

float UpdateElevators2(float State, struct Door* door1, struct Door* door2, int room1, int room2, struct Event* event);

ElevatorObj* AssignElevatorObj(int obj, struct Door* door, int in_facility);

}
#endif // OBJECTS_H_INCLUDED

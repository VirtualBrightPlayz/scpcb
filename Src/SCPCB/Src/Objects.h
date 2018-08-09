#ifndef OBJECTS_H_INCLUDED
#define OBJECTS_H_INCLUDED
#include <vector>

class MeshModel;

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
    MeshModel* obj;
    MeshModel* baseObj;
};

struct ElevatorObj {
private:
    static std::vector<ElevatorObj*> list;

public:
    ElevatorObj();
    ~ElevatorObj();
    static int getListSize();
    static ElevatorObj* getObject(int index);

    MeshModel* obj;
    int inFacility;
    struct Door* door;
};

// Functions.
MeshModel* CreateButton(float x, float y, float z, float pitch, float yaw, float roll = 0);

void UpdateButton(MeshModel* obj);

Lever* CreateLever();

void UpdateLevers();

float UpdateElevators(float state, struct Door* door1, struct Door* door2, Object* room1, Object* room2, struct Event* event);

float UpdateElevators2(float State, struct Door* door1, struct Door* door2, Object* room1, Object* room2, struct Event* event);

ElevatorObj* AssignElevatorObj(MeshModel* obj, struct Door* door, int in_facility);

}
#endif // OBJECTS_H_INCLUDED

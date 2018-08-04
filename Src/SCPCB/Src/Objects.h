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

    int succ = false;
    int locked = false;
    int obj;
    int baseObj;
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
    Door* door;
};

// Functions.
int CreateButton(float x, float y, float z, float pitch, float yaw, float roll = 0);

void UpdateButton(int obj);

Lever* CreateLever();

void UpdateLevers();

float UpdateElevators(float state, Door* door1, Door* door2, int room1, int room2, Event* event);

float UpdateElevators2(float State, Door* door1, Door* door2, int room1, int room2, Event* event);

ElevatorObj* AssignElevatorObj(int obj, Door* door, int in_facility);

}
#endif // OBJECTS_H_INCLUDED

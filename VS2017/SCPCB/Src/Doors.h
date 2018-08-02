#ifndef DOORS_H_INCLUDED
#define DOORS_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Door {
private:
    static std::vector<Door*> list;

public:
    Door();
    ~Door();
    static int getListSize();
    static Door* getObject(int index);

    int obj;
    int obj2;
    int frameobj;
    int buttons[2];
    int locked;
    int open;
    float angle;
    float openstate;
    int fastopen;
    int typ;
    int timer;
    float timerstate;
    Room* room;

    float dist;

    String tag;
    String code;

    int id;

    int autoClose;

    Door* linkedDoor;

    int isElevatorDoor = false;

    int mtfClose = true;
    //TODO: Not needed?
    int npcCalledElevator = false;
};

// Constants.
extern const int DOOR_TYPE_DEF;
extern const int DOOR_TYPE_HCZ;
extern const int DOOR_TYPE_CONT;

// Globals.
extern float UpdateDoorsTimer;
extern int DoorTempID;

// Functions.
Door* CreateDoor(float x, float y, float z, float angle, Room* room, int open = false, int doorType = DOOR_TYPE_DEF, String tag = "", String code = "");

void UpdateDoors();

void UseDoor(Door* d, int showmsg = true);

void RemoveDoor(Door* d);

}
#endif // DOORS_H_INCLUDED

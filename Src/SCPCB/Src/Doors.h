#ifndef DOORS_H_INCLUDED
#define DOORS_H_INCLUDED
#include <vector>

class MeshModel;

namespace CBN {

struct Room;

// Constants.
const int DOOR_TYPE_DEF = 0;
const int DOOR_TYPE_HCZ = 1;
const int DOOR_TYPE_CONT = 2;

// Structs.
struct Door {
private:
    static std::vector<Door*> list;

public:
    Door();
    ~Door();
    static int getListSize();
    static Door* getObject(int index);

    MeshModel* obj;
	MeshModel* obj2;
	MeshModel* frameobj;
    MeshModel* buttons[2];
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

// Globals.
extern float UpdateDoorsTimer;
extern int DoorTempID;

// Functions.
Door* CreateDoor(float x, float y, float z, float angle, Room* room, int open = false, int doorType = DOOR_TYPE_DEF, const String& tag = "", const String& code = "");

void UpdateDoors();

void UseDoor(Door* d, int showmsg = true);

void RemoveDoor(Door* d);

}
#endif // DOORS_H_INCLUDED

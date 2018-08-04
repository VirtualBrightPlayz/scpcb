#ifndef ROOM_LCK_AIR_2_H_INCLUDED
#define ROOM_LCK_AIR_2_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int ROOM_LCKA2_TRIGGERPIVOT;
extern const int ROOM_LCKA2_BROKENDOOR;
extern const int ROOM_LCKA2_CORPSESPAWN;
extern const int ROOM_LCKA2_PIPES;
extern const int EVENT_LCKA2_ACTIVE;
extern const int EVENT_LCKA2_LEAVING;
extern const int EVENT_LCKA2_TIMER;
extern const int EVENT_LCKA2_SPAWNEDCORPSE;

// Functions.
void FillRoom_lck_air_2(Room* r);

void UpdateEvent_lck_air_2(Event* e);

void UpdateEvent_lck_air_broke_2(Event* e);

}
#endif // ROOM_LCK_AIR_2_H_INCLUDED

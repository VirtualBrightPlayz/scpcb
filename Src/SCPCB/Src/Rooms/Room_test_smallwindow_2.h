#ifndef ROOM_TEST_SMALLWINDOW_2_H_INCLUDED
#define ROOM_TEST_SMALLWINDOW_2_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int ROOM_TSW2_173SPAWN;
extern const int ROOM_TSW2_173JUMP;
extern const int ROOM_TSW2_GLASS;
extern const int EVENT_TSW2_173SPAWNED;
extern const int EVENT_TSW2_173JUMPTIMER;

// Functions.
void FillRoom_test_smallwindow_2(struct Room* r);

void UpdateEventTestroom173(struct Event* e);

}
#endif // ROOM_TEST_SMALLWINDOW_2_H_INCLUDED

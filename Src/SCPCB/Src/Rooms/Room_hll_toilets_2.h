#ifndef ROOM_HLL_TOILETS_2_H_INCLUDED
#define ROOM_HLL_TOILETS_2_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
void FillRoom_hll_toilets_2(struct Room* r);

void UpdateEventToiletguard(struct Event* e);

void UpdateEventButtghost(struct Event* e);

}
#endif // ROOM_HLL_TOILETS_2_H_INCLUDED

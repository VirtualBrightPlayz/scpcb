#ifndef NPCTYPEGUARD_H_INCLUDED
#define NPCTYPEGUARD_H_INCLUDED
#include <vector>

namespace CBN {

struct NPC;

// Constants.
extern const int STATEGUARD_IDLE;
extern const int STATEGUARD_LOOK;
extern const int STATEGUARD_MOVE_TO_TARGET;
extern const int STATEGUARD_SHOOT_TARGET;
extern const int STATEGUARD_DEAD;

// Functions.
void InitializeNPCtypeGuard(NPC* n);

void UpdateNPCtypeGuard(NPC* n);

}
#endif // NPCTYPEGUARD_H_INCLUDED

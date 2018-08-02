#ifndef NPCTYPE173_H_INCLUDED
#define NPCTYPE173_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int STATE173_IDLE;
extern const int STATE173_ATTACK;
extern const int STATE173_MOVE_TO_TARGET;
extern const int STATE173_BEING_CONTAINED;
extern const int STATE173_CONTAINED;

// Functions.
void InitializeNPCtype173(NPC* n);

void UpdateNPCtype173(NPC* n);

}
#endif // NPCTYPE173_H_INCLUDED

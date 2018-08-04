#ifndef NPCTYPE049_H_INCLUDED
#define NPCTYPE049_H_INCLUDED
#include <vector>

namespace CBN {

// Constants.
extern const int STATE049_IDLE;
extern const int STATE049_ATTACK;
extern const int STATE049_ROAMING;

// Functions.
void InitializeNPCtype049(NPC* n);

void UpdateNPCtype049(NPC* n);

}
#endif // NPCTYPE049_H_INCLUDED

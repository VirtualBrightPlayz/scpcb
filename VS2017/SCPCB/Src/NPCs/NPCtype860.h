#ifndef NPCTYPE860_H_INCLUDED
#define NPCTYPE860_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
void InitializeNPCtype860(struct NPC* n);

void UpdateNPCtype860(struct NPC* n);

float Find860Angle(struct NPC* n, struct Forest* fr);

}
#endif // NPCTYPE860_H_INCLUDED

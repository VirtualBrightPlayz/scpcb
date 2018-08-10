#ifndef NPCTYPEMTF_H_INCLUDED
#define NPCTYPEMTF_H_INCLUDED
#include <vector>

namespace CBN {

struct NPC;
struct Room;

// Globals.
extern float MTFtimer;
extern Room* MTFrooms[10];
extern int MTFroomState[10];

// Functions.
void InitializeNPCtypeMTF(NPC* n);

void UpdateNPCtypeMTF(NPC* n);

void UpdateMTF();

}
#endif // NPCTYPEMTF_H_INCLUDED

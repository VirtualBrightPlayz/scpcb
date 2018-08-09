#ifndef NPCTYPE106_H_INCLUDED
#define NPCTYPE106_H_INCLUDED
#include <vector>

namespace CBN {

struct NPC;

// Structs.
struct Data106 {
private:
    static std::vector<Data106*> list;

public:
    Data106();
    ~Data106();
    static int getListSize();
    static Data106* getObject(int index);

};

// Constants.
extern const int STATE106_IDLE;
extern const int STATE106_RISE;
extern const int STATE106_ATTACK;
extern const int STATE106_PD;
extern const int STATE106_MOVE_TO_TARGET;
extern const int STATE106_CONTAINED;

// Functions.
void InitializeNPCtype106(NPC* n);

void UpdateNPCtype106(NPC* n);

}
#endif // NPCTYPE106_H_INCLUDED

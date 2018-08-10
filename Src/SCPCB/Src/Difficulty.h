#ifndef DIFFICULTY_H_INCLUDED
#define DIFFICULTY_H_INCLUDED
#include <StringType.h>
#include <vector>

namespace CBN {

// Constants.
const int SAFE = 0;
const int EUCLID = 1;
const int KETER = 2;
const int CUSTOM = 3;
const int SAVEANYWHERE = 0;
const int SAVEONQUIT = 1;
const int SAVEONSCREENS = 2;
const int EASY = 0;
const int NORMAL = 1;
const int HARD = 2;

// Structs.
struct Difficulty {
private:
    static std::vector<Difficulty*> list;

public:
    Difficulty();
    ~Difficulty();
    static int getListSize();
    static Difficulty* getObject(int index);

    String name;
    String description;
    int permaDeath;
    int aggressiveNPCs;
    int saveType;
    int otherFactors;

    int customizable;
};

// Globals.
extern Difficulty* difficulties[4];
extern Difficulty* SelectedDifficulty;

// Functions.
void InitializeDifficulties();

}
#endif // DIFFICULTY_H_INCLUDED

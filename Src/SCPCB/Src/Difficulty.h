#ifndef DIFFICULTY_H_INCLUDED
#define DIFFICULTY_H_INCLUDED
#include <vector>

namespace CBN {

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

// Constants.
extern const int SAFE;
extern const int EUCLID;
extern const int KETER;
extern const int CUSTOM;
extern const int SAVEANYWHERE;
extern const int SAVEONQUIT;
extern const int SAVEONSCREENS;
extern const int EASY;
extern const int NORMAL;
extern const int HARD;

// Globals.
extern Difficulty* difficulties[4];
extern Difficulty* SelectedDifficulty;

}
#endif // DIFFICULTY_H_INCLUDED

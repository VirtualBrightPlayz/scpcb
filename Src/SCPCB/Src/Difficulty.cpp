#include "Difficulty.h"
#include "include.h"

namespace CBN {

// Structs.
std::vector<Difficulty*> Difficulty::list;
Difficulty::Difficulty() {
    list.push_back(this);
}
Difficulty::~Difficulty() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Difficulty::getListSize() {
    return list.size();
}
Difficulty* Difficulty::getObject(int index) {
    return list[index];
}

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

// Globals.
Difficulty* difficulties[4];
Difficulty* SelectedDifficulty;

}

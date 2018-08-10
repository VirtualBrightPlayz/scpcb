#include "Difficulty.h"

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

// Globals.
Difficulty* difficulties[4];
Difficulty* SelectedDifficulty;

}

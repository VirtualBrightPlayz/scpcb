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

// Functions.
void InitializeDifficulties() {
    difficulties[SAFE] = new Difficulty();
    difficulties[SAFE]->name = "Safe";
    difficulties[SAFE]->description = "The game can be saved any time. However, as in the case of SCP Objects, a Safe classification does not mean that handling it does not pose a threat.";
    difficulties[SAFE]->permaDeath = false;
    difficulties[SAFE]->aggressiveNPCs = false;
    difficulties[SAFE]->saveType = SAVEANYWHERE;
    difficulties[SAFE]->customizable = false;
    difficulties[SAFE]->otherFactors = EASY;
;
    difficulties[EUCLID] = new Difficulty();
    difficulties[EUCLID]->name = "Euclid";
    difficulties[EUCLID]->description = "In Euclid difficulty, saving is only allowed at specific locations marked by lit up computer screens. ";
    difficulties[EUCLID]->description = difficulties[EUCLID]->description + "Euclid-class objects are inherently unpredictable, so that reliable containment is not always possible.";
    difficulties[EUCLID]->permaDeath = false;
    difficulties[EUCLID]->aggressiveNPCs = false;
    difficulties[EUCLID]->saveType = SAVEONSCREENS;
    difficulties[EUCLID]->customizable = false;
    difficulties[EUCLID]->otherFactors = NORMAL;
;
    difficulties[KETER] = new Difficulty();
    difficulties[KETER]->name = "Keter";
    difficulties[KETER]->description = "Keter-class objects are considered the most dangerous ones in Foundation containment. ";
    difficulties[KETER]->description = difficulties[KETER]->description + "The same can be said for this difficulty level: the SCPs are more aggressive, and you have only one life - when you die, the game is over. ";
    difficulties[KETER]->permaDeath = true;
    difficulties[KETER]->aggressiveNPCs = true;
    difficulties[KETER]->saveType = SAVEONQUIT;
    difficulties[KETER]->customizable = false;
    difficulties[KETER]->otherFactors = HARD;
;
    difficulties[CUSTOM] = new Difficulty();
    difficulties[CUSTOM]->name = "Custom";
    difficulties[CUSTOM]->permaDeath = false;
    difficulties[CUSTOM]->aggressiveNPCs = true;
    difficulties[CUSTOM]->saveType = SAVEANYWHERE;
    difficulties[CUSTOM]->customizable = true;
    difficulties[CUSTOM]->otherFactors = EASY;
;
    SelectedDifficulty = difficulties[SAFE];
}

}

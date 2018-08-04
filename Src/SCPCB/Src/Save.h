#ifndef SAVE_H_INCLUDED
#define SAVE_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
void SaveGame(String file);

void LoadGame(String file, int quick = false);

void LoadSaveGames();

void LoadSavedMaps();

void LoadMap(String file);

}
#endif // SAVE_H_INCLUDED

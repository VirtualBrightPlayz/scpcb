#ifndef SAVE_H_INCLUDED
#define SAVE_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
void SaveGame(const String& file);

void LoadGame(const String& file, int quick = false);

void LoadSaveGames();

void LoadSavedMaps();

void LoadMap(const String& file);

}
#endif // SAVE_H_INCLUDED

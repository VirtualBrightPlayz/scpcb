#ifndef MATERIALS_H_INCLUDED
#define MATERIALS_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
String StripFilename(String file);

int GetTextureFromCache(String name);

Material* GetCache(String name);

void AddTextureToCache(String name, int texture);

void ClearTextureCache();

void FreeTextureCache();

int LoadRMeshTexture(String roompath, String name, int flags);

}
#endif // MATERIALS_H_INCLUDED

#ifndef MATERIALS_H_INCLUDED
#define MATERIALS_H_INCLUDED
#include <StringType.h>
#include <vector>

class Texture;

namespace CBN {

// Structs.
struct Material {
private:
    static std::vector<Material*> list;

public:
    Material();
    ~Material();
    static int getListSize();
    static Material* getObject(int index);

    String name;
    Texture* diff;
    //Field Bump

    int stepSound;
};


// Functions.
String StripFilename(const String& file);

Texture* GetTextureFromCache(const String& name);

struct Material* GetCache(const String& name);

void AddTextureToCache(const String& name, Texture* texture);

void ClearTextureCache();

void FreeTextureCache();

Texture* LoadRMeshTexture(const String& roompath, String name, int flags);

}
#endif // MATERIALS_H_INCLUDED

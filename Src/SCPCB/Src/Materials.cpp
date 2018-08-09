#include <iostream>
#include <bbblitz3d.h>

#include "Materials.h"
#include "Assets.h"

namespace CBN {

// Functions.
String StripFilename(String file) {
    String mi = "";
    int lastSlash = 0;
    int i;
    if (file.size()>0) {
        for (i = 0; i < file.size(); i++) {
            mi = String(file.charAt(i));
            if (mi.equals('\\') || mi.equals('/')) {
                lastSlash = i;
            }
        }
    }

    return file.substr(0,lastSlash);
}

Texture* GetTextureFromCache(String name) {
    Material* tc;
    for (int i = 0; i < Material::getListSize(); i++) {
        tc = Material::getObject(i);

        if (tc->name.toLower().equals(name.toLower())) {
            return tc->diff;
        }
    }
    return 0;
}

Material* GetCache(String name) {
    Material* tc;
    for (int i = 0; i < Material::getListSize(); i++) {
        tc = Material::getObject(i);

        if (tc->name.toLower().equals(name.toLower())) {
            return tc;
        }
    }
    return nullptr;
}

void AddTextureToCache(String name, Texture* texture) {
    Material* tc = GetCache(name);
    if (tc==nullptr) {
        tc = new Material();
        tc->name = name.toLower();

        tc->diff = 0;
    }
    if (tc->diff==0) {
        tc->diff = texture;
    }
}

void ClearTextureCache() {
    Material* tc;
    for (int i = 0; i < Material::getListSize(); i++) {
        tc = Material::getObject(i);

        if (tc->diff!=0) {
            bbFreeTexture(tc->diff);
        }
        //If (tc\bump<>0) Then FreeTexture(tc\bump)
        delete tc;
    }
}

void FreeTextureCache() {
    Material* tc;
    for (int i = 0; i < Material::getListSize(); i++) {
        tc = Material::getObject(i);

        if (tc->diff!=0) {
            bbFreeTexture(tc->diff);
        }
        //If (tc\bump<>0) Then FreeTexture(tc\bump)
        // : tc\bump = 0
        tc->diff = 0;
    }
}

Texture* LoadRMeshTexture(String roompath, String name, int flags) {
    Texture* texture = nullptr;
    if (texture == nullptr) {
        texture = bbLoadTexture(GetImagePath(roompath+name),flags);
    }
    if (texture == nullptr) {
        texture = bbLoadTexture(GetImagePath("GFX/map/Textures/"+name),flags);
    }
    if (texture != 0) {
        std::cout << bbTextureName(texture);
    } else {
        //RuntimeError(name)
        texture = bbLoadTexture("GFX/Map/Textures/dirtymetal.jpg",flags);
    }
    return texture;
}

}

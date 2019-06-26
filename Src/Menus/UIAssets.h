#ifndef UIASSETS_H_INCLUDED
#define UIASSETS_H_INCLUDED

#include "../Wrap/Graphics.h"

class UIMesh;

// Collection of UI meshes.
class UIAssets {
public:
    UIMesh* menuWhite = nullptr;
    UIMesh* menuBlack = nullptr;
    // Partially-opaque grey tint to light up objects that are hovered over.
    UIMesh* hoverLightUp = nullptr;

    UIAssets(const Graphics& gfx);
    ~UIAssets();

    void update();
    void draw() const;
};

#endif // UIASSETS_H_INCLUDED

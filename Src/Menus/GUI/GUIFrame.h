#ifndef GUIFRAME_H_INCLUDED
#define GUIFRAME_H_INCLUDED

#include "GUI.h"

class UIAssets;

class GUIFrame : public GUI {
private:
    Image background;
    Image foreground;

    virtual void updateVisibility(bool visible) override;
    virtual void internalUpdate(PGE::Vector2f mousePos) override;
    virtual void activeUpdate(PGE::Vector2f mousePos) override;

public:
    GUIFrame();
    GUIFrame(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment = Alignment::CenterXY);
};

#endif // GUIFRAME_H_INCLUDED

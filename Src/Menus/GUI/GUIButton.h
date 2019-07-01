#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include "GUI.h"

class UIAssets;

class GUIButton : public GUI {
private:
    bool clicked;

    Image background;
    Image foreground;
    Image hoverColor;

    virtual void updateVisibility(bool visible) override;
    virtual void internalUpdate(PGE::Vector2f mousePos) override;
    virtual void activeUpdate(PGE::Vector2f mousePos) override;

public:
    // Whether this button can be clicked.
    bool locked;

    GUIButton();
    GUIButton(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment = Alignment::CenterXY);

    bool isClicked() const;
};

#endif // GUIBUTTON_H_INCLUDED

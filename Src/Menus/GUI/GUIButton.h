#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include "GUI.h"

class UIAssets;

class GUIButton : public GUI {
private:
    bool clicked;

    Image* background = nullptr;
    Image* foreground = nullptr;
    Image* hoverColor = nullptr;

    virtual void updateVisibility(bool visible) override;
    virtual void internalUpdate(PGE::Vector2f mousePos) override;
    virtual void activeUpdate(PGE::Vector2f mousePos) override;

public:
    GUIButton();
    GUIButton(float x, float y, float width, float height, const UIAssets* assets, Alignment alignment = Alignment::CenterXY);

    bool isClicked() const;
};

#endif // GUIBUTTON_H_INCLUDED

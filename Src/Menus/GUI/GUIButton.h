#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include "GUIComponent.h"

class UIAssets;

class GUIButton : public GUIComponent {
    protected:
        bool clicked;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

        GUIButton() {}
    public:
        // Whether this button can be clicked.
        bool locked;

        GUIButton(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, const UIAssets* assets, Alignment alignment = Alignment::CenterXY);

        bool isClicked() const;
};

#endif // GUIBUTTON_H_INCLUDED

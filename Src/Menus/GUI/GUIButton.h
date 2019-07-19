#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include "GUIComponent.h"

class GUIButton : public GUIComponent {
    private:
        PGE::FileName menuwhite;
        PGE::FileName menublack;
        PGE::Color hoverColor;
        float borderThickness;
    
    protected:
        // MouseHit.
        bool active;
        // Activated and MouseUP.
        bool clicked;
        bool hovered;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

        GUIButton() {}
    public:
        // Whether this button can be clicked.
        bool locked;

        GUIButton(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY);

        bool isClicked() const;
};

#endif // GUIBUTTON_H_INCLUDED

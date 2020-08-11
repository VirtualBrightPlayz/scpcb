#ifndef GUIBUTTON_H_INCLUDED
#define GUIBUTTON_H_INCLUDED

#include "GUIComponent.h"
#include "../../Graphics/Font.h"

class TxtManager;

class GUIButton : public GUIComponent {
    private:
        const PGE::String menuwhite;
        const PGE::String menublack;
        PGE::Color hoverColor;
        float borderThickness;

        Font* font;
        PGE::String text;
        TxtManager* txtMng;

    protected:
        // MouseHit.
        bool active;
        // Activated and MouseUP.
        bool clicked;
        bool hovered;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        // Whether this button can be clicked.
        bool locked;

        GUIButton(UIMesh* um, Font* fnt, KeyBinds* kb, Config* con, TxtManager* tm, float x, float y, float width, float height, const PGE::String& txt, Alignment alignment = Alignment::CenterXY);

        bool isClicked() const;
};

#endif // GUIBUTTON_H_INCLUDED

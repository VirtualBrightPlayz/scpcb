#ifndef GUITEXT_H_INCLUDED
#define GUITEXT_H_INCLUDED

#include "GUIComponent.h"

class Font;

class GUIText : public GUIComponent {
    private:
        Font* font;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        PGE::String text;

        GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* font, float x, float y, Alignment alignment = Alignment::CenterXY);
};

#endif // GUITEXT_H_INCLUDED

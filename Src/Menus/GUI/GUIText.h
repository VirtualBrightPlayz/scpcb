#ifndef GUITEXT_H_INCLUDED
#define GUITEXT_H_INCLUDED

#include "GUIComponent.h"

class Font;
class LocalizationManager;

class GUIText : public GUIComponent {
    private:
        Font* font;
        LocalizationManager* lm;

        bool center;
        PGE::String text;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        PGE::Color color;
        float rotation;
        float scale;

        GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt, LocalizationManager* lm, float x, float y, bool center = false, Alignment alignment = Alignment::CenterXY);

        using GUIComponent::setX;
        using GUIComponent::setY;

        void setText(const PGE::String& newText);
};

#endif // GUITEXT_H_INCLUDED

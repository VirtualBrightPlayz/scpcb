#ifndef GUITEXT_H_INCLUDED
#define GUITEXT_H_INCLUDED

#include "GUIComponent.h"

class Font;
class LocalizationManager;

// Ported.
class GUIText : public GUIComponent {
    private:
        Font* font;
        LocalizationManager* lm;

        bool centerX;
        bool centerY;
        PGE::String text;

        PGE::Vector2f pos;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

        void updatePosition();

    public:
        PGE::Color color;
        float rotation;
        float scale;

        GUIText(UIMesh* um, KeyBinds* kb, Config* con, Font* fnt, LocalizationManager* lm, float x, float y, bool centerX = false, bool centerY = false, Alignment alignment = Alignment::CenterXY);

        void setX(float x) override;
        void setY(float y) override;
        const PGE::Vector2f& getTextPos() const;

        float getWidth() const;
        float getWidth(const PGE::String& text) const;
        float getHeight() const;

        void setText(const PGE::String& newText);
        const PGE::String& getText();
};

#endif // GUITEXT_H_INCLUDED

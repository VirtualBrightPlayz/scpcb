#ifndef GUIFRAME_H_INCLUDED
#define GUIFRAME_H_INCLUDED

#include "GUIComponent.h"

// Ported.
class GUIFrame : public GUIComponent {
    private:
        PGE::Rectanglef foreground;
        PGE::Rectanglef background;
        
        PGE::String menuwhite;
        PGE::String menublack;

        bool hovered;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        GUIFrame(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY);

        void setHovered(bool hovered);
};

#endif // GUIFRAME_H_INCLUDED

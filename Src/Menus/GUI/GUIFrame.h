#ifndef GUIFRAME_H_INCLUDED
#define GUIFRAME_H_INCLUDED

#include "GUIComponent.h"

class GUIFrame : public GUIComponent {
    private:
        const PGE::Rectanglef foreground;
        const PGE::Rectanglef background;
        
        const PGE::String menuwhite;
        const PGE::String menublack;
    
        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        GUIFrame(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY);
};

#endif // GUIFRAME_H_INCLUDED

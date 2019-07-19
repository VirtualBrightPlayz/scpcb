#ifndef GUIFRAME_H_INCLUDED
#define GUIFRAME_H_INCLUDED

#include "GUIComponent.h"

class GUIFrame : public GUIComponent {
    private:
        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

        GUIFrame() {}
    public:
        GUIFrame(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY);
};

#endif // GUIFRAME_H_INCLUDED

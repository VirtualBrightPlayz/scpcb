#ifndef INVSLOT_H_INCLUDED
#define INVSLOT_H_INCLUDED

#include "GUI/GUIComponent.h"

class Item;
class Config;

class InvSlot : public GUIComponent {
    private:

        Item* item;

        const PGE::String menuwhite;
        const PGE::String menublack;
        PGE::Color hoverColor;
        float borderThickness;

        bool doubleClicked;
        bool hovered;
    
        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y);
    
        static const int SIZE = 5;

        bool hasItem() const;
};

#endif // INVSLOT_H_INCLUDED

#ifndef INVSLOT_H_INCLUDED
#define INVSLOT_H_INCLUDED

#include "../Menus/GUI/GUIComponent.h"

class Item;
class Config;

class InvSlot : public GUIComponent {
    private:
        static const int SIZE = 5;

        Item* item;

        PGE::FileName menuwhite;
        PGE::FileName menublack;
        PGE::Color hoverColor;
        float borderThickness;

        bool doubleClicked;
        bool hovered;

    public:
        InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y);

        bool hasItem() const;

        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;
};

#endif // INVSLOT_H_INCLUDED

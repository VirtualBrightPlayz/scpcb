#ifndef INVSLOT_H_INCLUDED
#define INVSLOT_H_INCLUDED

#include "GUI/GUIComponent.h"

class Item;
class GUIFrame;
class Config;

// TODO: Move all of this to Angelscript.
class InvSlot : public GUIComponent {
    private:
        PGE::Rectanglef background;
        PGE::Rectanglef top;
        PGE::Rectanglef bottom;
        PGE::Rectanglef right;
        PGE::Rectanglef left;
        
        Item* item;

        bool hovered;
        bool equipped;

        void updateRectangles(bool doubled);
    
        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        static constexpr float screenSize = 7.5f;

        InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y);
        ~InvSlot();

        void addItem(Item* it);
        bool hasItem() const;
};

#endif // INVSLOT_H_INCLUDED

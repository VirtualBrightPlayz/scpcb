#ifndef INVSLOT_H_INCLUDED
#define INVSLOT_H_INCLUDED

#include "GUI/GUIComponent.h"

class Item;
class GUIFrame;
class Config;

class InvSlot : public GUIComponent {
    private:
        GUIFrame* frame;
        
        Item* item;

        bool doubleClicked;
    
        void updateInternal(PGE::Vector2f mousePos) override;
        void renderInternal() override;

    public:
        static constexpr float screenSize = 7.5f;

        InvSlot(UIMesh* um, KeyBinds* kb, Config* con, float x, float y);
        ~InvSlot();

        bool hasItem() const;
};

#endif // INVSLOT_H_INCLUDED

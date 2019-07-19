#ifndef GUI_H_INCLUDED
#define GUI_H_INCLUDED

#include "../../Graphics/UIMesh.h"
#include "../../Input/KeyBinds.h"

class GUIComponent {
    protected:
        UIMesh* uiMesh;
        KeyBinds* keyBinds;

        bool visible;

        float x;
        float y;
        float width;
        float height;

        Alignment alignment;

        bool mouseDown;

        GUIComponent(UIMesh* um, KeyBinds* kb, float x, float y, float width, float height, Alignment alignment);

        float getX2() const;
        float getY2() const;

        virtual void updateInternal(PGE::Vector2f mousePos) =0;
        virtual void renderInternal() =0;

        GUIComponent() {}
    public:
        virtual ~GUIComponent();

        void setVisibility(bool vis);
        
        void update(PGE::Vector2f mousePos);
        void render();
};

#endif // GUI_H_INCLUDED

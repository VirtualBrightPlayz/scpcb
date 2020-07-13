#ifndef GUI_H_INCLUDED
#define GUI_H_INCLUDED

#include "../../Graphics/UIMesh.h"
#include "../../Input/KeyBinds.h"

class Config;

enum class Alignment {
    CenterXY = 0x0,
    Left = 0x1,
    Right = 0x2,
    Top = 0x4,
    Bottom = 0x8
};

class GUIComponent {
    private:
        float x;
        float y;

    protected:
        UIMesh* uiMesh;
        KeyBinds* keyBinds;
        Config* config;

        float width;
        float height;

        Alignment alignment;

        bool mouseDown;

        GUIComponent(UIMesh* um, KeyBinds* kb, Config* con, float x, float y, float width, float height, Alignment alignment);

        void setX(float x);
        void setY(float y);
        float getX() const;
        float getY() const;
        float getX2() const;
        float getY2() const;

        virtual void updateInternal(PGE::Vector2f mousePos) =0;
        virtual void renderInternal() =0;

    public:
        virtual ~GUIComponent();

        constexpr static float SCALE_MAGNITUDE = 50.f;

        void update(PGE::Vector2f mousePos);
        void render();
};

const Alignment operator&(const Alignment& a, const Alignment& b);
const Alignment operator|(const Alignment& a, const Alignment& b);

#endif // GUI_H_INCLUDED

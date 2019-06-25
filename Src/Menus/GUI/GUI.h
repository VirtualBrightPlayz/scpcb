#ifndef GUI_H_INCLUDED
#define GUI_H_INCLUDED

#include "../../Graphics/UIMesh.h"

class GUI {
private:
    // Stores which element has been activated (I.E. which had a mousedown trigger).
    static GUI* active;
    // Stores which element is being hovered over.
    static GUI* hover;

    float hovered;

protected:
    float x;
    float y;
    float width;
    float height;

    Alignment alignment;

    bool mouseDown;

    bool visible;

    GUI(float x, float y, float width, float height, Alignment alignment);

    float getX2() const;
    float getY2() const;

    virtual void internalUpdate(PGE::Vector2f mousePos)=0;
    // Performs updates on the object when it is active.
    virtual void activeUpdate(PGE::Vector2f mousePos)=0;

public:
    virtual ~GUI();

    bool isHovered() const;

    void setVisibility(bool vis);
    virtual void updateVisibility()=0;

    // This handles setting the active element and hover detection alongside calling internalUpdate().
    void update(PGE::Vector2f mousePos);

    // Removes hover element and resets active element when mouse1 is up.
    static void reset();
};

#endif // GUI_H_INCLUDED

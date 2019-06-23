#ifndef GUI_H_INCLUDED
#define GUI_H_INCLUDED

#include "../Graphics/UIMesh.h"

class GUI {
private:
    // Stores which element has been activated (I.E. which had a mousedown trigger).
    static GUI* active;
    // Stores which element is being hovered over.
    static GUI* hover;

protected:
    float x;
    float y;
    float width;
    float height;

    Alignment alignment;
    float hovered;

    bool mouseDown;
    bool mouseUp;

    float getX2() const;
    float getY2() const;

    virtual void internalUpdate(PGE::Vector2f mousePos)=0;
    virtual void draw() const=0;

public:
    virtual ~GUI();

    bool isHovered() const;

    // This handles setting the active element and hover detection alongside calling internalUpdate().
    void update(PGE::Vector2f mousePos);

    // Removes hover element.
    static void reset();
};

#endif // GUI_H_INCLUDED

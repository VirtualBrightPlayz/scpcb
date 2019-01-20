#ifndef GUIBASE_H_INCLUDED
#define GUIBASE_H_INCLUDED

namespace CBN {

class GUIBase {
private:
    int x;
    int y;
    int width;
    int height;

    bool mouseHover;
    bool mouseHit;
    bool mouseUp;
    static float getScreenWidthScale();

protected:
    int getRelativeVal(int val);
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    bool relative;

public:
    GUIBase(int x, int y, int width, int height, bool relative = true);

    bool visible;
    bool xRelativeToScreenWidth;
    bool widthRelativeToScreenWidth;
    bool isMouseHover();
    bool isMouseHit();
    bool isMouseUp();

    virtual void update();
    virtual void draw()=0;
};

}
#endif // GUIBASE_H_INCLUDED

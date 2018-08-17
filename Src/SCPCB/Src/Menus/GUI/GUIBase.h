#ifndef GUIBASE_H_INCLUDED
#define GUIBASE_H_INCLUDED

namespace CBN {

class GUIBase {
private:
    bool mouseHover;
    bool mouseHit;
    bool mouseUp;

protected:
    int x;
    int y;
    int width;
    int height;

    bool relative;

public:
    GUIBase(int x, int y, int width, int height, bool relative = true);

    bool isMouseHover();
    bool isMouseHit();
    bool isMouseUp();

    virtual void update();
    virtual void draw()=0;
};

}
#endif // GUIBASE_H_INCLUDED

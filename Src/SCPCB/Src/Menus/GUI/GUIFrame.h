#ifndef GUIFRAME_H_INCLUDED
#define GUIFRAME_H_INCLUDED

namespace CBN {

class GUIFrame {
private:
    int xoffset;
    int yoffset;
    bool relative;

    bool mouseHover;
    bool mouseHit;
    bool mouseUp;

protected:
    int x;
    int y;
    int width;
    int height;

public:
    GUIFrame(int x, int y, int width, int height, int xoffset = 0, int yoffset = 0, bool relative = true);

    bool isMouseHover();
    bool isMouseHit();
    bool isMouseUp();

    void update();
    void draw();
};

}
#endif // GUIFRAME_H_INCLUDED

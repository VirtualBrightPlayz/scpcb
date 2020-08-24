shared const float SCALE_MAGNITUDE = 50.0;

namespace GUIComponent {
    shared float borderThickness = 0.5;
}


shared enum Alignment {
    CenterXY = 0x0,
    Left = 0x1,
    Right = 0x2,
    Top = 0x4,
    Bottom = 0x8
}

shared abstract class GUIComponent {
    private float trueX;
    private float trueY;

    float x {
        get { return trueX; }
        set {
            trueX = value;
            if ((alignment & Alignment::Left) != Alignment::CenterXY) {
                trueX -= SCALE_MAGNITUDE * UI::getAspectRatio();
            }

            if ((alignment & Alignment::Right) != Alignment::CenterXY) {
                trueX += SCALE_MAGNITUDE * UI::getAspectRatio();
            }
        }
    }

    float y {
        get { return trueY; }
        set {
            trueY = value;
            if ((alignment & Alignment::Left) != Alignment::CenterXY) {
                trueY -= SCALE_MAGNITUDE * UI::getAspectRatio();
            }

            if ((alignment & Alignment::Right) != Alignment::CenterXY) {
                trueY += SCALE_MAGNITUDE * UI::getAspectRatio();
            }
        }
    }

    float x2 {
        get { return trueX + width; }
    }

    float y2 {
        get { return trueY + height; }
    }

    protected float width;
    protected float height;

    Alignment alignment;

    GUIComponent(float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY) {
        this.width = width;
        this.height = height;

        this.alignment = alignment;

        this.x = x;
        this.y = y;
    }

    void update(const Vector2f&in mousePos) {}
    void render() {}
}

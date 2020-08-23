shared enum Alignment {
    CenterXY = 0x0,
    Left = 0x1,
    Right = 0x2,
    Top = 0x4,
    Bottom = 0x8
}

shared class GUIComponent {
    private float x;
    private float y;

    protected float width;
    protected float height;

    Alignment alignment;

    GUIComponent(float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.alignment = alignment;
    }
}

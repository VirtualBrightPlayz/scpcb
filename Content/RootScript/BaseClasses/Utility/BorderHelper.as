shared class BorderHelper {
    private Rectanglef top;
    private Rectanglef left;
    private Rectanglef bottom;
    private Rectanglef right;

    BorderHelper(float x, float y, float width, float height, float thickness) {
        top = Rectanglef(x, y, x + width, y + thickness);
        left = Rectanglef(x, y + thickness, x + thickness, y + height - thickness);
        bottom = Rectanglef(x, y + height - thickness, x + width, y + height);
        right = Rectanglef(x + width - thickness, y + thickness, x + width, y + height - thickness);
    }

    void addToUI() {
        UI::addRect(top);
        UI::addRect(left);
        UI::addRect(bottom);
        UI::addRect(right);
    }
}

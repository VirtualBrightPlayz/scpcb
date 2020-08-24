namespace GUIFrame {
    shared const string menuwhite = "SCPCB/GFX/Menu/menuwhite";
    shared const string menublack = "SCPCB/GFX/Menu/menublack";
    shared const Color hoverColor = Color(70, 70, 150, 200);
}

shared class GUIFrame : GUIComponent {
    private Rectanglef foreground;
    private Rectanglef background;

    bool hovered = false;

    GUIFrame(Menu@ menu, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY) {
        super(menu, x, y, width, height, alignment);
        foreground = Rectanglef(x + GUIComponent::borderThickness, y + GUIComponent::borderThickness, x2 - GUIComponent::borderThickness, y2 - GUIComponent::borderThickness);
        background = Rectanglef(x, y, x2, y2);
    }

    void render() override {
        UI::setTextured(GUIFrame::menuwhite, true);
        UI::addRect(background);
        UI::setTextured(GUIFrame::menublack, true);
        UI::addRect(foreground);

        if (hovered) {
            UI::setTextureless();
            UI::setColor(GUIFrame::hoverColor);
            UI::addRect(foreground);
            UI::setColor(Color::White);
        }
    }
}

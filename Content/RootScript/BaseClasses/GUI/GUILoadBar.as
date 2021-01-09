namespace GUILoadBar {
    shared Texture@ whiteBar = Texture::get("SCPCB/GFX/HUD/BlinkMeter");
}

shared class GUILoadBar : GUIComponent {
    private GUIFrame@ frame;

    void set_active(bool value) property override {
        GUIComponent::set_active(value);
        frame.active = value;
    }

    GUILoadBar(Menu@ menu, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY) {
        //@frame = GUIFrame(menu, x, y, width, height, alignment);
        super(menu, x, y, width, height, alignment);
    }

    void render() override {
        UI::setColor(Color::White);
        UI::setTextured(GUILoadBar::whiteBar, false);
        for (int i = 0; i < 5; i++) {
            UI::addRect(Rectanglef(x + i * 3.f, y, x + i * 3.f + 2.f, y + 3.5f));
        }
    }
}

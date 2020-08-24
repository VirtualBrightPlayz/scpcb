shared class GUIButton : GUIComponent {
    GUIButton(Menu@ menu, float x, float y, float width, float height, Alignment alignment = Alignment::CenterXY) {
        super(menu, x, y, width, height, alignment);
    }

    void update(const Vector2f&in mousePos) override {
        
    }
}

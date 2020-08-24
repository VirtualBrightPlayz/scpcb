shared class GUIText : GUIComponent {
    private bool centerX;
    private bool centerY;
    private bool localized;

    private Font@ font;

    private string text = "";
    private Vector2f pos;

    Color color = Color::White;
    float rotation = 0.f;
    float scale;

    void set_x(float value) property override {
        GUIComponent::set_x(value);
        updatePosition();
    }

    GUIText(float x, float y, Alignment alignment, bool centerX, bool centerY, bool localized, Font@ font) {
        super(x, y, 0.f, 0.f, alignment);
        this.centerX = centerX;
        this.centerY = centerY;
        this.localized = localized;

        @this.font = font;

        scale = 100.f / UI::getScreenHeight();
        
        updatePosition();
    }

    void updatePosition() {
        pos = Vector2f(x, y);
        if (centerX) {
            pos.x -= font.stringWidth(text, scale) / 2.f;
        }
        if (centerY) {
            pos.y -= font.getHeight(scale) / 1.5f;
        }
    }

    void setText(const string&in newText) {
        if (localized) {
            text = Local::getTxt(newText);
        } else {
            text = newText;
        }
        updatePosition();
    }

    void render() override {
        font.draw(text, pos, scale, rotation, color);
    }
}
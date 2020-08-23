namespace InvSlot {
    const float screenSize = 7.5;
}

shared class InvSlot : GUIComponent {
    Rectanglef background;
    Rectanglef top;
    Rectanglef bottom;
    Rectanglef left;
    Rectanglef right;

    GUIText@ text;

    Item@ item = null;

    bool hovered = false;
    bool equipped = false;

    InvSlot(float x, float y) {
        super(x, y, InvSlot::screenSize, InvSlot::screenSize);
        background = Rectanglef(x + GUIComponent::borderThickness, y + GUIComponent::borderThickness, x2 - GUIComponent::borderThickness, y2 - GUIComponent::borderThickness);
        updateRectangles(false);
        @text = GUIText(0.0f, 0.0f, Alignment::CenterXY, true, true, false, Font::large);
    }

    private void updateRectangles(bool doubled) {
        float thickness = GUIComponent::borderThickness * (doubled ? 2 : 1);
        top = Rectanglef(x, y, x2, y + thickness);
        bottom = Rectanglef(x, y2, x2, y2 - thickness);
        right = Rectanglef(x, y + thickness, x + thickness, y2 - thickness);
        left = Rectanglef(x2 - thickness, y, x2, y2);
    }

    bool hasItem() {
        return item != null;
    }

    void setItem(Item@ it) {
        @item = it;
        text.setText(it.name);
    }

    void update(const Vector2f&in mousePos) override {
        if (mousePos.x >= x && mousePos.y >= y && mousePos.x <= x2 && mousePos.y <= y2) {
            hovered = true;
            
            if (hasItem() && Input::Mouse1::getClickCount() >= 2) {
                if (item.onUse() != equipped) {
                    equipped = !equipped;
                    updateRectangles(equipped);
                }
            }
        } else {
            hovered = false;
        }
    }

    void render() override {
        if (hasItem()) {
            UI::setTextured(item.icon, false);
            UI::addRect(background);
        } else {
            UI::setTextured("SCPCB/GFX/Menu/menublack", true);
            UI::addRect(background);
        }
        if (hovered) {
            UI::setTextureless();
            UI::setColor(Color(0.5f, 0.5f, 0.5f, 0.5f));
            UI::addRect(background);
            UI::setColor(Color::White);
        }
        UI::setTextured("SCPCB/GFX/Menu/menuwhite", true);
        UI::addRect(top);
        UI::addRect(bottom);
        UI::addRect(right);
        UI::addRect(left);
    }
}

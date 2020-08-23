shared class InvSlot : GUIComponent {
    /*Rectanglef background;
    Rectanglef top;
    Rectanglef bottom;
    Rectanglef left;
    Rectanglef right;*/

    private void updateRectangles() {

    }

    InvSlot() {
        super(0.0, 0.0, 10.0, 10.0);
    }

    Item@ item = null;

    bool hasItem() {
        return item != null;
    }

    void setItem(Item@ it) {
        @item = it;
    }
}

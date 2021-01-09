namespace HUDMenu {
    shared HUDMenu@ instance = HUDMenu();
}

shared class HUDMenu : Menu {
    private GUILoadBar@ blinkMeter;

    HUDMenu() {
        super("hud");

        @blinkMeter = GUILoadBar(this, 0, 0, 10, 10);
    }
}

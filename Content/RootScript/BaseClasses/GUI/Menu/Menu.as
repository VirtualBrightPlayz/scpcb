shared abstract class Menu {
    private string _type;
    string type {
        get { return _type; }
    }

    Menu(const string&in type) {
        _type = type;
    }

    void onActivate() {}
    void onDeactivate() {}

    // Returns whether this menu should detach.
    bool onEscapeHit() { return false; }

    // Returns whether this menu should detach.
    bool update(const Vector2f&in mousePosition, const Vector2f&in mouseWheelDelta) { return false; }
    void render() {}
}

shared class MenuManager {
    Menu@ currMenu;

    private Inventory@ inventory;

    MenuManager(Inventory@ inventory) {
        @this.inventory = inventory;
    }

    void deactivateMenu(Menu@ mu) {
        if (@currMenu == @mu) {
            @currMenu = null;
            mu.onDeactivate();
        } else {
            // Throw
        }
        World::paused = false;
    }

    void activateMenu(Menu@ mu) {
        if (currMenu == null) {
            @currMenu = mu;
            currMenu.onActivate();
        } else {
            // Throw
        }
        World::paused = true;
    }

    void update(const Vector2f&in mousePosition, const Vector2f&in mouseWheelDelta) {
        if (currMenu == null) {
            if ((Input::getHit() & Input::Inventory) != 0) {
                activateMenu(inventory);
            }
        } else {
            if (currMenu.update(mousePosition, mouseWheelDelta)) {
                deactivateMenu(currMenu);
            }
        }
    }

    void render() {
        if (currMenu != null) {
            currMenu.render();
        }
    }
}

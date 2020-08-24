shared abstract class Menu {
    private string _type;
    string type {
        get { return _type; }
    }

    array<GUIComponent@> components;

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
            for (int i = 0; i < mu.components.length(); i++) {
                mu.components[i].onClose();
            }
        } else {
            // Throw
        }
        World::paused = false;
    }

    void activateMenu(Menu@ mu) {
        if (currMenu == null) {
            @currMenu = mu;
            mu.onActivate();
            for (int i = 0; i < mu.components.length(); i++) {
                mu.components[i].onOpen();
            }
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
            } else {
                for (int i = 0; i < currMenu.components.length(); i++) {
                    currMenu.components[i].update(mousePosition);
                }
            }
        }
    }

    void render() {
        if (currMenu != null) {
            currMenu.render();
            for (int i = 0; i < currMenu.components.length(); i++) {
                if (currMenu.components[i].visible) {
                    currMenu.components[i].render();
                }
            }
        }
    }
}

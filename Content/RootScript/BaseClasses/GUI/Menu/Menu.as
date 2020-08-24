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
    bool update() { return false; }
    void render() {}
}

shared class MenuManager {
    Menu@ currMenu;

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

    void update() {
        if (currMenu == null) {
            if (Input::isEscapeHit()) {
                activateMenu(pauseMenu);
            } else if (Input::getHit() & Input::Inventory != 0) {
                activateMenu(inventoryMenu);
            }
        } else {
            if (Input::isEscapeHit() && currMenu.onEscapeHit() || currMenu.update()) {
                deactivateMenu(currMenu);
            } else {
                for (int i = 0; i < currMenu.components.length(); i++) {
                    if (currMenu.components[i].active) {
                        currMenu.components[i].update();
                    }
                }
            }
        }
    }

    void render() {
        if (currMenu != null) {
            currMenu.render();
            for (int i = 0; i < currMenu.components.length(); i++) {
                if (currMenu.components[i].active) {
                    currMenu.components[i].render();
                }
            }
        }
    }
}

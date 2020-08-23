shared class Item {
    private Model@ model;

    private Pickable@ pickable;
    private bool picked;

    Vector3f position {
        get { return model.position; }
        set { model.position = value;
            pickable.position = value; }
    }

    Vector3f rotation {
        get { return model.rotation; }
        set { model.rotation = value; }
    }

    Vector3f scale {
        get { return model.scale; }
        set { model.scale = value; }
    }

    Item() {
        @model = Model::create("SCPCB/GFX/Items/Firstaid/firstaid.fbx");
        @pickable = Pickable();
        Pickable::activatePickable(pickable);
    }

    ~Item() {
        Model::destroy(model);
        Pickable::deactivatePickable(pickable);
    }

    // Returns whether or not the item was equipped.
    bool onUse() { return false; }
    void onDrop() {}
    void onPick() {}
    // Returns whether or not the item can be picked up.
    bool canPick() { return true; }

    void update() {
        if (pickable.getPicked()) {
            if (canPick()) {
                if (inventory.addItem(this)) {
                    onPick();
                    picked = true;
                    Pickable::deactivatePickable(pickable);
                } else {
                    Msg::set("Inventory full!");
                }
            }
        }
    }

    void render() {
        if (!picked) {
            model.render();
        }
    }
}

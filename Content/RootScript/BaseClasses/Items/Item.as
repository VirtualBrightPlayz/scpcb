namespace Item {
    shared array<ItemTemplate> templates; // TODO: Replace with map.
    shared void register(const string&in name, const string&in model, float iconScl, const Vector3f&in iconRot, const Vector2f&in iconPos, float scale = 1.0) {
        ItemTemplate it;
        it.name = name;
        it.localName = Local::getTxt("Items." + name + ".Name");
        it.model = model;
        @it.icon = ModelImageGenerator::generate(model, iconScl, iconRot, iconPos);
        templates.insertLast(it);
    }
    shared array<Item@> items;
    shared void updateAll() {
        for (int i = 0; i < items.length(); i++) {
            items[i].update();
        }
    }
    shared void renderAll() {
        for (int i = 0; i < items.length(); i++) {
            items[i].render();
        }
    }
    shared Item@ spawn(const string&in name, const Vector3f&in position) {
        Debug::log("Creating item " + name);
        Reflection<Item> reflection;
        for (int i = 0; i < templates.length(); i++) {
            if (templates[i].name == name) {
                Debug::log("Found template!");
                reflection.setConstructorArgument(0, templates[i]);
                break;
            }
        }
        Item@ result = reflection.callConstructor(name);
        if (result == null) {
            Debug::error(@result);
        }
        result.position = position;
        items.insertLast(result);
        return result;
    }
}

shared abstract class Item {
    private ItemTemplate it;

    string name {
        get { return it.localName; }
    }

    Texture@ icon {
        get { return it.icon; }
    }

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

    Item(ItemTemplate it) {
        Debug::log(it.model);
        this.it = it;
        @model = Model::create(it.model);
        @pickable = Pickable();
        Pickable::activatePickable(pickable);
    }

    ~Item() {
        Model::destroy(model);
        Pickable::deactivatePickable(pickable);
    }

    // Returns whether or not the item is now equipped.
    bool onUse() { return false; }
    void onDrop() {}
    void onPick() {}
    // Returns whether or not the item can be picked up.
    bool canPick() { return true; }

    void update() {
        if (pickable.getPicked()) {
            if (canPick()) {
                if (InventoryMenu::instance.addItem(this)) {
                    onPick();
                    picked = true;
                    Pickable::deactivatePickable(pickable);
                } else {
                    //Msg::set("Inventory full!");
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

shared class ItemTemplate {
    string model;
    string name;
    string localName;
    Texture@ icon;
}

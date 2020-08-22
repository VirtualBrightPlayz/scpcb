shared class Item {
    Model@ model;

    private Pickable pickable;
    private bool picked;

    Item() {
        Debug::log("CONSTRUCTING AS ITEM");
        @model = Model::create("SCPCB/GFX/Items/Firstaid/firstaid.fbx");
        model.position = Vector3f(0, 10, 20);
    }

    ~Item() {
        Debug::log("DESTRUCTING AS ITEM");
        Model::destroy(model);
    }

    void update() {

    }

    void render() {
        model.render();
    }
}

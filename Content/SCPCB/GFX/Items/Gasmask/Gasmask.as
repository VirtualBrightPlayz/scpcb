shared class Gasmask : Item {
    bool onUse() override {
        Debug::log("AAAAA");
        return true;
    }

    Gasmask(ItemTemplate it) {
        Debug::log("Gasmask constructor");
        super(it);
    }
}

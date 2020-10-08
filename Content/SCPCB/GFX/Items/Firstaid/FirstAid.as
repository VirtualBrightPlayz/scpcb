shared class FirstAid : Item {
    void onPick() override {
        Debug::log("POG");
    }
    
    FirstAid(ItemTemplate it) {
        Debug::log("FirstAid constructor");
        super(it);
    }
}

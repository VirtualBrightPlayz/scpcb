shared Inventory@ inventory = Inventory(10);

shared class Inventory : Menu {
    private array<InvSlot@> slots;

    Inventory(int size) {
        super("inventory");

        const int rows = Math::ceil(size / 6.0);
        if (size % rows != 0) {
            // throw("Invalid inventory size"); // TODO
        }
        const int itemsPerRow = size / rows;

        slots = array<InvSlot@>(size);

        float screenX; float screenY = -(rows * 2 - 1) * InvSlot::screenSize / 2;
        for (int y = 0; y < rows; y++) {
            screenX = -(itemsPerRow * 2 - 1) * InvSlot::screenSize / 2;
            for (int x = 0; x < itemsPerRow; x++) {
                @slots[x + y * itemsPerRow] = InvSlot(this, screenX, screenY);
                screenX += InvSlot::screenSize * 2;
            }
            screenY += InvSlot::screenSize * 2;
        }
    }

    bool addItem(Item@ it) {
        for (int i = 0; i < slots.length(); i++) {
            if (slots[i].item == null) {
                @slots[i].item = it;
                return true;
            }
        }
        return false;
    }

    void onDeactivate() override {
        for (int i = 0; i < slots.length(); i++) {
            slots[i].onClose();
        }
    }

    bool onEscapeHit() override {
        return true;
    }

    bool update(const Vector2f&in mousePos, const Vector2f&in mouseWheelDelta) override {
        return (Input::getHit() & Input::Inventory) != 0;
    }
}

shared class Inventory {
    private array<InvSlot> slots;

    Inventory(int size) {
        int rows = Math::ceil(size / 6.0);
        slots = array<InvSlot>(size);
    }

    bool addItem(Item@ it) {
        for (int i = 0; i < slots.length(); i++) {
            if (!slots[i].hasItem()) {
                slots[i].setItem(it);
                return true;
            }
        }
        return false;
    }
}

shared Inventory@ inventory = Inventory(10);

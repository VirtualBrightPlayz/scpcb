void myEvent(float deltaTime) {
    RM2::load("GFX/Map/Rooms/EntranceZone/hll_plain_2/hll_plain_2.rm2");
}

void test() {
    PerTick::register(myEvent);
}
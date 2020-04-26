EntranceZone@ entranceZone;

external shared enum RoomType;

void main() {
    @entranceZone = EntranceZone();
    entranceZone.generate();
    entranceZone.registerRoom("hll_plain_4", Room4, hll_plain_4::construct);
    PerFrame::register(render);
}

void render(float interpolation) {
    entranceZone.render(interpolation);
}
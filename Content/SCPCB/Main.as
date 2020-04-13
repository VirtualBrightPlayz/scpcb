EntranceZone@ entranceZone;

void main() {
    @entranceZone = EntranceZone();
    entranceZone.loadMeshes();
    entranceZone.genLayout();
    PerFrame::register(render);
}

void render(float interpolation) {
    entranceZone.render(interpolation);
}
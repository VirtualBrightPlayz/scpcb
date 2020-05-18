serialize EntranceZone@ entranceZone;
serialize int why = 17;
serialize Vector3f whVy = Vector3f(17, 14, 14);
serialize string uh = "aaah";
serialize Matrix4x4f worldMatrix = Matrix4x4f::constructWorldMat(Vector3f(0, 0, 0), Vector3f(1, 1, 1), Vector3f(0, 0, 0));

external shared enum RoomType;

void main() {
    @entranceZone = EntranceZone();
    entranceZone.registerRoom("hll_plain_4", Room4, hll_plain_4::construct);
    entranceZone.generate();
    PerFrame::register(render);
}

void render(float interpolation) {
    entranceZone.render(interpolation);
}
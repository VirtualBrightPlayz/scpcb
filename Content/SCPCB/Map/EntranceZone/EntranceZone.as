external shared class Zone;

class EntranceZone : Zone {
    private array<array<int>> layout;

    void loadMeshes() {
        meshes = array<RM2@>();
        meshes.insertLast(RM2::load("SCPCB/Map/EntranceZone/hll_plain_4/hll_plain_4.rm2"));
    }
}

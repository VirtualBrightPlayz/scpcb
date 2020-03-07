class EntranceZone {
    private nonserialize int e;
    private nonserialize array<RM2@> meshes;
    private array<array<int>> layout;

    void loadMeshes() {
        meshes = array<RM2@>();
        meshes.insertLast(RM2::load("GFX/Map/Rooms/EntranceZone/hll_plain_4/hll_plain_4.rm2"));
    }

    void genLayout() {
        layout = array<array<int>>(10, array<int>(10, 0));
    }

    void render() {
        for (int x=0;x<10;x++) {
            for (int y=0;y<10;y++) {
                Vector3f pos = Vector3f(x * 50,0,y * 50);
                Vector3f scale = Vector3f(0.05,0.05,0.05);
                Vector3f rotation = Vector3f(0,0,0);
                Matrix4x4f matrix = Matrix4x4f::constructWorldMat(pos, scale, rotation);
                meshes[layout[x][y]].render(matrix);
            }
        }
    }
}


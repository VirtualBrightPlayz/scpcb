shared abstract class Zone {
    protected nonserialize array<RM2@> meshes;
    protected array<array<Room@>> rooms;

    void loadMeshes() {}
    void genLayout() {}

    void update(float deltaTime) {
        for (int x=0;x<rooms.length();x++) {
            for (int y=0;y<rooms[x].length();y++) {
                rooms[x][y].update(deltaTime);
            }
        }
    }

    void render(float interpolation) {
        for (int x=0;x<rooms.length();x++) {
            for (int y=0;y<rooms[x].length();y++) {
                rooms[x][y].render(interpolation);
            }
        }
    }
}

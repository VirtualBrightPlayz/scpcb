shared abstract class Zone {
    protected array<MapGenEntry> mapGenEntries;
    protected array<array<Room@>> rooms;

    void registerRoom(const string name, const RoomType type, RoomConstructor@ constructor) {
        MapGenEntry entry;
        entry.roomName = name;
        entry.roomType = type;
        @entry.roomConstructor = constructor;
        mapGenEntries.insertLast(entry);
    }

    RM2@ getMesh(const string name) {
        for (int i=0;i<mapGenEntries.length();i++) {
            if (mapGenEntries[i].roomName == name) {
                if (mapGenEntries[i].mesh == null) {
                    //TODO: remove hardcoding here
                    @mapGenEntries[i].mesh = RM2::load("SCPCB/Map/EntranceZone/" + mapGenEntries[i].roomName + "/" + mapGenEntries[i].roomName + ".rm2");
                }
                return mapGenEntries[i].mesh;
            }
        }
        return null;
    }

    void generate() {}

    void update(float deltaTime) {
        for (int x=0;x<rooms.length();x++) {
            for (int y=0;y<rooms[x].length();y++) {
                if (rooms[x][y] == null) { continue; }
                rooms[x][y].update(deltaTime);
            }
        }
    }

    void render(float interpolation) {
        testCounter++;
        if (testCounter > 60000) {
            @test_shared_global = null;
        }
        for (int x=0;x<rooms.length();x++) {
            for (int y=0;y<rooms[x].length();y++) {
                if (rooms[x][y] == null) { continue; }
                rooms[x][y].render(interpolation);
            }
        }
    }
}

shared int testCounter = 0;
shared Zone@ test_shared_global;

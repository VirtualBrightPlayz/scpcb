shared enum RoomType {
    Room1,
    Room2,
    Room2C,
    Room3,
    Room4
}

shared funcdef Room@ RoomConstructor(Zone@);

shared class MapGenEntry {
    RoomType roomType;
    string roomName;
    RoomConstructor@ roomConstructor;
    RM2@ mesh;
}

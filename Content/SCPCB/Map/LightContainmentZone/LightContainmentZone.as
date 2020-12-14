class LightContainmentZone : Zone {
    //TODO: refactor
    array<array<array<Collision::Instance>>> collisionInstances;
    private int mapDim = 19;

    void generate() {

        rooms = array<array<Room@>>(mapDim);
        collisionInstances = array<array<array<Collision::Instance>>>(mapDim);
        array<array<int>> layout = array<array<int>>(mapDim);

        for (int x = 0; x < mapDim; x++) {
            rooms[x] = array<Room@>(mapDim);
            collisionInstances[x] = array<array<Collision::Instance>>(mapDim);
            layout[x] = array<int>(mapDim);
        }

        //4x4 squares, offset 1 slot from 0,0
        const int rectWidth = 3;
        const int rectHeight = 3;
        for (int y = 0; y < mapDim; y++) {
            for (int x = 0; x < mapDim; x++) {
                if ((x % rectWidth==1) || (y % rectHeight==1)) {
                    if ((x >= rectWidth && x < mapDim-rectWidth) || (y >= rectHeight && y < mapDim-rectHeight)) {
                        layout[x][y] = 1;
                    }
                }
            }
        }

        printRooms(layout);

        layout = determineRoomTypes(layout);

        printRooms(layout);

        
        Debug::log("AAAAAAAA");
    }

    void printRooms(array<array<int>> layout) {
        Debug::log("Rooms:");
        for (int y = 0; y < mapDim; y++) {
            string t = "";
            for (int x = 0; x < mapDim; x++) {
                if (layout[x][y] == 0) {
                    t += "  ";
                } else {
                    t += toString(layout[x][y]) + " ";
                }
            }
            Debug::log(t);
        }
    }

    array<array<int>> determineRoomTypes(array<array<int>> layout) {
        for (int x = 0; x < mapDim; x++) {
            for (int y = 0; y < mapDim; y++) {
                if (layout[x][y] != 0) {
                    int horNeighbors = 0;
                    if (x > 0 && layout[x - 1][y] != 0) {
                        horNeighbors++;
                    }
                    if (x < mapDim - 1 && layout[x + 1][y] != 0) {
                        horNeighbors++;
                    }
                    int verNeighbors = 0;
                    if (y > 0 && layout[x][y - 1] != 0) {
                        verNeighbors++;
                    }
                    if (y < mapDim - 1 && layout[x][y + 1] != 0) {
                        verNeighbors++;
                    }
                    if (horNeighbors + verNeighbors == 4) {
                        layout[x][y] = Room4;
                    } else if (horNeighbors + verNeighbors == 3) {
                        layout[x][y] = Room3;
                    } else if (horNeighbors == 2 ^ verNeighbors == 2) {
                        layout[x][y] = Room2;
                    } else if (horNeighbors == 1 && verNeighbors == 1) {
                        layout[x][y] = Room2C;
                    } else if (horNeighbors + verNeighbors == 1) {
                        layout[x][y] = Room1;
                    } else {
                        layout[x][y] = 0;
                    }
                }
            }
        }
        return layout;
    }

    void update(float deltaTime) {
        Zone::update(deltaTime);
        for (int x=0;x<mapDim;x++) {
            for (int y=0;y<mapDim;y++) {
                for (int i=0;i<collisionInstances[x][y].length();i++) {
                    Matrix4x4f worldMatrix = rooms[x][y].worldMatrix;
                    testCollCollection.updateInstance(collisionInstances[x][y][i], worldMatrix);
                }
            }
        }
    }
}

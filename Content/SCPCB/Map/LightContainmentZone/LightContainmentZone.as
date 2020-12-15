class LightContainmentZone : Zone {
    //TODO: refactor
    array<array<array<Collision::Instance>>> collisionInstances;
    private int mapDim = 19;

    void generate() {
        zoneName = "LightContainmentZone";

        Random@ random = Random(666);

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
                if ((x % rectWidth == 1) || (y % rectHeight == 1)) {
                    if ((x >= rectWidth && x < mapDim-rectWidth) || (y >= rectHeight && y < mapDim-rectHeight)) {
                        layout[x][y] = 1;
                    }
                }
            }
        }

        determineRoomTypes(@layout);
        printRooms(layout);

        //shift some horizontal corridors
        int shift = 0;
        int nonShiftStreak = random.nextInt(6);
        for (int y = 1; y < mapDim - 1; y++) {
            for (int x = 0; x < mapDim - 1; x++) {
                if (y > 6 || x > 6) {
                    if ((y % rectHeight == 1) && layout[x][y] == Room2) {
                        shift = random.nextBool() ? 1 : 0;
                        if (nonShiftStreak == 0) {
                            shift = 0;
                        }
                        if (nonShiftStreak > 5) {
                            shift = 1;
                        }
                        if ((x/rectWidth) % 2 == 1) {
                            shift = -shift;
                        }
                        if (shift != 0) {
                            for (int i = 0; i < rectWidth-1; i++) {
                                layout[x + i][y] = 0;
                                layout[x + i][y + shift] = Room2;
                            }
                            nonShiftStreak = 0;
                        } else {
                            nonShiftStreak++;
                        }
                        x = x + rectWidth - 1;
                    }
                }
            }
        }

        determineRoomTypes(@layout);
        printRooms(layout);

        int roomAbove = 0;
        int roomBelow = 0;
        for (int y = 2; y <= mapDim-4; y++) {
            for (int x = 0; x <= mapDim-1; x++) {
                if ((((x/rectWidth) % 2) == (random.nextBool() ? 1 : 0)) && (layout[x][y]==Room2)) {
                    roomAbove = layout[x][y-1];
                    roomBelow = layout[x][y+1];
                    if (((roomAbove >= Room2) && (roomBelow >= Room2)) && ((roomAbove+roomBelow)>(Room2+Room3))) {
                        layout[x][y]=0;
                    }
                }
            }
        }

        determineRoomTypes(@layout);
        printRooms(layout);

        for (int x = 0; x < mapDim; x++) {
            for (int y = 0; y < mapDim; y++) {
                int angle = 0;
                if (layout[x][y] == Room4) {
                    @rooms[x][y] = createRoom("hll_plain_4_empty");
                    angle = random.nextInt(4) * 90;
                } else if (layout[x][y] == Room3) {
                    @rooms[x][y] = createRoom("hll_plain_3_empty");
                    if (x > 0 && layout[x-1][y] == 0) {
                        angle = 0;
                    } else if (y > 0 && layout[x][y-1] == 0) {
                        angle = 90;
                    } else if (x < mapDim-1 && layout[x+1][y] == 0) {
                        angle = 180;
                    } else {
                        angle = 270;
                    }
                } else {
                    continue;
                }

                Debug::log("AWESOME ROOM");

                rooms[x][y].position = Vector3f(x*204.8,0,y*204.8);
                rooms[x][y].rotation = angle;
                
                RM2@ mesh = rooms[x][y].mesh;
                collisionInstances[x][y] = array<Collision::Instance>(mesh.collisionMeshCount());
                for (int i=0; i < mesh.collisionMeshCount(); i++) {
                    collisionInstances[x][y][i] = testCollCollection.addInstance(mesh.getCollisionMesh(i), rooms[x][y].worldMatrix);
                }
            }
        }
        
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

    void determineRoomTypes(array<array<int>>@ layout) {
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
                    } else if ((horNeighbors == 2) || (verNeighbors == 2)) { // TODO: Should be xor
                        layout[x][y] = Room2;
                    } else if ((horNeighbors == 1) && (verNeighbors == 1)) {
                        layout[x][y] = Room2C;
                    } else if (horNeighbors + verNeighbors == 1) {
                        layout[x][y] = Room1;
                    } else {
                        layout[x][y] = 0;
                    }
                }
            }
        }
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

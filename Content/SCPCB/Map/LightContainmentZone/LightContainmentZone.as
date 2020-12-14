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

        determineRoomTypes(@layout);

        for (int y = 0; y < mapDim; y++) {
            string t = "";
            for (int x = 0; x < mapDim; x++) {
                t += toString(layout[x][y]);
                t += " ";
            }
            Debug::log(t);
        }


        
        Debug::log("AAAAAAAA");
    }

    void determineRoomTypes(array<array<int>>@ layout) {
        for (int x = 0; x < mapDim; x++) {
            for (int y = 0; y < mapDim; y++) {
                int horNeighbors = 0;
                if (x > 0) {
                    horNeighbors += layout[x - 1][y];
                }
                if (x < mapDim - 1) {
                    horNeighbors += layout[x + 1][y];
                }
                int verNeighbors = 0;
                if (y > 0) {
                    verNeighbors += layout[x][y - 1];
                }
                if (y < mapDim - 1) {
                    verNeighbors += layout[x][y + 1];
                }
                if (horNeighbors + verNeighbors == 4) {
                    layout[x][y] = Room4;
                } else if (horNeighbors + verNeighbors == 3) {
                    layout[x][y] = Room3;
                } else if (horNeighbors == 2 || verNeighbors == 2) {
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
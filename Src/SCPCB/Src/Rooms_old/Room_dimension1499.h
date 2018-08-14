#ifndef ROOM_DIMENSION1499_H_INCLUDED
#define ROOM_DIMENSION1499_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct ChunkPart {
private:
    static std::vector<ChunkPart*> list;

public:
    ChunkPart();
    ~ChunkPart();
    static int getListSize();
    static ChunkPart* getObject(int index);

    int amount;
    int obj[128];
    float randomYaw[128];
    int id;
};

struct Chunk {
private:
    static std::vector<Chunk*> list;

public:
    Chunk();
    ~Chunk();
    static int getListSize();
    static Chunk* getObject(int index);

    int obj[128];
    float x;
    float z;
    float y;
    int amount;
    //Field debugobj%
};

}
#endif // ROOM_DIMENSION1499_H_INCLUDED

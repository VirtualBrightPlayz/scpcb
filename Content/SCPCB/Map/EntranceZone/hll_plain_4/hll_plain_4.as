external shared class Room;

class hll_plain_4 : Room {
    hll_plain_4(Zone@ zone) {
        @mesh = zone.getMesh("hll_plain_4");
    }
}

namespace hll_plain_4 {
    Room@ construct(Zone@ zone) {
        return hll_plain_4(zone);
    }    
}

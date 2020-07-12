class hll_plain_2 : Room {
    hll_plain_2(Zone@ zone) {
        @_mesh = zone.getMesh("hll_plain_2");
    }
}

namespace hll_plain_2 {
    Room@ construct(Zone@ zone) {
        return hll_plain_2(zone);
    }    
}

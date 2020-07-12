class hll_plain_4_a : Room {
    hll_plain_4_a(Zone@ zone) {
        @_mesh = zone.getMesh("hll_plain_4_a");
    }
}

namespace hll_plain_4_a {
    Room@ construct(Zone@ zone) {
        return hll_plain_4_a(zone);
    }    
}

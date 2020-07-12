class hll_plain_2_a : Room {
    hll_plain_2_a(Zone@ zone) {
        @_mesh = zone.getMesh("hll_plain_2_a");
    }
}

namespace hll_plain_2_a {
    Room@ construct(Zone@ zone) {
        return hll_plain_2_a(zone);
    }    
}

#include <PGE/Init/Init.h>

#include "World/World.h"

int PGE::Init::main(const std::vector<PGE::String>& args) {
    World world;
    while (world.run());
    return 0;
}

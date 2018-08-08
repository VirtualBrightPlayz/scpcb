#ifndef SKYBOX_H_INCLUDED
#define SKYBOX_H_INCLUDED
#include <vector>

namespace CBN {

// Functions.
class MeshModel* sky_CreateSky(String filename, class Object* parent = nullptr);

void UpdateSky();

void Update1499Sky();

}
#endif // SKYBOX_H_INCLUDED

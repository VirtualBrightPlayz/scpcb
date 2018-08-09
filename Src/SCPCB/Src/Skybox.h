#ifndef SKYBOX_H_INCLUDED
#define SKYBOX_H_INCLUDED
#include <vector>

class Object;

namespace CBN {

// Functions.
MeshModel* sky_CreateSky(String filename, Object* parent = nullptr);

void UpdateSky();

void Update1499Sky();

}
#endif // SKYBOX_H_INCLUDED

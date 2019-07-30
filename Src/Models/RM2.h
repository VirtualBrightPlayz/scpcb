#ifndef RM2_H_INCLUDED
#define RM2_H_INCLUDED

#include <vector>
#include <Mesh/Mesh.h>
#include <Graphics/Graphics.h>
#include <Texture/Texture.h>
#include <Material/Material.h>
#include <Misc/FileName.h>
#include <Color/Color.h>
#include "../Collision/CollisionMesh.h"
#include "../Graphics/GfxResManager.h"

struct PointLight {
    PGE::Vector3f position;
    PGE::Vector2f range;
    PGE::Color color;
};

struct SpotLight {
    PGE::Vector3f position;
    PGE::Vector2f range;
    PGE::Vector3f direction;
    PGE::Vector2f cone;
    PGE::Color color;
};

struct Waypoint {
    PGE::Vector3f position;
    std::vector<int> connections;
};

class RM2 {
    private:
        std::vector<PGE::Material*> materials;
        std::vector<PGE::Texture*> textures;
        PGE::Shader* shader;

        std::vector<PGE::Mesh*> opaqueMeshes;
        std::vector<PGE::Mesh*> alphaMeshes;

        std::vector<CollisionMesh> collisionMeshes;
        std::vector<PointLight> pointLights;
        std::vector<SpotLight> spotLights;

    public:
        RM2(GfxResManager* shdrMgr, PGE::FileName filename);
        void render();
};

#endif

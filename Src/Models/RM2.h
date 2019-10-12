#ifndef RM2_H_INCLUDED
#define RM2_H_INCLUDED

#include <vector>
#include <Math/Matrix.h>
#include <Mesh/Mesh.h>
#include <Graphics/Graphics.h>
#include <Texture/Texture.h>
#include <Material/Material.h>
#include <Misc/FileName.h>
#include <Color/Color.h>
#include "../Collision/CollisionMesh.h"
#include "../Graphics/GraphicsResources.h"
#include "../Graphics/Camera.h"

struct PointLight {
    PGE::Vector3f position;
    PGE::Vector2f range;
    PGE::Color color;
};

struct Spotlight {
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

enum class RM2Error {
    None = 0,
    InvalidHeader,
    UnexpectedChunk,
    ReadPastEof
};

class RM2 {
    private:
        const static PGE::FileName opaqueShaderPath;
        const static PGE::FileName alphaShaderPath;

        std::vector<PGE::Material*> materials;
        std::vector<PGE::Texture*> textures;
        GraphicsResources* graphicsResources;

        PGE::Shader* opaqueShader;
        PGE::Shader::Constant* opaqueModelMatrixConstant;

        PGE::Shader* alphaShader;
        PGE::Shader::Constant* alphaModelMatrixConstant;

        std::vector<PGE::Mesh*> opaqueMeshes;
        std::vector<PGE::Mesh*> alphaMeshes;

        std::vector<CollisionMesh*> collisionMeshes;
        std::vector<PointLight> pointLights;
        std::vector<Spotlight> spotlights;
        std::vector<Waypoint> waypoints;

        RM2Error error;

        static const PGE::String texturePath;

    public:
        RM2(GraphicsResources* gfxMgr, PGE::FileName filename);
        //~RM2();

        //RM2Error getError() const;

        void render(PGE::Matrix4x4f modelMatrix);

        //const std::vector<CollisionMesh*>& getCollisionMeshes() const;
        //const std::vector<PointLight>& getPointLights() const;
        //const std::vector<Spotlight>& getSpotlights() const;
        //const std::vector<Waypoint>& getWaypoints() const;
};

#endif

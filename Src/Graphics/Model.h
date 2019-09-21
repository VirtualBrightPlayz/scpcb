#ifndef MODEL_H_INCLUDED
#define MODEL_H_INCLUDED

#include <Misc/String.h>

#include <Graphics/Graphics.h>
#include <Texture/Texture.h>
#include <Mesh/Mesh.h>
#include <Shader/Shader.h>
#include <Material/Material.h>

class GraphicsResources;

class Model {
    public:
        Model(GraphicsResources* gfx, const PGE::String& filename);
        ~Model();
    
        std::vector<PGE::Mesh*> getMeshes() const;
    private:
        std::vector<PGE::Mesh*> meshes;
        std::vector<PGE::Texture*> textures;
        std::vector<PGE::Material*> materials;
    
        GraphicsResources* gfxRes;
        PGE::Shader* shader;
};

#endif // MODEL_H_INCLUDED

#ifndef CBN_MODEL_H_INCLUDED
#define CBN_MODEL_H_INCLUDED

#include <Misc/String.h>

#include <Graphics/Graphics.h>
#include <Texture/Texture.h>
#include <Mesh/Mesh.h>
#include <Shader/Shader.h>
#include <Material/Material.h>

namespace CBN {

class Model {
    public:
        Model(PGE::Graphics* graphcis,PGE::String filename,PGE::Shader* shader);
        std::vector<PGE::Mesh*> getMeshes() const;
    private:
        std::vector<PGE::Mesh*> meshes;
        std::vector<PGE::Texture*> textures;
        std::vector<PGE::Material*> materials;
};

}

#endif

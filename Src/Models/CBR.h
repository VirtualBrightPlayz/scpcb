#ifndef CBR_H_INCLUDED
#define CBR_H_INCLUDED

#include <Mesh/Mesh.h>
#include <Misc/String.h>
#include <Math/Matrix.h>
#include <Shader/Shader.h>

class GraphicsResources;

class CBR {
    private:
        std::vector<PGE::Mesh*> meshes;

        PGE::Shader* shader;
        PGE::Shader::Constant* shaderModelMatrixConstant;

    public:
        CBR(GraphicsResources* gr, const PGE::String& filename);
        ~CBR();

        void render(const PGE::Matrix4x4f& modelMatrix);
};

#endif // CBR_H_INCLUDED

#ifndef MODELMANAGER_H_DEFINED
#define MODELMANAGER_H_DEFINED

#include <Misc/FilePath.h>
#include <Math/Matrix.h>
#include <Mesh/Mesh.h>

class GraphicsResources;

class Model {
    private:
        PGE::Mesh** meshes;
        int meshCount;

        PGE::Shader::Constant* modelMatrix;

        Model(); // We don't want this.
        
    public:
        Model(GraphicsResources* gr, const PGE::FilePath& filename);
        
        void render(const PGE::Matrix4x4f& modelMatrix) const;
};

class ModelInstance {
    private:
        Model* model;

        PGE::Matrix4x4f modelMatrix;

        PGE::Vector3f position = PGE::Vector3f::zero;
        PGE::Vector3f rotation = PGE::Vector3f::zero;
        PGE::Vector3f scale = PGE::Vector3f::one;

        ModelInstance(); // We don't want this.

        void recomputeModelMatrix();

    public:
        ModelInstance(Model* model);

        void setPosition(const PGE::Vector3f& pos);
        void setRotation(const PGE::Vector3f& rot);
        void setScale(const PGE::Vector3f& scl);

        const PGE::Vector3f& getPosition() const;
        const PGE::Vector3f& getRotation() const;
        const PGE::Vector3f& getScale() const;

        Model* getModel() const;

        void render() const;
};

#endif // MODELMANAGER_H_DEFINED

#ifndef MODELIMAGEGENERATOR_H_INCLUDED
#define MODELIMAGEGENERATOR_H_INCLUDED

#include <Graphics/Graphics.h>

class GraphicsResources;
class Camera;

class ModelImageGenerator {
    private:
        PGE::Graphics* graphics;
        GraphicsResources* gfxRes;

        int texSize;
        PGE::Rectanglei oldViewport;
        Camera* cam;

        bool initialized;
        
    public:
        ModelImageGenerator(PGE::Graphics* graphics, GraphicsResources* gfxRes);
        ~ModelImageGenerator();

        void initialize(int texSize);
        void deinitialize();
        bool getInitialized() const;

        PGE::Texture* generate(const PGE::String& model, float scale, const PGE::Vector3f& rotation, PGE::Vector2f position);
};

#endif // MODELIMAGEGENERATOR_H_INCLUDED

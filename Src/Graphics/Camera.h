#ifndef Camera_H_INCLUDED
#define Camera_H_INCLUDED

#include <Math/Matrix.h>
#include "../Wrap/Graphics.h"

class Camera {
private:
    PGE::Matrix4x4f viewMatrix;
    PGE::Matrix4x4f projectionMatrix;

    Graphics gfx;

public:
    Camera(Graphics& gfx);

    const PGE::Matrix4x4f& getViewMatrix() const;
    const PGE::Matrix4x4f& getProjectionMatrix() const;
};

#endif // Camera_H_INCLUDED

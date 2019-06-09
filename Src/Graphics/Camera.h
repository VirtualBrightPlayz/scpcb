#ifndef Camera_H_INCLUDED
#define Camera_H_INCLUDED

#include <Math/Matrix.h>
#include "../Wrap/Graphics.h"

class Camera {
private:
    PGE::Matrix4x4f viewMat;
    PGE::Matrix4x4f projectionMat;

    Graphics gfx;

public:
    Camera(Graphics gfx);
};

#endif // Camera_H_INCLUDED

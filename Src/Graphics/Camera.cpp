#include <Math/Rectangle.h>
#include "Camera.h"

Camera::Camera(Graphics gfx) : gfx(gfx) {
    PGE::Vector3f position = PGE::Vector3f::zero;
    PGE::Vector3f lookAt = PGE::Vector3f(0.0f, 0.0f, 1.0f);
    PGE::Vector3f upDir = PGE::Vector3f(0.0f, 0.1f, 0.0f);

    viewMat = PGE::Matrix4x4f::constructViewMat(position, lookAt, upDir);

    float aspectRatio = (float)gfx->getViewport().width() /  gfx->getViewport().height();
    float farClippingPlane = 10.f;
    float nearClippingPlane = 0.01f;

    projectionMat = PGE::Matrix4x4f::identity;
    projectionMat.elements[0][0] = -1.0f / aspectRatio;
    projectionMat.elements[1][1] = 1.f;
    projectionMat.elements[2][2] = farClippingPlane / (nearClippingPlane - farClippingPlane);
    projectionMat.elements[2][3] = -1.f;
    projectionMat.elements[3][2] = (nearClippingPlane * farClippingPlane / (nearClippingPlane - farClippingPlane));
}

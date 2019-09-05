#include <Math/Rectangle.h>

#include "Camera.h"
#include "../Utils/MathUtil.h"
#include "GraphicsResources.h"

Camera::Camera(GraphicsResources* gr, float aspectRatio, float fov) {
    gfxRes = gr;
    
    position = PGE::Vector3f(0.f, 0.f, 0.f);
    lookAt = PGE::Vector3f(0.f, 0.f, 1.f);
    upDir = PGE::Vector3f(0.f, 1.f, 0.f);

    viewMatrix = PGE::Matrix4x4f::constructViewMat(position, lookAt, upDir);

    xAngle = 0.f;
    yAngle = 0.f;
    yAngleLimit = MathUtil::PI / 2.f;
    tilt = 0.f;

    float nearZ = 0.01f;
    float farZ = 25.f;
    this->fov = fov;
    projectionMatrix = PGE::Matrix4x4f::constructPerspectiveMat(fov, aspectRatio, nearZ, farZ);

    rotation = PGE::Matrix4x4f::identity;
    
    needsViewUpdate = true;
    needsProjUpdate = true;
}

Camera::Camera(GraphicsResources* gr, float aspectRatio) : Camera(gr, aspectRatio, MathUtil::degToRad(90.0f)) { }

Camera::~Camera() {
    for (int i = 0; i < (int)shaders.size(); i++) {
        gfxRes->dropShader(shaders[i]);
    }
}

void Camera::addShader(PGE::FileName fn) {
    shaders.push_back(gfxRes->getShader(fn));
    
    needsViewUpdate = true;
    needsProjUpdate = true;
}

void Camera::update() {
    if (needsViewUpdate) {
        rotation = PGE::Matrix4x4f::constructWorldMat(PGE::Vector3f(0.f, 0.f, 0.f), PGE::Vector3f(1.f, 1.f, 1.f), PGE::Vector3f(-yAngle, xAngle, tilt));
        
        viewMatrix = PGE::Matrix4x4f::constructViewMat(position, rotation.transform(lookAt), rotation.transform(upDir));
        
        // Update shaders.
        for (int i = 0; i < (int)shaders.size(); i++) {
            shaders[i]->getVertexShaderConstant("viewMatrix")->setValue(viewMatrix);
        }
        
        needsViewUpdate = false;
    }
    
    if (needsProjUpdate) {
        for (int i = 0; i < (int)shaders.size(); i++) {
            shaders[i]->getVertexShaderConstant("projectionMatrix")->setValue(projectionMatrix);
        }
        
        needsProjUpdate = false;
    }
}

void Camera::setPosition(const PGE::Vector3f pos) {
    needsViewUpdate = true;
    position = pos;
}

void Camera::setTilt(float rad) {
    needsViewUpdate = !MathUtil::eqFloats(rad, tilt);
    tilt = rad;
}

void Camera::addAngle(float x, float y) {
    if (MathUtil::eqFloats(x, 0.f) && MathUtil::eqFloats(y, 0.f)) {
        return;
    }
    
    xAngle += x;
    yAngle -= y;

    if (yAngle < -yAngleLimit) {
        yAngle = -yAngleLimit;
    } else if (yAngle > yAngleLimit) {
        yAngle = yAngleLimit;
    }

    float PI_MUL_2 = MathUtil::PI * 2.f;

    if (xAngle > PI_MUL_2) {
        xAngle -= PI_MUL_2;
    } else if (xAngle < -PI_MUL_2) {
        xAngle += PI_MUL_2;
    }
        
    needsViewUpdate = true;
}

const PGE::Matrix4x4f& Camera::getViewMatrix() const {
    return viewMatrix;
}

const PGE::Matrix4x4f& Camera::getProjectionMatrix() const {
    return projectionMatrix;
}

const PGE::Matrix4x4f& Camera::getRotationMatrix() const {
    return rotation;
}

float Camera::getXAngle() const {
    return xAngle;
}

float Camera::getYAngle() const {
    return yAngle;
}

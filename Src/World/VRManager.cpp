#include "VRManager.h"

#include <iostream>

void VRCamera::setViewMatrix(const PGE::Matrix4x4f& viewMatrix) {
    this->viewMatrix = viewMatrix;
}

void VRCamera::setProjectionMatrix(const PGE::Matrix4x4f& projectionMatrix) {
    this->projectionMatrix = projectionMatrix;
}

PGE::Matrix4x4f vrMatrixToNative(vr::HmdMatrix44_t vrm) {
    return PGE::Matrix4x4f(
        -vrm.m[0][0], vrm.m[1][0], vrm.m[2][0], vrm.m[3][0],
        vrm.m[0][1], vrm.m[1][1], vrm.m[2][1], vrm.m[3][1],
        vrm.m[0][2], vrm.m[1][2], vrm.m[2][2], vrm.m[3][2],
        vrm.m[0][3], vrm.m[1][3], vrm.m[2][3], 1.f
    );
}

VRManager::VRManager(Config* config, GraphicsResources* gfxres) {
    if (!vr::VR_IsHmdPresent()) {
        throw new std::runtime_error("No HMD found.");
    }
    if (!vr::VR_IsRuntimeInstalled()) {
        throw new std::runtime_error("Open VR runtime not installed");
    }
    vr::EVRInitError e;
    vrSystem = vr::VR_Init(&e, vr::VRApplication_Scene);
    if (vrSystem == nullptr || e != vr::VRInitError_None) {
        throw std::runtime_error("VR failed to initialize! " + e);
    }
    unsigned int width;
    unsigned int height;
    vrSystem->GetRecommendedRenderTargetSize(&width, &height);
    std::cout << "OpenVR recommends a render size of " << width << "x" << height << std::endl;
    config->setResolution(width, height);
    camera = new VRCamera(gfxres, width, height);

    leftProjectionMatrix = vrMatrixToNative(vrSystem->GetProjectionMatrix(vr::Eye_Left, 0.01f, 30.f));
    rightProjectionMatrix = vrMatrixToNative(vrSystem->GetProjectionMatrix(vr::Eye_Right, 0.01f, 30.f));

    texture = nullptr;
}

VRManager::~VRManager() {
    vr::VR_Shutdown();
}

void VRManager::update() {
    vr::VRCompositor()->WaitGetPoses(tdp, vr::k_unMaxTrackedDeviceCount, nullptr, 0);
    for (int i = 0; i < vr::k_unMaxTrackedDeviceCount; i++) {
        if (tdp[i].bDeviceIsConnected) {
            if (tdp[i].bPoseIsValid) {
                if (vrSystem->GetTrackedDeviceClass(i) == vr::TrackedDeviceClass_HMD) {
                    vr::HmdMatrix34_t mat = tdp[i].mDeviceToAbsoluteTracking;
                    // Magic
                    camera->setViewMatrix(PGE::Matrix4x4f::constructViewMat(
                        camera->position.add(PGE::Vector3f(mat.m[0][3], mat.m[1][3], -mat.m[2][3]).multiply(20.f)),
                        PGE::Vector3f(-mat.m[0][2], -mat.m[1][2], mat.m[2][2]),
                        PGE::Vector3f(mat.m[0][1], mat.m[1][1], -mat.m[2][1])
                    ));
                }
            }
        }
    }
}

void VRManager::setEye(bool left) {
    camera->setProjectionMatrix(left ? leftProjectionMatrix : rightProjectionMatrix);
}

void VRManager::createTexture(PGE::Graphics* graphics, Config* config) {
    if (texture != nullptr) {
        throw std::runtime_error("TEXTURE HAS ALREADY BEEN CREATED!");
    } else {
        texture = PGE::Texture::create(graphics, config->getWidth(), config->getHeight(), true, nullptr, PGE::Texture::FORMAT::RGBA32);
    }
}

PGE::Texture* VRManager::getTexture() {
    return texture;
}

VRCamera* VRManager::getCamera() {
    return camera;
}

#include "VRManager.h"

#include <iostream>
#include <filesystem>

#include "../Utils/MathUtil.h"

void VRCamera::setViewMatrix(const PGE::Matrix4x4f& viewMatrix) {
    this->viewMatrix = viewMatrix;
}

void VRCamera::setProjectionMatrix(const PGE::Matrix4x4f& projectionMatrix) {
    this->projectionMatrix = projectionMatrix;
}

PGE::Matrix4x4f VRCamera::getRotationMatrix() const {
    return PGE::Matrix4x4f::rotate(PGE::Vector3f(0, dataInter.getInterpolatedRotation(1.f).y, 0));
}

static PGE::Matrix4x4f vrMatrixToNativeProjection(vr::HmdMatrix44_t vrm) {
    return PGE::Matrix4x4f(
        -vrm.m[0][0], vrm.m[1][0], vrm.m[2][0], vrm.m[3][0],
        vrm.m[0][1], vrm.m[1][1], vrm.m[2][1], vrm.m[3][1],
        vrm.m[0][2], vrm.m[1][2], vrm.m[2][2], vrm.m[3][2],
        vrm.m[0][3], vrm.m[1][3], vrm.m[2][3], 1.f
    );
}

static PGE::Vector3f vrMatrixToPosition(vr::HmdMatrix34_t vrm) {
    return PGE::Vector3f(vrm.m[0][3], vrm.m[1][3], -vrm.m[2][3]).multiply(20.f);
}

static void vrThrowOnInputError(vr::EVRInputError ierr, PGE::String msg) {
    if (ierr != vr::VRInputError_None) {
        throw new std::runtime_error((msg + " " + PGE::String::fromInt(ierr)).cstr());
    }
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

    leftProjectionMatrix = vrMatrixToNativeProjection(vrSystem->GetProjectionMatrix(vr::Eye_Left, 0.01f, 30.f));
    rightProjectionMatrix = vrMatrixToNativeProjection(vrSystem->GetProjectionMatrix(vr::Eye_Right, 0.01f, 30.f));

    texture = nullptr;
    
    vrThrowOnInputError(vr::VRInput()->SetActionManifestPath(PGE::FilePath::fromStr("Data/openVRBindings.json").cstr()), "SetActionManifestPath");

    vr::VRActionSetHandle_t main;
    vrThrowOnInputError(vr::VRInput()->GetActionHandle("/actions/main/in/Test", &test), "GetActionHandle");
    vrThrowOnInputError(vr::VRInput()->GetActionSetHandle("/actions/main", &main), "GetActionSetHandle");
    activeSet.ulActionSet = main;
    
    hasTurned = false;
    fade = 0.f;
}

VRManager::~VRManager() {
    delete texture;
    vr::VR_Shutdown();
}

void VRManager::tick(float timestep) {
    vrThrowOnInputError(vr::VRInput()->UpdateActionState(&activeSet, sizeof(activeSet), 1), "UpdateActionState");
    vr::InputAnalogActionData_t analogData;
    vrThrowOnInputError(vr::VRInput()->GetAnalogActionData(test, &analogData, sizeof(analogData), vr::k_ulInvalidInputValueHandle), "Test");
    if (analogData.x < -0.5f) {
        if (!hasTurned) {
            camera->addAngle(-MathUtil::PI * 0.2f, 0.f);
            hasTurned = true;
            fade = 1.f;
        }
    } else if (analogData.x > 0.5f) {
        if (!hasTurned) {
            camera->addAngle(MathUtil::PI * 0.2f, 0.f);
            hasTurned = true;
            fade = 1.f;
        }
    } else {
        hasTurned = false;
    }
    if (fade > 0.f) {
        fade -= timestep * 0.1f;
    }
}

void VRManager::update() {
    vr::VRCompositor()->WaitGetPoses(tdp, vr::k_unMaxTrackedDeviceCount, nullptr, 0);
    for (int i = 0; i < vr::k_unMaxTrackedDeviceCount; i++) {
        if (tdp[i].bDeviceIsConnected) {
            if (tdp[i].bPoseIsValid) {
                if (vrSystem->GetTrackedDeviceClass(i) == vr::TrackedDeviceClass_HMD) {
                    vr::HmdMatrix34_t mat = tdp[i].mDeviceToAbsoluteTracking;
                    PGE::Matrix4x4f camRot = camera->getRotationMatrix();
                    // Magic
                    camera->setViewMatrix(PGE::Matrix4x4f::constructViewMat(
                        camera->position.add(camRot.transform(vrMatrixToPosition(mat))),
                        camRot.transform(PGE::Vector3f(-mat.m[0][2], -mat.m[1][2], mat.m[2][2])),
                        camRot.transform(PGE::Vector3f(mat.m[0][1], mat.m[1][1], -mat.m[2][1]))
                    ));
                } else if (vrSystem->GetTrackedDeviceClass(i) == vr::TrackedDeviceClass_Controller) {
                    vr::HmdMatrix34_t mat = tdp[i].mDeviceToAbsoluteTracking;
                    vr::TrackedPropertyError e;
                    vr::ETrackedControllerRole role = (vr::ETrackedControllerRole) vrSystem->GetInt32TrackedDeviceProperty(i, vr::Prop_ControllerRoleHint_Int32, &e);
                    if (e != vr::TrackedProp_Success) {
                        throw new std::runtime_error("Tracked device property error! " + e);
                    }
                    if (role == vr::TrackedControllerRole_LeftHand) {
                        handLeft = vrMatrixToPosition(mat);
                    } else if (role == vr::TrackedControllerRole_RightHand) {
                        handRight = vrMatrixToPosition(mat);
                    }
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

PGE::Texture* VRManager::getTexture() const {
    return texture;
}

VRCamera* VRManager::getCamera() const {
    return camera;
}

PGE::Vector3f VRManager::getHandPosition(bool left) const {
    return left ? handLeft : handRight;
}

float VRManager::getFade() const {
    return fade;
}

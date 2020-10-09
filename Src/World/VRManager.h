#ifndef VRMANAGER_H_INCLUDED
#define VRMANAGER_H_INCLUDED

#include <openvr.h>

#include "../Graphics/Camera.h"

class GraphicsResources;
class Config;

class VRCamera : public Camera {
	private:
		void updateDrawTransform(float interpolation) override {};

	public:
		VRCamera(GraphicsResources* gr, int w, int h) : Camera(gr, w, h) {};

		void setViewMatrix(const PGE::Matrix4x4f& viewMatrix);
		void setProjectionMatrix(const PGE::Matrix4x4f& projectionMatrix);

		PGE::Matrix4x4f getRotationMatrix() const override;
};

class VRManager {
	private:
		vr::IVRSystem* vrSystem;
		vr::TrackedDevicePose_t tdp[vr::k_unMaxTrackedDeviceCount];

		VRCamera* camera;
		PGE::Matrix4x4f leftProjectionMatrix;
		PGE::Matrix4x4f rightProjectionMatrix;
		PGE::Texture* texture;

		PGE::Vector3f handLeft;
		PGE::Vector3f handRight;

		vr::VRActiveActionSet_t activeSet;
		vr::VRActionHandle_t test;
		bool hasTurned;
		float fade;

	public:
		VRManager(Config* config, GraphicsResources* gfxres);
		~VRManager();

		void tick(float timestep);
		void update();
		void setEye(bool left);
		void createTexture(PGE::Graphics* graphics, Config* config);
		PGE::Texture* getTexture() const;
		VRCamera* getCamera() const;
		PGE::Vector3f getHandPosition(bool left) const;
		float getFade() const;
};

#endif // VRMANAGER_H_INCLUDED

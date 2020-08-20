#include "Generic.h"

#include <iostream>

#include <assimp/Importer.hpp>
#include <assimp/postprocess.h>
#include <assimp/scene.h>
#include <assimp/mesh.h>
#include <assimp/vector3.h>

#include "../Graphics/GraphicsResources.h"

Model::Model(GraphicsResources* gr, const PGE::FilePath& filename) {
	Assimp::Importer importer;
	importer.SetPropertyInteger(AI_CONFIG_PP_RVC_FLAGS, aiComponent_CAMERAS | aiComponent_LIGHTS | aiComponent_COLORS | aiComponent_ANIMATIONS | aiComponent_BONEWEIGHTS);

	const aiScene* scene = importer.ReadFile((filename + ".fbx").cstr(), aiProcess_PreTransformVertices | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_RemoveComponent | aiProcess_MakeLeftHanded | aiProcess_FlipUVs | aiProcess_FlipWindingOrder);

	PGE::String err = importer.GetErrorString();
	if (!err.isEmpty()) {
		throw new std::runtime_error(err.cstr());
	}

	meshes = new PGE::Mesh*[scene->mNumMeshes];
	meshCount = scene->mNumMeshes;
	for (unsigned int i = 0; i < scene->mNumMeshes; i++) {
		std::vector<PGE::Vertex> vertices;
		for (int i = 0; i < scene->mMeshes[0]->mNumVertices; i++) {
			PGE::Vertex v = PGE::Vertex();
			v.setVector4f("position", PGE::Vector4f(scene->mMeshes[0]->mVertices[i].x, scene->mMeshes[0]->mVertices[i].y, scene->mMeshes[0]->mVertices[i].z, 1.f));
			v.setVector3f("normal", PGE::Vector3f::one);
			v.setColor("color", PGE::Color::White);
			vertices.push_back(v);
		}

		std::vector<PGE::Primitive> primitives;
		for (int i = 0; i < scene->mMeshes[0]->mNumFaces; i++) {
			primitives.push_back(PGE::Primitive(scene->mMeshes[0]->mFaces[i].mIndices[2], scene->mMeshes[0]->mFaces[i].mIndices[1], scene->mMeshes[0]->mFaces[i].mIndices[0]));
		}
		meshes[i] = PGE::Mesh::create(gr->getGraphics(), PGE::Primitive::TYPE::TRIANGLE);
		meshes[i]->setGeometry(vertices.size(), vertices, primitives.size(), primitives);
		PGE::Shader* shader = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/Model/"), true);
		PGE::Shader::Constant* colorConstant = shader->getFragmentShaderConstant("inColor");
		colorConstant->setValue(PGE::Color::White);
		modelMatrix = shader->getVertexShaderConstant("modelMatrix");
		PGE::Material* m = new PGE::Material(shader, gr->getTexture(filename.cstr()));
		meshes[i]->setMaterial(m);
	}
}

void Model::render(const PGE::Matrix4x4f& modelMatrix) const {
	this->modelMatrix->setValue(modelMatrix);
	for (int i = 0; i < meshCount; i++) {
		meshes[i]->render();
	}
}

ModelInstance::ModelInstance(Model* model) {
	this->model = model;
}

void ModelInstance::recomputeModelMatrix() {
	modelMatrix = PGE::Matrix4x4f::constructWorldMat(position, scale, rotation);
}

void ModelInstance::setPosition(const PGE::Vector3f& pos) {
	if (position != pos) {
		position = pos;
		recomputeModelMatrix();
	}
}

void ModelInstance::setRotation(const PGE::Vector3f& rot) {
	if (rotation != rot) {
		rotation = rot;
		recomputeModelMatrix();
	}
}

void ModelInstance::setScale(const PGE::Vector3f& scl) {
	if (scale != scl) {
		scale = scl;
		recomputeModelMatrix();
	}
}

const PGE::Vector3f& ModelInstance::getPosition() const {
	return position;
}

const PGE::Vector3f& ModelInstance::getRotation() const {
	return rotation;
}

const PGE::Vector3f& ModelInstance::getScale() const {
	return scale;
}

Model* ModelInstance::getModel() const {
	return model;
}

void ModelInstance::render() const {
	model->render(modelMatrix);
}

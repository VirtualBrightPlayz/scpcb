#include "Material.h"

std::map<PGE::Material*, int> Material::cpyTracker = std::map<PGE::Material*, int>();

void Material::increment() const {
    if (internal != nullptr) {
        cpyTracker[internal] = cpyTracker[internal] + 1;
    }
}

bool Material::decrement() const {
    if (internal == nullptr) { return false; }
    int newCount = cpyTracker[internal] = cpyTracker[internal] - 1;
    return newCount <= 0;
}

Material Material::create(Shader& shd, const std::vector<Texture>& t) {
    Material mat;
    mat.shader = shd;
    mat.textures = t;

    std::vector<PGE::Texture*> tex;
    for (int i = 0; i < (int)t.size(); i++) {
        tex.push_back(t[i].getInternal());
    }
    mat.internal = new PGE::Material(shd.getInternal(), tex);
    cpyTracker.insert(std::pair<PGE::Material*, int>(mat.internal, 1));

    return mat;
}

Material Material::create(Shader& shd, Texture& t) {
    Material mat;
    mat.shader = shd;
    mat.textures.push_back(t);

    mat.internal = new PGE::Material(shd.getInternal(), t.getInternal());
    cpyTracker.insert(std::pair<PGE::Material*, int>(mat.internal, 1));

    return mat;
}

Material Material::create(Shader& shd) {
    Material mat;
    mat.shader = shd;
    mat.internal = new PGE::Material(shd.getInternal());
    cpyTracker.insert(std::pair<PGE::Material*, int>(mat.internal, 1));

    return mat;
}

Material::Material(const Material& cpy) {
    shader = cpy.shader;
    textures = cpy.textures;
    internal = cpy.internal;
    cpy.increment();
}

Material& Material::operator=(const Material& other) {
    if (&other == this) return *this;

    // Wipe current reference.
    textures.clear();
    bool remove = decrement();

    if (remove) {
        cpyTracker.erase(internal);
        delete internal;
    }
    internal = nullptr;

    // Add new reference.
    shader = other.shader;
    textures = other.textures;
    internal = other.internal;
    other.increment();

    return *this;
}

Material::~Material() {
    textures.clear();
    bool remove = decrement();

    if (remove && internal != nullptr) {
        cpyTracker.erase(internal);
        delete internal;
    }
}

PGE::Material* Material::operator->() const {
    return internal;
}

PGE::Material* Material::getInternal() {
    return internal;
}

std::vector<Texture> Material::getTextures() const {
    return textures;
}

Shader Material::getShader() const {
    return shader;
}
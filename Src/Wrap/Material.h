#ifndef MATERIAL_H_INCLUDED
#define MATERIAL_H_INCLUDED

#include <Material/Material.h>
#include <map>
#include "Shader.h"
#include "Texture.h"

class Material {
private:
    Shader shader;
    std::vector<Texture> textures;

    static std::map<PGE::Material*, int> cpyTracker;
    // Increments the copy tracker.
    void increment() const;
    // Decrements the copy tracker and returns true if it tracks no more copies of an object.
    bool decrement() const;

    PGE::Material* internal = nullptr;

public:
    Material()=default;

    Material(const Material& cpy);
    Material& operator=(const Material& other);
    ~Material();

    static Material create(const Shader& sh, const std::vector<Texture>& t);
    static Material create(const Shader& sh, const Texture& t);
    static Material create(const Shader& sh);

    PGE::Material* operator->() const;

    PGE::Material* getInternal() const;

    std::vector<Texture> getTextures() const;
    Shader getShader() const;
};

#endif // MATERIAL_H_INCLUDED

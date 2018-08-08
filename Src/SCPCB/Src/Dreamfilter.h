#ifndef DREAMFILTER_H_INCLUDED
#define DREAMFILTER_H_INCLUDED
#include <vector>

namespace CBN {

// Globals.
extern class MeshModel* ark_blur_image;
extern class Texture* ark_blur_texture;
extern int ark_sw;
extern int ark_sh;
extern class Camera* ark_blur_cam;

// Functions.
void CreateBlurImage();

void UpdateBlur(float power);

}
#endif // DREAMFILTER_H_INCLUDED

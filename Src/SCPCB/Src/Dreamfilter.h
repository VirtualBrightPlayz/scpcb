#ifndef DREAMFILTER_H_INCLUDED
#define DREAMFILTER_H_INCLUDED
#include "entity.h"
#include "camera.h"
#include <vector>

namespace CBN {

// Globals.
extern int ark_blur_image;
extern class Texture* ark_blur_texture;
extern int ark_sw;
extern int ark_sh;
extern class Camera* ark_blur_cam;

// Functions.
void CreateBlurImage();

void UpdateBlur(float power);

}
#endif // DREAMFILTER_H_INCLUDED

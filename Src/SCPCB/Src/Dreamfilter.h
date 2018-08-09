#ifndef DREAMFILTER_H_INCLUDED
#define DREAMFILTER_H_INCLUDED
#include <vector>

class MeshModel;
class Texture;
class Camera;

namespace CBN {

// Globals.
extern MeshModel* ark_blur_image;
extern Texture* ark_blur_texture;
extern int ark_sw;
extern int ark_sh;
extern Camera* ark_blur_cam;

// Functions.
void CreateBlurImage();

void UpdateBlur(float power);

}
#endif // DREAMFILTER_H_INCLUDED

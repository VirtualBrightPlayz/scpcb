#ifndef DREAMFILTER_H_INCLUDED
#define DREAMFILTER_H_INCLUDED
#include <vector>

namespace CBN {

// Globals.
extern int ark_blur_image;
extern int ark_blur_texture;
extern int ark_sw;
extern int ark_sh;
extern int ark_blur_cam;

// Functions.
void CreateBlurImage();

void UpdateBlur(float power);

}
#endif // DREAMFILTER_H_INCLUDED

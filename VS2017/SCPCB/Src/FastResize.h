#ifndef FASTRESIZE_H_INCLUDED
#define FASTRESIZE_H_INCLUDED
#include <vector>

namespace CBN {

// Globals.
extern int fresize_image;
extern int fresize_texture;
extern int fresize_texture2;
extern int fresize_cam;

// Functions.
int ResizeImage2(int image, int width, int height);

void ScaleRender(float x, float y, float hscale = 1.0, float vscale = 1.0);

void InitFastResize();

}
#endif // FASTRESIZE_H_INCLUDED

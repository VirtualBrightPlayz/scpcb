#ifndef FASTRESIZE_H_INCLUDED
#define FASTRESIZE_H_INCLUDED
#include <vector>

namespace CBN {

// Globals.
extern class MeshModel* fresize_image;
extern class Texture* fresize_texture;
extern class Texture* fresize_texture2;
extern class Camera* fresize_cam;

// Functions.
class bbImage* ResizeImage2(class bbImage* image, int width, int height);

void ScaleRender(float x, float y, float hscale = 1.0, float vscale = 1.0);

void InitFastResize();

}
#endif // FASTRESIZE_H_INCLUDED

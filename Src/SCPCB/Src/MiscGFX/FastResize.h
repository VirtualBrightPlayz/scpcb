#ifndef FASTRESIZE_H_INCLUDED
#define FASTRESIZE_H_INCLUDED
#include <vector>

class MeshModel;
class Texture;
class Camera;
class bbImage;

namespace CBN {

// Globals.
extern MeshModel* fresize_image;
extern Texture* fresize_texture;
extern Texture* fresize_texture2;
extern Camera* fresize_cam;

// Functions.
bbImage* ResizeImage2(bbImage* image, int width, int height);

void ScaleRender(float x, float y, float hscale = 1.0, float vscale = 1.0);

void InitFastResize();

}
#endif // FASTRESIZE_H_INCLUDED

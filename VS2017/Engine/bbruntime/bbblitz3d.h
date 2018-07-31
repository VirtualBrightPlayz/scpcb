
#ifndef BBBLITZ3D_H
#define BBBLITZ3D_H

#include "bbsys.h"
#include "../gxruntime/gxscene.h"

extern gxScene *gx_scene;

class Entity * bbCreateCamera(class Entity *p);
void bbPointEntity(class Entity *e, class Entity *t, float roll);
class Entity * bbCreateCube(class Entity *p);
void bbPositionEntity(class Entity *e, float x, float y, float z, int global);
void bbRenderWorld(float tween);

#endif

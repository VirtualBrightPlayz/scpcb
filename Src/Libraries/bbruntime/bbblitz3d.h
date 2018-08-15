
#ifndef BBBLITZ3D_H
#define BBBLITZ3D_H

#include "bbsys.h"
#include "../blitz3d/blitz3d.h"
#include "../blitz3d/world.h"
#include "../blitz3d/texture.h"
#include "../blitz3d/brush.h"
#include "../blitz3d/camera.h"
#include "../blitz3d/sprite.h"
#include "../blitz3d/meshmodel.h"
#include "../blitz3d/loader_3ds.h"
#include "../blitz3d/loader_b3d.h"
#include "../blitz3d/meshutil.h"
#include "../blitz3d/pivot.h"
#include "../blitz3d/planemodel.h"
#include "../blitz3d/terrain.h"
#include "../blitz3d/listener.h"
#include "../blitz3d/cachedtexture.h"

extern gxScene *gx_scene;

void bbHWMultiTex(int enable);
int bbHWTexUnits();
int bbGfxDriverCaps3D();
void bbWBuffer(int enable);
void bbDither(int enable);
void bbAntiAlias(int enable);
void bbWireFrame(int enable);
void bbAmbientLight(float r, float g, float b);
void bbClearCollisions();
void bbCollisions(int src_type, int dest_type, int method, int response);
void bbUpdateWorld (float eslapsed=1.f);
void bbCaptureWorld ();
void bbRenderWorld(float tween=1.f);
void bbPrintWorldAssetList();
int bbActiveTextures();
int bbTrisRendered();
float bbStats3D(int n);
class Texture* bbCreateTexture(int w, int h, int flags=1, int frames=1);
void bbScaleTexture(Texture *t, float u_scale, float v_scale);
class Texture* bbLoadTexture(const String& file, int flags=1);
class Texture* bbLoadAnimTexture(const String& file, int flags, int w, int h, int first, int cnt);
void bbFreeTexture(class Texture* t);
void bbTextureBlend(class Texture* t, int blend);
void bbTextureCoords(class Texture* t, int flags);
void bbRotateTexture(class Texture* t, float angle);
void bbPositionTexture(class Texture* t, float u_pos, float v_pos);
int bbTextureWidth(class Texture* t);
int bbTextureHeight(class Texture* t);
String bbTextureName(class Texture* t);
void bbSetCubeFace(class Texture* t, int face);
void bbSetCubeMode(class Texture* t, int mode);
class gxCanvas* bbTextureBuffer(class Texture* t, int frame=0);
void bbClearTextureFilters();
class Brush* bbCreateBrush(float r=255.f, float g=255.f, float b=255.f);
class Brush* bbLoadBrush(const String& file, int flags=1, float u_scale=1.f, float v_scale=1.f);
void bbFreeBrush(class Brush* b);
void bbBrushColor(class Brush* br, float r, float g, float b);
void bbBrushAlpha(class Brush* b, float alpha);
void bbBrushShininess(class Brush* b, float n);
void bbBrushTexture(class Brush* b, Texture* t, int frame, int index);
class Texture* bbGetBrushTexture(class Brush* b, int index);
void bbBrushBlend(class Brush* b, int blend);
void bbBrushFX(class Brush* b, int fx);
class MeshModel* bbLoadMesh(const String& f, class Object* p=nullptr);
class MeshModel* bbLoadAnimMesh(const String& f, class Object* p=nullptr);
int bbLoadAnimSeq(class Object* o, const String& f);
class MeshModel* bbCreateMesh(class Object* p=nullptr);
class MeshModel* bbCreateCube(class Object* p=nullptr);
class MeshModel* bbCreateSphere(int segs, class Object* p=nullptr);
class MeshModel* bbCreateCylinder(int segs, int solid, class Object* p=nullptr);
class MeshModel* bbCreateCone(int segs, int solid, class Object* p=nullptr);
class MeshModel* bbDeepCopyMesh(class MeshModel* m, class Object* p=nullptr);
void bbScaleMesh(class MeshModel* m, float x, float y, float z);
void bbRotateMesh(class MeshModel* m, float x, float y, float z);
void bbPositionMesh(class MeshModel* m, float x, float y, float z);
void bbFitMesh(class MeshModel* m, float x, float y, float z, float w, float h, float d, int uniform);
void bbFlipMesh(class MeshModel* m);
void bbPaintMesh(class MeshModel* m, class Brush* b);
void bbAddMesh(class MeshModel* src, class MeshModel* dest);
void bbUpdateNormals(class MeshModel* m);
void bbLightMesh(class MeshModel* m, float r, float g, float b, float range = 0.f, float x = 0.f, float y = 0.f, float z = 0.f);
float bbMeshWidth(class MeshModel* m);
float bbMeshHeight(class MeshModel* m);
float bbMeshDepth(class MeshModel* m);
int bbCountSurfaces(class MeshModel* m);
class Surface* bbGetSurface(class MeshModel *m, int index);
void bbMeshCullBox(class MeshModel* m, float x, float y, float z, float width, float height, float depth);
class Surface* bbCreateSurface(class MeshModel* m, Brush* b=nullptr);
class Brush* bbGetSurfaceBrush(class Surface* s);
class Brush* bbGetEntityBrush(class Model* m);
class Surface* bbFindSurface(class MeshModel* m, Brush* b);
void bbPaintSurface(class Surface* s, Brush* b);
int bbAddVertex(class Surface* s, float x, float y, float z, float tu=0.f, float tv=0.f, float tw=1.f);
int bbAddTriangle(class Surface* s, int v0, int v1, int v2);
void bbVertexCoords(class Surface* s, int n, float x, float y, float z);
void bbVertexNormal(class Surface* s, int n, float x, float y, float z);
void bbVertexColor(class Surface* s, int n, float r, float g, float b, float a=1.f);
void bbVertexTexCoords(Surface *s, int n, float u, float v, float w=1.f, int set=0.f);
int bbCountVertices(class Surface* s);
int bbCountTriangles(class Surface* s);
float bbVertexX(class Surface* s, int n);
float bbVertexY(class Surface* s, int n);
float bbVertexZ(class Surface* s, int n);
float bbVertexNX(class Surface* s, int n);
float bbVertexNY(class Surface* s, int n);
float bbVertexNZ(class Surface* s, int n);
float bbVertexRed(class Surface* s, int n);
float bbVertexGreen(class Surface* s, int n);
float bbVertexBlue(class Surface* s, int n);
float bbVertexAlpha(class Surface* s, int n);
int bbTriangleVertex(class Surface* s, int n, int v);
class Camera* bbCreateCamera(class Object* p = nullptr);
void bbCameraZoom(class Camera* c, float zoom);
void bbCameraRange(class Camera* c, float nr, float fr);
void bbCameraClsColor(class Camera* c, float r, float g, float b);
void bbCameraClsMode(Camera *c, int cls_color, int cls_zbuffer);
void bbCameraProjMode(class Camera* c, int mode);
void bbCameraViewport(class Camera* c, int x, int y, int w, int h);
void bbCameraFogColor(class Camera* c, float r, float g, float b);
void bbCameraFogRange(class Camera* c, float nr, float fr);
void bbCameraFogMode(class Camera* c, int mode);
int bbCameraProject(class Camera* c, float x, float y, float z);
float bbProjectedX();
float bbProjectedY();
float bbProjectedZ();
int bbEntityInView(class Object* e, Camera* c);
class Object* bbEntityPick(class Object* src, float range);
int bbEntityVisible(class Object *src,class Object *dest);
void bbEntityType(Object *o, int type, int recurs=0);
void bbEntityPickMode(class Object* o, int mode, int obs=1);
class Object* bbLinePick(float x, float y, float z, float dx, float dy, float dz, float radius=0);
float bbPickedX();
float bbPickedY();
float bbPickedZ();
float bbPickedNX();
float bbPickedNY();
float bbPickedNZ();
float bbPickedTime();
class Object* bbPickedEntity();
void* bbPickedSurface();
int bbPickedTriangle();
class Light* bbCreateLight(int type, class Object* p=nullptr);
void bbLightColor(class Light* light, float r, float g, float b);
void bbLightRange(class Light* light, float range);
void bbLightConeAngles(class Light *light, float inner, float outer);
class Pivot* bbCreatePivot(class Object* p=nullptr);
class Sprite* bbCreateSprite(class Object* p=nullptr);
class Sprite* bbLoadSprite(const String& file, int flags, Object *p=nullptr);
void bbRotateSprite(class Sprite* s, float angle);
void bbScaleSprite(class Sprite *s, float x, float y);
void bbSpriteViewMode(class Sprite *s, int mode);
class Object* bbCreatePlane(int segs=1, class Object* p=nullptr);
void bbTerrainShading(class Terrain* t, int enable);
int bbTerrainSize(class Terrain* t);
class gxChannel* bbEmitSound(class gxSound* sound, class Object* o);
class Model* bbCopyModelEntity(class Model* e, class Object* p = nullptr);
class MeshModel* bbCopyMeshModelEntity(class MeshModel* e, class Object* p = nullptr);
class Pivot* bbCopyPivot(class Pivot* e, class Object* p = nullptr);
float bbEntityX(class Object* e, int global=1);
float bbEntityY(class Object* e, int global=1);
float bbEntityZ(class Object* e, int global=1);
float bbEntityPitch(class Object* e, int global=1);
float bbEntityYaw(class Object* e, int global=1);
float bbEntityRoll(class Object* e, int global=1);
float bbGetMatElement(class Object* e, int row, int col);
void  bbTFormPoint(float x, float y, float z, Object *src, Object *dest);
void  bbTFormVector(float x, float y, float z, Object *src, Object *dest);
float bbTFormedX();
float bbTFormedY();
float bbTFormedZ();
float bbVectorYaw(float x, float y, float z);
float bbVectorPitch(float x, float y, float z);
float bbDeltaYaw(Object *src, Object *dest);
float bbDeltaPitch(Object *src, Object *dest);
void bbResetEntity(class Object* o);
class Object* bbGetParent(class Object* e);
int bbGetEntityType(class Object* o);
void bbEntityRadius(Object *o, float x_radius, float y_radius=0.0f);
void bbEntityBox(class Object* o, float x, float y, float z, float w, float h, float d);
class Object* bbEntityCollided(class Object* o, int type);
int bbCountCollisions(class Object* o);
float bbCollisionX(Object *o, int index);
float bbCollisionY(Object *o, int index);
float bbCollisionZ(Object *o, int index);
float bbCollisionNX(Object *o, int index);
float bbCollisionNY(Object *o, int index);
float bbCollisionNZ(Object *o, int index);
float bbEntityDistance(class Object *src, class Object *dest);
float bbEntityDistanceSquared(class Object *src, class Object *dest);
void bbMoveEntity(class Object* e, float x, float y, float z);
void bbTurnEntity(class Object* e, float p, float y, float r, int global=1);
void bbTranslateEntity(class Object* e, float x, float y, float z, int global=1);
void bbScaleEntity(class Object *e, float x, float y, float z, int global=1);
void bbPositionEntity(class Object* e, float x, float y, float z, int global=1);
void bbRotateEntity(class Object* e, float p, float y, float r, int global=1);
void bbPointEntity(class Object* e, class Object* t, float roll=0.f);
void bbAlignToVector(Object* e, float nx, float ny, float nz, int axis, float rate=1.f);
void bbSetAnimTime(Object *o, float time, int seq=0);
void bbAnimate(class Object* o, int mode, float speed, int seq, float trans);
int bbAddAnimSeq(class Object* o, int length);
int bbAnimSeq(class Object* o);
float bbAnimTime(class Object* o);
int bbAnimLength(class Object* o);
int bbAnimating(class Object* o);
void bbEntityParent(class Object* e, class Object* p, int global=1);
int bbCountChildren(class Object* e);
class Object* bbGetChild(class Object* e, int index);
class Object* bbFindChild(class Object* e, const String& t);
void bbPaintEntity(class Model* m, Brush* b);
void bbEntityColor(class Model* m, float r, float g, float b);
void bbEntityAlpha(class Model* m, float alpha);
void bbEntityShininess(class Model* m, float shininess);
void bbEntityTexture(class Model* m, Texture* t, int frame=0, int index=0);
void bbEntityBlend(class Model* m, int blend);
void bbEntityFX(class Model* m, int fx);
void bbEntityAutoFade(class Model* m, float nr, float fr);
void bbEntityOrder(class Object* o, int n);
void bbHideEntity(class Object* e);
void bbShowEntity(class Object* e);
void bbFreeEntity(class Object* e);
void bbNameEntity(class Object* e, const String& t);
String bbEntityName(class Object* e);
String bbEntityClass(class Object* e);

#endif

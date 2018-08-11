#ifndef MATHUTILS_H_INCLUDED
#define MATHUTILS_H_INCLUDED
#include <vector>
#include <StringType.h>

class MeshModel;
class Object;

namespace CBN {

// Globals.
extern float Mesh_MinX;
extern float Mesh_MinY;
extern float Mesh_MinZ;
extern float Mesh_MaxX;
extern float Mesh_MaxY;
extern float Mesh_MaxZ;
extern float Mesh_MagX;
extern float Mesh_MagY;
extern float Mesh_MagZ;

// Functions.
float Distance(float x1, float y1, float x2, float y2);

float CurveValue(float number, float old, float smooth);

float CurveAngle(float val, float old, float smooth);

float WrapAngle(float angle);

float GetAngle(float x1, float y1, float x2, float y2);

int CircleToLineSegIsect(float cx, float cy, float r, float l1x, float l1y, float l2x, float l2y);

float Min(float a, float b);

float Max(float a, float b);

float Sgn(float f);

float Modf(float a,float div);

float angleDist(float a0, float a1);

float Inverse(float number);

float Rnd_Array(float numb1, float numb2, float Array1, float Array2);

int TimeInPosMilliSecs();

void MakeCollBox(MeshModel* mesh);

void GetMeshExtents(MeshModel* mesh);

float EntityScaleX(Object* entity, int globl = false);

float EntityScaleY(Object* entity, int globl = false);

float EntityScaleZ(Object* entity, int globl = false);

int SeedStringToInt(const String& seed);

float modFloat(float num, float divide);

}
#endif // MATHUTILS_H_INCLUDED

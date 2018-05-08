Graphics3D 1280,720,0,2

mesh% = LoadAnimMesh("GFX/npcs/guard.b3d")
SetAnimTime mesh,923
cam% = CreateCamera()
CameraRange cam,0.2,1000
CameraClsColor cam,255,0,255

TranslateEntity cam,0,-20,0
PointEntity cam,mesh

TranslateEntity cam,0,5,5

TurnEntity cam,0,0,180

RenderWorld
Flip 1

Delay 10000
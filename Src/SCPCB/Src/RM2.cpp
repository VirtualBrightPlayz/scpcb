#include <StringType.h>
#include <bbstream.h>
#include <bbblitz3d.h>
#include <bbfilesystem.h>

#include "RM2.h"
#include "MapSystem.h"
#include "Materials.h"

namespace CBN {

// Functions.
String ReadByteString(bbStream* stream) {
    String retVal = "";
    int length = bbReadByte(stream);
    for (int i = 1; i <= length; i++) {
        retVal = retVal+((char)bbReadByte(stream));
    }
    return retVal;
}

void LoadRM2(RoomTemplate* rt) {
    String fullFilename = rt->objPath;

    MeshModel* opaqueMesh = bbCreateMesh();
    MeshModel* alphaMesh;

    std::vector<int> usedTextures;

    std::vector<int> collisionObjs;
    std::vector<int>;

    String filename = StripPath(fullFilename);
    String filepath = StripFilename(fullFilename);

    bbFile* file = bbReadFile(fullFilename);

    if (file==nullptr) {
        throw ("Failed to read "+fullFilename);
    }

    String header = "";
    int i;
    for (int i = 0; i < 4; i++) {
        header = header+((char)bbReadByte(file));
    }

    if (!header.equals(RM2_HEADER)) {
        throw ("Error while loading "+fullFilename+": expected "+RM2_HEADER+", found "+header);
    }

    int partType;

    int count;
    String texName;
    int flags;
    int loadFlags;
    int blendFlags;
    int uvSet;
    Texture* texture;
    int shouldLoadTexture;

    ModelModel* mesh;
    ModelModel* clonedMesh;
    Brush* brush;
    int textureIndex[2];
    int layerCount;
    Material* mat;
    int surf;
    float x;
    float y;
    float z;
    int r;
    int g;
    int b;
    float u;
    float v;
    int vert1;
    int vert2;
    int vert3;
    int j;

    TempScreen* tempScreen;
    TempWayPoint* waypointTemp;

    float range;
    float intensity;

    float pitch;
    float yaw;
    float roll;

    float innerConeAngle;
    float outerConeAngle;

    LightTemplate* lightTemplate;

    int ambienceInd;

    String propName;
    float xScale;
    float yScale;
    float zScale;
    Prop* prop;

    int prevType;
    while (!bbEof(file)) {
        prevType = partType;
        partType = bbReadByte(file);
        switch (partType) {
            case RM2_TEXTURES: {
                //[Block]
                count = bbReadByte(file);
                for (i = 0; i < count; i++) {
                    texName = ReadByteString(file);
                    flags = bbReadByte(file);
                    loadFlags = flags >> 4;
                    blendFlags = flags & 0x0F;
                    uvSet = bbReadByte(file);

                    mat = GetCache(texName);
                    if (mat==nullptr) {
                        shouldLoadTexture = true;
                    } else if ((mat->diff==0)) {
                        shouldLoadTexture = true;
                    }

                    if (shouldLoadTexture) {
                        if (blendFlags==RM2_BLENDFLAG_NORMAL) {
                            blendFlags = 2;
                        } else if ((blendFlags==RM2_BLENDFLAG_DIFFUSE)) {
                            blendFlags = 5;
                        } else if ((blendFlags==RM2_BLENDFLAG_LM)) {
                            blendFlags = 3;
                        }

                        texture = LoadRMeshTexture(filepath,texName,loadFlags);
                        bbTextureBlend(texture,blendFlags);
                        bbTextureCoords(texture,uvSet);
                        AddTextureToCache(texName,texture);
                    }
                    PushIntArrayListElem(usedTextures,Handle(GetCache(texName)));
                }
                //[End Block]
            } break;
            case RM2_OPAQUE,RM2_ALPHA: {
                //[Block]
                mesh = bbCreateMesh();
                brush = 0;

                layerCount = 0;
                for (i = 0; i <= 1; i++) {
                    textureIndex[i] = bbReadByte(file);
                    if (textureIndex[i]>0) {
                        layerCount = layerCount+1;
                    }
                }

                for (i = 0; i <= 1; i++) {
                    mat = nullptr;
                    if (textureIndex[i]>0) {
                        mat = Object.Material(GetIntArrayListElem(usedTextures,textureIndex[i]-1));
                    }
                    if (mat!=nullptr) {
                        if (brush==0) {
                            brush = bbCreateBrush(255,255,255);
                        }
                        //TODO: replace this hack once we can start using shaders
                        BrushTexture(brush,mat->diff,0,i+(layerCount = 2));
                    }
                }

                if (brush!=0 & (layerCount==2)) {
                    bbBrushTexture(brush,AmbientLightRoomTex,0,0);
                }

                surf = bbCreateSurface(mesh);
                if (brush!=0) {
                    bbPaintSurface(surf,brush);
                    bbFreeBrush(brush);
                }

                //vertices
                count = bbReadShort(file);
                for (i = 0; i <= count-1; i++) {
                    x = bbReadFloat(file);
                    y = bbReadFloat(file);
                    z = bbReadFloat(file);

                    r = bbReadByte(file);
                    g = bbReadByte(file);
                    b = bbReadByte(file);

                    bbAddVertex(surf,x,y,z);
                    bbVertexColor(surf,i,r,g,b,1.0);
                    for (j = 0; j <= 1; j++) {
                        u = bbReadFloat(file);
                        v = bbReadFloat(file);
                        bbVertexTexCoords(surf,i,u,v,0.0,j);
                    }
                }

                //triangles
                count = bbReadShort(file);
                for (i = 0; i <= count-1; i++) {
                    vert1 = bbReadShort(file);
                    vert2 = bbReadShort(file);
                    vert3 = bbReadShort(file);

                    bbAddTriangle(surf,vert1,vert2,vert3);
                }

                if (partType==RM2_OPAQUE) {
                    bbAddMesh(mesh,opaqueMesh);
                } else if ((partType==RM2_ALPHA)) {
                    if (alphaMesh==0) {
                        alphaMesh = bbCreateMesh();
                    }
                    bbAddMesh(mesh,alphaMesh);
                }
                bbEntityPickMode(mesh,2,true);

                //double-sided collision bois
                clonedMesh = bbCopyMesh(mesh);
                bbFlipMesh(clonedMesh);
                bbAddMesh(clonedMesh,mesh);
                bbFreeEntity(clonedMesh);

                bbEntityAlpha(mesh,0.0);

                bbEntityType(mesh,HIT_MAP);
                PushIntArrayListElem(collisionObjs,mesh);
                bbHideEntity(mesh);
                //[End Block]
            } break;
            case RM2_INVISIBLE: {
                //[Block]
                mesh = bbCreateMesh();

                surf = bbCreateSurface(mesh);

                //vertices
                count = bbReadShort(file);

                for (i = 0; i <= count-1; i++) {
                    x = bbReadFloat(file);
                    y = bbReadFloat(file);
                    z = bbReadFloat(file);

                    bbAddVertex(surf,x,y,z);
                    bbVertexColor(surf,i,0,255,0);
                }

                //triangles
                count = bbReadShort(file);

                for (i = 0; i <= count-1; i++) {
                    vert1 = bbReadShort(file);
                    vert2 = bbReadShort(file);
                    vert3 = bbReadShort(file);

                    bbAddTriangle(surf,vert1,vert2,vert3);
                }

                bbEntityFX(mesh,1+2);
                bbEntityAlpha(mesh,1.0);
                bbEntityType(mesh,HIT_MAP);
                bbAddMesh(mesh,opaqueMesh);
                PushIntArrayListElem(collisionObjs,mesh);
                bbHideEntity(mesh);
                //[End Block]
            } break;
            case RM2_SCREEN: {
                //[Block]
                tempScreen = new TempScreen();
                tempScreen->x = bbReadFloat(file);
                tempScreen->y = bbReadFloat(file);
                tempScreen->z = bbReadFloat(file);
                tempScreen->imgpath = ReadByteString(file);
                tempScreen->roomtemplate = rt;
                //[End Block]
            } break;
            case RM2_WAYPOINT: {
                //[Block]
                waypointTemp = new TempWayPoint();
                waypointTemp->x = bbReadFloat(file);
                waypointTemp->y = bbReadFloat(file);
                waypointTemp->z = bbReadFloat(file);
                for (i = 0; i <= 16; i++) {
                    waypointTemp->connectedTo[i] = bbReadByte(file);
                    if (waypointTemp->connectedTo[i]==0) {
                        break;
                    }
                }
                waypointTemp->roomtemplate = rt;
                //did some waypoint-based lifeform just say... ICE
                //				cuboid% = CreateCube()
                //				ScaleMesh(cuboid,60.0,60.0,60.0)
                //				PositionMesh(cuboid,waypointTemp\x,waypointTemp\y,waypointTemp\z)
                //				AddMesh(cuboid,opaqueMesh)
                //				FreeEntity(cuboid)
                //GET ICCED DUMMY
                //RT FOR FREE IPOD
                //you may now access
                //         ^
                //        /|\
                //       / | \
                //      <--+-->
                //       \ | /
                //        \|/
                //         v
                //   THE OCTAHEDRON
                //[End Block]
            } break;
            case RM2_POINTLIGHT: {
                //[Block]
                x = bbReadFloat(file);
                y = bbReadFloat(file);
                z = bbReadFloat(file);

                range = bbReadFloat(file);

                r = bbReadByte(file);
                g = bbReadByte(file);
                b = bbReadByte(file);
                intensity = (float)(bbReadByte(file))/255.0;

                AddTempLight(rt, x,y,z, LIGHTTYPE_POINT, range, r,g,b);
                //[End Block]
            } break;
            case RM2_SPOTLIGHT: {
                //[Block]
                x = bbReadFloat(file);
                y = bbReadFloat(file);
                z = bbReadFloat(file);

                range = bbReadFloat(file);

                r = bbReadByte(file);
                g = bbReadByte(file);
                b = bbReadByte(file);
                intensity = (float)(bbReadByte(file))/255.0;

                pitch = bbReadFloat(file);
                yaw = bbReadFloat(file);

                innerConeAngle = bbReadFloat(file);
                outerConeAngle = bbReadFloat(file);

                lightTemplate = AddTempLight(rt, x,y,z, LIGHTTYPE_SPOT, range, (int)(r*intensity),(int)(g*intensity),(int)(b*intensity));

                lightTemplate->pitch = pitch;
                lightTemplate->yaw = yaw;
                lightTemplate->innerconeangle = innerConeAngle;
                lightTemplate->innerconeangle = outerConeAngle;
                //[End Block]
            } break;
            case RM2_SOUNDEMITTER: {
                //[Block]
                x = bbReadFloat(file);
                y = bbReadFloat(file);
                z = bbReadFloat(file);

                ambienceInd = bbReadByte(file);

                range = bbReadFloat(file);

                for (j = 0; j <= MaxRoomEmitters-1; j++) {
                    if (rt->tempSoundEmitter[j]==0) {
                        rt->tempSoundEmitterX[j] = x;
                        rt->tempSoundEmitterY[j] = y;
                        rt->tempSoundEmitterZ[j] = z;
                        rt->tempSoundEmitter[j] = ambienceInd;

                        rt->tempSoundEmitterRange[j] = range;
                        //temp1i=1
                        break;
                    }
                }
                //[End Block]
            } break;
            case RM2_PROP: {
                //[Block]
                propName = ReadByteString(file);

                x = bbReadFloat(file);
                y = bbReadFloat(file);
                z = bbReadFloat(file);

                pitch = bbReadFloat(file);
                yaw = bbReadFloat(file);
                roll = bbReadFloat(file);

                xScale = bbReadFloat(file);
                yScale = bbReadFloat(file);
                zScale = bbReadFloat(file);

                prop = LoadProp(propName,x,y,z,pitch,yaw,roll,xScale,yScale,zScale);

                if (props==nullptr) {
                    props = CreateIntArrayList();
                }
                PushIntArrayListElem(props,Handle(prop));
                //[End Block]
            } break;
            default: {
                throw ("Error after reading type "+String(prevType));
            } break;
        }
    }

    DeleteIntArrayList(usedTextures);

    bbEntityFX(opaqueMesh,1+2);
    if (alphaMesh!=0) {
        bbEntityFX(alphaMesh,1+2+16);
    }

    rt->opaqueMesh = opaqueMesh;
    bbHideEntity(opaqueMesh);
    if (alphaMesh!=0) {
        rt->alphaMesh = alphaMesh;
        bbHideEntity(alphaMesh);
    }
    rt->collisionObjs = collisionObjs;
    rt->props = props;

    rt->loaded = true;

    bbCloseFile(file);
}

}

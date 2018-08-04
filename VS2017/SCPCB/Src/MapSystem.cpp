#include "MapSystem.h"
#include "include.h"
#include <iostream>

namespace CBN {

// Structs.
std::vector<Material*> Material::list;
Material::Material() {
    list.push_back(this);
}
Material::~Material() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Material::getListSize() {
    return list.size();
}
Material* Material::getObject(int index) {
    return list[index];
}

std::vector<RoomTemplate*> RoomTemplate::list;
RoomTemplate::RoomTemplate() {
    list.push_back(this);
}
RoomTemplate::~RoomTemplate() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int RoomTemplate::getListSize() {
    return list.size();
}
RoomTemplate* RoomTemplate::getObject(int index) {
    return list[index];
}

std::vector<Room*> Room::list;
Room::Room() {
    list.push_back(this);
}
Room::~Room() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Room::getListSize() {
    return list.size();
}
Room* Room::getObject(int index) {
    return list[index];
}

std::vector<Grid*> Grid::list;
Grid::Grid() {
    list.push_back(this);
}
Grid::~Grid() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Grid::getListSize() {
    return list.size();
}
Grid* Grid::getObject(int index) {
    return list[index];
}

std::vector<LightTemplate*> LightTemplate::list;
LightTemplate::LightTemplate() {
    list.push_back(this);
}
LightTemplate::~LightTemplate() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int LightTemplate::getListSize() {
    return list.size();
}
LightTemplate* LightTemplate::getObject(int index) {
    return list[index];
}

std::vector<TempWayPoint*> TempWayPoint::list;
TempWayPoint::TempWayPoint() {
    list.push_back(this);
}
TempWayPoint::~TempWayPoint() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int TempWayPoint::getListSize() {
    return list.size();
}
TempWayPoint* TempWayPoint::getObject(int index) {
    return list[index];
}

std::vector<WayPoint*> WayPoint::list;
WayPoint::WayPoint() {
    list.push_back(this);
}
WayPoint::~WayPoint() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int WayPoint::getListSize() {
    return list.size();
}
WayPoint* WayPoint::getObject(int index) {
    return list[index];
}

std::vector<Screen*> Screen::list;
Screen::Screen() {
    list.push_back(this);
}
Screen::~Screen() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Screen::getListSize() {
    return list.size();
}
Screen* Screen::getObject(int index) {
    return list[index];
}

std::vector<TempScreen*> TempScreen::list;
TempScreen::TempScreen() {
    list.push_back(this);
}
TempScreen::~TempScreen() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int TempScreen::getListSize() {
    return list.size();
}
TempScreen* TempScreen::getObject(int index) {
    return list[index];
}

std::vector<SecurityCam*> SecurityCam::list;
SecurityCam::SecurityCam() {
    list.push_back(this);
}
SecurityCam::~SecurityCam() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int SecurityCam::getListSize() {
    return list.size();
}
SecurityCam* SecurityCam::getObject(int index) {
    return list[index];
}

std::vector<Prop*> Prop::list;
Prop::Prop() {
    list.push_back(this);
}
Prop::~Prop() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Prop::getListSize() {
    return list.size();
}
Prop* Prop::getObject(int index) {
    return list[index];
}

// Constants.
const int MaxRoomLights = 32;
const int MaxRoomEmitters = 8;
const int MaxRoomObjects = 30;
const int ROOM0 = 0;
const int ROOM1 = 1;
const int ROOM2 = 2;
const int ROOM2C = 3;
const int ROOM3 = 4;
const int ROOM4 = 5;
const int ZONE_LCZ = 1;
const int ZONE_HCZ = 2;
const int ZONE_EZ = 4;
const int MAP_SIZE = 19;
const int gridsz = 20;
const int LIGHTTYPE_POINT = 2;
const int LIGHTTYPE_SPOT = 3;

// Globals.
float RoomScale = 8.0 / 2048.0;
int RoomAmbience[20];
int Sky;
float HideDistance = 15.0;
float SecondaryLightOn;
int RemoteDoorOn;
int Contained106;
Screen* SelectedScreen;
SecurityCam* SelectedMonitor;
SecurityCam* CoffinCam;
int ScreenTexs[2];
IntArray* MapRooms = nullptr;

// Functions.
void LoadMaterials(String file) {
    String TemporaryString;
    Material* mat = nullptr;
    String StrTemp = "";

    int f = bbOpenFile(file);

    String stepSound = "";

    while (!bbEof(f)) {
        TemporaryString = bbReadLine(f).trim();
        if (TemporaryString.charAt(0) == '[') {
            TemporaryString = bbMid(TemporaryString, 2, TemporaryString.size() - 2);

            mat = new Material();

            mat->name = TemporaryString.toLower();

            mat->diff = 0;

            stepSound = GetINIString(file, TemporaryString, "stepsound");
            if (stepSound.toLower()=="metal") {
                mat->stepSound = STEPSOUND_METAL;
            }
        }
    }

    bbCloseFile(f);

}

String StripPath(String file) {
    String name = "";
    if (file.size() > 0) {
        for (int i = file.size(); i <= 1; i--) {

            String mi = bbMid(file,i,1);
            if (mi.equals('\\') || mi.equals('/')) {
                return name;
            }

            name = mi+name;
        }
    }
    return name;
}

RoomTemplate* CreateRoomTemplate(String meshpath) {
    RoomTemplate* rt = new RoomTemplate();

    rt->objPath = meshpath;
    rt->loaded = false;

    return rt;
}

void LoadRoomTemplates(String file) {
    String TemporaryString;
    int i;
    RoomTemplate* rt = nullptr;
    String StrTemp = "";
    String Zones = "";
    String AmountRange = "";
    String xRange;
    String yRange;

    int f = bbOpenFile(file);

    while (!bbEof(f)) {
        TemporaryString = bbReadLine(f).trim();
        if (TemporaryString.charAt(0) == '[') {
            std::cout << TemporaryString;
            TemporaryString = bbMid(TemporaryString, 2, TemporaryString.size() - 2);
            // TODO: Remove room ambience.
            if (!TemporaryString.toLower().equals("room ambience")) {
                StrTemp = GetINIString(file, TemporaryString, "meshpath");

                rt = CreateRoomTemplate(StrTemp);
                rt->name = TemporaryString.toLower();

                StrTemp = GetINIString(file, TemporaryString, "shape", "0").toLower();

                switch (StrTemp) {
                    case "room0", "0": {
                        rt->shape = ROOM0;
                    }
                    case "room1", "1": {
                        rt->shape = ROOM1;
                    }
                    case "room2", "2": {
                        rt->shape = ROOM2;
                    }
                    case "room2c", "2c": {
                        rt->shape = ROOM2C;
                    }
                    case "room3", "3": {
                        rt->shape = ROOM3;
                    }
                    case "room4", "4": {
                        rt->shape = ROOM4;
                    }
                    default: {
                        rt->shape = ROOM0;
                    }
                }

                Zones = GetINIString(file, TemporaryString, "zones").toLower();
                rt->zones = 0;
                if (bbInstr(Zones,"lcz")>0) {
                    rt->zones = rt->zones | ZONE_LCZ;
                }
                if (bbInstr(Zones,"hcz")>0) {
                    rt->zones = rt->zones | ZONE_HCZ;
                }
                if (bbInstr(Zones,"ez")>0) {
                    rt->zones = rt->zones | ZONE_EZ;
                }

                if (rt->shape!=ROOM0) {
                    rt->commonness = Max(Min(GetINIFloat(file, TemporaryString, "commonness"), 100), 0);

                    AmountRange = GetINIString(file, TemporaryString, "amount", "");
                    if (AmountRange.isEmpty()) {
                        rt->minAmount = -1;
                        rt->maxAmount = -1;
                    } else if ((bbInstr(AmountRange,"-")>0)) {
                        rt->minAmount = AmountRange.substr(0,AmountRange.findFirst("-")).toInt();
                        rt->maxAmount = (int)(bbMid(AmountRange,AmountRange.findFirst("-")+1));
                    } else {
                        rt->minAmount = AmountRange.toInt();
                        rt->maxAmount = rt->minAmount;
                    }

                    rt->large = GetINIInt(file, TemporaryString, "large");
                    rt->disableDecals = GetINIInt(file, TemporaryString, "disabledecals");

                    xRange = GetINIString(file, TemporaryString, "xrange");
                    yRange = GetINIString(file, TemporaryString, "yrange");

                    if (xRange.isEmpty()) {
                        xRange = "0-1";
                    }
                    if (yRange.isEmpty()) {
                        yRange = "0-1";
                    }

                    rt->xRangeStart = xRange.substr(0,bbInstr(xRange,"-")).toFloat();
                    rt->xRangeEnd = (float)(bbMid(xRange,bbInstr(xRange,"-")+1));

                    rt->yRangeStart = yRange.substr(0,bbInstr(yRange,"-")).toFloat();
                    rt->yrangeEnd = (float)(bbMid(yRange,bbInstr(yRange,"-")+1));
                } else {
                    rt->minAmount = 0;
                    rt->maxAmount = 0;
                    rt->zones = 0;
                    rt->commonness = 0;
                }

            }
        }
    }

    i = 1;
    while (true) {
        StrTemp = GetINIString(file, "room ambience", "ambience"+String(i));
        if (StrTemp.isEmpty()) {
            break;
        }

        RoomAmbience[i] = bbLoadSound(StrTemp);
        i++;
    }

    bbCloseFile(f);

}

void LoadRoomMesh(RoomTemplate* rt) {
    LoadRM2(rt);
}

void UpdateGrid(Grid* grid) {
    //local variables
    int tx;
    int ty;
    for (tx = 0; tx <= gridsz-1; tx++) {
        for (ty = 0; ty <= gridsz-1; ty++) {
            if (grid->entities[tx+(ty*gridsz)]!=0) {
                if (Abs(bbEntityY(mainPlayer->collider,true)-bbEntityY(grid->entities[tx+(ty*gridsz)],true))>4.0) {
                    break;
                }
                if (Abs(bbEntityX(mainPlayer->collider,true)-bbEntityX(grid->entities[tx+(ty*gridsz)],true))<HideDistance) {
                    if (Abs(bbEntityZ(mainPlayer->collider,true)-bbEntityZ(grid->entities[tx+(ty*gridsz)],true))<HideDistance) {
                        bbShowEntity(grid->entities[tx+(ty*gridsz)]);
                    } else {
                        bbHideEntity(grid->entities[tx+(ty*gridsz)]);
                    }
                } else {
                    bbHideEntity(grid->entities[tx+(ty*gridsz)]);
                }
            }
        }
    }
}

RoomTemplate* GetRoomTemplate(String name) {
    name = name.toLower();

    RoomTemplate* rt;
    for (int iterator71 = 0; iterator71 < RoomTemplate::getListSize(); iterator71++) {
        rt = RoomTemplate::getObject(iterator71);

        if (rt->name == name) {
            return rt;
        }
    }
}

int CountRooms(RoomTemplate* rt) {
    int count = 0;

    Room* r;
    for (int iterator72 = 0; iterator72 < Room::getListSize(); iterator72++) {
        r = Room::getObject(iterator72);

        if (r->roomTemplate == rt) {
            count = count+1;
        }
    }
    return count;
}

Room* CreateRoom(RoomTemplate* rt, float x, float y, float z) {
    Room* r = new Room();

    std::cout << "Placing "+rt->name;

    // TODO: do we still need this?
    r->zone = rt->zones;
    r->x = x;
    r->y = y;
    r->z = z;

    r->roomTemplate = rt;

    if (!rt->loaded) {
        LoadRoomMesh(rt);
    }

    int tempObj;
    Prop* tempProp;

    r->obj = bbCreatePivot();
    r->opaqueMesh = bbCopyEntity(rt->opaqueMesh);
    bbScaleEntity(r->opaqueMesh, RoomScale, RoomScale, RoomScale);
    bbEntityParent(r->opaqueMesh,r->obj);
    bbShowEntity(r->opaqueMesh);
    if (rt->alphaMesh!=0) {
        r->alphaMesh = bbCopyEntity(rt->alphaMesh);
        bbScaleEntity(r->alphaMesh, RoomScale, RoomScale, RoomScale);
        bbShowEntity(r->alphaMesh);
        bbEntityParent(r->alphaMesh,r->obj);
    }
    r->collisionObjs = CreateIntArray(rt->collisionObjs->size);
    int i;
    for (i = 0; i <= rt->collisionObjs->size-1; i++) {
        tempObj = bbCopyEntity(GetIntArrayListElem(rt->collisionObjs,i));
        bbScaleEntity(tempObj, RoomScale, RoomScale, RoomScale);
        SetIntArrayElem(r->collisionObjs,tempObj,i);
        bbShowEntity(tempObj);
        bbEntityAlpha(tempObj,0.0);
        bbEntityParent(tempObj,r->obj);
    }
    if (rt->props!=nullptr) {
        r->props = CreateIntArray(rt->props->size);
        for (i = 0; i <= rt->props->size-1; i++) {
            tempProp = Object.Prop(GetIntArrayListElem(rt->props,i));
            tempObj = bbCopyEntity(tempProp->obj);
            SetIntArrayElem(r->props,tempObj,i);
            bbPositionEntity(tempObj,tempProp->x*RoomScale,tempProp->y*RoomScale,tempProp->z*RoomScale);
            bbRotateEntity(tempObj,tempProp->pitch,tempProp->yaw,tempProp->roll);
            bbScaleEntity(tempObj,tempProp->xScale*RoomScale,tempProp->yScale*RoomScale,tempProp->zScale*RoomScale);
            bbShowEntity(tempObj);
            bbEntityParent(tempObj,r->obj);
        }
    }

    bbPositionEntity(r->obj, x, y, z);
    FillRoom(r);

    return r;
}

void FillRoom(Room* r) {
    switch (r->roomTemplate->name) {
        case "test_860_2": {
            FillRoom_test_860_2(r);
        }
        case "lck_cam_2c": {
            FillRoom_lck_cam_2c(r);
        }
        case "lck_096_2c": {
            FillRoom_lck_096_2c(r);
        }
        case "extend_gatea_1": {
            FillRoom_extend_gatea_1(r);
        }
        case "exit_gatea_1": {
            FillRoom_exit_gatea_1(r);
        }
        case "exit1": {
            FillRoom_exit_gateb_1(r);
        }
        case "cont_079_1": {
            FillRoom_cont_079_1(r);
        }
        case "hll_gas_2": {
            FillRoom_hll_gas_2(r);
        }
        case "test_smallwindow_2": {
            FillRoom_test_smallwindow_2(r);
        }
        case "tnnl_plain_3": {
            FillRoom_tnnl_plain_3(r);
        }
        case "hll_toilets_2": {
            FillRoom_hll_toilets_2(r);
        }
        case "scp_970_2": {
            FillRoom_scp_970_2(r);
        }
        case "off_rosewood_2": {
            FillRoom_off_rosewood_2(r);
        }
        case "off_gears_may_har_2": {
            FillRoom_off_gears_may_har_2(r);
        }
        case "off_l_conf_2": {
            FillRoom_off_l_conf_2(r);
        }
        case "hll_ele_2": {
            FillRoom_hll_ele_2(r);
        }
        case "hll_caf_2": {
            FillRoom_hll_caf_2(r);
        }
        case "tnnl_nuke_2": {
            FillRoom_tnnl_nuke_2(r);
        }
        case "tnnl_maintenance_2": {
            FillRoom_tnnl_maintenance_2(r);
        }
        case "cont_008_1": {
            FillRoom_cont_008_1(r);
        }
        case "cont_035_1": {
            FillRoom_cont_035_1(r);
        }
        case "cont_513_3": {
            FillRoom_cont_513_3(r);
        }
        case "cont_966_3": {
            FillRoom_cont_966_3(r);
        }
        case "strg_939_3": {
            FillRoom_strg_939_3(r);
        }
        case "cont_049_2": {
            FillRoom_cont_049_2(r);
        }
        case "hll_fan_2": {
            FillRoom_hll_fan_2(r);
        }
        case "cont_012_2": {
            FillRoom_cont_012_2(r);
        }
        case "tnnl_elec_2": {
            FillRoom_tnnl_elec_2(r);
        }
        case "tnnl_pipes_2": {
            FillRoom_tnnl_pipes_2(r);
        }
        case "hll_gas_3": {
            FillRoom_hll_gas_3(r);
        }
        case "srvr_096_2": {
            FillRoom_srvr_096_2(r);
        }
        case "srvr_farm_3": {
            FillRoom_srvr_farm_3(r);
        }
        case "srvr_lshape_3": {
            FillRoom_srvr_lshape_3(r);
        }
        case "test_682_2": {
            FillRoom_test_682_2(r);
        }
        case "closets_2": {
            FillRoom_closets_2(r);
        }
        case "off_plain_2": {
            FillRoom_off_plain_2(r);
        }
        case "off_lower_2": {
            FillRoom_off_lower_2(r);
        }
        case "off_2level_2": {
            FillRoom_off_2level_2(r);
        }
        case "cont_173_1": {
            FillRoom_cont_173_1(r);
        }
        case "cont_714_860_1025_2": {
            FillRoom_cont_714_860_1025_2(r);
        }
        case "cont_205_1": {
            FillRoom_cont_205_1(r);
        }
        case "end_1": {
            FillRoom_end_1(r);
        }
        case "coffin": {
            FillRoom_cont_895_1(r);
        }
        case "tsl_ez_2","tsl_lcz_2","tsl_hcz_2": {
            FillRoom_hll_tsl(r);
        }
        case "lck_tshape_2": {
            FillRoom_lck_tshape_2(r);
        }
        case "cont_914_1": {
            FillRoom_cont_914_1(r);
        }
        case "roomintro": {
            FillRoom_intro(r);
        }
        case "strg_elec_2c": {
            FillRoom_strg_elec_2c(r);
        }
        case "cont_106_1": {
            FillRoom_cont_106_1(r);
        }
        case "cont_1123_2": {
            FillRoom_cont_1123_2(r);
        }
        case "pocketdimension": {
            FillRoom_pocketdimension(r);
        }
        case "hll_bench_3": {
            FillRoom_hll_bench_3(r);
        }
        case "lifts_1": {
            FillRoom_lifts_1(r);
        }
        case "srvr_pc_2": {
            FillRoom_srvr_pc_2(r);
        }
        case "lck_air_2","lck_air_broke_2": {
            FillRoom_lck_air_2(r);
        }
        case "lck_ez_3": {
            FillRoom_lck_ez_3(r);
        }
        case "cont_1162_2c": {
            FillRoom_cont_1162_2c(r);
        }
        case "cont_500_1499_2": {
            FillRoom_cont_500_1499_2(r);
        }
        case "off_glss_3": {
            FillRoom_off_glss_3(r);
        }
        case "off_bain_2": {
            FillRoom_off_bain_2(r);
        }
        case "hll_sl_2": {
            FillRoom_hll_sl_2(r);
        }
        case "hll_lshape_2": {
            FillRoom_hll_lshape_2(r);
        }
        case "hll_dirty_3": {
            FillRoom_hll_dirty_3(r);
        }
        case "lck_broke_2c": {
            FillRoom_lck_broke_2c(r);
        }
    }

    LightTemplate* lt;
    int newlt;
    for (int iterator73 = 0; iterator73 < LightTemplate::getListSize(); iterator73++) {
        lt = LightTemplate::getObject(iterator73);

        if (lt->roomtemplate == r->roomTemplate) {
            newlt = AddLight(r, r->x+lt->x*RoomScale, r->y+lt->y*RoomScale, r->z+lt->z*RoomScale, lt->ltype, lt->range, lt->r, lt->g, lt->b);
            if (newlt != 0) {
                if (lt->ltype == 3) {
                    bbLightConeAngles(newlt, lt->innerconeangle, lt->outerconeangle);
                    bbRotateEntity(newlt, lt->pitch, lt->yaw, 0);
                }
            }
        }
    }

    TempScreen* ts;
    for (int iterator74 = 0; iterator74 < TempScreen::getListSize(); iterator74++) {
        ts = TempScreen::getObject(iterator74);

        if (ts->roomtemplate == r->roomTemplate) {
            CreateScreen(r->x+ts->x*RoomScale, r->y+ts->y*RoomScale, r->z+ts->z*RoomScale, ts->imgpath, r);
        }
    }

    IntArrayList* waypoints = CreateIntArrayList();
    WayPoint* waypoint;
    TempWayPoint* tw;
    for (int iterator75 = 0; iterator75 < TempWayPoint::getListSize(); iterator75++) {
        tw = TempWayPoint::getObject(iterator75);

        if (tw->roomtemplate == r->roomTemplate) {
            waypoint = CreateWaypoint(r->x+tw->x*RoomScale, r->y+tw->y*RoomScale, r->z+tw->z*RoomScale, r);
            PushIntArrayListElem(waypoints,Handle(waypoint));
        }
    }

    int i = 0;
    int j;
    for (int iterator76 = 0; iterator76 < TempWayPoint::getListSize(); iterator76++) {
        tw = TempWayPoint::getObject(iterator76);

        if (tw->roomtemplate == r->roomTemplate) {
            waypoint = Object.WayPoint(GetIntArrayListElem(waypoints,i));
            for (j = 0; j <= 15; j++) {
                if (tw->connectedTo[j]==0) {
                    break;
                }
                waypoint->connected[j] = Object.WayPoint(GetIntArrayListElem(waypoints,tw->connectedTo[j]-1));
                waypoint->dist[j] = bbEntityDistance(waypoint->obj,waypoint->connected[j]->obj);
            }
            i = i+1;
        }
    }

    DeleteIntArrayList(waypoints);

    //	If r\roomTemplate\tempTriggerboxAmount > 0
    //		r\triggerboxAmount = r\roomTemplate\tempTriggerboxAmount
    //		For i = 0 To r\triggerboxAmount-1
    //			r\triggerbox[i] = CopyEntity(r\roomTemplate\tempTriggerbox[i],r\obj)
    //			EntityAlpha(r\triggerbox[i],0.0)
    //			r\triggerboxName[i] = r\roomTemplate\tempTriggerboxName[i]
    //			DebugLog("Triggerbox found: "+i)
    //			DebugLog("Triggerbox "+i+" name: "+r\triggerboxName[i])
    //		Next
    //	EndIf

    for (i = 0; i <= MaxRoomEmitters-1; i++) {
        if (r->roomTemplate->tempSoundEmitter[i]!=0) {
            r->soundEmitterObj[i] = bbCreatePivot(r->obj);
            bbPositionEntity(r->soundEmitterObj[i], r->x+r->roomTemplate->tempSoundEmitterX[i]*RoomScale,r->y+r->roomTemplate->tempSoundEmitterY[i]*RoomScale,r->z+r->roomTemplate->tempSoundEmitterZ[i]*RoomScale,true);
            bbEntityParent(r->soundEmitterObj[i],r->obj);

            r->soundEmitter[i] = r->roomTemplate->tempSoundEmitter[i];
            r->soundEmitterRange[i] = r->roomTemplate->tempSoundEmitterRange[i];
        }
    }

}

void SetRoomVisibility(Room* r, int on) {
    if (on) {
        bbShowEntity(r->opaqueMesh);
        if (r->alphaMesh!=0) {
            bbShowEntity(r->alphaMesh);
        }
    } else {
        bbHideEntity(r->opaqueMesh);
        if (r->alphaMesh!=0) {
            bbHideEntity(r->alphaMesh);
        }
    }
}

void UpdateRooms() {
    float dist;
    int i;
    int j;
    Room* r;
    float minDist;

    float x;
    float z;
    int hide = true;

    //PlayerZone=Min(Max(GetZone(EntityZ(mainPlayer\collider)/8.0),0),ZONEAMOUNT-1)

    //TempLightVolume=0
    int foundPlayerRoom = false;
    if (mainPlayer->currRoom!=nullptr) {
        if (Abs(bbEntityY(mainPlayer->collider) - bbEntityY(mainPlayer->currRoom->obj)) < 1.5) {
            x = Abs(mainPlayer->currRoom->x-bbEntityX(mainPlayer->collider,true));
            if (x < 4.0) {
                z = Abs(mainPlayer->currRoom->z-bbEntityZ(mainPlayer->collider,true));
                if (z < 4.0) {
                    foundPlayerRoom = true;
                }
            }

            //it's likely that an adjacent room is the new player room, check for that
            if (foundPlayerRoom == false) {
                for (i = 0; i <= 3; i++) {
                    if (mainPlayer->currRoom->adjacent[i]!=nullptr) {
                        x = Abs(mainPlayer->currRoom->adjacent[i]->x-bbEntityX(mainPlayer->collider,true));
                        if (x < 4.0) {
                            z = Abs(mainPlayer->currRoom->adjacent[i]->z-bbEntityZ(mainPlayer->collider,true));
                            if (z < 4.0) {
                                foundPlayerRoom = true;
                                mainPlayer->currRoom = mainPlayer->currRoom->adjacent[i];
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            //mainPlayer\currRoom stays the same when you're high up, or deep down
            foundPlayerRoom = true;
        }
    } else {
        minDist = 999.0;
        for (int iterator77 = 0; iterator77 < Room::getListSize(); iterator77++) {
            r = Room::getObject(iterator77);

            x = Abs(r->x-bbEntityX(mainPlayer->collider,true));
            z = Abs(r->z-bbEntityZ(mainPlayer->collider,true));
            r->dist = Max(x,z);

            if (r->dist<minDist) {
                mainPlayer->currRoom = r;
                minDist = r->dist;
            }
        }
        return;
    }

    for (int iterator78 = 0; iterator78 < Room::getListSize(); iterator78++) {
        r = Room::getObject(iterator78);

        x = Abs(r->x-bbEntityX(mainPlayer->collider,true));
        z = Abs(r->z-bbEntityZ(mainPlayer->collider,true));
        r->dist = Max(x,z);


        if (x<16 & z < 16) {
            for (i = 0; i <= MaxRoomEmitters-1; i++) {
                if (r->soundEmitter[i]!=0) {
                    dist = bbEntityDistance(r->soundEmitterObj[i],mainPlayer->collider);
                    if (dist < r->soundEmitterRange[i]) {
                        r->soundEmitterCHN[i] = LoopRangedSound(RoomAmbience[r->soundEmitter[i]],r->soundEmitterCHN[i], mainPlayer->cam, r->soundEmitterObj[i],r->soundEmitterRange[i]);
                    }
                }
            }

            if ((!foundPlayerRoom) & (mainPlayer->currRoom!=r)) {
                if (x < 4.0) {
                    if (z < 4.0) {
                        if (Abs(bbEntityY(mainPlayer->collider) - bbEntityY(r->obj)) < 1.5) {
                            mainPlayer->currRoom = r;
                        }
                        foundPlayerRoom = true;
                    }
                }
            }
        }

        hide = true;

        if (r==mainPlayer->currRoom) {
            hide = false;
        }
        if (hide) {
            if (IsRoomAdjacent(mainPlayer->currRoom,r)) {
                hide = false;
            }
        }
        if (hide) {
            for (i = 0; i <= 3; i++) {
                if (IsRoomAdjacent(mainPlayer->currRoom->adjacent[i],r)) {
                    hide = false;
                    break;
                }
            }
        }

        if (hide) {
            bbHideEntity(r->obj);
        } else {
            bbShowEntity(r->obj);
            for (i = 0; i <= MaxRoomLights-1; i++) {
                if (r->lights[i] != 0) {
                    dist = bbEntityDistance(mainPlayer->collider,r->lights[i]);
                    if (dist < HideDistance) {
                        //TempLightVolume = TempLightVolume + r\lightIntensity[i]*r\lightIntensity[i]*((HideDistance-dist)/HideDistance)
                        //ShowEntity(r\lights[i])
                    }
                } else {
                    break;
                }
            }
        }
    }

    //MapFound(Floor(EntityX(mainPlayer\currRoom\obj) / 8.0), Floor(EntityZ(mainPlayer\currRoom\obj) / 8.0)) = 1
    mainPlayer->currRoom->found = true;

    //TempLightVolume = Max(TempLightVolume / 4.5, 1.0)

    if (mainPlayer->currRoom!=nullptr) {
        SetRoomVisibility(mainPlayer->currRoom,true);
        for (i = 0; i <= 3; i++) {
            if (mainPlayer->currRoom->adjacent[i]!=nullptr) {
                x = Abs(bbEntityX(mainPlayer->collider,true)-bbEntityX(mainPlayer->currRoom->adjDoor[i]->frameobj,true));
                z = Abs(bbEntityZ(mainPlayer->collider,true)-bbEntityZ(mainPlayer->currRoom->adjDoor[i]->frameobj,true));
                if (mainPlayer->currRoom->adjDoor[i]->openstate == 0) {
                    SetRoomVisibility(mainPlayer->currRoom->adjacent[i],false);
                } else if ((!bbEntityInView(mainPlayer->currRoom->adjDoor[i]->frameobj,mainPlayer->cam))) {
                    SetRoomVisibility(mainPlayer->currRoom->adjacent[i],false);
                } else {
                    SetRoomVisibility(mainPlayer->currRoom->adjacent[i],true);
                }

                for (j = 0; j <= 3; j++) {
                    if (mainPlayer->currRoom->adjacent[i]->adjacent[j]!=nullptr) {
                        if (mainPlayer->currRoom->adjacent[i]->adjacent[j]!=mainPlayer->currRoom) {
                            SetRoomVisibility(mainPlayer->currRoom->adjacent[i]->adjacent[j],false);
                        }
                    }
                }
            }
        }
    }

}

int IsRoomAdjacent(Room* thisRoom, Room* that) {
    if (thisRoom==nullptr) {
        return false;
    }
    if (thisRoom==that) {
        return true;
    }
    for (int i = 0; i <= 3; i++) {
        if (that==thisRoom->adjacent[i]) {
            return true;
        }
    }
    return false;
}

int AddLight(Room* room, float x, float y, float z, int ltype, float range, int r, int g, int b) {
    int i;
    //TODO: These names suck.
    int light;
    int sprite;
    int lightSpriteTex = GrabTexture("GFX/Sprites/light_flare.jpg");

    if (room!=nullptr) {
        for (i = 0; i <= MaxRoomLights-1; i++) {
            if (room->lights[i]==0) {
                room->lights[i] = bbCreateLight(ltype);
                //room\lightDist[i] = range
                bbLightRange(room->lights[i],range);
                bbLightColor(room->lights[i],r,g,b);
                bbPositionEntity(room->lights[i],x,y,z,true);
                bbEntityParent(room->lights[i],room->obj);

                room->lightIntensity[i] = (r+g+b)/255.0/3.0;

                room->lightSprites[i] = bbCreateSprite();
                bbPositionEntity(room->lightSprites[i], x, y, z);
                bbScaleSprite(room->lightSprites[i], 0.13 , 0.13);
                bbEntityTexture(room->lightSprites[i], lightSpriteTex);
                bbEntityBlend(room->lightSprites[i], 3);

                bbEntityParent(room->lightSprites[i], room->obj);

                room->lightSpritesPivot[i] = bbCreatePivot();
                bbEntityRadius(room->lightSpritesPivot[i],0.05);
                bbPositionEntity(room->lightSpritesPivot[i], x, y, z);
                bbEntityParent(room->lightSpritesPivot[i], room->obj);

                bbHideEntity(room->lights[i]);

                return room->lights[i];
            }
        }
    } else {
        light = bbCreateLight(ltype);
        bbLightRange(light,range);
        bbLightColor(light,r,g,b);
        bbPositionEntity(light,x,y,z,true);
        sprite = bbCreateSprite();
        bbPositionEntity(sprite, x, y, z);
        bbScaleSprite(sprite, 0.13 , 0.13);
        bbEntityTexture(sprite, lightSpriteTex);
        bbEntityBlend(sprite, 3);
        return light;
    }
    DropAsset(lightSpriteTex);
}

LightTemplate* AddTempLight(RoomTemplate* rt, float x, float y, float z, int ltype, float range, int r, int g, int b) {
    LightTemplate* lt = new LightTemplate();
    lt->roomtemplate = rt;
    lt->x = x;
    lt->y = y;
    lt->z = z;
    lt->ltype = ltype;
    lt->range = range;
    lt->r = r;
    lt->g = g;
    lt->b = b;

    return lt;
}

WayPoint* CreateWaypoint(float x, float y, float z, Room* room) {
    WayPoint* w = new WayPoint();

    w->obj = bbCreatePivot();
    bbPositionEntity(w->obj, x,y,z);

    bbEntityParent(w->obj, room->obj);

    w->room = room;

    return w;
}

void InitWayPoints(int loadingstart = 45) {

    Door* d;
    WayPoint* w;
    WayPoint* w2;
    Room* r;
    Room* ClosestRoom;

    float x;
    float y;
    float z;
    int i;
    int tline;

    int temper = bbMilliSecs();

    float dist;
    float dist2;

    for (int iterator79 = 0; iterator79 < WayPoint::getListSize(); iterator79++) {
        w = WayPoint::getObject(iterator79);

        bbEntityPickMode(w->obj, 0, 0);
        bbEntityRadius(w->obj, 0);

        for (i = 0; i <= 4; i++) {
            if (w->connected[i]!=nullptr) {
                tline = CreateLine(bbEntityX(w->obj,true),bbEntityY(w->obj,true),bbEntityZ(w->obj,true),bbEntityX(w->connected[i]->obj,true),bbEntityY(w->connected[i]->obj,true),bbEntityZ(w->connected[i]->obj,true));
                bbEntityColor(tline, 255,0,0);
                bbEntityParent(tline, w->obj);
            }
        }
    }

    std::cout << "InitWaypoints() - "+String(TimeInPosMilliSecs()-temper);

}

void RemoveWaypoint(WayPoint* w) {
    bbFreeEntity(w->obj);
    delete w;
}

int FindPath(NPC* n, float x, float y, float z) {

    std::cout << "findpath: "+String(n->npcType);

    int temp;
    float dist;
    float dist2;
    float xtemp;
    float ytemp;
    float ztemp;
    float gtemp;

    WayPoint* w;
    WayPoint* StartPoint;
    WayPoint* EndPoint;
    WayPoint* currpoint;
    WayPoint* twentiethpoint;

    int length = 0;

    int StartX = (int)(bbFloor(bbEntityX(n->collider,true) / 8.0 + 0.5));
    int StartZ = (int)(bbFloor(bbEntityZ(n->collider,true) / 8.0 + 0.5));
    //If (StartX < 0 Or StartX > MapWidth) Then Return 2
    //If (StartZ < 0 Or StartZ > MapWidth) Then Return 2

    int EndX = (int)(bbFloor(x / 8.0 + 0.5));
    int EndZ = (int)(bbFloor(z / 8.0 + 0.5));
    //If (EndX < 0 Or EndX > MapWidth) Then Return 2
    //If (EndZ < 0 Or EndZ > MapWidth) Then Return 2

    int CurrX;
    int CurrZ;

    //pathstatus = 0, ei ole etsitty reitti�
    //pathstatus = 1, reitti l�ydetty
    //pathstatus = 2, reitti� ei ole olemassa

    for (int iterator80 = 0; iterator80 < WayPoint::getListSize(); iterator80++) {
        w = WayPoint::getObject(iterator80);

        w->state = 0;
        w->fCost = 0;
        w->gCost = 0;
        w->hCost = 0;
    }

    n->pathStatus = 0;
    n->pathLocation = 0;

    int i;
    for (i = 0; i <= 19; i++) {
        n->path[i] = nullptr;
    }

    int pvt = bbCreatePivot();
    bbPositionEntity(pvt, x,y,z, true);

    temp = bbCreatePivot();
    bbPositionEntity(temp, bbEntityX(n->collider,true), bbEntityY(n->collider,true)+0.15, bbEntityZ(n->collider,true));

    dist = 350.0;
    for (int iterator81 = 0; iterator81 < WayPoint::getListSize(); iterator81++) {
        w = WayPoint::getObject(iterator81);

        xtemp = bbEntityX(w->obj,true)-bbEntityX(temp,true);
        //If (xtemp < 8.0) Then
        ztemp = bbEntityZ(w->obj,true)-bbEntityZ(temp,true);
        //If (ztemp < 8.0) Then
        ytemp = bbEntityY(w->obj,true)-bbEntityY(temp,true);
        //If (ytemp < 8.0) Then
        dist2 = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp);
        if (dist2 < dist) {
            //prefer waypoints that are visible
            if (!bbEntityVisible(w->obj, temp)) {
                dist2 = dist2*3;
            }
            if (dist2 < dist) {
                dist = dist2;
                StartPoint = w;
            }
        }
        //EndIf
        //EndIf
        //EndIf
    }
    std::cout << "DIST: "+String(dist);

    bbFreeEntity(temp);

    if (StartPoint == nullptr) {
        return 2;
    }
    StartPoint->state = 1;

    //If (EndPoint = Null) Then
    EndPoint = nullptr;
    dist = 400.0;
    for (int iterator82 = 0; iterator82 < WayPoint::getListSize(); iterator82++) {
        w = WayPoint::getObject(iterator82);

        xtemp = bbEntityX(pvt,true)-bbEntityX(w->obj,true);
        //If (xtemp =< 8.0) Then
        ztemp = bbEntityZ(pvt,true)-bbEntityZ(w->obj,true);
        //If (ztemp =< 8) Then
        ytemp = bbEntityY(pvt,true)-bbEntityY(w->obj,true);
        dist2 = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp);

        // And EntityVisible(w\obj, pvt)
        if (dist2 < dist) {
            dist = dist2;
            EndPoint = w;
        }
        //EndIf
        //EndIf
    }
    //EndIf

    bbFreeEntity(pvt);

    if (EndPoint == StartPoint) {
        if (dist < 0.4) {
            return 0;
        } else {
            n->path[0] = EndPoint;
            return 1;
        }
    }
    if (EndPoint == nullptr) {
        return 2;
    }

    //aloitus- ja lopetuspisteet l�ydetty, aletaan etsi� reitti�

    WayPoint* smallest;
    do {
        temp = false;
        smallest = nullptr;
        dist = 10000.0;
        for (int iterator83 = 0; iterator83 < WayPoint::getListSize(); iterator83++) {
            w = WayPoint::getObject(iterator83);

            if (w->state == 1) {
                temp = true;
                if ((w->fCost) < dist) {
                    dist = w->fCost;
                    smallest = w;
                }
            }
        }

        if (smallest != nullptr) {

            w = smallest;
            w->state = 2;

            for (i = 0; i <= 15; i++) {
                if (w->connected[i]!=nullptr) {
                    if (w->connected[i]->state < 2) {

                        //open list
                        if (w->connected[i]->state==1) {
                            gtemp = w->gCost+w->dist[i];
                            //TODO: fix?
                            //If (n\npcType = NPCtypeMTF) Then
                            //	If (w\connected[i]\door = Null) Then gtemp = gtemp + 0.5
                            //EndIf
                            //parempi reitti -> overwrite
                            if (gtemp < w->connected[i]->gCost) {
                                w->connected[i]->gCost = gtemp;
                                w->connected[i]->fCost = w->connected[i]->gCost + w->connected[i]->hCost;
                                w->connected[i]->parent = w;
                            }
                        } else {
                            w->connected[i]->hCost = Abs(bbEntityX(w->connected[i]->obj,true)-bbEntityX(EndPoint->obj,true))+Abs(bbEntityZ(w->connected[i]->obj,true)-bbEntityZ(EndPoint->obj,true));
                            gtemp = w->gCost+w->dist[i];
                            //TODO: fix?
                            //If (n\npcType = NPCtypeMTF) Then
                            //	If (w\connected[i]\door = Null) Then gtemp = gtemp + 0.5
                            //EndIf
                            w->connected[i]->gCost = gtemp;
                            w->connected[i]->fCost = w->gCost+w->hCost;
                            w->connected[i]->parent = w;
                            w->connected[i]->state = 1;
                        }
                    }

                }
            }
            //open listilt� ei l�ytynyt mit��n
        } else {
            if (EndPoint->state > 0) {
                StartPoint->parent = nullptr;
                EndPoint->state = 2;
                break;
            }
        }

        if (EndPoint->state > 0) {
            StartPoint->parent = nullptr;
            EndPoint->state = 2;
            break;
        }

    } while (temp = false);

    if (EndPoint->state > 0) {

        currpoint = EndPoint;
        twentiethpoint = EndPoint;

        length = 0;
        do {
            length++;
            currpoint = currpoint->parent;
            if (length>20) {
                twentiethpoint = twentiethpoint->parent;
            }
        } while (currpoint = nullptr);

        currpoint = EndPoint;
        while (twentiethpoint!=nullptr) {
            length = (int)(Min(length-1,19));
            //DebugLog("LENGTH "+length)
            twentiethpoint = twentiethpoint->parent;
            n->path[length] = twentiethpoint;
        }

        return 1;
        //RuntimeError(length)
        //      For i = 0 To (length-1)
        //         temp =False
        //         If (length < 20) Then
        //            n\path[length-1-i] = currpoint.WayPoint
        //         Else
        //            If (i < 20) Then
        //               n\path[20-1-i] = w.WayPoint
        //            Else
        //               ;Return 1
        //            EndIf
        //         EndIf
        //
        //         If (currpoint = StartPoint) Then Return 1
        //
        //         If (currpoint\parent <> Null) Then
        //            currpoint = currpoint\parent
        //         Else
        //            Exit
        //         EndIf
        //
        //      Next

    } else {
        std::cout << "FUNCTION FindPath() - reitti� ei l�ytynyt";
        //reitti� m��r�np��h�n ei l�ytynyt
        return 2;
    }

}

int CreateLine(float x1, float y1, float z1, float x2, float y2, float z2, int mesh = 0) {
    int surf;
    int verts;

    if (mesh == 0) {
        mesh = bbCreateMesh();
        bbEntityFX(mesh,16);
        surf = bbCreateSurface(mesh);
        verts = 0;

        bbAddVertex(surf,x1,y1,z1,0,0);
    } else {
        surf = bbGetSurface(mesh,1);
        verts = bbCountVertices(surf)-1;
    }

    bbAddVertex(surf,(x1+x2)/2,(y1+y2)/2,(z1+z2)/2,0,0);
    // you could skip creating the above vertex and change the line below to
    // AddTriangle(surf,verts,verts+1,verts+0)
    // so your line mesh would use less vertices, the drawback is that some videocards (like the matrox g400)
    // aren't able to create a triangle with 2 vertices. so, it's your call :)
    bbAddVertex(surf,x2,y2,z2,1,0);

    bbAddTriangle(surf,verts,verts+2,verts+1);

    return mesh;
}

Screen* CreateScreen(float x, float y, float z, String imgpath, Room* r) {
    Screen* s = new Screen();
    s->obj = bbCreatePivot();
    bbEntityPickMode(s->obj, 1);
    bbEntityRadius(s->obj, 0.1);

    bbPositionEntity(s->obj, x,y,z);
    s->imgpath = imgpath;
    s->room = r;
    bbEntityParent(s->obj, r->obj);

    return s;
}

void UpdateScreens() {
    if (SelectedScreen != nullptr) {
        return;
    }
    if (mainPlayer->selectedDoor != nullptr) {
        return;
    }

    Screen* s;
    for (int iterator84 = 0; iterator84 < Screen::getListSize(); iterator84++) {
        s = Screen::getObject(iterator84);

        if (s->room == mainPlayer->currRoom) {
            if (bbEntityDistance(mainPlayer->collider,s->obj)<1.2) {
                bbEntityPick(mainPlayer->cam, 1.2);
                if (bbPickedEntity()==s->obj & s->imgpath!="") {
                    DrawHandIcon = true;
                    if (MouseUp1) {
                        SelectedScreen = s;
                        s->img = bbLoadImage("GFX/screens/"+s->imgpath);
                        s->img = ResizeImage2(s->img, (int)(bbImageWidth(s->img) * MenuScale), (int)(bbImageHeight(s->img) * MenuScale));
                        bbMaskImage(s->img, 255,0,255);
                        PlaySound_SM(sndManager->button);
                        MouseUp1 = false;
                    }
                }
            }
        }
    }

}

SecurityCam* CreateSecurityCam(float x, float y, float z, Room* r, int screen = false) {
    SecurityCam* sc = new SecurityCam();
    float scale;

    sc->obj = bbCopyEntity(CamBaseOBJ);
    bbScaleEntity(sc->obj, 0.0015, 0.0015, 0.0015);
    sc->cameraObj = bbCopyEntity(CamOBJ);
    bbScaleEntity(sc->cameraObj, 0.01, 0.01, 0.01);

    sc->room = r;

    sc->screen = screen;
    if (screen) {
        sc->allowSaving = true;

        sc->renderInterval = 12;

        scale = RoomScale * 4.5 * 0.4;

        sc->scrObj = bbCreateSprite();
        bbEntityFX(sc->scrObj, 17);
        bbSpriteViewMode(sc->scrObj, 2);
        sc->scrTexture = 0;
        bbEntityTexture(sc->scrObj, ScreenTexs[sc->scrTexture]);
        bbScaleSprite(sc->scrObj, bbMeshWidth(Monitor) * scale * 0.95 * 0.5, bbMeshHeight(Monitor) * scale * 0.95 * 0.5);

        sc->scrOverlay = bbCreateSprite(sc->scrObj);
        //	scaleSprite(sc\scrOverlay , 0.5, 0.4)
        bbScaleSprite(sc->scrOverlay, bbMeshWidth(Monitor) * scale * 0.95 * 0.5, bbMeshHeight(Monitor) * scale * 0.95 * 0.5);
        bbMoveEntity(sc->scrOverlay, 0, 0, -0.0005);
        bbEntityTexture(sc->scrOverlay, MonitorTexture);
        bbSpriteViewMode(sc->scrOverlay, 2);
        bbEntityBlend(sc->scrOverlay , 3);

        sc->monitorObj = bbCopyEntity(Monitor, sc->scrObj);

        bbScaleEntity(sc->monitorObj, scale, scale, scale);

        sc->cam = bbCreateCamera();
        bbCameraViewport(sc->cam, 0, 0, 512, 512);
        bbCameraRange(sc->cam, 0.05, 6.0);
        bbCameraZoom(sc->cam, 0.8);
        bbHideEntity(sc->cam);
    }

    bbPositionEntity(sc->obj, x, y, z);

    if (r!=nullptr) {
        bbEntityParent(sc->obj, r->obj);
    }

    return sc;
}

void UpdateSecurityCams() {
    SecurityCam* sc;
    int close;
    float temp;
    int pvt;
    int i;
    int gorePics[GORE_PIC_COUNT];
    int aiPic;

    PlayerDetected = false;

    //coffineffect = 0, not affected by 895
    //coffineffect = 1, constantly affected by 895
    //coffineffect = 2, 079 can broadcast 895 feed on this screen
    //coffineffect = 3, 079 broadcasting 895 feed

    for (int iterator85 = 0; iterator85 < SecurityCam::getListSize(); iterator85++) {
        sc = SecurityCam::getObject(iterator85);

        close = false;
        if (sc->room == nullptr & (!sc->specialCam)) {
            bbHideEntity(sc->cam);
        } else {
            if (!sc->specialCam) {
                if (sc->room->dist < 6.0 | mainPlayer->currRoom==sc->room) {
                    close = true;
                } else if ((sc->isRoom2slCam)) {
                    close = true;
                } else if ((sc->cam!=0)) {
                    bbHideEntity(sc->cam);
                }
            }

            if (sc->isRoom2slCam) {
                sc->coffinEffect = 0;
            }
            if (sc->room != nullptr) {
                if (sc->room->roomTemplate->name == "hll_sl_2") {
                    sc->coffinEffect = 0;
                }
            }
            if (sc->specialCam) {
                sc->coffinEffect = 0;
            }

            if (close | sc==CoffinCam | sc->isRoom2slCam) {
                if (sc->followPlayer) {
                    if (sc!=CoffinCam) {
                        if (bbEntityVisible(sc->cameraObj,mainPlayer->cam)) {
                            PlayerDetected = true;
                        }
                    }
                    bbPointEntity(sc->cameraObj, mainPlayer->cam);
                    temp = bbEntityPitch(sc->cameraObj);
                    bbRotateEntity(sc->obj, 0, CurveAngle(bbEntityYaw(sc->cameraObj), bbEntityYaw(sc->obj), 75.0), 0);

                    if (temp < 40.0) {
                        temp = 40;
                    }
                    if (temp > 70.0) {
                        temp = 70;
                    }
                    bbRotateEntity(sc->cameraObj, CurveAngle(temp, bbEntityPitch(sc->cameraObj), 75.0), bbEntityYaw(sc->obj), 0);

                    bbPositionEntity(sc->cameraObj, bbEntityX(sc->obj, true), bbEntityY(sc->obj, true) - 0.083, bbEntityZ(sc->obj, true));
                    bbRotateEntity(sc->cameraObj, bbEntityPitch(sc->cameraObj), bbEntityYaw(sc->obj), 0);
                } else {
                    if (sc->turn > 0) {
                        if (sc->dir == 0) {
                            sc->currAngle = sc->currAngle+0.2 * timing->tickDuration;
                            if (sc->currAngle > (sc->turn * 1.3)) {
                                sc->dir = 1;
                            }
                        } else {
                            sc->currAngle = sc->currAngle-0.2 * timing->tickDuration;
                            if (sc->currAngle < (-sc->turn * 1.3)) {
                                sc->dir = 0;
                            }
                        }
                    }
                    bbRotateEntity(sc->obj, 0, sc->room->angle + sc->angle + Max(Min(sc->currAngle, sc->turn), -sc->turn), 0);

                    bbPositionEntity(sc->cameraObj, bbEntityX(sc->obj, true), bbEntityY(sc->obj, true) - 0.083, bbEntityZ(sc->obj, true));
                    bbRotateEntity(sc->cameraObj, bbEntityPitch(sc->cameraObj), bbEntityYaw(sc->obj), 0);

                    if (sc->cam!=0) {
                        bbPositionEntity(sc->cam, bbEntityX(sc->cameraObj, true), bbEntityY(sc->cameraObj, true), bbEntityZ(sc->cameraObj, true));
                        bbRotateEntity(sc->cam, bbEntityPitch(sc->cameraObj), bbEntityYaw(sc->cameraObj), 0);
                        bbMoveEntity(sc->cam, 0, 0, 0.1);
                    }

                    if (sc!=CoffinCam) {
                        if (Abs(bbDeltaYaw(sc->cameraObj,mainPlayer->cam))<60.0) {
                            if (bbEntityVisible(sc->cameraObj,mainPlayer->cam)) {
                                PlayerDetected = true;
                            }
                        }
                    }
                }
            }

            if (close == true | sc->isRoom2slCam | sc->specialCam) {
                if (sc->screen) {
                    sc->state = sc->state+timing->tickDuration;

                    if (sc->inSight & sc->allowSaving) {
                        if (SelectedDifficulty->saveType == SAVEONSCREENS & bbEntityDistance(mainPlayer->cam, sc->scrObj)<1.0) {
                            DrawHandIcon = true;
                            if (MouseHit1) {
                                SelectedMonitor = sc;
                            }
                        } else if ((SelectedMonitor == sc)) {
                            SelectedMonitor = nullptr;
                        }
                    } else {
                        SelectedMonitor = nullptr;
                    }

                    if (sc->state >= sc->renderInterval) {
                        sc->inSight = false;
                        if (mainPlayer->blinkTimer > - 5 & bbEntityInView(sc->scrObj, mainPlayer->cam)) {
                            if (bbEntityVisible(mainPlayer->cam,sc->scrObj)) {
                                sc->inSight = true;

                                if (sc->coffinEffect==1 | sc->coffinEffect==3) {
                                    if (mainPlayer->blinkTimer > - 5) {
                                        mainPlayer->sanity895 = mainPlayer->sanity895-(timing->tickDuration * 16);
                                    }

                                    if (mainPlayer->sanity895 < (-1000)) {
                                        DeathMSG = "\"What we know is that he died of cardiac arrest. My guess is that it was caused by SCP-895, although it has never been observed affecting video equipment from this far before. ";
                                        DeathMSG = DeathMSG + "Further testing is needed to determine whether SCP-895's \"Red Zone\" is increasing.\"";

                                        Kill(mainPlayer);
                                    }
                                }

                                if (!sc->isRoom2slCam) {
                                    if (!sc->specialCam) {
                                        if (CoffinCam == nullptr | bbRand(5)==5 | sc->coffinEffect != 3) {
                                            bbHideEntity(mainPlayer->cam);
                                            bbShowEntity(sc->cam);
                                            bbCls();

                                            bbSetBuffer(bbBackBuffer());
                                            bbRenderWorld();
                                            bbCopyRect(0,0,512,512,0,0,bbBackBuffer(),bbTextureBuffer(ScreenTexs[sc->scrTexture]));

                                            bbHideEntity(sc->cam);
                                            bbShowEntity(mainPlayer->cam);
                                        } else {
                                            bbHideEntity(mainPlayer->cam);
                                            bbShowEntity(CoffinCam->room->obj);
                                            SetRoomVisibility(CoffinCam->room,true);
                                            bbShowEntity(CoffinCam->cam);
                                            bbCls();

                                            bbSetBuffer(bbBackBuffer());
                                            bbRenderWorld();
                                            bbCopyRect(0,0,512,512,0,0,bbBackBuffer(),bbTextureBuffer(ScreenTexs[sc->scrTexture]));

                                            bbHideEntity(CoffinCam->room->obj);
                                            bbHideEntity(CoffinCam->cam);
                                            bbShowEntity(mainPlayer->cam);
                                        }
                                    } else {
                                        bbHideEntity(mainPlayer->cam);
                                        bbShowEntity(sc->cam);
                                        bbCls();

                                        bbRenderWorld();

                                        bbHideEntity(sc->cam);
                                        bbShowEntity(mainPlayer->cam);

                                        bbCopyRect(0,0,512,512,0,0,bbBackBuffer(),bbTextureBuffer(sc->room2slTexs[sc->scrTexture]));

                                    }
                                } else {
                                    bbHideEntity(mainPlayer->cam);
                                    bbShowEntity(sc->room->obj);
                                    SetRoomVisibility(sc->room,true);
                                    bbShowEntity(sc->cam);
                                    bbCls();

                                    bbRenderWorld();

                                    bbHideEntity(sc->room->obj);
                                    bbHideEntity(sc->cam);
                                    bbShowEntity(mainPlayer->cam);

                                    bbCopyRect(0, 0, userOptions->screenWidth, userOptions->screenHeight, 0, 0, bbBackBuffer(), bbTextureBuffer(sc->room2slTexs[sc->scrTexture]));
                                }

                            }
                        }
                        sc->state = 0;
                    }

                    if (SelectedMonitor == sc | sc->coffinEffect==1 | sc->coffinEffect==3) {
                        if (sc->inSight) {
                            //If ((Not NoClip)) Then
                            pvt = bbCreatePivot();
                            bbPositionEntity(pvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
                            bbPointEntity(pvt, sc->scrObj);

                            std::cout << "curvea: "+String(CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), Min(Max(15000.0 / (-mainPlayer->sanity895), 20.0), 200.0)));
                            bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider), CurveAngle(bbEntityYaw(pvt), bbEntityYaw(mainPlayer->collider), Min(Max(15000.0 / (-mainPlayer->sanity895), 20.0), 200.0)), 0);

                            bbTurnEntity(pvt, 90, 0, 0);
                            mainPlayer->headPitch = CurveAngle(bbEntityPitch(pvt), mainPlayer->headPitch + 90.0, Min(Max(15000.0 / (-mainPlayer->sanity895), 20.0), 200.0));
                            mainPlayer->headPitch = mainPlayer->headPitch-90;

                            std::cout << "pvt: "+String(bbEntityYaw(pvt))+"   - coll: "+String(bbEntityYaw(mainPlayer->collider));


                            bbFreeEntity(pvt);
                            //EndIf
                            if (sc->coffinEffect==1 | sc->coffinEffect==3) {
                                for (i = 0; i <= GORE_PIC_COUNT-1; i++) {
                                    gorePics[i] = GrabTexture("GFX/895pics/pic" + String(i + 1) + ".jpg");
                                }
                                if (mainPlayer->sanity895 < - 800) {
                                    if (bbRand(3) == 1) {
                                        bbEntityTexture(sc->scrOverlay, MonitorTexture);
                                    }
                                    if (bbRand(6) < 5) {
                                        bbEntityTexture(sc->scrOverlay, gorePics[bbRand(0, GORE_PIC_COUNT-1)]);
                                        //If (sc\playerState = 1) Then PlaySound(HorrorSFX(1)) ;TODO: fix
                                        sc->playerState = 2;
                                        if (sc->soundCHN == 0) {
                                            //sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
                                        } else {
                                            //If (Not IsChannelPlaying(sc\soundCHN)) Then sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
                                        }
                                        if (sc->coffinEffect==3 & bbRand(200)==1) {
                                            sc->coffinEffect = 2;
                                            sc->playerState = bbRand(10000, 20000);
                                        }
                                    }
                                    mainPlayer->blurTimer = 1000;
                                } else if ((mainPlayer->sanity895 < - 500)) {
                                    if (bbRand(7) == 1) {
                                        bbEntityTexture(sc->scrOverlay, MonitorTexture);
                                    }
                                    if (bbRand(50) == 1) {
                                        bbEntityTexture(sc->scrOverlay, gorePics[bbRand(0, GORE_PIC_COUNT-1)]);
                                        //If (sc\playerState = 0) Then PlaySound(HorrorSFX(0)) ;TODO: fix
                                        sc->playerState = (int)(Max(sc->playerState, 1));
                                        if (sc->coffinEffect==3 & bbRand(100)==1) {
                                            sc->coffinEffect = 2;
                                            sc->playerState = bbRand(10000, 20000);
                                        }
                                    }
                                } else {
                                    bbEntityTexture(sc->scrOverlay, MonitorTexture);
                                }
                                for (i = 0; i <= GORE_PIC_COUNT-1; i++) {
                                    DropAsset(gorePics[i]);
                                }
                            }
                        }
                    }

                    if (sc->inSight & sc->coffinEffect==0 | sc->coffinEffect==2) {
                        aiPic = GrabTexture("GFX/079pics/face.jpg");
                        if (sc->playerState == 0) {
                            sc->playerState = bbRand(60000, 65000);
                        }

                        if (bbRand(500) == 1) {
                            bbEntityTexture(sc->scrOverlay, aiPic);
                        }

                        if (TimeInPosMilliSecs() % sc->playerState) >= bbRand(600) {
                            bbEntityTexture(sc->scrOverlay, MonitorTexture);
                        } else {
                            if (sc->soundCHN == 0) {
                                sc->soundCHN = bbPlaySound(LoadTempSound("SFX/SCP/079/Broadcast"+String(bbRand(1,3))+".ogg"));
                                if (sc->coffinEffect==2) {
                                    sc->coffinEffect = 3;
                                    sc->playerState = 0;
                                }
                            } else if ((!IsChannelPlaying(sc->soundCHN))) {
                                sc->soundCHN = bbPlaySound(LoadTempSound("SFX/SCP/079/Broadcast"+String(bbRand(1,3))+".ogg"));
                                if (sc->coffinEffect==2) {
                                    sc->coffinEffect = 3;
                                    sc->playerState = 0;
                                }
                            }
                            bbEntityTexture(sc->scrOverlay, aiPic);
                        }
                        DropAsset(aiPic);
                    }

                    //if screen=true
                }

                if (!sc->inSight) {
                    sc->soundCHN = LoopRangedSound_SM(sndManager->camera, sc->soundCHN, mainPlayer->cam, sc->cameraObj, 4.0);
                }
            } else {
                if (SelectedMonitor==sc) {
                    SelectedMonitor = nullptr;
                }
            }
        }
    }

    bbCls();


}

Prop* LoadProp(String file, float x, float y, float z, float pitch, float yaw, float roll, float xScale, float yScale, float zScale) {
    Prop* p;
    p = new Prop();
    p->file = file;
    p->x = x;
    p->y = y;
    p->z = z;
    p->pitch = pitch;
    p->yaw = yaw;
    p->roll = roll;
    p->xScale = xScale;
    p->yScale = yScale;
    p->zScale = zScale;

    Prop* p2;
    for (int iterator86 = 0; iterator86 < Prop::getListSize(); iterator86++) {
        p2 = Prop::getObject(iterator86);

        if ((p!=p2) & (p2->file == file)) {
            p->obj = bbCopyEntity(p2->obj);
            break;
        }
    }

    if (p->obj==0) {
        p->obj = bbLoadMesh("GFX/Map/Props/"+file+".b3d");
    }
    if (p->obj==0) {
        bbRuntimeError(file);
    }
    bbHideEntity(p->obj);
    return p;
}

void CreateMap() {
    int i;
    int c;
    int j;

    std::cout << "Generating a map using the seed "+RandomSeed;

    bbSeedRnd(SeedStringToInt(RandomSeed));

    int mapDim = MAP_SIZE;
    IntArray* layout = CreateIntArray(mapDim,mapDim);
    MapRooms = CreateIntArray(mapDim,mapDim);

    //clear the grid
    int y;
    int x;
    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            SetIntArrayElem(layout,0,x,y);
            SetIntArrayElem(MapRooms,0,x,y);
        }
    }

    //4x4 squares, offset 1 slot from 0,0
    int rectWidth = 3;
    int rectHeight = 3;

    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            if ((x % rectWidth==1) | (y % rectHeight==1)) {
                if ((x>=rectWidth & x<mapDim-rectWidth) | (y>=rectHeight & y<mapDim-rectHeight)) {
                    SetIntArrayElem(layout,1,x,y);
                }
            }
        }
    }

    DetermineRoomTypes(layout,mapDim);

    //shift some horizontal corridors
    int shift;
    int nonShiftStreak = bbRand(0,5);
    for (y = 1; y <= mapDim-2; y++) {
        for (x = 0; x <= mapDim-2; x++) {
            if (y>6 | x>6) {
                if ((y % rectHeight==1) & GetIntArrayElem(layout,x,y)==ROOM2) {
                    shift = bbRand(0,1);
                    if (nonShiftStreak==0) {
                        shift = 0;
                    }
                    if (nonShiftStreak>5) {
                        shift = 1;
                    }
                    if ((x/rectWidth) % 2) {
                        shift = -shift;
                    }
                    if (shift!=0) {
                        for (i = 0; i <= rectWidth-2; i++) {
                            SetIntArrayElem(layout,0,x+i,y);
                            SetIntArrayElem(layout,ROOM2,x+i,y+shift);
                        }
                        nonShiftStreak = 0;
                    } else {
                        nonShiftStreak = nonShiftStreak+1;
                    }
                    x = x+rectWidth-1;
                }
            }
        }
    }

    DetermineRoomTypes(layout,mapDim);

    //punch out some holes to get room2c's
    int punchOffset = bbRand(0,1);
    int roomAbove;
    int roomBelow;
    for (y = 2; y <= mapDim-4; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            if ((((x/rectWidth) % 2)==punchOffset) & (GetIntArrayElem(layout,x,y)==ROOM2)) {
                roomAbove = GetIntArrayElem(layout,x,y-1);
                roomBelow = GetIntArrayElem(layout,x,y+1);
                if ((roomAbove>=ROOM2) & (roomBelow>=ROOM2)) & ((roomAbove+roomBelow)>(ROOM2+ROOM3)) {
                    SetIntArrayElem(layout,0,x,y);
                }
            }
        }
    }

    DetermineRoomTypes(layout,mapDim);

    int zone = ZONE_LCZ;

    //start off by placing rooms that ask to be placed a certain amount of times
    int prioritizedTemplateCount = 0;
    RoomTemplate* rt;

    for (int iterator87 = 0; iterator87 < RoomTemplate::getListSize(); iterator87++) {
        rt = RoomTemplate::getObject(iterator87);

        if (((rt->zones & zone)!=0) & (rt->maxAmount>0) & (rt->shape!=ROOM0)) {
            prioritizedTemplateCount = prioritizedTemplateCount+1;
        }
    }
    //TODO: replace with an array of the right type once we move to C++
    IntArray* prioritizedTemplates = CreateIntArray(prioritizedTemplateCount,1);
    RoomTemplate* tempTemplate;
    RoomTemplate* tempTemplate2;
    SetIntArrayElem(prioritizedTemplates,0,0,0);
    for (int iterator88 = 0; iterator88 < RoomTemplate::getListSize(); iterator88++) {
        rt = RoomTemplate::getObject(iterator88);

        if (((rt->zones & zone)!=0) & (rt->maxAmount>0) & (rt->shape!=ROOM0)) {
            tempTemplate = rt;
            std::cout << "queueing up "+rt->name;
            for (i = 0; i <= prioritizedTemplateCount-1; i++) {
                if (GetIntArrayElem(prioritizedTemplates,i,0)==0) {
                    if (i<prioritizedTemplateCount-1) {
                        SetIntArrayElem(prioritizedTemplates,0,i+1,0);
                    }
                    SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0);
                    break;
                } else {
                    tempTemplate2 = Object.RoomTemplate(GetIntArrayElem(prioritizedTemplates,i,0));
                    if (tempTemplate2->maxAmount>tempTemplate->maxAmount) {
                        SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0);
                        //DebugLog("swapping "+tempTemplate2\name+" for "+tempTemplate\name)
                        tempTemplate = tempTemplate2;
                    }
                }
            }
        }
    }

    int RoomCount[ROOM4+1];
    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            if (GetIntArrayElem(layout,x,y)!=ROOM0) {
                RoomCount[GetIntArrayElem(layout,x,y)] = RoomCount[GetIntArrayElem(layout,x,y)]+1;
            }
        }
    }
    for (i = 1; i <= ROOM4; i++) {
        std::cout << "Type"+String(i)+" count: "+String(RoomCount[i]);
    }

    Room* r;

    int placementCount;
    int loopStartX;
    int loopStartY;
    int loopEndX;
    int loopEndY;
    int loopX;
    int loopY;
    int offsetX;
    int offsetY;
    int placed;

    int k;
    for (k = 0; k <= prioritizedTemplateCount-1; k++) {
        rt = Object.RoomTemplate(GetIntArrayElem(prioritizedTemplates,k,0));

        placementCount = bbRand(rt->minAmount,rt->maxAmount);

        std::cout << "trying to place "+String(placementCount)+" "+rt->name;

        for (c = 1; c <= placementCount; c++) {
            loopStartX = (int)(Min(bbFloor((float)(mapDim)*rt->xRangeStart),mapDim-1));
            loopStartY = (int)(Min(bbFloor((float)(mapDim)*rt->yRangeStart),mapDim-1));
            loopEndX = (int)(Min(bbFloor((float)(mapDim)*rt->xRangeEnd),mapDim-1));
            loopEndY = (int)(Min(bbFloor((float)(mapDim)*rt->yrangeEnd),mapDim-1));

            loopX = loopEndX-loopStartX;
            loopY = loopEndY-loopStartY;

            offsetX = bbRand(0,loopX);
            offsetY = bbRand(0,loopY);

            placed = false;

            for (j = 0; j <= loopY; j++) {
                for (i = 0; i <= loopX; i++) {
                    x = ((i+offsetX) % (loopX+1)) + loopStartX;
                    y = ((j+offsetY) % (loopY+1)) + loopStartY;
                    if (GetIntArrayElem(layout,x,y)>0) & (GetIntArrayElem(layout,x,y)==rt->shape) {
                        r = CreateRoom(rt,x*8.0,0.0,y*8.0);
                        r->angle = DetermineRotation(layout,x,y);
                        bbTurnEntity(r->obj,0,r->angle,0);
                        //mark as used
                        SetIntArrayElem(layout,-1,x,y);
                        //add to the MapRooms array
                        SetIntArrayElem(MapRooms,Handle(r),x,y);
                        placed = true;
                    }

                    if (placed) {
                        break;
                    }
                }
                if (placed) {
                    break;
                }
            }
            if (!placed) {
                bbRuntimeError("(seed: "+RandomSeed+") Failed To place "+rt->name+" around ("+String(loopStartX)+","+String(loopStartY)+","+String(loopEndX)+","+String(loopEndY)+")");
            }
        }
    }

    DeleteIntArray(prioritizedTemplates);

    int randomTemplateCount;
    int totalCommonness[ROOM4+1];
    for (i = 1; i <= ROOM4; i++) {
        totalCommonness[i] = 0;
    }
    for (int iterator89 = 0; iterator89 < RoomTemplate::getListSize(); iterator89++) {
        rt = RoomTemplate::getObject(iterator89);

        if (((rt->zones & zone)!=0) & (rt->maxAmount<0) & (rt->shape!=ROOM0)) {
            randomTemplateCount = randomTemplateCount+1;
            totalCommonness[rt->shape] = totalCommonness[rt->shape]+(int)(rt->commonness);
        }
    }
    IntArray* randomTemplates = CreateIntArray(randomTemplateCount,1);
    int index = 0;
    int tempHandle1;
    int tempHandle2;

    for (int iterator90 = 0; iterator90 < RoomTemplate::getListSize(); iterator90++) {
        rt = RoomTemplate::getObject(iterator90);

        if (((rt->zones & zone)!=0) & (rt->maxAmount<0) & (rt->shape!=ROOM0)) {
            SetIntArrayElem(randomTemplates,Handle(rt),index,0);
            index = index+1;
        }
    }

    //shuffle the templates
    for (i = 0; i <= randomTemplateCount-1; i++) {
        index = bbRand(0,randomTemplateCount-1);
        tempHandle1 = GetIntArrayElem(randomTemplates,i,0);
        tempHandle2 = GetIntArrayElem(randomTemplates,index,0);
        SetIntArrayElem(randomTemplates,tempHandle2,i,0);
        SetIntArrayElem(randomTemplates,tempHandle1,index,0);
    }

    int targetCommonness = 0;
    int commonnessAccumulator = 0;
    int currType;
    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            commonnessAccumulator = 0;
            currType = GetIntArrayElem(layout,x,y);
            if (currType>0) {
                targetCommonness = bbRand(0,totalCommonness[currType]);

                for (i = 0; i <= randomTemplateCount-1; i++) {
                    tempTemplate = Object.RoomTemplate(GetIntArrayElem(randomTemplates,i,0));
                    if (tempTemplate->shape == currType) {
                        commonnessAccumulator = commonnessAccumulator+(int)(tempTemplate->commonness);
                        if (commonnessAccumulator>=targetCommonness) {
                            r = CreateRoom(tempTemplate,x*8.0,0.0,y*8.0);
                            r->angle = DetermineRotation(layout,x,y);
                            bbTurnEntity(r->obj,0,r->angle,0);
                            //mark as used
                            SetIntArrayElem(layout,-1,x,y);
                            //add to the MapRooms array
                            SetIntArrayElem(MapRooms,Handle(r),x,y);
                            break;
                        }
                    }
                }
            }
        }
    }

    DeleteIntArray(randomTemplates);
    DeleteIntArray(layout);

    //finally, let rooms know who their neighbors are
    int tempX;
    int tempY;
    WayPoint* tempWaypoint;
    WayPoint* newWaypoint;
    WayPoint* roomAWaypoint;
    WayPoint* roomBWaypoint;
    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            r = Object.Room(GetIntArrayElem(MapRooms,x,y));
            if (r!=nullptr) {
                for (i = 0; i <= 3; i++) {
                    switch (i) {
                        case 0: {
                            tempX = 1;
                            tempY = 0;
                        }
                        case 1: {
                            tempX = 0;
                            tempY = -1;
                        }
                        case 2: {
                            tempX = -1;
                            tempY = 0;
                        }
                        case 3: {
                            tempX = 0;
                            tempY = 1;
                        }
                    }

                    if (x+tempX>=0) & (x+tempX<mapDim) & (y+tempY>=0) & (y+tempY<mapDim) {
                        r->adjacent[i] = Object.Room(GetIntArrayElem(MapRooms,x+tempX,y+tempY));
                        if (r->adjacent[i]!=nullptr) {
                            if (r->adjacent[i]->adjDoor[(i+2) % 4]==nullptr) {
                                r->adjDoor[i] = CreateDoor(r->x+4.0*tempX, 0.0,r->z+4.0*tempY, 90.0*((i+1) % 2), nullptr);
                                newWaypoint = CreateWaypoint(r->x+4.0*tempX,50.0*RoomScale,r->z+4.0*tempY,r);

                                std::cout << "step1";
                                roomAWaypoint = nullptr;
                                roomBWaypoint = nullptr;
                                for (int iterator91 = 0; iterator91 < WayPoint::getListSize(); iterator91++) {
                                    tempWaypoint = WayPoint::getObject(iterator91);

                                    if (tempWaypoint!=newWaypoint) {
                                        if (tempWaypoint->room == r) {
                                            if (roomAWaypoint == nullptr) {
                                                roomAWaypoint = tempWaypoint;
                                            } else if ((bbEntityDistance(roomAWaypoint->obj,newWaypoint->obj)>bbEntityDistance(tempWaypoint->obj,newWaypoint->obj))) {
                                                roomAWaypoint = tempWaypoint;
                                            }
                                        }

                                        if (tempWaypoint->room == r->adjacent[i]) {
                                            if (roomBWaypoint == nullptr) {
                                                roomBWaypoint = tempWaypoint;
                                            } else if ((bbEntityDistance(roomBWaypoint->obj,newWaypoint->obj)>bbEntityDistance(tempWaypoint->obj,newWaypoint->obj))) {
                                                roomBWaypoint = tempWaypoint;
                                            }
                                        }
                                    }
                                }

                                std::cout << "step2";
                                if (roomAWaypoint!=nullptr & roomBWaypoint!=nullptr) {
                                    for (j = 0; j <= 15; j++) {
                                        if (roomAWaypoint->connected[j]==nullptr) {
                                            roomAWaypoint->connected[j] = newWaypoint;
                                            break;
                                        }
                                    }

                                    for (j = 0; j <= 15; j++) {
                                        if (roomBWaypoint->connected[j]==nullptr) {
                                            roomBWaypoint->connected[j] = newWaypoint;
                                            break;
                                        }
                                    }

                                    for (j = 0; j <= 15; j++) {
                                        if (newWaypoint->connected[j]==nullptr) {
                                            newWaypoint->connected[j] = roomAWaypoint;
                                            newWaypoint->connected[j+1] = roomBWaypoint;
                                            break;
                                        }
                                    }
                                }

                                std::cout << "step3";
                            } else {
                                r->adjDoor[i] = r->adjacent[i]->adjDoor[(i+2) % 4];
                            }
                        }
                    }
                }
            }
        }
    }
}

void DetermineRoomTypes(IntArray* layout, int mapDim) {
    int horNeighborCount = 0;
    int vertNeighborCount = 0;

    int y;
    int x;
    for (y = 0; y <= mapDim-1; y++) {
        for (x = 0; x <= mapDim-1; x++) {
            if (GetIntArrayElem(layout,x,y)!=0) {
                horNeighborCount = 0;
                if (x>0) {
                    horNeighborCount = horNeighborCount+(GetIntArrayElem(layout,x-1,y)!=0);
                }
                if (x<mapDim-1) {
                    horNeighborCount = horNeighborCount+(GetIntArrayElem(layout,x+1,y)!=0);
                }
                vertNeighborCount = 0;
                if (y>0) {
                    vertNeighborCount = vertNeighborCount+(GetIntArrayElem(layout,x,y-1)!=0);
                }
                if (y<mapDim-1) {
                    vertNeighborCount = vertNeighborCount+(GetIntArrayElem(layout,x,y+1)!=0);
                }

                if (horNeighborCount+vertNeighborCount == 1) {
                    SetIntArrayElem(layout,ROOM1,x,y);
                } else if ((horNeighborCount+vertNeighborCount == 3)) {
                    SetIntArrayElem(layout,ROOM3,x,y);
                } else if ((horNeighborCount+vertNeighborCount == 4)) {
                    SetIntArrayElem(layout,ROOM4,x,y);
                } else if ((horNeighborCount == 1) & (vertNeighborCount == 1)) {
                    SetIntArrayElem(layout,ROOM2C,x,y);
                } else if ((horNeighborCount == 2) ^ (vertNeighborCount == 2)) {
                    SetIntArrayElem(layout,ROOM2,x,y);
                } else {
                    SetIntArrayElem(layout,0,x,y);
                }
            }
        }
    }
}

int DetermineRotation(IntArray* layout, int x, int y) {
    switch (GetIntArrayElem(layout,x,y)) {
        case ROOM1: {
            if (x>0) & (GetIntArrayElem(layout,x-1,y)!=0) {
                return 270;
            } else if ((x<layout->xDim-1) & (GetIntArrayElem(layout,x+1,y)!=0)) {
                return 90;
            } else if ((y>0) & (GetIntArrayElem(layout,x,y-1)!=0)) {
                return 0;
            } else {
                return 180;
            }
        }
        case ROOM2: {
            if (GetIntArrayElem(layout,x-1,y)!=0) {
                return 90+bbRand(0,1)*180;
            } else {
                return (bbRand(0,1)*180);
            }
        }
        case ROOM2C: {
            if (x>0) & (GetIntArrayElem(layout,x-1,y)!=0) {
                if (y>0) & (GetIntArrayElem(layout,x,y-1)!=0) {
                    return 270;
                } else {
                    return 180;
                }
            } else {
                if (y>0) & (GetIntArrayElem(layout,x,y-1)!=0) {
                    return 0;
                } else {
                    return 90;
                }
            }
        }
        case ROOM3: {
            if (x>0) & (GetIntArrayElem(layout,x-1,y)==0) {
                return 90;
            } else if ((y>0) & (GetIntArrayElem(layout,x,y-1)==0)) {
                return 180;
            } else if ((x<layout->xDim-1) & (GetIntArrayElem(layout,x+1,y)==0)) {
                return 270;
            } else {
                return 0;
            }
        }
        case ROOM4: {
            return bbRand(0,3)*90;
        }
        default: {
            return -1;
        }
    }
}

int CheckRoomOverlap(String roomname, int x, int y) {
    //TODO: reimplement?
    return false;
}

int GetZone(int y) {
    //TODO: reimplement
    return -1;
}

void AmbientLightRooms(int value = 0) {
    int mesh;
    int surf;
    int brush;
    int tex0;

    if (value==AmbientLightRoomVal) {
        return;
    }
    AmbientLightRoomVal = value;

    //probably shouldn't make assumptions here but who cares, why wouldn't it use the backbuffer ;GetBuffer()
    int oldbuffer = bbBackBuffer();

    bbSetBuffer(bbTextureBuffer(AmbientLightRoomTex));

    bbClsColor(value,value,value);
    bbCls();
    bbClsColor(0,0,0);

    bbSetBuffer(oldbuffer);
}

void FindAndDeleteFakeMonitor(Room* r, float x, float y, float z, int Amount) {
    int i;

    for (i = 0; i <= Amount; i++) {
        if (r->objects[i]!=0) {
            if (bbEntityX(r->objects[i],true) == x) {
                if (bbEntityY(r->objects[i],true) == y) {
                    if (bbEntityZ(r->objects[i],true) == z) {
                        bbFreeEntity(r->objects[i]);
                        r->objects[i] = 0;
                        std::cout << "Deleted Fake Monitor: "+String(i);
                        break;
                    }
                }
            }
        }
    }

}

}

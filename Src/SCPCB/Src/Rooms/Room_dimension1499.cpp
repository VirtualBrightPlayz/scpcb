#include "Room_dimension1499.h"
#include "include.h"
#include <iostream>

namespace CBN {

// Structs.
std::vector<ChunkPart*> ChunkPart::list;
ChunkPart::ChunkPart() {
    list.push_back(this);
}
ChunkPart::~ChunkPart() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int ChunkPart::getListSize() {
    return list.size();
}
ChunkPart* ChunkPart::getObject(int index) {
    return list[index];
}

std::vector<Chunk*> Chunk::list;
Chunk::Chunk() {
    list.push_back(this);
}
Chunk::~Chunk() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Chunk::getListSize() {
    return list.size();
}
Chunk* Chunk::getObject(int index) {
    return list[index];
}

// Functions.
void UpdateEvent_dimension1499(Event* e) {
    float dist;
    int i;
    int temp;
    int pvt;
    String strtemp;
    int j;
    int k;

    Particle* p;
    NPC* n;
    Room* r;
    Event* e2;
    Item* it;
    Emitter* em;
    SecurityCam* sc;
    SecurityCam* sc2;

    String CurrTrigger = "";

    float x;
    float y;
    float z;

    float angle;

    //[Block]
    //e\eventState: If player entered dimension (will be resetted after the player leaves it)
    //0: The player never entered SCP-1499
    //1: The player had already entered the dimension at least once
    //2: The player is in dimension
    if (mainPlayer->currRoom == e->room) {
        if (e->eventState < 2.0) {
            //1499 random generator
            //[Block]
            //TODO: Reimplement.
            //			If e\eventState = 0.0
            //				If e\eventStr = ""
            //					e\eventStr = "load0"
            //				ElseIf e\eventStr = "load0"
            //					e\room\objects[0] = CreatePlane()
            //					Local planetex% = LoadTexture("GFX/Map/Rooms/dimension1499/grit3.jpg")
            //					EntityTexture(e\room\objects[0],planetex%)
            //					FreeTexture(planetex%)
            //					PositionEntity(e\room\objects[0],0,EntityY(e\room\obj),0)
            //					EntityType(e\room\objects[0],HIT_MAP)
            //					;EntityParent(e\room\objects[0],e\room\obj)
            //					e\eventStr = "load1"
            //				ElseIf e\eventStr = "load1"
            //					NTF_1499Sky = sky_CreateSky("GFX/Map/sky/1499sky")
            //					e\eventStr = 1
            //				Else
            //					If (int)(e\eventStr)<16
            //						e\room\objects[(int)(e\eventStr)] = LoadMesh("GFX/Map/Rooms/dimension1499/1499object"+((int)(e/EventStr))+".b3d")
            //						HideEntity(e\room\objects[(int)(e\eventStr)])
            //						e\eventStr = (int)(e\eventStr)+1
            //					ElseIf (int)(e\eventStr)=16
            //						CreateChunkParts(e\room)
            //						e\eventStr = 17
            //					ElseIf (int)(e\eventStr) = 17
            //						x# = EntityX(e\room\obj)
            //						z# = EntityZ(e\room\obj)
            //						Local ch.Chunk
            //						For i = -2 To 2 Step 2
            //							ch = CreateChunk(-1,x#*(i*2.5),EntityY(e\room\obj),z#)
            //						Next
            //						;If (Music(18)=0) Then Music(18) = LoadSound("SFX/Music/1499.ogg")
            //						e\eventState = 2.0
            //						e\eventStr = 18
            //					EndIf
            //				EndIf
            //			Else
            //				e\eventState = 2.0
            //			EndIf
            //[End Block]
            for (int iterator169 = 0; iterator169 < NPC::getListSize(); iterator169++) {
                n = NPC::getObject(iterator169);

                if (n->npcType == NPCtype1499) {
                    n->idle = false;
                    n->state = 0;
                    //If (Rand(2)=1) Then n\state2 = 500*3
                    n->angle = bbRnd(360);
                    bbPositionEntity(n->collider,bbEntityX(n->collider)+bbRnd(-60.0,60.0),bbEntityY(n->collider)+0.05,bbEntityZ(n->collider)+bbRnd(-60.0,60.0));
                    bbResetEntity(n->collider);
                }
            }
        }
        //PositionEntity(e\room\objects[0],0,800,0)
        bbCameraFogRange(mainPlayer->cam,40,80);
        bbCameraFogColor(mainPlayer->cam,96,97,104);
        bbCameraClsColor(mainPlayer->cam,96,97,104);
        bbCameraRange(mainPlayer->cam,0.05,90);

        for (int iterator170 = 0; iterator170 < Room::getListSize(); iterator170++) {
            r = Room::getObject(iterator170);

            bbHideEntity(r->obj);
        }
        bbShowEntity(e->room->obj);

        UpdateChunks(e->room,15);
        bbShowEntity(NTF_1499Sky);
        Update1499Sky();
        //ShouldPlay = 18 ;TODO
        if (bbEntityY(mainPlayer->collider)<800.0) {
            bbPositionEntity(mainPlayer->collider,bbEntityX(mainPlayer->collider),800.1,bbEntityZ(mainPlayer->collider),true);
        }
        bbResetEntity(mainPlayer->collider);

        mainPlayer->footstepOverride = 3;
    } else {
        if (e->eventState == 2.0) {
            bbHideEntity(NTF_1499Sky);
            HideChunks();
            for (int iterator171 = 0; iterator171 < NPC::getListSize(); iterator171++) {
                n = NPC::getObject(iterator171);

                if (n->npcType == NPCtype1499) {
                    //RemoveNPC(n)
                    n->idle = true;
                }
            }
            e->eventState = 1.0;
        }
    }
    //[End Block]
}

void UpdateLeave1499() {
    Room* r;
    Item* it;

    if ((!(IsPlayerWearingItem(mainPlayer,"scp1499") | IsPlayerWearingItem(mainPlayer,"super1499"))) & mainPlayer->currRoom->roomTemplate->name == "dimension1499") {
        for (int iterator172 = 0; iterator172 < Room::getListSize(); iterator172++) {
            r = Room::getObject(iterator172);

            if (r == NTF_1499PrevRoom) {
                mainPlayer->blinkTimer = -1;
                //Msg = "You removed the gas mask and reappeared inside the facility."
                //MsgTimer = 70 * 5
                NTF_1499X = bbEntityX(mainPlayer->collider);
                NTF_1499Y = bbEntityY(mainPlayer->collider);
                NTF_1499Z = bbEntityZ(mainPlayer->collider);
                bbPositionEntity(mainPlayer->collider, NTF_1499PrevX, NTF_1499PrevY+0.05, NTF_1499PrevZ);
                bbResetEntity(mainPlayer->collider);
                UpdateDoors();
                UpdateRooms();
                for (int iterator173 = 0; iterator173 < Item::getListSize(); iterator173++) {
                    it = Item::getObject(iterator173);

                    //it\disttimer = 0
                    if (it->itemTemplate->name == "scp1499" | it->itemTemplate->name == "super1499") {
                        if (bbEntityY(it->collider) >= bbEntityY(mainPlayer->currRoom->obj)-5) {
                            bbPositionEntity(it->collider,NTF_1499PrevX,NTF_1499PrevY+(bbEntityY(it->collider)-bbEntityY(mainPlayer->currRoom->obj)),NTF_1499PrevZ);
                            bbResetEntity(it->collider);
                        }
                    }
                }
                mainPlayer->currRoom = r;
                PlaySound2(LoadTempSound("SFX/SCP/1499/Exit.ogg"));
                NTF_1499PrevX = 0.0;
                NTF_1499PrevY = 0.0;
                NTF_1499PrevZ = 0.0;
                NTF_1499PrevRoom = nullptr;
                //Brightness = StoredBrightness
                break;
            }
        }
    }

}

void CreateChunkParts(Room* r) {
    String File = "Data/1499chunks.INI";
    int ChunkAmount = GetINIInt(File,"general","count");
    int i;
    String StrTemp;
    int j;
    ChunkPart* chp;
    ChunkPart* chp2;
    int obj;
    int loc;
    int objID;
    String x;
    String z;
    String yaw;

    bbSeedRnd(SeedStringToInt(RandomSeed));

    for (i = 0; i <= ChunkAmount; i++) {
        loc = GetINISectionLocation(File,"chunk"+String(i));
        if (loc > 0) {
            StrTemp = GetINIString2(File,loc,"count");
            chp = new ChunkPart();
            chp->amount = (int)(StrTemp);
            std::cout << "------------------";
            for (j = 0; j <= (int)(StrTemp); j++) {
                objID = (int)(GetINIString2(File,loc,"obj"+String(j)));
                x = GetINIString2(File,loc,"obj"+String(j)+"-x");
                z = GetINIString2(File,loc,"obj"+String(j)+"-z");
                yaw = GetINIString2(File,loc,"obj"+String(j)+"-yaw");
                std::cout << "1499 chunk X/Z/Yaw: "+x+"|"+z+"|"+yaw;
                chp->obj[j] = bbCopyEntity(r->objects[objID]);
                if (yaw.toLower() == "random") {
                    chp->randomYaw[j] = bbRnd(360);
                    bbRotateEntity(chp->obj[j],0,chp->randomYaw[j],0);
                } else {
                    bbRotateEntity(chp->obj[j],0,(float)(yaw),0);
                }
                bbPositionEntity(chp->obj[j],(float)(x),0,(float)(z));
                bbScaleEntity(chp->obj[j],RoomScale,RoomScale,RoomScale);
                bbEntityType(chp->obj[j],HIT_MAP);
                bbEntityPickMode(chp->obj[j],2);
                //EntityParent(chp\obj[j],r\obj)
            }
            chp2 = Before(chp);
            if (chp2 != nullptr) {
                chp->id = chp2->id+1;
            }
            std::cout << "<<<<<<<<<<<<<<<<";
            std::cout << "Generated 1499 chunk "+String(chp->id)+" sucessfully";
        }
    }

    bbSeedRnd(bbMilliSecs());

}

Chunk* CreateChunk(int obj, float x, float y, float z, int spawnNPCs = true) {
    Chunk* ch = new Chunk();
    ChunkPart* chp;
    int i;
    NPC* n;

    //If obj%<>0
    //	ch\obj = CopyEntity(obj%)
    //	PositionEntity(ch\obj,x,y,z)
    //	ScaleEntity(ch\obj,RoomScale,RoomScale,RoomScale)
    //	EntityType(ch\obj,HIT_MAP)
    //EndIf

    //ch\debugobj = CreateCube()
    //ScaleEntity(ch\debugobj,20,20,20)
    //PositionEntity(ch\debugobj,x#,y#+20,z#)
    //EntityColor(ch\debugobj,Rand(0,255),Rand(0,255),Rand(0,255))
    //EntityFX(ch\debugobj,1+FE_WIRE)

    if (obj > -1) {
        for (int iterator174 = 0; iterator174 < ChunkPart::getListSize(); iterator174++) {
            chp = ChunkPart::getObject(iterator174);

            if (chp->id == obj) {
                ch->amount = chp->amount;
                for (i = 0; i <= chp->amount; i++) {
                    ch->obj[i] = bbCopyEntity(chp->obj[i]);
                    bbPositionEntity(ch->obj[i],x,y,z);
                    //ScaleEntity(ch\obj[i],RoomScale,RoomScale,RoomScale)
                    bbMoveEntity(ch->obj[i],bbEntityX(chp->obj[i]),0,bbEntityZ(chp->obj[i]));
                }
                break;
            }
        }
        if (spawnNPCs) {
            for (i = 0; i <= bbRand(5,10); i++) {
                n = CreateNPC(NPCtype1499,x+bbRnd(-60.0,60.0),y+0.5,z+bbRnd(-60.0,60.0));
                if (bbRand(2)==1) {
                    n->state2 = 500*3;
                }
                n->angle = bbRnd(360);
            }
        }
    }

    ch->x = x;
    ch->z = z;
    ch->y = y;

    return ch;
}

void UpdateChunks(Room* r, int ChunkPartAmount, int spawnNPCs = true) {
    Chunk* ch;
    Chunk* ch2;
    ChunkPart* chp;
    int ChunkPartAmount2;
    int ChunkHideDistance = 120;
    int temp = false;
    int temp2 = false;
    float x;
    float z;
    int i;
    float y;
    float CurrChunkX;
    float CurrChunkZ;
    int obj;

    for (int iterator175 = 0; iterator175 < Chunk::getListSize(); iterator175++) {
        ch = Chunk::getObject(iterator175);

        //If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),ch\x,ch\z)<ChunkHideDistance
        //	;If (ch\obj <> 0) Then ShowEntity(ch\obj)
        //	If ch\obj[0]<>0
        //		For i = 0 To ch\amount
        //			ShowEntity(ch\obj[i])
        //		Next
        //	EndIf
        //Else
        //	;If (ch\obj <> 0) Then HideEntity(ch\obj)
        //	If ch\obj[0]<>0
        //		For i = 0 To ch\amount
        //			HideEntity(ch\obj[i])
        //		Next
        //	EndIf
        //EndIf
        if (ch->obj[0]!=0) {
            for (i = 0; i <= ch->amount; i++) {
                bbShowEntity(ch->obj[i]);
            }
        }
        y = ch->y;
        if (abs(bbEntityX(mainPlayer->collider)-ch->x)<20) {
            if (abs(bbEntityZ(mainPlayer->collider)-ch->z)<20) {
                CurrChunkX = ch->x;
                CurrChunkZ = ch->z;
            }
        }
    }

    //CurrChunkX# = (int)(EntityX(mainPlayer\collider)/40)*40
    //CurrChunkZ# = (int)(EntityZ(mainPlayer\collider)/40)*40

    x = -(ChunkHideDistance+(CurrChunkX));
    z = -(ChunkHideDistance+(CurrChunkZ));

    String StrTemp = "";
    bbSeedRnd(SeedStringToInt(RandomSeed));

    while (true) {
        temp2 = false;
        for (int i = 0; i < Chunk::getListSize(); i++) {
            ch = Chunk::getObject(i);

            if ((ch->x==x) & (ch->z==z)) {
                temp2 = true;
                break;
            }
        }
        if (!temp2) {
            //ch2 = CreateChunk(r\objects[Rand(1,ChunkPartAmount%)],x#,y#,z#)
            ChunkPartAmount2 = GetINIInt("Data/1499chunks.INI","general","count");
            ch2 = CreateChunk(bbRand(0,ChunkPartAmount2),x,y,z,spawnNPCs);
        }
        if (x < (ChunkHideDistance+(CurrChunkX))) {
            x = x + 40;
        } else {
            if (z < (ChunkHideDistance+(CurrChunkZ))) {
                x = -(ChunkHideDistance+(CurrChunkX));
                z = z + 40;
            } else {
                break;
            }
        }
    }

    bbSeedRnd(bbMilliSecs());
}

void HideChunks() {
    Chunk* ch;
    for (int i = 0; i < Chunk::getListSize(); i++) {
        ch = Chunk::getObject(i);

        //If (ch\obj <> 0) Then HideEntity(ch\obj)
        if (ch->obj[0]!=0) {
            for (int j = 0; j < ch->amount; j++) {
                bbHideEntity(ch->obj[j]);
            }
        }
    }
}

}

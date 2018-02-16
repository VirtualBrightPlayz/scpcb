Function FillRoom008(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    ;the container
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 292.0 * RoomScale, 130.0*RoomScale, r\z + 516.0 * RoomScale, True)
    
    ;the lid of the container
    r\Objects[1] = LoadMesh_Strict("GFX\map\008_2.b3d")
    ScaleEntity r\Objects[1], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[1], r\x + 292 * RoomScale, 151 * RoomScale, r\z + 576.0 * RoomScale, 0)
    EntityParent(r\Objects[1], r\obj)
    
    RotateEntity(r\Objects[1],89,0,0,True)
    
    Glasstex = LoadTexture_Strict("GFX\map\glass.png",1+2)
    r\Objects[2] = CreateSprite()
    EntityTexture(r\Objects[2],Glasstex)
    SpriteViewMode(r\Objects[2],2)
    ScaleSprite(r\Objects[2],256.0*RoomScale*0.5, 194.0*RoomScale*0.5)
    PositionEntity(r\Objects[2], r\x - 176.0 * RoomScale, 224.0*RoomScale, r\z + 448.0 * RoomScale)
    TurnEntity(r\Objects[2],0,90,0)			
    EntityParent(r\Objects[2], r\obj)
    
    FreeTexture Glasstex
    
    ;scp-173 spawnpoint
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x - 445.0 * RoomScale, 120.0*RoomScale, r\z + 544.0 * RoomScale, True)
    
    ;scp-173 attack point
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x + 67.0 * RoomScale, 120.0*RoomScale, r\z + 464.0 * RoomScale, True)
    
    r\Objects[5] = CreateSprite()
    PositionEntity(r\Objects[5], r\x - 158 * RoomScale, 368 * RoomScale, r\z + 298.0 * RoomScale)
    ScaleSprite(r\Objects[5], 0.02, 0.02)
    EntityTexture(r\Objects[5], LightSpriteTex(1))
    EntityBlend (r\Objects[5], 3)
    EntityParent(r\Objects[5], r\obj)
    HideEntity r\Objects[5]
    
    d = CreateDoor(r\zone, r\x + 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 4)
    d\AutoClose = False
    PositionEntity (d\buttons[1], r\x + 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    FreeEntity d\buttons[0] : d\buttons[0]=0
    FreeEntity d\obj2 : d\obj2=0
    r\RoomDoors[0] = d
    
    d2 = CreateDoor(r\zone, r\x + 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
    d2\AutoClose = False
    PositionEntity (d2\buttons[0], r\x + 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
    RotateEntity(d2\buttons[0], 0, -90, 0, True)			
    PositionEntity (d2\buttons[1], r\x + 164.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 128.0 * RoomScale, True)
    FreeEntity d2\obj2 : d2\obj2=0
    r\RoomDoors[1] = d2
    
    d\LinkedDoor = d2
    d2\LinkedDoor = d
    
    d = CreateDoor(r\zone, r\x - 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 0, r, False, 0, 4)
    d\AutoClose = False : d\locked = True : r\RoomDoors[2]=d
    
    
    it = CreateItem("Hazmat Suit", "hazmatsuit", r\x - 76.0 * RoomScale, 0.5, r\z - 396.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
    
    it = CreateItem("Document SCP-008", "paper", r\x - 245.0 * RoomScale, r\y + 192.0 * RoomScale, r\z + 368.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    ;spawnpoint for the scientist used in the "008 zombie scene"
    r\Objects[6] = CreatePivot(r\obj)
    PositionEntity(r\Objects[6], r\x + 160 * RoomScale, 672 * RoomScale, r\z - 384.0 * RoomScale, True)
    ;spawnpoint for the player
    r\Objects[7] = CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x, 672 * RoomScale, r\z + 352.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x+578.956*RoomScale, r\y+444.956*RoomScale, r\z+772.0*RoomScale, r)
    sc\angle = 135
    sc\turn = 45
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 7
End Function

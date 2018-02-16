Function FillRoom2Testroom(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x - 640.0 * RoomScale, 0.5, r\z - 912.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x - 632.0 * RoomScale, 0.5, r\z - 16.0 * RoomScale)
    EntityParent(r\Objects[1], r\obj)
    
    Local Glasstex = LoadTexture_Strict("GFX\map\glass.png",1+2)
    r\Objects[2] = CreateSprite()
    EntityTexture(r\Objects[2],Glasstex)
    SpriteViewMode(r\Objects[2],2)
    ScaleSprite(r\Objects[2],182.0*RoomScale*0.5, 192.0*RoomScale*0.5)
    PositionEntity(r\Objects[2], r\x - 595.0 * RoomScale, 224.0*RoomScale, r\z - 208.0 * RoomScale)
    TurnEntity(r\Objects[2],0,180,0)			
    EntityParent(r\Objects[2], r\obj)
    HideEntity (r\Objects[2])
    
    FreeTexture Glasstex
    
    ;d = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    ;d\AutoClose = False : d\open = False
    r\RoomDoors[0] = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
    
    d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, 0.0, r\z + 384.0 * RoomScale, 0, r, False, False)
    d\AutoClose = False : d\open = False					
    
    d = CreateDoor(r\zone, r\x - 816.0 * RoomScale, 0.0, r\z - 208.0 * RoomScale, 0, r, False, False)
    d\AutoClose = False : d\open = False
    FreeEntity(d\buttons[0]) : d\buttons[0]=0
    FreeEntity(d\buttons[1]) : d\buttons[1]=0
    
    it = CreateItem("Level 2 Key Card", "key2", r\x - 336.0 * RoomScale, r\y + 165.0 * RoomScale, r\z + 88.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("S-NAV 300 Navigator", "nav", r\x - 312.0 * RoomScale, r\y + 264.0 * RoomScale, r\z + 176.0 * RoomScale)
    it\state = 20 : EntityParent(it\collider, r\obj)
End Function

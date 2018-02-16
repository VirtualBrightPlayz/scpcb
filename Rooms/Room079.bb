Function FillRoom079(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x, -448.0*RoomScale, r\z + 1136.0 * RoomScale, 0, r, False,True, 4)
    d\dir = 1 : d\AutoClose = False : d\open = False
    PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, -250*RoomScale, r\z + 918.0 * RoomScale, True)
    ;TurnEntity(d\buttons[0],0,-90,0,True)
    PositionEntity(d\buttons[0], r\x - 240.0 * RoomScale, -250*RoomScale, r\z + 1366.0 * RoomScale, True)
    ;TurnEntity(d\buttons[1],0, 90,0,True)	
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 1456.0*RoomScale, -448.0*RoomScale, r\z + 976.0 * RoomScale, 0, r, False, True, 3)
    r\RoomDoors[0]\dir = 1 : r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 1760.0 * RoomScale, -250*RoomScale, r\z + 1236.0 * RoomScale, True)
    TurnEntity(r\RoomDoors[0]\buttons[0],0,-90-90,0,True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 1760.0 * RoomScale, -240*RoomScale, r\z + 740.0 * RoomScale, True)
    TurnEntity(r\RoomDoors[0]\buttons[1],0, 90-90,0,True)
    
    CreateDoor(0, r\x + 1144.0*RoomScale, -448.0*RoomScale, r\z + 704.0 * RoomScale, 90, r, False, False, -1)
    
    r\Objects[0] = LoadAnimMesh_Strict("GFX\map\079.b3d")
    ScaleEntity(r\Objects[0], 1.3, 1.3, 1.3, True)
    PositionEntity (r\Objects[0], r\x + 1856.0*RoomScale, -560.0*RoomScale, r\z-672.0*RoomScale, True)
    EntityParent(r\Objects[0], r\obj)
    TurnEntity r\Objects[0],0,180,0,True
    
    r\Objects[1] = CreateSprite(r\Objects[0])
    SpriteViewMode(r\Objects[1],2)
    PositionEntity(r\Objects[1], 0.082, 0.119, 0.010)
    ScaleSprite(r\Objects[1],0.18*0.5,0.145*0.5)
    TurnEntity(r\Objects[1],0,13.0,0)
    MoveEntity r\Objects[1], 0,0,-0.022
    EntityTexture (r\Objects[1],OldAiPics(0))
    
    HideEntity r\Objects[1]
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity (r\Objects[2], r\x + 1184.0*RoomScale, -448.0*RoomScale, r\z+1792.0*RoomScale, True)
    
    de.Decals = CreateDecal(3,  r\x + 1184.0*RoomScale, -448.0*RoomScale+0.01, r\z+1792.0*RoomScale,90,Rnd(360),0)
    de\Size = 0.5
    ScaleSprite(de\obj, de\Size,de\Size)
    EntityParent de\obj, r\obj
End Function
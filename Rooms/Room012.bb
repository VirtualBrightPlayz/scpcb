Function FillRoom012(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 672.0 * RoomScale, 270, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 880.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 304.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 840.0 * RoomScale, True)	
    TurnEntity d\buttons[1],0,0,0,True
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x -512.0 * RoomScale, -768.0*RoomScale, r\z -336.0 * RoomScale, 0, r, False, False)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False : r\RoomDoors[0]\locked = True
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 176.0 * RoomScale, -512.0*RoomScale, r\z - 364.0 * RoomScale, True)
    FreeEntity r\RoomDoors[0]\buttons[1] : r\RoomDoors[0]\buttons[1]=0
    
    r\Objects[0] = CopyEntity(LeverBaseOBJ)
    r\Objects[1] = CopyEntity(LeverOBJ)
    
    r\Levers[0] = r\Objects[1]
    
    For i% = 0 To 1
        ScaleEntity(r\Objects[i], 0.04, 0.04, 0.04)
        PositionEntity (r\Objects[i], r\x + 240.0 * RoomScale, r\y - 512.0 * RoomScale, r\z - 364 * RoomScale, True)
        
        EntityParent(r\Objects[i], r\obj)
    Next
    ;RotateEntity(r\Objects[0], 0, 0, 0)
    RotateEntity(r\Objects[1], 10, -180, 0)
    
    EntityPickMode r\Objects[1], 1, False
    EntityRadius r\Objects[1], 0.1
    
    r\Objects[2] = LoadMesh_Strict("GFX\map\room012_2.b3d")
    ScaleEntity r\Objects[2], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[2], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\Objects[2], r\obj)
    
    r\Objects[3] = CreateSprite()
    PositionEntity(r\Objects[3], r\x - 43.5 * RoomScale, - 574 * RoomScale, r\z - 362.0 * RoomScale)
    ScaleSprite(r\Objects[3], 0.015, 0.015)
    EntityTexture(r\Objects[3], LightSpriteTex(1))
    EntityBlend (r\Objects[3], 3)
    EntityParent(r\Objects[3], r\obj)
    HideEntity r\Objects[3]
    
    r\Objects[4] = LoadMesh_Strict("GFX\map\room012_3.b3d")
    tex=LoadTexture_Strict("GFX\map\scp-012_0.jpg")
    EntityTexture r\Objects[4],tex, 0,1
    ScaleEntity r\Objects[4], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[4], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\Objects[4], r\Objects[2])
    
    it = CreateItem("Document SCP-012", "paper", r\x - 56.0 * RoomScale, r\y - 576.0 * RoomScale, r\z - 408.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it.Items = CreateItem("Severed Hand", "hand", r\x - 784*RoomScale, -576*RoomScale+0.3, r\z+640*RoomScale)
    EntityParent(it\collider, r\obj)
    
    de.Decals = CreateDecal(3,  r\x - 784*RoomScale, -768*RoomScale+0.01, r\z+640*RoomScale,90,Rnd(360),0)
    de\Size = 0.5
    ScaleSprite(de\obj, de\Size,de\Size)
    EntityParent de\obj, r\obj
End Function

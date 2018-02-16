Function FillRoom860(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump	

    ;the wooden door
    r\Objects[2] = LoadMesh_Strict("GFX\map\forest\door_frame.b3d")
    PositionEntity r\Objects[2],r\x + 184.0 * RoomScale,0,r\z,True
    ScaleEntity r\Objects[2],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
    EntityParent r\Objects[2],r\obj
    
    r\Objects[3] =  LoadMesh_Strict("GFX\map\forest\door.b3d")
    PositionEntity r\Objects[3],r\x + 112.0 * RoomScale,0,r\z+0.05,True
    EntityType r\Objects[3], HIT_MAP
    
    ScaleEntity r\Objects[3],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[3],r\obj
    
    r\Objects[4] = CopyEntity(r\Objects[3])
    PositionEntity r\Objects[4],r\x + 256.0 * RoomScale,0,r\z-0.05,True
    RotateEntity r\Objects[4], 0,180,0
    ScaleEntity r\Objects[4],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[4],r\obj
    
    ;doors to observation booth
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,False,"ABCD")
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z - 640.0 * RoomScale,0,r,True,False,False,"ABCD")
    d\AutoClose = False
    
    ;doors to the room itself
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z - 640.0 * RoomScale,0,r,False,False,1)
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,1)
    
    ;the forest
    Local fr.Forest = New Forest
    r\fr=fr
    GenForestGrid(fr)
    PlaceForest(fr,r\x,r\y+30.0,r\z,r)
    ;EntityParent fr\Forest_Pivot,r\obj
    
    it = CreateItem("Document SCP-860-1", "paper", r\x + 672.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 335.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle+10, 0
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Document SCP-860", "paper", r\x + 1152.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 384.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle+170, 0
    EntityParent(it\collider, r\obj)
End Function

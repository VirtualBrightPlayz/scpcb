Function FillRoom1123(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    it = CreateItem("Document SCP-1123", "paper", r\x + 511.0 * RoomScale, r\y + 125.0 * RoomScale, r\z - 936.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("SCP-1123", "1123", r\x + 832.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 784.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Leaflet", "paper", r\x - 816.0 * RoomScale, r\y + 704.0 * RoomScale, r\z+ 888.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Gas Mask", "gasmask", r\x + 457.0 * RoomScale, r\y + 150.0 * RoomScale, r\z + 960.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    d.Doors = CreateDoor(r\zone, r\x + 832.0 * RoomScale, 0.0, r\z + 367.0 * RoomScale, 0, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 956.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 352.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 713.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 384.0 * RoomScale, True)
    FreeEntity d\obj2 : d\obj2 = 0
    d.Doors = CreateDoor(r\zone, r\x + 280.0 * RoomScale, 0.0, r\z - 607.0 * RoomScale, 90, r, False, False)
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    
    d.Doors = CreateDoor(r\zone, r\x + 280.0 * RoomScale, 512.0 * RoomScale, r\z - 607.0 * RoomScale, 90, r, False, False)
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    FreeEntity d\buttons[1] : d\buttons[1]=0
    r\RoomDoors[0] = d
    ;PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)		
    
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 832.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 784.0 * RoomScale, True)
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x -648.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 692.0 * RoomScale, True)
    r\Objects[5] = CreatePivot(r\obj)
    PositionEntity(r\Objects[5], r\x + 828.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 592.0 * RoomScale, True)
    
    r\Objects[6] = CreatePivot(r\obj)
    PositionEntity(r\Objects[6], r\x - 76.0 * RoomScale, r\y + 620.0 * RoomScale, r\z + 744.0 * RoomScale, True)
    r\Objects[7] = CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x - 640.0 * RoomScale, r\y + 620.0 * RoomScale, r\z - 864.0 * RoomScale, True)	
    
    r\Objects[8] = LoadMesh_Strict("GFX\map\forest\door_frame.b3d")
    PositionEntity r\Objects[8], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 288.0 * RoomScale,True
    RotateEntity r\Objects[8],0,90,0,True
    ScaleEntity r\Objects[8],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
    EntityParent r\Objects[8],r\obj
    
    r\Objects[9] =  LoadMesh_Strict("GFX\map\forest\door.b3d")
    PositionEntity r\Objects[9],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (288.0-70) * RoomScale,True
    RotateEntity r\Objects[9],0,10,0,True
    EntityType r\Objects[9], HIT_MAP
    ScaleEntity r\Objects[9],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[9],r\obj
    
    r\Objects[10] = CopyEntity(r\Objects[8])
    PositionEntity r\Objects[10], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 736.0 * RoomScale,True
    RotateEntity r\Objects[10],0,90,0,True
    ScaleEntity r\Objects[10],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
    EntityParent r\Objects[10],r\obj
    
    r\Objects[11] =  CopyEntity(r\Objects[9])
    PositionEntity r\Objects[11],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (736.0-70) * RoomScale,True
    RotateEntity r\Objects[11],0,90,0,True
    EntityType r\Objects[11], HIT_MAP
    ScaleEntity r\Objects[11],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[11],r\obj
    
    r\Objects[12] = CopyEntity(r\Objects[8])
    PositionEntity r\Objects[12], r\x - 592.0 * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True
    RotateEntity r\Objects[12],0,0,0,True
    ScaleEntity r\Objects[12],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
    EntityParent r\Objects[12],r\obj
    
    r\Objects[13] =  CopyEntity(r\Objects[9])
    PositionEntity r\Objects[13],r\x - (592.0+70.0) * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True
    RotateEntity r\Objects[13],0,0,0,True
    EntityType r\Objects[13], HIT_MAP
    ScaleEntity r\Objects[13],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[13],r\obj	
    
    r\Objects[14] = LoadMesh_Strict("GFX\map\1123_hb.b3d",r\obj)
    EntityPickMode r\Objects[14],2
    EntityType r\Objects[14],HIT_MAP
    EntityAlpha r\Objects[14],0.0
End Function

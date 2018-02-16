Function FillRoom914(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    ;d = CreateDoor(r\zone, r\x, 0, r\z - 368.0 * RoomScale, 0, r, False, True, 2)
    ;d\dir = 1 : d\AutoClose = False : d\open = False
    ;PositionEntity (d\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    ;TurnEntity(d\buttons[0], 0, 90, 0)
    r\RoomDoors[2] = CreateDoor(r\zone,r\x,0,r\z-368.0*RoomScale,0,r,False,True,2)
    r\RoomDoors[2]\dir=1 : r\RoomDoors[2]\AutoClose=False : r\RoomDoors[2]\open=False
    PositionEntity (r\RoomDoors[2]\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    TurnEntity(r\RoomDoors[2]\buttons[0], 0, 90, 0)
    
    r\Objects[0] = LoadMesh_Strict("GFX\map\914key.x")
    r\Objects[1] = LoadMesh_Strict("GFX\map\914knob.x")
    
    For  i% = 0 To 1
        ScaleEntity(r\Objects[i], RoomScale, RoomScale, RoomScale)
        EntityPickMode(r\Objects[i], 2)
    Next
    
    PositionEntity (r\Objects[0], r\x, r\y + 190.0 * RoomScale, r\z + 374.0 * RoomScale)
    PositionEntity (r\Objects[1], r\x, r\y + 230.0 * RoomScale, r\z + 374.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    EntityParent(r\Objects[1], r\obj)
    
    d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity (d\obj2) : d\obj2 = 0
    FreeEntity (d\buttons[0]) : d\buttons[0] = 0
    FreeEntity (d\buttons[1]) : d\buttons[1] = 0
    r\RoomDoors[0] = d: d\AutoClose = False
    
    d = CreateDoor(r\zone, r\x + 816.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity (d\obj2) : d\obj2 = 0	
    FreeEntity (d\buttons[0]) : d\buttons[0] = 0
    FreeEntity (d\buttons[1]) : d\buttons[1] = 0
    r\RoomDoors[1] = d : d\AutoClose = False
    
    r\Objects[2] = CreatePivot()
    r\Objects[3] = CreatePivot()
    PositionEntity(r\Objects[2], r\x - 712.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    PositionEntity(r\Objects[3], r\x + 728.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    EntityParent(r\Objects[2], r\obj)
    EntityParent(r\Objects[3], r\obj)
    
    it = CreateItem("Note", "paper", r\x +954.0 * RoomScale, r\y +228.0 * RoomScale, r\z + 127.0 * RoomScale)
    EntityParent(it\collider, r\obj)	
    
    it = CreateItem("First Aid Kit", "firstaid", r\x + 960.0 * RoomScale, r\y + 112.0 * RoomScale, r\z - 40.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
    
    it = CreateItem("Dr. L's Note", "paper", r\x - 928.0 * RoomScale, 160.0 * RoomScale, r\z - 160.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

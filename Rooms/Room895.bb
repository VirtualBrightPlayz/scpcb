Function FillRoom895(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x, 0, r\z - 448.0 * RoomScale, 0, r, False, True, 2)
    d\AutoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x - 384.0 * RoomScale, 0.7, r\z - 280.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x - 320.0 * RoomScale, r\y + 704 * RoomScale, r\z + 288.0 * RoomScale, r, True)
    sc\angle = 45 + 180
    sc\turn = 45
    sc\CoffinEffect = True
    TurnEntity(sc\CameraObj, 120, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    CoffinCam = sc
    
    PositionEntity(sc\ScrObj, r\x - 800 * RoomScale, 288.0 * RoomScale, r\z - 340.0 * RoomScale)
    EntityParent(sc\ScrObj, r\obj)
    TurnEntity(sc\ScrObj, 0, 180, 0)
    
    r\Objects[2] = CopyEntity(LeverBaseOBJ)
    r\Objects[3] = CopyEntity(LeverOBJ)
        
    r\Levers[0] = r\Objects[3]
        
    For i% = 0 To 1
        ScaleEntity(r\Objects[2 + i], 0.04, 0.04, 0.04)
        PositionEntity (r\Objects[2 + i], r\x - 800.0 * RoomScale, r\y + 180.0 * RoomScale, r\z - 336 * RoomScale, True)
            
        EntityParent(r\Objects[2 + i], r\obj)
    Next
    RotateEntity(r\Objects[2], 0, 180, 0)
    RotateEntity(r\Objects[3], 10, 0, 0)
    
    EntityPickMode r\Objects[3], 1, False
    EntityRadius r\Objects[3], 0.1
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x, -1320.0 * RoomScale, r\z + 2304.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    
    it = CreateItem("Document SCP-895", "paper", r\x - 688.0 * RoomScale, r\y + 133.0 * RoomScale, r\z - 304.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Level 3 Key Card", "key3", r\x + 240.0 * RoomScale, r\y -1456.0 * RoomScale, r\z + 2064.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Night Vision Goggles", "nvgoggles", r\x + 280.0 * RoomScale, r\y -1456.0 * RoomScale, r\z + 2164.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x + 96.0*RoomScale, -1532.0 * RoomScale, r\z + 2016.0 * RoomScale,True)
    
    ;de.Decals = CreateDecal(0, r\x + 96.0*RoomScale, -1535.0 * RoomScale, r\z + 32.0 * RoomScale, 90, Rand(360), 0)
    ;EntityParent de\obj, r\obj
End Function

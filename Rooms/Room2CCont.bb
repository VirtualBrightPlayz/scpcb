Function FillRoom2CCont(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    d = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, 2)
    d\AutoClose = False : d\open = False
    
    it = CreateItem("Note from Daniel", "paper", r\x-400.0*RoomScale,1040.0*RoomScale,r\z+115.0*RoomScale)
    EntityParent(it\collider, r\obj)
    
    For n% = 0 To 2
        r\Objects[n * 2] = CopyEntity(LeverBaseOBJ)
        r\Objects[n * 2 + 1] = CopyEntity(LeverOBJ)
        
        r\Levers[n] = r\Objects[n * 2 + 1]
        
        For  i% = 0 To 1
            ScaleEntity(r\Objects[n * 2 + i], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[n * 2 + i], r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 64.0 * n) * RoomScale, True)
            
            EntityParent(r\Objects[n * 2 + i], r\obj)
        Next
        RotateEntity(r\Objects[n * 2], 0, -90, 0)
        RotateEntity(r\Objects[n * 2 + 1], 10, -90 - 180, 0)
        
        EntityPickMode r\Objects[n * 2 + 1], 1, False
        EntityRadius r\Objects[n * 2 + 1], 0.1
    Next
    
    sc.SecurityCams = CreateSecurityCam(r\x-265.0*RoomScale, r\y+1280.0*RoomScale, r\z+105.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 45
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 10
End Function

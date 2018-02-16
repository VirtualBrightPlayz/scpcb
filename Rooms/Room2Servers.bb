Function FillRoom2Servers(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, False)
    d\locked = True
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z - 736.0 * RoomScale, 90, r, True, False)
    r\RoomDoors[0]\AutoClose=False
    r\RoomDoors[1] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z + 736.0 * RoomScale, 90, r, True, False)
    r\RoomDoors[1]\AutoClose=False
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 672.0 * RoomScale, 0.0, r\z - 1024.0 * RoomScale, 0, r, False, False, False, "GEAR")
    r\RoomDoors[2]\AutoClose=False : r\RoomDoors[2]\DisableWaypoint = True 
    FreeEntity(r\RoomDoors[2]\buttons[0]) : r\RoomDoors[2]\buttons[0]=0
    FreeEntity(r\RoomDoors[2]\buttons[1]) : r\RoomDoors[2]\buttons[1]=0
    
    For n% = 0 To 2
        r\Objects[n * 2] = CopyEntity(LeverBaseOBJ)
        r\Objects[n * 2 + 1] = CopyEntity(LeverOBJ)
        
        r\Levers[n] = r\Objects[n * 2 + 1]
        
        For i% = 0 To 1
            ScaleEntity(r\Objects[n * 2 + i], 0.03, 0.03, 0.03)
            
            Select n
                Case 0 ;power switch
                    PositionEntity (r\Objects[n * 2 + i], r\x - 1260.0 * RoomScale, r\y + 234.0 * RoomScale, r\z + 750 * RoomScale, True)	
                Case 1 ;generator fuel pump
                    PositionEntity (r\Objects[n * 2 + i], r\x - 920.0 * RoomScale, r\y + 164.0 * RoomScale, r\z + 898 * RoomScale, True)
                Case 2 ;generator on/off
                    PositionEntity (r\Objects[n * 2 + i], r\x - 837.0 * RoomScale, r\y + 152.0 * RoomScale, r\z + 886 * RoomScale, True)
            End Select
            
            EntityParent(r\Objects[n * 2 + i], r\obj)
        Next
        ;RotateEntity(r\Objects[n * 2], 0, -90, 0)
        RotateEntity(r\Objects[n*2+1], 81, -180, 0)
        
        ;EntityPickMode(r\Objects[n * 2 + 1], 2)
        EntityPickMode r\Objects[n * 2 + 1], 1, False
        EntityRadius r\Objects[n * 2 + 1], 0.1
        ;makecollbox(r\Objects[n * 2 + 1])
    Next
    
    RotateEntity(r\Objects[2+1], -81, -180, 0)
    RotateEntity(r\Objects[4+1], -81, -180, 0)
    
    ;096 spawnpoint
    r\Objects[6]=CreatePivot(r\obj)
    PositionEntity(r\Objects[6], r\x - 848*RoomScale, 0.5, r\z-576*RoomScale, True)
    ;guard spawnpoint
    r\Objects[7]=CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x - 1328.0 * RoomScale, 0.5, r\z + 528*RoomScale, True)
    ;the point where the guard walks to
    r\Objects[8]=CreatePivot(r\obj)
    PositionEntity(r\Objects[8], r\x - 1376.0 * RoomScale, 0.5, r\z + 32*RoomScale, True)
    
    r\Objects[9]=CreatePivot(r\obj)
    PositionEntity(r\Objects[9], r\x - 848*RoomScale, 0.5, r\z+576*RoomScale, True)
End Function

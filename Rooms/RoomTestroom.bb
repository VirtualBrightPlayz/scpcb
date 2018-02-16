 Function FillRoomTestroom(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    For xtemp = 0 To 1
        For ztemp = -1 To 1
            r\Objects[xtemp * 3 + (ztemp + 1)] = CreatePivot()
            PositionEntity(r\Objects[xtemp * 3 + (ztemp + 1)], r\x + (-236.0 + 280.0 * xtemp) * RoomScale, -700.0 * RoomScale, r\z + 384.0 * ztemp * RoomScale)
            EntityParent(r\Objects[xtemp * 3 + (ztemp + 1)], r\obj)
        Next
    Next
    
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6], r\x + 754.0 * RoomScale, r\y - 1248.0 * RoomScale, r\z)
    EntityParent(r\Objects[6], r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x + 744.0 * RoomScale, r\y - 856.0 * RoomScale, r\z + 236.0 * RoomScale, r)
    sc\FollowPlayer = True
    
    CreateDoor(0, r\x + 720.0 * RoomScale, 0, r\z, 0, r, False, 2, -1)
    
    CreateDoor(0, r\x - 624.0 * RoomScale, -1280.0 * RoomScale, r\z, 90, r, True)			
    
    it = CreateItem("Document SCP-682", "paper", r\x + 656.0 * RoomScale, r\y - 1200.0 * RoomScale, r\z - 16.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

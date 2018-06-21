Function FillRoom_tnnl_maintenance_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x + 2640.0 * RoomScale, -2496.0 * RoomScale, r\z + 400.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x - 4336.0 * RoomScale, -2496.0 * RoomScale, r\z - 2512.0 * RoomScale)
    EntityParent(r\Objects[1], r\obj)
    
    r\Objects[2] = CreatePivot()
    RotateEntity r\Objects[2],0.0,180.0,0.0,True
    PositionEntity(r\Objects[2], r\x + 552.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale)
    EntityParent(r\Objects[2], r\obj)
;			
    r\Objects[4] = CreatePivot()
    PositionEntity(r\Objects[4], r\x - 552.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale)
    EntityParent(r\Objects[4], r\obj)
;			
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[0]\autoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 224.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 304.0 * RoomScale, 0.7, r\z + 832.0 * RoomScale, True)			
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[2]\autoClose = False : r\RoomDoors[2]\open = True
    PositionEntity(r\RoomDoors[2]\buttons[0], r\x - 224.0 * RoomScale, 0.7, r\z - 480.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[2]\buttons[1], r\x - 304.0 * RoomScale, 0.7, r\z - 832.0 * RoomScale, True)
;			
    temp = ((Int(AccessCode)*3) Mod 10000)
    If temp < 1000 Then temp = temp+1000
    d.Doors = CreateDoor(0, r\x,r\y,r\z,0, r, False, True, False, temp)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, r\y + 0.7, r\z - 384.0 * RoomScale, True)
    RotateEntity (d\buttons[0], 0,-90,0,True)
    PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, r\y + 0.7, r\z + 384.0 * RoomScale, True)		
    RotateEntity (d\buttons[1], 0,90,0,True)
    
    de.Decals = CreateDecal(0, r\x + 64.0 * RoomScale, 0.005, r\z + 144.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    it = CreateItem("Scorched Note", "paper", r\x + 64.0 * RoomScale, r\y +144.0 * RoomScale, r\z - 384.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_tnnl_maintenance_2(e.Events)
	;TODO: reimplement lol
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
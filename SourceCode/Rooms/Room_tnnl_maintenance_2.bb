Function FillRoom_tnnl_maintenance_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1;, Bump

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x + 2640.0 * RoomScale, -2496.0 * RoomScale, r\z + 400.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)

    r\objects[1] = CreatePivot()
    PositionEntity(r\objects[1], r\x - 4336.0 * RoomScale, -2496.0 * RoomScale, r\z - 2512.0 * RoomScale)
    EntityParent(r\objects[1], r\obj)

    r\objects[2] = CreatePivot()
    RotateEntity r\objects[2],0.0,180.0,0.0,True
    PositionEntity(r\objects[2], r\x + 552.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale)
    EntityParent(r\objects[2], r\obj)
;
    r\objects[4] = CreatePivot()
    PositionEntity(r\objects[4], r\x - 552.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale)
    EntityParent(r\objects[4], r\obj)
;
    r\roomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = True
    PositionEntity(r\roomDoors[0]\buttons[1], r\x + 224.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x + 304.0 * RoomScale, 0.7, r\z + 832.0 * RoomScale, True)

    r\roomDoors[2] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\roomDoors[2]\autoClose = False : r\roomDoors[2]\open = True
    PositionEntity(r\roomDoors[2]\buttons[0], r\x - 224.0 * RoomScale, 0.7, r\z - 480.0 * RoomScale, True)
    PositionEntity(r\roomDoors[2]\buttons[1], r\x - 304.0 * RoomScale, 0.7, r\z - 832.0 * RoomScale, True)
;
    Local temp% = ((Int(AccessCode)*3) Mod 10000)
    If temp < 1000 Then temp = temp+1000
    d.Doors = CreateDoor(0, r\x,r\y,r\z,0, r, False, True, False, temp)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, r\y + 0.7, r\z - 384.0 * RoomScale, True)
    RotateEntity (d\buttons[0], 0,-90,0,True)
    PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, r\y + 0.7, r\z + 384.0 * RoomScale, True)
    RotateEntity (d\buttons[1], 0,90,0,True)

    de.Decals = CreateDecal(0, r\x + 64.0 * RoomScale, 0.005, r\z + 144.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    it = CreatePaper("docL6", r\x + 64.0 * RoomScale, r\y +144.0 * RoomScale, r\z - 384.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_tnnl_maintenance_2(e.Events)
	;TODO: reimplement lol
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
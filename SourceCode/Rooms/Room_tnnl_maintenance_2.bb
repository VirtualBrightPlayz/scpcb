Function FillRoom_tnnl_maintenance_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x + 2640.0 * RoomScale, -2496.0 * RoomScale, r\z + 400.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)

    r\objects[1] = CreatePivot()
    PositionEntity(r\objects[1], r\x - 4336.0 * RoomScale, -2496.0 * RoomScale, r\z - 2512.0 * RoomScale)
    EntityParent(r\objects[1], r\obj)

    r\objects[2] = CreatePivot()
    RotateEntity(r\objects[2],0.0,180.0,0.0,True)
    PositionEntity(r\objects[2], r\x + 552.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale)
    EntityParent(r\objects[2], r\obj)
;
    r\objects[4] = CreatePivot()
    PositionEntity(r\objects[4], r\x - 552.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale)
    EntityParent(r\objects[4], r\obj)
;
    r\doors[0] = CreateDoor(r\x + 264.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\doors[0]\autoClose = False : r\doors[0]\open = True
    PositionEntity(r\doors[0]\buttons[1], r\x + 224.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\doors[0]\buttons[0], r\x + 304.0 * RoomScale, 0.7, r\z + 832.0 * RoomScale, True)

    r\doors[2] = CreateDoor(r\x - 264.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\doors[2]\autoClose = False : r\doors[2]\open = True
    PositionEntity(r\doors[2]\buttons[0], r\x - 224.0 * RoomScale, 0.7, r\z - 480.0 * RoomScale, True)
    PositionEntity(r\doors[2]\buttons[1], r\x - 304.0 * RoomScale, 0.7, r\z - 832.0 * RoomScale, True)
;
    Local temp% = ((Int(AccessCode)*3) Mod 10000)
    If (temp < 1000) Then temp = temp+1000
    d = CreateDoor(0, r\x,r\y,r\z,0, r, False, DOOR_TYPE_CONT, "", Str(temp))
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, r\y + 0.7, r\z - 384.0 * RoomScale, True)
    RotateEntity(d\buttons[0], 0,-90,0,True)
    PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, r\y + 0.7, r\z + 384.0 * RoomScale, True)
    RotateEntity(d\buttons[1], 0,90,0,True)

    de = CreateDecal(0, r\x + 64.0 * RoomScale, 0.005, r\z + 144.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    it = CreatePaper("drL6", r\x + 64.0 * RoomScale, r\y +144.0 * RoomScale, r\z - 384.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_tnnl_maintenance_2(e.Event)
	;TODO: reimplement lol
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
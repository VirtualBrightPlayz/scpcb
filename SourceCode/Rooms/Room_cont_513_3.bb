Function FillRoom_cont_513_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\x - 704.0 * RoomScale, 0, r\z + 304.0 * RoomScale, 0, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    d\autoClose = False ;: d\buttons[0] = False
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.0 * RoomScale, True)
    PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.0 * RoomScale, True)

    sc = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 414*RoomScale, r\z + 656*RoomScale, r)
    sc\followPlayer = True

    it = CreateItem("scp513", r\x - 32.0 * RoomScale, r\y + 196.0 * RoomScale, r\z + 688.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreatePaper("drL3", r\x + 736.0 * RoomScale,1.0, r\z + 48.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreatePaper("scp1048", r\x - 480.0 * RoomScale, 104.0*RoomScale, r\z - 176.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
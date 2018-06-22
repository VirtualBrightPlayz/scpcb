Function FillRoom_cont_513_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\zone, r\x - 704.0 * RoomScale, 0, r\z + 304.0 * RoomScale, 0, r, False, 0, 2)
    d\autoClose = False ;: d\buttons[0] = False
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.0 * RoomScale, True)
    PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.0 * RoomScale, True)

    sc.SecurityCams = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 414*RoomScale, r\z + 656*RoomScale, r)
    sc\followPlayer = True

    it = CreateItem("SCP-513", "scp513", r\x - 32.0 * RoomScale, r\y + 196.0 * RoomScale, r\z + 688.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("Blood-stained Note", "paper", r\x + 736.0 * RoomScale,1.0, r\z + 48.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("Document SCP-1048", "paper", r\x - 480.0 * RoomScale, 104.0*RoomScale, r\z - 176.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

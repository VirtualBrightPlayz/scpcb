Function FillRoom_off_gears_may_har_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z + 448.0 * RoomScale, 90, r, False, False, 0, Str(AccessCode))
    PositionEntity(d\buttons[0], r\x + 248.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True),True)
    PositionEntity(d\buttons[1], r\x + 232.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True),True)
    d\autoClose = False : d\open = False

    d = CreateDoor(r\zone, r\x - 496.0 * RoomScale, 0.0, r\z, 90, r, False, False, 0, "ABCD")
    PositionEntity(d\buttons[0], r\x - 488.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True),True)
    PositionEntity(d\buttons[1], r\x - 504.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True),True)
    d\autoClose = False : d\open = False : d\locked = True

    d = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z - 576.0 * RoomScale, 90, r, False, False, 0, "7816")
    PositionEntity(d\buttons[0], r\x + 248.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True),True)
    PositionEntity(d\buttons[1], r\x + 232.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True),True)
    d\autoClose = False : d\open = False

    it = CreatePaper("big_reveal", r\x + 736.0 * RoomScale, r\y + 224.0 * RoomScale, r\z + 544.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    it = CreateItem("vest", r\x + 608.0 * RoomScale, r\y + 112.0 * RoomScale, r\z + 32.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)

    it = CreatePaper("docIR106", r\x + 704.0 * RoomScale, r\y + 183.0 * RoomScale, r\z - 576.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    it = CreatePaper("journal", r\x + 912 * RoomScale, r\y + 176.0 * RoomScale, r\z - 160.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    it = CreateItem("firstaid", r\x + 912.0 * RoomScale, r\y + 112.0 * RoomScale, r\z - 336.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
End Function

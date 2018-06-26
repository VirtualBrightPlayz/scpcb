Function FillRoom_off_rosewood_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\x + 1440.0 * RoomScale, 224.0 * RoomScale, r\z + 32.0 * RoomScale, 90, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    d\autoClose = False : d\open = False

    it = CreateItem("scp420j", r\x + 1776.0 * RoomScale, r\y + 400.0 * RoomScale, r\z + 427.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("scp420j", r\x + 1808.0 * RoomScale, r\y + 400.0 * RoomScale, r\z + 435.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreatePaper("docWar", r\x + 2248.0 * RoomScale, r\y + 440.0 * RoomScale, r\z + 372.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("radio", r\x + 2240.0 * RoomScale, r\y + 320.0 * RoomScale, r\z + 128.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

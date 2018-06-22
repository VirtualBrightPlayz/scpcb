Function FillRoom_srvr_pc_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 672.0 * RoomScale, 270, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 880.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 304.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 840.0 * RoomScale, True)
    TurnEntity d\buttons[1],0,0,0,True
    d.Doors = CreateDoor(r\zone, r\x -512.0 * RoomScale, -768.0*RoomScale, r\z -336.0 * RoomScale, 0, r, False, False, 3)
    d.Doors = CreateDoor(r\zone, r\x -509.0 * RoomScale, -768.0*RoomScale, r\z -1037.0 * RoomScale, 0, r, False, False, 3)
    d.Doors\locked = True
    it = CreateItem("Night Vision Goggles", "nvgoggles", r\x + 56.0154 * RoomScale, r\y - 648.0 * RoomScale, r\z + 749.638 * RoomScale)
    it\state = 20
    RotateEntity it\collider, 0, r\angle+Rand(245), 0
    EntityParent(it\collider, r\obj)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
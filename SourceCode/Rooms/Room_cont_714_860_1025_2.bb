Function FillRoom_cont_714_860_1025_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 90, r, True, False, 3)
    d\autoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    d.Doors = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z+32*RoomScale, 270, r, True, False, 3)
    d\autoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x - 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    r\roomDoors[1] = CreateDoor(r\zone, r\x-560.0 * RoomScale, 0, r\z - 240.0 * RoomScale, 0, r, True, False, 3)
    r\roomDoors[1]\autoClose = False : r\roomDoors[1]\open = False

    r\roomDoors[2] = CreateDoor(r\zone, r\x + 560.0 * RoomScale, 0, r\z - 272.0 * RoomScale, 180, r, True, False, 3)
    r\roomDoors[2]\autoClose = False : r\roomDoors[2]\open = False

    r\roomDoors[3] = CreateDoor(r\zone, r\x + 560.0 * RoomScale, 0, r\z + 272.0 * RoomScale, 180, r, True, False, 3)
    r\roomDoors[3]\autoClose = False : r\roomDoors[3]\open = False

    it = CreateItem("Some SCP-420-J", "420", r\x - 552.0 * RoomScale, r\y + 220.0 * RoomScale, r\z - 728.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("SCP-860", "scp860", r\x + 568.0 * RoomScale, r\y + 178.0 * RoomScale, r\z + 760.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x + 560.0 * RoomScale, r\y + 386 * RoomScale, r\z - 416.0 * RoomScale, r)
    sc\angle = 180 : sc\turn = 30
    TurnEntity(sc\cameraObj, 30, 0, 0)
    EntityParent(sc\obj, r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x - 560.0 * RoomScale, r\y + 386 * RoomScale, r\z - 416.0 * RoomScale, r)
    sc\angle = 180 : sc\turn = 30
    TurnEntity(sc\cameraObj, 30, 0, 0)
    EntityParent(sc\obj, r\obj)

    it = CreateItem("Document SCP-714", "paper", r\x - 728.0 * RoomScale, r\y + 288.0 * RoomScale, r\z - 328.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

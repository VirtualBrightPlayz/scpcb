Function FillRoom_lck_broke_2c(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\zone, r\x - 736.0 * RoomScale, 0, r\z - 104.0 * RoomScale, 0, r, True)
    d\timer = 70 * 5 : d\autoClose = False : d\open = False : d\locked = True

    EntityParent(d\buttons[0], 0)
    PositionEntity(d\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 640.0 * RoomScale)
    EntityParent(d\buttons[0], r\obj)

    FreeEntity(d\buttons[1]) : d\buttons[1] = 0

    d2 = CreateDoor(r\zone, r\x + 104.0 * RoomScale, 0, r\z + 736.0 * RoomScale, 270, r, True)
    d2\timer = 70 * 5 : d2\autoClose = False: d2\open = False : d2\locked = True
    EntityParent(d2\buttons[0], 0)
    PositionEntity(d2\buttons[0], r\x + 640.0 * RoomScale, 0.7, r\z + 288.0 * RoomScale)
    RotateEntity(d2\buttons[0], 0, 90, 0)
    EntityParent(d2\buttons[0], r\obj)

    FreeEntity(d2\buttons[1]) : d2\buttons[1] = 0

    d\linkedDoor = d2
    d2\linkedDoor = d

    Local scale# = RoomScale * 4.5 * 0.4

    r\objects[0] = CopyEntity(Monitor)
    ScaleEntity(r\objects[0],scale#,scale#,scale#)
    PositionEntity(r\objects[0],r\x+668*RoomScale,1.1,r\z-96.0*RoomScale,True)
    RotateEntity(r\objects[0],0,90,0)
    EntityParent(r\objects[0],r\obj)

    r\objects[1] = CopyEntity(Monitor)
    ScaleEntity(r\objects[1],scale#,scale#,scale#)
    PositionEntity(r\objects[1],r\x+96.0*RoomScale,1.1,r\z-668.0*RoomScale,True)
    EntityParent(r\objects[1],r\obj)
End Function

Function FillRoom_hll_lshape_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    r\objects[6] = CreatePivot()
    PositionEntity(r\objects[6], r\x + 640.0 * RoomScale, 8.0 * RoomScale, r\z - 896.0 * RoomScale)
    EntityParent(r\objects[6], r\obj)
End Function

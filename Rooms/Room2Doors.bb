Function FillRoom2Doors(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x, 0, r\z + 528.0 * RoomScale, 0, r, True)
    d\AutoClose = False ;: d\buttons[0] = False
    PositionEntity (d\buttons[0], r\x - 832.0 * RoomScale, 0.7, r\z + 160.0 * RoomScale, True)
    PositionEntity (d\buttons[1], r\x + 160.0 * RoomScale, 0.7, r\z + 536.0 * RoomScale, True)
    ;RotateEntity(d\buttons[1], 0, 90, 0, True)
    
    d2 = CreateDoor(r\zone, r\x, 0, r\z - 528.0 * RoomScale, 180, r, True)
    d2\AutoClose = False : FreeEntity (d2\buttons[0]) : d2\buttons[0] = 0
    PositionEntity (d2\buttons[1], r\x +160.0 * RoomScale, 0.7, r\z - 536.0 * RoomScale, True)
    ;RotateEntity(d2\buttons[1], 0, 90, 0, True)
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x - 832.0 * RoomScale, 0.5, r\z)
    EntityParent(r\Objects[0], r\obj)
    
    d2\LinkedDoor = d : d\LinkedDoor = d2
    
    d\open = False : d2\open = True
End Function

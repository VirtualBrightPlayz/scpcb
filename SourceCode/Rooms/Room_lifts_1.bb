Function FillRoom_lifts_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    r\objects[0] = CreateButton(r\x + 96.0*RoomScale, r\y + 160.0 * RoomScale, r\z + 64.0 * RoomScale, 0,0,0)
    EntityParent(r\objects[0],r\obj)
    r\objects[1] = CreateButton(r\x - 96.0*RoomScale, r\y + 160.0 * RoomScale, r\z + 64.0 * RoomScale, 0,0,0)
    EntityParent(r\objects[1],r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x+384.0*RoomScale, r\y+(448-64)*RoomScale, r\z-960.0*RoomScale, r, True)
    sc\angle = 45
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\cameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    sc\id = 3
End Function

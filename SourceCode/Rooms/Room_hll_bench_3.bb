Function FillRoom_hll_bench_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    sc.SecurityCams = CreateSecurityCam(r\x-320.0*RoomScale, r\y+384.0*RoomScale, r\z+512.25*RoomScale, r)
    sc\angle = 225
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    ;sc\followPlayer = True
    sc\iD = 2
End Function

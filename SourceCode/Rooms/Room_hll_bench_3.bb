Function FillRoom_hll_bench_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    sc = CreateSecurityCam(r\x-320.0*RoomScale, r\y+384.0*RoomScale, r\z+512.25*RoomScale, r)
    sc\angle = 225
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    ;sc\followPlayer = True
    sc\id = 2
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
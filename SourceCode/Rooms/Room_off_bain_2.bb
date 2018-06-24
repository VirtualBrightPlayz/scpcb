Function FillRoom_off_bain_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(0, r\x - 240.0 * RoomScale, 0.0, r\z, 90, r, False)
    d\open = False : d\autoClose = False
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
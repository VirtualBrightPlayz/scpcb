;TODO: Remove?
Function FillRoom_lck_096_2c(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1;, Bump

    For i = 0 To 5
        de.Decals = CreateDecal(Rand(2,3), r\x+Rnd(-392,520)*RoomScale, 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-392,520)*RoomScale,90,Rnd(360),0)
        de\size = Rnd(0.3,0.6)
        ScaleSprite(de\obj, de\size,de\size)
        CreateDecal(Rand(15,16), r\x+Rnd(-392,520)*RoomScale, 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-392,520)*RoomScale,90,Rnd(360),0)
        de\size = Rnd(0.1,0.6)
        ScaleSprite(de\obj, de\size,de\size)
        CreateDecal(Rand(15,16), r\x+Rnd(-0.5,0.5), 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-0.5,0.5),90,Rnd(360),0)
        de\size = Rnd(0.1,0.6)
        ScaleSprite(de\obj, de\size,de\size)
    Next

    sc.SecurityCams = CreateSecurityCam(r\x + 512.0 * RoomScale, r\y + 384 * RoomScale, r\z + 384.0 * RoomScale, r, True)
    sc\angle = 45 + 90
    sc\turn = 45
    TurnEntity(sc\cameraObj, 40, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x + 668 * RoomScale, 1.1, r\z - 96.0 * RoomScale)
    TurnEntity(sc\scrObj, 0, 90, 0)
    EntityParent(sc\scrObj, r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x - 384.0 * RoomScale, r\y + 384 * RoomScale, r\z - 512.0 * RoomScale, r, True)
    sc\angle = 45 + 90 + 180
    sc\turn = 45

    TurnEntity(sc\cameraObj, 40, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x + 96.0 * RoomScale, 1.1, r\z - 668.0 * RoomScale)
    EntityParent(sc\scrObj, r\obj)
End Function
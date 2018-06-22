Function FillRoom_lck_cam_2c(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\zone, r\x - 736.0 * RoomScale, 0, r\z - 104.0 * RoomScale, 0, r, True)
    d\timer = 70 * 5 : d\autoClose = False : d\open = False

    EntityParent(d\buttons[0], 0)
    PositionEntity(d\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 640.0 * RoomScale)
    EntityParent(d\buttons[0], r\obj)

    FreeEntity(d\buttons[1]) : d\buttons[1] = 0

    d2 = CreateDoor(r\zone, r\x + 104.0 * RoomScale, 0, r\z + 736.0 * RoomScale, 270, r, True)
    d2\timer = 70 * 5 : d2\autoClose = False: d2\open = False
    EntityParent(d2\buttons[0], 0)
    PositionEntity(d2\buttons[0], r\x + 640.0 * RoomScale, 0.7, r\z + 288.0 * RoomScale)
    RotateEntity(d2\buttons[0], 0, 90, 0)
    EntityParent(d2\buttons[0], r\obj)

    FreeEntity(d2\buttons[1]) : d2\buttons[1] = 0

    d\linkedDoor = d2
    d2\linkedDoor = d

    sc.SecurityCams = CreateSecurityCam(r\x - 688.0 * RoomScale, r\y + 384 * RoomScale, r\z + 688.0 * RoomScale, r, True)
    sc\angle = 45 + 180
    sc\turn = 45
    sc\scrTexture = 1
    EntityTexture(sc\scrObj, ScreenTexs[sc\scrTexture])

    TurnEntity(sc\cameraObj, 40, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x + 668 * RoomScale, 1.1, r\z - 96.0 * RoomScale)
    TurnEntity(sc\scrObj, 0, 90, 0)
    EntityParent(sc\scrObj, r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x - 112.0 * RoomScale, r\y + 384 * RoomScale, r\z + 112.0 * RoomScale, r, True)
    sc\angle = 45
    sc\turn = 45
    sc\scrTexture = 1
    EntityTexture(sc\scrObj, ScreenTexs[sc\scrTexture])

    TurnEntity(sc\cameraObj, 40, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x + 96.0 * RoomScale, 1.1, r\z - 668.0 * RoomScale)
    EntityParent(sc\scrObj, r\obj)

    Local em.Emitters = CreateEmitter(r\x - 175.0 * RoomScale, 370.0 * RoomScale, r\z + 656.0 * RoomScale, 0)
    TurnEntity(em\obj, 90, 0, 0, True)
    EntityParent(em\obj, r\obj)
    em\randAngle = 20
    em\speed = 0.05
    em\sizeChange = 0.007
    em\aChange = -0.006
    em\gravity = -0.24

    em.Emitters = CreateEmitter(r\x - 655.0 * RoomScale, 370.0 * RoomScale, r\z + 240.0 * RoomScale, 0)
    TurnEntity(em\obj, 90, 0, 0, True)
    EntityParent(em\obj, r\obj)
    em\randAngle = 20
    em\speed = 0.05
    em\sizeChange = 0.007
    em\aChange = -0.006
    em\gravity = -0.24
End Function

Function UpdateEventLockroom173(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\room\dist < 6.0  And e\room\dist > 0) Then
		If (Curr173\idle = 2) Then
			RemoveEvent(e)
		Else
			If ((Not EntityInView(Curr173\collider, mainPlayer\cam)) Or EntityDistance(Curr173\collider, mainPlayer\collider)>15.0) Then
				PositionEntity(Curr173\collider, e\room\x + Cos(225-90 + e\room\angle) * 2, 0.6, e\room\z + Sin(225-90 + e\room\angle) * 2)
				ResetEntity(Curr173\collider)
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


Function FillRoom_off_lower_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1;, Bump

    it = CreatePaper("doc895", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z + 368.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    If Rand(2) = 1 Then
        it = CreatePaper("doc860", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 464.0 * RoomScale)
    Else
        it = CreatePaper("doc093rm", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 464.0 * RoomScale)
    EndIf
    EntityParent(it\collider, r\obj)

    it = CreateItem("navigator", r\x - 336.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 480.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    r\objects[0] = LoadMesh("GFX/NPCs/ducks/duck.b3d")
    ScaleEntity(r\objects[0], 0.07, 0.07, 0.07)

    EntityParent(r\objects[0], r\obj)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x-808.0 * RoomScale, -72.0 * RoomScale, r\z - 40.0 * RoomScale, True)
    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x-488.0 * RoomScale, 160.0 * RoomScale, r\z + 700.0 * RoomScale, True)
    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x-488.0 * RoomScale, 160.0 * RoomScale, r\z - 668.0 * RoomScale, True)
    r\objects[4] = CreatePivot(r\obj)
    PositionEntity(r\objects[4], r\x-572.0 * RoomScale, 350.0 * RoomScale, r\z - 4.0 * RoomScale, True)

    Local temp% = Rand(1,4)
    PositionEntity(r\objects[0], EntityX(r\objects[temp],True),EntityY(r\objects[temp],True),EntityZ(r\objects[temp],True),True)
End Function


Function UpdateEvent_off_lower_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If mainPlayer\blinkTimer<-8 And mainPlayer\blinkTimer >-12 Then
			temp = Rand(1,4)
			PositionEntity e\room\objects[0], EntityX(e\room\objects[temp],True),EntityY(e\room\objects[temp],True),EntityZ(e\room\objects[temp],True),True
			RotateEntity e\room\objects[0], 0, Rnd(360), 0
		EndIf
	EndIf
	;[End Block]
End Function


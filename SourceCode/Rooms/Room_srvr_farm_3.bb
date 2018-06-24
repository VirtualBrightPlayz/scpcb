Function FillRoom_srvr_farm_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    it = CreateItem("battery", r\x - 132.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    If (Rand(2) = 1) Then
        it = CreateItem("battery", r\x - 76.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf
    If (Rand(2) = 1) Then
        it = CreateItem("battery", r\x - 196.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf

    it = CreateItem("navigator", r\x + 124.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
	EntityParent(it\collider, r\obj)

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z - 400.0 * RoomScale, True)
    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x - 552.0 * RoomScale, -512.0 * RoomScale, r\z - 528.0 * RoomScale, True)
    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z + 272.0 * RoomScale, True)

    r\objects[3] = LoadMesh("GFX/npcs/duck_low_res.b3d")
    ScaleEntity(r\objects[3], 0.07, 0.07, 0.07)
    Local tex% = LoadTexture("GFX/npcs/duck2.png")
    EntityTexture(r\objects[3], tex)
    PositionEntity(r\objects[3], r\x + 928.0 * RoomScale, -640*RoomScale, r\z + 704.0 * RoomScale)

    EntityParent(r\objects[3], r\obj)
End Function


Function UpdateEvent_srvr_farm_3(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (e\eventState3=0 And Curr173\idle = 0) Then
			If (mainPlayer\blinkTimer < -10) Then
				temp = Rand(0,2)
				PositionEntity(Curr173\collider, EntityX(e\room\objects[temp],True),EntityY(e\room\objects[temp],True),EntityZ(e\room\objects[temp],True))
				ResetEntity(Curr173\collider)
				e\eventState3=1
			EndIf
		EndIf

		If (e\room\objects[3]>0) Then
			If (mainPlayer\blinkTimer<-8 And mainPlayer\blinkTimer >-12) Then
				PointEntity(e\room\objects[3], mainPlayer\cam)
				RotateEntity(e\room\objects[3], 0, EntityYaw(e\room\objects[3],True),0, True)
			EndIf
			If (e\eventState2 = 0) Then
				e\eventState = CurveValue(0, e\eventState, 15.0)
				If (Rand(800)=1) Then e\eventState2 = 1
			Else
				e\eventState = e\eventState+(timing\tickDuration*0.5)
				If (e\eventState > 360) Then e\eventState = 0

				If (Rand(1200)=1) Then e\eventState2 = 0
			EndIf

			PositionEntity(e\room\objects[3], EntityX(e\room\objects[3],True), (-608.0*RoomScale)+0.05+Sin(e\eventState+270)*0.05, EntityZ(e\room\objects[3],True), True)
		EndIf
	EndIf
	;[End Block]
End Function


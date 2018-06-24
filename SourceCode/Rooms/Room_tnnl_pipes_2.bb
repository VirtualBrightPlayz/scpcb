Function FillRoom_tnnl_pipes_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0]= CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 368.0 * RoomScale, 0.0, r\z, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x - 368.0 * RoomScale, 0.0, r\z, True)

    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x + 224.0 * RoomScale - 0.005, 192.0 * RoomScale, r\z, True)

    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x - 224.0 * RoomScale + 0.005, 192.0 * RoomScale, r\z, True)
End Function


Function UpdateEventRoom2pipes106(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, d.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (Not Contained106) Then
		If (e\eventState = 0) Then
			If (mainPlayer\currRoom = e\room) Then e\eventState = 1
		Else
			e\eventState=(e\eventState+timing\tickDuration*0.7)
			;0-50 = walk to the middle
			;50-200 = look around
			;200-250 = leave
			If (e\eventState < 50) Then
				Curr106\idle = True
				PositionEntity(Curr106\collider, EntityX(e\room\objects[0], True), EntityY(mainPlayer\collider) - 0.15, EntityZ(e\room\objects[0], True))
				PointEntity(Curr106\collider, e\room\objects[1])
				MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\objects[0], e\room\objects[1])*0.5 * (e\eventState / 50.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			ElseIf (e\eventState < 200) Then
				Curr106\idle = True
				AnimateNPC(Curr106, 334, 494, 0.2)

				PositionEntity(Curr106\collider, (EntityX(e\room\objects[0], True)+EntityX(e\room\objects[1], True))/2, EntityY(mainPlayer\collider) - 0.15, (EntityZ(e\room\objects[0], True)+EntityZ(e\room\objects[1], True))/2)
				;MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\objects[0], e\room\objects[1])*0.5)
				RotateEntity(Curr106\collider,0, CurveValue(e\eventState,EntityYaw(Curr106\collider),30.0),0,True)
				If (EntityDistance(Curr106\collider, mainPlayer\collider)<4.0) Then
					pvt = CreatePivot()
					PositionEntity(pvt, EntityX(Curr106\collider),EntityY(Curr106\collider),EntityZ(Curr106\collider))
					PointEntity(pvt, mainPlayer\collider)
					If (WrapAngle(EntityYaw(pvt)-EntityYaw(Curr106\collider))<80) Then
						Curr106\state = -11
						Curr106\idle = False
						;PlaySound2(HorrorSFX(10)) ;TODO: fix
						e\eventState = 260
					EndIf
					FreeEntity(pvt)
				EndIf
			ElseIf (e\eventState < 250) Then
				Curr106\idle = True
				PositionEntity(Curr106\collider, EntityX(e\room\objects[0], True), EntityY(mainPlayer\collider) - 0.15, EntityZ(e\room\objects[0], True))
				PointEntity(Curr106\collider, e\room\objects[1])
				;200-250     (- 150)      50-100
				MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\objects[0], e\room\objects[1]) * ((e\eventState-150.0) / 100.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			EndIf
			ResetEntity(Curr106\collider)

			PositionEntity(Curr106\obj, EntityX(Curr106\collider), EntityY(Curr106\collider) - 0.15, EntityZ(Curr106\collider))
			RotateEntity(Curr106\obj, 0, EntityYaw(Curr106\collider), 0)

			;PositionEntity(Curr106\collider, EntityX(Curr106\collider), EntityY(mainPlayer\collider) - 0.20, EntityZ(Curr106\collider))

			If ((e\eventState / 250.0) > 0.3 And ((e\eventState - timing\tickDuration*0.7) / 250.0) <= 0.3) Then
				;e\soundChannels[0] = PlaySound2(HorrorSFX(6))
				mainPlayer\blurTimer = 800
				d = CreateDecal(0, EntityX(e\room\objects[2], True), EntityY(e\room\objects[2], True), EntityZ(e\room\objects[2], True), 0, e\room\angle - 90, Rnd(360)) ;90, Rnd(360), 0
				d\timer = 90000
				d\alpha = 0.01 : d\alphaChange = 0.005
				d\size = 0.1 : d\sizeChange = 0.003
			EndIf

			If ((e\eventState / 250.0) > 0.65 And ((e\eventState - timing\tickDuration*0.7) / 250.0) <= 0.65) Then
				d = CreateDecal(0, EntityX(e\room\objects[3], True), EntityY(e\room\objects[3], True), EntityZ(e\room\objects[3], True), 0, e\room\angle + 90, Rnd(360))
				d\timer = 90000
				d\alpha = 0.01 : d\alphaChange = 0.005
				d\size = 0.1 : d\sizeChange = 0.003
			EndIf

			If (e\eventState > 250) Then
				Curr106\idle = False : RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
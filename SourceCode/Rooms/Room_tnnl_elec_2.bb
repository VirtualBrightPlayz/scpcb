Function FillRoom_tnnl_elec_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x, 544.0 * RoomScale, r\z + 512.0 * RoomScale, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x, 544.0 * RoomScale, r\z - 512.0 * RoomScale, True)
End Function


Function UpdateEventTunnel2smoke(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If e\room\dist < 3.5 Then
			PlayRangedSound(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"), mainPlayer\cam, e\room\obj)
			For i = 0 To 1
				em.Emitters = CreateEmitter(EntityX(e\room\objects[i],True),EntityY(e\room\objects[i],True), EntityZ(e\room\objects[i],True),0)
				TurnEntity(em\obj, 90, 0, 0, True)
				EntityParent(em\obj, e\room\obj)
				em\size = 0.05
				em\randAngle = 10
				em\speed = 0.06
				em\sizeChange = 0.007
				;EntityParent(em\obj, e\room\obj)

				For z = 0 To 10
					p.Particles = CreateParticle(EntityX(em\obj, True), 448*RoomScale, EntityZ(em\obj, True), Rand(em\minImage, em\maxImage), em\size, em\gravity, em\lifeTime)
					p\speed = em\speed
					RotateEntity(p\pvt, Rnd(360), Rnd(360), 0, True)
					p\size = 0.05
					p\sizeChange = 0.008
				Next

			Next
			RemoveEvent(e)
		EndIf
	EndIf

	;[End Block]
End Function



Function UpdateEvent_tnnl_elec_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If Curr173\idle = 2 Then
			RemoveEvent(e)
		Else
			If e\eventState = 0 Then
				If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\obj), EntityZ(e\room\obj)) < 3.5 Then
					PlaySound_SM(sndManager\lightSwitch)

					;LightBlink = Rnd(0.0,1.0)*(e\eventState/200)
					e\eventState = 1
				EndIf
			ElseIf e\eventState < 200 Then

				mainPlayer\blinkTimer = -10
				If e\eventState > 30 Then
					;LightBlink = 1.0
					If e\eventState-timing\tickDuration =< 30 Then
						PlaySound2 LoadTempSound("SFX/ambient/general/ambient3.ogg")
					EndIf
				EndIf
				If e\eventState-timing\tickDuration =< 100 And e\eventState > 100 Then
					PlaySound2 LoadTempSound("SFX/ambient/general/ambient6.ogg")
					PositionEntity(Curr173\collider, EntityX(e\room\obj), 0.6, EntityZ(e\room\obj))
					ResetEntity(Curr173\collider)
					Curr173\idle = True
				EndIf
				;LightBlink = 1.0
				e\eventState = e\eventState + timing\tickDuration
			Else
				mainPlayer\blinkTimer = mainPlayer\blinkFreq

				Curr173\idle = False
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
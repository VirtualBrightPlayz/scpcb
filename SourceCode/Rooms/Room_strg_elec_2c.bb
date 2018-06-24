Function FillRoom_strg_elec_2c(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, 2)
    d\autoClose = False : d\open = False

    it = CreatePaper("docdan", r\x-400.0*RoomScale,1040.0*RoomScale,r\z+115.0*RoomScale)
    EntityParent(it\collider, r\obj)

	Local n%
    For n = 0 To 2
        r\levers[n] = CreateLever()

		ScaleEntity(r\levers[n]\obj, 0.04, 0.04, 0.04)
		ScaleEntity(r\levers[n]\baseObj, 0.04, 0.04, 0.04)
		PositionEntity(r\levers[n]\obj, r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 64.0 * n) * RoomScale, True)
		PositionEntity(r\levers[n]\baseObj, r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 64.0 * n) * RoomScale, True)

		EntityParent(r\levers[n]\obj, r\obj)
		EntityParent(r\levers[n]\baseObj, r\obj)

        RotateEntity(r\levers[n]\baseObj, 0, -90, 0)
        RotateEntity(r\levers[n]\obj, 10, -90 - 180, 0)

        EntityPickMode(r\levers[n]\obj, 1, False)
        EntityRadius(r\levers[n]\obj, 0.1)
    Next

    sc = CreateSecurityCam(r\x-265.0*RoomScale, r\y+1280.0*RoomScale, r\z+105.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    sc\id = 10
End Function


Function UpdateEvent_strg_elec_2c(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, prevpitch#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then

		EntityPick(mainPlayer\cam, 1.5)

		For i = 1 To 5 Step 2
			If (PickedEntity() = e\room\objects[i]) Then
				DrawHandIcon = True
				If (MouseHit1) Then mainPlayer\grabbedEntity = e\room\objects[i]

				If (e\eventState = 0) Then
					If (i = 3) Then
						e\eventState = Max(e\eventState,1)
						;PlaySound2(HorrorSFX(7))
						PlaySound_SM(sndManager\lever)
					EndIf
				EndIf
			EndIf

			prevpitch = EntityPitch(e\room\objects[i])

			If (MouseDown1 Or MouseHit1) Then
				If (mainPlayer\grabbedEntity <> 0) Then
					If (mainPlayer\grabbedEntity = e\room\objects[i]) Then
						DrawHandIcon = True
						TurnEntity(e\room\objects[i], mouse_y_speed_1 * 2.5, 0, 0)
						RotateEntity(mainPlayer\grabbedEntity, Max(Min(EntityPitch(e\room\objects[i]), 85), -85), EntityYaw(e\room\objects[i]), 0)

						DrawArrowIcon(0) = True
						DrawArrowIcon(2) = True

					EndIf
				EndIf
			Else
				If (EntityPitch(e\room\objects[i]) > 0) Then
					RotateEntity(e\room\objects[i], CurveValue(85, EntityPitch(e\room\objects[i]), 10), EntityYaw(e\room\objects[i]), 0)
				Else
					RotateEntity(e\room\objects[i], CurveValue(-85, EntityPitch(e\room\objects[i]), 10), EntityYaw(e\room\objects[i]), 0)
				EndIf
				mainPlayer\grabbedEntity = 0
			EndIf

			If (EntityPitch(e\room\objects[i]) > 83) Then
				If (prevpitch =< 83) Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, e\room\objects[i])
				If (i = 3) Then
					SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
					If (prevpitch =< 83) Then
						PlayRangedSound_SM(sndManager\lightSwitch, mainPlayer\cam, e\room\objects[i])
					EndIf
				Else
					RemoteDoorOn = True
				EndIf
			ElseIf (EntityPitch(e\room\objects[i]) < -83) Then

				If (prevpitch => -83) Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, e\room\objects[i])
				If (i = 3) Then
					If (prevpitch => -83) Then
						PlayRangedSound_SM(sndManager\lightSwitch, mainPlayer\cam, e\room\objects[i])
						For r = Each Rooms
							For z = 0 To 19
								If (r\lightSprites[Int(z)] <> 0) Then HideEntity(r\lightSprites[Int(z)])
							Next
						Next
					EndIf
					SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
				Else
					RemoteDoorOn = False
				EndIf
			EndIf
		Next

		If (e\eventState > 0 And e\eventState < 200) Then
			e\eventState = e\eventState + timing\tickDuration
			RotateEntity(e\room\levers[1]\obj, CurveValue(-85, EntityPitch(e\room\objects[3]), 5), EntityYaw(e\room\objects[3]), 0)
		EndIf

	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
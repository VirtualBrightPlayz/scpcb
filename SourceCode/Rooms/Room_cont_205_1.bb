;TODO: work on this next
Function FillRoom_cont_205_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\x + 128.0 * RoomScale, 0, r\z + 640.0 *RoomScale, 90, r, True, DOOR_TYPE_DEF, r\roomTemplate\name)
    d\autoClose = False : d\open = False
    ;PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    ;PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    r\doors[0] = CreateDoor(r\x - 1392.0 * RoomScale, -128.0 * RoomScale, r\z - 384*RoomScale, 0, r, True, DOOR_TYPE_DEF, r\roomTemplate\name)
    r\doors[0]\autoClose = False : r\doors[0]\open = False
    FreeEntity(r\doors[0]\buttons[0]) : r\doors[0]\buttons[0]=0
    FreeEntity(r\doors[0]\buttons[1]) : r\doors[0]\buttons[1]=0

    sc = CreateSecurityCam(r\x - 1152.0 * RoomScale, r\y + 900.0 * RoomScale, r\z + 176.0 * RoomScale, r, True)
    sc\angle = 90 : sc\turn = 0
    EntityParent(sc\obj, r\obj)

    sc\allowSaving = False
    sc\renderInterval = 0

    EntityParent(sc\scrObj, 0)
    PositionEntity(sc\scrObj, r\x - 1716.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 176.0 * RoomScale, True)
    TurnEntity(sc\scrObj, 0, 90, 0)
    ScaleSprite(sc\scrObj, 896.0*0.5*RoomScale, 896.0*0.5*RoomScale)

    EntityParent(sc\scrObj, r\obj)
    ;EntityBlend(sc\scrObj, 2)

    CameraZoom(sc\cam, 1.5)

    HideEntity(sc\scrOverlay)
    HideEntity(sc\monitorObj)

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x - 1536.0 * RoomScale, r\y + 730.0 * RoomScale, r\z + 192.0 * RoomScale, True)
    RotateEntity(r\objects[0], 0,-90,0,True)

    r\objects[1] = sc\scrObj
End Function


Function UpdateEvent_cont_205_1(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		e\overwriteMusic = True
		If (e\eventState=0 Or e\room\objects[0]=0) Then
			If (Not e\loaded) Then

				e\room\objects[3] = LoadAnimMesh("GFX/npcs/205_demon1.b3d")
				e\room\objects[4] = LoadAnimMesh("GFX/npcs/205_demon2.b3d")
				e\room\objects[5] = LoadAnimMesh("GFX/npcs/205_demon3.b3d")
				e\room\objects[6] = LoadAnimMesh("GFX/npcs/205_woman.b3d")

				For i = 3 To 6
				;EntityColor(e\room\objects[i], 30,30,30)
				;EntityShininess(e\room\objects[i],0.0)
				;vertex color + flatshaded
				;EntityFX(e\room\objects[i], 2)

					PositionEntity(e\room\objects[i], EntityX(e\room\objects[0],True), EntityY(e\room\objects[0],True), EntityZ(e\room\objects[0],True), True)
					RotateEntity(e\room\objects[i], -90, EntityYaw(e\room\objects[0],True), 0, True)
					ScaleEntity(e\room\objects[i], 0.05, 0.05, 0.05, True)
				Next

				HideEntity(e\room\objects[3])
				HideEntity(e\room\objects[4])
				HideEntity(e\room\objects[5])

				e\musicTrack = MUS_205
				e\loaded = True
				e\eventState = 1
			EndIf

		Else

			If (e\eventState<65) Then
				If (Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\objects[0],True), EntityZ(e\room\objects[0],True))<2.0) Then
					PlaySound2(LoadTempSound("SFX/SCP/205/Enter.ogg"))

					e\eventState = Max(e\eventState, 65)

					ShowEntity(e\room\objects[3])
					ShowEntity(e\room\objects[4])
					ShowEntity(e\room\objects[5])
					HideEntity(e\room\objects[6])

					SetAnimTime(e\room\objects[3], 492)
					SetAnimTime(e\room\objects[4], 434)
					SetAnimTime(e\room\objects[5], 434)

					e\room\doors[0]\open = False
				EndIf

				If (e\eventState>7) Then
					If (Rand(0,300)=1) Then
						e\room\doors[0]\open = Not e\room\doors[0]\open
					EndIf
				EndIf

				e\eventState2 = e\eventState2 + timing\tickDuration
			EndIf


			Select (e\eventState)
				Case 1
					;sitting
					Animate2(e\room\objects[6], AnimTime(e\room\objects[6]), 526, 530, 0.2)
					If (e\eventState2 > 20*70) Then e\eventState = e\eventState+1
				Case 3
					;laying down
					Animate2(e\room\objects[6], AnimTime(e\room\objects[6]), 377, 525, 0.2)
					If (e\eventState2 > 30*70) Then e\eventState = e\eventState+1
				Case 5
					;standing
					Animate2(e\room\objects[6], AnimTime(e\room\objects[6]), 228, 376, 0.2)
					If (e\eventState2 > 40*70) Then
						e\eventState = e\eventState+1
						PlayRangedSound(LoadTempSound("SFX/SCP/205/Horror.ogg"), mainPlayer\cam, e\room\objects[6], 10, 0.3)
					EndIf
				Case 7
					;first demon appears
					ShowEntity(e\room\objects[5])
					;le sexy demon pose
					Animate2(e\room\objects[5], AnimTime(e\room\objects[5]), 500, 648, 0.2)
					;TurnEntity(e\room\objects[6], 0.0, DeltaYaw(e\room\objects[6], e\room\objects[5])*0.01, 0.0, True)
					If (e\eventState2 > 60*70) Then
						e\eventState = e\eventState+1
						PlayRangedSound(LoadTempSound("SFX/SCP/205/Horror.ogg"), mainPlayer\cam, e\room\objects[6], 10, 0.5)
					EndIf
				Case 9
					;second demon appears
					ShowEntity(e\room\objects[4])
					;idle
					Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 2, 200, 0.2)
					Animate2(e\room\objects[5], AnimTime(e\room\objects[5]), 4, 125, 0.2)

					;TurnEntity(e\room\objects[6], 0.0, DeltaYaw(e\room\objects[6], e\room\objects[4])*0.01, 0.0, True)

					If (e\eventState2 > 80*70) Then
						e\eventState = e\eventState+1
						PlaySound2(LoadTempSound("SFX/SCP/205/Horror.ogg"))
					EndIf
				Case 11
					;third demon
					ShowEntity(e\room\objects[3])
					;idle
					Animate2(e\room\objects[3], AnimTime(e\room\objects[3]), 2, 226, 0.2)
					Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 2, 200, 0.2)
					Animate2(e\room\objects[5], AnimTime(e\room\objects[5]), 4, 125, 0.2)

					;TurnEntity(e\room\objects[6], 0.0, DeltaYaw(e\room\objects[5], e\room\objects[4])*0.01, 0.0, True)

					If (e\eventState2 > 85*70) Then e\eventState = e\eventState+1
				Case 13
					If (AnimTime(e\room\objects[6])<>227) Then SetAnimTime(e\room\objects[6], 227)

					Animate2(e\room\objects[3], AnimTime(e\room\objects[3]), 2, 491, 0.05)
					Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 197, 433, 0.05)
					Animate2(e\room\objects[5], AnimTime(e\room\objects[5]), 2, 433, 0.05)
				Case 66
					Animate2(e\room\objects[3], AnimTime(e\room\objects[3]), 492, 534, 0.1, False)
					Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 434, 466, 0.1, False)
					Animate2(e\room\objects[5], AnimTime(e\room\objects[5]), 434, 494, 0.1, False)

					If (AnimTime(e\room\objects[3])>515) Then
						;MoveEntity(e\room\objects[3], 0,-timing\tickDuration*0.001,0)
						;MoveEntity(e\room\objects[4], 0,-timing\tickDuration*0.001,0)
						;MoveEntity(e\room\objects[5], 0,-timing\tickDuration*0.001,0)
						If (AnimTime(e\room\objects[3])>533) Then
							e\eventState = 67
							e\eventState2 = 0
							e\eventState3 = 0
							HideEntity(e\room\objects[1])
						EndIf
					EndIf
				Case 67
					If (Rand(150)=1) Then
						DeathMSG = "The SCP-205 cycle seems to have resumed its normal course after the anomalies observed during "
						DeathMSG = DeathMSG + "[REDACTED]. The body of subject D-9341 was discovered inside the chamber. "
						DeathMSG = DeathMSG + "The subject exhibits signs of blunt force trauma typical for personnel who have "
						DeathMSG = DeathMSG + "entered the chamber when the lights are off."

						mainPlayer\injuries=mainPlayer\injuries+Rnd(0.4,0.8)
						PlaySound2(mainPlayer\damageSFX[Rand(2,3)])
						mainPlayer\camShake = 0.5

						e\eventState2 = Rnd(-0.1, 0.1)
						e\eventState3 = Rnd(-0.1, 0.1)

						If (mainPlayer\injuries>5.0) Then Kill(mainPlayer)
					EndIf

					TranslateEntity(mainPlayer\collider, e\eventState2,0,e\eventState3)
					e\eventState2 = CurveValue(e\eventState2, 0, 10.0)
					e\eventState3 = CurveValue(e\eventState3, 0, 10.0)
				Default
					If (Rand(3)=1) Then
						HideEntity(e\room\objects[1])
					Else
						ShowEntity(e\room\objects[1])
					EndIf

					e\eventState3 = e\eventState3 + timing\tickDuration
					If (e\eventState3>50) Then
						ShowEntity(e\room\objects[1])
						e\eventState = e\eventState+1
						e\eventState3=0
					EndIf
			End Select
		EndIf
	Else
		e\eventState = 0
		e\overwriteMusic = False
	EndIf
	;[End block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
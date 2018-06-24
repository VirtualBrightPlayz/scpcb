Function FillRoom_cont_914_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    ;d = CreateDoor(r\zone, r\x, 0, r\z - 368.0 * RoomScale, 0, r, False, True, 2)
    ;d\dir = 1 : d\autoClose = False : d\open = False
    ;PositionEntity(d\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    ;TurnEntity(d\buttons[0], 0, 90, 0)
    r\roomDoors[2] = CreateDoor(r\zone,r\x,0,r\z-368.0*RoomScale,0,r,False,True,2)
    r\roomDoors[2]\dir=1 : r\roomDoors[2]\autoClose=False : r\roomDoors[2]\open=False
    PositionEntity(r\roomDoors[2]\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    TurnEntity(r\roomDoors[2]\buttons[0], 0, 90, 0)

    r\objects[0] = LoadMesh("GFX/Map/914key.x")
    r\objects[1] = LoadMesh("GFX/Map/914knob.x")

    For i = 0 To 1
        ScaleEntity(r\objects[i], RoomScale, RoomScale, RoomScale)
        EntityPickMode(r\objects[i], 2)
    Next

    PositionEntity(r\objects[0], r\x, r\y + 190.0 * RoomScale, r\z + 374.0 * RoomScale)
    PositionEntity(r\objects[1], r\x, r\y + 230.0 * RoomScale, r\z + 374.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)
    EntityParent(r\objects[1], r\obj)

    d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity(d\obj2)
	d\obj2 = 0
    FreeEntity(d\buttons[0])
	d\buttons[0] = 0
    FreeEntity(d\buttons[1])
	d\buttons[1] = 0
    r\roomDoors[0] = d: d\autoClose = False

    d = CreateDoor(r\zone, r\x + 816.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity(d\obj2)
	d\obj2 = 0
    FreeEntity(d\buttons[0])
	d\buttons[0] = 0
    FreeEntity(d\buttons[1])
	d\buttons[1] = 0
    r\roomDoors[1] = d
	d\autoClose = False

    r\objects[2] = CreatePivot()
    r\objects[3] = CreatePivot()
    PositionEntity(r\objects[2], r\x - 712.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    PositionEntity(r\objects[3], r\x + 728.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    EntityParent(r\objects[2], r\obj)
    EntityParent(r\objects[3], r\obj)

    it = CreatePaper("914_organic", r\x +954.0 * RoomScale, r\y +228.0 * RoomScale, r\z + 127.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("firstaid", r\x + 960.0 * RoomScale, r\y + 112.0 * RoomScale, r\z - 40.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)

    it = CreatePaper("docL4", r\x - 928.0 * RoomScale, 160.0 * RoomScale, r\z - 160.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

;TODO: Overhaul keycard system.
Function UpdateEvent_cont_914_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, setting$

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (Not e\loaded) Then
			e\sounds[0] = LoadSound("SFX/SCP/914/Refining.ogg")
			e\sounds[1] = LoadSound("SFX/SCP/914/PlayerUse.ogg")
			e\sounds[2] = LoadSound("SFX/SCP/914/PlayerDeath.ogg")

			e\loaded = True
		EndIf

		EntityPick(mainPlayer\cam, 1.0)
		If (PickedEntity() = e\room\objects[0]) Then
			DrawHandIcon = True
			If (MouseHit1) Then mainPlayer\grabbedEntity = e\room\objects[0]
		ElseIf (PickedEntity() = e\room\objects[1]) Then
			DrawHandIcon = True
			If (MouseHit1) Then mainPlayer\grabbedEntity = e\room\objects[1]
		EndIf

		If (MouseDown1 Or MouseHit1) Then
			If (mainPlayer\grabbedEntity <> 0) Then ;avain
				If (mainPlayer\grabbedEntity = e\room\objects[0]) Then
					If (e\eventState = 0) Then
						DrawHandIcon = True
						TurnEntity(mainPlayer\grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)

						angle = WrapAngle(EntityRoll(e\room\objects[0]))
						If (angle > 181) Then DrawArrowIcon(3) = True
						DrawArrowIcon(1) = True

						If (angle < 90) Then
							RotateEntity(mainPlayer\grabbedEntity, 0, 0, 361.0)
						ElseIf (angle < 180) Then
							RotateEntity(mainPlayer\grabbedEntity, 0, 0, 180)
						EndIf

						If (angle < 181 And angle > 90) Then
							For it = Each Items
								If (it\collider <> 0 And it\picked = False) Then
									If (Abs(EntityX(it\collider) - (e\room\x - 712.0 * RoomScale)) < 200.0) Then
										If (Abs(EntityY(it\collider) - (e\room\y + 648.0 * RoomScale)) < 104.0) Then
											e\eventState = 1
											e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\objects[1])
											Exit
										EndIf
									EndIf
								EndIf
							Next
						EndIf
					EndIf
				ElseIf (mainPlayer\grabbedEntity = e\room\objects[1]) Then
					If (e\eventState = 0) Then
						DrawHandIcon = True
						TurnEntity(mainPlayer\grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)

						angle = WrapAngle(EntityRoll(e\room\objects[1]))
						DrawArrowIcon(3) = True
						DrawArrowIcon(1) = True

						If (angle > 90) Then
							If (angle < 180) Then
								RotateEntity(mainPlayer\grabbedEntity, 0, 0, 90.0)
							ElseIf (angle < 270) Then
								RotateEntity(mainPlayer\grabbedEntity, 0, 0, 270)
							EndIf
						EndIf

					EndIf
				EndIf
			EndIf
		Else
			mainPlayer\grabbedEntity = 0
		EndIf

		If (mainPlayer\grabbedEntity <> e\room\objects[1]) Then
			angle = WrapAngle(EntityRoll(e\room\objects[1]))
			If (angle < 22.5) Then
				angle = 0
				setting = "1:1"
			ElseIf (angle < 67.5) Then
				angle = 40
				setting = "coarse"
			ElseIf (angle < 180) Then
				angle = 90
				setting = "rough"
			ElseIf (angle > 337.5) Then
				angle = 359 - 360
				setting = "1:1"
			ElseIf (angle > 292.5) Then
				angle = 320 - 360
				setting = "fine"
			Else
				angle = 270 - 360
				setting = "very fine"
			EndIf
			RotateEntity(e\room\objects[1], 0, 0, CurveValue(angle, EntityRoll(e\room\objects[1]), 20))
		EndIf

		For i = 0 To 1
			If (mainPlayer\grabbedEntity = e\room\objects[i]) Then
				If (Not EntityInView(e\room\objects[i], mainPlayer\cam)) Then
					mainPlayer\grabbedEntity = 0
				ElseIf (EntityDistance(e\room\objects[i], mainPlayer\cam) > 1.0) Then
					mainPlayer\grabbedEntity = 0
				EndIf
			EndIf
		Next

		If (e\eventState > 0) Then
			e\eventState = e\eventState + timing\tickDuration

			e\room\roomDoors[1]\open = False
			If (e\eventState > 70 * 2) Then
				e\room\roomDoors[0]\open = False
			EndIf

			If (Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\objects[2], True), EntityZ(e\room\objects[2], True)) < (170.0 * RoomScale)) Then

				If (setting = "rough" Or setting = "coarse") Then
					If (e\eventState > 70 * 2.6 And e\eventState - timing\tickDuration < 70 * 2.6) Then PlaySound2(e\sounds[2])
				EndIf

				If (e\eventState > 70 * 3) Then
					Select setting
						Case "rough"
							Kill(mainPlayer)
							mainPlayer\blinkTimer = -10
							If (e\soundChannels[0] <> 0) Then StopChannel(e\soundChannels[0])
							DeathMSG = Chr(34)+"A heavily mutilated corpse found inside the output booth of SCP-914. DNA testing identified the corpse as Class D Subject D-9341. "
							DeathMSG = DeathMSG + "The subject had obviously been "+Chr(34)+"refined"+Chr(34)+" by SCP-914 on the "+Chr(34)+"Rough"+Chr(34)+" setting, but we are still confused as to how he "
							DeathMSG = DeathMSG + "ended up inside the intake booth and who or what wound the key."+Chr(34)
						Case "coarse"
							mainPlayer\blinkTimer = -10
							If (e\eventState - timing\tickDuration < 70 * 3) Then PlaySound2(e\sounds[1])
						Case "1:1"
							mainPlayer\blinkTimer = -10
							If (e\eventState - timing\tickDuration < 70 * 3) Then PlaySound2(e\sounds[1])
						Case "fine", "very fine"
							mainPlayer\blinkTimer = -10
							If (e\eventState - timing\tickDuration < 70 * 3) Then PlaySound2(e\sounds[1])
					End Select
				EndIf
			EndIf

			If (e\eventState > (6 * 70)) Then
				RotateEntity(e\room\objects[0], EntityPitch(e\room\objects[0]), EntityYaw(e\room\objects[0]), CurveAngle(0, EntityRoll(e\room\objects[0]),10.0))
			Else
				RotateEntity(e\room\objects[0], EntityPitch(e\room\objects[0]), EntityYaw(e\room\objects[0]), 180)
			EndIf

			If (e\eventState > (12 * 70)) Then
				For it = Each Items
					If (it\collider <> 0 And it\picked = False) Then
						If (Distance(EntityX(it\collider), EntityZ(it\collider), EntityX(e\room\objects[2], True), EntityZ(e\room\objects[2], True)) < (180.0 * RoomScale)) Then
							Use914(it, setting, EntityX(e\room\objects[3], True), EntityY(e\room\objects[3], True), EntityZ(e\room\objects[3], True))

						EndIf
					EndIf
				Next

				If (Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\objects[2], True), EntityZ(e\room\objects[2], True)) < (160.0 * RoomScale)) Then
					Select setting
						Case "coarse"
							mainPlayer\injuries = 4.0
							Msg = "You notice countless small incisions all around your body. They are bleeding heavily."
							MsgTimer = 70*8
						Case "1:1"
							userOptions\invertMouseY = (Not userOptions\invertMouseY)
						Case "fine", "very fine"
							mainPlayer\superMan = 1.0
					End Select
					mainPlayer\blurTimer = 1000
					PositionEntity(mainPlayer\collider, EntityX(e\room\objects[3], True), EntityY(e\room\objects[3], True) + 1.0, EntityZ(e\room\objects[3], True))
					ResetEntity(mainPlayer\collider)
					mainPlayer\dropSpeed = 0
				EndIf

				e\room\roomDoors[0]\open = True
				e\room\roomDoors[1]\open = True
				RotateEntity(e\room\objects[0], 0, 0, 0)
				e\eventState = 0
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function



;~IDEal Editor Parameters:
;~C#Blitz3D
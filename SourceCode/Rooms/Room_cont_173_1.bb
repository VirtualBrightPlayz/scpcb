Function FillRoom_cont_173_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1;, Bump

    ;the containment doors
    r\roomDoors[1] = CreateDoor(r\zone, r\x + 4000.0 * RoomScale, 384.0*RoomScale, r\z + 1696.0 * RoomScale, 90, r, True, True)
    r\roomDoors[1]\locked = False : r\roomDoors[1]\autoClose = False
    r\roomDoors[1]\dir = 1 : r\roomDoors[1]\open = True
    FreeEntity(r\roomDoors[1]\buttons[0]) : r\roomDoors[1]\buttons[0] = 0
    FreeEntity(r\roomDoors[1]\buttons[1]) : r\roomDoors[1]\buttons[1] = 0
    r\roomDoors[1]\mtfClose = False

    r\roomDoors[2] = CreateDoor(r\zone, r\x + 2704.0 * RoomScale, 384.0*RoomScale, r\z + 624.0 * RoomScale, 90, r, False)
    r\roomDoors[2]\autoClose = False : r\roomDoors[2]\open = False
    FreeEntity(r\roomDoors[2]\buttons[0]) : r\roomDoors[2]\buttons[0] = 0
    FreeEntity(r\roomDoors[2]\buttons[1]) : r\roomDoors[2]\buttons[1] = 0
    r\roomDoors[2]\mtfClose = False

    d.Doors = CreateDoor(r\zone, r\x + 1392.0 * RoomScale, 384.0*RoomScale, r\z + 64.0 * RoomScale, 90, r, True)
    d\autoClose = False
    FreeEntity(d\buttons[0]) : d\buttons[0]=0
    FreeEntity(d\buttons[1]) : d\buttons[1]=0
    d\mtfClose = False

    d.Doors = CreateDoor(r\zone, r\x - 640.0 * RoomScale, 384.0*RoomScale, r\z + 64.0 * RoomScale, 90, r, False)
    d\locked = True : d\autoClose = False

    d.Doors = CreateDoor(r\zone, r\x + 1280.0 * RoomScale, 384.0*RoomScale, r\z + 312.0 * RoomScale, 180, r, True)
    d\locked = True : d\autoClose = False
    PositionEntity(d\buttons[0], r\x + 1120.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 328.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 1120.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 296.0 * RoomScale, True)
    FreeEntity(d\obj2) : d\obj2=0
    d\mtfClose = False

    d.Doors = CreateDoor(r\zone, r\x, 0, r\z + 1184.0 * RoomScale, 0, r, False)
    d\locked = True

    r\objects[0] = LoadMesh("GFX/map/IntroDesk.b3d")
    ScaleEntity r\objects[0], RoomScale, RoomScale ,RoomScale
    PositionEntity r\objects[0], r\x + 272.0 * RoomScale, 0, r\z + 400.0 * RoomScale
    EntityParent r\objects[0], r\obj

    de.Decals = CreateDecal(0, r\x + 272.0 * RoomScale, 0.005, r\z + 262.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)

    r\objects[1] = LoadMesh("GFX/map/IntroDrawer.b3d")
    ScaleEntity r\objects[1], RoomScale, RoomScale ,RoomScale
    PositionEntity r\objects[1], r\x + 448.0 * RoomScale, 0, r\z + 192.0 * RoomScale
    EntityParent r\objects[1], r\obj

    de.Decals = CreateDecal(0, r\x + 456.0 * RoomScale, 0.005, r\z + 135.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)

    sc.SecurityCams = CreateSecurityCam(r\x - 336.0 * RoomScale, r\y + 352 * RoomScale, r\z + 48.0 * RoomScale, r, True)
    sc\angle = 270
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\cameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x + 1456 * RoomScale, 608 * RoomScale, r\z +352.0 * RoomScale)
    TurnEntity(sc\scrObj, 0, 90, 0)
    EntityParent(sc\scrObj, r\obj)

    r\objects[2] = CreatePivot()
    PositionEntity (r\objects[2], EntityX(r\obj) + 40.0 * RoomScale, 460.0 * RoomScale, EntityZ(r\obj) + 1072.0 * RoomScale)
    r\objects[3] = CreatePivot()
    PositionEntity (r\objects[3], EntityX(r\obj) - 80.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\objects[4] = CreatePivot()
    PositionEntity (r\objects[4], EntityX(r\obj) - 128.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)

    r\objects[5] = CreatePivot()
    PositionEntity (r\objects[5], EntityX(r\obj) + 660.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\objects[6] = CreatePivot()
    PositionEntity (r\objects[6], EntityX(r\obj) + 700 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)

    r\objects[7] = CreatePivot()
    PositionEntity (r\objects[7], EntityX(r\obj) + 1472.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 912.0 * RoomScale)

    For i = 2 To 7
        EntityParent(r\objects[i], r\obj)
    Next
End Function


Function UpdateEventAlarm(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]

	If e\room\roomDoors[5]=Null Then
		For i=0 To 3
			If e\room\adjDoor[i]<>Null Then
				e\room\roomDoors[5] = e\room\adjDoor[i]
				e\room\roomDoors[5]\open = True
				Exit
			EndIf
		Next
	EndIf
	If e\eventState = 0 Then
		If mainPlayer\currRoom = e\room Then

			e\room\roomDoors[2]\open=True

			ShowEntity mainPlayer\overlays[OVERLAY_FOG]
			AmbientLight Brightness, Brightness, Brightness
			;TODO: fix
			;CameraFogRange(mainPlayer\cam, mainPlayer\camFogNear, mainPlayer\camFogFar)
			CameraFogMode(mainPlayer\cam, 1)
			If SelectedDifficulty\saveType = SAVEANYWHERE Then
				Msg = "Press "+KeyName[keyBinds\save]+" to save."
				MsgTimer = 70*4
			ElseIf SelectedDifficulty\saveType = SAVEONSCREENS Then
				Msg = "Saving is only permitted on clickable monitors scattered throughout the facility."
				MsgTimer = 70 * 8
			EndIf

			Curr173\idle=False

			While e\room\roomDoors[1]\openstate < 180
				e\room\roomDoors[1]\openstate = Min(180, e\room\roomDoors[1]\openstate + 0.8)
				MoveEntity(e\room\roomDoors[1]\obj, Sin(e\room\roomDoors[1]\openstate) / 180.0, 0, 0)
				MoveEntity(e\room\roomDoors[1]\obj2, -Sin(e\room\roomDoors[1]\openstate) / 180.0, 0, 0)
			Wend

			If e\room\npc[0] <> Null Then SetNPCFrame(e\room\npc[0], 74) : e\room\npc[0]\state = 8

			If e\room\npc[1] = Null Then
				e\room\npc[1] = CreateNPC(NPCtypeD, 0,0,0)
				tex = LoadTexture("GFX/NPCs/classd/scientist2.jpg")
				e\room\npc[1]\texture = "GFX/NPCs/classd/scientist2.jpg"
				EntityTexture e\room\npc[1]\obj, tex
				FreeTexture tex
			EndIf
			PositionEntity e\room\npc[1]\collider, e\room\x, 0.5, e\room\z-1.0, True
			ResetEntity e\room\npc[1]\collider
			SetNPCFrame(e\room\npc[1], 210)

			If e\room\npc[2] = Null Then
				e\room\npc[2] = CreateNPC(NPCtypeGuard, 0,0,0)
			EndIf
			PositionEntity e\room\npc[2]\collider, e\room\x-240*RoomScale, 0.5, e\room\z+528*RoomScale, True
			ResetEntity e\room\npc[2]\collider
			e\room\npc[2]\state = 7
			PointEntity e\room\npc[2]\collider,e\room\npc[1]\collider

			If e\room\npc[0]=Null Then
				e\room\npc[3] = CreateNPC(NPCtypeGuard, EntityX(e\room\objects[2], True), EntityY(e\room\objects[2], True), EntityZ(e\room\objects[2], True))
				;e\room\npc[3]\angle = 180
				RotateEntity e\room\npc[3]\collider,0,90,0
				SetNPCFrame(e\room\npc[3], 151) : e\room\npc[3]\state = 8
				MoveEntity e\room\npc[3]\collider,1,0,0

				e\room\npc[4] = CreateNPC(NPCtypeD, EntityX(e\room\objects[3], True), 0.5, EntityZ(e\room\objects[3], True))
				;PointEntity(e\room\npc[4]\collider, e\room\objects[7])
				SetNPCFrame(e\room\npc[4], 19) : e\room\npc[4]\state = 3
				RotateEntity e\room\npc[4]\collider,0,270,0
				MoveEntity e\room\npc[4]\collider,0,0,2.65

				e\room\npc[5] = CreateNPC(NPCtypeD, EntityX(e\room\objects[4], True), 0.5, EntityZ(e\room\objects[4], True))
				;PointEntity(e\room\npc[5]\collider, e\room\objects[7])
				SetNPCFrame(e\room\npc[5], 19) : e\room\npc[5]\state = 3
				RotateEntity e\room\npc[5]\collider,0,270,0
				tex = LoadTexture("GFX/NPCs/classd/classd2.jpg")
				e\room\npc[5]\texture = "GFX/NPCs/classd/classd2.jpg"
				EntityTexture e\room\npc[5]\obj, tex
				FreeTexture tex
				MoveEntity e\room\npc[5]\collider,0.25,0,3.0
				RotateEntity e\room\npc[5]\collider,0,0,0

				x# = EntityX(e\room\obj, True)+3712*RoomScale
				y# = 384.0*RoomScale
				z# = EntityZ(e\room\obj, True)+1312*RoomScale

				For i = 3 To 5
					PositionEntity(e\room\npc[i]\collider, x + (EntityX(e\room\npc[i]\collider) - EntityX(e\room\obj)), y+EntityY(e\room\npc[i]\collider)+0.4, z + (EntityZ(e\room\npc[i]\collider) - EntityZ(e\room\obj)))
					ResetEntity(e\room\npc[i]\collider)
				Next
			EndIf

			e\eventState = 1
		EndIf
	Else

		If e\room\npc[0] <> Null Then AnimateNPC(e\room\npc[0], 113, 151, 0.4, False)

		CurrTrigger = CheckTriggers()

		If (CurrTrigger = "173scene_timer") Then
			e\eventState = e\eventState+timing\tickDuration
		ElseIf (CurrTrigger = "173scene_activated") Then
			e\eventState = Max(e\eventState, 500)
		EndIf


		If e\eventState >= 500 Then
			e\eventState = e\eventState+timing\tickDuration

			If e\eventState2 = 0 Then
				CanSave = False
				If e\eventState > 900 And e\room\roomDoors[5]\open Then
					If e\eventState - timing\tickDuration <= 900 Then
						e\room\npc[1]\soundChannels[0] = LoadSound("SFX/Room/Intro/WhatThe.ogg")
						e\room\npc[1]\soundChannels[0] = PlayRangedSound(e\room\npc[1]\soundChannels[0], mainPlayer\cam, e\room\npc[1]\collider)
					EndIf
					e\room\npc[1]\state = 3
					e\room\npc[1]\currSpeed = CurveValue(-0.008, e\room\npc[1]\currSpeed, 5.0)
					AnimateNPC(e\room\npc[1], 260, 236, e\room\npc[1]\currSpeed * 18)
					RotateEntity e\room\npc[1]\collider, 0, 0, 0


					If e\eventState > 900+2.5*70 Then
						If e\room\npc[2]\state <> 1 Then
							e\room\npc[2]\currSpeed = CurveValue(-0.012, e\room\npc[2]\currSpeed, 5.0)
							AnimateNPC(e\room\npc[2], 895, 843, e\room\npc[2]\currSpeed*50)
							MoveEntity e\room\npc[2]\collider, 0,0,e\room\npc[2]\currSpeed*timing\tickDuration
							e\room\npc[2]\state = 8

							If EntityZ(e\room\npc[2]\collider) < e\room\z-512*RoomScale Then
								PointEntity(e\room\npc[2]\obj, e\room\npc[1]\collider)
								RotateEntity e\room\npc[2]\collider, 0, CurveAngle(EntityYaw(e\room\npc[2]\obj)-180,EntityYaw(e\room\npc[2]\collider),15.0), 0
							Else
								RotateEntity e\room\npc[2]\collider, 0, 0, 0
							EndIf
						EndIf
					EndIf

					If e\eventState < 900+4*70 Then
						PositionEntity Curr173\collider, e\room\x+32*RoomScale, 0.31, e\room\z+1072*RoomScale, True
						RotateEntity Curr173\collider,0,190,0

						If e\eventState > 900+70 And e\eventState < 900+2.5*70 Then
							AnimateNPC(e\room\npc[2], 1539, 1553, 0.2, False)
							PointEntity(e\room\npc[2]\obj, Curr173\collider)
							RotateEntity e\room\npc[2]\collider, 0, CurveAngle(EntityYaw(e\room\npc[2]\obj),EntityYaw(e\room\npc[2]\collider),15.0), 0
						EndIf

					Else
						If e\eventState-timing\tickDuration < 900+4*70 Then
							PlaySound2(IntroSFX(11)); : LightBlink = 3.0

							;Stonedrag.
							PlayRangedSound (Curr173\sounds[0], mainPlayer\cam, Curr173\collider)

							PointEntity Curr173\collider, e\room\npc[2]\collider
							If EntityY(mainPlayer\collider)<320*RoomScale Then mainPlayer\blinkTimer = -10
						EndIf

						PositionEntity Curr173\collider, e\room\x-96*RoomScale, 0.31, e\room\z+592*RoomScale, True

						If e\room\npc[2]\state <> 1 And (Not mainPlayer\dead) Then
							If EntityZ(e\room\npc[2]\collider) < e\room\z-1150*RoomScale Then
								e\room\roomDoors[5]\open = False
								;LightBlink = 3.0
								PlaySound2(IntroSFX(11))
								mainPlayer\blinkTimer = -10

								;Stonedrag.
								PlayRangedSound (Curr173\sounds[0], mainPlayer\cam, Curr173\collider)

								PositionEntity Curr173\collider, 0,0,0
								ResetEntity Curr173\collider
								Msg = "Hold "+KeyName[keyBinds\sprint]+" to run."
								MsgTimer = 70*8
							EndIf
						EndIf
					EndIf

					;If Ulgrin can see the player then start shooting at them.
					If (CurrTrigger = "173scene_end") And EntityVisible(e\room\npc[2]\collider,mainPlayer\collider) And (Not mainPlayer\godMode) Then
						e\room\npc[2]\state = 1
						e\room\npc[2]\state3 = 1
					ElseIf e\room\npc[2]\state = 1 And (Not EntityVisible(e\room\npc[2]\collider, mainPlayer\collider)) Then
						e\room\npc[2]\state = 0
						e\room\npc[2]\state3 = 0
					EndIf

					If e\room\npc[2]\state = 1 Then e\room\roomDoors[5]\open = True
				Else
					CanSave = True
					If e\room\npc[2]\state <> 1 Then
						If EntityX(mainPlayer\collider)<(e\room\x+1384*RoomScale) Then e\eventState = Max(e\eventState,900)

						If e\room\roomDoors[5]\openstate = 0 Then
							HideEntity e\room\npc[1]\obj
							HideEntity e\room\npc[1]\collider

							HideEntity e\room\npc[2]\obj
							HideEntity e\room\npc[2]\collider
							e\eventState2=1
						EndIf
					EndIf
				EndIf
			EndIf


		;	If e\eventState > 900+3*70 And e\eventState < 900+4*70 Then
		;		mainPlayer\camShake = 0.2
		;	ElseIf e\eventState > 900+32.3*70 And e\eventState < 900+34*70
		;		mainPlayer\camShake = 0.4
		;	ElseIf e\eventState > 900+51*70 And e\eventState < 900+53.5*70
		;		mainPlayer\camShake = 1.0
		;	ElseIf e\eventState > 900+57.5*70 And e\eventState < 900+58.5*70
		;		mainPlayer\camShake = 0.4
		;	EndIf

			PositionEntity e\room\objects[0], EntityX(e\room\objects[0],True), -Max(e\eventState-1300,0)/4500, EntityZ(e\room\objects[0],True), True
			RotateEntity e\room\objects[0], -Max(e\eventState-1320,0)/130, 0, -Max(e\eventState-1300,0)/40, True

			PositionEntity e\room\objects[1], EntityX(e\room\objects[1],True), -Max(e\eventState-1800,0)/5000, EntityZ(e\room\objects[1],True), True
			RotateEntity e\room\objects[1], -Max(e\eventState-2040,0)/135, 0, -Max(e\eventState-2040,0)/43, True

			If EntityDistance(e\room\objects[0],mainPlayer\collider)<2.5 Then
				;If Rand(300)=2 Then PlayRangedSound(DecaySFX(Rand(1,3)),mainPlayer\cam,e\room\objects[0], 3.0)
			EndIf
		EndIf

		If (e\eventState < 2000) Then
			If e\soundChannels[0] = 0 Then
				e\soundChannels[0] = PlaySound(AlarmSFX(0))
			Else
				If Not IsChannelPlaying(e\soundChannels[0]) Then e\soundChannels[0] = PlaySound(AlarmSFX(0))
			EndIf
		EndIf

		If (e\eventState3<10) Then
			If (Not IsChannelPlaying(e\soundChannels[1])) Then
				e\eventState3 = e\eventState3+1

				If (e\sounds[1] <> 0) Then
					FreeSound(e\sounds[1])
					e\sounds[1] = 0
				EndIf

				e\sounds[1] = LoadSound("SFX/Alarm/Alarm2_"+Int(e\eventState3)+".ogg")
				e\soundChannels[1] = PlaySound(e\sounds[1])
				Else
					If Int(e\eventState3) = 8 Then mainPlayer\camShake = 1.0
			EndIf
		EndIf

		If ((e\eventState Mod 600 > 300) And ((e\eventState+timing\tickDuration) Mod 600 < 300)) Then
			i = Floor((e\eventState-5000)/600)+1

			If i = 0 Then PlaySound2(LoadTempSound("SFX/Room/Intro/PA/scripted/scripted6.ogg"))

			If (i>0 And i<24) Then
				If Not CommotionState(i) Then ;Prevents the same commotion file from playing more then once.
					PlaySound2(LoadTempSound("SFX/Room/Intro/Commotion/Commotion"+i+".ogg"))
					CommotionState(i) = True
				EndIf
			EndIf

			If (i>24) Then
				If e\room\npc[0] <> Null Then RemoveNPC(e\room\npc[0])
				If e\room\npc[1] <> Null Then RemoveNPC(e\room\npc[1])
				If e\room\npc[2] <> Null Then RemoveNPC(e\room\npc[2])

				FreeEntity e\room\objects[0]
				FreeEntity e\room\objects[1]

				DebugLog "delete alarm"

				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
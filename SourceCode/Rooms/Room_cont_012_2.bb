Function FillRoom_cont_012_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\x + 264.0 * RoomScale, 0.0, r\z + 672.0 * RoomScale, 270, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 880.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 304.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 840.0 * RoomScale, True)
    TurnEntity(d\buttons[1],0,0,0,True)

    r\doors[0] = CreateDoor(r\x -512.0 * RoomScale, -768.0*RoomScale, r\z -336.0 * RoomScale, 0, r, False)
    r\doors[0]\autoClose = False : r\doors[0]\open = False : r\doors[0]\locked = True
    PositionEntity(r\doors[0]\buttons[0], r\x + 176.0 * RoomScale, -512.0*RoomScale, r\z - 364.0 * RoomScale, True)
    FreeEntity(r\doors[0]\buttons[1])
	r\doors[0]\buttons[1]=0

    r\levers[0] = CreateLever()

	ScaleEntity(r\levers[0]\baseObj, 0.04, 0.04, 0.04)
	ScaleEntity(r\levers[0]\obj, 0.04, 0.04, 0.04)
	PositionEntity(r\levers[0]\baseObj, r\x + 240.0 * RoomScale, r\y - 512.0 * RoomScale, r\z - 364 * RoomScale, True)
	PositionEntity(r\levers[0]\obj, r\x + 240.0 * RoomScale, r\y - 512.0 * RoomScale, r\z - 364 * RoomScale, True)

	EntityParent(r\levers[0]\baseObj, r\obj)
	EntityParent(r\levers[0]\obj, r\obj)

    ;RotateEntity(r\levers[0]\baseObj, 0, 0, 0)
    RotateEntity(r\levers[0]\obj, 10, -180, 0)

    EntityPickMode(r\levers[0]\obj, 1, False)
    EntityRadius(r\levers[0]\obj, 0.1)

    r\objects[2] = LoadMesh("GFX/Map/room012_2.b3d")
    ScaleEntity(r\objects[2], RoomScale, RoomScale, RoomScale)
    PositionEntity(r\objects[2], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\objects[2], r\obj)

	Local lightSpriteRed% = LoadTexture("GFX/Sprites/light_flare_red.jpg", 1)
    r\objects[3] = CreateSprite()
    PositionEntity(r\objects[3], r\x - 43.5 * RoomScale, - 574 * RoomScale, r\z - 362.0 * RoomScale)
    ScaleSprite(r\objects[3], 0.015, 0.015)
    EntityTexture(r\objects[3], lightSpriteRed)
    EntityBlend(r\objects[3], 3)
    EntityParent(r\objects[3], r\obj)
    HideEntity(r\objects[3])
	FreeTexture(lightSpriteRed)

    r\objects[4] = LoadMesh("GFX/Map/room012_3.b3d")
    Local tex% = LoadTexture("GFX/Map/Textures/scp-012_0.jpg")
    EntityTexture(r\objects[4],tex, 0,1)
    ScaleEntity(r\objects[4], RoomScale, RoomScale, RoomScale)
    PositionEntity(r\objects[4], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\objects[4], r\objects[2])

    it = CreatePaper("doc012", r\x - 56.0 * RoomScale, r\y - 576.0 * RoomScale, r\z - 408.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    de = CreateDecal(DECAL_BLOOD_SPLATTER,  r\x - 784*RoomScale, -768*RoomScale+0.01, r\z+640*RoomScale,90,Rnd(360),0)
    de\size = 0.5
    ScaleSprite(de\obj, de\size,de\size)
    EntityParent(de\obj, r\obj)
End Function


Function UpdateEvent_cont_012_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then

		If (e\eventState=0) Then
			If (EntityDistance(mainPlayer\collider, e\room\doors[0]\obj)<2.5 And RemoteDoorOn) Then
				;PlaySound2(HorrorSFX(7))
				PlayRangedSound_SM(sndManager\lever,mainPlayer\cam,e\room\doors[0]\obj)
				e\eventState=1
				e\room\doors[0]\locked = False
				UseDoor(e\room\doors[0],False)
				e\room\doors[0]\locked = True
			EndIf
		Else

			If (e\sounds[0] = 0) Then LoadEventSound(e,"SFX/Music/012Golgotha.ogg")
			e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\objects[3], 5.0)

			; TODO: Move to musicmanager.
			;If (e\sounds[1] = 0) Then LoadEventSound(e, "SFX/Music/012.ogg", 1)

			If (e\eventState<90) Then e\eventState=CurveValue(90,e\eventState,500)
			PositionEntity(e\room\objects[2], EntityX(e\room\objects[2],True),(-130-448*Sin(e\eventState))*RoomScale,EntityZ(e\room\objects[2],True),True)

			If (e\eventState2 > 0 And e\eventState2 < 200) Then
				e\eventState2 = e\eventState2 + timing\tickDuration
				RotateEntity(e\room\levers[0]\obj, CurveValue(85, EntityPitch(e\room\levers[0]\obj), 5), EntityYaw(e\room\levers[0]\obj), 0)
			Else
				e\eventState2 = e\eventState2 + timing\tickDuration
				If (e\eventState2<250) Then
					ShowEntity(e\room\objects[3])
				Else
					HideEntity(e\room\objects[3])
					If (e\eventState2>300) Then e\eventState2=200
				EndIf
			EndIf

			If (Not IsPlayerWearingItem(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingItem(mainPlayer,"gasmask3")) And (Not IsPlayerWearingItem(mainPlayer,"nvgoggles")) Then
				temp = False
				If (EntityVisible(e\room\objects[2],mainPlayer\cam)) Then temp = True

				;012 not visible, walk to the door
				If (temp=False) Then
					If (EntityVisible(e\room\doors[0]\frameobj,mainPlayer\cam)) Then
						pvt = CreatePivot()
						PositionEntity(pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\collider), EntityZ(mainPlayer\cam))
						PointEntity(pvt, e\room\doors[0]\frameobj)
						;TurnEntity(pvt, 90, 0, 0)
						mainPlayer\headPitch = CurveAngle(90, mainPlayer\headPitch+90, 100)
						mainPlayer\headPitch=mainPlayer\headPitch-90
						RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 150), 0)

						angle = WrapAngle(EntityYaw(pvt)-EntityYaw(mainPlayer\collider))
						If (angle<40.0) Then
							mainPlayer\forceMove = (40.0-angle)*0.008
						ElseIf (angle > 310.0) Then
							mainPlayer\forceMove = (40.0-Abs(360.0-angle))*0.008
						EndIf

						FreeEntity(pvt)
					EndIf
				Else
					e\soundChannels[1] = LoopRangedSound(e\sounds[1], e\soundChannels[1], mainPlayer\cam, e\room\objects[3], 10, e\eventState3/(86.0*70.0))

					pvt = CreatePivot()
					PositionEntity(pvt, EntityX(mainPlayer\cam), EntityY(e\room\objects[2],True)-0.05, EntityZ(mainPlayer\cam))
					PointEntity(pvt, e\room\objects[2])
					RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 80-(e\eventState3/200.0)), 0)

					TurnEntity(pvt, 90, 0, 0)
					mainPlayer\headPitch = CurveAngle(EntityPitch(pvt)+25, mainPlayer\headPitch + 90.0, 80-(e\eventState3/200.0))
					mainPlayer\headPitch=mainPlayer\headPitch-90

					dist = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\objects[2],True),EntityZ(e\room\objects[2],True))

					mainPlayer\heartbeatIntensity = 150
					;HeartBeatVolume = Max(3.0-dist,0.0)/3.0
					mainPlayer\blurTimer = Max((2.0-dist)*(e\eventState3/800.0)*(Sin(Float(TimeInPosMilliSecs()) / 20.0 + 1.0)),mainPlayer\blurTimer)
					mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs()) / 20.0)+1.0)*8.0*Max((3.0-dist),0.0))

					If (mainPlayer\breathChn <> 0) Then
						If (IsChannelPlaying(mainPlayer\breathChn)) Then StopChannel(mainPlayer\breathChn)
					EndIf

					If (dist < 0.6) Then
						e\eventState3=Min(e\eventState3+timing\tickDuration,86*70)
						If (e\eventState3>70 And e\eventState3-timing\tickDuration=<70) Then
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech1.ogg"))
						ElseIf (e\eventState3>13*70 And e\eventState3-timing\tickDuration=<13*70) Then
							Msg="You start pushing your nails into your wrist, drawing blood."
							MsgTimer = 7*70
							mainPlayer\injuries=mainPlayer\injuries+0.5
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech2.ogg"))
						ElseIf (e\eventState3>31*70 And e\eventState3-timing\tickDuration=<31*70) Then
							tex = LoadTexture("GFX/Map/Textures/scp-012_1.jpg")
							EntityTexture(e\room\objects[4], tex,0,1)
							FreeTexture(tex)

							Msg="You tear open your left wrist and start writing on the composition with your blood."
							MsgTimer = 7*70
							mainPlayer\injuries=Max(mainPlayer\injuries,1.5)
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech"+Str(Rand(3,4))+".ogg"))
						ElseIf (e\eventState3>49*70 And e\eventState3-timing\tickDuration=<49*70) Then
							Msg="You push your fingers deeper into the wound."
							MsgTimer = 8*70
							mainPlayer\injuries=mainPlayer\injuries+0.3
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech5.ogg"))
						ElseIf (e\eventState3>63*70 And e\eventState3-timing\tickDuration=<63*70) Then
							tex = LoadTexture("GFX/Map/Textures/scp-012_2.jpg")
							EntityTexture(e\room\objects[4], tex,0,1)
							FreeTexture(tex)

							mainPlayer\injuries=mainPlayer\injuries+0.5
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech6.ogg"))
						ElseIf (e\eventState3>74*70 And e\eventState3-timing\tickDuration=<74*70) Then
							tex = LoadTexture("GFX/Map/Textures/scp-012_3.jpg")
							EntityTexture(e\room\objects[4], tex,0,1)
							FreeTexture(tex)

							Msg="You rip the wound wide open. Grabbing scoops of blood pouring out."
							MsgTimer = 7*70
							mainPlayer\injuries=mainPlayer\injuries+0.8
							PlaySound2(LoadTempSound("SFX/SCP/012/Speech7.ogg"))
							mainPlayer\crouching = True

							de = CreateDecal(DECAL_BLOOD_POOL,  EntityX(mainPlayer\collider), -768*RoomScale+0.01, EntityZ(mainPlayer\collider),90,Rnd(360),0)
							de\size = 0.1 : de\maxSize = 0.45 : de\sizeChange = 0.0002 : UpdateDecals()
						ElseIf (e\eventState3>85*70 And e\eventState3-timing\tickDuration=<85*70) Then
							DeathMSG = "Subject D-9341 found in a pool of blood next to SCP-012. Subject seems to have ripped open his wrists and written three extra "
							DeathMSG = DeathMSG + "lines to the composition before dying of blood loss."
							Kill(mainPlayer)
						EndIf

						RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(mainPlayer\collider)+Sin(e\eventState3*(e\eventState3/2000))*(e\eventState3/300), EntityYaw(mainPlayer\collider), 80), 0)

					Else
						angle = WrapAngle(EntityYaw(pvt)-EntityYaw(mainPlayer\collider))
						If (angle<40.0) Then
							mainPlayer\forceMove = (40.0-angle)*0.02
						ElseIf (angle > 310.0) Then
							mainPlayer\forceMove = (40.0-Abs(360.0-angle))*0.02
						EndIf
					EndIf

					FreeEntity(pvt)
				EndIf

			EndIf

		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
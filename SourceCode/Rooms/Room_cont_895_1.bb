Function FillRoom_cont_895_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\x, 0, r\z - 448.0 * RoomScale, 0, r, False, DOOR_TYPE_CONT, r\roomTemplate\name)
    d\autoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x - 384.0 * RoomScale, 0.7, r\z - 280.0 * RoomScale, True)

    sc = CreateSecurityCam(r\x - 320.0 * RoomScale, r\y + 704 * RoomScale, r\z + 288.0 * RoomScale, r, True)
    sc\angle = 45 + 180
    sc\turn = 45
    sc\coffinEffect = True
    TurnEntity(sc\cameraObj, 120, 0, 0)
    EntityParent(sc\obj, r\obj)

    CoffinCam = sc

    PositionEntity(sc\scrObj, r\x - 800 * RoomScale, 288.0 * RoomScale, r\z - 340.0 * RoomScale)
    EntityParent(sc\scrObj, r\obj)
    TurnEntity(sc\scrObj, 0, 180, 0)

    r\levers[0] = CreateLever()

	ScaleEntity(r\levers[0]\baseObj, 0.04, 0.04, 0.04)
	ScaleEntity(r\levers[0]\obj, 0.04, 0.04, 0.04)
	PositionEntity(r\levers[0]\baseObj, r\x - 800.0 * RoomScale, r\y + 180.0 * RoomScale, r\z - 336 * RoomScale, True)
	PositionEntity(r\levers[0]\obj, r\x - 800.0 * RoomScale, r\y + 180.0 * RoomScale, r\z - 336 * RoomScale, True)

	EntityParent(r\levers[0]\baseObj, r\obj)
	EntityParent(r\levers[0]\obj, r\obj)

    RotateEntity(r\levers[0]\baseObj, 0, 180, 0)
    RotateEntity(r\levers[0]\obj, 10, 0, 0)

    EntityPickMode(r\levers[0]\obj, 1, False)
    EntityRadius(r\levers[0]\obj, 0.1)

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x, -1320.0 * RoomScale, r\z + 2304.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)

    it = CreatePaper("doc895", r\x - 688.0 * RoomScale, r\y + 133.0 * RoomScale, r\z - 304.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("nvgoggles", r\x + 280.0 * RoomScale, r\y -1456.0 * RoomScale, r\z + 2164.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x + 96.0*RoomScale, -1532.0 * RoomScale, r\z + 2016.0 * RoomScale,True)

    ;de.Decal = CreateDecal(0, r\x + 96.0*RoomScale, -1535.0 * RoomScale, r\z + 32.0 * RoomScale, 90, Rand(360), 0)
    ;EntityParent(de\obj, r\obj)
End Function


Function UpdateEventCoffin106(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

End Function



Function UpdateEventCoffin(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, hasBatteryFor895%, tempF#, tempF2#, tempF3#

	;[Block]

	If (e\eventState < TimeInPosMilliSecs()) Then
		;SCP-079 starts broadcasting 895 camera feed on monitors after leaving the first zone
		;TODO: rewrite this to adjust for separate zone loading
		If (EntityPitch(e\room\levers[0]\obj, True) > 0) Then ;camera feed on
			For sc = Each SecurityCam
				If (Not sc\specialCam) Then
					If (sc\coffinEffect=0 And sc\room\roomTemplate\name<>"room106" And sc\room\roomTemplate\name<>"room205") Then sc\coffinEffect = 2
					If (sc\room = e\room) Then sc\screen = True
				EndIf
			Next
		Else ;camera feed off
			For sc = Each SecurityCam
				If (Not sc\specialCam) Then
					If (sc\coffinEffect<>1) Then sc\coffinEffect = 0
					If (sc\room = e\room) Then sc\screen = False
				EndIf
			Next
		EndIf

		e\eventState = TimeInPosMilliSecs()+3000
	EndIf

	If (mainPlayer\currRoom = e\room) Then
		CoffinDistance = EntityDistance(mainPlayer\collider, e\room\objects[1])
		If (CoffinDistance < 1.5) Then
			If ((Not Contained106) And e\name="coffin106" And e\eventState2 = 0) Then
				de = CreateDecal(0, EntityX(e\room\objects[1],True), -1531.0*RoomScale, EntityZ(e\room\objects[1],True), 90, Rand(360), 0)
				de\size = 0.05 : de\sizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals()

				If (Curr106\state > 0) Then
					PositionEntity(Curr106\collider, EntityX(e\room\objects[1],True), -10240*RoomScale, EntityZ(e\room\objects[1],True))
					Curr106\state = -0.1
					ShowEntity(Curr106\obj)
					e\eventState2 = 1
				EndIf
			EndIf
		EndIf

		;TODO: cleanup
		If (IsPlayerWearingItem(mainPlayer,"nvgoggles")) Then
			hasBatteryFor895 = 0
			For i = 0 To mainPlayer\inventory\size - 1
				If (mainPlayer\inventory\items[i] <> Null) Then
					If (mainPlayer\inventory\items[i]\template\name = "nvgoggles" Or mainPlayer\inventory\items[i]\template\name = "supernv") Then
						If (mainPlayer\inventory\items[i]\state > 0.0) Then
							hasBatteryFor895 = 1
							Exit
						EndIf
					EndIf
				EndIf
			Next
			;If (EntityVisible(mainPlayer\cam,e\room\levers[0]\baseObj)) Then
				;If (EntityInView(e\room\levers[0]\baseObj, mainPlayer\cam)) Then
			;If EntityVisible(mainPlayer\cam,e\room\objects[1])
				If (CoffinDistance < 4.0) And (hasBatteryFor895) Then

					mainPlayer\sanity895 = mainPlayer\sanity895-(timing\tickDuration*1.1);/WearingNightVision)
					mainPlayer\blurTimer = Sin(TimeInPosMilliSecs()/10)*Abs(mainPlayer\sanity895)

				    tempF = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\objects[1],True),EntityZ(e\room\objects[1],True))
					tempF2 = EntityYaw(mainPlayer\collider)
					tempF3 = angleDist(tempF+90+Sin(WrapAngle(e\eventState3/10)),tempF2)

					TurnEntity(mainPlayer\collider, 0,tempF3/4,0,True)

					tempF = Abs(Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\objects[1],True),EntityZ(e\room\objects[1],True)))
					tempF2 = -60.0 * Min(Max((2.0-tempF)/2.0,0.0),1.0)

					mainPlayer\headPitch=(mainPlayer\headPitch * 0.8)+(tempF2 * 0.2)

					;TODO: fix
					;If (Rand(Int(Max(tempF*100.0,1.0)))=1) And (e\eventState3<0.0) Then
					;	EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], GorePics(Rand(0, 5)))
					;	;PlaySound2(HorrorSFX(1))
					;	e\eventState3 = 10.0
					;	EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,255,255)
					;EndIf
					If (mainPlayer\sanity895 < (-1000)) Then
						If (IsPlayerWearingItem(mainPlayer,"supernv")) Then
							DeathMSG = Chr(34)+"Class D viewed SCP-895 through a pair of digital night vision goggles, presumably enhanced by SCP-914. It might be possible that the subject"
							DeathMSG = DeathMSG + "was able to resist the memetic effects partially through these goggles. The goggles have been stored for further study."+Chr(34)
						Else
							DeathMSG = Chr(34)+"Class D viewed SCP-895 through a pair of digital night vision goggles, killing him."+Chr(34)
						EndIf
						Kill(mainPlayer)
					EndIf
				EndIf
			;EndIf
		EndIf

		If (e\eventState3>0.0) Then e\eventState3=Max(e\eventState3-timing\tickDuration,0.0)
		If (e\eventState3=0.0) Then
			e\eventState3=-1.0
			;TODO: fix
			;EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], NVTexture)
			If (IsPlayerWearingItem(mainPlayer,"nvgoggles")) Then
				EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,255,0)
			ElseIf (IsPlayerWearingItem(mainPlayer,"supernv")) Then
				EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
			EndIf
		EndIf

		;TODO
		;ShouldPlay = 66

		If (e\room\levers[0]\succ) Then
			For sc = Each SecurityCam
				If (Not sc\specialCam) Then
					If (sc\coffinEffect=0 And sc\room\roomTemplate\name<>"room106") Then sc\coffinEffect = 2
					If (sc\coffinEffect = 1) Then EntityBlend(sc\scrOverlay, 3)
					If (sc\room = e\room) Then sc\screen = True
				EndIf
			Next
		Else
		For sc = Each SecurityCam
				If (Not sc\specialCam) Then
					If (sc\coffinEffect <> 1) Then sc\coffinEffect = 0
					If (sc\coffinEffect = 1) Then EntityBlend(sc\scrOverlay, 0)
					If (sc\room = e\room) Then sc\screen = False
				EndIf
			Next
		EndIf
	Else
		CoffinDistance = e\room\dist
	EndIf
	;[End Block]
End Function



;~IDEal Editor Parameters:
;~C#Blitz3D
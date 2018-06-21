Function FillRoom_cont_895_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x, 0, r\z - 448.0 * RoomScale, 0, r, False, True, 2)
    d\autoClose = False : d\open = False
    PositionEntity(d\buttons[0], r\x - 384.0 * RoomScale, 0.7, r\z - 280.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x - 320.0 * RoomScale, r\y + 704 * RoomScale, r\z + 288.0 * RoomScale, r, True)
    sc\angle = 45 + 180
    sc\turn = 45
    sc\coffinEffect = True
    TurnEntity(sc\cameraObj, 120, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    CoffinCam = sc
    
    PositionEntity(sc\ScrObj, r\x - 800 * RoomScale, 288.0 * RoomScale, r\z - 340.0 * RoomScale)
    EntityParent(sc\ScrObj, r\obj)
    TurnEntity(sc\ScrObj, 0, 180, 0)
        
    r\Levers[0] = CreateLever()

	ScaleEntity(r\Levers[0]\baseObj, 0.04, 0.04, 0.04)
	ScaleEntity(r\Levers[0]\obj, 0.04, 0.04, 0.04)
	PositionEntity (r\Levers[0]\baseObj, r\x - 800.0 * RoomScale, r\y + 180.0 * RoomScale, r\z - 336 * RoomScale, True)
	PositionEntity (r\Levers[0]\obj, r\x - 800.0 * RoomScale, r\y + 180.0 * RoomScale, r\z - 336 * RoomScale, True)
	
	EntityParent(r\Levers[0]\baseObj, r\obj)
	EntityParent(r\Levers[0]\obj, r\obj)
		
    RotateEntity(r\Levers[0]\baseObj, 0, 180, 0)
    RotateEntity(r\Levers[0]\obj, 10, 0, 0)
    
    EntityPickMode r\Levers[0]\obj, 1, False
    EntityRadius r\Levers[0]\obj, 0.1
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x, -1320.0 * RoomScale, r\z + 2304.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    
    it = CreateItem("Document SCP-895", "paper", r\x - 688.0 * RoomScale, r\y + 133.0 * RoomScale, r\z - 304.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Level 3 Key Card", "key3", r\x + 240.0 * RoomScale, r\y -1456.0 * RoomScale, r\z + 2064.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Night Vision Goggles", "nvgoggles", r\x + 280.0 * RoomScale, r\y -1456.0 * RoomScale, r\z + 2164.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x + 96.0*RoomScale, -1532.0 * RoomScale, r\z + 2016.0 * RoomScale,True)
    
    ;de.Decals = CreateDecal(0, r\x + 96.0*RoomScale, -1535.0 * RoomScale, r\z + 32.0 * RoomScale, 90, Rand(360), 0)
    ;EntityParent de\obj, r\obj
End Function


Function UpdateEventCoffin106(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

End Function



Function UpdateEventCoffin(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	If e\eventState < TimeInPosMilliSecs() Then
		;SCP-079 starts broadcasting 895 camera feed on monitors after leaving the first zone
		;TODO: rewrite this to adjust for separate zone loading
		If EntityPitch(e\room\Levers[0]\obj, True) > 0 Then ;camera feed on
			For sc.SecurityCams = Each SecurityCams
				If (Not sc\SpecialCam) Then
					If sc\coffinEffect=0 And sc\room\RoomTemplate\Name<>"room106" And sc\room\RoomTemplate\Name<>"room205" Then sc\coffinEffect = 2
					If sc\room = e\room Then sc\Screen = True
				EndIf
			Next
		Else ;camera feed off
			For sc.SecurityCams = Each SecurityCams
				If (Not sc\SpecialCam) Then
					If sc\coffinEffect<>1 Then sc\coffinEffect = 0
					If sc\room = e\room Then sc\Screen = False
				EndIf
			Next
		EndIf
		
		e\eventState = TimeInPosMilliSecs()+3000
	EndIf
	
	If mainPlayer\currRoom = e\room Then
		CoffinDistance = EntityDistance(mainPlayer\collider, e\room\Objects[1])
		If CoffinDistance < 1.5 Then 
			If (Not Contained106) And e\name="coffin106" And e\eventState2 = 0 Then
				de.Decals = CreateDecal(0, EntityX(e\room\Objects[1],True), -1531.0*RoomScale, EntityZ(e\room\Objects[1],True), 90, Rand(360), 0)
				de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals()
				
				If Curr106\state > 0 Then
					PositionEntity Curr106\collider, EntityX(e\room\Objects[1],True), -10240*RoomScale, EntityZ(e\room\Objects[1],True)
					Curr106\state = -0.1
					ShowEntity Curr106\obj
					e\eventState2 = 1
				EndIf
			EndIf
		EndIf
		
		;TODO: cleanup
		If IsPlayerWearingTempName(mainPlayer,"nvgoggles") Then
			Local hasBatteryFor895% = 0
			For i% = 0 To mainPlayer\inventory\size - 1
				If (mainPlayer\inventory\items[i] <> Null) Then
					If (mainPlayer\inventory\items[i]\itemtemplate\tempName = "nvgoggles" Or mainPlayer\inventory\items[i]\itemtemplate\tempName = "supernv") And IsPlayerWearingItem(mainPlayer,mainPlayer\inventory\items[i]) Then
						If mainPlayer\inventory\items[i]\state > 0.0 Then
							hasBatteryFor895 = 1
							Exit
						EndIf
					EndIf
				EndIf
			Next
			;If EntityVisible(mainPlayer\cam,e\room\Levers[0]\baseObj) Then
				;If EntityInView(e\room\Levers[0]\baseObj, mainPlayer\cam) Then
			;If EntityVisible(mainPlayer\cam,e\room\Objects[1])
				If (CoffinDistance < 4.0) And (hasBatteryFor895) Then
					
					mainPlayer\sanity895 = mainPlayer\sanity895-(timing\tickDuration*1.1);/WearingNightVision)
					mainPlayer\blurTimer = Sin(TimeInPosMilliSecs()/10)*Abs(mainPlayer\sanity895)
					
					tempF# = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\Objects[1],True),EntityZ(e\room\Objects[1],True))
					tempF2# = EntityYaw(mainPlayer\collider)
					tempF3# = angleDist(tempF+90+Sin(WrapAngle(e\eventState3/10)),tempF2)
					
					TurnEntity mainPlayer\collider, 0,tempF3/4,0,True
					
					tempF# = Abs(Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\Objects[1],True),EntityZ(e\room\Objects[1],True)))
					tempF2# = -60.0 * Min(Max((2.0-tempF)/2.0,0.0),1.0)
					
					mainPlayer\headPitch=(mainPlayer\headPitch * 0.8)+(tempF2 * 0.2)
					
					;TODO: fix
					;If (Rand(Int(Max(tempF*100.0,1.0)))=1) And (e\eventState3<0.0) Then
					;	EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], GorePics(Rand(0, 5)))
					;	;PlaySound2(HorrorSFX(1))
					;	e\eventState3 = 10.0
					;	EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,255,255)
					;EndIf
					If mainPlayer\sanity895 < (-1000) Then 
						If IsPlayerWearingTempName(mainPlayer,"supernv") Then
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
		
		If e\eventState3>0.0 Then e\eventState3=Max(e\eventState3-timing\tickDuration,0.0)
		If e\eventState3=0.0 Then
			e\eventState3=-1.0
			;TODO: fix
			;EntityTexture(mainPlayer\overlays[OVERLAY_NIGHTVISION], NVTexture)
			If IsPlayerWearingTempName(mainPlayer,"nvgoggles") Then
				EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,255,0)
			ElseIf IsPlayerWearingTempName(mainPlayer,"supernv") Then
				EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
			EndIf
		EndIf
		
		ShouldPlay = 66
		
		If (e\room\Levers[0]\succ) Then
			For sc.SecurityCams = Each SecurityCams
				If (Not sc\SpecialCam) Then
					If sc\coffinEffect=0 And sc\room\RoomTemplate\Name<>"room106" Then sc\coffinEffect = 2
					If sc\coffinEffect = 1 Then EntityBlend(sc\ScrOverlay, 3)
					If sc\room = e\room Then sc\Screen = True
				EndIf
			Next
		Else
			For sc.SecurityCams = Each SecurityCams
				If (Not sc\SpecialCam) Then
					If sc\coffinEffect <> 1 Then sc\coffinEffect = 0
					If sc\coffinEffect = 1 Then EntityBlend(sc\ScrOverlay, 0)
					If sc\room = e\room Then sc\Screen = False
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
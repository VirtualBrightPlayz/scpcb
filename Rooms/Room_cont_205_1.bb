Function FillRoom_cont_205_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d.Doors = CreateDoor(r\zone, r\x + 128.0 * RoomScale, 0, r\z + 640.0 *RoomScale, 90, r, True, False, 3)
    d\AutoClose = False : d\open = False
    ;PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    ;PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x - 1392.0 * RoomScale, -128.0 * RoomScale, r\z - 384*RoomScale, 0, r, True, False, 3)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
    FreeEntity(r\RoomDoors[0]\buttons[0]) : r\RoomDoors[0]\buttons[0]=0
    FreeEntity(r\RoomDoors[0]\buttons[1]) : r\RoomDoors[0]\buttons[1]=0
    
    sc.SecurityCams = CreateSecurityCam(r\x - 1152.0 * RoomScale, r\y + 900.0 * RoomScale, r\z + 176.0 * RoomScale, r, True)
    sc\angle = 90 : sc\turn = 0
    EntityParent(sc\obj, r\obj)
    
    sc\AllowSaving = False
    sc\RenderInterval = 0
    
    EntityParent(sc\ScrObj, 0)
    PositionEntity(sc\ScrObj, r\x - 1716.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 176.0 * RoomScale, True)
    TurnEntity sc\ScrObj, 0, 90, 0
    ScaleSprite sc\ScrObj, 896.0*0.5*RoomScale, 896.0*0.5*RoomScale
    
    EntityParent(sc\ScrObj, r\obj)
    ;EntityBlend(sc\ScrObj, 2)
    
    CameraZoom (sc\Cam, 1.5)
    
    HideEntity sc\ScrOverlay
    HideEntity sc\MonitorObj
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity r\Objects[0], r\x - 1536.0 * RoomScale, r\y + 730.0 * RoomScale, r\z + 192.0 * RoomScale, True
    RotateEntity r\Objects[0], 0,-90,0,True
    
    r\Objects[1] = sc\ScrObj
End Function


Function UpdateEvent_cont_205_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		ShouldPlay = 15
		If e\EventState=0 Or e\room\Objects[0]=0 Then
			If e\EventStr = ""
				e\EventStr = "load0"
			ElseIf e\EventStr = "load0"
				e\room\Objects[3] = LoadAnimMesh("GFX\npcs\205_demon1.b3d")
				e\EventStr = "load1"
			ElseIf e\EventStr = "load1"
				e\room\Objects[4] = LoadAnimMesh("GFX\npcs\205_demon2.b3d")
				e\EventStr = "load2"
			ElseIf e\EventStr = "load2"
				e\room\Objects[5] = LoadAnimMesh("GFX\npcs\205_demon3.b3d")
				e\EventStr = "load3"
			ElseIf e\EventStr = "load3"
				e\room\Objects[6] = LoadAnimMesh("GFX\npcs\205_woman.b3d")
				e\EventStr = "load4"
			ElseIf e\EventStr = "load4"
				;TODO: fix
				;If Music(15) = 0 Then
				;	Music(15) = LoadSound("SFX\Music\205.ogg")
				;EndIf
				e\EventStr = "load5"
			ElseIf e\EventStr = "load5"
				For i = 3 To 6
				;EntityColor e\room\Objects[i], 30,30,30
				;EntityShininess e\room\Objects[i],0.0
				;vertex color + flatshaded
				;EntityFX e\room\Objects[i], 2
					
					PositionEntity e\room\Objects[i], EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True), True
					RotateEntity e\room\Objects[i], -90, EntityYaw(e\room\Objects[0],True), 0, True
					ScaleEntity(e\room\Objects[i], 0.05, 0.05, 0.05, True)
				Next
				e\EventStr = "load6"
			ElseIf e\EventStr = "load6"
				HideEntity(e\room\Objects[3])
				HideEntity(e\room\Objects[4])
				HideEntity(e\room\Objects[5])
				e\EventState = 1
			EndIf
			
		Else
			
			If (e\EventState<65) Then
				If (Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))<2.0) Then
					PlaySound(LoadTempSound("SFX\SCP\205\Enter.ogg"))
					
					e\EventState = Max(e\EventState, 65)
					
					ShowEntity(e\room\Objects[3])
					ShowEntity(e\room\Objects[4])
					ShowEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[6])
					
					SetAnimTime(e\room\Objects[3], 492)
					SetAnimTime(e\room\Objects[4], 434)
					SetAnimTime(e\room\Objects[5], 434)
					
					e\room\RoomDoors[0]\open = False
				EndIf
				
				If e\EventState>7 Then
					If (Rand(0,300)=1) Then
						e\room\RoomDoors[0]\open = Not e\room\RoomDoors[0]\open
					EndIf
				EndIf 
				
				e\EventState2 = e\EventState2 + timing\tickDuration							
			EndIf
			
			
			Select e\EventState
				Case 1
					;sitting
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 526, 530, 0.2)
					If e\EventState2 > 20*70 Then e\EventState = e\EventState+1
				Case 3
					;laying down
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 377, 525, 0.2)
					If e\EventState2 > 30*70 Then e\EventState = e\EventState+1
				Case 5
					;standing
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 228, 376, 0.2)
					If e\EventState2 > 40*70 Then 
						e\EventState = e\EventState+1
						PlayRangedSound(LoadTempSound("SFX\SCP\205\Horror.ogg"), mainPlayer\cam, e\room\Objects[6], 10, 0.3)
					EndIf	
				Case 7
					;first demon appears
					ShowEntity(e\room\Objects[5])
					;le sexy demon pose
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 500, 648, 0.2)
					;TurnEntity(e\room\Objects[6], 0.0, DeltaYaw(e\room\Objects[6], e\room\Objects[5])*0.01, 0.0, True)
					If e\EventState2 > 60*70 Then 
						e\EventState = e\EventState+1
						PlayRangedSound(LoadTempSound("SFX\SCP\205\Horror.ogg"), mainPlayer\cam, e\room\Objects[6], 10, 0.5)
					EndIf
				Case 9
					;second demon appears
					ShowEntity(e\room\Objects[4])
					;idle
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 2, 200, 0.2)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 4, 125, 0.2)
					
					;TurnEntity(e\room\Objects[6], 0.0, DeltaYaw(e\room\Objects[6], e\room\Objects[4])*0.01, 0.0, True)
					
					If e\EventState2 > 80*70 Then 
						e\EventState = e\EventState+1
						PlaySound(LoadTempSound("SFX\SCP\205\Horror.ogg"))
					EndIf
				Case 11
					;third demon
					ShowEntity(e\room\Objects[3])
					;idle
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 2, 226, 0.2)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 2, 200, 0.2)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 4, 125, 0.2)
					
					;TurnEntity(e\room\Objects[6], 0.0, DeltaYaw(e\room\Objects[5], e\room\Objects[4])*0.01, 0.0, True)
					
					If e\EventState2 > 85*70 Then e\EventState = e\EventState+1
				Case 13
					If (AnimTime(e\room\Objects[6])<>227) Then SetAnimTime(e\room\Objects[6], 227)
						
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 2, 491, 0.05)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 197, 433, 0.05)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 2, 433, 0.05)
				Case 66
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 492, 534, 0.1, False)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 434, 466, 0.1, False)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 434, 494, 0.1, False)
					
					If AnimTime(e\room\Objects[3])>515 Then
						;MoveEntity(e\room\Objects[3], 0,-timing\tickDuration*0.001,0)
						;MoveEntity(e\room\Objects[4], 0,-timing\tickDuration*0.001,0)
						;MoveEntity(e\room\Objects[5], 0,-timing\tickDuration*0.001,0)
						If AnimTime(e\room\Objects[3])>533 Then 
							e\EventState = 67
							e\EventState2 = 0										
							e\EventState3 = 0
							HideEntity e\room\Objects[1]
						EndIf
					EndIf
				Case 67
					If (Rand(150)=1) Then
						DeathMSG = "The SCP-205 cycle seems to have resumed its normal course after the anomalies observed during "
						DeathMSG = DeathMSG + "[REDACTED]. The body of subject D-9341 was discovered inside the chamber. "
						DeathMSG = DeathMSG + "The subject exhibits signs of blunt force trauma typical for personnel who have "
						DeathMSG = DeathMSG + "entered the chamber when the lights are off."
						
						mainPlayer\injuries=mainPlayer\injuries+Rnd(0.4,0.8)
						PlaySound DamageSFX(Rand(2,3))
						mainPlayer\camShake = 0.5
						
						e\EventState2 = Rnd(-0.1, 0.1)
						e\EventState3 = Rnd(-0.1, 0.1)
						
						If (mainPlayer\injuries>5.0) Kill(mainPlayer)
					EndIf
					
					TranslateEntity(mainPlayer\collider, e\EventState2,0,e\EventState3)
					e\EventState2 = CurveValue(e\EventState2, 0, 10.0)								
					e\EventState3 = CurveValue(e\EventState3, 0, 10.0)
				Default
					If (Rand(3)=1) Then
						HideEntity e\room\Objects[1]
					Else
						ShowEntity e\room\Objects[1]
					EndIf
					
					e\EventState3 = e\EventState3 + timing\tickDuration
					If (e\EventState3>50) Then
						ShowEntity e\room\Objects[1]
						e\EventState = e\EventState+1
						e\EventState3=0
					EndIf
			End Select
		EndIf
	Else If (e\room\Objects[3]<>0)
		HideEntity(e\room\Objects[3])
		HideEntity(e\room\Objects[4])
		HideEntity(e\room\Objects[5])
		HideEntity(e\room\Objects[6])
	Else
		e\EventState = 0
		e\EventStr = ""
	EndIf
	;[End block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
Function FillRoom_cont_173_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    ;the containment doors
    r\RoomDoors[1] = CreateDoor(r\zone, r\x + 4000.0 * RoomScale, 384.0*RoomScale, r\z + 1696.0 * RoomScale, 90, r, True, True)
    r\RoomDoors[1]\locked = False : r\RoomDoors[1]\AutoClose = False
    r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\open = True 
    FreeEntity(r\RoomDoors[1]\buttons[0]) : r\RoomDoors[1]\buttons[0] = 0
    FreeEntity(r\RoomDoors[1]\buttons[1]) : r\RoomDoors[1]\buttons[1] = 0
    r\RoomDoors[1]\MTFClose = False
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x + 2704.0 * RoomScale, 384.0*RoomScale, r\z + 624.0 * RoomScale, 90, r, False)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
    FreeEntity(r\RoomDoors[2]\buttons[0]) : r\RoomDoors[2]\buttons[0] = 0
    FreeEntity(r\RoomDoors[2]\buttons[1]) : r\RoomDoors[2]\buttons[1] = 0
    r\RoomDoors[2]\MTFClose = False
    
    d.Doors = CreateDoor(r\zone, r\x + 1392.0 * RoomScale, 384.0*RoomScale, r\z + 64.0 * RoomScale, 90, r, True)
    d\AutoClose = False
    FreeEntity(d\buttons[0]) : d\buttons[0]=0
    FreeEntity(d\buttons[1]) : d\buttons[1]=0
    d\MTFClose = False
    
    d.Doors = CreateDoor(r\zone, r\x - 640.0 * RoomScale, 384.0*RoomScale, r\z + 64.0 * RoomScale, 90, r, False)
    d\locked = True : d\AutoClose = False
    
    d.Doors = CreateDoor(r\zone, r\x + 1280.0 * RoomScale, 384.0*RoomScale, r\z + 312.0 * RoomScale, 180, r, True)
    d\locked = True : d\AutoClose = False
    PositionEntity(d\buttons[0], r\x + 1120.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 328.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 1120.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 296.0 * RoomScale, True)
    FreeEntity(d\obj2) : d\obj2=0
    d\MTFClose = False
    
    d.Doors = CreateDoor(r\zone, r\x, 0, r\z + 1184.0 * RoomScale, 0, r, False)
    d\locked = True
    
    r\Objects[0] = LoadMesh("GFX\map\IntroDesk.b3d")
    ScaleEntity r\Objects[0], RoomScale, RoomScale ,RoomScale
    PositionEntity r\Objects[0], r\x + 272.0 * RoomScale, 0, r\z + 400.0 * RoomScale
    EntityParent r\Objects[0], r\obj
    
    de.Decals = CreateDecal(0, r\x + 272.0 * RoomScale, 0.005, r\z + 262.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    
    r\Objects[1] = LoadMesh("GFX\map\IntroDrawer.b3d")
    ScaleEntity r\Objects[1], RoomScale, RoomScale ,RoomScale
    PositionEntity r\Objects[1], r\x + 448.0 * RoomScale, 0, r\z + 192.0 * RoomScale
    EntityParent r\Objects[1], r\obj
    
    de.Decals = CreateDecal(0, r\x + 456.0 * RoomScale, 0.005, r\z + 135.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x - 336.0 * RoomScale, r\y + 352 * RoomScale, r\z + 48.0 * RoomScale, r, True)
    sc\angle = 270
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\CameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    PositionEntity(sc\ScrObj, r\x + 1456 * RoomScale, 608 * RoomScale, r\z +352.0 * RoomScale)
    TurnEntity(sc\ScrObj, 0, 90, 0)
    EntityParent(sc\ScrObj, r\obj)
    
    r\Objects[2] = CreatePivot()
    PositionEntity (r\Objects[2], EntityX(r\obj) + 40.0 * RoomScale, 460.0 * RoomScale, EntityZ(r\obj) + 1072.0 * RoomScale)
    r\Objects[3] = CreatePivot()
    PositionEntity (r\Objects[3], EntityX(r\obj) - 80.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\Objects[4] = CreatePivot()
    PositionEntity (r\Objects[4], EntityX(r\obj) - 128.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)
    
    r\Objects[5] = CreatePivot()
    PositionEntity (r\Objects[5], EntityX(r\obj) + 660.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\Objects[6] = CreatePivot()
    PositionEntity (r\Objects[6], EntityX(r\obj) + 700 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)
    
    r\Objects[7] = CreatePivot()
    PositionEntity (r\Objects[7], EntityX(r\obj) + 1472.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 912.0 * RoomScale)
    
    For i = 2 To 7
        EntityParent(r\Objects[i], r\obj)
    Next
End Function


Function UpdateEventAlarm(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	If e\room\RoomDoors[5]=Null Then
		For i=0 To 3
			If e\room\AdjDoor[i]<>Null Then
				e\room\RoomDoors[5] = e\room\AdjDoor[i]
				e\room\RoomDoors[5]\open = True
				Exit
			EndIf
		Next
	EndIf
	If e\EventState = 0 Then
		If mainPlayer\currRoom = e\room Then
			
			e\room\RoomDoors[2]\open=True
			
			ShowEntity mainPlayer\overlays[OVERLAY_FOG]
			AmbientLight Brightness, Brightness, Brightness
			;TODO: fix
			;CameraFogRange(mainPlayer\cam, mainPlayer\camFogNear, mainPlayer\camFogFar)
			CameraFogMode(mainPlayer\cam, 1)
			If SelectedDifficulty\saveType = SAVEANYWHERE Then
				Msg = "Press "+KeyName(keyBinds\save)+" to save."
				MsgTimer = 70*4
			ElseIf SelectedDifficulty\saveType = SAVEONSCREENS Then
				Msg = "Saving is only permitted on clickable monitors scattered throughout the facility."
				MsgTimer = 70 * 8
			EndIf
			
			Curr173\Idle=False
			
			While e\room\RoomDoors[1]\openstate < 180
				e\room\RoomDoors[1]\openstate = Min(180, e\room\RoomDoors[1]\openstate + 0.8)
				MoveEntity(e\room\RoomDoors[1]\obj, Sin(e\room\RoomDoors[1]\openstate) / 180.0, 0, 0)
				MoveEntity(e\room\RoomDoors[1]\obj2, -Sin(e\room\RoomDoors[1]\openstate) / 180.0, 0, 0)
			Wend
			
			If e\room\NPC[0] <> Null Then SetNPCFrame(e\room\NPC[0], 74) : e\room\NPC[0]\State = 8
			
			If e\room\NPC[1] = Null Then
				e\room\NPC[1] = CreateNPC(NPCtypeD, 0,0,0)
				tex = LoadTexture("GFX\npcs\scientist2.jpg")
				e\room\NPC[1]\texture = "GFX\npcs\scientist2.jpg"
				EntityTexture e\room\NPC[1]\obj, tex
				FreeTexture tex
			EndIf
			PositionEntity e\room\NPC[1]\Collider, e\room\x, 0.5, e\room\z-1.0, True
			ResetEntity e\room\NPC[1]\Collider
			SetNPCFrame(e\room\NPC[1], 210)
			
			If e\room\NPC[2] = Null Then
				e\room\NPC[2] = CreateNPC(NPCtypeGuard, 0,0,0)
			EndIf
			PositionEntity e\room\NPC[2]\Collider, e\room\x-240*RoomScale, 0.5, e\room\z+528*RoomScale, True
			ResetEntity e\room\NPC[2]\Collider
			e\room\NPC[2]\State = 7
			PointEntity e\room\NPC[2]\Collider,e\room\NPC[1]\Collider
			
			If e\room\NPC[0]=Null
				e\room\NPC[3] = CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[2], True), EntityY(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True))
				;e\room\NPC[3]\Angle = 180
				RotateEntity e\room\NPC[3]\Collider,0,90,0
				SetNPCFrame(e\room\NPC[3], 151) : e\room\NPC[3]\State = 8
				MoveEntity e\room\NPC[3]\Collider,1,0,0
				
				e\room\NPC[4] = CreateNPC(NPCtypeD, EntityX(e\room\Objects[3], True), 0.5, EntityZ(e\room\Objects[3], True))
				;PointEntity(e\room\NPC[4]\Collider, e\room\Objects[7])
				SetNPCFrame(e\room\NPC[4], 19) : e\room\NPC[4]\State = 3
				RotateEntity e\room\NPC[4]\Collider,0,270,0
				MoveEntity e\room\NPC[4]\Collider,0,0,2.65
				
				e\room\NPC[5] = CreateNPC(NPCtypeD, EntityX(e\room\Objects[4], True), 0.5, EntityZ(e\room\Objects[4], True))
				;PointEntity(e\room\NPC[5]\Collider, e\room\Objects[7])
				SetNPCFrame(e\room\NPC[5], 19) : e\room\NPC[5]\State = 3
				RotateEntity e\room\NPC[5]\Collider,0,270,0
				tex = LoadTexture("GFX\npcs\classd2.jpg")
				e\room\NPC[5]\texture = "GFX\npcs\classd2.jpg"
				EntityTexture e\room\NPC[5]\obj, tex
				FreeTexture tex
				MoveEntity e\room\NPC[5]\Collider,0.25,0,3.0
				RotateEntity e\room\NPC[5]\Collider,0,0,0
				
				x# = EntityX(e\room\obj, True)+3712*RoomScale
				y# = 384.0*RoomScale
				z# = EntityZ(e\room\obj, True)+1312*RoomScale
				
				For i = 3 To 5
					PositionEntity(e\room\NPC[i]\Collider, x + (EntityX(e\room\NPC[i]\Collider) - EntityX(e\room\obj)), y+EntityY(e\room\NPC[i]\Collider)+0.4, z + (EntityZ(e\room\NPC[i]\Collider) - EntityZ(e\room\obj)))
					ResetEntity(e\room\NPC[i]\Collider)
				Next
			EndIf
			
			e\EventState = 1
		EndIf
	Else
		
		If e\room\NPC[0] <> Null Then AnimateNPC(e\room\NPC[0], 113, 151, 0.4, False)
		
		CurrTrigger = CheckTriggers()
		
		If (CurrTrigger = "173scene_timer") Then
			e\EventState = e\EventState+timing\tickDuration
		Else If (CurrTrigger = "173scene_activated")
			e\EventState = Max(e\EventState, 500)
		EndIf
		
		
		If e\EventState >= 500 Then
			e\EventState = e\EventState+timing\tickDuration
			
			If e\EventState2 = 0 Then
				CanSave = False
				If e\EventState > 900 And e\room\RoomDoors[5]\open Then
					If e\EventState - timing\tickDuration <= 900 Then 
						e\room\NPC[1]\Sound = LoadSound("SFX\Room\Intro\WhatThe.ogg")
						e\room\NPC[1]\SoundChn = PlaySound2(e\room\NPC[1]\Sound, mainPlayer\cam, e\room\NPC[1]\Collider)
					EndIf
					e\room\NPC[1]\State = 3
					e\room\NPC[1]\CurrSpeed = CurveValue(-0.008, e\room\NPC[1]\CurrSpeed, 5.0)
					AnimateNPC(e\room\NPC[1], 260, 236, e\room\NPC[1]\CurrSpeed * 18)
					RotateEntity e\room\NPC[1]\Collider, 0, 0, 0
					
					
					If e\EventState > 900+2.5*70 Then
						If e\room\NPC[2]\State <> 1
							e\room\NPC[2]\CurrSpeed = CurveValue(-0.012, e\room\NPC[2]\CurrSpeed, 5.0)
							AnimateNPC(e\room\NPC[2], 895, 843, e\room\NPC[2]\CurrSpeed*50)
							MoveEntity e\room\NPC[2]\Collider, 0,0,e\room\NPC[2]\CurrSpeed*timing\tickDuration
							e\room\NPC[2]\State = 8
							
							If EntityZ(e\room\NPC[2]\Collider) < e\room\z-512*RoomScale Then
								PointEntity(e\room\NPC[2]\obj, e\room\NPC[1]\Collider)
								RotateEntity e\room\NPC[2]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[2]\obj)-180,EntityYaw(e\room\NPC[2]\Collider),15.0), 0
							Else
								RotateEntity e\room\NPC[2]\Collider, 0, 0, 0
							EndIf
						EndIf
					EndIf
					
					If e\EventState < 900+4*70 Then
						PositionEntity Curr173\Collider, e\room\x+32*RoomScale, 0.31, e\room\z+1072*RoomScale, True
						RotateEntity Curr173\Collider,0,190,0
						
						If e\EventState > 900+70 And e\EventState < 900+2.5*70 Then
							AnimateNPC(e\room\NPC[2], 1539, 1553, 0.2, False)
							PointEntity(e\room\NPC[2]\obj, Curr173\Collider)
							RotateEntity e\room\NPC[2]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[2]\obj),EntityYaw(e\room\NPC[2]\Collider),15.0), 0
						EndIf

					Else
						If e\EventState-timing\tickDuration < 900+4*70 Then 
							PlaySound(IntroSFX(11)); : LightBlink = 3.0
							
							;Stonedrag.
							PlaySound2 (Curr173\sounds[0], mainPlayer\cam, Curr173\Collider)

							PointEntity Curr173\Collider, e\room\NPC[2]\Collider
							If EntityY(mainPlayer\collider)<320*RoomScale Then mainPlayer\blinkTimer = -10
						EndIf
						
						PositionEntity Curr173\Collider, e\room\x-96*RoomScale, 0.31, e\room\z+592*RoomScale, True
						
						If e\room\NPC[2]\State <> 1 And (Not mainPlayer\dead) Then
							If EntityZ(e\room\NPC[2]\Collider) < e\room\z-1150*RoomScale Then
								e\room\RoomDoors[5]\open = False
								;LightBlink = 3.0
								PlaySound(IntroSFX(11))
								mainPlayer\blinkTimer = -10

								;Stonedrag.
								PlaySound2 (Curr173\sounds[0], mainPlayer\cam, Curr173\Collider)
								
								PositionEntity Curr173\Collider, 0,0,0
								ResetEntity Curr173\Collider
								Msg = "Hold "+KeyName(keyBinds\sprint)+" to run."
								MsgTimer = 70*8
							EndIf
						EndIf
					EndIf
					
					;If Ulgrin can see the player then start shooting at them.
					If (CurrTrigger = "173scene_end") And EntityVisible(e\room\NPC[2]\Collider,mainPlayer\collider) And (Not mainPlayer\godMode) Then
						e\room\NPC[2]\State = 1
						e\room\NPC[2]\State3 = 1
					ElseIf e\room\NPC[2]\State = 1 And (Not EntityVisible(e\room\NPC[2]\Collider, mainPlayer\collider))
						e\room\NPC[2]\State = 0
						e\room\NPC[2]\State3 = 0
					EndIf
					
					If e\room\NPC[2]\State = 1 Then e\room\RoomDoors[5]\open = True
				Else
					CanSave = True
					If e\room\NPC[2]\State <> 1
						If EntityX(mainPlayer\collider)<(e\room\x+1384*RoomScale) Then e\EventState = Max(e\EventState,900)
						
						If e\room\RoomDoors[5]\openstate = 0 Then 
							HideEntity e\room\NPC[1]\obj
							HideEntity e\room\NPC[1]\Collider
							
							HideEntity e\room\NPC[2]\obj
							HideEntity e\room\NPC[2]\Collider
							e\EventState2=1
						EndIf
					EndIf
				EndIf
			EndIf
			
			
		;	If e\EventState > 900+3*70 And e\EventState < 900+4*70 Then 
		;		mainPlayer\camShake = 0.2
		;	ElseIf e\EventState > 900+32.3*70 And e\EventState < 900+34*70
		;		mainPlayer\camShake = 0.4
		;	ElseIf e\EventState > 900+51*70 And e\EventState < 900+53.5*70
		;		mainPlayer\camShake = 1.0
		;	ElseIf e\EventState > 900+57.5*70 And e\EventState < 900+58.5*70
		;		mainPlayer\camShake = 0.4
		;	EndIf
			
			PositionEntity e\room\Objects[0], EntityX(e\room\Objects[0],True), -Max(e\EventState-1300,0)/4500, EntityZ(e\room\Objects[0],True), True
			RotateEntity e\room\Objects[0], -Max(e\EventState-1320,0)/130, 0, -Max(e\EventState-1300,0)/40, True
			
			PositionEntity e\room\Objects[1], EntityX(e\room\Objects[1],True), -Max(e\EventState-1800,0)/5000, EntityZ(e\room\Objects[1],True), True
			RotateEntity e\room\Objects[1], -Max(e\EventState-2040,0)/135, 0, -Max(e\EventState-2040,0)/43, True
			
			If EntityDistance(e\room\Objects[0],mainPlayer\collider)<2.5 Then
				;If Rand(300)=2 Then PlaySound2(DecaySFX(Rand(1,3)),mainPlayer\cam,e\room\Objects[0], 3.0)
			EndIf
		EndIf
		
		If (e\EventState < 2000) Then
			If e\soundChannels[0] = 0 Then
				e\soundChannels[0] = PlaySound(AlarmSFX(0))
			Else
				If Not ChannelPlaying(e\soundChannels[0]) Then e\soundChannels[0] = PlaySound(AlarmSFX(0))
			End If
		EndIf
		
		If (e\EventState3<10) Then
			If (Not ChannelPlaying(e\soundChannels[1])) Then
				e\EventState3 = e\EventState3+1
				
				If (e\sounds[1] <> 0) Then
					FreeSound(e\sounds[1])
					e\sounds[1] = 0
				EndIf
				
				e\sounds[1] = LoadSound("SFX\Alarm\Alarm2_"+Int(e\EventState3)+".ogg")
				e\soundChannels[1] = PlaySound(e\sounds[1])
				Else
					If Int(e\EventState3) = 8 Then mainPlayer\camShake = 1.0
			EndIf
		EndIf
		
		If ((e\EventState Mod 600 > 300) And ((e\EventState+timing\tickDuration) Mod 600 < 300)) Then
			i = Floor((e\EventState-5000)/600)+1
			
			If i = 0 Then PlaySound(LoadTempSound("SFX\Room\Intro\PA\scripted\scripted6.ogg"))
			
			If (i>0 And i<24) Then
				If Not CommotionState(i) Then ;Prevents the same commotion file from playing more then once.
					PlaySound(LoadTempSound("SFX\Room\Intro\Commotion\Commotion"+i+".ogg"))
					CommotionState(i) = True
				EndIf
			EndIf
			
			If (i>24) Then
				If e\room\NPC[0] <> Null Then RemoveNPC(e\room\NPC[0])
				If e\room\NPC[1] <> Null Then RemoveNPC(e\room\NPC[1])
				If e\room\NPC[2] <> Null Then RemoveNPC(e\room\NPC[2])
				
				FreeEntity e\room\Objects[0]
				FreeEntity e\room\Objects[1]
				
				DebugLog "delete alarm"
				
				RemoveEvent(e)							
			EndIf
		EndIf					
	End If
	;[End Block]
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D

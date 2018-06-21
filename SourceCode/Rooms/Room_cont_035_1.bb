Function FillRoom_cont_035_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 5)
    d\autoClose = False : d\locked = True : r\RoomDoors[0]=d
    PositionEntity (d\buttons[1], r\x - 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    FreeEntity d\buttons[0] : d\buttons[0]=0
    FreeEntity d\obj2 : d\obj2=0
    
    d2 = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
    d2\autoClose = False : d2\locked = True : r\RoomDoors[1]=d2
    PositionEntity (d2\buttons[0], r\x - 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
    RotateEntity(d2\buttons[0], 0, 90, 0, True)
    FreeEntity d2\buttons[1] : d2\buttons[1]=0
    FreeEntity d2\obj2 : d2\obj2=0
    
    ;door to the control room
    r\RoomDoors[2] = CreateDoor(r\zone, r\x + 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, False, 0, 5)
    r\RoomDoors[2]\autoClose = False
    
    ;door to the storage room
    r\RoomDoors[3] = CreateDoor(0, r\x + 768.0 * RoomScale, 0, r\z +512.0 * RoomScale, 90, r, False, 0, 0, "5731")
    r\RoomDoors[3]\autoClose = False			
    
    d\LinkedDoor = d2 : d2\LinkedDoor = d
    
    For i = 0 To 1
        r\Levers[i] = CreateLever()
        
		ScaleEntity(r\Levers[i]\baseObj, 0.04, 0.04, 0.04)
		ScaleEntity(r\Levers[i]\obj, 0.04, 0.04, 0.04)
		PositionEntity (r\Levers[i]\baseObj, r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - (208-i*76) * RoomScale, True)
		PositionEntity (r\Levers[i]\obj, r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - (208-i*76) * RoomScale, True)
		
		EntityParent(r\Levers[i]\baseObj, r\obj)
		EntityParent(r\Levers[i]\obj, r\obj)
        
        RotateEntity(r\Levers[i]\baseObj, 0, -90-180, 0)
        RotateEntity(r\Levers[i]\obj, -80, -90, 0)
        
        EntityPickMode r\Levers[i]\obj, 1, False
        EntityRadius r\Levers[i]\obj, 0.1				
    Next
    
    ;the control room
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 456 * RoomScale, 0.5, r\z + 400.0 * RoomScale, True)
    
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x - 576 * RoomScale, 0.5, r\z + 640.0 * RoomScale, True)
    
    For i = 0 To 1
        Local em.Emitters = CreateEmitter(r\x - 272.0 * RoomScale, 10, r\z + (624.0-i*512) * RoomScale, 0)
        TurnEntity(em\Obj, 90, 0, 0, True)
        EntityParent(em\Obj, r\obj)
        em\RandAngle = 15
        em\Speed = 0.05
        em\SizeChange = 0.007
        em\achange = -0.006
        em\gravity = -0.24
        
        r\Objects[5+i]=em\Obj
    Next
    
    ;the corners of the cont chamber (needed to calculate whether the player is inside the chamber)
    r\Objects[7] = CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x - 720 * RoomScale, 0.5, r\z + 880.0 * RoomScale, True)
    r\Objects[8] = CreatePivot(r\obj)
    PositionEntity(r\Objects[8], r\x + 176 * RoomScale, 0.5, r\z - 144.0 * RoomScale, True)			
    
    it = CreateItem("SCP-035 Addendum", "paper", r\x + 248.0 * RoomScale, r\y + 220.0 * RoomScale, r\z + 576.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Radio Transceiver", "radio", r\x - 544.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("SCP-500-01", "scp500", r\x + 1168*RoomScale, 224*RoomScale, r\z+576*RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Metal Panel", "scp148", r\x - 360 * RoomScale, 0.5, r\z + 644 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Document SCP-035", "paper", r\x + 1168.0 * RoomScale, 104.0 * RoomScale, r\z + 608.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_cont_035_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		;eventstate2 = has 035 told the code to the storage room (true/false)
		;eventstate3 = has the player opened the gas valves (0=no, 0<x<35*70 yes, x>35*70 the host has died)
		
		If e\eventState = 0 Then
			If EntityDistance(mainPlayer\collider, e\room\Objects[3])<2 Then 
				n.NPCs = CreateNPC(NPCtypeD, EntityX(e\room\Objects[4],True),0.5,EntityZ(e\room\Objects[4],True))
				
				n\texture = "GFX/NPCs/035victim.jpg"
				HideEntity n\obj	
				
				SetAnimTime(n\obj, 501)
				n\frame = 501
				
				n\state = 6
				
				e\eventState=1
			EndIf
			
		ElseIf e\eventState > 0 Then
			If e\room\NPC[0]=Null Then
				For n.NPCs = Each NPCs
					If n\texture = "GFX/NPCs/035victim.jpg" Then
						e\room\NPC[0]=n
						
						temp = e\room\NPC[0]\frame
						
						FreeEntity e\room\NPC[0]\obj
						e\room\NPC[0]\obj = LoadAnimMesh("GFX/NPCs/035.b3d")									
						x = 0.5 / MeshWidth(e\room\NPC[0]\obj)
						ScaleEntity e\room\NPC[0]\obj, x,x,x
						
						SetAnimTime(e\room\NPC[0]\obj, temp)
						
						ShowEntity e\room\NPC[0]\obj
						
						RotateEntity n\collider, 0, e\room\angle+270, 0, True
						
						Exit
					EndIf
				Next
			EndIf
			
			If IsChannelPlaying(e\room\NPC[0]\soundChannels[0]) Then
				e\room\NPC[0]\soundChannels[0]=LoopRangedSound(e\room\NPC[0]\sounds[0], e\room\NPC[0]\soundChannels[0], mainPlayer\cam, e\room\obj, 6.0)
				EndIf
			
			If e\eventState=1 Then
				If EntityDistance(mainPlayer\collider, e\room\Objects[3])<1.2 Then
					If EntityInView(e\room\NPC[0]\obj, mainPlayer\cam) Then
						PlaySound2(LoadTempSound("SFX/SCP/035/GetUp.ogg"))
						e\eventState = 1.5
					EndIf
				EndIf
			Else
				
				If e\room\RoomDoors[3]\open Then e\eventState2 = Max(e\eventState2, 1)
				
				;the door is closed
				If (e\eventState2 = 20) Then
					e\room\Levers[0]\succ = True
				EndIf

				If (Not e\room\Levers[0]\succ) Then
					;the gas valves are open
					temp = e\room\Levers[1]\succ
					If temp Or (e\eventState3>25*70 And e\eventState3<50*70) Then 
						If temp Then 
							PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 424.0*RoomScale, EntityZ(e\room\Objects[5],True),True)
							PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 424.0*RoomScale, EntityZ(e\room\Objects[6],True),True)
						Else
							PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
							PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
							
						EndIf
						
						If e\eventState3 >-30*70 Then 
							e\eventState3=Abs(e\eventState3)+timing\tickDuration
							If e\eventState3 > 1 And e\eventState3-timing\tickDuration=<1 Then
								e\room\NPC[0]\state = 0
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Gased1.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
							ElseIf e\eventState3>15*70 And e\eventState3<25*70 Then
								If e\eventState3-timing\tickDuration=<15*70 Then
									If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Gased2.ogg")
									e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									SetNPCFrame( e\room\NPC[0],553)
								EndIf
								e\room\NPC[0]\state = 6
								
								AnimateNPC(e\room\NPC[0], 553, 529, -0.12, False)
							ElseIf e\eventState3>25*70 And e\eventState3<35*70 Then
								e\room\NPC[0]\state = 6
								AnimateNPC(e\room\NPC[0], 529, 524, -0.08, False)
							ElseIf e\eventState3>35*70 Then
								If e\room\NPC[0]\state = 6 Then
									mainPlayer\sanity895 = -150*Sin(AnimTime(e\room\NPC[0]\obj)-524)*9
									AnimateNPC(e\room\NPC[0], 524, 553, 0.08, False)
									If e\room\NPC[0]\frame=553 Then e\room\NPC[0]\state = 0
								EndIf
								
								If e\eventState3-timing\tickDuration=<35*70 Then 
									If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedKilled1.ogg")
									e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									PlaySound2(LoadTempSound("SFX/SCP/035/KilledGetUp.ogg"))
									e\eventState = 60*70
								EndIf
							EndIf
						EndIf
					Else ;gas valves closed
						
						If e\room\NPC[0]\state = 6 Then
							If e\room\NPC[0]\frame>=501 And e\room\NPC[0]\frame<=523 Then
								e\room\NPC[0]\frame = Animate2(e\room\NPC[0]\obj, AnimTime(e\room\NPC[0]\obj), 501, 523, 0.08, False)
								If e\room\NPC[0]\frame=523 Then e\room\NPC[0]\state = 0
							EndIf	
							
							If e\room\NPC[0]\frame>=524 And e\room\NPC[0]\frame<=553 Then
								e\room\NPC[0]\frame = Animate2(e\room\NPC[0]\obj, AnimTime(e\room\NPC[0]\obj), 524, 553, 0.08, False)
								If e\room\NPC[0]\frame=553 Then e\room\NPC[0]\state = 0
							EndIf	
						EndIf
						
						PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
						PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
						
						If e\room\NPC[0]\state = 0 Then
							PointEntity e\room\NPC[0]\obj, mainPlayer\collider
							RotateEntity e\room\NPC[0]\collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\collider), 15.0), 0
							
							If Rand(500)=1 Then
								If EntityDistance(e\room\NPC[0]\collider, e\room\Objects[4])>2 Then
									e\room\NPC[0]\state2 = 1
								Else
									e\room\NPC[0]\state2 = 0
								EndIf
								e\room\NPC[0]\state = 1
							EndIf
						ElseIf e\room\NPC[0]\state = 1 Then
							If e\room\NPC[0]\state2 = 1 Then
								PointEntity e\room\NPC[0]\obj, e\room\Objects[4]
								If EntityDistance(e\room\NPC[0]\collider, e\room\Objects[4])<0.2 Then e\room\NPC[0]\state = 0
							Else
								RotateEntity e\room\NPC[0]\obj, 0, e\room\angle-180, 0, True
								If EntityDistance(e\room\NPC[0]\collider, e\room\Objects[4])>2 Then e\room\NPC[0]\state = 0
							EndIf
							
							RotateEntity e\room\NPC[0]\collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\collider), 15.0), 0
							
						EndIf
						
						If e\eventState3 > 0 Then
							e\eventState3=-e\eventState3
							If e\eventState3<-35*70 Then ;the host is dead
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedKilled2.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
								e\eventState = 60*70
							Else 
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								If e\eventState3<-20*70 Then
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedStop2.ogg")
								Else
									e\eventState3=-21*70
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedStop1.ogg")
								EndIf
								
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
								e\eventState = 61*70
							EndIf
						Else
							
							e\eventState = e\eventState+timing\tickDuration
							If e\eventState > 4*70 And e\eventState-timing\tickDuration =<4*70 Then
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Help1.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
								e\eventState = 10*70
							ElseIf e\eventState > 20*70 And e\eventState-timing\tickDuration =<20*70 Then
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Help2.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
							ElseIf e\eventState > 40*70 And e\eventState-timing\tickDuration =<40*70 Then
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle1.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
							ElseIf e\eventState > 50*70 And e\eventState-timing\tickDuration =<50*70 Then
								If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
								e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle2.ogg")
								e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
							ElseIf e\eventState > 80*70 And e\eventState-timing\tickDuration =<80*70 Then
								If e\eventState2 Then ;skip the closet part if player has already opened it
									e\eventState = 130*70
								Else
									If e\eventState3<-30*70 Then ;the host is dead
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedCloset.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									ElseIf e\eventState3 = 0 Then ;the gas valves haven't been opened
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Closet1.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									Else ;gas valves have been opened but 035 isn't dead
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedCloset.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									EndIf												
								EndIf
							ElseIf e\eventState > 80*70 Then
								If e\eventState2 Then e\eventState = Max(e\eventState,100*70)
								If e\eventState>110*70 And e\eventState-timing\tickDuration =<110*70 Then
									If e\eventState2 Then
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Closet2.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
										e\eventState = 130*70
									Else
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle3.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									EndIf
								ElseIf e\eventState>125*70 And e\eventState-timing\tickDuration =<125*70 Then
									If e\eventState2 Then
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Closet2.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									Else
										If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
										e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle4.ogg")
										e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
									EndIf
								ElseIf e\eventState>150*70 And e\eventState-timing\tickDuration =<150*70 Then
									If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle5.ogg")
									e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
								ElseIf e\eventState>200*70 And e\eventState-timing\tickDuration =<200*70 Then
									If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
									e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Idle6.ogg")
									e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
								EndIf
							EndIf
							
						EndIf
						
					EndIf								
					
				Else ;the player has opened the door
					If e\eventState2 < 10 Then
						e\room\RoomDoors[2]\open = False
						e\room\RoomDoors[2]\locked = True
						
						If e\room\RoomDoors[1]\open = False Then 
							e\room\RoomDoors[0]\locked = False
							e\room\RoomDoors[1]\locked = False
							UseDoor(e\room\RoomDoors[1])
							e\room\RoomDoors[0]\locked = True
							e\room\RoomDoors[1]\locked = True
							
						EndIf
						
						If e\eventState3=0 Then
							If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
							e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/Escape.ogg")
							e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
						ElseIf Abs(e\eventState3)>35*70 Then
							If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
							e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/KilledEscape.ogg")
							e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
						Else
							If e\room\NPC[0]\sounds[0]<>0 Then FreeSound(e\room\NPC[0]\sounds[0]) : e\room\NPC[0]\sounds[0] = 0
							e\room\NPC[0]\sounds[0] = LoadSound("SFX/SCP/035/GasedEscape.ogg")
							e\room\NPC[0]\soundChannels[0] = PlaySound(e\room\NPC[0]\sounds[0])
						EndIf
						e\eventState2 = 20
					EndIf
					
					If e\eventState2 = 20 Then
						dist = EntityDistance(e\room\RoomDoors[0]\frameobj, e\room\NPC[0]\collider)
						
						e\room\NPC[0]\state = 1
						If dist > 2.5 Then
							PointEntity e\room\NPC[0]\obj, e\room\RoomDoors[1]\frameobj
							RotateEntity e\room\NPC[0]\collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\collider), 15.0), 0
						ElseIf dist > 0.7 Then
							If IsChannelPlaying (e\room\NPC[0]\soundChannels[0]) Then
								e\room\NPC[0]\state = 0
								PointEntity e\room\NPC[0]\obj, mainPlayer\collider
								RotateEntity e\room\NPC[0]\collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\collider), 15.0), 0
							Else
								PointEntity e\room\NPC[0]\obj, e\room\RoomDoors[0]\frameobj
								RotateEntity e\room\NPC[0]\collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\collider), 15.0), 0
							EndIf
						Else
							RemoveNPC(e\room\NPC[0])
							e\room\NPC[0]=Null
							e\eventState = -1
							e\eventState2 = 0
							e\eventState3 = 0
							e\room\RoomDoors[0]\locked = False										
							e\room\RoomDoors[1]\locked = False
							e\room\RoomDoors[2]\locked = False
							UseDoor(e\room\RoomDoors[1],False)
							Local do.Doors
							For do = Each Doors
								If do\dir = 2 Then
									If Abs(EntityX(e\room\obj)-EntityX(do\frameobj,True))<4.5 Then 
										If Abs(EntityZ(e\room\obj)-EntityZ(do\frameobj,True))<4.5 Then 
											UseDoor(do,False)
											Exit
										EndIf
									EndIf
								EndIf
							Next
						EndIf
					EndIf
					
				EndIf
				
			EndIf ;eventstate > 1
			
		Else ;eventstate < 0 (SCP-035 has left)
			
			If (e\room\Levers[1]\succ) Then 
				PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 424.0*RoomScale, EntityZ(e\room\Objects[5],True),True)
				PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 424.0*RoomScale, EntityZ(e\room\Objects[6],True),True)
			Else
				PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
				PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
			EndIf
			
			;If (e\room\Levers[0]\succ) Then
			;	If e\room\RoomDoors[0]\open = True Then UseDoor(e\room\RoomDoors[1])
			;Else
			;	If e\room\RoomDoors[0]\open = False Then UseDoor(e\room\RoomDoors[1])
			;EndIf
			
			temp = False
			
			;player is inside the containment chamber
			If EntityX(mainPlayer\collider)>Min(EntityX(e\room\Objects[7],True),EntityX(e\room\Objects[8],True)) Then
				If EntityX(mainPlayer\collider)<Max(EntityX(e\room\Objects[7],True),EntityX(e\room\Objects[8],True)) Then
					If EntityZ(mainPlayer\collider)>Min(EntityZ(e\room\Objects[7],True),EntityZ(e\room\Objects[8],True)) Then
						If EntityZ(mainPlayer\collider)<Max(EntityZ(e\room\Objects[7],True),EntityZ(e\room\Objects[8],True)) Then
							If e\room\NPC[0]=Null Then
								If e\room\NPC[0]=Null Then e\room\NPC[0] = CreateNPC(NPCtypeTentacle, 0,0,0)
							EndIf
							
							PositionEntity e\room\NPC[0]\collider, EntityX(e\room\Objects[4],True), 0, EntityZ(e\room\Objects[4],True)
							
							If e\room\NPC[0]\state > 0 Then 
								If e\room\NPC[1]=Null Then
									If e\room\NPC[1]=Null Then e\room\NPC[1] = CreateNPC(NPCtypeTentacle, 0,0,0)
								EndIf
							EndIf
							
							mainPlayer\stamina = CurveValue(Min(60,mainPlayer\stamina), mainPlayer\stamina, 20.0)
							
							temp = True
							
							If e\sounds[0] = 0 Then LoadEventSound(e,"SFX/Room/035Chamber/Whispers1.ogg")
							If e\sounds[1] = 0 Then LoadEventSound(e,"SFX/Room/035Chamber/Whispers2.ogg",1)
							
							e\eventState2 = Min(e\eventState2+(timing\tickDuration/6000),1.0)
							e\eventState3 = CurveValue(e\eventState2, e\eventState3, 50)
							
							If (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingTempName(mainPlayer,"gasmask3")) Then
								mainPlayer\sanity895=mainPlayer\sanity895-timing\tickDuration*1.1
								mainPlayer\blurTimer = Sin(TimeInPosMilliSecs()/10)*Abs(mainPlayer\sanity895)
							EndIf
							
							If (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) Then
								mainPlayer\injuries = mainPlayer\injuries + (timing\tickDuration/5000)
							Else
								mainPlayer\injuries = mainPlayer\injuries + (timing\tickDuration/10000)
							EndIf
							
							If mainPlayer\dead = True And mainPlayer\bloodloss =>100 Then
								DeathMSG = "Class D Subject D-9341 found dead inside SCP-035's containment chamber. "
								DeathMSG = DeathMSG + "The subject exhibits heavy hemorrhaging of blood vessels around the eyes and inside the mouth and nose. "
								DeathMSG = DeathMSG + "Sent for autopsy."
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			
			If e\room\NPC[1]<>Null Then 
				PositionEntity e\room\NPC[1]\collider, EntityX(e\room\obj,True), 0, EntityZ(e\room\obj,True)
				angle = WrapAngle(EntityYaw(e\room\NPC[1]\collider)-e\room\angle)
				
				If angle>90 Then 
					If angle < 225 Then 
						RotateEntity e\room\NPC[1]\collider, 0, e\room\angle-89-180, 0
					Else
						RotateEntity e\room\NPC[1]\collider, 0, e\room\angle-1, 0	
					EndIf
				EndIf
			EndIf
			
			If temp = False Then 
				e\eventState2 = Max(e\eventState2-(timing\tickDuration/2000),0)
				e\eventState3 = Max(e\eventState3-(timing\tickDuration/100),0)
			EndIf
			
			If e\eventState3 > 0 And (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingTempName(mainPlayer,"gasmask3")) Then 
				e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\obj, 10, e\eventState3)
				e\soundChannels[1] = LoopRangedSound(e\sounds[1], e\soundChannels[1], mainPlayer\cam, e\room\obj, 10, (e\eventState3-0.5)*2)
			EndIf
			
		EndIf
		
	Else	
		If e\eventState=0 Then	
			If e\sounds[0] = 0 Then
				If EntityDistance(mainPlayer\collider, e\room\obj) < 20 Then
					LoadEventSound(e,"SFX/Room/035Chamber/InProximity.ogg")
					PlaySound2 e\sounds[0]
				EndIf
			EndIf
		ElseIf e\eventState < 0 Then
			For i = 0 To 1
				If e\room\NPC[i]<>Null Then 
					RemoveNPC(e\room\NPC[i])
					e\room\NPC[i]=Null
				EndIf						
			Next						
		EndIf
		
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
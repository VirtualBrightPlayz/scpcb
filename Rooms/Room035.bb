Function FillRoom035(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 5)
    d\AutoClose = False : d\locked = True : r\RoomDoors[0]=d
    PositionEntity (d\buttons[1], r\x - 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    FreeEntity d\buttons[0] : d\buttons[0]=0
    FreeEntity d\obj2 : d\obj2=0
    
    d2 = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
    d2\AutoClose = False : d2\locked = True : r\RoomDoors[1]=d2
    PositionEntity (d2\buttons[0], r\x - 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
    RotateEntity(d2\buttons[0], 0, 90, 0, True)
    FreeEntity d2\buttons[1] : d2\buttons[1]=0
    FreeEntity d2\obj2 : d2\obj2=0
    
    ;door to the control room
    r\RoomDoors[2] = CreateDoor(r\zone, r\x + 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, False, 0, 5)
    r\RoomDoors[2]\AutoClose = False
    
    ;door to the storage room
    r\RoomDoors[3] = CreateDoor(0, r\x + 768.0 * RoomScale, 0, r\z +512.0 * RoomScale, 90, r, False, 0, 0, "5731")
    r\RoomDoors[3]\AutoClose = False			
    
    d\LinkedDoor = d2 : d2\LinkedDoor = d
    
    For i = 0 To 1
        r\Objects[i*2] = CopyEntity(LeverBaseOBJ)
        r\Objects[i*2+1] = CopyEntity(LeverOBJ)
        
        r\Levers[i] = r\Objects[i*2+1]
        
        For n% = 0 To 1
            ScaleEntity(r\Objects[i*2+n], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[i*2+n], r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - (208-i*76) * RoomScale, True)
            
            EntityParent(r\Objects[i*2+n], r\obj)
        Next
        
        RotateEntity(r\Objects[i*2], 0, -90-180, 0)
        RotateEntity(r\Objects[i*2+1], -80, -90, 0)
        
        EntityPickMode r\Objects[i*2+1], 1, False
        EntityRadius r\Objects[i*2+1], 0.1				
    Next
    
    ;the control room
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 456 * RoomScale, 0.5, r\z + 400.0 * RoomScale, True)
    
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x - 576 * RoomScale, 0.5, r\z + 640.0 * RoomScale, True)
    
    For i = 0 To 1
        em.Emitters = CreateEmitter(r\x - 272.0 * RoomScale, 10, r\z + (624.0-i*512) * RoomScale, 0)
        TurnEntity(em\Obj, 90, 0, 0, True)
        EntityParent(em\Obj, r\obj)
        em\RandAngle = 15
        em\Speed = 0.05
        em\SizeChange = 0.007
        em\Achange = -0.006
        em\Gravity = -0.24
        
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


Function UpdateEventRoom035(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		;eventstate2 = has 035 told the code to the storage room (true/false)
		;eventstate3 = has the player opened the gas valves (0=no, 0<x<35*70 yes, x>35*70 the host has died)
		
		If e\EventState = 0 Then
			If EntityDistance(mainPlayer\collider, e\room\Objects[3])<2 Then 
				n.NPCs = CreateNPC(NPCtypeD, EntityX(e\room\Objects[4],True),0.5,EntityZ(e\room\Objects[4],True))
				
				n\texture = "GFX\NPCs\035victim.jpg"
				HideEntity n\obj	
				
				SetAnimTime(n\obj, 501)
				n\Frame = 501
				
				n\State = 6
				
				e\EventState=1
			EndIf
			
		ElseIf e\EventState > 0
			If e\room\NPC[0]=Null Then
				For n.NPCs = Each NPCs
					If n\texture = "GFX\NPCs\035victim.jpg" Then
						e\room\NPC[0]=n
						
						temp = e\room\NPC[0]\Frame
						
						FreeEntity e\room\NPC[0]\obj
						e\room\NPC[0]\obj = LoadAnimMesh_Strict("GFX\NPCs\035.b3d")									
						x = 0.5 / MeshWidth(e\room\NPC[0]\obj)
						ScaleEntity e\room\NPC[0]\obj, x,x,x
						
						SetAnimTime(e\room\NPC[0]\obj, temp)
						
						ShowEntity e\room\NPC[0]\obj
						
						RotateEntity n\Collider, 0, e\room\angle+270, 0, True
						
						Exit
					EndIf
				Next
			EndIf
			
			If e\room\NPC[0]\SoundChn <> 0 Then
				If ChannelPlaying(e\room\NPC[0]\SoundChn) Then
					e\room\NPC[0]\SoundChn=LoopSound2(e\room\NPC[0]\Sound, e\room\NPC[0]\SoundChn, mainPlayer\cam, e\room\obj, 6.0)
				EndIf
			EndIf
			
			If e\EventState=1 Then
				If EntityDistance(mainPlayer\collider, e\room\Objects[3])<1.2 
					If EntityInView(e\room\NPC[0]\obj, mainPlayer\cam) Then
						GiveAchievement(Achv035)
						PlaySound_Strict(LoadTempSound("SFX\SCP\035\GetUp.ogg"))
						e\EventState = 1.5
					EndIf
				EndIf
			Else
				
				If e\room\RoomDoors[3]\open Then e\EventState2 = Max(e\EventState2, 1)
				
				;the door is closed
				If UpdateLever(e\room\Levers[0],(e\EventState2=20)) = 0 Then
					;the gas valves are open
					temp = UpdateLever(e\room\Levers[1],False)
					If temp Or (e\EventState3>25*70 And e\EventState3<50*70) Then 
						If temp Then 
							PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 424.0*RoomScale, EntityZ(e\room\Objects[5],True),True)
							PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 424.0*RoomScale, EntityZ(e\room\Objects[6],True),True)
						Else
							PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
							PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
							
						EndIf
						
						If e\EventState3 >-30*70 Then 
							e\EventState3=Abs(e\EventState3)+timing\tickDuration
							If e\EventState3 > 1 And e\EventState3-timing\tickDuration=<1 Then
								e\room\NPC[0]\State = 0
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Gased1.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
							ElseIf e\EventState3>15*70 And e\EventState3<25*70
								If e\EventState3-timing\tickDuration=<15*70 Then
									If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Gased2.ogg")
									e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									SetNPCFrame( e\room\NPC[0],553)
								EndIf
								e\room\NPC[0]\State = 6
								
								AnimateNPC(e\room\NPC[0], 553, 529, -0.12, False)
							ElseIf e\EventState3>25*70 And e\EventState3<35*70
								e\room\NPC[0]\State = 6
								AnimateNPC(e\room\NPC[0], 529, 524, -0.08, False)
							ElseIf e\EventState3>35*70
								If e\room\NPC[0]\State = 6 Then
									mainPlayer\sanity895 = -150*Sin(AnimTime(e\room\NPC[0]\obj)-524)*9
									AnimateNPC(e\room\NPC[0], 524, 553, 0.08, False)
									If e\room\NPC[0]\Frame=553 Then e\room\NPC[0]\State = 0
								EndIf
								
								If e\EventState3-timing\tickDuration=<35*70 Then 
									If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedKilled1.ogg")
									e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									PlaySound_Strict(LoadTempSound("SFX\SCP\035\KilledGetUp.ogg"))
									e\EventState = 60*70
								EndIf
							EndIf
						EndIf
					Else ;gas valves closed
						
						If e\room\NPC[0]\State = 6 Then
							If e\room\NPC[0]\Frame>=501 And e\room\NPC[0]\Frame<=523 Then
								e\room\NPC[0]\Frame = Animate2(e\room\NPC[0]\obj, AnimTime(e\room\NPC[0]\obj), 501, 523, 0.08, False)
								If e\room\NPC[0]\Frame=523 Then e\room\NPC[0]\State = 0
							EndIf	
							
							If e\room\NPC[0]\Frame>=524 And e\room\NPC[0]\Frame<=553 Then
								e\room\NPC[0]\Frame = Animate2(e\room\NPC[0]\obj, AnimTime(e\room\NPC[0]\obj), 524, 553, 0.08, False)
								If e\room\NPC[0]\Frame=553 Then e\room\NPC[0]\State = 0
							EndIf	
						EndIf
						
						PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
						PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
						
						If e\room\NPC[0]\State = 0 Then
							PointEntity e\room\NPC[0]\obj, mainPlayer\collider
							RotateEntity e\room\NPC[0]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\Collider), 15.0), 0
							
							If Rand(500)=1 Then
								If EntityDistance(e\room\NPC[0]\Collider, e\room\Objects[4])>2 Then
									e\room\NPC[0]\State2 = 1
								Else
									e\room\NPC[0]\State2 = 0
								EndIf
								e\room\NPC[0]\State = 1
							EndIf
						ElseIf e\room\NPC[0]\State = 1
							If e\room\NPC[0]\State2 = 1 Then
								PointEntity e\room\NPC[0]\obj, e\room\Objects[4]
								If EntityDistance(e\room\NPC[0]\Collider, e\room\Objects[4])<0.2 Then e\room\NPC[0]\State = 0
							Else
								RotateEntity e\room\NPC[0]\obj, 0, e\room\angle-180, 0, True
								If EntityDistance(e\room\NPC[0]\Collider, e\room\Objects[4])>2 Then e\room\NPC[0]\State = 0
							EndIf
							
							RotateEntity e\room\NPC[0]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\Collider), 15.0), 0
							
						EndIf
						
						If e\EventState3 > 0 Then
							e\EventState3=-e\EventState3
							If e\EventState3<-35*70 Then ;the host is dead
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedKilled2.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
								e\EventState = 60*70
							Else 
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								If e\EventState3<-20*70 Then
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedStop2.ogg")
								Else
									e\EventState3=-21*70
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedStop1.ogg")
								EndIf
								
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
								e\EventState = 61*70
							EndIf
						Else
							
							e\EventState = e\EventState+timing\tickDuration
							If e\EventState > 4*70 And e\EventState-timing\tickDuration =<4*70 Then
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Help1.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
								e\EventState = 10*70
							ElseIf e\EventState > 20*70 And e\EventState-timing\tickDuration =<20*70
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Help2.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
							ElseIf e\EventState > 40*70 And e\EventState-timing\tickDuration =<40*70
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle1.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
							ElseIf e\EventState > 50*70 And e\EventState-timing\tickDuration =<50*70
								If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
								e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle2.ogg")
								e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
							ElseIf e\EventState > 80*70 And e\EventState-timing\tickDuration =<80*70
								If e\EventState2 Then ;skip the closet part if player has already opened it
									e\EventState = 130*70
								Else
									If e\EventState3<-30*70 Then ;the host is dead
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedCloset.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									ElseIf e\EventState3 = 0 ;the gas valves haven't been opened
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Closet1.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									Else ;gas valves have been opened but 035 isn't dead
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedCloset.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									EndIf												
								EndIf
							ElseIf e\EventState > 80*70
								If e\EventState2 Then e\EventState = Max(e\EventState,100*70)
								If e\EventState>110*70 And e\EventState-timing\tickDuration =<110*70 Then
									If e\EventState2 Then
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Closet2.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
										e\EventState = 130*70
									Else
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle3.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									EndIf
								ElseIf e\EventState>125*70 And e\EventState-timing\tickDuration =<125*70
									If e\EventState2 Then
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Closet2.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									Else
										If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
										e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle4.ogg")
										e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									EndIf
								ElseIf e\EventState>150*70 And e\EventState-timing\tickDuration =<150*70
									If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle5.ogg")
									e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
								ElseIf e\EventState>200*70 And e\EventState-timing\tickDuration =<200*70
									If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
									e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Idle6.ogg")
									e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
									
								EndIf
							EndIf
							
						EndIf
						
					EndIf								
					
				Else ;the player has opened the door
					If e\EventState2 < 10 Then
						e\room\RoomDoors[2]\open = False
						e\room\RoomDoors[2]\locked = True
						
						If e\room\RoomDoors[1]\open = False Then 
							e\room\RoomDoors[0]\locked = False
							e\room\RoomDoors[1]\locked = False
							UseDoor(e\room\RoomDoors[1])
							e\room\RoomDoors[0]\locked = True
							e\room\RoomDoors[1]\locked = True
							
						EndIf
						
						If e\EventState3=0 Then
							If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
							e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\Escape.ogg")
							e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
						ElseIf Abs(e\EventState3)>35*70
							If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
							e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\KilledEscape.ogg")
							e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
						Else
							If e\room\NPC[0]\Sound<>0 Then FreeSound_Strict(e\room\NPC[0]\Sound) : e\room\NPC[0]\Sound = 0
							e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\035\GasedEscape.ogg")
							e\room\NPC[0]\SoundChn = PlaySound_Strict(e\room\NPC[0]\Sound)
						EndIf
						e\EventState2 = 20
					EndIf
					
					If e\EventState2 = 20 Then
						dist = EntityDistance(e\room\RoomDoors[0]\frameobj, e\room\NPC[0]\Collider)
						
						e\room\NPC[0]\State = 1
						If dist > 2.5 Then
							PointEntity e\room\NPC[0]\obj, e\room\RoomDoors[1]\frameobj
							RotateEntity e\room\NPC[0]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\Collider), 15.0), 0
						ElseIf dist > 0.7
							If ChannelPlaying (e\room\NPC[0]\SoundChn) Then
								e\room\NPC[0]\State = 0
								PointEntity e\room\NPC[0]\obj, mainPlayer\collider
								RotateEntity e\room\NPC[0]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\Collider), 15.0), 0
							Else
								PointEntity e\room\NPC[0]\obj, e\room\RoomDoors[0]\frameobj
								RotateEntity e\room\NPC[0]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[0]\obj), EntityYaw(e\room\NPC[0]\Collider), 15.0), 0
							EndIf
						Else
							RemoveNPC(e\room\NPC[0])
							e\room\NPC[0]=Null
							e\EventState = -1
							e\EventState2 = 0
							e\EventState3 = 0
							e\room\RoomDoors[0]\locked = False										
							e\room\RoomDoors[1]\locked = False
							e\room\RoomDoors[2]\locked = False
							UseDoor(e\room\RoomDoors[1],False)
							For do.doors = Each Doors
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
			
			If UpdateLever(e\room\Levers[1],False) Then 
				PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 424.0*RoomScale, EntityZ(e\room\Objects[5],True),True)
				PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 424.0*RoomScale, EntityZ(e\room\Objects[6],True),True)
			Else
				PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
				PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
			EndIf
			
			;If UpdateLever(e\room\Levers[0]) Then
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
							
							ShouldPlay = 0
							
							If e\room\NPC[0]=Null Then
								If e\room\NPC[0]=Null Then e\room\NPC[0] = CreateNPC(NPCtypeTentacle, 0,0,0)
							EndIf
							
							PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[4],True), 0, EntityZ(e\room\Objects[4],True)
							
							If e\room\NPC[0]\State > 0 Then 
								If e\room\NPC[1]=Null Then
									If e\room\NPC[1]=Null Then e\room\NPC[1] = CreateNPC(NPCtypeTentacle, 0,0,0)
								EndIf
							EndIf
							
							mainPlayer\stamina = CurveValue(Min(60,mainPlayer\stamina), mainPlayer\stamina, 20.0)
							
							temp = True
							
							If e\Sound = 0 Then LoadEventSound(e,"SFX\Room\035Chamber\Whispers1.ogg")
							If e\Sound2 = 0 Then LoadEventSound(e,"SFX\Room\035Chamber\Whispers2.ogg",1)
							
							e\EventState2 = Min(e\EventState2+(timing\tickDuration/6000),1.0)
							e\EventState3 = CurveValue(e\EventState2, e\EventState3, 50)
							
							If (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingTempName(mainPlayer,"gasmask3")) Then
								mainPlayer\sanity895=mainPlayer\sanity895-timing\tickDuration*1.1
								mainPlayer\blurTimer = Sin(MilliSecs2()/10)*Abs(mainPlayer\sanity895)
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
				PositionEntity e\room\NPC[1]\Collider, EntityX(e\room\obj,True), 0, EntityZ(e\room\obj,True)
				angle = WrapAngle(EntityYaw(e\room\NPC[1]\Collider)-e\room\angle)
				
				If angle>90 Then 
					If angle < 225 Then 
						RotateEntity e\room\NPC[1]\Collider, 0, e\room\angle-89-180, 0
					Else
						RotateEntity e\room\NPC[1]\Collider, 0, e\room\angle-1, 0	
					EndIf
				EndIf
			EndIf
			
			If temp = False Then 
				e\EventState2 = Max(e\EventState2-(timing\tickDuration/2000),0)
				e\EventState3 = Max(e\EventState3-(timing\tickDuration/100),0)
			EndIf
			
			If e\EventState3 > 0 And (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingTempName(mainPlayer,"gasmask3")) Then 
				e\SoundCHN = LoopSound2(e\Sound, e\SoundCHN, mainPlayer\cam, e\room\obj, 10, e\EventState3)
				e\SoundCHN2 = LoopSound2(e\Sound2, e\SoundCHN2, mainPlayer\cam, e\room\obj, 10, (e\EventState3-0.5)*2)
			EndIf
			
		EndIf
		
	Else	
		If e\EventState=0 Then	
			If e\Sound = 0 Then
				If EntityDistance(mainPlayer\collider, e\room\obj) < 20 Then
					LoadEventSound(e,"SFX\Room\035Chamber\InProximity.ogg")
					PlaySound_Strict e\Sound
				EndIf
			EndIf
		ElseIf e\EventState < 0
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
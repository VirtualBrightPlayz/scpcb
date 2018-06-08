Const STATE173_IDLE%             = 0
Const STATE173_ATTACK%           = 1
Const STATE173_MOVE_TO_TARGET%   = 2
Const STATE173_BEING_CONTAINED%  = 3
Const STATE173_CONTAINED%        = 4

Function InitializeNPCtype173(n.NPCs)
	n\nvName = "SCP-173"
	n\collider = CreatePivot()
	EntityRadius(n\collider, 0.32)
	EntityType(n\collider, HIT_PLAYER)

	;TODO: Remove this?
	n\gravity = True
	
	n\obj = LoadMesh("GFX/npcs/173_2.b3d")
	
	;On Halloween set jack-o-latern texture.
	If (Left(CurrentDate(), 6) = "31 Oct") Then
		HalloweenTex = True
		Local texFestive% = LoadTexture("GFX/npcs/173h.pt", 1)
		EntityTexture(n\obj, texFestive, 0, 0)
		FreeTexture(texFestive)
	EndIf
	
	Local temp# = (GetINIFloat("DATA/NPCs.ini", "SCP-173", "scale") / MeshDepth(n\obj))			
	ScaleEntity(n\obj, temp, temp, temp)
	
	n\speed = (GetINIFloat("DATA/NPCs.ini", "SCP-173", "speed") / 100.0)
	
	n\obj2 = LoadMesh("GFX/173box.b3d")
	ScaleEntity(n\obj2, RoomScale, RoomScale, RoomScale)
	HideEntity(n\obj2)

	n\sounds[0] = LoadSound("SFX/SCP/173/StoneDrag.ogg")

	n\sounds[1] = LoadSound("SFX/SCP/173/Spotted1.ogg")
	n\sounds[2] = LoadSound("SFX/SCP/173/Spotted2.ogg")
	n\sounds[3] = LoadSound("SFX/SCP/173/Spotted3.ogg")

	n\sounds[4] = LoadSound("SFX/SCP/173/BigReveal1.ogg")
	n\sounds[5] = LoadSound("SFX/SCP/173/BigReveal2.ogg")
	n\sounds[6] = LoadSound("SFX/SCP/173/BigReveal3.ogg")
	n\sounds[7] = LoadSound("SFX/SCP/173/BigReveal4.ogg")
	n\sounds[8] = LoadSound("SFX/SCP/173/BigReveal5.ogg")

	n\sounds[9] = LoadSound("SFX/SCP/173/NeckSnap1.ogg")
	n\sounds[10] = LoadSound("SFX/SCP/173/NeckSnap2.ogg")
	n\sounds[11] = LoadSound("SFX/SCP/173/NeckSnap3.ogg")

	n\state = STATE173_ATTACK
End Function

Function UpdateNPCtype173(n.NPCs)
	If (n\state = STATE173_IDLE Or n\state = STATE173_CONTAINED) Then
		Return
	EndIf

	Local dist#
	Select n\state
		Case STATE173_ATTACK
			dist = EntityDistance(n\collider, mainPlayer\collider)
			PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider) - 0.32, EntityZ(n\collider))
			RotateEntity(n\obj, 0, EntityYaw(n\collider) - 180, 0)

			Local playerVisible% = False
			Local canMove% = True
			If dist < 15 Then
				If dist < 10.0 Then 
					If EntityVisible(n\collider, mainPlayer\collider) Then
						playerVisible = True
						n\targetX = EntityX(mainPlayer\collider, True)
						n\targetY = EntityY(mainPlayer\collider, True)
						n\targetZ = EntityZ(mainPlayer\collider, True)
					EndIf
				EndIf
				
				n\prevX = EntityX(n\collider)
				n\prevZ = EntityZ(n\collider)				
				
				If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6) Then
					If EntityInView(n\obj, mainPlayer\cam) Then canMove = False
				EndIf
			EndIf
			
			If NoTarget Then canMove = True
			
			;player is looking at it -> doesn't move
			If (Not canMove) Then
				;Blur and zoom camera slightly when looking at 173.
				mainPlayer\blurTimer = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), mainPlayer\blurTimer)
				mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0))								
				
				;If it's close spoopy horror sound.
				If dist < 3.5 And TimeInPosMilliSecs() - n\lastSeen > 60000 And playerVisible Then
					PlaySound2(n\sounds[Rand(1,3)])
					
					n\lastSeen = TimeInPosMilliSecs()
				EndIf
				
				If dist < 1.5 And n\lastDist > 2.0 And playerVisible Then
					mainPlayer\camZoom = 40.0
					mainPlayer\heartbeatIntensity = Max(mainPlayer\heartbeatIntensity, 140)
					;HeartBeatVolume = 0.5
					
					;Jumpscare.
					PlaySound2(n\sounds[Rand(4,8)])
				EndIf									
					
				n\lastDist = dist
			Else 
				;Stonedrag.
				n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 10.0, n\state)

				;more than 6 room lengths away from the player -> teleport to a room closer to the player
				If dist > 50 Then
					If Rand(70)=1 Then
						If mainPlayer\currRoom\RoomTemplate\name <> "exit1" And mainPlayer\currRoom\RoomTemplate\name <> "gatea" And mainPlayer\currRoom\RoomTemplate\Name <> "pocketdimension" Then
							For w.waypoints = Each WayPoints
								If Rand(5)=1 Then ;w\door=Null And (TODO: fix?)
									x = Abs(EntityX(mainPlayer\collider) - EntityX(w\obj, True))
									If x < 25.0 And x > 15.0 Then
										z = Abs(EntityZ(mainPlayer\collider)-EntityZ(w\obj,True))
										If z < 25 And z > 15.0 Then
											DebugLog "MOVING 173 TO " + w\room\roomtemplate\name
											PositionEntity(n\collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True))
											ResetEntity(n\collider)
											Exit
										EndIf
									EndIf
										
								EndIf
							Next
						EndIf
					EndIf
				ElseIf dist > HideDistance*0.8 ;3-6 rooms away from the player -> move randomly from waypoint to another
					If Rand(70)=1 Then TeleportCloser(n)
				Else ;less than 3 rooms away -> actively move towards the player
					
					;try to open doors
					If Rand(20) = 1 Then
						Local d.Doors
						For d.Doors = Each Doors
							If (Not d\locked) And d\open = False And d\Code = "" And d\KeyCard=0 Then
								For i% = 0 To 1
									If d\buttons[i] <> 0 Then
										If Abs(EntityX(n\collider) - EntityX(d\buttons[i])) < 0.5 Then
											If Abs(EntityZ(n\collider) - EntityZ(d\buttons[i])) < 0.5 Then
												If (d\openstate >= 180 Or d\openstate <= 0) Then
													If DeltaYaw(n\collider, d\buttons[i]) < 60 And DeltaYaw(d\buttons[i], n\collider) < 60 Then 
														PlaySound2(LoadTempSound("SFX/Door/DoorOpen173.ogg"))
														UseDoor(d,False)
													EndIf
												EndIf
											EndIf
										EndIf
									EndIf
								Next
							EndIf
						Next
					EndIf
					
					If NoTarget
						playerVisible = False
						n\targetX = 0
						n\targetY = 0
						n\targetZ = 0
					EndIf
					
					;player is not looking and is visible from 173's position -> attack
					If playerVisible Then 				
						If dist < 0.65 Then
							If (Not mainPlayer\dead) And (Not mainPlayer\godMode) Then
								
								Select mainPlayer\currRoom\RoomTemplate\Name
									Case "lockroom", "room2closets", "coffin"
										DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. The surveillance tapes confirm that the subject was killed by SCP-173."	
									Case "173"
										DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. According to Security Chief Franklin who was present at SCP-173's containment "
										DeathMSG = DeathMSG + "chamber during the breach, the subject was killed by SCP-173 as soon as the disruptions in the electrical network started."
									Case "room2doors"
										DeathMSG = Chr(34)+"If I'm not mistaken, one of the main purposes of these rooms was to stop SCP-173 from moving further in the event of a containment breach. "
										DeathMSG = DeathMSG + "So, who's brilliant idea was it to put A GODDAMN MAN-SIZED VENTILATION DUCT in there?"+Chr(34)
									Default 
										DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. Assumed to be attacked by SCP-173."
								End Select
								
								n\state = STATE173_IDLE
								
								;Necksnap.
								PlaySound2(n\sounds[Rand(9,11)])

								;TODO: Remove?
								If Rand(2) = 1 Then 
									TurnEntity(mainPlayer\cam, 0, Rand(80,100), 0)
								Else
									TurnEntity(mainPlayer\cam, 0, Rand(-100,-80), 0)
								EndIf

								Kill(mainPlayer)
							EndIf
						Else
							PointEntity(n\collider, mainPlayer\collider)
							RotateEntity n\collider, 0, EntityYaw(n\collider), EntityRoll(n\collider)
							TranslateEntity n\collider,Cos(EntityYaw(n\collider)+90.0)*n\speed*timing\tickDuration,0.0,Sin(EntityYaw(n\collider)+90.0)*n\speed*timing\tickDuration
						EndIf
						
					Else ;player is not visible -> move to the location where he was last seen							
						If (n\targetX <> 0) Then						
							If Distance(EntityX(n\collider), EntityZ(n\collider), n\targetX, n\targetZ) > 0.5 Then
								AlignToVector(n\collider, n\targetX-EntityX(n\collider), 0, n\targetZ-EntityZ(n\collider), 3)
								MoveEntity(n\collider, 0, 0, n\speed * timing\tickDuration)
								If Rand(500) = 1 Then n\targetX = 0 : n\targetY = 0 : n\targetZ = 0
							Else
								n\targetX = 0 : n\targetY = 0 : n\targetZ = 0
							EndIf
						Else
							If Rand(400)=1 Then RotateEntity(n\collider, 0, Rnd(360), 10)
							TranslateEntity n\collider,Cos(EntityYaw(n\collider)+90.0)*n\speed*timing\tickDuration,0.0,Sin(EntityYaw(n\collider)+90.0)*n\speed*timing\tickDuration
							
						EndIf
					EndIf
					
				EndIf ; less than 2 rooms away from the player
				
				;TODO: Why random Y?
				;PositionEntity(n\Collider, EntityX(n\Collider), Min(EntityY(n\Collider),0.35), EntityZ(n\Collider))
			EndIf
		Case STATE173_MOVE_TO_TARGET
			;If 173 was given a target then use its position.
			If (n\Target <> Null) Then
				n\targetX = EntityX(n\Target\collider)
				n\targetY = EntityY(n\Target\collider)
				n\targetZ = EntityZ(n\Target\collider)
			EndIf

			If (n\targetX <> 0) Then
				If Distance(EntityX(n\collider), EntityZ(n\collider), n\targetX, n\targetZ) > 0.5 Then
					AlignToVector(n\collider, n\targetX-EntityX(n\collider), 0, n\targetZ-EntityZ(n\collider), 3)
					MoveEntity(n\collider, 0, 0, n\speed * timing\tickDuration)
				Else
					n\targetX = 0
					n\targetY = 0
					n\targetZ = 0
					n\state = STATE173_IDLE
				EndIf
			Else
				n\state = STATE173_IDLE
			EndIf
		Case STATE173_BEING_CONTAINED
			If (n\Target <> Null) Then
				Local tmp = False
				If dist > HideDistance*0.7
					If EntityVisible(n\obj,mainPlayer\collider)=False
						tmp = True
					EndIf
				EndIf
				If (Not tmp)
					PointEntity n\obj, n\Target\collider
					RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),10.0), 0, True								
					dist = EntityDistance(n\collider, n\Target\collider)
					MoveEntity n\collider, 0, 0, 0.016*timing\tickDuration*Max(Min((dist*2-1.0)*0.5,1.0),-0.5)
				Else
					PositionEntity n\collider,EntityX(n\Target\collider),EntityY(n\Target\collider)+0.3,EntityZ(n\Target\collider)
					ResetEntity n\collider
					;PointEntity n\Collider, n\Target\Collider
					;RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0, True
					;dist = EntityDistance(n\Collider, n\Target\Collider)
					;MoveEntity n\Collider, 0, 0, dist-0.6
				EndIf
			EndIf
			
			PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider) + 0.05 + Sin(TimeInPosMilliSecs()*0.08)*0.02, EntityZ(n\collider))
			RotateEntity (n\obj, 0, EntityYaw(n\collider)-180, 0)
			
			ShowEntity n\obj2
			
			PositionEntity(n\obj2, EntityX(n\collider), EntityY(n\collider) - 0.05 + Sin(TimeInPosMilliSecs()*0.08)*0.02, EntityZ(n\collider))
			RotateEntity (n\obj2, 0, EntityYaw(n\collider)-180, 0)
	End Select
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
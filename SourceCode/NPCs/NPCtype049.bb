Const STATE049_IDLE%     = 0
Const STATE049_ATTACK%   = 1
Const STATE049_ROAMING%  = 2

Function InitializeNPCtype049(n.NPCs)
    n\nvName = "SCP-049"
    n\collider = CreatePivot()
	
    EntityRadius(n\collider, 0.2)
    EntityType(n\collider, HIT_PLAYER)
	
    n\obj = LoadAnimMesh("GFX/NPCs/scp049/scp-049.b3d")
    
    n\speed = GetINIFloat("DATA/NPCs.ini", "SCP-049", "speed") / 100.0
    
    Local temp# = GetINIFloat("DATA/NPCs.ini", "SCP-049", "scale")
    ScaleEntity(n\obj, temp, temp, temp)
    
    n\sounds[0] = LoadSound("SFX/SCP/049/Catch.ogg")
	n\sounds[1] = LoadSound("SFX/SCP/049/Spotted1.ogg")
	n\sounds[2] = LoadSound("SFX/SCP/049/Spotted2.ogg")
	n\sounds[3] = LoadSound("SFX/SCP/049/Detected1.ogg")
	n\sounds[4] = LoadSound("SFX/SCP/049/Detected2.ogg")
	n\sounds[5] = LoadSound("SFX/SCP/049/Detected3.ogg")
	n\sounds[6] = LoadSound("SFX/SCP/049/Detected4.ogg")
    
    ;If HorrorSFX(13)=0 Then HorrorSFX(13)=LoadSound("SFX/Music/049Chase.ogg") ;TODO: implement
End Function

Function UpdateNPCtype049(n.NPCs)
    Local prevFrame# = n\frame
	
	If (n\state <> STATE049_IDLE) Then
		;Depending on whether 049 has detected the player, choose the state it's in.
		Local npcDetectsPlayer% = MeNPCSeesPlayer(n)
		
		If (npcDetectsPlayer = 1) Then
			n\state = STATE049_ATTACK
		Else
			n\state = STATE049_ROAMING
		EndIf
	EndIf
    
	Select n\state
		Case STATE049_IDLE
			;Idle animation.
			AnimateNPC(n, 231, 344, 0.31, True)
			
		Case STATE049_ATTACK
			;[Block]
			;Playing a sound after detecting the player
			If (n\prevState = STATE049_ROAMING And (Not IsChannelPlaying(n\soundChannels[0]))) Then
				n\soundChannels[0] = LoopRangedSound(n\sounds[Rand(1,2)], n\soundChannels[0], mainPlayer\cam, n\obj)
			EndIf
			
			n\pathStatus = 0
			n\pathTimer# = 0.0
			n\pathLocation = 0
			
			PointEntity(n\obj, mainPlayer\collider)
			RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10.0), 0)
			
			;Now I must only T O U C H...
			If (n\playerDistance < 0.5) Then
				mainPlayer\camZoom = 20.0
				mainPlayer\blurTimer = 500.0
				
				If (Not mainPlayer\godMode) Then
					If (mainPlayer\currRoom\RoomTemplate\Name$ = "room049") Then
						DeathMSG = "Three (3) active instances of SCP-049-2 discovered in the tunnel outside SCP-049's containment chamber. Terminated by Nine-Tailed Fox."
						Local e.Events
						For e.Events = Each Events
							If e\name = "room049" Then e\eventState=-1 : Exit
						Next
					Else
						DeathMSG = "An active instance of SCP-049-2 was discovered in [REDACTED]. Terminated by Nine-Tailed Fox."
						Kill(mainPlayer)
					EndIf
					
					NPCStopAllChannels(n)
					PlaySound2(n\sounds[0])
					
					n\state = STATE049_IDLE
				EndIf
			Else ; S T O P P ;chasing
				n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
				MoveEntity n\collider, 0, 0, n\currSpeed * timing\tickDuration
				
				If n\playerDistance < 3.0 Then ;extends arm
					AnimateNPC(n, Max(Min(AnimTime(n\obj),428.0),387), 463.0, n\currSpeed*38)
				Else
					If n\frame>428.0 Then
						AnimateNPC(n, Min(AnimTime(n\obj),463.0), 498.0, n\currSpeed*38,False)
						If n\frame>497.9 Then n\frame = 358
					Else
						AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\currSpeed*38)
					EndIf
				EndIf
			EndIf
			;[End Block]
			
		Case STATE049_ROAMING
			;[Block]
			Local dist2#
			
			;Finding a path to the player
			If (npcDetectsPlayer = 2) Then
				n\lastSeen = 70*15
			EndIf
			
			If n\lastSeen > 0 Then
				If n\pathStatus = 1 Then ;Path to player found
					If n\path[n\pathLocation]=Null Then
						If n\pathLocation > 19 Then
							n\pathLocation = 0 : n\pathStatus = 0
						Else
							n\pathLocation = n\pathLocation + 1
						EndIf
					Else
						;closes doors behind him
						If n\pathLocation>0 Then
							If n\path[n\pathLocation-1] <> Null Then
								;TODO: fix
								;If n\path[n\pathLocation-1]\door <> Null Then
								;	If (Not n\path[n\pathLocation-1]\door\IsElevatorDoor)
								;		If EntityDistance(n\path[n\pathLocation-1]\obj,n\collider)>0.3
								;			If n\path[n\pathLocation-1]\door\open Then UseDoor(n\path[n\pathLocation-1]\door, False)
								;		EndIf
								;	EndIf
								;EndIf
							EndIf
						EndIf
						
						n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
						PointEntity n\obj,n\path[n\pathLocation]\obj
						RotateEntity n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),10.0),0
						MoveEntity n\collider,0,0,n\currSpeed*timing\tickDuration
						
						;opens doors in front of him
						dist2# = EntityDistance(n\collider,n\path[n\pathLocation]\obj)
						If (dist2 < 0.6) Then
							Local temp% = True
							;TODO: fix
							;If (n\path[n\pathLocation]\door <> Null) Then
							;	If (Not n\path[n\pathLocation]\door\IsElevatorDoor)
							;		If n\path[n\pathLocation]\door\locked Or n\path[n\pathLocation]\door\KeyCard>0 Or n\path[n\pathLocation]\door\code<>"" Then
							;			temp = False
							;			;n\currSpeed = 0
							;		Else
							;			If n\path[n\pathLocation]\door\open = False Then UseDoor(n\path[n\pathLocation]\door, False)
							;		EndIf
							;	EndIf
							;EndIf
							If dist2#<0.2 And temp Then
								n\pathLocation = n\pathLocation + 1
							ElseIf dist2#<0.5 And (Not temp) Then
								;Breaking up the path because the door cannot be operated by SCP-049
								n\pathStatus = 0
								n\pathTimer# = 0.0
							EndIf
						EndIf
						
						AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\currSpeed*38)
						
						;Playing a sound if he hears the player
						If (n\soundTimer < 0 And (Not IsChannelPlaying(n\soundChannels[0]))) Then
							n\soundTimer = Rand(10, 20) * 70
							
							If Rand(8)=3 Then
								n\soundChannels[0] = LoopRangedSound(n\sounds[6], n\soundChannels[0], mainPlayer\cam, n\obj)
							Else
								n\soundChannels[0] = LoopRangedSound(n\sounds[Rand(3, 5)], n\soundChannels[0], mainPlayer\cam, n\obj)
							EndIf
						EndIf
						
						If (n\timer > 70*7) Then ;Updating the path every 7 seconds
							n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
							n\timer = 0.0
						EndIf
					EndIf
					
					If n\currElevator <> Null Then
						dist2# = EntityDistance(n\collider,n\currElevator\door\frameobj)
						If dist2# < 0.7 Then
							n\pathStatus = 0
							n\pathLocation = 0
							n\pathTimer# = 0.0
						EndIf
					EndIf
				Else ;No Path to the player found - stands still and tries to find a path
					If n\currElevator = Null Then
						If n\pathStatus = 2 Then ;Updating path faster
							n\pathTimer# = n\pathTimer# + 2*timing\tickDuration
						Else
							n\pathTimer# = n\pathTimer# + timing\tickDuration
						EndIf
						If n\pathTimer# > 70*5-(2*SelectedDifficulty\aggressiveNPCs) Then
							If n\InFacility = InFacility Then
								n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
							Else
								FindNextElevator(n)
							EndIf
							n\pathTimer# = 0.0
						EndIf
						AnimateNPC(n, 269, 345, 0.2)
						;n\PrevState = 0
					Else
						;n\currSpeed = CurveValue(n\Speed, n\currSpeed, 20.0)
						n\currSpeed = n\speed
						GoToElevator(n)
						
						If n\currSpeed > 0.0 Then
							MoveEntity n\collider,0,0,n\currSpeed*timing\tickDuration
							AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\currSpeed*38)
						Else
							AnimateNPC(n, 269, 345, 0.2)
						EndIf
					EndIf
				EndIf
			Else ;Player hasn't been detected, just being idle
				If n\pathStatus = 1 Then
					If n\path[n\pathLocation]=Null Then
						If n\pathLocation > 19 Then
							n\pathLocation = 0 : n\pathStatus = 0
						Else
							n\pathLocation = n\pathLocation + 1
						EndIf
					Else
						;closes doors behind him
						If n\pathLocation>0 Then
							If n\path[n\pathLocation-1] <> Null Then
								;TODO: ffs
								;If n\path[n\pathLocation-1]\door <> Null Then
								;	If (Not n\path[n\pathLocation-1]\door\IsElevatorDoor)
								;		If EntityDistance(n\path[n\pathLocation-1]\obj,n\collider)>0.3
								;			If n\path[n\pathLocation-1]\door\open Then UseDoor(n\path[n\pathLocation-1]\door, False)
								;		EndIf
								;	EndIf
								;endIf
							EndIf
						EndIf
						
						n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
						PointEntity n\obj,n\path[n\pathLocation]\obj
						RotateEntity n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),10.0),0
						MoveEntity n\collider,0,0,n\currSpeed*timing\tickDuration
						
						;opens doors in front of him
						dist2# = EntityDistance(n\collider,n\path[n\pathLocation]\obj)
						If dist2 < 0.6 Then
							temp = True
							;TODO: AAAAAHHHHH
							;If n\path[n\pathLocation]\door <> Null Then
							;	If (Not n\path[n\pathLocation]\door\IsElevatorDoor)
							;		If n\path[n\pathLocation]\door\locked Or n\path[n\pathLocation]\door\KeyCard>0 Or n\path[n\pathLocation]\door\code<>"" Then
							;			temp = False
							;			;n\currSpeed = 0
							;		Else
							;			If n\path[n\pathLocation]\door\open = False Then UseDoor(n\path[n\pathLocation]\door, False)
							;		EndIf
							;	EndIf
							;EndIf
							If dist2#<0.2 And temp Then
								n\pathLocation = n\pathLocation + 1
							ElseIf dist2#<0.5 And (Not temp) Then
								;Breaking up the path because the door cannot be operated by SCP-049
								n\pathStatus = 0
								n\pathTimer# = 0.0
							EndIf
						EndIf
						
						AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\currSpeed*38)
						
						n\timer = n\timer + timing\tickDuration
						
						If n\InFacility = True Then
							If n\state3 > 70*14 Then ;Breaking the path after 14 seconds
								n\pathStatus = 0
								n\pathLocation = 0
								n\pathTimer# = 0.0
								n\state3 = 0.0
							EndIf
						EndIf
					EndIf
					
					If n\currElevator <> Null Then
						dist2# = EntityDistance(n\collider,n\currElevator\door\frameobj)
						If dist2# < 0.7 Then
							n\pathStatus = 0
							n\pathLocation = 0
							n\pathTimer# = 0.0
						EndIf
					EndIf
				Else
					If n\currElevator = Null Then
						n\currSpeed = 0
						AnimateNPC(n, 269, 345, 0.2)
						If n\pathTimer# < 300+Rand(100,300) Then
							n\pathTimer# = n\pathTimer# + timing\tickDuration
						Else
							If n\InFacility = True Then
								If Rand(2)=1 Then
									For r.Rooms = Each Rooms
										If EntityDistance(r\obj,n\collider)<12.0 And EntityDistance(r\obj,n\collider)>4.0 Then
											n\pathStatus = FindPath(n,EntityX(r\obj),EntityY(r\obj),EntityZ(r\obj))
											Exit
										EndIf
									Next
								EndIf
							Else
								FindNextElevator(n)
							EndIf
							n\pathTimer# = 0.0
						EndIf
					Else
						;n\currSpeed = CurveValue(n\Speed, n\currSpeed, 20.0)
						n\currSpeed = n\speed
						GoToElevator(n)
						
						If n\currSpeed > 0.0 Then
							MoveEntity(n\collider, 0, 0, n\currSpeed*timing\tickDuration)
							AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\currSpeed*38)
						Else
							AnimateNPC(n, 269, 345, 0.2)
						EndIf
					EndIf
				EndIf
			EndIf
			;[End Block]
	End Select
    
    PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider)-0.22, EntityZ(n\collider))
    
    RotateEntity n\obj, 0, EntityYaw(n\collider), 0
    
    n\lastSeen = Max(n\lastSeen-timing\tickDuration,0)
	
	If (n\soundTimer >= 0) Then
		n\soundTimer = n\soundTimer - timing\tickDuration
	EndIf
	
	n\prevState = n\state
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
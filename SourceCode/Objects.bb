Function CreateButton%(x#,y#,z#, pitch#,yaw#,roll#=0)
	Local buttonObj% = GrabMesh("GFX/Map/Meshes/button.b3d")
	Local obj% = CopyMesh(buttonObj)
	DropAsset(buttonObj)

	ScaleEntity(obj, 0.03, 0.03, 0.03)

	PositionEntity(obj, x,y,z)
	RotateEntity(obj, pitch,yaw,roll)

	EntityPickMode(obj, 2)

	Return obj
End Function

Function UpdateButton(obj%)
	Local dist# = EntityDistance(mainPlayer\collider, obj);entityDistance(collider, d\buttons[i])
	Local temp%
	
	If (dist < 0.8) Then
		temp = CreatePivot()
		PositionEntity(temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam))
		PointEntity(temp,obj)

		If (EntityPick(temp, 0.65) = obj) Then
			If (mainPlayer\closestButton = 0) Then
				mainPlayer\closestButton = obj
			Else
				If (dist < EntityDistance(mainPlayer\collider, mainPlayer\closestButton)) Then mainPlayer\closestButton = obj
			EndIf
		EndIf

		FreeEntity(temp)
	EndIf
End Function

;-------------------------------------------------------------------------------------------------------

Type Lever
    Field succ% = False
	Field locked% = False
    Field obj%
    Field baseObj%
End Type

Function CreateLever.Lever()
    Local leverObj% = GrabMesh("GFX/Map/Meshes/leverhandle.b3d")
    Local leverBaseObj% = GrabMesh("GFX/Map/Meshes/leverbase.b3d")

    Local lever.Lever = New Lever
    lever\obj = CopyEntity(leverObj)
    lever\baseObj = CopyEntity(leverBaseObj)
    DropAsset(leverObj)
    DropAsset(leverBaseObj)

    Return lever
End Function

; TODO: Call this somewhere.
Function UpdateLevers()
	Local lever.Lever, dist#, prevpitch#

	For lever = Each Lever
		dist = EntityDistance(mainPlayer\cam, lever\obj)

		If (dist < 8.0) Then
			If (dist < 0.8 And (Not lever\locked)) Then
				If (EntityInView(lever\obj, mainPlayer\cam)) Then

					EntityPick(mainPlayer\cam, 0.65)

					If ((PickedEntity() = lever\obj)) Then
						DrawHandIcon = True
						If (MouseHit1) Then mainPlayer\grabbedEntity = lever\obj
					EndIf

					prevpitch = EntityPitch(lever\obj)

					If ((MouseDown1 Or MouseHit1)) Then
						If (mainPlayer\grabbedEntity <> 0) Then
							If (mainPlayer\grabbedEntity = lever\obj) Then
								DrawHandIcon = True
								;TurnEntity(lever\obj, , 0, 0)
								RotateEntity(mainPlayer\grabbedEntity, Max(Min(EntityPitch(lever\obj)+Max(Min(mouse_y_speed_1 * 8,30.0),-30), 80), -80), EntityYaw(lever\obj), 0)

								DrawArrowIcon(0) = True
								DrawArrowIcon(2) = True

							EndIf
						EndIf
					EndIf

					If (EntityPitch(lever\obj,True) > 75) Then
						If (prevpitch =< 75) Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, lever\obj, 1.0)
					ElseIf (EntityPitch(lever\obj,True) < -75) Then
						If (prevpitch => -75) Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, lever\obj, 1.0)
					EndIf
				EndIf
			EndIf

			If (MouseDown1=False And MouseHit1=False) Then
				If (EntityPitch(lever\obj,True) > 0) Then
					RotateEntity(lever\obj, CurveValue(80, EntityPitch(lever\obj), 10), EntityYaw(lever\obj), 0)
				Else
					RotateEntity(lever\obj, CurveValue(-80, EntityPitch(lever\obj), 10), EntityYaw(lever\obj), 0)
				EndIf
				mainPlayer\grabbedEntity = 0
			EndIf

		EndIf

		If ((EntityPitch(lever\obj, True) > 0)) Then
			lever\succ = True
		Else
			lever\succ = False
		EndIf
	Next
End Function

;-------------------------------------------------------------------------------------------------------

;TODO: rewrite elevator code, use only one function
Function UpdateElevators#(state#, door1.Doors, door2.Doors, room1%, room2%, event.Events)
	Local x#, z#, n.NPCs, NPC_inside.NPCs

	door1\isElevatorDoor = 1
	door2\isElevatorDoor = 1
	If (door1\open = True And door2\open = False) Then
		state = -1
		If ((mainPlayer\closestButton = door2\buttons[0] Or mainPlayer\closestButton = door2\buttons[1]) And MouseHit1) Then
			UseDoor(door1,False)
		EndIf
		If (door2\npcCalledElevator = True) Then
			UseDoor(door1,False)
			door2\npcCalledElevator = 2
		EndIf
	ElseIf (door2\open = True And door1\open = False) Then
		state = 1
		If ((mainPlayer\closestButton = door1\buttons[0] Or mainPlayer\closestButton = door1\buttons[1]) And MouseHit1) Then
			UseDoor(door2,False)
		EndIf
		If (door1\npcCalledElevator = True) Then
			UseDoor(door2,False)
			door1\npcCalledElevator = 2
		EndIf
	ElseIf (Abs(door1\openstate-door2\openstate)<0.2) Then
		door1\isElevatorDoor = 2
		door2\isElevatorDoor = 2
	EndIf

	Local inside% = False
	NPC_inside = Null

	;molemmat ovet kiinni = hissi liikkuu
	If (door1\open = False And door2\open = False) Then
		door1\locked = True
		door2\locked = True
		door1\npcCalledElevator = 2
		door2\npcCalledElevator = 2
		If (state < 0) Then ;ylh��lt� alas
			state = state - timing\tickDuration
			;pelaaja hissin sis�ll�
			If (Abs(EntityX(mainPlayer\collider)-EntityX(room1,True))<280.0*RoomScale) Then
				If (Abs(EntityZ(mainPlayer\collider)-EntityZ(room1,True))<280.0*RoomScale) Then
					If (Abs(EntityY(mainPlayer\collider)-EntityY(room1,True))<280.0*RoomScale) Then
						inside = True

						If ((Not IsChannelPlaying(event\soundChannels[0]))) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf

						mainPlayer\camShake = Sin(Abs(state)/3.0)*0.3
					EndIf
				EndIf
			EndIf

			For n = Each NPCs
				If (n\canUseElevator) Then
					If (Abs(EntityX(n\collider)-EntityX(room1,True))<280.0*RoomScale) Then
						If (Abs(EntityZ(n\collider)-EntityZ(room1,True))<280.0*RoomScale) Then
							If (Abs(EntityY(n\collider)-EntityY(room1,True))<280.0*RoomScale) Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next

			If (NPC_inside <> Null And (Not inside)) Then
				NPC_inside\idle = True
			ElseIf (NPC_inside <> Null And inside) Then
				NPC_inside\idle = False
			EndIf

			If (state < -400) Then
				door1\locked = False
				door2\locked = False
				door1\npcCalledElevator = False
				door2\npcCalledElevator = False
				state = 0

				If (inside) Then
					x = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(mainPlayer\collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(mainPlayer\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity(mainPlayer\collider)
					UpdateDoorsTimer = 0
					mainPlayer\dropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf

				If (NPC_inside <> Null) Then
					x = Max(Min((EntityX(NPC_inside\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min((EntityZ(NPC_inside\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(NPC_inside\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity(NPC_inside\collider)
					UpdateDoorsTimer = 0
					NPC_inside\dropSpeed = 0
					If (Int(NPC_inside\idle)) Then
						TurnEntity(NPC_inside\obj,0,180,0)
						TurnEntity(NPC_inside\collider,0,180,0)
						NPC_inside\idle = False
					EndIf
					NPC_inside\currElevator = Null
				EndIf

				UseDoor(door2,False)

				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room2, 4.0)
			EndIf
		Else ;alhaalta yl�s
			state = state + timing\tickDuration
			;pelaaja hissin sis�ll�
			If (Abs(EntityX(mainPlayer\collider)-EntityX(room2,True))<280.0*RoomScale) Then
				If (Abs(EntityZ(mainPlayer\collider)-EntityZ(room2,True))<280.0*RoomScale) Then
					If (Abs(EntityY(mainPlayer\collider)-EntityY(room2,True))<280.0*RoomScale) Then
						inside = True

						If ((Not IsChannelPlaying(event\soundChannels[0]))) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf

						mainPlayer\camShake = Sin(Abs(state)/3.0)*0.3
					EndIf
				EndIf
			EndIf

			For n = Each NPCs
				If (n\canUseElevator) Then
					If (Abs(EntityX(n\collider)-EntityX(room2,True))<280.0*RoomScale) Then
						If (Abs(EntityZ(n\collider)-EntityZ(room2,True))<280.0*RoomScale) Then
							If (Abs(EntityY(n\collider)-EntityY(room2,True))<280.0*RoomScale) Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next

			If (NPC_inside <> Null And (Not inside)) Then
				NPC_inside\idle = True
			ElseIf (NPC_inside <> Null And inside) Then
				NPC_inside\idle = False
			EndIf

			If (state > 400) Then
				door1\locked = False
				door2\locked = False
				door1\npcCalledElevator = False
				door2\npcCalledElevator = False
				state = 0

				;pelaaja hissin sis�ll�, siirret��n
				If (inside) Then
					x = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(mainPlayer\collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(mainPlayer\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity(mainPlayer\collider)
					UpdateDoorsTimer = 0
					mainPlayer\dropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf

				If (NPC_inside <> Null) Then
					x = Max(Min((EntityX(NPC_inside\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min((EntityZ(NPC_inside\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(NPC_inside\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity(NPC_inside\collider)
					UpdateDoorsTimer = 0
					NPC_inside\dropSpeed = 0
					If (Int(NPC_inside\idle)) Then
						TurnEntity(NPC_inside\obj,0,180,0)
						TurnEntity(NPC_inside\collider,0,180,0)
						NPC_inside\idle = False
					EndIf
					NPC_inside\currElevator = Null
				EndIf

				UseDoor(door1,False)

				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room1, 4.0)
			EndIf

		EndIf
	EndIf

	Return state

End Function

Function UpdateElevators2#(State#, door1.Doors, door2.Doors, room1%, room2%, event.Events)
	Local x#, z#

	door1\isElevatorDoor = 1
	door2\isElevatorDoor = 1
	If (door1\open = True And door2\open = False) Then
		State = -1
		door2\isElevatorDoor = 2
		If ((mainPlayer\closestButton = door2\buttons[0] Or mainPlayer\closestButton = door2\buttons[1]) And MouseHit1) Then
			UseDoor(door1,False)
		EndIf
	ElseIf (door2\open = True And door1\open = False) Then
		State = 1
		door1\isElevatorDoor = 2
		If ((mainPlayer\closestButton = door1\buttons[0] Or mainPlayer\closestButton = door1\buttons[1]) And MouseHit1) Then
			UseDoor(door2,False)
		EndIf
	ElseIf (Abs(door1\openstate-door2\openstate)<0.2) Then
		door1\isElevatorDoor = 2
		door2\isElevatorDoor = 2
	EndIf

	Local inside% = False
	Local dist#, dir#

	;molemmat ovet kiinni = hissi liikkuu
	If (door1\open = False And door2\open = False) Then
		door1\locked = True
		door2\locked = True
		If (State < 0) Then ;ylh��lt?alas
			State = State - timing\tickDuration
			;pelaaja hissin sis�ll?
			If (Abs(EntityX(mainPlayer\collider)-EntityX(room1,True))<280.0*RoomScale) Then
				If (Abs(EntityZ(mainPlayer\collider)-EntityZ(room1,True))<280.0*RoomScale) Then
					If (Abs(EntityY(mainPlayer\collider)-EntityY(room1,True))<280.0*RoomScale) Then
						inside = True

						If ((Not IsChannelPlaying(event\soundChannels[0]))) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf

						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf

			If (State < -400) Then
				door1\locked = False
				door2\locked = False
				State = 0

				UseDoor(door2,True)

				If (inside) Then

					dist = Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room1,True),EntityZ(room1,True))

					dir = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room1,True),EntityZ(room1,True))
					dir=dir+EntityYaw(room2,True)-EntityYaw(room1,True);EntityYaw(room2,True)+angleDist(dir,EntityYaw(room1,True))
					;dir=dir-90.0

;					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room1,True)+EntityYaw(room2,True)

					dir=WrapAngle(dir)

					x = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)

					;x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)

					RotateEntity(mainPlayer\collider,EntityPitch(mainPlayer\collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(mainPlayer\collider,True),EntityYaw(room1,True)),EntityRoll(mainPlayer\collider,True),True) ;dir

					PositionEntity(mainPlayer\collider, EntityX(room2,True)+x,0.05+EntityY(room2,True)+(EntityY(mainPlayer\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)

					ResetEntity(mainPlayer\collider)
					UpdateDoors()
					UpdateRooms()
				EndIf

				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room2, 4.0)
				;PlaySound(ElevatorBeepSFX)
			EndIf
		Else ;alhaalta yl�s
			State = State + timing\tickDuration
			;pelaaja hissin sis�ll?
			If (Abs(EntityX(mainPlayer\collider)-EntityX(room2,True))<280.0*RoomScale) Then
				If (Abs(EntityZ(mainPlayer\collider)-EntityZ(room2,True))<280.0*RoomScale) Then
					If (Abs(EntityY(mainPlayer\collider)-EntityY(room2,True))<280.0*RoomScale) Then
						inside = True

						If ((Not IsChannelPlaying(event\soundChannels[0]))) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf

						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf

			If (State > 400) Then
				door1\locked = False
				door2\locked = False
				State = 0

				UseDoor(door1,True)

				;pelaaja hissin sis�ll? siirret��n
				If (inside) Then

					dist = Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room2,True),EntityZ(room2,True))

					dir = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room2,True),EntityZ(room2,True))
					dir=dir+EntityYaw(room1,True)-EntityYaw(room2,True) ;EntityYaw(room1,True)+angleDist(dir,EntityYaw(room2,True))
					;dir=dir-90.0

;					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room2,True)+EntityYaw(room1,True)

					;dir=WrapAngle(dir)

					x = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)

					;x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)

					RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(mainPlayer\collider,True),EntityYaw(room1,True)),EntityRoll(mainPlayer\collider,True),True) ;dir

					PositionEntity(mainPlayer\collider, EntityX(room1,True)+x,0.05+EntityY(room1,True)+(EntityY(mainPlayer\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)

					ResetEntity(mainPlayer\collider)
					UpdateDoors()
					UpdateRooms()
				EndIf

				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room1, 4.0)
			EndIf

		EndIf
	EndIf

	Return State

End Function

Type ElevatorObj
	Field obj%
	Field inFacility%
	Field door.Doors
End Type

Function AssignElevatorObj.ElevatorObj(obj%,door.Doors,in_facility%)
	Local eo.ElevatorObj = New ElevatorObj

	eo\obj = obj
	eo\door = door
	eo\inFacility = in_facility

	Return eo
End Function

Function DeleteElevatorObjects()

	Delete Each ElevatorObj

End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
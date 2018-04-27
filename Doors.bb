
Global ClosestButton.MarkedForRemoval, ClosestDoor.MarkedForRemoval
Global SelectedDoor.MarkedForRemoval, UpdateDoorsTimer#
Global DoorTempID%
Type Doors
	Field obj%, obj2%, frameobj%, buttons%[2]
	Field locked%, open%, angle%, openstate#, fastopen%
	Field dir%
	Field timer%, timerstate#
	Field KeyCard%
	Field room.Rooms
	
	Field DisableWaypoint%
	
	Field dist#
	
	Field SoundCHN%
	
	Field Code$
	
	Field ID%
	
	Field Level%
	Field LevelDest%
	
	Field AutoClose%
	
	Field LinkedDoor.Doors
	
	Field IsElevatorDoor% = False
	
	Field MTFClose% = True
	;TODO: Not needed?
	Field NPCCalledElevator% = False
End Type 

Dim BigDoorOBJ(2), HeavyDoorObj(2)

Function CreateDoor.Doors(lvl, x#, y#, z#, angle#, room.Rooms, dopen% = False,  big% = False, keycard% = False, code$="")
	Local d.Doors, parent, i%
	If room <> Null Then parent = room\obj
	
	d.Doors = New Doors
	If big=1 Then
		d\obj = CopyEntity(BigDoorOBJ(0))
		ScaleEntity(d\obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		d\obj2 = CopyEntity(BigDoorOBJ(1))
		ScaleEntity(d\obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		
		d\frameobj = CopyEntity(DoorColl)	;CopyMesh				
		ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
		EntityType d\frameobj, HIT_MAP
		EntityAlpha d\frameobj, 0.0
	ElseIf big=2
		d\obj = CopyEntity(HeavyDoorObj(0))
		ScaleEntity(d\obj, RoomScale, RoomScale, RoomScale)
		d\obj2 = CopyEntity(HeavyDoorObj(1))
		ScaleEntity(d\obj2, RoomScale, RoomScale, RoomScale)
		
		d\frameobj = CopyEntity(DoorFrameOBJ)
	Else
		d\obj = CopyEntity(DoorOBJ)
		ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		
		d\frameobj = CopyEntity(DoorFrameOBJ)
		d\obj2 = CopyEntity(DoorOBJ)
		
		ScaleEntity(d\obj2, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		;entityType d\obj2, HIT_MAP
	End If
	
	;scaleentity(d\obj, 0.1, 0.1, 0.1)
	PositionEntity d\frameobj, x, y, z	
	ScaleEntity(d\frameobj, (8.0 / 2048.0), (8.0 / 2048.0), (8.0 / 2048.0))
	EntityType d\obj, HIT_MAP
	EntityType d\obj2, HIT_MAP
	
	d\ID = DoorTempID
	DoorTempID=DoorTempID+1
	
	d\KeyCard = keycard
	d\Code = code
	
	d\Level = lvl
	d\LevelDest = 66
	
	For i% = 0 To 1
		If code <> "" Then 
			d\buttons[i]= CopyEntity(ButtonCodeOBJ)
			EntityFX(d\buttons[i], 1)
		Else
			If keycard>0 Then
				d\buttons[i]= CopyEntity(ButtonKeyOBJ)
			ElseIf keycard<0
				d\buttons[i]= CopyEntity(ButtonScannerOBJ)	
			Else
				d\buttons[i] = CopyEntity(ButtonOBJ)
			End If
		EndIf
		
		ScaleEntity(d\buttons[i], 0.03, 0.03, 0.03)
	Next
	
	If big=1 Then
		PositionEntity d\buttons[0], x - 432.0 * RoomScale, y + 0.7, z + 192.0 * RoomScale
		PositionEntity d\buttons[1], x + 432.0 * RoomScale, y + 0.7, z - 192.0 * RoomScale
		RotateEntity d\buttons[0], 0, 90, 0
		RotateEntity d\buttons[1], 0, 270, 0
	Else
		PositionEntity d\buttons[0], x + 0.6, y + 0.7, z - 0.1
		PositionEntity d\buttons[1], x - 0.6, y + 0.7, z + 0.1
		RotateEntity d\buttons[1], 0, 180, 0		
	End If
	EntityParent(d\buttons[0], d\frameobj)
	EntityParent(d\buttons[1], d\frameobj)
	EntityPickMode(d\buttons[0], 2)
	EntityPickMode(d\buttons[1], 2)
	
	PositionEntity d\obj, x, y, z
	
	RotateEntity d\obj, 0, angle, 0
	RotateEntity d\frameobj, 0, angle, 0
	
	If d\obj2 <> 0 Then
		PositionEntity d\obj2, x, y, z
		If big=1 Then
			RotateEntity(d\obj2, 0, angle, 0)
		Else
			RotateEntity(d\obj2, 0, angle + 180, 0)
		EndIf
		EntityParent(d\obj2, parent)
	EndIf
	
	EntityParent(d\frameobj, parent)
	EntityParent(d\obj, parent)
	
	d\angle = angle
	d\open = dopen		
	
	EntityPickMode(d\obj, 3)
	MakeCollBox(d\obj)
	If d\obj2 <> 0 Then
		EntityPickMode(d\obj2, 3)
		MakeCollBox(d\obj2)
	End If
	
	EntityPickMode d\frameobj,2
	
	If d\open And big = False And Rand(8) = 1 Then d\AutoClose = True
	d\dir=big
	d\room=room
	
	d\MTFClose = True
	
	Return d
	
End Function

Function CreateButton(x#,y#,z#, pitch#,yaw#,roll#=0)
	Local obj = CopyEntity(ButtonOBJ)	
	
	ScaleEntity(obj, 0.03, 0.03, 0.03)
	
	PositionEntity obj, x,y,z
	RotateEntity obj, pitch,yaw,roll
	
	EntityPickMode(obj, 2)	
	
	Return obj
End Function

Function UpdateDoors()
	
	Local i%, d.Doors, x#, z#, dist#
	If UpdateDoorsTimer =< 0 Then
		For d.Doors = Each Doors
			Local xdist# = Abs(EntityX(mainPlayer\collider)-EntityX(d\obj,True))
			Local zdist# = Abs(EntityZ(mainPlayer\collider)-EntityZ(d\obj,True))
			
			d\dist = xdist+zdist
			
			If d\dist > HideDistance*2 Then
				If d\obj <> 0 Then HideEntity d\obj
				If d\frameobj <> 0 Then HideEntity d\frameobj
				If d\obj2 <> 0 Then HideEntity d\obj2
				If d\buttons[0] <> 0 Then HideEntity d\buttons[0]
				If d\buttons[1] <> 0 Then HideEntity d\buttons[1]				
			Else
				If d\obj <> 0 Then ShowEntity d\obj
				If d\frameobj <> 0 Then ShowEntity d\frameobj
				If d\obj2 <> 0 Then ShowEntity d\obj2
				If d\buttons[0] <> 0 Then ShowEntity d\buttons[0]
				If d\buttons[1] <> 0 Then ShowEntity d\buttons[1]
			EndIf
			
			;TODO: this is cancer
			If mainPlayer\currRoom\RoomTemplate\Name$ = "room2sl"
				If ValidRoom2slCamRoom(d\room)
					If d\obj <> 0 Then ShowEntity d\obj
					If d\frameobj <> 0 Then ShowEntity d\frameobj
					If d\obj2 <> 0 Then ShowEntity d\obj2
					If d\buttons[0] <> 0 Then ShowEntity d\buttons[0]
					If d\buttons[1] <> 0 Then ShowEntity d\buttons[1]
				EndIf
			EndIf
		Next
		
		UpdateDoorsTimer = 30
	Else
		UpdateDoorsTimer = Max(UpdateDoorsTimer-timing\tickDuration,0)
	EndIf
	
	mainPlayer\closestButton = 0
	mainPlayer\closestDoor = Null
	
	For d.Doors = Each Doors
		If d\dist < HideDistance*2 Then 
			
			If (d\openstate >= 180 Or d\openstate <= 0) And mainPlayer\grabbedEntity = 0 Then
				For i% = 0 To 1
					If d\buttons[i] <> 0 Then
						If Abs(EntityX(mainPlayer\collider)-EntityX(d\buttons[i],True)) < 1.0 Then 
							If Abs(EntityZ(mainPlayer\collider)-EntityZ(d\buttons[i],True)) < 1.0 Then 
								dist# = Distance(EntityX(mainPlayer\collider, True), EntityZ(mainPlayer\collider, True), EntityX(d\buttons[i], True), EntityZ(d\buttons[i], True));entityDistance(collider, d\buttons[i])
								If dist < 0.7 Then
									;TODO: use deltayaw as faster way to determine whether the player can press the button or not
									Local temp% = CreatePivot()
									PositionEntity temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
									PointEntity temp,d\buttons[i]
									
									If EntityPick(temp, 0.6) = d\buttons[i] Then
										If mainPlayer\closestButton = 0 Then
											mainPlayer\closestButton = d\buttons[i]
											mainPlayer\closestDoor = d
										Else
											If dist < EntityDistance(mainPlayer\collider, mainPlayer\closestButton) Then mainPlayer\closestButton = d\buttons[i] : mainPlayer\closestDoor = d
										End If							
									End If
									
									FreeEntity temp
									
								EndIf							
							EndIf
						EndIf
						
					EndIf
				Next
			EndIf
			
			If d\open Then
				If d\openstate < 180 Then
					Select d\dir
						Case 0
							d\openstate = Min(180, d\openstate + timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen*2+1) * timing\tickDuration / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen+1) * timing\tickDuration / 80.0, 0, 0)		
						Case 1
							d\openstate = Min(180, d\openstate + timing\tickDuration * 0.8)
							MoveEntity(d\obj, Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, -Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
						Case 2
							d\openstate = Min(180, d\openstate + timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen+1) * timing\tickDuration / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen*2+1) * timing\tickDuration / 120.0, 0, 0)		
					End Select
				Else
					d\fastopen = 0
					ResetEntity(d\obj)
					If d\obj2 <> 0 Then ResetEntity(d\obj2)
					If d\timerstate > 0 Then
						d\timerstate = Max(0, d\timerstate - timing\tickDuration)
						If d\timerstate + timing\tickDuration > 110 And d\timerstate <= 110 Then PlayRangedSound(CautionSFX, mainPlayer\cam, d\obj)
						;If d\timerstate = 0 Then d\open = (Not d\open) : PlayRangedSound(CloseDoorSFX(Min(d\dir,1),Rand(0, 2)), mainPlayer\cam, d\obj)
						Local sound%
						If d\dir = 1 Then sound% = Rand(0, 1) Else sound% = Rand(0, 2)
						If d\timerstate = 0 Then d\open = (Not d\open) : PlayRangedSound(CloseDoorSFX(d\dir,sound%), mainPlayer\cam, d\obj)
					EndIf
					If d\AutoClose And RemoteDoorOn = True Then
						If EntityDistance(mainPlayer\cam, d\obj) < 2.1 Then
							;PlaySound HorrorSFX(7) ;TODO: fix
							d\open = False : PlayRangedSound(CloseDoorSFX(Min(d\dir,1), Rand(0, 2)), mainPlayer\cam, d\obj) : d\AutoClose = False
						EndIf
					End If				
				End If
			Else
				If d\openstate > 0 Then
					Select d\dir
						Case 0
							d\openstate = Max(0, d\openstate - timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration * (d\fastopen+1) / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -timing\tickDuration / 80.0, 0, 0)	
						Case 1
							d\openstate = Max(0, d\openstate - timing\tickDuration*0.8)
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
							If d\openstate < 15 And d\openstate+timing\tickDuration => 15
								For i = 0 To Rand(75,99)
									Local pvt% = CreatePivot()
									PositionEntity(pvt, EntityX(d\frameobj,True)+Rnd(-0.2,0.2), EntityY(d\frameobj,True)+Rnd(0.0,1.2), EntityZ(d\frameobj,True)+Rnd(-0.2,0.2))
									RotateEntity(pvt, 0, Rnd(360), 0)
									
									Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
									p\speed = 0.005
									RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
									
									p\SizeChange = -0.00001
									p\size = 0.01
									ScaleSprite p\obj,p\size,p\size
									
									p\Achange = -0.01
									
									EntityOrder p\obj,-1
									
									FreeEntity pvt
								Next
							EndIf
						Case 2
							d\openstate = Max(0, d\openstate - timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration * (d\fastopen+1) / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -timing\tickDuration / 120.0, 0, 0)
					End Select
					
					If d\angle = 0 Or d\angle=180 Then
						If Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.15 Then
							If Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.7*(d\dir*2+1) Then
								z# = CurveValue(EntityZ(d\frameobj,True)+0.15*Sgn(EntityZ(mainPlayer\collider)-EntityZ(d\frameobj, True)), EntityZ(mainPlayer\collider), 5)
								PositionEntity mainPlayer\collider, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), z
							EndIf
						EndIf
					Else
						If Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.15 Then	
							If Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.7*(d\dir*2+1) Then
								x# = CurveValue(EntityX(d\frameobj,True)+0.15*Sgn(EntityX(mainPlayer\collider)-EntityX(d\frameobj, True)), EntityX(mainPlayer\collider), 5)
								PositionEntity mainPlayer\collider, x, EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider)
							EndIf
						EndIf
					EndIf
					
				Else
					d\fastopen = 0
					PositionEntity(d\obj, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 Then PositionEntity(d\obj2, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 And d\dir = 0 Then
						MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
						MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
					EndIf	
				End If
			End If
			
		EndIf
		
	Next
End Function

Function UseDoor(d.Doors, showmsg%=True)
	Local temp% = 0
	If d\KeyCard > 0 Then
		If mainPlayer\selectedItem = Null Then
			If showmsg = True Then 
				Msg = "A keycard is required to operate this door."
				MsgTimer = 70 * 5
			EndIf
			Return
		Else
			Select mainPlayer\selectedItem\itemtemplate\tempname
				Case "key1"
					temp = 1
				Case "key2"
					temp = 2
				Case "key3"
					temp = 3
				Case "key4"
					temp = 4
				Case "key5"
					temp = 5
				Default 
					temp = -1
			End Select
			
			If temp =-1 Then 
				If showmsg = True Then 
					Msg = "A keycard is required to operate this door."
					MsgTimer = 70 * 5
				EndIf
				Return				
			ElseIf temp >= d\KeyCard 
				mainPlayer\selectedItem = Null
				If showmsg = True Then
					If d\locked Then
						PlaySound KeyCardSFX2
						Msg = "The keycard was inserted into the slot but nothing happened."
						MsgTimer = 70 * 5
						Return
					Else
						PlaySound KeyCardSFX1
						Msg = "The keycard was inserted into the slot."
						MsgTimer = 70 * 5		
					EndIf
				EndIf
			Else
				mainPlayer\selectedItem = Null
				If showmsg = True Then 
					PlaySound KeyCardSFX2					
					If d\locked Then
						Msg = "The keycard was inserted into the slot but nothing happened."
					Else
						Msg = "A keycard with a higher security clearance is required to operate this door."
					EndIf
					MsgTimer = 70 * 5							
				EndIf
				Return
			End If
		EndIf	
	ElseIf d\KeyCard < 0
		;I can't find any way to produce short circuited boolean expressions so work around this by using a temporary variable - risingstar64
		If mainPlayer\selectedItem <> Null Then
			temp = (mainPlayer\selectedItem\itemtemplate\tempname = "hand" And d\KeyCard=-1) Or (mainPlayer\selectedItem\itemtemplate\tempname = "hand2" And d\KeyCard=-2)
		EndIf
		If temp <> 0 Then
			PlaySound ScannerSFX1
			Msg = "You place the palm of the hand onto the scanner. The scanner reads: "+Chr(34)+"DNA verified. Access granted."+Chr(34)
			MsgTimer = 70 * 10
		Else
			If showmsg = True Then 
				PlaySound ScannerSFX2
				Msg = "You placed your palm onto the scanner. The scanner reads: "+Chr(34)+"DNA does not match known sample. Access denied."+Chr(34)
				MsgTimer = 70 * 10
			EndIf
			Return			
		EndIf
	Else
		If d\locked Then
			If showmsg = True Then 
				If Not (d\IsElevatorDoor>0) Then
					PlaySound ButtonSFX2
					Msg = "The door appears to be locked."
					MsgTimer = 70 * 5
				Else
					If d\IsElevatorDoor = 1 Then
						Msg = "You called the elevator."
						MsgTimer = 70 * 5
					ElseIf (Msg<>"You called the elevator.")
						If (Msg="You already called the elevator.") Or (MsgTimer<70*3)	
							Select Rand(10)
								Case 1
									Msg = "Stop spamming the button."
									MsgTimer = 70 * 7
								Case 2
									Msg = "Pressing it harder does not make the elevator come faster."
									MsgTimer = 70 * 7
								Case 3
									Msg = "If you continue pressing this button I will generate a Memory Access Violation."
									MsgTimer = 70 * 7
								Default
									Msg = "You already called the elevator."
									MsgTimer = 70 * 7
							End Select
						EndIf
					Else
						Msg = "You already called the elevator."
						MsgTimer = 70 * 7
					EndIf
				EndIf
				
			EndIf
			Return
		EndIf	
	EndIf
	
	d\open = (Not d\open)
	If d\LinkedDoor <> Null Then d\LinkedDoor\open = (Not d\LinkedDoor\open)
	
	Local sound = 0
	;If d\dir = 1 Then sound = 0 Else sound=Rand(0, 2)
	If d\dir = 1 Then sound=Rand(0, 1) Else sound=Rand(0, 2)
	
	If d\open Then
		If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
		d\timerstate = d\timer
		d\SoundCHN = PlayRangedSound (OpenDoorSFX(d\dir, sound), mainPlayer\cam, d\obj)
	Else
		d\SoundCHN = PlayRangedSound (CloseDoorSFX(d\dir, sound), mainPlayer\cam, d\obj)
	End If
	
	
End Function

Function RemoveDoor(d.Doors)
	If d\buttons[0] <> 0 Then EntityParent d\buttons[0], 0
	If d\buttons[1] <> 0 Then EntityParent d\buttons[1], 0	
	
	If d\obj <> 0 Then FreeEntity d\obj
	If d\obj2 <> 0 Then FreeEntity d\obj2
	If d\frameobj <> 0 Then FreeEntity d\frameobj
	If d\buttons[0] <> 0 Then FreeEntity d\buttons[0]
	If d\buttons[1] <> 0 Then FreeEntity d\buttons[1]	
	
	Delete d
End Function

Global ClosestButton.MarkedForRemoval, ClosestDoor.MarkedForRemoval
Global SelectedDoor.MarkedForRemoval, UpdateDoorsTimer#
Global DoorTempID%
Type Door
	Field obj%, obj2%, frameobj%, buttons%[2]
	Field locked%, open%, angle#, openstate#, fastopen%
	Field type%
	Field timer%, timerstate#
	Field room.Room

	Field dist#

	Field code$

	Field tag%
	Field id%

	Field autoClose%

	Field linkedDoor.Door

	Field isElevatorDoor% = False

	Field mtfClose% = True
	;TODO: Not needed?
	Field npcCalledElevator% = False
End Type

Dim BigDoorOBJ.MarkedForRemoval(2) ;yo yeye alright
Dim HeavyDoorObj.MarkedForRemoval(2)

Const DOOR_TYPE_DEF% = 0
Const DOOR_TYPE_HCZ% = 1
Const DOOR_TYPE_CONT% = 2
Function CreateDoor.Door(x#, y#, z#, angle#, room.Room, open% = False,  doorType% = DOOR_TYPE_DEF, tag$ = "" code$ = "")
	Local d.Door, parent%, i%
	If (room <> Null) Then parent = room\obj

	Local doorObj%      = GrabMesh("GFX/Map/Meshes/door.b3d")
	Local doorFrameObj% = GrabMesh("GFX/Map/Meshes/doorframe.b3d")
	Local doorColl%     = GrabMesh("GFX/Map/Meshes/doorcoll.b3d")
	Local buttonObj%    = GrabMesh("GFX/Map/Meshes/button.b3d")

	Local contDoorLeft% = GrabMesh("GFX/Map/Meshes/ContDoorLeft.b3d")
	Local contDoorRight% = GrabMesh("GFX/Map/Meshes/ContDoorRight.b3d")

	Local hczDoorObj%[2]
	For i=0 To 1
		hczDoorObj[i] = GrabMesh("GFX/Map/Meshes/heavydoor" + Str(i + 1) + ".b3d")
	Next

	Local buttonCodeObj%
	Local buttonKeyObj%
	Local buttonScannerOBJ%

	d = New Door
	d\type = doorType
	Select (d\type)
		Case DOOR_TYPE_CONT
			d\obj = CopyEntity(contDoorLeft)
			ScaleEntity(d\obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
			d\obj2 = CopyEntity(contDoorRight)
			ScaleEntity(d\obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)

			d\frameobj = CopyEntity(doorColl)
			ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
			EntityType(d\frameobj, HIT_MAP)
			EntityAlpha(d\frameobj, 0.0)
		Case DOOR_TYPE_HCZ
			d\obj = CopyEntity(hczDoorObj[0])
			ScaleEntity(d\obj, RoomScale, RoomScale, RoomScale)
			d\obj2 = CopyEntity(hczDoorObj[1])
			ScaleEntity(d\obj2, RoomScale, RoomScale, RoomScale)

			d\frameobj = CopyEntity(doorFrameObj)
		Default
			d\obj = CopyEntity(doorObj)
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))

			d\frameobj = CopyEntity(doorFrameObj)
			d\obj2 = CopyEntity(doorObj)

			ScaleEntity(d\obj2, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
			;entityType d\obj2, HIT_MAP
		EndIf
	End Select

	PositionEntity(d\frameobj, x, y, z)
	ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
	EntityType(d\obj, HIT_MAP)
	EntityType(d\obj2, HIT_MAP)

	d\id = DoorTempID
	DoorTempID=DoorTempID+1

	d\tag = tag
	d\code = code

	For i = 0 To 1
		If (tag <> "") Then
			buttonKeyObj = GrabMesh("GFX/Map/Meshes/ButtonKeycard.b3d")
			d\buttons[i]= CopyEntity(buttonKeyObj)
			DropAsset(buttonKeyObj)
		ElseIf (code <> "") Then
			buttonCodeObj = GrabMesh("GFX/Map/Meshes/ButtonCode.b3d")
			d\buttons[i] = CopyEntity(buttonCodeObj)
			EntityFX(d\buttons[i], 1)
			DropAsset(buttonCodeObj)
		Else
			d\buttons[i] = CopyEntity(buttonObj)
		EndIf

		ScaleEntity(d\buttons[i], 0.03, 0.03, 0.03)
	Next

	If (d\type = DOOR_TYPE_CONT) Then
		PositionEntity(d\buttons[0], x - 432.0 * RoomScale, y + 0.7, z + 192.0 * RoomScale)
		PositionEntity(d\buttons[1], x + 432.0 * RoomScale, y + 0.7, z - 192.0 * RoomScale)
		RotateEntity(d\buttons[0], 0, 90, 0)
		RotateEntity(d\buttons[1], 0, 270, 0)
	Else
		PositionEntity(d\buttons[0], x + 0.6, y + 0.7, z - 0.1)
		PositionEntity(d\buttons[1], x - 0.6, y + 0.7, z + 0.1)
		RotateEntity(d\buttons[1], 0, 180, 0)
	EndIf
	EntityParent(d\buttons[0], d\frameobj)
	EntityParent(d\buttons[1], d\frameobj)
	EntityPickMode(d\buttons[0], 2)
	EntityPickMode(d\buttons[1], 2)

	PositionEntity(d\obj, x, y, z)

	RotateEntity(d\obj, 0, angle, 0)
	RotateEntity(d\frameobj, 0, angle, 0)

	If (d\obj2 <> 0) Then
		PositionEntity(d\obj2, x, y, z)
		If (d\type = DOOR_TYPE_CONT) Then
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
	If (d\obj2 <> 0) Then
		EntityPickMode(d\obj2, 3)
		MakeCollBox(d\obj2)
	EndIf

	EntityPickMode(d\frameobj,2)

	d\room = room

	d\mtfClose = True

	DropAsset(doorObj)     ;Bust his nut.
	DropAsset(doorFrameObj);Bust his nut!!!
	DropAsset(doorColl)    ;BUST HIS NUT!!!
	DropAsset(buttonObj)   ;B U S T  H I S  N U T  ! ! !

	DropAsset(contDoorLeft)
	DropAsset(contDoorRight)

	For i=0 To 1
		DropAsset(hczDoorObj[i])
	Next

	Return d
End Function

Function UpdateDoors()
	Local i%, d.Door, p.Particle, pvt%, x#, z#, temp%

	Local dist#, xdist#, zdist#

	If (UpdateDoorsTimer =< 0) Then
		For d = Each Door
			xdist = Abs(EntityX(mainPlayer\collider)-EntityX(d\obj,True))
			zdist = Abs(EntityZ(mainPlayer\collider)-EntityZ(d\obj,True))

			d\dist = xdist+zdist

			If (d\dist > HideDistance*2) Then
				If (d\obj <> 0) Then HideEntity(d\obj)
				If (d\frameobj <> 0) Then HideEntity(d\frameobj)
				If (d\obj2 <> 0) Then HideEntity(d\obj2)
				If (d\buttons[0] <> 0) Then HideEntity(d\buttons[0])
				If (d\buttons[1] <> 0) Then HideEntity(d\buttons[1])
			Else
				If (d\obj <> 0) Then ShowEntity(d\obj)
				If (d\frameobj <> 0) Then ShowEntity(d\frameobj)
				If (d\obj2 <> 0) Then ShowEntity(d\obj2)
				If (d\buttons[0] <> 0) Then ShowEntity(d\buttons[0])
				If (d\buttons[1] <> 0) Then ShowEntity(d\buttons[1])
			EndIf
		Next

		UpdateDoorsTimer = 30
	Else
		UpdateDoorsTimer = Max(UpdateDoorsTimer-timing\tickDuration,0)
	EndIf

	mainPlayer\closestButton = 0
	mainPlayer\closestDoor = Null

	For d = Each Door
		If (d\dist < HideDistance*2) Then

			If ((d\openstate >= 180 Or d\openstate <= 0) And mainPlayer\grabbedEntity = 0) Then
				For i = 0 To 1
					If (d\buttons[i] <> 0) Then
						If (Abs(EntityX(mainPlayer\collider)-EntityX(d\buttons[i],True)) < 1.0) Then
							If (Abs(EntityZ(mainPlayer\collider)-EntityZ(d\buttons[i],True)) < 1.0) Then
								dist = Distance(EntityX(mainPlayer\collider, True), EntityZ(mainPlayer\collider, True), EntityX(d\buttons[i], True), EntityZ(d\buttons[i], True));entityDistance(collider, d\buttons[i])
								If (dist < 0.7) Then
									;TODO: use deltayaw as faster way to determine whether the player can press the button or not
									temp = CreatePivot()
									PositionEntity(temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam))
									PointEntity(temp,d\buttons[i])

									If (EntityPick(temp, 0.6) = d\buttons[i]) Then
										If (mainPlayer\closestButton = 0) Then
											mainPlayer\closestButton = d\buttons[i]
											mainPlayer\closestDoor = d
										Else
											If (dist < EntityDistance(mainPlayer\collider, mainPlayer\closestButton)) Then
												mainPlayer\closestButton = d\buttons[i] : mainPlayer\closestDoor = d
											EndIf
										EndIf
									EndIf

									FreeEntity(temp)

								EndIf
							EndIf
						EndIf

					EndIf
				Next
			EndIf

			If (d\open) Then
				If (d\openstate < 180) Then
					Select d\type
						Case DOOR_TYPE_CONT
							d\openstate = Min(180, d\openstate + timing\tickDuration * 0.8)
							MoveEntity(d\obj, Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, -Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
						Case DOOR_TYPE_HCZ
							d\openstate = Min(180, d\openstate + timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen+1) * timing\tickDuration / 85.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen*2+1) * timing\tickDuration / 120.0, 0, 0)
						Default
							d\openstate = Min(180, d\openstate + timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen*2+1) * timing\tickDuration / 80.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * timing\tickDuration / 80.0, 0, 0)
					End Select
				Else
					d\fastopen = 0
					ResetEntity(d\obj)
					If (d\obj2 <> 0) Then ResetEntity(d\obj2)
					If (d\timerstate > 0) Then
						d\timerstate = Max(0, d\timerstate - timing\tickDuration)
						If (d\timerstate + timing\tickDuration > 110 And d\timerstate <= 110) Then PlayRangedSound_SM(sndManager\caution, mainPlayer\cam, d\obj)

						If (d\timerstate = 0) Then
							d\open = (Not d\open)
							Select (d\type)
								Case DOOR_TYPE_CONT
									PlayRangedSound_SM(sndManager\closeBigDoor[Rand(0, 1)], mainPlayer\cam, d\obj)
								Case DOOR_TYPE_HCZ
									PlayRangedSound_SM(sndManager\closeHCZDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
								Default
									PlayRangedSound_SM(sndManager\closeDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
							End Select
						EndIf
					EndIf
					If (d\autoClose And RemoteDoorOn = True) Then
						If (EntityDistance(mainPlayer\cam, d\obj) < 2.1) Then
							;PlaySound2(HorrorSFX(7))) ;TODO: fix
							d\open = False
							Select (d\type)
								Case DOOR_TYPE_CONT
									PlayRangedSound_SM(sndManager\closeBigDoor[Rand(0, 1)], mainPlayer\cam, d\obj)
								Case DOOR_TYPE_HCZ
									PlayRangedSound_SM(sndManager\closeHCZDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
								Default
									PlayRangedSound_SM(sndManager\closeDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
							End Select
							d\autoClose = False
						EndIf
					EndIf
				EndIf
			Else
				If (d\openstate > 0) Then
					Select d\Type
						Case DOOR_TYPE_CONT
							d\openstate = Max(0, d\openstate - timing\tickDuration*0.8)
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration / 180.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, Sin(d\openstate) * timing\tickDuration / 180.0, 0, 0)
							If (d\openstate < 15 And d\openstate+timing\tickDuration => 15) Then
								For i = 0 To Rand(75,99)
									pvt = CreatePivot()
									PositionEntity(pvt, EntityX(d\frameobj,True)+Rnd(-0.2,0.2), EntityY(d\frameobj,True)+Rnd(0.0,1.2), EntityZ(d\frameobj,True)+Rnd(-0.2,0.2))
									RotateEntity(pvt, 0, Rnd(360), 0)

									p = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
									p\speed = 0.005
									RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)

									p\sizeChange = -0.00001
									p\size = 0.01
									ScaleSprite(p\obj,p\size,p\size)

									p\aChange = -0.01

									EntityOrder(p\obj,-1)

									FreeEntity(pvt)
								Next
							EndIf
						Case DOOR_TYPE_HCZ
							d\openstate = Max(0, d\openstate - timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration * (d\fastopen+1) / 85.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -timing\tickDuration / 120.0, 0, 0)
						Default
							d\openstate = Max(0, d\openstate - timing\tickDuration * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -timing\tickDuration * (d\fastopen+1) / 80.0, 0, 0)
							If (d\obj2 <> 0) Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -timing\tickDuration / 80.0, 0, 0)
					End Select

					If (d\angle = 0 Or d\angle=180) Then
						If (Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.15) Then
							If (Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.7*(d\type*2+1)) Then
								z = CurveValue(EntityZ(d\frameobj,True)+0.15*Sgn(EntityZ(mainPlayer\collider)-EntityZ(d\frameobj, True)), EntityZ(mainPlayer\collider), 5)
								PositionEntity(mainPlayer\collider, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), z)
							EndIf
						EndIf
					Else
						If (Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.15) Then
							If (Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.7*(d\type*2+1)) Then
								x = CurveValue(EntityX(d\frameobj,True)+0.15*Sgn(EntityX(mainPlayer\collider)-EntityX(d\frameobj, True)), EntityX(mainPlayer\collider), 5)
								PositionEntity(mainPlayer\collider, x, EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
							EndIf
						EndIf
					EndIf

				Else
					d\fastopen = 0
					PositionEntity(d\obj, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If (d\obj2 <> 0) Then PositionEntity(d\obj2, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If (d\obj2 <> 0 And d\type = DOOR_TYPE_DEF) Then
						MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
						MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
					EndIf
				EndIf
			EndIf
		EndIf
	Next
End Function

Function UseDoor(d.Door, showmsg% = True)
	Local i%, item.Item
	Local playerHasKeycard% = False
	Local playerHasRightKeycard% = False

	;Does the door require a keycard?
	If (d\tag <> "") Then
		;Does the player have the right keycard?
		For i=0 To mainPlayer\inventory\size-1
			item = mainPlayer\inventory\items[i]
			If (item <> Null) Then
				If (item\template\name = "keycard") Then
					If (d\locked) Then
						PlaySound_SM(sndManager\keycardErr)
						Msg = "The keycard was inserted into the slot but nothing happened."
						MsgTimer = 70 * 5
						Return
					EndIf

					playerHasKeycard = True
					If (HasTag(item, d\tag) Or HasTag(item, "omni")) Then
						playerHasRightKeycard = True
						Exit
					EndIf
				EndIf
			EndIf
		Next

		If (playerHasRightKeycard) Then
			PlaySound_SM(sndManager\keycardUse)
			If (showmsg) Then
				Msg = "The keycard was inserted into the slot."
				MsgTimer = 70 * 5
			EndIf
		ElseIf (playerHasKeycard) Then
			Msg = "A keycard with a higher security clearance is required to operate this door."
			MsgTimer = 70 * 5
			Return
		Else
			Msg = "A keycard is required to operate this door."
			MsgTimer = 70 * 5
			Return
		EndIf
	Else
		If (d\locked) Then
			If (showmsg = True) Then
				If (Not (d\isElevatorDoor>0)) Then
					PlaySound_SM(sndManager\buttonErr)
					Msg = "The door appears to be locked."
					MsgTimer = 70 * 5
				Else
					If (d\isElevatorDoor = 1) Then
						Msg = "You called the elevator."
						MsgTimer = 70 * 5
					ElseIf (Msg<>"You called the elevator.") Then
						If (Msg="You already called the elevator.") Or (MsgTimer<70*3) Then
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
	If (d\linkedDoor <> Null) Then d\linkedDoor\open = (Not d\linkedDoor\open)

	If (d\open) Then
		If (d\linkedDoor <> Null) Then d\linkedDoor\timerstate = d\linkedDoor\timer
		d\timerstate = d\timer
		Select (d\type)
			Case DOOR_TYPE_CONT
				PlayRangedSound_SM(sndManager\openBigDoor[Rand(0, 1)], mainPlayer\cam, d\obj)
			Case DOOR_TYPE_HCZ
				PlayRangedSound_SM(sndManager\openHCZDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
			Default
				PlayRangedSound_SM(sndManager\openDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
		End Select
	Else
		Select (d\type)
			Case DOOR_TYPE_CONT
				PlayRangedSound_SM(sndManager\closeBigDoor[Rand(0, 1)], mainPlayer\cam, d\obj)
			Case DOOR_TYPE_HCZ
				PlayRangedSound_SM(sndManager\closeHCZDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
			Default
				PlayRangedSound_SM(sndManager\closeDoor[Rand(0, 2)], mainPlayer\cam, d\obj)
		End Select
	EndIf
End Function

Function RemoveDoor(d.Door)
	If (d\buttons[0] <> 0) Then EntityParent(d\buttons[0], 0)
	If (d\buttons[1] <> 0) Then EntityParent(d\buttons[1], 0)

	If (d\obj <> 0) Then FreeEntity(d\obj)
	If (d\obj2 <> 0) Then FreeEntity(d\obj2)
	If (d\frameobj <> 0) Then FreeEntity(d\frameobj)
	If (d\buttons[0] <> 0) Then FreeEntity(d\buttons[0])
	If (d\buttons[1] <> 0) Then FreeEntity(d\buttons[1])

	Delete d
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
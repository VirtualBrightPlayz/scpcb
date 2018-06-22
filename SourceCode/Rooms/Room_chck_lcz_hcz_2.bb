Function FillRoom_chck_lcz_hcz_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\roomDoors[0] = CreateDoor(0, r\x + 48.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x - 152.0 * RoomScale, EntityY(r\roomDoors[0]\buttons[0],True), r\z - 352.0 * RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[1], r\x - 152.0 * RoomScale, EntityY(r\roomDoors[0]\buttons[1],True), r\z + 96.0 * RoomScale, True)

    r\roomDoors[1] = CreateDoor(0, r\x - 352.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    ;FreeEntity(r\roomDoors[1]\buttons[0])
    ;FreeEntity(r\roomDoors[1]\buttons[1])

    r\roomDoors[1]\linkedDoor = r\roomDoors[0]
    r\roomDoors[0]\linkedDoor = r\roomDoors[1]

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 720.0*RoomScale, 120.0*RoomScale, r\z+333.0*RoomScale, True)

    r\roomDoors[0]\timer = 70 * 5
    r\roomDoors[1]\timer = 70 * 5

    sc.SecurityCams = CreateSecurityCam(r\x+192.0*RoomScale, r\y+704.0*RoomScale, r\z-960.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 0
    TurnEntity(sc\cameraObj, 20, 0, 0)
    sc\id = 5

    ;r\objects[2] = CopyEntity(Monitor2,r\obj)
    ;ScaleEntity(r\objects[2], 2.0, 2.0, 2.0)
    ;PositionEntity(r\objects[2], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z+124.0*RoomScale, True)
    ;RotateEntity(r\objects[2],0,180,0)
    ;EntityFX(r\objects[2],1)

    ;r\objects[3] = CopyEntity(Monitor2,r\obj)
    ;ScaleEntity(r\objects[3], 2.0, 2.0, 2.0)
    ;PositionEntity(r\objects[3], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z-380.0*RoomScale, True)
    ;RotateEntity(r\objects[3],0,0,0)
    ;EntityFX(r\objects[3],1)

    ;If (MapTemp(Floor(r\x / 8.0),Floor(r\z /8.0)-1)=0) Then
    ;    CreateDoor(r\zone, r\x, 0, r\z  - 4.0, 0, r, 0, False, 0, "GEAR")
    ;EndIf
End Function


Function UpdateEventCheckpoint(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		;If (e\room\roomDoors[0]\open <> e\eventState) Then
		;	If (e\sounds[0] = 0) Then LoadEventSound(e,"SFX/Door/DoorCheckpoint.Ogg")
		;	PlaySound2(e\sounds[0])
		;EndIf

		;play a sound clip when the player passes through the gate
		;TODO: kill
		;If (e\eventState2 = 0) Then
		;	If (EntityZ(mainPlayer\collider) < e\room\z) Then
		;		If (PlayerZone = 1) Then
		;			PlaySound2(LoadTempSound("SFX/Ambient/ToZone2.ogg"))
		;		Else
		;			PlaySound2(LoadTempSound("SFX/Ambient/ToZone3.ogg"))
		;		EndIf
		;		e\eventState2 = 1
		;	EndIf
		;EndIf

		If (e\eventState3=0) Then
			If (Rand(2)=1) Then
				e\room\objects[1]=LoadAnimMesh("GFX/npcs/scp-1048.b3d")
				ScaleEntity(e\room\objects[1], 0.05,0.05,0.05)
				PositionEntity(e\room\objects[1],EntityX(e\room\objects[0],True),EntityY(e\room\objects[0],True),EntityZ(e\room\objects[0],True))
				SetAnimTime(e\room\objects[1],267)
			EndIf

			e\eventState3 = 1
		ElseIf (e\room\objects[1]<>0) Then
			If (e\eventState3 = 1) Then
				PointEntity(e\room\objects[1], mainPlayer\collider)
				RotateEntity(e\room\objects[1], -90, EntityYaw(e\room\objects[1]),0)
				angle = WrapAngle(DeltaYaw(mainPlayer\collider, e\room\objects[1]))
				If (angle<40 Or angle > 320) Then e\eventState3=2
			ElseIf (e\eventState3 = 2) Then
				PointEntity(e\room\objects[1], mainPlayer\collider)
				RotateEntity(e\room\objects[1], -90, EntityYaw(e\room\objects[1]),0)
				Animate2(e\room\objects[1],AnimTime(e\room\objects[1]),267,283,0.3,False)
				If (AnimTime(e\room\objects[1])=283) Then e\eventState3=3
			ElseIf (e\eventState3 = 3) Then
				Animate2(e\room\objects[1],AnimTime(e\room\objects[1]),283,267,-0.2,False)
				If (AnimTime( e\room\objects[1])=267) Then e\eventState3=4
			ElseIf (e\eventState3 = 4) Then
				angle = WrapAngle(DeltaYaw(mainPlayer\collider, e\room\objects[1]))
				If (angle>90 And angle < 270) Then
					FreeEntity(e\room\objects[1])
					e\room\objects[1]=0
					e\eventState3=5
				EndIf
			EndIf
		EndIf
	EndIf

	If (e\room\roomTemplate\name = "checkpoint2") Then
		For e2.Events = Each Events
			If (e2\name = "008") Then
				If (e2\eventState = 2) Then
					If (e\room\roomDoors[0]\locked) Then
						;TurnCheckpointMonitorsOff(1)
						e\room\roomDoors[0]\locked = False
						e\room\roomDoors[1]\locked = False
					EndIf
				Else
					If (e\room\dist < 12) Then
						;UpdateCheckpointMonitors(1)
						e\room\roomDoors[0]\locked = True
						e\room\roomDoors[1]\locked = True
					EndIf
				EndIf
			EndIf
		Next
	Else
		For e2.Events = Each Events
			If (e2\name = "room2sl") Then
				If (e2\eventState3 = 0) Then
					If (e\room\dist < 12) Then
						;TurnCheckpointMonitorsOff(0)
						e\room\roomDoors[0]\locked = False
						e\room\roomDoors[1]\locked = False
					EndIf
				Else
					If (e\room\dist < 12) Then
						;UpdateCheckpointMonitors(0)
						e\room\roomDoors[0]\locked = True
						e\room\roomDoors[1]\locked = True
					EndIf
				EndIf
			EndIf
		Next
	EndIf

	If (e\room\roomDoors[0]\open <> e\eventState) Then
		If (e\sounds[0] = 0) Then LoadEventSound(e,"SFX/Door/DoorCheckpoint.ogg")
		;TODO: wtf is this bullshit
		e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\roomDoors[0]\obj)
		e\soundChannels[1] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\roomDoors[1]\obj)
	EndIf

	e\eventState = e\room\roomDoors[0]\open

	If (IsChannelPlaying(e\soundChannels[0])) Then
		UpdateRangedSoundOrigin(e\soundChannels[0], mainPlayer\cam, e\room\roomDoors[0]\obj)
	EndIf
	If (IsChannelPlaying(e\soundChannels[1])) Then
		UpdateRangedSoundOrigin(e\soundChannels[1], mainPlayer\cam, e\room\roomDoors[1]\obj)
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
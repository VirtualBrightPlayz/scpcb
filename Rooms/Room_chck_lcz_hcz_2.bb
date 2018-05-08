Function FillRoom_chck_lcz_hcz_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\RoomDoors[0] = CreateDoor(0, r\x + 48.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x - 152.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z - 352.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x - 152.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z + 96.0 * RoomScale, True)
    
    r\RoomDoors[1] = CreateDoor(0, r\x - 352.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    ;FreeEntity r\RoomDoors[1]\buttons[0]
    ;FreeEntity r\RoomDoors[1]\buttons[1]
    
    r\RoomDoors[1]\LinkedDoor = r\RoomDoors[0]
    r\RoomDoors[0]\LinkedDoor = r\RoomDoors[1]
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity (r\Objects[0], r\x + 720.0*RoomScale, 120.0*RoomScale, r\z+333.0*RoomScale, True)
    
    r\RoomDoors[0]\timer = 70 * 5
    r\RoomDoors[1]\timer = 70 * 5
    
    sc.SecurityCams = CreateSecurityCam(r\x+192.0*RoomScale, r\y+704.0*RoomScale, r\z-960.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 0
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 5
    
    r\Objects[2] = CopyEntity(Monitor2,r\obj)
    ScaleEntity(r\Objects[2], 2.0, 2.0, 2.0)
    PositionEntity (r\Objects[2], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z+124.0*RoomScale, True)
    RotateEntity (r\Objects[2],0,180,0)
    EntityFX r\Objects[2],1
    
    r\Objects[3] = CopyEntity(Monitor2,r\obj)
    ScaleEntity(r\Objects[3], 2.0, 2.0, 2.0)
    PositionEntity (r\Objects[3], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z-380.0*RoomScale, True)
    RotateEntity (r\Objects[3],0,0,0)
    EntityFX r\Objects[3],1
    
    ;If MapTemp(Floor(r\x / 8.0),Floor(r\z /8.0)-1)=0 Then
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
	If mainPlayer\currRoom = e\room Then
		;If e\room\RoomDoors[0]\open <> e\EventState Then
		;	If e\sounds[0] = 0 Then LoadEventSound(e,"SFX\Door\DoorCheckpoint.Ogg")
		;	PlaySound2 e\sounds[0]
		;EndIf
		
		;play a sound clip when the player passes through the gate
		;TODO: kill
		;If e\EventState2 = 0 Then
		;	If EntityZ(mainPlayer\collider) < e\room\z Then
		;		If PlayerZone = 1 Then
		;			PlaySound2(LoadTempSound("SFX\Ambient\ToZone2.ogg"))
		;		Else
		;			PlaySound2(LoadTempSound("SFX\Ambient\ToZone3.ogg"))
		;		EndIf
		;		e\EventState2 = 1
		;	EndIf
		;EndIf
		
		If e\EventState3=0 Then
			If Rand(2)=1 Then
				e\room\Objects[1]=LoadAnimMesh("GFX\npcs\scp-1048.b3d")
				ScaleEntity e\room\Objects[1], 0.05,0.05,0.05
				PositionEntity(e\room\Objects[1],EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True))
				SetAnimTime e\room\Objects[1],267	
			EndIf
			
			e\EventState3 = 1
		ElseIf e\room\Objects[1]<>0
			If e\EventState3 = 1 Then
				PointEntity e\room\Objects[1], mainPlayer\collider
				RotateEntity e\room\Objects[1], -90, EntityYaw(e\room\Objects[1]),0
				angle = WrapAngle(DeltaYaw(mainPlayer\collider, e\room\Objects[1]))
				If angle<40 Or angle > 320 Then e\EventState3=2
			ElseIf e\EventState3 = 2
				PointEntity e\room\Objects[1], mainPlayer\collider
				RotateEntity e\room\Objects[1], -90, EntityYaw(e\room\Objects[1]),0
				Animate2(e\room\Objects[1],AnimTime(e\room\Objects[1]),267,283,0.3,False)
				If AnimTime(e\room\Objects[1])=283 Then e\EventState3=3
			ElseIf e\EventState3 = 3
				Animate2(e\room\Objects[1],AnimTime(e\room\Objects[1]),283,267,-0.2,False)
				If AnimTime( e\room\Objects[1])=267 Then e\EventState3=4
			ElseIf e\EventState3 = 4
				angle = WrapAngle(DeltaYaw(mainPlayer\collider, e\room\Objects[1]))
				If angle>90 And angle < 270 Then 
					FreeEntity(e\room\Objects[1])
					e\room\Objects[1]=0
					e\EventState3=5
				EndIf
			EndIf
		EndIf
	EndIf
	
	If e\room\RoomTemplate\Name = "checkpoint2"
		For e2.Events = Each Events
			If e2\EventName = "008"
				If e2\EventState = 2
					If e\room\RoomDoors[0]\locked
						TurnCheckpointMonitorsOff(1)
						e\room\RoomDoors[0]\locked = False
						e\room\RoomDoors[1]\locked = False
					EndIf
				Else
					If e\room\dist < 12
						UpdateCheckpointMonitors(1)
						e\room\RoomDoors[0]\locked = True
						e\room\RoomDoors[1]\locked = True
					EndIf
				EndIf
			EndIf
		Next
	Else
		For e2.Events = Each Events
			If e2\EventName = "room2sl"
				If e2\EventState3 = 0
					If e\room\dist < 12
						TurnCheckpointMonitorsOff(0)
						e\room\RoomDoors[0]\locked = False
						e\room\RoomDoors[1]\locked = False
					EndIf
				Else
					If e\room\dist < 12
						UpdateCheckpointMonitors(0)
						e\room\RoomDoors[0]\locked = True
						e\room\RoomDoors[1]\locked = True
					EndIf
				EndIf
			EndIf
		Next
	EndIf
	
	If e\room\RoomDoors[0]\open <> e\EventState Then
		If e\sounds[0] = 0 Then LoadEventSound(e,"SFX\Door\DoorCheckpoint.ogg")
		;TODO: wtf is this bullshit
		e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\RoomDoors[0]\obj)
		e\soundChannels[1] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\RoomDoors[1]\obj)
	EndIf
	
	e\EventState = e\room\RoomDoors[0]\open
	
	If IsChannelPlaying(e\soundChannels[0])
		UpdateRangedSoundOrigin(e\soundChannels[0], mainPlayer\cam, e\room\RoomDoors[0]\obj)
	EndIf
	If IsChannelPlaying(e\soundChannels[1])
		UpdateRangedSoundOrigin(e\soundChannels[1], mainPlayer\cam, e\room\RoomDoors[1]\obj)
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
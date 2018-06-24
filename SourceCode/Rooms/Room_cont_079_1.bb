Function FillRoom_cont_079_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\zone, r\x, -448.0*RoomScale, r\z + 1136.0 * RoomScale, 0, r, False,True, 4)
    d\dir = 1 : d\autoClose = False : d\open = False
    PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, -250*RoomScale, r\z + 918.0 * RoomScale, True)
    ;TurnEntity(d\buttons[0],0,-90,0,True)
    PositionEntity(d\buttons[0], r\x - 240.0 * RoomScale, -250*RoomScale, r\z + 1366.0 * RoomScale, True)
    ;TurnEntity(d\buttons[1],0, 90,0,True)

    r\roomDoors[0] = CreateDoor(r\zone, r\x + 1456.0*RoomScale, -448.0*RoomScale, r\z + 976.0 * RoomScale, 0, r, False, True, 3)
    r\roomDoors[0]\dir = 1 : r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = False
    PositionEntity(r\roomDoors[0]\buttons[1], r\x + 1760.0 * RoomScale, -250*RoomScale, r\z + 1236.0 * RoomScale, True)
    TurnEntity(r\roomDoors[0]\buttons[0],0,-90-90,0,True)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x + 1760.0 * RoomScale, -240*RoomScale, r\z + 740.0 * RoomScale, True)
    TurnEntity(r\roomDoors[0]\buttons[1],0, 90-90,0,True)

    CreateDoor(0, r\x + 1144.0*RoomScale, -448.0*RoomScale, r\z + 704.0 * RoomScale, 90, r, False, False, -1)

    r\objects[0] = LoadAnimMesh("GFX/Map/079.b3d")
    ScaleEntity(r\objects[0], 1.3, 1.3, 1.3, True)
    PositionEntity(r\objects[0], r\x + 1856.0*RoomScale, -560.0*RoomScale, r\z-672.0*RoomScale, True)
    EntityParent(r\objects[0], r\obj)
    TurnEntity(r\objects[0],0,180,0,True)

    r\objects[1] = CreateSprite(r\objects[0])
    SpriteViewMode(r\objects[1],2)
    PositionEntity(r\objects[1], 0.082, 0.119, 0.010)
    ScaleSprite(r\objects[1],0.18*0.5,0.145*0.5)
    TurnEntity(r\objects[1],0,13.0,0)
    MoveEntity(r\objects[1], 0,0,-0.022)
    EntityTexture(r\objects[1],OldAiPics(0))

    HideEntity(r\objects[1])

    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x + 1184.0*RoomScale, -448.0*RoomScale, r\z+1792.0*RoomScale, True)

    de = CreateDecal(3,  r\x + 1184.0*RoomScale, -448.0*RoomScale+0.01, r\z+1792.0*RoomScale,90,Rnd(360),0)
    de\size = 0.5
    ScaleSprite(de\obj, de\size,de\size)
    EntityParent(de\obj, r\obj)
End Function

Function UpdateEvent_cont_079_1(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then

		If (e\eventState = 0) Then
			;Music(4) = LoadSound("SFX/Music/079.ogg") ;TODO: fix
			e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[2],True), EntityY(e\room\objects[2],True)+0.5, EntityZ(e\room\objects[2],True))
			PointEntity(e\room\npc[0]\collider, e\room\obj)
			RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider),0, True)
			;SetAnimTime(e\room\npc[0]\obj, 906)
			SetNPCFrame(e\room\npc[0], 906)
			e\room\npc[0]\state = 8

			e\eventState = 1
		EndIf

		;TODO
		;ShouldPlay = 4
		If (RemoteDoorOn) Then
			If (e\room\roomDoors[0]\open) Then
				If (e\room\roomDoors[0]\openstate > 50 Or EntityDistance(mainPlayer\collider, e\room\roomDoors[0]\frameobj)<0.5) Then
					e\room\roomDoors[0]\openstate = Min(e\room\roomDoors[0]\openstate,50)
					e\room\roomDoors[0]\open = False
					PlaySound2((LoadTempSound("SFX/Door/DoorError.ogg")))
				EndIf
			EndIf
		ElseIf (e\eventState < 10000) Then
			If (e\eventState = 1) Then
				e\eventState = 2
			ElseIf (e\eventState = 2) Then
				If (EntityDistance(e\room\objects[0], mainPlayer\collider)<3.0) Then
					e\eventState = 3
					e\eventState2 = 1
					;e\sounds[0] = LoadSound("SFX/SCP/079/Speech.ogg")
					LoadEventSound(e,"SFX/SCP/079/Speech.ogg")
					e\soundChannels[0] = PlaySound(e\sounds[0])
				EndIf
			ElseIf (e\eventState = 3) Then
				If (e\eventState < 3500) Then
					If (IsChannelPlaying(e\soundChannels[0])) Then
						If (Rand(3) = 1) Then
							EntityTexture(e\room\objects[1], OldAiPics(0))
							ShowEntity(e\room\objects[1])
						ElseIf (Rand(10) = 1) Then
							HideEntity(e\room\objects[1])
						EndIf
					Else
						If (e\sounds[0] <> 0) Then
							FreeSound(e\sounds[0])
							e\sounds[0] = 0
						EndIf
						EntityTexture(e\room\objects[1], OldAiPics(1))
						ShowEntity(e\room\objects[1])
					EndIf
				Else
					If (EntityDistance(e\room\objects[0], mainPlayer\collider)<2.5) Then
						e\eventState = 10001
						;e\sounds[0] = LoadSound("SFX/SCP/079/Refuse.ogg")
						LoadEventSound(e,"SFX/SCP/079/Refuse.ogg")
						e\soundChannels[0] = PlaySound(e\sounds[0])
						EntityTexture(e\room\objects[1], OldAiPics(1))
						ShowEntity(e\room\objects[1])
					EndIf
				EndIf

			EndIf

		EndIf

	EndIf


	If (e\eventState2 = 1) Then
		If (RemoteDoorOn) Then
			LoadEventSound(e,"SFX/SCP/079/GateB.ogg")
			e\soundChannels[0] = PlaySound(e\sounds[0])
			e\eventState2 = 2

			For e2 = Each Event
				If (e2\name="exit1" Or e2\name="gateaentrance") Then
					e2\eventState3 = 1
				EndIf
			Next
		EndIf
	EndIf

	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
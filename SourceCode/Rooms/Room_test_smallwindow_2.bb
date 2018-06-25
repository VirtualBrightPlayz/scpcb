Const ROOM_TSW2_173SPAWN% = 0
Const ROOM_TSW2_173JUMP% = 1
Const ROOM_TSW2_GLASS% = 2

Function FillRoom_test_smallwindow_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[ROOM_TSW2_173SPAWN] = CreatePivot()
    PositionEntity(r\objects[0], r\x - 640.0 * RoomScale, 0.5, r\z - 912.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)

    r\objects[ROOM_TSW2_173JUMP] = CreatePivot()
    PositionEntity(r\objects[1], r\x - 632.0 * RoomScale, 0.5, r\z - 16.0 * RoomScale)
    EntityParent(r\objects[1], r\obj)

    Local Glasstex% = LoadTexture("GFX/Map/Textures/glass.png",1+2)
    r\objects[ROOM_TSW2_GLASS] = CreateSprite()
    EntityTexture(r\objects[ROOM_TSW2_GLASS],Glasstex)
    SpriteViewMode(r\objects[ROOM_TSW2_GLASS],2)
    ScaleSprite(r\objects[ROOM_TSW2_GLASS],182.0*RoomScale*0.5, 192.0*RoomScale*0.5)
    PositionEntity(r\objects[ROOM_TSW2_GLASS], r\x - 595.0 * RoomScale, 224.0*RoomScale, r\z - 208.0 * RoomScale)
    TurnEntity(r\objects[ROOM_TSW2_GLASS],0,180,0)
    EntityParent(r\objects[ROOM_TSW2_GLASS], r\obj)
    HideEntity(r\objects[ROOM_TSW2_GLASS])

    FreeTexture(Glasstex)

    ;d = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    ;d\autoClose = False : d\open = False
    r\doors[0] = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    r\doors[0]\autoClose = False : r\doors[0]\open = False

    d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, 0.0, r\z + 384.0 * RoomScale, 0, r, False, False)
    d\autoClose = False : d\open = False

    d = CreateDoor(r\zone, r\x - 816.0 * RoomScale, 0.0, r\z - 208.0 * RoomScale, 0, r, False, False)
    d\autoClose = False : d\open = False
    FreeEntity(d\buttons[0]) : d\buttons[0]=0
    FreeEntity(d\buttons[1]) : d\buttons[1]=0

    it = CreateItem("navigator", r\x - 312.0 * RoomScale, r\y + 264.0 * RoomScale, r\z + 176.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

Const EVENT_TSW2_173SPAWNED% = 0
Const EVENT_TSW2_173JUMPTIMER% = 0

Function UpdateEventTestroom173(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (Curr173\idle = 0) Then
			If (Not e\intState[EVENT_TSW2_173SPAWNED]) Then
				If (e\room\doors[0]\open) Then
					PositionEntity(Curr173\collider, EntityX(e\room\objects[0], True), 0.5, EntityZ(e\room\objects[0], True))
					ResetEntity(Curr173\collider)
					e\intState[EVENT_TSW2_173SPAWNED] = True
				EndIf
			Else
				ShowEntity(e\room\objects[ROOM_TSW2_GLASS])
				;start a timer for 173 breaking through the window
				e\floatState[EVENT_TSW2_173JUMPTIMER]=e\floatState[EVENT_TSW2_173JUMPTIMER]+timing\tickDuration
				dist = EntityDistance(mainPlayer\collider, e\room\objects[1])
				If (dist<1.0) Then
					;if close, increase the timer so that 173 is ready to attack
					e\floatState[EVENT_TSW2_173JUMPTIMER] = Max(e\floatState[EVENT_TSW2_173JUMPTIMER], 70*12)
				ElseIf (dist > 1.4) Then
					;if the player moves a bit further and blinks, 173 attacks
					If (e\floatState[EVENT_TSW2_173JUMPTIMER] > 70*12 And mainPlayer\blinkTimer =< -10) Then
						If (EntityDistance(Curr173\collider, e\room\objects[ROOM_TSW2_173SPAWN]) > 5.0) Then
							;if 173 is far away from the room (perhaps because the player
							;left and 173 moved to some other room?) -> disable the event
							RemoveEvent(e)
						Else
							PlayRangedSound(LoadTempSound("SFX/General/GlassBreak.ogg"), mainPlayer\cam, Curr173\obj)
							FreeEntity(e\room\objects[ROOM_TSW2_GLASS])
							PositionEntity(Curr173\collider, EntityX(e\room\objects[ROOM_TSW2_173SPAWN], True), 0.5, EntityZ(e\room\objects[1], True))
							ResetEntity(Curr173\collider)
							RemoveEvent(e)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
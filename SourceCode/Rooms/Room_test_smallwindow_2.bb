Function FillRoom_test_smallwindow_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x - 640.0 * RoomScale, 0.5, r\z - 912.0 * RoomScale)
    EntityParent(r\objects[0], r\obj)

    r\objects[1] = CreatePivot()
    PositionEntity(r\objects[1], r\x - 632.0 * RoomScale, 0.5, r\z - 16.0 * RoomScale)
    EntityParent(r\objects[1], r\obj)

    Local Glasstex% = LoadTexture("GFX/Map/Textures/glass.png",1+2)
    r\objects[2] = CreateSprite()
    EntityTexture(r\objects[2],Glasstex)
    SpriteViewMode(r\objects[2],2)
    ScaleSprite(r\objects[2],182.0*RoomScale*0.5, 192.0*RoomScale*0.5)
    PositionEntity(r\objects[2], r\x - 595.0 * RoomScale, 224.0*RoomScale, r\z - 208.0 * RoomScale)
    TurnEntity(r\objects[2],0,180,0)
    EntityParent(r\objects[2], r\obj)
    HideEntity(r\objects[2])

    FreeTexture(Glasstex)

    ;d = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    ;d\autoClose = False : d\open = False
    r\roomDoors[0] = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = False

    d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, 0.0, r\z + 384.0 * RoomScale, 0, r, False, False)
    d\autoClose = False : d\open = False

    d = CreateDoor(r\zone, r\x - 816.0 * RoomScale, 0.0, r\z - 208.0 * RoomScale, 0, r, False, False)
    d\autoClose = False : d\open = False
    FreeEntity(d\buttons[0]) : d\buttons[0]=0
    FreeEntity(d\buttons[1]) : d\buttons[1]=0

    it = CreateItem("Level 2 Key Card", "key2", r\x - 336.0 * RoomScale, r\y + 165.0 * RoomScale, r\z + 88.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("S-NAV 300 Navigator", "nav", r\x - 312.0 * RoomScale, r\y + 264.0 * RoomScale, r\z + 176.0 * RoomScale)
    it\state = 20 : EntityParent(it\collider, r\obj)
End Function

Function UpdateEventTestroom173(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (Curr173\idle = 0) Then
			If (e\eventState = 0) Then
				If (e\room\roomDoors[0]\open = True) Then
					PositionEntity(Curr173\collider, EntityX(e\room\objects[0], True), 0.5, EntityZ(e\room\objects[0], True))
					ResetEntity(Curr173\collider)
					e\eventState = 1
				EndIf
			Else
				ShowEntity(e\room\objects[2])
				;start a timer for 173 breaking through the window
				e\eventState = e\eventState + 1
				dist = EntityDistance(mainPlayer\collider, e\room\objects[1])
				If (dist<1.0) Then
					;if close, increase the timer so that 173 is ready to attack
					e\eventState = Max(e\eventState, 70*12)
				ElseIf (dist > 1.4) Then
					;if the player moves a bit further and blinks, 173 attacks
					If (e\eventState > 70*12 And mainPlayer\blinkTimer =< -10) Then
						If ((EntityDistance(Curr173\collider, e\room\objects[0]) > 5.0)) Then
							;if 173 is far away from the room (perhaps because the player
							;left and 173 moved to some other room?) -> disable the event
							RemoveEvent(e)
						Else
							PlayRangedSound(LoadTempSound("SFX/General/GlassBreak.ogg"), mainPlayer\cam, Curr173\obj)
							FreeEntity(e\room\objects[2])
							PositionEntity(Curr173\collider, EntityX(e\room\objects[1], True), 0.5, EntityZ(e\room\objects[1], True))
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
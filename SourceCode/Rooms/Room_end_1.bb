Function FillRoom_end_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    r\doors[0] = CreateDoor(r\x, 0, r\z + 1136 * RoomScale, 0, r, False, DOOR_TYPE_CONT, r\roomTemplate\name)
    r\doors[0]\autoClose = False : r\doors[0]\open = False
    FreeEntity(r\doors[0]\buttons[0])
	r\doors[0]\buttons[0]=0
    FreeEntity(r\doors[0]\buttons[1])
	r\doors[0]\buttons[1]=0
End Function


Function UpdateEventEndroom106(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (Contained106) Then
		If (e\eventState = 0) Then
			If (e\room\dist < 8 And e\room\dist > 0) Then
				If (Curr106\state < 0) Then
					RemoveEvent(e)
				Else
					e\room\doors[0]\open = True

					e\room\npc[0]=CreateNPC(NPCtypeD, EntityX(e\room\doors[0]\obj,True), 0.5, EntityZ(e\room\doors[0]\obj,True))

					tex = LoadTexture("GFX/NPCs/classd/janitor.jpg")
					e\room\npc[0]\texture = "GFX/NPCs/classd/janitor.jpg"
					EntityTexture(e\room\npc[0]\obj, tex)
					FreeTexture(tex)

					PointEntity(e\room\npc[0]\collider, e\room\obj)
					RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider),0, True)
					MoveEntity(e\room\npc[0]\collider, 0,0,0.5)

					e\room\doors[0]\open = False
					PlayRangedSound(LoadTempSound("SFX/Door/EndroomDoor.ogg"), mainPlayer\cam, e\room\obj, 15)

					e\eventState = 1
				EndIf
			EndIf
		ElseIf (e\eventState = 1) Then
			If (mainPlayer\currRoom = e\room) Then
				;PlaySound2(CloseDoorSFX(1,0))
				;PlaySound2(DecaySFX(0))
				;e\room\doors[0]\open = False
				e\room\npc[0]\state = 1
				e\eventState = 2

				e\sounds[0] = LoadSound("SFX/Character/Janitor/106Abduct.ogg")
				PlaySound2(e\sounds[0])

				If (e\soundChannels[0]<>0) Then StopChannel(e\soundChannels[0])
			ElseIf (e\room\dist < 8) Then
				If (e\sounds[0] = 0) Then e\sounds[0] = LoadSound("SFX/Character/Janitor/Idle.ogg")
				e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\npc[0]\obj, 15.0)
			EndIf
		ElseIf (e\eventState = 2) Then
			dist = EntityDistance(e\room\npc[0]\collider, e\room\obj)
			If (dist<1.5) Then
				de = CreateDecal(0, EntityX(e\room\obj), 0.01, EntityZ(e\room\obj), 90, Rand(360), 0)
				de\size = 0.05 : de\sizeChange = 0.008 : de\timer=10000 : UpdateDecals()
				e\eventState = 3

				;PlaySound2(DecaySFX(1))
			EndIf
		Else
			dist = Distance(EntityX(e\room\npc[0]\collider),EntityZ(e\room\npc[0]\collider), EntityX(e\room\obj),EntityZ(e\room\obj))
			PositionEntity(Curr106\obj, EntityX(e\room\obj, True), 0.0, EntityZ(e\room\obj, True))
			;ResetEntity(Curr106\collider)
			PointEntity(Curr106\obj, e\room\npc[0]\collider)
			RotateEntity(Curr106\obj, 0, EntityYaw(Curr106\obj), 0, True)

			Curr106\idle = True

			If (dist<0.4) Then
				If (e\room\npc[0]\state=1) Then
					;PlaySound2(HorrorSFX(10))
					SetNPCFrame(e\room\npc[0],41)
				EndIf
				e\eventState = e\eventState+timing\tickDuration/2
				e\room\npc[0]\state = 6
				e\room\npc[0]\currSpeed = CurveValue(0.0, e\room\npc[0]\currSpeed, 25.0)
				PositionEntity(e\room\npc[0]\collider, CurveValue(EntityX(e\room\obj, True), EntityX(e\room\npc[0]\collider), 25.0), 0.3-e\eventState/70, CurveValue(EntityZ(e\room\obj, True), EntityZ(e\room\npc[0]\collider), 25.0))
				ResetEntity(e\room\npc[0]\collider)

				;TurnEntity(e\room\npc[0]\collider,0,0,0.5*timing\tickDuration)
				AnimateNPC(e\room\npc[0], 41, 58, 0.1, False)

				AnimateNPC(Curr106, 206,112, -1.0, False)
			Else
				AnimateNPC(Curr106, 112,206, 1.5, False)
			EndIf

			If (e\eventState > 35) Then
				;PlayRangedSound(OldManSFX(Rand(1,2)), mainPlayer\cam, e\room\npc[0]\collider)

				PositionEntity(Curr106\obj, EntityX(Curr106\collider), -100.0, EntityZ(Curr106\collider), True)
				PositionEntity(Curr106\collider, EntityX(Curr106\collider), -100.0, EntityZ(Curr106\collider), True)

				Curr106\idle = False
				If (EntityDistance(mainPlayer\collider, e\room\obj)<2.5) Then Curr106\state=-0.1

				RemoveNPC(e\room\npc[0])

				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
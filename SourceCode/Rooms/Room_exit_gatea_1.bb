Function FillRoom_exit_gatea_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    ;alempi hissi
    r\doors[0] = CreateDoor(r\x+744.0*RoomScale, 0, r\z+512.0*RoomScale, 90, r, True)
    r\doors[0]\autoClose = False : r\doors[0]\open = True
    PositionEntity(r\doors[0]\buttons[1],r\x+688*RoomScale, EntityY(r\doors[0]\buttons[1],True), r\z+368*RoomScale, True)
    PositionEntity(r\doors[0]\buttons[0],r\x+784*RoomScale, EntityY(r\doors[0]\buttons[0],True), r\z+656*RoomScale, True)
    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x+1048.0*RoomScale, 0, r\z+512.0*RoomScale, True)
    EntityParent(r\objects[0], r\obj)

    r\doors[1] = CreateDoor(r\x, 0, r\z - 360.0 * RoomScale, 0, r, False, DOOR_TYPE_CONT, r\roomTemplate\name)
    PositionEntity(r\doors[1]\buttons[1], r\x+416*RoomScale, EntityY(r\doors[0]\buttons[1],True), r\z-576*RoomScale, True)
    RotateEntity(r\doors[1]\buttons[1],0,r\angle-90,0,True)
    PositionEntity(r\doors[1]\buttons[0], r\x, 20.0, r\z, True)
End Function

Function UpdateEvent_exit_gatea_1(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, gatea.Room

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (RemoteDoorOn=False) Then
			e\room\doors[1]\locked=True
		ElseIf (RemoteDoorOn And e\eventState3=0) Then
			e\room\doors[1]\locked=False
			If (e\room\doors[1]\open) Then
				If (e\room\doors[1]\openstate > 50 Or EntityDistance(mainPlayer\collider, e\room\doors[1]\frameobj)<0.5) Then
					e\room\doors[1]\openstate = Min(e\room\doors[1]\openstate,50)
					e\room\doors[1]\open = False
					PlayRangedSound (LoadTempSound("SFX/Door/DoorError.ogg"), mainPlayer\cam, e\room\doors[1]\frameobj)
				EndIf
			EndIf
		Else
			e\room\doors[1]\locked=False
			gatea =Null
			For r = Each Room
				If (r\roomTemplate\name = "gatea") Then
					gatea = r
					Exit
				EndIf
			Next

			e\eventState = UpdateElevators(e\eventState, e\room\doors[0], gatea\doors[1], e\room\objects[0], e\room\objects[1], e)
			If (Contained106 = False) Then
				If (e\eventState < -1.5 And e\eventState+timing\tickDuration=> -1.5) Then
					;PlaySound2(OldManSFX(3)) ;TODO: fix
				EndIf
			EndIf

			If (EntityDistance(mainPlayer\collider, e\room\objects[1])<4.0) Then
				gatea\doors[1]\locked = True
				mainPlayer\currRoom = gatea
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
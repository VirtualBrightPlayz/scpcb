Function FillRoom_exit_gatea_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    ;alempi hissi
    r\roomDoors[0] = CreateDoor(0, r\x+744.0*RoomScale, 0, r\z+512.0*RoomScale, 90, r, True)
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = True
    PositionEntity(r\roomDoors[0]\buttons[1],r\x+688*RoomScale, EntityY(r\roomDoors[0]\buttons[1],True), r\z+368*RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[0],r\x+784*RoomScale, EntityY(r\roomDoors[0]\buttons[0],True), r\z+656*RoomScale, True)
    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x+1048.0*RoomScale, 0, r\z+512.0*RoomScale, True)
    EntityParent(r\objects[0], r\obj)

    r\roomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z - 360.0 * RoomScale, 0, r, False, True, 5)
    r\roomDoors[1]\dir = 1 : r\roomDoors[1]\autoClose = False : r\roomDoors[1]\open = False
    PositionEntity(r\roomDoors[1]\buttons[1], r\x+416*RoomScale, EntityY(r\roomDoors[0]\buttons[1],True), r\z-576*RoomScale, True)
    RotateEntity(r\roomDoors[1]\buttons[1],0,r\angle-90,0,True)
    PositionEntity(r\roomDoors[1]\buttons[0], r\x, 20.0, r\z, True)
End Function

Function UpdateEvent_exit_gatea_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, gatea.Rooms

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If RemoteDoorOn=False Then
			e\room\roomDoors[1]\locked=True
		ElseIf RemoteDoorOn And e\eventState3=0 Then
			e\room\roomDoors[1]\locked=False
			If e\room\roomDoors[1]\open Then
				If e\room\roomDoors[1]\openstate > 50 Or EntityDistance(mainPlayer\collider, e\room\roomDoors[1]\frameobj)<0.5 Then
					e\room\roomDoors[1]\openstate = Min(e\room\roomDoors[1]\openstate,50)
					e\room\roomDoors[1]\open = False
					PlayRangedSound (LoadTempSound("SFX/Door/DoorError.ogg"), mainPlayer\cam, e\room\roomDoors[1]\frameobj)
				EndIf
			EndIf
		Else
			e\room\roomDoors[1]\locked=False
			gatea.Rooms =Null
			For r.Rooms = Each Rooms
				If r\roomTemplate\name = "gatea" Then
					gatea = r
					Exit
				EndIf
			Next

			e\eventState = UpdateElevators(e\eventState, e\room\roomDoors[0], gatea\roomDoors[1], e\room\objects[0], e\room\objects[1], e)
			If Contained106 = False Then
				If e\eventState < -1.5 And e\eventState+timing\tickDuration=> -1.5 Then
					;PlaySound2(OldManSFX(3)) ;TODO: fix
				EndIf
			EndIf

			If EntityDistance(mainPlayer\collider, e\room\objects[1])<4.0 Then
				gatea\roomDoors[1]\locked = True
				mainPlayer\currRoom = gatea
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
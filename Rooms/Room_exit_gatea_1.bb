Function FillRoom_exit_gatea_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    ;alempi hissi
    r\RoomDoors[0] = CreateDoor(0, r\x+744.0*RoomScale, 0, r\z+512.0*RoomScale, 90, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1],r\x+688*RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z+368*RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0],r\x+784*RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z+656*RoomScale, True)
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x+1048.0*RoomScale, 0, r\z+512.0*RoomScale, True)
    EntityParent r\Objects[0], r\obj
    
    r\RoomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z - 360.0 * RoomScale, 0, r, False, True, 5)
    r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
    PositionEntity(r\RoomDoors[1]\buttons[1], r\x+416*RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z-576*RoomScale, True)
    RotateEntity r\RoomDoors[1]\buttons[1],0,r\angle-90,0,True
    PositionEntity(r\RoomDoors[1]\buttons[0], r\x, 20.0, r\z, True)
End Function

Function UpdateEvent_exit_gatea_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then 
		If RemoteDoorOn=False Then
			e\room\RoomDoors[1]\locked=True
		ElseIf RemoteDoorOn And e\EventState3=0
			e\room\RoomDoors[1]\locked=False
			If e\room\RoomDoors[1]\open Then 
				If e\room\RoomDoors[1]\openstate > 50 Or EntityDistance(mainPlayer\collider, e\room\RoomDoors[1]\frameobj)<0.5 Then
					e\room\RoomDoors[1]\openstate = Min(e\room\RoomDoors[1]\openstate,50)
					e\room\RoomDoors[1]\open = False
					PlayRangedSound (LoadTempSound("SFX\Door\DoorError.ogg"), mainPlayer\cam, e\room\RoomDoors[1]\frameobj)
				EndIf							
			EndIf
		Else
			e\room\RoomDoors[1]\locked=False
			Local gatea.Rooms =Null
			For r.Rooms = Each Rooms
				If r\RoomTemplate\Name = "gatea" Then
					gatea = r 
					Exit
				EndIf
			Next
			
			e\EventState = UpdateElevators(e\EventState, e\room\RoomDoors[0], gatea\RoomDoors[1], e\room\Objects[0], e\room\Objects[1], e)
			If Contained106 = False Then 
				If e\EventState < -1.5 And e\EventState+timing\tickDuration=> -1.5 Then
					;PlaySound2(OldManSFX(3)) ;TODO: fix
				EndIf
			EndIf
			
			If EntityDistance(mainPlayer\collider, e\room\Objects[1])<4.0 Then
				gatea\RoomDoors[1]\locked = True
				mainPlayer\currRoom = gatea
				RemoveEvent(e)
			EndIf						
		EndIf
	EndIf
	;[End Block]
End Function

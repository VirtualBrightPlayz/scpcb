Function FillRoom_tnnl_plain_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity (r\Objects[0], r\x - 190.0*RoomScale, 4.0*RoomScale, r\z+190.0*RoomScale, True)    
End Function


Function UpdateEvent_tnnl_plain_3(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\EventState = 0 Then
		e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True)+0.5, EntityZ(e\room\Objects[0],True))
		PointEntity e\room\NPC[0]\Collider, e\room\obj
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider)+Rnd(-20,20),0, True
		SetNPCFrame (e\room\NPC[0], 906)
		e\room\NPC[0]\State = 8
		
		e\EventState = 1
		RemoveEvent(e)
	EndIf
	;[End Block]
End Function


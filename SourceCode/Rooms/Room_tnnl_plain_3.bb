Function FillRoom_tnnl_plain_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x - 190.0*RoomScale, 4.0*RoomScale, r\z+190.0*RoomScale, True)
End Function


Function UpdateEvent_tnnl_plain_3(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState = 0) Then
		e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[0],True), EntityY(e\room\objects[0],True)+0.5, EntityZ(e\room\objects[0],True))
		PointEntity(e\room\npc[0]\collider, e\room\obj)
		RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider)+Rnd(-20,20),0, True)
		SetNPCFrame(e\room\npc[0], 906)
		e\room\npc[0]\state = 8

		e\eventState = 1
		RemoveEvent(e)
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
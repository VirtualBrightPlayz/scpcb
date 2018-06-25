Function FillRoom_off_2level_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    If (Rand(2)=1) Then
        it = CreatePaper("docMTF", r\x + 744.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 944.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    Else
        it = CreatePaper("docSC", r\x + 680.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 944.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf

    it = CreatePaper("docOBJC", r\x + 160.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 568.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("radio", r\x - 1184.0 * RoomScale, r\y + 480.0 * RoomScale, r\z - 800.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    For i = 0 To Rand(0,1)
        it = CreateItem("eyedrops", r\x - 1529.0*RoomScale, r\y + 563.0 * RoomScale, r\z - 572.0*RoomScale + i*0.05)
        EntityParent(it\collider, r\obj)
    Next

    it = CreateItem("battery", r\x - 1545.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 372.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    If (Rand(2) = 1) Then
        it = CreateItem("battery", r\x - 1540.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 340.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf
    If (Rand(2) = 1) Then
        it = CreateItem("battery", r\x - 1529.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 308.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf

    r\doors[0] = CreateDoor(r\zone, r\x - 1056.0 * RoomScale, 384.0*RoomScale, r\z + 290.0 * RoomScale, 90, r, True)
    r\doors[0]\autoClose = False : r\doors[0]\open = True
    PositionEntity(r\doors[0]\buttons[0], EntityX(r\doors[0]\buttons[0],True),EntityY(r\doors[0]\buttons[0],True),r\z + 161.0 * RoomScale,True)
    PositionEntity(r\doors[0]\buttons[1], EntityX(r\doors[0]\buttons[1],True),EntityY(r\doors[0]\buttons[1],True),r\z + 161.0 * RoomScale,True)
End Function


Function UpdateEvent_off_2level_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		e\eventState = e\eventState+timing\tickDuration
		If (e\eventState > 700) Then
			If (EntityDistance(e\room\doors[0]\obj, mainPlayer\collider)>0.5) Then
				If (EntityInView(e\room\doors[0]\obj, mainPlayer\cam)=False) Then
					DebugLog("%@@= / {2E6C2=FD gi`h]c")
					e\room\doors[0]\open = False
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


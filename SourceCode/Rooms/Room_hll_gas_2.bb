Function FillRoom_hll_gas_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams, em.Emitters
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    i = 0
    For xtemp = -1 To 1 Step 2
        For ztemp = -1 To 1
            em = CreateEmitter(r\x + 202.0 * RoomScale * xtemp, 8.0 * RoomScale, r\z + 256.0 * RoomScale * ztemp, 0)
            em\randAngle = 30
            em\speed = 0.0045
            em\sizeChange = 0.007
            em\aChange = -0.016
            r\objects[i] = em\obj
            If (i < 3) Then
                TurnEntity(em\obj, 0, -90, 0, True)
            Else
                TurnEntity(em\obj, 0, 90, 0, True)
            EndIf
            TurnEntity(em\obj, -45, 0, 0, True)
            EntityParent(em\obj, r\obj)
            i=i+1
        Next
    Next

    r\objects[6] = CreatePivot()
    PositionEntity(r\objects[6], r\x + 640.0 * RoomScale, 8.0 * RoomScale, r\z - 896.0 * RoomScale)
    EntityParent(r\objects[6], r\obj)

    r\objects[7] = CreatePivot()
    PositionEntity(r\objects[7], r\x - 864.0 * RoomScale, -400.0 * RoomScale, r\z - 632.0 * RoomScale)
    EntityParent(r\objects[7],r\obj)
End Function


Function UpdateEvent_hll_gas_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (Curr173\idle = 0) Then
		If (e\room\dist < 8.0  And e\room\dist > 0) Then
			If ((Not EntityVisible(Curr173\collider, mainPlayer\cam)) And (Not EntityVisible(e\room\objects[6], mainPlayer\cam))) Then
				PositionEntity(Curr173\collider, EntityX(e\room\objects[6], True), 0.5, EntityZ(e\room\objects[6], True))
				ResetEntity(Curr173\collider)
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
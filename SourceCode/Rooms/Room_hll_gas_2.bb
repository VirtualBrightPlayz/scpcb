Function FillRoom_hll_gas_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    i = 0
    For  xtemp% = -1 To 1 Step 2
        For ztemp% = -1 To 1
            em.Emitters = CreateEmitter(r\x + 202.0 * RoomScale * xtemp, 8.0 * RoomScale, r\z + 256.0 * RoomScale * ztemp, 0)
            em\RandAngle = 30
            em\Speed = 0.0045
            em\SizeChange = 0.007
            em\Achange = -0.016
            r\Objects[i] = em\Obj
            If i < 3 Then 
                TurnEntity(em\Obj, 0, -90, 0, True) 
            Else 
                TurnEntity(em\Obj, 0, 90, 0, True)
            EndIf
            TurnEntity(em\Obj, -45, 0, 0, True)
            EntityParent(em\Obj, r\obj)
            i=i+1
        Next
    Next
    
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6], r\x + 640.0 * RoomScale, 8.0 * RoomScale, r\z - 896.0 * RoomScale)
    EntityParent(r\Objects[6], r\obj)
    
    r\Objects[7] = CreatePivot()
    PositionEntity(r\Objects[7], r\x - 864.0 * RoomScale, -400.0 * RoomScale, r\z - 632.0 * RoomScale)
    EntityParent(r\Objects[7],r\obj)
End Function


Function UpdateEvent_hll_gas_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If Curr173\Idle = 0 Then 
		If e\room\dist < 8.0  And e\room\dist > 0 Then			
			If (Not EntityVisible(Curr173\collider, mainPlayer\cam)) And (Not EntityVisible(e\room\Objects[6], mainPlayer\cam)) Then 
				PositionEntity(Curr173\collider, EntityX(e\room\Objects[6], True), 0.5, EntityZ(e\room\Objects[6], True))
				ResetEntity(Curr173\collider)
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


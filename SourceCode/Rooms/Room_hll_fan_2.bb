Function FillRoom_hll_fan_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump
    For r2.Rooms = Each Rooms
        If r2<>r Then
            If r2\roomTemplate\name = "room2_2" Then
                r\objects[0] = CopyEntity(r2\objects[0]) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If r\objects[0]=0 Then r\objects[0] = LoadMesh("GFX/Map/fan.b3d")
    ScaleEntity r\objects[0], RoomScale, RoomScale, RoomScale
    PositionEntity(r\objects[0], r\x - 248 * RoomScale, 528 * RoomScale, r\z, 0)
    EntityParent(r\objects[0], r\obj)
End Function


Function UpdateEventRoom2fan(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;eventstate1 = timer for turning the fan on/off
	;eventstate2 = fan on/off
	;eventstate3 = the speed of the fan
	If mainPlayer\currRoom = e\room Then
		TurnEntity (e\room\objects[0], e\eventState3*timing\tickDuration, 0, 0)
		If e\eventState3 > 0.01 Then
			e\room\soundCHN = LoopRangedSound (RoomAmbience[9], e\room\soundCHN, mainPlayer\cam, e\room\objects[0], 5.0, (e\eventState3/4.0))
		EndIf
		e\eventState3 = CurveValue(e\eventState2*5, e\eventState3, 150.0)
	EndIf

	If e\room\dist < 16.0 Then
		If e\eventState < 0 Then
			e\eventState = Rand(15,30)*70
			temp = e\eventState2
			e\eventState2 = Rand(0,1)
			If mainPlayer\currRoom<>e\room Then
				e\eventState3 = e\eventState2*5
			Else
				If temp = 0 And e\eventState2 = 1.0 Then ;turn on the fan
					PlayRangedSound (LoadTempSound("SFX/ambient/Room ambience/FanOn.ogg"), mainPlayer\cam, e\room\objects[0], 8.0)
				ElseIf temp = 1 And e\eventState2 = 0.0 Then ;turn off the fan
					PlayRangedSound (LoadTempSound("SFX/ambient/Room ambience/FanOff.ogg"), mainPlayer\cam, e\room\objects[0], 8.0)
				EndIf
			EndIf
		Else
			e\eventState = e\eventState-timing\tickDuration
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
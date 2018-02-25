

Function UpdateEventTestroom173(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room	
		If Curr173\Idle = 0 Then 
			If e\EventState = 0 Then
				If e\room\RoomDoors[0]\open = True
				PositionEntity(Curr173\Collider, EntityX(e\room\Objects[0], True), 0.5, EntityZ(e\room\Objects[0], True))
				ResetEntity(Curr173\Collider)
				e\EventState = 1
				EndIf
			Else
				ShowEntity (e\room\Objects[2])
				;start a timer for 173 breaking through the window
				e\EventState = e\EventState + 1
				dist# = EntityDistance(mainPlayer\collider, e\room\Objects[1])
				If dist <1.0 Then
					;if close, increase the timer so that 173 is ready to attack
					e\EventState = Max(e\EventState, 70*12)
				ElseIf dist > 1.4
					;if the player moves a bit further and blinks, 173 attacks
					If e\EventState > 70*12 And mainPlayer\blinkTimer =< -10 Then
						If (EntityDistance(Curr173\Collider, e\room\Objects[0]) > 5.0) Then
							;if 173 is far away from the room (perhaps because the player 
							;left and 173 moved to some other room?) -> disable the event
							RemoveEvent(e)
						Else
							PlaySound2(LoadTempSound("SFX\General\GlassBreak.ogg"), mainPlayer\cam, Curr173\obj) 
							FreeEntity(e\room\Objects[2])
							PositionEntity(Curr173\Collider, EntityX(e\room\Objects[1], True), 0.5, EntityZ(e\room\Objects[1], True))
							ResetEntity(Curr173\Collider)
							RemoveEvent(e)
						EndIf
					EndIf	
				EndIf
			End If
		EndIf
	End If	
	;[End Block]
End Function


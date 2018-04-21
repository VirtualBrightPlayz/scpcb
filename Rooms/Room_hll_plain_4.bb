

Function UpdateEvent_hll_plain_4(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\EventState < TimeInPosMilliSecs() Then
		If mainPlayer\currRoom <> e\room Then
			If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\obj),EntityZ(e\room\obj))<16.0 Then
				For n.NPCs = Each NPCs
					If n\NPCtype = NPCtype049 Then
						If n\State = 2 And EntityDistance(mainPlayer\collider,n\Collider)>16.0 Then
							TFormVector(368, 528, 176, e\room\obj, 0)
							PositionEntity n\Collider, EntityX(e\room\obj)+TFormedX(), TFormedY(), EntityZ(e\room\obj)+TFormedZ()
							DebugLog TFormedX()+", "+ TFormedY()+", "+ TFormedZ()
							ResetEntity n\Collider
							n\PathStatus = 0
							n\State = 4
							n\State2 = 0
							RemoveEvent(e)
						EndIf
						Exit
					EndIf
				Next
			EndIf
		EndIf
		If e<>Null Then e\EventState = TimeInPosMilliSecs()+5000
	EndIf
	;[End Block]
End Function


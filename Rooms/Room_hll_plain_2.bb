Function UpdateEventRoom2trick(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If EntityDistance(e\room\obj,mainPlayer\collider)<2.0 Then
			If EntityDistance(mainPlayer\collider, Curr173\obj)<6.0 Or EntityDistance(mainPlayer\collider, Curr106\obj)<6.0 Then
				RemoveEvent(e)
			Else
				DebugLog "%@@= \ {2E6C2=FD gi`h]c"
				
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider)
				PointEntity pvt, e\room\obj
				RotateEntity pvt, 0, EntityYaw(pvt),0,True
				MoveEntity pvt, 0,0,EntityDistance(pvt,e\room\obj)*2
				
				;mainPlayer\camShake = 5.0
				;mainPlayer\blurTimer = 100
				
				mainPlayer\blinkTimer = -10
				
				PlaySound2 DripSFX(0);HorrorSFX(11)
				
				PositionEntity mainPlayer\collider, EntityX(pvt),EntityY(pvt)+0.05,EntityZ(pvt)
				UpdateWorld()
				;ResetEntity Collider
				
				TurnEntity mainPlayer\collider, 0,180,0
				
				FreeEntity pvt
				RemoveEvent(e)							
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D

Function FillRoom_tnnl_pipes_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0]= CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 368.0 * RoomScale, 0.0, r\z, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x - 368.0 * RoomScale, 0.0, r\z, True)
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x + 224.0 * RoomScale - 0.005, 192.0 * RoomScale, r\z, True)
    
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x - 224.0 * RoomScale + 0.005, 192.0 * RoomScale, r\z, True)
End Function


Function UpdateEventRoom2pipes106(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (Not Contained106) Then 
		If e\EventState = 0 Then
			If mainPlayer\currRoom = e\room Then e\EventState = 1
		Else
			e\EventState=(e\EventState+timing\tickDuration*0.7)
			;0-50 = walk to the middle
			;50-200 = look around
			;200-250 = leave
			If e\EventState < 50 Then
				Curr106\Idle = True
				PositionEntity(Curr106\collider, EntityX(e\room\Objects[0], True), EntityY(mainPlayer\collider) - 0.15, EntityZ(e\room\Objects[0], True))
				PointEntity(Curr106\collider, e\room\Objects[1])
				MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\Objects[0], e\room\Objects[1])*0.5 * (e\EventState / 50.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			ElseIf e\EventState < 200 Then
				Curr106\Idle = True
				AnimateNPC(Curr106, 334, 494, 0.2)
				
				PositionEntity(Curr106\collider, (EntityX(e\room\Objects[0], True)+EntityX(e\room\Objects[1], True))/2, EntityY(mainPlayer\collider) - 0.15, (EntityZ(e\room\Objects[0], True)+EntityZ(e\room\Objects[1], True))/2)
				;MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\Objects[0], e\room\Objects[1])*0.5)
				RotateEntity(Curr106\collider,0, CurveValue(e\EventState,EntityYaw(Curr106\collider),30.0),0,True)
				If EntityDistance(Curr106\collider, mainPlayer\collider)<4.0 Then
					pvt = CreatePivot()
					PositionEntity(pvt, EntityX(Curr106\collider),EntityY(Curr106\collider),EntityZ(Curr106\collider))
					PointEntity pvt, mainPlayer\collider
					If WrapAngle(EntityYaw(pvt)-EntityYaw(Curr106\collider))<80 Then
						Curr106\state = -11
						Curr106\Idle = False
						;PlaySound2(HorrorSFX(10)) ;TODO: fix
						e\EventState = 260
					EndIf
					FreeEntity pvt
				EndIf
			ElseIf e\EventState < 250 Then
				Curr106\Idle = True
				PositionEntity(Curr106\collider, EntityX(e\room\Objects[0], True), EntityY(mainPlayer\collider) - 0.15, EntityZ(e\room\Objects[0], True))
				PointEntity(Curr106\collider, e\room\Objects[1])
				;200-250     (- 150)      50-100
				MoveEntity(Curr106\collider, 0, 0, EntityDistance(e\room\Objects[0], e\room\Objects[1]) * ((e\EventState-150.0) / 100.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			EndIf
			ResetEntity(Curr106\collider)
			
			PositionEntity(Curr106\obj, EntityX(Curr106\collider), EntityY(Curr106\collider) - 0.15, EntityZ(Curr106\collider))
			RotateEntity Curr106\obj, 0, EntityYaw(Curr106\collider), 0
			
			;PositionEntity(Curr106\collider, EntityX(Curr106\collider), EntityY(mainPlayer\collider) - 0.20, EntityZ(Curr106\collider))
			
			If (e\EventState / 250.0) > 0.3 And ((e\EventState - timing\tickDuration*0.7) / 250.0) <= 0.3 Then
				;e\soundChannels[0] = PlaySound2(HorrorSFX(6))
				mainPlayer\blurTimer = 800
				d.Decals = CreateDecal(0, EntityX(e\room\Objects[2], True), EntityY(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True), 0, e\room\angle - 90, Rnd(360)) ;90, Rnd(360), 0
				d\Timer = 90000
				d\Alpha = 0.01 : d\AlphaChange = 0.005
				d\Size = 0.1 : d\SizeChange = 0.003
			EndIf
			
			If (e\EventState / 250.0) > 0.65 And ((e\EventState - timing\tickDuration*0.7) / 250.0) <= 0.65 Then
				d.Decals = CreateDecal(0, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True), EntityZ(e\room\Objects[3], True), 0, e\room\angle + 90, Rnd(360))
				d\Timer = 90000
				d\Alpha = 0.01 : d\AlphaChange = 0.005
				d\Size = 0.1 : d\SizeChange = 0.003
			EndIf						
			
			If e\EventState > 250 Then Curr106\Idle = False :RemoveEvent(e)
			
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
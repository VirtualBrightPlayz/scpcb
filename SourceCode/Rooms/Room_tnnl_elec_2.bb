Function FillRoom_tnnl_elec_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x, 544.0 * RoomScale, r\z + 512.0 * RoomScale, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x, 544.0 * RoomScale, r\z - 512.0 * RoomScale, True)
End Function


Function UpdateEventTunnel2smoke(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If e\room\dist < 3.5 Then
			PlayRangedSound(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"), mainPlayer\cam, e\room\obj) 
			For i = 0 To 1
				em.Emitters = CreateEmitter(EntityX(e\room\Objects[i],True),EntityY(e\room\Objects[i],True), EntityZ(e\room\Objects[i],True),0)
				TurnEntity(em\Obj, 90, 0, 0, True)
				EntityParent(em\Obj, e\room\obj)
				em\Size = 0.05
				em\RandAngle = 10
				em\Speed = 0.06
				em\SizeChange = 0.007
				;EntityParent(em\Obj, e\room\obj)
				
				For z = 0 To 10
					p.Particles = CreateParticle(EntityX(em\Obj, True), 448*RoomScale, EntityZ(em\Obj, True), Rand(em\MinImage, em\MaxImage), em\Size, em\Gravity, em\LifeTime)
					p\speed = em\Speed
					RotateEntity(p\pvt, Rnd(360), Rnd(360), 0, True)
					p\size = 0.05
					p\SizeChange = 0.008
				Next
				
			Next
			RemoveEvent(e)
		End If					
	EndIf
	
	;[End Block]
End Function



Function UpdateEvent_tnnl_elec_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If Curr173\Idle = 2 Then
			RemoveEvent(e)
		Else		
			If e\EventState = 0 Then
				If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\obj), EntityZ(e\room\obj)) < 3.5 Then
					PlaySound_SM(sndManager\lightSwitch)
					
					;LightBlink = Rnd(0.0,1.0)*(e\EventState/200)
					e\EventState = 1
				End If
			ElseIf e\EventState < 200
				
				mainPlayer\blinkTimer = -10
				If e\EventState > 30 Then 
					;LightBlink = 1.0 
					If e\EventState-timing\tickDuration =< 30 Then 
						PlaySound2 LoadTempSound("SFX/ambient/general/ambient3.ogg")
					EndIf
				EndIf
				If e\EventState-timing\tickDuration =< 100 And e\EventState > 100 Then
					PlaySound2 LoadTempSound("SFX/ambient/general/ambient6.ogg")
					PositionEntity(Curr173\Collider, EntityX(e\room\obj), 0.6, EntityZ(e\room\obj))
					ResetEntity(Curr173\Collider)					
					Curr173\Idle = True		
				EndIf
				;LightBlink = 1.0
				e\EventState = e\EventState + timing\tickDuration
			Else
				mainPlayer\blinkTimer = mainPlayer\blinkFreq
				
				Curr173\Idle = False
				RemoveEvent(e)
			End If	
		EndIf
	EndIf					
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
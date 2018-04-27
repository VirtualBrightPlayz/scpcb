

Function UpdateEvent106sinkhole(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\EventState=0 Then
		de.Decals = CreateDecal(0, EntityX(e\room\obj)+Rnd(-0.5,0.5), 0.01, EntityZ(e\room\obj)+Rnd(-0.5,0.5), 90, Rand(360), 0)
		de\Size = 2.5 : ScaleSprite(de\obj, de\Size, de\Size);
		
		e\EventState=1
	ElseIf mainPlayer\currRoom = e\room
		If e\sounds[0]=0 Then
			e\sounds[0]=LoadSound("SFX\Room\Sinkhole.ogg")
		Else
			e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\obj, 4.5, 1.5)
		EndIf
		dist = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\obj),EntityZ(e\room\obj))
		If dist < 2.0 Then
			mainPlayer\footstepOverride=1
			mainPlayer\moveSpeed = CurveValue(0.0, mainPlayer\moveSpeed, Max(dist*50,1.0))	
			mainPlayer\crouchState = (2.0-dist)/2.0
			
			If dist<0.5 Then
				If e\EventState2 = 0 Then
					PlaySound(LoadTempSound("SFX\Room\SinkholeFall.ogg"))
				EndIf
				
				mainPlayer\moveSpeed = CurveValue(0.0, mainPlayer\moveSpeed, Max(dist*50,1.0))
				
				x = CurveValue(EntityX(e\room\obj),EntityX(mainPlayer\collider),10.0)
				y = CurveValue(EntityY(e\room\obj)-e\EventState2,EntityY(mainPlayer\collider),25.0)
				z = CurveValue(EntityZ(e\room\obj),EntityZ(mainPlayer\collider),10.0)
				PositionEntity mainPlayer\collider, x, y, z, True
				
				mainPlayer\dropSpeed = 0
				
				ResetEntity mainPlayer\collider
				
				e\EventState2=Min(e\EventState2+timing\tickDuration/200.0,2.0)
				
				;LightBlink = Min(e\EventState2*5,10.0)
				mainPlayer\blurTimer = e\EventState2*500
				
				If e\EventState2 = 2.0 Then MoveToPocketDimension()
			EndIf
		EndIf
	Else 
		e\EventState2=0
	EndIf
	
	;[End Block]
	
End Function


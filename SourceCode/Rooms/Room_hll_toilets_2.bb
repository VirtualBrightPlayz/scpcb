Function FillRoom_hll_toilets_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x + 1040.0 * RoomScale, 192.0 * RoomScale, r\z)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x + 1312.0*RoomScale, 0.5, r\z+448.0*RoomScale)
    EntityParent(r\Objects[1], r\obj)			
    
    r\Objects[2] = CreatePivot()
    PositionEntity(r\Objects[2], r\x + 1248.0*RoomScale, 0.01, r\z+384.0*RoomScale)
    EntityParent(r\Objects[2], r\obj)
End Function

Function UpdateEventToiletguard(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\EventState = 0 Then
		If e\room\dist < 8.0  And e\room\dist > 0 Then e\EventState = 1
	ElseIf e\EventState = 1 Then
		e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True)+0.5, EntityZ(e\room\Objects[1],True))
		PointEntity e\room\NPC[0]\Collider, e\room\obj
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider)-20,0, True
		
		SetNPCFrame (e\room\NPC[0], 906)
		e\room\NPC[0]\State = 8
		
		e\EventState = 2	
	Else
		If e\sounds[0] = 0 Then e\sounds[0] = LoadSound("SFX/Character/Guard/SuicideGuard1.ogg")
		If e\room\dist < 15.0 And e\room\dist >= 4.0 Then 
			e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\NPC[0]\Collider, 15.0)
			
		ElseIf e\room\dist<4.0 And mainPlayer\loudness > 1.0 Then
			de.Decals = CreateDecal(3,  EntityX(e\room\Objects[2],True), 0.01, EntityZ(e\room\Objects[2],True),90,Rnd(360),0)
			de\Size = 0.3 : ScaleSprite (de\obj, de\size, de\size)
			
			de.Decals = CreateDecal(17,  EntityX(e\room\Objects[2],True), 0.01, EntityZ(e\room\Objects[2],True),90,Rnd(360),0)
			de\Size = 0.1 : de\maxsize = 0.45 : de\sizechange = 0.0002 : UpdateDecals()
			
			;FreeSound e\sounds[0]
			;StopChannel e\soundChannels[0]
			;LoadEventSound(e,"SFX/Character/Guard/SuicideGuard2.ogg",1)
			If e\soundChannels[1] = 0 Then
				StopChannel(e\soundChannels[0])
				FreeSound(e\sounds[0])
				e\room\NPC[0]\sounds[0] = LoadSound("SFX/Character/Guard/SuicideGuard2.ogg")
				e\soundChannels[1] = PlayRangedSound(e\room\NPC[0]\sounds[0], mainPlayer\cam, e\room\NPC[0]\Collider, 15.0)
			EndIf
			UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\NPC[0]\Collider,15.0)
			If (Not IsChannelPlaying(e\soundChannels[1])) Then RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
	
End Function



Function UpdateEventButtghost(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If EntityDistance(mainPlayer\collider, e\room\Objects[0]) < 1.8 Then
			If e\EventState = 0 Then
				e\soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/Joke/789J.ogg"), mainPlayer\cam,e\room\Objects[0])
				e\EventState = 1
			Else
				If (Not IsChannelPlaying(e\soundChannels[0])) Then
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
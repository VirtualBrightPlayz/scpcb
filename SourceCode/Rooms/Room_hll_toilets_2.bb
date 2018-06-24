Function FillRoom_hll_toilets_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0], r\x + 1040.0 * RoomScale, 192.0 * RoomScale, r\z)
    EntityParent(r\objects[0], r\obj)

    r\objects[1] = CreatePivot()
    PositionEntity(r\objects[1], r\x + 1312.0*RoomScale, 0.5, r\z+448.0*RoomScale)
    EntityParent(r\objects[1], r\obj)

    r\objects[2] = CreatePivot()
    PositionEntity(r\objects[2], r\x + 1248.0*RoomScale, 0.01, r\z+384.0*RoomScale)
    EntityParent(r\objects[2], r\obj)
End Function

Function UpdateEventToiletguard(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams, de.Decals

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState = 0) Then
		If (e\room\dist < 8.0  And e\room\dist > 0) Then e\eventState = 1
	ElseIf (e\eventState = 1) Then
		e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[1],True), EntityY(e\room\objects[1],True)+0.5, EntityZ(e\room\objects[1],True))
		PointEntity(e\room\npc[0]\collider, e\room\obj)
		RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider)-20,0, True)

		SetNPCFrame(e\room\npc[0], 906)
		e\room\npc[0]\state = 8

		e\eventState = 2
	Else
		If (e\sounds[0] = 0) Then e\sounds[0] = LoadSound("SFX/Character/Guard/SuicideGuard1.ogg")
		If (e\room\dist < 15.0 And e\room\dist >= 4.0) Then
			e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\npc[0]\collider, 15.0)

		ElseIf (e\room\dist<4.0 And mainPlayer\loudness > 1.0) Then
			de = CreateDecal(3,  EntityX(e\room\objects[2],True), 0.01, EntityZ(e\room\objects[2],True),90,Rnd(360),0)
			de\size = 0.3 : ScaleSprite(de\obj, de\size, de\size)

			de = CreateDecal(17, EntityX(e\room\objects[2],True), 0.01, EntityZ(e\room\objects[2],True),90,Rnd(360),0)
			de\size = 0.1 : de\maxSize = 0.45 : de\sizeChange = 0.0002 : UpdateDecals()

			;FreeSound(e\sounds[0])
			;StopChannel(e\soundChannels[0])
			;LoadEventSound(e,"SFX/Character/Guard/SuicideGuard2.ogg",1)
			If (e\soundChannels[1] = 0) Then
				StopChannel(e\soundChannels[0])
				FreeSound(e\sounds[0])
				e\room\npc[0]\sounds[0] = LoadSound("SFX/Character/Guard/SuicideGuard2.ogg")
				e\soundChannels[1] = PlayRangedSound(e\room\npc[0]\sounds[0], mainPlayer\cam, e\room\npc[0]\collider, 15.0)
			EndIf
			UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\npc[0]\collider,15.0)
			If ((Not IsChannelPlaying(e\soundChannels[1]))) Then RemoveEvent(e)
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
	If (mainPlayer\currRoom = e\room) Then
		If (EntityDistance(mainPlayer\collider, e\room\objects[0]) < 1.8) Then
			If (e\eventState = 0) Then
				e\soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/Joke/789J.ogg"), mainPlayer\cam,e\room\objects[0])
				e\eventState = 1
			Else
				If ((Not IsChannelPlaying(e\soundChannels[0]))) Then
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
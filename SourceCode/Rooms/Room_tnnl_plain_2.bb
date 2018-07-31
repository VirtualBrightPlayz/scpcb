

Function UpdateEventTunnel106(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, d.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState = 0) Then
		If (e\room\dist < 5.0 And e\room\dist > 0) Then
			If (Curr106\state >= 0) Then e\eventState = 1
		ElseIf ((Contained106)) Then
			RemoveEvent(e)
		EndIf
	ElseIf ((e\eventState = 1)) Then

		If (e\room\dist < 3.0 Or Rand(7000)=1) Then
			e\eventState = 2
			d = CreateDecal(DECAL_CORROSION, EntityX(e\room\obj), 445.0*RoomScale, EntityZ(e\room\obj), -90, Rand(360), 0)
			d\size = Rnd(0.5, 0.7) : EntityAlpha(d\obj, 0.7) : d\id = 1 : ScaleSprite(d\obj, d\size, d\size)
			EntityAlpha(d\obj, Rnd(0.7, 0.85))

			;PlaySound2(HorrorSFX(10))
		ElseIf ((e\room\dist > 8.0)) Then
			If (Rand(5) = 1) Then
				Curr106\idle = False
				RemoveEvent(e)
			Else
				Curr106\idle = False
				Curr106\state = -10000
				RemoveEvent(e)
			EndIf
		EndIf
	Else
		e\eventState = e\eventState+timing\tickDuration

		PositionEntity(Curr106\collider, EntityX(e\room\obj, True) - Sin(TimeInPosMilliSecs() / 150.0) / 4.0, EntityY(mainPlayer\collider) + 1.0 - Min(Sin(e\eventState)*1.5,1.1), EntityZ(e\room\obj, True) - Sin(TimeInPosMilliSecs() / 190.0) / 4.0)

		;TranslateEntity(Curr106\collider, 0, -Max((3.0-dist),0), 0, True)
		PointEntity(Curr106\collider, mainPlayer\cam)
		Curr106\state = -11
		AnimateNPC(Curr106, 55, 104, 0.1)
		Curr106\idle = True

		If (e\eventState > 180) Then
			Curr106\idle = False
			PositionEntity(Curr106\collider, EntityX(Curr106\collider), -3.0, EntityZ(Curr106\collider), True)

			RemoveEvent(e)
		EndIf

	EndIf
	;[End Block]

End Function



Function UpdateEvent682roar(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState = 0) Then
		If (mainPlayer\currRoom = e\room) Then e\eventState = 70 * Rand(300,1000)
	ElseIf ((mainPlayer\currRoom\roomTemplate\name <> "pocketdimension" And mainPlayer\currRoom\roomTemplate\name <> "room860" And mainPlayer\currRoom\roomTemplate\name <> "room1123" And mainPlayer\currRoom\roomTemplate\name <> "dimension1499")) Then
		e\eventState = e\eventState-timing\tickDuration

		If (e\eventState < 17*70) Then
			If (e\eventState+timing\tickDuration => 17*70) Then
				LoadEventSound(e,"SFX/SCP/682/Roar.ogg") : e\soundChannels[0] = PlaySound(e\sounds[0]) ;e\sounds[0] = LoadSound("SFX/SCP/682/Roar.ogg")
			EndIf
			If (e\eventState > 17*70 - 3*70) Then mainPlayer\camShake = 0.5
			If (e\eventState < 17*70 - 7.5*70 And e\eventState > 17*70 - 11*70) Then mainPlayer\camShake = 2.0
			If (e\eventState < 70) Then
				If (e\sounds[0]<>0) Then FreeSound(e\sounds[0])
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]

End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
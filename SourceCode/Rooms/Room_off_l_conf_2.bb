Function FillRoom_off_l_conf_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1;, Bump

    d = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z + 48.0 * RoomScale, 270, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 176.0 * RoomScale,True)
    PositionEntity(d\buttons[1], r\x + 256.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True),True)
    d\autoClose = False : d\open = False

    r\roomDoors[0] = CreateDoor(r\zone, r\x - 432.0 * RoomScale, 0.0, r\z, 90, r, False, False, 0, "1234")
    PositionEntity(r\roomDoors[0]\buttons[0], r\x - 416.0 * RoomScale, EntityY(r\roomDoors[0]\buttons[0],True), r\z + 176.0 * RoomScale,True)
    FreeEntity r\roomDoors[0]\buttons[1] : r\roomDoors[0]\buttons[1] = 0
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = False : r\roomDoors[0]\locked = True

    de.Decals = CreateDecal(0, r\x - 808.0 * RoomScale, 0.005, r\z - 72.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    de.Decals = CreateDecal(2, r\x - 808.0 * RoomScale, 0.01, r\z - 72.0 * RoomScale, 90, Rand(360), 0)
    de\size = 0.3 : ScaleSprite(de\obj, de\size, de\size) : EntityParent(de\obj, r\obj)

    de.Decals = CreateDecal(0, r\x - 432.0 * RoomScale, 0.01, r\z, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x - 808.0 * RoomScale, 1.0, r\z - 72.0 * RoomScale, True)

    it = CreatePaper("docL1", r\x - 688.0 * RoomScale, 1.0, r\z - 16.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreatePaper("docL5", r\x - 808.0 * RoomScale, 1.0, r\z - 72.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_off_l_conf_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If e\eventState = 0 Then
			If e\room\roomDoors[0]\open = True Then
				If e\room\roomDoors[0]\openstate = 180 Then
					e\eventState = 1
					;TODO: load temp sound.
					;TODO: fix
					;PlaySound2 HorrorSFX(5)
				EndIf
			Else
				If (EntityDistance(mainPlayer\collider, e\room\roomDoors[0]\obj)<1.5) And (RemoteDoorOn) Then
					e\room\roomDoors[0]\open = True
				EndIf
			EndIf
		Else
			If EntityDistance(e\room\objects[0], mainPlayer\collider) < 2.0 Then
				;HeartBeatVolume = CurveValue(0.5, HeartBeatVolume, 5)
				mainPlayer\heartbeatIntensity = CurveValue(120, mainPlayer\heartbeatIntensity, 150)
				;TODO: fix
				;e\soundChannels[0] = LoopRangedSound(OldManSFX(4), e\soundChannels[0], mainPlayer\cam, e\room\obj, 5.0, 0.3)
				Curr106\state=Curr106\state-timing\tickDuration*3
			EndIf

		EndIf
	EndIf
	;[End Block]
End Function

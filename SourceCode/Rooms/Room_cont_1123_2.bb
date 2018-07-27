Function FillRoom_cont_1123_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    it = CreatePaper("doc1123", r\x + 511.0 * RoomScale, r\y + 125.0 * RoomScale, r\z - 936.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("gasmask", r\x + 457.0 * RoomScale, r\y + 150.0 * RoomScale, r\z + 960.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    d = CreateDoor(r\x + 832.0 * RoomScale, 0.0, r\z + 367.0 * RoomScale, 0, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    PositionEntity(d\buttons[0], r\x + 956.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 352.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 713.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 384.0 * RoomScale, True)
    FreeEntity(d\obj2)
	d\obj2 = 0
    d = CreateDoor(r\x + 280.0 * RoomScale, 0.0, r\z - 607.0 * RoomScale, 90, r, False)
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    d = CreateDoor(r\x + 280.0 * RoomScale, 512.0 * RoomScale, r\z - 607.0 * RoomScale, 90, r, False)
    PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    FreeEntity(d\buttons[1])
	d\buttons[1]=0
    r\doors[0] = d
    ;PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)

    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x + 832.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 784.0 * RoomScale, True)
    r\objects[4] = CreatePivot(r\obj)
    PositionEntity(r\objects[4], r\x -648.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 692.0 * RoomScale, True)
    r\objects[5] = CreatePivot(r\obj)
    PositionEntity(r\objects[5], r\x + 828.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 592.0 * RoomScale, True)

    r\objects[6] = CreatePivot(r\obj)
    PositionEntity(r\objects[6], r\x - 76.0 * RoomScale, r\y + 620.0 * RoomScale, r\z + 744.0 * RoomScale, True)
    r\objects[7] = CreatePivot(r\obj)
    PositionEntity(r\objects[7], r\x - 640.0 * RoomScale, r\y + 620.0 * RoomScale, r\z - 864.0 * RoomScale, True)

    r\objects[8] = LoadMesh("GFX/Map/forest/door_frame.b3d")
    PositionEntity(r\objects[8], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 288.0 * RoomScale,True)
    RotateEntity(r\objects[8],0,90,0,True)
    ScaleEntity(r\objects[8],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True)
    EntityParent(r\objects[8],r\obj)

    r\objects[9] =  LoadMesh("GFX/Map/forest/door.b3d")
    PositionEntity(r\objects[9],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (288.0-70) * RoomScale,True)
    RotateEntity(r\objects[9],0,10,0,True)
    EntityType(r\objects[9], HIT_MAP)
    ScaleEntity(r\objects[9],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True)
    EntityParent(r\objects[9],r\obj)

    r\objects[10] = CopyEntity(r\objects[8])
    PositionEntity(r\objects[10], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 736.0 * RoomScale,True)
    RotateEntity(r\objects[10],0,90,0,True)
    ScaleEntity(r\objects[10],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True)
    EntityParent(r\objects[10],r\obj)

    r\objects[11] =  CopyEntity(r\objects[9])
    PositionEntity(r\objects[11],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (736.0-70) * RoomScale,True)
    RotateEntity(r\objects[11],0,90,0,True)
    EntityType(r\objects[11], HIT_MAP)
    ScaleEntity(r\objects[11],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True)
    EntityParent(r\objects[11],r\obj)

    r\objects[12] = CopyEntity(r\objects[8])
    PositionEntity(r\objects[12], r\x - 592.0 * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True)
    RotateEntity(r\objects[12],0,0,0,True)
    ScaleEntity(r\objects[12],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True)
    EntityParent(r\objects[12],r\obj)

    r\objects[13] =  CopyEntity(r\objects[9])
    PositionEntity(r\objects[13],r\x - (592.0+70.0) * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True)
    RotateEntity(r\objects[13],0,0,0,True)
    EntityType(r\objects[13], HIT_MAP)
    ScaleEntity(r\objects[13],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True)
    EntityParent(r\objects[13],r\obj)

    ;r\objects[14] = LoadMesh("GFX/Map/1123_hb.b3d",r\obj)
    ;EntityPickMode(r\objects[14],2)
    ;EntityType(r\objects[14],HIT_MAP)
    ;EntityAlpha(r\objects[14],0.0)
End Function


Function UpdateEvent_cont_1123_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, nazi%, scale#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		;the event is started when the player picks up SCP-1123 (in Items.bb/UpdateItems())

		If (e\eventState = 1) Then
			;If (e\soundChannels[0] = 0) Then
			;	e\soundChannels[0] =
			;Else
			;	If (Not IsChannelPlaying(e\soundChannels[0])) Then e\soundChannels[0] = PlaySound2(moddedambience)
			;EndIf

			;Saving injuries and bloodloss, so that the player won't be healed automatically
			PrevInjuries = mainPlayer\injuries
			PrevBloodloss = mainPlayer\bloodloss

			e\room\npc[0] = CreateNPC(NPCtypeD, EntityX(e\room\objects[6],True),EntityY(e\room\objects[6],True),EntityZ(e\room\objects[6],True))
			;e\room\npc[1] = CreateNPC(NPCtypeD, EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True),EntityZ(e\room\objects[7],True))

			nazi = LoadAnimMesh("GFX/npcs/naziofficer.b3d")
			scale = 0.5 / MeshWidth(nazi)

			FreeEntity(e\room\npc[0]\obj)
			e\room\npc[0]\obj = CopyEntity(nazi)
			ScaleEntity(e\room\npc[0]\obj, scale, scale, scale)

			;FreeEntity(e\room\npc[1]\obj)
			;e\room\npc[1]\obj = CopyEntity(nazi)
			;ScaleEntity(e\room\npc[1]\obj, scale, scale, scale)

			FreeEntity(nazi)

			PositionEntity(mainPlayer\collider, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True),EntityZ(e\room\objects[4],True),True)
			ResetEntity(mainPlayer\collider)
			;PlaySound2(HorrorSFX(9))
			mainPlayer\camShake = 1.0
			mainPlayer\blurTimer = 1200
			mainPlayer\injuries = 1.0
			e\eventState = 2

		ElseIf (e\eventState = 2) Then
			e\eventState2 = e\eventState2 + timing\tickDuration

			PointEntity(e\room\npc[0]\collider, mainPlayer\collider)
			mainPlayer\blurTimer = Max(mainPlayer\blurTimer, 100)

			If (e\eventState2>200 And e\eventState2-timing\tickDuration<=200) Then
				;e\sounds[0] = LoadSound("SFX/Music/1123.ogg");TODO: fix
				e\soundChannels[0] = PlaySound(e\sounds[0])
			EndIf


			If (e\eventState2 > 1000) Then
				If (e\sounds[1]=0) Then
					e\sounds[1] = LoadSound("SFX/Door/1123DoorOpen.ogg")
					e\soundChannels[1] = PlaySound(e\sounds[1])
				EndIf
				RotateEntity(e\room\objects[11], 0, CurveAngle(10, EntityYaw(e\room\objects[11],0), 40), 0)

				If (e\eventState2>=1040 And e\eventState2-timing\tickDuration<1040) Then
					PlayRangedSound(LoadTempSound("SFX/SCP/1123/Officer1.ogg"), mainPlayer\cam, e\room\npc[0]\obj)
				ElseIf (e\eventState2>=1400 And e\eventState2-timing\tickDuration<1400) Then
					PlayRangedSound(LoadTempSound("SFX/SCP/1123/Officer2.ogg"), mainPlayer\cam, e\room\npc[0]\obj)
				EndIf

				;Animate2(e\room\objects[4], AnimTime(e\room\objects[4]), 0, 8, 0.1, False)
				If (EntityDistance(mainPlayer\collider, e\room\objects[4])>392*RoomScale) Then
					mainPlayer\blinkTimer = -10
					mainPlayer\blurTimer = 500
					PositionEntity(mainPlayer\collider, EntityX(e\room\objects[5],True),EntityY(e\room\objects[5],True),EntityZ(e\room\objects[5],True),True)
					RotateEntity(mainPlayer\collider, 0, EntityYaw(e\room\obj,True)+180, 0)
					ResetEntity(mainPlayer\collider)
					e\eventState = 3
				EndIf

			EndIf
		ElseIf (e\eventState=3) Then
			If (e\room\doors[0]\openstate>160) Then
				If (e\sounds[0]=0) Then e\sounds[0] = LoadSound("SFX/Music/1123.ogg")
				e\soundChannels[0] = PlaySound(e\sounds[0])

				PositionEntity(e\room\npc[0]\collider, EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True),EntityZ(e\room\objects[7],True))
				ResetEntity(e\room\npc[0]\collider)

				e\eventState=4
			EndIf
		ElseIf (e\eventState=4) Then

			;PointEntity(e\room\npc[1]\collider, mainPlayer\collider)

			TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,e\room\obj)

			If (TFormedX()<256 And TFormedZ()>-480) Then
				e\room\doors[0]\open = False
			EndIf

			If (EntityYaw(e\room\objects[13],False)=0) Then
				DebugLog("aaaaaaaa")
				If (EntityDistance(mainPlayer\collider, e\room\objects[12])<1.0) Then
					DrawHandIcon = True
					If (MouseHit1) Then
						RotateEntity(e\room\objects[13], 0, 1, 0, False)
						PlaySound2(LoadTempSound("SFX/SCP/1123/Horror.ogg"))
					EndIf
				EndIf
			Else
				DebugLog("bbbbbbb")
				RotateEntity(e\room\objects[13], 0, CurveAngle(90, EntityYaw(e\room\objects[13], False), 40), 0)
				If (EntityYaw(e\room\objects[13], False)>30) Then
					e\room\npc[0]\state = 3
					PointEntity(e\room\npc[0]\collider, mainPlayer\collider)
					AnimateNPC(e\room\npc[0], 570, 596, 0.5, False)
					If (e\room\npc[0]\frame => 596) Then
						e\eventState = 5
						e\eventState2 = 0
						PositionEntity(mainPlayer\collider, EntityX(e\room\obj,True),0.3,EntityZ(e\room\obj,True),True)
						ResetEntity(mainPlayer\collider)
						mainPlayer\blinkTimer = -10
						mainPlayer\blurTimer = 500
						mainPlayer\injuries = 1.5
						mainPlayer\bloodloss = 70
					;PlaySound2(LoadTempSound("SFX/Door/WoodenDoorClose.ogg"))
					EndIf
				EndIf

			EndIf

			;RemoveNPC(e\room\npc[0])
			;RemoveNPC(e\room\npc[1])
			;RemoveEvent(e)
		ElseIf (e\eventState = 5) Then
			e\eventState2 = e\eventState2 + timing\tickDuration
			If (e\eventState2 > 500) Then
				RotateEntity(e\room\objects[9],0,90,0,False)
				RotateEntity(e\room\objects[13],0,0,0,False)

				x = (EntityX(e\room\objects[8], True)+EntityX(e\room\objects[12], True))/2
				y = EntityY(e\room\objects[5], True)
				z = (EntityZ(e\room\objects[8], True)+EntityZ(e\room\objects[12], True))/2
				PositionEntity(mainPlayer\collider, x,y,z, True)
				ResetEntity(mainPlayer\collider)

				x = (EntityX(mainPlayer\collider, True)+EntityX(e\room\objects[12], True))/2
				z = (EntityZ(mainPlayer\collider, True)+EntityZ(e\room\objects[12], True))/2

				PositionEntity(e\room\npc[0]\collider, x,y+0.2,z)
				ResetEntity(e\room\npc[0]\collider)

				mainPlayer\injuries = 1.5
				mainPlayer\bloodloss = 70

				mainPlayer\blinkTimer = -10

				de = CreateDecal(DECAL_BLOOD_SPLATTER, EntityX(mainPlayer\collider), 512*RoomScale + 0.0005, EntityZ(mainPlayer\collider),90,Rnd(360),0)
				de\size = 0.5 : ScaleSprite(de\obj, de\size, de\size)

				e\room\npc[0]\sounds[0] = LoadSound("SFX/SCP/1123/Officer3.ogg")

				e\eventState = 6
			EndIf
		ElseIf (e\eventState = 6) Then
			PointEntity(e\room\npc[0]\collider, mainPlayer\collider)

			If (e\room\npc[0]\sounds[0]<>0) Then
				If (e\room\npc[0]\soundChannels[0]<>0) Then
					If (Not IsChannelPlaying(e\room\npc[0]\soundChannels[0])) Then
						PlaySound2(LoadTempSound("SFX/SCP/1123/Gunshot.ogg"))
						e\eventState = 7
						FreeSound(e\room\npc[0]\sounds[0])
						e\room\npc[0]\sounds[0]=0
					EndIf
				EndIf

				If (e\room\npc[0]\sounds[0]<>0) Then e\room\npc[0]\soundChannels[0] = LoopRangedSound(e\room\npc[0]\sounds[0], e\room\npc[0]\soundChannels[0], mainPlayer\cam, e\room\npc[0]\collider, 7.0)
			EndIf
		ElseIf (e\eventState=7) Then
			PositionEntity(mainPlayer\collider, EntityX(e\room\obj,True),0.3,EntityZ(e\room\obj,True),True)
			ResetEntity(mainPlayer\collider)
			ShowEntity(mainPlayer\overlays[OVERLAY_WHITE])
			mainPlayer\lightFlash = 6
			mainPlayer\blurTimer = 500
			mainPlayer\injuries = PrevInjuries
			mainPlayer\bloodloss = PrevBloodloss
			mainPlayer\crouching = False

			PrevInjuries = 0
			PrevBloodloss = 0

			For i = 0 To mainPlayer\inventory\size-1
				If (mainPlayer\inventory\items[i] <> Null) Then
					If (mainPlayer\inventory\items[i]\template\name = "Leaflet") Then
						mainPlayer\inventory\items[i] = Null
						RemoveItem(mainPlayer\inventory\items[i])
						Exit
					EndIf
				EndIf
			Next

			RemoveNPC(e\room\npc[0])
			RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]


End Function



;~IDEal Editor Parameters:
;~C#Blitz3D
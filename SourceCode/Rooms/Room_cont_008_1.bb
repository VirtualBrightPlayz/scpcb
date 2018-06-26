Function FillRoom_cont_008_1(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    ;the container
    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 292.0 * RoomScale, 130.0*RoomScale, r\z + 516.0 * RoomScale, True)

    ;the lid of the container
    r\objects[1] = LoadMesh("GFX/Map/008_2.b3d")
    ScaleEntity(r\objects[1], RoomScale, RoomScale, RoomScale)
    PositionEntity(r\objects[1], r\x + 292 * RoomScale, 151 * RoomScale, r\z + 576.0 * RoomScale, 0)
    EntityParent(r\objects[1], r\obj)

    RotateEntity(r\objects[1],89,0,0,True)

    Local Glasstex% = LoadTexture("GFX/Map/Textures/glass.png",1+2)
    r\objects[2] = CreateSprite()
    EntityTexture(r\objects[2],Glasstex)
    SpriteViewMode(r\objects[2],2)
    ScaleSprite(r\objects[2],256.0*RoomScale*0.5, 194.0*RoomScale*0.5)
    PositionEntity(r\objects[2], r\x - 176.0 * RoomScale, 224.0*RoomScale, r\z + 448.0 * RoomScale)
    TurnEntity(r\objects[2],0,90,0)
    EntityParent(r\objects[2], r\obj)

    FreeTexture(Glasstex)

    ;scp-173 spawnpoint
    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x - 445.0 * RoomScale, 120.0*RoomScale, r\z + 544.0 * RoomScale, True)

    ;scp-173 attack point
    r\objects[4] = CreatePivot(r\obj)
    PositionEntity(r\objects[4], r\x + 67.0 * RoomScale, 120.0*RoomScale, r\z + 464.0 * RoomScale, True)

    r\objects[5] = CreateSprite()
    PositionEntity(r\objects[5], r\x - 158 * RoomScale, 368 * RoomScale, r\z + 298.0 * RoomScale)
    ScaleSprite(r\objects[5], 0.02, 0.02)
    EntityTexture(r\objects[5], LightSpriteTex(1))
    EntityBlend(r\objects[5], 3)
    EntityParent(r\objects[5], r\obj)
    HideEntity(r\objects[5])

    d = CreateDoor(r\x + 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, DOOR_TYPE_DEF, r\roomTemplate\name)
    d\autoClose = False
    PositionEntity(d\buttons[1], r\x + 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    FreeEntity(d\buttons[0])
	d\buttons[0]=0
    FreeEntity(d\obj2)
	d\obj2=0
    r\doors[0] = d

    d2 = CreateDoor(r\x + 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
    d2\autoClose = False
    PositionEntity(d2\buttons[0], r\x + 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
    RotateEntity(d2\buttons[0], 0, -90, 0, True)
    PositionEntity(d2\buttons[1], r\x + 164.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 128.0 * RoomScale, True)
    FreeEntity(d2\obj2)
	d2\obj2=0
    r\doors[1] = d2

    d\linkedDoor = d2
    d2\linkedDoor = d

    d = CreateDoor(r\x - 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 0, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    d\autoClose = False : d\locked = True : r\doors[2]=d


    it = CreateItem("hazmatsuit", r\x - 76.0 * RoomScale, 0.5, r\z - 396.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)

    it = CreatePaper("doc008", r\x - 245.0 * RoomScale, r\y + 192.0 * RoomScale, r\z + 368.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    ;spawnpoint for the scientist used in the "008 zombie scene"
    r\objects[6] = CreatePivot(r\obj)
    PositionEntity(r\objects[6], r\x + 160 * RoomScale, 672 * RoomScale, r\z - 384.0 * RoomScale, True)
    ;spawnpoint for the player
    r\objects[7] = CreatePivot(r\obj)
    PositionEntity(r\objects[7], r\x, 672 * RoomScale, r\z + 352.0 * RoomScale, True)

    sc = CreateSecurityCam(r\x+578.956*RoomScale, r\y+444.956*RoomScale, r\z+772.0*RoomScale, r)
    sc\angle = 135
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    sc\id = 7
End Function


Function UpdateEvent_cont_008_1(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (Curr173\idle<2) Then
			;container open
			If (e\eventState = 0) Then

				PositionEntity(Curr173\collider, EntityX(e\room\objects[3],True),0.5,EntityZ(e\room\objects[3],True),True)
				ResetEntity(Curr173\collider)

				e\eventState = 1
			ElseIf (e\eventState = 1) Then
				e\soundChannels[0] = LoopRangedSound(AlarmSFX(0), e\soundChannels[0], mainPlayer\cam, e\room\objects[0], 5.0)

				If ((TimeInPosMilliSecs() Mod 1000)<500) Then
					ShowEntity(e\room\objects[5])
				Else
					HideEntity(e\room\objects[5])
				EndIf

				dist = EntityDistance(mainPlayer\collider, e\room\objects[0])
				If (dist<2.0) Then
					e\room\doors[0]\locked = True
					e\room\doors[1]\locked = True

					If (e\eventState2=0) Then
						ShowEntity(e\room\objects[2])
						If (mainPlayer\blinkTimer<-10 And Curr173\idle = 0) Then
							PositionEntity(Curr173\collider, EntityX(e\room\objects[4],True),0.5,EntityZ(e\room\objects[4],True),True)
							ResetEntity(Curr173\collider)

							HideEntity(e\room\objects[2])

							If (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit")) Then
								mainPlayer\injuries=mainPlayer\injuries+0.1
								mainPlayer\infect008=1
								Msg = "The window shattered and a piece of glass cut your arm."
								MsgTimer = 70*8
							EndIf

							PlayRangedSound(LoadTempSound("SFX/General/GlassBreak.ogg"), mainPlayer\cam, e\room\objects[0])

							e\eventState2=1
						EndIf
					EndIf

					If (dist<1.0) Then
						If (EntityInView(e\room\objects[0], mainPlayer\cam)) Then
							DrawHandIcon = True

							If (MouseDown1) Then
								RotateEntity(e\room\objects[1], Max(Min(EntityPitch(e\room\objects[1])+Max(Min(-mouse_y_speed_1,10.0),-10), 89), 35), EntityYaw(e\room\objects[1]), 0)
							EndIf
						EndIf
					EndIf
				EndIf

				If (EntityPitch(e\room\objects[1],True)<40) Then
					e\eventState = 2
					PlaySound_SM(sndManager\lever)
				Else
					p = CreateParticle(EntityX(e\room\objects[0],True),EntityY(e\room\objects[0],True),EntityZ(e\room\objects[0],True), 6, 0.02, -0.12)
					RotateEntity(p\pvt,-90,0,0,True)
					TurnEntity(p\pvt, Rnd(-26,26), Rnd(-26,26), Rnd(360))

					p\sizeChange = 0.012
					p\aChange = -0.015
				EndIf
			Else
				HideEntity(e\room\objects[5])
				e\room\doors[0]\locked = False
				e\room\doors[1]\locked = False
				e\room\doors[2]\locked = False

				RotateEntity(e\room\objects[1],CurveAngle(1,EntityPitch(e\room\objects[1],True),15.0),EntityYaw(e\room\objects[1],True),0,True)

				If (EntityPitch(e\room\objects[1],True)=<1.0) Then
					RemoveEvent(e)
				EndIf
			EndIf
		Else
			p = CreateParticle(EntityX(e\room\objects[0],True),EntityY(e\room\objects[0],True),EntityZ(e\room\objects[0],True), 6, 0.02, -0.12)
			RotateEntity(p\pvt,-90,0,0,True)
			TurnEntity(p\pvt, Rnd(-26,26), Rnd(-26,26), Rnd(360))
		EndIf

	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
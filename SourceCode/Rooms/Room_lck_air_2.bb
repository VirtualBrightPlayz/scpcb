Const ROOM_LCKA2_TRIGGERPIVOT% = 0
Const ROOM_LCKA2_BROKENDOOR% = 1 ;TODO: remove?
Const ROOM_LCKA2_CORPSESPAWN% = 2
Const ROOM_LCKA2_PIPES% = 3 ;TODO: add to room mesh?

Function FillRoom_lck_air_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump
	Local bd_temp%

    If (r\roomTemplate\name = "lck_air_broke_2") Then
        r\objects[ROOM_LCKA2_CORPSESPAWN] = CreatePivot(r\obj)
        PositionEntity(r\objects[ROOM_LCKA2_CORPSESPAWN], r\x - 156.825*RoomScale, -37.3458*RoomScale, r\z+121.364*RoomScale, True)

        de = CreateDecal(DECAL_BLOOD_SPLATTER,  r\x - 156.825*RoomScale, -37.3458*RoomScale, r\z+121.364*RoomScale,90,Rnd(360),0)
        de\size = 0.5
        ScaleSprite(de\obj, de\size,de\size)
        EntityParent(de\obj, r\obj)
    EndIf

    r\doors[0] = CreateDoor(r\x + 336.0 * RoomScale, 0.0, r\z - 382.0 * RoomScale, 0, r)
    PositionEntity(r\doors[0]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\doors[0]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\doors[0]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\doors[0]\buttons[1],True), r\z - 606.679 * RoomScale, True)
	r\doors[0]\locked = True
    r\doors[0]\mtfClose = False

    r\doors[1] = CreateDoor(r\x + 336.0 * RoomScale, 0.0, r\z + 462.0 * RoomScale, 180, r)
    PositionEntity(r\doors[1]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\doors[1]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\doors[1]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\doors[1]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\doors[1]\locked = True
    r\doors[1]\mtfClose = False

    For r2 = Each Room
        If (r2<>r) Then
            If (r2\roomTemplate\name = "lck_air_2" Or r2\roomTemplate\name = "lck_air_broke_2") Then
                r\objects[ROOM_LCKA2_PIPES] = CopyEntity(r2\objects[ROOM_LCKA2_PIPES],r\obj) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If (r\objects[ROOM_LCKA2_PIPES]=0) Then r\objects[ROOM_LCKA2_PIPES] = LoadMesh("GFX/Map/room2gw_pipes.b3d",r\obj)
    EntityPickMode(r\objects[ROOM_LCKA2_PIPES],2)

    If (r\roomTemplate\name = "lck_air_2") Then
        r\objects[ROOM_LCKA2_TRIGGERPIVOT] = CreatePivot()
        ;PositionEntity(r\objects[ROOM_LCKA2_TRIGGERPIVOT],r\x-48.0*RoomScale,128.0*RoomScale,r\z+320.0*RoomScale)
        PositionEntity(r\objects[ROOM_LCKA2_TRIGGERPIVOT],r\x+344.0*RoomScale,128.0*RoomScale,r\z)
        EntityParent(r\objects[ROOM_LCKA2_TRIGGERPIVOT],r\obj)

        bd_temp = False
        If (room2gw_brokendoor) Then
            If (room2gw_x = r\x) Then
                If (room2gw_z = r\z) Then
                    bd_temp = True
                EndIf
            EndIf
        EndIf

        If ((room2gw_brokendoor = 0 And Rand(1,2)=1) Or bd_temp) Then
            r\objects[ROOM_LCKA2_BROKENDOOR] = LoadMesh("GFX/Map/Meshes/door.b3d") ;TODO: Not this.
            ScaleEntity(r\objects[ROOM_LCKA2_BROKENDOOR], (204.0 * RoomScale) / MeshWidth(r\objects[ROOM_LCKA2_BROKENDOOR]), 312.0 * RoomScale / MeshHeight(r\objects[ROOM_LCKA2_BROKENDOOR]), 16.0 * RoomScale / MeshDepth(r\objects[ROOM_LCKA2_BROKENDOOR]))
            EntityType(r\objects[ROOM_LCKA2_BROKENDOOR], HIT_MAP)
            PositionEntity(r\objects[ROOM_LCKA2_BROKENDOOR], r\x + 336.0 * RoomScale, 0.0, r\z + 462.0 * RoomScale)
            RotateEntity(r\objects[ROOM_LCKA2_BROKENDOOR], 0, 180 + 180, 0)
            EntityParent(r\objects[ROOM_LCKA2_BROKENDOOR], r\obj)
            MoveEntity(r\objects[ROOM_LCKA2_BROKENDOOR],120.0,0,5.0)
            room2gw_brokendoor = True
            room2gw_x = r\x
            room2gw_z = r\z
            FreeEntity(r\doors[1]\obj2)
			r\doors[1]\obj2 = 0
        EndIf
    EndIf
End Function

Const EVENT_LCKA2_ACTIVE% = 0
Const EVENT_LCKA2_LEAVING% = 1
Const EVENT_LCKA2_TIMER% = 0

Function UpdateEvent_lck_air_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, d_ent%

	;[Block]
	e\room\doors[0]\locked = True
	e\room\doors[1]\locked = True

	Local brokendoor% = False
	If (e\room\objects[ROOM_LCKA2_BROKENDOOR]<>0) Then brokendoor = True

	If (mainPlayer\currRoom = e\room) Then
		If (Not e\intState[EVENT_LCKA2_ACTIVE]) Then
			If (EntityDistance(e\room\objects[ROOM_LCKA2_TRIGGERPIVOT],mainPlayer\collider)<1.4 And (Not e\intState[EVENT_LCKA2_LEAVING])) Then
				e\intState[EVENT_LCKA2_ACTIVE] = True
				If (brokendoor) Then
					If (e\sounds[1] <> 0) Then
						FreeSound(e\sounds[1]) : e\sounds[1] = 0
					EndIf
					e\sounds[1] = LoadSound("SFX/Door/DoorSparks.ogg")
					e\soundChannels[1] = PlayRangedSound(e\sounds[1],mainPlayer\cam,e\room\objects[ROOM_LCKA2_BROKENDOOR],5)
				EndIf
				StopChannel(e\soundChannels[0])
				e\soundChannels[0] = 0
				If (e\sounds[0] <> 0) Then
					FreeSound(e\sounds[0]) : e\sounds[0] = 0
				EndIf
				e\sounds[0] = LoadSound("SFX/Door/Airlock.ogg")
				e\room\doors[0]\locked = False
				e\room\doors[1]\locked = False
				UseDoor(e\room\doors[0])
				UseDoor(e\room\doors[1])
			ElseIf (EntityDistance(e\room\objects[ROOM_LCKA2_TRIGGERPIVOT],mainPlayer\collider)>2.4) Then
				e\intState[EVENT_LCKA2_LEAVING] = False
			EndIf
		Else
			If (e\floatState[EVENT_LCKA2_TIMER] < 70*7) Then
				e\floatState[EVENT_LCKA2_TIMER] = e\floatState[EVENT_LCKA2_TIMER] + timing\tickDuration
				e\room\doors[0]\open = False
				e\room\doors[1]\open = False
				If (e\floatState[EVENT_LCKA2_TIMER] < 70*1) Then

					If (brokendoor) Then
						pvt = CreatePivot()
						d_ent = e\room\objects[ROOM_LCKA2_BROKENDOOR]
						PositionEntity(pvt, EntityX(d_ent,True), EntityY(d_ent,True)+Rnd(0.0,0.05), EntityZ(d_ent,True))
						RotateEntity(pvt, 0, EntityYaw(d_ent,True)+90, 0)
						MoveEntity(pvt,0,0,0.2)

						For i = 0 To 3
							p = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
							p\speed = Rnd(0.01,0.05)
							;RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
							RotateEntity(p\pvt, Rnd(-45,0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)

							p\size = 0.0075
							ScaleSprite(p\obj,p\size,p\size)

							;EntityOrder(p\obj,-1)

							p\aChange = -0.05
						Next

						FreeEntity(pvt)
					EndIf

				ElseIf (e\floatState[EVENT_LCKA2_TIMER] > 70*3 And e\floatState[EVENT_LCKA2_TIMER] < 70*5.5) Then
					pvt = CreatePivot(e\room\obj)
					For i = 0 To 1
						If (e\room\roomTemplate\name$ = "lck_ez_3") Then
							If (i = 0) Then
								PositionEntity(pvt,-288.0*RoomScale,416.0*RoomScale,320.0*RoomScale,False)
							Else
								PositionEntity(pvt,192.0*RoomScale,416.0*RoomScale,320.0*RoomScale,False)
							EndIf
						Else
							If (i = 0) Then
								PositionEntity(pvt,312.0*RoomScale,416.0*RoomScale,-128.0*RoomScale,False)
							Else
								PositionEntity(pvt,312.0*RoomScale,416.0*RoomScale,224.0*RoomScale,False)
							EndIf
						EndIf

						p = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True), 6, 0.8, 0, 50)
						p\speed = 0.025
						RotateEntity(p\pvt, 90, 0, 0)

						p\aChange = -0.02
					Next

					FreeEntity(pvt)
					If (e\soundChannels[0] = 0) Then e\soundChannels[0] = PlayRangedSound(e\sounds[0],mainPlayer\cam,e\room\objects[ROOM_LCKA2_TRIGGERPIVOT],5)
				EndIf
			Else
				e\floatState[EVENT_LCKA2_TIMER] = 0.0
				e\intState[EVENT_LCKA2_ACTIVE] = False
				e\intState[EVENT_LCKA2_LEAVING] = True
				If (e\room\doors[0]\open = False) Then
					e\room\doors[0]\locked = False
					e\room\doors[1]\locked = False
					UseDoor(e\room\doors[0])
					UseDoor(e\room\doors[1])
				EndIf
			EndIf
		EndIf

		If (brokendoor) Then
			If (IsChannelPlaying(e\soundChannels[1])) Then
				UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\objects[ROOM_LCKA2_BROKENDOOR],5)
			EndIf
		EndIf
		If (IsChannelPlaying(e\soundChannels[0])) Then
			UpdateRangedSoundOrigin(e\soundChannels[0],mainPlayer\cam,e\room\objects[ROOM_LCKA2_TRIGGERPIVOT],5)
		EndIf
	Else
		e\intState[EVENT_LCKA2_LEAVING] = False
	EndIf
	;[End Block]
End Function

Const EVENT_LCKA2_SPAWNEDCORPSE% = 0

Function UpdateEvent_lck_air_broke_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\room\dist < 8) Then
		If (Not e\intState[EVENT_LCKA2_SPAWNEDCORPSE]) Then
			e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[ROOM_LCKA2_CORPSESPAWN],True), EntityY(e\room\objects[ROOM_LCKA2_CORPSESPAWN],True)+0.5, EntityZ(e\room\objects[ROOM_LCKA2_CORPSESPAWN],True))
			PointEntity(e\room\npc[0]\collider, e\room\obj)
			RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider),0, True)
			SetNPCFrame(e\room\npc[0], 906)
			e\room\npc[0]\state = 8

			e\intState[EVENT_LCKA2_SPAWNEDCORPSE] = True
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
Function FillRoom_lck_air_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1;, Bump

    If r\roomTemplate\name = "room2gw_b" Then
        r\objects[2] = CreatePivot(r\obj)
        PositionEntity (r\objects[2], r\x - 156.825*RoomScale, -37.3458*RoomScale, r\z+121.364*RoomScale, True)

        de.Decals = CreateDecal(3,  r\x - 156.825*RoomScale, -37.3458*RoomScale, r\z+121.364*RoomScale,90,Rnd(360),0)
        de\size = 0.5
        ScaleSprite(de\obj, de\size,de\size)
        EntityParent de\obj, r\obj
    EndIf

    r\roomDoors[0] = CreateDoor(r\zone, r\x + 336.0 * RoomScale, 0.0, r\z - 382.0 * RoomScale, 0, r, False, False)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[0]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[0]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\roomDoors[0]\dir = 0 : r\roomDoors[0]\autoClose = False	: r\roomDoors[0]\open = True  : r\roomDoors[0]\locked = True
    r\roomDoors[0]\mtfClose = False

    r\roomDoors[1] = CreateDoor(r\zone, r\x + 336.0 * RoomScale, 0.0, r\z + 462.0 * RoomScale, 180, r, False, False)
    PositionEntity(r\roomDoors[1]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[1]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\roomDoors[1]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[1]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\roomDoors[1]\dir = 0 : r\roomDoors[1]\autoClose = False	: r\roomDoors[1]\open = True  : r\roomDoors[1]\locked = True
    r\roomDoors[1]\mtfClose = False

    For r2.Rooms = Each Rooms
        If r2<>r Then
            If r2\roomTemplate\name = "lck_air_2" Or r2\roomTemplate\name = "lck_air_broke_2" Then
                r\objects[3] = CopyEntity(r2\objects[3],r\obj) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If r\objects[3]=0 Then r\objects[3] = LoadMesh("GFX/map/room2gw_pipes.b3d",r\obj)
    EntityPickMode r\objects[3],2

    If r\roomTemplate\name = "lck_air_2" Then
        r\objects[0] = CreatePivot()
        ;PositionEntity r\objects[0],r\x-48.0*RoomScale,128.0*RoomScale,r\z+320.0*RoomScale
        PositionEntity r\objects[0],r\x+344.0*RoomScale,128.0*RoomScale,r\z
        EntityParent r\objects[0],r\obj

        Local bd_temp% = False
        If room2gw_brokendoor Then
            If room2gw_x = r\x Then
                If room2gw_z = r\z Then
                    bd_temp% = True
                EndIf
            EndIf
        EndIf

        If (room2gw_brokendoor = 0 And Rand(1,2)=1) Or bd_temp% Then
            r\objects[1] = LoadMesh("GFX/Map/Meshes/door.b3d") ;TODO: Not this.
            ScaleEntity(r\objects[1], (204.0 * RoomScale) / MeshWidth(r\objects[1]), 312.0 * RoomScale / MeshHeight(r\objects[1]), 16.0 * RoomScale / MeshDepth(r\objects[1]))
            EntityType r\objects[1], HIT_MAP
            PositionEntity r\objects[1], r\x + 336.0 * RoomScale, 0.0, r\z + 462.0 * RoomScale
            RotateEntity(r\objects[1], 0, 180 + 180, 0)
            EntityParent(r\objects[1], r\obj)
            MoveEntity r\objects[1],120.0,0,5.0
            room2gw_brokendoor = True
            room2gw_x# = r\x
            room2gw_z# = r\z
            FreeEntity r\roomDoors[1]\obj2 : r\roomDoors[1]\obj2 = 0
        EndIf
    EndIf
End Function


Function UpdateEventRoom_gw(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\eventState: Determines if the airlock is in operation or not
	;e\eventState2: The timer for the airlocks
	;e\eventState3: Checks if the player had left the airlock or not

	e\room\roomDoors[0]\locked = True
	e\room\roomDoors[1]\locked = True

	Local brokendoor% = False
	If e\room\objects[1]<>0 Then brokendoor% = True

	If mainPlayer\currRoom = e\room Then
		If e\eventState = 0.0 Then
			If EntityDistance(e\room\objects[0],mainPlayer\collider)<1.4 And e\eventState3 = 0.0 Then
				e\eventState = 1.0
				If brokendoor Then
					If e\sounds[1] <> 0 Then FreeSound(e\sounds[1]) : e\sounds[1] = 0
					e\sounds[1] = LoadSound("SFX/Door/DoorSparks.ogg")
					e\soundChannels[1] = PlayRangedSound(e\sounds[1],mainPlayer\cam,e\room\objects[1],5)
				EndIf
				StopChannel e\soundChannels[0]
				e\soundChannels[0] = 0
				If e\sounds[0] <> 0 Then FreeSound(e\sounds[0]) : e\sounds[0] = 0
				e\sounds[0] = LoadSound("SFX/Door/Airlock.ogg")
				e\room\roomDoors[0]\locked = False
				e\room\roomDoors[1]\locked = False
				UseDoor(e\room\roomDoors[0])
				UseDoor(e\room\roomDoors[1])
			ElseIf EntityDistance(e\room\objects[0],mainPlayer\collider)>2.4 Then
				e\eventState3 = 0.0
			EndIf
		Else
			If e\eventState2 < 70*7 Then
				e\eventState2 = e\eventState2 + timing\tickDuration
				e\room\roomDoors[0]\open = False
				e\room\roomDoors[1]\open = False
				If e\eventState2 < 70*1 Then

					If brokendoor Then
						pvt% = CreatePivot()
						Local d_ent% = e\room\objects[1]
						PositionEntity(pvt, EntityX(d_ent%,True), EntityY(d_ent%,True)+Rnd(0.0,0.05), EntityZ(d_ent%,True))
						RotateEntity(pvt, 0, EntityYaw(d_ent%,True)+90, 0)
						MoveEntity pvt,0,0,0.2

						For i = 0 To 3
							p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
							p\speed = Rnd(0.01,0.05)
							;RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
							RotateEntity(p\pvt, Rnd(-45,0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)

							p\size = 0.0075
							ScaleSprite p\obj,p\size,p\size

							;EntityOrder p\obj,-1

							p\aChange = -0.05
						Next

						FreeEntity pvt
					EndIf

				ElseIf e\eventState2 > 70*3 And e\eventState < 70*5.5 Then
					pvt% = CreatePivot(e\room\obj)
					For i = 0 To 1
						If e\room\roomTemplate\name$ = "lck_ez_3" Then
							If i = 0 Then
								PositionEntity pvt%,-288.0*RoomScale,416.0*RoomScale,320.0*RoomScale,False
							Else
								PositionEntity pvt%,192.0*RoomScale,416.0*RoomScale,320.0*RoomScale,False
							EndIf
						Else
							If i = 0 Then
								PositionEntity pvt%,312.0*RoomScale,416.0*RoomScale,-128.0*RoomScale,False
							Else
								PositionEntity pvt%,312.0*RoomScale,416.0*RoomScale,224.0*RoomScale,False
							EndIf
						EndIf

						p.Particles = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True),  6, 0.8, 0, 50)
						p\speed = 0.025
						RotateEntity(p\pvt, 90, 0, 0)

						p\aChange = -0.02
					Next

					FreeEntity pvt
					If e\soundChannels[0] = 0 Then e\soundChannels[0] = PlayRangedSound(e\sounds[0],mainPlayer\cam,e\room\objects[0],5)
				EndIf
			Else
				e\eventState = 0.0
				e\eventState2 = 0.0
				e\eventState3 = 1.0
				If e\room\roomDoors[0]\open = False Then
					e\room\roomDoors[0]\locked = False
					e\room\roomDoors[1]\locked = False
					UseDoor(e\room\roomDoors[0])
					UseDoor(e\room\roomDoors[1])
				EndIf
			EndIf
		EndIf

		If brokendoor Then
			If IsChannelPlaying(e\soundChannels[1]) Then
				UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\objects[1],5)
			EndIf
		EndIf
		If IsChannelPlaying(e\soundChannels[0]) Then
			UpdateRangedSoundOrigin(e\soundChannels[0],mainPlayer\cam,e\room\objects[0],5)
		EndIf
	Else
		e\eventState3 = 0.0
	EndIf
	;[End Block]
End Function



Function UpdateEventRoom2gw_b(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\room\dist < 8 Then
		If e\eventState = 0 Then
			e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[2],True), EntityY(e\room\objects[2],True)+0.5, EntityZ(e\room\objects[2],True))
			PointEntity e\room\npc[0]\collider, e\room\obj
			RotateEntity e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider),0, True
			SetNPCFrame(e\room\npc[0], 906)
			e\room\npc[0]\state = 8

			e\eventState = 1
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
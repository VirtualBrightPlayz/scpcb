Function FillRoom_strg_939_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams, em.Emitters
	Local it.Items, i%, x#, z#
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x, 240.0 * RoomScale, r\z + 752.0 * RoomScale, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x + 5840.0 * RoomScale, -5392.0 * RoomScale, r\z + 1360.0 * RoomScale, True)

    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x + 608.0 * RoomScale, 240.0 * RoomScale, r\z - 624.0 * RoomScale, True)

    r\objects[3] = CreatePivot(r\obj)
    ;PositionEntity(r\objects[3], r\x + 720.0 * RoomScale, -5392.0 * RoomScale, r\z + 752.0 * RoomScale, True)
    PositionEntity(r\objects[3], r\x - 456.0 * RoomScale, -5392.0 * RoomScale, r\z - 1136 * RoomScale, True)

    ;"waypoints" # 1
    r\objects[4] = CreatePivot(r\obj)
    PositionEntity(r\objects[4], r\x + 2128.0 * RoomScale, -5550.0 * RoomScale, r\z + 2048.0 * RoomScale, True)

    r\objects[5] = CreatePivot(r\obj)
    PositionEntity(r\objects[5], r\x + 2128.0 * RoomScale, -5550.0 * RoomScale, r\z - 1136.0 * RoomScale, True)

    r\objects[6] = CreatePivot(r\obj)
    PositionEntity(r\objects[6], r\x + 3824.0 * RoomScale, -5550.0 * RoomScale, r\z - 1168.0 * RoomScale, True)

    r\objects[7] = CreatePivot(r\obj)
    PositionEntity(r\objects[7], r\x + 3760.0 * RoomScale, -5550.0 * RoomScale, r\z + 2048.0 * RoomScale, True)

    r\objects[8] = CreatePivot(r\obj)
    PositionEntity(r\objects[8], r\x + 4848.0 * RoomScale, -5550.0 * RoomScale, r\z + 112.0 * RoomScale, True)

    ;"waypoints" # 2
    r\objects[9] = CreatePivot(r\obj)
    PositionEntity(r\objects[9], r\x + 592.0 * RoomScale, -5550.0 * RoomScale, r\z + 6352.0 * RoomScale, True)

    r\objects[10] = CreatePivot(r\obj)
    PositionEntity(r\objects[10], r\x + 2928.0 * RoomScale, -5550.0 * RoomScale, r\z + 6352.0 * RoomScale, True)

    r\objects[11] = CreatePivot(r\obj)
    PositionEntity(r\objects[11], r\x + 2928.0 * RoomScale, -5550.0 * RoomScale, r\z + 5200.0 * RoomScale, True)

    r\objects[12] = CreatePivot(r\obj)
    PositionEntity(r\objects[12], r\x + 592.0 * RoomScale, -5550.0 * RoomScale, r\z + 5200.0 * RoomScale, True)

    ;"waypoints" # 3
    r\objects[13] = CreatePivot(r\obj)
    PositionEntity(r\objects[13], r\x + 1136.0 * RoomScale, -5550.0 * RoomScale, r\z + 2944.0 * RoomScale, True)

    r\objects[14] = CreatePivot(r\obj)
    PositionEntity(r\objects[14], r\x + 1104.0 * RoomScale, -5550.0 * RoomScale, r\z + 1184.0 * RoomScale, True)

    r\objects[15] = CreatePivot(r\obj)
    PositionEntity(r\objects[15], r\x - 464.0 * RoomScale,  -5550.0 * RoomScale, r\z + 1216.0 * RoomScale, True)

    r\objects[16] = CreatePivot(r\obj)
    PositionEntity(r\objects[16], r\x - 432.0 * RoomScale, -5550.0 * RoomScale, r\z + 2976.0 * RoomScale, True)

    ;r\objects[20] = LoadMesh("GFX/Map/room3storage_hb.b3d",r\obj)
    ;EntityPickMode(r\objects[20],2)
    ;EntityType(r\objects[20],HIT_MAP)
    ;EntityAlpha(r\objects[20],0.0)

    ;Doors
    r\roomDoors[0] = CreateDoor(r\zone, r\x, 0.0, r\z + 448.0 * RoomScale, 0, r, True)
    r\roomDoors[0]\autoClose = False : r\roomDoors[0]\open = True
    PositionEntity(r\roomDoors[0]\buttons[1], r\x - 160.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x + 160.0 * RoomScale, 0.7, r\z + 416.0 * RoomScale, True)

    r\roomDoors[1] = CreateDoor(r\zone, r\x + 5840.0 * RoomScale,  -5632.0 * RoomScale, r\z + 1048.0 * RoomScale, 0, r, False)
    r\roomDoors[1]\autoClose = False : r\roomDoors[1]\open = False
    PositionEntity(r\roomDoors[1]\buttons[0], r\x + 6000.0 * RoomScale, EntityY(r\roomDoors[1]\buttons[0],True), r\z + 1008.0 * RoomScale, True)
    PositionEntity(r\roomDoors[1]\buttons[1], r\x + 5680.0 * RoomScale, EntityY(r\roomDoors[1]\buttons[1],True), r\z + 1088.0 * RoomScale, True)

    r\roomDoors[2] = CreateDoor(r\zone, r\x + 608.0 * RoomScale, 0.0, r\z - 312.0 * RoomScale, 0, r, True)
    r\roomDoors[2]\autoClose = False : r\roomDoors[2]\open = True
    PositionEntity(r\roomDoors[2]\buttons[1], r\x + 448.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    PositionEntity(r\roomDoors[2]\buttons[0], r\x + 768.0 * RoomScale, 0.7, r\z - 352.0 * RoomScale, True)

    ;r\roomDoors[3] = CreateDoor(r\zone, r\x + 720.0 * RoomScale,  -5632.0 * RoomScale, r\z + 1064.0 * RoomScale, 0, r, False)
    ;PositionEntity(r\roomDoors[3]\buttons[0], r\x + 896.0 * RoomScale, EntityY(r\roomDoors[3]\buttons[0],True), r\z + 1024.0 * RoomScale, True)
    ;PositionEntity(r\roomDoors[3]\buttons[1], r\x + 544.0 * RoomScale, EntityY(r\roomDoors[3]\buttons[1],True), r\z + 1104.0 * RoomScale, True)
    r\roomDoors[3] = CreateDoor(r\zone, r\x - 456.0 * RoomScale,  -5632.0 * RoomScale, r\z - 824.0 * RoomScale, 0, r, False)
    r\roomDoors[3]\autoClose = False : r\roomDoors[3]\open = False
    ;X=+176 | Z=-40
    PositionEntity(r\roomDoors[3]\buttons[0], r\x - 280.0*RoomScale, EntityY(r\roomDoors[3]\buttons[0],True), r\z - 864.0 * RoomScale, True)
    ;X=-176 | Z=+40
    PositionEntity(r\roomDoors[3]\buttons[1], r\x - 632.0*RoomScale, EntityY(r\roomDoors[3]\buttons[1],True), r\z - 784.0 * RoomScale, True)

    em = CreateEmitter(r\x + 5218.0 * RoomScale, -5584.0*RoomScale, r\z - 600* RoomScale, 0)
    TurnEntity(em\obj, 20, -100, 0, True)
    EntityParent(em\obj, r\obj) : em\room = r
    em\randAngle = 15 : em\speed = 0.03
    em\sizeChange = 0.01 : em\aChange = -0.006
    em\gravity = -0.2

    Select Rand(3)
        Case 1
            x = 2312
            z = -952
        Case 2
            x = 3032
            z = 1288
        Case 3
            x = 2824
            z = 2808
    End Select

    it = CreateItem("nvgoggles", r\x + 1936.0 * RoomScale, r\y - 5496.0 * RoomScale, r\z - 944.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    de = CreateDecal(3,  r\x + x*RoomScale, -5632.0*RoomScale+0.01, r\z+z*RoomScale,90,Rnd(360),0)
    de\size = 0.5
    ScaleSprite(de\obj, de\size,de\size)
    EntityParent(de\obj, r\obj)

    ;Objects [20],[21],[22],[23]
	Local n%
    For n = 10 To 11
        r\levers[n-10] = CreateLever()

        ScaleEntity(r\levers[n-10]\obj, 0.04, 0.04, 0.04)
        ScaleEntity(r\levers[n-10]\baseObj, 0.04, 0.04, 0.04)

        If (n = 10) Then
            ;r\z+6578
            PositionEntity(r\levers[n-10]\obj,r\x+3101*RoomScale,r\y-5461*RoomScale,r\z+6568*RoomScale,True)
            PositionEntity(r\levers[n-10]\baseObj,r\x+3101*RoomScale,r\y-5461*RoomScale,r\z+6568*RoomScale,True)
        Else
            ;r\z+3174
            PositionEntity(r\levers[n-10]\obj,r\x+1209*RoomScale,r\y-5461*RoomScale,r\z+3164*RoomScale,True)
            PositionEntity(r\levers[n-10]\baseObj,r\x+1209*RoomScale,r\y-5461*RoomScale,r\z+3164*RoomScale,True)
        EndIf

        EntityParent(r\levers[n-10]\obj, r\obj)
        EntityParent(r\levers[n-10]\baseObj, r\obj)

        RotateEntity(r\levers[n-10]\baseObj, 0, 0, 0)
        RotateEntity(r\levers[n-10]\obj, -10, 0 - 180, 0)

        EntityPickMode(r\levers[n-10]\obj, 1, False)
        EntityRadius(r\levers[n-10]\obj, 0.1)
    Next

    r\roomDoors[4] = CreateDoor(r\zone,r\x+56*RoomScale,r\y-5632*RoomScale,r\z+6344*RoomScale,90,r,False,2)
    r\roomDoors[4]\autoClose = False : r\roomDoors[4]\open = False
    For i = 0 To 1
        FreeEntity(r\roomDoors[4]\buttons[i])
		r\roomDoors[4]\buttons[i] = 0
    Next

    d = CreateDoor(r\zone,r\x+1157.0*RoomScale,r\y-5632.0*RoomScale,r\z+660.0*RoomScale,0,r,False,2)
    d\locked = True : d\open = False : d\autoClose = False
    For i = 0 To 1
        FreeEntity(d\buttons[i])
		d\buttons[i]=0
    Next

    d = CreateDoor(r\zone,r\x+234.0*RoomScale,r\y-5632.0*RoomScale,r\z+5239.0*RoomScale,90,r,False,2)
    d\locked = True : d\open = False : d\autoClose = False
    For i = 0 To 1
        FreeEntity(d\buttons[i])
		d\buttons[i]=0
    Next

    d = CreateDoor(r\zone,r\x+3446.0*RoomScale,r\y-5632.0*RoomScale,r\z+6369.0*RoomScale,90,r,False,2)
    d\locked = True : d\open = False : d\autoClose = False
    For i = 0 To 1
        FreeEntity(d\buttons[i])
		d\buttons[i]=0
    Next
End Function


Function UpdateEvent_strg_939_3(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		e\eventState2 = UpdateElevators2(e\eventState2, e\room\roomDoors[0], e\room\roomDoors[1],e\room\objects[0],e\room\objects[1], e)

		e\eventState3 = UpdateElevators2(e\eventState3, e\room\roomDoors[2], e\room\roomDoors[3],e\room\objects[2],e\room\objects[3], e)

		If (EntityY(mainPlayer\collider)<-4600*RoomScale) Then

			;If (Music(7)=0) Then Music(7) = LoadSound("SFX/Music/Room3Storage.ogg") ;TODO: fix
			;ShouldPlay = 7

			;If (e\room\npc[0]=Null) Then
				;DrawLoading(0, True)
				;e\room\npc[0]=CreateNPC(NPCtype939, 0,0,0)

				;DrawLoading(20, True)
				;e\room\npc[1]=CreateNPC(NPCtype939, 0,0,0)

				;DrawLoading(50, True)
				;e\room\npc[2]=CreateNPC(NPCtype939, 0,0,0)

				;DrawLoading(100, True)
			;EndIf
			If (e\room\npc[2]=Null Or e\eventState = 3) Then
				If (e\eventState = 0) Then
					e\eventState = 1
				ElseIf (e\eventState = 1) Then
					e\room\npc[0]=CreateNPC(NPCtype939, 0,0,0)
					e\eventState = 2
				ElseIf (e\eventState = 2) Then
					e\room\npc[1]=CreateNPC(NPCtype939, 0,0,0)
					e\eventState = 3
				ElseIf (e\eventState = 3) Then
					e\room\npc[2]=CreateNPC(NPCtype939, 0,0,0)
					e\eventState = 0
				EndIf
			Else
				If (e\eventState = 0) Then
					;Instance 1
					PositionEntity(e\room\npc[0]\collider, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True)+0.2,EntityZ(e\room\objects[4],True))
					ResetEntity(e\room\npc[0]\collider)
					e\room\npc[0]\state = 2
					e\room\npc[0]\state2 = 5
					e\room\npc[0]\prevState = 7
					;Instance 2
					PositionEntity(e\room\npc[1]\collider, EntityX(e\room\objects[9],True),EntityY(e\room\objects[9],True)+0.2,EntityZ(e\room\objects[9],True))
					ResetEntity(e\room\npc[1]\collider)
					e\room\npc[1]\state = 2
					e\room\npc[1]\state2 = 10
					e\room\npc[1]\prevState = 12
					;Instance 3
					PositionEntity(e\room\npc[2]\collider, EntityX(e\room\objects[13],True),EntityY(e\room\objects[13],True)+0.2,EntityZ(e\room\objects[13],True))
					ResetEntity(e\room\npc[2]\collider)
					e\room\npc[2]\state = 2
					e\room\npc[2]\state2 = 14
					e\room\npc[2]\prevState = 16
					;Other
					e\eventState = 1
				EndIf

				If ((e\room\roomDoors[4]\open = False)) Then
					If ((e\room\levers[0]\succ Or e\room\levers[1]\succ)) Then
						e\room\roomDoors[4]\open = True
						If ((e\sounds[1] <> 0)) Then
                            FreeSound(e\sounds[1])
                            e\sounds[1] = 0
                        EndIf

						e\sounds[1] = LoadSound("SFX/Door/Door2Open1_dist.ogg")
						e\soundChannels[1] = PlayRangedSound(e\sounds[1],mainPlayer\cam,e\room\roomDoors[4]\obj,400)

                        e\room\levers[0]\locked = True
                        e\room\levers[1]\locked = True
					EndIf
				EndIf

				e\room\npc[0]\ignorePlayer = False
				e\room\npc[2]\ignorePlayer = False

				CurrTrigger = CheckTriggers()

				Select CurrTrigger
					Case "939-1_fix"
						e\room\npc[0]\ignorePlayer = True
					Case "939-3_fix"
						e\room\npc[2]\ignorePlayer = True
				End Select

				If (IsChannelPlaying(e\soundChannels[1])) Then
					UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\roomDoors[4]\obj,400)
				EndIf

				If (EntityY(mainPlayer\collider)<-6400*RoomScale And mainPlayer\dead = False) Then
					DeathMSG=""
					PlaySound2(LoadTempSound("SFX/Room/PocketDimension/Impact.ogg"))
					mainPlayer\dead = True
				EndIf
			EndIf
		Else
			e\eventState = 0
			If (e\room\npc[0]<>Null) Then e\room\npc[0]\state = 66
			If (e\room\npc[1]<>Null) Then e\room\npc[1]\state = 66
			If (e\room\npc[2]<>Null) Then e\room\npc[2]\state = 66
		EndIf
	Else
		If (e\room\npc[0]<>Null) Then e\room\npc[0]\state = 66
		If (e\room\npc[1]<>Null) Then e\room\npc[1]\state = 66
		If (e\room\npc[2]<>Null) Then e\room\npc[2]\state = 66
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
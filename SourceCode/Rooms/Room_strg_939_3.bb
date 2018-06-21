Function FillRoom_strg_939_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x, 240.0 * RoomScale, r\z + 752.0 * RoomScale, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x + 5840.0 * RoomScale, -5392.0 * RoomScale, r\z + 1360.0 * RoomScale, True)
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x + 608.0 * RoomScale, 240.0 * RoomScale, r\z - 624.0 * RoomScale, True)
    
    r\Objects[3] = CreatePivot(r\obj)
    ;PositionEntity(r\Objects[3], r\x + 720.0 * RoomScale, -5392.0 * RoomScale, r\z + 752.0 * RoomScale, True)
    PositionEntity(r\Objects[3], r\x - 456.0 * RoomScale, -5392.0 * RoomScale, r\z - 1136 * RoomScale, True)
    
    ;"waypoints" # 1
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x + 2128.0 * RoomScale, -5550.0 * RoomScale, r\z + 2048.0 * RoomScale, True)
    
    r\Objects[5] = CreatePivot(r\obj)
    PositionEntity(r\Objects[5], r\x + 2128.0 * RoomScale, -5550.0 * RoomScale, r\z - 1136.0 * RoomScale, True)
    
    r\Objects[6] = CreatePivot(r\obj)
    PositionEntity(r\Objects[6], r\x + 3824.0 * RoomScale, -5550.0 * RoomScale, r\z - 1168.0 * RoomScale, True)
    
    r\Objects[7] = CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x + 3760.0 * RoomScale, -5550.0 * RoomScale, r\z + 2048.0 * RoomScale, True)
    
    r\Objects[8] = CreatePivot(r\obj)
    PositionEntity(r\Objects[8], r\x + 4848.0 * RoomScale, -5550.0 * RoomScale, r\z + 112.0 * RoomScale, True)
    
    ;"waypoints" # 2
    r\Objects[9] = CreatePivot(r\obj)
    PositionEntity(r\Objects[9], r\x + 592.0 * RoomScale, -5550.0 * RoomScale, r\z + 6352.0 * RoomScale, True)
    
    r\Objects[10] = CreatePivot(r\obj)
    PositionEntity(r\Objects[10], r\x + 2928.0 * RoomScale, -5550.0 * RoomScale, r\z + 6352.0 * RoomScale, True)
    
    r\Objects[11] = CreatePivot(r\obj)
    PositionEntity(r\Objects[11], r\x + 2928.0 * RoomScale, -5550.0 * RoomScale, r\z + 5200.0 * RoomScale, True)
    
    r\Objects[12] = CreatePivot(r\obj)
    PositionEntity(r\Objects[12], r\x + 592.0 * RoomScale, -5550.0 * RoomScale, r\z + 5200.0 * RoomScale, True)
    
    ;"waypoints" # 3
    r\Objects[13] = CreatePivot(r\obj)
    PositionEntity(r\Objects[13], r\x + 1136.0 * RoomScale, -5550.0 * RoomScale, r\z + 2944.0 * RoomScale, True)
    
    r\Objects[14] = CreatePivot(r\obj)
    PositionEntity(r\Objects[14], r\x + 1104.0 * RoomScale, -5550.0 * RoomScale, r\z + 1184.0 * RoomScale, True)
    
    r\Objects[15] = CreatePivot(r\obj)
    PositionEntity(r\Objects[15], r\x - 464.0 * RoomScale,  -5550.0 * RoomScale, r\z + 1216.0 * RoomScale, True)
    
    r\Objects[16] = CreatePivot(r\obj)
    PositionEntity(r\Objects[16], r\x - 432.0 * RoomScale, -5550.0 * RoomScale, r\z + 2976.0 * RoomScale, True)
    
    ;r\Objects[20] = LoadMesh("GFX/map/room3storage_hb.b3d",r\obj)
    ;EntityPickMode r\Objects[20],2
    ;EntityType r\Objects[20],HIT_MAP
    ;EntityAlpha r\Objects[20],0.0
    
    ;Doors
    r\RoomDoors[0] = CreateDoor(r\zone, r\x, 0.0, r\z + 448.0 * RoomScale, 0, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x - 160.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 160.0 * RoomScale, 0.7, r\z + 416.0 * RoomScale, True)	
    
    r\RoomDoors[1] = CreateDoor(r\zone, r\x + 5840.0 * RoomScale,  -5632.0 * RoomScale, r\z + 1048.0 * RoomScale, 0, r, False)
    r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
    PositionEntity(r\RoomDoors[1]\buttons[0], r\x + 6000.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z + 1008.0 * RoomScale, True)					
    PositionEntity(r\RoomDoors[1]\buttons[1], r\x + 5680.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z + 1088.0 * RoomScale, True)
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x + 608.0 * RoomScale, 0.0, r\z - 312.0 * RoomScale, 0, r, True)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = True
    PositionEntity(r\RoomDoors[2]\buttons[1], r\x + 448.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)	
    PositionEntity(r\RoomDoors[2]\buttons[0], r\x + 768.0 * RoomScale, 0.7, r\z - 352.0 * RoomScale, True)
    
    ;r\RoomDoors[3] = CreateDoor(r\zone, r\x + 720.0 * RoomScale,  -5632.0 * RoomScale, r\z + 1064.0 * RoomScale, 0, r, False)
    ;PositionEntity(r\RoomDoors[3]\buttons[0], r\x + 896.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[0],True), r\z + 1024.0 * RoomScale, True)
    ;PositionEntity(r\RoomDoors[3]\buttons[1], r\x + 544.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[1],True), r\z + 1104.0 * RoomScale, True)
    r\RoomDoors[3] = CreateDoor(r\zone, r\x - 456.0 * RoomScale,  -5632.0 * RoomScale, r\z - 824.0 * RoomScale, 0, r, False)
    r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False
    ;X=+176 | Z=-40
    PositionEntity r\RoomDoors[3]\buttons[0], r\x - 280.0*RoomScale, EntityY(r\RoomDoors[3]\buttons[0],True), r\z - 864.0 * RoomScale, True
    ;X=-176 | Z=+40
    PositionEntity r\RoomDoors[3]\buttons[1], r\x - 632.0*RoomScale, EntityY(r\RoomDoors[3]\buttons[1],True), r\z - 784.0 * RoomScale, True
    
    em.Emitters = CreateEmitter(r\x + 5218.0 * RoomScale, -5584.0*RoomScale, r\z - 600* RoomScale, 0)
    TurnEntity(em\Obj, 20, -100, 0, True)
    EntityParent(em\Obj, r\obj) : em\Room = r
    em\RandAngle = 15 : em\Speed = 0.03
    em\SizeChange = 0.01 : em\Achange = -0.006
    em\Gravity = -0.2 
    
    Select Rand(3)
        Case 1
            x# = 2312
            z#=-952
        Case 2
            x# = 3032
            z#=1288
        Case 3
            x# = 2824
            z#=2808
    End Select
    
    it.Items = CreateItem("Black Severed Hand", "hand2", r\x + x*RoomScale, -5596.0*RoomScale+1.0, r\z+z*RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Night Vision Goggles", "nvgoggles", r\x + 1936.0 * RoomScale, r\y - 5496.0 * RoomScale, r\z - 944.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    de.Decals = CreateDecal(3,  r\x + x*RoomScale, -5632.0*RoomScale+0.01, r\z+z*RoomScale,90,Rnd(360),0)
    de\Size = 0.5
    ScaleSprite(de\obj, de\Size,de\Size)
    EntityParent de\obj, r\obj
    
    ;Objects [20],[21],[22],[23]
    For n% = 10 To 11
        r\Levers[n-10] = CreateLever()
        
        ScaleEntity(r\Levers[n-10]\obj, 0.04, 0.04, 0.04)
        ScaleEntity(r\Levers[n-10]\baseObj, 0.04, 0.04, 0.04)

        If n = 10 Then
            ;r\z+6578
            PositionEntity r\Levers[n-10]\obj,r\x+3101*RoomScale,r\y-5461*RoomScale,r\z+6568*RoomScale,True
            PositionEntity r\Levers[n-10]\baseObj,r\x+3101*RoomScale,r\y-5461*RoomScale,r\z+6568*RoomScale,True
        Else
            ;r\z+3174
            PositionEntity r\Levers[n-10]\obj,r\x+1209*RoomScale,r\y-5461*RoomScale,r\z+3164*RoomScale,True
            PositionEntity r\Levers[n-10]\baseObj,r\x+1209*RoomScale,r\y-5461*RoomScale,r\z+3164*RoomScale,True
        EndIf
        
        EntityParent(r\Levers[n-10]\obj, r\obj)
        EntityParent(r\Levers[n-10]\baseObj, r\obj)

        RotateEntity(r\Levers[n-10]\baseObj, 0, 0, 0)
        RotateEntity(r\Levers[n-10]\obj, -10, 0 - 180, 0)
        
        EntityPickMode r\Levers[n-10]\obj, 1, False
        EntityRadius r\Levers[n-10]\obj, 0.1
    Next
    
    r\RoomDoors[4] = CreateDoor(r\zone,r\x+56*RoomScale,r\y-5632*RoomScale,r\z+6344*RoomScale,90,r,False,2)
    r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False
    For i = 0 To 1
        FreeEntity r\RoomDoors[4]\buttons[i] : r\RoomDoors[4]\buttons[i] = 0
    Next
    
    d = CreateDoor(r\zone,r\x+1157.0*RoomScale,r\y-5632.0*RoomScale,r\z+660.0*RoomScale,0,r,False,2)
    d\locked = True : d\open = False : d\AutoClose = False
    For i = 0 To 1
        FreeEntity d\buttons[i] : d\buttons[i]=0
    Next
    
    d = CreateDoor(r\zone,r\x+234.0*RoomScale,r\y-5632.0*RoomScale,r\z+5239.0*RoomScale,90,r,False,2)
    d\locked = True : d\open = False : d\AutoClose = False
    For i = 0 To 1
        FreeEntity d\buttons[i] : d\buttons[i]=0
    Next
    
    d = CreateDoor(r\zone,r\x+3446.0*RoomScale,r\y-5632.0*RoomScale,r\z+6369.0*RoomScale,90,r,False,2)
    d\locked = True : d\open = False : d\AutoClose = False
    For i = 0 To 1
        FreeEntity d\buttons[i] : d\buttons[i]=0
    Next
End Function


Function UpdateEvent_strg_939_3(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		e\EventState2 = UpdateElevators2(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
		
		e\EventState3 = UpdateElevators2(e\EventState3, e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
		
		If EntityY(mainPlayer\collider)<-4600*RoomScale Then
			
			;If Music(7)=0 Then Music(7) = LoadSound("SFX/Music/Room3Storage.ogg") ;TODO: fix 
			ShouldPlay = 7
			
			;If e\room\NPC[0]=Null Then
				;DrawLoading(0, True)
				;e\room\NPC[0]=CreateNPC(NPCtype939, 0,0,0)
				
				;DrawLoading(20, True)
				;e\room\NPC[1]=CreateNPC(NPCtype939, 0,0,0)
				
				;DrawLoading(50, True)
				;e\room\NPC[2]=CreateNPC(NPCtype939, 0,0,0)
				
				;DrawLoading(100, True)
			;EndIf
			If e\room\NPC[2]=Null Or e\EventState = 3 Then
				If e\EventState = 0 Then
					e\EventState = 1
				ElseIf e\EventState = 1 Then
					e\room\NPC[0]=CreateNPC(NPCtype939, 0,0,0)
					e\EventState = 2
				ElseIf e\EventState = 2 Then
					e\room\NPC[1]=CreateNPC(NPCtype939, 0,0,0)
					e\EventState = 3
				ElseIf e\EventState = 3 Then
					e\room\NPC[2]=CreateNPC(NPCtype939, 0,0,0)
					e\EventState = 0
				EndIf
			Else
				If e\EventState = 0 Then
					;Instance 1
					PositionEntity(e\room\NPC[0]\collider, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True)+0.2,EntityZ(e\room\Objects[4],True))
					ResetEntity e\room\NPC[0]\collider
					e\room\NPC[0]\state = 2
					e\room\NPC[0]\state2 = 5
					e\room\NPC[0]\PrevState = 7
					;Instance 2
					PositionEntity(e\room\NPC[1]\collider, EntityX(e\room\Objects[9],True),EntityY(e\room\Objects[9],True)+0.2,EntityZ(e\room\Objects[9],True))
					ResetEntity e\room\NPC[1]\collider
					e\room\NPC[1]\state = 2
					e\room\NPC[1]\state2 = 10
					e\room\NPC[1]\PrevState = 12
					;Instance 3
					PositionEntity(e\room\NPC[2]\collider, EntityX(e\room\Objects[13],True),EntityY(e\room\Objects[13],True)+0.2,EntityZ(e\room\Objects[13],True))
					ResetEntity e\room\NPC[2]\collider
					e\room\NPC[2]\state = 2
					e\room\NPC[2]\state2 = 14
					e\room\NPC[2]\PrevState = 16
					;Other
					e\EventState = 1
				EndIf
				
				If (e\room\RoomDoors[4]\open = False) Then
					If (e\room\Levers[0]\succ Or e\room\Levers[1]\succ) Then
						e\room\RoomDoors[4]\open = True
						If (e\sounds[1] <> 0) Then
                            FreeSound e\sounds[1]
                            e\sounds[1] = 0
                        EndIf

						e\sounds[1] = LoadSound("SFX/Door/Door2Open1_dist.ogg")
						e\soundChannels[1] = PlayRangedSound(e\sounds[1],mainPlayer\cam,e\room\RoomDoors[4]\obj,400)

                        e\room\Levers[0]\locked = True
                        e\room\Levers[1]\locked = True
					EndIf
				EndIf
				
				e\room\NPC[0]\IgnorePlayer = False
				e\room\NPC[2]\IgnorePlayer = False
				
				CurrTrigger$ = CheckTriggers()
				
				Select CurrTrigger$
					Case "939-1_fix"
						e\room\NPC[0]\IgnorePlayer = True
					Case "939-3_fix"
						e\room\NPC[2]\IgnorePlayer = True
				End Select
				
				If IsChannelPlaying(e\soundChannels[1]) Then
					UpdateRangedSoundOrigin(e\soundChannels[1],mainPlayer\cam,e\room\RoomDoors[4]\obj,400)
				EndIf
				
				If EntityY(mainPlayer\collider)<-6400*RoomScale And mainPlayer\dead = False Then
					DeathMSG=""
					PlaySound2 LoadTempSound("SFX/Room/PocketDimension/Impact.ogg")
					mainPlayer\dead = True
				EndIf
			EndIf
		Else
			e\EventState = 0
			If e\room\NPC[0]<>Null Then e\room\NPC[0]\state = 66
			If e\room\NPC[1]<>Null Then e\room\NPC[1]\state = 66
			If e\room\NPC[2]<>Null Then e\room\NPC[2]\state = 66
		EndIf
	Else
		If e\room\NPC[0]<>Null Then e\room\NPC[0]\state = 66
		If e\room\NPC[1]<>Null Then e\room\NPC[1]\state = 66
		If e\room\NPC[2]<>Null Then e\room\NPC[2]\state = 66
	EndIf 
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
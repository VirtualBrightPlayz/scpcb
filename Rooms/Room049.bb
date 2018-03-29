Function FillRoom049(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 640.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x - 2032.0 * RoomScale, -3280.0 * RoomScale, r\z - 656.0 * RoomScale, True)
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x - 640.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale, True)
    
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 2040.0 * RoomScale, -3280.0 * RoomScale, r\z + 656.0 * RoomScale, True)
    
    ;storage room (the spawn point of scp-049)
    ;r\Objects[5] = CreatePivot(r\obj)
    ;PositionEntity(r\Objects[5], r\x + 584.0 * RoomScale, -3440.0 * RoomScale, r\z + 104.0 * RoomScale, True)
    
    ;zombie 1 and 049
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x + 528.0 * RoomScale, -3440.0 * RoomScale, r\z + 96.0 * RoomScale, True)
    ;zombie 2
    r\Objects[5] = CreatePivot(r\obj)
    PositionEntity(r\Objects[5], r\x  + 64.0 * RoomScale, -3440.0 * RoomScale, r\z - 1000.0 * RoomScale, True)
    
    For n% = 0 To 1
        r\Objects[n * 2 + 6] = CopyEntity(LeverBaseOBJ)
        r\Objects[n * 2 + 7] = CopyEntity(LeverOBJ)
        
        r\Levers[n] = r\Objects[n * 2 + 7]
        
        For i% = 0 To 1
            ScaleEntity(r\Objects[n * 2 + 6 + i], 0.03, 0.03, 0.03)
            
            Select n
                Case 0 ;power feed
                    PositionEntity (r\Objects[n * 2 + 6 + i], r\x - 328.0 * RoomScale, r\y - 3374.0 * RoomScale, r\z + 916 * RoomScale, True)
                    
                Case 1 ;generator
                    PositionEntity (r\Objects[n * 2 + 6 + i], r\x - 370.0 * RoomScale, r\y - 3400.0 * RoomScale, r\z - 799 * RoomScale, True)
                    
            End Select
            
            EntityParent(r\Objects[n * 2 + 6 + i], r\obj)
        Next
        
        RotateEntity(r\Objects[n*2+6], 0, -180*n, 0)
        RotateEntity(r\Objects[n*2+7], 81-92*n, -180*(Not n), 0)
        
        EntityPickMode r\Objects[n * 2 + 7], 1, False
        EntityRadius r\Objects[n * 2 + 7], 0.1
    Next
    
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 328.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 288.0 * RoomScale, 0.7, r\z + 512.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 368.0 * RoomScale, 0.7, r\z + 840.0 * RoomScale, True)			
    
    r\RoomDoors[1] = CreateDoor(r\zone, r\x - 2328.0 * RoomScale, -3520.0 * RoomScale, r\z - 656.0 * RoomScale, 90, r, False)
    r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False	
    PositionEntity(r\RoomDoors[1]\buttons[1], r\x - 2432.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z - 816.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[1]\buttons[0], r\x - 2304.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z - 472.0 * RoomScale, True)				
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 328.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = True
    PositionEntity(r\RoomDoors[2]\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 512.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[2]\buttons[1], r\x - 368.0 * RoomScale, 0.7, r\z - 840.0 * RoomScale, True)				
    
    r\RoomDoors[3] = CreateDoor(r\zone, r\x + 2360.0 * RoomScale, -3520.0 * RoomScale, r\z + 656.0 * RoomScale, 90, r, False)
    r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False		
    PositionEntity(r\RoomDoors[3]\buttons[0], r\x + 2432.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[0],True), r\z + 816.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[3]\buttons[1], r\x + 2312.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[1],True), r\z + 472.0 * RoomScale, True)	
    
    For i = 0 To 3
        If (i Mod 2) = 1
            AssignElevatorObj(r\Objects[i],r\RoomDoors[i],2)
        Else
            AssignElevatorObj(r\Objects[i],r\RoomDoors[i],True)
        EndIf
    Next
    
    ;storage room door
    r\RoomDoors[4] = CreateDoor(r\zone, r\x + 272.0 * RoomScale, -3552.0 * RoomScale, r\z + 104.0 * RoomScale, 90, r, False)
    r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False : r\RoomDoors[4]\locked = True
    
    d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, -2)
    
    it = CreateItem("Document SCP-049", "paper", r\x - 608.0 * RoomScale, r\y - 3332.0 * RoomScale, r\z + 876.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Level 4 Key Card", "key4", r\x - 512.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 864.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("First Aid Kit", "firstaid", r\x +385.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 271.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    r\Objects[10] = LoadMesh_Strict("GFX\map\room049_hb.b3d",r\obj)
    EntityPickMode r\Objects[10],2
    EntityType r\Objects[10],HIT_MAP
    EntityAlpha r\Objects[10],0.0
End Function


Function UpdateEventRoom049(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If EntityY(mainPlayer\collider) > -2848*RoomScale Then
			e\EventState2 = UpdateElevators(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
			e\EventState3 = UpdateElevators(e\EventState3, e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
		Else
			If Music(8)=0 Then Music(8) = LoadSound_Strict("SFX\Music\Room049.ogg") 
			ShouldPlay = 8
			
			If e\EventState = 0 Then
				If e\EventStr = ""
					QuickLoadPercent = 0
					e\EventStr = "load0"
				ElseIf e\EventStr = "load0"
					n.NPCs = CreateNPC(NPCtypeZombie, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
					PointEntity n\Collider, e\room\obj
					TurnEntity n\Collider, 0, 190, 0
					QuickLoadPercent = 20
					e\EventStr = "load1"
				ElseIf e\EventStr = "load1"
					n.NPCs = CreateNPC(NPCtypeZombie, EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True),EntityZ(e\room\Objects[5],True))
					PointEntity n\Collider, e\room\obj
					TurnEntity n\Collider, 0, 20, 0
					QuickLoadPercent = 60
					e\EventStr = "load2"
				ElseIf e\EventStr = "load2"
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype049
							e\room\NPC[0]=n
							e\room\NPC[0]\State = 2
							e\room\NPC[0]\Idle = 1
							PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True)+3,EntityZ(e\room\Objects[4],True)
							ResetEntity e\room\NPC[0]\Collider
							Exit
						EndIf
					Next
					If e\room\NPC[0]=Null
						n.NPCs = CreateNPC(NPCtype049, EntityX(e\room\Objects[4],True), EntityY(e\room\Objects[4],True)+3, EntityZ(e\room\Objects[4],True))
						PointEntity n\Collider, e\room\obj
						n\State = 2
						n\Idle = 1
						e\room\NPC[0]=n
					EndIf
					QuickLoadPercent = 100
					e\EventState=1
				EndIf
			ElseIf e\EventState > 0
				
				temp = Not UpdateLever(e\room\Objects[7]) ;power feed
				x = UpdateLever(e\room\Objects[9]) ;generator
				
				e\room\RoomDoors[1]\locked = True
				e\room\RoomDoors[3]\locked = True
				
				If temp Or x Then 
					;049 appears when either of the levers is turned
					e\EventState = Max(e\EventState,70*180)
					
					If temp And x Then
						e\room\RoomDoors[1]\locked = False
						e\room\RoomDoors[3]\locked = False								
						e\EventState2 = UpdateElevators(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
						e\EventState3 = UpdateElevators(e\EventState3, e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
						
						If e\Sound2=0 Then LoadEventSound(e,"SFX\General\GeneratorOn.ogg",1)
						e\SoundCHN2=LoopSound2(e\Sound2, e\SoundCHN2, mainPlayer\cam, e\room\Objects[8], 6.0, e\EventState3)
						
						If e\room\NPC[0]\Idle > 0
							i = 0
							If EntityDistance(mainPlayer\collider,e\room\RoomDoors[1]\frameobj)<3.0
								i = 1
							ElseIf EntityDistance(mainPlayer\collider,e\room\RoomDoors[3]\frameobj)<3.0
								i = 3
							EndIf
							If i > 0
								;If EntityVisible(mainPlayer\collider,e\room\RoomDoors[i]\frameobj)
								PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\Objects[i],True),EntityY(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)
								ResetEntity e\room\NPC[0]\Collider
								PlaySound2(ElevatorBeepSFX, mainPlayer\cam, e\room\Objects[i], 4.0)
								UseDoor(e\room\RoomDoors[i],False)
								e\room\RoomDoors[i-1]\open = False
								e\room\RoomDoors[i]\open = True
								e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
								PlaySound2(LoadTempSound("SFX\SCP\049\Greeting"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\NPC[0]\Collider)
								e\room\NPC[0]\Idle = 0
								;EndIf
							EndIf
						EndIf
						If EntityVisible(mainPlayer\collider,e\room\NPC[0]\Collider)
							GiveAchievement(Achv049)
						EndIf
					EndIf
				EndIf
				
				If e\EventState < 70*190 Then 
					e\EventState = Min(e\EventState+FPSfactor,70*190)
					;049 spawns after 3 minutes
					If e\EventState > 70*180 Then
						
						;If e\room\NPC[0]=Null Then
						;	For n.NPCs = Each NPCs
						;		If n\NPCtype=NPCtype049 Then e\room\NPC[0]=n : Exit
						;	Next
						;EndIf
						;e\room\NPC[0]\State = 1
						
						e\room\RoomDoors[4]\open = True
						PlaySound_Strict TeslaPowerUpSFX
						PlaySound2(OpenDoorSFX(0,Rand(0,2)),mainPlayer\cam, e\room\RoomDoors[4]\obj, 6.0)
						
						e\room\RoomDoors[1]\open = False
						e\room\RoomDoors[3]\open = False
						e\room\RoomDoors[0]\open = True
						e\room\RoomDoors[2]\open = True
						
						e\EventState= 70*190
					EndIf
				ElseIf e\EventState < 70*240
					;GiveAchievement(Achv049)
					
					;If e\room\NPC[0]=Null Then
					;	For n.NPCs = Each NPCs
					;		If n\NPCtype=NPCtype049 Then e\room\NPC[0]=n : Exit
					;	Next
					;Else
						;If EntityDistance(e\room\NPC[0]\Collider,mainPlayer\collider)<4.0 Then
						;	e\EventState=e\EventState+FPSfactor
						;	If e\EventState > 70*195 And e\EventState-FPSfactor =< 70*195 Then
						;		For n.NPCs = Each NPCs ;awake the zombies
						;			If n\NPCtype = NPCtypeZombie And n\State = 0 Then
						;				n\State = 1
						;				SetNPCFrame(n, 155)
						;			EndIf
						;		Next
						;		;PlaySound2(LoadTempSound("SFX\SCP\049\Greeting"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\NPC[0]\Collider)
						;	ElseIf e\EventState > 70*214 And e\EventState-FPSfactor =< 70*214
						;		;PlaySound2(LoadTempSound("SFX\SCP\049\Spotted"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\NPC[0]\Collider)
						;	ElseIf e\EventState > 70*227 And e\EventState-FPSfactor =< 70*227
						;		;PlaySound2(LoadTempSound("SFX\SCP\049\Detected"+Rand(1,3)+".ogg"),mainPlayer\cam, e\room\NPC[0]\Collider)
						;		e\EventState=70*241
						;	EndIf
						;EndIf
					;EndIf
					
					For n.NPCs = Each NPCs ;awake the zombies
						If n\NPCtype = NPCtypeZombie And n\State = 0 Then
							n\State = 1
							SetNPCFrame(n, 155)
						EndIf
					Next
					e\EventState=70*241
				EndIf
			EndIf
		EndIf
	Else
		e\EventState2 = UpdateElevators(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
		e\EventState3 = UpdateElevators(e\EventState3, e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
	EndIf 
	
	If e\EventState < 0 Then
		If e\EventState > -70*4 Then 
			If mainPlayer\fallTimer => 0 Then 
				mainPlayer\fallTimer = Min(-1, mainPlayer\fallTimer)
				PositionEntity(mainPlayer\head, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), True)
				ResetEntity (mainPlayer\head)
				RotateEntity(mainPlayer\head, 0, EntityYaw(mainPlayer\cam) + Rand(-45, 45), 0)
			ElseIf mainPlayer\fallTimer < -230
				mainPlayer\fallTimer = -231
				mainPlayer\blinkTimer = 0
				e\EventState = e\EventState-FPSfactor
				
				If e\EventState =< -70*4 Then 
					UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
					ShowEntity mainPlayer\collider
					mainPlayer\dropSpeed = 0
					mainPlayer\blinkTimer = -10
					mainPlayer\fallTimer = 0
					PositionEntity mainPlayer\collider, EntityX(e\room\obj,True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\obj,True)
					ResetEntity mainPlayer\collider										
					
					PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True),True
					ResetEntity e\room\NPC[0]\Collider
					
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtypeZombie Then
							PositionEntity n\Collider, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True),True
							ResetEntity n\Collider
							n\State = 4
							DebugLog "moving zombie"
						EndIf
					Next
					
					n.NPCs = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State = 6
					n\Reload = 6*70
					PointEntity n\Collider,mainPlayer\collider
					e\room\NPC[1] = n
					
					n.NPCs = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State = 6
					n\Reload = (6*70)+Rnd(15,30)
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					MoveEntity n\Collider,0.5,0,0
					PointEntity n\Collider,mainPlayer\collider
					
					n.NPCs = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State = 6
					;n\Reload = 70*4.75
					n\Reload = 10000
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					n\State2 = EntityYaw(n\Collider)
					MoveEntity n\Collider,-0.65,0,0
					e\room\NPC[2] = n
					
					MoveEntity e\room\NPC[1]\Collider,0,0,0.1
					PointEntity mainPlayer\collider, e\room\NPC[1]\Collider
					
					PlaySound_Strict LoadTempSound("SFX\Character\MTF\049\Player0492_1.ogg")
					
					LoadEventSound(e,"SFX\SCP\049\0492Breath.ogg")
					
					IsZombie = True
				EndIf
			EndIf
		Else
			mainPlayer\blurTimer = 800
			mainPlayer\forceMove = 0.5
			mainPlayer\injuries = Max(2.0,mainPlayer\injuries)
			mainPlayer\bloodloss = 0
			
			;Msg = ""
			
			If e\room\NPC[2]\State = 7
				If e\room\NPC[2]\State3 < 70*1.75
					e\room\NPC[2]\State3 = e\room\NPC[2]\State3 + FPSfactor
				Else
					e\room\NPC[2]\State = 6
					e\room\NPC[2]\Reload = e\room\NPC[1]\Reload+Rnd(5,10)
				EndIf
			ElseIf e\room\NPC[2]\State = 6 And e\room\NPC[2]\Reload > 70*4
				If e\room\NPC[2]\State3 > -(70*4)
					e\room\NPC[2]\State3 = e\room\NPC[2]\State3 - FPSfactor
				Else
					e\room\NPC[2]\State3 = 0.0
					e\room\NPC[2]\Reload = 45
					e\room\NPC[2]\State = 7
				EndIf
			EndIf
			
			pvt% = CreatePivot()
			PositionEntity pvt%,EntityX(e\room\NPC[1]\Collider),EntityY(e\room\NPC[1]\Collider)+0.2,EntityZ(e\room\NPC[1]\Collider)
			
			PointEntity mainPlayer\collider, e\room\NPC[1]\Collider
			PointEntity mainPlayer\cam, pvt%
			
			FreeEntity pvt%
			
			If mainPlayer\dead = True Then
				If ChannelPlaying(e\room\NPC[1]\SoundChn) Then StopChannel(e\room\NPC[1]\SoundChn)
				PlaySound_Strict LoadTempSound("SFX\Character\MTF\049\Player0492_2.ogg")
				RemoveEvent(e)
			Else
				If e\SoundCHN = 0 Then
					e\SoundCHN = PlaySound_Strict (e\Sound)
				Else
					If (Not ChannelPlaying(e\SoundCHN)) Then e\SoundCHN = PlaySound_Strict(e\Sound)
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

Function FillRoom860(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump	

    ;the wooden door
    r\Objects[2] = LoadMesh_Strict("GFX\map\forest\door_frame.b3d")
    PositionEntity r\Objects[2],r\x + 184.0 * RoomScale,0,r\z,True
    ScaleEntity r\Objects[2],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
    EntityParent r\Objects[2],r\obj
    
    r\Objects[3] =  LoadMesh_Strict("GFX\map\forest\door.b3d")
    PositionEntity r\Objects[3],r\x + 112.0 * RoomScale,0,r\z+0.05,True
    EntityType r\Objects[3], HIT_MAP
    
    ScaleEntity r\Objects[3],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[3],r\obj
    
    r\Objects[4] = CopyEntity(r\Objects[3])
    PositionEntity r\Objects[4],r\x + 256.0 * RoomScale,0,r\z-0.05,True
    RotateEntity r\Objects[4], 0,180,0
    ScaleEntity r\Objects[4],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
    EntityParent r\Objects[4],r\obj
    
    ;doors to observation booth
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,False,"ABCD")
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z - 640.0 * RoomScale,0,r,True,False,False,"ABCD")
    d\AutoClose = False
    
    ;doors to the room itself
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z - 640.0 * RoomScale,0,r,False,False,1)
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,1)
    
    ;the forest
    Local fr.Forest = New Forest
    r\fr=fr
    GenForestGrid(fr)
    PlaceForest(fr,r\x,r\y+30.0,r\z,r)
    ;EntityParent fr\Forest_Pivot,r\obj
    
    it = CreateItem("Document SCP-860-1", "paper", r\x + 672.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 335.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle+10, 0
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Document SCP-860", "paper", r\x + 1152.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 384.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle+170, 0
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEventRoom860(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	Local fr.Forest=e\room\fr
	
	If mainPlayer\currRoom = e\room And fr<>Null Then 
		
		;Local dp.DrawPortal
		
		If e\EventState=1.0 Then ;the player is in the forest
			CurrStepSFX = 2
			
			Curr106\Idle = True
			
			;ShowEntity fr\DetailEntities[0]
			;ShowEntity fr\DetailEntities[1]
			
			UpdateForest(fr,mainPlayer\collider)
			
			If e\EventStr = ""
				QuickLoadPercent = 0
				e\EventStr = "load0"
			ElseIf e\EventStr = "load0"
				If Music(9) = 0 Then Music(9) = LoadSound_Strict("SFX\Music\8601.ogg")
				QuickLoadPercent = 15
				e\EventStr = "load1"
			ElseIf e\EventStr = "load1"
				QuickLoadPercent = 40
				If Music(12) = 0 Then Music(12) = LoadSound_Strict("SFX\Music\8601Cancer.ogg")
				e\EventStr = "load2"
			ElseIf e\EventStr = "load2"
				QuickLoadPercent = 100
				If e\room\NPC[0]=Null Then e\room\NPC[0]=CreateNPC(NPCtype860, 0,0,0)
				e\EventStr = "loaddone"
			EndIf
			
			If e\room\NPC[0]<>Null Then
				If e\room\NPC[0]\State2 = 1 And e\room\NPC[0]\State>1 Then ;the monster is chasing the player
					ShouldPlay = 12
				Else
					ShouldPlay = 9
				EndIf
			EndIf
			
			;the player fell	
			If EntityY(mainPlayer\collider)<=28.5 Then 
				Kill() 
				mainPlayer\blinkTimer=-2
			ElseIf EntityY(mainPlayer\collider)>EntityY(fr\Forest_Pivot,True)+0.5
				MoveEntity(mainPlayer\collider, 0, ((EntityY(fr\Forest_Pivot,True)+0.5) - EntityY(mainPlayer\collider))*FPSfactor, 0)
			EndIf
			
			If e\room\NPC[0]<>Null
				If e\room\NPC[0]\State = 0 Or EntityDistance(mainPlayer\collider, e\room\NPC[0]\Collider)>12.0 Then
					e\EventState3 = e\EventState3 + (1+mainPlayer\moveSpeed)* FPSfactor
					If (e\EventState3 Mod 500) < 10.0 And ((e\EventState3-FPSfactor) Mod 500) > 490.0 Then
						;If e\EventState3 > 3500 And Rnd(10000)<e\EventState3 Then
						If e\EventState3 > 3000-(500*SelectedDifficulty\aggressiveNPCs) And Rnd(10000+(500*SelectedDifficulty\aggressiveNPCs)) < e\EventState3
							e\room\NPC[0]\State=2
							PositionEntity e\room\NPC[0]\Collider, 0,-110,0
							;e\EventState3=e\EventState3-Rnd(2000,3000)
							e\EventState3=e\EventState3-Rnd(1000,2000-(500*SelectedDifficulty\aggressiveNPCs))
							DebugLog "attack"
						Else
							e\room\NPC[0]\State=1
							PositionEntity e\room\NPC[0]\Collider, 0,-110,0
							DebugLog "spawn"
						EndIf
					EndIf
				EndIf
			EndIf
			
			;If KeyHit(25) Then
			;	e\room\NPC[0]\State=2
			;	PositionEntity e\room\NPC[0]\Collider, 0,-110,0
			;	e\EventState3=e\EventState3-Rnd(2000,3000)
			;EndIf
			
			For i = 0 To 1
				If EntityDistance(fr\Door[i], mainPlayer\collider)<0.5 Then
					If EntityInView(fr\Door[i], mainPlayer\cam) Then
						DrawHandIcon = True
						If MouseHit1 Then
							If i=1 Then
								mainPlayer\blinkTimer = -10
								
								PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorOpen.ogg"))
								
								RotateEntity e\room\Objects[3], 0, 0, 0
								RotateEntity e\room\Objects[4], 0, 180, 0
								;SetAnimTime e\room\Objects[3], 0.0
								;SetAnimTime e\room\Objects[4], 0.0
								
								;dp.DrawPortal=e\room\dp;Object.DrawPortal(e\room\Objects[0])
								PositionEntity mainPlayer\collider, EntityX(e\room\Objects[2],True),0.5,EntityZ(e\room\Objects[2],True)
								
								RotateEntity mainPlayer\collider, 0, EntityYaw(e\room\obj,True)+e\EventState2*180, 0
								MoveEntity mainPlayer\collider, 0,0,1.5
								
								ResetEntity Collider
								
								UpdateDoorsTimer = 0
								UpdateDoors()
								
								e\EventState = 0.0
								
							Else
								PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorBudge.ogg"))
								Msg = "The door will not budge."
								MsgTimer = 70*5
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If e\room\NPC[0]<>Null
				x = Max(1.0-(e\room\NPC[0]\State3/300.0),0.1)
			Else
				x = 2.0
			EndIf
			
			CameraClsColor mainPlayer\cam,98*x,133*x,162*x
			CameraRange mainPlayer\cam,RoomScale,8.5
			CameraFogRange mainPlayer\cam,0.5,8.0
			CameraFogColor mainPlayer\cam,98*x,133*x,162*x
			
		Else
			
			If (Not Contained106) Then Curr106\Idle = False
			
			;dp.DrawPortal=e\room\dp;Object.DrawPortal(e\room\Objects[0])
			
			;HideEntity fr\DetailEntities[0]
			;HideEntity fr\DetailEntities[1]
			
			If EntityYaw(e\room\Objects[3])=0.0 Then
				HideEntity fr.Forest\Forest_Pivot
				If (Abs(Distance(EntityX(e\room\Objects[3],True),EntityZ(e\room\Objects[3],True),EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True)))<1.0) Then
					DrawHandIcon = True
					
					If mainPlayer\selectedItem = Null Then
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorBudge.ogg"))
							Msg = "The door will not budge."
							MsgTimer = 5*70
						EndIf
					ElseIf SelectedItem\itemtemplate\tempname="scp860" 
						If MouseHit1 Then
							PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorOpen.ogg"))
							ShowEntity fr.Forest\Forest_Pivot
							mainPlayer\selectedItem = Null
							
							mainPlayer\blinkTimer = -10
							
							e\EventState=1.0
							
							PositionEntity mainPlayer\collider,EntityX(fr\Door[0],True),EntityY(fr\Door[0],True)+EntityY(mainPlayer\collider,True)+0.5,EntityZ(fr\Door[0],True),True
							
							RotateEntity mainPlayer\collider, 0.0, EntityYaw(fr\Door[0],True)-180, 0.0, True
							MoveEntity mainPlayer\collider, -0.5,0.0,0.5
							
							
							
							pvt = CreatePivot()
							PositionEntity pvt, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam)
							PointEntity pvt, e\room\obj
							ang# = WrapAngle(EntityYaw(pvt)-EntityYaw(e\room\obj,True))
							If ang > 90 And ang < 270 Then
								;TurnEntity mainPlayer\collider,0,180+90,0,True
								e\EventState2 = 1
							Else
									TurnEntity mainPlayer\collider,0,90,0,True
							;RotateEntity mainPlayer\collider,0,EntityYaw(fr\Door[0],True)+EntityYaw(mainPlayer\collider)-EntityYaw(dp\portal,True),0,True
								e\EventState2 = 0
							EndIf
							FreeEntity pvt
							
							ResetEntity Collider
							
							
							
							;RotateEntity e\room\Objects[3], 0, 0.5, 0
							;RotateEntity e\room\Objects[3], 0, 359.5, 0
						EndIf
					EndIf
				EndIf
			EndIf
			
		EndIf
		
	Else
		If (fr=Null) Then
			RemoveEvent(e)
		Else
			If (fr\Forest_Pivot<>0) Then HideEntity fr\Forest_Pivot
		EndIf
	EndIf
	
	;[End Block]
	
End Function

Function FillRoom_exit_gateb_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x+4356.0*RoomScale, 9767.0*RoomScale, r\z+2588.0*RoomScale, True)
    
    r\RoomDoors[4] = CreateDoor(r\zone, r\x, 0, r\z - 320.0 * RoomScale, 0, r, False, True, 5)
    r\RoomDoors[4]\dir = 1 : r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False
    PositionEntity(r\RoomDoors[4]\buttons[1], r\x+352*RoomScale, 0.7, r\z-528*RoomScale, True)
    RotateEntity r\RoomDoors[4]\buttons[1],0,r\angle-90,0,True
    PositionEntity(r\RoomDoors[4]\buttons[0], r\x, 7.0, r\z, True)		
    
    ;k�yt�v�n takaosa
    r\Objects[3] = CreatePivot()
    PositionEntity(r\Objects[3], r\x-7680.0*RoomScale, 10992.0*RoomScale, r\z-27048.0*RoomScale, True)
    EntityParent r\Objects[3], r\obj
    
    ;oikean puolen watchpoint 1
    r\Objects[4] = CreatePivot()
    PositionEntity(r\Objects[4], r\x+5203.36*RoomScale, 12128.0*RoomScale, r\z-1739.19*RoomScale, True)
    EntityParent r\Objects[4], r\obj
    ;oikean puolen watchpoint 2
    r\Objects[5] = CreatePivot()
    PositionEntity(r\Objects[5], r\x+4363.02*RoomScale, 10536.0*RoomScale, r\z+2766.16*RoomScale, True)
    EntityParent r\Objects[5], r\obj	
    ;vasemman puolen watchpoint 1
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6], r\x+5192.0*RoomScale, 12192.0*RoomScale, r\z-1760.0*RoomScale, True)
    EntityParent r\Objects[6], r\obj
    ;vasemman puolen watchpoint 2
    r\Objects[7] = CreatePivot()
    PositionEntity(r\Objects[7], r\x+5192.0*RoomScale, 12192.0*RoomScale, r\z-4352.0*RoomScale, True)
    EntityParent r\Objects[7], r\obj
    
    ;alempi hissi
    r\RoomDoors[0] = CreateDoor(0, r\x+720.0*RoomScale, 0, r\z+1432.0*RoomScale, 0, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    MoveEntity r\RoomDoors[0]\buttons[0],0,0,22.0*RoomScale
    MoveEntity r\RoomDoors[0]\buttons[1],0,0,22.0*RoomScale	
    r\Objects[8] = CreatePivot()
    PositionEntity(r\Objects[8], r\x+720.0*RoomScale, 0, r\z+1744.0*RoomScale, True)
    EntityParent r\Objects[8], r\obj
    
    ;ylempi hissi
    r\RoomDoors[1] = CreateDoor(0, r\x-5424.0*RoomScale, 10784.0*RoomScale, r\z-1380.0*RoomScale, 0, r, False)
    r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
    MoveEntity r\RoomDoors[1]\buttons[0],0,0,22.0*RoomScale
    MoveEntity r\RoomDoors[1]\buttons[1],0,0,22.0*RoomScale			
    r\Objects[9] = CreatePivot()
    PositionEntity(r\Objects[9], r\x-5424.0*RoomScale, 10784.0*RoomScale, r\z-1068.0*RoomScale, True)
    EntityParent r\Objects[9], r\obj		
    
    r\RoomDoors[2] = CreateDoor(0, r\x+4352.0*RoomScale, 10784.0*RoomScale, r\z-492.0*RoomScale, 0, r, False)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False	
    
    r\RoomDoors[3] = CreateDoor(0, r\x+4352.0*RoomScale, 10784.0*RoomScale, r\z+500.0*RoomScale, 0, r, False)
    r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False	
    
    ;walkway
    r\Objects[10] = CreatePivot()
    PositionEntity(r\Objects[10], r\x+4352.0*RoomScale, 10778.0*RoomScale, r\z+1344.0*RoomScale, True)
    EntityParent r\Objects[10], r\obj	
    
    ;"682"
    r\Objects[11] = CreatePivot()
    PositionEntity(r\Objects[11], r\x+2816.0*RoomScale, 11024.0*RoomScale, r\z-2816.0*RoomScale, True)
    EntityParent r\Objects[11], r\obj
    
    ;r\Objects[12] = 682:n k�si
    
    ;"valvomon" takaovi
    r\RoomDoors[5] = CreateDoor(0, r\x+3248.0*RoomScale, 9856.0*RoomScale, r\z+6400.0*RoomScale, 0, r, False, False, 0, "ABCD")
    r\RoomDoors[5]\AutoClose = False : r\RoomDoors[5]\open = False		
    
    ;"valvomon" etuovi
    d.Doors = CreateDoor(0, r\x+3072.0*RoomScale, 9856.0*RoomScale, r\z+5800.0*RoomScale, 90, r, False, False, 3)
    d\AutoClose = False : d\open = False
    
    r\Objects[14] = CreatePivot()
    PositionEntity(r\Objects[14], r\x+3536.0*RoomScale, 10256.0*RoomScale, r\z+5512.0*RoomScale, True)
    EntityParent r\Objects[14], r\obj
    r\Objects[15] = CreatePivot()
    PositionEntity(r\Objects[15], r\x+3536.0*RoomScale, 10256.0*RoomScale, r\z+5824.0*RoomScale, True)
    EntityParent r\Objects[15], r\obj			
    r\Objects[16] = CreatePivot()
    PositionEntity(r\Objects[16], r\x+3856.0*RoomScale, 10256.0*RoomScale, r\z+5512.0*RoomScale, True)
    EntityParent r\Objects[16], r\obj
    r\Objects[17] = CreatePivot()
    PositionEntity(r\Objects[17], r\x+3856.0*RoomScale, 10256.0*RoomScale, r\z+5824.0*RoomScale, True)
    EntityParent r\Objects[17], r\obj
    
    ;MTF:n spawnpoint
    r\Objects[18] = CreatePivot()
    ;PositionEntity(r\Objects[18], r\x+3727.0*RoomScale, 10066.0*RoomScale, r\z+6623.0*RoomScale, True)
    PositionEntity(r\Objects[18], r\x+3250.0*RoomScale, 9896.0*RoomScale, r\z+6623.0*RoomScale, True)
    EntityParent r\Objects[18], r\obj
    
    ;piste johon helikopterit pakenee nukea
    r\Objects[19] = CreatePivot()
    PositionEntity(r\Objects[19], r\x+3808.0*RoomScale, 12320.0*RoomScale, r\z-13568.0*RoomScale, True)
    EntityParent r\Objects[19], r\obj
End Function

Function UpdateEventExit1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		
		If EntityY(mainPlayer\collider)>1040.0*RoomScale Then
			
			For r.Rooms = Each Rooms
				HideEntity r\obj
			Next					
			ShowEntity e\room\obj
			
			Curr106\state = 20000
			Curr106\Idle = True
			
			If e\EventState = 0 Then
				DrawLoading(0,True)
				
				For i = 0 To 19
					If e\room\LightSprites[i]<>0 Then 
						EntityFX e\room\LightSprites[i], 1+8
					EndIf
				Next

				CameraFogMode(mainPlayer\cam, 0)
				SecondaryLightOn = True
				
				;Music(5) = LoadSound("SFX\Music\GateB1.ogg") ;TODO: fix
				DrawLoading(60,True)
				;Music(6) = LoadSound("SFX\Music\GateB2.ogg") ;TODO: fix
				DrawLoading(90,True)
				
				e\room\NPC[0] = CreateNPC(NPCtypeApache, e\room\x, 100.0, e\room\z)
				e\room\NPC[0]\state = 1
				
				e\room\NPC[1] = CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True)+0.2,EntityZ(e\room\Objects[4],True))
				e\room\NPC[1]\state = 0
				e\room\NPC[1]\state2 = 10
				
				
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True)
				
				e\room\Objects[0] = LoadMesh("GFX\map\exit1terrain.b3d", e\room\obj)
				ScaleEntity e\room\Objects[0],RoomScale,RoomScale,RoomScale,True
				RotateEntity e\room\Objects[0],0,e\room\angle,0,True
				PositionEntity(e\room\Objects[0], EntityX(pvt), EntityY(pvt), EntityZ(pvt), True)
				
				FreeEntity pvt
				
				Sky = sky_CreateSky("GFX\map\sky\sky")
				RotateEntity Sky,0,e\room\angle-90,0
				
				e\EventState = 1.0
				
				For n.NPCs = Each NPCs
					If n\npcType = NPCtypeMTF
						RemoveNPC(n)
					EndIf
				Next
				
				DrawLoading(100,True)
			Else
				
				;em.Emitters = CreateEmitter(EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True), 1)
				;TurnEntity(em\Obj, -90, 0, 0)
				;e\room\Objects[4] = em\Obj
				;em\RandAngle = 26
				;em\SizeChange = 0.01
				;em\Achange = -0.015
				;em\Gravity = -0.12
				
				UpdateSky()
				
				If e\EventState < 2.0 Then 
					If e\room\NPC[0]\state = 2 Then
						ShouldPlay = 6
					Else
						e\EventState2=(e\EventState2+timing\tickDuration) Mod 3600
						PositionEntity(e\room\NPC[0]\collider, EntityX(e\room\obj,True)+Cos(e\EventState2/10)*6000.0*RoomScale,14000*RoomScale,EntityZ(e\room\obj,True)+Sin(e\EventState2/10)*6000.0*RoomScale)
						RotateEntity e\room\NPC[0]\collider,7.0,(e\EventState2/10),20.0											
						ShouldPlay = 5
					EndIf
					
					If EntityDistance(mainPlayer\collider, e\room\Objects[10])<320*RoomScale Then
						e\EventState = 2.0
						e\room\RoomDoors[2]\open = False
						e\room\RoomDoors[2]\locked = 6
						e\room\RoomDoors[3]\open = False
						e\room\RoomDoors[3]\locked = 6
						
						e\room\NPC[2] = CreateNPC(NPCtypeApache, EntityX(e\room\Objects[9],True),EntityY(e\room\Objects[9],True)+0.5,EntityZ(e\room\Objects[9],True))
						e\room\NPC[2]\state = 3
						
						e\room\NPC[3] = CreateNPC(NPCtypeApache, EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True)-2.0,EntityZ(e\room\Objects[7],True))
						e\room\NPC[3]\state = 3
						
						e\room\NPC[0]\state = 3
						
						e\soundChannels[0] = PlaySound(LoadTempSound("SFX\Ending\GateB\682Battle.ogg"))
					EndIf								
				Else
					ShouldPlay = 6
					e\EventState=e\EventState+timing\tickDuration
					
					If e\EventState < 40.0*70 Then 	
						e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[11],True)+Sin(TimeInPosMilliSecs()/25.0)*3
						e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[11],True)+Cos(MilliSecs()/85.0)+9.0
						e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[11],True)+Cos(MilliSecs()/25.0)*3
						
						e\room\NPC[2]\EnemyX = EntityX(e\room\Objects[11],True)+Sin(TimeInPosMilliSecs()/23.0)*3
						e\room\NPC[2]\EnemyY = EntityY(e\room\Objects[11],True)+Cos(MilliSecs()/83.0)+5.0
						e\room\NPC[2]\EnemyZ = EntityZ(e\room\Objects[11],True)+Cos(MilliSecs()/23.0)*3
						
						If e\room\NPC[3]\state = 3 Then 
							e\room\NPC[3]\EnemyX = EntityX(e\room\Objects[11],True)+Sin(TimeInPosMilliSecs()/20.0)*3
							e\room\NPC[3]\EnemyY = EntityY(e\room\Objects[11],True)+Cos(MilliSecs()/80.0)+3.5
							e\room\NPC[3]\EnemyZ = EntityZ(e\room\Objects[11],True)+Cos(MilliSecs()/20.0)*3
						EndIf
					EndIf
				EndIf
				
				
				If e\EventState > 0.6*70 And e\EventState < 42.2*70 Then 
					If e\EventState < 0.7*70 Then
						mainPlayer\camShake = 0.5
					ElseIf e\EventState > 3.2*70 And e\EventState < 3.3*70	
						mainPlayer\camShake = 0.5
					ElseIf e\EventState > 6.1*70 And e\EventState < 6.2*70	
						mainPlayer\camShake = 0.5
					ElseIf e\EventState < 10.8*70 And e\EventState < 10.9*70	
						mainPlayer\camShake = 0.5
					ElseIf e\EventState > 12.1*70 And e\EventState < 12.3*70
						mainPlayer\camShake = 1.0
					ElseIf e\EventState > 13.3*70 And e\EventState < 13.5*70
						mainPlayer\camShake = 1.5
					ElseIf e\EventState > 16.5*70 And e\EventState < 18.5*70
						mainPlayer\camShake = 3.0
					ElseIf e\EventState > 21.5*70 And e\EventState < 24.0*70	
						mainPlayer\camShake = 2.0
					ElseIf e\EventState > 25.5*70 And e\EventState < 27.0*70	
						mainPlayer\camShake = 2.0	
					ElseIf e\EventState > 31.0*70 And e\EventState < 31.5*70	
						mainPlayer\camShake = 0.5	
					ElseIf e\EventState > 35.0*70 And e\EventState < 36.5*70	
						mainPlayer\camShake = 1.5		
						If e\EventState-timing\tickDuration =< 35.0*70 Then
							e\soundChannels[1] = PlaySound(LoadTempSound("SFX\Ending\GateB\DetonatingAlphaWarheads.ogg"))
						EndIf									
					ElseIf e\EventState > 39.5*70 And e\EventState < 39.8*70		
						mainPlayer\camShake = 1.0
					ElseIf e\EventState > 42.0*70
						mainPlayer\camShake = 0.5
						
						;helicopters leave
						e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[19],True)+4.0
						e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[19],True)+4.0
						e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[19],True)+4.0
						
						e\room\NPC[2]\EnemyX = EntityX(e\room\Objects[19],True)
						e\room\NPC[2]\EnemyY = EntityY(e\room\Objects[19],True)
						e\room\NPC[2]\EnemyZ = EntityZ(e\room\Objects[19],True)
						
					EndIf
				EndIf
				
				If e\EventState => 45.0*70 Then
					If e\EventState < 75.0*70 Then
						If (Not IsChannelPlaying(e\soundChannels[0])) Then
							e\soundChannels[0] = PlaySound(LoadTempSound("SFX\Ending\GateB\Siren.ogg"))
						EndIf
					Else
						If SelectedEnding = "" Then
							If IsChannelPlaying(e\soundChannels[0])=False Then 
								temp = True
								For e2.Events = Each Events
									If e2\EventName = "room2nuke" Then
										temp = e2\EventState
										Exit
									EndIf
								Next
								
								If temp = 1 Then ;remote detonation on -> explode
									ExplosionTimer = Max(ExplosionTimer, 0.1)
									SelectedEnding = "B2"
								Else
									LoadEventSound(e,"SFX\Ending\GateB\AlphaWarheadsFail.ogg")
									e\soundChannels[0] = PlaySound(e\sounds[0])
									
									For i = 0 To 1
										n.NPCs = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[18],True)+(i*0.4),EntityY(e\room\Objects[18],True)+0.29,EntityZ(e\room\Objects[18],True)+(i*0.4))
									Next
									
									n.NPCs = CreateNPC(NPCtypeMTF, EntityX(e\room\RoomDoors[2]\obj,True),EntityY(e\room\RoomDoors[2]\obj,True)+0.29,(EntityZ(e\room\RoomDoors[2]\obj,True)+EntityZ(e\room\RoomDoors[3]\obj,True))/2)
									
									For n.NPCs = Each NPCs
										If n\npcType = NPCtypeMTF Then
											n\lastSeen = (70*Rnd(30,35))
											n\state = 3
											n\state2 = 10
											n\EnemyX = EntityX(mainPlayer\collider)
											n\EnemyY = EntityY(mainPlayer\collider)
											n\EnemyZ = EntityZ(mainPlayer\collider)
										EndIf
									Next
									
									DebugLog "MTF Units spawned!"
									
									e\EventState = 85.0*70
									
									SelectedEnding = "B3"
								EndIf
								
							EndIf										
						Else
							If SelectedEnding = "B3" Then
								e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[11],True)+Sin(TimeInPosMilliSecs()/25.0)*3
								e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[11],True)+Cos(MilliSecs()/85.0)+9.0
								e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[11],True)+Cos(MilliSecs()/25.0)*3
								
								e\room\NPC[2]\EnemyX = EntityX(e\room\Objects[11],True)+Sin(TimeInPosMilliSecs()/23.0)*3
								e\room\NPC[2]\EnemyY = EntityY(e\room\Objects[11],True)+Cos(MilliSecs()/83.0)+5.0
								e\room\NPC[2]\EnemyZ = EntityZ(e\room\Objects[11],True)+Cos(MilliSecs()/23.0)*3
								
								e\room\RoomDoors[5]\open = True
								
								;If e\EventState-timing\tickDuration < 80.0*70 And e\EventState => 80.0*70 Then
								;	
								;EndIf
								
								;Update the MTF Units everytime they cannot detect the player
								If e\EventState3 = 0.0
									For n.NPCs = Each NPCs
										If n\npcType = NPCtypeMTF
											If n\state = 5
												n\state = 3
												n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
												n\pathTimer = 70*Rand(15,20)
												n\lastSeen = 70*300
											EndIf
											If EntityDistance(n\collider,mainPlayer\collider)<3.0
												n\state = 5
												n\pathStatus = 0
												n\pathTimer = 0
												n\currSpeed = 0
											EndIf
										EndIf
									Next
								EndIf
								
								For n.NPCs = Each NPCs
									If n\npcType = NPCtypeMTF
										If n\state = 5 And EntityDistance(n\collider,mainPlayer\collider)<3.0
											If e\EventState3 = 0.0
												PlaySound2 LoadTempSound("SFX\Ending\GateB\PlayerDetect.ogg")
												e\EventState3 = e\EventState3 + timing\tickDuration
												For n2.NPCs = Each NPCs
													If n2\NPCtype = n\npcType
														n2\State = 5
														n2\PathStatus = 0
														n2\PathTimer = 0
														n2\CurrSpeed = 0
													EndIf
												Next
												Exit
											EndIf
										EndIf
									EndIf
								Next
								
								If e\EventState3 > 0.0 And e\EventState3 <= 500.0
									e\EventState3 = e\EventState3 + timing\tickDuration
									mainPlayer\disableControls = True
									For n.NPCs = Each NPCs
										If n\NPCtype = NPCtypeMTF
											n\EnemyX = EntityX(mainPlayer\collider)
											n\EnemyY = EntityY(mainPlayer\collider)
											n\EnemyZ = EntityZ(mainPlayer\collider)
											n\BoneToManipulate = "spine"
											n\ManipulateBone = True
											n\ManipulationType = 1
											n\Gravity = 0
											n\GravityMult = 0
										EndIf
									Next
								ElseIf e\EventState3 > 500.0
									ent% = LoadSprite("GFX\blooddrop1.png",1+2)
									EntityFX ent%,1+2+8
									ScaleSprite ent%,1.5,1.5
									ShouldPlay = 0
									mainPlayer\moveSpeed = 0
									PlaySound2 LoadTempSound("SFX\Ending\GateB\Gunshot.ogg")
									mainPlayer\godMode = 0
									mainPlayer\noclip = 0
									DeathMSG = ""
									Kill(mainPlayer)
									mainPlayer\blinkTimer = -10
									For n.NPCs = Each NPCs
										If n\NPCtype = NPCtypeMTF
											RemoveNPC(n)
										EndIf
									Next
									RemoveEvent(e)
									Return
								EndIf
								
							EndIf
						EndIf
						
						
					EndIf
				EndIf
				
				If e\EventState > 26.5*70 Then
					If e\room\Objects[12] = 0 Then
						e\room\Objects[12] = LoadMesh("GFX\NPCs\682arm.b3d")
						ScaleEntity e\room\Objects[12], 0.15,0.15,0.15
						temp = (Min(((EntityDistance(e\room\NPC[3]\Collider,mainPlayer\collider)/RoomScale)-3000.0)/4,1000)+12192.0)*RoomScale
						PositionEntity e\room\Objects[12],EntityX(e\room\NPC[3]\Collider),12192.0*RoomScale,EntityZ(e\room\NPC[3]\Collider)
						RotateEntity e\room\Objects[12],0,e\room\angle+Rnd(-10,10),0,True
						TurnEntity e\room\Objects[12], 0,0,180
					Else
						If WrapAngle(EntityRoll(e\room\Objects[12]))<340.0 Then 
							angle# = WrapAngle(EntityRoll(e\room\Objects[12]))
							TurnEntity e\room\Objects[12], 0,0,(5.0+Abs(Sin(angle))*2)*timing\tickDuration
							If angle < 270 And WrapAngle(EntityRoll(e\room\Objects[12]))=> 270 Then
								PlaySound2 LoadTempSound("SFX\Character\Apache\Crash1.ogg")
								e\room\NPC[3]\State = 4
								e\room\NPC[3]\State2 = 1.0
								e\room\NPC[3]\EnemyX = EntityX(e\room\Objects[7],True)
								e\room\NPC[3]\EnemyY = EntityY(e\room\Objects[7],True)-2.5
								e\room\NPC[3]\EnemyZ = EntityZ(e\room\Objects[7],True)
								
								em.Emitters = CreateEmitter(EntityX(e\room\NPC[3]\Collider), EntityY(e\room\NPC[3]\Collider), EntityZ(e\room\NPC[3]\Collider),0)
								em\Room = mainPlayer\currRoom
								em\RandAngle = 45
								em\Gravity = -0.18
								em\LifeTime = 400
								em\SizeChange = Rnd(0.005,0.007)
								em\Achange = -0.004
								TurnEntity(em\Obj, -80+20*i, 0, 0)
								EntityParent em\Obj, e\room\NPC[3]\Collider
								
								For i = 0 To 7
									p.Particles = CreateParticle(EntityX(e\room\NPC[3]\Collider),EntityY(e\room\NPC[3]\Collider),EntityZ(e\room\NPC[3]\Collider), 0, Rnd(0.5,1.0), -0.1, 200)
									p\speed = 0.01
									p\SizeChange = 0.01
									p\A = 1.0
									p\Achange = -0.005
									RotateEntity p\pvt, Rnd(360),Rnd(360),0
									MoveEntity p\pvt, 0,0,0.3
								Next
								
								For i = 0 To 12
									p.Particles = CreateParticle(EntityX(e\room\NPC[3]\Collider),EntityY(e\room\NPC[3]\Collider),EntityZ(e\room\NPC[3]\Collider), 0, 0.02, 0.003, 200)
									p\speed = 0.04
									p\A = 1.0
									p\Achange = -0.005
									RotateEntity p\pvt, Rnd(360),Rnd(360),0
								Next
							EndIf
						Else
							HideEntity e\room\Objects[12]
						EndIf
					EndIf
				EndIf
				
				
				;0.5
				;2.1
				;3.3
				;6.5 - 8.5
				;11.5-14
				;15.5-17
				;21
				;25-26.5
				;29.5
				;32
			EndIf
			
			;dist = Max(Min(EntityDistance(mainPlayer\collider, e\room\objects[3])/10000.0,1.0),0.0)
			;EntityAlpha Fog, 1.0-dist
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
			CameraFogRange mainPlayer\cam, 5,45
			
			angle = Max(Sin(EntityYaw(mainPlayer\collider)),0.0)
			;250,230,200
			CameraFogColor (mainPlayer\cam,200+(angle*50),200+(angle*30),200)
			CameraClsColor (mainPlayer\cam,200+(angle*50),200+(angle*30),200)					
			CameraRange(mainPlayer\cam, 0.05, 60)
			
			AmbientLight (140, 140, 140)
			
			If Rand(3) = 1 Then
				p.Particles = CreateParticle(EntityX(mainPlayer\cam)+Rnd(-2.0,2.0), EntityY(mainPlayer\cam)+Rnd(0.9,2.0), EntityZ(mainPlayer\cam)+Rnd(-2.0,2.0), 2, 0.006, 0, 300)
				p\speed = Rnd(0.002,0.003)
				RotateEntity(p\pvt, Rnd(-20, 20), e\room\angle-90+Rnd(-15,15),0, 0)
				
				p\SizeChange = -0.00001
			End If

			;Helicopter spots or player is within range. --> Start shooting.
			If ((EntityDistance(e\room\NPC[1]\Collider,mainPlayer\collider) < 15.0) Or EntityVisible(e\room\NPC[0]\Collider,mainPlayer\collider)) And e\room\NPC[1]\State <> 1
				e\room\NPC[1]\State = 1
				e\room\NPC[1]\State3 = 1
			EndIf
			
			;Below roof or inside catwalk. --> Stop shooting.
			If (EntityDistance(e\room\NPC[1]\Collider,mainPlayer\collider) < 8.9) Or (EntityDistance(e\room\Objects[5],mainPlayer\collider) < 16.9) Then
				e\room\NPC[1]\State3 = 0
			Else
				e\room\NPC[1]\State3 = 1
			EndIf
			
		Else
			
			If RemoteDoorOn=False Then
				e\room\RoomDoors[4]\locked=True
			ElseIf RemoteDoorOn And e\EventState3=0
				e\room\RoomDoors[4]\locked=False
				If e\room\RoomDoors[4]\open Then 
					If e\room\RoomDoors[4]\openstate > 50 Or EntityDistance(mainPlayer\collider, e\room\RoomDoors[4]\frameobj)<0.5 Then
						e\room\RoomDoors[4]\openstate = Min(e\room\RoomDoors[4]\openstate,50)
						e\room\RoomDoors[4]\open = False
						PlayRangedSound (LoadTempSound("SFX\Door\DoorError.ogg"), mainPlayer\cam, e\room\RoomDoors[4]\frameobj)
					EndIf							
				EndIf
			Else
				e\room\RoomDoors[4]\locked=False
				
				e\EventState2 = UpdateElevators(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1], e\room\Objects[8], e\room\Objects[9], e)
				
				EntityAlpha mainPlayer\overlays[OVERLAY_FOG], 1.0						
			EndIf
			
			
		EndIf
		
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
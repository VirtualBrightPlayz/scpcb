Function FillRoomIntro(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    r\Objects[0] = CreatePivot()
    PositionEntity (r\Objects[0], EntityX(r\obj) + 40.0 * RoomScale, 460.0 * RoomScale, EntityZ(r\obj) + 1072.0 * RoomScale)
    r\Objects[1] = CreatePivot()
    PositionEntity (r\Objects[1], EntityX(r\obj) - 80.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\Objects[2] = CreatePivot()
    PositionEntity (r\Objects[2], EntityX(r\obj) - 128.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)
    
    r\Objects[3] = CreatePivot()
    PositionEntity (r\Objects[3], EntityX(r\obj) + 660.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 526.0 * RoomScale)
    r\Objects[4] = CreatePivot()
    PositionEntity (r\Objects[4], EntityX(r\obj) + 700 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 320.0 * RoomScale)
    
    r\Objects[5] = CreatePivot()
    PositionEntity (r\Objects[5], EntityX(r\obj) + 1472.0 * RoomScale, 100.0 * RoomScale, EntityZ(r\obj) + 912.0 * RoomScale)
    
    For i = 0 To 5
        EntityParent(r\Objects[i], r\obj)
    Next
    
    r\RoomDoors[1] = CreateDoor(r\zone, EntityX(r\obj) + 288.0 * RoomScale, 0, EntityZ(r\obj) + 384.0 * RoomScale, 90, r, False, True)
    r\RoomDoors[1]\AutoClose = False ;: r\RoomDoors[1]\locked = True
    r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\open = False
    
    FreeEntity(r\RoomDoors[1]\buttons[0]) : r\RoomDoors[1]\buttons[0] = 0
    FreeEntity(r\RoomDoors[1]\buttons[1]) : r\RoomDoors[1]\buttons[1] = 0
    
    de.Decals = CreateDecal(Rand(4, 5), EntityX(r\Objects[5], True), 0.002, EntityZ(r\Objects[5], True), 90, Rnd(360), 0)
    de\Size = 1.2
    ScaleSprite(de\obj, de\Size, de\Size)
    
    For xtemp% = 0 To 1
        For ztemp% = 0 To 1
            de.Decals = CreateDecal(Rand(4, 6), r\x + 700.0 * RoomScale + xtemp * 700.0 * RoomScale + Rnd(-0.5, 0.5), Rnd(0.001, 0.0018), r\z + 600 * ztemp * RoomScale + Rnd(-0.5, 0.5), 90, Rnd(360), 0)
            de\Size = Rnd(0.5, 0.8)
            de\Alpha = Rnd(0.8, 1.0)
            ScaleSprite(de\obj, de\Size, de\Size)
        Next
    Next
    
    ;AddLight(r, r\x-224.0*RoomScale, r\y+640.0*RoomScale, r\z+128.0*RoomScale,2,2,200,200,200)
    ;AddLight(r, r\x-1056.0*RoomScale, r\y+608.0*RoomScale, r\z+416.0*RoomScale,2,2,200,200,200)
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 1008.0 * RoomScale, 0, r\z - 688.0 * RoomScale, 90, r, True)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False : r\RoomDoors[2]\locked = True
    FreeEntity(r\RoomDoors[2]\buttons[0]) : r\RoomDoors[2]\buttons[0] = 0
    FreeEntity(r\RoomDoors[2]\buttons[1]) : r\RoomDoors[2]\buttons[1] = 0
    
    r\RoomDoors[3] = CreateDoor(r\zone, r\x - 2320.0 * RoomScale, 0, r\z - 1248.0 * RoomScale, 90, r, True)
    r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = True : r\RoomDoors[3]\locked = True
    
    r\RoomDoors[4] = CreateDoor(r\zone, r\x - 4352.0 * RoomScale, 0, r\z - 1248.0 * RoomScale, 90, r, True)
    r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = True : r\RoomDoors[4]\locked = True	
    
    ;the door in the office below the walkway
    r\RoomDoors[7] = CreateDoor(r\zone, r\x - 3712.0 * RoomScale, -385*RoomScale, r\z - 128.0 * RoomScale, 0, r, True)
    r\RoomDoors[7]\AutoClose = False : r\RoomDoors[7]\open = True
    
    d.Doors = CreateDoor(r\zone, r\x - 3712 * RoomScale, -385*RoomScale, r\z - 2336 * RoomScale, 0, r, False)
    d\locked = True : d\DisableWaypoint = True
    
    ;the door from the concrete tunnel to the large hall
    d.Doors = CreateDoor(r\zone, r\x - 6864 * RoomScale, 0, r\z - 1248 * RoomScale, 90, r, True)
    d\AutoClose = False
    d\locked = True
    
    ;the locked door to the lower level of the hall
    d.Doors = CreateDoor(r\zone, r\x - 5856 * RoomScale, 0, r\z - 1504 * RoomScale, 0, r, False)
    d\locked = True : d\DisableWaypoint = True
    
    ;the door to the staircase in the office room
    d.Doors = CreateDoor(r\zone, r\x - 2432 * RoomScale, 0, r\z - 1000 * RoomScale, 0, r, False)
    PositionEntity(d\buttons[0], r\x - 2592 * RoomScale, EntityY(d\buttons[0],True), r\z - 1024 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x - 2592 * RoomScale, EntityY(d\buttons[0],True), r\z - 992 * RoomScale, True)
    d\locked = True : d\DisableWaypoint = True
    
    tex = LoadTexture_Strict("GFX\map\Door02.jpg")
    For ztemp = 0 To 1
        d.Doors = CreateDoor(r\zone, r\x - 5760 * RoomScale, 0, r\z + (320+896*ztemp) * RoomScale, 0, r, False)
        d\locked = True
        d\DisableWaypoint = True
        
        d.Doors = CreateDoor(r\zone, r\x - 8288 * RoomScale, 0, r\z + (320+896*ztemp) * RoomScale, 0, r, False)
        d\locked = True
        If ztemp = 0 Then d\open = True Else d\DisableWaypoint = True
        
        For xtemp = 0 To 2
            d.Doors = CreateDoor(r\zone, r\x - (7424.0-512.0*xtemp) * RoomScale, 0, r\z + (1008.0-480.0*ztemp) * RoomScale, 180*(Not ztemp), r, False)
            EntityTexture d\obj, tex
            d\locked = True
            FreeEntity d\obj2 : d\obj2=0
            FreeEntity d\buttons[0] : d\buttons[0]=0
            FreeEntity d\buttons[1] : d\buttons[1]=0
            d\DisableWaypoint = True
        Next					
        For xtemp = 0 To 4
            d.Doors = CreateDoor(r\zone, r\x - (5120.0-512.0*xtemp) * RoomScale, 0, r\z + (1008.0-480.0*ztemp) * RoomScale, 180*(Not ztemp), r, False)
            EntityTexture d\obj, tex
            d\locked = True
            FreeEntity d\obj2 : d\obj2=0
            FreeEntity d\buttons[0] : d\buttons[0]=0
            FreeEntity d\buttons[1] : d\buttons[1]=0	
            d\DisableWaypoint = True
            
            If xtemp = 2 And ztemp = 1 Then r\RoomDoors[6] = d
        Next	
    Next
    
    CreateItem("Class D Orientation Leaflet", "paper", r\x-(2914+1024)*RoomScale, 170.0*RoomScale, r\z+40*RoomScale)
    
    sc.SecurityCams = CreateSecurityCam(r\x - 4048.0 * RoomScale, r\y - 32.0 * RoomScale, r\z - 1232.0 * RoomScale, r, True)
    sc\angle = 270
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\CameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    PositionEntity(sc\ScrObj, r\x - 2256 * RoomScale, 224.0 * RoomScale, r\z - 928.0 * RoomScale)
    TurnEntity(sc\ScrObj, 0, 90, 0)
    EntityParent(sc\ScrObj, r\obj)
    
    r\Objects[9] = LoadMesh_Strict("GFX\map\173_2.b3d",r\obj)
    EntityType r\Objects[9],HIT_MAP
    EntityPickMode r\Objects[9],2
End Function

Function UpdateEvent173(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	If KillTimer >= 0 And e\EventState2 = 0 Then
		
		PlayerZone = 0
		
		If e\EventState3>0 Then
			
			If Music(13)=0 Then Music(13) = LoadSound_Strict("SFX\Music\Intro.ogg")
			ShouldPlay = 13
			
			;slow the player down to match his speed to the guards
			mainPlayer\moveSpeed = Min(mainPlayer\moveSpeed - (mainPlayer\moveSpeed * (0.008/EntityDistance(e\room\NPC[3]\Collider, mainPlayer\collider)) * FPSfactor), mainPlayer\moveSpeed)
			
			If e\EventState3 < 170 Then 
				If e\EventState3 = 1.0 Then
					PositionEntity mainPlayer\cam, x, y, z
					HideEntity mainPlayer\collider
					PositionEntity mainPlayer\collider, x, 0.302, z	
					RotateEntity mainPlayer\cam, -70, 0, 0
					
					StopChannel MusicCHN
					CurrMusicVolume = 1.0
					MusicCHN = PlaySound_Strict(Music(13))
					NowPlaying = ShouldPlay
					
					PlaySound_Strict(IntroSFX(11))
					mainPlayer\blurTimer = 500
					ShowEntity Light
					EntityAlpha(Light, 0.5)
				EndIf
				
				If e\EventState3 < 3 Then
					e\EventState3 = e\EventState3+FPSfactor/100.0
				Else
					e\EventState3 = e\EventState3+FPSfactor/30.0
				EndIf
				
				If e\EventState3 < 15 Then
					
					x = EntityX(e\room\obj)-(3224.0+1024.0)*RoomScale
					y = 136.0*RoomScale
					z = EntityZ(e\room\obj)+8.0*RoomScale	
					
					If e\EventState3 < 14 Then
						mouse_x_speed_1#=0
						mouse_y_speed_1#=0
						
						If e\EventState3-FPSfactor/30.0 < 12 And e\EventState3 > 12 Then PlaySound2(StepSFX(0,0,0), mainPlayer\cam, mainPlayer\collider, 8, 0.3)
						
						ShowEntity Light
						EntityAlpha(Light, 0.9-(e\EventState3/2.0))
						
						x = x + (EntityX(e\room\obj)-(3048.0+1024.0)*RoomScale - x) * Max((e\EventState3-10.0)/4.0,0.0) 
						
						If e\EventState3 < 10 Then
							y = y + (0.2) * Min(Max((e\EventState3-3.0)/5.0, 0.0), 1.0)
						Else
							y = (y+0.2) + (0.302+0.6 - (y+0.2)) * Max((e\EventState3-10)/4.0,0.0) 
						EndIf
						
						z = z + (EntityZ(e\room\obj)+104.0*RoomScale - z) * Min(Max((e\EventState3-3)/5.0, 0), 1.0)
						
						;I'm sorry you have to see this
						RotateEntity mainPlayer\cam, -70.0 + 70.0*Min(Max((e\EventState3-3.0)/5.0,0.0),1.0)+Sin(e\EventState3*12.857)*5.0, -60.0*Max((e\EventState3-10.0)/4.0,0.0), Sin(e\EventState3*25.7)*8.0
						
						PositionEntity mainPlayer\cam, x, y, z
						HideEntity mainPlayer\collider
						PositionEntity mainPlayer\collider, x, 0.302, z	
						mainPlayer\dropSpeed = 0
					Else
						HideEntity Light
						
						PositionEntity mainPlayer\collider, EntityX(mainPlayer\collider), 0.302, EntityZ(mainPlayer\collider)
						ResetEntity mainPlayer\collider
						ShowEntity mainPlayer\collider
						mainPlayer\dropSpeed = 0
						e\EventState3 = 15
					EndIf
					
					mainPlayer\headPitch = 0
					RotateEntity mainPlayer\collider, 0, EntityYaw(mainPlayer\cam), 0
					
				ElseIf e\EventState3 < 30
					If e\EventState3 > 25 Then 
						e\EventState3 = e\EventState3+FPSfactor/3.0
						Msg = "Pick up the paper on the desk."
						MsgTimer=70*7
						e\EventState3=30
					EndIf
				ElseIf e\EventState3 < 35
					If mainPlayer\inventory\items[0]<>Null Then
						Msg = "Press "+KeyName(keyBinds\inv)+" to open the inventory."
						MsgTimer=70*4
						e\EventState3 = 40
						Exit
					EndIf
				ElseIf e\EventState > 45 And e\EventState3 < 45
					If InvOpen Then
						Msg = "Double click on the document to view it."
						MsgTimer=70*4
						e\EventState3 = 45
					EndIf
				EndIf
				
				If mainPlayer\selectedItem <> Null Then
					e\EventState3 = e\EventState3+FPSfactor/5.0
				EndIf							
				
			ElseIf e\EventState3 => 150.0 And e\EventState3 < 700
				If e\room\NPC[3]\State = 7 Then
					;mainPlayer\blinkTimer = -10
					
					e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\ExitCell.ogg")
					e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
					
					;e\room\NPC[3]\State = 7
					;e\room\NPC[4]\State = 7
					;e\room\NPC[5]\State = 7
					e\room\NPC[3]\State = 9
					e\room\NPC[4]\State = 9
					e\room\NPC[5]\State = 9
					
					e\room\RoomDoors[6]\locked = False		
					UseDoor(e\room\RoomDoors[6], False)
					e\room\RoomDoors[6]\locked = True									
				EndIf
				
				;PointEntity e\room\NPC[3]\obj, Collider
				;RotateEntity e\room\NPC[3]\Collider, 0, EntityYaw(e\room\NPC[3]\obj), 0
				
				;PointEntity e\room\NPC[4]\obj, Collider
				;RotateEntity e\room\NPC[4]\Collider, 0, EntityYaw(e\room\NPC[4]\obj), 0
				
				e\EventState3 = Min(e\EventState3+FPSfactor/4,699)
				
				;outside the cell
				If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),mainPlayer\currRoom\x-(3072+1024)*RoomScale, mainPlayer\currRoom\z+192.0*RoomScale)>1.5 Then
					;e\room\NPC[3]\State = 5
					;e\room\NPC[3]\EnemyX = EntityX(mainPlayer\collider)
					;e\room\NPC[3]\EnemyY = EntityY(mainPlayer\collider)
					;e\room\NPC[3]\EnemyZ = EntityZ(mainPlayer\collider)
					
					If e\EventState3 > 250 Then
						If e\room\NPC[3]\SoundChn<>0 Then
							If ChannelPlaying(e\room\NPC[3]\SoundChn) Then StopChannel e\room\NPC[3]\SoundChn
						EndIf
						FreeSound_Strict e\room\NPC[3]\Sound
						e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\Escort"+Rand(1,2)+".ogg")
						e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
						
						;e\room\NPC[3]\PathStatus = FindPath(e\room\NPC[3],mainPlayer\currRoom\x-1584*RoomScale, 0.3, mainPlayer\currRoom\z-1040*RoomScale)
						;e\room\NPC[4]\PathStatus = FindPath(e\room\NPC[4],mainPlayer\currRoom\x-1584*RoomScale, 0.3, mainPlayer\currRoom\z-1040*RoomScale)
						e\room\NPC[3]\PathStatus = FindPath(e\room\NPC[3],mainPlayer\currRoom\x-320*RoomScale, 0.3, mainPlayer\currRoom\z-704*RoomScale)
						e\room\NPC[4]\PathStatus = FindPath(e\room\NPC[4],mainPlayer\currRoom\x-320*RoomScale, 0.3, mainPlayer\currRoom\z-704*RoomScale)
						
						e\EventState3 = 710
					EndIf
				Else ;inside the cell
					e\room\NPC[3]\State = 9
					;PointEntity e\room\NPC[3]\Collider, mainPlayer\collider		
					;RotateEntity e\room\NPC[3]\Collider, 0, EntityYaw(e\room\NPC[3]\Collider), 0
					
					If e\EventState3-(FPSfactor/4) < 350 And e\EventState3=>350 Then
						FreeSound_Strict e\room\NPC[3]\Sound
						e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\ExitCellRefuse"+Rand(1,2)+".ogg")
						e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
					ElseIf e\EventState3-(FPSfactor/4) < 550 And e\EventState3=>550 
						FreeSound_Strict e\room\NPC[3]\Sound
						e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\CellGas"+Rand(1,2)+".ogg")
						e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
					ElseIf e\EventState3>630
						PositionEntity mainPlayer\collider, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), Min(EntityZ(mainPlayer\collider), EntityZ(e\room\obj,True)+490*RoomScale)
						If e\room\RoomDoors[6]\open = True Then 
							e\room\RoomDoors[6]\locked = False		
							UseDoor(e\room\RoomDoors[6],False)
							e\room\RoomDoors[6]\locked = True
							
							em.Emitters = CreateEmitter(mainPlayer\currRoom\x - (2976.0+1024) * RoomScale, 373.0 * RoomScale, mainPlayer\currRoom\z + 204.0 * RoomScale, 0)
							TurnEntity(em\Obj, 90, 0, 0, True)
							em\RandAngle = 7
							em\Speed = 0.03
							em\SizeChange = 0.003
							em\Room = mainPlayer\currRoom
							
							em.Emitters = CreateEmitter(mainPlayer\currRoom\x - (3168.0+1024) * RoomScale, 373.0 * RoomScale, mainPlayer\currRoom\z + 204.0 * RoomScale, 0)
							TurnEntity(em\Obj, 90, 0, 0, True)
							em\RandAngle = 7
							em\Speed = 0.03
							em\SizeChange = 0.003
							em\Room = mainPlayer\currRoom
						EndIf
						
						EyeIrritation=Max(EyeIrritation+FPSfactor * 4, 1.0)
					EndIf
					
				EndIf
			ElseIf e\EventState3 < 800
				e\EventState3 = e\EventState3+FPSfactor/4.0
				
				;e\room\NPC[3]\State = 5
				;e\room\NPC[3]\EnemyX = EntityX(mainPlayer\collider)
				;e\room\NPC[3]\EnemyY = EntityY(mainPlayer\collider)
				;e\room\NPC[3]\EnemyZ = EntityZ(mainPlayer\collider)
			ElseIf e\EventState3 < 900
				e\room\NPC[4]\Angle = 0
				
				If EntityX(mainPlayer\collider)<EntityX(e\room\obj,True)-5376*RoomScale And e\EventStr = "" Then
					If Rand(3)=1 Then
						;e\EventStr = "scripted\scripted"+Rand(1,6)+".ogg|off.ogg|"
						e\EventStr = "scripted\scripted"+Rand(1,5)+".ogg|off.ogg|"
					Else
						;generate the PA message
						e\EventStr = "1\attention"+Rand(1,2)+".ogg"
						Select Rand(3)
							Case 1
								strtemp = "crew"
								e\EventStr = e\EventStr + "|2\crew"+Rand(0,5)+".ogg"
							Case 2
								strtemp = "scientist"
								e\EventStr = e\EventStr + "|2\scientist"+Rand(0,19)+".ogg"
							Case 3
								strtemp = "security"	
								e\EventStr = e\EventStr + "|2\security"+Rand(0,5)+".ogg"
						End Select
						If Rand(2)=1 And strtemp = "scientist" Then ;call on line...
							e\EventStr = e\EventStr + "|3\callonline.ogg"
							
							e\EventStr = e\EventStr + "|numbers\"+Rand(1,9)+".ogg"
							If Rand(2)=1 Then e\EventStr = e\EventStr + "|numbers\"+Rand(1,9)+".ogg"
						Else
							e\EventStr = e\EventStr + "|3\report"+Rand(0,1)+".ogg"
							
							Select strtemp
								Case "crew"
									e\EventStr = e\EventStr + "|4\crew"+Rand(0,6)+".ogg"
									If Rand(2)=1 Then e\EventStr = e\EventStr + "|5\crew"+Rand(0,6)+".ogg"
								Case "scientist"
									e\EventStr = e\EventStr + "|4\scientist"+Rand(0,7)+".ogg"
									If Rand(2)=1 Then e\EventStr = e\EventStr + "|5\scientist0.ogg"
								Case "security"
									e\EventStr = e\EventStr + "|4\security"+Rand(0,5)+".ogg"
									If Rand(2)=1 Then e\EventStr = e\EventStr + "|5\security"+Rand(1,2)+".ogg"
							End Select
						EndIf
						e\EventStr = e\EventStr + "|off.ogg|"
					EndIf
				EndIf
				
				If e\room\NPC[6]<>Null Then ;the scientist
					If e\room\NPC[6]\State = 0 Then 
						If e\room\RoomDoors[7]\open Then 
							If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\obj,True)-3328*RoomScale, EntityZ(e\room\obj,True)-1232*RoomScale)<5.0 Then
								e\room\NPC[6]\State = 1
								If e\EventStr = "done" Then 
									PlaySound_Strict LoadTempSound("SFX\Room\Intro\PA\scripted\announcement"+Rand(1,7)+".ogg")
								EndIf
							EndIf
						EndIf
					Else
						If EntityZ(e\room\NPC[6]\Collider)>EntityZ(e\room\obj,True)-64.0*RoomScale Then
							RotateEntity e\room\NPC[6]\Collider, 0, CurveAngle(90,EntityYaw(e\room\NPC[6]\Collider),15.0),0
							If e\room\RoomDoors[7]\open Then UseDoor(e\room\RoomDoors[7],False)
							If e\room\RoomDoors[7]\openstate < 1.0 Then e\room\NPC[6]\State = 0
						EndIf
					EndIf
				EndIf
				
				If e\room\NPC[8]<>Null Then ;the 2 guards and ClassD
					If e\room\NPC[8]\State = 7 Then
						;If e\room\RoomDoors[7]\open Then 
						If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\obj,True)-6688*RoomScale, EntityZ(e\room\obj,True)-1252*RoomScale)<2.5 Then
							e\room\NPC[8]\State = 10
							e\room\NPC[9]\State = 1
							e\room\NPC[10]\State = 10
						EndIf
					Else
						If EntityX(e\room\NPC[8]\Collider)<EntityX(e\room\obj,True)-7100.0*RoomScale Then
							For i = 8 To 10
								e\room\NPC[i]\State = 0
							Next
						EndIf
					EndIf
				EndIf
				
				e\room\NPC[5]\SoundChn = LoopSound2(e\room\NPC[5]\Sound,e\room\NPC[5]\SoundChn,mainPlayer\cam,e\room\NPC[5]\obj,2,0.5)
				
				If e\EventStr <> "" And e\EventStr <> "done" Then
					If e\SoundCHN = 0 Then 
						e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Room\Intro\PA\on.ogg"))
					EndIf
					If ChannelPlaying(e\SoundCHN)=False Then
						strtemp = Left(e\EventStr, Instr(e\EventStr, "|", 1)-1)
						e\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\Room\Intro\PA\"+strtemp))
						e\EventStr = Right(e\EventStr, Len(e\EventStr)-Len(strtemp)-1)
						If e\EventStr = "" Then 
							FreeSound_Strict e\room\NPC[3]\Sound
							temp = Rand(1,5)
							e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Conversation"+temp+"a.ogg")
							e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
							e\room\NPC[4]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Conversation"+temp+"b.ogg")
							e\room\NPC[4]\SoundChn = PlaySound2(e\room\NPC[4]\Sound, mainPlayer\cam, e\room\NPC[4]\Collider)
							e\EventStr = "done"
						EndIf
					EndIf
				EndIf
				
				dist = Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\NPC[3]\Collider), EntityZ(e\room\NPC[3]\Collider))
				
				If dist < 3.0 Then
					e\room\NPC[3]\State3 = Min(Max(e\room\NPC[3]\State3-FPSfactor,0),50)
				Else
					e\room\NPC[3]\State3 = Max(e\room\NPC[3]\State3+FPSfactor,50)
					If e\room\NPC[3]\State3 => 70*8 And e\room\NPC[3]\State3-FPSfactor < 70*8 And e\room\NPC[3]\State=7 Then
						If e\room\NPC[4]\SoundChn <> 0 Then
							If ChannelPlaying(e\room\NPC[4]\SoundChn) Then StopChannel(e\room\NPC[4]\SoundChn)
						EndIf

						If e\room\NPC[3]\State2 < 2 Then
							FreeSound_Strict e\room\NPC[3]\Sound
							e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\EscortRefuse"+Rand(1,2)+".ogg")
							e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
							e\room\NPC[3]\State3=50
							e\room\NPC[3]\State2=3
						ElseIf e\room\NPC[3]\State2=3
							FreeSound_Strict e\room\NPC[3]\Sound
							e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\EscortPissedOff"+Rand(1,2)+".ogg")
							e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
							e\room\NPC[3]\State3=50
							e\room\NPC[3]\State2=4
						ElseIf e\room\NPC[3]\State2=4
							FreeSound_Strict e\room\NPC[3]\Sound
							e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\EscortKill"+Rand(1,2)+".ogg")
							e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
							e\room\NPC[3]\State3 = 50+70*2.5
							e\room\NPC[3]\State2=5
						ElseIf e\room\NPC[3]\State2=5
							e\room\NPC[3]\State = 11
							e\room\NPC[4]\State = 11
							e\room\NPC[5]\State = 11
							e\room\NPC[3]\State3 = 1
							e\room\NPC[4]\State3 = 1
							e\room\NPC[5]\State3 = 1
						EndIf
					EndIf
					If e\room\NPC[5]\State <> 11
						If EntityDistance(e\room\NPC[3]\Collider,e\room\NPC[5]\Collider)>5.0
							If EntityDistance(e\room\NPC[5]\Collider,mainPlayer\collider)<3.5
								e\room\NPC[5]\State = 11
								e\room\NPC[5]\State3 = 1
								e\room\NPC[5]\SoundChn2 = PlaySound2(e\room\NPC[5]\Sound2,mainPlayer\cam,e\room\NPC[5]\Collider)
								e\room\NPC[5]\Reload = 70*3
							EndIf
						EndIf
					EndIf
				EndIf
				
				If e\room\NPC[5]\State = 11
					UpdateSoundOrigin(e\room\NPC[5]\SoundChn2,mainPlayer\cam,e\room\NPC[5]\Collider)
				EndIf
				
				If e\room\NPC[3]\State <> 11 Then
					If dist < Min(Max(4.0-e\room\NPC[3]\State3*0.05, 1.5),4.0) Then
						If e\room\NPC[3]\PathStatus <> 1 Then
							e\room\NPC[3]\State = 7
							PointEntity e\room\NPC[3]\obj, Collider
							RotateEntity e\room\NPC[3]\Collider,0,CurveValue(EntityYaw(e\room\NPC[3]\obj),EntityYaw(e\room\NPC[3]\Collider),20.0),0,True
							
							If e\room\NPC[3]\PathStatus = 2 Then
								;e\room\NPC[3]\PathStatus = FindPath(e\room\NPC[3],EntityX(e\room\obj,True)-1584*RoomScale, 0.3, EntityZ(e\room\obj,True)-1040*RoomScale)
								e\room\NPC[3]\PathStatus = FindPath(e\room\NPC[3],mainPlayer\currRoom\x-320*RoomScale, 0.3, mainPlayer\currRoom\z-704*RoomScale)
								e\room\NPC[4]\PathStatus = FindPath(e\room\NPC[4],mainPlayer\currRoom\x-320*RoomScale, 0.3, mainPlayer\currRoom\z-704*RoomScale)
								e\room\NPC[3]\State = 3
							EndIf
						Else
							e\room\NPC[3]\State = 3
						EndIf
					Else
						e\room\NPC[3]\State = 7
						PointEntity e\room\NPC[3]\obj, Collider
						RotateEntity e\room\NPC[3]\Collider,0,CurveValue(EntityYaw(e\room\NPC[3]\obj),EntityYaw(e\room\NPC[3]\Collider),20.0),0,True		
						
						If dist > 5.5 Then
							e\room\NPC[3]\PathStatus = 2
							If e\room\NPC[3]\State2=0 Then
								FreeSound_Strict e\room\NPC[3]\Sound
								e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\EscortRun.ogg")
								e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
								PlaySound2(e\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
								e\room\NPC[3]\State2=1
							EndIf
							
							e\room\NPC[3]\State = 5
							e\room\NPC[3]\EnemyX = EntityX(mainPlayer\collider)
							e\room\NPC[3]\EnemyY = EntityY(mainPlayer\collider)
							e\room\NPC[3]\EnemyZ = EntityZ(mainPlayer\collider)
							;e\room\NPC[3]\PathStatus = FindPath(e\room\NPC[3],EntityY(mainPlayer\collider), 0.3, EntityZ(mainPlayer\collider))
						EndIf
					EndIf	
					
					;PointEntity e\room\NPC[5]\obj, Collider
					;RotateEntity e\room\NPC[5]\Collider, 0, CurveAngle(EntityYaw(e\room\NPC[5]\obj),EntityYaw(e\room\NPC[5]\Collider),30), 0
					
					dist = EntityDistance(mainPlayer\collider, e\room\NPC[4]\Collider)
					If dist > 1.5 And EntityDistance(e\room\NPC[3]\Collider, mainPlayer\collider)<EntityDistance(e\room\NPC[3]\Collider,e\room\NPC[4]\Collider) Then
						e\room\NPC[4]\State = 3	
						
					Else
						e\room\NPC[4]\State = 5
						e\room\NPC[4]\EnemyX = EntityX(mainPlayer\collider)
						e\room\NPC[4]\EnemyY = EntityY(mainPlayer\collider)
						e\room\NPC[4]\EnemyZ = EntityZ(mainPlayer\collider)
					EndIf
					
				EndIf
				
				dist = Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\RoomDoors[2]\frameobj,True), EntityZ(e\room\RoomDoors[2]\frameobj,True))
				
				If Distance(EntityX(e\room\NPC[3]\Collider), EntityZ(e\room\NPC[3]\Collider), EntityX(e\room\RoomDoors[2]\frameobj,True), EntityZ(e\room\RoomDoors[2]\frameobj,True)) < 4.5 And dist < 5.0 Then
					
					e\room\NPC[3]\State = 9
					
					If e\room\NPC[7]\SoundChn<>0 Then
						If ChannelPlaying(e\room\NPC[7]\SoundChn) Then
							StopChannel(e\room\NPC[7]\SoundChn)
							FreeSound_Strict e\room\NPC[7]\Sound 
							e\room\NPC[7]\Sound=0											
						EndIf
					EndIf
					
					FreeSound_Strict e\room\NPC[3]\Sound
					e\room\NPC[3]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Ulgrin\EscortDone"+Rand(1,5)+".ogg")
					e\room\NPC[3]\SoundChn = PlaySound2(e\room\NPC[3]\Sound, mainPlayer\cam, e\room\NPC[3]\Collider)
					
					PositionEntity e\room\NPC[6]\Collider, EntityX(e\room\obj,True)-1190*RoomScale, 450*RoomScale, EntityZ(e\room\obj, True)+456*RoomScale, True
					ResetEntity e\room\NPC[6]\Collider
					PointEntity e\room\NPC[6]\Collider, e\room\obj
					e\room\NPC[6]\CurrSpeed = 0
					e\room\NPC[6]\State = 0
					
					e\EventState3 = 910
					
					e\room\RoomDoors[3]\locked = False
					UseDoor(e\room\RoomDoors[3],False)
					e\room\RoomDoors[3]\locked = True
					
					e\room\RoomDoors[2]\locked = False
					UseDoor(e\room\RoomDoors[2],False)
					e\room\RoomDoors[2]\locked = True
				EndIf
			Else
				e\room\NPC[3]\State = 9
				;PointEntity e\room\NPC[3]\obj, Collider
				;RotateEntity e\room\NPC[3]\Collider,0,CurveAngle(EntityYaw(e\room\NPC[3]\obj),EntityYaw(e\room\NPC[3]\Collider),20.0),0,True	
				e\room\NPC[4]\State = 9
				;PointEntity e\room\NPC[4]\obj, Collider
				;RotateEntity e\room\NPC[4]\Collider,0,CurveAngle(EntityYaw(e\room\NPC[4]\obj),EntityYaw(e\room\NPC[4]\Collider),20.0),0,True
				
				If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\obj), EntityZ(e\room\obj)) < 4.0 Then
					e\room\RoomDoors[2]\locked = False
					UseDoor(e\room\RoomDoors[2],False)
					e\room\RoomDoors[2]\locked = True
					e\EventState3 = 0
					e\room\NPC[3]\State = 0
					e\room\NPC[4]\State = 0
					e\room\NPC[5]\State = 0
					
					UseDoor(e\room\RoomDoors[1],False)
				EndIf	
			EndIf
			
			;the scientist sitting at his desk
			If e\room\NPC[7]<>Null Then
				RotateEntity e\room\NPC[7]\Collider,0,180+Sin(MilliSecs2()/20)*3,0,True
				PositionEntity e\room\NPC[7]\Collider, EntityX(e\room\obj,True)-3361*RoomScale,-315*RoomScale,EntityZ(e\room\obj,True)-2165*RoomScale
				ResetEntity e\room\NPC[7]\Collider
				
				e\room\NPC[7]\State = 6
				SetNPCFrame(e\room\NPC[7], 182)
				
				If e\room\NPC[6]\State=1 And e\room\NPC[7]\Sound<>0 Then 
					If e\room\NPC[7]\SoundChn<>0 Then
						If (Not ChannelPlaying(e\room\NPC[7]\SoundChn)) Then FreeSound_Strict e\room\NPC[7]\Sound : e\room\NPC[7]\Sound=0	
						
					EndIf
					
					If e\room\NPC[7]\Sound<>0 Then e\room\NPC[7]\SoundChn = LoopSound2(e\room\NPC[7]\Sound, e\room\NPC[7]\SoundChn, mainPlayer\cam, e\room\NPC[7]\Collider, 7.0)
				EndIf
			EndIf
			
			For i = 3 To 4
				If e\room\NPC[i]\Sound <> 0 Then
					If ChannelPlaying(e\room\NPC[i]\SoundChn)=False Then
						FreeSound_Strict e\room\NPC[i]\Sound 
						e\room\NPC[i]\Sound=0
					Else
						e\room\NPC[i]\SoundChn=LoopSound2(e\room\NPC[i]\Sound, e\room\NPC[i]\SoundChn, mainPlayer\cam, e\room\NPC[i]\Collider)
					EndIf
				EndIf
			Next
			
		Else
			
			;ambience inside the chamber
			If IntroSFX(18)<>0 Then e\SoundCHN2 = LoopSound2(IntroSFX(18), e\SoundCHN2, mainPlayer\cam, e\room\Objects[4], 6)
			
			;[Block]
			If e\EventState = 0 Then
				If mainPlayer\currRoom = e\room Then
					IntroSFX(0) = LoadSound_Strict("SFX\Room\Intro\Scientist\Franklin\EnterChamber.ogg")
					IntroSFX(1) = LoadSound_Strict("SFX\Room\Intro\Scientist\Franklin\Approach173.ogg")
					IntroSFX(2) = LoadSound_Strict("SFX\Room\Intro\Scientist\Franklin\Problem.ogg")
					For i = 4 To 6
						IntroSFX(i) = LoadSound_Strict("SFX\Room\Intro\Scientist\Franklin\Refuse" + (i - 3) + ".ogg")
					Next
					IntroSFX(16) = LoadSound_Strict("SFX\Room\Intro\Horror.ogg")
					IntroSFX(17) = LoadSound_Strict("SFX\Room\Intro\See173.ogg")
					IntroSFX(18) = LoadSound_Strict("SFX\Room\Intro\173Chamber.ogg")
					
					Curr173\Idle = True
					
					e\room\NPC[0] = CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[0], True), EntityY(e\room\Objects[0], True), EntityZ(e\room\Objects[0], True))
					e\room\NPC[0]\Angle = 180
					
					e\room\NPC[1] = CreateNPC(NPCtypeD, EntityX(e\room\Objects[1], True), 0.5, EntityZ(e\room\Objects[1], True))
					PointEntity(e\room\NPC[1]\Collider, e\room\Objects[5])
					
					e\room\NPC[2] = CreateNPC(NPCtypeD, EntityX(e\room\Objects[2], True), 0.5, EntityZ(e\room\Objects[2], True))
					PointEntity(e\room\NPC[2]\Collider, e\room\Objects[5])
					tex = LoadTexture_Strict("GFX\npcs\classd2.jpg")
					EntityTexture e\room\NPC[2]\obj, tex
					FreeTexture tex
					
					e\room\NPC[3] = CreateNPC(NPCtypeGuard, e\room\x-4096*RoomScale+Rnd(-0.3,0.3), 0.3, e\room\z+Rand(860,896)*RoomScale)
					RotateEntity e\room\NPC[3]\Collider,0,e\room\angle+180,0
					e\room\NPC[3]\State = 7
					e\room\NPC[4] = CreateNPC(NPCtypeGuard, e\room\x-3840*RoomScale, 0.3, e\room\z+768*RoomScale)
					RotateEntity e\room\NPC[4]\Collider,0,e\room\angle+135,0
					e\room\NPC[4]\State = 7
					;SetNPCFrame(e\room\NPC[4], Rnd(1035, 1326))
					e\room\NPC[5] = CreateNPC(NPCtypeGuard, e\room\x-8288*RoomScale, 0.3, e\room\z+1096*RoomScale)
					e\room\NPC[5]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Music"+Rand(1,5)+".ogg")
					RotateEntity e\room\NPC[5]\Collider, 0, e\room\angle+180, 0, True
					e\room\NPC[5]\State = 7
					e\room\NPC[5]\Sound2 = LoadSound_Strict("SFX\Room\Intro\Guard\PlayerEscape.ogg")
					e\room\NPC[6] = CreateNPC(NPCtypeD, e\room\x-3712*RoomScale, -0.3, e\room\z-2208*RoomScale)
					tex = LoadTexture_Strict("GFX\npcs\scientist2.jpg")
					EntityTexture e\room\NPC[6]\obj, tex
					FreeTexture tex
					
					e\room\NPC[7] = CreateNPC(NPCtypeD, e\room\x-3712*RoomScale, -0.3, e\room\z-2208*RoomScale)
					tex = LoadTexture_Strict("GFX\npcs\scientist.jpg")
					e\room\NPC[7]\Sound = LoadSound_Strict("SFX\Room\Intro\Scientist\Conversation.ogg")
					EntityTexture e\room\NPC[7]\obj, tex
					FreeTexture tex
					
					pvt = CreatePivot()
					RotateEntity pvt,90,0,0
					
					e\room\NPC[8] = CreateNPC(NPCtypeGuard, e\room\x-3800.0*RoomScale, 1.0, e\room\z-3900.0*RoomScale)
					e\room\NPC[8]\State = 7
					e\room\NPC[9] = CreateNPC(NPCtypeD, e\room\x-4000.0*RoomScale, 1.1, e\room\z-3900.0*RoomScale)
					e\room\NPC[9]\State2 = 1.0
					tex = LoadTexture_Strict("GFX\npcs\classd3.jpg")
					EntityTexture e\room\NPC[9]\obj, tex
					FreeTexture tex
					e\room\NPC[10] = CreateNPC(NPCtypeGuard, e\room\x-4200.0*RoomScale, 1.0, e\room\z-3900.0*RoomScale)
					e\room\NPC[10]\State = 7
					
					For i = 8 To 10
						PositionEntity pvt,EntityX(e\room\NPC[i]\Collider),EntityY(e\room\NPC[i]\Collider),EntityZ(e\room\NPC[i]\Collider)
						EntityPick pvt,20.0
						If PickedEntity() <> 0
							PositionEntity e\room\NPC[i]\Collider,PickedX(),PickedY(),PickedZ(),True
							AlignToVector e\room\NPC[i]\Collider,-PickedNX(),-PickedNY(),-PickedNZ(),3
							RotateEntity e\room\NPC[i]\Collider,0,90,0
						EndIf
					Next
					
					FreeEntity pvt
					
					PositionEntity(Curr173\Collider, EntityX(e\room\Objects[5], True), 0.5, EntityZ(e\room\Objects[5], True))
					ResetEntity(Curr173\Collider)
					
					PositionEntity mainPlayer\collider, mainPlayer\currRoom\x-(3072+1024)*RoomScale, 0.3, mainPlayer\currRoom\z+192.0*RoomScale
					ResetEntity mainPlayer\collider
					
					e\EventState = 1
					e\EventState3 = 1
				EndIf
			ElseIf e\EventState < 10000
				If e\room\NPC[6]\SoundChn<>0 Then 
					If ChannelPlaying (e\room\NPC[6]\SoundChn) Then
						e\room\NPC[6]\State = 6
						;PointEntity e\room\NPC[6]\Collider, e\room\obj
						If AnimTime(e\room\NPC[6]\obj)=>325 Then
							Animate2(e\room\NPC[6]\obj, AnimTime(e\room\NPC[6]\obj),326,328, 0.02, False)
						Else
							Animate2(e\room\NPC[6]\obj, AnimTime(e\room\NPC[6]\obj),320,328, 0.05, False)
						EndIf
					Else
						Animate2(e\room\NPC[6]\obj,AnimTime(e\room\NPC[6]\obj), 328,320,-0.02, False)
					EndIf
				EndIf
				
				If IntroSFX(17)<>0 Then
					If EntityVisible(Curr173\Collider, mainPlayer\collider) Then
						If EntityInView(Curr173\obj, mainPlayer\cam) Then
							Msg = "Press "+KeyName(keyBinds\blink)+" to blink."
							MsgTimer = 70*4
							PlaySound_Strict IntroSFX(17)
							IntroSFX(17)=0
						EndIf
					EndIf
				EndIf
				
				e\EventState = Min(e\EventState + (FPSfactor / 3), 5000)
				If e\EventState >= 130 And e\EventState - (FPSfactor/3) < 130 Then
					e\room\NPC[6]\SoundChn = PlaySound_Strict(IntroSFX(0))
				ElseIf e\EventState > 230
					temp = True
					For i = 1 To 2
						If Distance(EntityX(e\room\NPC[i]\Collider), EntityZ(e\room\NPC[i]\Collider), EntityX(e\room\Objects[i + 2], True), EntityZ(e\room\Objects[i + 2], True)) > 0.3 Then
							
							PointEntity(e\room\NPC[i]\obj, e\room\Objects[i + 2])
							RotateEntity(e\room\NPC[i]\Collider, 0, CurveValue(EntityYaw(e\room\NPC[i]\obj),EntityYaw(e\room\NPC[i]\Collider),15.0),0)
							;moveentity(e\room\npc(i).mainPlayer\collider, 0, 0, 0.015 * FPSfactor)
							If e\EventState > (200 + i * 30) Then e\room\NPC[i]\State = 1
							temp = False
						Else
							e\room\NPC[i]\State = 0
							
							PointEntity(e\room\NPC[i]\obj, e\room\Objects[5])
							RotateEntity(e\room\NPC[i]\Collider, 0, CurveValue(EntityYaw(e\room\NPC[i]\obj),EntityYaw(e\room\NPC[i]\Collider),15.0), 0)
							
						EndIf
					Next
					
					If EntityX(mainPlayer\collider) < (EntityX(e\room\obj)) + 408.0 * RoomScale Then
						If e\EventState => 450 And e\EventState - (FPSfactor/3) < 450 Then ;"mene huoneeseen"
							e\room\NPC[6]\SoundChn = PlaySound_Strict(IntroSFX(4))
						ElseIf e\EventState => 650 And e\EventState - (FPSfactor/3) < 650 ;"viimeinen varoitus, 5 sek aikaa"
							e\room\NPC[6]\SoundChn = PlaySound_Strict(IntroSFX(5))
						ElseIf e\EventState => 850 And e\EventState - (FPSfactor/3) < 850 ;"fire at will"
							UseDoor(e\room\RoomDoors[1],False)
							;e\room\RoomDoors[1]\open = False
							e\room\NPC[6]\SoundChn = PlaySound_Strict(IntroSFX(6))
						ElseIf e\EventState > 1000
							e\room\NPC[0]\State = 1
							e\room\NPC[0]\State2= 10
							e\room\NPC[0]\State3= 1
							e\room\NPC[3]\State = 11
							e\room\RoomDoors[2]\locked = False
							UseDoor(e\room\RoomDoors[2],False)
							e\room\RoomDoors[2]\locked = True
							e\EventState2 = 1
							Exit
						EndIf
						
						If e\EventState > 850 Then
							PositionEntity(mainPlayer\collider, Min(EntityX(mainPlayer\collider), EntityX(e\room\obj) + 352.0 * RoomScale), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
						End If
					ElseIf temp = True ;pelaaja ja molemmat npc:t huoneessa
						e\EventState = 10000
						UseDoor(e\room\RoomDoors[1],False)
					End If
				End If
				
				e\room\NPC[6]\State = 7
				PointEntity e\room\NPC[6]\obj, Collider
				RotateEntity e\room\NPC[6]\Collider,0,CurveValue(EntityYaw(e\room\NPC[6]\obj),EntityYaw(e\room\NPC[6]\Collider),20.0),0,True	
				
				PositionEntity(Curr173\Collider, EntityX(e\room\Objects[5], True), EntityY(Curr173\Collider), EntityZ(e\room\Objects[5], True))
				RotateEntity(Curr173\Collider, 0, 0, 0, True)
				ResetEntity(Curr173\Collider)
			ElseIf e\EventState < 14000 ; player is inside the room
				e\EventState = Min(e\EventState + FPSfactor, 13000)
				
				If e\EventState < 10300 Then
					PositionEntity(mainPlayer\collider, Max(EntityX(mainPlayer\collider), EntityX(e\room\obj) + 352.0 * RoomScale), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
				End If
				
				e\room\NPC[6]\State = 6
				PointEntity e\room\NPC[6]\obj, Curr173\Collider
				RotateEntity e\room\NPC[6]\Collider,0,CurveValue(EntityYaw(e\room\NPC[6]\obj),EntityYaw(e\room\NPC[6]\Collider),50.0),0,True	
				
				If e\EventState => 10300 And e\EventState - FPSfactor < 10300 Then ;"please approach SCP-173..."
					e\SoundCHN = PlaySound_Strict(IntroSFX(1))
					PositionEntity(mainPlayer\collider, Max(EntityX(mainPlayer\collider), EntityX(e\room\obj) + 352.0 * RoomScale), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
				ElseIf e\EventState => 10440 And e\EventState - FPSfactor < 10440 ;the door opens
					UseDoor(e\room\RoomDoors[1],False)
					e\SoundCHN = PlaySound_Strict(IntroSFX(7)) ;bang
				ElseIf e\EventState => 10740 And e\EventState - FPSfactor < 10740 ;"there seems to be a problem..."
					e\SoundCHN = PlaySound_Strict(IntroSFX(2))
				ElseIf e\EventState => 11145 And e\EventState - FPSfactor < 11145;"I don't like this"
					e\SoundCHN = PlaySound_Strict(IntroSFX(10))
					e\room\NPC[1]\Sound = LoadSound_Strict("SFX\Room\Intro\ClassD\DontLikeThis.ogg")
					PlaySound2(e\room\NPC[1]\Sound, mainPlayer\cam, e\room\NPC[1]\Collider)
				ElseIf e\EventState => 11561 And e\EventState - FPSfactor < 11561 ;lights go out
					e\EventState = 14000
					PlaySound_Strict IntroSFX(16)
					e\room\NPC[2]\Sound = LoadSound_Strict("SFX\Room\Intro\ClassD\Breen.ogg")
					PlaySound2(e\room\NPC[2]\Sound, mainPlayer\cam, e\room\NPC[1]\Collider)
				End If
				
				;Guard Alert
				If e\EventState => 10440 And e\EventState - FPSfactor < 11561
					If EntityX(mainPlayer\collider) < EntityX(e\room\RoomDoors[1]\frameobj, True)
						If e\room\NPC[0]\State <> 12
							e\room\NPC[0]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Balcony\Alert"+Rand(1,2)+".ogg")
							e\room\NPC[0]\SoundChn = PlaySound2(e\room\NPC[0]\Sound,mainPlayer\cam,e\room\NPC[0]\Collider,20)
							e\room\NPC[0]\State = 12
							e\room\NPC[0]\State2 = 1
						EndIf
					EndIf
				EndIf
				
				If e\EventState > 10300 Then 
					
					If e\EventState > 10560 Then
						If e\EventState < 10750 Then
							e\room\NPC[1]\State = 1
							e\room\NPC[1]\CurrSpeed = 0.005										
						Else
							e\room\NPC[1]\State = 0
							e\room\NPC[1]\CurrSpeed = CurveValue(0,e\room\NPC[1]\CurrSpeed,10)	
						EndIf
						
					EndIf
					
					If AnimTime(e\room\NPC[6]\obj)=>325 Then
						Animate2(e\room\NPC[6]\obj, AnimTime(e\room\NPC[6]\obj),326,328, 0.02, False)
					Else
						Animate2(e\room\NPC[6]\obj, AnimTime(e\room\NPC[6]\obj),320,328, 0.05, False)
					EndIf
				EndIf
				
				PositionEntity(Curr173\Collider, EntityX(e\room\Objects[5], True),EntityY(Curr173\Collider), EntityZ(e\room\Objects[5], True))
				RotateEntity(Curr173\Collider, 0, 0, 0, True)
				ResetEntity(Curr173\Collider)
			ElseIf e\EventState < 20000
				pvt% = CreatePivot()
				PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(Curr173\Collider,True)-0.05, EntityZ(mainPlayer\cam)
				PointEntity(pvt, Curr173\Collider)
				RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 40), 0)
				
				TurnEntity(pvt, 90, 0, 0)
				mainPlayer\headPitch = CurveAngle(EntityPitch(pvt), mainPlayer\headPitch + 90.0, 40)
				mainPlayer\headPitch=user_camera_pitch-90
				FreeEntity pvt
				
				e\room\NPC[6]\State = 6
				PointEntity e\room\NPC[6]\obj, Curr173\Collider
				RotateEntity e\room\NPC[6]\Collider,0,CurveValue(EntityYaw(e\room\NPC[6]\obj),EntityYaw(e\room\NPC[6]\Collider),20.0),0,True	
				Animate2(e\room\NPC[6]\obj, AnimTime(e\room\NPC[6]\obj),357,381, 0.05)
				
				e\EventState = Min(e\EventState + FPSfactor, 19000)
				If e\EventState < 14100 Then ;lights go out and 173 kills the first Class D
					
					;14000-14030
					If e\EventState < 14060 Then
						mainPlayer\blinkTimer = Max((14000-e\EventState)/2-Rnd(0,1.0),-10)
						;0-60,   90-640
						If mainPlayer\blinkTimer = -10 Then
							PointEntity Curr173\Collider, e\room\NPC[1]\obj
							RotateEntity(Curr173\Collider, 0, EntityYaw(Curr173\Collider),0)
							MoveEntity Curr173\Collider, 0,0,Curr173\Speed*0.6*FPSfactor
							
							Curr173\SoundChn = LoopSound2(StoneDragSFX, Curr173\SoundChn, mainPlayer\cam, Curr173\Collider, 10.0, Curr173\State)
							
							Curr173\State = CurveValue(1.0, Curr173\State, 3)
							
						Else
							Curr173\State = Max(0, Curr173\State - FPSfactor / 20)
						EndIf
					ElseIf e\EventState < 14065
						mainPlayer\blinkTimer = -10
						If e\room\NPC[1]\State = 0 Then PlaySound2(NeckSnapSFX(Rand(0, 2)),mainPlayer\cam,Curr173\Collider)
						
						;e\room\NPC[0]\State=8
						SetAnimTime e\room\NPC[1]\obj, 0
						e\room\NPC[1]\State = 6
						PositionEntity(Curr173\Collider, EntityX(e\room\NPC[1]\obj), EntityY(Curr173\Collider), EntityZ(e\room\NPC[1]\obj))
						ResetEntity(Curr173\Collider)
						PointEntity(Curr173\Collider, e\room\NPC[2]\Collider)
						
						e\room\NPC[2]\State = 3
						RotateEntity e\room\NPC[2]\Collider, 0, EntityYaw(e\room\NPC[2]\Collider), 0
						Animate2(e\room\NPC[2]\obj, AnimTime(e\room\NPC[2]\obj),406,382,-0.01*15)
						MoveEntity e\room\NPC[2]\Collider, 0,0,-0.01*FPSfactor
						
						;Guard WTF
						e\room\NPC[0]\State = 12
						If e\room\NPC[0]\Sound<>0
							StopChannel(e\room\NPC[0]\SoundChn)
							FreeSound_Strict(e\room\NPC[0]\Sound)
							e\room\NPC[0]\Sound = 0
						EndIf
						e\room\NPC[0]\Angle = 180
						e\room\NPC[0]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Balcony\WTF"+Rand(1,2)+".ogg")
						e\room\NPC[0]\SoundChn = PlaySound2(e\room\NPC[0]\Sound,mainPlayer\cam,e\room\NPC[0]\Collider,20)
						e\room\NPC[0]\State2 = 0
					Else
						Animate2(e\room\NPC[1]\obj, AnimTime(e\room\NPC[1]\obj), 0, 19, 0.2, False)
						If e\room\NPC[2]\Sound=0 Then 
							e\room\NPC[2]\Sound = LoadSound_Strict("SFX\Room\Intro\ClassD\Gasp.ogg")
							PlaySound2 (e\room\NPC[2]\Sound, mainPlayer\cam, e\room\NPC[2]\Collider, 8.0)	
						EndIf									
					EndIf
					
					If e\EventState > 14080 And e\EventState - FPSfactor < 14080 Then PlaySound_Strict(IntroSFX(12))
					mainPlayer\camShake = 3
				ElseIf e\EventState < 14200 ;kills the other class d
					Animate2(e\room\NPC[1]\obj, AnimTime(e\room\NPC[1]\obj), 0, 19, 0.2, False)
					
					;Animate2(e\room\NPC[0]\obj, AnimTime(e\room\NPC[0]\obj), 110, 120, 0.2, False)
					e\room\NPC[0]\State=8
					If e\EventState > 14105 Then
						If e\room\NPC[2]\State<>6 Then PlaySound2 (NeckSnapSFX(1), mainPlayer\cam, e\room\NPC[2]\Collider, 8.0)
						e\room\NPC[2]\State = 6
						PositionEntity(Curr173\Collider, EntityX(e\room\NPC[2]\obj), EntityY(Curr173\Collider), EntityZ(e\room\NPC[2]\obj))
						ResetEntity(Curr173\Collider)
						PointEntity(Curr173\Collider, mainPlayer\collider)
					EndIf
					If e\EventState < 14130 Then 
						SetAnimTime e\room\NPC[2]\obj,50
						mainPlayer\blinkTimer = -10; : LightBlink = 1.0
					Else 
						Animate2(e\room\NPC[2]\obj, AnimTime(e\room\NPC[2]\obj), 50, 60, 0.2, False)
						Curr173\Idle = False
					EndIf
					If e\EventState > 14100 And e\EventState - FPSfactor < 14100 Then PlaySound_Strict(IntroSFX(8))
					If e\EventState < 14150 Then mainPlayer\camShake = 5
				Else
					Animate2(e\room\NPC[2]\obj, AnimTime(e\room\NPC[2]\obj), 45, 60, 0.2, False)
					If e\EventState > 14300 Then 
						If e\EventState > 14600 And e\EventState < 14700 Then mainPlayer\blinkTimer = -10; : LightBlink = 1.0
						If EntityX(mainPlayer\collider) < (EntityX(e\room\obj)) + 448.0 * RoomScale Then e\EventState = 20000
					EndIf
				End If
			ElseIf e\EventState < 30000
				e\EventState = Min(e\EventState + FPSfactor, 30000)
				If e\EventState < 20100 Then
					mainPlayer\camShake = 2
				Else
					If e\EventState < 20200 Then ;lights go out again and 173 teleports next to the guard
						If e\EventState > 20105 And e\EventState - FPSfactor < 20105 Then 
							PlaySound_Strict(IntroSFX(9))
							PositionEntity(e\room\NPC[0]\Collider, EntityX(e\room\obj) - 160.0 * RoomScale, EntityY(e\room\NPC[0]\Collider) + 0.1, EntityZ(e\room\obj) + 1280.0 * RoomScale)
							ResetEntity(e\room\NPC[0]\Collider)										
							
							;Guard OhShit
							If e\room\NPC[0]\Sound<>0
								StopChannel(e\room\NPC[0]\SoundChn)
								FreeSound_Strict(e\room\NPC[0]\Sound)
								e\room\NPC[0]\Sound = 0
							EndIf
							e\room\NPC[0]\Sound = LoadSound_Strict("SFX\Room\Intro\Guard\Balcony\OhSh.ogg")
							e\room\NPC[0]\SoundChn = PlaySound2(e\room\NPC[0]\Sound,mainPlayer\cam,e\room\NPC[0]\Collider,20)
						EndIf
						If e\EventState > 20105 Then
							Curr173\Idle = True 
							PointEntity(e\room\NPC[0]\Collider, Curr173\obj)
							PositionEntity(Curr173\Collider, EntityX(e\room\obj) - 608.0 * RoomScale, EntityY(e\room\obj) + 480.0 * RoomScale, EntityZ(e\room\obj) + 1312.0 * RoomScale)
							ResetEntity(Curr173\Collider)
							PointEntity(Curr173\Collider, e\room\NPC[0]\Collider)
						EndIf
						
						mainPlayer\blinkTimer = -10; : LightBlink = 1.0
						mainPlayer\camShake = 3
					ElseIf e\EventState < 20300 ;lights on, the guard starts shooting at 173
						PointEntity(e\room\NPC[0]\Collider, Curr173\Collider)
						MoveEntity(e\room\NPC[0]\Collider, 0, 0, -0.002)
						e\room\NPC[0]\State = 2
						UpdateSoundOrigin(e\room\NPC[0]\SoundChn,mainPlayer\cam,e\room\NPC[0]\Collider,20)
						If e\EventState > 20260 And e\EventState - FPSfactor < 20260 Then PlaySound_Strict(IntroSFX(12))
					Else ;lights out, guard dies
						
						If e\EventState - FPSfactor < 20300 Then
							mainPlayer\blinkTimer = -10; : LightBlink = 1.0
							mainPlayer\camShake = 3
							PlaySound_Strict(IntroSFX(11))
							PlaySound2 (NeckSnapSFX(1), mainPlayer\cam, e\room\NPC[0]\Collider, 8.0)
							
							Curr173\Idle = False
							
							e\SoundCHN = PlaySound_Strict(IntroSFX(15))
							
							PositionEntity(Curr173\Collider, EntityX(mainPlayer\currRoom\obj) - 400.0 * RoomScale, 100.0, EntityZ(mainPlayer\currRoom\obj) + 1072.0 * RoomScale)
							ResetEntity(Curr173\Collider)
							
							For r.Rooms = Each Rooms
								If r\RoomTemplate\Name = "start" Then
									DebugLog "tostart"
									;Msg = "Press "+KeyName(keyBinds\save)+" to save."
									;MsgTimer = 70*8
									
									mainPlayer\currRoom = r
									
									x# = EntityX(r\obj, True)+3712*RoomScale
									y# = 384.0*RoomScale
									z# = EntityZ(r\obj, True)+1312*RoomScale
									
									PositionEntity(mainPlayer\collider, x  + (EntityX(mainPlayer\collider) - EntityX(e\room\obj)), y+EntityY(mainPlayer\collider)+0.4, z + (EntityZ(mainPlayer\collider) - EntityZ(e\room\obj)))
									mainPlayer\dropSpeed = 0
									ResetEntity(mainPlayer\collider)
									
									For i = 0 To 2
										PositionEntity(e\room\NPC[i]\Collider, x + (EntityX(e\room\NPC[i]\Collider) - EntityX(e\room\obj)), y+EntityY(e\room\NPC[i]\Collider)+0.4, z + (EntityZ(e\room\NPC[i]\Collider) - EntityZ(e\room\obj)))
										ResetEntity(e\room\NPC[i]\Collider)
									Next
									
									FreeSound_Strict Music(13)
									ShouldPlay = 0
									
									For i = 0 To 9
										FreeSound_Strict IntroSFX(i)
									Next
									For i = 16 To 18
										FreeSound_Strict IntroSFX(i)
									Next
									
									r\NPC[0]=e\room\NPC[0]
									r\NPC[0]\State=8
									
									For do.doors = Each Doors
										If do\room = e\room Then
											Delete do
										EndIf
									Next
									
									For w.waypoints = Each WayPoints
										If w\room = e\room Then 
											FreeEntity w\obj
											Delete w
										EndIf
									Next
									
									For i = 3 To 5
										RemoveNPC(e\room\NPC[i])
									Next
									r\NPC[1]=e\room\NPC[6]
									RemoveNPC(e\room\NPC[7])
									
									FreeEntity e\room\obj
									Delete e\room
									
									For sc.SecurityCams = Each SecurityCams
										If sc\room = e\room Then Delete sc
									Next
									
									ShowEntity Fog
									AmbientLight Brightness, Brightness, Brightness
									CameraFogRange(mainPlayer\cam, CameraFogNear, CameraFogFar)
									CameraFogMode(mainPlayer\cam, 1)
									
									e\EventState2 = 1
									
									;For i = 8 To 10
									;	RemoveNPC(e\room\NPC[i])
									;Next
									
									Exit
								EndIf
							Next
						EndIf
						
					EndIf
					
				EndIf
			EndIf
			
			;[End block]
			
		EndIf
		
	Else
		If KillTimer<0 Then
			If e\room\NPC[3]\State = 1 Then 
				LoadEventSound(e,"SFX\Room\Intro\Guard\Ulgrin\EscortTerminated.ogg")
				PlaySound_Strict e\Sound
			EndIf
		EndIf
		
		For i = 0 To 6
			If IntroSFX(i)<>0 Then FreeSound_Strict IntroSFX(i) : IntroSFX(i)=0
		Next
		FreeSound_Strict IntroSFX(16) : IntroSFX(16)=0
		
		e\EventState2 = 1
	EndIf
	
	If mainPlayer\currRoom = e\room Then
		CameraFogMode(mainPlayer\cam, 0)
		AmbientLight (140, 140, 140)
		HideEntity(Fog)
		
		LightVolume = 4.0
		TempLightVolume = 4.0
	Else
		DebugLog "delete intro event"
		RemoveEvent(e)		
	EndIf	
	;[End Block]
End Function


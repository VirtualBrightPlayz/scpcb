Function FillRoomPocketDimension(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    Local hallway = LoadMesh_Strict("GFX\map\pocketdimension2.b3d") ;the tunnels in the first room
    r\Objects[8]=LoadMesh_Strict("GFX\map\pocketdimension3.b3d")	;the room with the throne, moving pillars etc 
    r\Objects[9]=LoadMesh_Strict("GFX\map\pocketdimension4.b3d") ;the flying pillar
    r\Objects[10]=CopyEntity(r\Objects[9])
    
    r\Objects[11]=LoadMesh_Strict("GFX\map\pocketdimension5.b3d") ;the pillar room
    
    
    terrain = LoadMesh_Strict("GFX\map\pocketdimensionterrain.b3d")
    ScaleEntity terrain,RoomScale,RoomScale,RoomScale,True
    ;RotateEntity terrain,0,e\room\angle,0,True
    PositionEntity terrain, 0, 2944, 0, True
    
    
    
    CreateItem("Burnt Note", "paper", EntityX(r\obj),0.5,EntityZ(r\obj)+3.5)
    
    For n = 0 To -1;4
        
        Select n
            Case 0
                entity = hallway 					
            Case 1
                entity = r\Objects[8]						
            Case 2
                entity = r\Objects[9]						
            Case 3
                entity = r\Objects[10]							
            Case 4
                entity = r\Objects[11]							
        End Select 
        
        ;If BumpEnabled Then 
        ;	
        ;	For i = 1 To CountSurfaces(entity)
        ;		sf = GetSurface(entity,i)
        ;		b = GetSurfaceBrush( sf )
        ;		t = GetBrushTexture(b,1)
        ;		texname$ =  StripPath(TextureName(t))
        ;		mat.Materials=GetCache(texname)
        ;		If mat<>Null Then
        ;			If mat\Bump<>0 Then
        ;				t1 = GetBrushTexture(b,0)
        ;				
        ;				BrushTexture b, t1, 0, 0	
        ;				BrushTexture b, mat\Bump, 0, 1
        ;				BrushTexture b, t, 0, 2					
        ;				
        ;				PaintSurface sf,b
        ;				
        ;				If t1<>0 Then FreeTexture t1 : t1=0
        ;			EndIf
        ;		EndIf
        ;		
        ;		If t<>0 Then FreeTexture t : t=0
        ;		If b<>0 Then FreeBrush b : b=0
        ;	Next
        ;	
        ;EndIf
        
    Next
    
    For i = 8 To 11
        ScaleEntity (r\Objects[i],RoomScale,RoomScale,RoomScale)
        EntityType r\Objects[i], HIT_MAP
        EntityPickMode r\Objects[i], 3
        PositionEntity(r\Objects[i],r\x,r\y,r\z+32.0,True)
    Next
    
    ScaleEntity (terrain,RoomScale,RoomScale,RoomScale)
    EntityType terrain, HIT_MAP
    EntityPickMode terrain, 3
    PositionEntity(terrain,r\x,r\y+2944.0*RoomScale,r\z+32.0,True)			
    
    r\RoomDoors[0] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0-1024*RoomScale,0,r,False)
    r\RoomDoors[1] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0+1024*RoomScale,180,r,False)
    
    de.Decals = CreateDecal(18, r\x-(1536*RoomScale), 0.02,r\z+608*RoomScale+32.0, 90,0,0)
    EntityParent(de\obj, r\obj)
    de\Size = Rnd(0.8, 0.8)
    de\blendmode = 2
    de\fx = 1+8
    ScaleSprite(de\obj, de\Size, de\Size)
    EntityFX(de\obj, 1+8)
    EntityBlend de\obj, 2
    
    ScaleEntity (r\Objects[10],RoomScale*1.5,RoomScale*2.0,RoomScale*1.5,True)			
    PositionEntity(r\Objects[11],r\x,r\y,r\z+64.0,True)			
    
    For i = 1 To 8
        r\Objects[i-1] = CopyEntity(hallway) ;CopyMesh
        ScaleEntity (r\Objects[i-1],RoomScale,RoomScale,RoomScale)
        angle# = (i-1) * (360.0/8.0)
        
        EntityType r\Objects[i-1], HIT_MAP
        EntityPickMode r\Objects[i-1], 3		
        
        RotateEntity(r\Objects[i-1],0,angle-90,0)
        PositionEntity(r\Objects[i-1],r\x+Cos(angle)*(512.0*RoomScale),0.0,r\z+Sin(angle)*(512.0*RoomScale))
        EntityParent (r\Objects[i-1], r\obj)
        
        If i < 6 Then 
            de.Decals = CreateDecal(i+7, r\x+Cos(angle)*(512.0*RoomScale)*3.0, 0.02,r\z+Sin(angle)*(512.0*RoomScale)*3.0, 90,angle-90,0)
            de\Size = Rnd(0.5, 0.5)
            de\blendmode = 2
            de\fx = 1+8
            ScaleSprite(de\obj, de\Size, de\Size)
            EntityFX(de\obj, 1+8)
            EntityBlend de\obj, 2
        EndIf				
    Next
    
    For i = 12 To 16
        r\Objects[i] = CreatePivot(r\Objects[11])
        Select i
            Case 12
                PositionEntity(r\Objects[i],r\x,r\y+200*RoomScale,r\z+64.0,True)	
            Case 13
                PositionEntity(r\Objects[i],r\x+390*RoomScale,r\y+200*RoomScale,r\z+64.0+272*RoomScale,True)	
            Case 14
                PositionEntity(r\Objects[i],r\x+838*RoomScale,r\y+200*RoomScale,r\z+64.0-551*RoomScale,True)	
            Case 15
                PositionEntity(r\Objects[i],r\x-139*RoomScale,r\y+200*RoomScale,r\z+64.0+1201*RoomScale,True)	
            Case 16
                PositionEntity(r\Objects[i],r\x-1238*RoomScale,r\y-1664*RoomScale,r\z+64.0+381*RoomScale,True)
        End Select 
        
    Next
    
    Local OldManEyes% = LoadTexture_Strict("GFX\npcs\oldmaneyes.jpg")
    r\Objects[17] = CreateSprite()
    ScaleSprite(r\Objects[17], 0.03, 0.03)
    EntityTexture(r\Objects[17], OldManEyes)
    EntityBlend (r\Objects[17], 3)
    EntityFX(r\Objects[17], 1 + 8)
    SpriteViewMode(r\Objects[17], 2)
    
    r\Objects[18] = LoadTexture_Strict("GFX\npcs\pdplane.png", 1+2)
    r\Objects[19] = LoadTexture_Strict("GFX\npcs\pdplaneeye.png", 1+2)		
    
    r\Objects[20] = CreateSprite()
    ScaleSprite(r\Objects[20], 8.0, 8.0)
    EntityTexture(r\Objects[20], r\Objects[18])
    EntityOrder r\Objects[20], 100
    EntityBlend (r\Objects[20], 2)
    EntityFX(r\Objects[20], 1 + 8)
    SpriteViewMode(r\Objects[20], 2)
    
    FreeTexture t
    FreeEntity hallway
End Function


Function UpdateEventPocketdimension(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	;eventstate: a timer for scaling the tunnels in the starting room
	;eventstate2:
		;0 if the player is in the starting room
		;1 if in the room with the throne, moving pillars, plane etc
		;12-15 if player is in the room with the tall pillars 
			;(goes down from 15 to 12 And 106 teleports from pillar to another, pillars being room\objects[12 to 15])
	;eventstate3:
		;1 when appearing in the tunnel that looks like the tunnels in hcz
		;2 after opening the door in the tunnel
		;otherwise 0
	
	If mainPlayer\currRoom = e\room Then
		ShowEntity e\room\obj
		
		mainPlayer\injuries = mainPlayer\injuries+FPSfactor*0.00005
		
		If (EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale) Then CurrStepSFX = 1
		
		If e\Sound = 0 Then LoadEventSound(e,"SFX\Room\PocketDimension\Rumble.ogg")
		If e\Sound2 = 0 Then e\Sound2 = LoadEventSound(e,"SFX\Room\PocketDimension\PrisonVoices.ogg",1)
		
		If e\EventState = 0 Then
			CameraFogColor mainPlayer\cam, 0,0,0
			CameraClsColor mainPlayer\cam, 0,0,0
			e\EventState = 0.1
		EndIf
		
		If Music(3)=0 Then Music(3) = LoadSound_Strict("SFX\Music\PD.ogg")	
		If EntityY(mainPlayer\collider)<2000*RoomScale Or e\EventState3=0 Or EntityY(mainPlayer\collider)>2608*RoomScale Then 
			ShouldPlay = 3
		Else 
			ShouldPlay = 0
		EndIf
		
		ScaleEntity(e\room\obj,RoomScale, RoomScale*(1.0 + Sin(e\EventState/14.0)*0.2), RoomScale)
		For i = 0 To 7
			ScaleEntity(e\room\Objects[i],RoomScale*(1.0 + Abs(Sin(e\EventState/21.0+i*45.0)*0.1)),RoomScale*(1.0 + Sin(e\EventState/14.0+i*20.0)*0.1), RoomScale,True)
		Next
		ScaleEntity(e\room\Objects[9],RoomScale*(1.5 + Abs(Sin(e\EventState/21.0+i*45.0)*0.1)),RoomScale*(1.0 + Sin(e\EventState/14.0+i*20.0)*0.1), RoomScale,True)
		
		e\EventState = e\EventState + FPSfactor
		
		If e\EventState2 = 0 Then 
			e\room\RoomDoors[0]\open = False
			e\room\RoomDoors[1]\open = False
			
			If e\EventState > 65*70 Then
				If Rand(800)=1 And Curr106\State =>0 Then	
					PlaySound_Strict HorrorSFX(8)
					Curr106\State = -0.1
					e\EventState = 601
				EndIf
			ElseIf Curr106\State > 0 ;106 circles around the starting room
				angle = (e\EventState/10 Mod 360)
				PositionEntity(Curr106\Collider, EntityX(e\room\obj), 0.2+0.35+Sin(e\EventState/14.0+i*20.0)*0.4, EntityX(e\room\obj))
				RotateEntity(Curr106\Collider, 0,angle,0)
				MoveEntity(Curr106\Collider,0,0,6.0-Sin(e\EventState/10.0))
				AnimateNPC(Curr106, 55, 104, 0.5)
				RotateEntity(Curr106\Collider, 0,angle+90,0)
				Curr106\Idle = True
			EndIf
		EndIf 
		
		If EntityDistance(mainPlayer\collider, Curr106\Collider) < 0.3 Then ;106 attacks if close enough to player
			Curr106\Idle = False
			Curr106\State = -11
		EndIf
		
		If e\EventState2 = 1 Then ;in the second room
			
			PositionEntity(e\room\Objects[9], EntityX(e\room\Objects[8],True)+3384*RoomScale, 0.0, EntityZ(e\room\Objects[8],True))
			
			TranslateEntity e\room\Objects[9], Cos(e\EventState*0.8)*5, 0, Sin(e\EventState*1.6)*4, True
			RotateEntity e\room\Objects[9],0,e\EventState * 2,0
			
			PositionEntity(e\room\Objects[10], EntityX(e\room\Objects[8],True), 0.0, EntityZ(e\room\Objects[8],True)+3384*RoomScale)
			
			TranslateEntity e\room\Objects[10], Sin(e\EventState*1.6)*4, 0, Cos(e\EventState*0.8)*5, True
			RotateEntity e\room\Objects[10],0,e\EventState * 2,0
			
			If e\EventState3 = 1 Or e\EventState3 = 2 Then ;the "trick room"
				If e\EventState3 = 1 And (e\room\RoomDoors[0]\openstate>150 Or e\room\RoomDoors[1]\openstate>150) Then
					PlaySound_Strict LoadTempSound("SFX\Horror\Horror16.ogg")
					mainPlayer\blurTimer = 800
					e\EventState3=2
				EndIf
				
				If EntityY(mainPlayer\collider)<5.0 Then e\EventState3 = 0
			Else
				;the trenches
				If EntityY(mainPlayer\collider)>6.0 Then
					ShouldPlay = 15
					If Music(15)=0 Then Music(15) = LoadSound("SFX\Music\PDTrench.ogg")
					
					CameraFogColor mainPlayer\cam, 38, 55, 47
					CameraClsColor mainPlayer\cam, 38, 55, 47
					
					If EntityX(e\room\Objects[20],True)<EntityX(e\room\Objects[8],True)-4000*RoomScale Then
						e\SoundCHN2 = PlaySound_Strict(e\Sound2)
						
						PositionEntity e\room\Objects[20], EntityX(mainPlayer\collider,True)+4000*RoomScale, 12.0, EntityZ(mainPlayer\collider,True)
					EndIf
					
					MoveEntity(mainPlayer\collider, 0, Min((12.0 - EntityY(mainPlayer\collider)),0.0)*FPSfactor, 0)
					
					x = -FPSfactor*RoomScale*4.0
					y = (17.0-Abs(EntityX(mainPlayer\collider)-EntityX(e\room\Objects[20]))*0.5)-EntityY(e\room\Objects[20])
					z = EntityZ(mainPlayer\collider,True)-EntityZ(e\room\Objects[20])
					TranslateEntity e\room\Objects[20], x, y, z,True
					RotateEntity e\room\Objects[20], -90-(EntityX(mainPlayer\collider)-EntityX(e\room\Objects[20]))*1.5, -90.0, 0.0, True
					
					
					;check if the plane can see the player
					Local safe=False
					For i = 0 To 2
						Select i
							Case 0
								x = -1452*RoomScale
								z = -37*RoomScale
							Case 1
								x = -121*RoomScale
								z = 188*RoomScale
							Case 2
								x = 1223*RoomScale
								z = -196*RoomScale							
						End Select
						
						x = x + EntityX(e\room\Objects[8],True)
						z = z + EntityZ(e\room\Objects[8],True)
						
						If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), x, z) < 200*RoomScale Then safe = True : Exit
					Next
					
					dist = EntityDistance(mainPlayer\collider, e\room\Objects[20])
					
					If e\SoundCHN2<>0 And ChannelPlaying(e\SoundCHN2)
						e\SoundCHN2 = LoopSound2(e\Sound2, e\SoundCHN2, mainPlayer\cam, mainPlayer\cam, 10.0, 0.3+(Not safe)*0.6)
					EndIf	
					
					If safe Then
						EntityTexture e\room\Objects[20], e\room\Objects[18]
					ElseIf dist < 8.0
						e\SoundCHN = LoopSound2(e\Sound, e\SoundCHN, mainPlayer\cam, e\room\Objects[20], 8.0)
						EntityTexture e\room\Objects[20], e\room\Objects[19]
						mainPlayer\injuries=Injuries+(8.0-dist)*FPSfactor*0.001
						
						If dist<7.0 Then 
							pvt% = CreatePivot()
							PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
							PointEntity(pvt, e\room\Objects[20])
							TurnEntity(pvt, 90, 0, 0)
							mainPlayer\headPitch = CurveAngle(EntityPitch(pvt), mainPlayer\headPitch + 90.0, 10.0)
							mainPlayer\headPitch=user_camera_pitch-90
							RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 10), 0)
							FreeEntity pvt
						EndIf
					EndIf
					
					mainPlayer\camShake = Max(4.0+((Not safe) * 4.0) - dist, 0.0)
					
					;check if player is at the sinkhole (the exit from the trench room)
					If EntityY(mainPlayer\collider)<8.5 Then
						LoadEventSound(e,"SFX\Room\PocketDimension\Rumble.ogg")
						LoadEventSound(e,"SFX\Room\PocketDimension\PrisonVoices.ogg",1)
						
						;move to the "exit room"
						mainPlayer\blurTimer = 1500
						e\EventState2=1
						mainPlayer\blinkTimer = -10
						
						PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[8],True)-400*RoomScale, -304*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
						
					EndIf
					
				Else
					e\EventState3 = 0
					
					For i = 9 To 10
						dist = Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider),EntityX(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True))
						If dist<6.0 Then 
							If dist<100.0*RoomScale Then
								pvt=CreatePivot()
								PositionEntity pvt, EntityX(e\room\Objects[i],True),EntityY(mainPlayer\collider),EntityZ(e\room\Objects[i],True)
								
								PointEntity pvt, Collider
								RotateEntity pvt, 0, Int(EntityYaw(pvt)/90)*90,0,True
								MoveEntity pvt, 0,0,100*RoomScale
								PositionEntity mainPlayer\collider, EntityX(pvt),EntityY(mainPlayer\collider),EntityZ(pvt)
								
								FreeEntity pvt
								
								If KillTimer = 0 Then
									DeathMSG = "In addition to the decomposed appearance typical of SCP-106's victims, the body exhibits injuries that have not been observed before: "
									DeathMSG = DeathMSG + "massive skull fracture, three broken ribs, fractured shoulder and multiple heavy lacerations."
									
									PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
									KillTimer=-1.0
								EndIf
							EndIf
							e\SoundCHN = LoopSound2(e\Sound, e\SoundCHN, mainPlayer\cam, e\room\Objects[i], 6.0)	
						EndIf
					Next
					
					pvt=CreatePivot()
					PositionEntity pvt, EntityX(e\room\Objects[8],True)-1536*RoomScale,500*RoomScale,EntityZ(e\room\Objects[8],True)+608*RoomScale
					If EntityDistance(pvt, mainPlayer\collider)<5.0 Then 
						e\SoundCHN2 = LoopSound2(e\Sound2, e\SoundCHN2, mainPlayer\cam, pvt, 3.0)
					EndIf
					FreeEntity pvt
					
					;106's eyes
					ShowEntity e\room\Objects[17]
					PositionEntity e\room\Objects[17], EntityX(e\room\Objects[8],True),1376*RoomScale,EntityZ(e\room\Objects[8],True)-2848*RoomScale
					PointEntity e\room\Objects[17], Collider
					TurnEntity e\room\Objects[17], 0, 180, 0
					
					temp = EntityDistance(mainPlayer\collider, e\room\Objects[17])
					If temp < 2000*RoomScale Then
						mainPlayer\injuries = mainPlayer\injuries + (FPSfactor/4000)
						
						If mainPlayer\injuries > 1.0 Then
							If mainPlayer\injuries - (FPSfactor/4000)=< 1.0 Then
								PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Kneel.ogg")
							EndIf
						EndIf
						
						mainPlayer\sanity895 = Max(mainPlayer\sanity895 - FPSfactor / temp / 8,-1000)
						
						e\SoundCHN = LoopSound2(OldManSFX(4), e\SoundCHN, mainPlayer\cam, e\room\Objects[17], 5.0, 0.6)
						
						mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(MilliSecs2()) / 20.0)+1.0)*15.0*Max((6.0-temp)/6.0,0.0))
						
						pvt% = CreatePivot()
						PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
						PointEntity(pvt, e\room\Objects[17])
						TurnEntity(pvt, 90, 0, 0)
						mainPlayer\headPitch = CurveAngle(EntityPitch(pvt), mainPlayer\headPitch + 90.0, Min(Max(15000.0 / (-mainPlayer\sanity895), 15.0), 500.0))
						mainPlayer\headPitch=user_camera_pitch-90
						RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), Min(Max(15000.0 / (-mainPlayer\sanity895), 15.0), 500.0)), 0)
						FreeEntity pvt
						
						;teleport the player to the trenches
						If Crouch Then
							mainPlayer\blinkTimer = -10
							PositionEntity mainPlayer\collider, EntityX(e\room\Objects[8],True)-1344*RoomScale,2944*RoomScale,EntityZ(e\room\Objects[8],True)-1184*RoomScale
							ResetEntity Collider
							Crouch = False
							
							LoadEventSound(e,"SFX\Room\PocketDimension\Explosion.ogg")
							LoadEventSound(e,"SFX\Room\PocketDimension\TrenchPlane.ogg",1)
							PositionEntity e\room\Objects[20], EntityX(e\room\Objects[8],True)-1000,0,0,True
							
						EndIf
					ElseIf EntityY(mainPlayer\collider)<-180*RoomScale ;the "exit room"
						temp = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[8],True)+1024*RoomScale,EntityZ(e\room\Objects[8],True))
						If temp<640*RoomScale
							mainPlayer\blurTimer = (640*RoomScale-temp)*3000
							
							e\SoundCHN2 = LoopSound2(DecaySFX(Rand(1, 3)), e\SoundCHN2, mainPlayer\cam, mainPlayer\collider, 2.0, (640*RoomScale-temp)*Abs(mainPlayer\moveSpeed)*100)
							mainPlayer\moveSpeed = CurveValue(0.0, mainPlayer\moveSpeed, temp*10)
							
							If temp < 130*RoomScale Then
								
								For r.Rooms = Each Rooms
									If r\RoomTemplate\Name = "room2_3" Then
										GiveAchievement(AchvPD)
										e\EventState = 0
										e\EventState2 = 0
										FreeSound_Strict Music(3) : Music(3)=0
										
										mainPlayer\blinkTimer = -10
										LightBlink = 5
										
										PlaySound_Strict(LoadTempSound("SFX\Room\PocketDimension\Exit.ogg"))
										
										de.Decals = CreateDecal(0, EntityX(r\obj), 381*RoomScale, EntityZ(r\obj), 270, Rand(360), 0)
										
										PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.4, EntityZ(r\obj))
										ResetEntity Collider
										Curr106\Idle = False
										Exit
									EndIf
								Next
							EndIf
						EndIf
					EndIf
				EndIf	
			EndIf
			
			
			
			If EntityY(mainPlayer\collider) < -1600*RoomScale Then
				If EntityDistance(mainPlayer\collider, e\room\Objects[8]) > 4750*RoomScale Then
					CameraFogColor mainPlayer\cam, 0,0,0
					CameraClsColor mainPlayer\cam, 0,0,0
					
					mainPlayer\dropSpeed = 0
					mainPlayer\blurTimer = 500
					mainPlayer\blurTimer = 1500
					PositionEntity(mainPlayer\collider, EntityX(e\room\obj,True), 0.4, EntityX(e\room\obj,True))
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name = "room106" Then
							e\EventState = 0
							e\EventState2 = 0
							FreeSound_Strict Music(3) : Music(3)=0
							PositionEntity(mainPlayer\collider, EntityX(r\obj,True), 0.4, EntityX(r\obj,True))
							
							GiveAchievement(AchvPD)
							
							Curr106\State = 10000
							Curr106\Idle = False
							Exit
						EndIf
					Next
					ResetEntity Collider
					
					e\EventState2 = 0
					UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
				Else ;the player is not at the exit, must've fallen down
					
					If KillTimer => 0 Then 
						PlaySound_Strict HorrorSFX(8)
						DeathMSG = "In addition to the decomposed appearance typical of the victims of SCP-106, the subject seems to have suffered multiple heavy fractures to both of his legs."
						
					EndIf
					KillTimer = Min(-1, KillTimer)	
					mainPlayer\blurTimer = 3000
				EndIf
			EndIf
			
			UpdateDoorsTimer = 0
			UpdateDoors()
			UpdateRooms()
			
		ElseIf e\EventState2 = 0
			dist# = EntityDistance(mainPlayer\collider, e\room\obj)	
			
			If dist > 1700*RoomScale Then
				mainPlayer\blinkTimer = -10
				
				Select Rand(25)
					Case 1,2,3,4
						PlaySound_Strict(OldManSFX(3))
						
						pvt = CreatePivot()
						PositionEntity(pvt, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
						
						PointEntity(pvt, e\room\obj)
						MoveEntity pvt, 0,0,dist*1.9
						PositionEntity(mainPlayer\collider, EntityX(pvt), EntityY(mainPlayer\collider), EntityZ(pvt))
						ResetEntity Collider
						
						MoveEntity pvt, 0,0,0.8
						PositionEntity(e\room\Objects[10], EntityX(pvt), 0.0, EntityZ(pvt))
						RotateEntity e\room\Objects[10], 0, EntityYaw(pvt), 0, True	
						
						FreeEntity pvt
					Case 5,6,7,8,9,10 
						e\EventState2=1
						mainPlayer\blinkTimer = -10
						PlaySound_Strict(OldManSFX(3))
						
						PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[8],True), 0.5, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
					Case 11,12 ;middle of the large starting room
						mainPlayer\blurTimer = 500
						PositionEntity mainPlayer\collider,EntityX(e\room\obj), 0.5, EntityZ(e\room\obj)
					Case 13,14,15 ;"exit room"
						mainPlayer\blurTimer = 1500
						e\EventState2=1
						mainPlayer\blinkTimer = -10
						
						PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[8],True)-400*RoomScale, -304*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
					Case 16,17,18,19
						mainPlayer\blurTimer = 1500
						For r.Rooms = Each Rooms
							If r\RoomTemplate\Name = "tunnel" Then
								GiveAchievement(AchvPD)
								e\EventState = 0
								e\EventState2 = 0
								FreeSound_Strict Music(3) : Music(3)=0
								PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.4, EntityZ(r\obj))
								ResetEntity Collider
								Curr106\Idle = False
								Exit
							EndIf
						Next
					Case 20,21,22 ;the tower room
						mainPlayer\blinkTimer = -10
						PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[12],True), 0.6, EntityZ(e\room\Objects[12],True))
						ResetEntity Collider
						e\EventState2 = 15
					Case 23,24,25
						mainPlayer\blurTimer = 1500
						e\EventState2=1
						e\EventState3=1
						mainPlayer\blinkTimer = -10
						
						PlaySound_Strict(OldManSFX(3))
						
						PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[8],True), 2288*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
				End Select 
				
				UpdateDoorsTimer = 0
				UpdateDoors()
				UpdateRooms()
			EndIf					
		Else ;pillar room
			CameraFogColor mainPlayer\cam, 38*0.5, 55*0.5, 47*0.5
			CameraClsColor mainPlayer\cam, 38*0.5, 55*0.5, 47*0.5
			
			If Rand(800)=1 Then 
				angle = EntityYaw(mainPlayer\cam,True)+Rnd(150,210)
				p.Particles = CreateParticle(EntityX(mainPlayer\collider)+Cos(angle)*7.5, 0.0, EntityZ(mainPlayer\collider)+Sin(angle)*7.5, 3, 4.0, 0.0, 2500)
				EntityBlend(p\obj, 2)
				;EntityFX(p\obj, 1)
				p\speed = 0.01
				p\SizeChange = 0
				PointEntity(p\pvt, mainPlayer\cam)
				TurnEntity(p\pvt, 0, 145, 0, True)
				TurnEntity(p\pvt, Rand(10,20), 0, 0, True)
			EndIf
			
			If e\EventState2 > 12 Then 
				Curr106\Idle = True
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[e\EventState2],True),0.27, EntityZ(e\room\Objects[e\EventState2],True))
				
				PointEntity(Curr106\Collider, mainPlayer\cam)
				TurnEntity(Curr106\Collider, 0, Sin(MilliSecs2() / 20) * 6.0, 0, True)
				MoveEntity(Curr106\Collider, 0, 0, Sin(MilliSecs2() / 15) * 0.06)
				
				If Rand(750)=1 And e\EventState2 > 12 Then
					mainPlayer\blinkTimer = -10
					e\EventState2 = e\EventState2-1
					PlaySound_Strict HorrorSFX(8)
				EndIf
				
				If e\EventState2 = 12 Then
					mainPlayer\camShake = 1.0
					PositionEntity(Curr106\Collider, EntityX(e\room\Objects[e\EventState2],True),-1.0, EntityZ(e\room\Objects[e\EventState2],True))
					Curr106\State = -11
					ResetEntity Curr106\Collider
				EndIf
				
			Else 
				Curr106\State = -11
				Curr106\Idle = False
			EndIf
			
			If EntityY(mainPlayer\collider) < -1600*RoomScale Then
				;player is at the exit
				If Distance(EntityX(e\room\Objects[16],True),EntityZ(e\room\Objects[16],True),EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider))<144*RoomScale Then
					
					CameraFogColor mainPlayer\cam, 0,0,0
					CameraClsColor mainPlayer\cam, 0,0,0
					
					mainPlayer\dropSpeed = 0
					mainPlayer\blurTimer = 500
					PositionEntity(mainPlayer\collider, EntityX(e\room\obj), 0.5, EntityZ(e\room\obj))
					ResetEntity Collider
					e\EventState2 = 0
					UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
				Else ;somewhere else -> must've fallen down
					If KillTimer => 0 Then PlaySound_Strict HorrorSFX(8)
					KillTimer = Min(-1, KillTimer)	
					mainPlayer\blurTimer = 3000
				EndIf
			EndIf 
			
		EndIf
		
	Else
		HideEntity e\room\obj
		
		CameraClsColor mainPlayer\cam, 0,0,0
		e\EventState = 0
		e\EventState2 = 0
		e\EventState3 = 0
	EndIf
	;[End Block]
End Function


Function FillRoom_extend_gatea_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\roomDoors[2] = CreateDoor(r\zone, r\x - 4064.0 * RoomScale, (-1280.0+12000.0)*RoomScale, r\z + 3952.0 * RoomScale, 0, r, False)
    r\roomDoors[2]\autoClose = False : r\roomDoors[2]\open = False
    
    d2 = CreateDoor(r\zone, r\x, 12000.0*RoomScale, r\z - 1024.0 * RoomScale, 0, r, False)
    d2\autoClose = False : d2\open = False : d2\locked = True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 2328.0 * RoomScale, 0, r, False, False, 2)
    ;TODO: fix
	;If SelectedEnding = "A2" Then 
    ;    d2\autoClose = False : d2\open = True : d2\locked = True	
    ;Else
    ;    d2\autoClose = False : d2\open = False : d2\locked = False	
    ;EndIf	
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 2288.0*RoomScale, True
    PositionEntity d2\buttons[1], r\x-1584*RoomScale, EntityY(d2\buttons[0],True), r\z + 2488.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 4352.0 * RoomScale, 0, r, False, False, 2)
    ;TODO: fix
	;If SelectedEnding = "A2" Then 
    ;    d2\autoClose = False : d2\open = True : d2\locked = True	
    ;Else
    ;    d2\autoClose = False : d2\open = False : d2\locked = False
    ;EndIf
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4384.0*RoomScale, True
    RotateEntity d2\buttons[0], 0, 180, 0, True	
    PositionEntity d2\buttons[1], r\x-1584.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4232.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True	
    
    For r2.Rooms = Each Rooms
        If r2\roomTemplate\name = "exit1" Then
            r\objects[1]=r2\objects[1]
            r\objects[2]=r2\objects[2]	
        ElseIf r2\roomTemplate\name = "gateaentrance" Then
            ;ylempi hissi
            r\roomDoors[1] = CreateDoor(0, r\x+1544.0*RoomScale,12000.0*RoomScale, r\z-64.0*RoomScale, 90, r, False)
            r\roomDoors[1]\autoClose = False : r\roomDoors[1]\open = False
            PositionEntity(r\roomDoors[1]\buttons[0],r\x+1584*RoomScale, EntityY(r\roomDoors[1]\buttons[0],True), r\z+80*RoomScale, True)
            PositionEntity(r\roomDoors[1]\buttons[1],r\x+1456*RoomScale, EntityY(r\roomDoors[1]\buttons[1],True), r\z-208*RoomScale, True)	
            r2\objects[1] = CreatePivot()
            PositionEntity(r2\objects[1], r\x+1848.0*RoomScale, 240.0*RoomScale, r\z-64.0*RoomScale, True)
            EntityParent r2\objects[1], r\obj						
        EndIf
    Next
    
    ;106:n spawnpoint
    r\objects[3]=CreatePivot()
    PositionEntity(r\objects[3], r\x+1216.0*RoomScale, 0, r\z+2112.0*RoomScale, True)
    EntityParent r\objects[3], r\obj
    
    ;sillan loppup��
    r\objects[4]=CreatePivot()
    PositionEntity(r\objects[4], r\x, 96.0*RoomScale, r\z+6400.0*RoomScale, True)
    EntityParent r\objects[4], r\obj		
    
    ;vartiotorni 1
    r\objects[5]=CreatePivot()
    PositionEntity(r\objects[5], r\x+1784.0*RoomScale, 2124.0*RoomScale, r\z+4512.0*RoomScale, True)
    EntityParent r\objects[5], r\obj	
    
    ;vartiotorni 2
    r\objects[6]=CreatePivot()
    PositionEntity(r\objects[6], r\x-5048.0*RoomScale, 1912.0*RoomScale, r\z+4656.0*RoomScale, True)
    EntityParent r\objects[6], r\obj	
    
    ;sillan takareuna
    r\objects[7]=CreatePivot()
    PositionEntity(r\objects[7], r\x+1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\objects[7], r\obj	
    
    ;sillan takareuna2
    r\objects[8]=CreatePivot()
    PositionEntity(r\objects[8], r\x-1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\objects[8], r\obj	
    
    ;"valopyssy"
    r\objects[9]=CreatePivot()
    PositionEntity(r\objects[9], r\x+2624.0*RoomScale, 992.0*RoomScale, r\z+6157.0*RoomScale, True)
    EntityParent r\objects[9], r\obj	
    ;objects[10] = valopyssyn yl�osa
    
    ;tunnelin loppu
    r\objects[11]=CreatePivot()
    PositionEntity(r\objects[11], r\x-4064.0*RoomScale, -1248.0*RoomScale, r\z-1696.0*RoomScale, True)
    EntityParent r\objects[11], r\obj
    
    r\objects[13]=LoadMesh("GFX/map/gateawall1.b3d",r\obj)
    PositionEntity(r\objects[13], r\x-4308.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)
    EntityColor r\objects[13], 25,25,25
    ;EntityFX(r\objects[13],1)
    
    r\objects[14]=LoadMesh("GFX/map/gateawall2.b3d",r\obj)
    PositionEntity(r\objects[14], r\x-3820.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)	
    EntityColor r\objects[14], 25,25,25
    ;EntityFX(r\objects[14],1)
    
    r\objects[15]=CreatePivot(r\obj)
    PositionEntity(r\objects[15], r\x-3568.0*RoomScale, -1089.0*RoomScale, r\z+4944.0*RoomScale, True)
End Function

Function UpdateEvent_extend_gatea_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then 
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		
		If e\eventState = 0 Then
			DrawLoading(0)
			e\room\objects[0] = LoadMesh("GFX/MAP/gateatunnel.b3d")
			PositionEntity e\room\objects[0], EntityX(e\room\obj,True),EntityY(e\room\obj,True),EntityZ(e\room\obj,True)
			ScaleEntity (e\room\objects[0],RoomScale,RoomScale,RoomScale)
			EntityType e\room\objects[0], HIT_MAP
			EntityPickMode e\room\objects[0], 3
			EntityParent(e\room\objects[0],e\room\obj)
			
			DrawLoading(30)
			
			For i = 0 To e\room\maxLights
				If e\room\lightSprites[i]<>0 Then 
					EntityFX e\room\lightSprites[i], 1+8
				EndIf
			Next

			CameraFogMode(mainPlayer\cam, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			For i = 2 To 4
				e\room\npc[i] = CreateNPC(NPCtypeApache, e\room\x, 100.0, e\room\z)
				e\room\npc[i]\state = (Not Contained106)
			Next
			
			;Music(5) = LoadSound("SFX/Music/GateA.ogg") ;TODO: fix
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(mainPlayer\collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX/map/sky/sky")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60)
			
			For n.NPCs = Each NPCs
				If n\npcType = NPCtypeMTF Then Delete n
			Next
			
			For i = 0 To 1
				e\room\npc[i] = CreateNPC(NPCtypeGuard, EntityX(e\room\objects[i+5],True),EntityY(e\room\objects[i+5],True),EntityZ(e\room\objects[i+5],True))
				e\room\npc[i]\state = 0
				PointEntity e\room\npc[i]\collider, e\room\objects[3]
			Next
			
			For i = 7 To 8
				e\room\npc[i] = CreateNPC(NPCtypeMTF, EntityX(e\room\objects[i],True)+0.8,EntityY(e\room\objects[i],True),EntityZ(e\room\objects[i],True)+0.8)
				e\room\npc[i]\state = 5
				e\room\npc[i]\prevState = 1
				PointEntity e\room\npc[i]\collider, e\room\objects[3]
			Next	
			
			For i = 5 To 6
				e\room\npc[i] = CreateNPC(NPCtypeMTF, EntityX(e\room\objects[i+2],True),EntityY(e\room\objects[i+2],True),EntityZ(e\room\objects[i+2],True))
				e\room\npc[i]\state = 5
				e\room\npc[i]\prevState = 1
				PointEntity e\room\npc[i]\collider, e\room\objects[3]
			Next		
			
			If Contained106 Then
				e\room\roomDoors[2]\locked = True
				
				PositionEntity e\room\npc[5]\collider, EntityX(e\room\objects[15],True)+(i-6)*0.2,EntityY(e\room\objects[15],True),EntityZ(e\room\objects[15],True)+(i-6)*0.2, True
				ResetEntity e\room\npc[5]\collider
				
			EndIf
			
			xtemp#=EntityX(e\room\objects[9],True)
			ztemp#=EntityZ(e\room\objects[9],True)
			FreeEntity e\room\objects[9]
			
			e\room\objects[9] = LoadMesh("GFX/map/lightgunbase.b3d")
			ScaleEntity e\room\objects[9], RoomScale,RoomScale,RoomScale
			EntityFX(e\room\objects[9],0)
			PositionEntity(e\room\objects[9], xtemp, (992.0+12000.0)*RoomScale, ztemp)
			e\room\objects[10] = LoadMesh("GFX/map/lightgun.b3d")
			EntityFX(e\room\objects[10],0)
			ScaleEntity e\room\objects[10], RoomScale,RoomScale,RoomScale
			PositionEntity(e\room\objects[10], xtemp, (992.0+12000.0+288.0)*RoomScale, ztemp-176.0*RoomScale,True)
			EntityParent e\room\objects[10],e\room\objects[9]
			RotateEntity e\room\objects[9], 0, 48, 0
			RotateEntity e\room\objects[10], 40, 0, 0
			
			For temp = 0 To 20
				For i = 0 To 1
					TranslateEntity e\room\npc[i]\collider, 0, -0.04, 0
				Next							
				For i = 5 To 8
					TranslateEntity e\room\npc[i]\collider, 0, -0.04, 0
				Next
			Next
			
			ResetEntity mainPlayer\collider
			e\eventState = 1.0
			
			If (Not Contained106) Then PlaySound2 LoadTempSound("SFX/Ending/GateA/106Escape.ogg") 
			
			DrawLoading(100)
		Else
			
			ShouldPlay = 5
			
			e\eventState = e\eventState+timing\tickDuration
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
			CameraFogRange mainPlayer\cam, 5,30
			
			angle = Max(Sin(EntityYaw(mainPlayer\collider)+90),0.0)
			;240,220,200
			CameraFogColor (mainPlayer\cam,200+(angle*40),200+(angle*20),200)
			CameraClsColor (mainPlayer\cam,200+(angle*40),200+(angle*20),200)		
			CameraRange(mainPlayer\cam, 0.05, 30)
			
			AmbientLight (140, 140, 140)
			
			For i = 2 To 4
				If e\room\npc[i]<>Null Then 
					If e\room\npc[i]\state < 2 Then 
						PositionEntity(e\room\npc[i]\collider, EntityX(e\room\objects[3],True)+Cos(e\eventState/10+(120*i))*6000.0*RoomScale,15000*RoomScale,EntityZ(e\room\objects[3],True)+Sin(e\eventState/10+(120*i))*6000.0*RoomScale)
						RotateEntity e\room\npc[i]\collider,7.0,(e\eventState/10+(120*i)),20.0
					EndIf
				EndIf
			Next
			
			UpdateSky()
			
			If e\eventState=>350 Then
				If Contained106=False Then
					If e\eventState-timing\tickDuration < 350 Then
						Curr106\state = -0.1
						;Curr106\idle = True
						SetNPCFrame(Curr106, 110.0)
						PositionEntity (Curr106\collider, EntityX(e\room\objects[3],True),EntityY(mainPlayer\collider)-50.0,EntityZ(e\room\objects[3],True),True)
						PositionEntity (Curr106\obj, EntityX(e\room\objects[3],True),EntityY(mainPlayer\collider)-50.0,EntityZ(e\room\objects[3],True),True)
						de.Decals = CreateDecal(0, EntityX(e\room\objects[3],True),EntityY(e\room\objects[3],True)+0.01,EntityZ(e\room\objects[3],True), 90, Rand(360), 0)
						de\size = 0.05 : de\sizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 
						;TODO: fix audio
						;PlaySound2 (HorrorSFX(5))
						;PlaySound2 DecaySFX(0)
					ElseIf Curr106\state < 0 Then
						HideEntity Curr106\obj2
						Curr106\pathTimer = 70*100
						
						If Curr106\state3 = 0 Then
							If Curr106\pathStatus <> 1 Then
								PositionEntity Curr106\collider,EntityX(e\room\objects[3],True),EntityY(Curr106\collider),EntityZ(e\room\objects[3],True),True
								If Curr106\state =< -10 Then
									dist# = EntityY(Curr106\collider)
									PositionEntity Curr106\collider,EntityX(Curr106\collider),EntityY(e\room\objects[3],True),EntityZ(Curr106\collider),True
									;Curr106\pathStatus = FindPath(Curr106, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True),EntityZ(e\room\objects[4],True))
									Curr106\pathStatus = FindPath(Curr106,EntityX(e\room\npc[5]\collider,True),EntityY(e\room\npc[5]\collider,True),EntityZ(e\room\npc[5]\collider,True))
									Curr106\pathTimer = 70*200
									PositionEntity Curr106\collider,EntityX(Curr106\collider),dist,EntityZ(Curr106\collider),True
									ResetEntity Curr106\collider
									Curr106\pathLocation = 1
									;Curr106\idle = False
								;Else	
									;PositionEntity (Curr106\collider, EntityX(e\room\objects[3],True),EntityY(e\room\objects[3],True),EntityZ(e\room\objects[3],True),True)
									;Curr106\idle = True
									;Animate2(Curr106\obj, AnimTime(Curr106\obj), 110, 259, 0.15, False)
									;If AnimTime(Curr106\obj)=>259 Then Curr106\idle = False													
									
								EndIf
							Else
								Curr106\pathTimer = 70*200
								For i = 2 To 4 ;helicopters start attacking 106
									e\room\npc[i]\state = 3 
									e\room\npc[i]\enemyX = EntityX(Curr106\obj,True)
									e\room\npc[i]\enemyY = EntityY(Curr106\obj,True)+5.0
									e\room\npc[i]\enemyZ = EntityZ(Curr106\obj,True)
								Next
								
								For i = 5 To 8
									e\room\npc[i]\state = 5
									e\room\npc[i]\enemyX = EntityX(Curr106\obj,True)
									e\room\npc[i]\enemyY = EntityY(Curr106\obj,True)+0.4
									e\room\npc[i]\enemyZ = EntityZ(Curr106\obj,True)											
								Next
								
								pvt=CreatePivot()
								PositionEntity pvt, EntityX(e\room\objects[10],True),EntityY(e\room\objects[10],True),EntityZ(e\room\objects[10],True)
								PointEntity pvt, Curr106\collider
								RotateEntity(e\room\objects[9],0,CurveAngle(EntityYaw(pvt),EntityYaw(e\room\objects[9],True),150.0),0,True)
								RotateEntity(e\room\objects[10],CurveAngle(EntityPitch(pvt),EntityPitch(e\room\objects[10],True),200.0),EntityYaw(e\room\objects[9],True),0, True)
								
								FreeEntity pvt
								
								If timing\tickDuration > 0 Then ;decals under 106
									If ((e\eventState-timing\tickDuration) Mod 100.0)=<50.0 And (e\eventState Mod 100.0)>50.0 Then
										de.Decals = CreateDecal(0, EntityX(Curr106\collider,True),EntityY(e\room\objects[3],True)+0.01,EntityZ(Curr106\collider,True), 90, Rand(360), 0)
										de\size = 0.2 : de\sizeChange = 0.004 : de\timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
							EndIf
						EndIf
						
						dist# = Distance(EntityX(Curr106\collider),EntityZ(Curr106\collider),EntityX(e\room\objects[4],True),EntityZ(e\room\objects[4],True))
						
						Curr106\currSpeed = CurveValue(0, Curr106\currSpeed, Max(5*dist,2.0))
						If dist < 15.0 Then
							If e\soundChannels[1] = 0 Then
								e\soundChannels[1] = PlaySound(LoadTempSound("SFX/Ending/GateA/Franklin.ogg"))
							EndIf
							
							If dist<0.4 Then
								Curr106\pathStatus = 0
								Curr106\pathTimer = 70*200
								If Curr106\state3=0 Then 
									SetNPCFrame(Curr106, 259.0)
									If e\sounds[0] <> 0 Then FreeSound e\sounds[0] : e\sounds[0] = 0
									LoadEventSound(e,"SFX/Ending/GateA/106Retreat.ogg")
									e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, Curr106\collider, 35.0)
								EndIf
								
								If timing\tickDuration > 0 Then ;106:n alle ilmestyy decaleita
									If ((e\eventState-timing\tickDuration) Mod 160.0)=<50.0 And (e\eventState Mod 160.0)>50.0 Then
										de.Decals = CreateDecal(0, EntityX(Curr106\collider,True),EntityY(e\room\objects[3],True)+0.01,EntityZ(Curr106\collider,True), 90, Rand(360), 0)
										de\size = 0.05 : de\sizeChange = 0.004 : de\timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
								
								AnimateNPC(Curr106, 259, 110, -0.1, False)
								
								Curr106\state3 = Curr106\state3+timing\tickDuration
								PositionEntity(Curr106\collider, EntityX(Curr106\collider,True),CurveValue(EntityY(e\room\objects[3],True)-(Curr106\state3/4500.0),EntityY(Curr106\collider,True),100.0),EntityZ(Curr106\collider,True))
								If Curr106\state3>700.0 Then
									Curr106\state = 100000
									e\eventState2 = 0
									For i = 5 To 8
										e\room\npc[i]\state = 1
									Next
									For i = 2 To 4 ;helicopters attack the player
										e\room\npc[i]\state = 2
									Next
									HideEntity Curr106\obj
								EndIf
							Else
								If dist < 8.5 Then 
									If e\eventState2=0 Then ;IsChannelPlaying(e\soundChannels[1]) = 0 Then
										e\soundChannels[1] = PlaySound(LoadTempSound("SFX/Ending/GateA/HIDTurret.ogg"))
										e\eventState2 = 1
									ElseIf e\eventState2>0 Then
										e\eventState2=e\eventState2+timing\tickDuration
										If e\eventState2=> 7.5*70 Then
											If e\eventState2-timing\tickDuration < 7.5*70 Then
												p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+0.4, EntityZ(Curr106\obj,True), 4, 7.0, 0, (6.7*70))
												p\speed = 0.0
												p\a = 1.0
												EntityParent p\pvt, Curr106\collider, True
												
												p.Particles = CreateParticle(EntityX(e\room\objects[10],True),EntityY(e\room\objects[10],True),EntityZ(e\room\objects[10],True), 4, 2.0, 0, (6.7*70))
												RotateEntity p\pvt, EntityPitch(e\room\objects[10],True),EntityYaw(e\room\objects[10],True),0,True
												MoveEntity p\pvt, 0, 92.0*RoomScale, 512.0*RoomScale
												p\speed = 0.0
												p\a = 1.0
												EntityParent p\pvt, e\room\objects[10], True
											ElseIf e\eventState2 < 14.3*70 Then
												mainPlayer\camShake = 0.5
												mainPlayer\lightFlash = 0.3+EntityInView(e\room\objects[10],mainPlayer\cam)*0.5
											EndIf
										EndIf
									EndIf
									
									For i = 0 To Rand(2,8)-Int(dist)
										p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+Rnd(0.4,0.9), EntityZ(Curr106\obj), 0, 0.006, -0.002, 40)
										p\speed = 0.005
										p\a = 0.8
										p\aChange = -0.01
										RotateEntity p\pvt, -Rnd(70,110), Rnd(360),0	
									Next										
								EndIf
								
								
							EndIf
						EndIf
					EndIf
					
					If e\eventState3 = 0.0 Then 
						If Abs(EntityY(mainPlayer\collider)-EntityY(e\room\objects[11],True))<1.0 Then
							If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\objects[11],True),EntityZ(e\room\objects[11],True)) < 12.0 Then
								Curr106\state = 100000
								HideEntity Curr106\obj
								
								;MTF spawns at the tunnel entrance
								For i = 5 To 8
									e\room\npc[i]\state = 3
									PositionEntity e\room\npc[i]\collider, EntityX(e\room\objects[15],True)+(i-6)*0.3,EntityY(e\room\objects[15],True),EntityZ(e\room\objects[15],True)+(i-6)*0.3, True
									ResetEntity e\room\npc[i]\collider
									
									e\room\npc[i]\pathStatus = FindPath(e\room\npc[i], EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
									e\room\npc[i]\pathTimer = 70*2
									e\room\npc[i]\lastSeen = 70*100
								Next
								e\room\npc[5]\sounds[0] = LoadSound("SFX/Character/MTF/ThereHeIs1.ogg")
								PlayRangedSound(e\room\npc[5]\sounds[0], mainPlayer\cam, e\room\npc[5]\collider, 25.0)
								
								e\room\roomDoors[2]\open = True
								
								For i = 2 To 4
									Delete e\room\npc[i]
									e\room\npc[i]=Null
								Next
								
								e\eventState3 = 1.0
							EndIf
						EndIf
					ElseIf e\eventState3 = 1.0 Then
						
						For i = 5 To 8
							If EntityDistance(e\room\npc[i]\collider,mainPlayer\collider)> 4.0 Then e\room\npc[i]\state = 3
						Next
						
						If Abs(EntityY(mainPlayer\collider)-EntityY(e\room\objects[11],True))<1.0 Then
							If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\objects[11],True),EntityZ(e\room\objects[11],True)) < 7.0 Then
								e\room\objects[12] = LoadMesh("GFX/npcs/CI/CI.b3d")
								EntityColor e\room\objects[12], 0,0,0
								ScaleMesh (e\room\objects[12], 0.32/21.3, 0.32/21.3, 0.32/21.3)
								PositionEntity e\room\objects[12], EntityX(e\room\objects[11],True), EntityY(e\room\objects[11],True), EntityZ(e\room\objects[11],True)
								
								obj = CopyEntity(e\room\objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-3968*RoomScale, EntityY(e\room\objects[11],True), EntityZ(e\room\obj,True)-1920*RoomScale
								
								obj = CopyEntity(e\room\objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-4160*RoomScale, EntityY(e\room\objects[11],True), EntityZ(e\room\obj,True)-1920*RoomScale
								
								obj = CopyEntity(e\room\objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-4064*RoomScale, EntityY(e\room\objects[11],True), EntityZ(e\room\obj,True)-2112*RoomScale
								
								e\soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/Ending/GateA/Bell1.ogg"), mainPlayer\cam, e\room\objects[12])
								
								p.Particles = CreateParticle(EntityX(e\room\objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.15
								p\a = 0.5
								p.Particles = CreateParticle(EntityX(e\room\objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.25
								p\a = 0.5
								PointEntity p\pvt, mainPlayer\collider
								
								mainPlayer\camShake = 1.0
								mainPlayer\lightFlash = 1.0
								
								e\eventState3 = 2.0
							EndIf
						EndIf
					Else
						e\eventState3=e\eventState3+timing\tickDuration
						PointEntity e\room\objects[12], mainPlayer\collider
						RotateEntity e\room\objects[12], 0, EntityYaw(e\room\objects[12]), 0
						
						mainPlayer\stamina = -5.0
						
						mainPlayer\blurTimer = Sin(e\eventState3*0.7)*1000.0
						
						If Not mainPlayer\dead Then 
							CameraZoom(mainPlayer\cam, 1.0+Sin(e\eventState3*0.8)*0.2)
							
							dist = EntityDistance(mainPlayer\collider,e\room\objects[11])
							If dist < 6.5 Then
								PositionEntity(mainPlayer\collider, CurveValue(EntityX(e\room\objects[11],True),EntityX(mainPlayer\collider),dist*80),EntityY(mainPlayer\collider),CurveValue(EntityZ(e\room\objects[0],True),EntityZ(mainPlayer\collider),dist*80))
							EndIf
						EndIf
						
						;tunneli menee umpeen
						If e\eventState3>50 And e\eventState3<230 Then
							mainPlayer\camShake = Sin(e\eventState3-50)*3
							TurnEntity e\room\objects[13], 0, Sin(e\eventState3-50)*-0.85, 0, True
							TurnEntity e\room\objects[14], 0, Sin(e\eventState3-50)*0.85, 0, True
							
							For i = 5 To 8
								PositionEntity (e\room\npc[i]\collider, CurveValue(EntityX(e\room\roomDoors[2]\frameobj,True), EntityX(e\room\npc[i]\collider,True),50.0),EntityY(e\room\npc[i]\collider,True),CurveValue(EntityZ(e\room\roomDoors[2]\frameobj,True), EntityZ(e\room\npc[i]\collider,True),50.0),True)
								ResetEntity e\room\npc[i]\collider
							Next
						EndIf
						
						If e\eventState3=>230.0 Then
							If e\eventState3-timing\tickDuration<230.0 Then
								e\soundChannels[0] = PlaySound(LoadTempSound("SFX/Ending/GateA/CI.ogg"))
							EndIf
							
							If IsChannelPlaying(e\soundChannels[0])=False Then; And SelectedEnding="" Then
								PlaySound2 LoadTempSound("SFX/Ending/GateA/Bell2.ogg")
								
								p.Particles = CreateParticle(EntityX(e\room\objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.15
								p\a = 0.5
								p.Particles = CreateParticle(EntityX(e\room\objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.25
								p\a = 0.5
								
								;SelectedEnding = "A1"
								mainPlayer\godMode = 0
								mainPlayer\noclip = 0
								DeathMSG = ""
								Kill(mainPlayer)
							EndIf
							
							;If SelectedEnding <> "" Then
							mainPlayer\camShake=CurveValue(2.0,mainPlayer\camShake,10.0)
							mainPlayer\lightFlash = CurveValue(2.0,mainPlayer\lightFlash,8.0)
							;EndIf
							
						EndIf
					EndIf
					
				Else ;contained106 = true
					
					If e\eventState2 = 0 Then
						;PositionEntity (e\room\npc[5]\collider, EntityX(e\room\obj,True)-3408*RoomScale, EntityY(e\room\obj,True)-796*RoomScale, EntityZ(e\room\obj,True)+4976, True)
						;ResetEntity e\room\npc[5]\collider
						e\eventState2 = 1
						
						For i = 5 To 8
							e\room\npc[i]\state = 3
							
							e\room\npc[i]\pathStatus = FindPath(e\room\npc[i], EntityX(e\room\obj)-1.0+2.0*(i Mod 2),EntityY(mainPlayer\collider)+0.2,EntityZ(e\room\obj)-2.0*(i Mod 2))
							e\room\npc[i]\pathTimer = 70*Rand(15,20)
							e\room\npc[i]\lastSeen = 70*300
						Next
					Else
						
						For i = 5 To 8
							If e\room\npc[i]\state = 5 Then
								e\room\npc[i]\enemyX = EntityX(mainPlayer\collider)
								e\room\npc[i]\enemyY = EntityY(mainPlayer\collider)
								e\room\npc[i]\enemyZ = EntityZ(mainPlayer\collider)
							Else
								If EntityDistance(e\room\npc[i]\collider,mainPlayer\collider)<6.0 Then
									e\room\npc[i]\state = 5
									e\room\npc[i]\currSpeed = 0
								EndIf
							EndIf
						Next
						
						If e\eventState2=<1 Then
							For i = 5 To 8
								If e\room\npc[i]\state = 5 Then
									For temp = 5 To 8
										e\room\npc[temp]\state = 5
										e\room\npc[temp]\enemyX = EntityX(mainPlayer\collider)
										e\room\npc[temp]\enemyY = EntityY(mainPlayer\collider)
										e\room\npc[temp]\enemyZ = EntityZ(mainPlayer\collider)
										e\room\npc[temp]\pathTimer = 70*Rand(7,10)
										e\room\npc[temp]\reload = 2000
										mainPlayer\disableControls = True
									Next
									
									If e\eventState2=1 Then
										e\soundChannels[0] = PlaySound(LoadTempSound("SFX/Ending/GateA/STOPRIGHTTHERE.ogg"))
										e\eventState2=2			
									EndIf
								Else
									e\room\npc[i]\lastSeen = 70*300
									e\room\npc[i]\reload = 2000
									e\room\npc[i]\state3 = 70*145											
								EndIf
							Next										
						Else
							
							ShouldPlay = 0
							mainPlayer\moveSpeed = 0
							If IsChannelPlaying(e\soundChannels[0])=False Then
								PlaySound2 IntroSFX(9)
								;SelectedEnding = "A2"
								mainPlayer\godMode = 0
								mainPlayer\noclip = 0
								;KillTimer = -0.1
								DeathMSG = ""
								Kill(mainPlayer)
								mainPlayer\blinkTimer = -10
								RemoveEvent(e)
								Return
							EndIf
						EndIf									
						
					EndIf
					
				EndIf
			EndIf
			
		EndIf
	Else
		HideEntity e\room\obj
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
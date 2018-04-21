Function FillRoom_extend_gatea_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 4064.0 * RoomScale, (-1280.0+12000.0)*RoomScale, r\z + 3952.0 * RoomScale, 0, r, False)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
    
    d2 = CreateDoor(r\zone, r\x, 12000.0*RoomScale, r\z - 1024.0 * RoomScale, 0, r, False)
    d2\AutoClose = False : d2\open = False : d2\locked = True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 2328.0 * RoomScale, 0, r, False, False, 2)
    ;TODO: fix
	;If SelectedEnding = "A2" Then 
    ;    d2\AutoClose = False : d2\open = True : d2\locked = True	
    ;Else
    ;    d2\AutoClose = False : d2\open = False : d2\locked = False	
    ;EndIf	
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 2288.0*RoomScale, True
    PositionEntity d2\buttons[1], r\x-1584*RoomScale, EntityY(d2\buttons[0],True), r\z + 2488.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 4352.0 * RoomScale, 0, r, False, False, 2)
    ;TODO: fix
	;If SelectedEnding = "A2" Then 
    ;    d2\AutoClose = False : d2\open = True : d2\locked = True	
    ;Else
    ;    d2\AutoClose = False : d2\open = False : d2\locked = False
    ;EndIf
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4384.0*RoomScale, True
    RotateEntity d2\buttons[0], 0, 180, 0, True	
    PositionEntity d2\buttons[1], r\x-1584.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4232.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True	
    
    For r2.Rooms = Each Rooms
        If r2\RoomTemplate\Name = "exit1" Then
            r\Objects[1]=r2\Objects[1]
            r\Objects[2]=r2\Objects[2]	
        ElseIf r2\RoomTemplate\Name = "gateaentrance"
            ;ylempi hissi
            r\RoomDoors[1] = CreateDoor(0, r\x+1544.0*RoomScale,12000.0*RoomScale, r\z-64.0*RoomScale, 90, r, False)
            r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
            PositionEntity(r\RoomDoors[1]\buttons[0],r\x+1584*RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z+80*RoomScale, True)
            PositionEntity(r\RoomDoors[1]\buttons[1],r\x+1456*RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z-208*RoomScale, True)	
            r2\Objects[1] = CreatePivot()
            PositionEntity(r2\Objects[1], r\x+1848.0*RoomScale, 240.0*RoomScale, r\z-64.0*RoomScale, True)
            EntityParent r2\Objects[1], r\obj						
        EndIf
    Next
    
    ;106:n spawnpoint
    r\Objects[3]=CreatePivot()
    PositionEntity(r\Objects[3], r\x+1216.0*RoomScale, 0, r\z+2112.0*RoomScale, True)
    EntityParent r\Objects[3], r\obj
    
    ;sillan loppup��
    r\Objects[4]=CreatePivot()
    PositionEntity(r\Objects[4], r\x, 96.0*RoomScale, r\z+6400.0*RoomScale, True)
    EntityParent r\Objects[4], r\obj		
    
    ;vartiotorni 1
    r\Objects[5]=CreatePivot()
    PositionEntity(r\Objects[5], r\x+1784.0*RoomScale, 2124.0*RoomScale, r\z+4512.0*RoomScale, True)
    EntityParent r\Objects[5], r\obj	
    
    ;vartiotorni 2
    r\Objects[6]=CreatePivot()
    PositionEntity(r\Objects[6], r\x-5048.0*RoomScale, 1912.0*RoomScale, r\z+4656.0*RoomScale, True)
    EntityParent r\Objects[6], r\obj	
    
    ;sillan takareuna
    r\Objects[7]=CreatePivot()
    PositionEntity(r\Objects[7], r\x+1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\Objects[7], r\obj	
    
    ;sillan takareuna2
    r\Objects[8]=CreatePivot()
    PositionEntity(r\Objects[8], r\x-1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\Objects[8], r\obj	
    
    ;"valopyssy"
    r\Objects[9]=CreatePivot()
    PositionEntity(r\Objects[9], r\x+2624.0*RoomScale, 992.0*RoomScale, r\z+6157.0*RoomScale, True)
    EntityParent r\Objects[9], r\obj	
    ;objects[10] = valopyssyn yl�osa
    
    ;tunnelin loppu
    r\Objects[11]=CreatePivot()
    PositionEntity(r\Objects[11], r\x-4064.0*RoomScale, -1248.0*RoomScale, r\z-1696.0*RoomScale, True)
    EntityParent r\Objects[11], r\obj
    
    r\Objects[13]=LoadMesh_Strict("GFX\map\gateawall1.b3d",r\obj)
    PositionEntity(r\Objects[13], r\x-4308.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)
    EntityColor r\Objects[13], 25,25,25
    ;EntityFX(r\Objects[13],1)
    
    r\Objects[14]=LoadMesh_Strict("GFX\map\gateawall2.b3d",r\obj)
    PositionEntity(r\Objects[14], r\x-3820.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)	
    EntityColor r\Objects[14], 25,25,25
    ;EntityFX(r\Objects[14],1)
    
    r\Objects[15]=CreatePivot(r\obj)
    PositionEntity(r\Objects[15], r\x-3568.0*RoomScale, -1089.0*RoomScale, r\z+4944.0*RoomScale, True)
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
		
		If e\EventState = 0 Then
			DrawLoading(0)
			e\room\Objects[0] = LoadMesh_Strict("GFX\MAP\gateatunnel.b3d")
			PositionEntity e\room\Objects[0], EntityX(e\room\obj,True),EntityY(e\room\obj,True),EntityZ(e\room\obj,True)
			ScaleEntity (e\room\Objects[0],RoomScale,RoomScale,RoomScale)
			EntityType e\room\Objects[0], HIT_MAP
			EntityPickMode e\room\Objects[0], 3
			EntityParent(e\room\Objects[0],e\room\obj)
			
			DrawLoading(30)
			
			For i = 0 To e\room\MaxLights
				If e\room\LightSprites[i]<>0 Then 
					EntityFX e\room\LightSprites[i], 1+8
				EndIf
			Next

			CameraFogMode(mainPlayer\cam, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			For i = 2 To 4
				e\room\NPC[i] = CreateNPC(NPCtypeApache, e\room\x, 100.0, e\room\z)
				e\room\NPC[i]\State = (Not Contained106)
			Next
			
			Music(5) = LoadSound_Strict("SFX\Music\GateA.ogg")
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(mainPlayer\collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX\map\sky\sky")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60)
			
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtypeMTF Then Delete n
			Next
			
			For i = 0 To 1
				e\room\NPC[i] = CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[i+5],True),EntityY(e\room\Objects[i+5],True),EntityZ(e\room\Objects[i+5],True))
				e\room\NPC[i]\State = 0
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next
			
			For i = 7 To 8
				e\room\NPC[i] = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[i],True)+0.8,EntityY(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)+0.8)
				e\room\NPC[i]\State = 5
				e\room\NPC[i]\PrevState = 1
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next	
			
			For i = 5 To 6
				e\room\NPC[i] = CreateNPC(NPCtypeMTF, EntityX(e\room\Objects[i+2],True),EntityY(e\room\Objects[i+2],True),EntityZ(e\room\Objects[i+2],True))
				e\room\NPC[i]\State = 5
				e\room\NPC[i]\PrevState = 1
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next		
			
			If Contained106 Then
				e\room\RoomDoors[2]\locked = True
				
				PositionEntity e\room\NPC[5]\Collider, EntityX(e\room\Objects[15],True)+(i-6)*0.2,EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True)+(i-6)*0.2, True
				ResetEntity e\room\NPC[5]\Collider
				
			EndIf
			
			xtemp#=EntityX(e\room\Objects[9],True)
			ztemp#=EntityZ(e\room\Objects[9],True)
			FreeEntity e\room\Objects[9]
			
			e\room\Objects[9] = LoadMesh_Strict("GFX\map\lightgunbase.b3d")
			ScaleEntity e\room\Objects[9], RoomScale,RoomScale,RoomScale
			EntityFX(e\room\Objects[9],0)
			PositionEntity(e\room\Objects[9], xtemp, (992.0+12000.0)*RoomScale, ztemp)
			e\room\Objects[10] = LoadMesh_Strict("GFX\map\lightgun.b3d")
			EntityFX(e\room\Objects[10],0)
			ScaleEntity e\room\Objects[10], RoomScale,RoomScale,RoomScale
			PositionEntity(e\room\Objects[10], xtemp, (992.0+12000.0+288.0)*RoomScale, ztemp-176.0*RoomScale,True)
			EntityParent e\room\Objects[10],e\room\Objects[9]
			RotateEntity e\room\Objects[9], 0, 48, 0
			RotateEntity e\room\Objects[10], 40, 0, 0
			
			For temp = 0 To 20
				For i = 0 To 1
					TranslateEntity e\room\NPC[i]\Collider, 0, -0.04, 0
				Next							
				For i = 5 To 8
					TranslateEntity e\room\NPC[i]\Collider, 0, -0.04, 0
				Next
			Next
			
			ResetEntity mainPlayer\collider
			e\EventState = 1.0
			
			If (Not Contained106) Then PlaySound_Strict LoadTempSound("SFX\Ending\GateA\106Escape.ogg") 
			
			DrawLoading(100)
		Else
			
			ShouldPlay = 5
			
			e\EventState = e\EventState+timing\tickDuration
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
			CameraFogRange mainPlayer\cam, 5,30
			
			angle = Max(Sin(EntityYaw(mainPlayer\collider)+90),0.0)
			;240,220,200
			CameraFogColor (mainPlayer\cam,200+(angle*40),200+(angle*20),200)
			CameraClsColor (mainPlayer\cam,200+(angle*40),200+(angle*20),200)		
			CameraRange(mainPlayer\cam, 0.05, 30)
			
			AmbientLight (140, 140, 140)
			
			For i = 2 To 4
				If e\room\NPC[i]<>Null Then 
					If e\room\NPC[i]\State < 2 Then 
						PositionEntity(e\room\NPC[i]\Collider, EntityX(e\room\Objects[3],True)+Cos(e\EventState/10+(120*i))*6000.0*RoomScale,15000*RoomScale,EntityZ(e\room\Objects[3],True)+Sin(e\EventState/10+(120*i))*6000.0*RoomScale)
						RotateEntity e\room\NPC[i]\Collider,7.0,(e\EventState/10+(120*i)),20.0
					EndIf
				EndIf
			Next
			
			UpdateSky()
			
			If e\EventState=>350 Then
				If Contained106=False Then
					If e\EventState-timing\tickDuration < 350
						Curr106\State = -0.1
						;Curr106\Idle = True
						SetNPCFrame(Curr106, 110.0)
						PositionEntity (Curr106\Collider, EntityX(e\room\Objects[3],True),EntityY(mainPlayer\collider)-50.0,EntityZ(e\room\Objects[3],True),True)
						PositionEntity (Curr106\obj, EntityX(e\room\Objects[3],True),EntityY(mainPlayer\collider)-50.0,EntityZ(e\room\Objects[3],True),True)
						de.Decals = CreateDecal(0, EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(e\room\Objects[3],True), 90, Rand(360), 0)
						de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 
						;TODO: fix audio
						;PlaySound_Strict (HorrorSFX(5))
						;PlaySound_Strict DecaySFX(0)
					ElseIf Curr106\State < 0
						HideEntity Curr106\obj2
						Curr106\PathTimer = 70*100
						
						If Curr106\State3 = 0 Then
							If Curr106\PathStatus <> 1 Then
								PositionEntity Curr106\Collider,EntityX(e\room\Objects[3],True),EntityY(Curr106\Collider),EntityZ(e\room\Objects[3],True),True
								If Curr106\State =< -10 Then
									dist# = EntityY(Curr106\Collider)
									PositionEntity Curr106\Collider,EntityX(Curr106\Collider),EntityY(e\room\Objects[3],True),EntityZ(Curr106\Collider),True
									;Curr106\PathStatus = FindPath(Curr106, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
									Curr106\PathStatus = FindPath(Curr106,EntityX(e\room\NPC[5]\Collider,True),EntityY(e\room\NPC[5]\Collider,True),EntityZ(e\room\NPC[5]\Collider,True))
									Curr106\PathTimer = 70*200
									PositionEntity Curr106\Collider,EntityX(Curr106\Collider),dist,EntityZ(Curr106\Collider),True
									ResetEntity Curr106\Collider
									Curr106\PathLocation = 1
									;Curr106\Idle = False
								;Else	
									;PositionEntity (Curr106\Collider, EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True),EntityZ(e\room\Objects[3],True),True)
									;Curr106\Idle = True
									;Animate2(Curr106\obj, AnimTime(Curr106\obj), 110, 259, 0.15, False)
									;If AnimTime(Curr106\obj)=>259 Then Curr106\Idle = False													
									
								EndIf
							Else
								Curr106\PathTimer = 70*200
								For i = 2 To 4 ;helicopters start attacking 106
									e\room\NPC[i]\State = 3 
									e\room\NPC[i]\EnemyX = EntityX(Curr106\obj,True)
									e\room\NPC[i]\EnemyY = EntityY(Curr106\obj,True)+5.0
									e\room\NPC[i]\EnemyZ = EntityZ(Curr106\obj,True)
								Next
								
								For i = 5 To 8
									e\room\NPC[i]\State = 5
									e\room\NPC[i]\EnemyX = EntityX(Curr106\obj,True)
									e\room\NPC[i]\EnemyY = EntityY(Curr106\obj,True)+0.4
									e\room\NPC[i]\EnemyZ = EntityZ(Curr106\obj,True)											
								Next
								
								pvt=CreatePivot()
								PositionEntity pvt, EntityX(e\room\Objects[10],True),EntityY(e\room\Objects[10],True),EntityZ(e\room\Objects[10],True)
								PointEntity pvt, Curr106\Collider
								RotateEntity(e\room\Objects[9],0,CurveAngle(EntityYaw(pvt),EntityYaw(e\room\Objects[9],True),150.0),0,True)
								RotateEntity(e\room\Objects[10],CurveAngle(EntityPitch(pvt),EntityPitch(e\room\Objects[10],True),200.0),EntityYaw(e\room\Objects[9],True),0, True)
								
								FreeEntity pvt
								
								If timing\tickDuration > 0 Then ;decals under 106
									If ((e\EventState-timing\tickDuration) Mod 100.0)=<50.0 And (e\EventState Mod 100.0)>50.0 Then
										de.Decals = CreateDecal(0, EntityX(Curr106\Collider,True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(Curr106\Collider,True), 90, Rand(360), 0)
										de\Size = 0.2 : de\SizeChange = 0.004 : de\timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
							EndIf
						EndIf
						
						dist# = Distance(EntityX(Curr106\Collider),EntityZ(Curr106\Collider),EntityX(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
						
						Curr106\CurrSpeed = CurveValue(0, Curr106\CurrSpeed, Max(5*dist,2.0))
						If dist < 15.0 Then
							If e\SoundCHN2 = 0 Then
								e\SoundCHN2 = PlaySound_Strict (LoadTempSound("SFX\Ending\GateA\Franklin.ogg"))
							EndIf
							
							If dist<0.4 Then
								Curr106\PathStatus = 0
								Curr106\PathTimer = 70*200
								If Curr106\State3=0 Then 
									SetNPCFrame(Curr106, 259.0)
									If e\Sound <> 0 Then FreeSound_Strict e\Sound : e\Sound = 0
									LoadEventSound(e,"SFX\Ending\GateA\106Retreat.ogg")
									e\SoundCHN = PlaySound2(e\Sound, mainPlayer\cam, Curr106\Collider, 35.0)
								EndIf
								
								If timing\tickDuration > 0 Then ;106:n alle ilmestyy decaleita
									If ((e\EventState-timing\tickDuration) Mod 160.0)=<50.0 And (e\EventState Mod 160.0)>50.0 Then
										de.Decals = CreateDecal(0, EntityX(Curr106\Collider,True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(Curr106\Collider,True), 90, Rand(360), 0)
										de\Size = 0.05 : de\SizeChange = 0.004 : de\timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
								
								AnimateNPC(Curr106, 259, 110, -0.1, False)
								
								Curr106\State3 = Curr106\State3+timing\tickDuration
								PositionEntity(Curr106\Collider, EntityX(Curr106\Collider,True),CurveValue(EntityY(e\room\Objects[3],True)-(Curr106\State3/4500.0),EntityY(Curr106\Collider,True),100.0),EntityZ(Curr106\Collider,True))
								If Curr106\State3>700.0 Then
									Curr106\State = 100000
									e\EventState2 = 0
									For i = 5 To 8
										e\room\NPC[i]\State = 1
									Next
									For i = 2 To 4 ;helicopters attack the player
										e\room\NPC[i]\State = 2
									Next
									HideEntity Curr106\obj
								EndIf
							Else
								If dist < 8.5 Then 
									If e\EventState2=0;ChannelPlaying(e\SoundCHN2) = 0 Then
										e\SoundCHN2 = PlaySound_Strict (LoadTempSound("SFX\Ending\GateA\HIDTurret.ogg"))
										e\EventState2 = 1
									ElseIf e\EventState2>0
										e\EventState2=e\EventState2+timing\tickDuration
										If e\EventState2=> 7.5*70 Then
											If e\EventState2-timing\tickDuration < 7.5*70 Then
												p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+0.4, EntityZ(Curr106\obj,True), 4, 7.0, 0, (6.7*70))
												p\speed = 0.0
												p\A = 1.0
												EntityParent p\pvt, Curr106\Collider, True
												
												p.Particles = CreateParticle(EntityX(e\room\Objects[10],True),EntityY(e\room\Objects[10],True),EntityZ(e\room\Objects[10],True), 4, 2.0, 0, (6.7*70))
												RotateEntity p\pvt, EntityPitch(e\room\Objects[10],True),EntityYaw(e\room\Objects[10],True),0,True
												MoveEntity p\pvt, 0, 92.0*RoomScale, 512.0*RoomScale
												p\speed = 0.0
												p\A = 1.0
												EntityParent p\pvt, e\room\Objects[10], True
											ElseIf e\EventState2 < 14.3*70
												mainPlayer\camShake = 0.5
												mainPlayer\lightFlash = 0.3+EntityInView(e\room\Objects[10],mainPlayer\cam)*0.5
											EndIf
										EndIf
									EndIf
									
									For i = 0 To Rand(2,8)-Int(dist)
										p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+Rnd(0.4,0.9), EntityZ(Curr106\obj), 0, 0.006, -0.002, 40)
										p\speed = 0.005
										p\A = 0.8
										p\Achange = -0.01
										RotateEntity p\pvt, -Rnd(70,110), Rnd(360),0	
									Next										
								EndIf
								
								
							EndIf
						EndIf
					EndIf
					
					If e\EventState3 = 0.0 Then 
						If Abs(EntityY(mainPlayer\collider)-EntityY(e\room\Objects[11],True))<1.0 Then
							If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True)) < 12.0 Then
								Curr106\State = 100000
								HideEntity Curr106\obj
								
								;MTF spawns at the tunnel entrance
								For i = 5 To 8
									e\room\NPC[i]\State = 3
									PositionEntity e\room\NPC[i]\Collider, EntityX(e\room\Objects[15],True)+(i-6)*0.3,EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True)+(i-6)*0.3, True
									ResetEntity e\room\NPC[i]\Collider
									
									e\room\NPC[i]\PathStatus = FindPath(e\room\NPC[i], EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
									e\room\NPC[i]\PathTimer = 70*2
									e\room\NPC[i]\LastSeen = 70*100
								Next
								e\room\NPC[5]\Sound = LoadSound_Strict("SFX\Character\MTF\ThereHeIs1.ogg")
								PlaySound2(e\room\NPC[5]\Sound, mainPlayer\cam, e\room\NPC[5]\Collider, 25.0)
								
								e\room\RoomDoors[2]\open = True
								
								For i = 2 To 4
									Delete e\room\NPC[i]
									e\room\NPC[i]=Null
								Next
								
								e\EventState3 = 1.0
							EndIf
						EndIf
					ElseIf e\EventState3 = 1.0
						
						For i = 5 To 8
							If EntityDistance(e\room\NPC[i]\Collider,mainPlayer\collider)> 4.0 Then e\room\NPC[i]\State = 3
						Next
						
						If Abs(EntityY(mainPlayer\collider)-EntityY(e\room\Objects[11],True))<1.0 Then
							If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True)) < 7.0 Then
								e\room\Objects[12] = LoadMesh_Strict("GFX\npcs\s2.b3d")
								EntityColor e\room\Objects[12], 0,0,0
								ScaleMesh (e\room\Objects[12], 0.32/21.3, 0.32/21.3, 0.32/21.3)
								PositionEntity e\room\Objects[12], EntityX(e\room\Objects[11],True), EntityY(e\room\Objects[11],True), EntityZ(e\room\Objects[11],True)
								
								obj = CopyEntity(e\room\Objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-3968*RoomScale, EntityY(e\room\Objects[11],True), EntityZ(e\room\obj,True)-1920*RoomScale
								
								obj = CopyEntity(e\room\Objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-4160*RoomScale, EntityY(e\room\Objects[11],True), EntityZ(e\room\obj,True)-1920*RoomScale
								
								obj = CopyEntity(e\room\Objects[12])
								PositionEntity obj, EntityX(e\room\obj,True)-4064*RoomScale, EntityY(e\room\Objects[11],True), EntityZ(e\room\obj,True)-2112*RoomScale
								
								e\SoundCHN = PlaySound2(LoadTempSound("SFX\Ending\GateA\Bell1.ogg"), mainPlayer\cam, e\room\Objects[12])
								
								p.Particles = CreateParticle(EntityX(e\room\Objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\Objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.15
								p\A = 0.5
								p.Particles = CreateParticle(EntityX(e\room\Objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\Objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.25
								p\A = 0.5
								PointEntity p\pvt, mainPlayer\collider
								
								mainPlayer\camShake = 1.0
								mainPlayer\lightFlash = 1.0
								
								e\EventState3 = 2.0
							EndIf
						EndIf
					Else
						e\EventState3=e\EventState3+timing\tickDuration
						PointEntity e\room\Objects[12], mainPlayer\collider
						RotateEntity e\room\Objects[12], 0, EntityYaw(e\room\Objects[12]), 0
						
						mainPlayer\stamina = -5.0
						
						mainPlayer\blurTimer = Sin(e\EventState3*0.7)*1000.0
						
						If Not mainPlayer\dead Then 
							CameraZoom(mainPlayer\cam, 1.0+Sin(e\EventState3*0.8)*0.2)
							
							dist = EntityDistance(mainPlayer\collider,e\room\Objects[11])
							If dist < 6.5 Then
								PositionEntity(mainPlayer\collider, CurveValue(EntityX(e\room\Objects[11],True),EntityX(mainPlayer\collider),dist*80),EntityY(mainPlayer\collider),CurveValue(EntityZ(e\room\Objects[0],True),EntityZ(mainPlayer\collider),dist*80))
							EndIf
						EndIf
						
						;tunneli menee umpeen
						If e\EventState3>50 And e\EventState3<230 Then
							mainPlayer\camShake = Sin(e\EventState3-50)*3
							TurnEntity e\room\Objects[13], 0, Sin(e\EventState3-50)*-0.85, 0, True
							TurnEntity e\room\Objects[14], 0, Sin(e\EventState3-50)*0.85, 0, True
							
							For i = 5 To 8
								PositionEntity (e\room\NPC[i]\Collider, CurveValue(EntityX(e\room\RoomDoors[2]\frameobj,True), EntityX(e\room\NPC[i]\Collider,True),50.0),EntityY(e\room\NPC[i]\Collider,True),CurveValue(EntityZ(e\room\RoomDoors[2]\frameobj,True), EntityZ(e\room\NPC[i]\Collider,True),50.0),True)
								ResetEntity e\room\NPC[i]\Collider
							Next
						EndIf
						
						If e\EventState3=>230.0 Then
							If e\EventState3-timing\tickDuration<230.0 Then
								e\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\Ending\GateA\CI.ogg"))
							EndIf
							
							If ChannelPlaying(e\SoundCHN)=False Then; And SelectedEnding="" Then
								PlaySound_Strict LoadTempSound("SFX\Ending\GateA\Bell2.ogg")
								
								p.Particles = CreateParticle(EntityX(e\room\Objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\Objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.15
								p\A = 0.5
								p.Particles = CreateParticle(EntityX(e\room\Objects[11],True),EntityY(mainPlayer\cam,True), EntityZ(e\room\Objects[11],True), 4, 8.0, 0, 50)
								p\speed = 0.25
								p\A = 0.5
								
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
					
					If e\EventState2 = 0 Then
						;PositionEntity (e\room\NPC[5]\Collider, EntityX(e\room\obj,True)-3408*RoomScale, EntityY(e\room\obj,True)-796*RoomScale, EntityZ(e\room\obj,True)+4976, True)
						;ResetEntity e\room\NPC[5]\Collider
						e\EventState2 = 1
						
						For i = 5 To 8
							e\room\NPC[i]\State = 3
							
							e\room\NPC[i]\PathStatus = FindPath(e\room\NPC[i], EntityX(e\room\obj)-1.0+2.0*(i Mod 2),EntityY(mainPlayer\collider)+0.2,EntityZ(e\room\obj)-2.0*(i Mod 2))
							e\room\NPC[i]\PathTimer = 70*Rand(15,20)
							e\room\NPC[i]\LastSeen = 70*300
						Next
					Else
						
						For i = 5 To 8
							If e\room\NPC[i]\State = 5
								e\room\NPC[i]\EnemyX = EntityX(mainPlayer\collider)
								e\room\NPC[i]\EnemyY = EntityY(mainPlayer\collider)
								e\room\NPC[i]\EnemyZ = EntityZ(mainPlayer\collider)
							Else
								If EntityDistance(e\room\NPC[i]\Collider,mainPlayer\collider)<6.0
									e\room\NPC[i]\State = 5
									e\room\NPC[i]\CurrSpeed = 0
								EndIf
							EndIf
						Next
						
						If e\EventState2=<1 Then
							For i = 5 To 8
								If e\room\NPC[i]\State = 5 Then
									For temp = 5 To 8
										e\room\NPC[temp]\State = 5
										e\room\NPC[temp]\EnemyX = EntityX(mainPlayer\collider)
										e\room\NPC[temp]\EnemyY = EntityY(mainPlayer\collider)
										e\room\NPC[temp]\EnemyZ = EntityZ(mainPlayer\collider)
										e\room\NPC[temp]\PathTimer = 70*Rand(7,10)
										e\room\NPC[temp]\Reload = 2000
										mainPlayer\disableControls = True
									Next
									
									If e\EventState2=1 Then
										e\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\Ending\GateA\STOPRIGHTTHERE.ogg"))
										e\EventState2=2			
									EndIf
								Else
									e\room\NPC[i]\LastSeen = 70*300
									e\room\NPC[i]\Reload = 2000
									e\room\NPC[i]\State3 = 70*145											
								EndIf
							Next										
						Else
							
							ShouldPlay = 0
							mainPlayer\moveSpeed = 0
							If ChannelPlaying(e\SoundCHN)=False Then
								PlaySound_Strict IntroSFX(9)
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

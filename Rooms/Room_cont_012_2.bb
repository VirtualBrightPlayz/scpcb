Function FillRoom_cont_012_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 672.0 * RoomScale, 270, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 880.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 304.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 840.0 * RoomScale, True)	
    TurnEntity d\buttons[1],0,0,0,True
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x -512.0 * RoomScale, -768.0*RoomScale, r\z -336.0 * RoomScale, 0, r, False, False)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False : r\RoomDoors[0]\locked = True
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 176.0 * RoomScale, -512.0*RoomScale, r\z - 364.0 * RoomScale, True)
    FreeEntity r\RoomDoors[0]\buttons[1] : r\RoomDoors[0]\buttons[1]=0
    
    r\Objects[0] = CopyEntity(LeverBaseOBJ)
    r\Objects[1] = CopyEntity(LeverOBJ)
    
    r\Levers[0] = r\Objects[1]
    
    For i% = 0 To 1
        ScaleEntity(r\Objects[i], 0.04, 0.04, 0.04)
        PositionEntity (r\Objects[i], r\x + 240.0 * RoomScale, r\y - 512.0 * RoomScale, r\z - 364 * RoomScale, True)
        
        EntityParent(r\Objects[i], r\obj)
    Next
    ;RotateEntity(r\Objects[0], 0, 0, 0)
    RotateEntity(r\Objects[1], 10, -180, 0)
    
    EntityPickMode r\Objects[1], 1, False
    EntityRadius r\Objects[1], 0.1
    
    r\Objects[2] = LoadMesh_Strict("GFX\map\room012_2.b3d")
    ScaleEntity r\Objects[2], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[2], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\Objects[2], r\obj)
    
    r\Objects[3] = CreateSprite()
    PositionEntity(r\Objects[3], r\x - 43.5 * RoomScale, - 574 * RoomScale, r\z - 362.0 * RoomScale)
    ScaleSprite(r\Objects[3], 0.015, 0.015)
    EntityTexture(r\Objects[3], LightSpriteTex(1))
    EntityBlend (r\Objects[3], 3)
    EntityParent(r\Objects[3], r\obj)
    HideEntity r\Objects[3]
    
    r\Objects[4] = LoadMesh_Strict("GFX\map\room012_3.b3d")
    tex=LoadTexture_Strict("GFX\map\scp-012_0.jpg")
    EntityTexture r\Objects[4],tex, 0,1
    ScaleEntity r\Objects[4], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[4], r\x - 360 * RoomScale, - 130 * RoomScale, r\z + 456.0 * RoomScale, 0)
    EntityParent(r\Objects[4], r\Objects[2])
    
    it = CreateItem("Document SCP-012", "paper", r\x - 56.0 * RoomScale, r\y - 576.0 * RoomScale, r\z - 408.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it.Items = CreateItem("Severed Hand", "hand", r\x - 784*RoomScale, -576*RoomScale+0.3, r\z+640*RoomScale)
    EntityParent(it\collider, r\obj)
    
    de.Decals = CreateDecal(3,  r\x - 784*RoomScale, -768*RoomScale+0.01, r\z+640*RoomScale,90,Rnd(360),0)
    de\Size = 0.5
    ScaleSprite(de\obj, de\Size,de\Size)
    EntityParent de\obj, r\obj
End Function


Function UpdateEvent_cont_012_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		
		If e\EventState=0 Then
			If EntityDistance(mainPlayer\collider, e\room\RoomDoors[0]\obj)<2.5 And RemoteDoorOn Then
				;PlaySound_Strict HorrorSFX(7)
				PlaySound2 (LeverSFX,mainPlayer\cam,e\room\RoomDoors[0]\obj) 
				e\EventState=1
				e\room\RoomDoors[0]\locked = False
				UseDoor(e\room\RoomDoors[0],False)
				e\room\RoomDoors[0]\locked = True
			EndIf
		Else
			
			If e\Sound=0 Then LoadEventSound(e,"SFX\Music\012Golgotha.ogg")
			e\SoundCHN = LoopSound2(e\Sound, e\SoundCHN, mainPlayer\cam, e\room\Objects[3], 5.0)
			
			If e\Sound2=0 Then LoadEventSound(e,"SFX\Music\012.ogg",1)
			
			If e\EventState<90 Then e\EventState=CurveValue(90,e\EventState,500)
			PositionEntity e\room\Objects[2], EntityX(e\room\Objects[2],True),(-130-448*Sin(e\EventState))*RoomScale,EntityZ(e\room\Objects[2],True),True
			
			If e\EventState2 > 0 And e\EventState2 < 200 Then
				e\EventState2 = e\EventState2 + timing\tickDuration
				RotateEntity(e\room\Objects[1], CurveValue(85, EntityPitch(e\room\Objects[1]), 5), EntityYaw(e\room\Objects[1]), 0)
			Else
				e\EventState2 = e\EventState2 + timing\tickDuration
				If e\EventState2<250 Then
					ShowEntity e\room\Objects[3] 
				Else
					HideEntity e\room\Objects[3] 
					If e\EventState2>300 Then e\EventState2=200
				EndIf
			EndIf
			
			If (Not IsPlayerWearingTempName(mainPlayer,"hazmatsuit3")) And (Not IsPlayerWearingTempName(mainPlayer,"gasmask3")) And (Not IsPlayerWearingTempName(mainPlayer,"nvgoggles")) Then
				temp = False
				If EntityVisible(e\room\Objects[2],mainPlayer\cam) Then temp = True
				
				;012 not visible, walk to the door
				If temp=False Then
					If EntityVisible(e\room\RoomDoors[0]\frameobj,mainPlayer\cam) Then
						pvt% = CreatePivot()
						PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\collider), EntityZ(mainPlayer\cam)
						PointEntity(pvt, e\room\RoomDoors[0]\frameobj)
						;TurnEntity(pvt, 90, 0, 0)
						mainPlayer\headPitch = CurveAngle(90, mainPlayer\headPitch+90, 100)
						mainPlayer\headPitch=mainPlayer\headPitch-90
						RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 150), 0)
						
						angle = WrapAngle(EntityYaw(pvt)-EntityYaw(mainPlayer\collider))
						If angle<40.0 Then
							mainPlayer\forceMove = (40.0-angle)*0.008
						ElseIf angle > 310.0
							mainPlayer\forceMove = (40.0-Abs(360.0-angle))*0.008
						EndIf
						
						FreeEntity pvt										
					EndIf
				Else
					e\SoundCHN2 = LoopSound2(e\Sound2, e\SoundCHN2, mainPlayer\cam, e\room\Objects[3], 10, e\EventState3/(86.0*70.0))
					
					pvt% = CreatePivot()
					PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(e\room\Objects[2],True)-0.05, EntityZ(mainPlayer\cam)
					PointEntity(pvt, e\room\Objects[2])
					RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), 80-(e\EventState3/200.0)), 0)
					
					TurnEntity(pvt, 90, 0, 0)
					mainPlayer\headPitch = CurveAngle(EntityPitch(pvt)+25, mainPlayer\headPitch + 90.0, 80-(e\EventState3/200.0))
					mainPlayer\headPitch=mainPlayer\headPitch-90
					
					dist = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[2],True),EntityZ(e\room\Objects[2],True))
					
					mainPlayer\heartbeatIntensity = 150
					;HeartBeatVolume = Max(3.0-dist,0.0)/3.0
					mainPlayer\blurTimer = Max((2.0-dist)*(e\EventState3/800.0)*(Sin(Float(TimeInPosMilliSecs()) / 20.0 + 1.0)),mainPlayer\blurTimer)
					mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs()) / 20.0)+1.0)*8.0*Max((3.0-dist),0.0))
					
					If mainPlayer\breathChn <> 0 Then
						If ChannelPlaying(mainPlayer\breathChn) Then StopChannel(mainPlayer\breathChn)
					EndIf
					
					If dist < 0.6 Then
						e\EventState3=Min(e\EventState3+timing\tickDuration,86*70)
						If e\EventState3>70 And e\EventState3-timing\tickDuration=<70 Then
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech1.ogg")
						ElseIf e\EventState3>13*70 And e\EventState3-timing\tickDuration=<13*70
							Msg="You start pushing your nails into your wrist, drawing blood."
							MsgTimer = 7*70
							mainPlayer\injuries=mainPlayer\injuries+0.5
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech2.ogg")
						ElseIf e\EventState3>31*70 And e\EventState3-timing\tickDuration=<31*70
							tex = LoadTexture_Strict("GFX\map\scp-012_1.jpg")
							EntityTexture (e\room\Objects[4], tex,0,1)
							FreeTexture tex
							
							Msg="You tear open your left wrist and start writing on the composition with your blood."
							MsgTimer = 7*70
							mainPlayer\injuries=Max(mainPlayer\injuries,1.5)
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech"+Rand(3,4)+".ogg")
						ElseIf e\EventState3>49*70 And e\EventState3-timing\tickDuration=<49*70
							Msg="You push your fingers deeper into the wound."
							MsgTimer = 8*70
							mainPlayer\injuries=mainPlayer\injuries+0.3
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech5.ogg")
						ElseIf e\EventState3>63*70 And e\EventState3-timing\tickDuration=<63*70
							tex = LoadTexture_Strict("GFX\map\scp-012_2.jpg")
							EntityTexture (e\room\Objects[4], tex,0,1)	
							FreeTexture tex
							
							mainPlayer\injuries=mainPlayer\injuries+0.5
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech6.ogg")
						ElseIf e\EventState3>74*70 And e\EventState3-timing\tickDuration=<74*70
							tex = LoadTexture_Strict("GFX\map\scp-012_3.jpg")
							EntityTexture (e\room\Objects[4], tex,0,1)
							FreeTexture tex
							
							Msg="You rip the wound wide open. Grabbing scoops of blood pouring out."
							MsgTimer = 7*70
							mainPlayer\injuries=mainPlayer\injuries+0.8
							PlaySound_Strict LoadTempSound("SFX\SCP\012\Speech7.ogg")
							mainPlayer\crouching = True
							
							de.Decals = CreateDecal(17,  EntityX(mainPlayer\collider), -768*RoomScale+0.01, EntityZ(mainPlayer\collider),90,Rnd(360),0)
							de\Size = 0.1 : de\maxsize = 0.45 : de\sizechange = 0.0002 : UpdateDecals()
						ElseIf e\EventState3>85*70 And e\EventState3-timing\tickDuration=<85*70	
							DeathMSG = "Subject D-9341 found in a pool of blood next to SCP-012. Subject seems to have ripped open his wrists and written three extra "
							DeathMSG = DeathMSG + "lines to the composition before dying of blood loss."
							Kill(mainPlayer)
						EndIf
						
						RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(mainPlayer\collider)+Sin(e\EventState3*(e\EventState3/2000))*(e\EventState3/300), EntityYaw(mainPlayer\collider), 80), 0)
						
					Else
						angle = WrapAngle(EntityYaw(pvt)-EntityYaw(mainPlayer\collider))
						If angle<40.0 Then
							mainPlayer\forceMove = (40.0-angle)*0.02
						ElseIf angle > 310.0
							mainPlayer\forceMove = (40.0-Abs(360.0-angle))*0.02
						EndIf
					EndIf								
					
					FreeEntity pvt								
				EndIf
				
			EndIf
			
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
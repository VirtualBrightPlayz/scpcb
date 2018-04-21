Function FillRoom_tsl_ez_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x - 114.0 * RoomScale, 0.0, r\z)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x + 114.0 * RoomScale, 0.0, r\z)
    EntityParent(r\Objects[1], r\obj)			
    
    r\Objects[2] = CreatePivot()
    PositionEntity(r\Objects[2], r\x, 0.0, r\z)
    EntityParent(r\Objects[2], r\obj)	
    
    r\Objects[3] = CreateSprite()
    EntityTexture (r\Objects[3], TeslaTexture)
    SpriteViewMode(r\Objects[3],2) 
    ;ScaleSprite (r\Objects[3],((512.0 * RoomScale)/2.0),((512.0 * RoomScale)/2.0))
    EntityBlend (r\Objects[3], 3) 
    EntityFX(r\Objects[3], 1 + 8 + 16)
    
    PositionEntity(r\Objects[3], r\x, 0.8, r\z)
    
    HideEntity r\Objects[3]
    EntityParent(r\Objects[3], r\obj)
    
    w.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z + 292.0 * RoomScale, Null, r)
    w2.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z - 284.0 * RoomScale, Null, r)
    w\connected[0] = w2 : w\dist[0] = EntityDistance(w\obj, w2\obj)
    w2\connected[0] = w : w2\dist[0] = w\dist[0]
    
    r\Objects[4] = CreateSprite()
    PositionEntity(r\Objects[4], r\x - 32 * RoomScale, 568 * RoomScale, r\z)
    ScaleSprite(r\Objects[4], 0.03, 0.03)
    EntityTexture(r\Objects[4], LightSpriteTex(1))
    EntityBlend (r\Objects[4], 3)
    EntityParent(r\Objects[4], r\obj)
    HideEntity r\Objects[4]
    
    r\Objects[5] = CreatePivot()
    PositionEntity(r\Objects[5],r\x,0,r\z-800*RoomScale)
    EntityParent(r\Objects[5],r\obj)
    
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6],r\x,0,r\z+800*RoomScale)
    EntityParent(r\Objects[6],r\obj)
End Function


Function UpdateEvent_tesla(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	temp = True
	If e\EventState2 > 70*3.5 And e\EventState2 < 70*90 Then temp = False
	
	If temp And EntityY(mainPlayer\collider, True) > EntityY(e\room\obj,True) And EntityY(mainPlayer\collider, True) < 4.0 Then
		
		If e\Sound = 0 Then e\Sound = LoadSound_Strict("SFX\Room\Tesla\Shock.ogg")
		
		If e\EventState = 0 Then
			If (TimeInPosMilliSecs() Mod 1500) < 800 Then
				ShowEntity e\room\Objects[4]
			Else
				HideEntity e\room\Objects[4]
			EndIf						
			
			If e\room\dist < 8
			If e\SoundCHN = 0 Then ;humming when the player isn't close
				e\SoundCHN = PlaySound2(TeslaIdleSFX, mainPlayer\cam, e\room\Objects[3],4.0,0.5)
			Else
				If Not ChannelPlaying(e\SoundCHN) Then e\SoundCHN = PlaySound2(TeslaIdleSFX, mainPlayer\cam, e\room\Objects[3],4.0,0.5)
			EndIf
			
			For i = 0 To 2
				If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)) < 300.0*RoomScale Then
					;play the activation sound
					If Not mainPlayer\dead Then 
						mainPlayer\loudness = Max(8.0,mainPlayer\loudness)
						StopChannel(e\SoundCHN)
						e\SoundCHN = PlaySound2(TeslaActivateSFX, mainPlayer\cam, e\room\Objects[3],4.0,0.5)
						e\EventState = 1
						Exit
					EndIf
				EndIf
			Next
			
			If Curr106\State < -10 And e\EventState = 0 Then 
				For i = 0 To 2
					If Distance(EntityX(Curr106\Collider),EntityZ(Curr106\Collider),EntityX(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)) < 300.0*RoomScale Then
						;play the activation sound
						If Not mainPlayer\dead Then 
							StopChannel(e\SoundCHN)
							e\SoundCHN = PlaySound2(TeslaActivateSFX, mainPlayer\cam, e\room\Objects[3],4.0,0.5)
							HideEntity e\room\Objects[4]
							e\EventState = 1
							Curr106\State = 70 * 60 * Rand(10,13)
							Exit
						EndIf
					EndIf
				Next
			EndIf
		Else
			e\EventState = e\EventState+timing\tickDuration
			If e\EventState =< 40 Then
				If (TimeInPosMilliSecs() Mod 100) < 50 Then
					ShowEntity e\room\Objects[4]
				Else
					HideEntity e\room\Objects[4]
				EndIf
			Else
				;If e\EventState-timing\tickDuration =< 40 Then PlaySound_Strict(e\Sound)
				If e\room\dist < 2
				If e\EventState-timing\tickDuration =< 40 Then PlaySound_Strict(e\Sound)	
				Else
					If e\EventState-timing\tickDuration =< 40 Then PlaySound2(e\Sound,mainPlayer\cam,e\room\Objects[2])
				EndIf
				If e\EventState < 70 Then 
					
					If Not mainPlayer\dead Then 
						For i = 0 To 2
							If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)) < 250.0*RoomScale Then
								ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
								mainPlayer\lightFlash = 0.4
								mainPlayer\camShake = 1.0
								Kill(mainPlayer)
								DeathMSG = "Subject D-9341 killed by the Tesla gate at [REDACTED]."
							EndIf
						Next
					EndIf
					
					If Curr106\State < -10 Then
						For i = 0 To 2
							If Distance(EntityX(Curr106\Collider),EntityZ(Curr106\Collider),EntityX(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)) < 250.0*RoomScale Then
								ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
								mainPlayer\lightFlash = 0.3
								For i = 0 To 10
									p.Particles = CreateParticle(EntityX(Curr106\Collider, True), EntityY(Curr106\Collider, True), EntityZ(Curr106\Collider, True), 0, 0.015, -0.2, 250)
									p\size = 0.03
									p\gravity = -0.2
									p\lifetime = 200
									p\SizeChange = 0.005
									p\speed = 0.001
									RotateEntity(p\pvt, Rnd(360), Rnd(360), 0, True)
								Next
								Curr106\State = -20000
								TranslateEntity(Curr106\Collider,0,-50.0,0,True)
							EndIf
						Next								
					EndIf
					
					HideEntity e\room\Objects[3]
					HideEntity e\room\Objects[4]
					
					If Rand(5)<5 Then 
						PositionTexture TeslaTexture,0.0,Rnd(0,1.0)
						ShowEntity e\room\Objects[3]								
					EndIf
				Else 
					If e\EventState-timing\tickDuration < 70 Then 
						StopChannel(e\SoundCHN)	
						e\SoundCHN = PlaySound2(TeslaPowerUpSFX, mainPlayer\cam, e\room\Objects[3],4.0,0.5)
					EndIf 
					HideEntity e\room\Objects[3]
					
					If e\EventState > 150 Then e\EventState = 0
				EndIf
			EndIf
		EndIf
	EndIf
	
	If mainPlayer\currRoom\RoomTemplate\Name <> "pocketdimension" And mainPlayer\currRoom\RoomTemplate\Name <> "room860" Then
		If e\EventState2 = 0 Then
			If e\EventState3 <=0 Then 
				temp = False
				For n.NPCs = Each NPCs
					If n\NPCtype = NPCtypeMTF Then
						If Abs(EntityX(n\Collider)-EntityX(e\room\obj,True))<4.0 Then
							If Abs(EntityZ(n\Collider)-EntityZ(e\room\obj,True))<4.0 Then
								temp = True
								If e\EventState2 = 0 Then
									n\Sound = LoadSound_Strict("SFX\Character\MTF\Tesla0.ogg")
									PlayMTFSound(n\Sound,n)
									
									LoadEventSound(e,"SFX\Character\MTF\Tesla1.ogg")
									e\SoundCHN = PlaySound_Strict (e\Sound)
									n\Idle = 70*10
									e\EventState2 = 70*100
								EndIf
							EndIf
						EndIf
					EndIf
				Next
				If temp = False Then e\EventState2=70*3.5
				e\EventState3=e\EventState3+140
			Else
				e\EventState3=e\EventState3-timing\tickDuration
			EndIf
		Else
			e\EventState2 = Max(e\EventState2-timing\tickDuration,0)
		EndIf					
	EndIf
	
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D

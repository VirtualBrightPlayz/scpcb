Function FillRoom_cont_106_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    it = CreateItem("Level 5 Key Card", "key5", r\x - 752.0 * RoomScale, r\y - 592 * RoomScale, r\z + 3026.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Dr. Allok's Note", "paper", r\x - 416.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2492.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Recall Protocol RP-106-N", "paper", r\x + 268.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2593.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    d = CreateDoor(r\zone, r\x - 968.0 * RoomScale, -764.0 * RoomScale, r\z + 1392.0 * RoomScale, 0, r, False, False, 4)
    d\AutoClose = False : d\open = False	
    
    d = CreateDoor(r\zone, r\x, 0, r\z - 464.0 * RoomScale, 0, r, False, False, 3)
    d\AutoClose = False : d\open = False			
    
    d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, -1280.0 * RoomScale, r\z, 90, r, False, False, 4)
    d\AutoClose = False : d\open = False	
    
    r\Objects[6] = LoadMesh("GFX\map\room1062.b3d")
    
    ScaleEntity (r\Objects[6],RoomScale,RoomScale,RoomScale)
    EntityType r\Objects[6], HIT_MAP
    EntityPickMode r\Objects[6], 3
    PositionEntity(r\Objects[6],r\x+784.0*RoomScale,-980.0*RoomScale,r\z+720.0*RoomScale,True)
    
    ;If BumpEnabled Then 
    ;	
    ;	For i = 1 To CountSurfaces(r\Objects[6])
    ;		sf = GetSurface(r\Objects[6],i)
    ;		b = GetSurfaceBrush( sf )
    ;		t = GetBrushTexture(b,1)
    ;		texname$ =  StripPath(TextureName(t))
    ;		
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
    
    EntityParent(r\Objects[6], r\obj)
    
    For n = 0 To 2 Step 2
        r\Objects[n] = CopyEntity(LeverBaseOBJ)
        r\Objects[n+1] = CopyEntity(LeverOBJ)
        
        r\Levers[n/2] = r\Objects[n+1]
        
        For i% = 0 To 1
            ScaleEntity(r\Objects[n+i], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[n+i], r\x - (555.0 - 81.0 * (n/2)) * RoomScale, r\y - 576.0 * RoomScale, r\z + 3040.0 * RoomScale, True)
            
            EntityParent(r\Objects[n+i], r\obj)
        Next
        RotateEntity(r\Objects[n], 0, 0, 0)
        RotateEntity(r\Objects[n+1], 10, -180, 0)
        
        ;EntityPickMode(r\Objects[n * 2 + 1], 2)
        EntityPickMode r\Objects[n+1], 1, False
        EntityRadius r\Objects[n+1], 0.1
        ;makecollbox(r\Objects[n * 2 + 1])
    Next
    
    RotateEntity(r\Objects[1], 81,-180,0)
    RotateEntity(r\Objects[3], -81,-180,0)			
    
    r\Objects[4] = CreateButton(r\x - 146.0*RoomScale, r\y - 576.0 * RoomScale, r\z + 3045.0 * RoomScale, 0,0,0)
    EntityParent (r\Objects[4],r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x + 768.0 * RoomScale, r\y + 1392.0 * RoomScale, r\z + 1696.0 * RoomScale, r, True)
    sc\angle = 45 + 90 + 180
    sc\turn = 20
    TurnEntity(sc\CameraObj, 45, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    r\Objects[7] = sc\CameraObj
    r\Objects[8] = sc\obj
    
    PositionEntity(sc\ScrObj, r\x - 272.0 * RoomScale, -544.0 * RoomScale, r\z + 3020.0 * RoomScale)
    TurnEntity(sc\ScrObj, 0, -10, 0)
    EntityParent sc\ScrObj, r\obj
    sc\CoffinEffect=0
    
    ;r\NPC[0] = CreateNPC(NPCtypeD, r\x + 1088.0 * RoomScale, 1096.0 * RoomScale, r\z + 1728.0 * RoomScale)
    r\Objects[5] = CreatePivot()
    TurnEntity r\Objects[5], 0,180,0
    PositionEntity (r\Objects[5], r\x + 1088.0 * RoomScale, 1104.0 * RoomScale, r\z + 1888.0 * RoomScale) 
    EntityParent r\Objects[5], r\obj
    ;HideEntity r\NPC[0]\obj
    
    r\Objects[9] = CreatePivot(r\obj)
    PositionEntity (r\Objects[9], r\x - 272 * RoomScale, r\y - 672.0 * RoomScale, r\z + 2736.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x-1216.0*RoomScale, r\y-336.0*RoomScale, r\z+1468.0*RoomScale, r, True)
    sc\angle = 315
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\CameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    sc\ID = 4
End Function


Function UpdateEvent_cont_106_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	;eventstate2 = are the magnets on
	
	If SoundTransmission Then 
		If e\EventState = 1 Then
			e\EventState3 = Min(e\EventState3+timing\tickDuration,4000)
		EndIf
		If IsChannelPlaying(e\soundChannels[0]) = False Then e\soundChannels[0] = PlaySound(RadioStatic)   
	EndIf
	
	If e\room\NPC[0]=Null Then ;add the lure subject
		TFormPoint(1088, 1096, 1728, e\room\obj, 0)
		e\room\NPC[0] = CreateNPC(NPCtypeD, TFormedX(), TFormedY(), TFormedZ())
		TurnEntity e\room\NPC[0]\Collider,0,e\room\angle+90,0,True
	EndIf
	
	If mainPlayer\currRoom = e\room And e\room\NPC[0]<>Null Then
		
		;ShowEntity e\room\NPC[0]\obj
		
		ShouldPlay = 66
		
		e\room\NPC[0]\State=6
		If e\room\NPC[0]\Idle = 0 Then
			AnimateNPC(e\room\NPC[0], 17.0, 19.0, 0.01, False)
			If e\room\NPC[0]\Frame = 19.0 Then e\room\NPC[0]\Idle = 1
		Else
			AnimateNPC(e\room\NPC[0], 19.0, 17.0, -0.01, False)	
			If e\room\NPC[0]\Frame = 17.0 Then e\room\NPC[0]\Idle = 0
		EndIf
		
		PositionEntity(e\room\NPC[0]\Collider, EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True)+0.1,EntityZ(e\room\Objects[5],True),True)
		RotateEntity(e\room\NPC[0]\Collider,EntityPitch(e\room\Objects[5],True),EntityYaw(e\room\Objects[5],True),0,True)
		ResetEntity(e\room\NPC[0]\Collider)
		
		temp = e\EventState2
		
		Local leverstate = UpdateLever(e\room\Objects[1],((EntityY(e\room\Objects[6],True)<-990*RoomScale) And (EntityY(e\room\Objects[6],True)>-1275.0*RoomScale)))
		If mainPlayer\grabbedEntity = e\room\Objects[1] And DrawHandIcon = True Then e\EventState2 = leverstate
		
		If e\EventState2 <> temp Then 
			If e\EventState2 = False Then
				PlaySound(MagnetDownSFX)
			Else
				PlaySound(MagnetUpSFX)	
			EndIf
		EndIf
		
		If ((e\EventState3>3200) Or (e\EventState3<2500)) Or (e\EventState<>1) Then
			SoundTransmission% = UpdateLever(e\room\Objects[3])
		EndIf
		If (Not SoundTransmission) Then
			If (e\soundChannels[1]<>0) Then
				If IsChannelPlaying(e\soundChannels[1]) Then StopChannel e\soundChannels[1]
			EndIf
			If (e\soundChannels[0]<>0) Then
				If IsChannelPlaying(e\soundChannels[0]) Then StopChannel e\soundChannels[0]
			EndIf
		EndIf
		
		If e\EventState = 0 Then 
			If SoundTransmission And Rand(100)=1 Then
				If e\soundChannels[1] = 0 Then
					LoadEventSound(e,"SFX\Character\LureSubject\Idle"+Rand(1,6)+".ogg",1)
					e\soundChannels[1] = PlaySound(e\sounds[1])								
				EndIf
				If IsChannelPlaying(e\soundChannels[1]) = False Then
					LoadEventSound(e,"SFX\Character\LureSubject\Idle"+Rand(1,6)+".ogg",1)
					e\soundChannels[1] = PlaySound(e\sounds[1])
				EndIf
			EndIf
			
			UpdateButton(e\room\Objects[4])
			If mainPlayer\closestButton = e\room\Objects[4] And MouseHit1 Then
				e\EventState = 1 ;start the femur breaker
				If SoundTransmission = True Then ;only play sounds if transmission is on
					If e\soundChannels[1] <> 0 Then
						If IsChannelPlaying(e\soundChannels[1]) Then StopChannel e\soundChannels[1]
					EndIf 
					FemurBreakerSFX = LoadSound("SFX\Room\106Chamber\FemurBreaker.ogg")
					e\soundChannels[1] = PlaySound (FemurBreakerSFX)
				EndIf
			EndIf
		ElseIf e\EventState = 1 ;bone broken
			If SoundTransmission And e\EventState3 < 2000 Then 
				If e\soundChannels[1] = 0 Then 
					LoadEventSound(e,"SFX\Character\LureSubject\Sniffling.ogg",1)
					e\soundChannels[1] = PlaySound(e\sounds[1])								
				EndIf
				If IsChannelPlaying(e\soundChannels[1]) = False Then
					LoadEventSound(e,"SFX\Character\LureSubject\Sniffling.ogg",1)
					e\soundChannels[1] = PlaySound(e\sounds[1])
				EndIf
			EndIf
			
			If e\EventState3 => 2500 Then
				
				If e\EventState2 = 1 And e\EventState3-timing\tickDuration < 2500 Then
					PositionEntity(Curr106\Collider, EntityX(e\room\Objects[6], True), EntityY(e\room\Objects[6], True), EntityZ(e\room\Objects[6], True))
					Contained106 = False
					ShowEntity Curr106\obj
					Curr106\Idle = False
					Curr106\State = -11
					e\EventState = 2
					Return
				EndIf
				
				ShouldPlay = 10
				
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[5], True), (700.0 + 108.0*(Min(e\EventState3-2500.0,800)/320.0))*RoomScale , EntityZ(e\room\Objects[5], True))
				HideEntity Curr106\obj2
				
				;PointEntity(Curr106\Collider, mainPlayer\cam)
				RotateEntity(Curr106\Collider,0, EntityYaw(e\room\Objects[5],True)+180.0, 0, True)
				Curr106\State = -11
				AnimateNPC(Curr106, 206, 250, 0.1)
				Curr106\Idle = True	
				
				If e\EventState3-timing\tickDuration < 2500 Then 
					d.Decals = CreateDecal(0, EntityX(e\room\Objects[5], True), 936.0*RoomScale, EntityZ(e\room\Objects[5], True), 90, 0, Rnd(360)) 
					d\Timer = 90000
					d\Alpha = 0.01 : d\AlphaChange = 0.005
					d\Size = 0.1 : d\SizeChange = 0.003	
					
					If e\soundChannels[1] <> 0 Then
						If IsChannelPlaying(e\soundChannels[1]) Then StopChannel e\soundChannels[1]
					EndIf 
					LoadEventSound(e,"SFX\Character\LureSubject\106Bait.ogg",1)
					e\soundChannels[1]=PlaySound(e\sounds[1])
				ElseIf e\EventState3-timing\tickDuration < 2900 And e\EventState3 => 2900 Then
					If FemurBreakerSFX <> 0 Then FreeSound FemurBreakerSFX : FemurBreakerSFX = 0
					
					d.Decals = CreateDecal(0, EntityX(e\room\Objects[7], True), EntityY(e\room\Objects[7], True) , EntityZ(e\room\Objects[7], True), 0, 0, 0) 
					RotateEntity(d\obj, EntityPitch(e\room\Objects[7], True)+Rand(10,20), EntityYaw(e\room\Objects[7], True)+30, EntityRoll(d\obj))
					MoveEntity d\obj, 0,0,0.15
					RotateEntity(d\obj, EntityPitch(e\room\Objects[7], True), EntityYaw(e\room\Objects[7], True), EntityRoll(d\obj))
					
					EntityParent d\obj, e\room\Objects[7]
					;TurnEntity (d\obj, 0, 180, 0)
					
					d\Timer = 90000
					d\Alpha = 0.01 : d\AlphaChange = 0.005
					d\Size = 0.05 : d\SizeChange = 0.002
				ElseIf e\EventState3 > 3200 Then
					PositionEntity e\room\Objects[8], 0, 1000.0, 0, True 
					PositionEntity e\room\Objects[7], 0, 1000.0, 0, True 
					
					If e\EventState2 = True Then ;magnets off -> 106 caught
						Contained106 = True
					Else ;magnets off -> 106 comes out and attacks
						PositionEntity(Curr106\Collider, EntityX(e\room\Objects[6], True), EntityY(e\room\Objects[6], True), EntityZ(e\room\Objects[6], True))
						
						Contained106 = False
						ShowEntity Curr106\obj
						Curr106\Idle = False
						Curr106\State = -11
						
						e\EventState = 2
						Return
					EndIf
				EndIf
				
			EndIf 
			
		EndIf
		
		If e\EventState2 Then
			PositionEntity (e\room\Objects[6],EntityX(e\room\Objects[6],True),CurveValue(-980.0*RoomScale + Sin(Float(TimeInPosMilliSecs())*0.04)*0.07,EntityY(e\room\Objects[6],True),200.0),EntityZ(e\room\Objects[6],True),True)
			RotateEntity(e\room\Objects[6], Sin(Float(TimeInPosMilliSecs())*0.03), EntityYaw(e\room\Objects[6],True), -Sin(Float(TimeInPosMilliSecs())*0.025), True)
		Else
			PositionEntity (e\room\Objects[6],EntityX(e\room\Objects[6],True),CurveValue(-1280.0*RoomScale,EntityY(e\room\Objects[6],True),200.0),EntityZ(e\room\Objects[6],True),True)
			RotateEntity(e\room\Objects[6], 0, EntityYaw(e\room\Objects[6],True), 0, True)
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D

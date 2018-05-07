

Dim ParticleTextures%(10)

Type Particles
	Field obj%, pvt%
	Field image%
	
	Field R#, G#, B#, A#, size#
	Field speed#, yspeed#, gravity#
	Field Rchange#, Gchange#, Bchange#, Achange#
	Field SizeChange#
	
	Field lifetime#
End Type 
	
Function CreateParticle.Particles(x#, y#, z#, image%, size#, gravity# = 1.0, lifetime% = 200)
	Local p.Particles = New Particles
	p\lifetime = lifetime
	
	p\obj = CreateSprite()
	PositionEntity(p\obj, x, y, z, True)
	EntityTexture(p\obj, ParticleTextures(image))
	RotateEntity(p\obj, 0, 0, Rnd(360))
	EntityFX(p\obj, 1 + 8)
	
	SpriteViewMode (p\obj, 3)
	
	Select image
		Case 0,5,6
			EntityBlend(p\obj, 1)
		Case 1,2,3,4,7
			EntityBlend(p\obj, BLEND_ADD)
	End Select
	
	p\pvt = CreatePivot()
	PositionEntity(p\pvt, x, y, z, True)
	
	p\image = image
	p\gravity = gravity * 0.004
	p\R = 255 : p\G = 255 : p\B = 255 : p\A = 1.0
	p\size = size
	ScaleSprite(p\obj, p\size, p\size)
	Return p
End Function
	
Function UpdateParticles()
	Local p.Particles
	For p.Particles = Each Particles
		MoveEntity(p\pvt, 0, 0, p\speed * timing\tickDuration)
		If p\gravity <> 0 Then p\yspeed = p\yspeed - p\gravity * timing\tickDuration
		TranslateEntity(p\pvt, 0, p\yspeed * timing\tickDuration, 0, True)
		
		PositionEntity(p\obj, EntityX(p\pvt,True), EntityY(p\pvt,True), EntityZ(p\pvt,True), True)
		
		;TurnEntity(p\obj, 0, 0, timing\tickDuration)
		
		If p\Achange <> 0 Then
			p\A=Min(Max(p\A+p\Achange * timing\tickDuration,0.0),1.0)
			EntityAlpha(p\obj, p\A)		
		EndIf
		
		If p\SizeChange <> 0 Then 
			p\size= p\size+p\SizeChange * timing\tickDuration
			ScaleSprite p\obj, p\size, p\size
		EndIf
		
		ShowEntity(p\obj)
		EntityAlpha(p\obj,1.0)
		
		p\lifetime=p\lifetime-timing\tickDuration
		If p\lifetime <= 0 Or p\size < 0.00001 Or p\A =< 0 Then
			RemoveParticle(p)
		End If
	Next
End Function
	
Function RemoveParticle(p.Particles)
	FreeEntity(p\obj)
	FreeEntity(p\pvt)	
	Delete p
End Function

Global InSmoke%
Global HissSFX.MarkedForRemoval

Type Emitters
	Field Obj%
	
	Field Size#
	Field MinImage%, MaxImage%
	Field Gravity#
	Field LifeTime%
	
	Field Disable%
	
	Field Room.Rooms
	
	Field soundCHN%
	
	Field Speed#, RandAngle#
	Field SizeChange#, Achange#
End Type 

Function UpdateEmitters()
	InSmoke = False
	For e.emitters = Each Emitters
		If timing\tickDuration > 0 And (mainPlayer\currRoom = e\room Or e\room\dist < 8) Then
			;If EntityDistance(mainPlayer\cam, e\Obj) < 6.0 Then
				Local p.Particles = CreateParticle(EntityX(e\obj, True), EntityY(e\obj, True), EntityZ(e\obj, True), Rand(e\minimage, e\maximage), e\size, e\gravity, e\lifetime)
				p\speed = e\speed
				RotateEntity(p\pvt, EntityPitch(e\Obj, True), EntityYaw(e\Obj, True), EntityRoll(e\Obj, True), True)
				TurnEntity(p\pvt, Rnd(-e\RandAngle, e\RandAngle), Rnd(-e\RandAngle, e\RandAngle), 0)
				
				TurnEntity p\obj, 0,0,Rnd(360)
				
				p\SizeChange = e\SizeChange
				
				p\Achange = e\achange
				
				e\soundCHN = LoopRangedSound_SM(sndManager\hiss, e\soundCHN, mainPlayer\cam, e\Obj)
				
				If InSmoke = False Then
					If IsPlayerWearingTempName(mainPlayer,"gasmask") And IsPlayerWearingTempName(mainPlayer,"hazmatsuit") Then
						Local dist# = Distance(EntityX(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), EntityX(e\obj, True), EntityZ(e\obj, True))
						If dist < 0.8 Then
							If Abs(EntityY(mainPlayer\cam, True)-EntityY(e\obj,True))<5.0 Then InSmoke = True
						EndIf
					EndIf					
				EndIf
			;EndIf
		End If
	Next
	
	If InSmoke Then
		If mainPlayer\blinkEffect > (70 * 6) Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (mainPlayer\blinkEffect - (70 * 6)) / (70.0 * 24.0))
		If mainPlayer\blinkEffect > (70 * 24) Then 
			DeathMSG = "Subject D-9341 found dead in [DATA REDACTED]. Cause of death: Suffocation due to decontamination gas."
			Kill(mainPlayer)
		EndIf
		
		If Not mainPlayer\dead Then 
			If Rand(150) = 1 Then
				;If Not IsChannelPlaying(CoughCHN) Then CoughCHN = PlaySound(CoughSFX(Rand(0, 2))) ;TODO: fix by not using a dim
			EndIf
		EndIf
		
		mainPlayer\blinkEffect=mainPlayer\blinkEffect+timing\tickDuration * 4
	EndIf	
End Function 

	
Function CreateEmitter.Emitters(x#, y#, z#, emittertype%) 
	Local e.Emitters = New Emitters
		
	e\Obj = CreatePivot()
	PositionEntity(e\Obj, x, y, z, True)
		
	Select emittertype
		Case 0 ;savu
			e\Size = 0.03
			e\Gravity = -0.2
			e\LifeTime = 200
			e\SizeChange = 0.005
			e\Speed = 0.004
			e\RandAngle = 20
			e\Achange = -0.008
		Case 1
			e\Size = 0.03
			e\Gravity = -0.2
			e\LifeTime = 200
			e\SizeChange = 0.008
			e\Speed = 0.004
			e\RandAngle = 40
			e\Achange = -0.01
			
			e\MinImage = 6 : e\MaxImage = 6
	End Select
	
	For r.Rooms = Each Rooms
		If Abs(EntityX(e\Obj) - EntityX(r\obj)) < 4.0 And Abs(EntityZ(e\Obj) - EntityZ(r\obj)) < 4.0 Then
			e\Room = r
		EndIf
	Next
	
	Return e
		
End Function
;~IDEal Editor Parameters:
;~F#4#10#4D#56
;~C#Blitz3D
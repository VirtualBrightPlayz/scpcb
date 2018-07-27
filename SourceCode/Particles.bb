Dim ParticleTextures%(10)

Type Particle
	Field obj%, pvt%
	Field image%

	Field r#, g#, b#, a#, size#
	Field speed#, yspeed#, gravity#
	Field rChange#, gChange#, bChange#, aChange#
	Field sizeChange#

	Field lifetime#
End Type

Function CreateParticle.Particle(x#, y#, z#, image%, size#, gravity# = 1.0, lifetime% = 200)
	Local p.Particle = New Particle
	p\lifetime = lifetime

	p\obj = CreateSprite()
	PositionEntity(p\obj, x, y, z, True)
	EntityTexture(p\obj, ParticleTextures(image))
	RotateEntity(p\obj, 0, 0, Rnd(360))
	EntityFX(p\obj, 1 + 8)

	SpriteViewMode(p\obj, 3)

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
	p\r = 255 : p\g = 255 : p\b = 255 : p\a = 1.0
	p\size = size
	ScaleSprite(p\obj, p\size, p\size)
	Return p
End Function

Function UpdateParticles()
	Local p.Particle
	For p = Each Particle
		MoveEntity(p\pvt, 0, 0, p\speed * timing\tickDuration)
		If (p\gravity <> 0) Then p\yspeed = p\yspeed - p\gravity * timing\tickDuration
		TranslateEntity(p\pvt, 0, p\yspeed * timing\tickDuration, 0, True)

		PositionEntity(p\obj, EntityX(p\pvt,True), EntityY(p\pvt,True), EntityZ(p\pvt,True), True)

		;TurnEntity(p\obj, 0, 0, timing\tickDuration)

		If (p\aChange <> 0) Then
			p\a=Min(Max(p\a+p\aChange * timing\tickDuration,0.0),1.0)
			EntityAlpha(p\obj, p\a)
		EndIf

		If (p\sizeChange <> 0) Then
			p\size= p\size+p\sizeChange * timing\tickDuration
			ScaleSprite(p\obj, p\size, p\size)
		EndIf

		ShowEntity(p\obj)
		EntityAlpha(p\obj,1.0)

		p\lifetime=p\lifetime-timing\tickDuration
		If (p\lifetime <= 0 Or p\size < 0.00001 Or p\a =< 0) Then
			RemoveParticle(p)
		EndIf
	Next
End Function

Function RemoveParticle(p.Particle)
	FreeEntity(p\obj)
	FreeEntity(p\pvt)
	Delete p
End Function

Global InSmoke%

Type Emitter
	Field obj%

	Field size#
	Field minImage%, maxImage%
	Field gravity#
	Field lifeTime%

	Field disable%

	Field room.Room

	Field soundCHN%

	Field speed#, randAngle#
	Field sizeChange#, aChange#
End Type

Function UpdateEmitters()
	InSmoke = False
	Local e.Emitter
	Local p.Particle
	Local dist#

	For e = Each Emitter
		If (timing\tickDuration > 0 And (mainPlayer\currRoom = e\room Or e\room\dist < 8)) Then
			;If (EntityDistance(mainPlayer\cam, e\obj) < 6.0) Then
			p = CreateParticle(EntityX(e\obj, True), EntityY(e\obj, True), EntityZ(e\obj, True), Rand(e\minImage, e\maxImage), e\size, e\gravity, e\lifeTime)
			p\speed = e\speed
			RotateEntity(p\pvt, EntityPitch(e\obj, True), EntityYaw(e\obj, True), EntityRoll(e\obj, True), True)
			TurnEntity(p\pvt, Rnd(-e\randAngle, e\randAngle), Rnd(-e\randAngle, e\randAngle), 0)

			TurnEntity(p\obj, 0,0,Rnd(360))

			p\sizeChange = e\sizeChange

			p\aChange = e\aChange

			e\soundCHN = LoopRangedSound_SM(sndManager\hiss, e\soundCHN, mainPlayer\cam, e\obj)

			If (InSmoke = False) Then
				If (IsPlayerWearingItem(mainPlayer,"gasmask") And IsPlayerWearingItem(mainPlayer,"hazmatsuit")) Then
					dist = Distance(EntityX(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), EntityX(e\obj, True), EntityZ(e\obj, True))
					If (dist < 0.8) Then
						If (Abs(EntityY(mainPlayer\cam, True)-EntityY(e\obj,True))<5.0) Then InSmoke = True
					EndIf
				EndIf
			EndIf
			;EndIf
		EndIf
	Next

	If (InSmoke) Then
		If (mainPlayer\blinkEffect > (70 * 6)) Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (mainPlayer\blinkEffect - (70 * 6)) / (70.0 * 24.0))
		If (mainPlayer\blinkEffect > (70 * 24)) Then
			DeathMSG = "Subject D-9341 found dead in [DATA REDACTED]. Cause of death: Suffocation due to decontamination gas."
			Kill(mainPlayer)
		EndIf

		If (Not mainPlayer\dead) Then
			If (Rand(150) = 1) Then
				;If (Not IsChannelPlaying(CoughCHN)) Then CoughCHN = PlaySound2(CoughSFX(Rand(0, 2))) ;TODO: fix by not using a dim
			EndIf
		EndIf

		mainPlayer\blinkEffect=mainPlayer\blinkEffect+timing\tickDuration * 4
	EndIf
End Function


Function CreateEmitter.Emitter(x#, y#, z#, emittertype%)
	Local e.Emitter = New Emitter

	e\obj = CreatePivot()
	PositionEntity(e\obj, x, y, z, True)

	Select emittertype
		Case 0 ;savu
			e\size = 0.03
			e\gravity = -0.2
			e\lifeTime = 200
			e\sizeChange = 0.005
			e\speed = 0.004
			e\randAngle = 20
			e\aChange = -0.008
		Case 1
			e\size = 0.03
			e\gravity = -0.2
			e\lifeTime = 200
			e\sizeChange = 0.008
			e\speed = 0.004
			e\randAngle = 40
			e\aChange = -0.01

			e\minImage = 6 : e\maxImage = 6
	End Select

	Local r.Room
	For r = Each Room
		If (Abs(EntityX(e\obj) - EntityX(r\obj)) < 4.0 And Abs(EntityZ(e\obj) - EntityZ(r\obj)) < 4.0) Then
			e\room = r
		EndIf
	Next

	Return e

End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
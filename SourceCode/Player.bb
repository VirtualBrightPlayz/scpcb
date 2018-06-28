Const WORNITEM_SLOT_COUNT% = 3
Const WORNITEM_SLOT_NONE%  = WORNITEM_SLOT_COUNT
Const WORNITEM_SLOT_HEAD%  = 0
Const WORNITEM_SLOT_BODY%  = 1
Const WORNITEM_SLOT_HAND%  = 2

Const PLAYER_INV_COUNT% = 6

Const OVERLAY_COUNT%       = 6
Const OVERLAY_BLACK%       = 0
Const OVERLAY_WHITE%       = 1
Const OVERLAY_FOG%         = 2
Const OVERLAY_GASMASK%     = 3
Const OVERLAY_NIGHTVISION% = 4
Const OVERLAY_008%         = 5

Type Player
	;entities
	Field collider%

	Field head%
	Field headPitch#
	Field headYaw#

	Field cam%
	Field camShake#
	Field camAnimState#
	Field camZoom#

	Field camFogNear#
	Field camFogFar#

	Field overlays%[OVERLAY_COUNT]

	Field grabbedEntity%

	Field closestItem.Item
	Field selectedItem.Item

	Field closestButton%
	Field closestDoor.Door
	Field selectedDoor.Door
	;------------

	;movement states
	Field crouching%
	Field crouchState#

	Field moveSpeed#
	Field dropSpeed#

	Field blinkFreq#
	Field blinkTimer#
	Field stamina#

	Field footstepOverride%
	;------------

	;ailments
	Field dead%
	Field fallTimer#

	Field injuries#
	Field bloodloss#

	Field heartbeatIntensity#

	Field superMan#

	Field infect008#

	Field sanity895#

	Field forceMove#
	Field forceAngle#

	Field disableControls%

	Field blinkEffect#
	Field blinkEffectTimer#
	Field staminaEffect#
	Field staminaEffectTimer#

	Field lightFlash#
	Field blurTimer#

	Field loudness#
	;------

	;items
	Field inventory.Inventory
	Field openInventory.Inventory
	;------

	;sounds
	Field breathingSFX.IntArray
	Field heartbeat%
	Field bloodDrip%[4]

	Field breathChn%
	;------

	;other states
	Field currRoom.Room

	Field godMode%
	Field noclip%
	;------
End Type
Global mainPlayer.Player = Null

Function CreatePlayer.Player()
	Local player.Player = New Player

	player\inventory = CreateInventory(PLAYER_INV_COUNT + WORNITEM_SLOT_COUNT)

	player\cam = CreateCamera()
	CameraViewport(player\cam, 0, 0, userOptions\screenWidth, userOptions\screenHeight)
	CameraRange(player\cam, 0.05, 60)
	CameraFogMode(player\cam, 1)
	CameraFogRange(player\cam, 0.05, 60) ;TODO: use constants
	;TODO: Change tint based on zone?
	CameraFogColor(player\cam, 0, 0, 0)

	Local fogTexture% = LoadTexture("GFX/Overlays/fog.jpg", 1)
	;FogNVTexture = LoadTexture("GFX/fogNV.jpg", 1)

	Local scaleWidth# = userOptions\screenWidth / 1024.0
	Local scaleHeight# = MenuScale * 0.8

	;TODO: take ownership of ark_blur_cam
	Local gasMaskTexture% = LoadTexture("GFX/Overlays/GasmaskOverlay.jpg", 1)
	player\overlays[OVERLAY_GASMASK] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_GASMASK], gasMaskTexture)
	EntityBlend(player\overlays[OVERLAY_GASMASK], 2)
	EntityFX(player\overlays[OVERLAY_GASMASK], 1)
	EntityOrder(player\overlays[OVERLAY_GASMASK], -1003)
	MoveEntity(player\overlays[OVERLAY_GASMASK], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_GASMASK])

	Local infectTexture% = LoadTexture("GFX/Overlays/InfectOverlay.jpg", 1)
	player\overlays[OVERLAY_008] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_008], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_008], infectTexture)
	EntityBlend(player\overlays[OVERLAY_008], 3)
	EntityFX(player\overlays[OVERLAY_008], 1)
	EntityOrder(player\overlays[OVERLAY_008], -1003)
	MoveEntity(player\overlays[OVERLAY_008], 0, 0, 1.0)
	;EntityAlpha(InfectOverlay, 255.0)
	HideEntity(player\overlays[OVERLAY_008])

	Local nvTexture% = LoadTexture("GFX/Overlays/NightVisionOverlay.jpg", 1)
	player\overlays[OVERLAY_NIGHTVISION] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_NIGHTVISION], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_NIGHTVISION], nvTexture)
	EntityBlend(player\overlays[OVERLAY_NIGHTVISION], 2)
	EntityFX(player\overlays[OVERLAY_NIGHTVISION], 1)
	EntityOrder(player\overlays[OVERLAY_NIGHTVISION], -1003)
	MoveEntity(player\overlays[OVERLAY_NIGHTVISION], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_NIGHTVISION])

	;TODO: do we need this?
	;NVBlink = CreateSprite(ark_blur_cam)
	;ScaleSprite(NVBlink, Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	;EntityColor(NVBlink,0,0,0)
	;EntityFX(NVBlink, 1)
	;EntityOrder(NVBlink, -1005)
	;MoveEntity(NVBlink, 0, 0, 1.0)
	;HideEntity(NVBlink)

	Local darkTexture% = CreateTexture(1024, 1024, 1 + 2)
	SetBuffer(TextureBuffer(darkTexture))
	Cls()
	SetBuffer(BackBuffer())

	scaleWidth = userOptions\screenWidth / 1240.0
	scaleHeight = userOptions\screenHeight / 960.0 * 0.8

	player\overlays[OVERLAY_FOG] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_FOG], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_FOG], fogTexture)
	EntityBlend(player\overlays[OVERLAY_FOG], 2)
	EntityOrder(player\overlays[OVERLAY_FOG], -1000)
	MoveEntity(player\overlays[OVERLAY_FOG], 0, 0, 1.0)

	player\overlays[OVERLAY_BLACK] = CreateSprite(player\cam)
	ScaleSprite(player\overlays[OVERLAY_BLACK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_BLACK], darkTexture)
	EntityBlend(player\overlays[OVERLAY_BLACK], 1)
	EntityOrder(player\overlays[OVERLAY_BLACK], -1002)
	MoveEntity(player\overlays[OVERLAY_BLACK], 0, 0, 1.0)
	EntityAlpha(player\overlays[OVERLAY_BLACK], 0.0)

	Local lightTexture% = CreateTexture(1024, 1024, 1 + 2)
	SetBuffer(TextureBuffer(lightTexture))
	ClsColor(255, 255, 255)
	Cls()
	ClsColor(0, 0, 0)
	SetBuffer(BackBuffer())

	player\overlays[OVERLAY_WHITE] = CreateSprite(player\cam)
	ScaleSprite(player\overlays[OVERLAY_WHITE], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_WHITE], lightTexture)
	EntityBlend(player\overlays[OVERLAY_WHITE], 1)
	EntityOrder(player\overlays[OVERLAY_WHITE], -1002)
	MoveEntity(player\overlays[OVERLAY_WHITE], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_WHITE])

	FreeTexture(gasMaskTexture)
	FreeTexture(infectTexture)
	FreeTexture(nvTexture)
	FreeTexture(fogTexture)
	FreeTexture(darkTexture)
	FreeTexture(lightTexture)

	player\collider = CreatePivot()
	EntityRadius(player\collider, 0.15, 0.30)
	EntityPickMode(player\collider, 1)
	EntityType(player\collider, HIT_PLAYER)

	player\head = CreatePivot()
	EntityRadius(player\head, 0.15)
	EntityType(player\head, HIT_PLAYER)

	;Sounds
	player\breathingSFX = CreateIntArray(2, 5)

	Local i%
	For i = 0 To 4
		SetIntArrayElem(player\breathingSFX, LoadSound("SFX/Character/D9341/breath"+Str(i)+".ogg"), 0, i)
		SetIntArrayElem(player\breathingSFX, LoadSound("SFX/Character/D9341/breath"+Str(i)+"gas.ogg"), 1, i)
	Next
	For i = 0 To 3
		player\bloodDrip[i] = LoadSound("SFX/Character/D9341/BloodDrip" + Str(i) + ".ogg")
	Next
	player\heartbeat = LoadSound("SFX/Character/D9341/Heartbeat.ogg")

	Return player
End Function

Function DeletePlayer(player.Player)
	;TODO: delete/drop worn items, delete inventory
	DeleteInventory(player\inventory)

	Local i%
	For i = 0 To OVERLAY_COUNT-1
		FreeEntity(player\overlays[i])
	Next

	For i = 0 To 4
		FreeSound(GetIntArrayElem(player\breathingSFX, 0, i))
		FreeSound(GetIntArrayElem(player\breathingSFX, 1, i))
	Next
	For i = 0 To 3
		FreeSound(player\bloodDrip[i])
	Next
	FreeSound(player\heartbeat)

	Delete player
End Function

Function UpdatePlayer()
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#, temp#, tempchn%, collidedFloor%, pvt%, de.Decal

	If (mainPlayer\superMan>0.0) Then
		Speed = Speed * 3

		mainPlayer\superMan=mainPlayer\superMan+timing\tickDuration

		mainPlayer\camShake = Sin(mainPlayer\superMan / 5.0) * (mainPlayer\superMan / 1500.0)

		If (mainPlayer\superMan > 70 * 50) Then
			DeathMSG = "A Class D jumpsuit found in [DATA REDACTED]. Upon further examination, the jumpsuit was found to be filled with 12.5 kilograms of blue ash-like substance. "
			DeathMSG = DeathMSG + "Chemical analysis of the substance remains non-conclusive. Most likely related to SCP-914."
			Kill(mainPlayer)
			ShowEntity(mainPlayer\overlays[OVERLAY_FOG])
		Else
			mainPlayer\blurTimer = 500
			HideEntity(mainPlayer\overlays[OVERLAY_FOG])
		EndIf
	EndIf

	;If (DeathTimer > 0) Then
	;	DeathTimer=DeathTimer-timing\tickDuration
	;	If (DeathTimer < 1) Then DeathTimer = -1.0
	;ElseIf DeathTimer < 0
	;	Kill(mainPlayer)
	;EndIf

	mainPlayer\stamina = Min(mainPlayer\stamina + 0.15 * timing\tickDuration, 100.0)

	If (mainPlayer\staminaEffectTimer > 0) Then
		mainPlayer\staminaEffect = mainPlayer\staminaEffect - (timing\tickDuration/70)
	Else
		mainPlayer\staminaEffect = 1.0
	EndIf

	If (mainPlayer\currRoom<>Null) Then
		If (mainPlayer\currRoom\roomTemplate\name <> "pocketdimension") Then
			If (KeyDown(keyBinds\sprint)) Then
				If (mainPlayer\stamina < 5) Then ;out of breath
					If (Not IsChannelPlaying(mainPlayer\breathChn)) Then mainPlayer\breathChn = PlaySound(GetIntArrayElem(mainPlayer\breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), 0))
				ElseIf (mainPlayer\stamina < 50) Then ;panting
					If (mainPlayer\breathChn = 0) Then
						mainPlayer\breathChn = PlaySound(GetIntArrayElem(mainPlayer\breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), Rand(1, 3)))
						ChannelVolume(mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\sndVolume)
					Else
						If (Not IsChannelPlaying(mainPlayer\breathChn)) Then
							mainPlayer\breathChn = PlaySound(GetIntArrayElem(mainPlayer\breathingSFX, IsPlayerWearingItem(mainPlayer,"gasmask"), Rand(1, 3)))
							ChannelVolume(mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\sndVolume)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf

	For i=0 To mainPlayer\inventory\size-1
		If (mainPlayer\inventory\items[i]<>Null) Then
			If (mainPlayer\inventory\items[i]\template\name="finevest") Then mainPlayer\stamina = Min(mainPlayer\stamina,60.0)
		EndIf
	Next

	;If (IsZombie) Then Crouch = False

	If (Abs(mainPlayer\crouchState-mainPlayer\crouching)<0.001) Then
		mainPlayer\crouchState = mainPlayer\crouching
	Else
		mainPlayer\crouchState = CurveValue(mainPlayer\crouching, mainPlayer\crouchState, 10.0)
	EndIf

	If (Not mainPlayer\noclip) Then
		If (((KeyDown(keyBinds\down) Xor KeyDown(keyBinds\up)) Or (KeyDown(keyBinds\rght) Xor KeyDown(keyBinds\lft)) And (Not mainPlayer\disableControls)) Or mainPlayer\forceMove>0) Then

			If (mainPlayer\crouching = 0 And (KeyDown(keyBinds\sprint)) And mainPlayer\stamina > 0.0) Then; And (Not IsZombie)) Then
				Sprint = 2.5
				mainPlayer\stamina = mainPlayer\stamina - timing\tickDuration * 0.5 * (1.0/mainPlayer\staminaEffect)
				If (mainPlayer\stamina <= 0) Then mainPlayer\stamina = -20.0
			EndIf

			If (mainPlayer\currRoom\roomTemplate\name = "pocketdimension") Then
				If (EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale) Then
					mainPlayer\stamina = 0
					Speed = 0.015
					Sprint = 1.0
				EndIf
			EndIf

			If (mainPlayer\forceMove>0) Then Speed=Speed*mainPlayer\forceMove

			If (mainPlayer\selectedItem<>Null) Then
				If (mainPlayer\selectedItem\template\name = "firstaid" Or mainPlayer\selectedItem\template\name = "finefirstaid" Or mainPlayer\selectedItem\template\name = "firstaid2") Then
					Sprint = 0
				EndIf
			EndIf

			temp = (mainPlayer\camAnimState Mod 360)
			If (Not mainPlayer\disableControls) Then mainPlayer\camAnimState = (mainPlayer\camAnimState + timing\tickDuration * Min(Sprint, 1.5) * 7) Mod 720
			If (temp < 180 And (mainPlayer\camAnimState Mod 360) >= 180 And (Not mainPlayer\dead)) Then
				;TODO: define constants for each override state
				If (mainPlayer\footstepOverride=0) Then
					temp = GetMaterialStepSound(mainPlayer\collider)

					If (Sprint = 1.0) Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)

						If (temp = STEPSOUND_METAL) Then
							tempchn = PlaySound_SM(sndManager\footstepMetal[Rand(0, 7)])
						Else
							tempchn = PlaySound_SM(sndManager\footstep[Rand(0, 7)])
						EndIf

						ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\sndVolume)
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)

						If (temp = 1) Then
							tempchn = PlaySound_SM(sndManager\footstepMetalRun[Rand(0, 7)])
						Else
							tempchn = PlaySound_SM(sndManager\footstepRun[Rand(0, 7)])
						EndIf

						ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\sndVolume)
					EndIf
				ElseIf (mainPlayer\footstepOverride=1) Then
					tempchn = PlaySound_SM(sndManager\footstepPD[Rand(0, 2)])
					ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\sndVolume)
				ElseIf (mainPlayer\footstepOverride=2) Then
					tempchn = PlaySound_SM(sndManager\footstep8601[Rand(0, 2)])
					ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\sndVolume)
				ElseIf (mainPlayer\footstepOverride=3) Then
					If (Sprint = 1.0) Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)
						tempchn = PlaySound_SM(sndManager\footstep[Rand(0, 7)])
						ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\sndVolume)
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)
						tempchn = PlaySound_SM(sndManager\footstepRun[Rand(0, 7)])
						ChannelVolume(tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\sndVolume)
					EndIf
				EndIf

			EndIf
		EndIf
	Else ;noclip on
		If (KeyDown(keyBinds\sprint)) Then
			Sprint = 2.5
		ElseIf (KeyDown(keyBinds\crouch)) Then
			Sprint = 0.5
		EndIf
	EndIf

	If (KeyHit(keyBinds\crouch) And (Not mainPlayer\disableControls)) Then mainPlayer\crouching = (Not mainPlayer\crouching)

	Local temp2# = (Speed * Sprint) / (1.0+mainPlayer\crouchState)

	If (mainPlayer\noclip) Then
		mainPlayer\camAnimState = 0
		mainPlayer\moveSpeed = 0
		mainPlayer\crouchState = 0
		mainPlayer\crouching = 0

		RotateEntity(mainPlayer\collider, WrapAngle(EntityPitch(mainPlayer\cam)), WrapAngle(EntityYaw(mainPlayer\cam)), 0)

		temp2 = temp2; * NoClipSpeed ;TODO: reimplement

		If (KeyDown(keyBinds\down)) Then MoveEntity(mainPlayer\collider, 0, 0, -temp2*timing\tickDuration)
		If (KeyDown(keyBinds\up)) Then MoveEntity(mainPlayer\collider, 0, 0, temp2*timing\tickDuration)

		If (KeyDown(keyBinds\lft)) Then MoveEntity(mainPlayer\collider, -temp2*timing\tickDuration, 0, 0)
		If (KeyDown(keyBinds\rght)) Then MoveEntity(mainPlayer\collider, temp2*timing\tickDuration, 0, 0)

		ResetEntity(mainPlayer\collider)
	Else
		temp2 = temp2 / Max((mainPlayer\injuries+3.0)/3.0,1.0)
		If (mainPlayer\injuries > 0.5) Then
			temp2 = temp2*Min((Sin(mainPlayer\camAnimState/2)+1.2),1.0)
		EndIf

		temp = False
		;If (Not IsZombie%)
		If (KeyDown(keyBinds\down) And (Not mainPlayer\disableControls)) Then
			temp = True
			angle = 180
			If (KeyDown(keyBinds\lft)) Then angle = 135
			If (KeyDown(keyBinds\rght)) Then angle = -135
		ElseIf (KeyDown(keyBinds\up) And (Not mainPlayer\disableControls)) Then; Or ForceMove>0
			temp = True
			angle = 0
			If (KeyDown(keyBinds\lft)) Then angle = 45
			If (KeyDown(keyBinds\rght)) Then angle = -45
		ElseIf (mainPlayer\forceMove>0) Then
			temp=True
			angle = mainPlayer\forceAngle
		ElseIf (Not mainPlayer\disableControls) Then
			If (KeyDown(keyBinds\lft)) Then
				angle = 90 : temp = True
			EndIf
			If (KeyDown(keyBinds\rght)) Then
				angle = -90 : temp = True
			EndIf
		EndIf
		;Else
		;	temp=True
		;	angle = ForceAngle
		;EndIf

		angle = WrapAngle(EntityYaw(mainPlayer\collider,True)+angle+90.0)

		If (Int(temp)) Then
			mainPlayer\moveSpeed = CurveValue(temp2, mainPlayer\moveSpeed, 20.0)
		Else
			mainPlayer\moveSpeed = Max(CurveValue(0.0, mainPlayer\moveSpeed-0.1, 1.0),0.0)
		EndIf

		If (Not mainPlayer\disableControls) Then TranslateEntity(mainPlayer\collider, Cos(angle)*mainPlayer\moveSpeed * timing\tickDuration, 0, Sin(angle)*mainPlayer\moveSpeed * timing\tickDuration, True)

		collidedFloor = False
		For i = 1 To CountCollisions(mainPlayer\collider)
			If (CollisionY(mainPlayer\collider, i) < EntityY(mainPlayer\collider,True)) And (Abs(CollisionNY(mainPlayer\collider, i))>0.8) Then
				collidedFloor = True
			EndIf
		Next

		If (collidedFloor = True) Then
			If (mainPlayer\dropSpeed# < - 0.07) Then
				If (mainPlayer\footstepOverride=0) Then
					If (GetMaterialStepSound(mainPlayer\collider) = 1) Then
						PlaySound_SM(sndManager\footstepMetal[Rand(0, 7)])
					Else
						PlaySound_SM(sndManager\footstep[Rand(0, 7)])
					EndIf
				ElseIf (mainPlayer\footstepOverride=1) Then
					PlaySound_SM(sndManager\footstepPD[Rand(0, 2)])
				ElseIf (mainPlayer\footstepOverride=2) Then
					PlaySound_SM(sndManager\footstep8601[Rand(0, 2)])
				Else
					PlaySound_SM(sndManager\footstep[Rand(0, 7)])
				EndIf
				mainPlayer\loudness = Max(3.0,mainPlayer\loudness)
			EndIf
			mainPlayer\dropSpeed = 0
		Else
			mainPlayer\dropSpeed = Min(Max(mainPlayer\dropSpeed - 0.006 * timing\tickDuration, -2.0), 0.0)
		EndIf

		If (Not mainPlayer\disableControls) Then TranslateEntity(mainPlayer\collider, 0, mainPlayer\dropSpeed * timing\tickDuration, 0)
	EndIf

	mainPlayer\forceMove = 0.0

	If (mainPlayer\injuries > 1.0) Then
		temp2 = mainPlayer\bloodloss
		mainPlayer\blurTimer = Max(Max(Sin(TimeInPosMilliSecs()/100.0)*mainPlayer\bloodloss*30.0,mainPlayer\bloodloss*2*(2.0-mainPlayer\crouchState)),mainPlayer\blurTimer)
		mainPlayer\bloodloss = Min(mainPlayer\bloodloss + (Min(mainPlayer\injuries,3.5)/300.0)*timing\tickDuration,100)

		If (temp2 <= 60 And mainPlayer\bloodloss > 60) Then
			Msg = "You are feeling faint from the amount of blood you loss."
			MsgTimer = 70*4
		EndIf
	EndIf

	UpdateInfect()

	If (mainPlayer\bloodloss > 0) Then
		If (Rnd(200)<Min(mainPlayer\injuries,4.0)) Then
			pvt = CreatePivot()
			PositionEntity(pvt, EntityX(mainPlayer\collider)+Rnd(-0.05,0.05),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)+Rnd(-0.05,0.05))
			TurnEntity(pvt, 90, 0, 0)
			EntityPick(pvt,0.3)
			de = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\size = Rnd(0.03,0.08)*Min(mainPlayer\injuries,3.0) : EntityAlpha(de\obj, 1.0) : ScaleSprite(de\obj, de\size, de\size)
			tempchn = PlaySound2(mainPlayer\bloodDrip[Rand(0,3)])
			ChannelVolume(tempchn, Rnd(0.0,0.8)*userOptions\sndVolume)
			ChannelPitch(tempchn, Rand(20000,30000))

			FreeEntity(pvt)
		EndIf

		mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0)*mainPlayer\bloodloss*0.2)

		If (mainPlayer\bloodloss > 60) Then mainPlayer\crouching = True
		If (mainPlayer\bloodloss => 100) Then
			Kill(mainPlayer)
			mainPlayer\heartbeatIntensity = 0.0
		ElseIf (mainPlayer\bloodloss > 80.0) Then
			mainPlayer\heartbeatIntensity = Max(150-(mainPlayer\bloodloss-80)*5,mainPlayer\heartbeatIntensity)
		ElseIf (mainPlayer\bloodloss > 35.0) Then
			mainPlayer\heartbeatIntensity = Max(70+mainPlayer\bloodloss,mainPlayer\heartbeatIntensity)
		EndIf
	EndIf

	If (Not mainPlayer\disableControls) Then
		If (KeyHit(keyBinds\blink)) Then mainPlayer\blinkTimer = 0
		If (KeyDown(keyBinds\blink) And mainPlayer\blinkTimer < - 10) Then mainPlayer\blinkTimer = -10
	EndIf


	If (mainPlayer\heartbeatIntensity > 0) Then
		tempchn = PlaySound2(mainPlayer\heartbeat)
		ChannelVolume(tempchn, Max(Min((mainPlayer\heartbeatIntensity-80.0)/60.0,1.0),0.0)*userOptions\sndVolume)

		mainPlayer\heartbeatIntensity = mainPlayer\heartbeatIntensity - timing\tickDuration
	EndIf

	; Equipped items.
	If (IsPlayerWearingItem(mainPlayer, "gasmask")) Then
		ShowEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	Else ;TODO: Add ElseIfs here for hazmat and nvgoggles.
		HideEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	EndIf

End Function

;TODO: these variable names are awful
;Also scrap the mouselook speed in favor of just interpolating to a final position
; - -Viewport.
Global viewport_center_x%
Global viewport_center_y%

; -- Mouselook.
Global mouselook_x_inc# = 0.3 ; This sets both the sensitivity and direction (+/-) of the mouse on the X axis.
Global mouselook_y_inc# = 0.3 ; This sets both the sensitivity and direction (+/-) of the mouse on the Y axis.
; Used to limit the mouse movement to within a certain number of pixels (250 is used here) from the center of the screen. This produces smoother mouse movement than continuously moving the mouse back to the center each loop.
Global mouse_left_limit%
Global mouse_right_limit%
Global mouse_top_limit%
Global mouse_bottom_limit%
Global mouse_x_speed_1#, mouse_y_speed_1#

Function MouseLook()
	Local i%, up#, roll#, the_yaw#, the_pitch#, collidedFloor%, pvt%, p.Particle

	mainPlayer\camShake = Max(mainPlayer\camShake - (timing\tickDuration / 10), 0)

	;CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.0)
	CameraZoom(mainPlayer\cam, Min(1.0+(mainPlayer\camZoom/400.0),1.1))
	mainPlayer\camZoom = Max(mainPlayer\camZoom - timing\tickDuration, 0)

	Local Nan1# = NAN

	If (mainPlayer\fallTimer >=0) Then

		;HeadDropSpeed = 0

		up = (Sin(mainPlayer\camAnimState) / (20.0+mainPlayer\crouchState*20.0))*0.6
		roll = Max(Min(Sin(mainPlayer\camAnimState*0.5)*2.5*Min(mainPlayer\injuries+0.25,3.0),8.0),-8.0)

		;tilt the camera to the side if the player is injured
		;RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), Max(Min(up*30*mainPlayer\injuries,50),-50))
		PositionEntity(mainPlayer\cam, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider))
		RotateEntity(mainPlayer\cam, 0, EntityYaw(mainPlayer\collider), roll*0.5)

		MoveEntity(mainPlayer\cam, 0.0, up + 0.6 + mainPlayer\crouchState * -0.3, 0)

		;RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), 0)
		;moveentity player, side, up, 0
		; -- Update the smoothing que To smooth the movement of the mouse.
		mouse_x_speed_1 = CurveValue(MouseXSpeed() * (userOptions\mouseSensitivity + 0.6) , mouse_x_speed_1, 6.0 / (userOptions\mouseSensitivity + 1.0))
		If (Int(mouse_x_speed_1) = Int(Nan1)) Then mouse_x_speed_1 = 0

		If (userOptions\invertMouseY) Then
			mouse_y_speed_1 = CurveValue(-MouseYSpeed() * (userOptions\mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions\mouseSensitivity+1.0))
		Else
			mouse_y_speed_1 = CurveValue(MouseYSpeed() * (userOptions\mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions\mouseSensitivity+1.0))
		EndIf
		If (Int(mouse_y_speed_1) = Int(Nan1)) Then mouse_y_speed_1 = 0

		;TODO: CHANGE THESE NAMES
		the_yaw = ((mouse_x_speed_1)) * mouselook_x_inc / (1.0+IsPlayerWearingItem(mainPlayer,"vest"))
		the_pitch = ((mouse_y_speed_1)) * mouselook_y_inc / (1.0+IsPlayerWearingItem(mainPlayer,"vest"))

		TurnEntity(mainPlayer\collider, 0.0, -the_yaw, 0.0) ; Turn the user on the Y (yaw) axis.)
		mainPlayer\headPitch = mainPlayer\headPitch + the_pitch
		; -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
		If (mainPlayer\headPitch > 70.0) Then mainPlayer\headPitch = 70.0
		If (mainPlayer\headPitch < - 70.0) Then mainPlayer\headPitch = -70.0

		RotateEntity(mainPlayer\cam, WrapAngle(mainPlayer\headPitch + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), WrapAngle(EntityYaw(mainPlayer\collider) + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), roll) ; Pitch the user;s camera up And down.)

		If (mainPlayer\currRoom\roomTemplate\name = "pocketdimension") Then
			If (EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale) Then
				RotateEntity(mainPlayer\cam, WrapAngle(EntityPitch(mainPlayer\cam)),WrapAngle(EntityYaw(mainPlayer\cam)), roll+WrapAngle(Sin(TimeInPosMilliSecs()/150.0)*30.0)) ; Pitch the user;s camera up And down.)
			EndIf
		EndIf

	Else
		HideEntity(mainPlayer\collider)
		PositionEntity(mainPlayer\cam, EntityX(mainPlayer\head), EntityY(mainPlayer\head), EntityZ(mainPlayer\head))

		collidedFloor = False
		For i = 1 To CountCollisions(mainPlayer\head)
			If (CollisionY(mainPlayer\head, i) < EntityY(mainPlayer\head) - 0.01) Then collidedFloor = True
		Next

		If (collidedFloor = True) Then
			;HeadDropSpeed# = 0
		Else

			;TODO: reimplement head falling
			;If (KillAnim = 0) Then
			;	MoveEntity(Head, 0, 0, HeadDropSpeed)
			;	RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
			;	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
			;Else
			;	MoveEntity(Head, 0, 0, -HeadDropSpeed)
			;	RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
			;	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
			;EndIf

			;HeadDropSpeed# = HeadDropSpeed - 0.002 * timing\tickDuration
		EndIf

		If (userOptions\invertMouseY) Then
			TurnEntity(mainPlayer\cam, -MouseYSpeed() * 0.05 * timing\tickDuration, -MouseXSpeed() * 0.15 * timing\tickDuration, 0)
		Else
			TurnEntity(mainPlayer\cam, MouseYSpeed() * 0.05 * timing\tickDuration, -MouseXSpeed() * 0.15 * timing\tickDuration, 0)
		EndIf

	EndIf

	;DUST PARTICLES
	If (Rand(35) = 1) Then
		pvt = CreatePivot()
		PositionEntity(pvt, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True))
		RotateEntity(pvt, 0, Rnd(360), 0)
		If (Rand(2) = 1) Then
			MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
		Else
			MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
		EndIf

		p = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
		p\speed = 0.001
		RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)

		p\sizeChange = -0.00001

		FreeEntity(pvt)
	EndIf

	; -- Limit the mouse;s movement. Using this method produces smoother mouselook movement than centering the mouse Each loop.
	If (MouseX() > mouse_right_limit) Or (MouseX() < mouse_left_limit) Or (MouseY() > mouse_bottom_limit) Or (MouseY() < mouse_top_limit) Then
		MoveMouse(viewport_center_x, viewport_center_y)
	EndIf

	;TODO: Move this to MovePlayer().
	;If (wearingGasMask Or wearingHazmat Or wearing1499) Then
	;	If (wearingGasMask = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
	;	If (wearing1499 = 2) Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
	;	If (wearingHazmat = 2) Then
	;		mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
	;	ElseIf (wearingHazmat=1) Then
	;		mainPlayer\stamina = Min(60, mainPlayer\stamina)
	;	EndIf
	;
	;	ShowEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	;Else
	;	HideEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	;EndIf

	;If (Not wearingNightVision=0) Then
	;	ShowEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
	;	If (wearingNightVision=2) Then
	;		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
	;		AmbientLightRooms(15)
	;	ElseIf (wearingNightVision=3) Then
	;		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,0,0)
	;		AmbientLightRooms(15)
	;	Else
	;		EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,255,0)
	;		AmbientLightRooms(15)
	;	EndIf
	;	;EntityTexture(Fog, FogNVTexture)
	;Else
	;	AmbientLightRooms(0)
	;	HideEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
	;	;EntityTexture(Fog, FogTexture)
	;EndIf
End Function

Function SpaceInInventory%(player.Player)
	Local itemCount%
	Local i%
	For i=WORNITEM_SLOT_COUNT To player\inventory\size-1
		If (player\inventory\items[i] <> Null) Then itemCount = itemCount + 1
	Next

	If (itemCount < player\inventory\size-WORNITEM_SLOT_COUNT) Then
		Return True
	EndIf

	Return False
End Function

Function Kill(player.Player)
	If (player\godMode) Then Return

	If (player\breathChn <> 0) Then
		If (IsChannelPlaying(player\breathChn)) Then StopChannel(player\breathChn)
	EndIf

	If (Not player\dead) Then
		;KillAnim = Rand(0,1)
		PlaySound2(DamageSFX(0))
		If (SelectedDifficulty\permaDeath) Then
			DeleteFile(CurrentDir() + SavePath + CurrSave+"/save.txt")
			DeleteDir(SavePath + CurrSave)
			LoadSaveGames()
		EndIf

		player\dead = True
		player\fallTimer = Min(-1,player\fallTimer)
		ShowEntity(player\head)
		PositionEntity(player\head, EntityX(player\cam, True), EntityY(player\cam, True), EntityZ(player\cam, True), True)
		ResetEntity(player\head)
		RotateEntity(player\head, 0, EntityYaw(player\cam), 0)
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
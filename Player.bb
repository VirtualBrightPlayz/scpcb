Const WORNITEM_SLOT_COUNT% = 2
Const WORNITEM_SLOT_NONE%  = WORNITEM_SLOT_COUNT
Const WORNITEM_SLOT_HEAD%  = 0
Const WORNITEM_SLOT_BODY%  = 1

Const OVERLAY_COUNT%       = 6
Const OVERLAY_BLACK%       = 0
Const OVERLAY_WHITE%       = 1
Const OVERLAY_FOG%         = 2
Const OVERLAY_GASMASK%     = 3
Const OVERLAY_NIGHTVISION% = 4
Const OVERLAY_008%         = 5

;TODO: remove after cleanup
Global FogTexture.MarkedForRemoval, Fog.MarkedForRemoval
Global GasMaskTexture.MarkedForRemoval, GasMaskOverlay.MarkedForRemoval
Global InfectTexture.MarkedForRemoval, InfectOverlay.MarkedForRemoval
Global DarkTexture.MarkedForRemoval, Dark.MarkedForRemoval

Global FogNVTexture.MarkedForRemoval
Global NVTexture.MarkedForRemoval, NVOverlay.MarkedForRemoval

Global LightTexture.MarkedForRemoval, Light.MarkedForRemoval

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
	
	Field closestItem.Items
	Field selectedItem.Items
	
	Field closestButton%
	Field closestDoor.Doors
	Field selectedDoor.Doors
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
	Field staminaEffect#
	
	Field lightFlash#
	Field blurTimer#
	
	Field loudness#
	;------
	
	;items
	Field inventory.Inventory
	Field openInventory.Inventory
	
	Field wornItems.Items[WORNITEM_SLOT_COUNT]
	;------
	
	;sounds
	Field breathingSFX.IntArray2D
	
	Field breathChn%
	;------
	
	;other states
	Field currRoom.Rooms
	
	Field godMode%
	Field noclip%
	;------
End Type
Global mainPlayer.Player = Null

Function CreatePlayer.Player()
	Local player.Player = New Player
	
	player\inventory = CreateInventory(10)
	
	player\cam = CreateCamera()
	CameraViewport(player\cam, 0, 0, userOptions\screenWidth, userOptions\screenHeight)
	CameraRange(player\cam, 0.05, 16)
	CameraFogMode(player\cam, 1)
	CameraFogRange(player\cam, 0.05, 6) ;TODO: use constants
	;TODO: Change tint based on zone?
	CameraFogColor(player\cam, 0, 0, 0)
	
	Local fogTexture% = LoadTexture("GFX\fog.jpg", 1)
	;FogNVTexture = LoadTexture("GFX\fogNV.jpg", 1)
	
	Local scaleWidth# = userOptions\screenWidth / 1024.0
	Local scaleHeight# = MenuScale * 0.8
	
	;TODO: take ownership of ark_blur_cam
	Local gasMaskTexture = LoadTexture("GFX\GasmaskOverlay.jpg", 1)
	player\overlays[OVERLAY_GASMASK] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_GASMASK], gasMaskTexture)
	EntityBlend(player\overlays[OVERLAY_GASMASK], 2)
	EntityFX(player\overlays[OVERLAY_GASMASK], 1)
	EntityOrder player\overlays[OVERLAY_GASMASK], -1003
	MoveEntity(player\overlays[OVERLAY_GASMASK], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_GASMASK])
	
	Local infectTexture = LoadTexture("GFX\InfectOverlay.jpg", 1)
	player\overlays[OVERLAY_008] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_008], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_008], infectTexture)
	EntityBlend (player\overlays[OVERLAY_008], 3)
	EntityFX(player\overlays[OVERLAY_008], 1)
	EntityOrder player\overlays[OVERLAY_008], -1003
	MoveEntity(player\overlays[OVERLAY_008], 0, 0, 1.0)
	;EntityAlpha (InfectOverlay, 255.0)
	HideEntity(player\overlays[OVERLAY_008])
	
	Local nvTexture = LoadTexture("GFX\NightVisionOverlay.jpg", 1)
	player\overlays[OVERLAY_NIGHTVISION] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_NIGHTVISION], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_NIGHTVISION], nvTexture)
	EntityBlend (player\overlays[OVERLAY_NIGHTVISION], 2)
	EntityFX(player\overlays[OVERLAY_NIGHTVISION], 1)
	EntityOrder player\overlays[OVERLAY_NIGHTVISION], -1003
	MoveEntity(player\overlays[OVERLAY_NIGHTVISION], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_NIGHTVISION])
	
	;TODO: do we need this?
	;NVBlink = CreateSprite(ark_blur_cam)
	;ScaleSprite(NVBlink, Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	;EntityColor(NVBlink,0,0,0)
	;EntityFX(NVBlink, 1)
	;EntityOrder NVBlink, -1005
	;MoveEntity(NVBlink, 0, 0, 1.0)
	;HideEntity(NVBlink)
	
	Local darkTexture = CreateTexture(1024, 1024, 1 + 2)
	SetBuffer TextureBuffer(darkTexture)
	Cls
	SetBuffer BackBuffer()

	scaleWidth = userOptions\screenWidth / 1240.0
	scaleHeight = userOptions\screenHeight / 960.0 * 0.8

	player\overlays[OVERLAY_FOG] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_FOG], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_FOG], fogTexture)
	EntityBlend (player\overlays[OVERLAY_FOG], 2)
	EntityOrder player\overlays[OVERLAY_FOG], -1000
	MoveEntity(player\overlays[OVERLAY_FOG], 0, 0, 1.0)

	player\overlays[OVERLAY_BLACK] = CreateSprite(player\cam)
	ScaleSprite(player\overlays[OVERLAY_BLACK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_BLACK], darkTexture)
	EntityBlend (player\overlays[OVERLAY_BLACK], 1)
	EntityOrder player\overlays[OVERLAY_BLACK], -1002
	MoveEntity(player\overlays[OVERLAY_BLACK], 0, 0, 1.0)
	EntityAlpha player\overlays[OVERLAY_BLACK], 0.0
	
	Local lightTexture = CreateTexture(1024, 1024, 1 + 2)
	SetBuffer TextureBuffer(lightTexture)
	ClsColor 255, 255, 255
	Cls
	ClsColor 0, 0, 0
	SetBuffer BackBuffer()
	
	player\overlays[OVERLAY_WHITE] = CreateSprite(player\cam)
	ScaleSprite(player\overlays[OVERLAY_WHITE], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_WHITE], lightTexture)
	EntityBlend (player\overlays[OVERLAY_WHITE], 1)
	EntityOrder player\overlays[OVERLAY_WHITE], -1002
	MoveEntity(player\overlays[OVERLAY_WHITE], 0, 0, 1.0)
	HideEntity player\overlays[OVERLAY_WHITE]
	
	player\collider = CreatePivot()
	EntityRadius player\collider, 0.15, 0.30
	EntityPickMode(player\collider, 1)
	EntityType player\collider, HIT_PLAYER
	
	player\head = CreatePivot()
	EntityRadius player\head, 0.15
	EntityType player\head, HIT_PLAYER
	
	;Sounds
	player\breathingSFX = CreateIntArray2D(2, 5)
	
	Local i%
	For i = 0 To 4
		SetIntArray2DElem(player\breathingSFX, 0, i, LoadSound("SFX\Character\D9341\breath"+i+".ogg"))
		SetIntArray2DElem(player\breathingSFX, 1, i, LoadSound("SFX\Character\D9341\breath"+i+"gas.ogg"))
	Next
	
	Return player
End Function

Function DeletePlayer(player.Player)
	;TODO: delete/drop worn items, delete inventory
	DeleteInventory(player\inventory)

	Local i%
	For i = 0 To 4
		FreeSound(GetIntArray2DElem(player\breathingSFX, 0, i))
		FreeSound(GetIntArray2DElem(player\breathingSFX, 1, i))
	Next

	Delete player
End Function

;TODO: remove these when we're absolutely sure we're done here
Global Collider.MarkedForRemoval, Head.MarkedForRemoval
Global Camera.MarkedForRemoval, CameraShake.MarkedForRemoval, CurrCameraZoom.MarkedForRemoval

Global DropSpeed.MarkedForRemoval, HeadDropSpeed.MarkedForRemoval, CurrSpeed.MarkedForRemoval
Global user_camera_pitch.MarkedForRemoval, side.MarkedForRemoval
Global Crouch.MarkedForRemoval, CrouchState.MarkedForRemoval

Global PlayerZone.MarkedForRemoval;, mainPlayer\currRoom.Rooms

Global GrabbedEntity.MarkedForRemoval

Global Shake.MarkedForRemoval

Global HeartBeatRate.MarkedForRemoval, HeartBeatTimer.MarkedForRemoval, HeartBeatVolume.MarkedForRemoval

Global WearingGasMask.MarkedForRemoval, WearingHazmat.MarkedForRemoval, WearingVest.MarkedForRemoval, WearingNightVision.MarkedForRemoval
Global Wearing1499.MarkedForRemoval
Global NVTimer.MarkedForRemoval

Global SuperMan.MarkedForRemoval, SuperManTimer.MarkedForRemoval

Global Injuries.MarkedForRemoval, Bloodloss.MarkedForRemoval, Infect.MarkedForRemoval

Global KillTimer.MarkedForRemoval, KillAnim.MarkedForRemoval, FallTimer.MarkedForRemoval, DeathTimer.MarkedForRemoval
Global Sanity.MarkedForRemoval, ForceMove.MarkedForRemoval, ForceAngle.MarkedForRemoval

Global Playable.MarkedForRemoval

Global BLINKFREQ.MarkedForRemoval
Global BlinkTimer.MarkedForRemoval, EyeIrritation.MarkedForRemoval, EyeStuck.MarkedForRemoval, BlinkEffect.MarkedForRemoval, BlinkEffectTimer.MarkedForRemoval

Global Stamina.MarkedForRemoval, StaminaEffect.MarkedForRemoval, StaminaEffectTimer.MarkedForRemoval

Global GodMode.MarkedForRemoval, NoClip.MarkedForRemoval, NoClipSpeed.MarkedForRemoval

Global PlayerSoundVolume.MarkedForRemoval

;TODO: maybe remove?
Global RefinedItems.MarkedForRemoval

Global LightBlink.MarkedForRemoval, LightFlash.MarkedForRemoval

Global BlurVolume.MarkedForRemoval, BlurTimer.MarkedForRemoval

Global PlayTime.MarkedForRemoval ;TODO: do we even need this?

;TODO: this is all bad


Global InfiniteStamina.MarkedForRemoval

Global NVBlink.MarkedForRemoval
Global IsNVGBlinking.MarkedForRemoval

Global CameraFogNear.MarkedForRemoval
Global CameraFogFar.MarkedForRemoval

Global StoredCameraFogFar.MarkedForRemoval

Function MovePlayer()
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#
	
	If mainPlayer\superMan>0.0 Then
		Speed = Speed * 3
		
		mainPlayer\superMan=mainPlayer\superMan+timing\tickDuration
		
		mainPlayer\camShake = Sin(mainPlayer\superMan / 5.0) * (mainPlayer\superMan / 1500.0)
		
		If mainPlayer\superMan > 70 * 50 Then
			DeathMSG = "A Class D jumpsuit found in [DATA REDACTED]. Upon further examination, the jumpsuit was found to be filled with 12.5 kilograms of blue ash-like substance. "
			DeathMSG = DeathMSG + "Chemical analysis of the substance remains non-conclusive. Most likely related to SCP-914."
			Kill(mainPlayer)
			ShowEntity mainPlayer\overlays[OVERLAY_FOG]
		Else
			mainPlayer\blurTimer = 500
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
		EndIf
	End If
	
	;If DeathTimer > 0 Then
	;	DeathTimer=DeathTimer-timing\tickDuration
	;	If DeathTimer < 1 Then DeathTimer = -1.0
	;ElseIf DeathTimer < 0 
	;	Kill(mainPlayer)
	;EndIf
	
	mainPlayer\stamina = Min(mainPlayer\stamina + 0.15 * timing\tickDuration, 100.0)
	
	If mainPlayer\staminaEffect > 1 Then
		mainPlayer\staminaEffect = mainPlayer\staminaEffect - (timing\tickDuration/70)
	Else
		If mainPlayer\staminaEffect <> 1.0 Then mainPlayer\staminaEffect = 1.0
		mainPlayer\staminaEffect = CurveValue(1.0, mainPlayer\staminaEffect, 50)
	EndIf
	
	If (mainPlayer\currRoom\RoomTemplate\Name <> "pocketdimension") Then 
		If KeyDown(keyBinds\sprint) Then
			If (mainPlayer\stamina < 5) Then ;out of breath
				If (Not IsChannelPlaying(mainPlayer\breathChn)) Then mainPlayer\breathChn = PlaySound(GetIntArray2DElem(mainPlayer\breathingSFX, IsPlayerWearingTempName(mainPlayer,"gasmask"), 0))
			ElseIf (mainPlayer\stamina < 50) ;panting
				If (mainPlayer\breathChn = 0) Then
					mainPlayer\breathChn = PlaySound(GetIntArray2DElem(mainPlayer\breathingSFX, IsPlayerWearingTempName(mainPlayer,"gasmask"), Rand(1, 3)))
					ChannelVolume(mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\soundVolume)
				Else
					If (Not IsChannelPlaying(mainPlayer\breathChn)) Then
						mainPlayer\breathChn = PlaySound(GetIntArray2DElem(mainPlayer\breathingSFX, IsPlayerWearingTempName(mainPlayer,"gasmask"), Rand(1, 3)))
						ChannelVolume(mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\soundVolume)		
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	For i%=0 To mainPlayer\inventory\size-1
		If (mainPlayer\inventory\items[i]<>Null) Then
			If (mainPlayer\inventory\items[i]\itemtemplate\tempname="finevest") Then mainPlayer\stamina = Min(mainPlayer\stamina,60.0)
		EndIf
	Next
	
	;If IsZombie Then Crouch = False
	
	If Abs(mainPlayer\crouchState-mainPlayer\crouching)<0.001 Then 
		mainPlayer\crouchState = mainPlayer\crouching
	Else
		mainPlayer\crouchState = CurveValue(mainPlayer\crouching, mainPlayer\crouchState, 10.0)
	EndIf
	
	If (Not mainPlayer\noclip) Then 
		If ((KeyDown(keyBinds\down) Xor KeyDown(keyBinds\up)) Or (KeyDown(keyBinds\rght) Xor KeyDown(keyBinds\lft)) And (Not mainPlayer\disableControls)) Or mainPlayer\forceMove>0 Then
			
			If mainPlayer\crouching = 0 And (KeyDown(keyBinds\sprint)) And mainPlayer\stamina > 0.0 Then; And (Not IsZombie) Then
				Sprint = 2.5
				mainPlayer\stamina = mainPlayer\stamina - timing\tickDuration * 0.5 * (1.0/mainPlayer\staminaEffect)
				If mainPlayer\stamina <= 0 Then mainPlayer\stamina = -20.0
			End If
			
			If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Then 
				If EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale Then
					mainPlayer\stamina = 0
					Speed = 0.015
					Sprint = 1.0					
				EndIf
			EndIf	
			
			If mainPlayer\forceMove>0 Then Speed=Speed*mainPlayer\forceMove
			
			If mainPlayer\selectedItem<>Null Then
				If mainPlayer\selectedItem\itemtemplate\tempname = "firstaid" Or mainPlayer\selectedItem\itemtemplate\tempname = "finefirstaid" Or mainPlayer\selectedItem\itemtemplate\tempname = "firstaid2" Then 
					Sprint = 0
				EndIf
			EndIf
			
			Local temp# = (mainPlayer\camAnimState Mod 360), tempchn%
			If (Not mainPlayer\disableControls) Then mainPlayer\camAnimState = (mainPlayer\camAnimState + timing\tickDuration * Min(Sprint, 1.5) * 7) Mod 720
			If temp < 180 And (mainPlayer\camAnimState Mod 360) >= 180 And (Not mainPlayer\dead) Then
				;TODO: define constants for each override state
				If mainPlayer\footstepOverride=0 Then
					temp = GetStepSound(mainPlayer\collider)
					
					If Sprint = 1.0 Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)
						tempchn% = PlaySound(StepSFX(temp, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\soundVolume
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)
						tempchn% = PlaySound(StepSFX(temp, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\soundVolume
					End If
				ElseIf mainPlayer\footstepOverride=1 Then
					tempchn% = PlaySound(Step2SFX(Rand(0, 2)))
					ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\soundVolume
				ElseIf mainPlayer\footstepOverride=2 Then
					tempchn% = PlaySound(Step2SFX(Rand(3,5)))
					ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\soundVolume
				ElseIf mainPlayer\footstepOverride=3 Then
					If Sprint = 1.0 Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)
						tempchn% = PlaySound(StepSFX(0, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\soundVolume
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)
						tempchn% = PlaySound(StepSFX(0, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\soundVolume
					End If
				EndIf
				
			EndIf	
		EndIf
	Else ;noclip on
		If (KeyDown(keyBinds\sprint)) Then 
			Sprint = 2.5
		ElseIf KeyDown(keyBinds\crouch)
			Sprint = 0.5
		EndIf
	EndIf
	
	If KeyHit(keyBinds\crouch) And (Not mainPlayer\disableControls) Then mainPlayer\crouching = (Not mainPlayer\crouching)
	
	Local temp2# = (Speed * Sprint) / (1.0+mainPlayer\crouchState)
	
	If mainPlayer\noclip Then 
		mainPlayer\camAnimState = 0
		mainPlayer\moveSpeed = 0
		mainPlayer\crouchState = 0
		mainPlayer\crouching = 0
		
		RotateEntity mainPlayer\collider, WrapAngle(EntityPitch(mainPlayer\cam)), WrapAngle(EntityYaw(mainPlayer\cam)), 0
		
		temp2 = temp2; * NoClipSpeed ;TODO: reimplement
		
		If KeyDown(keyBinds\down) Then MoveEntity mainPlayer\collider, 0, 0, -temp2*timing\tickDuration
		If KeyDown(keyBinds\up) Then MoveEntity mainPlayer\collider, 0, 0, temp2*timing\tickDuration
		
		If KeyDown(keyBinds\lft) Then MoveEntity mainPlayer\collider, -temp2*timing\tickDuration, 0, 0
		If KeyDown(keyBinds\rght) Then MoveEntity mainPlayer\collider, temp2*timing\tickDuration, 0, 0	
		
		ResetEntity mainPlayer\collider
	Else
		temp2# = temp2 / Max((mainPlayer\injuries+3.0)/3.0,1.0)
		If mainPlayer\injuries > 0.5 Then 
			temp2 = temp2*Min((Sin(mainPlayer\camAnimState/2)+1.2),1.0)
		EndIf
		
		temp = False
		;If (Not IsZombie%)
		If KeyDown(keyBinds\down) And (Not mainPlayer\disableControls) Then
			temp = True 
			angle = 180
			If KeyDown(keyBinds\lft) Then angle = 135 
			If KeyDown(keyBinds\rght) Then angle = -135 
		ElseIf (KeyDown(keyBinds\up) And (Not mainPlayer\disableControls)) Then; Or ForceMove>0
			temp = True
			angle = 0
			If KeyDown(keyBinds\lft) Then angle = 45 
			If KeyDown(keyBinds\rght) Then angle = -45 
		ElseIf mainPlayer\forceMove>0 Then
			temp=True
			angle = mainPlayer\forceAngle
		Else If (Not mainPlayer\disableControls) Then
			If KeyDown(keyBinds\lft) Then angle = 90 : temp = True
			If KeyDown(keyBinds\rght) Then angle = -90 : temp = True 
		EndIf
		;Else
		;	temp=True
		;	angle = ForceAngle
		;EndIf
		
		angle = WrapAngle(EntityYaw(mainPlayer\collider,True)+angle+90.0)
		
		If temp Then 
			mainPlayer\moveSpeed = CurveValue(temp2, mainPlayer\moveSpeed, 20.0)
		Else
			mainPlayer\moveSpeed = Max(CurveValue(0.0, mainPlayer\moveSpeed-0.1, 1.0),0.0)
		EndIf
		
		If (Not mainPlayer\disableControls) Then TranslateEntity mainPlayer\collider, Cos(angle)*mainPlayer\moveSpeed * timing\tickDuration, 0, Sin(angle)*mainPlayer\moveSpeed * timing\tickDuration, True
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(mainPlayer\collider)
			If (CollisionY(mainPlayer\collider, i) < EntityY(mainPlayer\collider,True)) And (Abs(CollisionNY(mainPlayer\collider, i))>0.8) Then
				CollidedFloor = True
			EndIf
		Next
		
		If CollidedFloor = True Then
			If mainPlayer\dropSpeed# < - 0.07 Then 
				If mainPlayer\footstepOverride=0 Then
					PlaySound(StepSFX(GetStepSound(mainPlayer\collider), 0, Rand(0, 7)))					
				ElseIf mainPlayer\footstepOverride=1
					PlaySound(Step2SFX(Rand(0, 2)))
				ElseIf mainPlayer\footstepOverride=2
					PlaySound(Step2SFX(Rand(3, 5)))
				ElseIf mainPlayer\footstepOverride=3
					PlaySound(StepSFX(0, 0, Rand(0, 7)))
				EndIf
				mainPlayer\loudness = Max(3.0,mainPlayer\loudness)
			EndIf
			mainPlayer\dropSpeed# = 0
		Else
			mainPlayer\dropSpeed# = Min(Max(mainPlayer\dropSpeed - 0.006 * timing\tickDuration, -2.0), 0.0)
		EndIf	
		
		If (Not mainPlayer\disableControls) Then TranslateEntity mainPlayer\collider, 0, mainPlayer\dropSpeed * timing\tickDuration, 0
	EndIf
	
	mainPlayer\forceMove = 0.0
	
	If mainPlayer\injuries > 1.0 Then
		temp2 = mainPlayer\bloodloss
		mainPlayer\blurTimer = Max(Max(Sin(TimeInPosMilliSecs()/100.0)*mainPlayer\bloodloss*30.0,mainPlayer\bloodloss*2*(2.0-mainPlayer\crouchState)),mainPlayer\blurTimer)
		mainPlayer\bloodloss = Min(mainPlayer\bloodloss + (Min(mainPlayer\injuries,3.5)/300.0)*timing\tickDuration,100)
		
		If temp2 <= 60 And mainPlayer\bloodloss > 60 Then
			Msg = "You are feeling faint from the amount of blood you loss."
			MsgTimer = 70*4
		EndIf
	EndIf
	
	UpdateInfect()
	
	If mainPlayer\bloodloss > 0 Then
		If Rnd(200)<Min(mainPlayer\injuries,4.0) Then
			Local pvt% = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\collider)+Rnd(-0.05,0.05),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)+Rnd(-0.05,0.05)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.decals = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\size = Rnd(0.03,0.08)*Min(mainPlayer\injuries,3.0) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\size, de\size
			tempchn% = PlaySound (DripSFX(Rand(0,2)))
			ChannelVolume tempchn, Rnd(0.0,0.8)*userOptions\soundVolume
			ChannelPitch tempchn, Rand(20000,30000)
			
			FreeEntity pvt
		EndIf
		
		mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0)*mainPlayer\bloodloss*0.2)
		
		If mainPlayer\bloodloss > 60 Then mainPlayer\crouching = True
		If mainPlayer\bloodloss => 100 Then 
			Kill(mainPlayer)
			mainPlayer\heartbeatIntensity = 0.0
		ElseIf mainPlayer\bloodloss > 80.0
			mainPlayer\heartbeatIntensity = Max(150-(mainPlayer\bloodloss-80)*5,mainPlayer\heartbeatIntensity)
		ElseIf mainPlayer\bloodloss > 35.0
			mainPlayer\heartbeatIntensity = Max(70+mainPlayer\bloodloss,mainPlayer\heartbeatIntensity)
		EndIf
	EndIf
	
	If (Not mainPlayer\disableControls) Then
		If KeyHit(keyBinds\blink) Then mainPlayer\blinkTimer = 0
		If KeyDown(keyBinds\blink) And mainPlayer\blinkTimer < - 10 Then mainPlayer\blinkTimer = -10
	EndIf
	
	
	If mainPlayer\heartbeatIntensity > 0 Then
		tempchn = PlaySound (HeartBeatSFX)
		ChannelVolume tempchn, Max(Min((mainPlayer\heartbeatIntensity-80.0)/60.0,1.0),0.0)*userOptions\soundVolume
		
		mainPlayer\heartbeatIntensity = mainPlayer\heartbeatIntensity - timing\tickDuration
	EndIf
	
End Function

;TODO: these variable names are awful
;Also scrap the mouselook speed in favor of just interpolating to a final position
; - -Viewport.
Global viewport_center_x% = userOptions\screenWidth / 2, viewport_center_y% = userOptions\screenHeight / 2

; -- Mouselook.
Global mouselook_x_inc# = 0.3 ; This sets both the sensitivity and direction (+/-) of the mouse on the X axis.
Global mouselook_y_inc# = 0.3 ; This sets both the sensitivity and direction (+/-) of the mouse on the Y axis.
; Used to limit the mouse movement to within a certain number of pixels (250 is used here) from the center of the screen. This produces smoother mouse movement than continuously moving the mouse back to the center each loop.
Global mouse_left_limit% = 250, mouse_right_limit% = GraphicsWidth () - 250
Global mouse_top_limit% = 150, mouse_bottom_limit% = GraphicsHeight () - 150 ; As above.
Global mouse_x_speed_1#, mouse_y_speed_1#

Function MouseLook()
	Local i%
	
	Local wearingGasMask%
	wearingGasMask = IsPlayerWearingTempName(mainPlayer,"gasmask")
	If Not wearingGasMask Then
		wearingGasMask = IsPlayerWearingTempName(mainPlayer,"supergasmask")*2
	EndIf
	
	Local wearingHazmat%
	wearingHazmat = IsPlayerWearingTempName(mainPlayer,"hazmatsuit")
	If Not wearingHazmat Then
		wearingHazmat = IsPlayerWearingTempName(mainPlayer,"hazmatsuit2")*2
	EndIf
	
	Local wearing1499%
	wearing1499 = IsPlayerWearingTempName(mainPlayer,"scp1499")
	If Not wearing1499 Then
		wearing1499 = IsPlayerWearingTempName(mainPlayer,"super1499")*2
	EndIf
	
	Local wearingNightVision%
	wearingNightVision = IsPlayerWearingTempName(mainPlayer,"nvgoggles")
	If Not wearingNightVision Then
		wearingNightVision = IsPlayerWearingTempName(mainPlayer,"supernv")*2
	EndIf
	
	mainPlayer\camShake = Max(mainPlayer\camShake - (timing\tickDuration / 10), 0)
	
	;CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.0)
	CameraZoom(mainPlayer\cam, Min(1.0+(mainPlayer\camZoom/400.0),1.1))
	mainPlayer\camZoom = Max(mainPlayer\camZoom - timing\tickDuration, 0)
	
	If mainPlayer\fallTimer >=0 Then
		
		;HeadDropSpeed = 0
		
		;TODO: remove after implementing fixed collision code
		;If 0 Then 
		;fixing the black screen bug with some bubblegum code 
		Local Zero# = 0.0
		Local Nan1# = 0.0 / Zero
		If Int(EntityX(mainPlayer\collider))=Int(Nan1) Then
			
			PositionEntity mainPlayer\collider, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True) - 0.5, EntityZ(mainPlayer\cam, True), True
			Msg = "EntityX(mainPlayer\collider) = NaN, RESETTING COORDINATES    -    New coordinates: "+EntityX(mainPlayer\collider)
			MsgTimer = 300				
		EndIf
		;EndIf
		
		Local up# = (Sin(mainPlayer\camAnimState) / (20.0+mainPlayer\crouchState*20.0))*0.6	
		Local roll# = Max(Min(Sin(mainPlayer\camAnimState*0.5)*2.5*Min(mainPlayer\injuries+0.25,3.0),8.0),-8.0)
		
		;tilt the camera to the side if the player is injured
		;RotateEntity mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), Max(Min(up*30*mainPlayer\injuries,50),-50)
		PositionEntity mainPlayer\cam, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider)
		RotateEntity mainPlayer\cam, 0, EntityYaw(mainPlayer\collider), roll*0.5
		
		MoveEntity mainPlayer\cam, 0.0, up + 0.6 + mainPlayer\crouchState * -0.3, 0
		
		;RotateEntity mainPlayer\collider, EntityPitch(mainPlayer\collider), EntityYaw(mainPlayer\collider), 0
		;moveentity player, side, up, 0	
		; -- Update the smoothing que To smooth the movement of the mouse.
		mouse_x_speed_1# = CurveValue(MouseXSpeed() * (userOptions\mouseSensitivity + 0.6) , mouse_x_speed_1, 6.0 / (userOptions\mouseSensitivity + 1.0)) 
		If Int(mouse_x_speed_1) = Int(Nan1) Then mouse_x_speed_1 = 0
		
		If userOptions\invertMouseY Then
			mouse_y_speed_1# = CurveValue(-MouseYSpeed() * (userOptions\mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions\mouseSensitivity+1.0)) 
		Else
			mouse_y_speed_1# = CurveValue(MouseYSpeed () * (userOptions\mouseSensitivity + 0.6), mouse_y_speed_1, 6.0/(userOptions\mouseSensitivity+1.0)) 
		EndIf
		If Int(mouse_y_speed_1) = Int(Nan1) Then mouse_y_speed_1 = 0
		
		;TODO: CHANGE THESE NAMES
		Local the_yaw# = ((mouse_x_speed_1#)) * mouselook_x_inc# / (1.0+IsPlayerWearingTempName(mainPlayer,"vest"))
		Local the_pitch# = ((mouse_y_speed_1#)) * mouselook_y_inc# / (1.0+IsPlayerWearingTempName(mainPlayer,"vest"))
		
		TurnEntity mainPlayer\collider, 0.0, -the_yaw#, 0.0 ; Turn the user on the Y (yaw) axis.
		mainPlayer\headPitch# = mainPlayer\headPitch# + the_pitch#
		; -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
		If mainPlayer\headPitch# > 70.0 Then mainPlayer\headPitch# = 70.0
		If mainPlayer\headPitch# < - 70.0 Then mainPlayer\headPitch# = -70.0
		
		RotateEntity mainPlayer\cam, WrapAngle(mainPlayer\headPitch + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), WrapAngle(EntityYaw(mainPlayer\collider) + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), roll ; Pitch the user;s camera up And down.
		
		If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Then
			If EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale Then
				RotateEntity mainPlayer\cam, WrapAngle(EntityPitch(mainPlayer\cam)),WrapAngle(EntityYaw(mainPlayer\cam)), roll+WrapAngle(Sin(TimeInPosMilliSecs()/150.0)*30.0) ; Pitch the user;s camera up And down.
			EndIf
		EndIf
		
	Else
		HideEntity mainPlayer\collider
		PositionEntity mainPlayer\cam, EntityX(mainPlayer\head), EntityY(mainPlayer\head), EntityZ(mainPlayer\head)
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(mainPlayer\head)
			If CollisionY(mainPlayer\head, i) < EntityY(mainPlayer\head) - 0.01 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			;HeadDropSpeed# = 0
		Else
			
			;TODO: reimplement head falling
			;If KillAnim = 0 Then 
			;	MoveEntity Head, 0, 0, HeadDropSpeed
			;	RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
			;	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
			;Else
			;	MoveEntity Head, 0, 0, -HeadDropSpeed
			;	RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
			;	RotateEntity(mainPlayer\cam, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(mainPlayer\cam), 40.0), EntityYaw(mainPlayer\cam), EntityRoll(mainPlayer\cam))
			;EndIf
			
			;HeadDropSpeed# = HeadDropSpeed - 0.002 * timing\tickDuration
		EndIf
		
		If userOptions\invertMouseY Then
			TurnEntity (mainPlayer\cam, -MouseYSpeed() * 0.05 * timing\tickDuration, -MouseXSpeed() * 0.15 * timing\tickDuration, 0)
		Else
			TurnEntity (mainPlayer\cam, MouseYSpeed() * 0.05 * timing\tickDuration, -MouseXSpeed() * 0.15 * timing\tickDuration, 0)
		End If
		
	EndIf
	
	;DUST PARTICLES
	If Rand(35) = 1 Then
		Local pvt% = CreatePivot()
		PositionEntity(pvt, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True))
		RotateEntity(pvt, 0, Rnd(360), 0)
		If Rand(2) = 1 Then
			MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
		Else
			MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
		End If
		
		Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
		p\speed = 0.001
		RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
		
		p\SizeChange = -0.00001
		
		FreeEntity pvt
	End If
	
	; -- Limit the mouse;s movement. Using this method produces smoother mouselook movement than centering the mouse Each loop.
	If (MouseX() > mouse_right_limit) Or (MouseX() < mouse_left_limit) Or (MouseY() > mouse_bottom_limit) Or (MouseY() < mouse_top_limit)
		MoveMouse viewport_center_x, viewport_center_y
	EndIf
	
	If wearingGasMask Or wearingHazmat Or wearing1499 Then
		If wearingGasMask = 2 Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
		If wearing1499 = 2 Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
		If wearingHazmat = 2 Then 
			mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
		ElseIf wearingHazmat=1
			mainPlayer\stamina = Min(60, mainPlayer\stamina)
		EndIf
		
		ShowEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	Else
		HideEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	End If
	
	If (Not wearingNightVision=0) Then
		ShowEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
		If wearingNightVision=2 Then
			EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
			AmbientLightRooms(15)
		ElseIf wearingNightVision=3 Then
			EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 255,0,0)
			AmbientLightRooms(15)
		Else
			EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,255,0)
			AmbientLightRooms(15)
		EndIf
		;EntityTexture(Fog, FogNVTexture)
	Else
		AmbientLightRooms(0)
		HideEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
		;EntityTexture(Fog, FogTexture)
	EndIf
End Function

Function EquipItem(player.Player,item.Items,toggle%)
	If item=Null Then Return
	If item\itemtemplate\invSlot = WORNITEM_SLOT_NONE Then Return
	Local currItem.Items = player\wornItems[item\itemtemplate\invSlot]
	DeEquipSlot(player,item\itemtemplate\invSlot)
	DebugLog (Not toggle)+" + "+(currItem<>item)
	If (Not toggle) Or currItem<>item Then
		player\wornItems[item\itemtemplate\invSlot] = item
		
		Local r.Rooms, it.Items
		Select item\itemtemplate\tempname
			Case "vest"
				Msg = "You put on the vest and feel slightly encumbered."
				MsgTimer = 70 * 7
			Case "finevest"
				Msg = "You put on the vest and feel heavily encumbered."
				MsgTimer = 70 * 7
			Case "scp1499","super1499"
				For r.Rooms = Each Rooms
					If r\RoomTemplate\Name = "dimension1499" Then
						player\blinkTimer = -1
						NTF_1499PrevRoom = player\currRoom
						NTF_1499PrevX# = EntityX(player\collider)
						NTF_1499PrevY# = EntityY(player\collider)
						NTF_1499PrevZ# = EntityZ(player\collider)
						
						If NTF_1499X# = 0.0 And NTF_1499Y# = 0.0 And NTF_1499Z# = 0.0
							PositionEntity (player\collider, r\x+676.0*RoomScale, r\y+314.0*RoomScale, r\z-2080.0*RoomScale)
						Else
							PositionEntity (player\collider, NTF_1499X#, NTF_1499Y#+0.05, NTF_1499Z#)
						EndIf
						ResetEntity(player\collider)
						UpdateDoors()
						UpdateRooms()
						For it.Items = Each Items
							it\disttimer = 0
						Next
						player\currRoom = r
						PlaySound NTF_1499EnterSFX%
						NTF_1499X# = 0.0
						NTF_1499Y# = 0.0
						NTF_1499Z# = 0.0
						Exit
					EndIf
				Next
		End Select
	EndIf
End Function

Function DeEquipItem(player.Player,item.Items)
	If item = Null Then
		Return
	EndIf
	
	If player\wornItems[item\itemtemplate\invSlot]<>item Then
		Return
	EndIf
	
	player\wornItems[item\itemtemplate\invSlot] = Null
	
	;TODO: implement as needed
	Select item\itemtemplate\tempname
		Case "vest","finevest"
			Msg = "You removed the vest."
			MsgTimer = 70 * 7
		Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
			Msg = "You removed the hazmat suit."
			MsgTimer = 70 * 7
			DropItem(item)
	End Select
End Function

Function DeEquipSlot(player.Player,invSlot%)
	If player\wornItems[invSlot] = Null Then
		Return
	EndIf
	
	DeEquipItem(player,player\wornItems[invSlot])
End Function

Function ToggleInventory(player.Player)
	If CurrGameState=GAMESTATE_INVENTORY Then
		If mainPlayer\openInventory = mainPlayer\inventory Then
			CurrGameState=GAMESTATE_PLAYING
			ResumeSounds()
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1# = 0.0 : mouse_y_speed_1# = 0.0
		Else
			mainPlayer\openInventory = mainPlayer\inventory
		EndIf
	Else
		CurrGameState=GAMESTATE_INVENTORY
		mainPlayer\openInventory = mainPlayer\inventory
		PauseSounds()
	EndIf
	
	mainPlayer\selectedItem = Null 
End Function

Function DrawInventory(player.Player)
	Local MouseSlot% = 66
	
	Local spacing%
	
	Local isMouseOn%
	
	Local strtemp$
	
	If CurrGameState=GAMESTATE_INVENTORY Then
		width% = 70
		height% = 70
		spacing% = 35
		
		itemsPerRow% = 5
		
		x = userOptions\screenWidth / 2 - (width * itemsPerRow + spacing * (itemsPerRow - 1)) / 2
		y = userOptions\screenHeight / 2 - height * (player\openInventory\size/itemsPerRow) + height / 2
		
		For  n% = 0 To player\openInventory\size - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				End If
			EndIf
			
			If player\openInventory\items[n] <> Null Then
				Color 200, 200, 200
				If IsPlayerWearingItem(player,player\openInventory\items[n]) Then
					Rect(x - 3, y - 3, width + 6, height + 6)
				EndIf
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
				Color 255, 0, 0
				Rect(x - 1, y - 1, width + 2, height + 2)
			EndIf
			
			Color 255, 255, 255
			DrawFrame(x, y, width, height, (x Mod 64), (x Mod 64))
			
			If player\openInventory\items[n] <> Null Then
				If (player\selectedItem <> player\openInventory\items[n] Or isMouseOn) Then 
					DrawImage(player\openInventory\items[n]\invimg, x + width / 2 - 32, y + height / 2 - 32)
				EndIf
			EndIf
			
			If player\openInventory\items[n] <> Null And player\selectedItem <> player\openInventory\items[n] Then
				If isMouseOn Then
					If player\selectedItem = Null Then
						SetFont Font1
						Color 0,0,0
						Text(x + width / 2 + 1, y + height + spacing - 15 + 1, player\openInventory\items[n]\name, True)							
						Color 255, 255, 255	
						Text(x + width / 2, y + height + spacing - 15, player\openInventory\items[n]\name, True)	
					EndIf
				EndIf
			EndIf					
			
			x=x+width + spacing
			If n Mod 5 = 4 Then 
				y = y + height*2 
				x = userOptions\screenWidth / 2 - (width * itemsPerRow + spacing * (itemsPerRow - 1)) / 2
			EndIf
		Next
		
		If player\selectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(player\selectedItem\invimg, ScaledMouseX() - ImageWidth(player\selectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(player\selectedItem\itemtemplate\invimg) / 2)
				ElseIf player\selectedItem <> player\openInventory\items[MouseSlot]
					DrawImage(player\selectedItem\invimg, ScaledMouseX() - ImageWidth(player\selectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(player\selectedItem\itemtemplate\invimg) / 2)
				EndIf
			End If
		End If
		
		If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
	Else
		If player\selectedItem <> Null Then
			Select player\selectedItem\itemtemplate\tempname
				Case "key1", "key2", "key3", "key4", "key5", "scp860", "hand", "hand2"
					DrawImage(player\selectedItem\itemtemplate\invimg, userOptions\screenWidth / 2 - ImageWidth(player\selectedItem\itemtemplate\invimg) / 2, userOptions\screenHeight / 2 - ImageHeight(player\selectedItem\itemtemplate\invimg) / 2)
				Case "firstaid","finefirstaid","firstaid2"
					DrawImage(player\selectedItem\itemtemplate\invimg, userOptions\screenWidth / 2 - ImageWidth(player\selectedItem\itemtemplate\invimg) / 2, userOptions\screenHeight / 2 - ImageHeight(player\selectedItem\itemtemplate\invimg) / 2)
					
					width% = 300
					height% = 20
					x% = userOptions\screenWidth / 2 - width / 2
					y% = userOptions\screenHeight / 2 + 80
					Rect(x, y, width+4, height, False)
					For  i% = 1 To Int((width - 2) * (player\selectedItem\state / 100.0) / 10)
						DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
					Next
				Case "paper","ticket"
					If player\selectedItem\itemtemplate\img = 0 Then
						Select player\selectedItem\itemtemplate\name
							Case "Burnt Note" 
								player\selectedItem\itemtemplate\img = LoadImage("GFX\items\bn.it")
								SetBuffer ImageBuffer(player\selectedItem\itemtemplate\img)
								Color 0,0,0
								Text 277, 469, AccessCode, True, True
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "Movie Ticket"
								;don't resize because it messes up the masking
								;TODO: this is retarded, fix
								player\selectedItem\itemtemplate\img=LoadImage(player\selectedItem\itemtemplate\imgpath)	
								
								If (player\selectedItem\state = 0) Then
									Msg = Chr(34)+"Hey, I remember getting this ticket from the kickstarter! Wonder if it ever came out..."+Chr(34)
									MsgTimer = 70*10
									PlaySound LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
									player\selectedItem\state = 1
								EndIf
							Default 
								player\selectedItem\itemtemplate\img=LoadImage(player\selectedItem\itemtemplate\imgpath)	
								player\selectedItem\itemtemplate\img = ResizeImage2(player\selectedItem\itemtemplate\img, ImageWidth(player\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(player\selectedItem\itemtemplate\img) * MenuScale)
						End Select
						
						MaskImage(player\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(player\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(player\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(player\selectedItem\itemtemplate\img) / 2)
				Case "radio","18vradio","fineradio","veryfineradio"
					strtemp$ = ""
					
					If player\selectedItem\itemtemplate\img=0 Then
						player\selectedItem\itemtemplate\img=LoadImage(player\selectedItem\itemtemplate\imgpath)	
						MaskImage(player\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					x = userOptions\screenWidth - ImageWidth(player\selectedItem\itemtemplate\img) ;+ 120
					y = userOptions\screenHeight - ImageHeight(player\selectedItem\itemtemplate\img) ;- 30
					
					DrawImage(player\selectedItem\itemtemplate\img, x, y)
					
					If player\selectedItem\state > 0 Then
						;TODO: remove coffindistance
						If Not (player\currRoom\RoomTemplate\Name = "pocketdimension" Or CoffinDistance < 4.0) Then
							Select Int(player\selectedItem\state2)
								Case 1 ;hälytyskanava
									strtemp = "        WARNING - CONTAINMENT BREACH          "
								Case 2 ;scp-radio
									ResumeChannel(RadioCHN(2))
									strtemp = "        SCP Foundation On-Site Radio          "
								Case 3
									strtemp = "             EMERGENCY CHANNEL - RESERVED FOR COMMUNICATION IN THE EVENT OF A CONTAINMENT BREACH         "
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If player\selectedItem\state <= 100 Then
								;Text (x - 60, y - 20, "BATTERY")
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(player\selectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							SetFont Font3
							Text(x+60, y, "CHN")						
							
							If player\selectedItem\itemtemplate\tempname = "veryfineradio" Then ;"KOODIKANAVA"
								strtemp = ""
								For i = 0 To Rand(5, 30)
									strtemp = strtemp + Chr(Rand(1,100))
								Next
								
								SetFont Font4
								Text(x+97, y+16, Rand(0,9),True,True)
							Else
								SetFont Font4
								Text(x+97, y+16, Int(player\selectedItem\state2+1),True,True)
							EndIf
							
							SetFont Font3
							If strtemp <> "" Then
								strtemp = Right(Left(strtemp, (Int(TimeInPosMilliSecs()/300) Mod Len(strtemp))),10)
								Text(x+32, y+33, strtemp)
							EndIf
							
							SetFont Font1
						EndIf
					EndIf
				Case "navigator", "nav"
					If player\selectedItem\itemtemplate\img=0 Then
						player\selectedItem\itemtemplate\img=LoadImage(player\selectedItem\itemtemplate\imgpath)	
						MaskImage(player\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					x = userOptions\screenWidth - ImageWidth(player\selectedItem\itemtemplate\img)*0.5+20
					y = userOptions\screenHeight - ImageHeight(player\selectedItem\itemtemplate\img)*0.4-85
					width = 287
					height = 256
					
					DrawImage(player\selectedItem\itemtemplate\img, x - ImageWidth(player\selectedItem\itemtemplate\img) / 2, y - ImageHeight(player\selectedItem\itemtemplate\img) / 2 + 85)
					
					SetFont Font3
					
					If player\currRoom\RoomTemplate\Name = "pocketdimension" Then
						If (TimeInPosMilliSecs() Mod 1000) > 300 Then	
							Text(x, y + height / 2 - 80, "ERROR 06", True)
							Text(x, y + height / 2 - 60, "LOCATION UNKNOWN", True)						
						EndIf
					Else
						
						If player\selectedItem\state > 0 And (Rnd(CoffinDistance + 15.0) > 1.0 Or player\currRoom\RoomTemplate\Name <> "coffin") Then
							
							If player\selectedItem\itemtemplate\name = "S-NAV Navigator" Then 
								Color(100, 0, 0)
							Else
								Color (30,30,30)
							EndIf
							If (TimeInPosMilliSecs() Mod 1000) > 300 Then
								If player\selectedItem\itemtemplate\name <> "S-NAV 310 Navigator" And player\selectedItem\itemtemplate\name <> "S-NAV Navigator Ultimate" Then
									Text(x, y + height / 2 - 40, "COULD NOT CONNECT", True)
									Text(x, y + height / 2 - 20, "TO MAP DATABASE", True)
								EndIf
								
								yawvalue = EntityYaw(player\collider)-90
								x1 = x+Cos(yawvalue)*6 : y1 = y-Sin(yawvalue)*6
								x2 = x+Cos(yawvalue-140)*5 : y2 = y-Sin(yawvalue-140)*5				
								x3 = x+Cos(yawvalue+140)*5 : y3 = y-Sin(yawvalue+140)*5
								
								Line x1,y1,x2,y2
								Line x1,y1,x3,y3
								Line x2,y2,x3,y3
							EndIf
							
							Local PlayerX% = Floor(EntityX(player\currRoom\obj) / 8.0 + 0.5), PlayerZ% = Floor(EntityZ(player\currRoom\obj) / 8.0 + 0.5)
							Local SCPs_found% = 0
							If player\selectedItem\itemtemplate\name = "S-NAV Navigator Ultimate" And (TimeInPosMilliSecs() Mod 600) < 400 Then
								Local dist# = EntityDistance(player\cam, Curr173\obj)
								dist = Ceil(dist / 8.0) * 8.0
								If dist < 8.0 * 4 Then
									Color 100, 0, 0
									Oval(x - dist * 3, y - 7 - dist * 3, dist * 3 * 2, dist * 3 * 2, False)
									Text(x - width / 2 + 20, y - height / 2 + 20, "SCP-173")
									SCPs_found% = SCPs_found% + 1
								EndIf
								dist# = EntityDistance(player\cam, Curr106\obj)
								If dist < 8.0 * 4 Then
									Color 100, 0, 0
									Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
									Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-106")
									SCPs_found% = SCPs_found% + 1
								EndIf
								If Curr096<>Null Then 
									dist# = EntityDistance(player\cam, Curr096\obj)
									If dist < 8.0 * 4 Then
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-096")
										SCPs_found% = SCPs_found% + 1
									EndIf
								EndIf
								Local np.NPCs
								For np = Each NPCs
									If np\npcType = NPCtype049
										dist# = EntityDistance(player\cam, np\obj)
										If dist < 8.0 * 4 Then
											Color 100, 0, 0
											Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
											Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-049")
											SCPs_found% = SCPs_found% + 1
										EndIf
									EndIf
								Next
								
								If player\currRoom\RoomTemplate\Name = "coffin" Then
									If CoffinDistance < 8.0 Then
										dist = Rnd(4.0, 8.0)
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-895")
									EndIf
								EndIf
							End If
							
							Color (30,30,30)
							If player\selectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							If player\selectedItem\state <= 100 Then
								Text (x - width/2 + 10, y - height/2 + 10, "BATTERY")
								xtemp = x - width/2 + 10
								ytemp = y - height/2 + 30		
								Line xtemp, ytemp, xtemp+20, ytemp
								Line xtemp, ytemp+100, xtemp+20, ytemp+100
								Line xtemp, ytemp, xtemp, ytemp+100
								Line xtemp+20, ytemp, xtemp+20, ytemp+100
								
								SetFont Font4
								For i = 1 To Ceil(player\selectedItem\state / 10.0)
									Text (xtemp+11, ytemp+i*10-26, "-", True)
									;Rect(x - width/2, y+i*15, 40 - i * 6, 5, Ceil(player\selectedItem\state / 20.0) > 4 - i)
								Next
								SetFont Font3
							EndIf
							
							x = x - 19 + ((EntityX(player\collider) - 4.0) Mod 8.0)*3
							y = y + 14 - ((EntityZ(player\collider)-4.0) Mod 8.0)*3
							;TODO: fix
							;For x2 = Max(1, PlayerX - 4) To Min(MapWidth - 1, PlayerX + 4)
							;	For z2 = Max(1, PlayerZ - 4) To Min(MapHeight - 1, PlayerZ + 4)
							;		
							;		If CoffinDistance > 16.0 Or Rnd(16.0)<CoffinDistance Then 
							;			If MapTemp(x2, z2) And (MapFound(x2, z2) > 0 Or player\selectedItem\itemtemplate\name = "S-NAV 310 Navigator" Or player\selectedItem\itemtemplate\name = "S-NAV Navigator Ultimate") Then
							;				Local drawx% = x + (PlayerX - x2) * 24 , drawy% = y - (PlayerZ - z2) * 24 
							;				
							;				Color (30,30,30)
							;				If player\selectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							;				
							;				If MapTemp(x2 + 1, z2) = False Then Line(drawx - 12, drawy - 12, drawx - 12, drawy + 12)
							;				If MapTemp(x2 - 1, z2) = False Then Line(drawx + 12, drawy - 12, drawx + 12, drawy + 12)
							;				
							;				If MapTemp(x2, z2 - 1) = False Then Line(drawx - 12, drawy - 12, drawx + 12, drawy - 12)
							;				If MapTemp(x2, z2 + 1)= False Then Line(drawx - 12, drawy + 12, drawx + 12, drawy + 12)
							;			EndIf
							;		EndIf
							;	Next
							;Next
						EndIf
					EndIf
				Case "badge"
					If player\selectedItem\itemtemplate\img=0 Then
						player\selectedItem\itemtemplate\img=LoadImage(player\selectedItem\itemtemplate\imgpath)	
						;player\selectedItem\itemtemplate\img = ResizeImage2(player\selectedItem\itemtemplate\img, ImageWidth(player\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(player\selectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(player\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(player\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(player\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(player\selectedItem\itemtemplate\img) / 2)
					
					If player\selectedItem\state = 0 Then
						PlaySound LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
						Select player\selectedItem\itemtemplate\name
							Case "Old Badge"
								Msg = Chr(34)+"Huh? This guy looks just like me!"+Chr(34)
								MsgTimer = 70*10
						End Select
						
						player\selectedItem\state = 1
					EndIf
				Case "key"
					If player\selectedItem\state = 0 Then
						PlaySound LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
						
						Msg = Chr(34)+"Isn't this the key to that old shack? The one where I... No, it can't be."+Chr(34)
						MsgTimer = 70*10						
					EndIf
					
					player\selectedItem\state = 1
					player\selectedItem = Null
				Case "oldpaper"
					If player\selectedItem\itemtemplate\img = 0 Then
						player\selectedItem\itemtemplate\img = LoadImage(player\selectedItem\itemtemplate\imgpath)	
						player\selectedItem\itemtemplate\img = ResizeImage2(player\selectedItem\itemtemplate\img, ImageWidth(player\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(player\selectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(player\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(player\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(player\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(player\selectedItem\itemtemplate\img) / 2)
					
					If player\selectedItem\state = 0
						Select player\selectedItem\itemtemplate\name
							Case "Disciplinary Hearing DH-S-4137-17092"
								player\blurTimer = 1000
								
								Msg = Chr(34)+"Why does this seem so familiar?"+Chr(34)
								MsgTimer = 70*10
								PlaySound LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
								player\selectedItem\state = 1
						End Select
					EndIf
			End Select
		EndIf
	EndIf
	
End Function

Function UpdateInventory(player.Player)
	;TODO: cleanup
	Local PrevInvOpen% = (CurrGameState=GAMESTATE_INVENTORY)
	Local MouseSlot% = 66
	
	Local spacing%
	
	Local OtherSize.MarkedForRemoval,OtherAmount.MarkedForRemoval
	
	Local isEmpty.MarkedForRemoval
	
	Local isMouseOn%
	
	Local closedInv.MarkedForRemoval
	
	Local strtemp$
	If CurrGameState=GAMESTATE_INVENTORY Then
		If (player\currRoom\RoomTemplate\Name = "gatea") Then
			HideEntity player\overlays[OVERLAY_FOG]
			CameraFogRange player\cam, 5,30
			CameraFogColor (player\cam,200,200,200)
			CameraClsColor (player\cam,200,200,200)					
			CameraRange(player\cam, 0.05, 30)
		ElseIf (player\currRoom\RoomTemplate\Name = "exit1") And (EntityY(player\collider)>1040.0*RoomScale)
			HideEntity player\overlays[OVERLAY_FOG]
			CameraFogRange player\cam, 5,45
			CameraFogColor (player\cam,200,200,200)
			CameraClsColor (player\cam,200,200,200)					
			CameraRange(player\cam, 0.05, 60)
		EndIf
		
		mainPlayer\selectedDoor = Null
		
		width% = 70
		height% = 70
		spacing% = 35
		
		itemsPerRow% = 5
		
		x = userOptions\screenWidth / 2 - (width * itemsPerRow + spacing * (itemsPerRow - 1)) / 2
		y = userOptions\screenHeight / 2 - height * (player\openInventory\size/itemsPerRow) + height / 2
		
		For  n% = 0 To player\openInventory\size - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				End If
			EndIf
			
			If player\openInventory\items[n] <> Null And player\selectedItem <> player\openInventory\items[n] Then
				If isMouseOn Then
					If player\selectedItem = Null Then
						If MouseHit1 Then
							player\selectedItem = player\openInventory\items[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If player\openInventory\items[n]\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\openInventory\items[n]\itemtemplate\sound))
								CurrGameState = GAMESTATE_PLAYING
								DoubleClick = False
							EndIf
						EndIf
					EndIf
				EndIf
			Else
				If isMouseOn And MouseHit1 Then
					For z% = 0 To player\openInventory\size - 1
						If player\openInventory\items[z] = player\selectedItem Then player\openInventory\items[z] = Null
					Next
					player\openInventory\items[n] = player\selectedItem
				End If
			EndIf
			
			If isMouseOn Then
				MouseSlot = n
			EndIf
			
			x=x+width + spacing
			If n Mod 5 = 4 Then 
				y = y + height*2 
				x = userOptions\screenWidth / 2 - (width * itemsPerRow + spacing * (itemsPerRow - 1)) / 2
			EndIf
		Next
		
		If player\selectedItem <> Null Then
			If Not MouseDown1 Then
				If MouseSlot = 66 Then
					DropItem(player\selectedItem)		
					
					player\selectedItem = Null		
					CurrGameState = GAMESTATE_PLAYING	
					
					MoveMouse viewport_center_x, viewport_center_y
				Else
					If player\openInventory\items[MouseSlot] = Null Then
						For z% = 0 To player\openInventory\size - 1
							If player\openInventory\items[z] = player\selectedItem Then player\openInventory\items[z] = Null
						Next
						player\openInventory\items[MouseSlot] = player\selectedItem
						player\selectedItem = Null
					ElseIf player\openInventory\items[MouseSlot] <> player\selectedItem Then
						Select player\selectedItem\itemtemplate\tempname
							Case "paper","key1","key2","key3","key4","key5","misc","oldpaper","badge","ticket" ;BoH stuff
								If player\openInventory\items[MouseSlot]\itemtemplate\tempname = "clipboard" Then
									;Add an item to clipboard
									Local added.Items = Null
									If player\selectedItem\itemtemplate\tempname<>"misc" Or (player\selectedItem\itemtemplate\name="Playing Card" Or player\selectedItem\itemtemplate\name="Mastercard") Then
										For c% = 0 To player\openInventory\items[MouseSlot]\inventory\size-1
											If (player\openInventory\items[MouseSlot]\inventory\items[c] = Null)
												If player\selectedItem <> Null Then
													player\openInventory\items[MouseSlot]\inventory\items[c] = player\selectedItem
													player\openInventory\items[MouseSlot]\state = 1.0
													SetAnimTime player\openInventory\items[MouseSlot]\model,0.0
													player\openInventory\items[MouseSlot]\invimg = player\inventory\items[MouseSlot]\itemtemplate\invimg
													
													For ri% = 0 To player\openInventory\size - 1
														If player\openInventory\items[ri] = player\selectedItem Then
															player\openInventory\items[ri] = Null
															PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))
														EndIf
													Next
													added = player\selectedItem
													player\selectedItem = Null : Exit
												EndIf
											EndIf
										Next
										If player\selectedItem <> Null Then
											Msg = "The paperclip is not strong enough to hold any more items."
										Else
											If added\itemtemplate\tempname = "paper" Or added\itemtemplate\tempname = "oldpaper" Then
												Msg = "This document was added to the clipboard."
											ElseIf added\itemtemplate\tempname = "badge"
												Msg = added\itemtemplate\name + " was added to the clipboard."
											Else
												Msg = "The " + added\itemtemplate\name + " was added to the clipboard."
											EndIf
											
										EndIf
										MsgTimer = 70 * 5
									Else
										Msg = "You cannot combine these two items."
										MsgTimer = 70 * 5
									EndIf
								EndIf
								player\selectedItem = Null
								
							Case "battery", "bat"
								Select player\openInventory\items[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										If player\selectedItem\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))	
										RemoveItem (player\selectedItem)
										player\selectedItem = Null
										player\openInventory\items[MouseSlot]\state = 100.0
										Msg = "You replaced the navigator's battery."
										MsgTimer = 70 * 5
									Case "S-NAV Navigator Ultimate"
										Msg = "There seems to be no place for batteries in this navigator."
										MsgTimer = 70 * 5
									Case "Radio Transceiver"
										Select player\openInventory\items[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												Msg = "There seems to be no place for batteries in this radio."
												MsgTimer = 70 * 5
											Case "18vradio"
												Msg = "The battery does not fit inside this radio."
												MsgTimer = 70 * 5
											Case "radio"
												If player\selectedItem\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))	
												RemoveItem (player\selectedItem)
												player\selectedItem = Null
												player\openInventory\items[MouseSlot]\state = 100.0
												Msg = "You replaced the radio's battery."
												MsgTimer = 70 * 5
										End Select
									Case "Night Vision Goggles"
										Local nvname$ = player\openInventory\items[MouseSlot]\itemtemplate\tempname
										If nvname$="nvgoggles" Or nvname$="supernv" Then
											If player\selectedItem\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))	
											RemoveItem (player\selectedItem)
											player\selectedItem = Null
											player\openInventory\items[MouseSlot]\state = 1000.0
											Msg = "You replaced the goggles' battery."
											MsgTimer = 70 * 5
										Else
											Msg = "There seems to be no place for batteries in these night vision goggles."
											MsgTimer = 70 * 5
										EndIf
									Default
										Msg = "You cannot combine these two items."
										MsgTimer = 70 * 5	
								End Select
							Case "18vbat"
								Select player\openInventory\items[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										Msg = "The battery does not fit inside this navigator."
										MsgTimer = 70 * 5
									Case "S-NAV Navigator Ultimate"
										Msg = "There seems to be no place for batteries in this navigator."
										MsgTimer = 70 * 5
									Case "Radio Transceiver"
										Select player\openInventory\items[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												Msg = "There seems to be no place for batteries in this radio."
												MsgTimer = 70 * 5		
											Case "18vradio"
												If player\selectedItem\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))	
												RemoveItem (player\selectedItem)
												player\selectedItem = Null
												player\openInventory\items[MouseSlot]\state = 100.0
												Msg = "You replaced the radio's battery."
												MsgTimer = 70 * 5
										End Select 
									Default
										Msg = "You cannot combine these two items."
										MsgTimer = 70 * 5	
								End Select
							Default
								Msg = "You cannot combine these two items."
								MsgTimer = 70 * 5
						End Select					
					End If
					
				End If
				player\selectedItem = Null
			End If
		End If
		
		If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
		
		If CurrGameState = GAMESTATE_PLAYING Then 
			ResumeSounds() 
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
		EndIf
	Else
		If player\selectedItem <> Null Then
			Select player\selectedItem\itemtemplate\tempname
				Case "1123"
					If player\currRoom\RoomTemplate\Name <> "room1123" Then
						ShowEntity player\overlays[OVERLAY_WHITE]
						player\lightFlash = 7
						PlaySound(LoadTempSound("SFX\SCP\1123\Touch.ogg"))		
						DeathMSG = "Subject D-9341 was shot dead after attempting to attack a member of Nine-Tailed Fox. Surveillance tapes show that the subject had been "
						DeathMSG = DeathMSG + "wandering around the site approximately 9 minutes prior, shouting the phrase " + Chr(34) + "get rid of the four pests" + Chr(34)
						DeathMSG = DeathMSG + " in chinese. SCP-1123 was found in [REDACTED] nearby, suggesting the subject had come into physical contact with it. How "
						DeathMSG = DeathMSG + "exactly SCP-1123 was removed from its containment chamber is still unknown."
						Kill(player)
						Return
					EndIf
					For e.Events = Each Events
						If e\EventName = "room1123" Then 
							If e\EventState = 0 Then
								ShowEntity player\overlays[OVERLAY_WHITE]
								player\lightFlash = 3
								PlaySound(LoadTempSound("SFX\SCP\1123\Touch.ogg"))											
							EndIf
							e\EventState = Max(1, e\EventState)
							Exit
						EndIf
					Next
				Case "battery","key1", "key2", "key3", "key4", "key5", "scp860", "hand", "hand2"
					;do nothing
				Case "scp513"
					PlaySound LoadTempSound("SFX\SCP\513\Bell"+Rand(1,3)+".ogg")
					
					temp = True
					For np.NPCs = Each NPCs
						If np\NPCtype = NPCtype5131 Then
							temp = False
							Exit
						EndIf
					Next
					If temp = True Then
						CreateNPC(NPCtype5131, 0,0,0)
					EndIf	
					player\selectedItem = Null
				Case "scp500"
					If (player\injuries > 0 Or player\bloodloss > 0) And player\infect008 > 0 Then
						Msg = "You swallowed. Your wounds are healing rapidly and your nausea is fading."
					ElseIf player\infect008 > 0 Then
						Msg = "You swallowed. Your nausea is fading."
					Else
						Msg = "You swallowed. Your wounds are healing rapidly."
					EndIf
					MsgTimer = 70*7
					
					player\dead = False
					player\fallTimer = 0
					player\injuries = 0
					player\bloodloss = 0
					player\infect008 = 0
					player\stamina = 100
					
					RemoveItem(player\selectedItem)
					player\selectedItem = Null
					
				Case "veryfinefirstaid"
					Select Rand(5)
						Case 1
							player\injuries = 3.5
							Msg = "You started bleeding heavily."
							MsgTimer = 70*7
						Case 2
							player\injuries = 0
							player\bloodloss = 0
							Msg = "Your wounds are healing up rapidly."
							MsgTimer = 70*7
						Case 3
							player\injuries = Max(0, player\injuries - Rnd(0.5,3.5))
							player\bloodloss = Max(0, player\bloodloss - Rnd(10,100))
							Msg = "You feel much better."
							MsgTimer = 70*7
						Case 4
							player\blurTimer = 10000
							player\bloodloss = 0
							Msg = "You feel nauseated."
							MsgTimer = 70*7
						Case 5
							player\blinkTimer = -10
							For r.Rooms = Each Rooms
								If r\RoomTemplate\Name = "pocketdimension" Then
									PositionEntity(player\collider, EntityX(r\obj),0.8,EntityZ(r\obj))		
									ResetEntity player\collider									
									UpdateDoors()
									UpdateRooms()
									PlaySound(Use914SFX)
									player\dropSpeed = 0
									Curr106\State = -2500
									Exit
								EndIf
							Next
							Msg = "For some inexplicable reason. You find yourself inside the pocket dimension."
							MsgTimer = 70*8
					End Select
					
					RemoveItem(player\selectedItem)
				Case "firstaid", "finefirstaid", "firstaid2"
					If player\bloodloss = 0 And player\injuries = 0 Then
						Msg = "You do not need to use a first aid right now."
						MsgTimer = 70*5
						player\selectedItem = Null
					Else
						player\moveSpeed = CurveValue(0, player\moveSpeed, 5.0)
						player\crouching = True
						
						player\selectedItem\state = Min(player\selectedItem\state+(timing\tickDuration/5.0),100)			
						
						If player\selectedItem\state = 100 Then
							If player\selectedItem\itemtemplate\tempname = "finefirstaid" Then
								player\bloodloss = 0
								player\injuries = Max(0, player\injuries - 2.0)
								If player\injuries = 0 Then
									Msg = "You bandaged the wounds and took a painkiller. You feel fine."
								ElseIf player\injuries > 1.0
									Msg = "You bandaged the wounds and took a painkiller, but you were not able to stop the bleeding."
								Else
									Msg = "You bandaged the wounds and took a painkiller, but you still feel sore."
								EndIf
								MsgTimer = 70*5
								RemoveItem(player\selectedItem)
							Else
								player\bloodloss = Max(0, player\bloodloss - Rand(10,20))
								If player\injuries => 2.5 Then
									Msg = "The wounds were way too severe to staunch the bleeding completely."
									player\injuries = Max(2.5, player\injuries-Rnd(0.3,0.7))
								ElseIf player\injuries > 1.0
									player\injuries = Max(0.5, player\injuries-Rnd(0.5,1.0))
									If player\injuries > 1.0 Then
										Msg = "You bandaged the wounds but were unable to staunch the bleeding completely."
									Else
										Msg = "You managed to stop the bleeding."
									EndIf
								Else
									If player\injuries > 0.5 Then
										player\injuries = 0.5
										Msg = "You took a painkiller, easing the pain slightly."
									Else
										player\injuries = 0.5
										Msg = "You took a painkiller, but it still hurts to walk."
									EndIf
								EndIf
								
								If player\selectedItem\itemtemplate\tempname = "firstaid2" Then 
									Select Rand(6)
										Case 1
											player\superMan = 1.0
											Msg = "You have becomed overwhelmedwithadrenalineholyshitWOOOOOO~!"
										Case 2
											userOptions\invertMouseY = (Not userOptions\invertMouseY)
											Msg = "You suddenly find it very difficult to turn your head."
										Case 3
											player\blurTimer = 5000
											Msg = "You feel nauseated."
										Case 4
											player\blinkEffect = 0.6
											;BlinkEffectTimer = 70*Rand(20,30)
										Case 5
											player\bloodloss = 0
											player\injuries = 0
											Msg = "You bandaged the wounds. The bleeding stopped completely and you feel fine."
										Case 6
											Msg = "You bandaged the wounds and blood started pouring heavily through the bandages."
											player\injuries = 3.5
									End Select
								EndIf
								
								MsgTimer = 70*5
								RemoveItem(player\selectedItem)
							EndIf							
						EndIf
						
					EndIf
				Case "eyedrops"
					player\blinkEffect = 0.6
					;BlinkEffectTimer = 70*Rand(20,30)
					player\blurTimer = 200
					RemoveItem(player\selectedItem)
				Case "fineeyedrops"
					player\blinkEffect = 0.4
					;BlinkEffectTimer = 70*Rand(30,40)
					player\bloodloss = Max(player\bloodloss-1.0, 0)
					player\blurTimer = 200
					RemoveItem(player\selectedItem)
				Case "supereyedrops"
					player\blinkEffect = 0.0
					;TODO: fix
					;BlinkEffectTimer = 60
					;EyeStuck = 10000
					player\blurTimer = 1000
					RemoveItem(player\selectedItem)					
				Case "paper", "ticket"
					;do nothing
				Case "cup"
					
					player\selectedItem\name = Trim(Lower(player\selectedItem\name))
					If Left(player\selectedItem\name, Min(6,Len(player\selectedItem\name))) = "cup of" Then
						player\selectedItem\name = Right(player\selectedItem\name, Len(player\selectedItem\name)-7)
					ElseIf Left(player\selectedItem\name, Min(8,Len(player\selectedItem\name))) = "a cup of" 
						player\selectedItem\name = Right(player\selectedItem\name, Len(player\selectedItem\name)-9)
					EndIf
					
					;the state of refined items is more than 1.0 (fine setting increases it by 1, very fine doubles it)
					x2 = (player\selectedItem\state+1.0)
					
					Local iniStr$ = "DATA\SCP-294.ini"
					
					Local loc% = GetINISectionLocation(iniStr, player\selectedItem\name)
					
					;Stop
					
					strtemp = GetINIString2(iniStr, loc, "message")
					If strtemp <> "" Then Msg = strtemp : MsgTimer = 70*6
					
					If GetINIInt2(iniStr, loc, "lethal") Then 
						DeathMSG = GetINIString2(iniStr, loc, "deathmessage")
						Kill(player)
					EndIf
					player\blurTimer = GetINIInt2(iniStr, loc, "blur")*70;*temp
					player\injuries = Max(player\injuries + GetINIInt2(iniStr, loc, "damage"),0);*temp
					player\bloodloss = Max(player\bloodloss + GetINIInt2(iniStr, loc, "blood loss"),0);*temp
					strtemp =  GetINIString2(iniStr, loc, "sound")
					If strtemp<>"" Then
						PlaySound LoadTempSound(strtemp)
					EndIf
					;If GetINIInt2(iniStr, loc, "stomachache") Then ;TODO: fix
					
					;TODO: fix
					;DeathTimer=GetINIInt2(iniStr, loc, "deathtimer")*70
					
					;BlinkEffect = (BlinkEffect + Float(GetINIString2(iniStr, loc, "blinkeffect", 1.0))*x2)/2.0
					;BlinkEffectTimer = (BlinkEffectTimer + Float(GetINIString2(iniStr, loc, "blinkeffecttimer", 1.0))*x2)/2.0
					
					;StaminaEffect = (StaminaEffect + Float(GetINIString2(iniStr, loc, "stamina effect", 1.0))*x2)/2.0
					;StaminaEffectTimer = (StaminaEffectTimer + Float(GetINIString2(iniStr, loc, "staminaeffecttimer", 1.0))*x2)/2.0
					
					strtemp = GetINIString2(iniStr, loc, "refusemessage")
					If strtemp <> "" Then
						Msg = strtemp 
						MsgTimer = 70*6		
					Else
						it.Items = CreateItem("Empty Cup", "emptycup", 0,0,0)
						it\Picked = True
						For i = 0 To player\openInventory\size-1
							If player\openInventory\items[i]=player\selectedItem Then player\openInventory\items[i] = it : Exit
						Next					
						EntityType (it\collider, HIT_ITEM)
						
						RemoveItem(player\selectedItem)						
					EndIf
					
				Case "radio","18vradio","fineradio","veryfineradio"
					If player\selectedItem\state <= 100 Then player\selectedItem\state = Max(0, player\selectedItem\state - timing\tickDuration * 0.004)
					
					;radiostate(5) = has the "use the number keys" -message been shown yet (true/false)
					;radiostate(6) = a timer for the "code channel"
					;RadioState(7) = another timer for the "code channel"
					
					If RadioState(5) = 0 Then 
						Msg = "Use the numbered keys 1 through 5 to cycle between various channels."
						MsgTimer = 70 * 5
						RadioState(5) = 1
						RadioState(0) = -1
					EndIf
					
					If player\selectedItem\state > 0 Then
						;TODO: remove coffindistance
						If player\currRoom\RoomTemplate\Name = "pocketdimension" Or CoffinDistance < 4.0 Then
							ResumeChannel(RadioCHN(5))
							If IsChannelPlaying(RadioCHN(5)) = False Then RadioCHN(5) = PlaySound(RadioStatic)	
						Else
							Select Int(player\selectedItem\state2)
								Case 0 ;randomkanava
									ResumeChannel(RadioCHN(0))
									If IsChannelPlaying(RadioCHN(0)) = False Then
										RadioCHN(0) = PlaySound(RadioStatic)
									EndIf
								Case 1 ;hälytyskanava
									DebugLog RadioState(1) 
									
									ResumeChannel(RadioCHN(1))
									;strtemp = "        WARNING - CONTAINMENT BREACH          "
									If IsChannelPlaying(RadioCHN(1)) = False Then
										
										If RadioState(1) => 5 Then
											RadioCHN(1) = PlaySound(RadioSFX(1,1))	
											RadioState(1) = 0
										Else
											RadioState(1)=RadioState(1)+1	
											RadioCHN(1) = PlaySound(RadioSFX(1,0))	
										EndIf
										
									EndIf
									
								Case 2 ;scp-radio
									ResumeChannel(RadioCHN(2))
									;strtemp = "        SCP Foundation On-Site Radio          "
									If IsChannelPlaying(RadioCHN(2)) = False Then
										RadioState(2)=RadioState(2)+1
										If RadioState(2) = 17 Then RadioState(2) = 1
										If Floor(RadioState(2)/2)=Ceil(RadioState(2)/2) Then ;parillinen, soitetaan normiviesti
											RadioCHN(2) = PlaySound(RadioSFX(2,Int(RadioState(2)/2)))	
										Else ;pariton, soitetaan musiikkia
											RadioCHN(2) = PlaySound(RadioSFX(2,0))
										EndIf
									EndIf 
								Case 3
									ResumeChannel(RadioCHN(3))
									;strtemp = "             EMERGENCY CHANNEL - RESERVED FOR COMMUNICATION IN THE EVENT OF A CONTAINMENT BREACH         "
									If IsChannelPlaying(RadioCHN(3)) = False Then RadioCHN(3) = PlaySound(RadioStatic)
									
									If MTFtimer > 0 Then 
										RadioState(3)=RadioState(3)+Max(Rand(-10,1),0)
										Select RadioState(3)
											Case 40
												If Not RadioState3(0) Then
													RadioCHN(3) = PlaySound(LoadTempSound("SFX\Character\MTF\Random1.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(0) = True	
												EndIf											
											Case 400
												If Not RadioState3(1) Then
													RadioCHN(3) = PlaySound(LoadTempSound("SFX\Character\MTF\Random2.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(1) = True	
												EndIf	
											Case 800
												If Not RadioState3(2) Then
													RadioCHN(3) = PlaySound(LoadTempSound("SFX\Character\MTF\Random3.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(2) = True
												EndIf													
											Case 1200
												If Not RadioState3(3) Then
													RadioCHN(3) = PlaySound(LoadTempSound("SFX\Character\MTF\Random4.ogg"))	
													RadioState(3) = RadioState(3)+1	
													RadioState3(3) = True
												EndIf		
										End Select
									EndIf
								Case 4
									ResumeChannel(RadioCHN(6)) ;taustalle kohinaa
									If IsChannelPlaying(RadioCHN(6)) = False Then RadioCHN(6) = PlaySound(RadioStatic)									
									
									ResumeChannel(RadioCHN(4))
									If IsChannelPlaying(RadioCHN(4)) = False Then 
										If RemoteDoorOn = False And RadioState(8) = False Then
											RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\Chatter3.ogg"))	
											RadioState(8) = True
										Else
											RadioState(4)=RadioState(4)+Max(Rand(-10,1),0)
											
											Select RadioState(4)
												Case 10
													If Not RadioState4(0) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\OhGod.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(0) = True
													EndIf													
												Case 100
													If Not RadioState4(1) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\Chatter2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(1) = True
													EndIf		
												Case 158
													If MTFtimer = 0 And (Not RadioState4(2)) Then 
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\franklin1.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState(2) = True
													EndIf
												Case 200
													If Not RadioState4(3) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\Chatter4.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(3) = True
													EndIf		
												Case 260
													If Not RadioState4(4) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\SCP\035\RadioHelp1.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(4) = True
													EndIf		
												Case 300
													If Not RadioState4(5) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\Chatter1.ogg"))	
														RadioState(4) = RadioState(4)+1	
														RadioState4(5) = True
													EndIf		
												Case 350
													If Not RadioState4(6) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\franklin2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(6) = True
													EndIf		
												Case 400
													If Not RadioState4(7) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\SCP\035\RadioHelp2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(7) = True
													EndIf		
												Case 450
													If Not RadioState4(8) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\franklin3.ogg"))	
														RadioState(4) = RadioState(4)+1		
														RadioState4(8) = True
													EndIf		
												Case 600
													If Not RadioState4(9) Then
														RadioCHN(4) = PlaySound(LoadTempSound("SFX\radio\franklin4.ogg"))	
														RadioState(4) = RadioState(4)+1	
														RadioState4(9) = True
													EndIf		
											End Select
										EndIf
									EndIf
									
									
								Case 5
									ResumeChannel(RadioCHN(5))
									If IsChannelPlaying(RadioCHN(5)) = False Then RadioCHN(5) = PlaySound(RadioStatic)
							End Select 
							
							x=x+66
							y=y+419
							
							If player\selectedItem\state <= 100 Then
								;Text (x - 60, y - 20, "BATTERY")
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(player\selectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							If player\selectedItem\itemtemplate\tempname = "veryfineradio" Then ;"KOODIKANAVA"
								ResumeChannel(RadioCHN(0))
								If IsChannelPlaying(RadioCHN(0)) = False Then RadioCHN(0) = PlaySound(RadioStatic)
								
								;radiostate(7)=kuinka mones piippaus menossa
								;radiostate(8)=kuinka mones access coden numero menossa
								RadioState(6)=RadioState(6) + timing\tickDuration
								temp = Mid(Str(AccessCode),RadioState(8)+1,1)
								If RadioState(6)-timing\tickDuration =< RadioState(7)*50 And RadioState(6)>RadioState(7)*50 Then
									PlaySound(RadioBuzz)
									RadioState(7)=RadioState(7)+1
									If RadioState(7)=>temp Then
										RadioState(7)=0
										RadioState(6)=-100
										RadioState(8)=RadioState(8)+1
										If RadioState(8)=4 Then RadioState(8)=0 : RadioState(6)=-200
									EndIf
								EndIf
							Else
								For i = 2 To 6
									If KeyHit(i) Then
										If player\selectedItem\state2 <> i-2 Then ;pausetetaan nykyinen radiokanava
											PlaySound RadioSquelch
											If RadioCHN(Int(player\selectedItem\state2)) <> 0 Then PauseChannel(RadioCHN(Int(player\selectedItem\state2)))
										EndIf
										player\selectedItem\state2 = i-2
										;jos nykyistä kanavaa ollaan soitettu, laitetaan jatketaan toistoa samasta kohdasta
										If RadioCHN(player\selectedItem\state2)<>0 Then ResumeChannel(RadioCHN(player\selectedItem\state2))
									EndIf
								Next
							EndIf
						EndIf
					EndIf
				Case "cigarette"
					If player\selectedItem\state = 0 Then
						Select Rand(6)
							Case 1
								Msg = Chr(34)+"I don't have anything to light it with. Umm, what about that... Nevermind."+Chr(34)
							Case 2
								Msg = "You are unable to get lit."
							Case 3
								Msg = Chr(34)+"I quit that a long time ago."+Chr(34)
								RemoveItem(player\selectedItem)
							Case 4
								Msg = Chr(34)+"Even if I wanted one, I have nothing to light it with."+Chr(34)
							Case 5
								Msg = Chr(34)+"Could really go for one now... Wish I had a lighter."+Chr(34)
							Case 6
								Msg = Chr(34)+"Don't plan on starting, even at a time like this."+Chr(34)
								RemoveItem(player\selectedItem)
						End Select
						player\selectedItem\state = 1 
					Else
						Msg = "You are unable to get lit."
					EndIf
					
					MsgTimer = 70 * 5
				Case "420"
					Msg = Chr(34) + "MAN DATS SUM GOOD ASS SHIT" + Chr(34)
					player\injuries = Max(player\injuries-0.5, 0)
					player\blurTimer = 500
					PlaySound LoadTempSound("SFX\Music\420J.ogg")
					MsgTimer = 70 * 5
					RemoveItem(player\selectedItem)
				Case "420s"
					DeathMSG = "Subject D-9341 found in a comatose state in [DATA REDACTED]. The subject was holding what appears to be a cigarette while smiling widely. "
					DeathMSG = DeathMSG+"Chemical analysis of the cigarette has been inconclusive, although it seems to contain a high concentration of an unidentified chemical "
					DeathMSG = DeathMSG+"whose molecular structure is remarkably similar to that of tetrahydrocannabinol."
					Msg = Chr(34) + "UH WHERE... WHAT WAS I DOING AGAIN... MAN I NEED TO TAKE A NAP..." + Chr(34)
					Kill(player)
					MsgTimer = 70 * 6
					RemoveItem(player\selectedItem)
				Case "navigator", "nav"
					If player\selectedItem\state <= 100 Then player\selectedItem\state = Max(0, player\selectedItem\state - timing\tickDuration * 0.005)
				Case "coin"
					If player\selectedItem\state = 0
						PlaySound LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
					EndIf
					
					Msg = ""
					
					player\selectedItem\state = 1
					player\selectedItem = Null
				Default
					If player\selectedItem\inventory<>Null Then
						;check if the item is an inventory-type object
						DoubleClick = 0
						MouseHit1 = 0
						MouseDown1 = 0
						LastMouseHit1 = 0
						CurrGameState = GAMESTATE_INVENTORY
						player\openInventory = player\selectedItem\inventory
						player\selectedItem = Null
					ElseIf player\selectedItem\itemtemplate\invSlot <> WORNITEM_SLOT_NONE Then
						;check if the item is equippable
						EquipItem(player,player\selectedItem,True)
						player\selectedItem = Null
					EndIf
			End Select
			
			If player\selectedItem <> Null Then
				If player\selectedItem\itemtemplate\img <> 0
					Local IN$ = player\selectedItem\itemtemplate\tempname
					If IN$ = "paper" Or IN$ = "badge" Or IN$ = "oldpaper" Or IN$ = "ticket" Then
						For a_it.Items = Each Items
							If a_it <> player\selectedItem
								Local IN2$ = a_it\itemtemplate\tempname
								If IN2$ = "paper" Or IN2$ = "badge" Or IN2$ = "oldpaper" Or IN2$ = "ticket" Then
									If a_it\itemtemplate\img<>0
										If a_it\itemtemplate\img <> player\selectedItem\itemtemplate\img
											FreeImage(a_it\itemtemplate\img)
											a_it\itemtemplate\img = 0
										EndIf
									EndIf
								EndIf
							EndIf
						Next
					EndIf
				EndIf			
			EndIf
			
			If MouseHit2 Then
				EntityAlpha player\overlays[OVERLAY_BLACK], 0.0
				
				If player\selectedItem\itemtemplate\sound <> 66 Then PlaySound(PickSFX(player\selectedItem\itemtemplate\sound))
				player\selectedItem = Null
			EndIf
		EndIf
	EndIf
	
	If mainPlayer\selectedItem = Null Then
		For i = 0 To 6
			If RadioCHN(i) <> 0 Then 
				If IsChannelPlaying(RadioCHN(i)) Then PauseChannel(RadioCHN(i))
			EndIf
		Next
	EndIf 
	
	If PrevInvOpen And (CurrGameState<>GAMESTATE_INVENTORY) Then MoveMouse viewport_center_x, viewport_center_y
End Function

Function IsPlayerWearingTempName(player.Player,templateName$)
	Local it.ItemTemplates = FindItemTemplate("",templateName)
	If it=Null Then Return False
	Local slot% = it\invSlot
	If slot=WORNITEM_SLOT_NONE Then Return False
	If player\wornItems[slot]=Null Then Return False
	Return (player\wornItems[slot]\itemtemplate\tempname=templateName)
End Function

Function IsPlayerWearingItem(player.Player,item.Items)
	If item = Null Then
		Return False
	EndIf
	Local slot% = item\itemtemplate\invSlot
	If slot=WORNITEM_SLOT_NONE Then Return False
	If player\wornItems[slot]=Null Then Return False
	Return (player\wornItems[slot] = item)
End Function

Global TakeOffStuff.MarkedForRemoval

Function Kill(player.Player)
	If player\godMode Then Return
	
	If player\breathChn <> 0 Then
		If IsChannelPlaying(player\breathChn) Then StopChannel(player\breathChn)
	EndIf
	
	If Not player\dead Then
		;KillAnim = Rand(0,1)
		PlaySound(DamageSFX(0))
		If SelectedDifficulty\permaDeath Then
			DeleteFile(CurrentDir() + SavePath + CurrSave+"\save.txt") 
			DeleteDir(SavePath + CurrSave)
			LoadSaveGames()
		End If
		
		player\dead = True
		player\fallTimer = Min(-1,player\fallTimer)
		ShowEntity player\head
		PositionEntity(player\head, EntityX(player\cam, True), EntityY(player\cam, True), EntityZ(player\cam, True), True)
		ResetEntity(player\head)
		RotateEntity(player\head, 0, EntityYaw(player\cam), 0)		
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
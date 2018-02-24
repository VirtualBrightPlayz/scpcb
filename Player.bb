Const WORNITEM_SLOT_COUNT%=3
Const WORNITEM_HEAD_SLOT%=0
Const WORNITEM_BODY_SLOT%=1
Const WORNITEM_HAND_SLOT%=2

Const OVERLAY_COUNT%=7
Const OVERLAY_BLACK%=0
Const OVERLAY_WHITE%=1
Const OVERLAY_FOG%=2
Const OVERLAY_GASMASK%=3
Const OVERLAY_NIGHTVISION%=4
Const OVERLAY_008%=5
Const OVERLAY_178%=6

;TODO: remove after cleanup
Global FogTexture.MarkedForRemoval, Fog.MarkedForRemoval
Global GasMaskTexture.MarkedForRemoval, GasMaskOverlay.MarkedForRemoval
Global InfectTexture.MarkedForRemoval, InfectOverlay.MarkedForRemoval
Global DarkTexture.MarkedForRemoval, Dark.MarkedForRemoval

Global GlassesTexture.MarkedForRemoval, GlassesOverlay.MarkedForRemoval

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
	
	Field wornItems.Items[WORNITEM_SLOT_COUNT]
	;------
	
	;sound channels
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
	
	Local fogTexture% = LoadTexture_Strict("GFX\fog.jpg", 1)
	;FogNVTexture = LoadTexture_Strict("GFX\fogNV.jpg", 1)
	
	Local scaleWidth# = userOptions\screenWidth / 1024.0
	Local scaleHeight# = MenuScale * 0.8
	
	;TODO: take ownership of ark_blur_cam
	Local gasMaskTexture = LoadTexture_Strict("GFX\GasmaskOverlay.jpg", 1)
	player\overlays[OVERLAY_GASMASK] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_GASMASK], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_GASMASK], gasMaskTexture)
	EntityBlend(player\overlays[OVERLAY_GASMASK], 2)
	EntityFX(player\overlays[OVERLAY_GASMASK], 1)
	EntityOrder player\overlays[OVERLAY_GASMASK], -1003
	MoveEntity(player\overlays[OVERLAY_GASMASK], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_GASMASK])
	
	Local infectTexture = LoadTexture_Strict("GFX\InfectOverlay.jpg", 1)
	player\overlays[OVERLAY_008] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_008], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_008], infectTexture)
	EntityBlend (player\overlays[OVERLAY_008], 3)
	EntityFX(player\overlays[OVERLAY_008], 1)
	EntityOrder player\overlays[OVERLAY_008], -1003
	MoveEntity(player\overlays[OVERLAY_008], 0, 0, 1.0)
	;EntityAlpha (InfectOverlay, 255.0)
	HideEntity(player\overlays[OVERLAY_008])
	
	Local nvTexture = LoadTexture_Strict("GFX\NightVisionOverlay.jpg", 1)
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
	
	Local glassesTexture = LoadTexture_Strict("GFX\GlassesOverlay.jpg",1)
	player\overlays[OVERLAY_178] = CreateSprite(ark_blur_cam)
	ScaleSprite(player\overlays[OVERLAY_178], Max(scaleWidth, 1.0), Max(scaleHeight, 0.8))
	EntityTexture(player\overlays[OVERLAY_178], glassesTexture)
	EntityBlend (player\overlays[OVERLAY_178], 2)
	EntityFX(player\overlays[OVERLAY_178], 1)
	EntityOrder player\overlays[OVERLAY_178], -1003
	MoveEntity(player\overlays[OVERLAY_178], 0, 0, 1.0)
	HideEntity(player\overlays[OVERLAY_178])
	
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
	SetBuffer TextureBuffer(LightTexture)
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
	EntityRadius player\collider, 0.15
	EntityType player\collider, HIT_PLAYER
End Function

Function DeletePlayer(player.Player)
	;TODO: delete/drop worn items, delete inventory
	DeleteInventory(player\inventory)
	Delete player
End Function

;TODO: move these into the player struct and give them more appropriate names
Global Collider.MarkedForRemoval, Head.MarkedForRemoval
Global Camera.MarkedForRemoval, CameraShake.MarkedForRemoval, CurrCameraZoom.MarkedForRemoval

Global DropSpeed.MarkedForRemoval, HeadDropSpeed.MarkedForRemoval, CurrSpeed.MarkedForRemoval
Global user_camera_pitch.MarkedForRemoval, side.MarkedForRemoval
Global Crouch.MarkedForRemoval, CrouchState.MarkedForRemoval

Global PlayerZone.MarkedForRemoval;, mainPlayer\currRoom.Rooms

Global GrabbedEntity.MarkedForRemoval

Global Shake.MarkedForRemoval

Global HeartBeatRate.MarkedForRemoval, HeartBeatTimer.MarkedForRemoval, HeartBeatVolume.MarkedForRemoval

Global WearingGasMask.MarkedForRemoval, WearingHazmat.MarkedForRemoval, WearingVest.MarkedForRemoval, Wearing714.MarkedForRemoval, WearingNightVision.MarkedForRemoval, Wearing178.MarkedForRemoval
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

;TODO: Murder.
Global SCP1025state.MarkedForRemoval[6]

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
		
		mainPlayer\superMan=mainPlayer\superMan+FPSfactor
		
		mainPlayer\camShake = Sin(mainPlayer\superMan / 5.0) * (mainPlayer\superMan / 1500.0)
		
		If mainPlayer\superMan > 70 * 50 Then
			DeathMSG = "A Class D jumpsuit found in [DATA REDACTED]. Upon further examination, the jumpsuit was found to be filled with 12.5 kilograms of blue ash-like substance. "
			DeathMSG = DeathMSG + "Chemical analysis of the substance remains non-conclusive. Most likely related to SCP-914."
			Kill()
			ShowEntity mainPlayer\overlays[OVERLAY_FOG];Fog
		Else
			mainPlayer\blurTimer = 500
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
		EndIf
	End If
	
	;If DeathTimer > 0 Then
	;	DeathTimer=DeathTimer-FPSfactor
	;	If DeathTimer < 1 Then DeathTimer = -1.0
	;ElseIf DeathTimer < 0 
	;	Kill()
	;EndIf
	
	mainPlayer\stamina = Min(mainPlayer\stamina + 0.15 * FPSfactor, 100.0)
	
	If mainPlayer\staminaEffect > 1 Then
		mainPlayer\staminaEffect = mainPlayer\staminaEffect - (FPSfactor/70)
	Else
		If mainPlayer\staminaEffect <> 1.0 Then mainPlayer\staminaEffect = 1.0
		mainPlayer\staminaEffect = CurveValue(1.0, mainPlayer\staminaEffect, 50)
	EndIf
	
	;If mainPlayer\currRoom\RoomTemplate\Name<>"pocketdimension" Then 
	;	If KeyDown(keyBinds\sprint) Then
	;		If mainPlayer\stamina < 5 Then
	;			If ChannelPlaying(mainPlayer\breathChn)=False Then mainPlayer\breathChn = PlaySound_Strict(BreathSFX((WearingGasMask>0), 0))
	;		ElseIf mainPlayer\stamina < 50
	;			If mainPlayer\breathChn=0 Then
	;				mainPlayer\breathChn = PlaySound_Strict(BreathSFX((WearingGasMask>0), Rand(1,3)))
	;				ChannelVolume mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\SoundVolume
	;			Else
	;				If ChannelPlaying(mainPlayer\breathChn)=False Then
	;					mainPlayer\breathChn = PlaySound_Strict(BreathSFX((WearingGasMask>0), Rand(1,3)))
	;					ChannelVolume mainPlayer\breathChn, Min((70.0-mainPlayer\stamina)/70.0,1.0)*userOptions\SoundVolume			
	;				EndIf
	;			EndIf
	;		EndIf
	;	EndIf
	;EndIf
	
	For i%=0 To mainPlayer\inventory\size-1
		If (mainPlayer\inventory\items[i]<>Null) Then
			If (mainPlayer\inventory\items[i]\itemtemplate\tempname="finevest") Then mainPlayer\stamina = Min(mainPlayer\stamina,60.0)
		EndIf
	Next
	
	If IsPlayerWearing(mainPlayer,"scp714",WORNITEM_HAND_SLOT) Then 
		mainPlayer\stamina = Min(mainPlayer\stamina, 10)
		mainPlayer\sanity895 = Max(-850, mainPlayer\sanity895)
	EndIf
	
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
				mainPlayer\stamina = mainPlayer\stamina - FPSfactor * 0.5 * (1.0/mainPlayer\staminaEffect)
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
			If (Not mainPlayer\disableControls) Then mainPlayer\camAnimState = (mainPlayer\camAnimState + FPSfactor * Min(Sprint, 1.5) * 7) Mod 720
			If temp < 180 And (mainPlayer\camAnimState Mod 360) >= 180 And (Not mainPlayer\dead) Then
				;TODO: define constants for each override state
				If mainPlayer\footstepOverride=0 Then
					temp = GetStepSound(mainPlayer\collider)
					
					If Sprint = 1.0 Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)
						tempchn% = PlaySound_Strict(StepSFX(temp, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\SoundVolume
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)
						tempchn% = PlaySound_Strict(StepSFX(temp, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\SoundVolume
					End If
				ElseIf mainPlayer\footstepOverride=1 Then
					tempchn% = PlaySound_Strict(Step2SFX(Rand(0, 2)))
					ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\SoundVolume
				ElseIf mainPlayer\footstepOverride=2 Then
					tempchn% = PlaySound_Strict(Step2SFX(Rand(3,5)))
					ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.4))*userOptions\SoundVolume
				ElseIf mainPlayer\footstepOverride=3 Then
					If Sprint = 1.0 Then
						mainPlayer\loudness = Max(4.0,mainPlayer\loudness)
						tempchn% = PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\SoundVolume
					Else
						mainPlayer\loudness = Max(2.5-(mainPlayer\crouching*0.6),mainPlayer\loudness)
						tempchn% = PlaySound_Strict(StepSFX(0, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(mainPlayer\crouching*0.6))*userOptions\SoundVolume
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
		
		If KeyDown(keyBinds\down) Then MoveEntity mainPlayer\collider, 0, 0, -temp2*FPSfactor
		If KeyDown(keyBinds\up) Then MoveEntity mainPlayer\collider, 0, 0, temp2*FPSfactor
		
		If KeyDown(keyBinds\lft) Then MoveEntity mainPlayer\collider, -temp2*FPSfactor, 0, 0
		If KeyDown(keyBinds\rght) Then MoveEntity mainPlayer\collider, temp2*FPSfactor, 0, 0	
		
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
		
		If (Not mainPlayer\disableControls) Then TranslateEntity mainPlayer\collider, Cos(angle)*mainPlayer\moveSpeed * FPSfactor, 0, Sin(angle)*mainPlayer\moveSpeed * FPSfactor, True
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(mainPlayer\collider)
			If CollisionY(mainPlayer\collider, i) < EntityY(mainPlayer\collider) - 0.25 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			If mainPlayer\dropSpeed# < - 0.07 Then 
				If mainPlayer\footstepOverride=0 Then
					PlaySound_Strict(StepSFX(GetStepSound(mainPlayer\collider), 0, Rand(0, 7)))					
				ElseIf mainPlayer\footstepOverride=1
					PlaySound_Strict(Step2SFX(Rand(0, 2)))
				ElseIf mainPlayer\footstepOverride=2
					PlaySound_Strict(Step2SFX(Rand(3, 5)))
				ElseIf mainPlayer\footstepOverride=3
					PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
				EndIf
				mainPlayer\loudness = Max(3.0,mainPlayer\loudness)
			EndIf
			mainPlayer\dropSpeed# = 0
		Else
			mainPlayer\dropSpeed# = Min(Max(mainPlayer\dropSpeed - 0.006 * FPSfactor, -2.0), 0.0)
		EndIf	
		
		If (Not mainPlayer\disableControls) Then TranslateEntity mainPlayer\collider, 0, mainPlayer\dropSpeed * FPSfactor, 0
	EndIf
	
	mainPlayer\forceMove = 0.0
	
	If mainPlayer\injuries > 1.0 Then
		temp2 = mainPlayer\bloodloss
		mainPlayer\blurTimer = Max(Max(Sin(MilliSecs2()/100.0)*mainPlayer\bloodloss*30.0,mainPlayer\bloodloss*2*(2.0-mainPlayer\crouchState)),mainPlayer\blurTimer)
		mainPlayer\bloodloss = Min(mainPlayer\bloodloss + (Min(mainPlayer\injuries,3.5)/300.0)*FPSfactor,100)
		
		If temp2 <= 60 And mainPlayer\bloodloss > 60 Then
			Msg = "You are feeling faint from the amount of blood you loss."
			MsgTimer = 70*4
		EndIf
	EndIf
	
	UpdateInfect()
	
	If mainPlayer\bloodloss > 0 Then
		If Rnd(200)<Min(mainPlayer\injuries,4.0) Then
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\collider)+Rnd(-0.05,0.05),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)+Rnd(-0.05,0.05)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.decals = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\size = Rnd(0.03,0.08)*Min(mainPlayer\injuries,3.0) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\size, de\size
			tempchn% = PlaySound_Strict (DripSFX(Rand(0,2)))
			ChannelVolume tempchn, Rnd(0.0,0.8)*userOptions\soundVolume
			ChannelPitch tempchn, Rand(20000,30000)
			
			FreeEntity pvt
		EndIf
		
		mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(MilliSecs2())/20.0)+1.0)*mainPlayer\bloodloss*0.2)
		
		If mainPlayer\bloodloss > 60 Then mainPlayer\crouching = True
		If mainPlayer\bloodloss => 100 Then 
			Kill()
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
		tempchn = PlaySound_Strict (HeartBeatSFX)
		ChannelVolume tempchn, Max(Min((mainPlayer\heartbeatIntensity-80.0)/60.0,1.0),0.0)*userOptions\SoundVolume
		
		mainPlayer\heartbeatIntensity = mainPlayer\heartbeatIntensity - FPSfactor
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
	wearingGasMask = IsPlayerWearing(mainPlayer,"gasmask",WORNITEM_HEAD_SLOT)
	If Not wearingGasMask Then
		wearingGasMask = IsPlayerWearing(mainPlayer,"supergasmask",WORNITEM_HEAD_SLOT)*2
	EndIf
	
	Local wearingHazmat%
	wearingHazmat = IsPlayerWearing(mainPlayer,"hazmatsuit",WORNITEM_BODY_SLOT)
	If Not wearingHazmat Then
		wearingHazmat = IsPlayerWearing(mainPlayer,"hazmatsuit2",WORNITEM_BODY_SLOT)*2
	EndIf
	
	Local wearing1499%
	wearing1499 = IsPlayerWearing(mainPlayer,"scp1499",WORNITEM_HEAD_SLOT)
	If Not wearing1499 Then
		wearing1499 = IsPlayerWearing(mainPlayer,"super1499",WORNITEM_HEAD_SLOT)*2
	EndIf
	
	Local wearingNightVision%
	wearingNightVision = IsPlayerWearing(mainPlayer,"nvgoggles",WORNITEM_HEAD_SLOT)
	If Not wearingNightVision Then
		wearingNightVision = IsPlayerWearing(mainPlayer,"supernv",WORNITEM_HEAD_SLOT)*2
	EndIf
	
	mainPlayer\camShake = Max(mainPlayer\camShake - (FPSfactor / 10), 0)
	
	;CameraZoomTemp = CurveValue(mainPlayer\camZoom,CameraZoomTemp, 5.0)
	CameraZoom(mainPlayer\cam, Min(1.0+(mainPlayer\camZoom/400.0),1.1))
	mainPlayer\camZoom = Max(mainPlayer\camZoom - FPSfactor, 0)
	
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
		
		;käännetään kameraa sivulle jos pelaaja on vammautunut
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
		Local the_yaw# = ((mouse_x_speed_1#)) * mouselook_x_inc# / (1.0+IsPlayerWearing(mainPlayer,"vest",WORNITEM_BODY_SLOT))
		Local the_pitch# = ((mouse_y_speed_1#)) * mouselook_y_inc# / (1.0+IsPlayerWearing(mainPlayer,"vest",WORNITEM_BODY_SLOT))
		
		TurnEntity mainPlayer\collider, 0.0, -the_yaw#, 0.0 ; Turn the user on the Y (yaw) axis.
		mainPlayer\headPitch# = mainPlayer\headPitch# + the_pitch#
		; -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
		If mainPlayer\headPitch# > 70.0 Then mainPlayer\headPitch# = 70.0
		If mainPlayer\headPitch# < - 70.0 Then mainPlayer\headPitch# = -70.0
		
		RotateEntity mainPlayer\cam, WrapAngle(mainPlayer\headPitch + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), WrapAngle(EntityYaw(mainPlayer\collider) + Rnd(-mainPlayer\camShake, mainPlayer\camShake)), roll ; Pitch the user;s camera up And down.
		
		If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Then
			If EntityY(mainPlayer\collider)<2000*RoomScale Or EntityY(mainPlayer\collider)>2608*RoomScale Then
				RotateEntity mainPlayer\cam, WrapAngle(EntityPitch(mainPlayer\cam)),WrapAngle(EntityYaw(mainPlayer\cam)), roll+WrapAngle(Sin(MilliSecs2()/150.0)*30.0) ; Pitch the user;s camera up And down.
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
			
			;HeadDropSpeed# = HeadDropSpeed - 0.002 * FPSfactor
		EndIf
		
		If userOptions\invertMouseY Then
			TurnEntity (mainPlayer\cam, -MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
		Else
			TurnEntity (mainPlayer\cam, MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
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
		If wearingGasMask = 2 Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*FPSfactor)
		If wearing1499 = 2 Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*FPSfactor)
		If wearingHazmat = 2 Then 
			mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*FPSfactor)
		ElseIf WearingHazmat=1
			mainPlayer\stamina = Min(60, mainPlayer\stamina)
		EndIf
		
		ShowEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	Else
		HideEntity(mainPlayer\overlays[OVERLAY_GASMASK])
	End If
	
	If (Not WearingNightVision=0) Then
		ShowEntity(mainPlayer\overlays[OVERLAY_NIGHTVISION])
		If WearingNightVision=2 Then
			EntityColor(mainPlayer\overlays[OVERLAY_NIGHTVISION], 0,100,255)
			AmbientLightRooms(15)
		ElseIf WearingNightVision=3 Then
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
	
	;TODO: cleanup
	If IsPlayerWearing(mainPlayer,"scp178",WORNITEM_HEAD_SLOT) Then
		If Music(14)=0 Then Music(14)=LoadSound_Strict("SFX\Music\178.ogg")
		ShouldPlay = 14
		ShowEntity(mainPlayer\overlays[OVERLAY_178])
	Else
		HideEntity(mainPlayer\overlays[OVERLAY_178])
	EndIf
	
	canSpawn178%=0
	
	If Not IsPlayerWearing(mainPlayer,"scp178",WORNITEM_HEAD_SLOT) Then
		For n.NPCs = Each NPCs
			If (n\NPCtype = NPCtype178) Then
				If n\State3>0 Then canSpawn178=1
				If (n\State<=0) And (n\State3=0) Then
					RemoveNPC(n)
				Else If EntityDistance(mainPlayer\collider,n\Collider)>HideDistance*1.5 Then
					RemoveNPC(n)
				EndIf
			EndIf
		Next
	EndIf
	
	If (canSpawn178=1) Or IsPlayerWearing(mainPlayer,"scp178",WORNITEM_HEAD_SLOT) Then
		tempint%=0
		For n.NPCs = Each NPCs
			If (n\NPCtype = NPCtype178) Then
				tempint=tempint+1
				If EntityDistance(mainPlayer\collider,n\Collider)>HideDistance*1.5 Then
					RemoveNPC(n)
				EndIf
				;If n\State<=0 Then RemoveNPC(n)
			EndIf
		Next
		If tempint<10 Then ;create the npcs
			For w.WayPoints = Each WayPoints
				Local dist#
				dist=EntityDistance(mainPlayer\collider,w\obj)
				If (dist<HideDistance*1.5) And (dist>1.2) And (w\door = Null) And (Rand(0,1)=1) Then
					tempint2=True
					For n.NPCs = Each NPCs
						If n\NPCtype=NPCtype178 Then
							If EntityDistance(n\Collider,w\obj)<0.5
								tempint2=False
								Exit
							EndIf
						EndIf
					Next
					If tempint2 Then
						CreateNPC(NPCtype178, EntityX(w\obj,True),EntityY(w\obj,True)+0.15,EntityZ(w\obj,True))
					EndIf	
				EndIf
			Next
		EndIf
	EndIf
	
	;TODO: kill
	;For i = 0 To 5
	;	If SCP1025state[i]>0 Then
	;		Select i
	;			Case 0 ;common cold
	;				If FPSfactor>0 Then 
	;					If Rand(1000)=1 Then
	;						If CoughCHN = 0 Then
	;							CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						Else
	;							If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						End If
	;					EndIf
	;				EndIf
	;				mainPlayer\stamina = mainPlayer\stamina - FPSfactor * 0.3
	;			Case 1 ;chicken pox
	;				If Rand(9000)=1 And Msg="" Then
	;					Msg="Your skin is feeling itchy."
	;					MsgTimer =70*4
	;				EndIf
	;			Case 2 ;cancer of the lungs
	;				If FPSfactor>0 Then 
	;					If Rand(800)=1 Then
	;						If CoughCHN = 0 Then
	;							CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						Else
	;							If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						End If
	;					EndIf
	;				EndIf
	;				mainPlayer\stamina = mainPlayer\stamina - FPSfactor * 0.1
	;			Case 3 ;appendicitis
	;				;0.035/sec = 2.1/min
	;				SCP1025state[i]=SCP1025state[i]+FPSfactor*0.0005
	;				If SCP1025state[i]>20.0 Then
	;					If SCP1025state[i]-FPSfactor<=20.0 Then Msg="The pain in your stomach is becoming unbearable."
	;					mainPlayer\stamina = mainPlayer\stamina - FPSfactor * 0.3
	;				ElseIf SCP1025state[i]>10.0
	;					If SCP1025state[i]-FPSfactor<=10.0 Then Msg="Your stomach is aching."
	;				EndIf
	;			Case 4 ;asthma
	;				If mainPlayer\stamina < 35 Then
	;					If Rand(Int(140+mainPlayer\stamina*8))=1 Then
	;						If CoughCHN = 0 Then
	;							CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						Else
	;							If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
	;						End If
	;					EndIf
	;					mainPlayer\moveSpeed = CurveValue(0, mainPlayer\moveSpeed, 10+mainPlayer\stamina*15)
	;				EndIf
	;			Case 5;cardiac arrest
	;				SCP1025state[i]=SCP1025state[i]+FPSfactor*0.35
	;				;35/sec
	;				If SCP1025state[i]>110 Then
	;					HeartBeatRate=0
	;					mainPlayer\blurTimer = Max(mainPlayer\blurTimer, 500)
	;					If SCP1025state[i]>140 Then 
	;						DeathMSG = Chr(34)+"He died of a cardiac arrest after reading SCP-1025, that's for sure. Is there such a thing as psychosomatic cardiac arrest, or does SCP-1025 have some "
	;						DeathMSG = DeathMSG + "anomalous properties we are not yet aware of?"+Chr(34)
	;						Kill()
	;					EndIf
	;				Else
	;					HeartBeatRate=Max(HeartBeatRate, 70+SCP1025state[i])
	;					HeartBeatVolume = 1.0
	;				EndIf
	;		End Select 
	;	EndIf
	;Next
End Function

Function IsPlayerWearing(player.Player,templateName$,slot%)
	If player\wornItems[slot]=Null Then Return False
	Return (player\wornItems[slot]\itemtemplate\tempname=templateName)
End Function

Function TakeOffStuff(flag%=0)
	;FLAG variables:
		;1: GasMask
		;2: Hazmat Suit
		;4: SCP-714
		;8: SCP-178
		;16: Kevlar Vest
		;32: Night Vision Goggles
		;64: SCP-1499
	
	;TODO: remove?
	;Local numb_flag% = Bin(flag%)
	;
	;If Right(numb_flag%,1) = 1
	;	WearingGasMask = False
	;	DebugLog "GasMask Off"
	;EndIf
	;If Len(numb_flag%)>1
	;	If Mid(numb_flag%,Len(numb_flag%)-1,1) = 1
	;		WearingHazmat = False
	;		For i = 0 To MaxItemAmount-1
	;			If Inventory(i) <> Null Then
	;				If Inventory(i)\itemtemplate\name = "Hazmat Suit" Or Inventory(i)\itemtemplate\tempname = "hazmatsuit3"
	;					DropItem(Inventory(i))
	;					Exit
	;				EndIf
	;			EndIf
	;		Next
	;		DebugLog "Hazmat Off"
	;	EndIf
	;EndIf
	;If Len(numb_flag%)>2
	;	If Mid(numb_flag%,Len(numb_flag%)-2,1) = 1
	;		Wearing714 = False
	;		DebugLog "SCP-714 Off"
	;	EndIf
	;EndIf
	;If Len(numb_flag%)>3
	;	If Mid(numb_flag%,Len(numb_flag%)-3,1) = 1
	;		Wearing178 = False
	;		DebugLog "SCP-178 Off"
	;	EndIf
	;EndIf
	;If Len(numb_flag%)>4
	;	If Mid(numb_flag%,Len(numb_flag%)-4,1) = 1
	;		WearingVest = False
	;		DebugLog "Kevlar Off"
	;	EndIf
	;EndIf
	;If Len(numb_flag%)>5
	;	If Mid(numb_flag%,Len(numb_flag%)-5,1) = 1
	;		WearingNightVision = False
	;		CameraFogFar = StoredCameraFogFar
	;		DebugLog "NVG Off"
	;	EndIf
	;EndIf
	;If Len(numb_flag%)>6
	;	If Mid(numb_flag%,Len(numb_flag%)-6,1) = 1
	;		Wearing1499 = False
	;		DebugLog "SCP-1499 Off"
	;	EndIf
	;EndIf
	
End Function

Function Kill()
	If mainPlayer\godMode Then Return
	
	If mainPlayer\breathChn <> 0 Then
		If ChannelPlaying(mainPlayer\breathCHN) Then StopChannel(mainPlayer\breathCHN)
	EndIf
	
	If Not mainPlayer\dead Then
		;KillAnim = Rand(0,1)
		PlaySound_Strict(DamageSFX(0))
		If SelectedDifficulty\permaDeath Then
			DeleteFile(CurrentDir() + SavePath + CurrSave+"\save.txt") 
			DeleteDir(SavePath + CurrSave)
			LoadSaveGames()
		End If
		
		mainPlayer\dead = True
		mainPlayer\fallTimer = Min(-1,mainPlayer\fallTimer)
		;KillTimer = Min(-1, KillTimer)
		ShowEntity mainPlayer\head
		PositionEntity(mainPlayer\head, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), True)
		ResetEntity (mainPlayer\head)
		RotateEntity(mainPlayer\head, 0, EntityYaw(mainPlayer\cam), 0)		
	EndIf
End Function
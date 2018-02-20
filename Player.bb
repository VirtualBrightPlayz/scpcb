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
	;------------
	
	;movement states
	Field crouching%
	Field crouchState#
	
	Field moveSpeed#
	Field dropSpeed#
	
	Field blinkTimer#
	Field stamina#
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
	;------
	
	Field inventory.Inventory
	
	Field wornItems.Items[WORNITEM_SLOT_COUNT]
	
	Field currRoom.Rooms
	
	Field godMode%
	Field noclip%
End Type
Global mainPlayer.Player = Null

Function CreatePlayer.Player()
	Local player.Player = New Player
	
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
End Function

;TODO: move these into the player struct and give them more appropriate names
Global Collider.MarkedForRemoval, Head.MarkedForRemoval
Global Camera.MarkedForRemoval, CameraShake.MarkedForRemoval, CurrCameraZoom.MarkedForRemoval

Global DropSpeed.MarkedForRemoval, HeadDropSpeed.MarkedForRemoval, CurrSpeed.MarkedForRemoval
Global user_camera_pitch.MarkedForRemoval, side.MarkedForRemoval
Global Crouch.MarkedForRemoval, CrouchState.MarkedForRemoval

Global PlayerZone.MarkedForRemoval;, PlayerRoom.Rooms

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

;TODO: Murder.
;Global SCP1025state#[6]

;TODO: maybe remove?
Const RefinedItems%=0

Const LightBlink#=0, LightFlash#=0

Const BlurVolume#=0, BlurTimer#=0

Const PlayTime%=0 ;TODO: do we even need this?

;TODO: this is all bad
Const PlayerSoundVolume#=0

Const InfiniteStamina%=0

Const NVBlink%=0
Const IsNVGBlinking%=0

Const CameraFogNear#=0
Const CameraFogFar#=0

Const StoredCameraFogFar# = 0

Function MovePlayer()
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#
	
	If SuperMan Then
		Speed = Speed * 3
		
		SuperManTimer=SuperManTimer+FPSfactor
		
		CameraShake = Sin(SuperManTimer / 5.0) * (SuperManTimer / 1500.0)
		
		If SuperManTimer > 70 * 50 Then
			DeathMSG = "A Class D jumpsuit found in [DATA REDACTED]. Upon further examination, the jumpsuit was found to be filled with 12.5 kilograms of blue ash-like substance. "
			DeathMSG = DeathMSG + "Chemical analysis of the substance remains non-conclusive. Most likely related to SCP-914."
			Kill()
			ShowEntity Fog
		Else
			BlurTimer = 500
			HideEntity Fog
		EndIf
	End If
	
	If DeathTimer > 0 Then
		DeathTimer=DeathTimer-FPSfactor
		If DeathTimer < 1 Then DeathTimer = -1.0
	ElseIf DeathTimer < 0 
		Kill()
	EndIf
	
	Stamina = Min(Stamina + 0.15 * FPSfactor, 100.0)
	
	If StaminaEffectTimer > 0 Then
		StaminaEffectTimer = StaminaEffectTimer - (FPSfactor/70)
	Else
		If StaminaEffect <> 1.0 Then StaminaEffect = 1.0
		StaminaEffect = CurveValue(1.0, StaminaEffect, 50)
	EndIf
	
	If PlayerRoom\RoomTemplate\Name<>"pocketdimension" Then 
		If KeyDown(KEY_SPRINT) Then
			If Stamina < 5 Then
				If ChannelPlaying(BreathCHN)=False Then BreathCHN = PlaySound_Strict(BreathSFX((WearingGasMask>0), 0))
			ElseIf Stamina < 50
				If BreathCHN=0 Then
					BreathCHN = PlaySound_Strict(BreathSFX((WearingGasMask>0), Rand(1,3)))
					ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*userOptions\soundVolume
				Else
					If ChannelPlaying(BreathCHN)=False Then
						BreathCHN = PlaySound_Strict(BreathSFX((WearingGasMask>0), Rand(1,3)))
						ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*userOptions\soundVolume			
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	For i = 0 To MaxItemAmount-1
		If Inventory(i)<>Null Then
			If Inventory(i)\itemtemplate\tempname = "finevest" Then Stamina = Min(Stamina, 60)
		EndIf
	Next
	
	If Wearing714 Then 
		Stamina = Min(Stamina, 10)
		Sanity = Max(-850, Sanity)
	EndIf
	
	If IsZombie Then Crouch = False
	
	If Abs(CrouchState-Crouch)<0.001 Then 
		CrouchState = Crouch
	Else
		CrouchState = CurveValue(Crouch, CrouchState, 10.0)
	EndIf
	
	If (Not NoClip) Then 
		If ((KeyDown(KEY_DOWN) Xor KeyDown(KEY_UP)) Or (KeyDown(KEY_RIGHT) Xor KeyDown(KEY_LEFT)) And Playable) Or ForceMove>0 Then
			
			If Crouch = 0 And (KeyDown(KEY_SPRINT)) And Stamina > 0.0 And (Not IsZombie) Then
				Sprint = 2.5
				Stamina = Stamina - FPSfactor * 0.5 * StaminaEffect
				If Stamina <= 0 Then Stamina = -20.0
			End If
			
			If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then 
				If EntityY(Collider)<2000*RoomScale Or EntityY(Collider)>2608*RoomScale Then
					Stamina = 0
					Speed = 0.015
					Sprint = 1.0					
				EndIf
			EndIf	
			
			If ForceMove>0 Then Speed=Speed*ForceMove
			
			If SelectedItem<>Null Then
				If SelectedItem\itemtemplate\tempname = "firstaid" Or SelectedItem\itemtemplate\tempname = "finefirstaid" Or SelectedItem\itemtemplate\tempname = "firstaid2" Then 
					Sprint = 0
				EndIf
			EndIf
			
			Local temp# = (Shake Mod 360), tempchn%
			If (Not UnableToMove%) Then Shake# = (Shake + FPSfactor * Min(Sprint, 1.5) * 7) Mod 720
			If temp < 180 And (Shake Mod 360) >= 180 And KillTimer>=0 Then
				If CurrStepSFX=0 Then
					temp = GetStepSound(Collider)
					
					If Sprint = 1.0 Then
						PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
						tempchn% = PlaySound_Strict(StepSFX(temp, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(Crouch*0.6))*userOptions\soundVolume
					Else
						PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
						tempchn% = PlaySound_Strict(StepSFX(temp, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(Crouch*0.6))*userOptions\soundVolume
					End If
				ElseIf CurrStepSFX=1
					tempchn% = PlaySound_Strict(Step2SFX(Rand(0, 2)))
					ChannelVolume tempchn, (1.0-(Crouch*0.4))*userOptions\soundVolume
				ElseIf CurrStepSFX=2
					tempchn% = PlaySound_Strict(Step2SFX(Rand(3,5)))
					ChannelVolume tempchn, (1.0-(Crouch*0.4))*userOptions\soundVolume
				ElseIf CurrStepSFX=3
					If Sprint = 1.0 Then
						PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
						tempchn% = PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(Crouch*0.6))*userOptions\soundVolume
					Else
						PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
						tempchn% = PlaySound_Strict(StepSFX(0, 1, Rand(0, 7)))
						ChannelVolume tempchn, (1.0-(Crouch*0.6))*userOptions\soundVolume
					End If
				EndIf
				
			EndIf	
		EndIf
	Else ;noclip on
		If (KeyDown(KEY_SPRINT)) Then 
			Sprint = 2.5
		ElseIf KeyDown(KEY_CROUCH)
			Sprint = 0.5
		EndIf
	EndIf
	
	If KeyHit(KEY_CROUCH) And Playable Then Crouch = (Not Crouch)
	
	Local temp2# = (Speed * Sprint) / (1.0+CrouchState)
	
	If NoClip Then 
		Shake = 0
		CurrSpeed = 0
		CrouchState = 0
		Crouch = 0
		
		RotateEntity Collider, WrapAngle(EntityPitch(Camera)), WrapAngle(EntityYaw(Camera)), 0
		
		temp2 = temp2 * NoClipSpeed
		
		If KeyDown(KEY_DOWN) Then MoveEntity Collider, 0, 0, -temp2*FPSfactor
		If KeyDown(KEY_UP) Then MoveEntity Collider, 0, 0, temp2*FPSfactor
		
		If KeyDown(KEY_LEFT) Then MoveEntity Collider, -temp2*FPSfactor, 0, 0
		If KeyDown(KEY_RIGHT) Then MoveEntity Collider, temp2*FPSfactor, 0, 0	
		
		ResetEntity Collider
	Else
		temp2# = temp2 / Max((Injuries+3.0)/3.0,1.0)
		If Injuries > 0.5 Then 
			temp2 = temp2*Min((Sin(Shake/2)+1.2),1.0)
		EndIf
		
		temp = False
		If (Not IsZombie%)
			If KeyDown(KEY_DOWN) And Playable Then
				temp = True 
				angle = 180
				If KeyDown(KEY_LEFT) Then angle = 135 
				If KeyDown(KEY_RIGHT) Then angle = -135 
			ElseIf (KeyDown(KEY_UP) And Playable) Then; Or ForceMove>0
				temp = True
				angle = 0
				If KeyDown(KEY_LEFT) Then angle = 45 
				If KeyDown(KEY_RIGHT) Then angle = -45 
			ElseIf ForceMove>0 Then
				temp=True
				angle = ForceAngle
			Else If Playable Then
				If KeyDown(KEY_LEFT) Then angle = 90 : temp = True
				If KeyDown(KEY_RIGHT) Then angle = -90 : temp = True 
			EndIf
		Else
			temp=True
			angle = ForceAngle
		EndIf
		
		angle = WrapAngle(EntityYaw(Collider,True)+angle+90.0)
		
		If temp Then 
			CurrSpeed = CurveValue(temp2, CurrSpeed, 20.0)
		Else
			CurrSpeed = Max(CurveValue(0.0, CurrSpeed-0.1, 1.0),0.0)
		EndIf
		
		If (Not UnableToMove%) Then TranslateEntity Collider, Cos(angle)*CurrSpeed * FPSfactor, 0, Sin(angle)*CurrSpeed * FPSfactor, True
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(Collider)
			If CollisionY(Collider, i) < EntityY(Collider) - 0.25 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			If DropSpeed# < - 0.07 Then 
				If CurrStepSFX=0 Then
					PlaySound_Strict(StepSFX(GetStepSound(Collider), 0, Rand(0, 7)))					
				ElseIf CurrStepSFX=1
					PlaySound_Strict(Step2SFX(Rand(0, 2)))
				ElseIf CurrStepSFX=2
					PlaySound_Strict(Step2SFX(Rand(3, 5)))
				ElseIf CurrStepSFX=3
					PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
				EndIf
				PlayerSoundVolume = Max(3.0,PlayerSoundVolume)
			EndIf
			DropSpeed# = 0
		Else
			DropSpeed# = Min(Max(DropSpeed - 0.006 * FPSfactor, -2.0), 0.0)
		EndIf	
		
		If (Not UnableToMove%) Then TranslateEntity Collider, 0, DropSpeed * FPSfactor, 0
	EndIf
	
	ForceMove = False
	
	If Injuries > 1.0 Then
		temp2 = Bloodloss
		BlurTimer = Max(Max(Sin(MilliSecs2()/100.0)*Bloodloss*30.0,Bloodloss*2*(2.0-CrouchState)),BlurTimer)
		Bloodloss = Min(Bloodloss + (Min(Injuries,3.5)/300.0)*FPSfactor,100)
		
		If temp2 <= 60 And Bloodloss > 60 Then
			Msg = "You are feeling faint from the amount of blood you loss."
			MsgTimer = 70*4
		EndIf
	EndIf
	
	UpdateInfect()
	
	If Bloodloss > 0 Then
		If Rnd(200)<Min(Injuries,4.0) Then
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.decals = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\size = Rnd(0.03,0.08)*Min(Injuries,3.0) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\size, de\size
			tempchn% = PlaySound_Strict (DripSFX(Rand(0,2)))
			ChannelVolume tempchn, Rnd(0.0,0.8)*userOptions\soundVolume
			ChannelPitch tempchn, Rand(20000,30000)
			
			FreeEntity pvt
		EndIf
		
		CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs2())/20.0)+1.0)*Bloodloss*0.2)
		
		If Bloodloss > 60 Then Crouch = True
		If Bloodloss => 100 Then 
			Kill()
			HeartBeatVolume = 0.0
		ElseIf Bloodloss > 80.0
			HeartBeatRate = Max(150-(Bloodloss-80)*5,HeartBeatRate)
			HeartBeatVolume = Max(HeartBeatVolume, 0.75+(Bloodloss-80.0)*0.0125)	
		ElseIf Bloodloss > 35.0
			HeartBeatRate = Max(70+Bloodloss,HeartBeatRate)
			HeartBeatVolume = Max(HeartBeatVolume, (Bloodloss-35.0)/60.0)			
		EndIf
	EndIf
	
	If Playable Then
		If KeyHit(KEY_BLINK) Then BlinkTimer = 0
		If KeyDown(KEY_BLINK) And BlinkTimer < - 10 Then BlinkTimer = -10
	EndIf
	
	
	If HeartBeatVolume > 0 Then
		If HeartBeatTimer <= 0 Then
			tempchn = PlaySound_Strict (HeartBeatSFX)
			ChannelVolume tempchn, HeartBeatVolume*userOptions\soundVolume
			
			HeartBeatTimer = 70.0*(60.0/Max(HeartBeatRate,1.0))
		Else
			HeartBeatTimer = HeartBeatTimer - FPSfactor
		EndIf
		
		HeartBeatVolume = Max(HeartBeatVolume - FPSfactor*0.05, 0)
	EndIf
	
End Function

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
	
	CameraShake = Max(CameraShake - (FPSfactor / 10), 0)
	
	;CameraZoomTemp = CurveValue(CurrCameraZoom,CameraZoomTemp, 5.0)
	CameraZoom(Camera, Min(1.0+(CurrCameraZoom/400.0),1.1))
	CurrCameraZoom = Max(CurrCameraZoom - FPSfactor, 0)
	
	If KillTimer >= 0 And FallTimer >=0 Then
		
		HeadDropSpeed = 0
		
		;If 0 Then 
		;fixing the black screen bug with some bubblegum code 
		Local Zero# = 0.0
		Local Nan1# = 0.0 / Zero
		If Int(EntityX(Collider))=Int(Nan1) Then
			
			PositionEntity Collider, EntityX(Camera, True), EntityY(Camera, True) - 0.5, EntityZ(Camera, True), True
			Msg = "EntityX(Collider) = NaN, RESETTING COORDINATES    -    New coordinates: "+EntityX(Collider)
			MsgTimer = 300				
		EndIf
		;EndIf
		
		Local up# = (Sin(Shake) / (20.0+CrouchState*20.0))*0.6;, side# = Cos(Shake / 2.0) / 35.0		
		Local roll# = Max(Min(Sin(Shake/2)*2.5*Min(Injuries+0.25,3.0),8.0),-8.0)
		
		;käännetään kameraa sivulle jos pelaaja on vammautunut
		;RotateEntity Collider, EntityPitch(Collider), EntityYaw(Collider), Max(Min(up*30*Injuries,50),-50)
		PositionEntity Camera, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
		RotateEntity Camera, 0, EntityYaw(Collider), roll*0.5
		
		MoveEntity Camera, side, up + 0.6 + CrouchState * -0.3, 0
		
		;RotateEntity Collider, EntityPitch(Collider), EntityYaw(Collider), 0
		;moveentity player, side, up, 0	
		; -- Update the smoothing que To smooth the movement of the mouse.
		mouse_x_speed_1# = CurveValue(MouseXSpeed() * (MouseSens + 0.6) , mouse_x_speed_1, 6.0 / (MouseSens + 1.0)) 
		If Int(mouse_x_speed_1) = Int(Nan1) Then mouse_x_speed_1 = 0
		
		If InvertMouse Then
			mouse_y_speed_1# = CurveValue(-MouseYSpeed() * (MouseSens + 0.6), mouse_y_speed_1, 6.0/(MouseSens+1.0)) 
		Else
			mouse_y_speed_1# = CurveValue(MouseYSpeed () * (MouseSens + 0.6), mouse_y_speed_1, 6.0/(MouseSens+1.0)) 
		EndIf
		If Int(mouse_y_speed_1) = Int(Nan1) Then mouse_y_speed_1 = 0
		
		Local the_yaw# = ((mouse_x_speed_1#)) * mouselook_x_inc# / (1.0+WearingVest)
		Local the_pitch# = ((mouse_y_speed_1#)) * mouselook_y_inc# / (1.0+WearingVest)
		
		TurnEntity Collider, 0.0, -the_yaw#, 0.0 ; Turn the user on the Y (yaw) axis.
		user_camera_pitch# = user_camera_pitch# + the_pitch#
		; -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
		If user_camera_pitch# > 70.0 Then user_camera_pitch# = 70.0
		If user_camera_pitch# < - 70.0 Then user_camera_pitch# = -70.0
		
		RotateEntity Camera, WrapAngle(user_camera_pitch + Rnd(-CameraShake, CameraShake)), WrapAngle(EntityYaw(Collider) + Rnd(-CameraShake, CameraShake)), roll ; Pitch the user;s camera up And down.
		
		If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
			If EntityY(Collider)<2000*RoomScale Or EntityY(Collider)>2608*RoomScale Then
				RotateEntity Camera, WrapAngle(EntityPitch(Camera)),WrapAngle(EntityYaw(Camera)), roll+WrapAngle(Sin(MilliSecs2()/150.0)*30.0) ; Pitch the user;s camera up And down.
			EndIf
		EndIf
		
	Else
		HideEntity Collider
		PositionEntity Camera, EntityX(Head), EntityY(Head), EntityZ(Head)
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(Head)
			If CollisionY(Head, i) < EntityY(Head) - 0.01 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			HeadDropSpeed# = 0
		Else
			
			If KillAnim = 0 Then 
				MoveEntity Head, 0, 0, HeadDropSpeed
				RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			Else
				MoveEntity Head, 0, 0, -HeadDropSpeed
				RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			EndIf
			
			HeadDropSpeed# = HeadDropSpeed - 0.002 * FPSfactor
		EndIf
		
		If InvertMouse Then
			TurnEntity (Camera, -MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
		Else
			TurnEntity (Camera, MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
		End If
		
	EndIf
	
	;pölyhiukkasia
	If Rand(35) = 1 Then
		Local pvt% = CreatePivot()
		PositionEntity(pvt, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True))
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
	
	If WearingGasMask Or WearingHazmat Or Wearing1499 Then
		If WearingGasMask = 2 Then Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*FPSfactor)
		If Wearing1499 = 2 Then Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*FPSfactor)
		If WearingHazmat = 2 Then 
			Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*FPSfactor)
		ElseIf WearingHazmat=1
			Stamina = Min(60, Stamina)
		EndIf
		
		ShowEntity(GasMaskOverlay)
	Else
		HideEntity(GasMaskOverlay)
	End If
	
	If (Not WearingNightVision=0) Then
		ShowEntity(NVOverlay)
		If WearingNightVision=2 Then
			EntityColor(NVOverlay, 0,100,255)
			AmbientLightRooms(15)
		ElseIf WearingNightVision=3 Then
			EntityColor(NVOverlay, 255,0,0)
			AmbientLightRooms(15)
		Else
			EntityColor(NVOverlay, 0,255,0)
			AmbientLightRooms(15)
		EndIf
		EntityTexture(Fog, FogNVTexture)
	Else
		AmbientLightRooms(0)
		HideEntity(NVOverlay)
		EntityTexture(Fog, FogTexture)
	EndIf
	
	If Wearing178>0 Then
		If Music(14)=0 Then Music(14)=LoadSound_Strict("SFX\Music\178.ogg")
		ShouldPlay = 14
		ShowEntity(GlassesOverlay)
	Else
		HideEntity(GlassesOverlay)
	EndIf
	
	canSpawn178%=0
	
	If Wearing178<>1 Then
		For n.NPCs = Each NPCs
			If (n\NPCtype = NPCtype178) Then
				If n\State3>0 Then canSpawn178=1
				If (n\State<=0) And (n\State3=0) Then
					RemoveNPC(n)
				Else If EntityDistance(Collider,n\Collider)>HideDistance*1.5 Then
					RemoveNPC(n)
				EndIf
			EndIf
		Next
	EndIf
	
	If (canSpawn178=1) Or (Wearing178=1) Then
		tempint%=0
		For n.NPCs = Each NPCs
			If (n\NPCtype = NPCtype178) Then
				tempint=tempint+1
				If EntityDistance(Collider,n\Collider)>HideDistance*1.5 Then
					RemoveNPC(n)
				EndIf
				;If n\State<=0 Then RemoveNPC(n)
			EndIf
		Next
		If tempint<10 Then ;create the npcs
			For w.WayPoints = Each WayPoints
				Local dist#
				dist=EntityDistance(Collider,w\obj)
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
	
	For i = 0 To 5
		If SCP1025state[i]>0 Then
			Select i
				Case 0 ;common cold
					If FPSfactor>0 Then 
						If Rand(1000)=1 Then
							If CoughCHN = 0 Then
								CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							Else
								If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							End If
						EndIf
					EndIf
					Stamina = Stamina - FPSfactor * 0.3
				Case 1 ;chicken pox
					If Rand(9000)=1 And Msg="" Then
						Msg="Your skin is feeling itchy."
						MsgTimer =70*4
					EndIf
				Case 2 ;cancer of the lungs
					If FPSfactor>0 Then 
						If Rand(800)=1 Then
							If CoughCHN = 0 Then
								CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							Else
								If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							End If
						EndIf
					EndIf
					Stamina = Stamina - FPSfactor * 0.1
				Case 3 ;appendicitis
					;0.035/sec = 2.1/min
					SCP1025state[i]=SCP1025state[i]+FPSfactor*0.0005
					If SCP1025state[i]>20.0 Then
						If SCP1025state[i]-FPSfactor<=20.0 Then Msg="The pain in your stomach is becoming unbearable."
						Stamina = Stamina - FPSfactor * 0.3
					ElseIf SCP1025state[i]>10.0
						If SCP1025state[i]-FPSfactor<=10.0 Then Msg="Your stomach is aching."
					EndIf
				Case 4 ;asthma
					If Stamina < 35 Then
						If Rand(Int(140+Stamina*8))=1 Then
							If CoughCHN = 0 Then
								CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							Else
								If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX(Rand(0, 2)))
							End If
						EndIf
						CurrSpeed = CurveValue(0, CurrSpeed, 10+Stamina*15)
					EndIf
				Case 5;cardiac arrest
					SCP1025state[i]=SCP1025state[i]+FPSfactor*0.35
					;35/sec
					If SCP1025state[i]>110 Then
						HeartBeatRate=0
						BlurTimer = Max(BlurTimer, 500)
						If SCP1025state[i]>140 Then 
							DeathMSG = Chr(34)+"He died of a cardiac arrest after reading SCP-1025, that's for sure. Is there such a thing as psychosomatic cardiac arrest, or does SCP-1025 have some "
							DeathMSG = DeathMSG + "anomalous properties we are not yet aware of?"+Chr(34)
							Kill()
						EndIf
					Else
						HeartBeatRate=Max(HeartBeatRate, 70+SCP1025state[i])
						HeartBeatVolume = 1.0
					EndIf
			End Select 
		EndIf
	Next
End Function

Function Kill()
	If GodMode Then Return
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then StopChannel(BreathCHN)
	EndIf
	
	If KillTimer >= 0 Then
		KillAnim = Rand(0,1)
		PlaySound_Strict(DamageSFX(0))
		If SelectedDifficulty\permaDeath Then
			DeleteFile(CurrentDir() + SavePath + CurrSave+"\save.txt") 
			DeleteDir(SavePath + CurrSave)
			LoadSaveGames()
		End If
		
		KillTimer = Min(-1, KillTimer)
		ShowEntity Head
		PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
		ResetEntity (Head)
		RotateEntity(Head, 0, EntityYaw(Camera), 0)		
	EndIf
End Function
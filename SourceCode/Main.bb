Include "SourceCode/MarkedForRemoval.bb"

;TODO: kill all the dims
;I have to place them up here because the includes depend
;on them being declared before they are used
;[Block]

;TODO: Radio struct.
Dim RadioState#(10)
Dim RadioState3%(3)
Dim RadioState4%(9)
Dim RadioCHN%(8)

;TODO: Assets.bb
Dim OldAiPics%(5)

;TODO: Assets.bb
Dim LightSpriteTex%(10)

;TODO: Use struct of file paths.
Dim Music.MarkedForRemoval(40)

;TODO: Audio.bb
Dim OpenDoorSFX.MarkedForRemoval(3,3), CloseDoorSFX.MarkedForRemoval(3,3)

Dim DripSFX.MarkedForRemoval(4)

;TODO: Radio struct.
Dim RadioSFX(5,10)

Dim PickSFX.MarkedForRemoval(10)

Dim AmbientSFXAmount.MarkedForRemoval(6)

Dim AmbientSFX.MarkedForRemoval(6, 15)

;TODO: Move to NPCData106.
Dim OldManSFX.MarkedForRemoval(6)
;For i = 0 To 2
;	OldManSFX(i) = LoadSound("SFX/SCP/106/Corrosion" + (i + 1) + ".ogg")
;Next
;OldManSFX(3) = LoadSound("SFX/SCP/106/Laugh.ogg")
;OldManSFX(4) = LoadSound("SFX/SCP/106/Breathing.ogg")
;OldManSFX(5) = LoadSound("SFX/Room/PocketDimension/Enter.ogg")

;TODO: Most of these sounds are NPC-specific anyway. Divide this up.
Dim HorrorSFX.MarkedForRemoval(20)
;For i = 0 To 10
;	HorrorSFX(i) = LoadSound("SFX/Horror/Horror" + i + ".ogg")
;Next
;For i = 14 To 15
;	HorrorSFX(i) = LoadSound("SFX/Horror/Horror" + i + ".ogg")
;Next

;TODO: Audio.bb
Dim IntroSFX%(20)

;TODO: Audio.bb
Dim AlarmSFX%(5)

Dim CommotionState%(23)

;TODO: Move to NPCData173.
Dim NeckSnapSFX.MarkedForRemoval(3)
;For i = 0 To 2
;	NeckSnapSFX(i) =  LoadSound("SFX/SCP/173/NeckSnap"+(i+1)+".ogg")
;Next

;TODO: Player struct.
Dim DamageSFX%(9)

;TODO: NPCDataMTF.
Dim MTFSFX%(8)

;TODO: Player struct.
Dim CoughSFX%(3)

Dim LightSpriteTex%(5)

Dim DecalTextures%(20)
;[End Block]

Include "SourceCode/KeyName.bb"
Include "SourceCode/Options.bb"
Include "SourceCode/FastResize.bb"
Include "SourceCode/Array.bb"
Include "SourceCode/Difficulty.bb"
Include "SourceCode/Menus/Menu.bb"
Include "SourceCode/Console.bb"
Include "SourceCode/Dreamfilter.bb"
Include "SourceCode/Items.bb"
Include "SourceCode/Particles.bb"
Include "SourceCode/Doors.bb"
Include "SourceCode/Objects.bb"
Include "SourceCode/MapSystem.bb"
Include "SourceCode/NPCs/NPCs.bb"
Include "SourceCode/Player.bb"
Include "SourceCode/Events.bb"
Include "SourceCode/Assets.bb"
Include "SourceCode/Save.bb"
Include "SourceCode/Audio.bb"
Include "SourceCode/MathUtils.bb"
Include "SourceCode/INI.bb"

Const VERSION$ = "1.CBN"

Global Depth.MarkedForRemoval

;TODO: Move somewhere more relevant.
Global WireframeState%
Global HalloweenTex%

Global RealGraphicWidth.MarkedForRemoval
Global RealGraphicHeight.MarkedForRemoval
Global AspectRatioRatio.MarkedForRemoval

Function VerifyResolution%()
	Local selectedMode% = 1

	Local i%
	For i = 1 To CountGfxModes3D()
		If (GfxModeDepth(i) = 32) Then
			If (userOptions\screenWidth = GfxModeWidth(i)) And (userOptions\screenHeight = GfxModeHeight(i)) Then
				selectedMode = i
				Exit
			EndIf
		EndIf
	Next
	
	userOptions\screenWidth = GfxModeWidth(selectedMode)
	userOptions\screenHeight = GfxModeHeight(selectedMode)

	Return selectedMode-1
End Function

Global Font1.MarkedForRemoval, Font2.MarkedForRemoval, Font3.MarkedForRemoval, Font4.MarkedForRemoval, Font5.MarkedForRemoval
Global ConsoleFont.MarkedForRemoval

;TODO: cleanup
Type Timing
	Field tickDuration# ;tick duration (1 = 1/70th of a second) TODO: change this value to be relative to a second
	Field accumulator# ;accumulated steps
	
	Field prevTime%
	Field currTime%
	
	Field fps#
End Type
Global timing.Timing

Function SetTickrate(tickrate%)
	timing\tickDuration = 70.0/Float(tickrate)
End Function

Function AddToTimingAccumulator(milliseconds%)
	If milliseconds<1 Or milliseconds>500 Then
		;DebugLog milliseconds
		Return
	EndIf
	timing\accumulator = timing\accumulator+Max(0,Float(milliseconds)*70.0/1000.0)
End Function

Function ResetTimingAccumulator()
	timing\accumulator = 0.0
End Function

Global CurrFrameLimit# ;TODO: what is this

Const HIT_MAP% = 1, HIT_PLAYER% = 2, HIT_ITEM% = 3, HIT_APACHE% = 4, HIT_DEAD% = 5

Global GameSaved%

;TODO: Player.bb
Global CanSave%

;TODO: Assets.bb
Global CursorIMG.MarkedForRemoval

Global BlinkMeterIMG.MarkedForRemoval

Global MouseHit1%, MouseDown1%, MouseHit2%, DoubleClick%, LastMouseHit1%, MouseUp1%

;TODO: Make this not global.
Global CoffinDistance#

;TODO: Move somewhere?
Global ExplosionTimer#, ExplosionSFX.MarkedForRemoval

Global LightsOn% = True ;secondary lighting on

;TODO: Not global assuming this is for 106's containment chamber.
Global SoundTransmission%

;TODO: Move somewhere?
Global Brightness%

;TODO: Audio.bb
Global SoundEmitter%
Global TempSounds%[10]
Global TempSoundIndex% = 0

;TODO: Audio.bb
Global KeyCardSFX1.MarkedForRemoval
Global KeyCardSFX2.MarkedForRemoval
Global ButtonSFX2.MarkedForRemoval
Global ScannerSFX1.MarkedForRemoval
Global ScannerSFX2.MarkedForRemoval

Global OpenDoorFastSFX.MarkedForRemoval
Global CautionSFX.MarkedForRemoval

Global NuclearSirenSFX.MarkedForRemoval

Global CameraSFX.MarkedForRemoval

Global GunshotSFX.MarkedForRemoval
Global Gunshot2SFX.MarkedForRemoval
Global Gunshot3SFX.MarkedForRemoval
Global BullethitSFX.MarkedForRemoval

Global TeslaIdleSFX.MarkedForRemoval
Global TeslaActivateSFX.MarkedForRemoval
Global TeslaPowerUpSFX.MarkedForRemoval

Global MagnetUpSFX.MarkedForRemoval
Global MagnetDownSFX.MarkedForRemoval
Global FemurBreakerSFX.MarkedForRemoval

Global BurstSFX.MarkedForRemoval

Global Death914SFX.MarkedForRemoval
Global Use914SFX.MarkedForRemoval

Global LeverSFX.MarkedForRemoval
Global LightSFX.MarkedForRemoval

Global ButtGhostSFX.MarkedForRemoval

Global RadioSquelch
Global RadioStatic
Global RadioBuzz

Global ElevatorBeepSFX.MarkedForRemoval
Global ElevatorMoveSFX.MarkedForRemoval

;TODO: More Audio.bb
Global AmbientSFXCHN.MarkedForRemoval, CurrAmbientSFX.MarkedForRemoval

Global HeartBeatSFX.MarkedForRemoval

;TODO: Why the fuck is this a global?
Global MachineSFX.MarkedForRemoval

;TODO: WHY IS THIS A GLOBAL???
Global ApacheSFX.MarkedForRemoval

;TODO: Die forever.
Global NTF_1499EnterSFX.MarkedForRemoval
Global NTF_1499LeaveSFX.MarkedForRemoval

;TODO: Not be globals.
Global Monitor2.MarkedForRemoval, Monitor3.MarkedForRemoval, MonitorTexture2.MarkedForRemoval, MonitorTexture3.MarkedForRemoval, MonitorTexture4.MarkedForRemoval, MonitorTextureOff.MarkedForRemoval
Global MonitorTimer.MarkedForRemoval, MonitorTimer2.MarkedForRemoval
Global UpdateCheckpoint1.MarkedForRemoval, UpdateCheckpoint2.MarkedForRemoval

;TODO: die
;This variable is for when a camera detected the player
	;False: Player is not seen (will be set after every call of the Main Loop
	;True: The Player got detected by a camera
Global PlayerDetected%
Global PrevInjuries#,PrevBloodloss#
Global NoTarget%

;TODO: Assets.bb
Global NVGImages.MarkedForRemoval

Global AmbientLightRoomTex%, AmbientLightRoomVal%

;Global NVGImage% = CreateImage(userOptions\screenWidth,userOptions\screenHeight),NVGCam%

;TODO: Die forever.
Global NTF_1499PrevX#
Global NTF_1499PrevY#
Global NTF_1499PrevZ#
Global NTF_1499PrevRoom.Rooms
Global NTF_1499X#
Global NTF_1499Y#
Global NTF_1499Z#
Global NTF_1499Sky%

;TODO: Redo using CurrGameSubstate
Global OptionsMenu.MarkedForRemoval
Global QuitMSG.MarkedForRemoval

;TODO: Die.
Global InFacility%

;TODO: Remove.
Global IsZombie%

;TODO: This is fucking stupid.
Global room2gw_brokendoor%
Global room2gw_x#
Global room2gw_z#

;TODO: Assets.bb
Global PauseMenuIMG.MarkedForRemoval

;TODO: Assets.bb
Global SprintIcon.MarkedForRemoval
Global BlinkIcon.MarkedForRemoval
Global CrouchIcon.MarkedForRemoval
Global HandIcon.MarkedForRemoval
Global HandIcon2.MarkedForRemoval

Global StaminaMeterIMG.MarkedForRemoval

Global KeypadHUD.MarkedForRemoval

;TODO: cleanup
Global Panel294.MarkedForRemoval
Global Using294.MarkedForRemoval
Global Input294$

;TODO: Assets.bb
Global TeslaTexture%

Global DoorOBJ.MarkedForRemoval, DoorFrameOBJ.MarkedForRemoval

Global LeverOBJ.MarkedForRemoval, LeverBaseOBJ.MarkedForRemoval

Global DoorColl.MarkedForRemoval
Global ButtonOBJ.MarkedForRemoval, ButtonKeyOBJ.MarkedForRemoval, ButtonCodeOBJ.MarkedForRemoval, ButtonScannerOBJ.MarkedForRemoval

Global Monitor%, MonitorTexture%
Global CamBaseOBJ%, CamOBJ%

Global LiquidObj.MarkedForRemoval, MTFObj.MarkedForRemoval, ClassDObj.MarkedForRemoval
Global ApacheObj.MarkedForRemoval, ApacheRotorObj.MarkedForRemoval

Global UnableToMove.MarkedForRemoval

Function Main%()
	keyBinds.KeyBinds = New KeyBinds
	userOptions.Options = New Options
	LoadOptionsINI()
	
	timing = New Timing
	SetTickrate(60)

	sndManager.SoundManager = CreateSoundManager()
	
	If userOptions\launcher Then
		CurrGameState = GAMESTATE_LAUNCHER
		launcher = CreateLauncher()
	Else
		InitializeMainGame()
	EndIf
	
	Repeat
		UpdateGame()
	Forever
End Function

;TODO: Shuffle the initialization of stuff around in between DrawLoading() calls.
Function InitializeMainGame()
	CurrGameState = GAMESTATE_MAINMENU
	CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
	
	Graphics3DExt(userOptions\screenWidth, userOptions\screenHeight, 0, (1 + (Not userOptions\fullscreen)))
	AppTitle "SCP - Containment Breach v"+VERSION
	
	MenuScale = (userOptions\screenHeight / 1024.0)
	
	CurrFrameLimit = userOptions\framelimit
	
	SetBuffer(BackBuffer())
	
	SeedRnd MilliSecs()
	
	CanSave = True
	
	LoadingBack = LoadImage("Loadingscreens/loadingback.jpg")
	InitLoadingScreens("Loadingscreens/loadingscreens.ini")
	
	InitializeUIAssets()

	musicManager.MusicManager = CreateMusicManager()
	SetNextMusicTrack(MUS_EZ, False)
	
	SetFont uiAssets\font[1]
	
	DrawLoading(0, True)
	
	Brightness% = 50
	
	DrawLoading(10, True)
	
	;TODO: doesn't need to be hardcoded
	;0 = light containment, 1 = heavy containment, 2 = entrance
	;AmbientSFXAmount(0)=8 : AmbientSFXAmount(1)=11 : AmbientSFXAmount(2)=12
	;3 = general, 4 = pre-breach
	;AmbientSFXAmount(3)=15 : AmbientSFXAmount(4)=5
	;5 = forest
	;AmbientSFXAmount(5)=10
	
	DrawLoading(20, True)
	
	RadioSFX(1,0) = LoadSound("SFX/Radio/RadioAlarm.ogg")
	RadioSFX(1,1) = LoadSound("SFX/Radio/RadioAlarm2.ogg")
	For i = 0 To 8
		RadioSFX(2,i) = LoadSound("SFX/Radio/scpradio"+i+".ogg")
	Next
	RadioSquelch = LoadSound("SFX/Radio/squelch.ogg")
	RadioStatic = LoadSound("SFX/Radio/static.ogg")
	RadioBuzz = LoadSound("SFX/Radio/buzz.ogg")
	
	DrawLoading(25, True)
	;TODO: Audio.bb
	For i = 7 To 9
		IntroSFX(i) = LoadSound("SFX/Room/Intro/Bang" + (i - 6) + ".ogg")
	Next
	For i = 10 To 12
		IntroSFX(i) = LoadSound("SFX/Room/Intro/Light" + (i - 9) + ".ogg")
	Next
	;IntroSFX(13) = LoadSound("SFX/intro/shoot1.ogg")
	;IntroSFX(14) = LoadSound("SFX/intro/shoot2.ogg")
	IntroSFX(15) = LoadSound("SFX/Room/Intro/173Vent.ogg")
	
	;TODO: Audio.bb
	AlarmSFX(0) = LoadSound("SFX/Alarm/Alarm.ogg")
	;AlarmSFX(1) = LoadSound("SFX/Alarm/Alarm2.ogg")
	AlarmSFX(2) = LoadSound("SFX/Alarm/Alarm3.ogg")
	
	For i = 0 To 8
		DamageSFX(i) = LoadSound("SFX/Character/D9341/Damage"+(i+1)+".ogg")
	Next
	
	;TODO: Player struct.
	For i = 0 To 2
		CoughSFX(i) = LoadSound("SFX/Character/D9341/Cough" + (i + 1) + ".ogg")
	Next
	
	DrawLoading(30, True)
	
	;TODO: Assets.bb
	;NVGImages = LoadAnimImage("GFX/battery.png",64,64,0,2)
	;MaskImage NVGImages,255,0,255
	
	;TODO: Die.
	InFacility% = True
	
	;TODO: Remove.
	IsZombie% = False
	
	;TODO: This is fucking stupid.
	room2gw_brokendoor% = False
	room2gw_x# = 0.0
	room2gw_z# = 0.0
	
	DrawLoading(40,True)
	
	;TODO: remove/replace with functions
	SecondaryLightOn# = True
	RemoteDoorOn = True
	Contained106 = False
	
	DrawLoading(80,True)
	
	viewport_center_x% = userOptions\screenWidth / 2
	viewport_center_y% = userOptions\screenHeight / 2
	
	mouse_left_limit% = 250
	mouse_right_limit% = userOptions\screenWidth - 250
	mouse_top_limit% = 150
	mouse_bottom_limit% = userOptions\screenHeight - 150
	
	Collisions HIT_PLAYER, HIT_MAP, 2, 2
	Collisions HIT_PLAYER, HIT_PLAYER, 1, 3
	Collisions HIT_ITEM, HIT_MAP, 2, 2
	Collisions HIT_APACHE, HIT_APACHE, 1, 2
	Collisions HIT_DEAD, HIT_MAP, 2, 2
	
	DrawLoading(90, True)
	
	LoadSaveGames()
	
	FlushKeys()
	FlushMouse()
	
	DrawLoading(100, True)
End Function

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------       		MAIN LOOP                 ---------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------
Function UpdateGame()
	Local elapsedMilliseconds%
	timing\currTime = MilliSecs()
	elapsedMilliseconds = timing\currTime-timing\prevTime
	AddToTimingAccumulator(elapsedMilliseconds)
	timing\prevTime = timing\currTime
	
	;TODO: remove or replace
	;If userOptions\framelimit > 0 Then
	;    ;Framelimit
	;	Local WaitingTime% = (1000.0 / userOptions\framelimit) - (MilliSecs() - LoopDelay)
	;	Delay WaitingTime%
	;	
	;   LoopDelay = MilliSecs()
	;EndIf
	
	;Counting the fps
	Local instantFramerate# = 1000.0/Max(1,elapsedMilliseconds)
	timing\fps = Max(0,timing\fps*0.99 + instantFramerate*0.01)
	
	;[Block]
	While timing\accumulator>0.0
		timing\accumulator = timing\accumulator-timing\tickDuration
		If timing\accumulator<=0.0 Then CaptureWorld
		
		Cls
		
		DoubleClick = False
		MouseHit1 = MouseHit(1)
		If MouseHit1 Then
			If TimeInPosMilliSecs() - LastMouseHit1 < 800 Then DoubleClick = True
			LastMouseHit1 = TimeInPosMilliSecs()
		EndIf
		
		Local prevmousedown1 = MouseDown1
		MouseDown1 = MouseDown(1)
		If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
		
		MouseHit2 = MouseHit(2)
		;TODO: A better way?
		If (CurrGameState <> GAMESTATE_LAUNCHER) Then
			UpdateMusic()
		EndIf
		
		If CurrGameState=GAMESTATE_LAUNCHER Then
			UpdateLauncher()
		ElseIf CurrGameState=GAMESTATE_MAINMENU Then
			UpdateMainMenu()
		Else
			If (Not MouseDown1) And (Not MouseHit1) Then mainPlayer\grabbedEntity = 0
			
			ShouldPlay = 0 ;TODO: FIX ;Min(PlayerZone,2)
			
			DrawHandIcon = False
			
			If timing\tickDuration > 0 Then UpdateSecurityCams()
			
			If KeyHit(keyBinds\inv) Then
				ToggleInventory(mainPlayer)
			EndIf
			
			If mainPlayer\currRoom\RoomTemplate\name <> "pocketdimension" And mainPlayer\currRoom\RoomTemplate\name <> "gatea" And mainPlayer\currRoom\RoomTemplate\name <> "exit1" And (Not IsPaused()) Then 
				
				If Rand(1500) = 1 Then
					;TODO: reimplement
;					For i = 0 To 5
;						If AmbientSFX(i,CurrAmbientSFX) <> 0 Then
;							If IsChannelPlaying(AmbientSFXCHN) = 0 Then FreeSound AmbientSFX(i,CurrAmbientSFX) : AmbientSFX(i,CurrAmbientSFX) = 0
;						EndIf			
;					Next
;					
;					PositionEntity (SoundEmitter, EntityX(mainPlayer\cam) + Rnd(-1.0, 1.0), 0.0, EntityZ(mainPlayer\cam) + Rnd(-1.0, 1.0))
;					
;					If mainPlayer\currRoom\RoomTemplate\Name = "room860"
;						For e.Events = Each Events
;							If e\EventName = "room860"
;								If e\EventState = 1.0 Then
;									PositionEntity (SoundEmitter, EntityX(mainPlayer\cam) + Rnd(-1.0, 1.0), 30.0, EntityZ(mainPlayer\cam) + Rnd(-1.0, 1.0))
;								EndIf
;								
;								Exit
;							EndIf
;						Next
;					EndIf
					
					;CurrAmbientSFX = Rand(0,AmbientSFXAmount(PlayerZone)-1) ;TODO: fix
					
					;Select PlayerZone
					;	Case 0,1,2
					;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound("SFX/Ambient/Zone"+(PlayerZone+1)+"/ambient"+(CurrAmbientSFX+1)+".ogg")
					;	Case 3
					;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound("SFX/Ambient/General/ambient"+(CurrAmbientSFX+1)+".ogg")
					;	Case 4
					;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound("SFX/Ambient/Pre-breach/ambient"+(CurrAmbientSFX+1)+".ogg")
					;	Case 5
					;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound("SFX/Ambient/Forest/ambient"+(CurrAmbientSFX+1)+".ogg")
					;End Select
					
					;AmbientSFXCHN = PlayRangedSound(AmbientSFX(PlayerZone,CurrAmbientSFX), mainPlayer\cam, SoundEmitter)
				EndIf
				
				If Rand(50000) = 3 Then
					Local RN$ = mainPlayer\currRoom\RoomTemplate\name$
					If RN$ <> "room860" And RN$ <> "room1123" And RN$ <> "173" And RN$ <> "dimension1499" Then
						;If timing\tickDuration > 0 Then LightBlink = Rnd(1.0,2.0)
						PlaySound2  LoadTempSound("SFX/SCP/079/Broadcast"+Rand(1,7)+".ogg")
					EndIf 
				EndIf
			EndIf
			
			;UpdateCheckpoint1 = False
			;UpdateCheckpoint2 = False
			
			If Not IsPaused() Then
				;LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
				;CameraFogRange(mainPlayer\cam, mainPlayer\camFogNear*LightVolume,mainPlayer\camFogFar*LightVolume)
				;CameraFogColor(mainPlayer\cam, 0,0,0)
				;CameraFogMode mainPlayer\cam,1
				;CameraRange(mainPlayer\cam, 0.05, Min(mainPlayer\camFogFar*LightVolume*1.5,28))	
				
				AmbientLight Brightness, Brightness, Brightness	
				mainPlayer\loudness = CurveValue(0.0, mainPlayer\loudness, 5.0)
				
				CanSave% = True
				UpdateEmitters()
				MouseLook()
				MovePlayer()
				InFacility = CheckForPlayerInFacility()
				UpdateDoors()
				UpdateLevers()
				UpdateEvents()
				UpdateDecals()
				UpdateMTF()
				UpdateNPCs()
				UpdateItems()
				UpdateParticles()
				UpdateScreens()
				;TimeCheckpointMonitors()
				UpdateLeave1499()
			EndIf
			
			;If InfiniteStamina Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*timing\tickDuration)
			
			UpdateWorld()
			ManipulateNPCBones()
			UpdateNVG()
			
			mainPlayer\blurTimer = Min(CurveValue(0.0, mainPlayer\blurTimer, 20.0),1000.0)
			;If mainPlayer\blurTimer > 0.0 Then
			;	mainPlayer\blurTimer = Max(Min(0.95, mainPlayer\blurTimer / 1000.0), mainPlayer\blurTimer)
			;	mainPlayer\blurTimer = Max(mainPlayer\blurTimer - timing\tickDuration, 0.0)
			;End If
			
			;[Block]
			
			Local darkA# = 0.0
			If Not IsPaused()  Then
				If mainPlayer\sanity895 < 0 Then
					mainPlayer\sanity895 = Min(mainPlayer\sanity895 + timing\tickDuration, 0.0)
					If mainPlayer\sanity895 < (-200) Then 
						darkA = Max(Min((-mainPlayer\sanity895 - 200) / 700.0, 0.6), darkA)
						If Not mainPlayer\dead Then 
							;HeartBeatVolume = Min(Abs(mainPlayer\sanity895+200)/500.0,1.0)
							mainPlayer\heartbeatIntensity = Max(70 + Abs(mainPlayer\sanity895+200)/6.0,mainPlayer\heartbeatIntensity)
						EndIf
					EndIf
				End If
				
				;TODO: fix
	;			If EyeStuck > 0 Then 
	;				mainPlayer\blinkTimer = mainPlayer\blinkFreq
	;				EyeStuck = Max(EyeStuck-timing\tickDuration,0)
	;				
	;				If EyeStuck < 9000 Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (9000-EyeStuck)*0.5)
	;				If EyeStuck < 6000 Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.0),1.0)
	;				If EyeStuck < 9000 And EyeStuck+timing\tickDuration =>9000 Then 
	;					Msg = "The eyedrops are causing your eyes to tear up."
	;					MsgTimer = 70*6
	;				EndIf
	;			EndIf
				
				If mainPlayer\blinkTimer < 0 Then
					If mainPlayer\blinkTimer > - 5 Then
						darkA = Max(darkA, Sin(Abs(mainPlayer\blinkTimer * 18.0)))
					ElseIf mainPlayer\blinkTimer > - 15
						darkA = 1.0
					Else
						darkA = Max(darkA, Abs(Sin(mainPlayer\blinkTimer * 18.0)))
					EndIf
					
					If mainPlayer\blinkTimer <= - 20 Then
						;Randomizes the frequency of blinking. Scales with difficulty.
						Select SelectedDifficulty\otherFactors
							Case EASY
								mainPlayer\blinkFreq = Rnd(490,700)
							Case NORMAL
								mainPlayer\blinkFreq = Rnd(455,665)
							Case HARD
								mainPlayer\blinkFreq = Rnd(420,630)
						End Select 
						mainPlayer\blinkTimer = mainPlayer\blinkFreq
					EndIf
	
					mainPlayer\blinkTimer = mainPlayer\blinkTimer - timing\tickDuration
				Else
					mainPlayer\blinkTimer = mainPlayer\blinkTimer - timing\tickDuration * 0.6 * mainPlayer\blinkEffect
					;TODO: fix
					;If EyeIrritation > 0 Then mainPlayer\blinkTimer=BlinkTimer-Min(EyeIrritation / 100.0 + 1.0, 4.0) * timing\tickDuration
					
					darkA = Max(darkA, 0.0)
				End If
				
				;TODO: fix
				;EyeIrritation = Max(0, EyeIrritation - timing\tickDuration)
				
				If mainPlayer\blinkEffectTimer > 0 Then
					mainPlayer\blinkEffect = mainPlayer\blinkEffect - (timing\tickDuration/70)
				Else
					mainPlayer\blinkEffect = 1.0
				EndIf
				
				;TODO: reimplement
				;LightBlink = Max(LightBlink - (timing\tickDuration / 35.0), 0)
				;If LightBlink > 0 Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3, 0.8)), 1.0)
				
				If CurrGameState=GAMESTATE_SCP294 Then darkA=1.0
				
				If (Not IsPlayerWearingTempName(mainPlayer,"nvgoggles")) Then darkA = Max((1.0-SecondaryLightOn)*0.9, darkA)
				
				If mainPlayer\dead Then
					CurrGameState = GAMESTATE_PLAYING
					CurrGameSubstate = GAMESUBSTATE_PAUSED_DEAD
					mainPlayer\selectedItem = Null
					SelectedScreen = Null
					SelectedMonitor = Null
					;mainPlayer\blurTimer = Abs(mainPlayer\fallTimer*5)
					;mainPlayer\fallTimer=mainPlayer\fallTimer-(timing\tickDuration*0.8)
					If mainPlayer\fallTimer < - 360 Then 
						CurrGameState = GAMESTATE_PAUSED
						;TODO: fix
						;If SelectedEnding <> "" Then EndingTimer = Min(mainPlayer\fallTimer,-0.1)
					EndIf
					darkA = Max(darkA, Min(Abs(mainPlayer\fallTimer / 400.0), 1.0))
				EndIf
				
				If mainPlayer\fallTimer < 0 Then
					CurrGameState = GAMESTATE_PLAYING
					mainPlayer\selectedItem = Null
					SelectedScreen = Null
					SelectedMonitor = Null
					mainPlayer\blurTimer = Abs(mainPlayer\fallTimer*10)
					mainPlayer\fallTimer=mainPlayer\fallTimer-timing\tickDuration
					darkA = Max(darkA, Min(Abs(mainPlayer\fallTimer / 400.0), 1.0))				
				EndIf
				
				If mainPlayer\selectedItem <> Null Then
					If mainPlayer\selectedItem\itemtemplate\tempname = "navigator" Or mainPlayer\selectedItem\itemtemplate\tempname = "nav" Then darkA = Max(darkA, 0.5)
				End If
				If SelectedScreen <> Null Then darkA = Max(darkA, 0.5)
				
				EntityAlpha(mainPlayer\overlays[OVERLAY_BLACK], darkA)	
			EndIf
			
			If mainPlayer\lightFlash > 0 Then
				ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
				EntityAlpha(mainPlayer\overlays[OVERLAY_WHITE], Max(Min(mainPlayer\lightFlash + Rnd(-0.2, 0.2), 1.0), 0.0))
				mainPlayer\lightFlash = Max(mainPlayer\lightFlash - (timing\tickDuration / 70.0), 0)
			Else
				HideEntity mainPlayer\overlays[OVERLAY_WHITE]
				;EntityAlpha(Light, mainPlayer\lightFlash)
			End If
			
			EntityColor mainPlayer\overlays[OVERLAY_WHITE],255,255,255
			
			;[End block]
			
			If KeyHit(keyBinds\save) Then
				If SelectedDifficulty\saveType = SAVEANYWHERE Then
					RN$ = mainPlayer\currRoom\RoomTemplate\name$
					If RN$ = "173" Or RN$ = "exit1" Or RN$ = "gatea"
						Msg = "You cannot save in this location."
						MsgTimer = 70 * 4
					ElseIf (Not CanSave)
						Msg = "You cannot save at this moment."
						MsgTimer = 70 * 4
					Else
						SaveGame(SavePath + CurrSave + "/")
					EndIf
				ElseIf SelectedDifficulty\saveType = SAVEONSCREENS
					If SelectedScreen=Null And SelectedMonitor=Null Then
						Msg = "Saving is only permitted on clickable monitors scattered throughout the facility."
						MsgTimer = 70 * 4						
					Else
						RN$ = mainPlayer\currRoom\RoomTemplate\name$
						If RN$ = "173" Or RN$ = "exit1" Or RN$ = "gatea"
							Msg = "You cannot save in this location."
							MsgTimer = 70 * 4
						ElseIf (Not CanSave)
							Msg = "You cannot save at this moment."
							MsgTimer = 70 * 4
						Else
							SaveGame(SavePath + CurrSave + "/")
						EndIf
					EndIf
				Else
					Msg = "Quick saving is disabled."
					MsgTimer = 70 * 4
				EndIf
			Else If SelectedDifficulty\saveType = SAVEONSCREENS And (SelectedScreen<>Null Or SelectedMonitor<>Null)
				If (Msg<>"Game progress saved." And Msg<>"You cannot save in this location."And Msg<>"You cannot save at this moment.") Or MsgTimer<=0 Then
					Msg = "Press "+KeyName[keyBinds\save]+" to save."
					MsgTimer = 70*4
				EndIf
				
				If MouseHit2 Then SelectedMonitor = Null
			EndIf
			
			If KeyHit(keyBinds\console) Then
				If CurrGameState=GAMESTATE_CONSOLE Then
					ResumeSounds()
					MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
					CurrGameState=GAMESTATE_PLAYING
				Else
					PauseSounds()
					CurrGameState=GAMESTATE_CONSOLE
				EndIf
				FlushKeys()
			EndIf
			
			UpdateGUI()
			
			;TODO: fix
			;If EndingTimer < 0 Then
			;	If SelectedEnding <> "" Then DrawEnding()
			;Else
			UpdatePauseMenu()		
			;EndIf
			
			UpdateConsole()
			
			If MsgTimer > 0 Then
				Local temp% = False ;TODO: change this variable's name because it's dumb as hell
				If CurrGameState<>GAMESTATE_INVENTORY Then
					If mainPlayer\selectedItem <> Null
						If mainPlayer\selectedItem\itemtemplate\tempname = "paper" Or mainPlayer\SelectedItem\itemtemplate\tempname = "oldpaper"
							temp% = True
						EndIf
					EndIf
				EndIf
				
				If (Not temp%) Then
					Color 0,0,0
					Text((userOptions\screenWidth / 2)+1, (userOptions\screenHeight / 2) + 201, Msg, True, False);, Min(MsgTimer / 2, 255)/255.0)
					Color 255,255,255;Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255)
					Text((userOptions\screenWidth / 2), (userOptions\screenHeight / 2) + 200, Msg, True, False);, Min(MsgTimer / 2, 255)/255.0)
				Else
					Color 0,0,0
					Text((userOptions\screenWidth / 2)+1, (userOptions\screenHeight * 0.94) + 1, Msg, True, False);, Min(MsgTimer / 2, 255)/255.0)
					Color 255,255,255;Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255), Min(MsgTimer / 2, 255)
					Text((userOptions\screenWidth / 2), (userOptions\screenHeight * 0.94), Msg, True, False);, Min(MsgTimer / 2, 255)/255.0)
				EndIf
				MsgTimer=MsgTimer-timing\tickDuration 
			End If
		End If
	Wend
	
	If CurrGameState=GAMESTATE_LAUNCHER Then
		If launcher<>Null Then DrawLauncher()
	ElseIf CurrGameState=GAMESTATE_MAINMENU Then
		DrawMainMenu()
	Else
		RenderWorld2()
		
		UpdateBlur(Sqr(mainPlayer\blurTimer/1400.0))
		
		DrawGUI()
		DrawPauseMenu()
		DrawConsole()
		
		Color 255, 255, 255
		If userOptions\showFPS Then
			SetFont uiAssets\consoleFont
			Text 20, 20, "FPS: " + Int(timing\fps)
			SetFont uiAssets\font[0]
		EndIf
	EndIf
	
	;not by any means a perfect solution
	;Not even proper gamma correction but it's a nice looking alternative that works in windowed mode
	If userOptions\screenGamma>1.0 Then
		CopyRect 0,0,userOptions\screenWidth,userOptions\screenHeight,1024-userOptions\screenWidth/2,1024-userOptions\screenHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(userOptions\screenWidth),1.0/Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,3
		EntityAlpha fresize_image,userOptions\screenGamma-1.0
		ScaleRender(-1.0/Float(userOptions\screenWidth),1.0/Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth))
	ElseIf userOptions\screenGamma<1.0 Then ;todo: maybe optimize this if it's too slow, alternatively give players the option to disable gamma
		CopyRect 0,0,userOptions\screenWidth,userOptions\screenHeight,1024-userOptions\screenWidth/2,1024-userOptions\screenHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(userOptions\screenWidth),1.0/Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,2
		EntityAlpha fresize_image,1.0
		SetBuffer TextureBuffer(fresize_texture2)
		ClsColor 255*userOptions\screenGamma,255*userOptions\screenGamma,255*userOptions\screenGamma
		Cls
		SetBuffer BackBuffer()
		ScaleRender(-1.0/Float(userOptions\screenWidth),1.0/Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth),2048.0 / Float(userOptions\screenWidth))
		SetBuffer(TextureBuffer(fresize_texture2))
		ClsColor 0,0,0
		Cls
		SetBuffer(BackBuffer())
	EndIf
	EntityFX fresize_image,1
	EntityBlend fresize_image,1
	EntityAlpha fresize_image,1.0
	
	Flip userOptions\vsync<>0
	;[End block]
End Function

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

;[Block] ;TODO: FIX
;Function DrawEnding()
;	
;	timing\tickDuration = 0
;	EndingTimer=EndingTimer-timing\tickDuration2
;	
;		
;	Local x,y,width,height, temp
;	Local itt.ItemTemplates, r.Rooms
;	
;	Select Lower(SelectedEnding)
;		Case "b2", "a1"
;			ClsColor Max(255+(EndingTimer)*2.8,0), Max(255+(EndingTimer)*2.8,0), Max(255+(EndingTimer)*2.8,0)
;		Default
;			ClsColor 0,0,0
;	End Select
;	
;	ShouldPlay = 66
;	
;	Cls
;	
;	If EndingTimer<-200 Then
;		
;		If BreathCHN <> 0 Then
;			If IsChannelPlaying(BreathCHN) Then StopChannel BreathCHN : mainPlayer\stamina = 100
;		EndIf
;		
;		If EndingTimer <-400 Then 
;			If Music(5)=0 Then Music(5) = LoadSound("SFX/Music/Intro.ogg")
;			ShouldPlay = 5
;		EndIf
;		
;		If EndingScreen = 0 Then 
;			EndingScreen = LoadImage("GFX/endingscreen.pt")
;			
;			temp = LoadSound ("SFX/Music/Ending.ogg")
;			PlaySound2 temp
;			
;			PlaySound2 LightSFX
;		EndIf
;		
;		If EndingTimer > -700 Then 
;			
;			;-200 -> -700
;			;Max(50 - (Abs(KillTimer)-200),0)    =    0->50
;			If Rand(1,150)<Min((Abs(EndingTimer)-200),155) Then
;				DrawImage EndingScreen, userOptions\screenWidth/2-400, userOptions\screenHeight/2-400
;			Else
;				Color 0,0,0
;				Rect 100,100,userOptions\screenWidth-200,userOptions\screenHeight-200
;				Color 255,255,255
;			EndIf
;			
;			If EndingTimer+timing\tickDuration2 > -450 And EndingTimer <= -450 Then
;				Select Lower(SelectedEnding)
;					Case "a1", "a2"
;						PlaySound2 LoadTempSound("SFX/Ending/GateA/Ending"+SelectedEnding+".ogg")
;					Case "b1", "b2", "b3"
;						PlaySound2 LoadTempSound("SFX/Ending/GateB/Ending"+SelectedEnding+".ogg")
;				End Select
;			EndIf			
;			
;		Else
;			
;			DrawImage EndingScreen, userOptions\screenWidth/2-400, userOptions\screenHeight/2-400
;			
;			If EndingTimer < -1000 Then 
;				
;				width = ImageWidth(PauseMenuIMG)
;				height = ImageHeight(PauseMenuIMG)
;				x = userOptions\screenWidth / 2 - width / 2
;				y = userOptions\screenHeight / 2 - height / 2
;				
;				DrawImage PauseMenuIMG, x, y
;				
;				Color(255, 255, 255)
;				SetFont uiAssets\font[1]
;				Text(x + width / 2 + 40*MenuScale, y + 20*MenuScale, "THE END", True)
;				SetFont uiAssets\font[0]

;					x = x+132*MenuScale
;					y = y+122*MenuScale
;					
;					Local roomamount = 0, roomsfound = 0
;					For r.Rooms = Each Rooms
;						roomamount = roomamount + 1
;						roomsfound = roomsfound + r\found
;					Next
;					
;					Local docamount=0, docsfound=0
;					For itt.ItemTemplates = Each ItemTemplates
;						If itt\tempname = "paper" Then
;							docamount=docamount+1
;							docsfound=docsfound+itt\found
;						EndIf
;					Next
;					
;					Local scpsEncountered=1
;					For i = 0 To 24
;						scpsEncountered = scpsEncountered
;					Next
;					
;					Text x, y, "SCPs encountered: " +scpsEncountered
;					Text x, y+40*MenuScale, "Rooms found: " + roomsfound+"/"+roomamount
;					Text x, y+60*MenuScale, "Documents discovered: " +docsfound+"/"+docamount
;					Text x, y+80*MenuScale, "Items refined in SCP-914: " +RefinedItems			
;					
;					x = userOptions\screenWidth / 2 - width / 2
;					y = userOptions\screenHeight / 2 - height / 2
;					x = x+width/2
;					y = y+height-100*MenuScale
;					
;					If DrawButton(x-145*MenuScale,y-100*MenuScale,390*MenuScale,60*MenuScale,"MAIN MENU", True) Then
;						NullGame()
;						Music(21) = LoadSound("SFX/Ending/MenuBreath.ogg")
;						ShouldPlay = 21
;						MenuOpen = False
;						MainMenuOpen = True
;						MainMenuTab = 0
;						CurrSave = ""
;						FlushKeys()
;					EndIf					
;				Else
;					DrawMenu()
;				EndIf
;			
;		EndIf
;		
;	EndIf
;	
;	ShowPointer2()
;
;	SetFont uiAssets\font[0]
;End Function
;[End Block]

;--------------------------------------- GUI, menu etc ------------------------------------------------

Function UpdateGUI()
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$
	
	If mainPlayer\closestButton <> 0 And mainPlayer\selectedDoor = Null And CurrGameState=GAMESTATE_PLAYING Then
		If MouseUp1 Then
			MouseUp1 = False
			If mainPlayer\closestDoor <> Null Then 
				If mainPlayer\closestDoor\Code <> "" Then
					mainPlayer\selectedDoor = mainPlayer\closestDoor
				ElseIf Not mainPlayer\disableControls Then
					PlayRangedSound_SM(sndManager\button, mainPlayer\cam, mainPlayer\closestButton)
					UseDoor(mainPlayer\closestDoor,True)			
				EndIf
			EndIf
		EndIf
	EndIf
	
	If (CurrGameState = GAMESTATE_SCP294) Then
		Update294()
	EndIf
	
	If SelectedScreen <> Null Then
		If MouseUp1 Or MouseHit2 Then
			FreeImage SelectedScreen\img : SelectedScreen\img = 0
			SelectedScreen = Null
			MouseUp1 = False
		EndIf
	EndIf
	;TODO: cleanup
	Local shouldDrawHUD%=True
	If mainPlayer\selectedDoor <> Null Then
		mainPlayer\selectedItem = Null
		
		If shouldDrawHUD Then
			Local buttonObj% = GrabMesh("GFX/Map/Meshes/Button.b3d")
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True),EntityZ(mainPlayer\closestButton,True)
			RotateEntity pvt, 0, EntityYaw(mainPlayer\closestButton,True)-180,0
			MoveEntity pvt, 0,0,0.22
			PositionEntity mainPlayer\head, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
			PointEntity mainPlayer\head, mainPlayer\closestButton
			FreeEntity pvt
			
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)+MeshHeight(buttonObj)*0.015,EntityZ(mainPlayer\closestButton,True))
			projY# = ProjectedY()
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)-MeshHeight(buttonObj)*0.015,EntityZ(mainPlayer\closestButton,True))
			scale# = (ProjectedY()-projy)/462.0
			
			x = userOptions\screenWidth/2-ImageWidth(uiAssets\keypadHUD)*scale/2
			y = userOptions\screenHeight/2-ImageHeight(uiAssets\keypadHUD)*scale/2

			DropAsset(buttonObj)
			
			SetFont uiAssets\font[2]
			If KeypadMSG <> "" Then 
				KeypadTimer = KeypadTimer-timing\tickDuration
				
				If (KeypadTimer Mod 70) < 35 Then Text userOptions\screenWidth/2, y+124*scale, KeypadMSG, True,True
				If KeypadTimer =<0 Then
					KeypadMSG = ""
					SelectedDoor = Null
					MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
				EndIf
			EndIf
			
			x = x+44*scale
			y = y+249*scale
			
			For n = 0 To 3
				For i = 0 To 2
					xtemp = x+Int(58.5*scale*n)
					ytemp = y+(67*scale)*i
					
					temp = False
					If MouseOn(xtemp,ytemp, 54*scale,65*scale) And KeypadMSG = "" Then
						If MouseUp1 Then 
							PlaySound_SM(sndManager\button)
							
							Select (n+1)+(i*4)
								Case 1,2,3
									KeypadInput=KeypadInput + ((n+1)+(i*4))
								Case 4
									KeypadInput=KeypadInput + "0"
								Case 5,6,7
									KeypadInput=KeypadInput + ((n+1)+(i*4)-1)
								Case 8 ;enter
									If KeypadInput = mainPlayer\selectedDoor\Code Then
										PlaySound_SM(sndManager\scannerUse)								
										
										mainPlayer\selectedDoor\locked = 0
										UseDoor(mainPlayer\selectedDoor,True)
										mainPlayer\selectedDoor = Null
										MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
									Else
										PlaySound_SM(sndManager\scannerErr)
										KeypadMSG = "ACCESS DENIED"
										KeypadTimer = 210
										KeypadInput = ""	
									EndIf
								Case 9,10,11
									KeypadInput=KeypadInput + ((n+1)+(i*4)-2)
								Case 12
									KeypadInput = ""
							End Select 
							
							If Len(KeypadInput)> 4 Then KeypadInput = Left(KeypadInput,4)
						EndIf
						
					Else
						temp = False
					EndIf
					
				Next
			Next
			
			If MouseHit2 Then
				mainPlayer\selectedDoor = Null
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
			EndIf
		Else
			mainPlayer\selectedDoor = Null
		EndIf
	Else
		KeypadInput = ""
		KeypadTimer = 0
		KeypadMSG= ""
	EndIf
	
	If KeyHit(1) Then;TODO: fix ;And EndingTimer = 0 Then 
		If IsPaused() Then
			If CurrGameState=GAMESTATE_INVENTORY Then
				ToggleInventory(mainPlayer)
			Else
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
				CurrGameState = GAMESTATE_PLAYING
			EndIf
		Else
			PauseSounds()
			CurrGameState = GAMESTATE_PAUSED
		EndIf
		
		;OptionsMenu = 0
		;QuitMSG = 0
		
		mainPlayer\selectedDoor = Null
		SelectedScreen = Null
		SelectedMonitor = Null
	EndIf
	
	UpdateInventory(mainPlayer)
End Function

Function DrawGUI()
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$
	
	Local e.Events, it.Items
	
	If mainPlayer\currRoom\RoomTemplate\name = "pocketdimension" Then
		For e.Events = Each Events
			If e\room = mainPlayer\currRoom And e\EventState > 600 Then
				If mainPlayer\blinkTimer < -3 And mainPlayer\blinkTimer > -11 Then
					If e\img = 0 Then
						If mainPlayer\blinkTimer > -5 And Rand(30)=1 Then
							If e\img = 0 Then e\img = LoadImage("GFX/npcs/106face.jpg")
						EndIf
					Else
						DrawImage e\img, userOptions\screenWidth/2-Rand(390,310), userOptions\screenHeight/2-Rand(290,310)
					EndIf
				Else
					If e\img <> 0 Then FreeImage e\img : e\img = 0
				EndIf
				
				Exit
			EndIf
		Next
	EndIf
	
	If mainPlayer\closestButton <> 0 And mainPlayer\selectedDoor = Null And CurrGameState=GAMESTATE_PLAYING Then
		yawvalue# = WrapAngle(-DeltaYaw(mainPlayer\cam,mainPlayer\closestButton))
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = WrapAngle(-DeltaPitch(mainPlayer\cam,mainPlayer\closestButton))
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		DrawImage(uiAssets\handIcon[HAND_ICON_TOUCH], userOptions\screenWidth / 2 + Sin(yawvalue) * (userOptions\screenWidth / 3) - 32, userOptions\screenHeight / 2 - Sin(pitchvalue) * (userOptions\screenHeight / 3) - 32)
	EndIf
	
	If mainPlayer\closestItem <> Null Then
		yawvalue# = -DeltaYaw(mainPlayer\cam, mainPlayer\closestItem\collider)
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = -DeltaPitch(mainPlayer\cam, mainPlayer\closestItem\collider)
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		DrawImage(uiAssets\handIcon[HAND_ICON_GRAB], userOptions\screenWidth / 2 + Sin(yawvalue) * (userOptions\screenWidth / 3) - 32, userOptions\screenHeight / 2 - Sin(pitchvalue) * (userOptions\screenHeight / 3) - 32)
	EndIf
	
	If DrawHandIcon Then DrawImage(uiAssets\handIcon[HAND_ICON_TOUCH], userOptions\screenWidth / 2 - 32, userOptions\screenHeight / 2 - 32)
	For i = 0 To 3
		If DrawArrowIcon(i) Then
			x = userOptions\screenWidth / 2 - 32
			y = userOptions\screenHeight / 2 - 32		
			Select i
				Case 0
					y = y - 64 - 5
				Case 1
					x = x + 64 + 5
				Case 2
					y = y + 64 + 5
				Case 3
					x = x - 5 - 64
			End Select
			DrawImage(uiAssets\handIcon[HAND_ICON_TOUCH], x, y)
			Color 0, 0, 0
			Rect(x + 4, y + 4, 64 - 8, 64 - 8)
			DrawImage(uiAssets\arrow[i], x + 21, y + 21)
			DrawArrowIcon(i) = False
		End If
	Next
	
	If (CurrGameState = GAMESTATE_SCP294) Then
		Draw294()
	EndIf
	
	If userOptions\hudEnabled Then 
		
		Local width% = 204, height% = 20
		x% = 80
		y% = userOptions\screenHeight - 95
		
		Color 255, 255, 255	
		Rect (x, y, width, height, False)
		For i = 1 To Int(((width - 2) * (mainPlayer\blinkTimer / (mainPlayer\blinkFreq))) / 10)
			DrawImage(uiAssets\blinkBar, x + 3 + 10 * (i - 1), y + 3)
		Next	
		Color 0, 0, 0
		Rect(x - 50, y, 30, 30)
		
		;TODO: FIX
		;If EyeIrritation > 0 Then
		;	Color 200, 0, 0
		;	Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
		;End If
		
		Color 255, 255, 255
		Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
		
		DrawImage uiAssets\blinkIcon, x - 50, y
		
		y = userOptions\screenHeight - 55
		Color 255, 255, 255
		Rect (x, y, width, height, False)
		For i = 1 To Int(((width - 2) * (mainPlayer\stamina / 100.0)) / 10)
			DrawImage(uiAssets\staminaBar, x + 3 + 10 * (i - 1), y + 3)
		Next	
		
		Color 0, 0, 0
		Rect(x - 50, y, 30, 30)
		
		Color 255, 255, 255
		Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
		If mainPlayer\crouching Then
			DrawImage uiAssets\crouchIcon, x - 50, y
		Else
			DrawImage uiAssets\sprintIcon, x - 50, y
		EndIf
		
		If DebugHUD Then
			Color 255, 255, 255
			SetFont uiAssets\ConsoleFont
			
			;Text x + 250, 50, "Zone: " + (EntityZ(mainPlayer\collider)/8.0)
			Text x - 50, 50, "Player Position: (" + f2s(EntityX(mainPlayer\collider), 3) + ", " + f2s(EntityY(mainPlayer\collider), 3) + ", " + f2s(EntityZ(mainPlayer\collider), 3) + "), speed: "+f2s(mainPlayer\dropSpeed, 3)
			Text x - 50, 70, "Camera Position: (" + f2s(EntityX(mainPlayer\cam), 3)+ ", " + f2s(EntityY(mainPlayer\cam), 3) +", " + f2s(EntityZ(mainPlayer\cam), 3) + ")"
			Text x - 50, 100, "Player Rotation: (" + f2s(EntityPitch(mainPlayer\collider), 3) + ", " + f2s(EntityYaw(mainPlayer\collider), 3) + ", " + f2s(EntityRoll(mainPlayer\collider), 3) + ")"
			Text x - 50, 120, "Camera Rotation: (" + f2s(EntityPitch(mainPlayer\cam), 3)+ ", " + f2s(EntityYaw(mainPlayer\cam), 3) +", " + f2s(EntityRoll(mainPlayer\cam), 3) + ")"
			Text x - 50, 150, "Room: " + mainPlayer\currRoom\RoomTemplate\name
			For ev.Events = Each Events
				If ev\room = mainPlayer\currRoom Then
					Text x - 50, 170, "Room event: " + ev\EventName   
					Text x - 50, 190, "state: " + ev\EventState
					Text x - 50, 210, "state2: " + ev\EventState2   
					Text x - 50, 230, "state3: " + ev\EventState3
					Exit
				EndIf
			Next
			Text x - 50, 250, "Room coordinates: (" + Floor(EntityX(mainPlayer\currRoom\obj) / 8.0 + 0.5) + ", " + Floor(EntityZ(mainPlayer\currRoom\obj) / 8.0 + 0.5) + ")"
			Text x - 50, 280, "Stamina: " + f2s(mainPlayer\stamina, 3)
			Text x - 50, 300, "Dead: " + mainPlayer\dead               
			Text x - 50, 320, "Blink timer: " + f2s(mainPlayer\blinkTimer, 3)
			Text x - 50, 340, "Injuries: " + mainPlayer\injuries
			Text x - 50, 360, "Bloodloss: " + mainPlayer\bloodloss
			Text x - 50, 390, "SCP - 173 Position (collider): (" + f2s(EntityX(Curr173\collider), 3) + ", " + f2s(EntityY(Curr173\collider), 3) + ", " + f2s(EntityZ(Curr173\collider), 3) + ")"
			Text x - 50, 410, "SCP - 173 Position (obj): (" + f2s(EntityX(Curr173\obj), 3) + ", " + f2s(EntityY(Curr173\obj), 3) + ", " + f2s(EntityZ(Curr173\obj), 3) + ")"
			;Text x - 50, 410, "SCP - 173 Idle: " + Curr173\Idle
			Text x - 50, 430, "SCP - 173 State: " + Curr173\state
			Text x - 50, 450, "SCP - 106 Position: (" + f2s(EntityX(Curr106\obj), 3) + ", " + f2s(EntityY(Curr106\obj), 3) + ", " + f2s(EntityZ(Curr106\obj), 3) + ")"
			Text x - 50, 470, "SCP - 106 Idle: " + Curr106\Idle
			Text x - 50, 490, "SCP - 106 State: " + Curr106\state
			offset% = 0
			For npc.NPCs = Each NPCs
				If npc\NPCtype = NPCtype096 Then
					Text x - 50, 510, "SCP - 096 Position: (" + f2s(EntityX(npc\obj), 3) + ", " + f2s(EntityY(npc\obj), 3) + ", " + f2s(EntityZ(npc\obj), 3) + ")"
					Text x - 50, 530, "SCP - 096 Idle: " + npc\Idle
					Text x - 50, 550, "SCP - 096 State: " + npc\State
					Text x - 50, 570, "SCP - 096 Speed: " + f2s(npc\currspeed, 5)
				EndIf
				If npc\NPCtype = NPCtypeMTF Then
					Text x - 50, 600 + 60 * offset, "MTF " + offset + " Position: (" + f2s(EntityX(npc\obj), 3) + ", " + f2s(EntityY(npc\obj), 3) + ", " + f2s(EntityZ(npc\obj), 3) + ")"
					Text x - 50, 640 + 60 * offset, "MTF " + offset + " State: " + npc\State
					Text x - 50, 620 + 60 * offset, "MTF " + offset + " LastSeen: " + npc\lastseen					
					offset = offset + 1
				EndIf
			Next
			
			SetFont uiAssets\font[0]
		EndIf
		
	EndIf
	
	If SelectedScreen <> Null Then
		DrawImage SelectedScreen\img, userOptions\screenWidth/2-ImageWidth(SelectedScreen\img)/2,userOptions\screenHeight/2-ImageHeight(SelectedScreen\img)/2
	EndIf
	
	;TODO: cleanup
	Local shouldDrawHUD%=True
	If mainPlayer\selectedDoor <> Null Then
		mainPlayer\selectedItem = Null
		
		If shouldDrawHUD Then
			Local buttonObj% = GrabMesh("GFX/Map/Meshes/Button.b3d")
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True),EntityZ(mainPlayer\closestButton,True)
			RotateEntity pvt, 0, EntityYaw(mainPlayer\closestButton,True)-180,0
			MoveEntity pvt, 0,0,0.22
			PositionEntity mainPlayer\head, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
			PointEntity mainPlayer\head, mainPlayer\closestButton
			FreeEntity pvt
			
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)+MeshHeight(buttonObj)*0.015,EntityZ(mainPlayer\closestButton,True))
			projY# = ProjectedY()
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)-MeshHeight(buttonObj)*0.015,EntityZ(mainPlayer\closestButton,True))
			scale# = (ProjectedY()-projy)/462.0
			
			x = userOptions\screenWidth/2-ImageWidth(uiAssets\keypadHUD)*scale/2
			y = userOptions\screenHeight/2-ImageHeight(uiAssets\keypadHUD)*scale/2

			DropAsset(buttonObj)
			
			SetFont uiAssets\font[2]
			If KeypadMSG = "" Then
				Text userOptions\screenWidth/2, y+70*scale, "ACCESS CODE: ",True,True	
				SetFont uiAssets\font[3]
				Text userOptions\screenWidth/2, y+124*scale, KeypadInput,True,True	
			EndIf
			
			ShowPointer2()
		EndIf
	EndIf
	
	DrawInventory(mainPlayer)
End Function

Function DrawPauseMenu()
	Local titleText$ = "PAUSED"
	Local x%, y%, width%, height%
	
	If CurrGameState = GAMESTATE_PAUSED Then
		width = ImageWidth(uiAssets\pauseMenuBG)
		height = ImageHeight(uiAssets\pauseMenuBG)
		x = userOptions\screenWidth / 2 - width / 2
		y = userOptions\screenHeight / 2 - height / 2
		
		DrawImage uiAssets\pauseMenuBG, x, y
		
		Color(255, 255, 255)
		
		x = x+132*MenuScale
		y = y+122*MenuScale	
		
		If mainPlayer\dead Then
			titleText = "YOU DIED"
		End If
		SetFont uiAssets\font[1]
		Text(x, y-(122-45)*MenuScale, titleText,False,True)
		
		SetFont uiAssets\font[0]
		Text x, y, "Difficulty: "+SelectedDifficulty\name
		Text x, y+20*MenuScale, "Save: "+CurrSave
		Text x, y+40*MenuScale, "Map seed: "+RandomSeed
		
		y = y+10
		
		If Not mainPlayer\dead Then
			y = y+72*MenuScale
			
			DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "Resume", True)
			y = y + 75*MenuScale
			If (Not SelectedDifficulty\permaDeath) Then
				If GameSaved Then
					DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game")
				Else
					DrawFrame(x,y,390*MenuScale, 60*MenuScale)
					Color (100, 100, 100)
					SetFont uiAssets\font[1]
					Text(x + (390*MenuScale) / 2, y + (60*MenuScale) / 2, "Load Game", True, True)
				EndIf
				y = y + 75*MenuScale
			EndIf
			
			DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "Options")
			y = y + 75*MenuScale
			
			DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "Quit")
		Else
			y = y+104*MenuScale
			If GameSaved And (Not SelectedDifficulty\permaDeath) Then
				DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game")
			Else
				DrawUIButton(x, y, 390*MenuScale, 60*MenuScale, "")
				Color 50,50,50
				Text(x + 185*MenuScale, y + 30*MenuScale, "Load Game", True, True)
			EndIf
			DrawUIButton(x, y + 80*MenuScale, 390*MenuScale, 60*MenuScale, "Quit to Menu")
			y = y + 80*MenuScale
		EndIf
		
		SetFont uiAssets\font[0]
		If mainPlayer\dead Then RowText(DeathMSG$, x, y + 80*MenuScale, 390*MenuScale, 600*MenuScale)
		;EndIf

		ShowPointer2()
	EndIf
	
	SetFont uiAssets\font[0]
	
End Function

Function UpdatePauseMenu()
	Local x%, y%, width%, height%
	
	If CurrGameState = GAMESTATE_PAUSED Then
		width = ImageWidth(uiAssets\pauseMenuBG)
		height = ImageHeight(uiAssets\pauseMenuBG)
		x = userOptions\screenWidth / 2 - width / 2
		y = userOptions\screenHeight / 2 - height / 2
		
		x = x+132*MenuScale
		y = y+122*MenuScale	
		
		Local AchvXIMG% = (x + (22*MenuScale))
		Local scale# = userOptions\screenHeight/768.0
		Local SeparationConst% = 76*scale
		Local imgsize% = 64
		
		y = y+10
		
		If Not mainPlayer\dead Then
			y = y+72*MenuScale
			
			If UpdateUIButton(x, y, 390*MenuScale, 60*MenuScale, "Resume", True) Then
				CurrGameState = GAMESTATE_PLAYING
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
			EndIf
			
			y = y + 75*MenuScale
			If (Not SelectedDifficulty\permaDeath) Then
				If GameSaved Then
					If UpdateUIButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game") Then
						DrawLoading(0)
						
						CurrGameState = GAMESTATE_PLAYING
						LoadGame(SavePath + CurrSave + "/",True)
						
						MoveMouse viewport_center_x,viewport_center_y
						SetFont uiAssets\font[0]
						HidePointer ()
						
						FlushKeys()
						FlushMouse()
						mainPlayer\disableControls = False
						
						UpdateRooms()
						
						For r.Rooms = Each Rooms
							x = Abs(EntityX(mainPlayer\collider) - EntityX(r\obj))
							z = Abs(EntityZ(mainPlayer\collider) - EntityZ(r\obj))
							
							If x < 12.0 And z < 12.0 Then
								;MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
								If x < 4.0 And z < 4.0 Then
									If Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5 Then mainPlayer\currRoom = r
									;MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
								EndIf
							End If
						Next
						
						DrawLoading(100)
						
						mainPlayer\dropSpeed=0
						
						UpdateWorld 0.0
					EndIf
				EndIf
				y = y + 75*MenuScale
			EndIf
			
			;If UpdateUIButton(x, y, 390*MenuScale, 60*MenuScale, "Options") Then OptionsMenu = 1 ;TODO: fix
			y = y + 75*MenuScale
			If UpdateUIButton(x, y, 390*MenuScale, 60*MenuScale, "Quit") Then
				;TODO: ask for saving
				NullGame()
				CurrGameState = GAMESTATE_MAINMENU
				CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
				CurrSave = ""
				FlushKeys()
			EndIf
		Else
			y = y+104*MenuScale
			If GameSaved And (Not SelectedDifficulty\permaDeath) Then
				If UpdateUIButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game") Then
					DrawLoading(0)
					
					CurrGameState = GAMESTATE_PLAYING
					LoadGame(SavePath + CurrSave + "/",True)
					
					MoveMouse viewport_center_x,viewport_center_y
					SetFont uiAssets\font[0]
					HidePointer ()
					
					FlushKeys()
					FlushMouse()
					mainPlayer\disableControls = False
					
					UpdateRooms()
					
					For r.Rooms = Each Rooms
						x = Abs(EntityX(mainPlayer\collider) - EntityX(r\obj))
						z = Abs(EntityZ(mainPlayer\collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							;MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5 Then mainPlayer\currRoom = r
								;MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
							EndIf
						End If
					Next
					
					DrawLoading(100)
					
					mainPlayer\dropSpeed=0
					
					UpdateWorld 0.0
				EndIf
			EndIf
			If UpdateUIButton(x, y + 80*MenuScale, 390*MenuScale, 60*MenuScale, "Quit to Menu") Then
				NullGame()
				CurrGameState = GAMESTATE_MAINMENU
				CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
				CurrSave = ""
				FlushKeys()
			EndIf
			y = y + 80*MenuScale
		EndIf
	EndIf
	
	SetFont uiAssets\font[0]
	
End Function

;--------------------------------------- random -------------------------------------------------------

Function f2s$(n#, count%)
	Return Left(n, Len(Int(n))+count+1)
End Function

Function Animate2#(entity%, curr#, start%, quit%, speed#, loop=True)
	
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(curr + speed * timing\tickDuration,quit),start)
		
		If loop Then
			If newTime => quit Then 
				;SetAnimTime entity, start
				newTime = start
			Else
				;SetAnimTime entity, newTime
			EndIf
		Else
			;SetAnimTime entity, newTime
		EndIf
	Else
		If start < quit Then
			temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = curr + speed * timing\tickDuration
			
			If newTime < quit Then newTime = start
			If newTime > start Then newTime = quit
			
			;SetAnimTime entity, newTime
		Else
			;SetAnimTime (entity, Max(Min(curr + speed * timing\tickDuration,start),quit))
			newTime = Max(Min(curr + speed * timing\tickDuration,start),quit)
		EndIf
	EndIf
	
	SetAnimTime entity, newTime
	Return newTime
	
End Function 

Function UpdateInfect()
	Local temp#, i%, r.Rooms
	
	If mainPlayer\infect008>0 Then
		ShowEntity mainPlayer\overlays[OVERLAY_008]
		
		If mainPlayer\infect008 < 93.0 Then
			temp=mainPlayer\infect008
			mainPlayer\infect008 = Min(mainPlayer\infect008+timing\tickDuration*0.002,100)
			
			mainPlayer\blurTimer = Max(mainPlayer\infect008*3*(2.0-mainPlayer\crouchState),mainPlayer\blurTimer)
			
			;HeartBeatRate = Max(HeartBeatRate, 100)
			mainPlayer\heartbeatIntensity = Max(100, mainPlayer\infect008/120.0)
			
			EntityAlpha mainPlayer\overlays[OVERLAY_008], Min(((mainPlayer\infect008*0.2)^2)/1000.0,0.5) * (Sin(TimeInPosMilliSecs()/8.0)+2.0)
			
			For i = 0 To 6
				If mainPlayer\infect008>i*15+10 And temp =< i*15+10 Then
					PlaySound2 LoadTempSound("SFX/SCP/008/Voices"+i+".ogg")
				EndIf
			Next
			
			If mainPlayer\infect008 > 20 And temp =< 20.0 Then
				Msg = "You feel kinda feverish."
				MsgTimer = 70*6
			ElseIf mainPlayer\infect008 > 40 And temp =< 40.0
				Msg = "You feel nauseated."
				MsgTimer = 70*6
			ElseIf mainPlayer\infect008 > 60 And temp =< 60.0
				Msg = "The nausea's getting worse."
				MsgTimer = 70*6
			ElseIf mainPlayer\infect008 > 80 And temp =< 80.0
				Msg = "You feel very faint."
				MsgTimer = 70*6
			ElseIf mainPlayer\infect008 =>91.5
				mainPlayer\blinkTimer = Max(Min(-10*(mainPlayer\infect008-91.5),mainPlayer\blinkTimer),-10)
				If mainPlayer\infect008 >= 92.7 And temp < 92.7 Then
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name="008" Then
							PositionEntity mainPlayer\collider, EntityX(r\Objects[7],True),EntityY(r\Objects[7],True),EntityZ(r\Objects[7],True),True
							ResetEntity mainPlayer\collider
							r\NPC[0] = CreateNPC(NPCtypeD, EntityX(r\Objects[6],True),EntityY(r\Objects[6],True)+0.2,EntityZ(r\Objects[6],True))
							r\NPC[0]\sounds[0] = LoadSound("SFX/SCP/008/KillScientist1.ogg")
							r\NPC[0]\soundChannels[0] = PlaySound(r\NPC[0]\sounds[0])
							tex = LoadTexture("GFX/npcs/scientist2.jpg")
							EntityTexture r\NPC[0]\obj, tex
							FreeTexture tex
							r\NPC[0]\state=6
							mainPlayer\currRoom = r
							Exit
						EndIf
					Next
				EndIf
			EndIf
		Else
			temp=mainPlayer\infect008
			mainPlayer\infect008 = Min(mainPlayer\infect008+timing\tickDuration*0.004,100)
			
			If mainPlayer\infect008 < 94.7 Then
				EntityAlpha mainPlayer\overlays[OVERLAY_008], 0.5 * (Sin(TimeInPosMilliSecs()/8.0)+2.0)
				mainPlayer\blurTimer = 900
				
				If mainPlayer\infect008 > 94.5 Then mainPlayer\blinkTimer = Max(Min(-50*(mainPlayer\infect008-94.5),mainPlayer\blinkTimer),-10)
				PointEntity mainPlayer\collider, mainPlayer\currRoom\NPC[0]\collider
				PointEntity mainPlayer\currRoom\NPC[0]\collider, mainPlayer\collider
				mainPlayer\forceMove = 0.4
				mainPlayer\injuries = 2.5
				mainPlayer\bloodloss = 0
				
				Animate2(mainPlayer\currRoom\NPC[0]\obj, AnimTime(mainPlayer\currRoom\NPC[0]\obj), 357, 381, 0.3)
			ElseIf mainPlayer\infect008 < 98.5
				
				EntityAlpha mainPlayer\overlays[OVERLAY_008], 0.5 * (Sin(TimeInPosMilliSecs()/5.0)+2.0)
				mainPlayer\blurTimer = 950
				
				If temp < 94.7 Then 
					mainPlayer\currRoom\NPC[0]\sounds[0] = LoadSound("SFX/SCP/008/KillScientist2.ogg")
					mainPlayer\currRoom\NPC[0]\soundChannels[0] = PlaySound(mainPlayer\currRoom\NPC[0]\sounds[0])
					
					DeathMSG = "Subject D-9341 found ingesting Dr. [REDACTED] at Sector [REDACTED]. Subject was immediately terminated by Nine-Tailed Fox and sent for autopsy. "
					DeathMSG = DeathMSG + "SCP-008 infection was confirmed, after which the body was incinerated."
					
					Kill(mainPlayer)
					de.Decals = CreateDecal(3, EntityX(mainPlayer\currRoom\NPC[0]\Collider), 544*RoomScale + 0.01, EntityZ(mainPlayer\currRoom\NPC[0]\Collider),90,Rnd(360),0)
					de\Size = 0.8
					ScaleSprite(de\obj, de\Size,de\Size)
				ElseIf mainPlayer\overlays[OVERLAY_008] > 96
					mainPlayer\blinkTimer = Max(Min(-10*(mainPlayer\infect008-96),mainPlayer\blinkTimer),-10)
				Else
					;TODO: wtf??????
					;KillTimer = Max(-350, KillTimer)
				EndIf
				
				;TODO: this could break
				If mainPlayer\currRoom\NPC[0]\State2=0 Then
					Animate2(mainPlayer\currRoom\NPC[0]\obj, AnimTime(mainPlayer\currRoom\NPC[0]\obj), 13, 19, 0.3,False)
					If AnimTime(mainPlayer\currRoom\NPC[0]\obj) => 19 Then mainPlayer\currRoom\NPC[0]\State2=1
				Else
					Animate2(mainPlayer\currRoom\NPC[0]\obj, AnimTime(mainPlayer\currRoom\NPC[0]\obj), 19, 13, -0.3)
					If AnimTime(mainPlayer\currRoom\NPC[0]\obj) =< 13 Then mainPlayer\currRoom\NPC[0]\State2=0
				EndIf
				
				If Rand(50)=1 Then
					p.Particles = CreateParticle(EntityX(mainPlayer\currRoom\NPC[0]\Collider),EntityY(mainPlayer\currRoom\NPC[0]\Collider),EntityZ(mainPlayer\currRoom\NPC[0]\Collider), 5, Rnd(0.05,0.1), 0.15, 200)
					p\speed = 0.01
					p\SizeChange = 0.01
					p\A = 0.5
					p\Achange = -0.01
					RotateEntity p\pvt, Rnd(360),Rnd(360),0
				EndIf
				
				PositionEntity mainPlayer\head, EntityX(mainPlayer\currRoom\NPC[0]\Collider,True), EntityY(mainPlayer\currRoom\NPC[0]\Collider,True)+0.65,EntityZ(mainPlayer\currRoom\NPC[0]\Collider,True),True
				RotateEntity mainPlayer\head, (1.0+Sin(TimeInPosMilliSecs()/5.0))*15, mainPlayer\currRoom\angle-180, 0, True
				MoveEntity mainPlayer\head, 0,0,0.4
				TurnEntity mainPlayer\head, 80+(Sin(TimeInPosMilliSecs()/5.0))*30,(Sin(TimeInPosMilliSecs()/5.0))*40,0
			EndIf
		EndIf
		
		
	Else
		HideEntity mainPlayer\overlays[OVERLAY_008]
	EndIf
End Function

;--------------------------------------- decals -------------------------------------------------------
;TODO: Move to their own file?
Type Decals
	Field obj%
	Field SizeChange#, Size#, MaxSize#
	Field AlphaChange#, Alpha#
	Field blendmode%
	Field fx%
	Field ID%
	Field Timer#
	
	Field lifetime#
	
	Field x#, y#, z#
	Field pitch#, yaw#, roll#
End Type

Function CreateDecal.Decals(id%, x#, y#, z#, pitch#, yaw#, roll#)
	Local d.Decals = New Decals
	
	d\x = x
	d\y = y
	d\z = z
	d\pitch = pitch
	d\yaw = yaw
	d\roll = roll
	
	d\MaxSize = 1.0
	
	d\Alpha = 1.0
	d\Size = 1.0
	d\obj = CreateSprite()
	d\blendmode = 1
	
	EntityTexture(d\obj, DecalTextures(id))
	EntityFX(d\obj, 0)
	SpriteViewMode(d\obj, 2)
	PositionEntity(d\obj, x, y, z)
	RotateEntity(d\obj, pitch, yaw, roll)
	
	d\ID = id
	
	If DecalTextures(id) = 0 Or d\obj = 0 Then Return Null
	
	Return d
End Function

Function UpdateDecals()
	Local d.Decals
	For d.Decals = Each Decals
		If d\SizeChange <> 0 Then
			d\Size=d\Size + d\SizeChange * timing\tickDuration
			ScaleSprite(d\obj, d\Size, d\Size)
			
			Select d\ID
				Case 0
					If d\Timer <= 0 Then
						Local angle# = Rand(360)
						Local temp# = Rnd(d\Size)
						Local d2.Decals = CreateDecal(1, EntityX(d\obj) + Cos(angle) * temp, EntityY(d\obj) - 0.0005, EntityZ(d\obj) + Sin(angle) * temp, EntityPitch(d\obj), Rnd(360), EntityRoll(d\obj))
						d2\Size = Rnd(0.1, 0.5) : ScaleSprite(d2\obj, d2\Size, d2\Size)
						;TODO: fix
						;PlayRangedSound(DecaySFX(Rand(1, 3)), mainPlayer\cam, d2\obj, 10.0, Rnd(0.1, 0.5))
						;d\Timer = d\Timer + Rand(50,150)
						d\Timer = Rand(50, 100)
					Else
						d\Timer= d\Timer-timing\tickDuration
					End If
				;Case 6
				;	EntityBlend d\obj, 2
			End Select
			
			If d\Size >= d\MaxSize Then
				d\SizeChange = 0
				d\Size = d\MaxSize
			EndIf
		End If
		
		If d\AlphaChange <> 0 Then
			d\Alpha = Min(d\Alpha + timing\tickDuration * d\AlphaChange, 1.0)
			EntityAlpha(d\obj, d\Alpha)
		End If
		
		If d\lifetime > 0 Then
			d\lifetime=Max(d\lifetime-timing\tickDuration,5)
		EndIf
		
		If d\Size <= 0 Or d\Alpha <= 0 Or d\lifetime=5.0  Then
			FreeEntity(d\obj)
			Delete d
		End If
	Next
End Function

Function Graphics3DExt%(width%,height%,depth%=32,mode%=2)
	;If FE_InitExtFlag = 1 Then DeInitExt() ;prevent FastExt from breaking itself
	Graphics3D width,height,depth,mode
	InitFastResize()
	;InitExt()
End Function

Function UpdateNVG()
	Local wornItem.Items = mainPlayer\wornItems[WORNITEM_SLOT_HEAD]
	
	If wornItem<>Null Then
		If wornItem\itemtemplate\tempname <> "nvgoggles" And wornItem\itemtemplate\tempname <> "supernv" Then
			wornItem = Null
		EndIf
	EndIf
	
    If wornItem<>Null Then
        wornItem\state = Max(-1.0,wornItem\state - (timing\tickDuration * (0.02 * decayMultiplier)))
    EndIf
End Function

Function RenderWorld2()
	CameraProjMode ark_blur_cam,0
	CameraProjMode mainPlayer\cam,1
	
	;TODO: fix
	;If WearingNightVision>0 And WearingNightVision<3 Then
	;	AmbientLight Min(Brightness*2,255), Min(Brightness*2,255), Min(Brightness*2,255)
	;ElseIf WearingNightVision=3
	;	AmbientLight 255,255,255
	;ElseIf mainPlayer\currRoom<>Null
	;	If (mainPlayer\currRoom\RoomTemplate\Name<>"173") And (mainPlayer/currRoom\RoomTemplate\Name<>"exit1") And (mainPlayer/currRoom\RoomTemplate/Name<>"gatea") Then
	;		AmbientLight Brightness, Brightness, Brightness
	;	EndIf
	;EndIf
	
	;IsNVGBlinking% = False
	;HideEntity NVBlink
	
	Local hasBattery% = 2
	Local power% = 0
	
	Local wornItem.Items = mainPlayer\wornItems[WORNITEM_SLOT_HEAD]
	
    If wornItem<>Null Then
		If wornItem\itemtemplate\tempname <> "nvgoggles" And wornItem\itemtemplate\tempname <> "supernv" Then
			wornItem = Null
		EndIf
	EndIf
	
	If wornItem<>Null Then
		Local decayMultiplier# = 1.0
		If wornItem\itemTemplate\tempname = "supernv" Then decayMultiplier = 2.0
		
		power = Int(wornItem\state)
		If wornItem\state <= 0.0 Then ;this nvg can't be used
			hasBattery = 0
			Msg = "The batteries in these night vision goggles died."
			mainPlayer\blinkTimer = -1.0
			MsgTimer = 350
		ElseIf mainPlayer\inventory\items[i]\state <= 100.0 Then
			hasBattery = 1
		EndIf
		
		If (hasBattery) Then
			RenderWorld(Max(0.0,1.0+(timing\accumulator/timing\tickDuration)))
		EndIf
	Else
		RenderWorld(Max(0.0,1.0+(timing\accumulator/timing\tickDuration)))
	EndIf
	
	;If hasBattery=0 And WearingNightVision<>3
	;	;IsNVGBlinking% = True
	;	ShowEntity NVBlink%
	;EndIf
	
	If mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6
		If IsPlayerWearingTempName(mainPlayer,"supernv") And hasBattery<>0 Then ;show a HUD
			;NVTimer=NVTimer-timing\tickDuration
			
			;If NVTimer<=0.0 Then
			For np.NPCs = Each NPCs
				np\NVX = EntityX(np\Collider,True)
				np\NVY = EntityY(np\Collider,True)
				np\NVZ = EntityZ(np\Collider,True)
			Next
			;IsNVGBlinking% = True
			;	ShowEntity NVBlink%
			;	If NVTimer<=-10 Then
			;		NVTimer = 600.0
			;	EndIf
			;EndIf
			
			Color 255,255,255
			
			SetFont uiAssets\font[2]
			
			Text userOptions\screenWidth/2,20*MenuScale,"REFRESHING DATA IN",True,False
			
			;Text userOptions\screenWidth/2,60*MenuScale,Max(f2s(NVTimer/60.0,1),0.0),True,False
			Text userOptions\screenWidth/2,100*MenuScale,"SECONDS",True,False
			
			temp% = CreatePivot() : temp2% = CreatePivot()
			PositionEntity temp, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider)
			
			Color 255,255,255;*(NVTimer/600.0)
			
			For np.NPCs = Each NPCs
				If np\NVName<>"" Then ;don't waste your time if the string is empty
					PositionEntity temp2,np\NVX,np\NVY,np\NVZ
					dist# = EntityDistance(temp2,mainPlayer\collider)
					If dist<23.5 Then ;don't draw text if the NPC is too far away
						PointEntity temp, temp2
						yawvalue# = WrapAngle(EntityYaw(mainPlayer\cam) - EntityYaw(temp))
						xvalue# = 0.0
						If yawvalue > 90 And yawvalue <= 180 Then
							xvalue# = Sin(90)/90*yawvalue
						Else If yawvalue > 180 And yawvalue < 270 Then
							xvalue# = Sin(270)/yawvalue*270
						Else
							xvalue = Sin(yawvalue)
						EndIf
						pitchvalue# = WrapAngle(EntityPitch(mainPlayer\cam) - EntityPitch(temp))
						yvalue# = 0.0
						If pitchvalue > 90 And pitchvalue <= 180 Then
							yvalue# = Sin(90)/90*pitchvalue
						Else If pitchvalue > 180 And pitchvalue < 270 Then
							yvalue# = Sin(270)/pitchvalue*270
						Else
							yvalue# = Sin(pitchvalue)
						EndIf
						
						;If (Not IsNVGBlinking%) Then
						Text userOptions\screenWidth / 2 + xvalue * (userOptions\screenWidth / 2),userOptions\screenHeight / 2 - yvalue * (userOptions\screenHeight / 2),np\NVName,True,True
						Text userOptions\screenWidth / 2 + xvalue * (userOptions\screenWidth / 2),userOptions\screenHeight / 2 - yvalue * (userOptions\screenHeight / 2) + 30.0 * MenuScale,f2s(dist,1)+" m",True,True
						;EndIf
					EndIf
				EndIf
			Next
			
			FreeEntity (temp) : FreeEntity (temp2)
			
			Color 0,0,55
			For k=0 To 10
				Rect 45,userOptions\screenHeight*0.5-(k*20),54,10,True
			Next
			Color 0,0,255
			For l=0 To Floor((power%+50)*0.01)
				Rect 45,userOptions\screenHeight*0.5-(l*20),54,10,True
			Next
			;DrawImage NVGImages,40,userOptions\screenHeight*0.5+30,1
			
			Color 255,255,255
		ElseIf IsPlayerWearingTempName(mainPlayer,"nvgoggles") And hasBattery<>0 Then
			Color 0,55,0
			For k=0 To 10
				Rect 45,userOptions\screenHeight*0.5-(k*20),54,10,True
			Next
			Color 0,255,0
			For l=0 To Floor((power%+50)*0.01)
				Rect 45,userOptions\screenHeight*0.5-(l*20),54,10,True
			Next
			;DrawImage NVGImages,40,userOptions\screenHeight*0.5+30,0
		EndIf
	EndIf
	
	;render sprites
	CameraProjMode ark_blur_cam,2
	CameraProjMode mainPlayer\cam,0
	RenderWorld()
	CameraProjMode ark_blur_cam,0
	
	If mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6
		If (wornItem<>Null) And (hasBattery=1) And ((TimeInPosMilliSecs() Mod 800) < 400) Then
			Color 255,0,0
			SetFont uiAssets\font[2]
			
			Text userOptions\screenWidth/2,20*MenuScale,"WARNING: LOW BATTERY",True,False
			Color 255,255,255
		EndIf
	EndIf
End Function

Function CheckForPlayerInFacility()
	;False (=0): NPC is not in facility (mostly meant for "dimension1499")
	;True (=1): NPC is in facility
	;2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)
	
	If EntityY(mainPlayer\collider)>100.0
		Return False
	EndIf
	If EntityY(mainPlayer\collider)< -10.0
		Return 2
	EndIf
	If EntityY(mainPlayer\collider)> 7.0 And EntityY(mainPlayer\collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

;Function ControlSoundVolume()
;	Local snd.Sound,i
;	
;	For snd.Sound = Each Sound
;		For i=0 To 31
;			;If snd\channels[i]<>0 Then
;			;	ChannelVolume snd\channels[i],userOptions\soundVolume#
;			;Else
;				ChannelVolume snd\channels[i],userOptions\soundVolume#
;			;EndIf
;		Next
;	Next
;	
;End Function

;TODO: cleanup
Function CheckTriggers$()
	Local i%,sx#,sy#,sz#
	Local inside% = -1
	
	If mainPlayer\currRoom\TriggerboxAmount = 0
		Return ""
	Else
		For i = 0 To mainPlayer\currRoom\TriggerboxAmount-1
			EntityAlpha mainPlayer\currRoom\Triggerbox[i],1.0
			sx# = EntityScaleX(mainPlayer\currRoom\Triggerbox[i], 1)
			sy# = Max(EntityScaleY(mainPlayer\currRoom\Triggerbox[i], 1), 0.001)
			sz# = EntityScaleZ(mainPlayer\currRoom\Triggerbox[i], 1)
			GetMeshExtents(mainPlayer\currRoom\Triggerbox[i])
			EntityAlpha mainPlayer\currRoom\Triggerbox[i],0.0
			If EntityX(mainPlayer\collider)>((sx#*Mesh_MinX)+mainPlayer\currRoom\x) And EntityX(mainPlayer\collider)<((sx#*Mesh_MaxX)+mainPlayer\currRoom\x)
				If EntityY(mainPlayer\collider)>((sy#*Mesh_MinY)+mainPlayer\currRoom\y) And EntityY(mainPlayer\collider)<((sy#*Mesh_MaxY)+mainPlayer\currRoom\y)
					If EntityZ(mainPlayer\collider)>((sz#*Mesh_MinZ)+mainPlayer\currRoom\z) And EntityZ(mainPlayer\collider)<((sz#*Mesh_MaxZ)+mainPlayer\currRoom\z)
						inside% = i%
						Exit
					EndIf
				EndIf
			EndIf
		Next
		
		If inside% > -1 Then Return mainPlayer\currRoom\TriggerboxName[inside%]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#5
;~C#Blitz3D
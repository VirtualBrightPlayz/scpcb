Include "MarkedForRemoval.bb"

Include "StrictLoads.bb"
Include "KeyName.bb"
Include "Options.bb"

;TODO: FreeFont Font5. Make it local.
Global Font1%, Font2%, Font3%, Font4%, Font5%
Global ConsoleFont%

Global VersionNumber$ = "1.CBN"
;TODO: Deprecate.
Global CompatibleNumber$ = "1.CBN"

AppTitle "SCP - Containment Breach Launcher"

;TODO: Graphics.bb
Global MenuWhite%, MenuBlack%

;TODO: Audio.bb
Global ButtonSFX%

;TODO: Assets.bb
Dim ArrowIMG(4)

;[Block]

;TODO: Assets.bb
Global LauncherIMG%

Global Depth% = 0

Global SelectedGFXMode%

Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%

;TODO: Move somewhere more relevant.
Global WireframeState
Global HalloweenTex

Global TotalGFXModes% = CountGfxModes3D(), GFXModes%
Dim GfxModeWidths%(TotalGFXModes), GfxModeHeights%(TotalGFXModes)

Global RealGraphicWidth%
Global RealGraphicHeight%
Global AspectRatioRatio#

If userOptions\launcher Then 
	AspectRatioRatio = 1.0
	UpdateLauncher()
Else
	For i% = 1 To TotalGFXModes
		Local samefound% = False
		For  n% = 0 To TotalGFXModes - 1
			If GfxModeWidths(n) = GfxModeWidth(i) And GfxModeHeights(n) = GfxModeHeight(i) Then samefound = True : Exit
		Next
		If samefound = False Then
			If userOptions\screenWidth = GfxModeWidth(i) And userOptions\screenHeight = GfxModeHeight(i) Then SelectedGFXMode = GFXModes
			GfxModeWidths(GFXModes) = GfxModeWidth(i)
			GfxModeHeights(GFXModes) = GfxModeHeight(i)
			GFXModes=GFXModes+1
		End If
	Next
	
	userOptions\screenWidth = GfxModeWidths(SelectedGFXMode)
	userOptions\screenHeight = GfxModeHeights(SelectedGFXMode)
EndIf

If userOptions\borderlessWindowed
	RuntimeError "Borderless windowed is not implemented! USERLIBS MUST DIE"
Else
	AspectRatioRatio = 1.0
	Graphics3DExt(userOptions\screenWidth, userOptions\screenHeight, 0, (1 + (Not userOptions\fullscreen)))
EndIf

Global MenuScale# = (userOptions\screenHeight / 1024.0)

SetBuffer(BackBuffer())

Global CurTime%, PrevTime%, LoopDelay%, FPSfactor#, FPSfactor2#
Local CheckFPS%, ElapsedLoops%, FPS%, ElapsedTime#

;TODO: wtf is this?
Global CurrFrameLimit# = userOptions\framelimit

Const HIT_MAP% = 1, HIT_PLAYER% = 2, HIT_ITEM% = 3, HIT_APACHE% = 4, HIT_178% = 5, HIT_DEAD% = 6
SeedRnd MilliSecs()

;[End block]

Global GameSaved%

;TODO: Player.bb
Global CanSave% = True

AppTitle "SCP - Containment Breach v"+VersionNumber

;---------------------------------------------------------------------------------------------------------------------

;[Block]
;TODO: Assets.bb
Global CursorIMG% = LoadImage_Strict("GFX\cursor.png")

;TODO: Assets.bb
Global SelectedLoadingScreen.LoadingScreens, LoadingScreenAmount%, LoadingScreenText%
Global LoadingBack% = LoadImage_Strict("Loadingscreens\loadingback.jpg")
InitLoadingScreens("Loadingscreens\loadingscreens.ini")

;TODO: Assets.bb
;For some reason, Blitz3D doesn't load fonts that have filenames that
;don't match their "internal name" (i.e. their display name in applications
;like Word and such). As a workaround, I moved the files and renamed them so they
;can load without FastText.
Font1% = LoadFont("GFX\font\cour\Courier New.ttf", Int(18 * MenuScale), 0,0,0)
Font2% = LoadFont("GFX\font\courbd\Courier New.ttf", Int(58 * MenuScale), 0,0,0)
Font3% = LoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(22 * MenuScale), 0,0,0)
Font4% = LoadFont("GFX\font\DS-DIGI\DS-Digital.ttf", Int(60 * MenuScale), 0,0,0)
Font5% = LoadFont("GFX\font\Journal\Journal.ttf", Int(58 * MenuScale), 0,0,0)

ConsoleFont% = LoadFont("Blitz", Int(20 * MenuScale), 0,0,0)

SetFont Font2

;TODO: Assets.bb
Global BlinkMeterIMG% = LoadImage_Strict("GFX\blinkmeter.jpg")

DrawLoading(0, True)

Global Mesh_MinX#, Mesh_MinY#, Mesh_MinZ#
Global Mesh_MaxX#, Mesh_MaxY#, Mesh_MaxZ#
Global Mesh_MagX#, Mesh_MagY#, Mesh_MagZ#

Include "Achievements.bb"

Global MouseHit1%, MouseDown1%, MouseHit2%, DoubleClick%, LastMouseHit1%, MouseUp1%

;TODO: Make this not global.
Global CoffinDistance#

Global ExplosionTimer#, ExplosionSFX% ;gate b explosion?

Global LightsOn% = True ;secondary lighting on

;TODO: Not global assuming this is for 106's containment chamber.
Global SoundTransmission%

;misc ---------------------------------------------------------------------------------------------------------------

Include "Difficulty.bb"

;TODO: Radio struct.
Dim RadioState#(10)
Dim RadioState3%(3)
Dim RadioState4%(9)
Dim RadioCHN%(8)

;TODO: Assets.bb
Dim OldAiPics%(5)

;[End block]

Include "Console.bb"

;TODO: Move somewhere?
Global Brightness% = 50

Include "Dreamfilter.bb"

;TODO: Assets.bb
Dim LightSpriteTex(10)

;----------------------------------------------  Sounds -----------------------------------------------------

;[Block]

;TODO: Audio.bb
Global SoundEmitter%
Global TempSounds%[10]
Global TempSoundCHN%
Global TempSoundIndex% = 0

;TODO: Use struct of file paths.
Dim Music%(40)
Music(0) = LoadSound_Strict("SFX\Music\The Dread.ogg")
Music(1) = LoadSound_Strict("SFX\Music\HeavyContainment.ogg") 
Music(2) = LoadSound_Strict("SFX\Music\EntranceZone.ogg") 
;Music(3) = LoadSound_Strict("SFX\Music\PD.ogg")
;Music(4) = LoadSound_Strict("SFX\Music\079.ogg")
;Music(5) = LoadSound_Strict("SFX\Music\GateB1.ogg")
;Music(6) = LoadSound_Strict("SFX\Music\GateB2.ogg")
;Music(7) = LoadSound_Strict("SFX\Music\Room3Storage.ogg") 
;Music(8) = LoadSound_Strict("SFX\Music\Room049.ogg") 
;Music(9) = LoadSound_Strict("SFX\Music\8601.ogg") 
Music(10) = LoadSound_Strict("SFX\Music\106.ogg")
Music(11) = LoadSound_Strict("SFX\Music\Menu.ogg")
;Music(12) = LoadSound_strict("SFX\Music\8601Cancer.ogg")
;Music(13) = LoadSound_strict("SFX\Music\Intro.ogg")
;Music(14) = LoadSound("SFX\178.ogg")
;Music(15) = LoadSound("SFX\Music\PDTrench.ogg")
;Music(15) = LoadSound("SFX\Music\205.ogg")
;
;Music(18): Dimension1499 normal theme
;Music(19): Dimension1499 aggressive theme
;Music(20): SCP-049 tension theme (for "room2sl")
;Music(21): Breath theme after beating the game

;TODO: Audio.bb
Global MusicCHN% = PlaySound_Strict(Music(2))
ChannelVolume(MusicCHN, userOptions\musicVolume)

;TODO: Audio.bb
;Used for fading out music tracks.
Global CurrMusicVolume# = 1.0
Global NowPlaying% = 2
Global ShouldPlay% = 11

DrawLoading(10, True)

;TODO: Audio.bb
Dim OpenDoorSFX%(3,3), CloseDoorSFX%(3,3)
For i = 0 To 2
	OpenDoorSFX(0,i) = LoadSound_Strict("SFX\Door\DoorOpen" + (i + 1) + ".ogg")
	CloseDoorSFX(0,i) = LoadSound_Strict("SFX\Door\DoorClose" + (i + 1) + ".ogg")
	OpenDoorSFX(2,i) = LoadSound_Strict("SFX\Door\Door2Open" + (i + 1) + ".ogg")
	CloseDoorSFX(2,i) = LoadSound_Strict("SFX\Door\Door2Close" + (i + 1) + ".ogg")
Next
For i = 0 To 1
	OpenDoorSFX(1,i) = LoadSound_Strict("SFX\Door\BigDoorOpen" + (i + 1) + ".ogg")
	CloseDoorSFX(1,i) = LoadSound_Strict("SFX\Door\BigDoorClose" + (i + 1) + ".ogg")
Next

;TODO: Audio.bb
Global KeyCardSFX1 = LoadSound_Strict("SFX\Interact\KeyCardUse1.ogg")
Global KeyCardSFX2 = LoadSound_Strict("SFX\Interact\KeyCardUse2.ogg")
Global ButtonSFX2 = LoadSound_Strict("SFX\Interact\Button2.ogg")
Global ScannerSFX1 = LoadSound_Strict("SFX\Interact\ScannerUse1.ogg")
Global ScannerSFX2 = LoadSound_Strict("SFX\Interact\ScannerUse2.ogg")

Global OpenDoorFastSFX=LoadSound_Strict("SFX\Door\DoorOpenFast.ogg")
Global CautionSFX% = LoadSound_Strict("SFX\Room\LockroomSiren.ogg")

Global NuclearSirenSFX%

Global CameraSFX = LoadSound_Strict("SFX\General\Camera.ogg") 

Global StoneDragSFX.MarkedForRemoval

Global GunshotSFX% = LoadSound_Strict("SFX\General\Gunshot.ogg")
Global Gunshot2SFX% = LoadSound_Strict("SFX\General\Gunshot2.ogg")
Global Gunshot3SFX% = LoadSound_Strict("SFX\General\BulletMiss.ogg")
Global BullethitSFX% = LoadSound_Strict("SFX\General\BulletHit.ogg")

Global TeslaIdleSFX = LoadSound_Strict("SFX\Room\Tesla\Idle.ogg")
Global TeslaActivateSFX = LoadSound_Strict("SFX\Room\Tesla\WindUp.ogg")
Global TeslaPowerUpSFX = LoadSound_Strict("SFX\Room\Tesla\PowerUp.ogg")

Global MagnetUpSFX% = LoadSound_Strict("SFX\Room\106Chamber\MagnetUp.ogg"), MagnetDownSFX = LoadSound_Strict("SFX\Room\106Chamber\MagnetDown.ogg")
Global FemurBreakerSFX%

Dim DecaySFX.MarkedForRemoval(5)
;For i = 0 To 3
;	DecaySFX(i) = LoadSound_Strict("SFX\SCP\106\Decay" + i + ".ogg")
;Next

Global BurstSFX = LoadSound_Strict("SFX\Room\TunnelBurst.ogg")

DrawLoading(20, True)

Global Death914SFX% = LoadSound_Strict("SFX\SCP\914\PlayerDeath.ogg"), Use914SFX% = LoadSound_Strict("SFX\SCP\914\PlayerUse.ogg")

Dim DripSFX%(4)
For i = 0 To 3
	DripSFX(i) = LoadSound_Strict("SFX\Character\D9341\BloodDrip" + i + ".ogg")
Next

Global LeverSFX% = LoadSound_Strict("SFX\Interact\LeverFlip.ogg"), LightSFX% = LoadSound_Strict("SFX\General\LightSwitch.ogg")

Global ButtGhostSFX% = LoadSound_Strict("SFX\SCP\Joke\789J.ogg")

Dim RadioSFX(5,10)
RadioSFX(1,0) = LoadSound_Strict("SFX\Radio\RadioAlarm.ogg")
RadioSFX(1,1) = LoadSound_Strict("SFX\Radio\RadioAlarm2.ogg")
For i = 0 To 8
	RadioSFX(2,i) = LoadSound_Strict("SFX\Radio\scpradio"+i+".ogg")
Next
Global RadioSquelch = LoadSound_Strict("SFX\Radio\squelch.ogg")
Global RadioStatic = LoadSound_Strict("SFX\Radio\static.ogg")
Global RadioBuzz = LoadSound_Strict("SFX\Radio\buzz.ogg")

Global ElevatorBeepSFX = LoadSound_Strict("SFX\General\Elevator\Beep.ogg"), ElevatorMoveSFX = LoadSound_Strict("SFX\General\Elevator\Moving.ogg") 

Dim PickSFX%(10)
For i = 0 To 3
	PickSFX(i) = LoadSound_Strict("SFX\Interact\PickItem" + i + ".ogg")
Next

;TODO: More Audio.bb
Global AmbientSFXCHN%, CurrAmbientSFX%
Dim AmbientSFXAmount(6)
;0 = light containment, 1 = heavy containment, 2 = entrance
AmbientSFXAmount(0)=8 : AmbientSFXAmount(1)=11 : AmbientSFXAmount(2)=12
;3 = general, 4 = pre-breach
AmbientSFXAmount(3)=15 : AmbientSFXAmount(4)=5
;5 = forest
AmbientSFXAmount(5)=10

Dim AmbientSFX%(6, 15)

;TODO: Move to NPCData106.
Dim OldManSFX.MarkedForRemoval(6)
;For i = 0 To 2
;	OldManSFX(i) = LoadSound_Strict("SFX\SCP\106\Corrosion" + (i + 1) + ".ogg")
;Next
;OldManSFX(3) = LoadSound_Strict("SFX\SCP\106\Laugh.ogg")
;OldManSFX(4) = LoadSound_Strict("SFX\SCP\106\Breathing.ogg")
;OldManSFX(5) = LoadSound_Strict("SFX\Room\PocketDimension\Enter.ogg")

;TODO: Most of these sounds are NPC-specific anyway. Divide this up.
Dim HorrorSFX.MarkedForRemoval(20)
;For i = 0 To 10
;	HorrorSFX(i) = LoadSound_Strict("SFX\Horror\Horror" + i + ".ogg")
;Next
;For i = 14 To 15
;	HorrorSFX(i) = LoadSound_Strict("SFX\Horror\Horror" + i + ".ogg")
;Next

DrawLoading(25, True)

;TODO: Audio.bb
Dim IntroSFX%(20)

For i = 7 To 9
	IntroSFX(i) = LoadSound_Strict("SFX\Room\Intro\Bang" + (i - 6) + ".ogg")
Next
For i = 10 To 12
	IntroSFX(i) = LoadSound_Strict("SFX\Room\Intro\Light" + (i - 9) + ".ogg")
Next
;IntroSFX(13) = LoadSound_Strict("SFX\intro\shoot1.ogg")
;IntroSFX(14) = LoadSound_Strict("SFX\intro\shoot2.ogg")
IntroSFX(15) = LoadSound_Strict("SFX\Room\Intro\173Vent.ogg")

;TODO: Audio.bb
Dim AlarmSFX%(5)
AlarmSFX(0) = LoadSound_Strict("SFX\Alarm\Alarm.ogg")
;AlarmSFX(1) = LoadSound_Strict("SFX\Alarm\Alarm2.ogg")
AlarmSFX(2) = LoadSound_Strict("SFX\Alarm\Alarm3.ogg")

Dim CommotionState%(23)

Global HeartBeatSFX = LoadSound_Strict("SFX\Character\D9341\Heartbeat.ogg")

Dim BreathSFX(2,5)
Global BreathCHN%
For i = 0 To 4
	BreathSFX(0,i)=LoadSound_Strict("SFX\Character\D9341\breath"+i+".ogg")
	BreathSFX(1,i)=LoadSound_Strict("SFX\Character\D9341\breath"+i+"gas.ogg")
Next

;TODO: Move to NPCData173.
Dim NeckSnapSFX.MarkedForRemoval(3)
;For i = 0 To 2
;	NeckSnapSFX(i) =  LoadSound_Strict("SFX\SCP\173\NeckSnap"+(i+1)+".ogg")
;Next

;TODO: Player struct.
Dim DamageSFX%(9)
For i = 0 To 8
	DamageSFX(i) = LoadSound_Strict("SFX\Character\D9341\Damage"+(i+1)+".ogg")
Next

;TODO: NPCDataMTF.
Dim MTFSFX%(8)

;TODO: Player struct.
Dim CoughSFX%(3)
Global CoughCHN%
For i = 0 To 2
	CoughSFX(i) = LoadSound_Strict("SFX\Character\D9341\Cough" + (i + 1) + ".ogg")
Next

;TODO: Why the fuck is this a global?
Global MachineSFX% = LoadSound_Strict("SFX\SCP\914\Refining.ogg")

;TODO: WHY IS THIS A GLOBAL???
Global ApacheSFX = LoadSound_Strict("SFX\Character\Apache\Propeller.ogg")

;TODO: Player struct.
Global CurrStepSFX.MarkedForRemoval
Dim StepSFX%(4, 2, 8) ;(normal/metal, walk/run, id)
For i = 0 To 7
	StepSFX(0, 0, i) = LoadSound_Strict("SFX\Step\Step" + (i + 1) + ".ogg")
	StepSFX(1, 0, i) = LoadSound_Strict("SFX\Step\StepMetal" + (i + 1) + ".ogg")
	StepSFX(0, 1, i)= LoadSound_Strict("SFX\Step\Run" + (i + 1) + ".ogg")
	StepSFX(1, 1, i) = LoadSound_Strict("SFX\Step\RunMetal" + (i + 1) + ".ogg")
	If i < 3
		StepSFX(2, 0, i) = LoadSound_Strict("SFX\Character\MTF\Step" + (i + 1) + ".ogg")
		StepSFX(3, 0, i) = LoadSound_Strict("SFX\SCP\049\Step"+ (i + 1) + ".ogg")
	EndIf
Next

;TODO: Player struct, also load when actually needed and not be fucking globals.
Dim Step2SFX(6)
For i = 0 To 2
	Step2SFX(i) = LoadSound_Strict("SFX\Step\StepPD" + (i + 1) + ".ogg")
	Step2SFX(i+3) = LoadSound_Strict("SFX\Step\StepForest" + (i + 1) + ".ogg")
Next 

DrawLoading(30, True)

;[End block]

;[Block]
;TODO: Die forever.
Global NTF_1499EnterSFX% = LoadSound_Strict("SFX\SCP\1499\Enter.ogg")
Global NTF_1499LeaveSFX% = LoadSound_Strict("SFX\SCP\1499\Exit.ogg")

;TODO: Not be globals.
Global Monitor2, Monitor3, MonitorTexture2, MonitorTexture3, MonitorTexture4, MonitorTextureOff
Global MonitorTimer# = 0.0, MonitorTimer2# = 0.0, UpdateCheckpoint1%, UpdateCheckpoint2%

;This variable is for when a camera detected the player
	;False: Player is not seen (will be set after every call of the Main Loop
	;True: The Player got detected by a camera
Global PlayerDetected%
Global PrevInjuries#,PrevBloodloss#
Global NoTarget% = False

;TODO: Assets.bb
Global NVGImages = LoadAnimImage("GFX\battery.png",64,64,0,2)
MaskImage NVGImages,255,0,255

;TODO: Player.bb
Global Wearing1499% = False

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

;TODO: Menu.bb
Global OptionsMenu% = 0

;TODO: Not sure what to do with this but it should be moved.
Global QuitMSG% = 0

;TODO: Die.
Global InFacility% = True

;TODO: Remove.
Global IsZombie% = False

;TODO: This is fucking stupid.
Global room2gw_brokendoor% = False
Global room2gw_x# = 0.0
Global room2gw_z# = 0.0
;[End Block]

;-----------------------------------------  Images ----------------------------------------------------------
;TODO: Assets.bb
Global PauseMenuIMG% = LoadImage_Strict("GFX\menu\pausemenu.jpg")
MaskImage PauseMenuIMG, 255,255,0
ScaleImage PauseMenuIMG,MenuScale,MenuScale

;TODO: Assets.bb
Global SprintIcon% = LoadImage_Strict("GFX\sprinticon.png")
Global BlinkIcon% = LoadImage_Strict("GFX\blinkicon.png")
Global CrouchIcon% = LoadImage_Strict("GFX\sneakicon.png")
Global HandIcon% = LoadImage_Strict("GFX\handsymbol.png")
Global HandIcon2% = LoadImage_Strict("GFX\handsymbol2.png")

Global StaminaMeterIMG% = LoadImage_Strict("GFX\staminameter.jpg")

Global KeypadHUD =  LoadImage_Strict("GFX\keypadhud.jpg")
MaskImage(KeypadHUD, 255,0,255)

;TODO: cleanup
Global Panel294 = LoadImage_Strict("GFX\294panel.jpg"), Using294.MarkedForRemoval, Input294$
MaskImage(Panel294, 255,0,255)

DrawLoading(35, True)

;----------------------------------------------  Items  -----------------------------------------------------

Include "Items.bb"

;--------------------------------------- Particles ------------------------------------------------------------

Include "Particles.bb"

;-------------------------------------  Doors --------------------------------------------------------------

Global ClosestButton.MarkedForRemoval, ClosestDoor.MarkedForRemoval
Global SelectedDoor.MarkedForRemoval, UpdateDoorsTimer#
Global DoorTempID%
Type Doors
	Field obj%, obj2%, frameobj%, buttons%[2]
	Field locked%, open%, angle%, openstate#, fastopen%
	Field dir%
	Field timer%, timerstate#
	Field KeyCard%
	Field room.Rooms
	
	Field DisableWaypoint%
	
	Field dist#
	
	Field SoundCHN%
	
	Field Code$
	
	Field ID%
	
	Field Level%
	Field LevelDest%
	
	Field AutoClose%
	
	Field LinkedDoor.Doors
	
	Field IsElevatorDoor% = False
	
	Field MTFClose% = True
	;TODO: Not needed?
	Field NPCCalledElevator% = False
End Type 

;TODO: Move door stuff to its own file?
Dim BigDoorOBJ(2), HeavyDoorObj(2)

Function CreateDoor.Doors(lvl, x#, y#, z#, angle#, room.Rooms, dopen% = False,  big% = False, keycard% = False, code$="")
	Local d.Doors, parent, i%
	If room <> Null Then parent = room\obj
	
	d.Doors = New Doors
	If big=1 Then
		d\obj = CopyEntity(BigDoorOBJ(0))
		ScaleEntity(d\obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		d\obj2 = CopyEntity(BigDoorOBJ(1))
		ScaleEntity(d\obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		
		d\frameobj = CopyEntity(DoorColl)	;CopyMesh				
		ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
		EntityType d\frameobj, HIT_MAP
		EntityAlpha d\frameobj, 0.0
	ElseIf big=2
		d\obj = CopyEntity(HeavyDoorObj(0))
		ScaleEntity(d\obj, RoomScale, RoomScale, RoomScale)
		d\obj2 = CopyEntity(HeavyDoorObj(1))
		ScaleEntity(d\obj2, RoomScale, RoomScale, RoomScale)
		
		d\frameobj = CopyEntity(DoorFrameOBJ)
	Else
		d\obj = CopyEntity(DoorOBJ)
		ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		
		d\frameobj = CopyEntity(DoorFrameOBJ)
		d\obj2 = CopyEntity(DoorOBJ)
		
		ScaleEntity(d\obj2, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		;entityType d\obj2, HIT_MAP
	End If
	
	;scaleentity(d\obj, 0.1, 0.1, 0.1)
	PositionEntity d\frameobj, x, y, z	
	ScaleEntity(d\frameobj, (8.0 / 2048.0), (8.0 / 2048.0), (8.0 / 2048.0))
	EntityType d\obj, HIT_MAP
	EntityType d\obj2, HIT_MAP
	
	d\ID = DoorTempID
	DoorTempID=DoorTempID+1
	
	d\KeyCard = keycard
	d\Code = code
	
	d\Level = lvl
	d\LevelDest = 66
	
	For i% = 0 To 1
		If code <> "" Then 
			d\buttons[i]= CopyEntity(ButtonCodeOBJ)
			EntityFX(d\buttons[i], 1)
		Else
			If keycard>0 Then
				d\buttons[i]= CopyEntity(ButtonKeyOBJ)
			ElseIf keycard<0
				d\buttons[i]= CopyEntity(ButtonScannerOBJ)	
			Else
				d\buttons[i] = CopyEntity(ButtonOBJ)
			End If
		EndIf
		
		ScaleEntity(d\buttons[i], 0.03, 0.03, 0.03)
	Next
	
	If big=1 Then
		PositionEntity d\buttons[0], x - 432.0 * RoomScale, y + 0.7, z + 192.0 * RoomScale
		PositionEntity d\buttons[1], x + 432.0 * RoomScale, y + 0.7, z - 192.0 * RoomScale
		RotateEntity d\buttons[0], 0, 90, 0
		RotateEntity d\buttons[1], 0, 270, 0
	Else
		PositionEntity d\buttons[0], x + 0.6, y + 0.7, z - 0.1
		PositionEntity d\buttons[1], x - 0.6, y + 0.7, z + 0.1
		RotateEntity d\buttons[1], 0, 180, 0		
	End If
	EntityParent(d\buttons[0], d\frameobj)
	EntityParent(d\buttons[1], d\frameobj)
	EntityPickMode(d\buttons[0], 2)
	EntityPickMode(d\buttons[1], 2)
	
	PositionEntity d\obj, x, y, z
	
	RotateEntity d\obj, 0, angle, 0
	RotateEntity d\frameobj, 0, angle, 0
	
	If d\obj2 <> 0 Then
		PositionEntity d\obj2, x, y, z
		If big=1 Then
			RotateEntity(d\obj2, 0, angle, 0)
		Else
			RotateEntity(d\obj2, 0, angle + 180, 0)
		EndIf
		EntityParent(d\obj2, parent)
	EndIf
	
	EntityParent(d\frameobj, parent)
	EntityParent(d\obj, parent)
	
	d\angle = angle
	d\open = dopen		
	
	EntityPickMode(d\obj, 3)
	MakeCollBox(d\obj)
	If d\obj2 <> 0 Then
		EntityPickMode(d\obj2, 3)
		MakeCollBox(d\obj2)
	End If
	
	EntityPickMode d\frameobj,2
	
	If d\open And big = False And Rand(8) = 1 Then d\AutoClose = True
	d\dir=big
	d\room=room
	
	d\MTFClose = True
	
	Return d
	
End Function

Function CreateButton(x#,y#,z#, pitch#,yaw#,roll#=0)
	Local obj = CopyEntity(ButtonOBJ)	
	
	ScaleEntity(obj, 0.03, 0.03, 0.03)
	
	PositionEntity obj, x,y,z
	RotateEntity obj, pitch,yaw,roll
	
	EntityPickMode(obj, 2)	
	
	Return obj
End Function

Function UpdateDoors()
	
	Local i%, d.Doors, x#, z#, dist#
	If UpdateDoorsTimer =< 0 Then
		For d.Doors = Each Doors
			Local xdist# = Abs(EntityX(mainPlayer\collider)-EntityX(d\obj,True))
			Local zdist# = Abs(EntityZ(mainPlayer\collider)-EntityZ(d\obj,True))
			
			d\dist = xdist+zdist
			
			If d\dist > HideDistance*2 Then
				If d\obj <> 0 Then HideEntity d\obj
				If d\frameobj <> 0 Then HideEntity d\frameobj
				If d\obj2 <> 0 Then HideEntity d\obj2
				If d\buttons[0] <> 0 Then HideEntity d\buttons[0]
				If d\buttons[1] <> 0 Then HideEntity d\buttons[1]				
			Else
				If d\obj <> 0 Then ShowEntity d\obj
				If d\frameobj <> 0 Then ShowEntity d\frameobj
				If d\obj2 <> 0 Then ShowEntity d\obj2
				If d\buttons[0] <> 0 Then ShowEntity d\buttons[0]
				If d\buttons[1] <> 0 Then ShowEntity d\buttons[1]
			EndIf
			
			;TODO: this is cancer
			If mainPlayer\currRoom\RoomTemplate\Name$ = "room2sl"
				If ValidRoom2slCamRoom(d\room)
					If d\obj <> 0 Then ShowEntity d\obj
					If d\frameobj <> 0 Then ShowEntity d\frameobj
					If d\obj2 <> 0 Then ShowEntity d\obj2
					If d\buttons[0] <> 0 Then ShowEntity d\buttons[0]
					If d\buttons[1] <> 0 Then ShowEntity d\buttons[1]
				EndIf
			EndIf
		Next
		
		UpdateDoorsTimer = 30
	Else
		UpdateDoorsTimer = Max(UpdateDoorsTimer-FPSfactor,0)
	EndIf
	
	mainPlayer\closestButton = 0
	mainPlayer\closestDoor = Null
	
	For d.Doors = Each Doors
		If d\dist < HideDistance*2 Then 
			
			If (d\openstate >= 180 Or d\openstate <= 0) And mainPlayer\grabbedEntity = 0 Then
				For i% = 0 To 1
					If d\buttons[i] <> 0 Then
						If Abs(EntityX(mainPlayer\collider)-EntityX(d\buttons[i],True)) < 1.0 Then 
							If Abs(EntityZ(mainPlayer\collider)-EntityZ(d\buttons[i],True)) < 1.0 Then 
								dist# = Distance(EntityX(mainPlayer\collider, True), EntityZ(mainPlayer\collider, True), EntityX(d\buttons[i], True), EntityZ(d\buttons[i], True));entityDistance(collider, d\buttons[i])
								If dist < 0.7 Then
									;TODO: use deltayaw as faster way to determine whether the player can press the button or not
									Local temp% = CreatePivot()
									PositionEntity temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
									PointEntity temp,d\buttons[i]
									
									If EntityPick(temp, 0.6) = d\buttons[i] Then
										If mainPlayer\closestButton = 0 Then
											mainPlayer\closestButton = d\buttons[i]
											mainPlayer\closestDoor = d
										Else
											If dist < EntityDistance(mainPlayer\collider, mainPlayer\closestButton) Then mainPlayer\closestButton = d\buttons[i] : mainPlayer\closestDoor = d
										End If							
									End If
									
									FreeEntity temp
									
								EndIf							
							EndIf
						EndIf
						
					EndIf
				Next
			EndIf
			
			If d\open Then
				If d\openstate < 180 Then
					Select d\dir
						Case 0
							d\openstate = Min(180, d\openstate + FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen*2+1) * FPSfactor / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen+1) * FPSfactor / 80.0, 0, 0)		
						Case 1
							d\openstate = Min(180, d\openstate + FPSfactor * 0.8)
							MoveEntity(d\obj, Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, -Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
						Case 2
							d\openstate = Min(180, d\openstate + FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen+1) * FPSfactor / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen*2+1) * FPSfactor / 120.0, 0, 0)		
					End Select
				Else
					d\fastopen = 0
					ResetEntity(d\obj)
					If d\obj2 <> 0 Then ResetEntity(d\obj2)
					If d\timerstate > 0 Then
						d\timerstate = Max(0, d\timerstate - FPSfactor)
						If d\timerstate + FPSfactor > 110 And d\timerstate <= 110 Then PlaySound2(CautionSFX, mainPlayer\cam, d\obj)
						;If d\timerstate = 0 Then d\open = (Not d\open) : PlaySound2(CloseDoorSFX(Min(d\dir,1),Rand(0, 2)), mainPlayer\cam, d\obj)
						Local sound%
						If d\dir = 1 Then sound% = Rand(0, 1) Else sound% = Rand(0, 2)
						If d\timerstate = 0 Then d\open = (Not d\open) : PlaySound2(CloseDoorSFX(d\dir,sound%), mainPlayer\cam, d\obj)
					EndIf
					If d\AutoClose And RemoteDoorOn = True Then
						If EntityDistance(mainPlayer\cam, d\obj) < 2.1 Then
							;PlaySound_Strict HorrorSFX(7) ;TODO: fix
							d\open = False : PlaySound2(CloseDoorSFX(Min(d\dir,1), Rand(0, 2)), mainPlayer\cam, d\obj) : d\AutoClose = False
						EndIf
					End If				
				End If
			Else
				If d\openstate > 0 Then
					Select d\dir
						Case 0
							d\openstate = Max(0, d\openstate - FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 80.0, 0, 0)	
						Case 1
							d\openstate = Max(0, d\openstate - FPSfactor*0.8)
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
							If d\openstate < 15 And d\openstate+FPSfactor => 15
								For i = 0 To Rand(75,99)
									Local pvt% = CreatePivot()
									PositionEntity(pvt, EntityX(d\frameobj,True)+Rnd(-0.2,0.2), EntityY(d\frameobj,True)+Rnd(0.0,1.2), EntityZ(d\frameobj,True)+Rnd(-0.2,0.2))
									RotateEntity(pvt, 0, Rnd(360), 0)
									
									Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
									p\speed = 0.005
									RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
									
									p\SizeChange = -0.00001
									p\size = 0.01
									ScaleSprite p\obj,p\size,p\size
									
									p\Achange = -0.01
									
									EntityOrder p\obj,-1
									
									FreeEntity pvt
								Next
							EndIf
						Case 2
							d\openstate = Max(0, d\openstate - FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 120.0, 0, 0)
					End Select
					
					If d\angle = 0 Or d\angle=180 Then
						If Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.15 Then
							If Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.7*(d\dir*2+1) Then
								z# = CurveValue(EntityZ(d\frameobj,True)+0.15*Sgn(EntityZ(mainPlayer\collider)-EntityZ(d\frameobj, True)), EntityZ(mainPlayer\collider), 5)
								PositionEntity mainPlayer\collider, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), z
							EndIf
						EndIf
					Else
						If Abs(EntityX(d\frameobj, True)-EntityX(mainPlayer\collider))<0.15 Then	
							If Abs(EntityZ(d\frameobj, True)-EntityZ(mainPlayer\collider))<0.7*(d\dir*2+1) Then
								x# = CurveValue(EntityX(d\frameobj,True)+0.15*Sgn(EntityX(mainPlayer\collider)-EntityX(d\frameobj, True)), EntityX(mainPlayer\collider), 5)
								PositionEntity mainPlayer\collider, x, EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider)
							EndIf
						EndIf
					EndIf
					
				Else
					d\fastopen = 0
					PositionEntity(d\obj, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 Then PositionEntity(d\obj2, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 And d\dir = 0 Then
						MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
						MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
					EndIf	
				End If
			End If
			
		EndIf
		
	Next
End Function

Function UseDoor(d.Doors, showmsg%=True)
	Local temp% = 0
	If d\KeyCard > 0 Then
		If mainPlayer\selectedItem = Null Then
			If showmsg = True Then 
				Msg = "A keycard is required to operate this door."
				MsgTimer = 70 * 5
			EndIf
			Return
		Else
			Select mainPlayer\selectedItem\itemtemplate\tempname
				Case "key1"
					temp = 1
				Case "key2"
					temp = 2
				Case "key3"
					temp = 3
				Case "key4"
					temp = 4
				Case "key5"
					temp = 5
				Case "key6"
					temp = 6
				Default 
					temp = -1
			End Select
			
			If temp =-1 Then 
				If showmsg = True Then 
					Msg = "A keycard is required to operate this door."
					MsgTimer = 70 * 5
				EndIf
				Return				
			ElseIf temp >= d\KeyCard 
				mainPlayer\selectedItem = Null
				If showmsg = True Then
					If d\locked Then
						PlaySound_Strict KeyCardSFX2
						Msg = "The keycard was inserted into the slot but nothing happened."
						MsgTimer = 70 * 5
						Return
					Else
						PlaySound_Strict KeyCardSFX1
						Msg = "The keycard was inserted into the slot."
						MsgTimer = 70 * 5		
					EndIf
				EndIf
			Else
				mainPlayer\selectedItem = Null
				If showmsg = True Then 
					PlaySound_Strict KeyCardSFX2					
					If d\locked Then
						Msg = "The keycard was inserted into the slot but nothing happened."
					Else
						Msg = "A keycard with a higher security clearance is required to operate this door."
					EndIf
					MsgTimer = 70 * 5							
				EndIf
				Return
			End If
		EndIf	
	ElseIf d\KeyCard < 0
		;I can't find any way to produce short circuited boolean expressions so work around this by using a temporary variable - risingstar64
		If mainPlayer\selectedItem <> Null Then
			temp = (mainPlayer\selectedItem\itemtemplate\tempname = "hand" And d\KeyCard=-1) Or (mainPlayer\selectedItem\itemtemplate\tempname = "hand2" And d\KeyCard=-2)
		EndIf
		If temp <> 0 Then
			PlaySound_Strict ScannerSFX1
			Msg = "You place the palm of the hand onto the scanner. The scanner reads: "+Chr(34)+"DNA verified. Access granted."+Chr(34)
			MsgTimer = 70 * 10
		Else
			If showmsg = True Then 
				PlaySound_Strict ScannerSFX2
				Msg = "You placed your palm onto the scanner. The scanner reads: "+Chr(34)+"DNA does not match known sample. Access denied."+Chr(34)
				MsgTimer = 70 * 10
			EndIf
			Return			
		EndIf
	Else
		If d\locked Then
			If showmsg = True Then 
				If Not (d\IsElevatorDoor>0) Then
					PlaySound_Strict ButtonSFX2
					Msg = "The door appears to be locked."
					MsgTimer = 70 * 5
				Else
					If d\IsElevatorDoor = 1 Then
						Msg = "You called the elevator."
						MsgTimer = 70 * 5
					ElseIf (Msg<>"You called the elevator.")
						If (Msg="You already called the elevator.") Or (MsgTimer<70*3)	
							Select Rand(10)
								Case 1
									Msg = "Stop spamming the button."
									MsgTimer = 70 * 7
								Case 2
									Msg = "Pressing it harder does not make the elevator come faster."
									MsgTimer = 70 * 7
								Case 3
									Msg = "If you continue pressing this button I will generate a Memory Access Violation."
									MsgTimer = 70 * 7
								Default
									Msg = "You already called the elevator."
									MsgTimer = 70 * 7
							End Select
						EndIf
					Else
						Msg = "You already called the elevator."
						MsgTimer = 70 * 7
					EndIf
				EndIf
				
			EndIf
			Return
		EndIf	
	EndIf
	
	d\open = (Not d\open)
	If d\LinkedDoor <> Null Then d\LinkedDoor\open = (Not d\LinkedDoor\open)
	
	Local sound = 0
	;If d\dir = 1 Then sound = 0 Else sound=Rand(0, 2)
	If d\dir = 1 Then sound=Rand(0, 1) Else sound=Rand(0, 2)
	
	If d\open Then
		If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
		d\timerstate = d\timer
		d\SoundCHN = PlaySound2 (OpenDoorSFX(d\dir, sound), mainPlayer\cam, d\obj)
	Else
		d\SoundCHN = PlaySound2 (CloseDoorSFX(d\dir, sound), mainPlayer\cam, d\obj)
	End If
		
	
End Function

Function RemoveDoor(d.Doors)
	If d\buttons[0] <> 0 Then EntityParent d\buttons[0], 0
	If d\buttons[1] <> 0 Then EntityParent d\buttons[1], 0	
	
	If d\obj <> 0 Then FreeEntity d\obj
	If d\obj2 <> 0 Then FreeEntity d\obj2
	If d\frameobj <> 0 Then FreeEntity d\frameobj
	If d\buttons[0] <> 0 Then FreeEntity d\buttons[0]
	If d\buttons[1] <> 0 Then FreeEntity d\buttons[1]	
	
	Delete d
End Function

;---------------------------------------------------------------------------------------------------

Include "Menu.bb"

;---------------------------------------------------------------------------------------------------

DrawLoading(40,True)

Include "MapSystem.bb"

DrawLoading(80,True)

Include "NPCs.bb"

Include "Player.bb"

;-------------------------------------  Events --------------------------------------------------------------
;TODO: Events.bb
Type Events
	Field EventName$
	Field room.Rooms
	
	Field EventState#, EventState2#, EventState3#
	Field SoundCHN%, SoundCHN2%
	Field Sound, Sound2
	
	Field EventStr$
	
	Field img%
End Type 

Include "Events.bb"

Collisions HIT_PLAYER, HIT_MAP, 2, 2
Collisions HIT_PLAYER, HIT_PLAYER, 1, 3
Collisions HIT_ITEM, HIT_MAP, 2, 2
Collisions HIT_APACHE, HIT_APACHE, 1, 2
Collisions HIT_178, HIT_MAP, 2, 2
Collisions HIT_178, HIT_178, 1, 3
Collisions HIT_DEAD, HIT_MAP, 2, 2

DrawLoading(90, True)

;----------------------------------- meshes and textures ----------------------------------------------------------------
;TODO: Assets.bb
Global TeslaTexture%

Dim LightSpriteTex%(5)
Global DoorOBJ%, DoorFrameOBJ%

Global LeverOBJ%, LeverBaseOBJ%

Global DoorColl%
Global ButtonOBJ%, ButtonKeyOBJ%, ButtonCodeOBJ%, ButtonScannerOBJ%

Dim DecalTextures%(20)

Global Monitor%, MonitorTexture%
Global CamBaseOBJ%, CamOBJ%

Global LiquidObj%,MTFObj%,ClassDObj%
Global ApacheObj%,ApacheRotorObj%

Global UnableToMove.MarkedForRemoval

FlushKeys()
FlushMouse()

DrawLoading(100, True)

LoopDelay = MilliSecs()

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------       		MAIN LOOP                 ---------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

Repeat
	UpdateGame()
Forever

Function UpdateGame()
	Cls
	
	CurTime = MilliSecs()
	ElapsedTime = (CurTime - PrevTime) / 1000.0
	PrevTime = CurTime
	FPSfactor = Max(Min(ElapsedTime * 70, 5.0), 0.2)
	FPSfactor2 = FPSfactor
	
	If IsPaused() Then FPSfactor = 0
	
	If userOptions\framelimit > 0 Then
	    ;Framelimit
		Local WaitingTime% = (1000.0 / userOptions\framelimit) - (MilliSecs() - LoopDelay)
		Delay WaitingTime%
		
	   LoopDelay = MilliSecs()
	EndIf
	
	;Counting the fps
	If CheckFPS < MilliSecs2() Then
		FPS = ElapsedLoops
		ElapsedLoops = 0
		CheckFPS = MilliSecs2()+1000
	EndIf
	ElapsedLoops = ElapsedLoops + 1
	
	DoubleClick = False
	MouseHit1 = MouseHit(1)
	If MouseHit1 Then
		If MilliSecs2() - LastMouseHit1 < 800 Then DoubleClick = True
		LastMouseHit1 = MilliSecs2()
	EndIf
	
	Local prevmousedown1 = MouseDown1
	MouseDown1 = MouseDown(1)
	If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
	
	MouseHit2 = MouseHit(2)
	
	If (Not MouseDown1) And (Not MouseHit1) Then mainPlayer\grabbedEntity = 0
	
	UpdateMusic()
	
	If CurrGameState=GAMESTATE_MAINMENU Then
		If ShouldPlay = 21 Then
			If TempSoundCHN = 0 Then
				For snd.Sound = Each Sound
					For i = 0 To 31
						If snd\channels[i]<>0 Then
							StopChannel snd\channels[i]
						EndIf
					Next
				Next
				TempSoundCHN = PlaySound_Strict(Music(21))
			EndIf
			If (Not ChannelPlaying(TempSoundCHN)) Then FreeSound_Strict Music(21) : ShouldPlay = 11
		Else
			ShouldPlay = 11
		EndIf
		UpdateMainMenu()
	Else
		ShouldPlay = 0 ;TODO: FIX ;Min(PlayerZone,2)
		
		DrawHandIcon = False
		
		If FPSfactor > 0 Then UpdateSecurityCams()
		
		If KeyHit(keyBinds\inv) Then 
			If CurrGameState=GAMESTATE_INVENTORY Then
				CurrGameState=GAMESTATE_PLAYING
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1# = 0.0 : mouse_y_speed_1# = 0.0
			Else
				CurrGameState=GAMESTATE_INVENTORY
				PauseSounds()
			EndIf
			
			mainPlayer\selectedItem = Null 
		EndIf
		
		If mainPlayer\currRoom\RoomTemplate\Name <> "pocketdimension" And mainPlayer\currRoom\RoomTemplate\Name <> "gatea" And mainPlayer\currRoom\RoomTemplate\Name <> "exit1" And (Not IsPaused()) Then 
			
			If Rand(1500) = 1 Then
				For i = 0 To 5
					If AmbientSFX(i,CurrAmbientSFX) <> 0 Then
						If ChannelPlaying(AmbientSFXCHN) = 0 Then FreeSound_Strict AmbientSFX(i,CurrAmbientSFX) : AmbientSFX(i,CurrAmbientSFX) = 0
					EndIf			
				Next
				
				PositionEntity (SoundEmitter, EntityX(mainPlayer\cam) + Rnd(-1.0, 1.0), 0.0, EntityZ(mainPlayer\cam) + Rnd(-1.0, 1.0))
				
				If mainPlayer\currRoom\RoomTemplate\Name = "room860"
					For e.Events = Each Events
						If e\EventName = "room860"
							If e\EventState = 1.0 Then
								PositionEntity (SoundEmitter, EntityX(mainPlayer\cam) + Rnd(-1.0, 1.0), 30.0, EntityZ(mainPlayer\cam) + Rnd(-1.0, 1.0))
							EndIf
							
							Exit
						EndIf
					Next
				EndIf
				
				;CurrAmbientSFX = Rand(0,AmbientSFXAmount(PlayerZone)-1) ;TODO: fix
				
				;Select PlayerZone
				;	Case 0,1,2
				;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound_Strict("SFX\Ambient\Zone"+(PlayerZone+1)+"\ambient"+(CurrAmbientSFX+1)+".ogg")
				;	Case 3
				;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound_Strict("SFX\Ambient\General\ambient"+(CurrAmbientSFX+1)+".ogg")
				;	Case 4
				;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound_Strict("SFX\Ambient\Pre-breach\ambient"+(CurrAmbientSFX+1)+".ogg")
				;	Case 5
				;		If AmbientSFX(PlayerZone,CurrAmbientSFX)=0 Then AmbientSFX(PlayerZone,CurrAmbientSFX)=LoadSound_Strict("SFX\Ambient\Forest\ambient"+(CurrAmbientSFX+1)+".ogg")
				;End Select
				
				;AmbientSFXCHN = PlaySound2(AmbientSFX(PlayerZone,CurrAmbientSFX), mainPlayer\cam, SoundEmitter)
			EndIf
			
			If Rand(50000) = 3 Then
				Local RN$ = mainPlayer\currRoom\RoomTemplate\Name$
				If RN$ <> "room860" And RN$ <> "room1123" And RN$ <> "173" And RN$ <> "dimension1499" Then
					;If FPSfactor > 0 Then LightBlink = Rnd(1.0,2.0)
					PlaySound_Strict  LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,7)+".ogg")
				EndIf 
			EndIf
		EndIf
		
		UpdateCheckpoint1 = False
		UpdateCheckpoint2 = False
		
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
			UpdateEvents()
			UpdateDecals()
			UpdateMTF()
			UpdateNPCs()
			UpdateItems()
			UpdateParticles()
			UpdateScreens()
			TimeCheckpointMonitors()
			UpdateLeave1499()
		EndIf
		
		;If InfiniteStamina Then mainPlayer\stamina = Min(100, mainPlayer\stamina + (100.0-mainPlayer\stamina)*0.01*FPSfactor)
		
		UpdateWorld()
		ManipulateNPCBones()
		RenderWorld2()
		
		mainPlayer\blurTimer = Min(CurveValue(0.0, mainPlayer\blurTimer, 20.0),0.95)
		If mainPlayer\blurTimer > 0.0 Then
			mainPlayer\blurTimer = Max(Min(0.95, mainPlayer\blurTimer / 1000.0), mainPlayer\blurTimer)
			mainPlayer\blurTimer = Max(mainPlayer\blurTimer - FPSfactor, 0.0)
		End If
		
		UpdateBlur(mainPlayer\blurTimer)
		
		;[Block]
		
		Local darkA# = 0.0
		If Not IsPaused()  Then
			If mainPlayer\sanity895 < 0 Then
				mainPlayer\sanity895 = Min(mainPlayer\sanity895 + FPSfactor, 0.0)
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
;				EyeStuck = Max(EyeStuck-FPSfactor,0)
;				
;				If EyeStuck < 9000 Then mainPlayer\blurTimer = Max(mainPlayer\blurTimer, (9000-EyeStuck)*0.5)
;				If EyeStuck < 6000 Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.0),1.0)
;				If EyeStuck < 9000 And EyeStuck+FPSfactor =>9000 Then 
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

				mainPlayer\blinkTimer = mainPlayer\blinkTimer - FPSfactor
			Else
				mainPlayer\blinkTimer = mainPlayer\blinkTimer - FPSfactor * 0.6
				;TODO: fix
				;If EyeIrritation > 0 Then mainPlayer\blinkTimer=BlinkTimer-Min(EyeIrritation / 100.0 + 1.0, 4.0) * FPSfactor * BlinkEffect
				
				darkA = Max(darkA, 0.0)
			End If
			
			;TODO: fix
			;EyeIrritation = Max(0, EyeIrritation - FPSfactor)
			
			If mainPlayer\blinkEffect > 0 Then
				mainPlayer\blinkEffect = mainPlayer\blinkEffect - (FPSfactor/70)
			;TODO: wtf does this do	
			;Else
			;	If BlinkEffect <> 1.0 Then BlinkEffect = 1.0
			;	BlinkEffect = CurveValue(1.0,BlinkEffect,500)
			EndIf
			
			;TODO: reimplement
			;LightBlink = Max(LightBlink - (FPSfactor / 35.0), 0)
			;If LightBlink > 0 Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3, 0.8)), 1.0)
			
			If CurrGameState=GAMESTATE_SCP294 Then darkA=1.0
			
			If (Not IsPlayerWearing(mainPlayer,"nvgoggles")) Then darkA = Max((1.0-SecondaryLightOn)*0.9, darkA)
			
			If mainPlayer\dead Then
				CurrGameState = GAMESTATE_PLAYING
				CurrGameSubstate = GAMESUBSTATE_PAUSED_DEAD
				mainPlayer\selectedItem = Null
				SelectedScreen = Null
				SelectedMonitor = Null
				;mainPlayer\blurTimer = Abs(mainPlayer\fallTimer*5)
				;mainPlayer\fallTimer=mainPlayer\fallTimer-(FPSfactor*0.8)
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
				mainPlayer\fallTimer=mainPlayer\fallTimer-FPSfactor
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
			mainPlayer\lightFlash = Max(mainPlayer\lightFlash - (FPSfactor / 70.0), 0)
		Else
			HideEntity mainPlayer\overlays[OVERLAY_WHITE]
			;EntityAlpha(Light, mainPlayer\lightFlash)
		End If
		
		EntityColor mainPlayer\overlays[OVERLAY_WHITE],255,255,255
		
		;[End block]
		
		If KeyHit(keyBinds\save) Then
			If SelectedDifficulty\saveType = SAVEANYWHERE Then
				RN$ = mainPlayer\currRoom\RoomTemplate\Name$
				If RN$ = "173" Or RN$ = "exit1" Or RN$ = "gatea"
					Msg = "You cannot save in this location."
					MsgTimer = 70 * 4
				ElseIf (Not CanSave) Or QuickLoadPercent > -1
					Msg = "You cannot save at this moment."
					MsgTimer = 70 * 4
					If QuickLoadPercent > -1
						Msg = Msg + " (game is loading)"
					EndIf
				Else
					SaveGame(SavePath + CurrSave + "\")
				EndIf
			ElseIf SelectedDifficulty\saveType = SAVEONSCREENS
				If SelectedScreen=Null And SelectedMonitor=Null Then
					Msg = "Saving is only permitted on clickable monitors scattered throughout the facility."
					MsgTimer = 70 * 4						
				Else
					RN$ = mainPlayer\currRoom\RoomTemplate\Name$
					If RN$ = "173" Or RN$ = "exit1" Or RN$ = "gatea"
						Msg = "You cannot save in this location."
						MsgTimer = 70 * 4
					ElseIf (Not CanSave) Or QuickLoadPercent > -1
						Msg = "You cannot save at this moment."
						MsgTimer = 70 * 4
						If QuickLoadPercent > -1
							Msg = Msg + " (game is loading)"
						EndIf
					Else
						SaveGame(SavePath + CurrSave + "\")
					EndIf
				EndIf
			Else
				Msg = "Quick saving is disabled."
				MsgTimer = 70 * 4
			EndIf
		Else If SelectedDifficulty\saveType = SAVEONSCREENS And (SelectedScreen<>Null Or SelectedMonitor<>Null)
			If (Msg<>"Game progress saved." And Msg<>"You cannot save in this location."And Msg<>"You cannot save at this moment.") Or MsgTimer<=0 Then
				Msg = "Press "+KeyName(keyBinds\save)+" to save."
				MsgTimer = 70*4
			EndIf
			
			If MouseHit2 Then SelectedMonitor = Null
		EndIf
		
		If KeyHit(keyBinds\console) Then
			If CurrGameState=GAMESTATE_CONSOLE Then
				UsedConsole = True
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
				CurrGameState=GAMESTATE_PLAYING
			Else
				PauseSounds()
				CurrGameState=GAMESTATE_CONSOLE
			EndIf
			FlushKeys()
		EndIf
		
		DrawGUI()
		
		;TODO: fix
		;If EndingTimer < 0 Then
		;	If SelectedEnding <> "" Then DrawEnding()
		;Else
		;	DrawMenu()			
		;EndIf
		
		UpdateConsole()
		
		If MsgTimer > 0 Then
			Local temp% = False ;TODO: change this variable's name because it's dumb as hell
			If CurrGameState<>GAMESTATE_INVENTORY Then
				If mainPlayer\selectedItem <> Null
					If mainPlayer\selectedItem\itemtemplate\tempname = "paper" Or mainPlayer\selectedItem\itemtemplate\tempname = "oldpaper"
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
			MsgTimer=MsgTimer-FPSfactor2 
		End If
		
		Color 255, 255, 255
		If userOptions\showFPS Then
			SetFont ConsoleFont
			Text 20, 20, "FPS: " + FPS
			SetFont Font1
		EndIf
		
		DrawQuickLoading()
	End If
	
	If userOptions\borderlessWindowed Then
		If (RealGraphicWidth<>userOptions\screenWidth) Or (RealGraphicHeight<>userOptions\screenHeight) Then
			SetBuffer TextureBuffer(fresize_texture)
			ClsColor 0,0,0 : Cls
			CopyRect 0,0,userOptions\screenWidth,userOptions\screenHeight,1024-userOptions\screenWidth/2,1024-userOptions\screenHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
			SetBuffer BackBuffer()
			ClsColor 0,0,0 : Cls
			ScaleRender(0,0,2050.0 / Float(userOptions\screenWidth) * AspectRatioRatio, 2050.0 / Float(userOptions\screenWidth) * AspectRatioRatio)
			;might want to replace Float(userOptions\screenWidth) with Max(userOptions\screenWidth,userOptions\screenHeight) if portrait sizes cause issues
			;everyone uses landscape so it's probably a non-issue
		EndIf
	EndIf
	
	;not by any means a perfect solution
	;Not even proper gamma correction but it's a nice looking alternative that works in windowed mode
	If userOptions\screenGamma>1.0 Then
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,1024-RealGraphicWidth/2,1024-RealGraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,3
		EntityAlpha fresize_image,userOptions\screenGamma-1.0
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth))
	ElseIf userOptions\screenGamma<1.0 Then ;todo: maybe optimize this if it's too slow, alternatively give players the option to disable gamma
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,1024-RealGraphicWidth/2,1024-RealGraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,2
		EntityAlpha fresize_image,1.0
		SetBuffer TextureBuffer(fresize_texture2)
		ClsColor 255*userOptions\screenGamma,255*userOptions\screenGamma,255*userOptions\screenGamma
		Cls
		SetBuffer BackBuffer()
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth),2048.0 / Float(RealGraphicWidth))
		SetBuffer(TextureBuffer(fresize_texture2))
		ClsColor 0,0,0
		Cls
		SetBuffer(BackBuffer())
	EndIf
	EntityFX fresize_image,1
	EntityBlend fresize_image,1
	EntityAlpha fresize_image,1.0
	
	If (Not userOptions\vsync) Then
		Flip 0
	Else 
		Flip 1
	EndIf
End Function

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

;[Block] ;TODO: FIX
;Function DrawEnding()
;	
;	ShowPointer()
;	
;	FPSfactor = 0
;	EndingTimer=EndingTimer-FPSfactor2
;	
;	GiveAchievement(Achv055)
;	If (Not UsedConsole) GiveAchievement(AchvConsole)
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
;			If ChannelPlaying(BreathCHN) Then StopChannel BreathCHN : mainPlayer\stamina = 100
;		EndIf
;		
;		If EndingTimer <-400 Then 
;			If Music(5)=0 Then Music(5) = LoadSound_Strict("SFX\Music\Intro.ogg")
;			ShouldPlay = 5
;		EndIf
;		
;		If EndingScreen = 0 Then 
;			EndingScreen = LoadImage_Strict("GFX\endingscreen.pt")
;			
;			temp = LoadSound_Strict ("SFX\Music\Ending.ogg")
;			PlaySound_Strict temp
;			
;			PlaySound_Strict LightSFX
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
;			If EndingTimer+FPSfactor2 > -450 And EndingTimer <= -450 Then
;				Select Lower(SelectedEnding)
;					Case "a1", "a2"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateA\Ending"+SelectedEnding+".ogg")
;					Case "b1", "b2", "b3"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateB\Ending"+SelectedEnding+".ogg")
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
;				SetFont Font2
;				Text(x + width / 2 + 40*MenuScale, y + 20*MenuScale, "THE END", True)
;				SetFont Font1
;				
;				If AchievementsMenu=0 Then 
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
;						scpsEncountered = scpsEncountered+Achievements(i)
;					Next
;					
;					Local achievementsUnlocked =0
;					For i = 0 To MAXACHIEVEMENTS-1
;						achievementsUnlocked = achievementsUnlocked + Achievements(i)
;					Next
;					
;					Text x, y, "SCPs encountered: " +scpsEncountered
;					Text x, y+20*MenuScale, "Achievements unlocked: " + achievementsUnlocked+"/"+(MAXACHIEVEMENTS-1)
;					Text x, y+40*MenuScale, "Rooms found: " + roomsfound+"/"+roomamount
;					Text x, y+60*MenuScale, "Documents discovered: " +docsfound+"/"+docamount
;					Text x, y+80*MenuScale, "Items refined in SCP-914: " +RefinedItems			
;					
;					x = userOptions\screenWidth / 2 - width / 2
;					y = userOptions\screenHeight / 2 - height / 2
;					x = x+width/2
;					y = y+height-100*MenuScale
;					
;					If DrawButton(x-145*MenuScale,y-200*MenuScale,390*MenuScale,60*MenuScale,"ACHIEVEMENTS", True) Then
;						AchievementsMenu = 1
;					EndIf
;					
;					If DrawButton(x-145*MenuScale,y-100*MenuScale,390*MenuScale,60*MenuScale,"MAIN MENU", True) Then
;						NullGame()
;						Music(21) = LoadSound_Strict("SFX\Ending\MenuBreath.ogg")
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
;			EndIf
;			
;		EndIf
;		
;	EndIf
;	
;	If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
;	
;	SetFont Font1
;End Function
;[End Block]

;--------------------------------------- GUI, menu etc ------------------------------------------------

Function DrawGUI()	
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$
	
	Local e.Events, it.Items
	
	If IsPaused() Then
		ShowPointer()
	Else
		HidePointer()
	EndIf 	
	
	If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Then
		For e.Events = Each Events
			If e\room = mainPlayer\currRoom And e\EventState > 600 Then
				If mainPlayer\blinkTimer < -3 And mainPlayer\blinkTimer > -11 Then
					If e\img = 0 Then
						If mainPlayer\blinkTimer > -5 And Rand(30)=1 Then
							If Rand(5)<5 Then PlaySound_Strict DripSFX(0)
							If e\img = 0 Then e\img = LoadImage_Strict("GFX\npcs\106face.jpg")
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
		temp% = CreatePivot()
		PositionEntity temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
		PointEntity temp, mainPlayer\closestButton
		yawvalue# = WrapAngle(EntityYaw(mainPlayer\cam) - EntityYaw(temp))
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = WrapAngle(EntityPitch(mainPlayer\cam) - EntityPitch(temp))
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		FreeEntity (temp)
		
		DrawImage(HandIcon, userOptions\screenWidth / 2 + Sin(yawvalue) * (userOptions\screenWidth / 3) - 32, userOptions\screenHeight / 2 - Sin(pitchvalue) * (userOptions\screenHeight / 3) - 32)
		
		If MouseUp1 Then
			MouseUp1 = False
			If mainPlayer\closestDoor <> Null Then 
				If mainPlayer\closestDoor\Code <> "" Then
					mainPlayer\selectedDoor = mainPlayer\closestDoor
				ElseIf Not mainPlayer\disableControls Then
					PlaySound2(ButtonSFX, mainPlayer\cam, mainPlayer\closestButton)
					UseDoor(mainPlayer\closestDoor,True)				
				EndIf
			EndIf
		EndIf
	EndIf
	
	If mainPlayer\closestItem <> Null Then
		yawvalue# = -DeltaYaw(mainPlayer\cam, mainPlayer\closestItem\collider)
		If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
		If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
		pitchvalue# = -DeltaPitch(mainPlayer\cam, mainPlayer\closestItem\collider)
		If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
		If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
		
		DrawImage(HandIcon2, userOptions\screenWidth / 2 + Sin(yawvalue) * (userOptions\screenWidth / 3) - 32, userOptions\screenHeight / 2 - Sin(pitchvalue) * (userOptions\screenHeight / 3) - 32)
	EndIf
	
	If DrawHandIcon Then DrawImage(HandIcon, userOptions\screenWidth / 2 - 32, userOptions\screenHeight / 2 - 32)
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
			DrawImage(HandIcon, x, y)
			Color 0, 0, 0
			Rect(x + 4, y + 4, 64 - 8, 64 - 8)
			DrawImage(ArrowIMG(i), x + 21, y + 21)
			DrawArrowIcon(i) = False
		End If
	Next
	
	If CurrGameState=GAMESTATE_SCP294 Then Use294()
	
	If userOptions\hudEnabled Then 
		
		Local width% = 204, height% = 20
		x% = 80
		y% = userOptions\screenHeight - 95
		
		Color 255, 255, 255	
		Rect (x, y, width, height, False)
		For i = 1 To Int(((width - 2) * (mainPlayer\blinkTimer / (mainPlayer\blinkFreq))) / 10)
			DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
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
		
		DrawImage BlinkIcon, x - 50, y
		
		y = userOptions\screenHeight - 55
		Color 255, 255, 255
		Rect (x, y, width, height, False)
		For i = 1 To Int(((width - 2) * (mainPlayer\stamina / 100.0)) / 10)
			DrawImage(StaminaMeterIMG, x + 3 + 10 * (i - 1), y + 3)
		Next	
		
		Color 0, 0, 0
		Rect(x - 50, y, 30, 30)
		
		Color 255, 255, 255
		Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
		If mainPlayer\crouching Then
			DrawImage CrouchIcon, x - 50, y
		Else
			DrawImage SprintIcon, x - 50, y
		EndIf
		
		If DebugHUD Then
			Color 255, 255, 255
			SetFont ConsoleFont
			
			;Text x + 250, 50, "Zone: " + (EntityZ(mainPlayer\collider)/8.0)
			Text x - 50, 50, "Player Position: (" + f2s(EntityX(mainPlayer\collider), 3) + ", " + f2s(EntityY(mainPlayer\collider), 3) + ", " + f2s(EntityZ(mainPlayer\collider), 3) + ")"
			Text x - 50, 70, "Camera Position: (" + f2s(EntityX(mainPlayer\cam), 3)+ ", " + f2s(EntityY(mainPlayer\cam), 3) +", " + f2s(EntityZ(mainPlayer\cam), 3) + ")"
			Text x - 50, 100, "Player Rotation: (" + f2s(EntityPitch(mainPlayer\collider), 3) + ", " + f2s(EntityYaw(mainPlayer\collider), 3) + ", " + f2s(EntityRoll(mainPlayer\collider), 3) + ")"
			Text x - 50, 120, "Camera Rotation: (" + f2s(EntityPitch(mainPlayer\cam), 3)+ ", " + f2s(EntityYaw(mainPlayer\cam), 3) +", " + f2s(EntityRoll(mainPlayer\cam), 3) + ")"
			Text x - 50, 150, "Room: " + mainPlayer\currRoom\RoomTemplate\Name
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
			Text x - 50, 390, "SCP - 173 Position (collider): (" + f2s(EntityX(Curr173\Collider), 3) + ", " + f2s(EntityY(Curr173\Collider), 3) + ", " + f2s(EntityZ(Curr173\Collider), 3) + ")"
			Text x - 50, 410, "SCP - 173 Position (obj): (" + f2s(EntityX(Curr173\obj), 3) + ", " + f2s(EntityY(Curr173\obj), 3) + ", " + f2s(EntityZ(Curr173\obj), 3) + ")"
			;Text x - 50, 410, "SCP - 173 Idle: " + Curr173\Idle
			Text x - 50, 430, "SCP - 173 State: " + Curr173\State
			Text x - 50, 450, "SCP - 106 Position: (" + f2s(EntityX(Curr106\obj), 3) + ", " + f2s(EntityY(Curr106\obj), 3) + ", " + f2s(EntityZ(Curr106\obj), 3) + ")"
			Text x - 50, 470, "SCP - 106 Idle: " + Curr106\Idle
			Text x - 50, 490, "SCP - 106 State: " + Curr106\State
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
			
			SetFont Font1
		EndIf
		
	EndIf
	
	If SelectedScreen <> Null Then
		DrawImage SelectedScreen\img, userOptions\screenWidth/2-ImageWidth(SelectedScreen\img)/2,userOptions\screenHeight/2-ImageHeight(SelectedScreen\img)/2
		
		If MouseUp1 Or MouseHit2 Then
			FreeImage SelectedScreen\img : SelectedScreen\img = 0
			SelectedScreen = Null
			MouseUp1 = False
		EndIf
	EndIf
	
	;TODO: cleanup
	Local PrevInvOpen% = (CurrGameState=GAMESTATE_INVENTORY), MouseSlot% = 66
	
	Local shouldDrawHUD%=True
	If mainPlayer\selectedDoor <> Null Then
		mainPlayer\selectedItem = Null
		
		If shouldDrawHUD Then
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True),EntityZ(mainPlayer\closestButton,True)
			RotateEntity pvt, 0, EntityYaw(mainPlayer\closestButton,True)-180,0
			MoveEntity pvt, 0,0,0.22
			PositionEntity mainPlayer\cam, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
			PointEntity mainPlayer\cam, mainPlayer\closestButton
			FreeEntity pvt	
			
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)+MeshHeight(ButtonOBJ)*0.015,EntityZ(mainPlayer\closestButton,True))
			projY# = ProjectedY()
			CameraProject(mainPlayer\cam, EntityX(mainPlayer\closestButton,True),EntityY(mainPlayer\closestButton,True)-MeshHeight(ButtonOBJ)*0.015,EntityZ(mainPlayer\closestButton,True))
			scale# = (ProjectedY()-projy)/462.0
			
			x = userOptions\screenWidth/2-ImageWidth(KeypadHUD)*scale/2
			y = userOptions\screenHeight/2-ImageHeight(KeypadHUD)*scale/2		
			
			SetFont Font3
			If KeypadMSG <> "" Then 
				KeypadTimer = KeypadTimer-FPSfactor2
				
				If (KeypadTimer Mod 70) < 35 Then Text userOptions\screenWidth/2, y+124*scale, KeypadMSG, True,True
				If KeypadTimer =<0 Then
					KeypadMSG = ""
					SelectedDoor = Null
					MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
				EndIf
			Else
				Text userOptions\screenWidth/2, y+70*scale, "ACCESS CODE: ",True,True	
				SetFont Font4
				Text userOptions\screenWidth/2, y+124*scale, KeypadInput,True,True	
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
							PlaySound_Strict ButtonSFX
							
							Select (n+1)+(i*4)
								Case 1,2,3
									KeypadInput=KeypadInput + ((n+1)+(i*4))
								Case 4
									KeypadInput=KeypadInput + "0"
								Case 5,6,7
									KeypadInput=KeypadInput + ((n+1)+(i*4)-1)
								Case 8 ;enter
									If KeypadInput = mainPlayer\selectedDoor\Code Then
										PlaySound_Strict ScannerSFX1
										
										If mainPlayer\selectedDoor\Code = Str(AccessCode) Then
											GiveAchievement(AchvMaynard)
										ElseIf mainPlayer\selectedDoor\Code = "7816" Then ;TODO: do this better
											GiveAchievement(AchvHarp)
										EndIf									
										
										mainPlayer\selectedDoor\locked = 0
										UseDoor(mainPlayer\selectedDoor,True)
										mainPlayer\selectedDoor = Null
										MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
									Else
										PlaySound_Strict ScannerSFX2
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
			
			If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
			
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
			ResumeSounds()
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
			CurrGameState = GAMESTATE_INVENTORY
		Else
			PauseSounds()
			CurrGameState = GAMESTATE_PAUSED
		EndIf
		
		;AchievementsMenu = 0
		;OptionsMenu = 0
		;QuitMSG = 0
		
		mainPlayer\selectedDoor = Null
		SelectedScreen = Null
		SelectedMonitor = Null
	EndIf
	
	Local spacing%
	Local PrevOtherOpen.Items
	
	Local OtherSize%,OtherAmount%
	
	Local isEmpty%
	
	Local isMouseOn%
	
	Local closedInv%
	
	;TODO: handle other inventories here
	If CurrGameState=GAMESTATE_INVENTORY Then
		
		If (mainPlayer\currRoom\RoomTemplate\Name = "gatea") Then
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
			CameraFogRange mainPlayer\cam, 5,30
			CameraFogColor (mainPlayer\cam,200,200,200)
			CameraClsColor (mainPlayer\cam,200,200,200)					
			CameraRange(mainPlayer\cam, 0.05, 30)
		ElseIf (mainPlayer\currRoom\RoomTemplate\Name = "exit1") And (EntityY(mainPlayer\collider)>1040.0*RoomScale)
			HideEntity mainPlayer\overlays[OVERLAY_FOG]
			CameraFogRange mainPlayer\cam, 5,45
			CameraFogColor (mainPlayer\cam,200,200,200)
			CameraClsColor (mainPlayer\cam,200,200,200)					
			CameraRange(mainPlayer\cam, 0.05, 60)
		EndIf
		
		SelectedDoor = Null
		
		width% = 70
		height% = 70
		spacing% = 35
		
		x = userOptions\screenWidth / 2 - (width * mainPlayer\inventory\size /2 + spacing * (mainPlayer\inventory\size / 2 - 1)) / 2
		y = userOptions\screenHeight / 2 - height
		
		For  n% = 0 To mainPlayer\inventory\size - 1
			isMouseOn% = False
			If ScaledMouseX() > x And ScaledMouseX() < x + width Then
				If ScaledMouseY() > y And ScaledMouseY() < y + height Then
					isMouseOn = True
				End If
			EndIf
			
			If mainPlayer\inventory\items[n] <> Null Then
				Color 200, 200, 200
				If IsPlayerWearing(mainPlayer,mainPlayer\inventory\items[n]\itemtemplate\tempname) Then
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
			
			If mainPlayer\inventory\items[n] <> Null Then
				If (mainPlayer\selectedItem <> mainPlayer\inventory\items[n] Or isMouseOn) Then 
					DrawImage(mainPlayer\inventory\items[n]\invimg, x + width / 2 - 32, y + height / 2 - 32)
				EndIf
			EndIf
			
			If mainPlayer\inventory\items[n] <> Null And mainPlayer\selectedItem <> mainPlayer\inventory\items[n] Then
				;drawimage(mainPlayer\inventory\items[n].InvIMG, x + width / 2 - 32, y + height / 2 - 32)
				If isMouseOn Then
					If mainPlayer\selectedItem = Null Then
						If MouseHit1 Then
							mainPlayer\selectedItem = mainPlayer\inventory\items[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If mainPlayer\inventory\items[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\inventory\items[n]\itemtemplate\sound))
								CurrGameState = GAMESTATE_PLAYING
								DoubleClick = False
							EndIf
							
						EndIf
						
						SetFont Font1
						Color 0,0,0
						Text(x + width / 2 + 1, y + height + spacing - 15 + 1, mainPlayer\inventory\items[n]\name, True)							
						Color 255, 255, 255	
						Text(x + width / 2, y + height + spacing - 15, mainPlayer\inventory\items[n]\name, True)	
						
					EndIf
				EndIf
			Else
				If isMouseOn And MouseHit1 Then
					For z% = 0 To mainPlayer\inventory\size - 1
						If mainPlayer\inventory\items[z] = mainPlayer\selectedItem Then mainPlayer\inventory\items[z] = Null
					Next
					mainPlayer\inventory\items[n] = mainPlayer\selectedItem
				End If
				
			EndIf					
			
			x=x+width + spacing
			If n = 4 Then 
				y = y + height*2 
				x = userOptions\screenWidth / 2 - (width * mainPlayer\inventory\size /2 + spacing * (mainPlayer\inventory\size / 2 - 1)) / 2
			EndIf
		Next
		
		If mainPlayer\selectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(mainPlayer\selectedItem\invimg, ScaledMouseX() - ImageWidth(mainPlayer\selectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(mainPlayer\selectedItem\itemtemplate\invimg) / 2)
				ElseIf mainPlayer\selectedItem <> mainPlayer\inventory\items[MouseSlot]
					DrawImage(mainPlayer\selectedItem\invimg, ScaledMouseX() - ImageWidth(mainPlayer\selectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(mainPlayer\selectedItem\itemtemplate\invimg) / 2)
				EndIf
			Else
				If MouseSlot = 66 Then
					DropItem(mainPlayer\selectedItem)		
		
					mainPlayer\selectedItem = Null		
					CurrGameState = GAMESTATE_PLAYING	
							
					MoveMouse viewport_center_x, viewport_center_y
				Else
					If mainPlayer\inventory\items[MouseSlot] = Null Then
						For z% = 0 To mainPlayer\inventory\size - 1
							If mainPlayer\inventory\items[z] = mainPlayer\selectedItem Then mainPlayer\inventory\items[z] = Null
						Next
						mainPlayer\inventory\items[MouseSlot] = mainPlayer\selectedItem
						mainPlayer\selectedItem = Null
					ElseIf mainPlayer\inventory\items[MouseSlot] <> mainPlayer\selectedItem Then
						Select mainPlayer\selectedItem\itemtemplate\tempname
							Case "paper","key1","key2","key3","key4","key5","key6","misc","oldpaper","badge","ticket" ;BoH stuff
								If mainPlayer\inventory\items[MouseSlot]\itemtemplate\tempname = "clipboard" Then
									;Add an item to clipboard
									Local added.Items = Null
									If mainPlayer\selectedItem\itemtemplate\tempname<>"misc" Or (mainPlayer\selectedItem\itemtemplate\name="Playing Card" Or mainPlayer\selectedItem\itemtemplate\name="Mastercard") Then
										For c% = 0 To mainPlayer\inventory\items[MouseSlot]\inventory\size-1
											If (mainPlayer\inventory\items[MouseSlot]\inventory\items[c] = Null)
												If mainPlayer\selectedItem <> Null Then
													mainPlayer\inventory\items[MouseSlot]\inventory\items[c] = mainPlayer\selectedItem
													mainPlayer\inventory\items[MouseSlot]\state = 1.0
													SetAnimTime mainPlayer\inventory\items[MouseSlot]\model,0.0
													mainPlayer\inventory\items[MouseSlot]\invimg = mainPlayer\inventory\items[MouseSlot]\itemtemplate\invimg
													
													For ri% = 0 To mainPlayer\inventory\size - 1
														If mainPlayer\inventory\items[ri] = mainPlayer\selectedItem Then
															mainPlayer\inventory\items[ri] = Null
															PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))
														EndIf
													Next
													added = mainPlayer\selectedItem
													mainPlayer\selectedItem = Null : Exit
												EndIf
											EndIf
										Next
										If mainPlayer\selectedItem <> Null Then
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
								mainPlayer\selectedItem = Null
								
							Case "battery", "bat"
								Select mainPlayer\inventory\items[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										If mainPlayer\selectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))	
										RemoveItem (mainPlayer\selectedItem)
										mainPlayer\selectedItem = Null
										mainPlayer\inventory\items[MouseSlot]\state = 100.0
										Msg = "You replaced the navigator's battery."
										MsgTimer = 70 * 5
									Case "S-NAV Navigator Ultimate"
										Msg = "There seems to be no place for batteries in this navigator."
										MsgTimer = 70 * 5
									Case "Radio Transceiver"
										Select mainPlayer\inventory\items[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												Msg = "There seems to be no place for batteries in this radio."
												MsgTimer = 70 * 5
											Case "18vradio"
												Msg = "The battery does not fit inside this radio."
												MsgTimer = 70 * 5
											Case "radio"
												If mainPlayer\selectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))	
												RemoveItem (mainPlayer\selectedItem)
												mainPlayer\selectedItem = Null
												mainPlayer\inventory\items[MouseSlot]\state = 100.0
												Msg = "You replaced the radio's battery."
												MsgTimer = 70 * 5
										End Select
									Case "Night Vision Goggles"
										Local nvname$ = mainPlayer\inventory\items[MouseSlot]\itemtemplate\tempname
										If nvname$="nvgoggles" Or nvname$="supernv" Then
											If mainPlayer\selectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))	
											RemoveItem (mainPlayer\selectedItem)
											mainPlayer\selectedItem = Null
											mainPlayer\inventory\items[MouseSlot]\state = 1000.0
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
								Select mainPlayer\inventory\items[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										Msg = "The battery does not fit inside this navigator."
										MsgTimer = 70 * 5
									Case "S-NAV Navigator Ultimate"
										Msg = "There seems to be no place for batteries in this navigator."
										MsgTimer = 70 * 5
									Case "Radio Transceiver"
										Select mainPlayer\inventory\items[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												Msg = "There seems to be no place for batteries in this radio."
												MsgTimer = 70 * 5		
											Case "18vradio"
												If mainPlayer\selectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))	
												RemoveItem (mainPlayer\selectedItem)
												mainPlayer\selectedItem = Null
												mainPlayer\inventory\items[MouseSlot]\state = 100.0
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
				mainPlayer\selectedItem = Null
			End If
		End If
		
		If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
		
		If CurrGameState = GAMESTATE_PLAYING Then 
			ResumeSounds() 
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
		EndIf
	Else ;invopen = False
		
		If mainPlayer\selectedItem <> Null Then
			Select mainPlayer\selectedItem\itemtemplate\tempname
					
					;[Block]
				Case "nvgoggles","supernv","veryfinenvgoggles"
					Equip(mainPlayer,mainPlayer\selectedItem)

					mainPlayer\selectedItem = Null
				Case "1123"
					If mainPlayer\currRoom\RoomTemplate\Name <> "room1123" Then
						ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
						mainPlayer\lightFlash = 7
						PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Touch.ogg"))		
						DeathMSG = "Subject D-9341 was shot dead after attempting to attack a member of Nine-Tailed Fox. Surveillance tapes show that the subject had been "
						DeathMSG = DeathMSG + "wandering around the site approximately 9 minutes prior, shouting the phrase " + Chr(34) + "get rid of the four pests" + Chr(34)
						DeathMSG = DeathMSG + " in chinese. SCP-1123 was found in [REDACTED] nearby, suggesting the subject had come into physical contact with it. How "
						DeathMSG = DeathMSG + "exactly SCP-1123 was removed from its containment chamber is still unknown."
						Kill(mainPlayer)
						Return
					EndIf
					For e.Events = Each Events
						If e\EventName = "room1123" Then 
							If e\EventState = 0 Then
								ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
								mainPlayer\lightFlash = 3
								PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Touch.ogg"))											
							EndIf
							e\EventState = Max(1, e\EventState)
							Exit
						EndIf
					Next
				Case "battery"
					;InvOpen = True

				Case "key1", "key2", "key3", "key4", "key5", "key6", "keyomni", "scp860", "hand", "hand2"
					DrawImage(mainPlayer\selectedItem\itemtemplate\invimg, userOptions\screenWidth / 2 - ImageWidth(mainPlayer\selectedItem\itemtemplate\invimg) / 2, userOptions\screenHeight / 2 - ImageHeight(mainPlayer\selectedItem\itemtemplate\invimg) / 2)
				Case "scp513"
					PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell"+Rand(1,3)+".ogg")
					
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
					mainPlayer\selectedItem = Null
				Case "scp500"
					GiveAchievement(Achv500)
					
					If (mainPlayer\injuries > 0 Or mainPlayer\bloodloss > 0) And mainPlayer\infect008 > 0 Then
						Msg = "You swallowed the pill. Your wounds are healing rapidly and your nausea is fading."
					ElseIf mainPlayer\infect008 > 0 Then
						Msg = "You swallowed the pill. Your nausea is fading."
					Else
						Msg = "You swallowed the pill. Your wounds are healing rapidly."
					EndIf
					MsgTimer = 70*7
					
					mainPlayer\dead = False
					mainPlayer\fallTimer = 0
					mainPlayer\injuries = 0
					mainPlayer\bloodloss = 0
					mainPlayer\infect008 = 0
					mainPlayer\stamina = 100
					
					RemoveItem(mainPlayer\selectedItem)
					mainPlayer\selectedItem = Null
					
				Case "veryfinefirstaid"
					Select Rand(5)
						Case 1
							mainPlayer\injuries = 3.5
							Msg = "You started bleeding heavily."
							MsgTimer = 70*7
						Case 2
							mainPlayer\injuries = 0
							mainPlayer\bloodloss = 0
							Msg = "Your wounds are healing up rapidly."
							MsgTimer = 70*7
						Case 3
							mainPlayer\injuries = Max(0, mainPlayer\injuries - Rnd(0.5,3.5))
							mainPlayer\bloodloss = Max(0, mainPlayer\bloodloss - Rnd(10,100))
							Msg = "You feel much better."
							MsgTimer = 70*7
						Case 4
							mainPlayer\blurTimer = 10000
							mainPlayer\bloodloss = 0
							Msg = "You feel nauseated."
							MsgTimer = 70*7
						Case 5
							mainPlayer\blinkTimer = -10
							For r.Rooms = Each Rooms
								If r\RoomTemplate\Name = "pocketdimension" Then
									PositionEntity(mainPlayer\collider, EntityX(r\obj),0.8,EntityZ(r\obj))		
									ResetEntity mainPlayer\collider									
									UpdateDoors()
									UpdateRooms()
									PlaySound_Strict(Use914SFX)
									mainPlayer\dropSpeed = 0
									Curr106\State = -2500
									Exit
								EndIf
							Next
							Msg = "For some inexplicable reason. You find yourself inside the pocket dimension."
							MsgTimer = 70*8
					End Select
					
					RemoveItem(mainPlayer\selectedItem)
				Case "firstaid", "finefirstaid", "firstaid2"
					If mainPlayer\bloodloss = 0 And mainPlayer\injuries = 0 Then
						Msg = "You do not need to use a first aid right now."
						MsgTimer = 70*5
						mainPlayer\selectedItem = Null
					Else
						mainPlayer\moveSpeed = CurveValue(0, mainPlayer\moveSpeed, 5.0)
						mainPlayer\crouching = True
						
						DrawImage(mainPlayer\selectedItem\itemtemplate\invimg, userOptions\screenWidth / 2 - ImageWidth(mainPlayer\selectedItem\itemtemplate\invimg) / 2, userOptions\screenHeight / 2 - ImageHeight(mainPlayer\selectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = userOptions\screenWidth / 2 - width / 2
						y% = userOptions\screenHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						For  i% = 1 To Int((width - 2) * (mainPlayer\selectedItem\state / 100.0) / 10)
							DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
						Next
						
						mainPlayer\selectedItem\state = Min(mainPlayer\selectedItem\state+(FPSfactor/5.0),100)			
						
						If mainPlayer\selectedItem\state = 100 Then
							If mainPlayer\selectedItem\itemtemplate\tempname = "finefirstaid" Then
								mainPlayer\bloodloss = 0
								mainPlayer\injuries = Max(0, mainPlayer\injuries - 2.0)
								If mainPlayer\injuries = 0 Then
									Msg = "You bandaged the wounds and took a painkiller. You feel fine."
								ElseIf mainPlayer\injuries > 1.0
									Msg = "You bandaged the wounds and took a painkiller, but you were not able to stop the bleeding."
								Else
									Msg = "You bandaged the wounds and took a painkiller, but you still feel sore."
								EndIf
								MsgTimer = 70*5
								RemoveItem(mainPlayer\selectedItem)
							Else
								mainPlayer\bloodloss = Max(0, mainPlayer\bloodloss - Rand(10,20))
								If mainPlayer\injuries => 2.5 Then
									Msg = "The wounds were way too severe to staunch the bleeding completely."
									mainPlayer\injuries = Max(2.5, mainPlayer\injuries-Rnd(0.3,0.7))
								ElseIf mainPlayer\injuries > 1.0
									mainPlayer\injuries = Max(0.5, mainPlayer\injuries-Rnd(0.5,1.0))
									If mainPlayer\injuries > 1.0 Then
										Msg = "You bandaged the wounds but were unable to staunch the bleeding completely."
									Else
										Msg = "You managed to stop the bleeding."
									EndIf
								Else
									If mainPlayer\injuries > 0.5 Then
										mainPlayer\injuries = 0.5
										Msg = "You took a painkiller, easing the pain slightly."
									Else
										mainPlayer\injuries = 0.5
										Msg = "You took a painkiller, but it still hurts to walk."
									EndIf
								EndIf
								
								If mainPlayer\selectedItem\itemtemplate\tempname = "firstaid2" Then 
									Select Rand(6)
										Case 1
											mainPlayer\superMan = 1.0
											Msg = "You have becomed overwhelmedwithadrenalineholyshitWOOOOOO~!"
										Case 2
											userOptions\invertMouseY = (Not userOptions\invertMouseY)
											Msg = "You suddenly find it very difficult to turn your head."
										Case 3
											mainPlayer\blurTimer = 5000
											Msg = "You feel nauseated."
										Case 4
											mainPlayer\blinkEffect = 0.6
											;BlinkEffectTimer = 70*Rand(20,30)
										Case 5
											mainPlayer\bloodloss = 0
											mainPlayer\injuries = 0
											Msg = "You bandaged the wounds. The bleeding stopped completely and you feel fine."
										Case 6
											Msg = "You bandaged the wounds and blood started pouring heavily through the bandages."
											mainPlayer\injuries = 3.5
									End Select
								EndIf
								
								MsgTimer = 70*5
								RemoveItem(mainPlayer\selectedItem)
							EndIf							
						EndIf
						
					EndIf
				Case "eyedrops"
					mainPlayer\blinkEffect = 0.6
					;BlinkEffectTimer = 70*Rand(20,30)
					mainPlayer\blurTimer = 200
					RemoveItem(mainPlayer\selectedItem)
				Case "fineeyedrops"
					mainPlayer\blinkEffect = 0.4
					;BlinkEffectTimer = 70*Rand(30,40)
					mainPlayer\bloodloss = Max(mainPlayer\bloodloss-1.0, 0)
					mainPlayer\blurTimer = 200
					RemoveItem(mainPlayer\selectedItem)
				Case "supereyedrops"
					mainPlayer\blinkEffect = 0.0
					;TODO: fix
					;BlinkEffectTimer = 60
					;EyeStuck = 10000
					mainPlayer\blurTimer = 1000
					RemoveItem(mainPlayer\selectedItem)					
				Case "paper", "ticket"
					If mainPlayer\selectedItem\itemtemplate\img = 0 Then
						Select mainPlayer\selectedItem\itemtemplate\name
							Case "Burnt Note" 
								mainPlayer\selectedItem\itemtemplate\img = LoadImage_Strict("GFX\items\bn.it")
								SetBuffer ImageBuffer(mainPlayer\selectedItem\itemtemplate\img)
								Color 0,0,0
								Text 277, 469, AccessCode, True, True
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "Movie Ticket"
								;don't resize because it messes up the masking
								;TODO: this is retarded, fix
								mainPlayer\selectedItem\itemtemplate\img=LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
								
								If (mainPlayer\selectedItem\state = 0) Then
									Msg = Chr(34)+"Hey, I remember getting this ticket from the kickstarter! Wonder if it ever came out..."+Chr(34)
									MsgTimer = 70*10
									PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
									mainPlayer\selectedItem\state = 1
								EndIf
							Default 
								mainPlayer\selectedItem\itemtemplate\img=LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
								mainPlayer\selectedItem\itemtemplate\img = ResizeImage2(mainPlayer\selectedItem\itemtemplate\img, ImageWidth(mainPlayer\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(mainPlayer\selectedItem\itemtemplate\img) * MenuScale)
						End Select
						
						MaskImage(mainPlayer\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(mainPlayer\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(mainPlayer\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(mainPlayer\selectedItem\itemtemplate\img) / 2)
				Case "cup"
					
					mainPlayer\selectedItem\name = Trim(Lower(mainPlayer\selectedItem\name))
					If Left(mainPlayer\selectedItem\name, Min(6,Len(mainPlayer\selectedItem\name))) = "cup of" Then
						mainPlayer\selectedItem\name = Right(mainPlayer\selectedItem\name, Len(mainPlayer\selectedItem\name)-7)
					ElseIf Left(mainPlayer\selectedItem\name, Min(8,Len(mainPlayer\selectedItem\name))) = "a cup of" 
						mainPlayer\selectedItem\name = Right(mainPlayer\selectedItem\name, Len(mainPlayer\selectedItem\name)-9)
					EndIf
					
					;the state of refined items is more than 1.0 (fine setting increases it by 1, very fine doubles it)
					x2 = (mainPlayer\selectedItem\state+1.0)
					
					Local iniStr$ = "DATA\SCP-294.ini"
					
					Local loc% = GetINISectionLocation(iniStr, mainPlayer\selectedItem\name)
					
					;Stop
					
					strtemp = GetINIString2(iniStr, loc, "message")
					If strtemp <> "" Then Msg = strtemp : MsgTimer = 70*6
					
					If GetINIInt2(iniStr, loc, "lethal") Then 
						DeathMSG = GetINIString2(iniStr, loc, "deathmessage")
						Kill(mainPlayer)
					EndIf
					mainPlayer\blurTimer = GetINIInt2(iniStr, loc, "blur")*70;*temp
					mainPlayer\injuries = Max(mainPlayer\injuries + GetINIInt2(iniStr, loc, "damage"),0);*temp
					mainPlayer\bloodloss = Max(mainPlayer\bloodloss + GetINIInt2(iniStr, loc, "blood loss"),0);*temp
					strtemp =  GetINIString2(iniStr, loc, "sound")
					If strtemp<>"" Then
						PlaySound_Strict LoadTempSound(strtemp)
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
						For i = 0 To mainPlayer\inventory\size-1
							If mainPlayer\inventory\items[i]=mainPlayer\selectedItem Then mainPlayer\inventory\items[i] = it : Exit
						Next					
						EntityType (it\collider, HIT_ITEM)
						
						RemoveItem(mainPlayer\selectedItem)						
					EndIf
					
				Case "radio","18vradio","fineradio","veryfineradio"
					If mainPlayer\selectedItem\state <= 100 Then mainPlayer\selectedItem\state = Max(0, mainPlayer\selectedItem\state - FPSfactor * 0.004)
					
					If mainPlayer\selectedItem\itemtemplate\img=0 Then
						mainPlayer\selectedItem\itemtemplate\img=LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
						MaskImage(mainPlayer\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					;radiostate(5) = has the "use the number keys" -message been shown yet (true/false)
					;radiostate(6) = a timer for the "code channel"
					;RadioState(7) = another timer for the "code channel"
					
					If RadioState(5) = 0 Then 
						Msg = "Use the numbered keys 1 through 5 to cycle between various channels."
						MsgTimer = 70 * 5
						RadioState(5) = 1
						RadioState(0) = -1
					EndIf
					
					strtemp$ = ""
					
					x = userOptions\screenWidth - ImageWidth(mainPlayer\selectedItem\itemtemplate\img) ;+ 120
					y = userOptions\screenHeight - ImageHeight(mainPlayer\selectedItem\itemtemplate\img) ;- 30
					
					DrawImage(mainPlayer\selectedItem\itemtemplate\img, x, y)
					
					If mainPlayer\selectedItem\state > 0 Then
						;TODO: remove coffindistance
						If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Or CoffinDistance < 4.0 Then
							ResumeChannel(RadioCHN(5))
							If ChannelPlaying(RadioCHN(5)) = False Then RadioCHN(5) = PlaySound_Strict(RadioStatic)	
						Else
							Select Int(mainPlayer\selectedItem\state2)
								Case 0 ;randomkanava
									ResumeChannel(RadioCHN(0))
									If ChannelPlaying(RadioCHN(0)) = False Then
										RadioCHN(0) = PlaySound(RadioStatic)
									EndIf
								Case 1 ;hälytyskanava
									DebugLog RadioState(1) 
									
									ResumeChannel(RadioCHN(1))
									strtemp = "        WARNING - CONTAINMENT BREACH          "
									If ChannelPlaying(RadioCHN(1)) = False Then
										
										If RadioState(1) => 5 Then
											RadioCHN(1) = PlaySound_Strict(RadioSFX(1,1))	
											RadioState(1) = 0
										Else
											RadioState(1)=RadioState(1)+1	
											RadioCHN(1) = PlaySound_Strict(RadioSFX(1,0))	
										EndIf
										
									EndIf
									
								Case 2 ;scp-radio
									ResumeChannel(RadioCHN(2))
									strtemp = "        SCP Foundation On-Site Radio          "
									If ChannelPlaying(RadioCHN(2)) = False Then
										RadioState(2)=RadioState(2)+1
										If RadioState(2) = 17 Then RadioState(2) = 1
										If Floor(RadioState(2)/2)=Ceil(RadioState(2)/2) Then ;parillinen, soitetaan normiviesti
											RadioCHN(2) = PlaySound_Strict(RadioSFX(2,Int(RadioState(2)/2)))	
										Else ;pariton, soitetaan musiikkia
											RadioCHN(2) = PlaySound_Strict(RadioSFX(2,0))
										EndIf
									EndIf 
								Case 3
									ResumeChannel(RadioCHN(3))
									strtemp = "             EMERGENCY CHANNEL - RESERVED FOR COMMUNICATION IN THE EVENT OF A CONTAINMENT BREACH         "
									If ChannelPlaying(RadioCHN(3)) = False Then RadioCHN(3) = PlaySound_Strict(RadioStatic)
									
									If MTFtimer > 0 Then 
										RadioState(3)=RadioState(3)+Max(Rand(-10,1),0)
										Select RadioState(3)
											Case 40
												If Not RadioState3(0) Then
													RadioCHN(3) = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random1.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(0) = True	
												EndIf											
											Case 400
												If Not RadioState3(1) Then
													RadioCHN(3) = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random2.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(1) = True	
												EndIf	
											Case 800
												If Not RadioState3(2) Then
													RadioCHN(3) = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random3.ogg"))
													RadioState(3) = RadioState(3)+1	
													RadioState3(2) = True
												EndIf													
											Case 1200
												If Not RadioState3(3) Then
													RadioCHN(3) = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random4.ogg"))	
													RadioState(3) = RadioState(3)+1	
													RadioState3(3) = True
												EndIf		
										End Select
									EndIf
								Case 4
									ResumeChannel(RadioCHN(6)) ;taustalle kohinaa
									If ChannelPlaying(RadioCHN(6)) = False Then RadioCHN(6) = PlaySound_Strict(RadioStatic)									
									
									ResumeChannel(RadioCHN(4))
									If ChannelPlaying(RadioCHN(4)) = False Then 
										If RemoteDoorOn = False And RadioState(8) = False Then
											RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter3.ogg"))	
											RadioState(8) = True
										Else
											RadioState(4)=RadioState(4)+Max(Rand(-10,1),0)
											
											Select RadioState(4)
												Case 10
													If Not RadioState4(0) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\OhGod.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(0) = True
													EndIf													
												Case 100
													If Not RadioState4(1) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(1) = True
													EndIf		
												Case 158
													If MTFtimer = 0 And (Not RadioState4(2)) Then 
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\franklin1.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState(2) = True
													EndIf
												Case 200
													If Not RadioState4(3) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter4.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(3) = True
													EndIf		
												Case 260
													If Not RadioState4(4) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp1.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(4) = True
													EndIf		
												Case 300
													If Not RadioState4(5) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter1.ogg"))	
														RadioState(4) = RadioState(4)+1	
														RadioState4(5) = True
													EndIf		
												Case 350
													If Not RadioState4(6) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\franklin2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(6) = True
													EndIf		
												Case 400
													If Not RadioState4(7) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp2.ogg"))
														RadioState(4) = RadioState(4)+1
														RadioState4(7) = True
													EndIf		
												Case 450
													If Not RadioState4(8) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\franklin3.ogg"))	
														RadioState(4) = RadioState(4)+1		
														RadioState4(8) = True
													EndIf		
												Case 600
													If Not RadioState4(9) Then
														RadioCHN(4) = PlaySound_Strict(LoadTempSound("SFX\radio\franklin4.ogg"))	
														RadioState(4) = RadioState(4)+1	
														RadioState4(9) = True
													EndIf		
											End Select
										EndIf
									EndIf
									
									
								Case 5
									ResumeChannel(RadioCHN(5))
									If ChannelPlaying(RadioCHN(5)) = False Then RadioCHN(5) = PlaySound_Strict(RadioStatic)
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If mainPlayer\selectedItem\state <= 100 Then
								;Text (x - 60, y - 20, "BATTERY")
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(mainPlayer\selectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							SetFont Font3
							Text(x+60, y, "CHN")						
							
							If mainPlayer\selectedItem\itemtemplate\tempname = "veryfineradio" Then ;"KOODIKANAVA"
								ResumeChannel(RadioCHN(0))
								If ChannelPlaying(RadioCHN(0)) = False Then RadioCHN(0) = PlaySound_Strict(RadioStatic)
								
								;radiostate(7)=kuinka mones piippaus menossa
								;radiostate(8)=kuinka mones access coden numero menossa
								RadioState(6)=RadioState(6) + FPSfactor
								temp = Mid(Str(AccessCode),RadioState(8)+1,1)
								If RadioState(6)-FPSfactor =< RadioState(7)*50 And RadioState(6)>RadioState(7)*50 Then
									PlaySound_Strict(RadioBuzz)
									RadioState(7)=RadioState(7)+1
									If RadioState(7)=>temp Then
										RadioState(7)=0
										RadioState(6)=-100
										RadioState(8)=RadioState(8)+1
										If RadioState(8)=4 Then RadioState(8)=0 : RadioState(6)=-200
									EndIf
								EndIf
								
								strtemp = ""
								For i = 0 To Rand(5, 30)
									strtemp = strtemp + Chr(Rand(1,100))
								Next
								
								SetFont Font4
								Text(x+97, y+16, Rand(0,9),True,True)
								
							Else
								For i = 2 To 6
									If KeyHit(i) Then
										If mainPlayer\selectedItem\state2 <> i-2 Then ;pausetetaan nykyinen radiokanava
											PlaySound_Strict RadioSquelch
											If RadioCHN(Int(mainPlayer\selectedItem\state2)) <> 0 Then PauseChannel(RadioCHN(Int(mainPlayer\selectedItem\state2)))
										EndIf
										mainPlayer\selectedItem\state2 = i-2
										;jos nykyistä kanavaa ollaan soitettu, laitetaan jatketaan toistoa samasta kohdasta
										If RadioCHN(mainPlayer\selectedItem\state2)<>0 Then ResumeChannel(RadioCHN(mainPlayer\selectedItem\state2))
									EndIf
								Next
								
								SetFont Font4
								Text(x+97, y+16, Int(mainPlayer\selectedItem\state2+1),True,True)
							EndIf
							
							SetFont Font3
							If strtemp <> "" Then
								strtemp = Right(Left(strtemp, (Int(MilliSecs2()/300) Mod Len(strtemp))),10)
								Text(x+32, y+33, strtemp)
							EndIf
							
							SetFont Font1
							
						EndIf
						
					EndIf
					
				Case "cigarette"
					If mainPlayer\selectedItem\state = 0 Then
						Select Rand(6)
							Case 1
								Msg = Chr(34)+"I don't have anything to light it with. Umm, what about that... Nevermind."+Chr(34)
							Case 2
								Msg = "You are unable to get lit."
							Case 3
								Msg = Chr(34)+"I quit that a long time ago."+Chr(34)
								RemoveItem(mainPlayer\selectedItem)
							Case 4
								Msg = Chr(34)+"Even if I wanted one, I have nothing to light it with."+Chr(34)
							Case 5
								Msg = Chr(34)+"Could really go for one now... Wish I had a lighter."+Chr(34)
							Case 6
								Msg = Chr(34)+"Don't plan on starting, even at a time like this."+Chr(34)
								RemoveItem(mainPlayer\selectedItem)
						End Select
						mainPlayer\selectedItem\state = 1 
					Else
						Msg = "You are unable to get lit."
					EndIf

					MsgTimer = 70 * 5
				Case "420"
					Msg = Chr(34) + "MAN DATS SUM GOOD ASS SHIT" + Chr(34)
					mainPlayer\injuries = Max(mainPlayer\injuries-0.5, 0)
					mainPlayer\blurTimer = 500
					GiveAchievement(Achv420)
					PlaySound_Strict LoadTempSound("SFX\Music\420J.ogg")
					MsgTimer = 70 * 5
					RemoveItem(mainPlayer\selectedItem)
				Case "420s"
					DeathMSG = "Subject D-9341 found in a comatose state in [DATA REDACTED]. The subject was holding what appears to be a cigarette while smiling widely. "
					DeathMSG = DeathMSG+"Chemical analysis of the cigarette has been inconclusive, although it seems to contain a high concentration of an unidentified chemical "
					DeathMSG = DeathMSG+"whose molecular structure is remarkably similar to that of tetrahydrocannabinol."
					Msg = Chr(34) + "UH WHERE... WHAT WAS I DOING AGAIN... MAN I NEED TO TAKE A NAP..." + Chr(34)
					Kill(mainPlayer)
					MsgTimer = 70 * 6
					RemoveItem(mainPlayer\selectedItem)
				Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
					Msg = "You removed the hazmat suit."
					DeEquip(mainPlayer,WORNITEM_SLOT_BODY)
					MsgTimer = 70 * 5
					DropItem(mainPlayer\selectedItem)
					mainPlayer\selectedItem = Null	
				Case "vest"
					If IsPlayerWearing(mainPlayer,"vest") Then
						Msg = "You removed the vest."
						DeEquip(mainPlayer,WORNITEM_SLOT_BODY)
					Else
						Msg = "You put on the vest and feel slightly encumbered."
						Equip(mainPlayer,mainPlayer\selectedItem)
						;TakeOffStuff(2)
					EndIf
					MsgTimer = 70 * 7
					mainPlayer\selectedItem = Null
				Case "finevest"
					If IsPlayerWearing(mainPlayer,"finevest") Then
						Msg = "You removed the vest."
						DeEquip(mainPlayer,WORNITEM_SLOT_BODY)					
					Else
						Msg = "You put on the vest and feel heavily encumbered."
						Equip(mainPlayer,mainPlayer\selectedItem)
						;TakeOffStuff(2)
					EndIf
					mainPlayer\selectedItem = Null	
				Case "gasmask", "supergasmask", "gasmask3"
					Equip(mainPlayer,mainPlayer\selectedItem)

					mainPlayer\selectedItem = Null				
				Case "navigator", "nav"
					
					If mainPlayer\selectedItem\itemtemplate\img=0 Then
						mainPlayer\selectedItem\itemtemplate\img=LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
						MaskImage(mainPlayer\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					If mainPlayer\selectedItem\state <= 100 Then mainPlayer\selectedItem\state = Max(0, mainPlayer\selectedItem\state - FPSfactor * 0.005)
					
					x = userOptions\screenWidth - ImageWidth(mainPlayer\selectedItem\itemtemplate\img)*0.5+20
					y = userOptions\screenHeight - ImageHeight(mainPlayer\selectedItem\itemtemplate\img)*0.4-85
					width = 287
					height = 256
					
					DrawImage(mainPlayer\selectedItem\itemtemplate\img, x - ImageWidth(mainPlayer\selectedItem\itemtemplate\img) / 2, y - ImageHeight(mainPlayer\selectedItem\itemtemplate\img) / 2 + 85)
					
					SetFont Font3
					
					If mainPlayer\currRoom\RoomTemplate\Name = "pocketdimension" Then
						If (MilliSecs2() Mod 1000) > 300 Then	
							Text(x, y + height / 2 - 80, "ERROR 06", True)
							Text(x, y + height / 2 - 60, "LOCATION UNKNOWN", True)						
						EndIf
					Else
						
						If mainPlayer\selectedItem\state > 0 And (Rnd(CoffinDistance + 15.0) > 1.0 Or mainPlayer\currRoom\RoomTemplate\Name <> "coffin") Then
							
							If mainPlayer\selectedItem\itemtemplate\name = "S-NAV Navigator" Then 
								Color(100, 0, 0)
							Else
								Color (30,30,30)
							EndIf
							If (MilliSecs2() Mod 1000) > 300 Then
								If mainPlayer\selectedItem\itemtemplate\name <> "S-NAV 310 Navigator" And mainPlayer\selectedItem\itemtemplate\name <> "S-NAV Navigator Ultimate" Then
									Text(x, y + height / 2 - 40, "COULD NOT CONNECT", True)
									Text(x, y + height / 2 - 20, "TO MAP DATABASE", True)
								EndIf
								
								yawvalue = EntityYaw(mainPlayer\collider)-90
								x1 = x+Cos(yawvalue)*6 : y1 = y-Sin(yawvalue)*6
								x2 = x+Cos(yawvalue-140)*5 : y2 = y-Sin(yawvalue-140)*5				
								x3 = x+Cos(yawvalue+140)*5 : y3 = y-Sin(yawvalue+140)*5
								
								Line x1,y1,x2,y2
								Line x1,y1,x3,y3
								Line x2,y2,x3,y3
							EndIf
							
							Local PlayerX% = Floor(EntityX(mainPlayer\currRoom\obj) / 8.0 + 0.5), PlayerZ% = Floor(EntityZ(mainPlayer\currRoom\obj) / 8.0 + 0.5)
							Local SCPs_found% = 0
							If mainPlayer\selectedItem\itemtemplate\name = "S-NAV Navigator Ultimate" And (MilliSecs2() Mod 600) < 400 Then
								Local dist# = EntityDistance(mainPlayer\cam, Curr173\obj)
								dist = Ceil(dist / 8.0) * 8.0
								If dist < 8.0 * 4 Then
									Color 100, 0, 0
									Oval(x - dist * 3, y - 7 - dist * 3, dist * 3 * 2, dist * 3 * 2, False)
									Text(x - width / 2 + 20, y - height / 2 + 20, "SCP-173")
									SCPs_found% = SCPs_found% + 1
								EndIf
								dist# = EntityDistance(mainPlayer\cam, Curr106\obj)
								If dist < 8.0 * 4 Then
									Color 100, 0, 0
									Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
									Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-106")
									SCPs_found% = SCPs_found% + 1
								EndIf
								If Curr096<>Null Then 
									dist# = EntityDistance(mainPlayer\cam, Curr096\obj)
									If dist < 8.0 * 4 Then
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-096")
										SCPs_found% = SCPs_found% + 1
									EndIf
								EndIf
								For np.NPCs = Each NPCs
									If np\NPCtype = NPCtype049
										dist# = EntityDistance(mainPlayer\cam, np\obj)
										If dist < 8.0 * 4 Then
											Color 100, 0, 0
											Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
											Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-049")
											SCPs_found% = SCPs_found% + 1
										EndIf
									EndIf
								Next
								
								If mainPlayer\currRoom\RoomTemplate\Name = "coffin" Then
									If CoffinDistance < 8.0 Then
										dist = Rnd(4.0, 8.0)
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 20, y - height / 2 + 20 + (20*SCPs_found), "SCP-895")
									EndIf
								EndIf
							End If
							
							Color (30,30,30)
							If mainPlayer\selectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							If mainPlayer\selectedItem\state <= 100 Then
								Text (x - width/2 + 10, y - height/2 + 10, "BATTERY")
								xtemp = x - width/2 + 10
								ytemp = y - height/2 + 30		
								Line xtemp, ytemp, xtemp+20, ytemp
								Line xtemp, ytemp+100, xtemp+20, ytemp+100
								Line xtemp, ytemp, xtemp, ytemp+100
								Line xtemp+20, ytemp, xtemp+20, ytemp+100
								
								SetFont Font4
								For i = 1 To Ceil(mainPlayer\selectedItem\state / 10.0)
									Text (xtemp+11, ytemp+i*10-26, "-", True)
									;Rect(x - width/2, y+i*15, 40 - i * 6, 5, Ceil(mainPlayer\selectedItem\state / 20.0) > 4 - i)
								Next
								SetFont Font3
							EndIf
							
							x = x - 19 + ((EntityX(mainPlayer\collider) - 4.0) Mod 8.0)*3
							y = y + 14 - ((EntityZ(mainPlayer\collider)-4.0) Mod 8.0)*3
							For x2 = Max(1, PlayerX - 4) To Min(MapWidth - 1, PlayerX + 4)
								For z2 = Max(1, PlayerZ - 4) To Min(MapHeight - 1, PlayerZ + 4)
									
									If CoffinDistance > 16.0 Or Rnd(16.0)<CoffinDistance Then 
										If MapTemp(x2, z2) And (MapFound(x2, z2) > 0 Or mainPlayer\selectedItem\itemtemplate\name = "S-NAV 310 Navigator" Or mainPlayer\selectedItem\itemtemplate\name = "S-NAV Navigator Ultimate") Then
											Local drawx% = x + (PlayerX - x2) * 24 , drawy% = y - (PlayerZ - z2) * 24 
											
											Color (30,30,30)
											If mainPlayer\selectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
											
											If MapTemp(x2 + 1, z2) = False Then Line(drawx - 12, drawy - 12, drawx - 12, drawy + 12)
											If MapTemp(x2 - 1, z2) = False Then Line(drawx + 12, drawy - 12, drawx + 12, drawy + 12)
											
											If MapTemp(x2, z2 - 1) = False Then Line(drawx - 12, drawy - 12, drawx + 12, drawy - 12)
											If MapTemp(x2, z2 + 1)= False Then Line(drawx - 12, drawy + 12, drawx + 12, drawy + 12)
											
										End If
									EndIf
									
								Next
							Next
							
						EndIf
						
					EndIf
				;new Items in SCP:CB 1.3
				Case "scp1499","super1499"
					If (Not Wearing1499%) Then
						GiveAchievement(Achv1499)
						
						Equip(mainPlayer,mainPlayer\selectedItem)
						
						For r.Rooms = Each Rooms
							If r\RoomTemplate\Name = "dimension1499" Then
								mainPlayer\blinkTimer = -1
								NTF_1499PrevRoom = mainPlayer\currRoom
								NTF_1499PrevX# = EntityX(mainPlayer\collider)
								NTF_1499PrevY# = EntityY(mainPlayer\collider)
								NTF_1499PrevZ# = EntityZ(mainPlayer\collider)
								
								If NTF_1499X# = 0.0 And NTF_1499Y# = 0.0 And NTF_1499Z# = 0.0
									PositionEntity (mainPlayer\collider, r\x+676.0*RoomScale, r\y+314.0*RoomScale, r\z-2080.0*RoomScale)
								Else
									PositionEntity (mainPlayer\collider, NTF_1499X#, NTF_1499Y#+0.05, NTF_1499Z#)
								EndIf
								ResetEntity(mainPlayer\collider)
								UpdateDoors()
								UpdateRooms()
								For it.Items = Each Items
									it\disttimer = 0
								Next
								mainPlayer\currRoom = r
								PlaySound_Strict NTF_1499EnterSFX%
								NTF_1499X# = 0.0
								NTF_1499Y# = 0.0
								NTF_1499Z# = 0.0
								Exit
							EndIf
						Next
					EndIf
					If mainPlayer\selectedItem\itemtemplate\tempname="super1499"
						If Wearing1499%=0 Then Wearing1499% = 2 Else Wearing1499%=0
					Else
						Wearing1499% = (Not Wearing1499%)
					EndIf
					mainPlayer\selectedItem = Null
				Case "badge"
					If mainPlayer\selectedItem\itemtemplate\img=0 Then
						mainPlayer\selectedItem\itemtemplate\img=LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
						;mainPlayer\selectedItem\itemtemplate\img = ResizeImage2(mainPlayer\selectedItem\itemtemplate\img, ImageWidth(mainPlayer\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(mainPlayer\selectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(mainPlayer\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(mainPlayer\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(mainPlayer\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(mainPlayer\selectedItem\itemtemplate\img) / 2)
					
					If mainPlayer\selectedItem\state = 0 Then
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
						Select mainPlayer\selectedItem\itemtemplate\name
							Case "Old Badge"
								Msg = Chr(34)+"Huh? This guy looks just like me!"+Chr(34)
								MsgTimer = 70*10
						End Select
						
						mainPlayer\selectedItem\state = 1
					EndIf
				Case "key"
					If mainPlayer\selectedItem\state = 0 Then
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
						
						Msg = Chr(34)+"Isn't this the key to that old shack? The one where I... No, it can't be."+Chr(34)
						MsgTimer = 70*10						
					EndIf
					
					mainPlayer\selectedItem\state = 1
					mainPlayer\selectedItem = Null
				Case "oldpaper"
					If mainPlayer\selectedItem\itemtemplate\img = 0 Then
						mainPlayer\selectedItem\itemtemplate\img = LoadImage_Strict(mainPlayer\selectedItem\itemtemplate\imgpath)	
						mainPlayer\selectedItem\itemtemplate\img = ResizeImage2(mainPlayer\selectedItem\itemtemplate\img, ImageWidth(mainPlayer\selectedItem\itemtemplate\img) * MenuScale, ImageHeight(mainPlayer\selectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(mainPlayer\selectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(mainPlayer\selectedItem\itemtemplate\img, userOptions\screenWidth / 2 - ImageWidth(mainPlayer\selectedItem\itemtemplate\img) / 2, userOptions\screenHeight / 2 - ImageHeight(mainPlayer\selectedItem\itemtemplate\img) / 2)
					
					If mainPlayer\selectedItem\state = 0
						Select mainPlayer\selectedItem\itemtemplate\name
							Case "Disciplinary Hearing DH-S-4137-17092"
								mainPlayer\blurTimer = 1000
								
								Msg = Chr(34)+"Why does this seem so familiar?"+Chr(34)
								MsgTimer = 70*10
								PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
								mainPlayer\selectedItem\state = 1
						End Select
					EndIf
				Case "coin"
					If mainPlayer\selectedItem\state = 0
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(1,10)+".ogg")
					EndIf
					
					Msg = ""
					
					mainPlayer\selectedItem\state = 1
					mainPlayer\selectedItem = Null
				Default
					;check if the item is an inventory-type object
					If mainPlayer\selectedItem\inventory<>Null Then
						DoubleClick = 0
						MouseHit1 = 0
						MouseDown1 = 0
						LastMouseHit1 = 0
						;TODO: fix
						;OtherOpen = mainPlayer\selectedItem
						mainPlayer\selectedItem = Null
					EndIf
					
			End Select
			
			If mainPlayer\selectedItem <> Null Then
				If mainPlayer\selectedItem\itemtemplate\img <> 0
					Local IN$ = mainPlayer\selectedItem\itemtemplate\tempname
					If IN$ = "paper" Or IN$ = "badge" Or IN$ = "oldpaper" Or IN$ = "ticket" Then
						For a_it.Items = Each Items
							If a_it <> mainPlayer\selectedItem
								Local IN2$ = a_it\itemtemplate\tempname
								If IN2$ = "paper" Or IN2$ = "badge" Or IN2$ = "oldpaper" Or IN2$ = "ticket" Then
									If a_it\itemtemplate\img<>0
										If a_it\itemtemplate\img <> mainPlayer\selectedItem\itemtemplate\img
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
				EntityAlpha mainPlayer\overlays[OVERLAY_BLACK], 0.0
				
				If mainPlayer\selectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(mainPlayer\selectedItem\itemtemplate\sound))
				mainPlayer\selectedItem = Null
			EndIf
		End If		
	EndIf
	
	If mainPlayer\selectedItem = Null Then
		For i = 0 To 6
			If RadioCHN(i) <> 0 Then 
				If ChannelPlaying(RadioCHN(i)) Then PauseChannel(RadioCHN(i))
			EndIf
		Next
	EndIf 
	
	If PrevInvOpen And (CurrGameState<>GAMESTATE_INVENTORY) Then MoveMouse viewport_center_x, viewport_center_y
End Function

Function DrawPauseMenu()
	Local titleText$ = "PAUSED"
	Local x%, y%, width%, height%
	
	If CurrGameState = GAMESTATE_PAUSED Then
		
		width = ImageWidth(PauseMenuIMG)
		height = ImageHeight(PauseMenuIMG)
		x = userOptions\screenWidth / 2 - width / 2
		y = userOptions\screenHeight / 2 - height / 2
		
		DrawImage PauseMenuIMG, x, y
		
		Color(255, 255, 255)
		
		x = x+132*MenuScale
		y = y+122*MenuScale	
		
		;TODO: fix
;		If AchievementsMenu > 0 Then
;			SetFont Font2
;			Text(x, y-(122-45)*MenuScale, "ACHIEVEMENTS",False,True)
;			SetFont Font1
;		ElseIf OptionsMenu > 0 Then
;			SetFont Font2
;			Text(x, y-(122-45)*MenuScale, "OPTIONS",False,True)
;			SetFont Font1
;		ElseIf QuitMSG > 0 Then
;			SetFont Font2
;			Text(x, y-(122-45)*MenuScale, "QUIT?",False,True)
;			SetFont Font1
;		Else
		If mainPlayer\dead Then
			titleText = "YOU DIED"
		End If
		SetFont Font2
		Text(x, y-(122-45)*MenuScale, titleText,False,True)
		SetFont Font1
		
		Local AchvXIMG% = (x + (22*MenuScale))
		Local scale# = userOptions\screenHeight/768.0
		Local SeparationConst% = 76*scale
		Local imgsize% = 64
		
		;TODO: fix everything
		;If AchievementsMenu <= 0 And OptionsMenu <= 0 And QuitMSG <= 0
		SetFont Font1
		Text x, y, "Difficulty: "+SelectedDifficulty\name
		Text x, y+20*MenuScale, "Save: "+CurrSave
		Text x, y+40*MenuScale, "Map seed: "+RandomSeed
;		ElseIf AchievementsMenu <= 0 And OptionsMenu > 0 And QuitMSG <= 0 And KillTimer >= 0
;			If DrawButton(x + 101 * MenuScale, y + 390 * MenuScale, 230 * MenuScale, 60 * MenuScale, "Back") Then
;				AchievementsMenu = 0
;				OptionsMenu = 0
;				QuitMSG = 0
;				MouseHit1 = False
;				
;				SaveOptionsINI()
;				;TextureLodBias TextureFloat#
;			EndIf
;			
;			Color 0,255,0
;			If OptionsMenu = 1
;				Rect(x-10*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 2
;				Rect(x+100*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 3
;				Rect(x+210*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 4
;				Rect(x+320*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			EndIf
;			
;			If DrawButton(x-5*MenuScale,y,100*MenuScale,30*MenuScale,"GRAPHICS",False) Then OptionsMenu = 1
;			If DrawButton(x+105*MenuScale,y,100*MenuScale,30*MenuScale,"AUDIO",False) Then OptionsMenu = 2
;			If DrawButton(x+215*MenuScale,y,100*MenuScale,30*MenuScale,"CONTROLS",False) Then OptionsMenu = 3
;			If DrawButton(x+325*MenuScale,y,100*MenuScale,30*MenuScale,"ADVANCED",False) Then OptionsMenu = 4
;			
;			
;			Color 255,255,255
;			Select OptionsMenu
;				Case 1 ;Graphics
;					SetFont Font1
;					;[Block]
;					y=y+50*MenuScale
;					
;					Color 255,255,255				
;					Text(x, y, "Show HUD:")	
;					userOptions\hudEnabled = DrawTick(x + 270 * MenuScale, y + MenuScale, userOptions\hudEnabled)	
;					
;					y=y+30*MenuScale
;					
;					Color 100,100,100				
;					Text(x, y, "Enable bump mapping:")	
;					DrawTick(x + 270 * MenuScale, y + MenuScale, False, True)
;					If MouseOn(x + 270 * MenuScale, y + MenuScale, 20*MenuScale,20*MenuScale)
;						DrawTooltip("Not available in this version")
;					EndIf
;					
;					y=y+30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "VSync:")
;					userOptions\vsync = DrawTick(x + 270 * MenuScale, y + MenuScale, userOptions\vsync)
;					
;					y=y+30*MenuScale
;					
;					;Local prevGamma# = userOptions\screenGamma
;					userOptions\screenGamma = (SlideBar(x + 270*MenuScale, y+6*MenuScale, 100*MenuScale, userOptions\screenGamma*50.0)/50.0)
;					Color 255,255,255
;					Text(x, y, "Screen gamma")
;					;Text(x+5+MenuScale, y + 15 * MenuScale, "(userOptions\fullscreen only)")
;					
;					;If prevGamma<>userOptions\screenGamma Then
;					;	UpdateScreenGamma()
;					;EndIf
;					
;					y=y+40*MenuScale
;					
;					Color 100,100,100
;					Text(x, y, "Texture quality:")
;					DrawImage ArrowIMG(1),x + 270 * MenuScale, y-4*MenuScale
;					
;					Text(x + 300 * MenuScale, y + MenuScale, "DISABLED")
;					If MouseOn(x + 270 * MenuScale, y-4*MenuScale, ImageWidth(ArrowIMG(1)),ImageHeight(ArrowIMG(1)))
;						DrawTooltip("Not available in this version")
;					EndIf
;					
;					;[End Block]
;				Case 2 ;Audio
;					SetFont Font1
;					;[Block]
;					y = y + 50*MenuScale
;					
;					userOptions\musicVolume = (SlideBar(x + 250*MenuScale, y-4*MenuScale, 100*MenuScale, userOptions\musicVolume*100.0)/100.0)
;					Color 255,255,255
;					Text(x, y, "Music volume:")
;					
;					y = y + 30*MenuScale
;					
;					userOptions\SoundVolume = (SlideBar(x + 250*MenuScale, y-4*MenuScale, 100*MenuScale, userOptions\SoundVolume*100.0)/100.0)
;					Color 255,255,255
;					Text(x, y, "Sound volume:")
;					;[End Block]
;				Case 3 ;Controls
;					;Text(x+210*MenuScale,y,"CONTROLS",True,True)
;					SetFont Font1
;					;[Block]
;					y = y + 50*MenuScale
;					
;					userOptions\mouseSensitivity = (SlideBar(x + 270*MenuScale, y-4*MenuScale, 100*MenuScale, (userOptions\mouseSensitivity+0.5)*100.0)/100.0)-0.5
;					Color(255, 255, 255)
;					Text(x, y, "Mouse sensitivity:")
;					
;					y = y + 30*MenuScale
;					
;					Color(255, 255, 255)
;					Text(x, y, "Invert mouse Y-axis:")
;					userOptions\invertMouseY = DrawTick(x + 270 * MenuScale, y + MenuScale, userOptions\invertMouseY)
;					
;					y = y + 30*MenuScale
;					Text(x, y, "Control configuration:")
;					y = y + 10*MenuScale
;					
;					Text(x, y + 20 * MenuScale, "Move Forward")
;					InputBox(x + 200 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\up,210)),5)		
;					Text(x, y + 40 * MenuScale, "Strafe Left")
;					InputBox(x + 200 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\Left,210)),3)	
;					Text(x, y + 60 * MenuScale, "Move Backward")
;					InputBox(x + 200 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\down,210)),6)				
;					Text(x, y + 80 * MenuScale, "Strafe Right")
;					InputBox(x + 200 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\Right,210)),4)
;					
;					Text(x, y + 100 * MenuScale, "Manual Blink")
;					InputBox(x + 200 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\blink,210)),7)				
;					Text(x, y + 120 * MenuScale, "Sprint")
;					InputBox(x + 200 * MenuScale, y + 120 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\sprint,210)),8)
;					Text(x, y + 140 * MenuScale, "Open/Close Inventory")
;					InputBox(x + 200 * MenuScale, y + 140 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\inv,210)),9)
;					Text(x, y + 160 * MenuScale, "Crouch")
;					InputBox(x + 200 * MenuScale, y + 160 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\crouch,210)),10)
;					Text(x, y + 180 * MenuScale, "Quick Save")
;					InputBox(x + 200 * MenuScale, y + 180 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\save,210)),11)	
;					Text(x, y + 200 * MenuScale, "Open/Close Console")
;					InputBox(x + 200 * MenuScale, y + 200 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\console,210)),12)
;					
;					For i = 0 To 227
;						If KeyHit(i) Then key = i : Exit
;					Next
;					If key <> 0 Then
;						Select SelectedInputBox
;							Case 3
;								keyBinds\Left = key
;							Case 4
;								keyBinds\Right = key
;							Case 5
;								keyBinds\up = key
;							Case 6
;								keyBinds\down = key
;							Case 7
;								keyBinds\blink = key
;							Case 8
;								keyBinds\sprint = key
;							Case 9
;								keyBinds\inv = key
;							Case 10
;								keyBinds\crouch = key
;							Case 11
;								keyBinds\save = key
;							Case 12
;								keyBinds\console = key
;						End Select
;						SelectedInputBox = 0
;					EndIf
;					;[End Block]
;				Case 4 ;Advanced
;					;Text(x+210*MenuScale,y,"ADVANCED",True,True)
;					SetFont Font1
;					;[Block]
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Enable console:")
;					userOptions\allowConsole = DrawTick(x +270 * MenuScale, y + MenuScale, userOptions\allowConsole)
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Open console on error:")
;					userOptions\consoleOpenOnError = DrawTick(x + 270 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Achievement popups:")
;					userOptions\achvPopup = DrawTick(x + 270 * MenuScale, y, userOptions\achvPopup)
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Show FPS:")
;					userOptions\showFPS = DrawTick(x + 270 * MenuScale, y, userOptions\showFPS)
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Framelimit:")
;					
;					Color 255,255,255
;					If DrawTick(x + 270 * MenuScale, y, CurrFrameLimit > 0.0) Then
;						CurrFrameLimit# = (SlideBar(x + 150*MenuScale, y+30*MenuScale, 100*MenuScale, CurrFrameLimit#*50.0)/50.0)
;						CurrFrameLimit = Max(CurrFrameLimit, 0.1)
;						userOptions\framelimit% = CurrFrameLimit#*100.0
;						Color 255,255,0
;						Text(x + 5 * MenuScale, y + 25 * MenuScale, userOptions\framelimit%+" FPS")
;					Else
;						CurrFrameLimit# = 0.0
;						userOptions\framelimit = 0
;					EndIf
;					
;					y = y + 80*MenuScale
;					
;					;[End Block]
;			End Select
;		ElseIf AchievementsMenu <= 0 And OptionsMenu <= 0 And QuitMSG > 0 And KillTimer >= 0
;			Local QuitButton% = 60 
;			If SelectedDifficulty\saveType = SAVEONQUIT Or SelectedDifficulty\saveType = SAVEANYWHERE Then
;				Local RN$ = mainPlayer\currRoom\RoomTemplate\Name$
;				Local AbleToSave% = True
;				If RN$ = "173" Or RN$ = "exit1" Or RN$ = "gatea" Then AbleToSave = False
;				If (Not CanSave) Then AbleToSave = False
;				If AbleToSave
;					QuitButton = 140
;					If DrawButton(x, y + 60*MenuScale, 390*MenuScale, 60*MenuScale, "Save & Quit") Then
;						mainPlayer\dropSpeed = 0
;						SaveGame(SavePath + CurrSave + "\")
;						NullGame()
;						MenuOpen = False
;						MainMenuOpen = True
;						MainMenuTab = 0
;						CurrSave = ""
;						FlushKeys()
;					EndIf
;				EndIf
;			EndIf
;			
;			If DrawButton(x, y + QuitButton*MenuScale, 390*MenuScale, 60*MenuScale, "Quit") Then
;				NullGame()
;				MenuOpen = False
;				MainMenuOpen = True
;				MainMenuTab = 0
;				CurrSave = ""
;				FlushKeys()
;			EndIf
;			
;			If DrawButton(x+101*MenuScale, y + 344*MenuScale, 230*MenuScale, 60*MenuScale, "Back") Then
;				AchievementsMenu = 0
;				OptionsMenu = 0
;				QuitMSG = 0
;				MouseHit1 = False
;			EndIf
;		Else
;			If DrawButton(x+101*MenuScale, y + 344*MenuScale, 230*MenuScale, 60*MenuScale, "Back") Then
;				AchievementsMenu = 0
;				OptionsMenu = 0
;				QuitMSG = 0
;				MouseHit1 = False
;			EndIf
;			
;			If AchievementsMenu>0 Then
;				;DebugLog AchievementsMenu
;				If AchievementsMenu <= Floor(Float(MAXACHIEVEMENTS-1)/12.0) Then 
;					If DrawButton(x+341*MenuScale, y + 344*MenuScale, 50*MenuScale, 60*MenuScale, ">") Then
;						AchievementsMenu = AchievementsMenu+1
;					EndIf
;				EndIf
;				If AchievementsMenu > 1 Then
;					If DrawButton(x+41*MenuScale, y + 344*MenuScale, 50*MenuScale, 60*MenuScale, "<") Then
;						AchievementsMenu = AchievementsMenu-1
;					EndIf
;				EndIf
;				
;				For i=0 To 11
;					If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
;						DrawAchvIMG(AchvXIMG,y+((i/4)*120*MenuScale),i+((AchievementsMenu-1)*12))
;					Else
;						Exit
;					EndIf
;				Next
;				
;				For i=0 To 11
;					If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
;						If MouseOn(AchvXIMG+((i Mod 4)*SeparationConst),y+((i/4)*120*MenuScale),64*scale,64*scale) Then
;							AchievementTooltip(i+((AchievementsMenu-1)*12))
;							Exit
;						EndIf
;					Else
;						Exit
;					EndIf
;				Next
;				
;			EndIf
;		EndIf
		
		y = y+10
		
		;TODO: FFS
		;If AchievementsMenu<=0 And OptionsMenu<=0 And QuitMSG<=0 Then
		If Not mainPlayer\dead Then
			y = y+72*MenuScale
			
			If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Resume", True, True) Then
				CurrGameState = GAMESTATE_PLAYING
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
			EndIf
			
			y = y + 75*MenuScale
			If (Not SelectedDifficulty\permaDeath) Then
				If GameSaved Then
					If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game") Then
						DrawLoading(0)
						
						CurrGameState = GAMESTATE_PLAYING
						LoadGame(SavePath + CurrSave + "\",True)
						
						MoveMouse viewport_center_x,viewport_center_y
						SetFont Font1
						HidePointer ()
						
						FlushKeys()
						FlushMouse()
						mainPlayer\disableControls = False
						
						UpdateRooms()
						
						For r.Rooms = Each Rooms
							x = Abs(EntityX(mainPlayer\collider) - EntityX(r\obj))
							z = Abs(EntityZ(mainPlayer\collider) - EntityZ(r\obj))
							
							If x < 12.0 And z < 12.0 Then
								MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
								If x < 4.0 And z < 4.0 Then
									If Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5 Then mainPlayer\currRoom = r
									MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
								EndIf
							End If
						Next
						
						DrawLoading(100)
						
						mainPlayer\dropSpeed=0
						
						UpdateWorld 0.0
						
						PrevTime = MilliSecs()
						FPSfactor = 0
					EndIf
				Else
					DrawFrame(x,y,390*MenuScale, 60*MenuScale)
					Color (100, 100, 100)
					SetFont Font2
					Text(x + (390*MenuScale) / 2, y + (60*MenuScale) / 2, "Load Game", True, True)
				EndIf
				y = y + 75*MenuScale
		EndIf
			
			If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Achievements") Then AchievementsMenu = 1
			y = y + 75*MenuScale
			If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Options") Then OptionsMenu = 1
			y = y + 75*MenuScale
		Else
			y = y+104*MenuScale
			If GameSaved And (Not SelectedDifficulty\permaDeath) Then
				If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Load Game") Then
					DrawLoading(0)
					
					CurrGameState = GAMESTATE_PLAYING
					LoadGame(SavePath + CurrSave + "\",True)
					
					MoveMouse viewport_center_x,viewport_center_y
					SetFont Font1
					HidePointer ()
					
					FlushKeys()
					FlushMouse()
					mainPlayer\disableControls = False
					
					UpdateRooms()
					
					For r.Rooms = Each Rooms
						x = Abs(EntityX(mainPlayer\collider) - EntityX(r\obj))
						z = Abs(EntityZ(mainPlayer\collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = Max(MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)), 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5 Then mainPlayer\currRoom = r
								MapFound(Floor(EntityX(r\obj) / 8.0), Floor(EntityZ(r\obj) / 8.0)) = 1
							EndIf
						End If
					Next
					
					DrawLoading(100)
					
					mainPlayer\dropSpeed=0
					
					UpdateWorld 0.0
					
					PrevTime = MilliSecs()
					FPSfactor = 0
				EndIf
			Else
				DrawButton(x, y, 390*MenuScale, 60*MenuScale, "")
				Color 50,50,50
				Text(x + 185*MenuScale, y + 30*MenuScale, "Load Game", True, True)
			EndIf
			If DrawButton(x, y + 80*MenuScale, 390*MenuScale, 60*MenuScale, "Quit to Menu") Then
				NullGame()
				CurrGameState = GAMESTATE_MAINMENU
				MainMenuTab = 0
				CurrSave = ""
				FlushKeys()
			EndIf
			y = y + 80*MenuScale
		EndIf
		
		If Not mainPlayer\dead And CurrGameState<>GAMESTATE_MAINMENU Then
			If DrawButton(x, y, 390*MenuScale, 60*MenuScale, "Quit") Then
				RuntimeError "REIMPLEMENT"
				;QuitMSG = 1
			EndIf
		EndIf
		
		SetFont Font1
		If mainPlayer\dead Then RowText(DeathMSG$, x, y + 80*MenuScale, 390*MenuScale, 600*MenuScale)
		;EndIf
		
		If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
		
	End If
	
	SetFont Font1
	
End Function

Function MouseOn%(x%, y%, width%, height%)
	If ScaledMouseX() > x And ScaledMouseX() < x + width Then
		If ScaledMouseY() > y And ScaledMouseY() < y + height Then
			Return True
		End If
	End If
	Return False
End Function

;----------------------------------------------------------------------------------------------

Include "Assets.bb"

Include "Save.bb"

Include "Audio.bb"

;--------------------------------------- random -------------------------------------------------------

Function f2s$(n#, count%)
	Return Left(n, Len(Int(n))+count+1)
End Function

Function Animate2#(entity%, curr#, start%, quit%, speed#, loop=True)
	
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(curr + speed * FPSfactor,quit),start)
		
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
			newTime = curr + speed * FPSfactor
			
			If newTime < quit Then newTime = start
			If newTime > start Then newTime = quit
			
			;SetAnimTime entity, newTime
		Else
			;SetAnimTime (entity, Max(Min(curr + speed * FPSfactor,start),quit))
			newTime = Max(Min(curr + speed * FPSfactor,start),quit)
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
			mainPlayer\infect008 = Min(mainPlayer\infect008+FPSfactor*0.002,100)
			
			mainPlayer\blurTimer = Max(mainPlayer\infect008*3*(2.0-mainPlayer\crouchState),mainPlayer\blurTimer)
			
			;HeartBeatRate = Max(HeartBeatRate, 100)
			mainPlayer\heartbeatIntensity = Max(100, mainPlayer\infect008/120.0)
			
			EntityAlpha mainPlayer\overlays[OVERLAY_008], Min(((mainPlayer\infect008*0.2)^2)/1000.0,0.5) * (Sin(MilliSecs2()/8.0)+2.0)
			
			For i = 0 To 6
				If mainPlayer\infect008>i*15+10 And temp =< i*15+10 Then
					PlaySound_Strict LoadTempSound("SFX\SCP\008\Voices"+i+".ogg")
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
							r\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist1.ogg")
							r\NPC[0]\SoundChn = PlaySound_Strict(r\NPC[0]\Sound)
							tex = LoadTexture_Strict("GFX\npcs\scientist2.jpg")
							EntityTexture r\NPC[0]\obj, tex
							FreeTexture tex
							r\NPC[0]\State=6
							mainPlayer\currRoom = r
							Exit
						EndIf
					Next
				EndIf
			EndIf
		Else
			temp=mainPlayer\infect008
			mainPlayer\infect008 = Min(mainPlayer\infect008+FPSfactor*0.004,100)
			
			If mainPlayer\infect008 < 94.7 Then
				EntityAlpha mainPlayer\overlays[OVERLAY_008], 0.5 * (Sin(MilliSecs2()/8.0)+2.0)
				mainPlayer\blurTimer = 900
				
				If mainPlayer\infect008 > 94.5 Then mainPlayer\blinkTimer = Max(Min(-50*(mainPlayer\infect008-94.5),mainPlayer\blinkTimer),-10)
				PointEntity mainPlayer\collider, mainPlayer\currRoom\NPC[0]\Collider
				PointEntity mainPlayer\currRoom\NPC[0]\Collider, mainPlayer\collider
				mainPlayer\forceMove = 0.4
				mainPlayer\injuries = 2.5
				mainPlayer\bloodloss = 0
				
				Animate2(mainPlayer\currRoom\NPC[0]\obj, AnimTime(mainPlayer\currRoom\NPC[0]\obj), 357, 381, 0.3)
			ElseIf mainPlayer\infect008 < 98.5
				
				EntityAlpha mainPlayer\overlays[OVERLAY_008], 0.5 * (Sin(MilliSecs2()/5.0)+2.0)
				mainPlayer\blurTimer = 950
				
				If temp < 94.7 Then 
					mainPlayer\currRoom\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist2.ogg")
					mainPlayer\currRoom\NPC[0]\SoundChn = PlaySound_Strict(mainPlayer\currRoom\NPC[0]\Sound)
					
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
				RotateEntity mainPlayer\head, (1.0+Sin(MilliSecs2()/5.0))*15, mainPlayer\currRoom\angle-180, 0, True
				MoveEntity mainPlayer\head, 0,0,0.4
				TurnEntity mainPlayer\head, 80+(Sin(MilliSecs2()/5.0))*30,(Sin(MilliSecs2()/5.0))*40,0
			EndIf
		EndIf
		
		
	Else
		HideEntity mainPlayer\overlays[OVERLAY_008]
	EndIf
End Function

Include "MathUtils.bb"

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
			d\Size=d\Size + d\SizeChange * FPSfactor
			ScaleSprite(d\obj, d\Size, d\Size)
			
			Select d\ID
				Case 0
					If d\Timer <= 0 Then
						Local angle# = Rand(360)
						Local temp# = Rnd(d\Size)
						Local d2.Decals = CreateDecal(1, EntityX(d\obj) + Cos(angle) * temp, EntityY(d\obj) - 0.0005, EntityZ(d\obj) + Sin(angle) * temp, EntityPitch(d\obj), Rnd(360), EntityRoll(d\obj))
						d2\Size = Rnd(0.1, 0.5) : ScaleSprite(d2\obj, d2\Size, d2\Size)
						;TODO: fix
						;PlaySound2(DecaySFX(Rand(1, 3)), mainPlayer\cam, d2\obj, 10.0, Rnd(0.1, 0.5))
						;d\Timer = d\Timer + Rand(50,150)
						d\Timer = Rand(50, 100)
					Else
						d\Timer= d\Timer-FPSfactor
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
			d\Alpha = Min(d\Alpha + FPSfactor * d\AlphaChange, 1.0)
			EntityAlpha(d\obj, d\Alpha)
		End If
		
		If d\lifetime > 0 Then
			d\lifetime=Max(d\lifetime-FPSfactor,5)
		EndIf
		
		If d\Size <= 0 Or d\Alpha <= 0 Or d\lifetime=5.0  Then
			FreeEntity(d\obj)
			Delete d
		End If
	Next
End Function

Include "INI.bb"

;--------------------------------------- MakeCollBox -functions -------------------------------------------------------


; Create a collision box For a mesh entity taking into account entity scale
; (will not work in non-uniform scaled space)
Function MakeCollBox(mesh%)
	Local sx# = EntityScaleX(mesh, 1)
	Local sy# = Max(EntityScaleY(mesh, 1), 0.001)
	Local sz# = EntityScaleZ(mesh, 1)
	GetMeshExtents(mesh)
	EntityBox mesh, Mesh_MinX * sx, Mesh_MinY * sy, Mesh_MinZ * sz, Mesh_MagX * sx, Mesh_MagY * sy, Mesh_MagZ * sz
End Function

; Find mesh extents
Function GetMeshExtents(Mesh%)
	Local s%, surf%, surfs%, v%, verts%, x#, y#, z#
	Local minx# = INFINITY
	Local miny# = INFINITY
	Local minz# = INFINITY
	Local maxx# = -INFINITY
	Local maxy# = -INFINITY
	Local maxz# = -INFINITY
	
	surfs = CountSurfaces(Mesh)
	
	For s = 1 To surfs
		surf = GetSurface(Mesh, s)
		verts = CountVertices(surf)
		
		For v = 0 To verts - 1
			x = VertexX(surf, v)
			y = VertexY(surf, v)
			z = VertexZ(surf, v)
			
			If (x < minx) Then minx = x
			If (x > maxx) Then maxx = x
			If (y < miny) Then miny = y
			If (y > maxy) Then maxy = y
			If (z < minz) Then minz = z
			If (z > maxz) Then maxz = z
		Next
	Next
	
	Mesh_MinX = minx
	Mesh_MinY = miny
	Mesh_MinZ = minz
	Mesh_MaxX = maxx
	Mesh_MaxY = maxy
	Mesh_MaxZ = maxz
	Mesh_MagX = maxx-minx
	Mesh_MagY = maxy-miny
	Mesh_MagZ = maxz-minz
	
End Function

Function EntityScaleX#(entity%, globl% = False)
	If globl Then TFormVector 1, 0, 0, entity, 0 Else TFormVector 1, 0, 0, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 

Function EntityScaleY#(entity%, globl% = False)
	If globl Then TFormVector 0, 1, 0, entity, 0 Else TFormVector 0, 1, 0, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 

Function EntityScaleZ#(entity%, globl% = False)
	If globl Then TFormVector 0, 0, 1, entity, 0 Else TFormVector 0, 0, 1, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 

Function Graphics3DExt%(width%,height%,depth%=32,mode%=2)
	;If FE_InitExtFlag = 1 Then DeInitExt() ;prevent FastExt from breaking itself
	Graphics3D width,height,depth,mode
	InitFastResize()
	;InitExt()
End Function

Function ResizeImage2(image%,width%,height%)
    img% = CreateImage(width,height)
	
	oldWidth% = ImageWidth(image)
	oldHeight% = ImageHeight(image)
	CopyRect 0,0,oldWidth,oldHeight,1024-oldWidth/2,1024-oldHeight/2,ImageBuffer(image),TextureBuffer(fresize_texture)
	SetBuffer BackBuffer()
	ScaleRender(0,0,2048.0 / Float(RealGraphicWidth) * Float(width) / Float(oldWidth), 2048.0 / Float(RealGraphicWidth) * Float(height) / Float(oldHeight))
	;might want to replace Float(userOptions\screenWidth) with Max(userOptions\screenWidth,userOptions\screenHeight) if portrait sizes cause issues
	;everyone uses landscape so it's probably a non-issue
	CopyRect RealGraphicWidth/2-width/2,RealGraphicHeight/2-height/2,width,height,0,0,BackBuffer(),ImageBuffer(img)
	
    FreeImage image
    Return img
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
	;	If (mainPlayer\currRoom\RoomTemplate\Name<>"173") And (mainPlayer\currRoom\RoomTemplate\Name<>"exit1") And (mainPlayer\currRoom\RoomTemplate\Name<>"gatea") Then
	;		AmbientLight Brightness, Brightness, Brightness
	;	EndIf
	;EndIf
	
	;IsNVGBlinking% = False
	;HideEntity NVBlink
	
	Local hasBattery% = 2
	Local power% = 0
	
	Local wornItem.Items = mainPlayer\wornItems[WORNITEM_SLOT_HEAD]
	
	If wornItem<>Null Then
		If wornItem\itemTemplate\tempname <> "nvgoggles" And wornItem\itemTemplate\tempname <> "supernv" Then
			wornItem = Null
		EndIf
	EndIf
	
	If wornItem<>Null Then
		Local decayMultiplier# = 1.0
		If wornItem\itemTemplate\tempname = "supernv" Then decayMultiplier = 2.0
		
		wornItem\state = wornItem\state - (FPSfactor * (0.02 * decayMultiplier))
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
			RenderWorld()
		EndIf
	Else
		RenderWorld()
	EndIf
	
	;If hasBattery=0 And WearingNightVision<>3
	;	;IsNVGBlinking% = True
	;	ShowEntity NVBlink%
	;EndIf
	
	If mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6
		If IsPlayerWearing(mainPlayer,"supernv") And hasBattery<>0 Then ;show a HUD
			;NVTimer=NVTimer-FPSfactor
			
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
			
			SetFont Font3
			
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
			DrawImage NVGImages,40,userOptions\screenHeight*0.5+30,1
			
			Color 255,255,255
		ElseIf IsPlayerWearing(mainPlayer,"nvgoggles") And hasBattery<>0 Then
			Color 0,55,0
			For k=0 To 10
				Rect 45,userOptions\screenHeight*0.5-(k*20),54,10,True
			Next
			Color 0,255,0
			For l=0 To Floor((power%+50)*0.01)
				Rect 45,userOptions\screenHeight*0.5-(l*20),54,10,True
			Next
			DrawImage NVGImages,40,userOptions\screenHeight*0.5+30,0
		EndIf
	EndIf
	
	;render sprites
	CameraProjMode ark_blur_cam,2
	CameraProjMode mainPlayer\cam,0
	RenderWorld()
	CameraProjMode ark_blur_cam,0
	
	If mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6
		If (wornItem<>Null) And (hasBattery=1) And ((MilliSecs2() Mod 800) < 400) Then
			Color 255,0,0
			SetFont Font3
			
			Text userOptions\screenWidth/2,20*MenuScale,"WARNING: LOW BATTERY",True,False
			Color 255,255,255
		EndIf
	EndIf
End Function


Function ScaleRender(x#,y#,hscale#=1.0,vscale#=1.0)
	If mainPlayer\cam<>0 Then HideEntity mainPlayer\cam
	WireFrame 0
	ShowEntity fresize_image
	ScaleEntity fresize_image,hscale,vscale,1.0
	PositionEntity fresize_image, x, y, 1.0001
	ShowEntity fresize_cam
	RenderWorld()
	HideEntity fresize_cam
	HideEntity fresize_image
	WireFrame WireframeState
	If mainPlayer\cam<>0 Then ShowEntity mainPlayer\cam
End Function

Function InitFastResize()
    ;Create Camera
	Local cam% = CreateCamera()
	CameraProjMode cam, 2
	CameraZoom cam, 0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, -10000
	
	fresize_cam = cam
	
    ;ark_sw = GraphicsWidth()
    ;ark_sh = GraphicsHeight()
	
    ;Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	ScaleEntity spr, 2048.0 / Float(RealGraphicWidth), 2048.0 / Float(RealGraphicHeight), 1
	PositionEntity spr, 0, 0, 1.0001
	EntityOrder spr, -100001
	EntityBlend spr, 1
	fresize_image = spr
	
    ;Create texture
	fresize_texture = CreateTexture(2048, 2048, 1+256)
	fresize_texture2 = CreateTexture(2048, 2048, 1+256)
	TextureBlend fresize_texture2,3
	SetBuffer(TextureBuffer(fresize_texture2))
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())
	;TextureAnisotropy(fresize_texture)
	EntityTexture spr, fresize_texture,0,0
	EntityTexture spr, fresize_texture2,0,1
	
	HideEntity fresize_cam
End Function

Function UpdateLeave1499()
	Local r.Rooms, it.Items
	
	If (Not Wearing1499) And mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499"
		For r.Rooms = Each Rooms
			If r = NTF_1499PrevRoom
				mainPlayer\blinkTimer = -1
				;Msg = "You removed the gas mask and reappeared inside the facility."
				;MsgTimer = 70 * 5
				NTF_1499X# = EntityX(mainPlayer\collider)
				NTF_1499Y# = EntityY(mainPlayer\collider)
				NTF_1499Z# = EntityZ(mainPlayer\collider)
				PositionEntity (mainPlayer\collider, NTF_1499PrevX#, NTF_1499PrevY#+0.05, NTF_1499PrevZ#)
				ResetEntity(mainPlayer\collider)
				UpdateDoors()
				UpdateRooms()
				For it.Items = Each Items
					it\disttimer = 0
					If it\itemtemplate\tempname = "scp1499" Or it\itemtemplate\tempname = "super1499"
						If EntityY(it\collider) >= EntityY(mainPlayer\currRoom\obj)-5
							PositionEntity it\collider,NTF_1499PrevX#,NTF_1499PrevY#+(EntityY(it\collider)-EntityY(mainPlayer\currRoom\obj)),NTF_1499PrevZ#
							ResetEntity it\collider
						EndIf
					EndIf
				Next
				mainPlayer\currRoom = r
				PlaySound_Strict NTF_1499LeaveSFX%
				NTF_1499PrevX# = 0.0
				NTF_1499PrevY# = 0.0
				NTF_1499PrevZ# = 0.0
				NTF_1499PrevRoom = Null
				;Brightness = StoredBrightness
				Exit
			EndIf
		Next
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

Function IsItemGoodFor1162(itt.ItemTemplates)
	Local IN$ = itt\tempname$
	
	Select itt\tempname
		Case "key1", "key2", "key3"
			Return True
		Case "misc", "420", "cigarette"
			Return True
		Case "vest", "finevest","gasmask"
			Return True
		Case "radio","18vradio"
			Return True
		Case "clipboard","eyedrops","nvgoggles"
			Return True
		Default
			If itt\tempname <> "paper" Then
				Return False
			Else If Instr(itt\name, "Leaflet")
				Return False
			Else
				;if the item is a paper, only allow spawning it if the name contains the word "note" or "log"
				;(because those are items created recently, which D-9341 has most likely never seen)
				Return ((Not Instr(itt\name, "Note")) And (Not Instr(itt\name, "Log")))
			EndIf
	End Select
End Function

Function ControlSoundVolume()
	Local snd.Sound,i
	
	For snd.Sound = Each Sound
		For i=0 To 31
			;If snd\channels[i]<>0 Then
			;	ChannelVolume snd\channels[i],userOptions\soundVolume#
			;Else
				ChannelVolume snd\channels[i],userOptions\SoundVolume#
			;EndIf
		Next
	Next
	
End Function

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

Function ScaledMouseX%()
	Return Float(MouseX()-(RealGraphicWidth*0.5*(1.0-AspectRatioRatio)))*Float(userOptions\screenWidth)/Float(RealGraphicWidth*AspectRatioRatio)
End Function

Function ScaledMouseY%()
	Return Float(MouseY())*Float(userOptions\screenHeight)/Float(RealGraphicHeight)
End Function
;~IDEal Editor Parameters:
;~F#607
;~C#Blitz3D
Type Sound
	Field internal%
	Field file$
End Type

Const STEPSOUND_DEFAULT% = 0
Const STEPSOUND_METAL% = 1
;TODO: add more STEPSOUND constants

Type SoundManager
	Field chnList.IntArrayList

	Field button.Sound
	Field buttonErr.Sound
	
	; Footsteps
	Field footstep.Sound[8]
	Field footstepRun.Sound[8]
	Field footstepMetal.Sound[8]
	Field footstepMetalRun.Sound[8]
	Field footstepPD.Sound[3]
	Field footstep8601.Sound[3]

	; Doors
	Field openDoor.Sound[3]
	Field openBigDoor.Sound[2]
	Field openHCZDoor.Sound[3]

	Field closeDoor.Sound[3]
	Field closeBigDoor.Sound[2]
	Field closeHCZDoor.Sound[3]
	
	; Keycards
	Field keycardUse.Sound
	Field keycardErr.Sound
	Field scannerUse.Sound
	Field scannerErr.Sound
	
	; Pickups
	Field itemPick.Sound[4]
	
	; Elevator
	Field elevatorBeep.Sound
	Field elevatorMove.Sound
	
	; Tesla
	Field teslaIdle.Sound
	Field teslaActive.Sound
	Field teslaPowerUp.Sound
	Field teslaShock.Sound
	
	; Bullets
	Field gunshot.Sound[2]
	Field bulletHit.Sound
	Field bulletMiss.Sound
	
	Field caution.Sound
	Field hiss.Sound
	Field lightSwitch.Sound
	Field lever.Sound
	Field burst.Sound
	Field camera.Sound
	Field heartbeat.Sound
End Type
Global sndManager.SoundManager

Function CreateSoundManager.SoundManager()
	Local sndMan.SoundManager = New SoundManager

	sndMan\chnList = CreateIntArrayList()
	sndMan\button = LoadSound_SM("SFX\Interact\Button.ogg")

	Return sndMan
End Function

Function LoadInGameSounds(sndMan.SoundManager)
	Local i%
	
	sndMan\buttonErr = InitializeSound_SM("SFX\Interact\Button2.ogg")
	
	For i = 0 To 7
		sndMan\footstep[i] = LoadSound_SM("SFX\Step\Step" + (i + 1) + ".ogg")
		sndMan\footstepRun[i] = LoadSound_SM("SFX\Step\Run" + (i + 1) + ".ogg")
		sndMan\footstepMetal[i] = LoadSound_SM("SFX\Step\StepMetal" + (i + 1) + ".ogg")
		sndMan\footstepMetalRun[i] = LoadSound_SM("SFX\Step\RunMetal" + (i + 1) + ".ogg")
	Next

	For i = 0 To 2
		sndMan\footstepPD[i] = InitializeSound_SM("SFX\Step\StepPD" + (i + 1) + ".ogg")
		sndMan\footstep8601[i] = InitializeSound_SM("SFX\Step\StepForest" + (i + 1) + ".ogg")
	Next

	For i = 0 To 2
		sndMan\openDoor[i] = LoadSound_SM("SFX\Door\DoorOpen" + (i + 1) + ".ogg")
		sndMan\closeDoor[i] = LoadSound_SM("SFX\Door\DoorClose" + (i + 1) + ".ogg")
		sndMan\openHCZDoor[i] = LoadSound_SM("SFX\Door\Door2Open" + (i + 1) + ".ogg")
		sndMan\closeHCZDoor[i] = LoadSound_SM("SFX\Door\Door2Close" + (i + 1) + ".ogg")
	Next

	For i = 0 To 1
		sndMan\openBigDoor[i] = LoadSound_SM("SFX\Door\BigDoorOpen" + (i + 1) + ".ogg")
		sndMan\closeBigDoor[i] = LoadSound_SM("SFX\Door\BigDoorClose" + (i + 1) + ".ogg")
	Next
	
	sndMan\keycardUse = InitializeSound_SM("SFX\Interact\KeyCardUse1.ogg")
	sndMan\keycardErr = InitializeSound_SM("SFX\Interact\KeyCardUse2.ogg")
	sndMan\scannerUse = InitializeSound_SM("SFX\Interact\ScannerUse1.ogg")
	sndMan\scannerErr = InitializeSound_SM("SFX\Interact\ScannerUse2.ogg")
	
	For i = 0 To 3
		sndMan\itemPick[i] = LoadSound_SM("SFX\Interact\PickItem" + i + ".ogg")
	Next
	
	sndMan\elevatorBeep = InitializeSound_SM("SFX\General\Elevator\Beep.ogg")
	sndMan\elevatorMove = InitializeSound_SM("SFX\General\Elevator\Moving.ogg")
	
	sndMan\teslaIdle = InitializeSound_SM("SFX\Room\Tesla\Idle.ogg")
	sndMan\teslaActive = InitializeSound_SM("SFX\Room\Tesla\WindUp.ogg")
	sndMan\teslaPowerUp = InitializeSound_SM("SFX\Room\Tesla\PowerUp.ogg")
	sndMan\teslaShock = InitializeSound_SM("SFX\Room\Tesla\Shock.ogg")
	
	sndMan\gunshot[0] = InitializeSound_SM("SFX\General\Gunshot.ogg")
	sndMan\gunshot[1] = InitializeSound_SM("SFX\General\Gunshot2.ogg")
	sndMan\bulletHit = InitializeSound_SM("SFX\General\BulletHit.ogg")
	sndMan\bulletMiss = InitializeSound_SM("SFX\General\BulletMiss.ogg")
	
	sndMan\caution = InitializeSound_SM("SFX\Room\LockroomSiren.ogg")
	sndMan\hiss = InitializeSound_SM("SFX\General\Hiss.ogg")
	sndMan\lightSwitch = InitializeSound_SM("SFX\General\LightSwitch.ogg")
	sndMan\lever = InitializeSound_SM("SFX\Interact\LeverFlip.ogg")
	sndMan\burst = InitializeSound_SM("SFX\Room\TunnelBurst.ogg")
	sndMan\camera = InitializeSound_SM("SFX\General\Camera.ogg")
	sndMan\heartbeat = InitializeSound_SM("SFX\Character\D9341\Heartbeat.ogg")
End Function

Function DeloadInGameSounds(sndMan.SoundManager)
	Local i%
	For i = 0 To 7
		FreeSound_SM(sndMan\footstep[i])
		FreeSound_SM(sndMan\footstepRun[i])
		FreeSound_SM(sndMan\footstepMetal[i])
		FreeSound_SM(sndMan\footstepMetalRun[i])
	Next

	For i = 0 To 2
		FreeSound_SM(sndMan\footstepPD[i])
		FreeSound_SM(sndMan\footstep8601[i])
	Next

	For i = 0 To 2
		FreeSound_SM(sndMan\openDoor[i])
		FreeSound_SM(sndMan\closeDoor[i])
		FreeSound_SM(sndMan\openHCZDoor[i])
		FreeSound_SM(sndMan\closeHCZDoor[i])
	Next

	For i = 0 To 1
		FreeSound_SM(sndMan\openBigDoor[i])
		FreeSound_SM(sndMan\closeBigDoor[i])
	Next

	FreeSound_SM(sndMan\keycardErr)
	FreeSound_SM(sndMan\keycardUse)
	FreeSound_SM(sndMan\scannerUse)
	FreeSound_SM(sndMan\scannerErr)
	
	For i = 0 To 3
		FreeSound_SM(sndMan\itemPick[i])
	Next
	
	FreeSound_SM(sndMan\elevatorBeep)
	FreeSound_SM(sndMan\elevatorMove)
	
	FreeSound_SM(sndMan\teslaIdle)
	FreeSound_SM(sndMan\teslaActive)
	FreeSound_SM(sndMan\teslaPowerUp)
	FreeSound_SM(sndMan\teslaShock)
	
	FreeSound_SM(sndMan\gunshot[0])
	FreeSound_SM(sndMan\gunshot[1])
	FreeSound_SM(sndMan\bulletHit)
	FreeSound_SM(sndMan\bulletMiss)
	
	FreeSound_SM(sndMan\caution)
	FreeSound_SM(sndMan\hiss)
	FreeSound_SM(sndMan\lightSwitch)
	FreeSound_SM(sndMan\lever)
	FreeSound_SM(sndMan\burst)
	FreeSound_SM(sndMan\camera)
	FreeSound_SM(sndMan\heartbeat)
End Function

Type SoundChannel
	Field internal%

	Field camera%
	Field point%

	Field range#
	Field volume#
End Type

; Add a channel to the list.
Function AddChannel(ref%)
	If (ref = 0) Then
		Return
	EndIf

	Local chn.SoundChannel = New SoundChannel
	chn\internal = ref

	PushIntArrayListElem(sndManager\chnList, Handle(chn))
End Function

Function AddPositionalChannel(ref%, cam%, ent%, range# = 10, vol# = 1.0)
	If (ref = 0) Then
		Return
	EndIf

	Local chn.SoundChannel = New SoundChannel
	chn\internal = ref
	chn\camera = cam
	chn\point = CreatePivot()
	PositionEntity(chn\point, EntityX(ent), EntityY(ent), EntityZ(ent))
	chn\range = range
	chn\volume = vol

	PushIntArrayListElem(sndManager\chnList, Handle(chn))
End Function

Function UpdateChannelList()
	Local i%
	For i = 0 To sndManager\chnList\size-1
		Local chn.SoundChannel =  Object.SoundChannel(GetIntArrayListElem(sndManager\chnList, i))
		If (Not IsChannelPlaying(chn\internal)) Then
			EraseIntArrayListElem(sndManager\chnList, i)
			FreeEntity(chn\point)
			Delete chn
			i = i - 1
		ElseIf (chn\camera <> 0) Then
			UpdateRangedSoundOrigin_SM(chn)
		EndIf
	Next
End Function

; Creates a new sound object.
Function InitializeSound_SM.Sound(fileName$)
	Local snd.Sound = New Sound
	snd\file = fileName

	Return snd
End Function

; Creates a new sound object and loads the given sound.
Function LoadSound_SM.Sound(fileName$)
	Local snd.Sound = InitializeSound_SM(fileName)
	snd\internal = LoadSound(fileName)

	Return snd
End Function

Function PlaySound2%(snd%)
	Local chn% = PlaySound(snd)
	AddChannel(chn)
	Return chn
End Function

Function PlaySound_SM%(snd.Sound)
	;If the sound hasn't been loaded yet then do that.
	If (snd\internal = 0) Then
		snd\internal = LoadSound(snd\file)
	EndIf
	
	;Play the sound.
	Return PlaySound2(snd\internal)
End Function

Function FreeSound_SM(snd.Sound)
	If (snd\internal <> 0) Then
		FreeSound(snd\internal)
		snd\internal = 0
	EndIf

	Delete snd
End Function

Function IsChannelPlaying%(chn%)
	If (chn = 0) Then
		Return False
	EndIf

	Return ChannelPlaying(chn)
End Function

Function PlayRangedSound%(soundHandle%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range, 1.0)
	Local soundChn% = 0
	
	If (volume > 0) Then
		Local dist# = EntityDistance(cam, entity) / range
		If (1 - dist > 0 And 1 - dist < 1) Then
			Local panvalue# = Sin(-DeltaYaw(cam, entity))
			soundChn = PlaySound(soundHandle)
			
			ChannelVolume(soundChn, volume * (1 - dist) * userOptions\SoundVolume)
			ChannelPan(soundChn, panvalue)
		EndIf
	EndIf
	
	AddPositionalChannel(soundChn, cam, entity, range, volume)
	
	Return soundChn
End Function

Function PlayRangedSound_SM%(snd.Sound, cam%, entity%, range# = 10, volume# = 1.0)
	If (snd\internal = 0) Then
		snd\internal = LoadSound(snd\file)
	EndIf
	
	Return PlayRangedSound(snd\internal, cam, entity, range, volume)
End Function

; Only begins playing the sound if the sound channel isn't playing anything else.
Function LoopRangedSound%(soundHandle%, chn%, cam%, entity%, range# = 10, volume# = 1.0)
	range = Max(range,1.0)
	
	If (Not IsChannelPlaying(chn)) Then chn% = PlaySound(soundHandle)
	
	UpdateRangedSoundOrigin(chn, cam, entity, range, volume)

	Return chn
End Function

Function LoopRangedSound_SM%(snd.Sound, chn%, cam%, entity%, range# = 10, volume# = 1.0)
	If (snd\internal = 0) Then
		snd\internal = LoadSound(snd\file)
	EndIf
	Return LoopRangedSound(snd\internal, chn, cam, entity, range, volume)
End Function

Function UpdateRangedSoundOrigin(chn%, cam%, entity%, range# = 10, volume# = 1.0)
	range = Max(range, 1.0)
	
	If (volume > 0) Then
		
		Local dist# = EntityDistance(cam, entity) / range
		If (1 - dist > 0 And 1 - dist < 1) Then
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			
			ChannelVolume(chn, volume * (1 - dist) * userOptions\SoundVolume)
			ChannelPan(chn, panvalue)
		EndIf
	Else
		If (chn <> 0) Then
			ChannelVolume(chn, 0)
		EndIf 
	EndIf
End Function

Function UpdateRangedSoundOrigin_SM(chn.SoundChannel)
	If (chn\volume > 0) Then
		Local dist# = EntityDistance(chn\camera, chn\point) / chn\range
		If (1 - dist > 0 And 1 - dist < 1) Then
			Local panvalue# = Sin(-DeltaYaw(chn\camera, chn\point))
			
			ChannelVolume(chn\internal, chn\volume * (1 - dist) * userOptions\SoundVolume)
			ChannelPan(chn\internal, panvalue)
		EndIf
	Else
		ChannelVolume(chn\internal, 0)
	EndIf
End Function

Function LoadTempSound%(file$)
	If (TempSounds[TempSoundIndex] <> 0) Then
		FreeSound(TempSounds[TempSoundIndex])
	EndIf

	Local TempSound% = LoadSound(file)
	TempSounds[TempSoundIndex] = TempSound

	TempSoundIndex = (TempSoundIndex + 1) Mod 10

	Return TempSound
End Function

Function LoadEventSound(e.Events,file$,num%=0)
	Local i%
	For i = 0 To EVENT_CHANNEL_COUNT-1
		If (num = i) Then
			If (e\sounds[i]<>0) Then FreeSound(e\sounds[i]) : e\sounds[i]=0
			e\sounds[i] = LoadSound(file)
			Return e\sounds[i]
		EndIf
	Next
End Function

Function PauseSounds()
	Local sc.SoundChannel, sc2.SecurityCams, r.Rooms, e.Events, n.NPCs, em.Emitters, i%

	For sc = Each SoundChannel
		If (IsChannelPlaying(sc\internal)) Then
			PauseChannel(sc\internal)
		EndIf
	Next
	
	For sc2 = Each SecurityCams
		If (IsChannelPlaying(sc2\soundCHN)) Then
			PauseChannel(sc2\soundCHN)
		EndIf
	Next
	
	For r = Each Rooms
		If (IsChannelPlaying(r\SoundCHN)) Then
			PauseChannel(r\SoundCHN)
		EndIf
	Next

	For e = Each Events
		For i = 0 To EVENT_CHANNEL_COUNT-1
			If IsChannelPlaying(e\soundChannels[i]) Then
				PauseChannel(e\soundChannels[i])
			EndIf
		Next
	Next
	
	For n = Each NPCs
		For i = 0 To NPC_CHANNEL_COUNT-1
			If IsChannelPlaying(n\soundChannels[i]) Then
				PauseChannel(n\soundChannels[i])
			EndIf
		Next
	Next
	
	For em = Each Emitters
		If (IsChannelPlaying(em\soundCHN)) Then
			PauseChannel(em\soundCHN)
		EndIf
	Next
	
	If (IsChannelPlaying(mainPlayer\breathChn)) Then
		PauseChannel(mainPlayer\breathChn)
	EndIf
End Function

Function ResumeSounds()
	Local sc.SoundChannel, sc2.SecurityCams, r.Rooms, e.Events, n.NPCs, em.Emitters, i%

	For sc = Each SoundChannel
		If (IsChannelPlaying(sc\internal)) Then
			ResumeChannel(sc\internal)
		EndIf
	Next
	
	For sc2 = Each SecurityCams
		If (IsChannelPlaying(sc2\soundCHN)) Then
			ResumeChannel(sc2\soundCHN)
		EndIf
	Next
	
	For r = Each Rooms
		If (IsChannelPlaying(r\SoundCHN)) Then
			ResumeChannel(r\SoundCHN)
		EndIf
	Next

	For e = Each Events
		For i = 0 To EVENT_CHANNEL_COUNT-1
			If IsChannelPlaying(e\soundChannels[i]) Then
				ResumeChannel(e\soundChannels[i])
			EndIf
		Next
	Next
	
	For n = Each NPCs
		For i = 0 To NPC_CHANNEL_COUNT-1
			If IsChannelPlaying(n\soundChannels[i]) Then
				ResumeChannel(n\soundChannels[i])
			EndIf
		Next
	Next
	
	For em = Each Emitters
		If (IsChannelPlaying(em\soundCHN)) Then
			ResumeChannel(em\soundCHN)
		EndIf
	Next
	
	If (IsChannelPlaying(mainPlayer\breathChn)) Then
		ResumeChannel(mainPlayer\breathChn)
	EndIf
End Function

Function GetMaterialStepSound(entity%)
    Local picker% = LinePick(EntityX(entity),EntityY(entity),EntityZ(entity),0,-1,0)

    If picker <> 0 Then
        If GetEntityType(picker) <> HIT_MAP Then Return 0
        Local brush% = GetSurfaceBrush(GetSurface(picker,CountSurfaces(picker)))
        If brush <> 0 Then
            Local texture% = GetBrushTexture(brush,2)
			Local name$
			Local mat.Materials

            If texture <> 0 Then
                name = StripPath(TextureName(texture))
                If (name <> "") FreeTexture(texture)
                For mat.Materials = Each Materials
                    If mat\name = name Then
                        If mat\StepSound > 0 Then
                            FreeBrush(brush)
                            Return mat\StepSound
                        EndIf
                        Exit
                    EndIf
                Next                
            EndIf
            texture = GetBrushTexture(brush,1)
            If texture <> 0 Then
                name = StripPath(TextureName(texture))
                If (name <> "") FreeTexture(texture)
                FreeBrush(brush)
                For mat.Materials = Each Materials
                    If mat\name = name Then
                        If mat\StepSound > 0 Then
                            Return mat\StepSound
                        EndIf
                        Exit
                    EndIf
                Next                
            EndIf
        EndIf
    EndIf
    
    Return 0
End Function



;;; Music
Const MUS_MENU$    = "SFX\Music\Menu.ogg"
Const MUS_INTRO$   = "SFX\Music\Intro.ogg"
Const MUS_LCZ$     = "SFX\Music\The Dread.ogg"
Const MUS_EZ$      = "SFX\Music\EntranceZone.ogg"
Const MUS_HCZ$     = "SFX\Music\HeavyContainment.ogg"
Const MUS_049$     = "SFX\Music\Room049.ogg"
Const MUS_079$     = "SFX\Music\079.ogg"
Const MUS_106$     = "SFX\Music\106.ogg"
Const MUS_205$     = "SFX\Music\205.ogg"
Const MUS_939$     = "SFX\Music\Room939.ogg"
Const MUS_8601$    = "SFX\Music\8601.ogg"
Const MUS_8602$    = "SFX\Music\8601Cancer.ogg"
Const MUS_14991$   = "SFX\Music\1499.ogg"
Const MUS_14992$   = "SFX\Music\1499Danger.ogg"
Const MUS_PD$      = "SFX\Music\PD.ogg"
Const MUS_TRENCH$  = "SFX\Music\PDTrench.ogg"
Const MUS_GATEA$   = "SFX\Music\GateA.ogg"
Const MUS_GATEB1$  = "SFX\Music\GateB1.ogg"
Const MUS_GATEB2$  = "SFX\Music\GateB2.ogg"
Const MUS_END$     = "SFX\Music\Ending.ogg"
Const MUS_BREATH$  = "SFX\Music\Breath.ogg"

Type MusicManager
	Field currMusic%
	Field channel%

	; Whether the music playing should be relevant to the current zone/menu
	; the player is in.
	Field useDefault%

	Field nowPlaying$
	Field shouldPlay$

	; Used for fading out tracks.
	Field fadeOut%
	Field currMusicVolume#
End Type
Global musicManager.MusicManager

Function CreateMusicManager.MusicManager()
	Local musMan.MusicManager = New MusicManager
	musMan\useDefault = True

	Return musMan
End Function

Function RestoreDefaultMusic()
	musicManager\fadeOut = True
	musicManager\useDefault = True
End Function

Function SetNextMusicTrack(trackName$, fadeOut% = True)
	If (musicManager\shouldPlay = trackName) Then
		Return
	EndIf
	
	;If the track's already being overwritten then don't let anything else change it.
	If (Not musicManager\useDefault) Then
		Return
	EndIf

	musicManager\useDefault = False
	musicManager\shouldPlay = trackName
	musicManager\fadeOut = fadeOut
End Function

; Frees the current music track.
Function FreeMusic()
	If (musicManager\currMusic <> 0) Then
		FreeSound(musicManager\currMusic)
		musicManager\currMusic = 0
	EndIf
End Function

Function UpdateMusic()
	If (userOptions\musicVolume <= 0) Then
		Return
	EndIf

	If (musicManager\useDefault) Then
		If (CurrGameState = GAMESTATE_MAINMENU) Then
			musicManager\shouldPlay = MUS_MENU
		Else
			musicManager\shouldPlay = MUS_LCZ
		EndIf
		;TODO: Play zone track here depending on game's current state.
	EndIf
	
	If (musicManager\nowPlaying <> musicManager\shouldPlay) Then
		If (musicManager\fadeOut And musicManager\currMusicVolume > 0) Then
			musicManager\currMusicVolume = musicManager\currMusicVolume - (timing\tickDuration / 250.0)
		Else
			If (musicManager\channel <> 0) Then
				StopChannel(musicManager\channel)
				musicManager\channel = 0
			EndIf
			
			FreeMusic()
			musicManager\nowPlaying = musicManager\shouldPlay
			musicManager\currMusic = LoadSound(musicManager\nowPlaying)

			musicManager\fadeOut = False
			musicManager\currMusicVolume = userOptions\musicVolume
		EndIf
	ElseIf (musicManager\currMusicVolume <> userOptions\musicVolume) Then
		musicManager\currMusicVolume = userOptions\musicVolume
	EndIf

	;If nothing is playing then figure out what the next track is.
	If (Not IsChannelPlaying(musicManager\channel)) Then
		musicManager\channel = PlaySound(musicManager\currMusic)
	EndIf
	
	ChannelVolume(musicManager\channel, musicManager\currMusicVolume)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
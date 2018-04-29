Type Sound
	Field internal%
	Field file$
End Type

Type SoundManager
	; Footsteps
	Field footstep.Sound[8]
	Field footstepRun.Sound[8]
	Field footstepMetal.Sound[8]
	Field footstepMetalRun.Sound[8]
	Field footstepPD.Sound[3]
	Field footstep8601.Sound[3]
End Type
Global sndManager.SoundManager

Function CreateSoundManager.SoundManager()
	Local sndMan.SoundManager = New SoundManager



	Return sndMan
End Function

Function LoadSoundManager(sndMan.SoundManager)
	Local i%
	For i = 0 To 7
		sndMan\footstep = StoreSound_SM("SFX\Step\Step" + (i + 1) + ".ogg")
		sndMan\footstepRun = StoreSound_SM("SFX\Step\Run" + (i + 1) + ".ogg", 0, 1, i)
		sndMan\footstepMetal = StoreSound_SM("SFX\Step\StepMetal" + (i + 1) + ".ogg", 1, 0, i)
		sndMan\footstepMetalRun = StoreSound_SM("SFX\Step\RunMetal" + (i + 1) + ".ogg", 1, 1, i)
	Next

	For i = 0 To 2
		sndMan\footstepPD = LoadSound_SM("SFX\Step\StepPD" + (i + 1) + ".ogg")
		sndMan\footstep8601 = LoadSound_SM("SFX\Step\StepForest" + (i + 1) + ".ogg")
	Next
End Function

Function DeloadSoundManager(sndMan.SoundManager)
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
End Function

; Creates a new sound object.
Function LoadSound_SM.Sound(fileName$)
	Local snd.Sound = New Sound
	snd\file = fileName

	Return snd
End Function

; Creates a new sound object and loads the given sound.
Function StoreSound_SM.Sound(fileName$)
	Local snd.Sound = LoadSound_SM(fileName)
	snd\internal = LoadSound(fileName)

	Return snd
End Function

Function PlaySound_SM%(snd.Sound)
	;If the sound hasn't been loaded yet then do that.
	If (snd\internal = 0) Then
		snd\internal = LoadSound(fileName)
	EndIf

	;Play the sound.
	Local chn% = PlaySound(snd\internal)
	Return chn
End Function

Function FreeSound_SM(snd.Sound)
	If (snd\internal <> 0) Then
		FreeSound(snd\internal)
		snd\internal = 0
	EndIf

	Delete snd
End Function

Function IsChannelPlaying(chn%)
	If (chn = 0) Then
		Return False
	EndIf

	Return ChannelPlaying(chn)
End Function

Function PlayRangedSound%(soundHandle%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range, 1.0)
	Local soundChn% = 0
	
	If (volume > 0) Then
		Local dist# = EntityDistance(cam, entity) / range#
		If (1 - dist# > 0 And 1 - dist# < 1) Then
			Local panvalue# = Sin(-DeltaYaw(cam, entity))
			soundChn% = PlaySound(soundHandle)
			
			ChannelVolume(soundChn, volume# * (1 - dist#) * userOptions\SoundVolume)
			ChannelPan(soundChn, panvalue)
		EndIf
	EndIf
	
	Return soundChn
End Function

; Only begins playing the sound if the sound channel isn't playing anything else.
Function LoopRangedSound%(soundHandle%, chn%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range,1.0)
	
	If (Not IsChannelPlaying(chn)) Then chn% = PlaySound(soundHandle)
	
	UpdateRangedSoundOrigin(chn,cam,entity,range,volume)

	Return Chn
End Function

Function LoadTempSound(file$)
	If TempSounds[TempSoundIndex]<>0 Then FreeSound(TempSounds[TempSoundIndex])
	Local TempSound% = LoadSound(file)
	TempSounds[TempSoundIndex] = TempSound

	TempSoundIndex=(TempSoundIndex+1) Mod 10

	Return TempSound
End Function

Function LoadEventSound(e.Events,file$,num%=0)
	Local i%
	For i = 0 To 1
		If (num = i) Then
			If (e\sounds[i]<>0) Then FreeSound(e\sounds[i]) : e\sounds[i]=0
			e\sounds[i]=LoadSound(file)
			Return e\sounds[i]
		EndIf
	Next
End Function

Function UpdateMusic()
	
	;TODO: Not hack jorge.
	If ConsoleFlush Then
		If Not IsChannelPlaying(MusicCHN) Then MusicCHN = PlaySound(ConsoleMusFlush)
	Else
		If timing\tickDuration > 0 Or OptionsMenu = 2 Then 
			If NowPlaying <> ShouldPlay Then ; playing the wrong clip, fade out
				CurrMusicVolume# = Max(CurrMusicVolume - (timing\tickDuration / 250.0), 0)
				If CurrMusicVolume = 0 Then
					NowPlaying = ShouldPlay
					If MusicCHN <> 0 Then StopChannel MusicCHN
				EndIf
			Else ; playing the right clip
				CurrMusicVolume = CurrMusicVolume + (MusicVolume - CurrMusicVolume) * 0.1
			EndIf
		EndIf
		
		If NowPlaying < 66 Then
			If MusicCHN = 0 Then
				MusicCHN = PlaySound(Music(NowPlaying))
			Else
				If (Not IsChannelPlaying(MusicCHN)) Then MusicCHN = PlaySound(Music(NowPlaying))
			EndIf
		EndIf
		
		ChannelVolume(MusicCHN, CurrMusicVolume)
	EndIf
	
End Function 

Function PauseSounds()
	Local e.Events, n.NPCs, d.Doors

	For e = Each Events
		If e\soundChannels[0] <> 0 Then
			If IsChannelPlaying(e\soundChannels[0]) Then PauseChannel(e\soundChannels[0])
		EndIf
		If e\soundChannels[1] <> 0 Then
			If IsChannelPlaying(e\soundChannels[1]) Then PauseChannel(e\soundChannels[1])
		EndIf		
	Next
	
	For n = Each NPCs
		If n\soundchn <> 0 Then
			If IsChannelPlaying(n\soundchn) Then PauseChannel(n\soundchn)
		EndIf
	Next	
	
	For d = Each Doors
		If d\soundchn <> 0 Then
			If IsChannelPlaying(d\soundchn) Then PauseChannel(d\soundchn)
		EndIf
	Next	
	
	If AmbientSFXCHN <> 0 Then
		If IsChannelPlaying(AmbientSFXCHN) Then PauseChannel(AmbientSFXCHN)
	EndIf
End Function

Function ResumeSounds()
	Local e.Events, n.NPCs, d.Doors

	For e = Each Events
		If e\soundChannels[0] <> 0 Then
			If IsChannelPlaying(e\soundChannels[0]) Then ResumeChannel(e\soundChannels[0])
		EndIf
		If e\soundChannels[1] <> 0 Then
			If IsChannelPlaying(e\soundChannels[1]) Then ResumeChannel(e\soundChannels[1])
		EndIf	
	Next
	
	For n = Each NPCs
		If n\soundchn <> 0 Then
			If IsChannelPlaying(n\soundchn) Then ResumeChannel(n\soundchn)
		EndIf
	Next	
	
	For d = Each Doors
		If d\soundchn <> 0 Then
			If IsChannelPlaying(d\soundchn) Then ResumeChannel(d\soundchn)
		EndIf
	Next	
	
	If AmbientSFXCHN <> 0 Then
		If IsChannelPlaying(AmbientSFXCHN) Then ResumeChannel(AmbientSFXCHN)
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

Function UpdateRangedSoundOrigin(Chn%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range,1.0)
	
	If volume>0 Then
		
		Local dist# = EntityDistance(cam, entity) / range#
		If 1 - dist# > 0 And 1 - dist# < 1 Then
			
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			
			ChannelVolume(Chn, volume# * (1 - dist#)*userOptions\SoundVolume)
			ChannelPan(Chn, panvalue)
		EndIf
	Else
		If Chn <> 0 Then
			ChannelVolume (Chn, 0)
		EndIf 
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
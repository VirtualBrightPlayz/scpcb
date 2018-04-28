Type Sound
	Field internal%
	Field file$
End Type

Type SoundManager
	Field Footstep
End Type
Global soundManager.SoundManager

Function CreateSoundManager.SoundManager()
	Local sndManager.SoundManager = New SoundManager


	Return sndManager
End Function

Function DeleteSoundManager(sndManager.SoundManager)

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
	
	If volume>0 Then
		
		Local dist# = EntityDistance(cam, entity) / range#
		If 1 - dist# > 0 And 1 - dist# < 1 Then
			
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			
			If chn = 0 Then
				chn% = PlaySound(soundHandle)
			Else
				If (Not IsChannelPlaying(chn)) Then chn% = PlaySound(soundHandle)
			EndIf
			
			ChannelVolume(Chn, volume# * (1 - dist#)*userOptions\SoundVolume)
			ChannelPan(Chn, panvalue)
		EndIf
	Else
		If Chn <> 0 Then
			ChannelVolume (Chn, 0)
		EndIf 
	EndIf

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

Function GetStepSound(entity%)
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
                            Return mat\StepSound-1
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
                            Return mat\StepSound-1
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
			
			ChannelVolume(Chn, volume# * (1 - dist#))
			ChannelPan(Chn, panvalue)
		EndIf
	Else
		If Chn <> 0 Then
			ChannelVolume (Chn, 0)
		EndIf 
	EndIf
End Function

Function UpdateSoundOrigin(Chn%, cam%, entity%, range# = 10, volume# = 1.0)
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


Function PlaySound2%(SoundHandle%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range, 1.0)
	Local soundchn% = 0
	
	If volume > 0 Then 
		Local dist# = EntityDistance(cam, entity) / range#
		If 1 - dist# > 0 And 1 - dist# < 1
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			soundchn% = PlaySound_Strict (SoundHandle)
			
			ChannelVolume(soundchn, volume# * (1 - dist#)*userOptions\soundVolume)
			ChannelPan(soundchn, panvalue)			
		EndIf
	EndIf
	
	Return soundchn
End Function

Function LoopSound2%(SoundHandle%, Chn%, cam%, entity%, range# = 10, volume# = 1.0)
	range# = Max(range,1.0)
	
	If volume>0 Then
		
		Local dist# = EntityDistance(cam, entity) / range#
		If 1 - dist# > 0 And 1 - dist# < 1 Then
			
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			
			If Chn = 0 Then
				Chn% = PlaySound_Strict (SoundHandle)
			Else
				If (Not ChannelPlaying(Chn)) Then Chn% = PlaySound_Strict(SoundHandle)
			EndIf
			
			ChannelVolume(Chn, volume# * (1 - dist#)*userOptions\soundVolume)
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
	If TempSounds[TempSoundIndex]<>0 Then FreeSound_Strict(TempSounds[TempSoundIndex])
	TempSound = LoadSound_Strict(file)
	TempSounds[TempSoundIndex] = TempSound
	
	TempSoundIndex=(TempSoundIndex+1) Mod 10
	
	Return TempSound
End Function

Function LoadEventSound(e.Events,file$,num%=0)
	
	If num=0 Then
		If e\Sound<>0 Then FreeSound_Strict e\Sound : e\Sound=0
		e\Sound=LoadSound_Strict(file)
		Return e\Sound
	Else If num=1 Then
		If e\Sound2<>0 Then FreeSound_Strict e\Sound2 : e\Sound2=0
		e\Sound2=LoadSound_Strict(file)
		Return e\Sound2
	EndIf
End Function

Function UpdateMusic()
	
	;TODO: Not hack jorge.
	If ConsoleFlush Then
		If Not ChannelPlaying(MusicCHN) Then MusicCHN = PlaySound(ConsoleMusFlush)
	Else
		If FPSfactor > 0 Or OptionsMenu = 2 Then 
			If NowPlaying <> ShouldPlay Then ; playing the wrong clip, fade out
				CurrMusicVolume# = Max(CurrMusicVolume - (FPSfactor / 250.0), 0)
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
				MusicCHN = PlaySound_Strict(Music(NowPlaying))
			Else
				If (Not ChannelPlaying(MusicCHN)) Then MusicCHN = PlaySound_Strict(Music(NowPlaying))
			EndIf
		EndIf
		
		ChannelVolume(MusicCHN, CurrMusicVolume)
	EndIf
	
End Function 

Function PauseSounds()
	For e.events = Each Events
		If e\soundchn <> 0 Then
			If ChannelPlaying(e\soundchn) Then PauseChannel(e\soundchn)
		EndIf
		If e\soundchn2 <> 0 Then
			If ChannelPlaying(e\soundchn2) Then PauseChannel(e\soundchn2)
		EndIf		
	Next
	
	For n.npcs = Each NPCs
		If n\soundchn <> 0 Then
			If ChannelPlaying(n\soundchn) Then PauseChannel(n\soundchn)
		EndIf
	Next	
	
	For d.doors = Each Doors
		If d\soundchn <> 0 Then
			If ChannelPlaying(d\soundchn) Then PauseChannel(d\soundchn)
		EndIf
	Next	
	
	If AmbientSFXCHN <> 0 Then
		If ChannelPlaying(AmbientSFXCHN) Then PauseChannel(AmbientSFXCHN)
	EndIf
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then PauseChannel(BreathCHN)
	EndIf
End Function

Function ResumeSounds()
	For e.events = Each Events
		If e\soundchn <> 0 Then
			If ChannelPlaying(e\soundchn) Then ResumeChannel(e\soundchn)
		EndIf
		If e\soundchn2 <> 0 Then
			If ChannelPlaying(e\soundchn2) Then ResumeChannel(e\soundchn2)
		EndIf	
	Next
	
	For n.npcs = Each NPCs
		If n\soundchn <> 0 Then
			If ChannelPlaying(n\soundchn) Then ResumeChannel(n\soundchn)
		EndIf
	Next	
	
	For d.doors = Each Doors
		If d\soundchn <> 0 Then
			If ChannelPlaying(d\soundchn) Then ResumeChannel(d\soundchn)
		EndIf
	Next	
	
	If AmbientSFXCHN <> 0 Then
		If ChannelPlaying(AmbientSFXCHN) Then ResumeChannel(AmbientSFXCHN)
	EndIf	
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then ResumeChannel(BreathCHN)
	EndIf
End Function

Function GetStepSound(entity%)
    Local picker%,brush%,texture%,name$
    Local mat.Materials
    
    picker = LinePick(EntityX(entity),EntityY(entity),EntityZ(entity),0,-1,0)
    If picker <> 0 Then
        If GetEntityType(picker) <> HIT_MAP Then Return 0
        brush = GetSurfaceBrush(GetSurface(picker,CountSurfaces(picker)))
        If brush <> 0 Then
            texture = GetBrushTexture(brush,2)
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

Function UpdateSoundOrigin2(Chn%, cam%, entity%, range# = 10, volume# = 1.0)
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
			
			ChannelVolume(Chn, volume# * (1 - dist#)*userOptions\soundVolume)
			ChannelPan(Chn, panvalue)
		EndIf
	Else
		If Chn <> 0 Then
			ChannelVolume (Chn, 0)
		EndIf 
	EndIf
End Function

Const RADIO_CHANNEL_COUNT% = 5
Type Radio
	Field currChn%

	;How long each channel has been played for.
	Field airTime#[RADIO_CHANNEL_COUNT]

	Field channels%[RADIO_CHANNEL_COUNT]

	;Sounds
	Field sndStatic%
End Type
Global radio.Radio = Null

Function CreateRadio.Radio()
	Local rad.Radio = New Radio

	Return rad
End Function

Function ChangeRadioChannel(newChn%)
	If (IsChannelPlaying(radio\channels[radio\currChn])) Then
		PauseChannel(radio\channels[radio\currChn])
	EndIf

	If (Not IsChannelPlaying(radio\channels[newChn])) Then
		radio\channels[newChn] = PlaySound(radio\sndStatic)
	Else
		ResumeChannel(radio\channels[newChn])
	EndIf

	radio\currChn = newChn
End Function


Function UpdateRadio(i.Item)
	Select radio\currChn
		Case 0 ;-nothing
			;TODO: something?
		Case 1 ;-alarms and alert messages

		Case 2 ;-scp on-site radio

		Case 3 ;-MTF broadcasts

		Case 4 ;-idfk
	End Select

	radio\airTime[radio\currChn] = radio\airTime[radio\currChn] + (timing\tickDuration/70)
End Function
Global SelectedLoadingScreen.LoadingScreens, LoadingScreenAmount%, LoadingScreenText%
Global LoadingBack%

;TODO: Redo loading screens to provide tips rather than SCP article info.
Type LoadingScreens
	Field imgpath$
	Field img%
	Field id%
	Field title$
	Field alignx%, aligny%
	Field disablebackground%
	Field txt$[5], txtamount%
End Type

Function InitLoadingScreens(file$)
	Local TemporaryString$, i%
	Local ls.LoadingScreens

	Local f% = OpenFile(file)

	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)

			ls.LoadingScreens = New LoadingScreens
			LoadingScreenAmount=LoadingScreenAmount+1
			ls\id = LoadingScreenAmount

			ls\title = TemporaryString
			ls\imgpath = GetINIString(file, TemporaryString, "image path")

			For i = 0 To 4
				ls\txt[i] = GetINIString(file, TemporaryString, "text"+(i+1))
				If ls\txt[i]<> "" Then ls\txtamount=ls\txtamount+1
			Next

			ls\disablebackground = GetINIInt(file, TemporaryString, "disablebackground")

			Select Lower(GetINIString(file, TemporaryString, "align x"))
				Case "left"
					ls\alignx = -1
				Case "middle", "center"
					ls\alignx = 0
				Case "right"
					ls\alignx = 1
			End Select

			Select Lower(GetINIString(file, TemporaryString, "align y"))
				Case "top", "up"
					ls\aligny = -1
				Case "middle", "center"
					ls\aligny = 0
				Case "bottom", "down"
					ls\aligny = 1
			End Select

		EndIf
	Wend

	CloseFile f
End Function



Function DrawLoading(percent%, shortloading%=False)
	Local x%, y%
	Local i%
	Local strtemp$
	Local temp%
	Local ls.LoadingScreens
	Local width%, height%
	
	If percent = 0 Then
		LoadingScreenText=0
		
		temp = Rand(1,LoadingScreenAmount)
		For ls = Each LoadingScreens
			If ls\id = temp Then
				If ls\img=0 Then ls\img = LoadImage("Loadingscreens/"+ls\imgpath)
				SelectedLoadingScreen = ls
				Exit
			EndIf
		Next
	EndIf

	Local firstloop% = True
	Repeat

		;Color 0,0,0
		;Rect 0,0,userOptions\screenWidth,userOptions\screenHeight,True
		;Color 255, 255, 255
		ClsColor 0,0,0
		Cls

		;Cls(True,False)

		If percent > 24 Then
			UpdateMusic()
		EndIf

		If shortloading = False Then
			If percent > (100.0 / SelectedLoadingScreen\txtamount)*(LoadingScreenText+1) Then
				LoadingScreenText=LoadingScreenText+1
			EndIf
		EndIf

		If (Not SelectedLoadingScreen\disablebackground) Then
			DrawImage LoadingBack, userOptions\screenWidth/2 - ImageWidth(LoadingBack)/2, userOptions\screenHeight/2 - ImageHeight(LoadingBack)/2
		EndIf

		If SelectedLoadingScreen\alignx = 0 Then
			x = userOptions\screenWidth/2 - ImageWidth(SelectedLoadingScreen\img)/2
		ElseIf  SelectedLoadingScreen\alignx = 1 Then
			x = userOptions\screenWidth - ImageWidth(SelectedLoadingScreen\img)
		Else
			x = 0
		EndIf

		If SelectedLoadingScreen\aligny = 0 Then
			y = userOptions\screenHeight/2 - ImageHeight(SelectedLoadingScreen\img)/2
		ElseIf  SelectedLoadingScreen\aligny = 1 Then
			y = userOptions\screenHeight - ImageHeight(SelectedLoadingScreen\img)
		Else
			y = 0
		EndIf

		DrawImage SelectedLoadingScreen\img, x, y

		width = 300 : height = 20
		x% = userOptions\screenWidth / 2 - width / 2
		y% = userOptions\screenHeight / 2 + 30 - 100

		Rect(x, y, width+4, height, False)
		For  i = 1 To Int((width - 2) * (percent / 100.0) / 10)
			DrawImage(uiAssets\blinkBar, x + 3 + 10 * (i - 1), y + 3)
		Next

		If SelectedLoadingScreen\title = "CWM" Then

			If Not shortloading Then
				If firstloop Then
					If percent = 0 Then
						PlaySound2 LoadTempSound("SFX/SCP/990/cwm1.cwm")
					ElseIf percent = 100 Then
						PlaySound2 LoadTempSound("SFX/SCP/990/cwm2.cwm")
					EndIf
				EndIf
			EndIf

			SetFont uiAssets\font[1]
			temp = Rand(2,9)
			For i = 0 To temp
				strtemp$ = strtemp + Chr(Rand(48,122))
			Next
			Text(userOptions\screenWidth / 2, userOptions\screenHeight / 2 + 80, strtemp, True, True)

			If percent = 0 Then
				If Rand(5)=1 Then
					Select Rand(2)
						Case 1
							SelectedLoadingScreen\txt[0] = "It will happen on " + CurrentDate() + "."
						Case 2
							SelectedLoadingScreen\txt[0] = CurrentTime()
					End Select
				Else
					Select Rand(13)
						Case 1
							SelectedLoadingScreen\txt[0] = "A very fine radio might prove to be useful."
						Case 2
							SelectedLoadingScreen\txt[0] = "ThIS PLaCE WiLL BUrN"
						Case 3
							SelectedLoadingScreen\txt[0] = "You cannot control it."
						Case 4
							SelectedLoadingScreen\txt[0] = "eof9nsd3jue4iwe1fgj"
						Case 5
							SelectedLoadingScreen\txt[0] = "YOU NEED TO TRUST IT"
						Case 6
							SelectedLoadingScreen\txt[0] = "Look my friend in the eye when you address him, isn't that the way of the gentleman?"
						Case 7
							SelectedLoadingScreen\txt[0] = "???____??_???__????n?"
						Case 8, 9
							SelectedLoadingScreen\txt[0] = "Jorge has been expecting you."
						Case 10
							SelectedLoadingScreen\txt[0] = "???????????"
						Case 11
							SelectedLoadingScreen\txt[0] = "Make her a member of the midnight crew."
						Case 12
							SelectedLoadingScreen\txt[0] = "oncluded that coming here was a mistake. We have to turn back."
						Case 13
							SelectedLoadingScreen\txt[0] = "This alloy contains the essence of my life."
					End Select
				EndIf
			EndIf

			strtemp$ = SelectedLoadingScreen\txt[0]
			temp = Int(Len(SelectedLoadingScreen\txt[0])-Rand(5))
			For i = 0 To Rand(10,15);temp
				strtemp$ = Replace(SelectedLoadingScreen\txt[0],Mid(SelectedLoadingScreen\txt[0],Rand(1,Len(strtemp)-1),1),Chr(Rand(130,250)))
			Next
			SetFont uiAssets\font[0]
			RowText(strtemp, userOptions\screenWidth / 2-200, userOptions\screenHeight / 2 +120,400,300,True)
		Else

			Color 0,0,0
			SetFont uiAssets\font[1]
			Text(userOptions\screenWidth / 2 + 1, userOptions\screenHeight / 2 + 80 + 1, SelectedLoadingScreen\title, True, True)
			SetFont uiAssets\font[0]
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], userOptions\screenWidth / 2-200+1, userOptions\screenHeight / 2 +120+1,400,300,True)

			Color 255,255,255
			SetFont uiAssets\font[1]
			Text(userOptions\screenWidth / 2, userOptions\screenHeight / 2 +80, SelectedLoadingScreen\title, True, True)
			SetFont uiAssets\font[0]
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], userOptions\screenWidth / 2-200, userOptions\screenHeight / 2 +120,400,300,True)

		EndIf

		Color 0,0,0
		Text(userOptions\screenWidth / 2 + 1, userOptions\screenHeight / 2 - 100 + 1, "LOADING - " + percent + " %", True, True)
		Color 255,255,255
		Text(userOptions\screenWidth / 2, userOptions\screenHeight / 2 - 100, "LOADING - " + percent + " %", True, True)

		If percent = 100 Then
			;If firstloop And SelectedLoadingScreen\title <> "CWM" Then PlaySound2 HorrorSFX(8) ;TODO: fix
			timing\prevTime = MilliSecs()
			Text(userOptions\screenWidth / 2, userOptions\screenHeight - 50, "PRESS ANY KEY TO CONTINUE", True, True)
		Else
			FlushKeys()
			FlushMouse()
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

		Flip False

		firstloop = False
		If (percent <> 100) Then
			Exit
		EndIf

	Until (GetKey()<>0 Or MouseHit(1))

	If (percent >= 100) Then
		RestoreDefaultMusic()
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
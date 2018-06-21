;menus, GUI ---------------------------------------------------------------------------------------------------------
Global MainMenuOpen.MarkedForRemoval, MenuOpen.MarkedForRemoval, InvOpen.MarkedForRemoval ;TODO: REMOVE
;Global OtherOpen.Items = Null
Global SelectedEnding$ ;TODO: REMOVE
Global EndingScreen.MarkedForRemoval, EndingTimer.MarkedForRemoval

Const GAMESTATE_MAINMENU% = 0
Const GAMESTATE_PLAYING% = 1
Const GAMESTATE_PAUSED% = 2
Const GAMESTATE_CONSOLE% = 3
Const GAMESTATE_INVENTORY% = 4
Const GAMESTATE_SCP294% = 5
Const GAMESTATE_ENDING% = 6
Const GAMESTATE_LAUNCHER% = 7

;Paused substates
Const GAMESUBSTATE_PAUSED_MAIN% = 0
Const GAMESUBSTATE_PAUSED_DEAD% = 1
Const GAMESUBSTATE_PAUSED_OPTIONS% = 2

Global CurrGameState% = GAMESTATE_MAINMENU
Global CurrGameSubstate% = GAMESUBSTATE_MAINMENU_MAIN
Global CurrGameStatePage% = 0

Function IsPaused()
	Return CurrGameState <> GAMESTATE_PLAYING
End Function

Global MsgTimer#, Msg$, DeathMSG$

Global AccessCode%, KeypadInput$, KeypadTimer#, KeypadMSG$

Global DrawHandIcon%

Global MenuScale#

;TODO: Assets.bb
Dim DrawArrowIcon%(4)

Global QuickLoadIcon.MarkedForRemoval

Global MenuBack.MarkedForRemoval
Global MenuText.MarkedForRemoval
Global Menu173.MarkedForRemoval
Global MenuWhite.MarkedForRemoval
Global MenuBlack.MarkedForRemoval

Global RandomSeed$

Dim MenuBlinkTimer%(2), MenuBlinkDuration%(2)
MenuBlinkTimer%(0) = 1
MenuBlinkTimer%(1) = 1

Global MenuStr$, MenuStrX%, MenuStrY%

Global MainMenuTab.MarkedForRemoval

Global SelectedInputBox%

Global SavePath$ = "Saves/"
Global SaveMSG$

;nykyisen tallennuksen nimi ja samalla missä kansiossa tallennustiedosto sijaitsee saves-kansiossa
Global CurrSave$

Global SaveGameAmount%
Dim SaveGames$(SaveGameAmount+1) 
Dim SaveGameTime$(SaveGameAmount + 1)
Dim SaveGameDate$(SaveGameAmount + 1)

Const MAXSAVEDMAPS = 20
Dim SavedMaps$(MAXSAVEDMAPS)
Global SelectedMap$

Include "SourceCode/Menus/Launcher.bb"
Include "SourceCode/Menus/MainMenu.bb"
Include "SourceCode/Menus/LoadingScreen.bb"

Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	
	Local x2% = x
	While x2 < x+width
		Local y2% = y
		While y2 < y+height
			If x2 + srcwidth > x + width Then srcwidth = srcwidth - Max((x2 + srcwidth) - (x + width), 1)
			If y2 + srcheight > y + height Then srcheight = srcheight - Max((y2 + srcheight) - (y + height), 1)
			DrawImageRect(img, x2, y2, srcX, srcY, srcwidth, srcheight)
			y2 = y2 + srcheight
		Wend
		x2 = x2 + srcwidth
	Wend
	
End Function



Function rInput$(aString$)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If value = 8 Then
		value = 0
		If length > 0 Then aString$ = Left(aString, length - 1)
	EndIf
	
	If value = 13 Or value = 0 Then
		Return aString$
	ElseIf value > 0 And value < 7 Or value > 26 And value < 32 Or value = 9 Then
		Return aString$
	Else
		aString$ = aString$ + Chr(value)
		Return aString$
	EndIf
End Function



Function MouseOn%(x%, y%, width%, height%)
	If MouseX() > x And MouseX() < x + width Then
		If MouseY() > y And MouseY() < y + height Then
			Return True
		EndIf
	EndIf
	Return False
End Function

Function UpdateInputBox$(x%, y%, width%, height%, Txt$, ID% = 0)
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		MouseOnBox = True
		If MouseHit1 Then SelectedInputBox = ID : FlushKeys
	EndIf
	
	If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt)
	EndIf
	
	Return Txt
End Function

Function DrawInputBox$(x%, y%, width%, height%, Txt$, ID% = 0)
	;TextBox(x,y,width,height,Txt$)
	Color (255, 255, 255)
	DrawTiledImageRect(uiAssets\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If SelectedInputBox = ID Then
		If (TimeInPosMilliSecs() Mod 800) < 400 Then Rect (x + width / 2 + StringWidth(Txt) / 2 + 2, y + height / 2 - 5, 2, 12)
	EndIf	
	
	Text(x + width / 2, y + height / 2, Txt, True, True)
End Function

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0)
	Color 255, 255, 255
	DrawTiledImageRect(uiAssets\tileWhite, xoffset, (y Mod 256), 512, 512, x, y, width, height)
	
	DrawTiledImageRect(uiAssets\tileBlack, yoffset, (y Mod 256), 512, 512, x+3*MenuScale, y+3*MenuScale, width-6*MenuScale, height-6*MenuScale)	
End Function

Function DrawUIButton(x%, y%, width%, height%, txt$, bigfont% = True)
	
	DrawFrame (x, y, width, height)
	If MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		Rect(x + 4, y + 4, width - 8, height - 8)	
	Else
		Color(0, 0, 0)
	EndIf
	
	Color (255, 255, 255)
	If bigfont Then SetFont uiAssets\font[1] Else SetFont uiAssets\font[0]
	Text(x + width / 2, y + height / 2, txt, True, True)
End Function

Function UpdateUIButton%(x%, y%, width%, height, txt$="", waitForMouseUp%=False)
	Local clicked% = False
	
	If MouseOn(x, y, width, height) Then
		If (MouseHit1 And (Not waitForMouseUp)) Or (MouseUp1 And waitForMouseUp) Then 
			clicked = True
			PlaySound_SM(sndManager\button)
		EndIf
	EndIf
	
	Return clicked
End Function

Function DrawUITick(x%, y%, selected%, locked% = False)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Color (255, 255, 255)
	DrawTiledImageRect(uiAssets\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		Color(50, 50, 50)
	Else
		Color(0, 0, 0)		
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	
	If selected Then
		If Highlight Then
			Color 255,255,255
		Else
			Color 200,200,200
		EndIf
		DrawTiledImageRect(uiAssets\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x + 4, y + 4, width - 8, height - 8)
		;Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	Color 255, 255, 255
End Function

Function UpdateUITick%(x%, y%, selected%, locked% = False)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		If MouseHit1 Then
			selected = (Not selected)
			PlaySound_SM(sndManager\button)
		EndIf
	EndIf
	
	Return selected
End Function

Function UpdateSlideBar#(x%, y%, width%, value#)
	If MouseDown1 Then
		If MouseX() >= x And MouseX() <= x + width + 14 And MouseY() >= y And MouseY() <= y + 20 Then
			value = Min(Max((MouseX() - x) * 100 / width, 0), 100)
		EndIf
	EndIf
	
	Return value
End Function

Function DrawSlideBar(x%, y%, width%, value#)
	Color 255,255,255
	Rect(x, y, width + 14, 20,False)
	
	DrawImage(uiAssets\blinkBar, x + width * value / 100.0 +3, y+3)
	
	Color 170,170,170 
	Text (x - 50 * MenuScale, y + 4*MenuScale, "LOW")					
	Text (x + width + 38 * MenuScale, y+4*MenuScale, "HIGH")	
End Function




Function RowText(A$, X, Y, W, H, align% = 0, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$)
		Local temp$ = Left(A$, space)
		Local trimmed$ = Trim(temp) ;we might ignore a final space 
		Local extra = 0 ;we haven't ignored it yet
		;ignore final space If doing so would make a word fit at End of Line:
		If (StringWidth (b$ + temp$) > W) And (StringWidth (b$ + trimmed$) <= W) Then
			temp = trimmed
			extra = 1
		EndIf
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			If align Then
				Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b)
			Else
				Text(X, LinesShown * Height + Y, b)
			EndIf			
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			b$ = b$ + temp$
			A$ = Right(A$, Len(A$) - (Len(temp$) + extra))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	If (b$ <> "") And((LinesShown + 1) <= H) Then
		If align Then
			Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		Else
			Text(X, LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		EndIf
	EndIf
End Function

Function LimitText%(txt$, x%, y%, width%, usingAA%=True)
	Local TextLength%
	Local UnFitting%
	Local LetterWidth%
	If usingAA Then
		If txt = "" Or width = 0 Then Return 0
		TextLength = StringWidth(txt)
		UnFitting = TextLength - width
		If UnFitting <= 0 Then ;mahtuu
			Text(x, y, txt)
		Else ;ei mahdu
			LetterWidth = TextLength / Len(txt)
			
			Text(x, y, Left(txt, Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
		EndIf
	Else
		If txt = "" Or width = 0 Then Return 0
		TextLength = StringWidth(txt)
		UnFitting = TextLength - width
		If UnFitting <= 0 Then ;mahtuu
			Text(x, y, txt)
		Else ;ei mahdu
			LetterWidth = TextLength / Len(txt)
			
			Text(x, y, Left(txt, Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
		EndIf
	EndIf
End Function

Function DrawTooltip(message$)
	Local scale# = userOptions\screenHeight/768.0
	
	Local width = (StringWidth(message$))+20*MenuScale
	
	Color 25,25,25
	Rect(MouseX()+20,MouseY(),width,19*scale,True)
	Color 150,150,150
	Rect(MouseX()+20,MouseY(),width,19*scale,False)
	SetFont uiAssets\font[0]
	Text(MouseX()+(20*MenuScale)+(width/2),MouseY()+(12*MenuScale), message$, True, True)
End Function

Function ShowPointer2()
	ShowPointer()
	If (userOptions\fullscreen) Then
		DrawImage(uiAssets\cursorIMG, MouseX(), MouseY())
	EndIf
End Function

Global QuickLoadPercent.MarkedForRemoval
Global QuickLoadPercent_DisplayTimer.MarkedForRemoval


;~IDEal Editor Parameters:
;~C#Blitz3D
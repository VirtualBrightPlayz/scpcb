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

;Main menu substates
Const GAMESUBSTATE_MAINMENU_MAIN% = 0
Const GAMESUBSTATE_MAINMENU_NEWGAME% = 1
Const GAMESUBSTATE_MAINMENU_CUSTOMMAP% = 2
Const GAMESUBSTATE_MAINMENU_LOADGAME% = 3
Const GAMESUBSTATE_MAINMENU_OPTIONS% = 4

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

Type UITextures
	Field back%
	Field scpText%
	Field tileWhite%
	Field tileBlack%
	Field scp173%
	Field arrow%[4]
End Type

Global uiTextures.UITextures

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

Global MainMenuTab%

Global SelectedInputBox%

Global SavePath$ = "Saves\"
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

LoadSaveGames()

Function InitializeUITextures()
	uiTextures = New UITextures
	
	uiTextures\back = LoadImage("GFX\menu\back.jpg")
	uiTextures\scpText = LoadImage("GFX\menu\scptext.jpg")
	uiTextures\scp173 = LoadImage("GFX\menu\173back.jpg")
	uiTextures\tileWhite = LoadImage("GFX\menu\menuwhite.jpg")
	uiTextures\tileBlack = LoadImage("GFX\menu\menublack.jpg")
	MaskImage uiTextures\tileBlack, 255,255,0
	
	ResizeImage(uiTextures\back, ImageWidth(uiTextures\back) * MenuScale, ImageHeight(uiTextures\back) * MenuScale)
	ResizeImage(uiTextures\scpText, ImageWidth(uiTextures\scpText) * MenuScale, ImageHeight(uiTextures\scpText) * MenuScale)
	ResizeImage(uiTextures\scp173, ImageWidth(uiTextures\scp173) * MenuScale, ImageHeight(uiTextures\scp173) * MenuScale)
	
	For i = 0 To 3
		uiTextures\arrow[i] = LoadImage("GFX\menu\arrow.png")
		RotateImage(uiTextures\arrow[i], 90 * i)
		HandleImage(uiTextures\arrow[i], 0, 0)
	Next
End Function

Function UpdateMainMenu()
	Local x%, y%, width%, height%, temp.MarkedForRemoval
	
	Local mouseHitButton%
	If MainMenuTab = 0 Then
		For i% = 0 To 3
			mouseHitButton = False
			x = 159 * MenuScale
			y = (286 + 100 * i) * MenuScale
			
			width = 400 * MenuScale
			height = 70 * MenuScale
			
			mouseHitButton = UpdateUIButton(x, y, width, height);(MouseHit1 And MouseOn(x, y, width, height))
			
			Select i
				Case 0 ;new game
					RandomSeed = ""
					If mouseHitButton Then 
						If Rand(15)=1 Then 
							Select Rand(13)
								Case 1 
									RandomSeed = "NIL"
								Case 2
									RandomSeed = "NO"
								Case 3
									RandomSeed = "d9341"
								Case 4
									RandomSeed = "5CP_I73"
								Case 5
									RandomSeed = "DONTBLINK"
								Case 6
									RandomSeed = "CRUNCH"
								Case 7
									RandomSeed = "die"
								Case 8
									RandomSeed = "HTAED"
								Case 9
									RandomSeed = "rustledjim"
								Case 10
									RandomSeed = "larry"
								Case 11
									RandomSeed = "JORGE"
								Case 12
									RandomSeed = "dirtymetal"
								Case 13
									RandomSeed = "whatpumpkin"
							End Select
						Else
							n = Rand(4,8)
							For i = 1 To n
								If Rand(3)=1 Then
									RandomSeed = RandomSeed + Rand(0,9)
								Else
									RandomSeed = RandomSeed + Chr(Rand(97,122))
								EndIf
							Next							
						EndIf
						
						;RandomSeed = MilliSecs()
						MainMenuTab = 1
					EndIf
				Case 1 ;load game
					If mouseHitButton Then
						LoadSaveGames()
						MainMenuTab = 2
					EndIf
				Case 2 ;options
					If mouseHitButton Then MainMenuTab = 3
				Case 3 ;quit
					If mouseHitButton Then
						;DeInitExt
						End
					EndIf
			End Select
		Next	
		
	Else
		
		x = 159 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		If UpdateUIButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, "BACK") Then 
			Select MainMenuTab
				Case 1
					PutINIValue(OptionFile, "general", "intro enabled", userOptions\introEnabled)
					MainMenuTab = 0
				Case 3,5,6,7 ;save the options
					SaveOptionsINI()
					
					MainMenuTab = 0
				Case 4 ;move back to the "new game" tab
					MainMenuTab = 1
					MouseHit1 = False
				Default
					MainMenuTab = 0
			End Select
		EndIf
		
		Select MainMenuTab
			Case 1 ; New game
				;[Block]
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				CurrSave = UpdateInputBox(x + 150 * MenuScale, y + 15 * MenuScale, 200 * MenuScale, 30 * MenuScale, CurrSave, 1)
				CurrSave = Left(CurrSave, 15)
				
				If SelectedMap = "" Then
					RandomSeed = Left(UpdateInputBox(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale, RandomSeed, 3),15)	
				Else
					If UpdateUIButton(x+370*MenuScale, y+55*MenuScale, 120*MenuScale, 30*MenuScale, "Deselect") Then
						SelectedMap=""
					EndIf
				EndIf	
				
				userOptions\introEnabled = UpdateUITick(x + 280 * MenuScale, y + 110 * MenuScale, userOptions\introEnabled)	
				
				For i = SAFE To CUSTOM
					If UpdateUITick(x + 20 * MenuScale, y + (180+30*i) * MenuScale, (SelectedDifficulty = difficulties(i))) Then SelectedDifficulty = difficulties(i)
				Next
				
				If SelectedDifficulty\customizable Then
					SelectedDifficulty\permaDeath =  UpdateUITick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\permaDeath))
					
					If UpdateUITick(x + 160 * MenuScale, y + 195 * MenuScale, SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath), SelectedDifficulty\permaDeath) Then 
						SelectedDifficulty\saveType = SAVEANYWHERE
					Else
						SelectedDifficulty\saveType = SAVEONSCREENS
					EndIf
					
					SelectedDifficulty\aggressiveNPCs =  UpdateUITick(x + 160 * MenuScale, y + 225 * MenuScale, SelectedDifficulty\aggressiveNPCs)
					
					;Other factor's difficulty
					If MouseHit1 Then
						If MouseOn(x + 155 * MenuScale, y+251*MenuScale, ImageWidth(uiTextures\arrow[1]), ImageHeight(uiTextures\arrow[1])) Then
							If SelectedDifficulty\otherFactors < HARD
								SelectedDifficulty\otherFactors = SelectedDifficulty\otherFactors + 1
							Else
								SelectedDifficulty\otherFactors = EASY
							EndIf
							PlaySound(ButtonSFX)
						EndIf
					EndIf			
				EndIf
				
				If UpdateUIButton(x, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "Load map") Then
					MainMenuTab = 4
					LoadSavedMaps()
				EndIf
				
				If UpdateUIButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "START") Then
					If CurrSave <> "" Then
						If RandomSeed = "" Then
							RandomSeed = Abs(MilliSecs())
						EndIf
						Local strtemp$ = ""
						SeedRnd SeedStringToInt(RandomSeed)
						
						Local SameFound% = False
						For  i% = 1 To SaveGameAmount
							If SaveGames(i - 1) = CurrSave Then SameFound=SameFound+1
						Next
						
						If SameFound > 0 Then CurrSave = CurrSave + " (" + (SameFound + 1) + ")"
						
						LoadEntities()
						InitNewGame()
						CurrGameState = GAMESTATE_PLAYING
						FlushKeys()
						FlushMouse()
						
						PutINIValue(OptionFile, "general", "intro enabled", userOptions\introEnabled)
					EndIf
					
				EndIf
				
				;[End Block]
			Case 2 ;load game
				;[Block]
				
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 300 * MenuScale
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If SaveGameAmount <>0 Then
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i% = 1 To SaveGameAmount
						If SaveMSG = "" Then
							If UpdateUIButton(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Load") Then
								LoadEntities()
								LoadGame(SavePath + SaveGames(i - 1) + "\")
								CurrSave = SaveGames(i - 1)
								InitLoadGame()
								CurrGameState = GAMESTATE_PLAYING
							EndIf
							
							If UpdateUIButton(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Delete") Then
								SaveMSG = SaveGames(i - 1)
								DebugLog SaveMSG
								Exit
							EndIf
						EndIf
						
						y = y + 80 * MenuScale
						
					Next
					
					If SaveMSG <> ""
						x = userOptions\screenWidth / 2
						y = userOptions\screenHeight / 2
						If UpdateUIButton(x + 250 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Yes") Then
							DeleteFile(CurrentDir() + SavePath + SaveMSG + "\save.txt")
							DeleteDir(CurrentDir() + SavePath + SaveMSG)
							SaveMSG = ""
							LoadSaveGames()
						EndIf
						If UpdateUIButton(x + 50 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, "No") Then
							SaveMSG = ""
						EndIf
					EndIf
				EndIf
				
				;[End Block]
			Case 3,5,6,7 ;options
				;[Block]
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				If UpdateUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False) Then MainMenuTab = 3
				If UpdateUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False) Then MainMenuTab = 5
				If UpdateUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False) Then MainMenuTab = 6
				If UpdateUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False) Then MainMenuTab = 7
				y = y + 70 * MenuScale
				
				If MainMenuTab = 3 ;Graphics
					;[Block]
					height = 300 * MenuScale
					
					y=y+20*MenuScale
					
					userOptions\hudEnabled = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)	
					
					y=y+30*MenuScale
					
					UpdateUITick(x + 310 * MenuScale, y + MenuScale, False, True)
					
					y=y+30*MenuScale
					
					userOptions\vsync = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
					
					y=y+40*MenuScale
					
					userOptions\screenGamma = (UpdateSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.0)/50.0)
					
					y = y + 50*MenuScale
					
					y=y+30*MenuScale
					;[End Block]
				ElseIf MainMenuTab = 5 ;Audio
					;[Block]
					height = 220 * MenuScale
					
					y = y + 20*MenuScale
					
					userOptions\musicVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.0)/100.0)
					
					y = y + 40*MenuScale
					
					userOptions\SoundVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\SoundVolume*100.0)/100.0)
					;[End Block]
				ElseIf MainMenuTab = 6 ;Controls
					;[Block]
					height = 230 * MenuScale
					
					y = y + 20*MenuScale
					
					userOptions\mouseSensitivity = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.0)/100.0)-0.5
					
					y = y + 40*MenuScale
					
					userOptions\invertMouseY = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY)
					
					y = y + 30*MenuScale
					
					y = y + 10*MenuScale
					
					UpdateInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\up,210)),5)		
					
					UpdateInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\lft,210)),3)	
					
					UpdateInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\down,210)),6)				
					
					UpdateInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\rght,210)),4)	
					
					UpdateInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\save,210)),11)
					
					
					UpdateInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\blink,210)),7)				
					
					UpdateInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\sprint,210)),8)
					
					UpdateInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\inv,210)),9)
					
					UpdateInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\crouch,210)),10)	
					
					UpdateInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\console,210)),12)
					
					For i = 0 To 227
						If KeyHit(i) Then key = i : Exit
					Next
					If key<>0 Then
						Select SelectedInputBox
							Case 3
								keyBinds\lft = key
							Case 4
								keyBinds\rght = key
							Case 5
								keyBinds\up = key
							Case 6
								keyBinds\down = key
							Case 7
								keyBinds\blink = key
							Case 8
								keyBinds\sprint = key
							Case 9
								keyBinds\inv = key
							Case 10
								keyBinds\crouch = key
							Case 11
								keyBinds\save = key
							Case 12
								keyBinds\console = key
						End Select
						SelectedInputBox = 0
					EndIf
					;[End Block]
				ElseIf MainMenuTab = 7 ;Advanced
					;[Block]
					height = 310 * MenuScale
					
					y = y + 20*MenuScale
					
					y = y + 30*MenuScale
					
					userOptions\consoleOpenOnError = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
					
					y = y + 50*MenuScale
					
					ShowFPS% = UpdateUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
					
					y = y + 30*MenuScale
					
					If UpdateUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.0) Then
						CurrFrameLimit# = (UpdateSlideBar(x + 150*MenuScale, y+30*MenuScale, 100*MenuScale, CurrFrameLimit#*50.0)/50.0)
						CurrFrameLimit = Max(CurrFrameLimit, 0.1)
						userOptions\framelimit = CurrFrameLimit#*100.0
					Else
						CurrFrameLimit# = 0.0
						userOptions\framelimit = 0
					EndIf
					
					y = y + 80*MenuScale
					
					;[End Block]
				EndIf
				;[End Block]
			Case 4 ; load map
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				If SavedMaps(0)<>"" Then 
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i = 0 To MAXSAVEDMAPS-1
						If SavedMaps(i)<>"" Then
							
							If UpdateUIButton(x + 20 * MenuScale, y + 20 * MenuScale, 170, 25, SavedMaps(i)) Then
								SelectedMap=SavedMaps(i)
								MainMenuTab = 1
							EndIf
							
							y=y+30*MenuScale
							If y > (286+230) * MenuScale Then
								y = 286*MenuScale + 2*MenuScale
								x = x+175*MenuScale
							EndIf
						Else
							Exit
						EndIf
					Next
				EndIf
				
				;[End Block]
		End Select
		
	EndIf
End Function

Function DrawMainMenu()
	Local x%, y%, width%, height%, temp.MarkedForRemoval
	
	Color 0,0,0
	Rect 0,0,userOptions\screenWidth,userOptions\screenHeight,True
	
	ShowPointer()
	
	DrawImage(uiTextures\back, 0, 0)
	
	If (TimeInPosMilliSecs() Mod MenuBlinkTimer(0)) >= Rand(MenuBlinkDuration(0)) Then
		DrawImage(uiTextures\scp173, userOptions\screenWidth - ImageWidth(uiTextures\scp173), userOptions\screenHeight - ImageHeight(uiTextures\scp173))
	EndIf
	
	If Rand(300) = 1 Then
		MenuBlinkTimer(0) = Rand(4000, 8000)
		MenuBlinkDuration(0) = Rand(200, 500)
	End If
	
	SetFont Font1
	
	MenuBlinkTimer(1)=MenuBlinkTimer(1)-timing\tickDuration
	If MenuBlinkTimer(1) < MenuBlinkDuration(1) Then
		Color(50, 50, 50)
		Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
		If MenuBlinkTimer(1) < 0 Then
			MenuBlinkTimer(1) = Rand(700, 800)
			MenuBlinkDuration(1) = Rand(10, 35)
			MenuStrX = Rand(700, 1000) * MenuScale
			MenuStrY = Rand(100, 600) * MenuScale
			
			Select Rand(0, 22)
				Case 0, 2, 3
					MenuStr = "DON'T BLINK"
				Case 4, 5
					MenuStr = "Secure. Contain. Protect."
				Case 6, 7, 8
					MenuStr = "You want happy endings? Fuck you."
				Case 9, 10, 11
					MenuStr = "Sometimes we would have had time to scream."
				Case 12, 19
					MenuStr = "NIL"
				Case 13
					MenuStr = "NO"
				Case 14
					MenuStr = "black white black white black white gray"
				Case 15
					MenuStr = "Stone does not care"
				Case 16
					MenuStr = "9341"
				Case 17
					MenuStr = "It controls the doors"
				Case 18
					MenuStr = "e8m106]af173o+079m895w914"
				Case 20
					MenuStr = "It has taken over everything"
				Case 21
					MenuStr = "The spiral is growing"
				Case 22
					MenuStr = Chr(34)+"Some kind of gestalt effect due to massive reality damage."+Chr(34)
			End Select
		EndIf
	EndIf
	
	SetFont Font2
	
	DrawImage(uiTextures\scpText, userOptions\screenWidth / 2 - ImageWidth(uiTextures\scpText) / 2, userOptions\screenHeight - 20 * MenuScale - ImageHeight(uiTextures\scpText))
	
	If userOptions\screenWidth > 1240 * MenuScale Then
		DrawTiledImageRect(uiTextures\tileWhite, 0, 5, 512, 7 * MenuScale, 985.0 * MenuScale, 407.0 * MenuScale, (userOptions\screenWidth - 1240 * MenuScale) + 300, 7 * MenuScale)
	EndIf
	
	If MainMenuTab = 0 Then
		For i% = 0 To 3
			x = 159 * MenuScale
			y = (286 + 100 * i) * MenuScale
			
			width = 400 * MenuScale
			height = 70 * MenuScale
			
			Local txt$
			Select i
				Case 0 ;new game
					txt = "NEW GAME"
				Case 1 ;load game
					txt = "LOAD GAME"
				Case 2 ;options
					txt = "OPTIONS"
				Case 3 ;quit
					txt = "QUIT"
			End Select
			
			DrawUIButton(x, y, width, height, txt)
		Next
	Else
		x = 159 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		DrawFrame(x, y, width, height)
		
		DrawUIButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, "BACK", False)
		
		Select MainMenuTab
			Case 1 ; New game
				;[Block]
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "NEW GAME", True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				DrawFrame(x, y, width, height)				
				
				SetFont Font1
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, "Name:")
				DrawInputBox(x + 150 * MenuScale, y + 15 * MenuScale, 200 * MenuScale, 30 * MenuScale, CurrSave, 1)
				
				Color 255,255,255
				If SelectedMap = "" Then
					Text (x + 20 * MenuScale, y + 60 * MenuScale, "Map seed:")
					DrawInputBox(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale, RandomSeed, 3)
				Else
					Text (x + 20 * MenuScale, y + 60 * MenuScale, "Selected map:")
					Color (255, 255, 255)
					Rect(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale)
					Color (0, 0, 0)
					Rect(x+150*MenuScale+2, y+55*MenuScale+2, 200*MenuScale-4, 30*MenuScale-4)
					
					Color (255, 0,0)
					Text(x+150*MenuScale + 100*MenuScale, y+55*MenuScale + 15*MenuScale, SelectedMap, True, True)
					
					DrawUIButton(x+370*MenuScale, y+55*MenuScale, 120*MenuScale, 30*MenuScale, "Deselect", False)
				EndIf	
				
				Text(x + 20 * MenuScale, y + 110 * MenuScale, "Enable intro sequence:")
				DrawUITick(x + 280 * MenuScale, y + 110 * MenuScale, userOptions\introEnabled)	
				
				;Local modeName$, modeDescription$, selectedDescription$
				Text (x + 20 * MenuScale, y + 150 * MenuScale, "Difficulty:")				
				For i = SAFE To CUSTOM
					DrawUITick(x + 20 * MenuScale, y + (180+30*i) * MenuScale, (SelectedDifficulty = difficulties(i)))
					
					Text(x + 60 * MenuScale, y + (180+30*i) * MenuScale, difficulties(i)\name)
				Next
				
				Color(255, 255, 255)
				DrawFrame(x + 150 * MenuScale,y + 155 * MenuScale, 410*MenuScale, 150*MenuScale)
				
				If SelectedDifficulty\customizable Then
					DrawUITick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\permaDeath))
					Text(x + 200 * MenuScale, y + 165 * MenuScale, "Permadeath")
					
					DrawUITick(x + 160 * MenuScale, y + 195 * MenuScale, SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath), SelectedDifficulty\permaDeath)
					
					Text(x + 200 * MenuScale, y + 195 * MenuScale, "Save anywhere")	
					
					DrawUITick(x + 160 * MenuScale, y + 225 * MenuScale, SelectedDifficulty\aggressiveNPCs)
					Text(x + 200 * MenuScale, y + 225 * MenuScale, "Aggressive NPCs")
					
					;Other factor's difficulty
					Color 255,255,255
					DrawImage uiTextures\arrow[1],x + 155 * MenuScale, y+251*MenuScale
					
					Color 255,255,255
					Select SelectedDifficulty\otherFactors
						Case EASY
							Text(x + 200 * MenuScale, y + 255 * MenuScale, "Other difficulty factors: Easy")
						Case NORMAL
							Text(x + 200 * MenuScale, y + 255 * MenuScale, "Other difficulty factors: Normal")
						Case HARD
							Text(x + 200 * MenuScale, y + 255 * MenuScale, "Other difficulty factors: Hard")
					End Select
				Else
					RowText(SelectedDifficulty\description, x+160*MenuScale, y+160*MenuScale, (410-20)*MenuScale, 200)					
				EndIf
				
				DrawUIButton(x, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "Load map", False)
				
				SetFont Font2
				
				DrawUIButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "START", False)
				;[End Block]
			Case 2 ;load game
				;[Block]
				
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 300 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "LOAD GAME", True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				SetFont Font1	
				
				If SaveGameAmount = 0 Then
					Text (x + 20 * MenuScale, y + 20 * MenuScale, "No saved games.")
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i% = 1 To SaveGameAmount
						DrawFrame(x,y,540* MenuScale, 70* MenuScale)
						
						Text(x + 20 * MenuScale, y + 10 * MenuScale, SaveGames(i - 1))
						Text(x + 20 * MenuScale, y + (10+23) * MenuScale, SaveGameTime(i - 1))
						Text(x + 120 * MenuScale, y + (10+23) * MenuScale, SaveGameDate(i - 1))
						
						If SaveMSG = "" Then
							DrawUIButton(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Load", False)
							
							DrawUIButton(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Delete", False)
							
						Else
							DrawFrame(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
							Color(100, 100, 100)
							Text(x + 330 * MenuScale, y + 35 * MenuScale, "Load", True, True)
							
							DrawFrame(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
							Color(100, 100, 100)
							Text(x + 450 * MenuScale, y + 35 * MenuScale, "Delete", True, True)
						EndIf
						
						y = y + 80 * MenuScale
						
					Next
					
					If SaveMSG <> "" Then
						x = userOptions\screenWidth / 2
						y = userOptions\screenHeight / 2
						DrawFrame(x, y, 400 * MenuScale, 200 * MenuScale)
						Text(x + 20 * MenuScale, y + 15 * MenuScale, "Are you sure you want to delete this save?")
						DrawUIButton(x + 250 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, "Yes", False)
						DrawUIButton(x + 50 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, "No", False)
					EndIf
				EndIf
				
				;[End Block]
			Case 3,5,6,7 ;options
				;[Block]
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "OPTIONS", True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				DrawUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False)
				DrawUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False)
				DrawUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False)
				DrawUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False)
				Color 0,255,0
				If MainMenuTab = 3
					Rect x+20*MenuScale,y+15*MenuScale,width/5,height/2,False
				ElseIf MainMenuTab = 5
					Rect x+160*MenuScale,y+15*MenuScale,width/5,height/2,False
				ElseIf MainMenuTab = 6
					Rect x+300*MenuScale,y+15*MenuScale,width/5,height/2,False
				ElseIf MainMenuTab = 7
					Rect x+440*MenuScale,y+15*MenuScale,width/5,height/2,False
				EndIf
				SetFont Font1
				y = y + 70 * MenuScale
				
				If MainMenuTab = 3 ;Graphics
					;[Block]
					height = 300 * MenuScale
					DrawFrame(x, y, width, height)
					
					y=y+20*MenuScale
					
					Color 255,255,255				
					Text(x + 20 * MenuScale, y, "Show HUD:")	
					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)	
					
					y=y+30*MenuScale
					
					Color 100,100,100				
					Text(x + 20 * MenuScale, y, "Enable bump mapping:")	
					DrawUITick(x + 310 * MenuScale, y + MenuScale, False, True)
					If MouseOn(x + 310 * MenuScale, y + MenuScale, 20*MenuScale,20*MenuScale)
						DrawTooltip("Not available in this version")
					EndIf
					
					y=y+30*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "VSync:")
					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
					
					y=y+40*MenuScale
					
					DrawSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.0)
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Screen gamma")
					
					y = y + 50*MenuScale
					
					Color 100,100,100
					Text(x + 20 * MenuScale, y, "Texture quality:")
					DrawImage uiTextures\arrow[1],x + 310 * MenuScale, y-4*MenuScale
					
					Text(x + 340 * MenuScale, y + MenuScale, "DISABLED")
					If MouseOn(x + 310 * MenuScale, y-4*MenuScale, ImageWidth(uiTextures\arrow[1]),ImageHeight(uiTextures\arrow[1]))
						DrawTooltip("Not available in this version")
					EndIf
					
					y=y+30*MenuScale
					;[End Block]
				ElseIf MainMenuTab = 5 ;Audio
					;[Block]
					height = 220 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*MenuScale
					
					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.0)
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Music volume:")
					
					y = y + 40*MenuScale
					
					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\SoundVolume*100.0)
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Sound volume:")
					;[End Block]
				ElseIf MainMenuTab = 6 ;Controls
					;[Block]
					height = 230 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*MenuScale
					
					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.0)
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, "Mouse sensitivity:")
					
					y = y + 40*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, "Invert mouse Y-axis:")
					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY)
					
					y = y + 30*MenuScale
					Text(x + 20 * MenuScale, y, "Control configuration:")
					y = y + 10*MenuScale
					
					Text(x + 20 * MenuScale, y + 20 * MenuScale, "Move Forward")
					DrawInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\up,210)),5)		
					Text(x + 20 * MenuScale, y + 40 * MenuScale, "Strafe Left")
					DrawInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\lft,210)),3)	
					Text(x + 20 * MenuScale, y + 60 * MenuScale, "Move Backward")
					DrawInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\down,210)),6)				
					Text(x + 20 * MenuScale, y + 80 * MenuScale, "Strafe Right")
					DrawInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\rght,210)),4)	
					Text(x + 20 * MenuScale, y + 100 * MenuScale, "Quick Save")
					DrawInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\save,210)),11)
					
					Text(x + 280 * MenuScale, y + 20 * MenuScale, "Manual Blink")
					DrawInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\blink,210)),7)				
					Text(x + 280 * MenuScale, y + 40 * MenuScale, "Sprint")
					DrawInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\sprint,210)),8)
					Text(x + 280 * MenuScale, y + 60 * MenuScale, "Open/Close Inventory")
					DrawInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\inv,210)),9)
					Text(x + 280 * MenuScale, y + 80 * MenuScale, "Crouch")
					DrawInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\crouch,210)),10)	
					Text(x + 280 * MenuScale, y + 100 * MenuScale, "Open/Close Console")
					DrawInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(keyBinds\console,210)),12)
					;[End Block]
				ElseIf MainMenuTab = 7 ;Advanced
					;[Block]
					height = 310 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*MenuScale
					
					Color 255,255,255
					y = y + 30*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Open console on error:")
					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
					
					y = y + 50*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Show FPS:")
					DrawUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
					
					y = y + 30*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, "Framelimit:")
					Color 255,255,255
					DrawUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.0)
					
					y = y + 80*MenuScale
					
					;[End Block]
				EndIf
				;[End Block]
			Case 4 ; load map
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "LOAD MAP", True, True)
				SetFont Font1
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				SetFont Font1
				
				If SavedMaps(0)="" Then 
					Text (x + 20 * MenuScale, y + 20 * MenuScale, "No saved maps. Use the Map Creator to create new maps.")
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i = 0 To MAXSAVEDMAPS-1
						If SavedMaps(i)<>"" Then
							
							DrawUIButton(x + 20 * MenuScale, y + 20 * MenuScale, 170, 25, SavedMaps(i), False)
							
							y=y+30*MenuScale
							If y > (286+230) * MenuScale Then
								y = 286*MenuScale + 2*MenuScale
								x = x+175*MenuScale
							EndIf
						Else
							Exit
						EndIf
					Next
				EndIf
				
				;[End Block]
		End Select
	EndIf
	
	;DrawTiledImageRect(MenuBack, 985 * MenuScale, 860 * MenuScale, 200 * MenuScale, 20 * MenuScale, 1200 * MenuScale, 866 * MenuScale, 300, 20 * MenuScale)
	
	If userOptions\fullscreen Then DrawImage CursorIMG, MouseX(),MouseY()
	
	SetFont Font1
End Function

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



Type LoadingScreens
	Field imgpath$
	Field img%
	Field ID%
	Field title$
	Field alignx%, aligny%
	Field disablebackground%
	Field txt$[5], txtamount%
End Type

Function InitLoadingScreens(file$)
	Local TemporaryString$, i%
	Local ls.LoadingScreens
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			ls.LoadingScreens = New LoadingScreens
			LoadingScreenAmount=LoadingScreenAmount+1
			ls\ID = LoadingScreenAmount
			
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



Function DrawLoading(percent%, shortloading=False)
	
	Local x%, y%
	
	If percent = 0 Then
		LoadingScreenText=0
		
		temp = Rand(1,LoadingScreenAmount)
		For ls.loadingscreens = Each LoadingScreens
			If ls\id = temp Then
				If ls\img=0 Then ls\img = LoadImage("Loadingscreens\"+ls\imgpath)
				SelectedLoadingScreen = ls 
				Exit
			EndIf
		Next
	EndIf	
	
	firstloop = True
	Repeat 
		
		;Color 0,0,0
		;Rect 0,0,userOptions\screenWidth,userOptions\screenHeight,True
		;Color 255, 255, 255
		ClsColor 0,0,0
		Cls
		
		;Cls(True,False)
		
		If percent > 20 Then
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
		ElseIf  SelectedLoadingScreen\alignx = 1
			x = userOptions\screenWidth - ImageWidth(SelectedLoadingScreen\img)
		Else
			x = 0
		EndIf
		
		If SelectedLoadingScreen\aligny = 0 Then
			y = userOptions\screenHeight/2 - ImageHeight(SelectedLoadingScreen\img)/2 
		ElseIf  SelectedLoadingScreen\aligny = 1
			y = userOptions\screenHeight - ImageHeight(SelectedLoadingScreen\img)
		Else
			y = 0
		EndIf	
		
		DrawImage SelectedLoadingScreen\img, x, y
		
		Local width% = 300, height% = 20
		x% = userOptions\screenWidth / 2 - width / 2
		y% = userOptions\screenHeight / 2 + 30 - 100
		
		Rect(x, y, width+4, height, False)
		For  i% = 1 To Int((width - 2) * (percent / 100.0) / 10)
			DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
		Next
		
		If SelectedLoadingScreen\title = "CWM" Then
			
			If Not shortloading Then 
				If firstloop Then 
					If percent = 0 Then
						PlaySound LoadTempSound("SFX\SCP\990\cwm1.cwm")
					ElseIf percent = 100
						PlaySound LoadTempSound("SFX\SCP\990\cwm2.cwm")
					EndIf
				EndIf
			EndIf
			
			SetFont Font2
			strtemp$ = ""
			temp = Rand(2,9)
			For i = 0 To temp
				strtemp$ = STRTEMP + Chr(Rand(48,122))
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
			SetFont Font1
			RowText(strtemp, userOptions\screenWidth / 2-200, userOptions\screenHeight / 2 +120,400,300,True)		
		Else
			
			Color 0,0,0
			SetFont Font2
			Text(userOptions\screenWidth / 2 + 1, userOptions\screenHeight / 2 + 80 + 1, SelectedLoadingScreen\title, True, True)
			SetFont Font1
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], userOptions\screenWidth / 2-200+1, userOptions\screenHeight / 2 +120+1,400,300,True)
			
			Color 255,255,255
			SetFont Font2
			Text(userOptions\screenWidth / 2, userOptions\screenHeight / 2 +80, SelectedLoadingScreen\title, True, True)
			SetFont Font1
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], userOptions\screenWidth / 2-200, userOptions\screenHeight / 2 +120,400,300,True)
			
		EndIf
		
		Color 0,0,0
		Text(userOptions\screenWidth / 2 + 1, userOptions\screenHeight / 2 - 100 + 1, "LOADING - " + percent + " %", True, True)
		Color 255,255,255
		Text(userOptions\screenWidth / 2, userOptions\screenHeight / 2 - 100, "LOADING - " + percent + " %", True, True)
		
		If percent = 100 Then
			;If firstloop And SelectedLoadingScreen\title <> "CWM" Then PlaySound HorrorSFX(8) ;TODO: fix
			timing\prevTime = MilliSecs()
			Text(userOptions\screenWidth / 2, userOptions\screenHeight - 50, "PRESS ANY KEY TO CONTINUE", True, True)
		Else
			FlushKeys()
			FlushMouse()
		EndIf
		
		;not by any means a perfect solution
		;Not even proper gamma correction but it's a nice looking alternative that works in windowed mode
		If userOptions\screenGamma>1.0 Then
			CopyRect 0,0,GraphicWidth,GraphicHeight,1024-GraphicWidth/2,1024-GraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
			EntityBlend fresize_image,1
			ClsColor 0,0,0 : Cls
			ScaleRender(-1.0/Float(GraphicWidth),1.0/Float(GraphicWidth),2048.0 / Float(GraphicWidth),2048.0 / Float(GraphicWidth))
			EntityFX fresize_image,1+32
			EntityBlend fresize_image,3
			EntityAlpha fresize_image,userOptions\screenGamma-1.0
			ScaleRender(-1.0/Float(GraphicWidth),1.0/Float(GraphicWidth),2048.0 / Float(GraphicWidth),2048.0 / Float(GraphicWidth))
		ElseIf userOptions\screenGamma<1.0 Then ;todo: maybe optimize this if it's too slow, alternatively give players the option to disable gamma
			CopyRect 0,0,GraphicWidth,GraphicHeight,1024-GraphicWidth/2,1024-GraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
			EntityBlend fresize_image,1
			ClsColor 0,0,0 : Cls
			ScaleRender(-1.0/Float(GraphicWidth),1.0/Float(GraphicWidth),2048.0 / Float(GraphicWidth),2048.0 / Float(GraphicWidth))
			EntityFX fresize_image,1+32
			EntityBlend fresize_image,2
			EntityAlpha fresize_image,1.0
			SetBuffer TextureBuffer(fresize_texture2)
			ClsColor 255*userOptions\screenGamma,255*userOptions\screenGamma,255*userOptions\screenGamma
			Cls
			SetBuffer BackBuffer()
			ScaleRender(-1.0/Float(GraphicWidth),1.0/Float(GraphicWidth),2048.0 / Float(GraphicWidth),2048.0 / Float(GraphicWidth))
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
		If percent <> 100 Then Exit
		
	Until (GetKey()<>0 Or MouseHit(1))
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
	ElseIf value > 0 And value < 7 Or value > 26 And value < 32 Or value = 9
		Return aString$
	Else
		aString$ = aString$ + Chr(value)
		Return aString$
	End If
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
	DrawTiledImageRect(uiTextures\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
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
	DrawTiledImageRect(uiTextures\tileWhite, xoffset, (y Mod 256), 512, 512, x, y, width, height)
	
	DrawTiledImageRect(uiTextures\tileBlack, yoffset, (y Mod 256), 512, 512, x+3*MenuScale, y+3*MenuScale, width-6*MenuScale, height-6*MenuScale)	
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
	If bigfont Then SetFont Font2 Else SetFont Font1
	Text(x + width / 2, y + height / 2, txt, True, True)
End Function

Function UpdateUIButton%(x%, y%, width%, height, txt$="", waitForMouseUp%=False)
	Local clicked% = False
	
	If MouseOn(x, y, width, height) Then
		If (MouseHit1 And (Not waitForMouseUp)) Or (MouseUp1 And waitForMouseUp) Then 
			clicked = True
			PlaySound(ButtonSFX)
		EndIf
	EndIf
	
	Return clicked
End Function

Function DrawUITick(x%, y%, selected%, locked% = False)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Color (255, 255, 255)
	DrawTiledImageRect(uiTextures\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		Color(50, 50, 50)
	Else
		Color(0, 0, 0)		
	End If
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	
	If selected Then
		If Highlight Then
			Color 255,255,255
		Else
			Color 200,200,200
		EndIf
		DrawTiledImageRect(uiTextures\tileWhite, (x Mod 256), (y Mod 256), 512, 512, x + 4, y + 4, width - 8, height - 8)
		;Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	Color 255, 255, 255
End Function

Function UpdateUITick%(x%, y%, selected%, locked% = False)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		If MouseHit1 Then selected = (Not selected) : PlaySound (ButtonSFX)
	End If
	
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
	
	DrawImage(BlinkMeterIMG, x + width * value / 100.0 +3, y+3)
	
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
		End If
	Else
		If txt = "" Or width = 0 Then Return 0
		TextLength = StringWidth(txt)
		UnFitting = TextLength - width
		If UnFitting <= 0 Then ;mahtuu
			Text(x, y, txt)
		Else ;ei mahdu
			LetterWidth = TextLength / Len(txt)
			
			Text(x, y, Left(txt, Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
		End If
	EndIf
End Function

Function DrawTooltip(message$)
	Local scale# = userOptions\screenHeight/768.0
	
	Local width = (StringWidth(message$))+20*MenuScale
	
	Color 25,25,25
	Rect(MouseX()+20,MouseY(),width,19*scale,True)
	Color 150,150,150
	Rect(MouseX()+20,MouseY(),width,19*scale,False)
	SetFont Font1
	Text(MouseX()+(20*MenuScale)+(width/2),MouseY()+(12*MenuScale), message$, True, True)
End Function

Global QuickLoadPercent% = -1
Global QuickLoadPercent_DisplayTimer# = 0

Function DrawQuickLoading()
	
	If QuickLoadPercent > -1 Then
		Color 255,255,255
		SetFont Font1
		Text userOptions\screenWidth-100,userOptions\screenHeight-90,"LOADING: "+QuickLoadPercent+"%",1
		If QuickLoadPercent > 99 Then
			If QuickLoadPercent_DisplayTimer < 70 Then
				QuickLoadPercent_DisplayTimer# = Min(QuickLoadPercent_DisplayTimer+timing\tickDuration,70)
			Else
				QuickLoadPercent = -1
			EndIf
		EndIf
	Else
		QuickLoadPercent = -1
		QuickLoadPercent_DisplayTimer# = 0
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
;Main menu substates
Const GAMESUBSTATE_MAINMENU_MAIN% = 0
Const GAMESUBSTATE_MAINMENU_NEWGAME% = 1
Const GAMESUBSTATE_MAINMENU_CUSTOMMAP% = 2
Const GAMESUBSTATE_MAINMENU_LOADGAME% = 3
Const GAMESUBSTATE_MAINMENU_OPTIONS% = 4

Const MAINMENU_BUTTON_NEWGAME% = 0
Const MAINMENU_BUTTON_LOADGAME% = 1
Const MAINMENU_BUTTON_OPTIONS% = 2
Const MAINMENU_BUTTON_QUIT% = 3
Const MAINMENU_BUTTON_COUNT% = 4

Function UpdateMainMenu()
	Local x%, y%, width%, height%, temp.MarkedForRemoval
	Local n%
	Local i%
	Local strtemp$

	Local mouseHitButton%
	If (CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN) Then
		For i = 0 To MAINMENU_BUTTON_COUNT-1
			mouseHitButton = False
			x = Int(159.0 * MenuScale)
			y = Int((286.0 + 100.0 * i) * MenuScale)

			width = Int(400 * MenuScale)
			height = Int(70 * MenuScale)

			mouseHitButton = UpdateUIButton(x, y, width, height)

			Select i
				Case MAINMENU_BUTTON_NEWGAME
					RandomSeed = ""
					If (mouseHitButton) Then
						If (Rand(15)=1) Then
							Select Rand(14)
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
								Case 14
									RandomSeed = "BOYO"
							End Select
						Else
							n = Rand(4,8)
							For i = 1 To n
								If (Rand(3)=1) Then
									RandomSeed = RandomSeed + Str(Rand(0,9))
								Else
									RandomSeed = RandomSeed + Chr(Rand(97,122))
								EndIf
							Next
						EndIf

						;RandomSeed = MilliSecs()
						CurrGameSubstate = GAMESUBSTATE_MAINMENU_NEWGAME
					EndIf
				Case MAINMENU_BUTTON_LOADGAME
					If (mouseHitButton) Then
						CurrGameSubstate = GAMESUBSTATE_MAINMENU_LOADGAME
					EndIf
				Case MAINMENU_BUTTON_OPTIONS
					If (mouseHitButton) Then CurrGameSubstate = GAMESUBSTATE_MAINMENU_OPTIONS
				Case MAINMENU_BUTTON_QUIT
					If (mouseHitButton) Then
						End()
					EndIf
			End Select
		Next

	Else

		x = Int(159.0 * MenuScale)
		y = Int(286.0 * MenuScale)

		width = Int(400.0 * MenuScale)
		height = Int(70.0 * MenuScale)

		If (UpdateUIButton(x + width + Int(20.0 * MenuScale), y, Int(580.0 * MenuScale - width - 20.0 * MenuScale), height, "BACK")) Then
			Select CurrGameSubstate
				Case GAMESUBSTATE_MAINMENU_NEWGAME
					PutINIValue(OptionFile, "general", "intro enabled", Str(userOptions\introEnabled))
					CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
				Case GAMESUBSTATE_MAINMENU_OPTIONS ;save the options
					SaveOptionsINI()

					CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
				Case GAMESUBSTATE_MAINMENU_CUSTOMMAP ;move back to the "new game" tab
					CurrGameSubstate = GAMESUBSTATE_MAINMENU_NEWGAME
					MouseHit1 = False
				Default
					CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN
			End Select
		EndIf

		Select CurrGameSubstate
			Case GAMESUBSTATE_MAINMENU_NEWGAME
				;[Block]

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				x = Int(160.0 * MenuScale)
				y = Int(y + height + 20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(330.0 * MenuScale)

				CurrSave = UpdateInputBox(x + Int(150.0 * MenuScale), y + Int(15.0 * MenuScale), Int(200.0 * MenuScale), Int(30.0 * MenuScale), CurrSave, 1)
				CurrSave = Left(CurrSave, 15)

				userOptions\introEnabled = UpdateUITick(x + Int(280.0 * MenuScale), y + Int(110.0 * MenuScale), userOptions\introEnabled)

				For i = SAFE To CUSTOM
					If (UpdateUITick(x + Int(20.0 * MenuScale), y + Int((180.0+30.0*i) * MenuScale), (SelectedDifficulty = difficulties[i]))) Then SelectedDifficulty = difficulties[i]
				Next

				If (SelectedDifficulty\customizable) Then
					SelectedDifficulty\permaDeath =  UpdateUITick(x + Int(160.0 * MenuScale), y + Int(165.0 * MenuScale), (SelectedDifficulty\permaDeath))

					If (UpdateUITick(x + Int(160.0 * MenuScale), y + Int(195.0 * MenuScale), SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath), SelectedDifficulty\permaDeath)) Then
						SelectedDifficulty\saveType = SAVEANYWHERE
					Else
						SelectedDifficulty\saveType = SAVEONSCREENS
					EndIf

					SelectedDifficulty\aggressiveNPCs =  UpdateUITick(x + Int(160.0 * MenuScale), y + Int(225.0 * MenuScale), SelectedDifficulty\aggressiveNPCs)

					;Other factor's difficulty
					If (MouseHit1) Then
						If (MouseOn(x + Int(155.0 * MenuScale), y+Int(251.0*MenuScale), ImageWidth(uiAssets\arrow[1]), ImageHeight(uiAssets\arrow[1]))) Then
							If (SelectedDifficulty\otherFactors < HARD) Then
								SelectedDifficulty\otherFactors = SelectedDifficulty\otherFactors + 1
							Else
								SelectedDifficulty\otherFactors = EASY
							EndIf
							PlaySound_SM(sndManager\button)
						EndIf
					EndIf
				EndIf

				If (UpdateUIButton(x, y + height + Int(20.0 * MenuScale), Int(160.0 * MenuScale), Int(70.0 * MenuScale), "Load map")) Then
					CurrGameSubstate = GAMESUBSTATE_MAINMENU_CUSTOMMAP
					LoadSavedMaps()
				EndIf

				If (UpdateUIButton(x + Int(420.0 * MenuScale), y + height + Int(20.0 * MenuScale), Int(160.0 * MenuScale), Int(70.0 * MenuScale), "START")) Then
					If (CurrSave <> "") Then CurrSave = "untitled"

					If (RandomSeed = "") Then
						RandomSeed = Str(Abs(MilliSecs()))
					EndIf
					strtemp = ""
					SeedRnd(SeedStringToInt(RandomSeed))

;					SameFound = False
;					For i = 1 To SaveGameAmount
;						If (SaveGames(i - 1) = CurrSave) Then SameFound=SameFound+1
;					Next
;
;					If (SameFound > 0) Then CurrSave = CurrSave + " (" + Str(SameFound + 1) + ")"

					LoadEntities()
					InitNewGame()
					CurrGameState = GAMESTATE_PLAYING
					FlushKeys()
					FlushMouse()

					PutINIValue(OptionFile, "general", "intro enabled", Str(userOptions\introEnabled))
				EndIf

				;[End Block]
			Case GAMESUBSTATE_MAINMENU_LOADGAME ;load game
				;[Block]

				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(300.0 * MenuScale)

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(296.0 * MenuScale)

				;[End Block]
			Case GAMESUBSTATE_MAINMENU_OPTIONS ;options
				;[Block]

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(60.0 * MenuScale)
				;If (UpdateUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False)) Then MainMenuTab = 3
				;If (UpdateUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False)) Then MainMenuTab = 5
				;If (UpdateUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False)) Then MainMenuTab = 6
				;If (UpdateUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False)) Then MainMenuTab = 7
				y = y + Int(70.0 * MenuScale)

;				If MainMenuTab = 3 ;Graphics
;					;[Block]
;					height = 300 * MenuScale
;
;					y=y+20*MenuScale
;
;					userOptions\hudEnabled = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)
;
;					y=y+30*MenuScale
;
;					UpdateUITick(x + 310 * MenuScale, y + MenuScale, False, True)
;
;					y=y+30*MenuScale
;
;					userOptions\vsync = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
;
;					y=y+40*MenuScale
;
;					userOptions\screenGamma = (UpdateSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.0)/50.0)
;
;					y = y + 50*MenuScale
;
;					y=y+30*MenuScale
;					;[End Block]
;				ElseIf MainMenuTab = 5 ;Audio
;					;[Block]
;					height = 220 * MenuScale
;
;					y = y + 20*MenuScale
;
;					userOptions\musicVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.0)/100.0)
;
;					y = y + 40*MenuScale
;
;					userOptions\soundVolume = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\soundVolume*100.0)/100.0)
;					;[End Block]
;				ElseIf MainMenuTab = 6 ;Controls
;					;[Block]
;					height = 230 * MenuScale
;
;					y = y + 20*MenuScale
;
;					userOptions\mouseSensitivity = (UpdateSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.0)/100.0)-0.5
;
;					y = y + 40*MenuScale
;
;					userOptions\invertMouseY(= UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY))
;
;					y = y + 30*MenuScale
;
;					y = y + 10*MenuScale
;
;					UpdateInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\up,210)],5)
;
;					UpdateInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\lft,210)],3)
;
;					UpdateInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\down,210)],6)
;
;					UpdateInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\rght,210)],4)
;
;					UpdateInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\save,210)],11)
;
;
;					UpdateInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\blink,210)],7)
;
;					UpdateInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\sprint,210)],8)
;
;					UpdateInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\inv,210)],9)
;
;					UpdateInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\crouch,210)],10)
;
;					UpdateInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\console,210)],12)
;
;					For i = 0 To 227
;						If (KeyHit(i)) Then key = i : Exit
;					Next
;					If (key<>0) Then
;						Select SelectedInputBox
;							Case 3
;								keyBinds\lft = key
;							Case 4
;								keyBinds\rght = key
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
;				ElseIf MainMenuTab = 7 ;Advanced
;					;[Block]
;					height = 310 * MenuScale
;
;					y = y + 20*MenuScale
;
;					y = y + 30*MenuScale
;
;					userOptions\consoleOpenOnError = UpdateUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
;
;					y = y + 50*MenuScale
;
;					ShowFPS% = UpdateUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
;
;					y = y + 30*MenuScale
;
;					If (UpdateUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.0)) Then
;						CurrFrameLimit# = (UpdateSlideBar(x + 150*MenuScale, y+30*MenuScale, 100*MenuScale, CurrFrameLimit#*50.0)/50.0)
;						CurrFrameLimit = Max(CurrFrameLimit, 0.1)
;						userOptions\framelimit = CurrFrameLimit#*100.0
;					Else
;						CurrFrameLimit# = 0.0
;						userOptions\framelimit = 0
;					EndIf
;
;					y = y + 80*MenuScale
;
;					;[End Block]
;				EndIf
				;[End Block]
			Case GAMESUBSTATE_MAINMENU_CUSTOMMAP ; load map
				;[Block]
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(350.0 * MenuScale)

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(350.0 * MenuScale)

				;[End Block]
		End Select

	EndIf
End Function

Function DrawMainMenu()
	Local x%, y%, width%, height%, temp.MarkedForRemoval
	Local i%

	Color(0,0,0)
	Rect(0,0,userOptions\screenWidth,userOptions\screenHeight,True)

	DrawImage(uiAssets\back, 0, 0)
	
	If (TimeInPosMilliSecs() Mod Int(MenuBlinkTimer[0]) >= Rand(Int(MenuBlinkDuration[0]))) Then
		DrawImage(uiAssets\scp173, userOptions\screenWidth - ImageWidth(uiAssets\scp173), userOptions\screenHeight - ImageHeight(uiAssets\scp173))
	EndIf

	If (Rand(300) = 1) Then
		MenuBlinkTimer[0] = Rand(4000, 8000)
		MenuBlinkDuration[0] = Rand(200, 500)
	EndIf

	SetFont(uiAssets\font[0])

	MenuBlinkTimer[1]=MenuBlinkTimer[1]-timing\tickDuration
	If (MenuBlinkTimer[1] < MenuBlinkDuration[1]) Then
		Color(50, 50, 50)
		Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
		If (MenuBlinkTimer[1] < 0) Then
			MenuBlinkTimer[1] = Rand(700, 800)
			MenuBlinkDuration[1] = Rand(10, 35)
			MenuStrX = Int(Rand(700, 1000) * MenuScale)
			MenuStrY = Int(Rand(100, 600) * MenuScale)

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

	SetFont(uiAssets\font[1])

	DrawImage(uiAssets\scpText, userOptions\screenWidth / 2 - ImageWidth(uiAssets\scpText) / 2, userOptions\screenHeight - Int(20.0 * MenuScale) - ImageHeight(uiAssets\scpText))

	If (userOptions\screenWidth > 1240 * MenuScale) Then
		DrawTiledImageRect(uiAssets\tileWhite, 0, 5, 512, Int(7.0 * MenuScale), Int(985.0 * MenuScale), Int(407.0 * MenuScale), (userOptions\screenWidth - Int(1240 * MenuScale)) + 300, Int(7.0 * MenuScale))
	EndIf

	Local txt$
	If (CurrGameSubstate = GAMESUBSTATE_MAINMENU_MAIN) Then
		For i = 0 To MAINMENU_BUTTON_COUNT-1
			x = Int(159.0 * MenuScale)
			y = Int((286.0 + 100.0 * i) * MenuScale)

			width = Int(400.0 * MenuScale)
			height = Int(70.0 * MenuScale)

			Select i
				Case MAINMENU_BUTTON_NEWGAME ;new game
					txt = "NEW GAME"
				Case MAINMENU_BUTTON_LOADGAME ;load game
					txt = "LOAD GAME"
				Case MAINMENU_BUTTON_OPTIONS ;options
					txt = "OPTIONS"
				Case MAINMENU_BUTTON_QUIT ;quit
					txt = "QUIT"
			End Select

			DrawUIButton(x, y, width, height, txt)
		Next
	Else
		x = Int(159.0 * MenuScale)
		y = Int(286.0 * MenuScale)

		width = Int(400.0 * MenuScale)
		height = Int(70.0 * MenuScale)

		DrawFrame(x, y, width, height)

		DrawUIButton(x + width + Int(20.0 * MenuScale), y, Int(580.0 * MenuScale) - width - Int(20.0 * MenuScale), height, "BACK", False)

		Select CurrGameSubstate
			Case GAMESUBSTATE_MAINMENU_NEWGAME ; New game
				;[Block]

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				Color(255, 255, 255)
				SetFont(uiAssets\font[1])
				Text(x + width / 2, y + height / 2, "NEW GAME", True, True)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(330.0 * MenuScale)

				DrawFrame(x, y, width, height)

				SetFont(uiAssets\font[0])

				Text(x + Int(20.0 * MenuScale), y + Int(20.0 * MenuScale), "Name:")
				DrawInputBox(x + Int(150.0 * MenuScale), y + Int(15.0 * MenuScale), Int(200.0 * MenuScale), Int(30.0 * MenuScale), CurrSave, 1)

				Color(255,255,255)
;				If (SelectedMap = "") Then
				Text(x + Int(20.0 * MenuScale), y + Int(60.0 * MenuScale), "Map seed:")
				DrawInputBox(x+Int(150.0*MenuScale), y+Int(55.0*MenuScale), Int(200.0*MenuScale), Int(30.0*MenuScale), RandomSeed, 3)
;				Else
;					Text(x + Int(20.0 * MenuScale), y + Int(60.0 * MenuScale), "Selected map:")
;					Color(255, 255, 255)
;					Rect(x+Int(150.0*MenuScale), y+Int(55.0*MenuScale), Int(200.0*MenuScale), Int(30.0*MenuScale))
;					Color(0, 0, 0)
;					Rect(x+Int(150.0*MenuScale)+2, y+Int(55.0*MenuScale)+2, Int(200.0*MenuScale)-4, Int(30.0*MenuScale)-4)
;
;					Color(255, 0,0)
;					Text(x+Int(150.0*MenuScale + 100.0*MenuScale), y+Int(55.0*MenuScale + 15.0*MenuScale), SelectedMap, True, True)
;
;					DrawUIButton(x+Int(370.0*MenuScale), y+Int(55.0*MenuScale), Int(120.0*MenuScale), Int(30.0*MenuScale), "Deselect", False)
;				EndIf

				Text(x + Int(20.0 * MenuScale), y + Int(110.0 * MenuScale), "Enable intro sequence:")
				DrawUITick(x + Int(280.0 * MenuScale), y + Int(110.0 * MenuScale), userOptions\introEnabled)

				;Local modeName$, modeDescription$, selectedDescription$
				Text(x + Int(20.0 * MenuScale), y + Int(150.0 * MenuScale), "Difficulty:")
				For i = SAFE To CUSTOM
					DrawUITick(x + Int(20.0 * MenuScale), y + Int((180.0+30.0*i) * MenuScale), (SelectedDifficulty = difficulties[i]))

					Text(x + Int(60.0 * MenuScale), y + Int((180.0+30.0*i) * MenuScale), difficulties[i]\name)
				Next

				Color(255, 255, 255)
				DrawFrame(x + Int(150.0 * MenuScale),y + Int(155.0 * MenuScale), Int(410.0*MenuScale), Int(150.0*MenuScale))

				If (SelectedDifficulty\customizable) Then
					DrawUITick(x + Int(160.0 * MenuScale), y + Int(165.0 * MenuScale), (SelectedDifficulty\permaDeath))
					Text(x + Int(200.0 * MenuScale), y + Int(165.0 * MenuScale), "Permadeath")

					DrawUITick(x + Int(160.0 * MenuScale), y + Int(195.0 * MenuScale), SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath), SelectedDifficulty\permaDeath)

					Text(x + Int(200.0 * MenuScale), y + Int(195.0 * MenuScale), "Save anywhere")

					DrawUITick(x + Int(160.0 * MenuScale), y + Int(225.0 * MenuScale), SelectedDifficulty\aggressiveNPCs)
					Text(x + Int(200.0 * MenuScale), y + Int(225.0 * MenuScale), "Aggressive NPCs")

					;Other factor's difficulty
					Color(255,255,255)
					DrawImage(uiAssets\arrow[1],x + Int(155.0 * MenuScale), y+Int(251.0*MenuScale))

					Color(255,255,255)
					Select SelectedDifficulty\otherFactors
						Case EASY
							Text(x + Int(200.0 * MenuScale), y + Int(255.0 * MenuScale), "Other difficulty factors: Easy")
						Case NORMAL
							Text(x + Int(200.0 * MenuScale), y + Int(255.0 * MenuScale), "Other difficulty factors: Normal")
						Case HARD
							Text(x + Int(200.0 * MenuScale), y + Int(255.0 * MenuScale), "Other difficulty factors: Hard")
					End Select
				Else
					RowText(SelectedDifficulty\description, x+Int(160.0*MenuScale), y+Int(160.0*MenuScale), Int((410.0-20.0)*MenuScale), Int(200.0*MenuScale))
				EndIf

				DrawUIButton(x, y + height + Int(20.0 * MenuScale), Int(160.0 * MenuScale), Int(70.0 * MenuScale), "Load map", False)

				SetFont(uiAssets\font[1])

				DrawUIButton(x + Int(420.0 * MenuScale), y + height + Int(20.0 * MenuScale), Int(160.0 * MenuScale), Int(70.0 * MenuScale), "START", False)
				;[End Block]
			Case GAMESUBSTATE_MAINMENU_LOADGAME ;load game
				;[Block]

				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(300.0 * MenuScale)

				DrawFrame(x, y, width, height)

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				Color(255, 255, 255)
				SetFont(uiAssets\font[1])
				Text(x + width / 2, y + height / 2, "LOAD GAME", True, True)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(296.0 * MenuScale)

				SetFont(uiAssets\font[0])

;				If (SaveGameAmount = 0) Then
				Text(x + Int(20.0 * MenuScale), y + Int(20.0 * MenuScale), "No saved games.")
;				Else
;					x = x + Int(20.0 * MenuScale)
;					y = y + Int(20.0 * MenuScale)
;					For i = 1 To SaveGameAmount
;						DrawFrame(x,y,Int(540.0*MenuScale), Int(70.0*MenuScale))
;
;						Text(x + Int(20.0 * MenuScale), y + Int(10.0 * MenuScale), SaveGames(i - 1))
;						Text(x + Int(20.0 * MenuScale), y + Int((10.0+23.0) * MenuScale), SaveGameTime(i - 1))
;						Text(x + Int(120.0 * MenuScale), y + Int((10.0+23.0) * MenuScale), SaveGameDate(i - 1))
;
;						If (SaveMSG = "") Then
;							DrawUIButton(x + Int(280.0 * MenuScale), y + Int(20.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale), "Load", False)
;
;							DrawUIButton(x + Int(400.0 * MenuScale), y + Int(20.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale), "Delete", False)
;
;						Else
;							DrawFrame(x + Int(280.0 * MenuScale), y + Int(20.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale))
;							Color(100, 100, 100)
;							Text(x + Int(330.0 * MenuScale), y + Int(35.0 * MenuScale), "Load", True, True)
;
;							DrawFrame(x + Int(400.0 * MenuScale), y + Int(20.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale))
;							Color(100, 100, 100)
;							Text(x + Int(450.0 * MenuScale), y + Int(35.0 * MenuScale), "Delete", True, True)
;						EndIf
;
;						y = y + Int(80.0 * MenuScale)
;
;					Next
;
;					If (SaveMSG <> "") Then
;						x = userOptions\screenWidth / 2
;						y = userOptions\screenHeight / 2
;						DrawFrame(x, y, Int(400.0 * MenuScale), Int(200.0 * MenuScale))
;						Text(x + Int(20.0 * MenuScale), y + Int(15.0 * MenuScale), "Are you sure you want to delete this save?")
;						DrawUIButton(x + Int(250.0 * MenuScale), y + Int(150.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale), "Yes", False)
;						DrawUIButton(x + Int(50.0 * MenuScale), y + Int(150.0 * MenuScale), Int(100.0 * MenuScale), Int(30.0 * MenuScale), "No", False)
;					EndIf
;				EndIf

				;[End Block]
			Case GAMESUBSTATE_MAINMENU_OPTIONS ;options
				;[Block]

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				Color(255, 255, 255)
				SetFont(uiAssets\font[1])
				Text(x + width / 2, y + height / 2, "OPTIONS", True, True)

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(60.0 * MenuScale)
				DrawFrame(x, y, width, height)
;				DrawUIButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, "GRAPHICS", False)
;				DrawUIButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, "AUDIO", False)
;				DrawUIButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, "CONTROLS", False)
;				DrawUIButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, "ADVANCED", False)
;				Color(0,255,0)
;				If MainMenuTab = 3
;					Rect(x+20*MenuScale,y+15*MenuScale,width/5,height/2,False)
;				ElseIf MainMenuTab = 5
;					Rect(x+160*MenuScale,y+15*MenuScale,width/5,height/2,False)
;				ElseIf MainMenuTab = 6
;					Rect(x+300*MenuScale,y+15*MenuScale,width/5,height/2,False)
;				ElseIf MainMenuTab = 7
;					Rect(x+440*MenuScale,y+15*MenuScale,width/5,height/2,False)
;				EndIf
				SetFont(uiAssets\font[0])
				y = y + Int(70.0 * MenuScale)

;				If MainMenuTab = 3 ;Graphics
;					;[Block]
;					height = 300 * MenuScale
;					DrawFrame(x, y, width, height)
;
;					y=y+20*MenuScale
;
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Show HUD:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\hudEnabled)
;
;					y=y+30*MenuScale
;
;					Color(100,100,100)
;					Text(x + 20 * MenuScale, y, "Enable bump mapping:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, False, True)
;					If MouseOn(x + 310 * MenuScale, y + MenuScale, 20*MenuScale,20*MenuScale)
;						DrawTooltip("Not available in this version")
;					EndIf
;
;					y=y+30*MenuScale
;
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "VSync:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\vsync)
;
;					y=y+40*MenuScale
;
;					DrawSlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, userOptions\screenGamma*50.0)
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Screen gamma")
;
;					y = y + 50*MenuScale
;
;					Color(100,100,100)
;					Text(x + 20 * MenuScale, y, "Texture quality:")
;					DrawImage(uiAssets\arrow[1],x + 310 * MenuScale, y-4*MenuScale)
;
;					Text(x + 340 * MenuScale, y + MenuScale, "DISABLED")
;					If MouseOn(x + 310 * MenuScale, y-4*MenuScale, ImageWidth(uiAssets\arrow[1]),ImageHeight(uiAssets\arrow[1]))
;						DrawTooltip("Not available in this version")
;					EndIf
;
;					y=y+30*MenuScale
;					;[End Block]
;				ElseIf MainMenuTab = 5 ;Audio
;					;[Block]
;					height = 220 * MenuScale
;					DrawFrame(x, y, width, height)
;
;					y = y + 20*MenuScale
;
;					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\musicVolume*100.0)
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Music volume:")
;
;					y = y + 40*MenuScale
;
;					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, userOptions\soundVolume*100.0)
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Sound volume:")
;					;[End Block]
;				ElseIf MainMenuTab = 6 ;Controls
;					;[Block]
;					height = 230 * MenuScale
;					DrawFrame(x, y, width, height)
;
;					y = y + 20*MenuScale
;
;					DrawSlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (userOptions\mouseSensitivity+0.5)*100.0)
;					Color(255, 255, 255)
;					Text(x + 20 * MenuScale, y, "Mouse sensitivity:")
;
;					y = y + 40*MenuScale
;
;					Color(255, 255, 255)
;					Text(x + 20 * MenuScale, y, "Invert mouse Y-axis:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\invertMouseY)
;
;					y = y + 30*MenuScale
;					Text(x + 20 * MenuScale, y, "Control configuration:")
;					y = y + 10*MenuScale
;
;					Text(x + 20 * MenuScale, y + 20 * MenuScale, "Move Forward")
;					DrawInputBox(x + 160 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\up,210)],5)
;					Text(x + 20 * MenuScale, y + 40 * MenuScale, "Strafe Left")
;					DrawInputBox(x + 160 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\lft,210)],3)
;					Text(x + 20 * MenuScale, y + 60 * MenuScale, "Move Backward")
;					DrawInputBox(x + 160 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\down,210)],6)
;					Text(x + 20 * MenuScale, y + 80 * MenuScale, "Strafe Right")
;					DrawInputBox(x + 160 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\rght,210)],4)
;					Text(x + 20 * MenuScale, y + 100 * MenuScale, "Quick Save")
;					DrawInputBox(x + 160 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\save,210)],11)
;
;					Text(x + 280 * MenuScale, y + 20 * MenuScale, "Manual Blink")
;					DrawInputBox(x + 470 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\blink,210)],7)
;					Text(x + 280 * MenuScale, y + 40 * MenuScale, "Sprint")
;					DrawInputBox(x + 470 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\sprint,210)],8)
;					Text(x + 280 * MenuScale, y + 60 * MenuScale, "Open/Close Inventory")
;					DrawInputBox(x + 470 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\inv,210)],9)
;					Text(x + 280 * MenuScale, y + 80 * MenuScale, "Crouch")
;					DrawInputBox(x + 470 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\crouch,210)],10)
;					Text(x + 280 * MenuScale, y + 100 * MenuScale, "Open/Close Console")
;					DrawInputBox(x + 470 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName[Min(keyBinds\console,210)],12)
;					;[End Block]
;				ElseIf MainMenuTab = 7 ;Advanced
;					;[Block]
;					height = 310 * MenuScale
;					DrawFrame(x, y, width, height)
;
;					y = y + 20*MenuScale
;
;					Color(255,255,255)
;					y = y + 30*MenuScale
;
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Open console on error:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, userOptions\consoleOpenOnError)
;
;					y = y + 50*MenuScale
;
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Show FPS:")
;					DrawUITick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
;
;					y = y + 30*MenuScale
;
;					Color(255,255,255)
;					Text(x + 20 * MenuScale, y, "Framelimit:")
;					Color(255,255,255)
;					DrawUITick(x + 310 * MenuScale, y, CurrFrameLimit > 0.0)
;
;					y = y + 80*MenuScale
;
;					;[End Block]
;				EndIf
				;[End Block]
			Case GAMESUBSTATE_MAINMENU_CUSTOMMAP ; load map
				;[Block]
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(350.0 * MenuScale)

				DrawFrame(x, y, width, height)

				x = Int(159.0 * MenuScale)
				y = Int(286.0 * MenuScale)

				width = Int(400.0 * MenuScale)
				height = Int(70.0 * MenuScale)

				Color(255, 255, 255)
				SetFont(uiAssets\font[1])
				Text(x + width / 2, y + height / 2, "LOAD MAP", True, True)
				SetFont(uiAssets\font[0])

				x = Int(160.0 * MenuScale)
				y = y + height + Int(20.0 * MenuScale)
				width = Int(580.0 * MenuScale)
				height = Int(350.0 * MenuScale)

				SetFont(uiAssets\font[0])

;				If (SavedMaps(0)="") Then
				Text(x + Int(20.0 * MenuScale), y + Int(20.0 * MenuScale), "No saved maps. Use the Map Creator to create new maps.")
;				Else
;					x = x + Int(20.0 * MenuScale)
;					y = y + Int(20.0 * MenuScale)
;					For i = 0 To MAXSAVEDMAPS-1
;						If (SavedMaps(i)<>"") Then
;
;							DrawUIButton(x + Int(20.0 * MenuScale), y + Int(20.0 * MenuScale), Int(170.0 * MenuScale), Int(25.0 * MenuScale), SavedMaps(i), False)
;
;							y=y+Int(30.0*MenuScale)
;							If (y > Int((286.0+230.0) * MenuScale)) Then
;								y = Int(286.0*MenuScale + 2.0*MenuScale)
;								x = x+Int(175.0*MenuScale)
;							EndIf
;						Else
;							Exit
;						EndIf
;					Next
;				EndIf

				;[End Block]
		End Select
	EndIf

	;DrawTiledImageRect(MenuBack, 985 * MenuScale, 860 * MenuScale, 200 * MenuScale, 20 * MenuScale, 1200 * MenuScale, 866 * MenuScale, 300, 20 * MenuScale)



	SetFont(uiAssets\font[0])
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
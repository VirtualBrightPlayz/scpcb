;----------------------------------------------  Console -----------------------------------------------------

Global ConsoleInput$
Global ConsoleScroll#,ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR% = 255,ConsoleG% = 255,ConsoleB% = 255

Global ConsoleFlush%
Global ConsoleFlushSnd% = 0, ConsoleMusFlush% = 0

Global DebugHUD%

Type ConsoleMsg
	Field txt$
	Field isCommand%
	Field r%,g%,b%
End Type

Function CreateConsoleMsg(txt$,r%=-1,g%=-1,b%=-1,isCommand%=False)
	Local c.ConsoleMsg = New ConsoleMsg
	Insert c Before First ConsoleMsg

	c\txt = txt
	c\isCommand = isCommand

	c\r = r
	c\g = g
	c\b = b

	If (c\r<0) Then c\r = ConsoleR
	If (c\g<0) Then c\g = ConsoleG
	If (c\b<0) Then c\b = ConsoleB
End Function

Function DrawConsole()
	Local cm.ConsoleMsg
	Local inBar%
	Local inBox%

	Local x%,y%
	Local width%,height%

	Local consoleHeight%
	Local scrollbarHeight%

	Local TempY%
	Local count%
	If (CurrGameState=GAMESTATE_CONSOLE) Then
		SetFont(uiAssets\consoleFont)

		x = 0
		y = userOptions\screenHeight-Int(300.0*MenuScale)
		width = userOptions\screenWidth
		height = Int(300.0*MenuScale-30.0*MenuScale)

		DrawFrame(x,y,width,height+Int(30.0*MenuScale))

		consoleHeight = 0
		scrollbarHeight = 0

		For cm = Each ConsoleMsg
			consoleHeight = consoleHeight + Int(15.0*MenuScale)
		Next
		scrollbarHeight = Int((Float(height)/Float(consoleHeight))*height)
		If (scrollbarHeight>height) Then scrollbarHeight = height
		If (consoleHeight<height) Then consoleHeight = height

		Color(50,50,50)
		inBar = MouseOn(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height)
		If (inBar) Then Color(70,70,70)
		Rect(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height,True)


		Color(120,120,120)
		inBox = MouseOn(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight)
		If (inBox) Then Color(200,200,200)
		If (ConsoleScrollDragging) Then Color(255,255,255)
		Rect(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight,True)

		Color(255, 255, 255)

		TempY = y + height - Int(25.0*MenuScale) - Int(ConsoleScroll)
		count = 0
		For cm = Each ConsoleMsg
			count = count+1
			If (count>1000) Then
				Delete cm
			Else
				If (TempY >= y And TempY < y + height - Int(20.0*MenuScale)) Then
					If (cm=ConsoleReissue) Then
						Color(cm\r/4,cm\g/4,cm\b/4)
						Rect(x,TempY-Int(2.0*MenuScale),width-Int(30.0*MenuScale),Int(24.0*MenuScale),True)
					EndIf
					Color(cm\r,cm\g,cm\b)
					If (cm\isCommand) Then
						Text(x + Int(20.0*MenuScale), TempY, "> "+cm\txt)
					Else
						Text(x + Int(20.0*MenuScale), TempY, cm\txt)
					EndIf
				EndIf
				TempY = TempY - Int(15.0*MenuScale)
			EndIf

		Next

		Color(255,255,255)

		DrawInputBox(x, y + height, width, Int(30.0*MenuScale), ConsoleInput, 2)


	EndIf
End Function

Function UpdateConsole()
	Local inBar%,inBox%
	Local mouseScroll%
	Local x%,y%,c%
	Local sf%,b%,t%,texname$
	Local StrTemp$, StrTemp2$, StrTemp3$, temp%,  i%
	Local ev.Event, r.Room, it.Item
	Local consoleHeight%
	Local scrollbarHeight%
	Local width%,height%
	Local itt.ItemTemplate

	Local cm.ConsoleMsg
	Local n.NPC
	Local args$

	Local reissuePos%

	Local oldConsoleInput$

	Local tex%,tex2%
	If (CurrGameState=GAMESTATE_CONSOLE) Then
		ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255

		x = 0
		y = userOptions\screenHeight-Int(300.0*MenuScale)
		width = userOptions\screenWidth
		height = Int(300.0*MenuScale-30.0*MenuScale)

		consoleHeight = 0
		scrollbarHeight = 0
		For cm = Each ConsoleMsg
			consoleHeight = consoleHeight + Int(15.0*MenuScale)
		Next
		scrollbarHeight = Int((Float(height)/Float(consoleHeight))*height)
		If (scrollbarHeight>height) Then scrollbarHeight = height
		If (consoleHeight<height) Then consoleHeight = height

		inBar = MouseOn(x+width-Int(26.0*MenuScale),y,Int(26.0*MenuScale),height)

		inBox = MouseOn(x+width-Int(23.0*MenuScale),y+height-scrollbarHeight+Int(ConsoleScroll*scrollbarHeight/height),Int(20.0*MenuScale),scrollbarHeight)

		If (Not MouseDown(1)) Then
			ConsoleScrollDragging=False
		ElseIf (ConsoleScrollDragging) Then
			ConsoleScroll = ConsoleScroll+((MouseY()-ConsoleMouseMem)*height/scrollbarHeight)
			ConsoleMouseMem = MouseY()
		EndIf

		If (Not ConsoleScrollDragging) Then
			If (MouseHit1) Then
				If (inBox) Then
					ConsoleScrollDragging=True
					ConsoleMouseMem = MouseY()
				ElseIf (inBar) Then
					ConsoleScroll = ConsoleScroll+((MouseY()-(y+height))*consoleHeight/height+(height/2))
					ConsoleScroll = ConsoleScroll/2
				EndIf
			EndIf
		EndIf

		mouseScroll = MouseZSpeed()
		If (mouseScroll=1) Then
			ConsoleScroll = ConsoleScroll - 15*MenuScale
		ElseIf (mouseScroll=-1) Then
			ConsoleScroll = ConsoleScroll + 15*MenuScale
		EndIf

		If (KeyHit(200)) Then
			reissuePos = 0
			If (ConsoleReissue=Null) Then
				ConsoleReissue=First ConsoleMsg

				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - Int(15.0*MenuScale)
					ConsoleReissue = After ConsoleReissue
				Wend

			Else
				cm = First ConsoleMsg
				While (cm <> Null)
					If (cm=ConsoleReissue) Then Exit
					reissuePos = reissuePos-Int(15.0*MenuScale)
					cm = After cm
				Wend
				ConsoleReissue = After ConsoleReissue
				reissuePos = reissuePos-Int(15.0*MenuScale)

				While (True)
					If (ConsoleReissue=Null) Then
						ConsoleReissue=First ConsoleMsg
						reissuePos = 0
					EndIf

					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - Int(15.0*MenuScale)
					ConsoleReissue = After ConsoleReissue
				Wend
			EndIf

			If (ConsoleReissue<>Null) Then
				ConsoleInput= ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf

		If (KeyHit(208)) Then
			reissuePos = -consoleHeight+Int(15.0*MenuScale)
			If (ConsoleReissue=Null) Then
				ConsoleReissue=Last ConsoleMsg

				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + Int(15.0*MenuScale)
					ConsoleReissue = Before ConsoleReissue
				Wend

			Else
				cm = Last ConsoleMsg
				While (cm<>Null)
					If (cm=ConsoleReissue) Then Exit
					reissuePos = reissuePos+Int(15.0*MenuScale)
					cm = Before cm
				Wend
				ConsoleReissue = Before ConsoleReissue
				reissuePos = reissuePos+Int(15.0*MenuScale)

				While (True)
					If (ConsoleReissue=Null) Then
						ConsoleReissue=Last ConsoleMsg
						reissuePos=-consoleHeight+Int(15.0*MenuScale)
					EndIf

					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + Int(15.0*MenuScale)
					ConsoleReissue = Before ConsoleReissue
				Wend
			EndIf

			If (ConsoleReissue<>Null) Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf

		If (ConsoleScroll<-consoleHeight+height) Then ConsoleScroll = -consoleHeight+height
		If (ConsoleScroll>0) Then ConsoleScroll = 0

		SelectedInputBox = 2
		oldConsoleInput = ConsoleInput
		ConsoleInput = UpdateInputBox(x, y + height, width, Int(30.0*MenuScale), ConsoleInput, 2)
		If (oldConsoleInput<>ConsoleInput) Then
			ConsoleReissue = Null
		EndIf
		ConsoleInput = Left(ConsoleInput, 100)

		If (KeyHit(28) And ConsoleInput<> "") Then
			ConsoleReissue = Null
			ConsoleScroll = 0
			CreateConsoleMsg(ConsoleInput,255,255,0,True)
			If (Instr(ConsoleInput, " ") > 0) Then
				StrTemp = Lower(Left(ConsoleInput, Instr(ConsoleInput, " ") - 1))
			Else
				StrTemp = Lower(ConsoleInput)
			EndIf

			;TODO: Overhaul this. Move all of the argument stuff to dedicated functions so this is actually readable/maintainable.
			Select (Lower(StrTemp))
				Case "help"
					If (Instr(ConsoleInput, " ")<>0) Then
						StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Else
						StrTemp = ""
					EndIf
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255

					Select (Lower(StrTemp))
						Case "1",""
							CreateConsoleMsg("LIST OF COMMANDS - PAGE 1/3")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("- asd")
							CreateConsoleMsg("- status")
							CreateConsoleMsg("- camerapick")
							CreateConsoleMsg("- ending")
							CreateConsoleMsg("- noclipspeed")
							CreateConsoleMsg("- noclip")
							CreateConsoleMsg("- injure [value]")
							CreateConsoleMsg("- infect [value]")
							CreateConsoleMsg("- heal")
							CreateConsoleMsg("- teleport [room name]")
							CreateConsoleMsg("- spawnitem [item name]")
							CreateConsoleMsg("- wireframe")
							CreateConsoleMsg("- 173speed")
							CreateConsoleMsg("- 106speed")
							CreateConsoleMsg("- 173state")
							CreateConsoleMsg("- 106state")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Use "+Chr(34)+"help 2/3"+Chr(34)+" to find more commands.")
							CreateConsoleMsg("Use "+Chr(34)+"help [command name]"+Chr(34)+" to get more information about a command.")
							CreateConsoleMsg("******************************")
						Case "2"
							CreateConsoleMsg("LIST OF COMMANDS - PAGE 2/3")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("- spawn513-1")
							CreateConsoleMsg("- spawn106")
							CreateConsoleMsg("- reset096")
							CreateConsoleMsg("- disable173")
							CreateConsoleMsg("- enable173")
							CreateConsoleMsg("- disable106")
							CreateConsoleMsg("- enable106")
							CreateConsoleMsg("- halloween")
							CreateConsoleMsg("- sanic")
							CreateConsoleMsg("- scp-420-j")
							CreateConsoleMsg("- godmode")
							CreateConsoleMsg("- revive")
							CreateConsoleMsg("- noclip")
							CreateConsoleMsg("- showfps")
							CreateConsoleMsg("- 096state")
							CreateConsoleMsg("- debughud")
							CreateConsoleMsg("- camerafog [near] [far]")
							CreateConsoleMsg("- gamma [value]")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Use "+Chr(34)+"help [command name]"+Chr(34)+" to get more information about a command.")
							CreateConsoleMsg("******************************")
						Case "3"
							CreateConsoleMsg("- spawn [npc type]")
							CreateConsoleMsg("- infinitestamina")
							CreateConsoleMsg("- notarget")
							CreateConsoleMsg("- spawnnpcstate [npc type] [state]")
							CreateConsoleMsg("- unlockexits")
						Case "asd"
							CreateConsoleMsg("HELP - asd")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Actives godmode, noclip, wireframe and")
							CreateConsoleMsg("sets fog distance to 20 near, 30 far")
							CreateConsoleMsg("******************************")
						Case "camerafog"
							CreateConsoleMsg("HELP - camerafog")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Sets the draw distance of the fog.")
							CreateConsoleMsg("The fog begins generating at 'CameraFogNear' units")
							CreateConsoleMsg("away from the camera and becomes completely opaque")
							CreateConsoleMsg("at 'CameraFogFar' units away from the camera.")
							CreateConsoleMsg("Example: camerafog 20 40")
							CreateConsoleMsg("******************************")
						Case "gamma"
							CreateConsoleMsg("HELP - gamma")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Sets the gamma correction.")
							CreateConsoleMsg("Should be set to a value between 0.0 and 2.0.")
							CreateConsoleMsg("Default is 1.0.")
							CreateConsoleMsg("******************************")
						Case "noclip","fly"
							CreateConsoleMsg("HELP - noclip")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles noclip, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Allows the camera to move in any direction while")
							CreateConsoleMsg("bypassing collision.")
							CreateConsoleMsg("******************************")
						Case "godmode"
							CreateConsoleMsg("HELP - godmode")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles godmode, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Prevents player death under normal circumstances.")
							CreateConsoleMsg("******************************")
						Case "wireframe"
							CreateConsoleMsg("HELP - wireframe")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles wireframe, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Allows only the edges of geometry to be rendered,")
							CreateConsoleMsg("making everything else transparent.")
							CreateConsoleMsg("******************************")
						Case "spawnitem"
							CreateConsoleMsg("HELP - spawnitem")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Spawns an item at the player's location.")
							CreateConsoleMsg("Any name that can appear in your inventory")
							CreateConsoleMsg("is a valid parameter.")
							CreateConsoleMsg("Example: spawnitem key5")
							CreateConsoleMsg("******************************")
						Case "spawn"
							CreateConsoleMsg("HELP - spawn")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Spawns an NPC at the player's location.")
							CreateConsoleMsg("Valid parameters are:")
							CreateConsoleMsg("049 / zombie (049-2) / 096 / 106 / 173 / 513-1")
							CreateConsoleMsg("/ 966 / 1499-1 / guard / mtf")
							CreateConsoleMsg("******************************")
						Case "revive","undead","resurrect"
							CreateConsoleMsg("HELP - revive")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Resets the player's death timer after the dying")
							CreateConsoleMsg("animation triggers.")
							CreateConsoleMsg("Does not affect injury, blood loss")
							CreateConsoleMsg("or 008 infection values.")
							CreateConsoleMsg("******************************")
						Case "teleport"
							CreateConsoleMsg("HELP - teleport")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Teleports the player to the first instance")
							CreateConsoleMsg("of the specified room. Any room that appears")
							CreateConsoleMsg("in rooms.ini is a valid parameter.")
							CreateConsoleMsg("******************************")
						Case "stopsound", "stfu"
							CreateConsoleMsg("HELP - stopsound")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Stops all currently playing sounds.")
							CreateConsoleMsg("******************************")
						Case "camerapick"
							CreateConsoleMsg("HELP - camerapick")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Prints the texture name and coordinates of")
							CreateConsoleMsg("the model the camera is pointing at.")
							CreateConsoleMsg("******************************")
						Case "status"
							CreateConsoleMsg("HELP - status")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Prints player, camera, and room information.")
							CreateConsoleMsg("******************************")
						Case "weed","scp-420-j","420"
							CreateConsoleMsg("HELP - 420")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Generates dank memes.")
							CreateConsoleMsg("******************************")

						Default
							CreateConsoleMsg("There is no help available for that command.",255,150,0)
					End Select

				Case "asd"
					WireFrame(1)
					WireframeState=1
					mainPlayer\godMode = True
					mainPlayer\noclip = True
					;CameraFogNear = 15
					;CameraFogFar = 20
				Case "status"
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
					CreateConsoleMsg("******************************")
					CreateConsoleMsg("Status: ")
					CreateConsoleMsg("Coordinates: ")
					CreateConsoleMsg("    - collider: "+Str(EntityX(mainPlayer\collider))+", "+Str(EntityY(mainPlayer\collider))+", "+Str(EntityZ(mainPlayer\collider)))
					CreateConsoleMsg("    - camera: "+Str(EntityX(mainPlayer\cam))+", "+Str(EntityY(mainPlayer\cam))+", "+Str(EntityZ(mainPlayer\cam)))

					CreateConsoleMsg("Rotation: ")
					CreateConsoleMsg("    - collider: "+Str(EntityPitch(mainPlayer\collider))+", "+Str(EntityYaw(mainPlayer\collider))+", "+Str(EntityRoll(mainPlayer\collider)))
					CreateConsoleMsg("    - camera: "+Str(EntityPitch(mainPlayer\cam))+", "+Str(EntityYaw(mainPlayer\cam))+", "+Str(EntityRoll(mainPlayer\cam)))

					CreateConsoleMsg("Room: "+mainPlayer\currRoom\roomTemplate\name)
					For ev = Each Event
						If (ev\room = mainPlayer\currRoom) Then
							CreateConsoleMsg("Room event: "+ev\name)
							CreateConsoleMsg("-    state: "+Str(ev\eventState))
							CreateConsoleMsg("-    state2: "+Str(ev\eventState2))
							CreateConsoleMsg("-    state3: "+Str(ev\eventState3))
							Exit
						EndIf
					Next

					CreateConsoleMsg("Room coordinates: "+Str(Floor(EntityX(mainPlayer\currRoom\obj) / 8.0 + 0.5))+", "+Str(Floor(EntityZ(mainPlayer\currRoom\obj) / 8.0 + 0.5)))
					CreateConsoleMsg("Stamina: "+Str(mainPlayer\stamina))
					;CreateConsoleMsg("Dead: "+mainPlayer\dead)
					CreateConsoleMsg("Blinktimer: "+Str(mainPlayer\blinkTimer))
					CreateConsoleMsg("Injuries: "+Str(mainPlayer\injuries))
					CreateConsoleMsg("Bloodloss: "+Str(mainPlayer\bloodloss))
					CreateConsoleMsg("******************************")

				Case "camerapick"
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
					c = CameraPick(mainPlayer\cam, userOptions\screenWidth / 2, userOptions\screenHeight / 2)
					If (c = 0) Then
						CreateConsoleMsg("******************************")
						CreateConsoleMsg("No entity picked")
						CreateConsoleMsg("******************************")
					Else
						CreateConsoleMsg("******************************")
						CreateConsoleMsg("Picked entity:")
						sf = GetSurface(c,1)
						b = GetSurfaceBrush( sf )
						t = GetBrushTexture(b,0)
						texname =  StripPath(TextureName(t))
						CreateConsoleMsg("Texture name: "+texname)
						CreateConsoleMsg("Coordinates: "+Str(EntityX(c))+", "+Str(EntityY(c))+", "+Str(EntityZ(c)))
						CreateConsoleMsg("******************************")
						FreeTexture(t)
						FreeBrush(b)
					EndIf

				Case "hidedistance"
					HideDistance = Float(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					CreateConsoleMsg("Hidedistance set to "+Str(HideDistance))

				Case "ending"
					CurrGameState = GAMESTATE_ENDING
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Kill(mainPlayer)

				Case "noclipspeed"
					RuntimeError("TODO: reimplement?")
					;StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					;NoClipSpeed = Float(StrTemp)

				Case "injure"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					mainPlayer\injuries = Float(StrTemp)

				Case "infect"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					mainPlayer\infect008 = Float(StrTemp)

				Case "heal"
					mainPlayer\injuries = 0
					mainPlayer\bloodloss = 0

				Case "teleport"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					For r = Each Room
						If (r\roomTemplate\name = StrTemp) Then
							;PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
							PositionEntity(mainPlayer\collider, r\x, r\y+0.7, r\z)
							ResetEntity(mainPlayer\collider)
							UpdateDoors()
							UpdateRooms()
							mainPlayer\currRoom = r
							Exit
						EndIf
					Next

					If (mainPlayer\currRoom\roomTemplate\name <> StrTemp) Then CreateConsoleMsg("Room not found.",255,150,0)

				Case "guh"
					For r = Each Room
						If (r\roomTemplate\name = "cont_914_1") Then
							;PositionEntity(mainPlayer\collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
							PositionEntity(mainPlayer\collider, r\x, r\y+0.7, r\z)
							ResetEntity(mainPlayer\collider)
							UpdateDoors()
							UpdateRooms()
							mainPlayer\currRoom = r
							CreateItem("gasmask", r\x, r\y+2, r\z)
							Exit
						EndIf
					Next

				Case "spawnitem"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					temp = False
					For itt = Each ItemTemplate
						If (Lower(itt\name) = StrTemp) Then
							temp = True
							CreateConsoleMsg(itt\name + " spawned.")
							it = CreateItem(itt\name, EntityX(mainPlayer\collider), EntityY(mainPlayer\cam,True), EntityZ(mainPlayer\collider))
							Exit
						EndIf
					Next

					If (temp = False) Then CreateConsoleMsg("Item not found.",255,150,0)

				Case "spawndoc"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					CreatePaper(StrTemp, EntityX(mainPlayer\collider), EntityY(mainPlayer\cam,True), EntityZ(mainPlayer\collider))

				Case "test914key"
					it = CreateItem("keycard", EntityX(mainPlayer\collider), EntityY(mainPlayer\cam,True), EntityZ(mainPlayer\collider))
					AssignTag(it, "cont_914_1")

				Case "spawnomni"
					it = CreateItem("keycard", EntityX(mainPlayer\collider), EntityY(mainPlayer\cam,True), EntityZ(mainPlayer\collider))
					AssignTag(it, ITEM_TAG_OMNI)
					CreateConsoleMsg("Nerd.",255,150,0)

				Case "wireframe"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Select (StrTemp)
						Case "on", "1", "true"
							WireframeState = True
						Case "off", "0", "false"
							WireframeState = False
						Default
							WireframeState = Not WireframeState
					End Select

					If (WireframeState) Then
						CreateConsoleMsg("WIREFRAME ON")
					Else
						CreateConsoleMsg("WIREFRAME OFF")
					EndIf

					WireFrame(WireframeState)

				Case "173speed"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Curr173\speed = Float(StrTemp)
					CreateConsoleMsg("173's speed set to " + StrTemp)

				Case "106speed"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Curr106\speed = Float(StrTemp)
					CreateConsoleMsg("106's speed set to " + StrTemp)

				Case "173state"
					CreateConsoleMsg("SCP-173")
					CreateConsoleMsg("Position: " + Str(EntityX(Curr173\obj)) + ", " + Str(EntityY(Curr173\obj)) + ", " + Str(EntityZ(Curr173\obj)))
					CreateConsoleMsg("Idle: " + Str(Curr173\idle))
					CreateConsoleMsg("State: " + Str(Curr173\state))

				Case "106state"
					CreateConsoleMsg("SCP-106")
					CreateConsoleMsg("Position: " + Str(EntityX(Curr106\obj)) + ", " + Str(EntityY(Curr106\obj)) + ", " + Str(EntityZ(Curr106\obj)))
					CreateConsoleMsg("Idle: " + Str(Curr106\idle))
					CreateConsoleMsg("State: " + Str(Curr106\state))

				Case "spawn513-1"
					CreateNPC(NPCtype5131, 0,0,0)

				Case "spawn106"
					Curr106\state = -1
					PositionEntity(Curr106\collider, EntityX(mainPlayer\collider), EntityY(Curr106\collider), EntityZ(mainPlayer\collider))

				Case "reset096"
					For n = Each NPC
						If (n\npcType = NPCtype096) Then
							n\state = 0
							Exit
						EndIf
					Next

				Case "disable173"
					Curr173\idle = 3 ;TODO: clean up
					HideEntity(Curr173\obj)
					HideEntity(Curr173\collider)

				Case "enable173"
					Curr173\idle = False
					ShowEntity(Curr173\obj)
					ShowEntity(Curr173\collider)

				Case "disable106"
					Curr106\idle = True
					Curr106\state = 200000
					Contained106 = True

				Case "enable106"
					Curr106\idle = False
					Contained106 = False
					ShowEntity(Curr106\collider)
					ShowEntity(Curr106\obj)

				Case "halloween"
					HalloweenTex = Not HalloweenTex
					If (HalloweenTex) Then
						tex = LoadTexture("GFX/npcs/173h.pt", 1)
						EntityTexture(Curr173\obj, tex, 0, 0)
						FreeTexture(tex)
						CreateConsoleMsg("173 JACK-O-LANTERN ON")
					Else
						tex2 = LoadTexture("GFX/npcs/173texture.png", 1)
						EntityTexture(Curr173\obj, tex2, 0, 0)
						FreeTexture(tex2)
						CreateConsoleMsg("173 JACK-O-LANTERN OFF")
					EndIf

				Case "sanic"
					mainPlayer\superMan = Not mainPlayer\superMan
					If (mainPlayer\superMan = True) Then
						CreateConsoleMsg("GOTTA GO FAST")
					Else
						CreateConsoleMsg("WHOA SLOW DOWN")
					EndIf
				Case "godmode"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Select (StrTemp)
						Case "on", "1", "true"
							mainPlayer\godMode = True
						Case "off", "0", "false"
							mainPlayer\godMode = False
						Default
							mainPlayer\godMode = Not mainPlayer\godMode
					End Select
					If (mainPlayer\godMode) Then
						CreateConsoleMsg("GODMODE ON")
					Else
						CreateConsoleMsg("GODMODE OFF")
					EndIf

				Case "revive","undead","resurrect"
					mainPlayer\dead = False

					mainPlayer\dropSpeed = -0.1
					mainPlayer\camShake = 0
					mainPlayer\moveSpeed = 0

					mainPlayer\heartbeatIntensity = 0

					mainPlayer\lightFlash = 0
					mainPlayer\blurTimer = 0

					mainPlayer\fallTimer = 0

					mainPlayer\godMode = 0
					mainPlayer\noclip = 0

					ShowEntity(mainPlayer\collider)

				Case "noclip","fly"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Select (StrTemp)
						Case "on", "1", "true"
							mainPlayer\noclip = True
							mainPlayer\disableControls = False
						Case "off", "0", "false"
							mainPlayer\noclip = False
							RotateEntity(mainPlayer\collider, 0, EntityYaw(mainPlayer\collider), 0)
						Default
							mainPlayer\noclip = Not mainPlayer\noclip
							If (mainPlayer\noclip = False) Then
								RotateEntity(mainPlayer\collider, 0, EntityYaw(mainPlayer\collider), 0)
							Else
								mainPlayer\disableControls = False
							EndIf
					End Select

					If (mainPlayer\noclip) Then
						CreateConsoleMsg("NOCLIP ON")
					Else
						CreateConsoleMsg("NOCLIP OFF")
					EndIf

					mainPlayer\dropSpeed = 0

				Case "showfps"
					userOptions\showFPS = Not userOptions\showFPS
					CreateConsoleMsg("ShowFPS: " + Str(userOptions\showFPS))

				Case "096state"
					For n = Each NPC
						If (n\npcType = NPCtype096) Then
							CreateConsoleMsg("SCP-096")
							CreateConsoleMsg("Position: " + Str(EntityX(n\obj)) + ", " + Str(EntityY(n\obj)) + ", " + Str(EntityZ(n\obj)))
							CreateConsoleMsg("Idle: " + Str(n\idle))
							CreateConsoleMsg("State: " + Str(n\state))
							Exit
						EndIf
					Next
					CreateConsoleMsg("SCP-096 has not spawned.")

				Case "debughud"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Select (StrTemp)
						Case "on", "1", "true"
							DebugHUD = True
						Case "off", "0", "false"
							DebugHUD = False
						Default
							DebugHUD = Not DebugHUD
					End Select

					If (DebugHUD) Then
						CreateConsoleMsg("Debug Mode On")
					Else
						CreateConsoleMsg("Debug Mode Off")
					EndIf

				Case "stopsound", "stfu"
					StopSounds()

				Case "camerafog"
					RuntimeError("TODO: reimplement?")
					;args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					;CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
					;CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
					;CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

				Case "gamma"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					userOptions\screenGamma = Float(StrTemp)
					CreateConsoleMsg("Gamma set to " + Str(userOptions\screenGamma))

				Case "spawn"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Console_SpawnNPC(StrTemp)

				Case "infinitestamina","infstam"
					RuntimeError("TODO: reimplement?")

					;StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					;Select (StrTemp)
					;	Case "on", "1", "true"
					;		InfiniteStamina% = True
					;	Case "off", "0", "false"
					;		InfiniteStamina% = False
					;	Default
					;		InfiniteStamina% = Not InfiniteStamina%
					;End Select

					;If InfiniteStamina
					;	CreateConsoleMsg("INFINITE STAMINA ON")
					;Else
					;	CreateConsoleMsg("INFINITE STAMINA OFF")
					;EndIf

				Case "spawnnpcstate"
					args = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					StrTemp = Piece$(args,1," ")
					StrTemp2 = Piece$(args,2," ")
					Console_SpawnNPC(StrTemp,Int(StrTemp2))

				Case "toggle_warhead_lever"
					For ev = Each Event
						If (ev\name = "room2nuke") Then
							ev\eventState = (Not ev\eventState)
							Exit
						EndIf
					Next

				Case "unlockexits"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Select (StrTemp)
						Case "a"
							For ev = Each Event
								If (ev\name = "gateaentrance") Then
									ev\eventState3 = 1
									ev\room\doors[1]\open = True
									Exit
								EndIf
							Next
							CreateConsoleMsg("Gate A is now unlocked.")
						Case "b"
							For ev = Each Event
								If (ev\name = "exit1") Then
									ev\eventState3 = 1
									ev\room\doors[4]\open = True
									Exit
								EndIf
							Next
							CreateConsoleMsg("Gate B is now unlocked.")
						Default
							For ev = Each Event
								If (ev\name = "gateaentrance") Then
									ev\eventState3 = 1
									ev\room\doors[1]\open = True
								ElseIf (ev\name = "exit1") Then
									ev\eventState3 = 1
									ev\room\doors[4]\open = True
								EndIf
							Next
							CreateConsoleMsg("Gate A and B are now unlocked.")
					End Select

					RemoteDoorOn = True

				Case "kill","suicide"
					Kill(mainPlayer)

					Select (Rand(4))
						Case 1
							DeathMSG = "[REDACTED]"
						Case 2
							DeathMSG = "Subject D-9341 found dead in Sector [REDACTED]. "
							DeathMSG = DeathMSG + "The subject appears to have attained no physical damage, and there is no visible indication as to what killed him. "
							DeathMSG = DeathMSG + "Body was sent for autopsy."
						Case 3
							DeathMSG = "EXCP_ACCESS_VIOLATION"
						Case 4
							DeathMSG = "Subject D-9341 found dead in Sector [REDACTED]. "
							DeathMSG = DeathMSG + "The subject appears to have scribbled the letters "+Chr(34)+"kys"+Chr(34)+" in his own blood beside him. "
							DeathMSG = DeathMSG + "No other signs of physical trauma or struggle can be observed. Body was sent for autopsy."
					End Select

				Case "tp_to_mtf"
					For n = Each NPC
						If (n\npcType = NPCtypeMTF) Then
							If (n\mtfLeader = Null) Then
								PositionEntity(mainPlayer\collider,EntityX(n\collider),EntityY(n\collider)+5,EntityZ(n\collider))
								ResetEntity(mainPlayer\collider)
								Exit
							EndIf
						EndIf
					Next

				Case "tele"
					args = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					StrTemp = Piece$(args,1," ")
					StrTemp2 = Piece$(args,2," ")
					StrTemp3 = Piece$(args,3," ")
					PositionEntity(mainPlayer\collider,Float(StrTemp),Float(StrTemp2),Float(StrTemp3))
					PositionEntity(mainPlayer\cam,Float(StrTemp),Float(StrTemp2),Float(StrTemp3))
					CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+Str(EntityX(mainPlayer\collider))+"|"+Str(EntityY(mainPlayer\collider))+"|"+Str(EntityZ(mainPlayer\collider)))

				Case "notarget"
					StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))

					Select (StrTemp)
						Case "on", "1", "true"
							NoTarget = True
						Case "off", "0", "false"
							NoTarget = False
						Default
							NoTarget = Not NoTarget
					End Select

					If (NoTarget = False) Then
						CreateConsoleMsg("NOTARGET OFF")
					Else
						CreateConsoleMsg("NOTARGET ON")
					EndIf
				Case "teleport173"
					PositionEntity(Curr173\collider,EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
					ResetEntity(Curr173\collider)
				Case Chr($6A)+Chr($6F)+Chr($72)+Chr($67)+Chr($65) ;TODO
					RuntimeError("Implement")
				Default
					CreateConsoleMsg("Command not found.",255,0,0)
			End Select

			ConsoleInput = ""
		EndIf
	EndIf

	SetFont(uiAssets\font[0])

End Function

ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
CreateConsoleMsg("Console commands: ")
CreateConsoleMsg("  - teleport [room name]")
CreateConsoleMsg("  - godmode [on/off]")
CreateConsoleMsg("  - noclip [on/off]")
CreateConsoleMsg("  - noclipspeed [x] (default = 2.0)")
CreateConsoleMsg("  - wireframe [on/off]")
CreateConsoleMsg("  - debughud [on/off]")
CreateConsoleMsg("  - camerafog [near] [far]")
CreateConsoleMsg(" ")
CreateConsoleMsg("  - status")
CreateConsoleMsg("  - heal")
CreateConsoleMsg(" ")
CreateConsoleMsg("  - spawnitem [item name]")
CreateConsoleMsg(" ")
CreateConsoleMsg("  - 173speed [x] (default = 35)")
CreateConsoleMsg("  - disable173/enable173")
CreateConsoleMsg("  - disable106/enable106")
CreateConsoleMsg("  - 173state/106state/096state")
CreateConsoleMsg("  - spawn [npc type]")

;---------------------------------------------------------------------------------------------------
;~IDEal Editor Parameters:
;~C#Blitz3D
;----------------------------------------------  Console -----------------------------------------------------

Global ConsoleOpen%, ConsoleInput$
Global ConsoleScroll#,ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR% = 255,ConsoleG% = 255,ConsoleB% = 255

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

Function UpdateConsole()
	
	If CanOpenConsole = False Then
		ConsoleOpen = False
		Return
	EndIf
	
	If ConsoleOpen Then
		Local cm.ConsoleMsg
	
		SetFont ConsoleFont
		
		ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
	
		Local x% = 0, y% = GraphicHeight-300*MenuScale, width% = GraphicWidth, height% = 300*MenuScale-30*MenuScale
		Local StrTemp$, temp%,  i%
		Local ev.Events, r.Rooms, it.Items
		
		DrawFrame x,y,width,height+30*MenuScale
		
		Local consoleHeight% = 0
		Local scrollbarHeight% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			consoleHeight = consoleHeight + 15*MenuScale
		Next
		scrollbarHeight = (Float(height)/Float(consoleHeight))*height
		If scrollbarHeight>height Then scrollbarHeight = height
		If consoleHeight<height Then consoleHeight = height
		
		Color 50,50,50
		inBar% = MouseOn(x+width-26*MenuScale,y,26*MenuScale,height)
		If inBar Then Color 70,70,70
		Rect x+width-26*MenuScale,y,26*MenuScale,height,True
		
		
		Color 120,120,120
		inBox% = MouseOn(x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight)
		If inBox Then Color 200,200,200
		If ConsoleScrollDragging Then Color 255,255,255
		Rect x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight,True
		
		If Not MouseDown(1) Then
			ConsoleScrollDragging=False
		ElseIf ConsoleScrollDragging Then
			ConsoleScroll = ConsoleScroll+((ScaledMouseY()-ConsoleMouseMem)*height/scrollbarHeight)
			ConsoleMouseMem = ScaledMouseY()
		EndIf
		
		If (Not ConsoleScrollDragging) Then
			If MouseHit1 Then
				If inBox Then
					ConsoleScrollDragging=True
					ConsoleMouseMem = ScaledMouseY()
				ElseIf inBar Then
					ConsoleScroll = ConsoleScroll+((ScaledMouseY()-(y+height))*consoleHeight/height+(height/2))
					ConsoleScroll = ConsoleScroll/2
				EndIf
			EndIf
		EndIf
		
		mouseScroll = MouseZSpeed()
		If mouseScroll=1 Then
			ConsoleScroll = ConsoleScroll - 15*MenuScale
		ElseIf mouseScroll=-1 Then
			ConsoleScroll = ConsoleScroll + 15*MenuScale
		EndIf
		
		Local reissuePos%
		If KeyHit(200) Then
			reissuePos% = 0
			If (ConsoleReissue=Null) Then
				ConsoleReissue=First ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = First ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos-15*MenuScale
					cm = After cm
				Wend
				ConsoleReissue = After ConsoleReissue
				reissuePos = reissuePos-15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=First ConsoleMsg
						reissuePos = 0
					EndIf
				
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf
		
		If KeyHit(208) Then
			reissuePos% = -consoleHeight+15*MenuScale
			If (ConsoleReissue=Null) Then
				ConsoleReissue=Last ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = Last ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos+15*MenuScale
					cm = Before cm
				Wend
				ConsoleReissue = Before ConsoleReissue
				reissuePos = reissuePos+15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=Last ConsoleMsg
						reissuePos=-consoleHeight+15*MenuScale
					EndIf
				
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf
		
		If ConsoleScroll<-consoleHeight+height Then ConsoleScroll = -consoleHeight+height
		If ConsoleScroll>0 Then ConsoleScroll = 0
		
		Color 255, 255, 255
		
		SelectedInputBox = 2
		Local oldConsoleInput$ = ConsoleInput
		ConsoleInput = InputBox(x, y + height, width, 30*MenuScale, ConsoleInput, 2)
		If oldConsoleInput<>ConsoleInput Then
			ConsoleReissue = Null
		EndIf
		ConsoleInput = Left(ConsoleInput, 100)
		
		If KeyHit(28) And ConsoleInput <> "" Then
			ConsoleReissue = Null
			ConsoleScroll = 0
			CreateConsoleMsg(ConsoleInput,255,255,0,True)
			If Instr(ConsoleInput, " ") > 0 Then
				StrTemp$ = Lower(Left(ConsoleInput, Instr(ConsoleInput, " ") - 1))
			Else
				StrTemp$ = Lower(ConsoleInput)
			End If
			
			Select Lower(StrTemp)
				Case "help"
					If Instr(ConsoleInput, " ")<>0 Then
						StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Else
						StrTemp$ = ""
					EndIf
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
					
					Select Lower(StrTemp)
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
							CreateConsoleMsg("- playmusic [clip + .wav/.ogg]")
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
							CreateConsoleMsg("Example: spawnitem Key Card Omni")
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
						Case "playmusic"
							CreateConsoleMsg("HELP - playmusic")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Will play tracks in .ogg/.wav format")
							CreateConsoleMsg("from "+Chr(34)+"SFX\Music\Custom\"+Chr(34)+".")
							CreateConsoleMsg("******************************")
							
						Default
							CreateConsoleMsg("There is no help available for that command.",255,150,0)
					End Select
					
				Case "asd"
					WireFrame 1
					WireframeState=1
					GodMode = 1
					NoClip = 1
					CameraFogNear = 15
					CameraFogFar = 20

				Case "status"
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
					CreateConsoleMsg("******************************")
					CreateConsoleMsg("Status: ")
					CreateConsoleMsg("Coordinates: ")
					CreateConsoleMsg("    - collider: "+EntityX(Collider)+", "+EntityY(Collider)+", "+EntityZ(Collider))
					CreateConsoleMsg("    - camera: "+EntityX(Camera)+", "+EntityY(Camera)+", "+EntityZ(Camera))
					
					CreateConsoleMsg("Rotation: ")
					CreateConsoleMsg("    - collider: "+EntityPitch(Collider)+", "+EntityYaw(Collider)+", "+EntityRoll(Collider))
					CreateConsoleMsg("    - camera: "+EntityPitch(Camera)+", "+EntityYaw(Camera)+", "+EntityRoll(Camera))
					
					CreateConsoleMsg("Room: "+PlayerRoom\RoomTemplate\Name)
					For ev.Events = Each Events
						If ev\room = PlayerRoom Then
							CreateConsoleMsg("Room event: "+ev\EventName)	
							CreateConsoleMsg("-    state: "+ev\EventState)
							CreateConsoleMsg("-    state2: "+ev\EventState2)	
							CreateConsoleMsg("-    state3: "+ev\EventState3)
							Exit
						EndIf
					Next
					
					CreateConsoleMsg("Room coordinates: "+Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5)+", "+ Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5))
					CreateConsoleMsg("Stamina: "+Stamina)
					CreateConsoleMsg("Death timer: "+KillTimer)					
					CreateConsoleMsg("Blinktimer: "+BlinkTimer)
					CreateConsoleMsg("Injuries: "+Injuries)
					CreateConsoleMsg("Bloodloss: "+Bloodloss)
					CreateConsoleMsg("******************************")

				Case "camerapick"
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
					c = CameraPick(Camera,GraphicWidth/2, GraphicHeight/2)
					If c = 0 Then
						CreateConsoleMsg("******************************")
						CreateConsoleMsg("No entity  picked")
						CreateConsoleMsg("******************************")								
					Else
						CreateConsoleMsg("******************************")
						CreateConsoleMsg("Picked entity:")
						sf = GetSurface(c,1)
						b = GetSurfaceBrush( sf )
						t = GetBrushTexture(b,0)
						texname$ =  StripPath(TextureName(t))
						CreateConsoleMsg("Texture name: "+texname)
						CreateConsoleMsg("Coordinates: "+EntityX(c)+", "+EntityY(c)+", "+EntityZ(c))
						CreateConsoleMsg("******************************")							
					EndIf

				Case "hidedistance"
					HideDistance = Float(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					CreateConsoleMsg("Hidedistance set to "+HideDistance)		

				Case "ending"
					SelectedEnding = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					KillTimer = -0.1
					;EndingTimer = -0.1

				Case "noclipspeed"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					NoClipSpeed = Float(StrTemp)

				Case "injure"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Injuries = Float(StrTemp)

				Case "infect"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Infect = Float(StrTemp)

				Case "heal"
					Injuries = 0
					Bloodloss = 0

				Case "teleport"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "895", "scp-895"
							StrTemp = "coffin"
						Case "scp-914"
							StrTemp = "914"
						Case "offices", "office"
							StrTemp = "room2offices"
					End Select
					
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name = StrTemp Then
							;PositionEntity (Collider, EntityX(r\obj), 0.7, EntityZ(r\obj))
							PositionEntity (Collider, EntityX(r\obj), EntityY(r\obj)+0.7, EntityZ(r\obj))
							ResetEntity(Collider)
							UpdateDoors()
							UpdateRooms()
							For it.Items = Each Items
								it\disttimer = 0
							Next
							PlayerRoom = r
							Exit
						EndIf
					Next
					
					If PlayerRoom\RoomTemplate\Name <> StrTemp Then CreateConsoleMsg("Room not found.",255,150,0)
					
				Case "spawnitem"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					temp = False 
					For itt.Itemtemplates = Each ItemTemplates
						If (Lower(itt\name) = StrTemp) Then
							temp = True
							CreateConsoleMsg(itt\name + " spawned.")
							it.Items = CreateItem(itt\name, itt\tempname, EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
							EntityType(it\collider, HIT_ITEM)
							Exit
						Else If (Lower(itt\tempname) = StrTemp) Then
							temp = True
							CreateConsoleMsg(itt\name + " spawned.")
							it.Items = CreateItem(itt\name, itt\tempname, EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
							EntityType(it\collider, HIT_ITEM)
							Exit
						End If
					Next
					
					If temp = False Then CreateConsoleMsg("Item not found.",255,150,0)

				Case "wireframe"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "on", "1", "true"
							WireframeState = True							
						Case "off", "0", "false"
							WireframeState = False
						Default
							WireframeState = Not WireframeState
					End Select
					
					If WireframeState Then
						CreateConsoleMsg("WIREFRAME ON")
					Else
						CreateConsoleMsg("WIREFRAME OFF")	
					EndIf
					
					WireFrame WireframeState

				Case "173speed"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Curr173\Speed = Float(StrTemp)
					CreateConsoleMsg("173's speed set to " + StrTemp)

				Case "106speed"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Curr106\Speed = Float(StrTemp)
					CreateConsoleMsg("106's speed set to " + StrTemp)

				Case "173state"
					CreateConsoleMsg("SCP-173")
					CreateConsoleMsg("Position: " + EntityX(Curr173\obj) + ", " + EntityY(Curr173\obj) + ", " + EntityZ(Curr173\obj))
					CreateConsoleMsg("Idle: " + Curr173\Idle)
					CreateConsoleMsg("State: " + Curr173\State)

				Case "106state"
					CreateConsoleMsg("SCP-106")
					CreateConsoleMsg("Position: " + EntityX(Curr106\obj) + ", " + EntityY(Curr106\obj) + ", " + EntityZ(Curr106\obj))
					CreateConsoleMsg("Idle: " + Curr106\Idle)
					CreateConsoleMsg("State: " + Curr106\State)

				Case "spawn513-1"
					CreateNPC(NPCtype5131, 0,0,0)

				Case "spawn106"
					Curr106\State = -1
					PositionEntity Curr106\Collider, EntityX(Collider), EntityY(Curr106\Collider), EntityZ(Collider)

				Case "reset096"
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype096 Then
							n\State = 0
							Exit
						EndIf
					Next

				Case "disable173"
					Curr173\Idle = 3 ;This phenominal comment is brought to you by PolyFox. His absolute wisdom in this fatigue of knowledge brought about a new era of 173 state checks.
					HideEntity Curr173\obj
					HideEntity Curr173\Collider

				Case "enable173"
					Curr173\Idle = False
					ShowEntity Curr173\obj
					ShowEntity Curr173\Collider

				Case "disable106"
					Curr106\Idle = True
					Curr106\State = 200000
					Contained106 = True

				Case "enable106"
					Curr106\Idle = False
					Contained106 = False
					ShowEntity Curr106\Collider
					ShowEntity Curr106\obj

				Case "halloween"
					HalloweenTex = Not HalloweenTex
					If HalloweenTex Then
						Local tex = LoadTexture_Strict("GFX\npcs\173h.pt", 1)
						EntityTexture Curr173\obj, tex, 0, 0
						FreeTexture tex
						CreateConsoleMsg("173 JACK-O-LANTERN ON")
					Else
						Local tex2 = LoadTexture_Strict("GFX\npcs\173texture.png", 1)
						EntityTexture Curr173\obj, tex2, 0, 0
						FreeTexture tex2
						CreateConsoleMsg("173 JACK-O-LANTERN OFF")
					EndIf

				Case "sanic"
					SuperMan = Not SuperMan
					If SuperMan = True Then
						CreateConsoleMsg("GOTTA GO FAST")
					Else
						CreateConsoleMsg("WHOA SLOW DOWN")
					EndIf

				Case "scp-420-j","420","weed"
					For i = 1 To 20
						If Rand(2)=1 Then
							it.Items = CreateItem("Some SCP-420-J","420", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
						Else
							it.Items = CreateItem("Joint","420s", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
						EndIf
						EntityType (it\collider, HIT_ITEM)
					Next
					PlaySound_Strict LoadTempSound("SFX\Music\420J.ogg")

				Case "godmode"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "on", "1", "true"
							GodMode = True						
						Case "off", "0", "false"
							GodMode = False
						Default
							GodMode = Not GodMode
					End Select	
					If GodMode Then
						CreateConsoleMsg("GODMODE ON")
					Else
						CreateConsoleMsg("GODMODE OFF")	
					EndIf

				Case "revive","undead","resurrect"
					DropSpeed = -0.1
					HeadDropSpeed = 0.0
					Shake = 0
					CurrSpeed = 0
					
					HeartBeatVolume = 0
					
					CameraShake = 0
					Shake = 0
					LightFlash = 0
					BlurTimer = 0
					
					FallTimer = 0
					MenuOpen = False
					
					GodMode = 0
					NoClip = 0
					
					ShowEntity Collider
					
					KillTimer = 0
					KillAnim = 0
					
				Case "noclip","fly"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "on", "1", "true"
							NoClip = True
							Playable = True
						Case "off", "0", "false"
							NoClip = False	
							RotateEntity Collider, 0, EntityYaw(Collider), 0
						Default
							NoClip = Not NoClip
							If NoClip = False Then		
								RotateEntity Collider, 0, EntityYaw(Collider), 0
							Else
								Playable = True
							EndIf
					End Select

					If NoClip Then
						CreateConsoleMsg("NOCLIP ON")
					Else
						CreateConsoleMsg("NOCLIP OFF")
					EndIf
					
					DropSpeed = 0
					
				Case "showfps"
					ShowFPS = Not ShowFPS
					CreateConsoleMsg("ShowFPS: "+Str(ShowFPS))
					
				Case "096state"
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype096 Then
							CreateConsoleMsg("SCP-096")
							CreateConsoleMsg("Position: " + EntityX(n\obj) + ", " + EntityY(n\obj) + ", " + EntityZ(n\obj))
							CreateConsoleMsg("Idle: " + n\Idle)
							CreateConsoleMsg("State: " + n\State)
							Exit
						EndIf
					Next
					CreateConsoleMsg("SCP-096 has not spawned.")
					
				Case "debughud"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Select StrTemp
						Case "on", "1", "true"
							DebugHUD = True
						Case "off", "0", "false"
							DebugHUD = False
						Default
							DebugHUD = Not DebugHUD
					End Select

					If DebugHUD Then
						CreateConsoleMsg("Debug Mode On")
					Else
						CreateConsoleMsg("Debug Mode Off")
					EndIf

				Case "stopsound", "stfu"
					For snd.Sound = Each Sound
						For i = 0 To 31
							If snd\channels[i]<>0 Then
								StopChannel snd\channels[i]
							EndIf
						Next
					Next

					For e.events = Each Events
						If e\eventname = "alarm" Then 
							If e\room\NPC[0] <> Null Then RemoveNPC(e\room\NPC[0])
							If e\room\NPC[1] <> Null Then RemoveNPC(e\room\NPC[1])
							If e\room\NPC[2] <> Null Then RemoveNPC(e\room\NPC[2])
							
							FreeEntity e\room\Objects[0]
							FreeEntity e\room\Objects[1]
							PositionEntity Curr173\Collider, 0,0,0
							ResetEntity Curr173\Collider
							RemoveEvent(e)
							Exit
						EndIf
					Next
					CreateConsoleMsg("Stopped all sounds.")

					
				Case "camerafog"
					args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
					CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
					CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)
					
				Case "gamma"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					ScreenGamma = Int(StrTemp)
					CreateConsoleMsg("Gamma set to " + ScreenGamma)

				Case "spawn"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Console_SpawnNPC(StrTemp$)

				Case "infinitestamina","infstam"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "on", "1", "true"
							InfiniteStamina% = True						
						Case "off", "0", "false"
							InfiniteStamina% = False
						Default
							InfiniteStamina% = Not InfiniteStamina%
					End Select

					If InfiniteStamina
						CreateConsoleMsg("INFINITE STAMINA ON")
					Else
						CreateConsoleMsg("INFINITE STAMINA OFF")	
					EndIf
					
				Case "asd2"
					GodMode = 1
					InfiniteStamina = 1
					Curr173\Idle = 3
					Curr106\Idle = True
					Curr106\State = 200000
					Contained106 = True

				Case "spawnnpcstate"
					args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					StrTemp$ = Piece$(args$,1," ")
					StrTemp2$ = Piece$(args$,2," ")
					Console_SpawnNPC(StrTemp$,Int(StrTemp2$))

				Case "toggle_warhead_lever"
					For e.Events = Each Events
						If e\EventName = "room2nuke" Then
							e\EventState = (Not e\EventState)
							Exit
						EndIf
					Next
					
				Case "unlockexits"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "a"
							For e.Events = Each Events
								If e\EventName = "gateaentrance" Then
									e\EventState3 = 1
									e\room\RoomDoors[1]\open = True
									Exit
								EndIf
							Next
							CreateConsoleMsg("Gate A is now unlocked.")	
						Case "b"
							For e.Events = Each Events
								If e\EventName = "exit1" Then
									e\EventState3 = 1
									e\room\RoomDoors[4]\open = True
									Exit
								EndIf
							Next	
							CreateConsoleMsg("Gate B is now unlocked.")	
						Default
							For e.Events = Each Events
								If e\EventName = "gateaentrance" Then
									e\EventState3 = 1
									e\room\RoomDoors[1]\open = True
								ElseIf e\EventName = "exit1" Then
									e\EventState3 = 1
									e\room\RoomDoors[4]\open = True
								EndIf
							Next
							CreateConsoleMsg("Gate A and B are now unlocked.")	
					End Select

					RemoteDoorOn = True

				Case "kill","suicide"
					KillTimer = -1
					Select Rand(4)
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

				Case "playmusic"
					If Instr(ConsoleInput, " ")<>0 Then
						StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Else
						StrTemp$ = ""
					EndIf
					
					If StrTemp$ <> ""
						PlayCustomMusic% = True
						If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
						If MusicCHN <> 0 Then StopChannel MusicCHN
						CustomMusic = LoadSound_Strict("SFX\Music\Custom\"+StrTemp$)
						If CustomMusic = 0
							PlayCustomMusic% = False
						EndIf
					Else
						PlayCustomMusic% = False
						If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
						If MusicCHN <> 0 Then StopChannel MusicCHN
					EndIf

				Case "tp"
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtypeMTF
							If n\MTFLeader = Null
								PositionEntity Collider,EntityX(n\Collider),EntityY(n\Collider)+5,EntityZ(n\Collider)
								ResetEntity Collider
								Exit
							EndIf
						EndIf
					Next

				Case "tele"
					args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					StrTemp$ = Piece$(args$,1," ")
					StrTemp2$ = Piece$(args$,2," ")
					StrTemp3$ = Piece$(args$,3," ")
					PositionEntity Collider,StrTemp$,StrTemp2$,StrTemp3$
					PositionEntity Camera,StrTemp$,StrTemp2$,StrTemp3$
					CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+EntityX(Collider)+"|"+EntityY(Collider)+"|"+EntityZ(Collider))

				Case "notarget"
					StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
					Select StrTemp
						Case "on", "1", "true"
							NoTarget% = True						
						Case "off", "0", "false"
							NoTarget% = False	
						Default
							NoTarget% = Not NoTarget%
					End Select

					If NoTarget% = False Then
						CreateConsoleMsg("NOTARGET OFF")
					Else
						CreateConsoleMsg("NOTARGET ON")	
					EndIf

				Case "spawnradio"
					it.Items = CreateItem("Radio Transceiver", "fineradio", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
					EntityType(it\collider, HIT_ITEM)
					it\state = 101
				Case "spawnnvg"
					it.Items = CreateItem("Night Vision Goggles", "nvgoggles", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
					EntityType(it\collider, HIT_ITEM)
					it\state = 1000
				Case "spawnpumpkin","pumpkin"
					CreateConsoleMsg("What pumpkin?")
				Case "spawnnav"
					it.Items = CreateItem("S-NAV Navigator Ultimate", "nav", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
					EntityType(it\collider, HIT_ITEM)
					it\state = 101
				Case "teleport173"
					PositionEntity Curr173\Collider,EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider)
					ResetEntity Curr173\Collider
				Case Chr($6A)+Chr($6F)+Chr($72)+Chr($67)+Chr($65)
					ConsoleFlush = True 
					
					If ConsoleFlushSnd = 0 Then
						ConsoleFlushSnd = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(83)+Chr(67)+Chr(80)+Chr(92)+Chr(57)+Chr(55)+Chr(48)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
						If MusicCHN <> 0 Then StopChannel MusicCHN
						ConsoleMusFlush% = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(77)+Chr(117)+Chr(115)+Chr(105)+Chr(99)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
						CreateConsoleMsg(Chr(74)+Chr(79)+Chr(82)+Chr(71)+Chr(69)+Chr(32)+Chr(72)+Chr(65)+Chr(83)+Chr(32)+Chr(66)+Chr(69)+Chr(69)+Chr(78)+Chr(32)+Chr(69)+Chr(88)+Chr(80)+Chr(69)+Chr(67)+Chr(84)+Chr(73)+Chr(78)+Chr(71)+Chr(32)+Chr(89)+Chr(79)+Chr(85)+Chr(46))
					Else
						CreateConsoleMsg(Chr(74)+Chr(32)+Chr(79)+Chr(32)+Chr(82)+Chr(32)+Chr(71)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(65)+Chr(32)+Chr(78)+Chr(32)+Chr(78)+Chr(32)+Chr(79)+Chr(32)+Chr(84)+Chr(32)+Chr(32)+Chr(66)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(79)+Chr(32)+Chr(78)+Chr(32)+Chr(84)+Chr(32)+Chr(65)+Chr(32)+Chr(73)+Chr(32)+Chr(78)+Chr(32)+Chr(69)+Chr(32)+Chr(68)+Chr(46))
					EndIf
				Default
					CreateConsoleMsg("Command not found.",255,0,0)
			End Select
			
			ConsoleInput = ""
		End If
		
		Local TempY% = y + height - 25*MenuScale - ConsoleScroll
		Local count% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			count = count+1
			If count>1000 Then
				Delete cm
			Else
				If TempY >= y And TempY < y + height - 20*MenuScale Then
					If cm=ConsoleReissue Then
						Color cm\r/4,cm\g/4,cm\b/4
						Rect x,TempY-2*MenuScale,width-30*MenuScale,24*MenuScale,True
					EndIf
					Color cm\r,cm\g,cm\b
					If cm\isCommand Then
						Text(x + 20*MenuScale, TempY, "> "+cm\txt)
					Else
						Text(x + 20*MenuScale, TempY, cm\txt)
					EndIf
				EndIf
				TempY = TempY - 15*MenuScale
			EndIf
			
		Next
		
		Color 255,255,255
		
		If Fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
	End If
	
	SetFont Font1
	
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


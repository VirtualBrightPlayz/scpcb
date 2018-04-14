Function FillRoom2Cafeteria(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    ;scp-294
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x+1847.0*RoomScale, -240.0*RoomScale, r\z-321*RoomScale, True)
    ;"spawnpoint" for the cups
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x+1780.0*RoomScale, -248.0*RoomScale, r\z-276*RoomScale, True)
    
    it = CreateItem("cup", "cup", r\x-508.0*RoomScale, -187*RoomScale, r\z+284.0*RoomScale, 240,175,70)
    EntityParent(it\collider, r\obj) : it\name = "Cup of Orange Juice"
    
    it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.0 * RoomScale, 87,62,45)
    EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
    
    it = CreateItem("Empty Cup", "emptycup", r\x-540*RoomScale, -187*RoomScale, r\z+124.0*RoomScale)
    EntityParent(it\collider, r\obj)
End Function

Function Use294()
	Local x#,y#, xtemp%,ytemp%, strtemp$, temp%
	
	ShowPointer()
	
	x = userOptions\screenWidth/2 - (ImageWidth(Panel294)/2)
	y = userOptions\screenHeight/2 - (ImageHeight(Panel294)/2)
	DrawImage Panel294, x, y
	If userOptions\fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
	
	temp = True
	If mainPlayer\currRoom\SoundCHN<>0 Then temp = False
	
	Text x+907, y+185, Input294, True,True
	
	If temp Then
		If MouseHit1 Then
			xtemp = Floor((ScaledMouseX()-x-228) / 35.5)
			ytemp = Floor((ScaledMouseY()-y-342) / 36.5)
			
			If ytemp => 0 And ytemp < 5 Then
				If xtemp => 0 And xtemp < 10 Then PlaySound_Strict ButtonSFX
			EndIf
			
			strtemp = ""
			
			temp = False
			
			Select ytemp
				Case 0
					strtemp = (xtemp + 1) Mod 10
				Case 1
					Select xtemp
						Case 0
							strtemp = "Q"
						Case 1
							strtemp = "W"
						Case 2
							strtemp = "E"
						Case 3
							strtemp = "R"
						Case 4
							strtemp = "T"
						Case 5
							strtemp = "Y"
						Case 6
							strtemp = "U"
						Case 7
							strtemp = "I"
						Case 8
							strtemp = "O"
						Case 9
							strtemp = "P"
					End Select
				Case 2
					Select xtemp
						Case 0
							strtemp = "A"
						Case 1
							strtemp = "S"
						Case 2
							strtemp = "D"
						Case 3
							strtemp = "F"
						Case 4
							strtemp = "G"
						Case 5
							strtemp = "H"
						Case 6
							strtemp = "J"
						Case 7
							strtemp = "K"
						Case 8
							strtemp = "L"
						Case 9 ;dispense
							temp = True
					End Select
				Case 3
					Select xtemp
						Case 0
							strtemp = "Z"
						Case 1
							strtemp = "X"
						Case 2
							strtemp = "C"
						Case 3
							strtemp = "V"
						Case 4
							strtemp = "B"
						Case 5
							strtemp = "N"
						Case 6
							strtemp = "M"
						Case 7
							strtemp = "-"
						Case 8
							strtemp = " "
						Case 9
							Input294 = Left(Input294, Max(Len(Input294)-1,0))
					End Select
				Case 4
					strtemp = " "
			End Select
			
			Input294 = Input294 + strtemp
			
			Input294 = Left(Input294, Min(Len(Input294),15))
			
			If temp And Input294 <> "" Then ;dispense
				Input294 = Trim(Lower(Input294))
				If Left(Input294, Min(7,Len(Input294))) = "cup of " Then
					Input294 = Right(Input294, Len(Input294)-7)
				ElseIf Left(Input294, Min(9,Len(Input294))) = "a cup of " 
					Input294 = Right(Input294, Len(Input294)-9)
				EndIf
				
				Local loc% = GetINISectionLocation("DATA\SCP-294.ini", Input294)
				
				If loc > 0 Then
					strtemp$ = GetINIString2("DATA\SCP-294.ini", loc, "dispensesound")
					If strtemp = "" Then
						mainPlayer\currRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\dispense1.ogg"))
					Else
						mainPlayer\currRoom\SoundCHN = PlaySound_Strict (LoadTempSound(strtemp))
					EndIf
					
					If GetINIInt2("DATA\SCP-294.ini", loc, "explosion")=True Then 
						ExplosionTimer = 135
						DeathMSG = GetINIString2("DATA\SCP-294.ini", loc, "deathmessage")
					EndIf
					
					strtemp$ = GetINIString2("DATA\SCP-294.ini", loc, "color")
					
					sep1 = Instr(strtemp, ",", 1)
					sep2 = Instr(strtemp, ",", sep1+1)
					r% = Trim(Left(strtemp, sep1-1))
					g% = Trim(Mid(strtemp, sep1+1, sep2-sep1-1))
					b% = Trim(Right(strtemp, Len(strtemp)-sep2))
					
					alpha# = Float(GetINIString2("DATA\SCP-294.ini", loc, "alpha"))
					glow = GetINIInt2("DATA\SCP-294.ini", loc, "glow")
					If alpha = 0 Then alpha = 1.0
					If glow Then alpha = -alpha
					
					it.items = CreateItem("Cup", "cup", EntityX(mainPlayer\currRoom\Objects[1],True),EntityY(mainPlayer\currRoom\Objects[1],True),EntityZ(mainPlayer\currRoom\Objects[1],True), r,g,b,alpha)
					it\name = "Cup of "+Input294
					EntityType (it\collider, HIT_ITEM)
				Else
					;out of range
					Input294 = "OUT OF RANGE"
					mainPlayer\currRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\outofrange.ogg"))
				EndIf
				
			EndIf
			
		EndIf ;if mousehit1
		
		If MouseHit2 And GameState=GAMESTATE_SCP294 Then 
			HidePointer()
			GameState = GAMESTATE_PLAYING
			Input294 = ""
		EndIf
		
	Else ;playing a dispensing sound
		If Input294 <> "OUT OF RANGE" Then Input294 = "DISPENSING..." : DebugLog "Generated dat dispenser"
		
		If Not ChannelPlaying(mainPlayer\currRoom\SoundCHN) Then
			If Input294 <> "OUT OF RANGE" Then
				HidePointer()
				GameState = GAMESTATE_SCP294
			EndIf
			Input294=""
			mainPlayer\currRoom\SoundCHN=0
		EndIf
	EndIf
	
End Function


Function UpdateEventRoom2cafeteria(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If GameState<>GAMESTATE_SCP294 Then
			If EntityDistance(e\room\Objects[0], mainPlayer\collider)<1.5 Then
				GiveAchievement(Achv294)
				If EntityInView(e\room\Objects[0], mainPlayer\cam) Then
					DrawHandIcon = True
					If MouseHit1 Then
						temp = True
						For it.Items = Each Items
							If it\Picked=False Then
								If EntityX(it\collider)-EntityX(e\room\Objects[1],True)=0 Then
									If EntityZ(it\collider)-EntityZ(e\room\Objects[1],True)=0 Then
										temp = False
										Exit
									EndIf
								EndIf
							EndIf
						Next
						If temp Then
							GameState = GAMESTATE_SCP294
							MouseHit1=False
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf		
	EndIf
	
	If e\EventState = 0 Then
		CreateNPC(NPCtype066, EntityX(e\room\obj), 0.5, EntityZ(e\room\obj))
		e\EventState = 1
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
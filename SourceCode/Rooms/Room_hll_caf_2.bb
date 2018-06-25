Function FillRoom_hll_caf_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    ;scp-294
    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x+1847.0*RoomScale, -240.0*RoomScale, r\z-321*RoomScale, True)
    ;"spawnpoint" for the cups
    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x+1780.0*RoomScale, -248.0*RoomScale, r\z-276*RoomScale, True)

    ;it = CreateItem("cup", "cup", r\x-508.0*RoomScale, -187*RoomScale, r\z+284.0*RoomScale, 240,175,70)
    ;EntityParent(it\collider, r\obj) : it\name = "Cup of Orange Juice"

    ;it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.0 * RoomScale, 87,62,45)
    ;EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"

    it = CreateItem("cup", r\x-540*RoomScale, -187*RoomScale, r\z+124.0*RoomScale)
    EntityParent(it\collider, r\obj)
End Function

Function Draw294()
	Local x%,y%, xtemp%,ytemp%, strtemp$, temp%

	Local panel294% = GrabImage("GFX/HUD/294panel.jpg")
	x = userOptions\screenWidth/2 - (ImageWidth(panel294)/2)
	y = userOptions\screenHeight/2 - (ImageHeight(panel294)/2)

	MaskImage(panel294, 255, 0, 255)
	DrawImage(panel294, x, y)
	DropAsset(panel294)

	Text(x+907, y+185, Input294, True,True)

	
End Function

Function Update294()
	Local x#,y#, xtemp%,ytemp%, strtemp$, temp%, loc%
	Local sep1%
	Local sep2%
	Local r%
	Local g%
	Local b%

	Local alpha#
	Local glow%

	Local panel294% = GrabImage("GFX/HUD/294panel.jpg")
	x = userOptions\screenWidth/2 - (ImageWidth(panel294)/2)
	y = userOptions\screenHeight/2 - (ImageHeight(panel294)/2)
	DropAsset(panel294)

	temp = True
	If (mainPlayer\currRoom\soundCHN<>0) Then temp = False

	If (temp) Then
		If (MouseHit1) Then
			xtemp = Int(Floor((MouseX()-x-228) / 35.5))
			ytemp = Int(Floor((MouseY()-y-342) / 36.5))

			If (ytemp => 0 And ytemp < 5) Then
				If (xtemp => 0 And xtemp < 10) Then
					PlaySound_SM(sndManager\button)
				EndIf
			EndIf

			strtemp = ""

			temp = False

			Select ytemp
				Case 0
					strtemp = Str((xtemp + 1) Mod 10)
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
							Input294 = Left(Input294, Int(Max(Len(Input294)-1,0)))
					End Select
				Case 4
					strtemp = " "
			End Select

			Input294 = Input294 + strtemp

			Input294 = Left(Input294, Int(Min(Len(Input294),15)))

			If (temp And Input294 <> "") Then ;dispense
				Input294 = Trim(Lower(Input294))
				If (Left(Input294, Int(Min(7,Len(Input294)))) = "cup of ") Then
					Input294 = Right(Input294, Len(Input294)-7)
				ElseIf (Left(Input294, Int(Min(9,Len(Input294)))) = "a cup of " ) Then
					Input294 = Right(Input294, Len(Input294)-9)
				EndIf

				loc = GetINISectionLocation("Data/SCP-294.ini", Input294)

				If (loc > 0) Then
					strtemp = GetINIString2("Data/SCP-294.ini", loc, "dispensesound")
					If (strtemp = "") Then
						mainPlayer\currRoom\soundCHN = PlaySound(LoadTempSound("SFX/SCP/294/dispense1.ogg"))
					Else
						mainPlayer\currRoom\soundCHN = PlaySound(LoadTempSound(strtemp))
					EndIf

					If (GetINIInt2("Data/SCP-294.ini", loc, "explosion")=True) Then
						ExplosionTimer = 135
						DeathMSG = GetINIString2("Data/SCP-294.ini", loc, "deathmessage")
					EndIf

					strtemp = GetINIString2("Data/SCP-294.ini", loc, "color")

					sep1 = Instr(strtemp, ",", 1)
					sep2 = Instr(strtemp, ",", sep1+1)
					r = Int(Trim(Left(strtemp, sep1-1)))
					g = Int(Trim(Mid(strtemp, sep1+1, sep2-sep1-1)))
					b = Int(Trim(Right(strtemp, Len(strtemp)-sep2)))

					alpha = Float(GetINIString2("Data/SCP-294.ini", loc, "alpha"))
					glow = GetINIInt2("Data/SCP-294.ini", loc, "glow")
					If (alpha = 0) Then alpha = 1.0
					If (glow) Then alpha = -alpha
					;TODO: Re-implement
					;it.Item = CreateItem("Cup", "cup", EntityX(mainPlayer\currRoom\objects[1],True),EntityY(mainPlayer\currRoom\objects[1],True),EntityZ(mainPlayer\currRoom\objects[1],True), r,g,b,alpha)
					;it\name = "Cup of "+Input294
					;EntityType(it\collider, HIT_ITEM)
				Else
					;out of range
					Input294 = "OUT OF RANGE"
					mainPlayer\currRoom\soundCHN = PlaySound(LoadTempSound("SFX/SCP/294/outofrange.ogg"))
				EndIf

			EndIf

		EndIf ;if mousehit1

		If (MouseHit2 And CurrGameState=GAMESTATE_SCP294) Then
			HidePointer()
			CurrGameState = GAMESTATE_PLAYING
			Input294 = ""
		EndIf

	Else ;playing a dispensing sound
		If (Input294 <> "OUT OF RANGE") Then
			Input294 = "DISPENSING..." : DebugLog("cringe")
		EndIf

		If (Not IsChannelPlaying(mainPlayer\currRoom\soundCHN)) Then
			If (Input294 <> "OUT OF RANGE") Then
				HidePointer()
				CurrGameState = GAMESTATE_SCP294
			EndIf
			Input294=""
			mainPlayer\currRoom\soundCHN=0
		EndIf
	EndIf

End Function

Function UpdateEvent_hll_caf_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (CurrGameState<>GAMESTATE_SCP294) Then
			If (EntityDistance(e\room\objects[0], mainPlayer\collider)<1.5) Then
				If (EntityInView(e\room\objects[0], mainPlayer\cam)) Then
					DrawHandIcon = True
					If (MouseHit1) Then
						temp = True
						For it = Each Item
							If (it\picked=False) Then
								If (EntityX(it\collider)-EntityX(e\room\objects[1],True)=0) Then
									If (EntityZ(it\collider)-EntityZ(e\room\objects[1],True)=0) Then
										temp = False
										Exit
									EndIf
								EndIf
							EndIf
						Next
						If (temp) Then
							CurrGameState = GAMESTATE_SCP294
							MouseHit1=False
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf

	If (e\eventState = 0) Then
		CreateNPC(NPCtype066, EntityX(e\room\obj), 0.5, EntityZ(e\room\obj))
		e\eventState = 1
	EndIf
	;[End Block]
End Function



;~IDEal Editor Parameters:
;~C#Blitz3D
Function FillRoom1162(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    d = CreateDoor(r\zone, r\x + 248.0*RoomScale, 0.0, r\z - 736.0*RoomScale, 90, r, False, False, 2)
    r\Objects[0] = CreatePivot()
    PositionEntity r\Objects[0],r\x+1012.0*RoomScale,r\y+128.0*RoomScale,r\z-640.0*RoomScale
    EntityParent r\Objects[0],r\obj
    EntityPickMode r\Objects[0],1
    it = CreateItem("Document SCP-1162", "paper", r\x + 863.227 * RoomScale, r\y + 152.0 * RoomScale, r\z - 953.231 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x-192.0*RoomScale, r\y+704.0*RoomScale, r\z+192.0*RoomScale, r)
    sc\angle = 225
    sc\turn = 45
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 8
End Function


Function UpdateEventRoom1162(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\EventState = A variable to determine the "nostalgia" items
	;- 0.0 = No nostalgia item
	;- 1.0 = Lost key
	;- 2.0 = Disciplinary Hearing DH-S-4137-17092
	;- 3.0 = Coin
	;- 4.0 = Movie Ticket
	;- 5.0 = Old Badge
	;e\EventState2 = Defining which slot from the Inventory should be picked
	;e\EventState3 = A check for if a item should be removed
	;- 0.0 = no item "trade" will happen
	;- 1.0 = item "trade" will happen
	;- 2.0 = the player doesn't has any items in the Inventory, giving him heavily injuries and giving him a random item
	;- 3.0 = player got a memorial item (to explain a bit D-9341's background)
	;- 3.1 = player got a memorial item + injuries (because he didn't had any item in his inventory before)
	If mainPlayer\currRoom = e\room
		
		mainPlayer\grabbedEntity = 0
		
		e\EventState = 0
		
		Local Pick1162% = True
		Local pp% = CreatePivot(e\room\obj)
		PositionEntity pp,976,128,-640,False
		
		For it.Items = Each Items
			If (Not it\Picked)
				If EntityDistance(it\collider,e\room\Objects[0])<0.75
					Pick1162% = False
				EndIf
			EndIf
		Next
		
		If EntityDistance(e\room\Objects[0],mainPlayer\collider)<0.75 And Pick1162%
			DrawHandIcon = True
			If MouseHit1 Then mainPlayer\grabbedEntity = e\room\Objects[0]
		EndIf
		
		If mainPlayer\grabbedEntity <> 0
			e\EventState2 = Rand(0,mainPlayer\inventory\size-1)
			If mainPlayer\inventory\items[e\EventState2]<>Null
				;randomly picked item slot has an item in it, using this slot
				e\EventState3 = 1.0
				DebugLog "pick1"
			Else
				;randomly picked item slot is empty, getting the first available slot
				For i = 0 To mainPlayer\inventory\size-1
					Local isSlotEmpty% = (mainPlayer\inventory\items[(i+e\EventState2) Mod mainPlayer\inventory\size] = Null)
					
					If (Not isSlotEmpty) Then
						;successful
						e\EventState2 = (i+e\EventState2) Mod mainPlayer\inventory\size
					EndIf
					
					If Rand(8)=1 Then
						If isSlotEmpty Then
							e\EventState3 = 3.1
						Else
							e\EventState3 = 3.0
						EndIf
						
						e\EventState = Rand(1,5)
						
						;Checking if the selected nostalgia item already exists or not
						Local itemName$ = ""
						Select (e\EventState)
							Case 1
								itemName = "Lost Key"
							Case 2
								itemName = "Disciplinary Hearing DH-S-4137-17092"
							Case 3
								itemName = "Coin"
							Case 4
								itemName = "Movie Ticket"
							Case 5
								itemName = "Old Badge"
						End Select
						
						Local itemExists% = False
						For it.Items = Each Items
							If (it\name = itemName) Then
								itemExists = True
								e\EventState3 = 1.0
								e\EventState = 0.0
								Exit
							EndIf
						Next
						
						If ((Not itemExists) And (Not isSlotEmpty)) Exit
					Else
						If isSlotEmpty Then
							e\EventState3 = 2.0
						Else
							e\EventState3 = 1.0
							Exit
						EndIf
					EndIf
				Next
			EndIf
		EndIf
		
		
		;trade successful
		If e\EventState3 = 1.0
			Local shouldCreateItem% = False
			
			For itt.ItemTemplates = Each ItemTemplates
				If (IsItemGoodFor1162(itt)) Then
					Select mainPlayer\inventory\items[e\EventState2]\itemtemplate\tempname
						Case "key"
							If itt\tempname = "key1" Or itt\tempname = "key2" And Rand(2)=1
								shouldCreateItem = True
								DebugLog "lostkey"
							EndIf
						Case "paper","oldpaper"
							If itt\tempname = "paper" And Rand(12)=1 Then
								shouldCreateItem = True
								DebugLog "paper"
							EndIf
						Case "gasmask","gasmask3","supergasmask","hazmatsuit","hazmatsuit2","hazmatsuit3"
							If itt\tempname = "gasmask" Or itt\tempname = "gasmask3" Or itt\tempname = "supergasmask" Or itt\tempname = "hazmatsuit" Or itt\tempname = "hazmatsuit2" Or itt\tempname = "hazmatsuit3" And Rand(2)=1
								shouldCreateItem = True
								DebugLog "gasmask hazmat"
							EndIf
						Case "key1","key2","key3"
							If itt\tempname = "key1" Or itt\tempname = "key2" Or itt\tempname = "key3" Or itt\tempname = "misc" And Rand(6)=1
								shouldCreateItem = True
								DebugLog "key"
							EndIf
						Case "vest","finevest"
							If itt\tempname = "vest" Or itt\tempname = "finevest" And Rand(1)=1
								shouldCreateItem = True
								DebugLog "vest"
							EndIf
						Default
							If itt\tempname = "misc" And Rand(6)=1
								shouldCreateItem = True
								DebugLog "default"
							EndIf
					End Select
				EndIf
				
				If (shouldCreateItem) Then
					RemoveItem(mainPlayer\inventory\items[e\EventState2])
					it=CreateItem(itt\name,itt\tempname,EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
					EntityType(it\collider, HIT_ITEM)
					PlaySound_Strict LoadTempSound("SFX\SCP\1162\Exchange"+Rand(0,4)+".ogg")
					e\EventState3 = 0.0
					
					
					GiveAchievement(Achv1162)
					MouseHit1 = False
					Exit
				EndIf
			Next
		;trade not sucessful (player got in return to injuries a new item)
		ElseIf e\EventState3 = 2.0
			mainPlayer\injuries = mainPlayer\injuries + 5.0
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.decals = CreateDecal(3, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\size = 0.75 : ScaleSprite de\obj, de\size, de\size
			FreeEntity pvt
			For itt.ItemTemplates = Each ItemTemplates
				If IsItemGoodFor1162(itt) And Rand(6)=1
					it = CreateItem(itt\name, itt\tempname, EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
					EntityType(it\collider, HIT_ITEM)
					GiveAchievement(Achv1162)
					MouseHit1 = False
					e\EventState3 = 0.0
					If mainPlayer\injuries > 15
						DeathMSG = "A dead Class D subject was discovered within the containment chamber of SCP-1162."
						DeathMSG = DeathMSG + " An autopsy revealed that his right lung was missing, which suggests"
						DeathMSG = DeathMSG + " interaction with SCP-1162."
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
						mainPlayer\lightFlash = 5.0
						Kill()
					Else
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
						mainPlayer\lightFlash = 5.0
						Msg = "You feel a sudden overwhelming pain in your chest."
						MsgTimer = 70*5
					EndIf
					Exit
				EndIf
			Next
		;trade with nostalgia item
		ElseIf e\EventState3 >= 3.0
			If e\EventState3 < 3.1
				PlaySound_Strict LoadTempSound("SFX\SCP\1162\Exchange"+Rand(0,4)+".ogg")
				RemoveItem(mainPlayer\inventory\items[e\EventState2])
			Else
				mainPlayer\injuries = mainPlayer\injuries + 5.0
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)
				TurnEntity pvt, 90, 0, 0
				EntityPick(pvt,0.3)
				de.decals = CreateDecal(3, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
				de\size = 0.75 : ScaleSprite de\obj, de\size, de\size
				FreeEntity pvt
				If mainPlayer\injuries > 15
					DeathMSG = "A dead Class D subject was discovered within the containment chamber of SCP-1162."
					DeathMSG = DeathMSG + " An autopsy revealed that his right lung was missing, which suggests"
					DeathMSG = DeathMSG + " interaction with SCP-1162."
					PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
					mainPlayer\lightFlash = 5.0
					Kill()
				Else
					PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
					mainPlayer\lightFlash = 5.0
					Msg = "You notice something moving in your pockets and a sudden pain in your chest."
					MsgTimer = 70*5
				EndIf
				e\EventState2 = 0.0
			EndIf
			Select e\EventState
				Case 1
					it = CreateItem("Lost Key","key",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
				Case 2
					it = CreateItem("Disciplinary Hearing DH-S-4137-17092","oldpaper",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
				Case 3
					it = CreateItem("Coin","coin",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
				Case 4
					it = CreateItem("Movie Ticket","ticket",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
				Case 5
					it = CreateItem("Old Badge","badge",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
			End Select
			EntityType(it\collider, HIT_ITEM)
			GiveAchievement(Achv1162)
			MouseHit1 = False
			e\EventState3 = 0.0
		EndIf
		FreeEntity pp
	EndIf
	;[End Block]
End Function

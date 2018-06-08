Function FillRoom_cont_914_1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    ;d = CreateDoor(r\zone, r\x, 0, r\z - 368.0 * RoomScale, 0, r, False, True, 2)
    ;d\dir = 1 : d\AutoClose = False : d\open = False
    ;PositionEntity (d\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    ;TurnEntity(d\buttons[0], 0, 90, 0)
    r\RoomDoors[2] = CreateDoor(r\zone,r\x,0,r\z-368.0*RoomScale,0,r,False,True,2)
    r\RoomDoors[2]\dir=1 : r\RoomDoors[2]\AutoClose=False : r\RoomDoors[2]\open=False
    PositionEntity (r\RoomDoors[2]\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
    TurnEntity(r\RoomDoors[2]\buttons[0], 0, 90, 0)
    
    r\Objects[0] = LoadMesh("GFX/map/914key.x")
    r\Objects[1] = LoadMesh("GFX/map/914knob.x")
    
    For  i% = 0 To 1
        ScaleEntity(r\Objects[i], RoomScale, RoomScale, RoomScale)
        EntityPickMode(r\Objects[i], 2)
    Next
    
    PositionEntity (r\Objects[0], r\x, r\y + 190.0 * RoomScale, r\z + 374.0 * RoomScale)
    PositionEntity (r\Objects[1], r\x, r\y + 230.0 * RoomScale, r\z + 374.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    EntityParent(r\Objects[1], r\obj)
    
    d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity (d\obj2) : d\obj2 = 0
    FreeEntity (d\buttons[0]) : d\buttons[0] = 0
    FreeEntity (d\buttons[1]) : d\buttons[1] = 0
    r\RoomDoors[0] = d: d\AutoClose = False
    
    d = CreateDoor(r\zone, r\x + 816.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
    FreeEntity (d\obj2) : d\obj2 = 0	
    FreeEntity (d\buttons[0]) : d\buttons[0] = 0
    FreeEntity (d\buttons[1]) : d\buttons[1] = 0
    r\RoomDoors[1] = d : d\AutoClose = False
    
    r\Objects[2] = CreatePivot()
    r\Objects[3] = CreatePivot()
    PositionEntity(r\Objects[2], r\x - 712.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    PositionEntity(r\Objects[3], r\x + 728.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
    EntityParent(r\Objects[2], r\obj)
    EntityParent(r\Objects[3], r\obj)
    
    it = CreateItem("Note", "paper", r\x +954.0 * RoomScale, r\y +228.0 * RoomScale, r\z + 127.0 * RoomScale)
    EntityParent(it\collider, r\obj)	
    
    it = CreateItem("First Aid Kit", "firstaid", r\x + 960.0 * RoomScale, r\y + 112.0 * RoomScale, r\z - 40.0 * RoomScale)
    EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
    
    it = CreateItem("Dr. L's Note", "paper", r\x - 928.0 * RoomScale, 160.0 * RoomScale, r\z - 160.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function

;TODO: Overhaul keycard system.
Function Use914(item.Items, setting$, x#, y#, z#)
	
	;RefinedItems = RefinedItems+1
	
	Local it2.Items, d.Decals, n.NPCs
	Select item\itemtemplate\name
		Case "Gas Mask", "Heavy Gas Mask"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case "1:1"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "fine", "very fine"
					it2 = CreateItem("Gas Mask", "supergasmask", x, y, z)
					RemoveItem(item)
			End Select
		Case "SCP-1499"
				Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case "1:1"
					it2 = CreateItem("Gas Mask", "gasmask", x, y, z)
					RemoveItem(item)
				Case "fine"
					it2 = CreateItem("SCP-1499", "super1499", x, y, z)
					RemoveItem(item)
				Case "very fine"
					n.NPCs = CreateNPC(NPCtype1499,x,y,z)
					n\state = 1
					n\sounds[0] = LoadSound("SFX/SCP/1499/Triggered.ogg")
					n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider,20.0)
					n\state3 = 1
					RemoveItem(item)
			End Select
		Case "Ballistic Vest"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case "1:1"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "fine"
					it2 = CreateItem("Heavy Ballistic Vest", "finevest", x, y, z)
					RemoveItem(item)
				Case "very fine"
					it2 = CreateItem("Bulky Ballistic Vest", "veryfinevest", x, y, z)
					RemoveItem(item)
			End Select
		Case "Clipboard"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(7, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					
					RemoveItem(item)
				Case "1:1"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "fine"
					item\inventory\size = Max(item\inventory\size,15)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "very fine"
					item\inventory\size = Max(item\inventory\size,20)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Cowbell"
			Select setting
				Case "rough","coarse"
					d.Decals = CreateDecal(0, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case "1:1","fine","very fine"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Night Vision Goggles"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case "1:1"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "fine"
					it2 = CreateItem("Night Vision Goggles", "supernv", x, y, z)
					RemoveItem(item)
				Case "very fine"
					it2 = CreateItem("Night Vision Goggles", "veryfinenvgoggles", x, y, z)
					RemoveItem(item)
			End Select
		Case "Metal Panel", "SCP-148 Ingot"
			Select setting
				Case "rough", "coarse"
					it2 = CreateItem("SCP-148 Ingot", "scp148ingot", x, y, z)
					RemoveItem(item)
				Case "1:1", "fine", "very fine"
					it2 = Null
					For it.Items = Each Items
						If it<>item And it\collider <> 0 And it\Picked = False Then
							If Distance(EntityX(it\collider,True), EntityZ(it\collider,True), EntityX(item\collider, True), EntityZ(item\collider, True)) < (180.0 * RoomScale) Then
								it2 = it
								Exit
							ElseIf Distance(EntityX(it\collider,True), EntityZ(it\collider,True), x,z) < (180.0 * RoomScale)
								it2 = it
								Exit
							End If
						End If
					Next
					
					If it2<>Null Then
						Select it2\itemtemplate\tempname
							Case "gasmask", "supergasmask"
								RemoveItem (it2)
								RemoveItem (item)
								
								it2 = CreateItem("Heavy Gas Mask", "gasmask3", x, y, z)
							Case "vest"
								RemoveItem (it2)
								RemoveItem(item)
								it2 = CreateItem("Heavy Ballistic Vest", "finevest", x, y, z)
							Case "hazmatsuit","hazmatsuit2"
								RemoveItem (it2)
								RemoveItem(item)
								it2 = CreateItem("Heavy Hazmat Suit", "hazmatsuit3", x, y, z)
						End Select
					Else 
						If item\itemtemplate\name="SCP-148 Ingot" Then
							it2 = CreateItem("Metal Panel", "scp148", x, y, z)
							RemoveItem(item)
						Else
							PositionEntity(item\collider, x, y, z)
							ResetEntity(item\collider)							
						EndIf
					EndIf					
			End Select

		Case "Severed Hand", "Black Severed Hand"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(3, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1", "fine", "very fine"
					If (item\itemtemplate\name = "Severed Hand")
						it2 = CreateItem("Black Severed Hand", "hand2", x, y, z)
					Else
						it2 = CreateItem("Severed Hand", "hand", x, y, z)
					EndIf
			End Select
			RemoveItem(item)

		Case "First Aid Kit", "Blue First Aid Kit"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
				If Rand(2)=1 Then
					it2 = CreateItem("Blue First Aid Kit", "firstaid2", x, y, z)
				Else
				    it2 = CreateItem("First Aid Kit", "firstaid", x, y, z)
				EndIf
				Case "fine"
					it2 = CreateItem("Small First Aid Kit", "finefirstaid", x, y, z)
				Case "very fine"
					it2 = CreateItem("Strange Bottle", "veryfinefirstaid", x, y, z)
			End Select
			RemoveItem(item)
		Case "Level 1 Key Card", "Level 2 Key Card", "Level 3 Key Card", "Level 4 Key Card", "Level 5 Key Card", "Key Card"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					it2 = CreateItem("Playing Card", "misc", x, y, z)
				Case "fine"
					If Rand(6)=1 Then 
						it2 = CreateItem("Playing Card", "misc", x, y, z)
					Else
						Select item\itemtemplate\name
							Case "Level 1 Key Card"
								Select SelectedDifficulty\otherFactors
									Case EASY
											it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
									Case NORMAL
										If Rand(3)=1 Then
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										Else
											it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
										EndIf
									 Case HARD
										If Rand(3)=1 Then
											it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
								End Select
							Case "Level 2 Key Card"
								Select SelectedDifficulty\otherFactors
									Case EASY
										If Rand(3)=1 Then
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										Else
											it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
										EndIf
									Case NORMAL
										If Rand(4)=1 Then
											it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									 Case HARD
										If Rand(5)=1 Then
											it2 = CreateItem("Level 3 Key Card", "key3", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
								End Select
			     			Case "Level 3 Key Card"
								Select SelectedDifficulty\otherFactors
									Case EASY
										If Rand(20)=1 Then
											it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									Case NORMAL
										If Rand(40)=1 Then
											it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									 Case HARD
										If Rand(60)=1 Then
											it2 = CreateItem("Level 4 Key Card", "key4", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
								End Select
							Case "Level 4 Key Card"
								Select SelectedDifficulty\otherFactors
									Case EASY
										If Rand(3)=1 Then
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										Else
											it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
										EndIf
									Case NORMAL
										If Rand(3)=1 Then
											it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									 Case HARD
										If Rand(4)=1 Then
											it2 = CreateItem("Level 5 Key Card", "key5", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
								End Select
							Case "Level 5 Key Card"	
								Select SelectedDifficulty\otherFactors
									Case EASY
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Case NORMAL
										it2 = CreateItem("Mastercard", "misc", x, y, z)
									Case HARD
										it2 = CreateItem("Mastercard", "misc", x, y, z)
								End Select
						End Select
					EndIf
				Case "very fine"
					Select SelectedDifficulty\otherFactors
						Case EASY
							it2 = CreateItem("Mastercard", "misc", x, y, z)
						Case NORMAL
							it2 = CreateItem("Mastercard", "misc", x, y, z)
						Case HARD
							it2 = CreateItem("Mastercard", "misc", x, y, z)
					End Select
			End Select			
			
			RemoveItem(item)
		Case "Playing Card", "Mastercard", "Coin"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
						it2 = CreateItem("Level 1 Key Card", "key1", x, y, z)	
			    Case "fine", "very fine"
			            it2 = CreateItem("Level 2 Key Card", "key2", x, y, z)
			End Select
			RemoveItem(item)
		Case "S-NAV 300 Navigator", "S-NAV 310 Navigator", "S-NAV Navigator", "S-NAV Navigator Ultimate"
			Select setting
				Case "rough", "coarse"
					it2 = CreateItem("Electronical components", "misc", x, y, z)
				Case "1:1"
					it2 = CreateItem("S-NAV Navigator", "nav", x, y, z)
					it2\state = 100
				Case "fine"
					it2 = CreateItem("S-NAV 310 Navigator", "nav", x, y, z)
					it2\state = 100
				Case "very fine"
					it2 = CreateItem("S-NAV Navigator Ultimate", "nav", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case "Radio Transceiver"
			Select setting
				Case "rough", "coarse"
					it2 = CreateItem("Electronical components", "misc", x, y, z)
				Case "1:1"
					it2 = CreateItem("Radio Transceiver", "18vradio", x, y, z)
					it2\state = 100
				Case "fine"
					it2 = CreateItem("Radio Transceiver", "fineradio", x, y, z)
					it2\state = 101
				Case "very fine"
					it2 = CreateItem("Radio Transceiver", "veryfineradio", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case "SCP-513"
			Select setting
				Case "rough", "coarse"
					PlaySound2 LoadTempSound("SFX/SCP/513/914Refine.ogg")
					For n.npcs = Each NPCs
						If n\npctype = NPCtype5131 Then RemoveNPC(n)
					Next
					d.Decals = CreateDecal(0, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1", "fine", "very fine"
					it2 = CreateItem("SCP-513", "scp513", x, y, z)
					
			End Select
			
			RemoveItem(item)
		Case "Some SCP-420-J", "Cigarette"
			Select setting
				Case "rough", "coarse"			
					d.Decals = CreateDecal(0, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					it2 = CreateItem("Cigarette", "cigarette", x + 1.5, y + 0.5, z + 1.0)
				Case "fine"
					it2 = CreateItem("Joint", "420s", x + 1.5, y + 0.5, z + 1.0)
				Case "very fine"
					it2 = CreateItem("Smelly Joint", "420s", x + 1.5, y + 0.5, z + 1.0)
			End Select
			
			RemoveItem(item)
		Case "9V Battery", "18V Battery", "Strange Battery"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					it2 = CreateItem("18V Battery", "18vbat", x, y, z)
				Case "fine"
					it2 = CreateItem("Strange Battery", "killbat", x, y, z)
				Case "very fine"
					it2 = CreateItem("Strange Battery", "killbat", x, y, z)
			End Select
			
			RemoveItem(item)
		Case "ReVision Eyedrops", "RedVision Eyedrops", "Eyedrops"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					it2 = CreateItem("RedVision Eyedrops", "eyedrops", x,y,z)
				Case "fine"
					it2 = CreateItem("Eyedrops", "fineeyedrops", x,y,z)
				Case "very fine"
					it2 = CreateItem("Eyedrops", "supereyedrops", x,y,z)
			End Select
			
			RemoveItem(item)		
		Case "Hazmat Suit"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					it2 = CreateItem("Hazmat Suit", "hazmatsuit", x,y,z)
				Case "fine"
					it2 = CreateItem("Hazmat Suit", "hazmatsuit2", x,y,z)
				Case "very fine"
					it2 = CreateItem("Hazmat Suit", "hazmatsuit2", x,y,z)
			End Select
			
			RemoveItem(item)
		Default
			
			Select item\itemtemplate\tempname
				Case "cup"
					Select setting
						Case "rough", "coarse"
							d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
							d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
						Case "1:1"
							it2 = CreateItem("cup", "cup", x,y,z)
							it2\name = item\name
							it2\r = 255-item\r
							it2\g = 255-item\g
							it2\b = 255-item\b
						Case "fine"
							it2 = CreateItem("cup", "cup", x,y,z)
							it2\name = item\name
							it2\state = 1.0
							it2\r = Min(item\r*Rnd(0.9,1.1),255)
							it2\g = Min(item\g*Rnd(0.9,1.1),255)
							it2\b = Min(item\b*Rnd(0.9,1.1),255)
						Case "very fine"
							it2 = CreateItem("cup", "cup", x,y,z)
							it2\name = item\name
							it2\state = Max(it2\state*2.0,2.0)	
							it2\r = Min(item\r*Rnd(0.5,1.5),255)
							it2\g = Min(item\g*Rnd(0.5,1.5),255)
							it2\b = Min(item\b*Rnd(0.5,1.5),255)
							If Rand(5)=1 Then
								ExplosionTimer = 135
							EndIf
					End Select	
					
					RemoveItem(item)
				Case "paper"
					Select setting
						Case "rough", "coarse"
							d.Decals = CreateDecal(7, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
						Case "1:1"
							Select Rand(6)
								Case 1
									it2 = CreateItem("Document SCP-106", "paper", x, y, z)
								Case 2
									it2 = CreateItem("Document SCP-079", "paper", x, y, z)
								Case 3
									it2 = CreateItem("Document SCP-173", "paper", x, y, z)
								Case 4
									it2 = CreateItem("Document SCP-895", "paper", x, y, z)
								Case 5
									it2 = CreateItem("Document SCP-682", "paper", x, y, z)
								Case 6
									it2 = CreateItem("Document SCP-860", "paper", x, y, z)
							End Select
						Case "fine", "very fine"
							it2 = CreateItem("Origami", "misc", x, y, z)
					End Select
					
					RemoveItem(item)
				Default
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)	
			End Select
			
	End Select
	
	If it2 <> Null Then EntityType (it2\collider, HIT_ITEM)
End Function


Function UpdateEvent_cont_914_1(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If (Not e\loaded) Then
			e\sounds[0] = LoadSound("SFX/SCP/914/Refining.ogg")
			e\sounds[1] = LoadSound("SFX/SCP/914/PlayerUse.ogg")
			e\sounds[2] = LoadSound("SFX/SCP/914/PlayerDeath.ogg")

			e\loaded = True
		EndIf
		
		EntityPick(mainPlayer\cam, 1.0)
		If PickedEntity() = e\room\Objects[0] Then
			DrawHandIcon = True
			If MouseHit1 Then mainPlayer\grabbedEntity = e\room\Objects[0]
		ElseIf PickedEntity() = e\room\Objects[1]
			DrawHandIcon = True
			If MouseHit1 Then mainPlayer\grabbedEntity = e\room\Objects[1]
		EndIf
		
		If MouseDown1 Or MouseHit1 Then
			If mainPlayer\grabbedEntity <> 0 Then ;avain
				If mainPlayer\grabbedEntity = e\room\Objects[0] Then
					If e\EventState = 0 Then
						DrawHandIcon = True
						TurnEntity(mainPlayer\grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)
						
						angle = WrapAngle(EntityRoll(e\room\Objects[0]))
						If angle > 181 Then DrawArrowIcon(3) = True
						DrawArrowIcon(1) = True
						
						If angle < 90 Then
							RotateEntity(mainPlayer\grabbedEntity, 0, 0, 361.0)
						ElseIf angle < 180
							RotateEntity(mainPlayer\grabbedEntity, 0, 0, 180)
						EndIf
						
						If angle < 181 And angle > 90 Then
							For it.Items = Each Items
								If it\collider <> 0 And it\Picked = False Then
									If Abs(EntityX(it\collider) - (e\room\x - 712.0 * RoomScale)) < 200.0 Then
										If Abs(EntityY(it\collider) - (e\room\y + 648.0 * RoomScale)) < 104.0 Then
											e\EventState = 1
											e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\Objects[1])
											Exit
										EndIf
									End If
								End If
							Next
						EndIf
					End If
				ElseIf mainPlayer\grabbedEntity = e\room\Objects[1]
					If e\EventState = 0 Then
						DrawHandIcon = True
						TurnEntity(mainPlayer\grabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)
						
						angle# = WrapAngle(EntityRoll(e\room\Objects[1]))
						DrawArrowIcon(3) = True
						DrawArrowIcon(1) = True
						
						If angle > 90 Then
							If angle < 180 Then
								RotateEntity(mainPlayer\grabbedEntity, 0, 0, 90.0)
							ElseIf angle < 270
								RotateEntity(mainPlayer\grabbedEntity, 0, 0, 270)
							EndIf
						EndIf
						
					End If
				End If
			End If
		Else
			mainPlayer\grabbedEntity = 0
		End If
		
		Local setting$ = ""
		
		If mainPlayer\grabbedEntity <> e\room\Objects[1] Then
			angle# = WrapAngle(EntityRoll(e\room\Objects[1]))
			If angle < 22.5 Then
				angle = 0
				setting = "1:1"
			ElseIf angle < 67.5
				angle = 40
				setting = "coarse"
			ElseIf angle < 180
				angle = 90
				setting = "rough"
			ElseIf angle > 337.5
				angle = 359 - 360
				setting = "1:1"
			ElseIf angle > 292.5
				angle = 320 - 360
				setting = "fine"
			Else
				angle = 270 - 360
				setting = "very fine"
			End If
			RotateEntity(e\room\Objects[1], 0, 0, CurveValue(angle, EntityRoll(e\room\Objects[1]), 20))
		EndIf
		
		For i% = 0 To 1
			If mainPlayer\grabbedEntity = e\room\Objects[i] Then
				If Not EntityInView(e\room\Objects[i], mainPlayer\cam) Then
					mainPlayer\grabbedEntity = 0
				ElseIf EntityDistance(e\room\Objects[i], mainPlayer\cam) > 1.0
					mainPlayer\grabbedEntity = 0
				End If
			End If
		Next
		
		If e\EventState > 0 Then
			e\EventState = e\EventState + timing\tickDuration
			
			
			e\room\RoomDoors[1]\open = False
			If e\EventState > 70 * 2 Then
				e\room\RoomDoors[0]\open = False
			EndIf
			
			If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True)) < (170.0 * RoomScale) Then
				
				If setting = "rough" Or setting = "coarse" Then
					If e\EventState > 70 * 2.6 And e\EventState - timing\tickDuration < 70 * 2.6 Then PlaySound2(e\sounds[2])
				EndIf
				
				If e\EventState > 70 * 3 Then
					Select setting
						Case "rough"
							Kill(mainPlayer)
							mainPlayer\blinkTimer = -10
							If e\soundChannels[0] <> 0 Then StopChannel e\soundChannels[0]
							DeathMSG = Chr(34)+"A heavily mutilated corpse found inside the output booth of SCP-914. DNA testing identified the corpse as Class D Subject D-9341. "
							DeathMSG = DeathMSG + "The subject had obviously been "+Chr(34)+"refined"+Chr(34)+" by SCP-914 on the "+Chr(34)+"Rough"+Chr(34)+" setting, but we are still confused as to how he "
							DeathMSG = DeathMSG + "ended up inside the intake booth and who or what wound the key."+Chr(34)
						Case "coarse"
							mainPlayer\blinkTimer = -10
							If e\EventState - timing\tickDuration < 70 * 3 Then PlaySound2(LoadSound(e\sounds[1]))
						Case "1:1"
							mainPlayer\blinkTimer = -10
							If e\EventState - timing\tickDuration < 70 * 3 Then PlaySound2(LoadSound(e\sounds[1]))
						Case "fine", "very fine"
							mainPlayer\blinkTimer = -10
							If e\EventState - timing\tickDuration < 70 * 3 Then PlaySound2(LoadSound(e\sounds[1]))
					End Select
				End If
			EndIf
			
			If e\EventState > (6 * 70) Then	
				RotateEntity(e\room\Objects[0], EntityPitch(e\room\Objects[0]), EntityYaw(e\room\Objects[0]), CurveAngle(0, EntityRoll(e\room\Objects[0]),10.0))
			Else
				RotateEntity(e\room\Objects[0], EntityPitch(e\room\Objects[0]), EntityYaw(e\room\Objects[0]), 180)
			EndIf
			
			If e\EventState > (12 * 70) Then							
				For it.Items = Each Items
					If it\collider <> 0 And it\Picked = False Then
						If Distance(EntityX(it\collider), EntityZ(it\collider), EntityX(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True)) < (180.0 * RoomScale) Then
							Use914(it, setting, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True), EntityZ(e\room\Objects[3], True))
							
						End If
					End If
				Next
				
				If Distance(EntityX(mainPlayer\collider), EntityZ(mainPlayer\collider), EntityX(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True)) < (160.0 * RoomScale) Then
					Select setting
						Case "coarse"
							mainPlayer\injuries = 4.0
							Msg = "You notice countless small incisions all around your body. They are bleeding heavily."
							MsgTimer = 70*8
						Case "1:1"
							userOptions\invertMouseY = (Not userOptions\invertMouseY)
						Case "fine", "very fine"
							mainPlayer\superMan = 1.0
					End Select
					mainPlayer\blurTimer = 1000
					PositionEntity(mainPlayer\collider, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True) + 1.0, EntityZ(e\room\Objects[3], True))
					ResetEntity(mainPlayer\collider)
					mainPlayer\dropSpeed = 0
				EndIf								
				
				e\room\RoomDoors[0]\open = True
				e\room\RoomDoors[1]\open = True
				RotateEntity(e\room\Objects[0], 0, 0, 0)
				e\EventState = 0
			End If
		End If
		
	EndIf
	;[End Block]
End Function



;~IDEal Editor Parameters:
;~C#Blitz3D
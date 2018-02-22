Function FillRoom914(r.Rooms)
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
    
    r\Objects[0] = LoadMesh_Strict("GFX\map\914key.x")
    r\Objects[1] = LoadMesh_Strict("GFX\map\914knob.x")
    
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

Function Use914(item.Items, setting$, x#, y#, z#)
	
	;RefinedItems = RefinedItems+1
	
	Local it2.Items
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
					n\State = 1
					n\Sound = LoadSound_Strict("SFX\SCP\1499\Triggered.ogg")
					n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider,20.0)
					n\State3 = 1
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
		Case "3-D Glasses"
			Select setting
				Case "rough,coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype178 Then RemoveNPC(n)
					Next
				Case "1:1","fine","very fine"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
			End Select
		Case "Clipboard"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(7, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					For i% = 0 To 19
						If item\SecondInv[i]<>Null Then RemoveItem(item\SecondInv[i])
						item\SecondInv[i]=Null
					Next
					RemoveItem(item)
				Case "1:1"
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "fine"
					item\invSlots = Max(item\state2,15)
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case "very fine"
					item\invSlots = Max(item\state2,20)
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
										If Rand(500)=1 Then
											it2 = CreateItem("Key Card Omni", "key6", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									Case NORMAL
										If Rand(700)=1 Then
											it2 = CreateItem("Key Card Omni", "key6", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
									Case HARD
										If Rand(1000)=1 Then
											it2 = CreateItem("Key Card Omni", "key6", x, y, z)
										Else
											it2 = CreateItem("Mastercard", "misc", x, y, z)
										EndIf
								End Select
						End Select
					EndIf
				Case "very fine"
					Select SelectedDifficulty\otherFactors
						Case EASY
							If Rand(500)=5 Then
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							Else
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							EndIf
						Case NORMAL
							If Rand(700)=7 Then
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							Else
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							EndIf
						Case HARD
							If Rand(1000)=10 Then
								it2 = CreateItem("Key Card Omni", "key6", x, y, z)
							Else
								it2 = CreateItem("Mastercard", "misc", x, y, z)
							EndIf
					End Select
			End Select			
			
			RemoveItem(item)
		Case "Key Card Omni"
			Select setting
				Case "rough", "coarse"
					d.Decals = CreateDecal(0, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case "1:1"
					If Rand(2)=1 Then
						it2 = CreateItem("Mastercard", "misc", x, y, z)
					Else
						it2 = CreateItem("Playing Card", "misc", x, y, z)			
					EndIf	
				Case "fine", "very fine"
					it2 = CreateItem("Key Card Omni", "key6", x, y, z)
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
					PlaySound_Strict LoadTempSound("SFX\SCP\513\914Refine.ogg")
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

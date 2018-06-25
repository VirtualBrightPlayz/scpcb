Global BurntNote.MarkedForRemoval

;TODO: remove, use Inventory type instead
Global MaxItemAmount.MarkedForRemoval
Global ItemAmount.MarkedForRemoval
Dim Inventory.MarkedForRemoval(10)
Global InvSelect.MarkedForRemoval, SelectedItem.MarkedForRemoval

Global ClosestItem.MarkedForRemoval

Const ITEM_TAG_914F$   = "914_fine"
Const ITEM_TAG_914VF$  = "914_veryfine"
Const ITEM_TAG_ALTINV$ = "alt_inv"

Const ITEMPICK_SOUND_PAPER%  = 0
Const ITEMPICK_SOUND_MEDIUM% = 1
Const ITEMPICK_SOUND_LARGE%  = 2
Const ITEMPICK_SOUND_SMALL%  = 3

Type ItemTemplate
	Field name$
	Field invName$

	Field obj%
	Field objPath$

	Field invImage%[2]
	Field invImagePath$[2]

	Field tex%
	Field texPath$

	Field sound%

	Field wornSlot%
	Field wornOnly%

	Field scale#
End Type

Function CreateItemTemplate(file$, section$)
	Local it.ItemTemplate = New ItemTemplate
	Local flags%

	it\name = section
	it\invName = GetINIString(file, section, "invname")

	;The model and inv image are in the specified directory.
	Local dataPath$ = GetINIString(file, section, "datapath")
	If (dataPath <> "") Then
		If (FileType(dataPath) <> 2) Then RuntimeError("Item template directory not found ("+section+", "+dataPath+")")

		it\objPath = dataPath + it\name + ".b3d"
		it\invImagePath[0] = GetImagePath(dataPath + "inv_" + it\name)
	EndIf

	;Otherwise the obj, tex and inv paths are specified in the INI.
	Local objPath$ = GetINIString(file, section, "objpath")
	If (objPath <> "") Then
		it\objPath = objPath
	EndIf

	Local texPath$ = GetINIString(file, section, "texpath")
	If (texPath <> "") Then
		it\texPath = texPath
	EndIf

	Local invImgPath$ = GetINIString(file, section, "invimgpath")
	If (invImgPath <> "") Then
		it\invImagePath[0] = invImgPath
	EndIf

	Local invImgPath2$ = GetINIString(file, section, "invimgpath2")
	If (invImgPath2 <> "") Then
		it\invImagePath[1] = invImgPath2
	EndIf

	Local slot$ = Lower(GetINIString(file, section, "slot"))
	Select slot
		Case "head"
			it\wornSlot = WORNITEM_SLOT_HEAD
		Case "body"
			it\wornSlot = WORNITEM_SLOT_BODY
		Default
			it\wornSlot = WORNITEM_SLOT_NONE
	End Select

	it\wornOnly = (GetINIInt(file, section, "wornonly") = 1)

	Local sound$ = Lower(GetINIString(file, section, "sound"))
	Select sound
		Case "medium"
			it\sound = ITEMPICK_SOUND_MEDIUM
		Case "large"
			it\sound = ITEMPICK_SOUND_LARGE
		Case "small"
			it\sound = ITEMPICK_SOUND_SMALL
		Default
			it\sound = ITEMPICK_SOUND_PAPER
	End Select

	;Start loading the assets needed.

	;Does another item already use that model?
	Local it2.ItemTemplate
	For it2 = Each ItemTemplate
		If (it2\objPath = it\objPath And it2\obj <> 0) Then
			it\obj = CopyEntity(it2\obj)
			Exit
		EndIf
	Next

	;Otherwise load the model.
	If (it\obj = 0) Then
		If (GetINIInt(file, section, "animated") = 1) Then
			it\obj = LoadAnimMesh(it\objPath)
		Else
			it\obj = LoadMesh(it\objPath)
		EndIf
	EndIf

	If (it\texPath <> "") Then
		For it2 = Each ItemTemplate
			If (it2\texPath = it\texPath And it2\tex <> 0) Then
				it\tex = it2\tex
				Exit
			EndIf
		Next

		If (it\tex = 0) Then
			flags = GetINIInt(file, section, "textureflags", 1+8)
			it\tex = LoadTexture(it\texPath, flags)
		EndIf

		EntityTexture(it\obj, it\tex)
	EndIf

	Local i%
	For i=0 To 1
		If (it\invImagePath[i] <> "") Then
			For it2 = Each ItemTemplate
				If (it2\invImagePath[i] = it\invImagePath[i] And it2\invImage[i] <> 0) Then
					it\invImage[i] = it2\invImage[i]
					Exit
				EndIf
			Next

			If (it\invImage[i] = 0) Then
				it\invImage[i] = LoadImage(it\invImagePath[i])
				MaskImage(it\invImage[i], 255, 0, 255)
			EndIf
		EndIf
	Next
	
	Local scale# = GetINIFloat(file, section, "scale", 1.0)
	it\scale = scale
	ScaleEntity(it\obj, scale * RoomScale, scale * RoomScale, scale * RoomScale, True)

	HideEntity(it\obj)
End Function

Function FindItemTemplate.ItemTemplate(tempname$)
	Local it.ItemTemplate = Null
	Local candidate.ItemTemplate = Null
	For it = Each ItemTemplate
		If (it\name = tempname) Then
			candidate = it
			Exit
		EndIf
	Next

	Return candidate
End Function

Function LoadItemTemplates(file$)
	Local f% = OpenFile(file)
	Local it.ItemTemplate
	Local section$

	While Not Eof(f)
		section = Trim(ReadLine(f))
		If (Left(section,1) = "[") Then
			section = Mid(section, 2, Len(section) - 2)

			CreateItemTemplate(file, section)
		EndIf
	Wend

	CloseFile(f)
End Function



Type Item
	Field name$
	Field collider%
	Field model%
	Field template.ItemTemplate
	Field img%
	Field id%

	Field r%,g%,b%,a#

	Field dist#

	Field state#
	;TODO: Deprecate
	Field state2#

	Field picked%
	Field dropped%
	
	Field invImage%

	Field wontColl% = False
	Field xspeed#
	Field zspeed#
	Field dropSpeed#

	Field tags$[5]
	Field subInventory.Inventory = Null
End Type

Const MAX_ITEM_COUNT% = 20
Type Inventory
	Field items.Item[MAX_ITEM_COUNT]
	Field size% = 10
	Field parent.Inventory = Null
End Type

Function CreateInventory.Inventory(size%)
	Local inv.Inventory = New Inventory
	inv\size = size
	Return inv
End Function

Function DeleteInventory(inv.Inventory)
	Local i%
	For i=0 To MAX_ITEM_COUNT-1
		If (inv\items[i]<>Null) Then RemoveItem(inv\items[i])
	Next
	Delete inv
End Function

Function CountItemsInInventory%(inv.Inventory)
	Local retVal% = 0
	Local i%
	For i=0 To inv\size-1
		If (inv\items[i]<>Null) Then
			retVal=retVal+1
		EndIf
	Next
	Return retVal
End Function

Global LastItemID%

Function CreateItem.Item(name$, x#, y#, z#, invSlots%=0)
	Local i.Item = New Item
	Local it.ItemTemplate

	For it = Each ItemTemplate
		If (it\name = name) Then
			i\template = it
			i\collider = CreatePivot()
			EntityRadius(i\collider, 0.01)
			EntityPickMode(i\collider, 1, False)
			i\model = CopyEntity(it\obj, i\collider)
			i\name = it\invName
			ShowEntity(i\collider)
			ShowEntity(i\model)

			Exit
		EndIf
	Next

	If (i\template = Null) Then
		RuntimeError("Item template not found ("+name+")")
	EndIf

	ResetEntity(i\collider)
	PositionEntity(i\collider, x, y, z, True)
	RotateEntity(i\collider, 0, Rand(360), 0)
	i\dropSpeed = 0.0

	;TODO: Re-implement.
;	If (tempname="clipboard") And (invSlots=0) Then
;		invSlots = 20
;		SetAnimTime(i\model, 17.0)
;		i\invimg = i\template\invimg2 ;<-- this Future Mark.
;	EndIf

	i\subInventory = Null
	If (invSlots>0) Then
		i\subInventory = CreateInventory(invSlots)
	EndIf

	i\id=LastItemID+1
	LastItemID=i\id

	EntityType(i\collider, HIT_ITEM)
	Return i
End Function

Function CreatePaper.Item(name$, x#, y#, z#)
	Local i.Item = CreateItem("paper", x, y, z, 0)
	i\name = GetINIString("Data/Items/paper.ini", name, "name")

	;Load the document image.
	Local imgPath$ = GetImagePath("GFX/Items/Paper/Documents/" + name)
	If (FileType(imgPath) <> 1) Then
		imgPath = GetImagePath("GFX/Items/Paper/Notes/" + name)
	EndIf

	i\img = LoadImage(imgPath)
	MaskImage(i\img, 255, 255, 0)

	;Make a resized copy to texture the model with.
	Local texDim% = 256
	Local img% = CopyImage(i\img)
	img = ResizeImage2(img, texDim, texDim)

	Local tex% = CreateTexture(texDim, texDim, 1+8)
	CopyRect(0, 0, texDim, texDim, 0, 0, ImageBuffer(img), TextureBuffer(tex))
	EntityTexture(i\model, tex)
	FreeImage(img)
	FreeTexture(tex)

	Return i
End Function

Function RemoveItem(i.Item)
	If (i\subInventory<>Null) Then DeleteInventory(i\subInventory)

	If (i\img <> 0) Then
		FreeImage(i\img)
	EndIf

	DropItem(i,False)

	FreeEntity(i\model) : FreeEntity(i\collider) : i\collider = 0

	Delete i
End Function

Global itemDistanceTimer% = 0
Function UpdateItems()
	Local n%, i.Item, i2.Item
	Local xtemp#, ytemp#, ztemp#
	Local temp%, np.NPC

	Local HideDist# = HideDistance*0.5
	Local deletedItem% = False
	
	Local ed#

	mainPlayer\closestItem = Null
	For i = Each Item
		i\dropped = 0

		If (Not i\picked) Then
			If (itemDistanceTimer < TimeInPosMilliSecs()) Then
				i\dist = EntityDistance(mainPlayer\collider, i\collider)
			EndIf

			If (i\dist < HideDist) Then
				ShowEntity(i\collider)

				If (i\dist < 1.2) Then
					If (mainPlayer\closestItem = Null) Then
						If (EntityInView(i\model, mainPlayer\cam)) Then
							mainPlayer\closestItem = i
						EndIf
					ElseIf (i\dist < EntityDistance(mainPlayer\collider, mainPlayer\closestItem\collider)) Then
						If (EntityInView(i\model, mainPlayer\cam)) Then
							mainPlayer\closestItem = i
						EndIf
					EndIf
				EndIf

				If (EntityCollided(i\collider, HIT_MAP)) Then
					i\dropSpeed = 0
					i\xspeed = 0.0
					i\zspeed = 0.0
				Else
					i\dropSpeed = i\dropSpeed - 0.0004 * timing\tickDuration
					TranslateEntity(i\collider, i\xspeed*timing\tickDuration, i\dropSpeed * timing\tickDuration, i\zspeed*timing\tickDuration)
					If (i\wontColl) Then ResetEntity(i\collider)
				EndIf

				If (i\dist<HideDist*0.2) Then
					For i2 = Each Item
						If (i<>i2 And (Not i2\picked) And i2\dist<HideDist*0.2) Then

							xtemp = (EntityX(i2\collider,True)-EntityX(i\collider,True))
							ytemp = (EntityY(i2\collider,True)-EntityY(i\collider,True))
							ztemp = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))

							ed = (xtemp*xtemp+ztemp*ztemp)
							If (ed<0.07 And Abs(ytemp)<0.25) Then
								;items are too close together, push away

								xtemp = xtemp*(0.07-ed)
								ztemp = ztemp*(0.07-ed)

								While Abs(xtemp)+Abs(ztemp)<0.001
									xtemp = xtemp+Rnd(-0.002,0.002)
									ztemp = ztemp+Rnd(-0.002,0.002)
								Wend

								TranslateEntity(i2\collider,xtemp,0,ztemp)
								TranslateEntity(i\collider,-xtemp,0,-ztemp)
							EndIf
						EndIf
					Next
				EndIf

				If (EntityY(i\collider) < - 35.0) Then
					DebugLog("remove: " + i\name)
					RemoveItem(i)
				EndIf
			Else
				HideEntity(i\collider)
			EndIf
		EndIf
	Next
	
	Local canSeePlayer% = True
	If (mainPlayer\closestItem <> Null) Then
		;Can the player see this? (TODO: fix)
		;canSeePlayer = EntityVisible(mainPlayer\closestItem\collider,mainPlayer\cam)
		;If (Not canSeePlayer) Then canSeePlayer = EntityVisible(mainPlayer\closestItem\collider,mainPlayer\collider)
		If (canSeePlayer) Then
			If (MouseHit1) Then
				PickItem(mainPlayer\closestItem)
			EndIf
		EndIf
	EndIf

	If (itemDistanceTimer < TimeInPosMilliSecs()) Then
		itemDistanceTimer = TimeInPosMilliSecs() + 800
	EndIf
End Function

Function PickItem(item.Item)
	Local n% = 0
	Local e.Event
	Local z%

	Select item\template\name
		Case "battery"
			If (HasTag(item, ITEM_TAG_914VF)) Then
				ShowEntity(mainPlayer\overlays[OVERLAY_WHITE])
				mainPlayer\lightFlash = 1.0
				PlaySound2(IntroSFX(11))
				DeathMSG = "Subject D-9341 found dead inside SCP-914's output booth next to what appears to be an ordinary nine-volt battery. The subject is covered in severe "
				DeathMSG = DeathMSG + "electrical burns, and assumed to be killed via an electrical shock caused by the battery. The battery has been stored for further study."
				Kill(mainPlayer)

				Return
			EndIf
		Case "vest"
			If (HasTag(item, ITEM_TAG_914VF)) Then
				Msg = "The vest is too heavy to pick up."
				MsgTimer = 70*6

				Return
			EndIf
		Case "hazmatsuit"
			Msg = "You put on the hazmat suit."
			MsgTimer = 70 * 5
			mainPlayer\wornItems[WORNITEM_SLOT_BODY] = item

			Return
	End Select

	If (CountItemsInInventory(mainPlayer\inventory) < mainPlayer\inventory\size) Then
		For n = 0 To mainPlayer\inventory\size - 1
			If (mainPlayer\inventory\items[n] = Null) Then
				PlaySound_SM(sndManager\itemPick[item\template\sound])
				item\picked = True
				item\dropped = -1

				mainPlayer\inventory\items[n] = item
				HideEntity(item\collider)
				Exit
			EndIf
		Next
	Else
		Msg = "You cannot carry any more items."
		MsgTimer = 70 * 5
	EndIf
End Function

Function DropItem(item.Item,playDropSound%=True)
	Local player.Player
	For player = Each Player
		DeEquipItem(player,item)
	Next

	If (playDropSound) Then
		PlaySound_SM(sndManager\itemPick[item\template\sound])
	EndIf

	item\dropped = 1

	ShowEntity(item\collider)
	PositionEntity(item\collider, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam))
	RotateEntity(item\collider, EntityPitch(mainPlayer\cam), EntityYaw(mainPlayer\cam)+Rnd(-20,20), 0)
	MoveEntity(item\collider, 0, -0.1, 0.1)
	RotateEntity(item\collider, 0, EntityYaw(mainPlayer\cam)+Rnd(-110,110), 0)

	ResetEntity(item\collider)

	item\picked = False
	Local inv.Inventory
	Local j%
	For inv = Each Inventory
		For j=0 To inv\size-1
			If (inv\items[j]=item) Then inv\items[j]=Null
		Next
	Next
End Function

Function AssignTag(item.Item, tag$)
	If (HasTag(item, tag)) Then
		Return
	EndIf

	Local space% = False
	Local i%
	For i=0 To 4
		If (item\tags[i] = "") Then
			space = True
			item\tags[i] = tag
			Return
		EndIf
	Next

	If (Not space) Then
		RuntimeError("Assigned tag without space: " + item\name + ", tag: " + tag)
	EndIf
End Function

Function RemoveTag(item.Item, tag$)
	Local found% = False
	Local i%
	For i=0 To 4
		If (item\tags[i] = tag) Then
			found = True
			item\tags[i] = ""
			Return
		EndIf
	Next

	If (Not found) Then
		RuntimeError("Removed non-existant tag: " + item\name + ", tag: " + tag)
	EndIf
End Function

Function HasTag%(item.Item, tag$)
	Local i%
	For i=0 To 4
		If (item\tags[i] = tag) Then
			Return True
		EndIf
	Next

	Return False
End Function

Function IsPlayerWearingTempName%(player.Player,templateName$)
	Local it.ItemTemplate = FindItemTemplate(templateName)
	If (it=Null) Then Return False
	Local slot% = it\wornSlot
	If (slot=WORNITEM_SLOT_NONE) Then Return False
	If (player\wornItems[slot]=Null) Then Return False
	Return (player\wornItems[slot]\template\name=templateName)
End Function

Function IsPlayerWearingItem%(player.Player,item.Item)
	If (item = Null) Then
		Return False
	EndIf
	Local slot% = item\template\wornSlot
	If (slot=WORNITEM_SLOT_NONE) Then Return False
	If (player\wornItems[slot]=Null) Then Return False
	Return (player\wornItems[slot] = item)
End Function

Function EquipItem(player.Player, item.Item)
	If (item=Null) Then Return
	If (item\template\wornSlot = WORNITEM_SLOT_NONE) Then Return

	player\wornItems[item\template\wornSlot] = item
End Function

Function DeEquipItem(player.Player,item.Item)
	If (item = Null) Then
		Return
	EndIf

	If (player\wornItems[item\template\wornSlot]<>item) Then
		Return
	EndIf

	player\wornItems[item\template\wornSlot] = Null
	If (item\template\wornSlot = WORNITEM_SLOT_HAND) Then
		Return
	EndIf

	;Check if this item can be put back into the inventory.
	If (item\template\wornOnly) Then
		DropItem(item)
	ElseIf (CountItemsInInventory(mainPlayer\inventory) >= mainPlayer\inventory\size) Then
		DropItem(item)
	Else
		PickItem(item)
	EndIf
End Function

Const ITEM_CELL_SIZE% = 70
Const ITEM_CELL_SPACING% = 35
Const ITEMS_PER_ROW% = 6
Function UpdateInventory(player.Player)
	;TODO: cleanup
	Local prevInvOpen% = (CurrGameState=GAMESTATE_INVENTORY)
	Local mouseSlot% = 66

	Local np.NPC, e.Event, it.Item

	Local x%, y%, isMouseOn%, i%

	Local slotIndex%
	If (CurrGameState = GAMESTATE_INVENTORY) Then
		mainPlayer\selectedDoor = Null

		x = userOptions\screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2
		y = userOptions\screenHeight / 2 - ITEM_CELL_SIZE * (player\openInventory\size / ITEMS_PER_ROW) + ITEM_CELL_SIZE / 2

		For slotIndex = 0 To player\openInventory\size - 1
			isMouseOn = False
			If (MouseX() > x And MouseX() < x + ITEM_CELL_SIZE) Then
				If (MouseY() > y And MouseY() < y + ITEM_CELL_SIZE) Then
					isMouseOn = True
				EndIf
			EndIf

			If (isMouseOn) Then
				mouseSlot = slotIndex
				
				If (MouseHit1) Then
					;Selecting an item.
					If (player\selectedItem = Null) Then
						If (player\openInventory\items[slotIndex] <> Null) Then
							player\selectedItem = player\openInventory\items[slotIndex]
						EndIf
					EndIf
					
					MouseHit1 = False
					If (DoubleClick) Then
						;Using the item.
						;UseItem(player\openInventory\items[slotIndex])
						DoubleClick = False
					EndIf
				ElseIf (MouseUp1 And player\selectedItem <> Null) Then
					;Item already selected and mouse release.
					
					;Hovering over empty slot. Move the item to the empty slot.
					If (player\openInventory\items[slotIndex] = Null) Then
						;Remove the item from its previous slot.
						For i=0 To player\openInventory\size - 1
							If (player\openInventory\items[i] = player\selectedItem) Then
								player\openInventory\items[i] = Null
								Exit
							EndIf
						Next
						
						player\openInventory\items[slotIndex] = player\selectedItem
						player\selectedItem = Null
					ElseIf (player\openInventory\items[slotIndex] <> player\selectedItem) Then
						;Hovering over another item. Attempt to combine the items.
						;CombineItems(player\selectedItem, player\openInventory\items[slotIndex])
					Else
						;Hovering over the item's slot. Stop selecting the item.
						player\selectedItem = Null
					EndIf
				EndIf

				;If the mouse was hovering over this slot then don't bother iterating through the rest of the inventory.
				Exit
			EndIf

			;Move x and y coords to point to next item.
			x = x + ITEM_CELL_SIZE + ITEM_CELL_SPACING
			If (slotIndex Mod ITEMS_PER_ROW = ITEMS_PER_ROW-1) Then
				y = y + ITEM_CELL_SIZE * 2
				x = userOptions\screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2
			EndIf
		Next

		If (MouseUp1 And player\selectedItem <> Null) Then
			;Mouse release outside a slot, drop the item.
			If (mouseSlot = 66) Then
				DropItem(player\selectedItem)
				player\selectedItem = Null
			EndIf
		EndIf

	;Update any items that are used outside the inventory (firstaid for example).
	Else
		If (player\selectedItem <> Null) Then
			If (MouseHit2) Then
				;TODO: Move to de-equip function.
				EntityAlpha(player\overlays[OVERLAY_BLACK], 0.0)

				PlaySound_SM(sndManager\itemPick[player\selectedItem\template\sound])
				player\selectedItem = Null
			EndIf
		EndIf
	EndIf

	If (prevInvOpen And CurrGameState <> GAMESTATE_INVENTORY) Then
		MoveMouse(viewport_center_x, viewport_center_y)
	EndIf
End Function

Function ToggleInventory(player.Player)
	If (CurrGameState = GAMESTATE_INVENTORY) Then
		If (mainPlayer\openInventory = mainPlayer\inventory) Then
			CurrGameState = GAMESTATE_PLAYING
			ResumeSounds()
			MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1 = 0.0 : mouse_y_speed_1 = 0.0
		Else
			mainPlayer\openInventory = mainPlayer\inventory
		EndIf
	Else
		CurrGameState = GAMESTATE_INVENTORY
		mainPlayer\openInventory = mainPlayer\inventory
		PauseSounds()
	EndIf

	mainPlayer\selectedItem = Null
End Function

Function DrawInventory(player.Player)
	Local MouseSlot% = 66

	Local isMouseOn%

	Local strtemp$

	Local x%, y%, i%, yawvalue#, x1#, x2#, x3#, y1#, y2#, y3#, xtemp%, ytemp%
	
	Local n%
	
	Local tempCamera%, tempLight%, tempObj%
	
	If (CurrGameState = GAMESTATE_INVENTORY) Then
		x = userOptions\screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2
		y = userOptions\screenHeight / 2 - ITEM_CELL_SIZE * (player\openInventory\size / ITEMS_PER_ROW) + ITEM_CELL_SIZE / 2
		
		For  n = 0 To player\openInventory\size - 1
			isMouseOn = False
			If (MouseX() > x And MouseX() < x + ITEM_CELL_SIZE) Then
				If (MouseY() > y And MouseY() < y + ITEM_CELL_SIZE) Then
					isMouseOn = True
				EndIf
			EndIf

			If (isMouseOn) Then
				MouseSlot = n
				Color(255, 0, 0)
				Rect(x - 1, y - 1, ITEM_CELL_SIZE + 2, ITEM_CELL_SIZE + 2)
			EndIf

			Color(255, 255, 255)
			DrawFrame(x, y, ITEM_CELL_SIZE, ITEM_CELL_SIZE, (x Mod 64), (x Mod 64))

			If (player\openInventory\items[n] <> Null) Then
				Color(200, 200, 200)
				If (IsPlayerWearingItem(player,player\openInventory\items[n])) Then
					Rect(x - 3, y - 3, ITEM_CELL_SIZE + 6, ITEM_CELL_SIZE + 6)
				EndIf
				Color(255, 255, 255)
				
				;Render icon.
				If (player\openInventory\items[n]\invImage = 0) Then
					player\openInventory\items[n]\invImage = CreateImage(64,64)
					tempCamera = CreateCamera()
					tempObj = player\openInventory\items[n]\collider
					CameraZoom(tempCamera,1.2)
					tempLight = CreateLight(1)
					AmbientLight(40,40,40)
					
					RotateEntity(tempObj,0,0,0,True)
					
					CameraRange(tempCamera,0.01,512.0*RoomScale)
					CameraViewport(tempCamera,0,0,64,64)
					CameraClsColor(tempCamera,255,0,255)
					PositionEntity(tempCamera,10000.0+10.0*RoomScale,10000.0+70.0*RoomScale,10000.0+20.0*RoomScale,True)
					PositionEntity(tempLight,10000.0,10000.0+20.0*RoomScale,10000.0,True)
					ShowEntity(tempObj)
					PositionEntity(tempObj,10000.0,10000.0,10000.0,True)
					PointEntity(tempCamera,tempObj)
					PointEntity(tempLight,tempObj)
					PositionEntity(tempObj,10000.0,10000.0+12.0*RoomScale,10000.0,True)
					HideEntity(mainPlayer\cam)
					
					SetBuffer(BackBuffer())
					RenderWorld()
					CopyRect(0,0,64,64,0,0,BackBuffer(),ImageBuffer(player\openInventory\items[n]\invImage))
					MaskImage(player\openInventory\items[n]\invImage,255,0,255)
					
					HideEntity(tempObj)
					ShowEntity(mainPlayer\cam)
					FreeEntity(tempCamera)
					FreeEntity(tempLight)
					AmbientLight(Brightness, Brightness, Brightness)
				EndIf
				
				If (player\selectedItem <> player\openInventory\items[n] Or isMouseOn) Then
					DrawImage(player\openInventory\items[n]\invImage, x + ITEM_CELL_SIZE / 2 - 32, y + ITEM_CELL_SIZE / 2 - 32)
				EndIf
			EndIf

			If (player\openInventory\items[n] <> Null And player\selectedItem <> player\openInventory\items[n]) Then
				If (isMouseOn) Then
					If (player\selectedItem = Null) Then
						SetFont(uiAssets\font[0])
						Color(0,0,0)
						Text(x + ITEM_CELL_SIZE / 2 + 1, y + ITEM_CELL_SIZE + ITEM_CELL_SPACING - 15 + 1, player\openInventory\items[n]\name, True)
						Color(255, 255, 255)
						Text(x + ITEM_CELL_SIZE / 2, y + ITEM_CELL_SIZE + ITEM_CELL_SPACING - 15, player\openInventory\items[n]\name, True)
					EndIf
				EndIf
			EndIf

			x = x + ITEM_CELL_SIZE + ITEM_CELL_SPACING
			If (n Mod ITEMS_PER_ROW = ITEMS_PER_ROW-1) Then
				y = y + ITEM_CELL_SIZE * 2
				x = userOptions\screenWidth / 2 - (ITEM_CELL_SIZE * ITEMS_PER_ROW + ITEM_CELL_SPACING * (ITEMS_PER_ROW - 1)) / 2
			EndIf
		Next
		
		;Only re-draw the item under the cursor once it has left the item's original slot.
		If (player\selectedItem <> Null) Then
			If (MouseDown1) Then
				;TODO: Short-circuit eval in C.
				If (MouseSlot = 66) Then
					DrawImage(player\selectedItem\invImage, MouseX() - ImageWidth(player\selectedItem\invImage) / 2, MouseY() - ImageHeight(player\selectedItem\invImage) / 2)
				ElseIf (player\selectedItem <> player\openInventory\items[MouseSlot]) Then
					DrawImage(player\selectedItem\invImage, MouseX() - ImageWidth(player\selectedItem\invImage) / 2, MouseY() - ImageHeight(player\selectedItem\invImage) / 2)
				EndIf
			EndIf
		EndIf
	Else
		If (player\selectedItem <> Null) Then
			;TODO: Draw firstaid, nav, radio, docs.
		EndIf
	EndIf

End Function

Const RADIO_CHANNEL_COUNT% = 5
Type Radio
	Field currChn%

	;How long each channel has been played for.
	Field airTime#[RADIO_CHANNEL_COUNT]

	Field channels%[RADIO_CHANNEL_COUNT]

	;Sounds
	Field sndStatic%
End Type
Global radio.Radio = Null

Function CreateRadio.Radio()
	Local rad.Radio = New Radio

	Return rad
End Function

Function ChangeRadioChannel(newChn%)
	If (IsChannelPlaying(radio\channels[radio\currChn])) Then
		PauseChannel(radio\channels[radio\currChn])
	EndIf

	If (Not IsChannelPlaying(radio\channels[newChn])) Then
		radio\channels[newChn] = PlaySound(radio\sndStatic)
	Else
		ResumeChannel(radio\channels[newChn])
	EndIf

	radio\currChn = newChn
End Function


Function UpdateRadio(i.Item)
	Select radio\currChn
		Case 0 ;-nothing
			;TODO: something?
		Case 1 ;-alarms and alert messages

		Case 2 ;-scp on-site radio

		Case 3 ;-MTF broadcasts

		Case 4 ;-idfk
	End Select

	radio\airTime[radio\currChn] = radio\airTime[radio\currChn] + (timing\tickDuration/70)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
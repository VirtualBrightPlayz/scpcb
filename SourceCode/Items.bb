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

Type ItemTemplates
	Field name$
	Field invName$

	Field obj%
	Field objPath$

	Field invImage%[2]
	Field invImagePath$[2]

	Field tex%
	Field texPath$

	Field sound%

	Field bodySlot%

	Field scale#

	;TODO: Remove.
	Field invSlot%

	Field invimg%,invimg2%,invimgpath$

	Field found%

	Field imgpath$, img% ;Doing something with this that's (hopefully) a little smarter.
End Type

Function CreateItemTemplate(file$, section$)
	Local it.ItemTemplates = New ItemTemplates
	Local flags%

	it\name = section
	it\invName = GetINIString(file, section, "invname")

	;The model and inv image are in the specified directory.
	Local dataPath$ = GetINIString(file, section, "datapath")
	If (dataPath <> "") Then
		If (FileType(dataPath) <> 2) Then RuntimeError("Item template directory not found ("+section+", "+dataPath+")")

		it\objPath = dataPath + it\name + ".b3d"
		it\invImagePath[0] = dataPath + "inv_" + it\name + ".jpg"
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
			it\bodySlot = WORNITEM_SLOT_HEAD
		Case "body"
			it\bodySlot = WORNITEM_SLOT_BODY
		Default
			it\bodySlot = WORNITEM_SLOT_NONE
	End Select

	Local sound$ = Lower(GetINIInt(file, section, "sound"))
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
	Local it2.ItemTemplates
	For it2 = Each ItemTemplates
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
		For it2 = Each ItemTemplates
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
			For it2 = Each ItemTemplates
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

	;TODO: Item scale.
	Local scale# = 1.0
	it\scale = scale
	ScaleEntity(it\obj, scale, scale, scale, True)

	HideEntity it\obj
End Function

Function FindItemTemplate.ItemTemplates(tempname$)
	Local it.ItemTemplates = Null
	Local candidate.ItemTemplates = Null
	For it = Each ItemTemplates
		If it\name = tempname Then
			candidate = it
			Exit
		EndIf
	Next

	Return candidate
End Function

Function LoadItemTemplates(file$)
	Local f% = OpenFile(file)
	Local it.ItemTemplates
	Local section$

	While Not Eof(f)
		section = Trim(ReadLine(f))
		If Left(section,1) = "[" Then
			section = Mid(section, 2, Len(section) - 2)

			CreateItemTemplate(file, section)
		EndIf
	Wend

	CloseFile f
End Function

Function InitItemTemplates()
	Local it.ItemTemplates,it2.ItemTemplates

	

	For it = Each ItemTemplates
		If (it\tex<>0) Then
			If (it\texPath<>"") Then
				For it2=Each ItemTemplates
					If (it2<>it) And (it2\tex=it\tex) Then
						it2\tex = 0
					EndIf
				Next
			EndIf
			FreeTexture it\tex : it\tex = 0
		EndIf
	Next

End Function



Type Items
	Field name$
	Field collider%
	Field model%
	Field itemtemplate.ItemTemplates
	Field dropSpeed#

	Field r%,g%,b%,a#

	Field level% ;TODO: what

	Field dist#, disttimer#

	Field state#, state2#

	Field picked%
	Field dropped%

	Field invimg%
	Field wontColl% = False
	Field xspeed#
	Field zspeed#
	Field id%

	Field Inventory.Inventory = Null
End Type

Const MAX_ITEM_COUNT% = 20
Type Inventory
	Field items.Items[MAX_ITEM_COUNT]
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
		If inv\items[i]<>Null Then RemoveItem(inv\items[i])
	Next
	Delete inv
End Function

Function CountItemsInInventory%(inv.Inventory)
	Local retVal% = 0
	Local i%
	For i=0 To inv\size-1
		If inv\items[i]<>Null Then
			retVal=retVal+1
		EndIf
	Next
	Return retVal
End Function

Global LastItemID%

Function CreateItem.Items(name$, tempname$, x#, y#, z#, r# = 1.0, g# = 1.0, b# = 1.0, a# = 1.0, invSlots%=0)
	Local i.Items = New Items
	Local it.ItemTemplates

	name = Lower(name)
	tempname = Lower(tempname)

	For it.ItemTemplates = Each ItemTemplates
		If Lower(it\name) = name Then
			If Lower(it\name) = tempname Then
				i\itemtemplate = it
				i\collider = CreatePivot()
				EntityRadius i\collider, 0.01
				EntityPickMode i\collider, 1, False
				i\model = CopyEntity(it\obj,i\collider)
				i\name = it\name
				ShowEntity i\collider
				ShowEntity i\model
			EndIf
		EndIf
	Next

	i\wontColl = False

	;If i\itemtemplate = Null Then RuntimeError("Item template not found ("+name+", "+tempname+")")
	i\model = CreateMesh()
	i\collider = CreatePivot()

	ResetEntity i\collider
	PositionEntity(i\collider, x, y, z, True)
	RotateEntity (i\collider, 0, Rand(360), 0)
	i\dist = EntityDistance(mainPlayer\collider, i\collider)
	i\dropSpeed = 0.0

	;If tempname = "cup" Then
	;	i\r=r
	;	i\g=g
	;	i\b=b
	;	i\a=a

		;TODO: re-implement.
		;Local liquid = CopyEntity(LiquidObj)
		;ScaleEntity liquid, i\itemtemplate\scale,i\itemtemplate\scale,i\itemtemplate\scale,True
		;PositionEntity liquid, EntityX(i\collider,True),EntityY(i\collider,True),EntityZ(i\collider,True)
		;EntityParent liquid, i\model
		;EntityColor liquid, r,g,b

		;If a < 0 Then
		;	EntityFX liquid, 1
		;	EntityAlpha liquid, Abs(a)
		;Else
		;	EntityAlpha liquid, Abs(a)
		;EndIf

		;EntityShininess liquid, 1.0
	;EndIf

	i\invimg = CreateImage(64, 64) ;i\invimg = i\itemtemplate\invimg

;	If (tempname="clipboard") And (invSlots=0) Then
;		invSlots = 20
;		SetAnimTime i\model,17.0
;		i\invimg = i\itemtemplate\invimg2
;	EndIf

	i\inventory = Null
	If invSlots>0 Then
		i\inventory = CreateInventory(invSlots)
	EndIf

	i\id=LastItemID+1
	LastItemID=i\id

	Return i
End Function

Function RemoveItem(i.Items)
	If i\inventory<>Null Then DeleteInventory(i\inventory)

	DropItem(i,False)

	Local n%
	FreeEntity(i\model) : FreeEntity(i\collider) : i\collider = 0

	If i\itemtemplate\img <> 0 Then
		FreeImage i\itemtemplate\img
		i\itemtemplate\img = 0
	EndIf
	Delete i

End Function


Function UpdateItems()
	Local n%, i.Items, i2.Items
	Local xtemp#, ytemp#, ztemp#
	Local temp%, np.NPCs

	Local HideDist% = HideDistance*0.5
	Local deletedItem% = False
	
	Local ed#

	mainPlayer\closestItem = Null
	For i.Items = Each Items
		i\dropped = 0

		If (Not i\picked) Then
			If i\disttimer < TimeInPosMilliSecs() Then
				i\dist = EntityDistance(mainPlayer\collider, i\collider)
				i\disttimer = TimeInPosMilliSecs() + Rand(600,800)
				If i\dist < HideDist Then ShowEntity i\collider
			EndIf

			If i\dist < HideDist Then
				ShowEntity i\collider

				If (Not EntityVisible(i\collider,mainPlayer\cam)) Then
					;the player can't grab this
					If (Not EntityVisible(i\collider,mainPlayer\collider)) Then i\dist = 2.5
				EndIf

				If i\dist < 1.2 Then
					If mainPlayer\closestItem = Null Then
						If EntityInView(i\model, mainPlayer\cam) Then mainPlayer\closestItem = i
						ElseIf mainPlayer\closestItem = i Or i\dist < EntityDistance(mainPlayer\collider, mainPlayer\closestItem\collider) Then
							If EntityInView(i\model, mainPlayer\cam) Then mainPlayer\closestItem = i
					EndIf
				EndIf

				If EntityCollided(i\collider, HIT_MAP) Then
					i\dropSpeed = 0
					i\xspeed = 0.0
					i\zspeed = 0.0
				Else
					i\dropSpeed = i\dropSpeed - 0.0004 * timing\tickDuration
					TranslateEntity i\collider, i\xspeed*timing\tickDuration, i\dropSpeed * timing\tickDuration, i\zspeed*timing\tickDuration
					If i\wontColl Then ResetEntity(i\collider)
				EndIf

				If i\dist<HideDist*0.2 Then
					For i2.Items = Each Items
						If i<>i2 And (Not i2\picked) And i2\dist<HideDist*0.2 Then

							xtemp# = (EntityX(i2\collider,True)-EntityX(i\collider,True))
							ytemp# = (EntityY(i2\collider,True)-EntityY(i\collider,True))
							ztemp# = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))

							ed = (xtemp*xtemp+ztemp*ztemp)
							If ed<0.07 And Abs(ytemp)<0.25 Then
								;items are too close together, push away

								xtemp = xtemp*(0.07-ed)
								ztemp = ztemp*(0.07-ed)

								While Abs(xtemp)+Abs(ztemp)<0.001
									xtemp = xtemp+Rnd(-0.002,0.002)
									ztemp = ztemp+Rnd(-0.002,0.002)
								Wend

								TranslateEntity i2\collider,xtemp,0,ztemp
								TranslateEntity i\collider,-xtemp,0,-ztemp
							EndIf
						EndIf
					Next
				EndIf

				If EntityY(i\collider) < - 35.0 Then DebugLog "remove: " + i\itemtemplate\name:RemoveItem(i):deletedItem=True
			Else
				HideEntity i\collider
			EndIf
		EndIf

		deletedItem = False
	Next

	If mainPlayer\closestItem <> Null Then
		;DrawHandIcon = True

		If MouseHit1 Then PickItem(mainPlayer\closestItem)
	EndIf

End Function

Function PickItem(item.Items)
	Local n% = 0
	Local e.Events
	Local z%

	If CountItemsInInventory(mainPlayer\inventory) < mainPlayer\inventory\size Then
		For n% = 0 To mainPlayer\inventory\size - 1
			If mainPlayer\inventory\items[n] = Null Then
				Select item\itemtemplate\name
					Case "1123"
						If mainPlayer\currRoom\roomTemplate\name <> "room1123" Then
							ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
							mainPlayer\lightFlash = 7.0
							PlaySound2(LoadTempSound("SFX/SCP/1123/Touch.ogg"))
							DeathMSG = "Subject D-9341 was shot dead after attempting to attack a member of Nine-Tailed Fox. Surveillance tapes show that the subject had been "
							DeathMSG = DeathMSG + "wandering around the site approximately 9 minutes prior, shouting the phrase " + Chr(34) + "get rid of the four pests" + Chr(34)
							DeathMSG = DeathMSG + " in chinese. SCP-1123 was found in [REDACTED] nearby, suggesting the subject had come into physical contact with it. How "
							DeathMSG = DeathMSG + "exactly SCP-1123 was removed from its containment chamber is still unknown."
							Kill(mainPlayer)
							Return
						EndIf
						For e.Events = Each Events
							If e\name = "room1123" Then
								If e\eventState = 0 Then
									ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
									mainPlayer\lightFlash = 3.0
									PlaySound2(LoadTempSound("SFX/SCP/1123/Touch.ogg"))
								EndIf
								e\eventState = Max(1, e\eventState)
								Exit
							EndIf
						Next
					Case "killbat"
						ShowEntity mainPlayer\overlays[OVERLAY_WHITE]
						mainPlayer\lightFlash = 1.0
						PlaySound2(IntroSFX(11))
						DeathMSG = "Subject D-9341 found dead inside SCP-914's output booth next to what appears to be an ordinary nine-volt battery. The subject is covered in severe "
						DeathMSG = DeathMSG + "electrical burns, and assumed to be killed via an electrical shock caused by the battery. The battery has been stored for further study."
						Kill(mainPlayer)
					Case "veryfinevest"
						Msg = "The vest is too heavy to pick up."
						MsgTimer = 70*6
						Exit
					Case "firstaid", "finefirstaid", "veryfinefirstaid", "firstaid2"
						item\state = 0
					Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
						Msg = "You put on the hazmat suit."
						MsgTimer = 70 * 5
						mainPlayer\wornItems[WORNITEM_SLOT_BODY] = item

						For z% = 0 To mainPlayer\inventory\size - 1
							If mainPlayer\inventory\items[z] <> Null Then
								If mainPlayer\inventory\items[z]\itemtemplate\name="hazmatsuit" Or mainPlayer\inventory\items[z]\itemtemplate\name="hazmatsuit2" Or mainPlayer\inventory\items[z]\itemtemplate\name="hazmatsuit3" Then
									DropItem(mainPlayer\inventory\items[z])
								EndIf
							EndIf
						Next

				End Select

				If item\itemtemplate\sound <> 66 Then PlaySound_SM(sndManager\itemPick[item\itemtemplate\sound])
				item\picked = True
				item\dropped = -1

				item\itemtemplate\found=True

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

Function DropItem(item.Items,playDropSound%=True)
	Local player.Player
	For player.Player = Each Player
		DeEquipItem(player,item)
	Next

	If playDropSound And (item\itemtemplate\sound <> 66) Then PlaySound_SM(sndManager\itemPick[item\itemtemplate\sound])

	item\dropped = 1

	ShowEntity(item\collider)
	PositionEntity(item\collider, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam))
	RotateEntity(item\collider, EntityPitch(mainPlayer\cam), EntityYaw(mainPlayer\cam)+Rnd(-20,20), 0)
	MoveEntity(item\collider, 0, -0.1, 0.1)
	RotateEntity(item\collider, 0, EntityYaw(mainPlayer\cam)+Rnd(-110,110), 0)

	ResetEntity (item\collider)

	;move the item so that it doesn't overlap with other items
	;For it.Items = Each Items
	;	If it <> item And it\picked = False Then
	;		x = Abs(EntityX(item\collider, True)-EntityX(it\collider, True))
	;		;If x < 0.2 Then
	;		If x < 0.01 Then
	;			z = Abs(EntityZ(item\obj, True)-EntityZ(it\collider, True))
	;			;If z < 0.2 Then
	;			If z < 0.01 Then
	;				;While (x+z)<0.25
	;				While (x+z)<0.05
	;					;MoveEntity(item\obj, 0, 0, 0.025)
	;					MoveEntity(item\collider, 0, 0, 0.005)
	;					x = Abs(EntityX(item\collider, True)-EntityX(it\collider, True))
	;					z = Abs(EntityZ(item\collider, True)-EntityZ(it\collider, True))
	;				Wend
	;			EndIf
	;		EndIf
	;	EndIf
	;Next

	item\picked = False
	Local inv.Inventory
	Local j%
	For inv.Inventory = Each Inventory
		For j%=0 To inv\size-1
			If inv\items[j]=item Then inv\items[j]=Null
		Next
	Next
	;Select item\itemtemplate\name
	;	Case "gasmask", "supergasmask", "gasmask3"
	;		WearingGasMask = False
	;	Case "hazmatsuit",  "hazmatsuit2", "hazmatsuit3"
	;		WearingHazmat = False
	;	Case "vest", "finevest"
	;		WearingVest = False
	;	Case "nvgoggles"
	;		If WearingNightVision = 1 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
	;	Case "supernv"
	;		If WearingNightVision = 2 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
	;	Case "veryfinenvgoggles"
	;		If WearingNightVision = 3 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
	;	Case "scp1499","super1499"
	;		Wearing1499 = False
	;End Select
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


Function UpdateRadio(i.Items)
	Select radio\currChn
		Case 0 ;-nothing
			;TODO: something?
		Case 1 ;-alarms and alert messages

		Case 2 ;-scp on-site radio

		Case 3 ;-MTF broadcasts

		Case 4 ;-idfk
	End Select

	radio\airTime[radio\currChn] = radio\airTime[radio\currChn] + timing\tickDuration
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
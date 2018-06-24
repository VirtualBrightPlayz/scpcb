Function FillRoom_hll_gas_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams, em.Emitters
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    em = CreateEmitter(r\x + 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
    TurnEntity(em\obj, -90, 0, 0)
    EntityParent(em\obj, r\obj)
    em\randAngle = 55
    em\speed = 0.0005
    em\aChange = -0.015
    em\sizeChange = 0.007

    em = CreateEmitter(r\x - 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
    TurnEntity(em\obj, -90, 0, 0)
    EntityParent(em\obj, r\obj)
    em\randAngle = 55
    em\speed = 0.0005
    em\aChange = -0.015
    em\sizeChange = 0.007

    r\objects[0]= CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 704.0 * RoomScale, 112.0*RoomScale, r\z-416.0*RoomScale, True)
End Function


Function UpdateEventRoom3pitduck(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (e\room\objects[2] = 0) Then
			e\room\objects[2] =	LoadMesh("GFX/npcs/duck_low_res.b3d")
			ScaleEntity(e\room\objects[2], 0.07, 0.07, 0.07)
			tex = LoadTexture("GFX/npcs/duck1.png")
			EntityTexture(e\room\objects[2], tex)
			FreeTexture(tex)
			PositionEntity(e\room\objects[2], EntityX(e\room\objects[0],True), EntityY(e\room\objects[0],True), EntityZ(e\room\objects[0],True))
			PointEntity(e\room\objects[2], e\room\obj)
			RotateEntity(e\room\objects[2], 0, EntityYaw(e\room\objects[2],True),0, True)

			LoadEventSound(e,"SFX/SCP/Joke/Saxophone.ogg")
		Else
			If (EntityInView(e\room\objects[2],mainPlayer\cam)=False) Then
				e\eventState = e\eventState + timing\tickDuration
				If (Rand(200)=1 And e\eventState > 300) Then
					e\eventState = 0
					e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\objects[2],6.0)
				EndIf
			Else
				If (e\soundChannels[0] <> 0) Then
					If (IsChannelPlaying(e\soundChannels[0])) Then StopChannel(e\soundChannels[0])
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function



Function UpdateEventRoom3pit1048(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%, brush%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, itt.ItemTemplates, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, imgPath$, sf%, b%, t%, texname$
	
	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (e\room\objects[2] = 0) Then
			e\room\objects[2] =	LoadAnimMesh("GFX/npcs/scp-1048pp.b3d")
			ScaleEntity(e\room\objects[2], 0.05,0.05,0.05)
			SetAnimTime(e\room\objects[2], 414)

			imgPath = "GFX/items/1048/1048_"+Str(Rand(1,20))+".jpg"
			
			For itt = Each ItemTemplates
				If (itt\name = "Drawing") Then
					If (itt\img<>0) Then FreeImage(itt\img)
					itt\img = LoadImage(imgPath)
					MaskImage(itt\img, 255,0,255)
					itt\imgpath = imgPath

					Exit
				EndIf
			Next

			tex = LoadTexture(imgPath)
			brush = LoadBrush(imgPath, 1)

			For i = 1 To CountSurfaces(e\room\objects[2])
				sf = GetSurface(e\room\objects[2],i)
				b = GetSurfaceBrush( sf )
				t = GetBrushTexture(b, 0)
				texname = StripPath(TextureName(t))
				DebugLog("texname: "+texname)
				If (Lower(texname) = "1048_1.jpg") Then
					PaintSurface(sf, brush)
				EndIf
				;TODO: I'm just todo-ing so Juan will see it later and explain it to me.
				;MAV???
				;If (texname<>"") Then FreeTexture(t)
				FreeBrush(b)
			Next

			FreeTexture(tex)
			FreeBrush(brush)

			PositionEntity(e\room\objects[2], EntityX(e\room\objects[0],True), EntityY(e\room\objects[0],True), EntityZ(e\room\objects[0],True))

			;e\sounds[0] = LoadSound("SFX/SCP/Joke/Saxophone.ogg")
		Else
			PointEntity(e\room\objects[2], mainPlayer\collider)
			RotateEntity(e\room\objects[2], -90, EntityYaw(e\room\objects[2],True),0, True)

			If (e\eventState=0) Then
				If (EntityDistance(mainPlayer\collider, e\room\objects[2])<3.0) Then
					If (EntityInView(e\room\objects[2],mainPlayer\cam)) Then
						e\eventState = 1
					EndIf
				EndIf
			ElseIf (e\eventState=1) Then
				Animate2(e\room\objects[2], AnimTime(e\room\objects[2]), 414, 543, 0.5, False)
				If (AnimTime(e\room\objects[2])=543) Then e\eventState=2
			ElseIf (e\eventState = 2) Then
				Animate2(e\room\objects[2], AnimTime(e\room\objects[2]), 543, 692, 1.0)
				If (EntityDistance(mainPlayer\collider, e\room\objects[2])<1.5) Then
					DrawHandIcon = True

					If (MouseHit1) Then
						mainPlayer\selectedItem = CreateItem("paper", 0.0, 0.0, 0.0)

						If (CountItemsInInventory(mainPlayer\inventory) >= mainPlayer\inventory\size) Then DropItem(mainPlayer\inventory\items[0])

						PickItem(mainPlayer\selectedItem)

						FreeEntity(e\room\objects[2])

						e\eventState = 3
						RemoveEvent(e)
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
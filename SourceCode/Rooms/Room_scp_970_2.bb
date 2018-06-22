Function FillRoom_scp_970_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\roomDoors[0] = CreateDoor(r\zone, r\x - 1288.0 * RoomScale, 0, r\z, 270, r)
    r\roomDoors[1] = CreateDoor(r\zone, r\x - 760.0 * RoomScale, 0, r\z, 270, r)
    r\roomDoors[2] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z, 270, r)
    r\roomDoors[3] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 270, r)
    r\roomDoors[4] = CreateDoor(r\zone, r\x + 760.0 * RoomScale, 0, r\z, 270, r)
    r\roomDoors[5] = CreateDoor(r\zone, r\x + 1288.0 * RoomScale, 0, r\z, 270, r)

    For i = 0 To 5
        MoveEntity(r\roomDoors[i]\buttons[0], 0,0,-8.0)
        MoveEntity(r\roomDoors[i]\buttons[1], 0,0,-8.0)
        r\roomDoors[i]\autoClose = False : r\roomDoors[i]\open = False
    Next

    it = CreateItem("Document SCP-939", "paper", r\x + 352.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 256.0 * RoomScale)
    RotateEntity(it\collider, 0, r\angle+4, 0)
    EntityParent(it\collider, r\obj)

    it = CreateItem("9V Battery", "bat", r\x + 352.0 * RoomScale, r\y + 112.0 * RoomScale, r\z + 448.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("Empty Cup", "emptycup", r\x-672*RoomScale, 240*RoomScale, r\z+288.0*RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("Level 1 Key Card", "key1", r\x - 672.0 * RoomScale, r\y + 240.0 * RoomScale, r\z + 224.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_scp_970_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, it2.Items, itt.ItemTemplates, em.Emitters, sc.SecurityCams, sc2.SecurityCams, de.Decals

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, tex970%

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (e\eventState2 <= 0) Then
			e\room\roomDoors[1]\locked = False
			e\room\roomDoors[4]\locked = False

			If (EntityDistance(mainPlayer\collider, Curr173\obj)<8.0 Or EntityDistance(mainPlayer\collider, Curr106\obj)<8.0) Then
				e\room\roomDoors[1]\locked = True
				e\room\roomDoors[4]\locked = True
			Else
				For n.NPCs = Each NPCs
					If (n\npcType = NPCtypeMTF) Then
						If (EntityDistance(mainPlayer\collider, Curr173\obj)<8.0) Then
							e\room\roomDoors[1]\locked = True
							e\room\roomDoors[4]\locked = True
							Exit
						EndIf
					EndIf
				Next
			EndIf
			e\eventState2 = 70*5
		Else
			e\eventState2 = e\eventState2 - timing\tickDuration
		EndIf

		;LightVolume = TempLightVolume*0.5

		TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,e\room\obj)

		temp = 0
		If (TFormedX()>730) Then
			UpdateWorld()
			TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,e\room\obj)

			;1->3, 2->4
			;3->0, 4->0
			For i = 1 To 2
				e\room\roomDoors[i]\open = e\room\roomDoors[i+2]\open
				e\room\roomDoors[i]\openstate = e\room\roomDoors[i+2]\openstate
				PositionEntity(e\room\roomDoors[i]\obj, EntityX(e\room\roomDoors[i+2]\obj),EntityY(e\room\roomDoors[i+2]\obj),EntityZ(e\room\roomDoors[i+2]\obj))
				PositionEntity(e\room\roomDoors[i]\obj2, EntityX(e\room\roomDoors[i+2]\obj2),EntityY(e\room\roomDoors[i+2]\obj2),EntityZ(e\room\roomDoors[i+2]\obj2))

				e\room\roomDoors[i+2]\open = False
				e\room\roomDoors[i+2]\openstate = 0
				PositionEntity(e\room\roomDoors[i+2]\obj, EntityX(e\room\roomDoors[0]\obj),EntityY(e\room\roomDoors[0]\obj),EntityZ(e\room\roomDoors[0]\obj))
				PositionEntity(e\room\roomDoors[i+2]\obj2, EntityX(e\room\roomDoors[0]\obj2),EntityY(e\room\roomDoors[0]\obj2),EntityZ(e\room\roomDoors[0]\obj2))
			Next

			TFormPoint(TFormedX()-1024, TFormedY(), TFormedZ(),e\room\obj,0)
			HideEntity(mainPlayer\collider)
			PositionEntity(mainPlayer\collider, TFormedX(), EntityY(mainPlayer\collider), TFormedZ(), True)
			ShowEntity(mainPlayer\collider)
			DebugLog("tformedx()>720")
			temp = True

		ElseIf (TFormedX()<-730) Then
			UpdateWorld()
			TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,e\room\obj)

			;3->1, 4->2
			;1->0, 2->0
			For i = 1 To 2
				e\room\roomDoors[i+2]\open = e\room\roomDoors[i]\open
				e\room\roomDoors[i+2]\openstate = e\room\roomDoors[i]\openstate
				PositionEntity(e\room\roomDoors[i+2]\obj, EntityX(e\room\roomDoors[i]\obj),EntityY(e\room\roomDoors[i]\obj),EntityZ(e\room\roomDoors[i]\obj))
				PositionEntity(e\room\roomDoors[i+2]\obj2, EntityX(e\room\roomDoors[i]\obj2),EntityY(e\room\roomDoors[i]\obj2),EntityZ(e\room\roomDoors[i]\obj2))

				e\room\roomDoors[i]\open = False
				e\room\roomDoors[i]\openstate = 0
				PositionEntity(e\room\roomDoors[i]\obj, EntityX(e\room\roomDoors[0]\obj),EntityY(e\room\roomDoors[0]\obj),EntityZ(e\room\roomDoors[0]\obj))
				PositionEntity(e\room\roomDoors[i]\obj2, EntityX(e\room\roomDoors[0]\obj2),EntityY(e\room\roomDoors[0]\obj2),EntityZ(e\room\roomDoors[0]\obj2))
			Next

			TFormPoint(TFormedX()+1024, TFormedY(), TFormedZ(),e\room\obj,0)
			HideEntity(mainPlayer\collider)
			PositionEntity(mainPlayer\collider, TFormedX(), EntityY(mainPlayer\collider), TFormedZ(), True)
			ShowEntity(mainPlayer\collider)

			DebugLog("tformedx()<720")

			temp = True
		EndIf

		If (temp = True) Then

			e\eventState=e\eventState+1;Rand(1,2)

			For it.Items = Each Items
				If (EntityDistance(it\collider,mainPlayer\collider)<5.0) Then

					TFormPoint(EntityX(it\collider),EntityY(it\collider),EntityZ(it\collider),0,e\room\obj)
					x = TFormedX() : y = TFormedY() : z = TFormedZ()
					If (TFormedX()>264) Then
						TFormPoint(x-1024,y,z,e\room\obj,0)
						PositionEntity(it\collider, TFormedX(), TFormedY(), TFormedZ())
						ResetEntity(it\collider)
					ElseIf (TFormedX()<-264) Then
						TFormPoint(x+1024,y,z,e\room\obj,0)
						PositionEntity(it\collider, TFormedX(), TFormedY(), TFormedZ())
						ResetEntity(it\collider)
					EndIf

				EndIf
			Next

			Select e\eventState
				Case 2
					i = Rand(mainPlayer\inventory\size)
					If (mainPlayer\inventory\items[i]<>Null) Then RemoveItem(mainPlayer\inventory\items[i])
				Case 5
					mainPlayer\injuries = mainPlayer\injuries + 0.3
				Case 10
					de.Decals = CreateDecal(3, EntityX(e\room\obj)+Cos(e\room\angle-90)*760*RoomScale, 0.0005, EntityZ(e\room\obj)+Sin(e\room\angle-90)*760*RoomScale,90,Rnd(360),0)
				Case 14
					For i = 0 To mainPlayer\inventory\size-1
						If (mainPlayer\inventory\items[i]<> Null) Then
							If (mainPlayer\inventory\items[i]\itemtemplate\name = "paper") Then
								RemoveItem(mainPlayer\inventory\items[i])
								For itt.ItemTemplates = Each ItemTemplates
									If (itt\name = "paper" And Rand(6)=1) Then
										mainPlayer\inventory\items[i] = CreateItem(itt\name, itt\name, 1,1,1)
										HideEntity(mainPlayer\inventory\items[i]\collider)
										mainPlayer\inventory\items[i]\picked = True
										Exit
									EndIf
								Next
								Exit
							EndIf
						EndIf
					Next
				Case 18
					TFormPoint(-344,176, 272, e\room\obj,0)
					it.Items = CreateItem("Strange Note", "paper", TFormedX(), TFormedY(), TFormedZ())
					EntityType(it\collider, HIT_ITEM)
				Case 25
					e\room\npc[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj)+Cos(e\room\angle-90)*760*RoomScale, 0.35, EntityZ(e\room\obj)+Sin(e\room\angle-90)*760*RoomScale)
					RotateEntity(e\room\npc[0]\collider, 0, e\room\angle-200, 0, True)
					tex=LoadTexture("GFX/NPCs/corpse.jpg")
					e\room\npc[0]\texture = "GFX/NPCs/corpse.jpg"
					EntityTexture(e\room\npc[0]\obj, tex)
					FreeTexture(tex)
					SetAnimTime(e\room\npc[0]\obj,80)
					e\room\npc[0]\state=10
				Case 30
					i = Rand(0,mainPlayer\inventory\size)
					If (mainPlayer\inventory\items[i]<>Null) Then RemoveItem(mainPlayer\inventory\items[i])
					mainPlayer\inventory\items[i] = CreateItem("Strange Note", "paper", 1,1,1)
					HideEntity(mainPlayer\inventory\items[i]\collider)
					mainPlayer\inventory\items[i]\picked = True
				Case 35
					For i = 0 To 3
						de.Decals = CreateDecal(17, e\room\x+Rnd(-2,2), 700*RoomScale, e\room\z+Rnd(-2,2), 270, Rand(360), 0)
						de\size = 0.05 : de\sizeChange = 0.0005 : EntityAlpha(de\obj, 0.8) : UpdateDecals()
					Next
				Case 40
					PlaySound2(LoadTempSound("SFX/radio/franklin4.ogg"))
				Case 50
					e\room\npc[1]=CreateNPC(NPCtypeGuard, EntityX(e\room\obj)+Cos(e\room\angle+90)*600*RoomScale, 0.35, EntityZ(e\room\obj)+Sin(e\room\angle+90)*600*RoomScale)
					e\room\npc[1]\state=7
				Case 52
					If (e\room\npc[1] <> Null) Then
						RemoveNPC(e\room\npc[1])
						e\room\npc[1]=Null
					EndIf
				Case 60
					If ((Not HalloweenTex)) Then
						tex970 = LoadTexture("GFX/npcs/173h.pt", 1)
						EntityTexture(Curr173\obj, tex970, 0, 0)
						FreeTexture(tex970)
					EndIf
			End Select

			;TODO: Reimplement
			;If (Rand(10)=1) Then
			;	temp = Rand(0,2)
			;	PlaySound2(AmbientSFX(temp, Rand(0,AmbientSFXAmount(temp)-1)))
			;EndIf
		Else
			If (e\room\npc[0] <> Null) Then
				If (EntityDistance(mainPlayer\collider, e\room\npc[0]\collider)<3.0) Then
					If (EntityInView(e\room\npc[0]\obj, mainPlayer\cam)) Then
						mainPlayer\camZoom = (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0)*15.0
						;HeartBeatVolume = Max(CurveValue(0.3, HeartBeatVolume, 2.0), HeartBeatVolume)
						mainPlayer\heartbeatIntensity = Max(mainPlayer\heartbeatIntensity, 120)
					EndIf
				EndIf
			EndIf

			If (e\room\npc[1] <> Null) Then
				PointEntity(e\room\npc[1]\obj, mainPlayer\collider)
				RotateEntity(e\room\npc[1]\collider, 0, CurveAngle(EntityYaw(e\room\npc[1]\obj),EntityYaw(e\room\npc[1]\collider),35),0)
			EndIf

			;If (Abs(TFormedX())<264) Then
			For it.Items = Each Items
				If ((it\dropped=1 And Abs(TFormedX())<264) Or it\dropped=-1) Then
					DebugLog("dropping/picking: "+it\dropped+" - "+EntityX(it\collider)+", "+EntityY(it\collider)+", "+EntityZ(it\collider))

					TFormPoint(EntityX(it\collider),EntityY(it\collider),EntityZ(it\collider),0,e\room\obj)
					x = TFormedX() : y = TFormedY() : z = TFormedZ()

					If (it\dropped=1) Then
						For i = - 1 To 1 Step 2
							TFormPoint(x+1024*i,y,z,e\room\obj,0)
							it2.Items = CreateItem(it\name, it\itemtemplate\name, TFormedX(), EntityY(it\collider), TFormedZ())
							RotateEntity(it2\collider, EntityPitch(it\collider),EntityYaw(it\collider),0)
							EntityType(it2\collider, HIT_ITEM)
						Next
					Else
						For it2.Items = Each Items
							If (it2<>it And it2\dist < 15.0) Then

								TFormPoint(EntityX(it2\collider),EntityY(it2\collider),EntityZ(it2\collider),0,e\room\obj)
								DebugLog(TFormedZ()+" - "+z)

								If (TFormedZ()=z) Then RemoveItem(it2) : DebugLog("item removed")
							EndIf
						Next
					EndIf

					Exit
				EndIf
			Next
			;EndIf
		EndIf

	EndIf


	If (e\eventState > 26) Then
		If (Abs(EntityX(mainPlayer\collider)-e\room\x)<8.0) Then
			If (Abs(EntityZ(mainPlayer\collider)-e\room\z)<8.0) Then
				If (e\sounds[0] = 0) Then
					e\sounds[0] = LoadSound("SFX/SCP/970/Corpse.ogg")
				EndIf
				e\soundChannels[0] = LoopRangedSound(e\sounds[0], e\soundChannels[0], mainPlayer\cam, e\room\npc[0]\obj);
				If (e\eventState < 30) Then
					;LightVolume = TempLightVolume*0.4
				ElseIf (e\eventState > 60) Then
					AnimateNPC(e\room\npc[0], 80, 61, -0.02, False)

					e\room\npc[0]\dropSpeed = 0
					y = CurveValue(1.5+Sin(Float(TimeInPosMilliSecs())/20.0)*0.1,EntityY(e\room\npc[0]\collider),50.0)

					PositionEntity(e\room\npc[0]\collider,EntityX(e\room\npc[0]\collider),y,EntityZ(e\room\npc[0]\collider))
					TurnEntity(e\room\npc[0]\collider,0,0.1*timing\tickDuration,0)
				EndIf
			EndIf

		EndIf
	EndIf


	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
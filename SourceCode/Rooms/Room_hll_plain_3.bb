Function UpdateEvent106victim(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams, de.Decals

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, fdir#

	;[Block]
	If (Not Contained106) Then
		If mainPlayer\currRoom = e\room Then
			If e\eventState = 0 Then
				de.Decals = CreateDecal(0, EntityX(e\room\obj), 799.0*RoomScale, EntityZ(e\room\obj), -90, Rand(360), 0)
				de\size = 0.05 : de\sizeChange = 0.0015 : EntityAlpha(de\obj, 0.8) : UpdateDecals()
				;TODO: fix
				;PlayRangedSound(DecaySFX(3), mainPlayer\cam, de\obj, 15.0)
				e\eventState=1
			EndIf
		EndIf

		If e\eventState > 0 Then
			If e\room\npc[0]=Null Then
				e\eventState=e\eventState+timing\tickDuration
			EndIf
			If e\eventState>200 Then
				If e\room\npc[0]=Null Then
					e\room\npc[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj), 900.0*RoomScale, EntityZ(e\room\obj))
					RotateEntity(e\room\npc[0]\collider, 0, Rnd(360), 0, True)
					tex=LoadTexture("GFX/NPCs/106victim.jpg")
					e\room\npc[0]\texture = "GFX/NPCs/106victim.jpg"
					EntityTexture(e\room\npc[0]\obj, tex)
					FreeTexture(tex)
					e\room\npc[0]\state=6

					;PlaySound2(HorrorSFX(0))
					;TODO: fix
					;PlayRangedSound(DecaySFX(2), mainPlayer\cam, e\room\npc[0]\collider, 15.0)
				EndIf

				EntityType(e\room\npc[0]\collider,HIT_PLAYER)
				If EntityY(e\room\npc[0]\collider)>0.35 Then
					AnimateNPC(e\room\npc[0], 1, 10, 0.12, False)
					dist# = EntityDistance(mainPlayer\collider,e\room\npc[0]\collider)
					If dist<0.8 Then ;get the player out of the way
						fdir# = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\npc[0]\collider,True),EntityZ(e\room\npc[0]\collider,True))
						TranslateEntity(mainPlayer\collider,Cos(-fdir+90)*(dist-0.8)*(dist-0.8),0,Sin(-fdir+90)*(dist-0.8)*(dist-0.8))
					EndIf

					If EntityY(e\room\npc[0]\collider)>0.6 Then EntityType(e\room\npc[0]\collider,0)
				Else
					e\eventState=e\eventState+timing\tickDuration
					AnimateNPC(e\room\npc[0], 11, 19, 0.25, False)
					If e\sounds[0]=0 Then
						;e\sounds[0] = LoadSound("SFX/General/BodyFall.ogg")
						LoadEventSound(e,"SFX/General/BodyFall.ogg")
						PlaySound2(e\sounds[0])

						de.Decals = CreateDecal(0, EntityX(e\room\obj), 0.001, EntityZ(e\room\obj), 90, Rand(360), 0)
						de\size = 0.4 : EntityAlpha(de\obj, 0.8) : UpdateDecals()
					EndIf

					If e\eventState>400 Then
						If e\sounds[0]<>0 Then
							FreeSound(e\sounds[0])
							e\sounds[0]=0
						EndIf
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
Function FillRoom_hll_ele_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x+888.0*RoomScale, 240.0*RoomScale, r\z, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x+1024.0*RoomScale-0.01, 120.0*RoomScale, r\z, True)

    r\doors[0] = CreateDoor(r\x + 448.0 * RoomScale, 0.0, r\z, 90, r)
    PositionEntity(r\doors[0]\buttons[1], r\x + 416.0 * RoomScale, EntityY(r\doors[0]\buttons[1],True), r\z - 208.0 * RoomScale,True)
    PositionEntity(r\doors[0]\buttons[0], r\x + 480.0 * RoomScale, EntityY(r\doors[0]\buttons[0],True), r\z + 184.0 * RoomScale,True)
    r\doors[0]\autoClose = False : r\doors[0]\open = True : r\doors[0]\locked = True
End Function

Function UpdateEventRoom2elevator2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\room\dist < 8.0 And e\room\dist > 0) Then

		de = CreateDecal(DECAL_BLOOD_SPLATTER, EntityX(e\room\objects[0],True), 0.0005, EntityZ(e\room\objects[0],True),90,Rnd(360),0)

		de = CreateDecal(DECAL_BLOOD_POOL, EntityX(e\room\objects[0],True), 0.002, EntityZ(e\room\objects[0],True),90,Rnd(360),0)
		de\size = 0.5

		e\room\npc[0]=CreateNPC(NPCtypeD, EntityX(e\room\objects[0],True), 0.5, EntityZ(e\room\objects[0],True))
		e\room\npc[0]\texture = "GFX/NPCs/gonzales.jpg"
		tex = LoadTexture(e\room\npc[0]\texture)
		EntityTexture(e\room\npc[0]\obj, tex)
		FreeTexture(tex)

		RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\obj)-80,0, True)

		SetNPCFrame(e\room\npc[0], 19)
		e\room\npc[0]\state=8

		RemoveEvent(e)
	EndIf
	;[End Block]
End Function



Function UpdateEvent_hll_ele_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState = 0) Then
		If (e\room\dist < 8.0 And e\room\dist > 0) Then
			e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\obj,True), 0.5, EntityZ(e\room\obj,True))
			PointEntity(e\room\npc[0]\collider, mainPlayer\collider)
			RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\npc[0]\collider),0, True)

			e\eventState = 1
		EndIf
	Else
		If (e\eventState = 1) Then
			If (e\room\dist<5.0 Or Rand(700)=1) Then
				e\eventState = 2

				e\room\npc[0]\state = 5
				e\room\npc[0]\enemyX = EntityX(e\room\objects[1],True)
				e\room\npc[0]\enemyY = EntityY(e\room\objects[1],True)
				e\room\npc[0]\enemyZ = EntityZ(e\room\objects[1],True)
			EndIf
		ElseIf ((e\eventState = 2)) Then
			If (EntityDistance(e\room\npc[0]\collider,e\room\objects[1])<2.0) Then
				e\room\doors[0]\open = False
				;PlayRangedSound(CloseDoorSFX(0, 0), mainPlayer\cam, e\room\doors[0]\obj, 8.0)

				PlaySound2((LoadTempSound("SFX/Room/Room2ElevatorDeath.ogg")))

				e\eventState = 2.05
			EndIf
		ElseIf ((e\eventState < 13*70)) Then
			e\eventState = e\eventState+timing\tickDuration
			;6.7 - 7.4
			;8.6 - 10
			If (e\eventState > 6.7*70 And e\eventState < 7.4*70) Then
				mainPlayer\camShake = 7.4-(e\eventState/70.0)
			ElseIf ((e\eventState > 8.6*70 And e\eventState < 10.6*70)) Then
				mainPlayer\camShake = 10.6-(e\eventState/70.0)
			ElseIf ((e\eventState > 12.6*70)) Then
				mainPlayer\camShake = 0
				If (e\eventState-timing\tickDuration < 12.6*70 And e\room\npc[0]<>Null) Then
					RemoveNPC(e\room\npc[0])
					e\room\npc[0]=Null

					de = CreateDecal(DECAL_BLOOD_SPLATTER, EntityX(e\room\objects[0],True), 0.0005, EntityZ(e\room\objects[0],True),90,Rnd(360),0)

					de = CreateDecal(DECAL_BLOOD_POOL, EntityX(e\room\objects[0],True), 0.002, EntityZ(e\room\objects[0],True),90,Rnd(360),0)
					de\size = 0.5

					de = CreateDecal(DECAL_BLOOD_SPLATTER, EntityX(e\room\objects[1],True), EntityY(e\room\objects[1],True), EntityZ(e\room\objects[1],True),0,e\room\angle+270,0)
					de\size = 0.9
				EndIf
				e\room\doors[0]\locked = False
			EndIf
		Else
			If (e\room\doors[0]\open) Then
				e\room\doors[0]\locked = True : RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
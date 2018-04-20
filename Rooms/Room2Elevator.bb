Function FillRoom2Elevator(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x+888.0*RoomScale, 240.0*RoomScale, r\z, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x+1024.0*RoomScale-0.01, 120.0*RoomScale, r\z, True)
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 448.0 * RoomScale, 0.0, r\z, 90, r)
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 416.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z - 208.0 * RoomScale,True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 480.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z + 184.0 * RoomScale,True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True : r\RoomDoors[0]\locked = True
End Function

Function UpdateEventRoom2elevator2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\room\dist < 8.0 And e\room\dist > 0 Then
		
		de.Decals = CreateDecal(3, EntityX(e\room\Objects[0],True), 0.0005, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
		
			de.Decals = CreateDecal(17, EntityX(e\room\Objects[0],True), 0.002, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
			de\size = 0.5
		
		e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\Objects[0],True), 0.5, EntityZ(e\room\Objects[0],True))
		e\room\NPC[0]\texture = "GFX\NPCs\gonzales.jpg"
		tex = LoadTexture_Strict(e\room\NPC[0]\texture)
		EntityTexture(e\room\NPC[0]\obj, tex)
		FreeTexture tex
		
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)-80,0, True	
		
		SetNPCFrame e\room\NPC[0], 19
		e\room\NPC[0]\State=8
		
		RemoveEvent(e)
	EndIf
	;[End Block]
End Function



Function UpdateEventRoom2elevator(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\EventState = 0 Then
		If e\room\dist < 8.0 And e\room\dist > 0 Then
			e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\obj,True), 0.5, EntityZ(e\room\obj,True))
			PointEntity e\room\NPC[0]\Collider, mainPlayer\collider
			RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider),0, True	
			
			e\EventState = 1
		EndIf
	Else
		If e\EventState = 1 Then
			If e\room\dist<5.0 Or Rand(700)=1 Then 
				e\EventState = 2
				
				e\room\NPC[0]\State = 5
				e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[1],True)
				e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[1],True)
				e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[1],True)
			EndIf
		ElseIf e\EventState = 2
			If EntityDistance(e\room\NPC[0]\Collider,e\room\Objects[1])<2.0 Then
				e\room\RoomDoors[0]\open = False
				PlaySound2(CloseDoorSFX(0, 0), mainPlayer\cam, e\room\RoomDoors[0]\obj, 8.0)			
				
				PlaySound_Strict (LoadTempSound("SFX\Room\Room2ElevatorDeath.ogg"))
				
				e\EventState = 2.05
			EndIf
		ElseIf e\EventState < 13*70
			e\EventState = e\EventState+timing\tickDuration
			;6.7 - 7.4
			;8.6 - 10
			If e\EventState > 6.7*70 And e\EventState < 7.4*70 Then
				mainPlayer\camShake = 7.4-(e\EventState/70.0)
			ElseIf e\EventState > 8.6*70 And e\EventState < 10.6*70 
				mainPlayer\camShake = 10.6-(e\EventState/70.0)
			ElseIf e\EventState > 12.6*70
				mainPlayer\camShake = 0
				If e\EventState-timing\tickDuration < 12.6*70 And e\room\NPC[0]<>Null Then
					RemoveNPC(e\room\NPC[0])
					e\room\NPC[0]=Null
					
					de.Decals = CreateDecal(3, EntityX(e\room\Objects[0],True), 0.0005, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
					
					de.Decals = CreateDecal(17, EntityX(e\room\Objects[0],True), 0.002, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
					de\size = 0.5
					
					de.Decals = CreateDecal(3, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True), EntityZ(e\room\Objects[1],True),0,e\room\angle+270,0)
					de\size = 0.9
				EndIf
				e\room\RoomDoors[0]\locked = False
			EndIf
		Else
			If e\room\RoomDoors[0]\open Then e\room\RoomDoors[0]\locked = True : RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
End Function

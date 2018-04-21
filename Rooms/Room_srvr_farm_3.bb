Function FillRoom_srvr_farm_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    it = CreateItem("9V Battery", "bat", r\x - 132.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    If Rand(2) = 1 Then
        it = CreateItem("9V Battery", "bat", r\x - 76.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf
    If Rand(2) = 1 Then
        it = CreateItem("9V Battery", "bat", r\x - 196.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
        EntityParent(it\collider, r\obj)
    EndIf
    
    it = CreateItem("S-NAV 300 Navigator", "nav", r\x + 124.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
    it\state = 20 : EntityParent(it\collider, r\obj)
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z - 400.0 * RoomScale, True)
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x - 552.0 * RoomScale, -512.0 * RoomScale, r\z - 528.0 * RoomScale, True)			
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z + 272.0 * RoomScale, True)
    
    r\Objects[3] = LoadMesh_Strict("GFX\npcs\duck_low_res.b3d")
    ScaleEntity(r\Objects[3], 0.07, 0.07, 0.07)
    tex = LoadTexture_Strict("GFX\npcs\duck2.png")
    EntityTexture r\Objects[3], tex
    PositionEntity (r\Objects[3], r\x + 928.0 * RoomScale, -640*RoomScale, r\z + 704.0 * RoomScale)
    
    EntityParent r\Objects[3], r\obj
End Function


Function UpdateEvent_srvr_farm_3(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If e\EventState3=0 And Curr173\Idle = 0 Then
			If mainPlayer\blinkTimer < -10 Then 
				temp = Rand(0,2)
				PositionEntity Curr173\Collider, EntityX(e\room\Objects[temp],True),EntityY(e\room\Objects[temp],True),EntityZ(e\room\Objects[temp],True)
				ResetEntity Curr173\Collider
				e\EventState3=1
			EndIf
		EndIf
		
		If e\room\Objects[3]>0 Then 
			If mainPlayer\blinkTimer<-8 And mainPlayer\blinkTimer >-12 Then
				PointEntity e\room\Objects[3], mainPlayer\cam
				RotateEntity(e\room\Objects[3], 0, EntityYaw(e\room\Objects[3],True),0, True)
			EndIf
			If e\EventState2 = 0 Then 
				e\EventState = CurveValue(0, e\EventState, 15.0)
				If Rand(800)=1 Then e\EventState2 = 1
			Else
				e\EventState = e\EventState+(timing\tickDuration*0.5)
				If e\EventState > 360 Then e\EventState = 0	
				
				If Rand(1200)=1 Then e\EventState2 = 0
			EndIf
			
			PositionEntity e\room\Objects[3], EntityX(e\room\Objects[3],True), (-608.0*RoomScale)+0.05+Sin(e\EventState+270)*0.05, EntityZ(e\room\Objects[3],True), True
		EndIf
	EndIf
	;[End Block]
End Function


 Function FillRoom_test_682_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    For xtemp = 0 To 1
        For ztemp = -1 To 1
            r\Objects[xtemp * 3 + (ztemp + 1)] = CreatePivot()
            PositionEntity(r\Objects[xtemp * 3 + (ztemp + 1)], r\x + (-236.0 + 280.0 * xtemp) * RoomScale, -700.0 * RoomScale, r\z + 384.0 * ztemp * RoomScale)
            EntityParent(r\Objects[xtemp * 3 + (ztemp + 1)], r\obj)
        Next
    Next
    
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6], r\x + 754.0 * RoomScale, r\y - 1248.0 * RoomScale, r\z)
    EntityParent(r\Objects[6], r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x + 744.0 * RoomScale, r\y - 856.0 * RoomScale, r\z + 236.0 * RoomScale, r)
    sc\FollowPlayer = True
    
    CreateDoor(0, r\x + 720.0 * RoomScale, 0, r\z, 0, r, False, 2, -1)
    
    CreateDoor(0, r\x - 624.0 * RoomScale, -1280.0 * RoomScale, r\z, 90, r, True)			
    
    it = CreateItem("Document SCP-682", "paper", r\x + 656.0 * RoomScale, r\y - 1200.0 * RoomScale, r\z - 16.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_test_682_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e <> Null Then
		If mainPlayer\currRoom = e\room Then
			If e\EventState = 0 Then
				e\room\Objects[7]=LoadAnimMesh("GFX/npcs/scp-1048.b3d")
				ScaleEntity e\room\Objects[7], 0.05,0.05,0.05
				
				TFormPoint EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,e\room\obj
				If TFormedZ()=0 Then temp = -1 Else temp = -Sgn(TFormedZ())
				TFormPoint -720,0,816*temp,e\room\obj,0
				PositionEntity(e\room\Objects[7],TFormedX(),0,TFormedZ())
				
				RotateEntity e\room\Objects[7],-90,e\room\angle-90,0
				SetAnimTime e\room\Objects[7],297
				e\EventState = 1
			EndIf
			If EntityDistance(mainPlayer\collider, e\room\Objects[6]) < 2.5 And e\EventState > 0 Then
				PlaySound2(LoadTempSound("SFX/SCP/079/TestroomWarning.ogg"))
				For i = 0 To 5
					em.Emitters = CreateEmitter(EntityX(e\room\Objects[i], True), EntityY(e\room\Objects[i], True), EntityZ(e\room\Objects[i], True), 0)
					TurnEntity(em\Obj, 90, 0, 0, True)
					;entityParent(em\obj, e\room\obj)
					em\RandAngle = 5
					em\Speed = 0.042
					em\SizeChange = 0.0025									
				Next
				;Delete e
				e\EventState = e\EventState * -1
			EndIf
			If e\room\Objects[7]<>0 Then
				Animate2(e\room\Objects[7],AnimTime(e\room\Objects[7]),284,308,0.3)
				MoveEntity e\room\Objects[7],0,-0.008*timing\tickDuration,0
				TFormPoint EntityX(e\room\Objects[7]),EntityY(e\room\Objects[7]),EntityZ(e\room\Objects[7]),0,e\room\obj
				
				If Abs(TFormedX())>725 Then
					FreeEntity(e\room\Objects[7])
					e\room\Objects[7]=0
					e\EventState = e\EventState *2
				EndIf
			EndIf
			If e\EventState = -2 Then RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
End Function


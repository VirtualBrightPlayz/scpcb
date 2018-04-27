Function FillRoom_strg_elec_2c(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    d = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, 2)
    d\AutoClose = False : d\open = False
    
    it = CreateItem("Note from Daniel", "paper", r\x-400.0*RoomScale,1040.0*RoomScale,r\z+115.0*RoomScale)
    EntityParent(it\collider, r\obj)
    
    For n% = 0 To 2
        r\Objects[n * 2] = CopyEntity(LeverBaseOBJ)
        r\Objects[n * 2 + 1] = CopyEntity(LeverOBJ)
        
        r\Levers[n] = r\Objects[n * 2 + 1]
        
        For  i% = 0 To 1
            ScaleEntity(r\Objects[n * 2 + i], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[n * 2 + i], r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 64.0 * n) * RoomScale, True)
            
            EntityParent(r\Objects[n * 2 + i], r\obj)
        Next
        RotateEntity(r\Objects[n * 2], 0, -90, 0)
        RotateEntity(r\Objects[n * 2 + 1], 10, -90 - 180, 0)
        
        EntityPickMode r\Objects[n * 2 + 1], 1, False
        EntityRadius r\Objects[n * 2 + 1], 0.1
    Next
    
    sc.SecurityCams = CreateSecurityCam(r\x-265.0*RoomScale, r\y+1280.0*RoomScale, r\z+105.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 45
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 10
End Function


Function UpdateEvent_strg_elec_2c(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		
		EntityPick(mainPlayer\cam, 1.5)
		
		For i = 1 To 5 Step 2
			If PickedEntity() = e\room\Objects[i] Then
				DrawHandIcon = True
				If MouseHit1 Then mainPlayer\grabbedEntity = e\room\Objects[i]
				
				If e\EventState = 0 Then 
					If i = 3 Then 
						e\EventState = Max(e\EventState,1)
						;PlaySound HorrorSFX(7)
						PlaySound LeverSFX
					EndIf
				EndIf 
			End If
			
			Local prevpitch# = EntityPitch(e\room\Objects[i])
			
			If MouseDown1 Or MouseHit1 Then
				If mainPlayer\grabbedEntity <> 0 Then
					If mainPlayer\grabbedEntity = e\room\Objects[i] Then
						DrawHandIcon = True
						TurnEntity(e\room\Objects[i], mouse_y_speed_1 * 2.5, 0, 0)
						RotateEntity(mainPlayer\grabbedEntity, Max(Min(EntityPitch(e\room\Objects[i]), 85), -85), EntityYaw(e\room\Objects[i]), 0)
						
						DrawArrowIcon(0) = True
						DrawArrowIcon(2) = True
						
					EndIf
				EndIf
			Else
				If EntityPitch(e\room\Objects[i]) > 0 Then
					RotateEntity(e\room\Objects[i], CurveValue(85, EntityPitch(e\room\Objects[i]), 10), EntityYaw(e\room\Objects[i]), 0)
				Else
					RotateEntity(e\room\Objects[i], CurveValue(-85, EntityPitch(e\room\Objects[i]), 10), EntityYaw(e\room\Objects[i]), 0)
				EndIf
				mainPlayer\grabbedEntity = 0
			End If
			
			If EntityPitch(e\room\Objects[i]) > 83 Then
				If prevpitch =< 83 Then PlaySound2(LeverSFX, mainPlayer\cam, e\room\Objects[i])
				If i = 3 Then 
					SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
					If prevpitch =< 83 Then
						PlaySound2(LightSFX, mainPlayer\cam, e\room\Objects[i])
					EndIf		
				Else
					RemoteDoorOn = True
				EndIf
			ElseIf EntityPitch(e\room\Objects[i]) < -83
				
				If prevpitch => -83 Then PlaySound2(LeverSFX, mainPlayer\cam, e\room\Objects[i])
				If i = 3 Then 
					If prevpitch => -83 Then
						PlaySound2(LightSFX, mainPlayer\cam, e\room\Objects[i])
						For r.Rooms = Each Rooms
							For z = 0 To 19
								If r\LightSprites[z] <> 0 Then HideEntity r\LightSprites[z]
							Next 
						Next 
					EndIf 
					SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
				Else
					RemoteDoorOn = False
				EndIf						
			EndIf
		Next
		
		If e\EventState > 0 And e\EventState < 200 Then
			e\EventState = e\EventState + timing\tickDuration
			RotateEntity(e\room\Objects[3], CurveValue(-85, EntityPitch(e\room\Objects[3]), 5), EntityYaw(e\room\Objects[3]), 0)
		EndIf 
		
	EndIf
	;[End Block]
End Function


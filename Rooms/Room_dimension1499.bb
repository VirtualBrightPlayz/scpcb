Function UpdateEvent_dimension1499(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\EventState: If player entered dimension (will be resetted after the player leaves it)
		;0: The player never entered SCP-1499
		;1: The player had already entered the dimension at least once
		;2: The player is in dimension
	If mainPlayer\currRoom = e\room Then
		If e\EventState < 2.0
			;1499 random generator
			;[Block]
			;TODO: Reimplement.
;			If e\EventState = 0.0
;				If e\EventStr = ""
;					e\EventStr = "load0"
;				ElseIf e\EventStr = "load0"
;					e\room\Objects[0] = CreatePlane()
;					Local planetex% = LoadTexture("GFX\map\dimension1499\grit3.jpg")
;					EntityTexture e\room\Objects[0],planetex%
;					FreeTexture planetex%
;					PositionEntity e\room\Objects[0],0,EntityY(e\room\obj),0
;					EntityType e\room\Objects[0],HIT_MAP
;					;EntityParent e\room\Objects[0],e\room\obj
;					e\EventStr = "load1"
;				ElseIf e\EventStr = "load1"
;					NTF_1499Sky = sky_CreateSky("GFX\map\sky\1499sky")
;					e\EventStr = 1
;				Else
;					If Int(e\EventStr)<16
;						e\room\Objects[Int(e\EventStr)] = LoadMesh("GFX\map\dimension1499\1499object"+(Int(e\EventStr))+".b3d")
;						HideEntity e\room\Objects[Int(e\EventStr)]
;						e\EventStr = Int(e\EventStr)+1
;					ElseIf Int(e\EventStr)=16
;						CreateChunkParts(e\room)
;						e\EventStr = 17
;					ElseIf Int(e\EventStr) = 17
;						x# = EntityX(e\room\obj)
;						z# = EntityZ(e\room\obj)
;						Local ch.Chunk
;						For i = -2 To 2 Step 2
;							ch = CreateChunk(-1,x#*(i*2.5),EntityY(e\room\obj),z#)
;						Next
;						;If Music(18)=0 Then Music(18) = LoadSound("SFX\Music\1499.ogg")
;						e\EventState = 2.0
;						e\EventStr = 18
;					EndIf
;				EndIf
;			Else
;				e\EventState = 2.0
;			EndIf
			;[End Block]
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtype1499
					n\Idle = False
					n\State = 0
					;If Rand(2)=1 Then n\State2 = 500*3
					n\Angle = Rnd(360)
					PositionEntity n\Collider,EntityX(n\Collider)+Rnd(-60.0,60.0),EntityY(n\Collider)+0.05,EntityZ(n\Collider)+Rnd(-60.0,60.0)
					ResetEntity n\Collider
				EndIf
			Next
		EndIf
		;PositionEntity e\room\Objects[0],0,800,0
		CameraFogRange mainPlayer\cam,40,80
		CameraFogColor mainPlayer\cam,96,97,104
		CameraClsColor mainPlayer\cam,96,97,104
		CameraRange mainPlayer\cam,0.05,90
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next
		ShowEntity e\room\obj
		
		UpdateChunks(e\room,15)
		ShowEntity NTF_1499Sky
		Update1499Sky()
		ShouldPlay = 18
		If EntityY(mainPlayer\collider)<800.0 Then PositionEntity mainPlayer\collider,EntityX(mainPlayer\collider),800.1,EntityZ(mainPlayer\collider),True
		ResetEntity mainPlayer\collider
			
		mainPlayer\footstepOverride=3
	Else
		If e\EventState = 2.0
			HideEntity NTF_1499Sky
			HideChunks()
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtype1499
					;RemoveNPC(n)
					n\Idle = True
				EndIf
			Next
			e\EventState = 1.0
		EndIf
	EndIf
	;[End Block]
End Function

Function UpdateLeave1499()
	Local r.Rooms, it.Items
	
	If (Not (IsPlayerWearingTempName(mainPlayer,"scp1499") Or IsPlayerWearingTempName(mainPlayer,"super1499"))) And mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499"
		For r.Rooms = Each Rooms
			If r = NTF_1499PrevRoom
				mainPlayer\blinkTimer = -1
				;Msg = "You removed the gas mask and reappeared inside the facility."
				;MsgTimer = 70 * 5
				NTF_1499X# = EntityX(mainPlayer\collider)
				NTF_1499Y# = EntityY(mainPlayer\collider)
				NTF_1499Z# = EntityZ(mainPlayer\collider)
				PositionEntity (mainPlayer\collider, NTF_1499PrevX#, NTF_1499PrevY#+0.05, NTF_1499PrevZ#)
				ResetEntity(mainPlayer\collider)
				UpdateDoors()
				UpdateRooms()
				For it.Items = Each Items
					it\disttimer = 0
					If it\itemtemplate\tempname = "scp1499" Or it\itemtemplate\tempname = "super1499"
						If EntityY(it\collider) >= EntityY(mainPlayer\currRoom\obj)-5
							PositionEntity it\collider,NTF_1499PrevX#,NTF_1499PrevY#+(EntityY(it\collider)-EntityY(mainPlayer\currRoom\obj)),NTF_1499PrevZ#
							ResetEntity it\collider
						EndIf
					EndIf
				Next
				mainPlayer\currRoom = r
				PlaySound(LoadTempSound(LoadSound("SFX\SCP\1499\Exit.ogg")))
				NTF_1499PrevX# = 0.0
				NTF_1499PrevY# = 0.0
				NTF_1499PrevZ# = 0.0
				NTF_1499PrevRoom = Null
				;Brightness = StoredBrightness
				Exit
			EndIf
		Next
	EndIf
	
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
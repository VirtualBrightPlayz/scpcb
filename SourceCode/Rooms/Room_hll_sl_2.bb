Function FillRoom_hll_sl_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    Local scale# = RoomScale * 4.5 * 0.4
    
    ;Monitor Objects
    For i = 0 To 14
        If i <> 7
            r\Objects[i] = CopyEntity(Monitor)
            ScaleEntity(r\Objects[i], scale, scale, scale)
        EndIf
    Next
    For i = 0 To 2
        PositionEntity r\Objects[i],r\x-207.94*RoomScale,r\y+(648.0+(112*i))*RoomScale,r\z-60.0686*RoomScale
        RotateEntity r\Objects[i],0,105+r\angle,0
        EntityParent r\Objects[i],r\obj
        DebugLog i
    Next
    For i = 3 To 5
        PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-3)))*RoomScale,r\z+95.7443*RoomScale
        RotateEntity r\Objects[i],0,90+r\angle,0
        EntityParent r\Objects[i],r\obj
        DebugLog i
    Next
    For i = 6 To 8 Step 2
        PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-6)))*RoomScale,r\z+255.744*RoomScale
        RotateEntity r\Objects[i],0,90+r\angle,0
        EntityParent r\Objects[i],r\obj
        DebugLog i
    Next
    For i = 9 To 11
        PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-9)))*RoomScale,r\z+415.744*RoomScale
        RotateEntity r\Objects[i],0,90+r\angle,0
        EntityParent r\Objects[i],r\obj
        DebugLog i
    Next
    For i = 12 To 14
        PositionEntity r\Objects[i],r\x-208.138*RoomScale,r\y+(648.0+(112*(i-12)))*RoomScale,r\z+571.583*RoomScale
        RotateEntity r\Objects[i],0,75+r\angle,0
        EntityParent r\Objects[i],r\obj
        DebugLog i
    Next
    
    ;Doors for room
    r\RoomDoors[0] = CreateDoor(r\zone,r\x+480.0*RoomScale,r\y,r\z-640.0*RoomScale,90,r,False,False,3)
    r\RoomDoors[0]\AutoClose = False
    PositionEntity r\RoomDoors[0]\buttons[0],r\x+576.0*RoomScale,EntityY(r\RoomDoors[0]\buttons[0],True),r\z-480*RoomScale,True
    RotateEntity r\RoomDoors[0]\buttons[0],0,270,0
    r\RoomDoors[1] = CreateDoor(r\zone,r\x+544.0*RoomScale,r\y+480.0*RoomScale,r\z+256.0*RoomScale,270,r,False,False,3)
    r\RoomDoors[1]\AutoClose = False
    FreeEntity r\RoomDoors[1]\obj2 : r\RoomDoors[1]\obj2 = 0
    d = CreateDoor(r\zone,r\x+1504.0*RoomScale,r\y+480.0*RoomScale,r\z+960.0*RoomScale,0,r)
    d\AutoClose = False : d\locked = True
    
    ;PathPoint 1 for SCP-049
    r\Objects[7] = CreatePivot()
    PositionEntity r\Objects[7],r\x,r\y+100.0*RoomScale,r\z-800.0*RoomScale,True
    EntityParent r\Objects[7],r\obj
    
    ;PathPoints for SCP-049
    r\Objects[15] = CreatePivot()
    PositionEntity r\Objects[15],r\x+700.0*RoomScale,r\y+700.0*RoomScale,r\z+256.0*RoomScale,True
    EntityParent r\Objects[15],r\obj
    r\Objects[16] = CreatePivot()
    PositionEntity r\Objects[16],r\x-100.0*RoomScale,r\y+700.0*RoomScale,r\z+256.0*RoomScale,True
    EntityParent r\Objects[16],r\obj
    
    ;Faked room409
    r\Objects[17] = LoadMesh("GFX/map/room2sl_2.b3d",r\obj)
    sc.SecurityCams = CreateSecurityCam(r\x-160.0*RoomScale,r\y-22689.1*RoomScale,r\z-288.0*RoomScale,Null)
    sc\angle = 225
    TurnEntity sc\CameraObj, 20, 0, 0
    EntityParent sc\obj,r\obj
    sc\SpecialCam = True
    
    ;-49.0 689.0 912.0
    ;Objects [18],[19]
    r\Objects[9 * 2] = CopyEntity(LeverBaseOBJ)
    r\Objects[9 * 2 + 1] = CopyEntity(LeverOBJ)
    
    r\Levers[0] = r\Objects[9 * 2 + 1]
    
    For  i% = 0 To 1
        ScaleEntity(r\Objects[9 * 2 + i], 0.04, 0.04, 0.04)
        PositionEntity r\Objects[9 * 2 + i],r\x-49*RoomScale,r\y+689*RoomScale,r\z+912*RoomScale,True
        
        EntityParent(r\Objects[9 * 2 + i], r\obj)
    Next
    RotateEntity(r\Objects[9 * 2], 0, 0, 0)
    RotateEntity(r\Objects[9 * 2 + 1], 10, 0 - 180, 0)
        
    EntityPickMode r\Objects[9 * 2 + 1], 1, False
    EntityRadius r\Objects[9 * 2 + 1], 0.1
    
    ;Camera in the room itself
    sc.SecurityCams = CreateSecurityCam(r\x-159.0*RoomScale, r\y+384.0*RoomScale, r\z-929.0*RoomScale, r, True)
    sc\angle = 315
    ;sc\turn = 45
    sc\room = r
    TurnEntity(sc\CameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    PositionEntity(sc\ScrObj, r\x-231.489*RoomScale, r\y+760.0*RoomScale, r\z+255.744*RoomScale)
    TurnEntity(sc\ScrObj, 0, 90, 0)
    EntityParent(sc\ScrObj, r\obj)
    
    r\Objects[20] = CopyEntity(LeverBaseOBJ)
    r\Objects[21] = CopyEntity(LeverOBJ)
    
    For  i% = 0 To 1
        ScaleEntity(r\Objects[20 + i], 0.04, 0.04, 0.04)
        PositionEntity r\Objects[20],r\x+82.0*RoomScale, r\y+689.0*RoomScale, r\z+912.0*RoomScale,True
        PositionEntity r\Objects[21],r\x+90.9775*RoomScale, r\y+604.347*RoomScale, r\z+890.584*RoomScale,True
        
        EntityParent(r\Objects[20 + i], r\obj)
    Next
    
    RotateEntity r\Objects[21],8.6,-150.0,-5.0
    
    ;For tw.TempWayPoints = Each TempWayPoints
    ;	If tw\roomtemplate = r\RoomTemplate
    ;		If tw\y > 480.0*RoomScale
    ;			tw\y = 540.0*RoomScale
    ;		EndIf
    ;	EndIf
    ;Next
    
    ;w.waypoints = CreateWaypoint(r\x, r\y + 64.0 * RoomScale, r\z - 640.0 * RoomScale, Null, r)
    ;w2.waypoints = CreateWaypoint(r\x + 1024.0*RoomScale, r\y + 320.0 * RoomScale, r\z - 640.0 * RoomScale, Null, r)
    ;w3.waypoints = CreateWaypoint(r\x + 1552.0*RoomScale, r\y + 540.0 * RoomScale, r\z - 636.0*RoomScale, Null, r)
    ;w3\connected[0] = w2 : w3\dist[0] = EntityDistance(w3\obj, w2\obj)
    ;w2\connected[1] = w3 : w2\dist[1] = w3\dist[0]
    
    ;r\MaxWayPointY# = 400.0*RoomScale
    
    ;r\Objects[22] = LoadMesh("GFX/map/room2sl_hb.b3d",r\obj)
	;ScaleEntity r\Objects[22],RoomScale,RoomScale,RoomScale
    ;EntityPickMode r\Objects[22],2
    ;EntityType r\Objects[22],HIT_MAP
    ;EntityAlpha r\Objects[22],1.0
End Function


Function UpdateEvent_hll_sl_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\EventState: Determines if the player already entered the room or not (0 = No, 1 = Yes)
	;e\EventState2: Variable used for the SCP-049 event
	;e\EventState3: Checks if Lever is activated or not
	
	;mainPlayer\cam-Spawning Code + SCP-049-Spawning (it is a little messy!) ;TODO: reimplement
	;[Block]
	If mainPlayer\currRoom = e\room
	EndIf
	If e\EventState = 0
		For r.Rooms = Each Rooms
			If ValidRoom2slCamRoom(r)
				For sc.SecurityCams = Each SecurityCams
					If sc\room = r And (Not sc\SpecialCam)
						Local HasCamera% = False
						For sc2.SecurityCams = Each SecurityCams
							If sc2\room <> sc\room And (Not sc2\SpecialCam)
								If sc2\room\RoomTemplate\name = sc\room\RoomTemplate\name
									If sc2\Screen
										HasCamera% = True
										DebugLog "HasCamera% = True ("+Chr(34)+sc2\room\RoomTemplate\Name+Chr(34)+")"
										Exit
									EndIf
								EndIf
							EndIf
						Next
						If (Not HasCamera%) Then
							If (Not e\loaded) Then
								For i = 0 To 9
									sc\Screen = True
									sc\AllowSaving = False
									
									sc\RenderInterval = 12
									
									scale# = RoomScale * 4.5 * 0.4
									
									sc\ScrObj = CreateSprite()
									EntityFX sc\ScrObj, 17
									SpriteViewMode(sc\ScrObj, 2)
									sc\ScrTexture = 0
										;EntityTexture sc\ScrObj, ScreenTexs[sc\ScrTexture]
									ScaleSprite(sc\ScrObj, MeshWidth(Monitor) * scale * 0.95* 0.5, MeshHeight(Monitor) * scale * 0.95* 0.5)
									
									sc\ScrOverlay = CreateSprite(sc\ScrObj)
									
									ScaleSprite(sc\ScrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
									MoveEntity(sc\ScrOverlay, 0, 0, -0.0005)
									EntityTexture(sc\ScrOverlay, MonitorTexture)
									SpriteViewMode(sc\ScrOverlay, 2)
									EntityBlend(sc\ScrOverlay , 3)
									
									sc\MonitorObj = CopyEntity(Monitor, sc\ScrObj)
									
									ScaleEntity(sc\MonitorObj, scale, scale, scale)
									
									sc\Cam = CreateCamera()
									CameraViewport(sc\Cam, 0, 0, 512, 512)
									CameraRange sc\Cam, 0.05, 6.0
									CameraZoom(sc\Cam, 0.8)
									HideEntity(sc\Cam)
									
									sc\IsRoom2slCam = True
									sc\Room2slTexs%[0] = CreateTexture(512, 512, 1+256)
									EntityTexture sc\ScrObj, sc\Room2slTexs%[0]
									
									pvt% = CreatePivot(e\room\obj)
									Select r\RoomTemplate\Name$
										Case "room2closets" ;ID=0 q
											PositionEntity pvt%,-207.94,872.0,-60.0686,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room2closets"+Chr(34)
										Case "room1archive" ;ID=1 q
											PositionEntity pvt%,-231.489,872.0,95.7443,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1archive"+Chr(34)
										Case "room3z3" ;ID=2 q
											PositionEntity pvt%,-231.489,872.0,255.744,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room3z3"+Chr(34)
										Case "room1lifts" ;ID=3 q
											PositionEntity pvt%,-231.489,872.0,415.744,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1lifts"+Chr(34)
										Case "room106" ;ID=4 q
											PositionEntity pvt%,-208.138,872.0,571.583,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 75+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room106"+Chr(34)
										Case "checkpoint1" ;ID=5 q
											PositionEntity pvt%,-207.94,760.0,-60.0686,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"checkpoint1"+Chr(34)
										Case "room2nuke" ;ID=6 q
											PositionEntity pvt%,-231.489,760.0,415.744,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room2nuke"+Chr(34)
										Case "008" ;ID=7 q
											PositionEntity pvt%,-208.138,760.0,571.583,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 75+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"008"+Chr(34)
										Case "room1162" ;ID=8 q
											PositionEntity pvt%,-207.94,648.0,-60.0686,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1162"+Chr(34)
										Case "room966" ;ID=9 q
											PositionEntity pvt%,-231.489,648.0,255.744,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room966"+Chr(34)
										Case "room2ccont" ;ID=10 q
											PositionEntity pvt%,-231.489,648.0,415.744,False
											PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\ScrObj, e\room\obj)
											TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room2ccont"+Chr(34)
									End Select
									
									FreeEntity pvt%
								Next
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		Next
		If (Not e\loaded) Then
			For sc.SecurityCams = Each SecurityCams
				If sc\SpecialCam Then
					sc\Screen = True
					sc\AllowSaving = False
					
					sc\RenderInterval = 12
					
					scale# = RoomScale * 4.5 * 0.4
					
					sc\ScrObj = CreateSprite()
					EntityFX sc\ScrObj, 17
					SpriteViewMode(sc\ScrObj, 2)
					sc\ScrTexture = 0
					ScaleSprite(sc\ScrObj, MeshWidth(Monitor) * scale * 0.95* 0.5, MeshHeight(Monitor) * scale * 0.95* 0.5)
					
					sc\ScrOverlay = CreateSprite(sc\ScrObj)
					
					ScaleSprite(sc\ScrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
					MoveEntity(sc\ScrOverlay, 0, 0, -0.0005)
					EntityTexture(sc\ScrOverlay, MonitorTexture)
					SpriteViewMode(sc\ScrOverlay, 2)
					EntityBlend(sc\ScrOverlay , 3)
					
					sc\MonitorObj = CopyEntity(Monitor, sc\ScrObj)
					
					ScaleEntity(sc\MonitorObj, scale, scale, scale)
					
					sc\Cam = CreateCamera()
					CameraViewport(sc\Cam, 0, 0, 512, 512)
					CameraRange sc\Cam, 0.05, 6.0
					CameraZoom(sc\Cam, 0.8)
					HideEntity(sc\Cam)
					
					sc\Room2slTexs%[0] = CreateTexture(512, 512, 1+256)
					EntityTexture sc\ScrObj, sc\Room2slTexs%[0]
					
					pvt% = CreatePivot(e\room\obj)
					
					PositionEntity pvt%,-231.489,648.0,95.7443,False
					PositionEntity(sc\ScrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
					EntityParent(sc\ScrObj, e\room\obj)
					TurnEntity(sc\ScrObj, 0, 90+e\room\angle, 0)
					FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
					DebugLog "Created Monitor for "+Chr(34)+"room2sl"+Chr(34)+" (faked SCP-409 mainPlayer/cam)"
					
					FreeEntity pvt%
					Exit
				EndIf
			Next
			
				;For SCP-049
			If e\room\NPC[0]=Null Then
				For n.NPCs = Each NPCs
					If n\npcType = NPCtype049
						e\room\NPC[0] = n
						Exit
					EndIf
				Next
				
				If e\room\NPC[0]=Null Then
					e\room\NPC[0] = CreateNPC(NPCtype049,EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True)+5,EntityZ(e\room\Objects[7],True))
				EndIf
			EndIf
			
			PositionEntity e\room\NPC[0]\collider,EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True)+5,EntityZ(e\room\Objects[7],True)
			ResetEntity e\room\NPC[0]\collider
			RotateEntity e\room\NPC[0]\collider,0,e\room\angle+180,0
			
			DebugLog(EntityX(e\room\Objects[7],True)+", "+EntityY(e\room\Objects[7],True)+", "+EntityZ(e\room\Objects[7],True))
			
			e\room\NPC[0]\state = 0
			e\room\NPC[0]\prevState = 2
			
			e\loaded = True
			e\EventState = 1
			If e\EventState2 = 0 Then e\EventState2 = -(70*5)
		EndIf
	EndIf
	;[End Block]
	
	;SCP-049
	;[Block]
	If e\EventState = 1
		If e\EventState2 < 0
			If e\EventState2 = -(70*5)
				For sc.SecurityCams = Each SecurityCams
					If sc\room = e\room
						If EntityDistance(sc\ScrObj,mainPlayer\cam)<5.0
							If EntityVisible(sc\ScrObj,mainPlayer\cam)
								e\EventState2 = Min(e\EventState2+timing\tickDuration,0)
								Exit
							EndIf
						EndIf
					EndIf
				Next
			Else
				e\EventState2 = Min(e\EventState2+timing\tickDuration,0)
			EndIf
		ElseIf e\EventState2 = 0
			If e\room\NPC[0] <> Null
				;PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\NPC[0]\Collider),EntityY(e\room\Objects[7],True),EntityZ(e\room\NPC[0]\Collider)
				Local AdjDist1# = 0.0
				Local AdjDist2# = 0.0
				Local Adj1% = -1
				Local Adj2% = -1
				For i = 0 To 3
					If e\room\AdjDoor[i]<>Null
						If Adj1 = -1
							AdjDist1# = EntityDistance(e\room\Objects[7],e\room\AdjDoor[i]\frameobj)
							Adj1 = i
						Else
							AdjDist2# = EntityDistance(e\room\Objects[7],e\room\AdjDoor[i]\frameobj)
							Adj2 = i
						EndIf
					EndIf
				Next
				If AdjDist1# > AdjDist2#
					PositionEntity e\room\NPC[0]\collider,EntityX(e\room\AdjDoor[Adj1]\frameobj),EntityY(e\room\Objects[7],True),EntityZ(e\room\AdjDoor[Adj1]\frameobj)
				Else
					PositionEntity e\room\NPC[0]\collider,EntityX(e\room\AdjDoor[Adj2]\frameobj),EntityY(e\room\Objects[7],True),EntityZ(e\room\AdjDoor[Adj2]\frameobj)
				EndIf
				PointEntity e\room\NPC[0]\collider,e\room\obj
				MoveEntity e\room\NPC[0]\collider,0,0,-1
				ResetEntity e\room\NPC[0]\collider
				e\room\NPC[0]\pathX = EntityX(e\room\NPC[0]\collider)
				e\room\NPC[0]\pathZ = EntityZ(e\room\NPC[0]\collider)
				e\room\NPC[0]\state = 5
				DebugLog "aaaaaaaaa"
				e\EventState2 = 1
			EndIf
		ElseIf e\EventState2 = 1
			If e\room\NPC[0]\pathStatus <> 1
				e\room\NPC[0]\pathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[15],True),EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True))
			Else
				DebugLog "bbbbbbbbb"
				e\EventState2 = 2
			EndIf
		ElseIf e\EventState2 = 2
			If e\room\NPC[0]\pathStatus <> 1
				e\room\NPC[0]\state3 = 1.0
				e\EventState2 = 3
				e\room\NPC[0]\pathTimer# = 0.0
				DebugLog "ccccccccc"
			Else
				If EntityDistance(e\room\NPC[0]\collider,e\room\RoomDoors[0]\frameobj) < 5.0
					e\room\RoomDoors[0]\locked = True
					e\room\RoomDoors[1]\locked = True
					If e\room\NPC[0]\Reload = 0
						PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
						DebugLog "079 - OPEN DOORS IN ROOM2SL"
						e\room\NPC[0]\Reload = 1
					EndIf
					If (Not e\room\RoomDoors[0]\open)
						e\room\RoomDoors[0]\open = True
						sound=Rand(0, 2)
						;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\RoomDoors[0]\obj)
					EndIf
					If (Not e\room\RoomDoors[1]\open)
						e\room\RoomDoors[1]\open = True
						sound=Rand(0, 2)
						;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\RoomDoors[1]\obj)
					EndIf
				EndIf
				If e\room\NPC[0]\Reload = 1
					e\room\NPC[0]\dropSpeed = 0
				EndIf
			EndIf
			
			If e\room\NPC[0]\state <> 5
				e\EventState2 = 7
				DebugLog "fffffffff"
			EndIf
		ElseIf e\EventState2 = 3
			If e\room\NPC[0]\state <> 5
				e\EventState2 = 7
				DebugLog "fffffffff"
			EndIf
			
			If MeNPCSeesPlayer(e\room\NPC[0],True)=2
				e\EventState2 = 4
				DebugLog "ddddddddd"
			EndIf
			
			If e\room\NPC[0]\pathStatus <> 1
				;If e\room\NPC[0]\PathTimer# < 70*3
				If e\room\NPC[0]\pathTimer# = 0.0
					;e\room\NPC[0]\PathTimer# = e\room\NPC[0]\PathTimer# + timing\tickDuration
					If e\room\NPC[0]\prevState = 1 Then
						If (e\room\NPC[0]\soundChannels[1] = 0) Then
							e\room\NPC[0]\sounds[1] = LoadSound("SFX/SCP/049/Room2SLEnter.ogg")
							e\room\NPC[0]\soundChannels[1] = PlayRangedSound(e\room\NPC[0]\sounds[1], mainPlayer\cam, e\room\NPC[0]\collider)
						Else
							If (Not IsChannelPlaying(e\room\NPC[0]\soundChannels[1]))
								e\room\NPC[0]\pathTimer# = 1.0
							EndIf
						EndIf
					ElseIf e\room\NPC[0]\prevState = 2
						If e\room\NPC[0]\frame >= 1118
							e\room\NPC[0]\pathTimer# = 1.0
						EndIf
					EndIf
				Else
					Select e\room\NPC[0]\state3
						Case 1
							e\room\NPC[0]\pathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[16],True),EntityY(e\room\Objects[16],True),EntityZ(e\room\Objects[16],True))
							e\room\NPC[0]\prevState = 1
							DebugLog "Path1"
						Case 2
							e\room\NPC[0]\pathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[15],True),EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True))
							e\room\NPC[0]\prevState = 2
							DebugLog "Path2"
						Case 3
							;e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True),EntityZ(e\room\Objects[7],True))
							;e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(e\room\obj,True),EntityY(e\room\Objects[7],True),EntityZ(e\room\obj,True))
							e\room\NPC[0]\pathStatus = FindPath(e\room\NPC[0],e\room\NPC[0]\pathX,0.1,e\room\NPC[0]\pathZ)
							e\room\NPC[0]\prevState = 1
							DebugLog "Path3"
						Case 4
							e\EventState2 = 5
					End Select
					e\room\NPC[0]\pathTimer# = 0.0
					e\room\NPC[0]\state3 = e\room\NPC[0]\state3 + 1
				EndIf
			EndIf
		ElseIf e\EventState2 = 4
			If e\room\NPC[0]\state <> 5
				e\EventState2 = 7
				e\room\NPC[0]\state3 = 5.0
				DebugLog "fffffffff"
			EndIf
		ElseIf e\EventState2 = 5
			;RemoveNPC(e\room\NPC[0])
			DebugLog "ddddddddd"
			e\room\NPC[0]\state = 2
			For r.Rooms = Each Rooms
				If r <> mainPlayer\currRoom
					If (EntityDistance(r\obj,e\room\NPC[0]\collider)<HideDistance*2 And EntityDistance(r\obj,e\room\NPC[0]\collider)>HideDistance)
						e\room\NPC[0]\pathStatus = FindPath(e\room\NPC[0],EntityX(r\obj),EntityY(r\obj),EntityZ(r\obj))
						e\room\NPC[0]\pathTimer = 0.0
						If e\room\NPC[0]\pathStatus = 1 Then e\EventState2 = 6
						Exit
					EndIf
				EndIf
			Next
		ElseIf e\EventState2 = 6
			If MeNPCSeesPlayer(e\room\NPC[0],True) Or e\room\NPC[0]\state2 > 0 Or e\room\NPC[0]\lastSeen > 0
				DebugLog "fffffffff"
				e\EventState2 = 7
			Else
				;Still playing the Music for SCP-049 (in the real, SCP-049's State will be set to 2, causing it to stop playing the chasing track)
				;If Music(20) = 0 Then Music(20) = LoadSound("SFX/Horror/Horror12.ogg") ;TODO: fix
				ShouldPlay = 20
				If e\room\NPC[0]\pathStatus<>1
					e\room\NPC[0]\Idle = 70*60 ;(Making SCP-049 idle for one minute (twice as fast for aggressive NPCs = True))
					PositionEntity e\room\NPC[0]\collider,0,500,0
					ResetEntity e\room\NPC[0]\collider
					DebugLog "eeeeeeeee"
					e\EventState2 = 7
				EndIf
			EndIf
		;ElseIf e\EventState2 = 7
		;	e\room\RoomDoors[0]\locked = False
		;	e\room\RoomDoors[1]\locked = False
		;	e\EventState2 = 8
		EndIf
		
		If e\room\NPC[0]<>Null
			If e\EventState2 < 7
				If e\EventState2 > 2
					If Abs(EntityY(e\room\RoomDoors[0]\frameobj)-EntityY(e\room\NPC[0]\collider))>1.0
						If Abs(EntityY(e\room\RoomDoors[0]\frameobj)-EntityY(mainPlayer\collider))<1.0
							If e\room\RoomDoors[0]\open
								e\room\RoomDoors[0]\open = False
								e\room\RoomDoors[0]\fastopen = 1
								PlaySound2 LoadTempSound("SFX/Door/DoorClose079.ogg")
								DebugLog "079 - CLOSE DOOR AT HALLWAY IN ROOM2SL"
							EndIf
						EndIf
					Else
						If e\room\RoomDoors[0]\open = False
							e\room\RoomDoors[0]\fastopen = 0
							e\room\RoomDoors[0]\open = True
							sound=Rand(0, 2)
							;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\RoomDoors[0]\obj)
							PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
							DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
						EndIf
					EndIf
				EndIf
				
				If e\EventState2 > 0 Then CanSave% = False
			Else
				If e\room\RoomDoors[0]\open = False
					e\room\RoomDoors[0]\fastopen = 0
					e\room\RoomDoors[0]\open = True
					sound=Rand(0, 2)
					;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\RoomDoors[0]\obj)
					PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
					DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
	
	;Lever for checkpoint locking (might have a function in the future for the case if the checkpoint needs to be locked again)
	If mainPlayer\currRoom = e\room
		e\EventState3 = UpdateLever(e\room\Levers[0])
		If e\EventState3 = 1 Then
			;UpdateCheckpointMonitors(0)
		Else
			;TurnCheckpointMonitorsOff(0)
		EndIf
	EndIf
	
	;[End Block]
End Function


Function ValidRoom2slCamRoom(r.Rooms)
	If (r = Null) Then
		Return False
	EndIf
	
	Local RN$ = r\RoomTemplate\Name$
	
	If RN$ = "room2closets" Then Return True
	If RN$ = "room1archive" Then Return True
	If RN$ = "room3z3" Then Return True
	If RN$ = "room1lifts" Then Return True
	If RN$ = "room106" Then Return True
	If RN$ = "checkpoint1" Then Return True
	If RN$ = "room2nuke" Then Return True
	If RN$ = "008" Then Return True
	If RN$ = "room1162" Then Return True
	If RN$ = "room966" Then Return True
	If RN$ = "room2ccont" Then Return True
	
	Return False
	
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D
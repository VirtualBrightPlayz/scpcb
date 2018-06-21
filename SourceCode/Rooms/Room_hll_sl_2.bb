Function FillRoom_hll_sl_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1;, Bump

    Local scale# = RoomScale * 4.5 * 0.4

    ;Monitor Objects
    For i = 0 To 14
        If i <> 7 Then
            r\objects[i] = CopyEntity(Monitor)
            ScaleEntity(r\objects[i], scale, scale, scale)
        EndIf
    Next
	;TODO: cleanu- oh wait, this is room2sl, i already had to rewrite this
    For i = 0 To 2
        PositionEntity r\objects[i],r\x-207.94*RoomScale,r\y+(648.0+(112*i))*RoomScale,r\z-60.0686*RoomScale
        RotateEntity r\objects[i],0,105+r\angle,0
        EntityParent r\objects[i],r\obj
        DebugLog i
    Next
    For i = 3 To 5
        PositionEntity r\objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-3)))*RoomScale,r\z+95.7443*RoomScale
        RotateEntity r\objects[i],0,90+r\angle,0
        EntityParent r\objects[i],r\obj
        DebugLog i
    Next
    For i = 6 To 8 Step 2
        PositionEntity r\objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-6)))*RoomScale,r\z+255.744*RoomScale
        RotateEntity r\objects[i],0,90+r\angle,0
        EntityParent r\objects[i],r\obj
        DebugLog i
    Next
    For i = 9 To 11
        PositionEntity r\objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-9)))*RoomScale,r\z+415.744*RoomScale
        RotateEntity r\objects[i],0,90+r\angle,0
        EntityParent r\objects[i],r\obj
        DebugLog i
    Next
    For i = 12 To 14
        PositionEntity r\objects[i],r\x-208.138*RoomScale,r\y+(648.0+(112*(i-12)))*RoomScale,r\z+571.583*RoomScale
        RotateEntity r\objects[i],0,75+r\angle,0
        EntityParent r\objects[i],r\obj
        DebugLog i
    Next

    ;Doors for room
    r\roomDoors[0] = CreateDoor(r\zone,r\x+480.0*RoomScale,r\y,r\z-640.0*RoomScale,90,r,False,False,3)
    r\roomDoors[0]\autoClose = False
    PositionEntity r\roomDoors[0]\buttons[0],r\x+576.0*RoomScale,EntityY(r\roomDoors[0]\buttons[0],True),r\z-480*RoomScale,True
    RotateEntity r\roomDoors[0]\buttons[0],0,270,0
    r\roomDoors[1] = CreateDoor(r\zone,r\x+544.0*RoomScale,r\y+480.0*RoomScale,r\z+256.0*RoomScale,270,r,False,False,3)
    r\roomDoors[1]\autoClose = False
    FreeEntity r\roomDoors[1]\obj2 : r\roomDoors[1]\obj2 = 0
    d = CreateDoor(r\zone,r\x+1504.0*RoomScale,r\y+480.0*RoomScale,r\z+960.0*RoomScale,0,r)
    d\autoClose = False : d\locked = True

    ;PathPoint 1 for SCP-049
    r\objects[7] = CreatePivot()
    PositionEntity r\objects[7],r\x,r\y+100.0*RoomScale,r\z-800.0*RoomScale,True
    EntityParent r\objects[7],r\obj

    ;PathPoints for SCP-049
    r\objects[15] = CreatePivot()
    PositionEntity r\objects[15],r\x+700.0*RoomScale,r\y+700.0*RoomScale,r\z+256.0*RoomScale,True
    EntityParent r\objects[15],r\obj
    r\objects[16] = CreatePivot()
    PositionEntity r\objects[16],r\x-100.0*RoomScale,r\y+700.0*RoomScale,r\z+256.0*RoomScale,True
    EntityParent r\objects[16],r\obj

    ;Faked room409
    r\objects[17] = LoadMesh("GFX/map/room2sl_2.b3d",r\obj)
    sc.SecurityCams = CreateSecurityCam(r\x-160.0*RoomScale,r\y-22689.1*RoomScale,r\z-288.0*RoomScale,Null)
    sc\angle = 225
    TurnEntity sc\cameraObj, 20, 0, 0
    EntityParent sc\obj,r\obj
    sc\specialCam = True

    ;-49.0 689.0 912.0

    r\levers[0] = CreateLever()

	ScaleEntity(r\levers[0]\baseObj, 0.04, 0.04, 0.04)
	ScaleEntity(r\levers[0]\obj, 0.04, 0.04, 0.04)
	PositionEntity r\levers[0]\baseObj,r\x-49*RoomScale,r\y+689*RoomScale,r\z+912*RoomScale,True
	PositionEntity r\levers[0]\obj,r\x-49*RoomScale,r\y+689*RoomScale,r\z+912*RoomScale,True

	EntityParent(r\levers[0]\baseObj, r\obj)
	EntityParent(r\levers[0]\obj, r\obj)

    RotateEntity(r\levers[0]\baseObj, 0, 0, 0)
    RotateEntity(r\levers[0]\obj, 10, 0 - 180, 0)

    EntityPickMode r\levers[0]\obj, 1, False
    EntityRadius r\levers[0]\obj, 0.1

    ;Camera in the room itself
    sc.SecurityCams = CreateSecurityCam(r\x-159.0*RoomScale, r\y+384.0*RoomScale, r\z-929.0*RoomScale, r, True)
    sc\angle = 315
    ;sc\turn = 45
    sc\room = r
    TurnEntity(sc\cameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)

    PositionEntity(sc\scrObj, r\x-231.489*RoomScale, r\y+760.0*RoomScale, r\z+255.744*RoomScale)
    TurnEntity(sc\scrObj, 0, 90, 0)
    EntityParent(sc\scrObj, r\obj)

	r\levers[1] = CreateLever()

	ScaleEntity(r\levers[1]\baseObj, 0.04, 0.04, 0.04)
	ScaleEntity(r\levers[1]\obj, 0.04, 0.04, 0.04)

	PositionEntity r\levers[1]\baseObj,r\x+82.0*RoomScale, r\y+689.0*RoomScale, r\z+912.0*RoomScale,True
	PositionEntity r\levers[1]\obj,r\x+90.9775*RoomScale, r\y+604.347*RoomScale, r\z+890.584*RoomScale,True

	EntityParent(r\levers[1]\baseObj, r\obj)
	EntityParent(r\levers[1]\obj, r\obj)

    RotateEntity r\levers[1]\obj,8.6,-150.0,-5.0

	;lmfao
	;For  i% = 0 To 1
	;	ScaleEntity(r\objects[20 + i], 0.04, 0.04, 0.04)
	;	PositionEntity r\objects[20],r\x+82.0*RoomScale, r\y+689.0*RoomScale, r\z+912.0*RoomScale,True
	;	PositionEntity r\objects[21],r\x+90.9775*RoomScale, r\y+604.347*RoomScale, r\z+890.584*RoomScale,True

	;	EntityParent(r\objects[20 + i], r\obj)
	;Next

    ;For tw.TempWayPoints = Each TempWayPoints
    ;	If tw\roomtemplate = r\roomTemplate
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

    ;r\maxWayPointY# = 400.0*RoomScale

    ;r\objects[22] = LoadMesh("GFX/map/room2sl_hb.b3d",r\obj)
	;ScaleEntity r\objects[22],RoomScale,RoomScale,RoomScale
    ;EntityPickMode r\objects[22],2
    ;EntityType r\objects[22],HIT_MAP
    ;EntityAlpha r\objects[22],1.0
End Function


Function UpdateEvent_hll_sl_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\eventState: Determines if the player already entered the room or not (0 = No, 1 = Yes)
	;e\eventState2: Variable used for the SCP-049 event
	;e\eventState3: Checks if Lever is activated or not

	;mainPlayer\cam-Spawning Code + SCP-049-Spawning (it is a little messy!) ;TODO: reimplement
	;[Block]
	If mainPlayer\currRoom = e\room Then
	EndIf
	If e\eventState = 0 Then
		For r.Rooms = Each Rooms
			If ValidRoom2slCamRoom(r) Then
				For sc.SecurityCams = Each SecurityCams
					If sc\room = r And (Not sc\specialCam) Then
						Local HasCamera% = False
						For sc2.SecurityCams = Each SecurityCams
							If sc2\room <> sc\room And (Not sc2\specialCam) Then
								If sc2\room\roomTemplate\name = sc\room\roomTemplate\name Then
									If sc2\screen Then
										HasCamera% = True
										DebugLog "HasCamera% = True ("+Chr(34)+sc2\room\roomTemplate\name+Chr(34)+")"
										Exit
									EndIf
								EndIf
							EndIf
						Next
						If (Not HasCamera%) Then
							If (Not e\loaded) Then
								For i = 0 To 9
									sc\screen = True
									sc\allowSaving = False

									sc\renderInterval = 12

									Local scale# = RoomScale * 4.5 * 0.4

									sc\scrObj = CreateSprite()
									EntityFX sc\scrObj, 17
									SpriteViewMode(sc\scrObj, 2)
									sc\scrTexture = 0
									;EntityTexture sc\scrObj, ScreenTexs[sc\scrTexture]
									ScaleSprite(sc\scrObj, MeshWidth(Monitor) * scale * 0.95* 0.5, MeshHeight(Monitor) * scale * 0.95* 0.5)

									sc\scrOverlay = CreateSprite(sc\scrObj)

									ScaleSprite(sc\scrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
									MoveEntity(sc\scrOverlay, 0, 0, -0.0005)
									EntityTexture(sc\scrOverlay, MonitorTexture)
									SpriteViewMode(sc\scrOverlay, 2)
									EntityBlend(sc\scrOverlay , 3)

									sc\monitorObj = CopyEntity(Monitor, sc\scrObj)

									ScaleEntity(sc\monitorObj, scale, scale, scale)

									sc\cam = CreateCamera()
									CameraViewport(sc\cam, 0, 0, 512, 512)
									CameraRange sc\cam, 0.05, 6.0
									CameraZoom(sc\cam, 0.8)
									HideEntity(sc\cam)

									sc\isRoom2slCam = True
									sc\room2slTexs%[0] = CreateTexture(512, 512, 1+256)
									EntityTexture sc\scrObj, sc\room2slTexs%[0]

									pvt% = CreatePivot(e\room\obj)
									Select r\roomTemplate\name$
										Case "room2closets" ;ID=0 q
											PositionEntity pvt%,-207.94,872.0,-60.0686,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room2closets"+Chr(34)
										Case "room1archive" ;ID=1 q
											PositionEntity pvt%,-231.489,872.0,95.7443,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1archive"+Chr(34)
										Case "room3z3" ;ID=2 q
											PositionEntity pvt%,-231.489,872.0,255.744,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room3z3"+Chr(34)
										Case "room1lifts" ;ID=3 q
											PositionEntity pvt%,-231.489,872.0,415.744,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1lifts"+Chr(34)
										Case "room106" ;ID=4 q
											PositionEntity pvt%,-208.138,872.0,571.583,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 75+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room106"+Chr(34)
										Case "checkpoint1" ;ID=5 q
											PositionEntity pvt%,-207.94,760.0,-60.0686,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"checkpoint1"+Chr(34)
										Case "room2nuke" ;ID=6 q
											PositionEntity pvt%,-231.489,760.0,415.744,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room2nuke"+Chr(34)
										Case "008" ;ID=7 q
											PositionEntity pvt%,-208.138,760.0,571.583,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 75+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"008"+Chr(34)
										Case "room1162" ;ID=8 q
											PositionEntity pvt%,-207.94,648.0,-60.0686,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 105+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room1162"+Chr(34)
										Case "room966" ;ID=9 q
											PositionEntity pvt%,-231.489,648.0,255.744,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
											FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
											DebugLog "Created Monitor for "+Chr(34)+"room966"+Chr(34)
										Case "room2ccont" ;ID=10 q
											PositionEntity pvt%,-231.489,648.0,415.744,False
											PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
											EntityParent(sc\scrObj, e\room\obj)
											TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
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
				If sc\specialCam Then
					sc\screen = True
					sc\allowSaving = False

					sc\renderInterval = 12

					scale# = RoomScale * 4.5 * 0.4

					sc\scrObj = CreateSprite()
					EntityFX sc\scrObj, 17
					SpriteViewMode(sc\scrObj, 2)
					sc\scrTexture = 0
					ScaleSprite(sc\scrObj, MeshWidth(Monitor) * scale * 0.95* 0.5, MeshHeight(Monitor) * scale * 0.95* 0.5)

					sc\scrOverlay = CreateSprite(sc\scrObj)

					ScaleSprite(sc\scrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
					MoveEntity(sc\scrOverlay, 0, 0, -0.0005)
					EntityTexture(sc\scrOverlay, MonitorTexture)
					SpriteViewMode(sc\scrOverlay, 2)
					EntityBlend(sc\scrOverlay , 3)

					sc\monitorObj = CopyEntity(Monitor, sc\scrObj)

					ScaleEntity(sc\monitorObj, scale, scale, scale)

					sc\cam = CreateCamera()
					CameraViewport(sc\cam, 0, 0, 512, 512)
					CameraRange sc\cam, 0.05, 6.0
					CameraZoom(sc\cam, 0.8)
					HideEntity(sc\cam)

					sc\room2slTexs%[0] = CreateTexture(512, 512, 1+256)
					EntityTexture sc\scrObj, sc\room2slTexs%[0]

					pvt% = CreatePivot(e\room\obj)

					PositionEntity pvt%,-231.489,648.0,95.7443,False
					PositionEntity(sc\scrObj,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True))
					EntityParent(sc\scrObj, e\room\obj)
					TurnEntity(sc\scrObj, 0, 90+e\room\angle, 0)
					FindAndDeleteFakeMonitor(e\room,EntityX(pvt%,True),EntityY(pvt%,True),EntityZ(pvt%,True),14)
					DebugLog "Created Monitor for "+Chr(34)+"room2sl"+Chr(34)+" (faked SCP-409 mainPlayer/cam)"

					FreeEntity pvt%
					Exit
				EndIf
			Next

				;For SCP-049
			If e\room\npc[0]=Null Then
				For n.NPCs = Each NPCs
					If n\npcType = NPCtype049 Then
						e\room\npc[0] = n
						Exit
					EndIf
				Next

				If e\room\npc[0]=Null Then
					e\room\npc[0] = CreateNPC(NPCtype049,EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True)+5,EntityZ(e\room\objects[7],True))
				EndIf
			EndIf

			PositionEntity e\room\npc[0]\collider,EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True)+5,EntityZ(e\room\objects[7],True)
			ResetEntity e\room\npc[0]\collider
			RotateEntity e\room\npc[0]\collider,0,e\room\angle+180,0

			DebugLog(EntityX(e\room\objects[7],True)+", "+EntityY(e\room\objects[7],True)+", "+EntityZ(e\room\objects[7],True))

			e\room\npc[0]\state = 0
			e\room\npc[0]\prevState = 2

			e\loaded = True
			e\eventState = 1
			If e\eventState2 = 0 Then e\eventState2 = -(70*5)
		EndIf
	EndIf
	;[End Block]

	;SCP-049
	;[Block]
	If e\eventState = 1 Then
		If e\eventState2 < 0 Then
			If e\eventState2 = -(70*5) Then
				For sc.SecurityCams = Each SecurityCams
					If sc\room = e\room Then
						If EntityDistance(sc\scrObj,mainPlayer\cam)<5.0 Then
							If EntityVisible(sc\scrObj,mainPlayer\cam) Then
								e\eventState2 = Min(e\eventState2+timing\tickDuration,0)
								Exit
							EndIf
						EndIf
					EndIf
				Next
			Else
				e\eventState2 = Min(e\eventState2+timing\tickDuration,0)
			EndIf
		ElseIf e\eventState2 = 0 Then
			If e\room\npc[0] <> Null Then
				;PositionEntity e\room\npc[0]\collider,EntityX(e\room\npc[0]\collider),EntityY(e\room\objects[7],True),EntityZ(e\room\npc[0]\collider)
				Local AdjDist1# = 0.0
				Local AdjDist2# = 0.0
				Local Adj1% = -1
				Local Adj2% = -1
				For i = 0 To 3
					If e\room\adjDoor[i]<>Null Then
						If Adj1 = -1 Then
							AdjDist1# = EntityDistance(e\room\objects[7],e\room\adjDoor[i]\frameobj)
							Adj1 = i
						Else
							AdjDist2# = EntityDistance(e\room\objects[7],e\room\adjDoor[i]\frameobj)
							Adj2 = i
						EndIf
					EndIf
				Next
				If AdjDist1# > AdjDist2# Then
					PositionEntity e\room\npc[0]\collider,EntityX(e\room\adjDoor[Adj1]\frameobj),EntityY(e\room\objects[7],True),EntityZ(e\room\adjDoor[Adj1]\frameobj)
				Else
					PositionEntity e\room\npc[0]\collider,EntityX(e\room\adjDoor[Adj2]\frameobj),EntityY(e\room\objects[7],True),EntityZ(e\room\adjDoor[Adj2]\frameobj)
				EndIf
				PointEntity e\room\npc[0]\collider,e\room\obj
				MoveEntity e\room\npc[0]\collider,0,0,-1
				ResetEntity e\room\npc[0]\collider
				e\room\npc[0]\pathX = EntityX(e\room\npc[0]\collider)
				e\room\npc[0]\pathZ = EntityZ(e\room\npc[0]\collider)
				e\room\npc[0]\state = 5
				DebugLog "aaaaaaaaa"
				e\eventState2 = 1
			EndIf
		ElseIf e\eventState2 = 1 Then
			If e\room\npc[0]\pathStatus <> 1 Then
				e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(e\room\objects[15],True),EntityY(e\room\objects[15],True),EntityZ(e\room\objects[15],True))
			Else
				DebugLog "bbbbbbbbb"
				e\eventState2 = 2
			EndIf
		ElseIf e\eventState2 = 2 Then
			If e\room\npc[0]\pathStatus <> 1 Then
				e\room\npc[0]\state3 = 1.0
				e\eventState2 = 3
				e\room\npc[0]\pathTimer# = 0.0
				DebugLog "ccccccccc"
			Else
				If EntityDistance(e\room\npc[0]\collider,e\room\roomDoors[0]\frameobj) < 5.0 Then
					e\room\roomDoors[0]\locked = True
					e\room\roomDoors[1]\locked = True
					If e\room\npc[0]\reload = 0 Then
						PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
						DebugLog "079 - OPEN DOORS IN ROOM2SL"
						e\room\npc[0]\reload = 1
					EndIf
					If (Not e\room\roomDoors[0]\open) Then
						e\room\roomDoors[0]\open = True
						;sound=Rand(0, 2)
						;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\roomDoors[0]\obj)
					EndIf
					If (Not e\room\roomDoors[1]\open) Then
						e\room\roomDoors[1]\open = True
						;sound=Rand(0, 2)
						;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\roomDoors[1]\obj)
					EndIf
				EndIf
				If e\room\npc[0]\reload = 1 Then
					e\room\npc[0]\dropSpeed = 0
				EndIf
			EndIf

			If e\room\npc[0]\state <> 5 Then
				e\eventState2 = 7
				DebugLog "fffffffff"
			EndIf
		ElseIf e\eventState2 = 3 Then
			If e\room\npc[0]\state <> 5 Then
				e\eventState2 = 7
				DebugLog "fffffffff"
			EndIf

			If MeNPCSeesPlayer(e\room\npc[0],True)=2 Then
				e\eventState2 = 4
				DebugLog "ddddddddd"
			EndIf

			If e\room\npc[0]\pathStatus <> 1 Then
				;If e\room\npc[0]\pathTimer# < 70*3
				If e\room\npc[0]\pathTimer# = 0.0 Then
					;e\room\npc[0]\pathTimer# = e\room\npc[0]\pathTimer# + timing\tickDuration
					If e\room\npc[0]\prevState = 1 Then
						If (e\room\npc[0]\soundChannels[1] = 0) Then
							e\room\npc[0]\sounds[1] = LoadSound("SFX/SCP/049/Room2SLEnter.ogg")
							e\room\npc[0]\soundChannels[1] = PlayRangedSound(e\room\npc[0]\sounds[1], mainPlayer\cam, e\room\npc[0]\collider)
						Else
							If (Not IsChannelPlaying(e\room\npc[0]\soundChannels[1])) Then
								e\room\npc[0]\pathTimer# = 1.0
							EndIf
						EndIf
					ElseIf e\room\npc[0]\prevState = 2 Then
						If e\room\npc[0]\frame >= 1118 Then
							e\room\npc[0]\pathTimer# = 1.0
						EndIf
					EndIf
				Else
					Select e\room\npc[0]\state3
						Case 1
							e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(e\room\objects[16],True),EntityY(e\room\objects[16],True),EntityZ(e\room\objects[16],True))
							e\room\npc[0]\prevState = 1
							DebugLog "Path1"
						Case 2
							e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(e\room\objects[15],True),EntityY(e\room\objects[15],True),EntityZ(e\room\objects[15],True))
							e\room\npc[0]\prevState = 2
							DebugLog "Path2"
						Case 3
							;e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True),EntityZ(e\room\objects[7],True))
							;e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(e\room\obj,True),EntityY(e\room\objects[7],True),EntityZ(e\room\obj,True))
							e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],e\room\npc[0]\pathX,0.1,e\room\npc[0]\pathZ)
							e\room\npc[0]\prevState = 1
							DebugLog "Path3"
						Case 4
							e\eventState2 = 5
					End Select
					e\room\npc[0]\pathTimer# = 0.0
					e\room\npc[0]\state3 = e\room\npc[0]\state3 + 1
				EndIf
			EndIf
		ElseIf e\eventState2 = 4 Then
			If e\room\npc[0]\state <> 5 Then
				e\eventState2 = 7
				e\room\npc[0]\state3 = 5.0
				DebugLog "fffffffff"
			EndIf
		ElseIf e\eventState2 = 5 Then
			;RemoveNPC(e\room\npc[0])
			DebugLog "ddddddddd"
			e\room\npc[0]\state = 2
			For r.Rooms = Each Rooms
				If r <> mainPlayer\currRoom Then
					If (EntityDistance(r\obj,e\room\npc[0]\collider)<HideDistance*2 And EntityDistance(r\obj,e\room\npc[0]\collider)>HideDistance) Then
						e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(r\obj),EntityY(r\obj),EntityZ(r\obj))
						e\room\npc[0]\pathTimer = 0.0
						If e\room\npc[0]\pathStatus = 1 Then e\eventState2 = 6
						Exit
					EndIf
				EndIf
			Next
		ElseIf e\eventState2 = 6 Then
			If MeNPCSeesPlayer(e\room\npc[0],True) Or e\room\npc[0]\state2 > 0 Or e\room\npc[0]\lastSeen > 0 Then
				DebugLog "fffffffff"
				e\eventState2 = 7
			Else
				;Still playing the Music for SCP-049 (in the real, SCP-049's State will be set to 2, causing it to stop playing the chasing track)
				;If Music(20) = 0 Then Music(20) = LoadSound("SFX/Horror/Horror12.ogg") ;TODO: fix ;TODOTODO: None of this to begin with.
				;ShouldPlay = 20
				If e\room\npc[0]\pathStatus<>1 Then
					e\room\npc[0]\idle = 70*60 ;(Making SCP-049 idle for one minute (twice as fast for aggressive NPCs = True))
					PositionEntity e\room\npc[0]\collider,0,500,0
					ResetEntity e\room\npc[0]\collider
					DebugLog "eeeeeeeee"
					e\eventState2 = 7
				EndIf
			EndIf
		;ElseIf e\eventState2 = 7
		;	e\room\roomDoors[0]\locked = False
		;	e\room\roomDoors[1]\locked = False
		;	e\eventState2 = 8
		EndIf

		If e\room\npc[0]<>Null Then
			If e\eventState2 < 7 Then
				If e\eventState2 > 2 Then
					If Abs(EntityY(e\room\roomDoors[0]\frameobj)-EntityY(e\room\npc[0]\collider))>1.0 Then
						If Abs(EntityY(e\room\roomDoors[0]\frameobj)-EntityY(mainPlayer\collider))<1.0 Then
							If e\room\roomDoors[0]\open Then
								e\room\roomDoors[0]\open = False
								e\room\roomDoors[0]\fastopen = 1
								PlaySound2 LoadTempSound("SFX/Door/DoorClose079.ogg")
								DebugLog "079 - CLOSE DOOR AT HALLWAY IN ROOM2SL"
							EndIf
						EndIf
					Else
						If e\room\roomDoors[0]\open = False Then
							e\room\roomDoors[0]\fastopen = 0
							e\room\roomDoors[0]\open = True
							;sound=Rand(0, 2)
							;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\roomDoors[0]\obj)
							PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
							DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
						EndIf
					EndIf
				EndIf

				If e\eventState2 > 0 Then CanSave% = False
			Else
				If e\room\roomDoors[0]\open = False Then
					e\room\roomDoors[0]\fastopen = 0
					e\room\roomDoors[0]\open = True
					;sound=Rand(0, 2)
					;PlayRangedSound(OpenDoorSFX(0,sound),mainPlayer\cam,e\room\roomDoors[0]\obj)
					PlaySound2 LoadTempSound("SFX/Door/DoorOpen079.ogg")
					DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]

	;Lever for checkpoint locking (might have a function in the future for the case if the checkpoint needs to be locked again)
	If mainPlayer\currRoom = e\room Then
		e\eventState3 = e\room\levers[0]\succ
		If e\eventState3 = 1 Then
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

	Local RN$ = r\roomTemplate\name$

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
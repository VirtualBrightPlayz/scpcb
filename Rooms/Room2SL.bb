Function FillRoom2SL(r.Rooms)
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
    r\Objects[17] = LoadMesh_Strict("GFX\map\room2sl_2.b3d",r\obj)
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
    
    r\Objects[22] = LoadMesh_Strict("GFX\map\room2sl_hb.b3d",r\obj)
    EntityPickMode r\Objects[22],2
    EntityType r\Objects[22],HIT_MAP
    EntityAlpha r\Objects[22],0.0
End Function

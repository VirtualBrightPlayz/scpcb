Function InitializeNPCtype173(n.NPCs)
    n\NVName = "SCP-173"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.32
    EntityType n\Collider, HIT_PLAYER
    n\Gravity = True
    
    n\obj = LoadMesh_Strict("GFX\npcs\173_2.b3d")
    
    ;On Halloween set jack-o-latern texture.
    If (Left(CurrentDate(), 7) = "31 Oct ") Then
        HalloweenTex = True
        Local texFestive = LoadTexture_Strict("GFX\npcs\173h.pt", 1)
        EntityTexture n\obj, texFestive, 0, 0
        FreeTexture texFestive
    EndIf
    
    temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "scale") / MeshDepth(n\obj))			
    ScaleEntity n\obj, temp,temp,temp
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "speed") / 100.0)
    ;n\obj = LoadMesh_Strict("GFX\mesh\173.3ds")
    ;scaleentity n\obj, 0.35 / meshWidth(n\obj), 0.30 / meshWidth(n\obj), 0.30 / meshWidth(n\obj)
    
    n\obj2 = LoadMesh_Strict("GFX\173box.b3d")
    ScaleEntity n\obj2, RoomScale, RoomScale, RoomScale
    HideEntity n\obj2
End Function

Function UpdateNPCtype173(n.NPCs)
    If Curr173\Idle <> 3 Then
        dist# = EntityDistance(n\Collider, Collider)		
        
        n\State3 = 1
        
        If n\Idle < 2 Then 
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
            RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
            
            If n\Idle = False Then
                Local temp% = False
                Local move% = True
                If dist < 15 Then
                    If dist < 10.0 Then 
                        If EntityVisible(n\Collider, Collider) Then
                            temp = True
                            n\EnemyX = EntityX(Collider, True)
                            n\EnemyY = EntityY(Collider, True)
                            n\EnemyZ = EntityZ(Collider, True)
                        EndIf
                    EndIf										
                    
                    Local SoundVol# = Max(Min((Distance(EntityX(n\Collider), EntityZ(n\Collider), n\PrevX, n\PrevZ) * 2.5), 1.0), 0.0)
                    n\SoundChn = LoopSound2(StoneDragSFX, n\SoundChn, Camera, n\Collider, 10.0, n\State)
                    
                    n\PrevX = EntityX(n\Collider)
                    n\PrevZ = EntityZ(n\Collider)				
                    
                    If (BlinkTimer < - 16 Or BlinkTimer > - 6) And (IsNVGBlinking=False) Then
                        If EntityInView(n\obj, Camera) Then move = False
                    EndIf
                EndIf
                
                If NoTarget Then move = True
                
                ;player is looking at it -> doesn't move
                If move=False Then
                    BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
                    CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs2())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0))								
                    
                    If dist < 3.5 And MilliSecs2() - n\LastSeen > 60000 And temp Then
                        PlaySound_Strict(HorrorSFX(Rand(3,4)))
                        
                        n\LastSeen = MilliSecs2()
                    EndIf
                    
                    If dist < 1.5 And Rand(700) = 1 Then PlaySound2(Scp173SFX(Rand(0, 2)), Camera, n\obj)
                    
                    If dist < 1.5 And n\LastDist > 2.0 And temp Then
                        CurrCameraZoom = 40.0
                        HeartBeatRate = Max(HeartBeatRate, 140)
                        HeartBeatVolume = 0.5
                        
                        Select Rand(5)
                            Case 1
                                PlaySound_Strict(HorrorSFX(1))
                            Case 2
                                PlaySound_Strict(HorrorSFX(2))
                            Case 3
                                PlaySound_Strict(HorrorSFX(9))
                            Case 4
                                PlaySound_Strict(HorrorSFX(10))
                            Case 5
                                PlaySound_Strict(HorrorSFX(14))
                        End Select
                    EndIf									
                        
                    n\LastDist = dist
                    
                    n\State = Max(0, n\State - FPSfactor / 20)
                Else 
                    ;more than 6 room lengths away from the player -> teleport to a room closer to the player
                    If dist > 50 Then
                        If Rand(70)=1 Then
                            If PlayerRoom\RoomTemplate\Name <> "exit1" And PlayerRoom\RoomTemplate\Name <> "gatea" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
                                For w.waypoints = Each WayPoints
                                    If w\door=Null And Rand(5)=1 Then
                                        x = Abs(EntityX(Collider)-EntityX(w\obj,True))
                                        If x < 25.0 And x > 15.0 Then
                                            z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
                                            If z < 25 And z > 15.0 Then
                                                DebugLog "MOVING 173 TO "+w\room\roomtemplate\name
                                                PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
                                                ResetEntity n\Collider
                                                Exit
                                            EndIf
                                        EndIf
                                            
                                    EndIf
                                Next
                            EndIf
                        EndIf
                    ElseIf dist > HideDistance*0.8 ;3-6 rooms away from the player -> move randomly from waypoint to another
                        If Rand(70)=1 Then TeleportCloser(n)
                    Else ;less than 3 rooms away -> actively move towards the player
                        n\State = CurveValue(SoundVol, n\State, 3)
                        
                        ;try to open doors
                        If Rand(20) = 1 Then
                            For d.Doors = Each Doors
                                If (Not d\locked) And d\open = False And d\Code = "" And d\KeyCard=0 Then
                                    For i% = 0 To 1
                                        If d\buttons[i] <> 0 Then
                                            If Abs(EntityX(n\Collider) - EntityX(d\buttons[i])) < 0.5 Then
                                                If Abs(EntityZ(n\Collider) - EntityZ(d\buttons[i])) < 0.5 Then
                                                    If (d\openstate >= 180 Or d\openstate <= 0) Then
                                                        pvt = CreatePivot()
                                                        PositionEntity pvt, EntityX(n\Collider), EntityY(n\Collider) + 0.5, EntityZ(n\Collider)
                                                        PointEntity pvt, d\buttons[i]
                                                        MoveEntity pvt, 0, 0, n\Speed * 0.6
                                                        
                                                        If EntityPick(pvt, 0.5) = d\buttons[i] Then 
                                                            PlaySound_Strict (LoadTempSound("SFX\Door\DoorOpen173.ogg"))
                                                            UseDoor(d,False)
                                                        EndIf
                                                        
                                                        FreeEntity pvt
                                                    EndIf
                                                EndIf
                                            EndIf
                                        EndIf
                                    Next
                                EndIf
                            Next
                        EndIf
                        
                        If NoTarget
                            temp = False
                            n\EnemyX = 0
                            n\EnemyY = 0
                            n\EnemyZ = 0
                        EndIf
                        
                        ;player is not looking and is visible from 173's position -> attack
                        If temp Then 				
                            If dist < 0.65 Then
                                If KillTimer >= 0 And (Not GodMode) Then
                                    
                                    Select PlayerRoom\RoomTemplate\Name
                                        Case "lockroom", "room2closets", "coffin"
                                            DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. The surveillance tapes confirm that the subject was killed by SCP-173."	
                                        Case "173"
                                            DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. According to Security Chief Franklin who was present at SCP-173's containment "
                                            DeathMSG = DeathMSG + "chamber during the breach, the subject was killed by SCP-173 as soon as the disruptions in the electrical network started."
                                        Case "room2doors"
                                            DeathMSG = Chr(34)+"If I'm not mistaken, one of the main purposes of these rooms was to stop SCP-173 from moving further in the event of a containment breach. "
                                            DeathMSG = DeathMSG + "So, who's brilliant idea was it to put A GODDAMN MAN-SIZED VENTILATION DUCT in there?"+Chr(34)
                                        Default 
                                            DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. Assumed to be attacked by SCP-173."
                                    End Select
                                    
                                    If (Not GodMode) Then n\Idle = True
                                    PlaySound_Strict(NeckSnapSFX(Rand(0,2)))
                                    If Rand(2) = 1 Then 
                                        TurnEntity(Camera, 0, Rand(80,100), 0)
                                    Else
                                        TurnEntity(Camera, 0, Rand(-100,-80), 0)
                                    EndIf
                                    Kill()
                                    
                                EndIf
                            Else
                                PointEntity(n\Collider, Collider)
                                RotateEntity n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider)
                                ;MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
                                TranslateEntity n\Collider,Cos(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor,0.0,Sin(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor
                            EndIf
                            
                        Else ;player is not visible -> move to the location where he was last seen							
                            If n\EnemyX <> 0 Then						
                                If Distance(EntityX(n\Collider), EntityZ(n\Collider), n\EnemyX, n\EnemyZ) > 0.5 Then
                                    AlignToVector(n\Collider, n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider), 3)
                                    MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
                                    If Rand(500) = 1 Then n\EnemyX = 0 : n\EnemyY = 0 : n\EnemyZ = 0
                                Else
                                    n\EnemyX = 0 : n\EnemyY = 0 : n\EnemyZ = 0
                                End If
                            Else
                                If Rand(400)=1 Then RotateEntity (n\Collider, 0, Rnd(360), 10)
                                TranslateEntity n\Collider,Cos(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor,0.0,Sin(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor
                                
                            End If
                        EndIf
                        
                    EndIf ; less than 2 rooms away from the player
                    
                EndIf
                
            EndIf ;idle = false
            
            PositionEntity(n\Collider, EntityX(n\Collider), Min(EntityY(n\Collider),0.35), EntityZ(n\Collider))
            
        Else ;idle = 2
            
            If n\Target <> Null Then
                Local tmp = False
                If dist > HideDistance*0.7
                    If EntityVisible(n\obj,Collider)=False
                        tmp = True
                    EndIf
                EndIf
                If (Not tmp)
                    PointEntity n\obj, n\Target\Collider
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0, True								
                    dist = EntityDistance(n\Collider, n\Target\Collider)
                    ;MoveEntity n\Collider, 0, 0, 0.008*FPSfactor*Max(Min((dist*2-1.0)*0.5,1.0),-0.5)
                    MoveEntity n\Collider, 0, 0, 0.016*FPSfactor*Max(Min((dist*2-1.0)*0.5,1.0),-0.5)
                    n\GravityMult = 1.0
                Else
                    PositionEntity n\Collider,EntityX(n\Target\Collider),EntityY(n\Target\Collider)+0.3,EntityZ(n\Target\Collider)
                    ResetEntity n\Collider
                    n\DropSpeed = 0
                    n\GravityMult = 0.0
                    ;PointEntity n\Collider, n\Target\Collider
                    ;RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0, True
                    ;dist = EntityDistance(n\Collider, n\Target\Collider)
                    ;MoveEntity n\Collider, 0, 0, dist-0.6
                EndIf
                
                ;For r.Rooms = Each Rooms
                ;	If r\RoomTemplate\Name = "start" Then
                ;		If Distance(EntityX(n\Collider),EntityZ(n\Collider),EntityX(r\obj,True)+1024*RoomScale,EntityZ(r\obj,True)+384*RoomScale)<1.6 Then
                ;			n\Idle = 3
                ;			n\Target = Null
                ;		EndIf
                ;		Exit
                ;	EndIf
                ;Next
            EndIf
            
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) + 0.05 + Sin(MilliSecs2()*0.08)*0.02, EntityZ(n\Collider))
            RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
            
            ShowEntity n\obj2
            
            PositionEntity(n\obj2, EntityX(n\Collider), EntityY(n\Collider) - 0.05 + Sin(MilliSecs2()*0.08)*0.02, EntityZ(n\Collider))
            RotateEntity (n\obj2, 0, EntityYaw(n\Collider)-180, 0)
        EndIf
    EndIf
End Function

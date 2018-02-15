Const STATE173_IDLE%             = 0
Const STATE173_ATTACK%           = 1
Const STATE173_MOVE_TO_TARGET%   = 2
Const STATE173_BEING_CONTAINED%  = 3
Const STATE173_CONTAINED%        = 4
Type NPCData173
    ;TODO: Add SFX here. Remember to deload them when the NPC's deletion is called.
End Type

Function InitializeNPCtype173(n.NPCs)
    n\nvName = "SCP-173"
    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.32)
    EntityType(n\collider, HIT_PLAYER)

    ;TODO: Remove this?
    n\gravity = True
    
    n\obj = LoadMesh("GFX\npcs\173_2.b3d")
    
    ;On Halloween set jack-o-latern texture.
    If (Left(CurrentDate(), 7) = "31 Oct ") Then
        HalloweenTex = True
        Local texFestive% = LoadTexture("GFX\npcs\173h.pt", 1)
        EntityTexture(n\obj, texFestive, 0, 0)
        FreeTexture(texFestive)
    EndIf
    
    Local temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "scale") / MeshDepth(n\obj))			
    ScaleEntity(n\obj, temp, temp, temp)
    
    n\speed = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "speed") / 100.0)
    
    n\obj2 = LoadMesh("GFX\173box.b3d")
    ScaleEntity(n\obj2, RoomScale, RoomScale, RoomScale)
    HideEntity(n\obj2)

	n\state = STATE173_ATTACK
End Function

Function UpdateNPCtype173(n.NPCs)
    Local dist#

    If (n\state = STATE173_IDLE Or n\state = STATE173_CONTAINED) Then
        Return
    EndIf

    Select n\state
        Case STATE173_ATTACK
            dist = EntityDistance(n\Collider, Collider)
            PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider) - 0.32, EntityZ(n\collider))
            RotateEntity(n\obj, 0, EntityYaw(n\collider) - 180, 0)

            Local playerVisible% = False
            Local canMove% = True
            If dist < 15 Then
                If dist < 10.0 Then 
                    If EntityVisible(n\collider, collider) Then
                        playerVisible = True
                        n\targetX = EntityX(Collider, True)
                        n\targetY = EntityY(Collider, True)
                        n\targetZ = EntityZ(Collider, True)
                    EndIf
                EndIf
                
                n\prevX = EntityX(n\collider)
                n\prevZ = EntityZ(n\collider)				
                
                If (BlinkTimer < - 16 Or BlinkTimer > - 6) And (Not IsNVGBlinking) Then
                    If EntityInView(n\obj, Camera) Then canMove = False
                EndIf
            EndIf
            
            If NoTarget Then canMove = True
            
            ;player is looking at it -> doesn't move
            If (Not canMove) Then
                ;Blur and zoom camera slightly when looking at 173.
                BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
                CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs2())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0))								
                
                ;If it's close spoopy horror sound.
                If dist < 3.5 And MilliSecs2() - n\lastSeen > 60000 And playerVisible Then
                    PlaySound_Strict(HorrorSFX(Rand(3,4)))
                    
                    n\lastSeen = MilliSecs2()
                EndIf
                
                If dist < 1.5 And Rand(700) = 1 Then PlaySound2(Scp173SFX(Rand(0, 2)), Camera, n\obj)
                
                If dist < 1.5 And n\lastDist > 2.0 And playerVisible Then
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
                    
                n\lastDist = dist
            Else 
                n\soundChn = LoopSound2(StoneDragSFX, n\soundChn, Camera, n\collider, 10.0, n\state)

                ;more than 6 room lengths away from the player -> teleport to a room closer to the player
                If dist > 50 Then
                    If Rand(70)=1 Then
                        If PlayerRoom\RoomTemplate\Name <> "exit1" And PlayerRoom\RoomTemplate\Name <> "gatea" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
                            For w.waypoints = Each WayPoints
                                If w\door=Null And Rand(5)=1 Then
                                    x = Abs(EntityX(Collider) - EntityX(w\obj, True))
                                    If x < 25.0 And x > 15.0 Then
                                        z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
                                        If z < 25 And z > 15.0 Then
                                            DebugLog "MOVING 173 TO " + w\room\roomtemplate\name
                                            PositionEntity(n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True))
                                            ResetEntity(n\Collider)
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
                    
                    ;try to open doors
                    If Rand(20) = 1 Then
                        Local d.Doors
                        For d.Doors = Each Doors
                            If (Not d\locked) And d\open = False And d\Code = "" And d\KeyCard=0 Then
                                For i% = 0 To 1
                                    If d\buttons[i] <> 0 Then
                                        If Abs(EntityX(n\collider) - EntityX(d\buttons[i])) < 0.5 Then
                                            If Abs(EntityZ(n\collider) - EntityZ(d\buttons[i])) < 0.5 Then
                                                If (d\openstate >= 180 Or d\openstate <= 0) Then
                                                    If DeltaYaw(n\collider, d\buttons[i]) < 60 And DeltaYaw(d\buttons[i], n\collider) < 60 Then 
                                                        PlaySound_Strict(LoadTempSound("SFX\Door\DoorOpen173.ogg"))
                                                        UseDoor(d,False)
                                                    EndIf
                                                EndIf
                                            EndIf
                                        EndIf
                                    EndIf
                                Next
                            EndIf
                        Next
                    EndIf
                    
                    If NoTarget
                        playerVisible = False
                        n\targetX = 0
                        n\targetY = 0
                        n\targetZ = 0
                    EndIf
                    
                    ;player is not looking and is visible from 173's position -> attack
                    If playerVisible Then 				
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
                                
                                n\state = STATE173_IDLE
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
                            TranslateEntity n\Collider,Cos(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor,0.0,Sin(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor
                        EndIf
                        
                    Else ;player is not visible -> move to the location where he was last seen							
                        If n\targetX <> 0 Then						
                            If Distance(EntityX(n\Collider), EntityZ(n\Collider), n\targetX, n\targetZ) > 0.5 Then
                                AlignToVector(n\Collider, n\targetX-EntityX(n\Collider), 0, n\targetZ-EntityZ(n\Collider), 3)
                                MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
                                If Rand(500) = 1 Then n\targetX = 0 : n\targetY = 0 : n\targetZ = 0
                            Else
                                n\targetX = 0 : n\targetY = 0 : n\targetZ = 0
                            End If
                        Else
                            If Rand(400)=1 Then RotateEntity(n\Collider, 0, Rnd(360), 10)
                            TranslateEntity n\Collider,Cos(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor,0.0,Sin(EntityYaw(n\Collider)+90.0)*n\Speed*FPSfactor
                            
                        End If
                    EndIf
                    
                EndIf ; less than 2 rooms away from the player
                
                ;TODO: Why random Y?
                ;PositionEntity(n\Collider, EntityX(n\Collider), Min(EntityY(n\Collider),0.35), EntityZ(n\Collider))
            EndIf
        Case STATE173_MOVE_TO_TARGET
            ;If 173 was given a target then use its position.
            If (n\target <> Null) Then
                n\targetX = EntityX(n\target\collider)
                n\targetY = EntityY(n\target\collider)
                n\targetZ = EntityZ(n\target\collider)
            EndIf

            If (n\targetX <> 0) Then
                If Distance(EntityX(n\Collider), EntityZ(n\Collider), n\targetX, n\targetZ) > 0.5 Then
                    AlignToVector(n\Collider, n\targetX-EntityX(n\Collider), 0, n\targetZ-EntityZ(n\Collider), 3)
                    MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
                Else
                    n\targetX = 0
                    n\targetY = 0
                    n\targetZ = 0
                    n\state = STATE173_IDLE
                EndIf
            Else
                n\state = STATE173_IDLE
            EndIf
        Case STATE173_BEING_CONTAINED
            If (n\target <> Null) Then
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
                    MoveEntity n\Collider, 0, 0, 0.016*FPSfactor*Max(Min((dist*2-1.0)*0.5,1.0),-0.5)
                Else
                    PositionEntity n\Collider,EntityX(n\Target\Collider),EntityY(n\Target\Collider)+0.3,EntityZ(n\Target\Collider)
                    ResetEntity n\Collider
                    ;PointEntity n\Collider, n\Target\Collider
                    ;RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0, True
                    ;dist = EntityDistance(n\Collider, n\Target\Collider)
                    ;MoveEntity n\Collider, 0, 0, dist-0.6
                EndIf
            EndIf
            
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) + 0.05 + Sin(MilliSecs2()*0.08)*0.02, EntityZ(n\Collider))
            RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
            
            ShowEntity n\obj2
            
            PositionEntity(n\obj2, EntityX(n\Collider), EntityY(n\Collider) - 0.05 + Sin(MilliSecs2()*0.08)*0.02, EntityZ(n\Collider))
            RotateEntity (n\obj2, 0, EntityYaw(n\Collider)-180, 0)
    End Select
End Function
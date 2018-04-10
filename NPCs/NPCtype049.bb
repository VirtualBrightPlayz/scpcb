Function InitializeNPCtype049(n.NPCs)
    n\NVName = "SCP-049"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    EntityType n\Collider, HIT_PLAYER
    n\obj = LoadAnimMesh_Strict("GFX\npcs\scp-049.b3d")
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049", "speed") / 100.0)
    
    temp# = GetINIFloat("DATA\NPCs.ini", "SCP-049", "scale")
    ScaleEntity n\obj, temp, temp, temp	
    
    n\Sound = LoadSound_Strict("SFX\Horror\Horror12.ogg")
    
    ;If HorrorSFX(13)=0 Then HorrorSFX(13)=LoadSound_Strict("SFX\Horror\Horror13.ogg") ;TODO: fix
    
    n\CanUseElevator = True
End Function

Function UpdateNPCtype049(n.NPCs)
    ;n\state = the "main state" of the NPC
    ;n\state2 = attacks the player when the value is above 0.0
    ;n\state3 = timer for updating the path again
    
    prevFrame# = n\Frame
    
    dist  = EntityDistance(mainPlayer\collider, n\Collider)
    
    n\BlinkTimer# = 1.0
    
    If n\Idle > 0.1
        If mainPlayer\currRoom\RoomTemplate\Name$ <> "room049"
            n\Idle = Max(n\Idle-(1+SelectedDifficulty\aggressiveNPCs)*FPSfactor,0.1)
        EndIf
        n\DropSpeed = 0
        If ChannelPlaying(n\SoundChn) Then StopChannel(n\SoundChn)
        If ChannelPlaying(n\SoundChn2) Then StopChannel(n\SoundChn2)
    Else
        If n\Idle = 0.1
            TeleportCloser(n)
            n\Idle = 0.0
            DebugLog "SCP-049 not idle"
        EndIf
        
        Select n\State
            Case 0 ;nothing (used for events)
            Case 1 ;looking around before getting active
                ;[Block]
                If n\Frame=>538 Then
                    AnimateNPC(n, 659, 538, -0.45, False)
                    If n\Frame > 537.9 Then n\Frame = 37
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 659, 538, -0.45, False)
                    ;If AnimTime(n\obj)=538 Then SetAnimTime(n\obj, 37)
                Else
                    AnimateNPC(n, 37, 269, 0.7, False)
                    If n\Frame>268.9 Then n\State = 2
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 37, 269, 0.7, False)
                    ;If AnimTime(n\obj)=269 Then n\State = 2
                EndIf
                ;[End Block]
            Case 2 ;being active
                ;[Block]
                If (dist < HideDistance*2 Or n\InFacility<>1) And (Not n\Idle) ;Checking if the player is in range/if 049 is in facility/if 049 is not idle
                    n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider)
                    PlayerSeeAble% = MeNPCSeesPlayer(n)
                    If PlayerSeeAble%=True Or n\State2>0 ;Player is visible for 049's sight - attacking
                        ;Playing a sound after detecting the player
                        If n\PrevState < 1 And ChannelPlaying(n\SoundChn2)=False
                            If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
                            n\Sound2 = LoadSound_Strict("SFX\SCP\049\Spotted"+Rand(1,2)+".ogg")
                            n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,mainPlayer\cam,n\obj)
                            n\PrevState = 2
                        EndIf
                        n\PathStatus = 0
                        n\PathTimer# = 0.0
                        n\PathLocation = 0
                        If PlayerSeeAble%=True Then n\State2 = 70*2
                        PointEntity n\obj,mainPlayer\collider
                        RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
                        If dist < 0.5 Then
                            mainPlayer\camZoom = 20.0
                            mainPlayer\blurTimer = 500.0
                            
                            If (Not mainPlayer\godMode) Then
                                If mainPlayer\currRoom\RoomTemplate\Name$ = "room049"
                                    DeathMSG = "Three (3) active instances of SCP-049-2 discovered in the tunnel outside SCP-049's containment chamber. Terminated by Nine-Tailed Fox."
                                    For e.events = Each Events
                                        If e\EventName = "room049" Then e\EventState=-1 : Exit
                                    Next
                                Else
                                    DeathMSG = "An active instance of SCP-049-2 was discovered in [REDACTED]. Terminated by Nine-Tailed Fox."
                                    Kill(mainPlayer)
                                EndIf
                                ;PlaySound_Strict HorrorSFX(13) ;TODO: fix
                                n\State = 3
                            EndIf
                        Else
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor	
                            
                            If n\PrevState = 3 Then n\PrevState = 2
                            
                            If dist < 3.0 Then
                                AnimateNPC(n, Max(Min(AnimTime(n\obj),428.0),387), 463.0, n\CurrSpeed*38)
                            Else
                                If n\Frame>428.0 Then
                                    AnimateNPC(n, Min(AnimTime(n\obj),463.0), 498.0, n\CurrSpeed*38,False)
                                    If n\Frame>497.9 Then n\Frame = 358
                                Else
                                    AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                                EndIf
                            EndIf
                        EndIf
                    Else ;Finding a path to the player
                        If PlayerSeeAble% = 2 Then n\LastSeen = 70*15
                        
                        If n\LastSeen > 0
                            If n\PathStatus = 1 ;Path to player found
                                If n\Path[n\PathLocation]=Null Then
                                    If n\PathLocation > 19 Then
                                        n\PathLocation = 0 : n\PathStatus = 0
                                    Else
                                        n\PathLocation = n\PathLocation + 1
                                    EndIf
                                Else
                                    ;closes doors behind him
                                    If n\PathLocation>0 Then
                                        If n\Path[n\PathLocation-1] <> Null
                                            If n\Path[n\PathLocation-1]\door <> Null Then
                                                If (Not n\Path[n\PathLocation-1]\door\IsElevatorDoor)
                                                    If EntityDistance(n\Path[n\PathLocation-1]\obj,n\Collider)>0.3
                                                        If n\Path[n\PathLocation-1]\door\open Then UseDoor(n\Path[n\PathLocation-1]\door, False)
                                                    EndIf
                                                EndIf
                                            EndIf
                                        EndIf
                                    EndIf
                                    
                                    n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                                    PointEntity n\obj,n\Path[n\PathLocation]\obj
                                    RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
                                    MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
                                    
                                    ;opens doors in front of him
                                    dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                                    If dist2 < 0.6 Then
                                        temp = True
                                        If n\Path[n\PathLocation]\door <> Null Then
                                            If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
                                                If n\Path[n\PathLocation]\door\locked Or n\Path[n\PathLocation]\door\KeyCard>0 Or n\Path[n\PathLocation]\door\Code<>"" Then
                                                    temp = False
                                                    ;n\CurrSpeed = 0
                                                Else
                                                    If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
                                                EndIf
                                            EndIf
                                        EndIf
                                        If dist2#<0.2 And temp
                                            n\PathLocation = n\PathLocation + 1
                                        ElseIf dist2#<0.5 And (Not temp)
                                            ;Breaking up the path because the door cannot be operated by SCP-049
                                            n\PathStatus = 0
                                            n\PathTimer# = 0.0
                                        EndIf
                                    EndIf
                                    
                                    AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                                    
                                    ;Playing a sound if he hears the player
                                    If n\PrevState = 0 And ChannelPlaying(n\SoundChn2)=False
                                        If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
                                        If Rand(8)=3
                                            n\Sound2 = LoadSound_Strict("SFX\SCP\049\Detected4.ogg")
                                        Else
                                            n\Sound2 = LoadSound_Strict("SFX\SCP\049\Detected"+Rand(1,3)+".ogg")
                                        EndIf
                                        n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,mainPlayer\cam,n\obj)
                                        n\PrevState = 1
                                    EndIf
                                    
                                    ;Resetting the "PrevState" value randomly, to make 049 talking randomly 
                                    If Rand(300)=1 And ChannelPlaying(n\SoundChn2)=False Then n\PrevState = 0
                                    
                                    n\State3 = n\State3 + FPSfactor
                                    
                                    If n\PrevState > 1 Then n\PrevState = 1
                                    
                                    If n\State3 > 70*7 ;Upadting the path every 7 seconds
                                        If n\InFacility = InFacility
                                            n\PathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
                                        EndIf
                                        n\State3 = 0.0
                                    EndIf
                                EndIf
                                
                                If n\CurrElevator <> Null
                                    dist2# = EntityDistance(n\Collider,n\CurrElevator\door\frameobj)
                                    If dist2# < 0.7
                                        n\PathStatus = 0
                                        n\PathLocation = 0
                                        n\PathTimer# = 0.0
                                    EndIf
                                EndIf
                            Else ;No Path to the player found - stands still and tries to find a path
                                If n\CurrElevator = Null
                                    If n\PathStatus = 2 ;Updating path faster
                                        n\PathTimer# = n\PathTimer# + 2*FPSfactor
                                    Else
                                        n\PathTimer# = n\PathTimer# + FPSfactor
                                    EndIf
                                    If n\PathTimer# > 70*5-(2*SelectedDifficulty\aggressiveNPCs)
                                        If n\InFacility = InFacility
                                            n\PathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
                                        Else
                                            FindNextElevator(n)
                                        EndIf
                                        n\PathTimer# = 0.0
                                    EndIf
                                    AnimateNPC(n, 269, 345, 0.2)
                                    ;n\PrevState = 0
                                Else
                                    ;n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                                    n\CurrSpeed = n\Speed
                                    GoToElevator(n)
                                    
                                    If n\CurrSpeed > 0.0
                                        MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
                                        AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                                    Else
                                        AnimateNPC(n, 269, 345, 0.2)
                                    EndIf
                                EndIf
                            EndIf
                        Else ;Player hasn't been detected, just being idle
                            If n\PathStatus = 1
                                If n\Path[n\PathLocation]=Null Then
                                    If n\PathLocation > 19 Then
                                        n\PathLocation = 0 : n\PathStatus = 0
                                    Else
                                        n\PathLocation = n\PathLocation + 1
                                    EndIf
                                Else
                                    ;closes doors behind him
                                    If n\PathLocation>0 Then
                                        If n\Path[n\PathLocation-1] <> Null
                                            If n\Path[n\PathLocation-1]\door <> Null Then
                                                If (Not n\Path[n\PathLocation-1]\door\IsElevatorDoor)
                                                    If EntityDistance(n\Path[n\PathLocation-1]\obj,n\Collider)>0.3
                                                        If n\Path[n\PathLocation-1]\door\open Then UseDoor(n\Path[n\PathLocation-1]\door, False)
                                                    EndIf
                                                EndIf
                                            EndIf
                                        EndIf
                                    EndIf
                                    
                                    n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                                    PointEntity n\obj,n\Path[n\PathLocation]\obj
                                    RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
                                    MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
                                    
                                    ;opens doors in front of him
                                    dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                                    If dist2 < 0.6 Then
                                        temp = True
                                        If n\Path[n\PathLocation]\door <> Null Then
                                            If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
                                                If n\Path[n\PathLocation]\door\locked Or n\Path[n\PathLocation]\door\KeyCard>0 Or n\Path[n\PathLocation]\door\Code<>"" Then
                                                    temp = False
                                                    ;n\CurrSpeed = 0
                                                Else
                                                    If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
                                                EndIf
                                            EndIf
                                        EndIf
                                        If dist2#<0.2 And temp
                                            n\PathLocation = n\PathLocation + 1
                                        ElseIf dist2#<0.5 And (Not temp)
                                            ;Breaking up the path because the door cannot be operated by SCP-049
                                            n\PathStatus = 0
                                            n\PathTimer# = 0.0
                                        EndIf
                                    EndIf
                                    
                                    If n\PrevState = 0 And ChannelPlaying(n\SoundChn2)=False
                                        If Rand(8)=3
                                            If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
                                            n\Sound2 = LoadSound_Strict("SFX\SCP\049\Detected4.ogg")
                                            n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,mainPlayer\cam,n\obj)
                                        EndIf
                                        n\PrevState = 1
                                    EndIf
                                    
                                    AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                                    
                                    n\PrevState = 0
                                    n\State3 = n\State3 + FPSfactor
                                    
                                    If n\InFacility = True
                                        If n\State3 > 70*14 ;Breaking the path after 14 seconds
                                            n\PathStatus = 0
                                            n\PathLocation = 0
                                            n\PathTimer# = 0.0
                                            n\State3 = 0.0
                                        EndIf
                                    EndIf
                                EndIf
                                
                                If n\CurrElevator <> Null
                                    dist2# = EntityDistance(n\Collider,n\CurrElevator\door\frameobj)
                                    If dist2# < 0.7
                                        n\PathStatus = 0
                                        n\PathLocation = 0
                                        n\PathTimer# = 0.0
                                    EndIf
                                EndIf
                            Else
                                If n\CurrElevator = Null
                                    n\CurrSpeed = 0
                                    AnimateNPC(n, 269, 345, 0.2)
                                    If n\PathTimer# < 300+Rand(100,300)
                                        n\PathTimer# = n\PathTimer# + FPSfactor
                                    Else
                                        If n\InFacility = True
                                            If Rand(2)=1
                                                For r.Rooms = Each Rooms
                                                    If EntityDistance(r\obj,n\Collider)<12.0 And EntityDistance(r\obj,n\Collider)>4.0
                                                        n\PathStatus = FindPath(n,EntityX(r\obj),EntityY(r\obj),EntityZ(r\obj))
                                                        Exit
                                                    EndIf
                                                Next
                                            EndIf
                                        Else
                                            FindNextElevator(n)
                                        EndIf
                                        n\PathTimer# = 0.0
                                    EndIf
                                Else
                                    ;n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                                    n\CurrSpeed = n\Speed
                                    GoToElevator(n)
                                    
                                    If n\CurrSpeed > 0.0
                                        MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
                                        AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                                    Else
                                        AnimateNPC(n, 269, 345, 0.2)
                                    EndIf
                                EndIf
                            EndIf
                        EndIf
                    EndIf
                    
                    If n\CurrSpeed > 0.005 Then
                        If (prevFrame < 361 And n\Frame=>361) Or (prevFrame < 377 And n\Frame=>377) Then
                            PlaySound2(StepSFX(3,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.8,1.0))						
                        ElseIf (prevFrame < 431 And n\Frame=>431) Or (prevFrame < 447 And n\Frame=>447)
                            PlaySound2(StepSFX(3,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.8,1.0))
                        EndIf
                    EndIf
                    
                    If ChannelPlaying(n\SoundChn2)
                        UpdateSoundOrigin(n\SoundChn2,mainPlayer\cam,n\obj)
                    EndIf
                ElseIf (Not n\Idle)
                    If ChannelPlaying(n\SoundChn) Then StopChannel(n\SoundChn)
                    If PlayerInReachableRoom() ;Player is in a room where SCP-049 can teleport to
                        ;If Rand(100-(30*SelectedDifficulty\aggressiveNPCs))=1 Then TeleportCloser(n)
                        If Rand(50-(20*SelectedDifficulty\aggressiveNPCs))=1
                            For w.waypoints = Each WayPoints
                                If w\door=Null And w\room\dist < HideDistance And Rand(3)=1 Then
                                    If EntityDistance(w\room\obj,n\Collider)<EntityDistance(mainPlayer\collider,n\Collider)
                                        x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
                                        If x < 12.0 And x > 4.0 Then
                                            z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
                                            If z < 12 And z > 4.0 Then
                                                If w\room\dist > 4
                                                    DebugLog "MOVING 049 TO "+w\room\roomtemplate\name
                                                    PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
                                                    ResetEntity n\Collider
                                                    n\PathStatus = 0
                                                    n\PathTimer# = 0.0
                                                    n\PathLocation = 0
                                                    Exit
                                                EndIf
                                            EndIf
                                        EndIf
                                    EndIf
                                EndIf
                            Next
                        EndIf
                    EndIf
                EndIf
                ;[End Block]
            Case 3 ;The player was killed by SCP-049
                ;[Block]
                AnimateNPC(n, 537, 660, 0.7, False)
                
                ;Animate2(n\obj, AnimTime(n\obj), 537, 660, 0.7, False)
                PositionEntity n\Collider, CurveValue(EntityX(mainPlayer\collider),EntityX(n\Collider),20.0),EntityY(n\Collider),CurveValue(EntityZ(mainPlayer\collider),EntityZ(n\Collider),20.0)
                RotateEntity n\Collider, 0, CurveAngle(EntityYaw(mainPlayer\collider)-180.0,EntityYaw(n\Collider),40), 0
                ;[End Block]
            Case 4 ;Standing on catwalk in room4
                ;[Block]
                If dist < 8.0 Then
                    AnimateNPC(n, 18, 19, 0.05)
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 18, 19, 0.05)
                    PointEntity n\obj, mainPlayer\collider	
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 45.0), 0
                ElseIf dist > HideDistance*0.8
                    n\State = 2
                    TeleportCloser(n)
                EndIf
                ;[End Block]
            Case 5 ;used for "room2sl"
                ;[Block]
                n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider)
                PlayerSeeAble% = MeNPCSeesPlayer(n,True)
                If PlayerSeeAble% = True
                    n\State = 2
                    n\PathStatus = 0
                    n\PathLocation = 0
                    n\PathTimer = 0
                    n\State3 = 0
                    n\State2 = 70*2
                    n\PrevState = 0
                    PlaySound_Strict LoadTempSound("SFX\Room\Room2SL049Spawn.ogg")
                ElseIf PlayerSeeAble% = 2 And n\State3 > 0.0
                    n\PathStatus = FindPath(n,EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
                Else
                    If n\State3 = 5.0
                        If EntityDistance(n\Collider,mainPlayer\collider)>HideDistance
                            n\State = 2
                            n\PathStatus = 0
                            n\PathLocation = 0
                            n\PathTimer = 0
                            n\State3 = 0
                            n\PrevState = 0
                        Else
                            If n\PathStatus <> 1 Then n\PathStatus = FindPath(n,EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
                        EndIf
                    EndIf
                    
                    If n\PathStatus = 1
                        If n\Path[n\PathLocation]=Null
                            If n\PathLocation > 19 Then
                                n\PathLocation = 0 : n\PathStatus = 0
                            Else
                                n\PathLocation = n\PathLocation + 1
                            EndIf
                        Else
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                            PointEntity n\obj,n\Path[n\PathLocation]\obj
                            RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
                            MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
                            
                            ;closes doors behind him
                            If n\PathLocation>0 Then
                                If n\Path[n\PathLocation-1] <> Null
                                    If n\Path[n\PathLocation-1]\door <> Null Then
                                        If n\Path[n\PathLocation-1]\door\KeyCard=0
                                            If EntityDistance(n\Path[n\PathLocation-1]\obj,n\Collider)>0.3
                                                If n\Path[n\PathLocation-1]\door\open Then UseDoor(n\Path[n\PathLocation-1]\door, False)
                                            EndIf
                                        EndIf
                                    EndIf
                                EndIf
                            EndIf
                            
                            ;opens doors in front of him
                            dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                            If dist2 < 0.6 Then
                                If n\Path[n\PathLocation]\door <> Null Then
                                    If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
                                EndIf
                            EndIf
                            
                            If dist2#<0.2
                                n\PathLocation = n\PathLocation + 1
                            EndIf
                            
                            AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
                        EndIf
                    Else
                        Select n\PrevState
                            Case 0
                                AnimateNPC(n, 269, 345, 0.2)
                            Case 1
                                AnimateNPC(n, 661, 891, 0.4, False)
                            Case 2
                                AnimateNPC(n, 892, 1119, 0.4, False)
                        End Select
                    EndIf
                EndIf
                
                If Music(20) = 0 Then Music(20) = LoadSound_Strict("SFX\Music\049Chase.ogg")
                ShouldPlay = 20
                
                If n\CurrSpeed > 0.005 Then
                    If (prevFrame < 361 And n\Frame=>361) Or (prevFrame < 377 And n\Frame=>377) Then
                        PlaySound2(StepSFX(3,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.8,1.0))						
                    ElseIf (prevFrame < 431 And n\Frame=>431) Or (prevFrame < 447 And n\Frame=>447)
                        PlaySound2(StepSFX(3,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.8,1.0))
                    EndIf
                EndIf
                
                If ChannelPlaying(n\SoundChn2)
                    UpdateSoundOrigin(n\SoundChn2,mainPlayer\cam,n\obj)
                EndIf
                ;[End Block]
        End Select
    EndIf
    
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.22, EntityZ(n\Collider))
    
    RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
    
    n\LastSeen = Max(n\LastSeen-FPSfactor,0)
    
    n\State2 = Max(n\State2-FPSfactor,0)
End Function
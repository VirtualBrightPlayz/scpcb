Function InitializeNPCtypeZombie(n.NPCs)
    n\NVName = "Human"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    EntityType n\Collider, HIT_PLAYER
    
    For n2.NPCs = Each NPCs
        If n\NPCtype = n2\NPCtype And n<>n2 Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\npcs\zombie1.b3d")
        
        temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
        ScaleEntity n\obj, temp, temp, temp
        
        MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
    EndIf
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "speed") / 100.0)
    
    SetAnimTime(n\obj, 107)
    
    n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
    
    n\HP = 100
End Function

Function UpdateNPCtypeZombie(n.NPCs)
    If Abs(EntityY(mainPlayer\collider)-EntityY(n\Collider))<4.0 Then
        
        prevFrame# = n\Frame
        
        If (Not n\IsDead)
            Select n\State
                Case 0
                    AnimateNPC(n, 719, 777, 0.2, False)
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 719, 777, 0.2, False)
                    If n\Frame=777 Then
                        If Rand(700)=1 Then 							
                            If EntityDistance(mainPlayer\collider, n\Collider)<5.0 Then
                                n\Frame = 719
                                ;SetAnimTime (n\obj, 719)	
                                If Rand(3)=1 Then 
                                    n\State=1
                                    n\Frame = 155
                                    ;SetAnimTime n\obj, 155
                                EndIf
                            EndIf
                        EndIf
                    EndIf
                Case 1 ;stands up
                    If n\Frame=>682 Then 
                        AnimateNPC(n, 926, 935, 0.3, False)
                        If n\Frame = 935 Then n\State = 2
                        
                        ;Animate2(n\obj, AnimTime(n\obj), 926, 935, 0.3, False)
                        ;If AnimTime(n\obj)=935 Then n\State = 2
                    Else
                        AnimateNPC(n, 155, 682, 1.5, False)
                        ;Animate2(n\obj, AnimTime(n\obj), 155, 682, 1.5, False)
                    EndIf
                Case 2 ;following the player
                    If n\State3 < 0 Then ;check if the player is visible every three seconds
                        If EntityDistance(mainPlayer\collider, n\Collider)<5.0 Then 
                            If EntityVisible(mainPlayer\collider, n\Collider) Then n\State2 = 70*5
                        EndIf
                        n\State3=70*3
                    Else
                        n\State3=n\State3-timing\tickDuration
                    EndIf
                    
                    If n\State2 > 0 And (Not NoTarget) Then ;player is visible -> attack
                        n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider, 6.0, 0.6)
                        
                        n\PathStatus = 0
                        
                        dist = EntityDistance(mainPlayer\collider, n\Collider)
                        
                        PointEntity n\obj, mainPlayer\collider
                        RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
                        
                        If dist < 0.7 Then 
                            n\State = 3
                            If Rand(2)=1 Then
                                n\Frame = 2
                                ;SetAnimTime n\obj, 2
                            Else
                                n\Frame = 66
                                ;SetAnimTime n\obj, 66
                            EndIf
                        Else
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                            MoveEntity n\Collider, 0, 0, n\CurrSpeed * timing\tickDuration
                            
                            AnimateNPC(n, 936, 1017, n\CurrSpeed*60)
                            
                            ;Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\CurrSpeed*60)
                            
                        EndIf
                        
                        n\State2=n\State2-timing\tickDuration
                    Else
                        If n\PathStatus = 1 Then ;path found
                            If n\Path[n\PathLocation]=Null Then 
                                If n\PathLocation > 19 Then 
                                    n\PathLocation = 0 : n\PathStatus = 0
                                Else
                                    n\PathLocation = n\PathLocation + 1
                                EndIf
                            Else
                                PointEntity n\obj, n\Path[n\PathLocation]\obj
                                
                                RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
                                n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                                MoveEntity n\Collider, 0, 0, n\CurrSpeed * timing\tickDuration
                                
                                AnimateNPC(n, 936, 1017, n\CurrSpeed*60)
                                ;Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\CurrSpeed*60)
                                
                                If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
                                    n\PathLocation = n\PathLocation + 1
                                EndIf 
                            EndIf
                        Else ;no path to the player, stands still
                            n\CurrSpeed = 0
                            AnimateNPC(n, 778, 926, 0.1)
                            ;Animate2(n\obj, AnimTime(n\obj), 778, 926, 0.1)
                            
                            n\PathTimer = n\PathTimer-timing\tickDuration
                            If n\PathTimer =< 0 Then
                                n\PathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.1,EntityZ(mainPlayer\collider))
                                n\PathTimer = n\PathTimer+70*5
                            EndIf
                        EndIf
                    EndIf
                    
                    ;65, 80, 93, 109, 123
                    If n\CurrSpeed > 0.005 Then
                        If (prevFrame < 977 And n\Frame=>977) Or (prevFrame > 1010 And n\Frame<940) Then
                            ;PlaySound2(StepSFX(0,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))
                            PlaySound2(StepSFX(2,0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))
                        EndIf
                    EndIf						
                Case 3
                    If NoTarget Then n\State = 2
                    If n\Frame < 66 Then
                        AnimateNPC(n, 2, 65, 0.7, False)
                        
                        ;Animate2(n\obj, AnimTime(n\obj), 2, 65, 0.7, False)
                        If prevFrame < 23 And n\Frame=>23 Then 
                            PlaySound_Strict DamageSFX(Rand(5,8))
                            mainPlayer\injuries = mainPlayer\injuries+Rnd(0.4,1.0)
                            DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
                        ElseIf n\Frame=65 Then
                            n\State = 2
                        EndIf							
                    Else
                        AnimateNPC(n, 66, 132, 0.7, False)
                        ;Animate2(n\obj, AnimTime(n\obj), 66, 132, 0.7, False)
                        If prevFrame < 90 And n\Frame=>90 Then 
                            PlaySound_Strict DamageSFX(Rand(5,8))
                            mainPlayer\injuries = mainPlayer\injuries+Rnd(0.4,1.0)
                            DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
                        ElseIf n\Frame=132 Then
                            n\State = 2
                        EndIf		
                    EndIf
                    
            End Select
        Else
            AnimateNPC(n, 133, 157, 0.5, False)
        EndIf
        
        PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
        
        RotateEntity n\obj, -90, EntityYaw(n\Collider), 0
    EndIf
End Function
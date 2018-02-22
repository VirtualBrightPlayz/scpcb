Function InitializeNPCtype939(n.NPCs)
    ;i = 53
    ;For n2.NPCs = Each NPCs
    ;	If (n\NPCtype = n2\NPCtype) And (n<>n2) Then i=i+36
    ;Next
    ;n\NVName = "SCP-939-"+i
    Local amount939% = 0
    For n2.NPCs = Each NPCs
        If (n\NPCtype = n2\NPCtype) And (n<>n2)
            amount939% = amount939% + 1
        EndIf
    Next
    If amount939% = 0 Then i = 53
    If amount939% = 1 Then i = 89
    If amount939% = 2 Then i = 96
    n\NVName = "SCP-939-"+i
    
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.3
    EntityType n\Collider, HIT_PLAYER
    For n2.NPCs = Each NPCs
        If n\NPCtype = n2\NPCtype And n<>n2 Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\NPCs\scp-939.b3d")
        
        temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
        ScaleEntity n\obj, temp, temp, temp		
    EndIf
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
End Function

Function UpdateNPCtype939(n.NPCs)
    If mainPlayer\currRoom\RoomTemplate\Name <> "room3storage"
        n\State = 66
    EndIf
    
    ;state is set to 66 in the room3storage-event if player isn't inside the room
    If n\State < 66 Then 
        Select n\State
            Case 0
                AnimateNPC(n, 290,405,0.1)
                
                ;Animate2(n\obj,AnimTime(n\obj),290,405,0.1)
            Case 1
                
                If n\Frame=>644 And n\Frame<683 Then ;finish the walking animation
                    ;n\CurrSpeed = CurveValue(n\Speed*0.2, n\CurrSpeed, 10.0)
                    n\CurrSpeed = CurveValue(n\Speed*0.05, n\CurrSpeed, 10.0)
                    AnimateNPC(n, 644,683,28*n\CurrSpeed*4,False)
                    If n\Frame=>682 Then n\Frame =175
                    
                    ;Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed,False)
                    ;If AnimTime(n\obj)=683 Then SetAnimTime(n\obj,175)
                Else
                    n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
                    AnimateNPC(n, 175,297,0.22,False)
                    If n\Frame=>296 Then n\State = 2
                    
                    ;Animate2(n\obj,AnimTime(n\obj),175,297,0.22,False)
                    ;If AnimTime(n\obj)=297 Then n\State = 2
                EndIf
                
                n\LastSeen = 0
                
                MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor						
                
            Case 2
                n\State2 = Max(n\State2, (n\PrevState-3))
                
                dist = EntityDistance(n\Collider, mainPlayer\currRoom\Objects[n\State2])
                
                n\CurrSpeed = CurveValue(n\Speed*0.3*Min(dist,1.0), n\CurrSpeed, 10.0)
                MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor 
                
                prevFrame = n\Frame
                AnimateNPC(n, 644,683,28*n\CurrSpeed) ;walk
                
                ;prevFrame = AnimTime(n\obj)
                ;Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed) ;walk
                
                If (prevFrame<664 And n\Frame=>664) Or (prevFrame>673 And n\Frame<654) Then
                    PlaySound2(StepSFX(1, 0, Rand(0,3)), mainPlayer\cam, n\Collider, 12.0)
                    If Rand(10)=1 Then
                        temp = False
                        If n\SoundChn = 0 Then 
                            temp = True
                        ElseIf Not ChannelPlaying(n\SoundChn)
                            temp = True
                        EndIf
                        If temp Then
                            If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
                            n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Lure"+Rand(1,10)+".ogg")
                            n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider)
                        EndIf
                    EndIf
                EndIf
                
                PointEntity n\obj, mainPlayer\currRoom\Objects[n\State2]
                RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0), 0
                
                If dist<0.4 Then
                    n\State2 = n\State2 + 1
                    If n\State2 > n\PrevState Then n\State2 = (n\PrevState-3)
                    n\State = 1
                EndIf
                
            Case 3
                If EntityVisible(mainPlayer\collider, n\Collider) Then
                    If n\Sound2 = 0 Then n\Sound2 = LoadSound_Strict("SFX\General\Slash1.ogg")
                    
                    n\EnemyX = EntityX(mainPlayer\collider)
                    n\EnemyZ = EntityZ(mainPlayer\collider)
                    n\LastSeen = 10*7
                EndIf
                
                If n\LastSeen > 0 And (Not NoTarget) Then
                    prevFrame = n\Frame
                    
                    If (n\Frame=>18.0 And n\Frame<68.0) Then
                        n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
                        AnimateNPC(n, 18,68,0.5,True)
                        ;Animate2(n\obj,AnimTime(n\obj),18,68,0.5,True)
                        
                        ;hasn't hit
                        temp = False
                        
                        If prevFrame < 24 And n\Frame>=24 Then
                            temp = True
                        ElseIf prevFrame < 57 And n\Frame>=57
                            temp = True
                        EndIf
                        
                        If temp Then
                            If Distance(n\EnemyX, n\EnemyZ, EntityX(n\Collider), EntityZ(n\Collider))<1.5 Then
                                PlaySound_Strict n\Sound2
                                mainPlayer\injuries = mainPlayer\injuries + Rnd(1.5, 2.5)-IsPlayerWearing(mainPlayer,"vest",WORNITEM_BODY_SLOT)*0.5
                                mainPlayer\blurTimer = 500		
                            Else
                                n\Frame	 = 449
                                ;SetAnimTime(n\obj, 449)
                            EndIf
                        EndIf
                        
                        If mainPlayer\injuries>4.0 Then 
                            DeathMSG=Chr(34)+"All four (4) escaped SCP-939 specimens have been captured and recontained successfully. "
                            DeathMSG=DeathMSG+"Three (3) of them made quite a mess at Storage Area 6. A cleaning team has been dispatched."+Chr(34)
                            Kill()
                            If (Not mainPlayer\godMode) Then n\State = 5
                        EndIf								
                    Else
                        If n\LastSeen = 10*7 Then 
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                            
                            AnimateNPC(n, 449,464,6*n\CurrSpeed) ;run
                            ;Animate2(n\obj,AnimTime(n\obj),449,464,6*n\CurrSpeed) ;run
                            
                            If (prevFrame<452 And n\Frame=>452) Or (prevFrame<459 And n\Frame=>459) Then
                                PlaySound2(StepSFX(1, 1, Rand(0,3)), mainPlayer\cam, n\Collider, 12.0)
                            EndIf										
                            
                            If Distance(n\EnemyX, n\EnemyZ, EntityX(n\Collider), EntityZ(n\Collider))<1.1 Then ;player is visible
                                n\Frame = 18
                                ;SetAnimTime(n\obj, 18.0)
                            EndIf
                        Else
                            n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
                            AnimateNPC(n, 175,297,5*n\CurrSpeed,True)	
                            ;Animate2(n\obj,AnimTime(n\obj),175,297,5*n\CurrSpeed,True)
                        EndIf
                        
                    EndIf
                    
                    angle = VectorYaw(n\EnemyX-EntityX(n\Collider), 0.0, n\EnemyZ-EntityZ(n\Collider))
                    RotateEntity n\Collider, 0, CurveAngle(angle,EntityYaw(n\Collider),15.0), 0									
                    
                    MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor							
                    
                    n\LastSeen = n\LastSeen - FPSfactor
                Else
                    n\State = 2
                EndIf
                
            ;Animate2(n\obj,AnimTime(n\obj),406,437,0.1) ;leap
            Case 5
                If n\Frame<68 Then
                    AnimateNPC(n, 18,68,0.5,False) ;finish the attack animation
                    
                    ;Animate2(n\obj,AnimTime(n\obj),18,68,0.5,False) ;finish the attack animation
                Else
                    AnimateNPC(n, 464,473,0.5,False) ;attack to idle
                    
                    ;Animate2(n\obj,AnimTime(n\obj),464,473,0.5,False) ;attack to idle
                EndIf
                
        End Select
        
        If n\State < 3 And (Not NoTarget) And (Not n\IgnorePlayer) Then
            dist = EntityDistance(n\Collider, mainPlayer\collider)
            
            If dist < 4.0 Then dist = dist - EntityVisible(mainPlayer\collider, n\Collider)
            If mainPlayer\loudness*1.2>dist Or dist < 1.5 Then
                If n\State3 = 0 Then
                    If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
                    n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Attack"+Rand(1,3)+".ogg")
                    n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider)										
                    
                    PlaySound_Strict(LoadTempSound("SFX\SCP\939\attack.ogg"))
                    n\State3 = 1
                EndIf
                
                n\State = 3
            ElseIf mainPlayer\loudness*1.6>dist Then
                If n\State<>1 And n\Reload <= 0 Then
                    If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
                    n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Alert"+Rand(1,3)+".ogg")
                    n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider)	
                    
                    n\Frame = 175
                    n\Reload = 70 * 3
                    ;SetAnimTime(n\obj, 175)	
                EndIf
                
                n\State = 1
                
            EndIf
            
            n\Reload = n\Reload - FPSfactor
            
        EndIf				
        
        RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
        
        PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
        RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True					
    EndIf
End Function    
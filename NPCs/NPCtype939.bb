Function InitializeNPCtype939(n.NPCs)
    ;i = 53
    ;For n2.NPCs = Each NPCs
    ;	If (n\NPCtype = n2\NPCtype) And (n<>n2) Then i=i+36
    ;Next
    ;n\NVName = "SCP-939-"+i
    Local amount939% = 0
    For n2.NPCs = Each NPCs
        If (n\npcType = n2\NPCtype) And (n<>n2)
            amount939% = amount939% + 1
        EndIf
    Next
    If amount939% = 0 Then i = 53
    If amount939% = 1 Then i = 89
    If amount939% = 2 Then i = 96
    n\nvName = "SCP-939-"+i
    
    n\collider = CreatePivot()
    EntityRadius n\collider, 0.3
    EntityType n\collider, HIT_PLAYER
    For n2.NPCs = Each NPCs
        If n\npcType = n2\NPCtype And n<>n2 Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh("GFX\NPCs\scp-939.b3d")
        
        temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
        ScaleEntity n\obj, temp, temp, temp		
    EndIf
    
    n\speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
End Function

Function UpdateNPCtype939(n.NPCs)
    If mainPlayer\currRoom\RoomTemplate\Name <> "room3storage"
        n\state = 66
    EndIf
    
    ;state is set to 66 in the room3storage-event if player isn't inside the room
    If n\state < 66 Then 
        Select n\state
            Case 0
                AnimateNPC(n, 290,405,0.1)
                
                ;Animate2(n\obj,AnimTime(n\obj),290,405,0.1)
            Case 1
                
                If n\frame=>644 And n\frame<683 Then ;finish the walking animation
                    ;n\CurrSpeed = CurveValue(n\Speed*0.2, n\CurrSpeed, 10.0)
                    n\currSpeed = CurveValue(n\speed*0.05, n\currSpeed, 10.0)
                    AnimateNPC(n, 644,683,28*n\currSpeed*4,False)
                    If n\frame=>682 Then n\frame =175
                    
                    ;Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed,False)
                    ;If AnimTime(n\obj)=683 Then SetAnimTime(n\obj,175)
                Else
                    n\currSpeed = CurveValue(0, n\currSpeed, 5.0)
                    AnimateNPC(n, 175,297,0.22,False)
                    If n\frame=>296 Then n\state = 2
                    
                    ;Animate2(n\obj,AnimTime(n\obj),175,297,0.22,False)
                    ;If AnimTime(n\obj)=297 Then n\State = 2
                EndIf
                
                n\lastSeen = 0
                
                MoveEntity n\collider, 0,0,n\currSpeed*timing\tickDuration						
                
            Case 2
                n\state2 = Max(n\state2, (n\prevState-3))
                
                dist = EntityDistance(n\collider, mainPlayer\currRoom\Objects[n\state2])
                
                n\currSpeed = CurveValue(n\speed*0.3*Min(dist,1.0), n\currSpeed, 10.0)
                MoveEntity n\collider, 0,0,n\currSpeed*timing\tickDuration 
                
                prevFrame = n\frame
                AnimateNPC(n, 644,683,28*n\currSpeed) ;walk
                
                ;prevFrame = AnimTime(n\obj)
                ;Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed) ;walk
                
                If (prevFrame<664 And n\frame=>664) Or (prevFrame>673 And n\frame<654) Then
                    PlayRangedSound(sndManager\footstepMetal[Rand(0,7)]\internal, mainPlayer\cam, n\collider, 12.0)
                    If Rand(10)=1 Then
                        temp = False
                        If (Not IsChannelPlaying(n\soundChannels[0]))
                            temp = True
                        EndIf
                        If temp Then
                            If n\sounds[0] <> 0 Then FreeSound n\sounds[0] : n\sounds[0] = 0
                            n\sounds[0] = LoadSound("SFX\SCP\939\"+(n\ID Mod 3)+"Lure"+Rand(1,10)+".ogg")
                            n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider)
                        EndIf
                    EndIf
                EndIf
                
                PointEntity n\obj, mainPlayer\currRoom\Objects[n\state2]
                RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0), 0
                
                If dist<0.4 Then
                    n\state2 = n\state2 + 1
                    If n\state2 > n\prevState Then n\state2 = (n\prevState-3)
                    n\state = 1
                EndIf
                
            Case 3
                If EntityVisible(mainPlayer\collider, n\collider) Then
                    If n\sounds[1] = 0 Then n\sounds[1] = LoadSound("SFX\General\Slash1.ogg")
                    
                    n\EnemyX = EntityX(mainPlayer\collider)
                    n\EnemyZ = EntityZ(mainPlayer\collider)
                    n\lastSeen = 10*7
                EndIf
                
                If n\lastSeen > 0 And (Not NoTarget) Then
                    prevFrame = n\frame
                    
                    If (n\frame=>18.0 And n\frame<68.0) Then
                        n\currSpeed = CurveValue(0, n\currSpeed, 5.0)
                        AnimateNPC(n, 18,68,0.5,True)
                        ;Animate2(n\obj,AnimTime(n\obj),18,68,0.5,True)
                        
                        ;hasn't hit
                        temp = False
                        
                        If prevFrame < 24 And n\frame>=24 Then
                            temp = True
                        ElseIf prevFrame < 57 And n\frame>=57
                            temp = True
                        EndIf
                        
                        If temp Then
                            If Distance(n\EnemyX, n\EnemyZ, EntityX(n\collider), EntityZ(n\collider))<1.5 Then
                                PlaySound2(n\sounds[1])
                                mainPlayer\injuries = mainPlayer\injuries + Rnd(1.5, 2.5)-IsPlayerWearingTempName(mainPlayer,"vest")*0.5
                                mainPlayer\blurTimer = 500		
                            Else
                                n\frame	 = 449
                                ;SetAnimTime(n\obj, 449)
                            EndIf
                        EndIf
                        
                        If mainPlayer\injuries>4.0 Then 
                            DeathMSG=Chr(34)+"All four (4) escaped SCP-939 specimens have been captured and recontained successfully. "
                            DeathMSG=DeathMSG+"Three (3) of them made quite a mess at Storage Area 6. A cleaning team has been dispatched."+Chr(34)
                            Kill(mainPlayer)
                            If (Not mainPlayer\godMode) Then n\State = 5
                        EndIf								
                    Else
                        If n\LastSeen = 10*7 Then 
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
                            
                            AnimateNPC(n, 449,464,6*n\currSpeed) ;run
                            ;Animate2(n\obj,AnimTime(n\obj),449,464,6*n\CurrSpeed) ;run
                            
                            If (prevFrame<452 And n\frame=>452) Or (prevFrame<459 And n\frame=>459) Then
                                PlayRangedSound(sndManager\footstepMetalRun[Rand(0,7)]\internal, mainPlayer\cam, n\collider, 12.0)
                            EndIf										
                            
                            If Distance(n\EnemyX, n\EnemyZ, EntityX(n\collider), EntityZ(n\collider))<1.1 Then ;player is visible
                                n\frame = 18
                                ;SetAnimTime(n\obj, 18.0)
                            EndIf
                        Else
                            n\currSpeed = CurveValue(0, n\currSpeed, 5.0)
                            AnimateNPC(n, 175,297,5*n\currSpeed,True)	
                            ;Animate2(n\obj,AnimTime(n\obj),175,297,5*n\CurrSpeed,True)
                        EndIf
                        
                    EndIf
                    
                    angle = VectorYaw(n\EnemyX-EntityX(n\collider), 0.0, n\EnemyZ-EntityZ(n\collider))
                    RotateEntity n\collider, 0, CurveAngle(angle,EntityYaw(n\collider),15.0), 0									
                    
                    MoveEntity n\collider, 0,0,n\currSpeed*timing\tickDuration							
                    
                    n\lastSeen = n\lastSeen - timing\tickDuration
                Else
                    n\state = 2
                EndIf
                
            ;Animate2(n\obj,AnimTime(n\obj),406,437,0.1) ;leap
            Case 5
                If n\frame<68 Then
                    AnimateNPC(n, 18,68,0.5,False) ;finish the attack animation
                    
                    ;Animate2(n\obj,AnimTime(n\obj),18,68,0.5,False) ;finish the attack animation
                Else
                    AnimateNPC(n, 464,473,0.5,False) ;attack to idle
                    
                    ;Animate2(n\obj,AnimTime(n\obj),464,473,0.5,False) ;attack to idle
                EndIf
                
        End Select
        
        If n\state < 3 And (Not NoTarget) And (Not n\IgnorePlayer) Then
            dist = EntityDistance(n\collider, mainPlayer\collider)
            
            If dist < 4.0 Then dist = dist - EntityVisible(mainPlayer\collider, n\collider)
            If mainPlayer\loudness*1.2>dist Or dist < 1.5 Then
                If n\state3 = 0 Then
                    If n\sounds[0] <> 0 Then FreeSound n\sounds[0] : n\sounds[0] = 0
                    n\sounds[0] = LoadSound("SFX\SCP\939\"+(n\ID Mod 3)+"Attack"+Rand(1,3)+".ogg")
                    n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider)										
                    
                    PlaySound2(LoadTempSound("SFX\SCP\939\attack.ogg"))
                    n\state3 = 1
                EndIf
                
                n\state = 3
            ElseIf mainPlayer\loudness*1.6>dist Then
                If n\state<>1 And n\Reload <= 0 Then
                    If n\sounds[0] <> 0 Then FreeSound n\sounds[0] : n\sounds[0] = 0
                    n\sounds[0] = LoadSound("SFX\SCP\939\"+(n\ID Mod 3)+"Alert"+Rand(1,3)+".ogg")
                    n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider)	
                    
                    n\frame = 175
                    n\Reload = 70 * 3
                    ;SetAnimTime(n\obj, 175)	
                EndIf
                
                n\State = 1
                
            EndIf
            
            n\Reload = n\Reload - timing\tickDuration
            
        EndIf				
        
        RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
        
        PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
        RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True					
    EndIf
End Function    
;~IDEal Editor Parameters:
;~C#Blitz3D
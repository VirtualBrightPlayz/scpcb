Function InitializeNPCtypeTentacle(n.NPCs)
    n\NVName = "Unidentified"
    
    n\Collider = CreatePivot()
    
    For n2.NPCs = Each NPCs
        If n\NPCtype = n2\NPCtype And n<>n2 Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\NPCs\035tentacle.b3d")
        ScaleEntity n\obj, 0.065,0.065,0.065
    EndIf
    
    SetAnimTime n\obj, 283
End Function

Function UpdateNPCtypeTentacle(n.NPCs)
    dist = EntityDistance(n\Collider,Collider)
    
    If dist < 8.0 Then 
        
        Select n\State 
            Case 0 ;spawn
                
                If n\Frame>283 Then
                    HeartBeatVolume = Max(CurveValue(1.0, HeartBeatVolume, 50),HeartBeatVolume)
                    HeartBeatRate = Max(CurveValue(130, HeartBeatRate, 100),HeartBeatRate)
                    
                    PointEntity n\obj, Collider
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
                    
                    AnimateNPC(n, 283, 389, 0.3, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 283, 389, 0.3, False)
                    
                    If n\Frame>388 Then n\State = 1
                Else
                    If dist < 2.5 Then 
                        SetNPCFrame(n, 284)
                        n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
                        PlaySound_Strict(n\Sound2)
                    EndIf
                EndIf
                ;spawn 283,389
                ;attack 2, 32
                ;idle 33, 174
            Case 1 ;idle
                If dist < 1.8 Then 
                    If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
                        n\State = 2
                        If n\Sound<>0 Then FreeSound_Strict n\Sound : n\Sound = 0 
                        If n\Sound2<>0 Then FreeSound_Strict n\Sound2 : n\Sound2 = 0 
                        
                    EndIf
                EndIf
                
                PointEntity n\obj, Collider
                RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
                
                AnimateNPC(n, 33, 174, 0.3, True)
                ;Animate2(n\obj, AnimTime(n\obj), 33, 174, 0.3, True)
            Case 2
                
                ;finish the idle animation before playing the attack animation
                If n\Frame>33 And n\Frame<174 Then
                    AnimateNPC(n, 33, 174, 2.0, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 33, 174, 2.0, False)
                Else
                    PointEntity n\obj, Collider
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0							
                    
                    If n\Frame>33 Then 
                        ;SetAnimTime(n\obj,2)
                        n\Frame = 2
                        n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
                        PlaySound_Strict(n\Sound)
                    EndIf
                    AnimateNPC(n, 2, 32, 0.3, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 2, 32, 0.3, False)
                    
                    If n\Frame>=5 And n\Frame<6 Then
                        If dist < 1.8 Then
                            If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
                                If WearingHazmat Then
                                    Injuries = Injuries+Rnd(0.5)
                                    PlaySound_Strict(LoadTempSound("SFX\General\BodyFall.ogg"))
                                Else
                                    BlurTimer = 100
                                    Injuries = Injuries+Rnd(1.0,1.5)
                                    PlaySound_Strict DamageSFX(Rand(3,4))
                                    
                                    If Injuries > 3.0 Then 
                                        DeathMSG = Chr(34)+"We will need more than the regular cleaning team to care of this. "
                                        DeathMSG = DeathMSG + "Two large and highly active tentacle-like appendages seem "
                                        DeathMSG = DeathMSG + "to have formed inside the chamber. Their level of aggression is "
                                        DeathMSG = DeathMSG + "unlike anything we've seen before - it looks like they have "
                                        DeathMSG = DeathMSG + "beaten some unfortunate Class D to death at some point during the breach."+Chr(34)
                                        Kill()
                                    EndIf
                                EndIf
                                
                            EndIf
                        EndIf
                        
                        n\Frame = 6
                        ;SetAnimTime(n\obj, 6)
                    ElseIf n\Frame=32
                        n\State = 1
                        n\Frame = 173
                        ;SetAnimTime(n\obj, 173)
                    EndIf
                EndIf
                
        End Select
        
        PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
        RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider)-180, EntityRoll(n\Collider), True
        
        n\DropSpeed = 0
        
        ResetEntity n\Collider
        
    EndIf
End Function

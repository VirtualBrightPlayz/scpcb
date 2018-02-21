Function InitializeNPCtype096(n.NPCs)
    n\NVName = "SCP-096"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.3
    EntityType n\Collider, HIT_PLAYER
    n\obj = LoadAnimMesh_Strict("GFX\npcs\scp096.b3d")
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "speed") / 100.0)
    
    temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "scale") / 3.0)
    ScaleEntity n\obj, temp, temp, temp	
    
    MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
End Function

Function UpdateNPCtype096(n.NPCs)
    dist = EntityDistance(mainPlayer\collider, n\Collider)
    
    Select n\State
        Case 0
            If dist<8.0 Then
                GiveAchievement(Achv096)
                If n\Sound = 0 Then
                    n\Sound = LoadSound_Strict("SFX\Music\096.ogg")
                Else
                    n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider, 8.0, 1.0)
                EndIf
                
                AnimateNPC(n, 1085,1412, 0.1) ;sitting
                ;Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.1) ;sitting
                
                angle = WrapAngle(DeltaYaw(n\Collider, mainPlayer\collider));-EntityYaw(n\Collider,True))
                
                If (Not NoTarget)
                    If angle<90 Or angle>270 Then
                        CameraProject mainPlayer\cam,EntityX(n\Collider), EntityY(n\Collider)+0.25, EntityZ(n\Collider)
                        
                        If ProjectedX()>0 And ProjectedX()<userOptions\screenWidth Then
                            If ProjectedY()>0 And ProjectedY()<userOptions\screenHeight Then
                                If EntityVisible(mainPlayer\collider, n\Collider) Then
                                    If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6)
                                        PlaySound_Strict LoadTempSound("SFX\SCP\096\Triggered.ogg")
                                        
                                        CurrCameraZoom = 10
                                        
                                        n\Frame = 307
                                        ;SetAnimTime n\obj, 307
                                        StopChannel n\SoundChn
                                        FreeSound_Strict n\Sound
                                        n\Sound = 0
                                        n\State = 1
                                    EndIf
                                EndIf									
                            EndIf
                        EndIf								
                        
                    EndIf
                EndIf
                
            EndIf
        Case 4
            CurrCameraZoom = CurveValue(Max(CurrCameraZoom, (Sin(Float(MilliSecs2())/20.0)+1.0) * 10.0),CurrCameraZoom,8.0)
            
            If n\Target = Null Then 
                If n\Sound = 0 Then
                    n\Sound = LoadSound_Strict("SFX\SCP\096\Scream.ogg")
                Else
                    n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider, 7.5, 1.0)
                EndIf
                
                If n\Sound2 = 0 Then
                    n\Sound2 = LoadSound_Strict("SFX\Music\096Chase.ogg")
                Else
                    If n\SoundChn2 = 0 Then
                        n\SoundChn2 = PlaySound_Strict (n\Sound2)
                    Else
                        If (Not ChannelPlaying(n\SoundChn2)) Then n\SoundChn2 = PlaySound_Strict(n\Sound2)
                        ChannelVolume(n\SoundChn2, Min(Max(8.0-dist,0.6),1.0)*userOptions\soundVolume)
                    EndIf
                EndIf
            EndIf
            
            If NoTarget And n\Target = Null Then n\State = 5
            
            If KillTimer =>0 Then
                
                If MilliSecs2() > n\State3 Then
                    n\LastSeen=0
                    If n\Target=Null Then
                        If EntityVisible(mainPlayer\collider, n\Collider) Then n\LastSeen=1
                    Else
                        If EntityVisible(n\Target\Collider, n\Collider) Then n\LastSeen=1
                    EndIf
                    n\State3=MilliSecs2()+3000
                EndIf
                
                If n\LastSeen=1 Then
                    n\PathTimer=Max(70*3, n\PathTimer)
                    n\PathStatus=0
                    
                    If n\Target<> Null Then dist = EntityDistance(n\Target\Collider, n\Collider)
                    
                    If dist < 2.8 Or n\Frame<150 Then 
                        If n\Frame>193 Then n\Frame = 2.0 ;go to the start of the jump animation
                        
                        AnimateNPC(n, 2, 193, 0.7)
                        ;Animate2(n\obj, AnimTime(n\obj), 2, 193, 0.7)
                        
                        If dist > 1.0 Then 
                            n\CurrSpeed = CurveValue(n\Speed*2.0,n\CurrSpeed,15.0)
                        Else
                            n\CurrSpeed = 0
                            
                            If n\Target=Null Then
                                If (Not GodMode) Then 
                                    PlaySound_Strict DamageSFX(4)
                                    
                                    pvt = CreatePivot()
                                    mainPlayer\camShake = 30
                                    BlurTimer = 2000
                                    DeathMSG = "A large amount of blood found in [DATA REDACTED]. DNA indentified as Subject D-9341. Most likely [DATA REDACTED] by SCP-096."
                                    Kill()
                                    KillAnim = 1
                                    For i = 0 To 6
                                        PositionEntity pvt, EntityX(mainPlayer\collider)+Rnd(-0.1,0.1),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)+Rnd(-0.1,0.1)
                                        TurnEntity pvt, 90, 0, 0
                                        EntityPick(pvt,0.3)
                                        
                                        de.Decals = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
                                        de\Size = Rnd(0.2,0.6) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
                                    Next
                                    FreeEntity pvt
                                EndIf
                            EndIf				
                        EndIf
                        
                        If n\Target=Null Then
                            PointEntity n\Collider, mainPlayer\collider
                        Else
                            PointEntity n\Collider, n\Target\Collider
                        EndIf
                        
                    Else
                        If n\Target=Null Then 
                            PointEntity n\obj, mainPlayer\collider
                        Else
                            PointEntity n\obj, n\Target\Collider
                        EndIf
                        
                        RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0
                        
                        If n\Frame>1000 Then n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,20.0)
                        
                        If n\Frame<1058 Then
                            AnimateNPC(n, 892, 1058, n\Speed*5, False)	
                            ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*5, False)	
                        Else
                            AnimateNPC(n, 1059, 1074, n\CurrSpeed*5)	
                            ;Animate2(n\obj, AnimTime(n\obj),1059,1074, n\CurrSpeed*5)	
                        EndIf
                    EndIf
                    
                    RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True
                    MoveEntity n\Collider, 0,0,n\CurrSpeed
                    
                Else
                    If n\PathStatus = 1 Then
                        
                        If n\Path[n\PathLocation]=Null Then 
                            If n\PathLocation > 19 Then 
                                n\PathLocation = 0 : n\PathStatus = 0
                            Else
                                n\PathLocation = n\PathLocation + 1
                            EndIf
                        Else
                            PointEntity n\obj, n\Path[n\PathLocation]\obj
                            
                            RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0
                            
                            If n\Frame>1000 Then n\CurrSpeed = CurveValue(n\Speed*1.5,n\CurrSpeed,15.0)
                            MoveEntity n\Collider, 0,0,n\CurrSpeed
                            
                            If n\Frame<1058 Then
                                AnimateNPC(n, 892,1058, n\Speed*8, False)
                                ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*8, False)
                            Else
                                AnimateNPC(n, 1059,1084, n\CurrSpeed*8)	
                                ;Animate2(n\obj, AnimTime(n\obj),1059,1084, n\CurrSpeed*8)	
                            EndIf
                            
                            dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                            If dist2 < 0.4 Then
                                If n\Path[n\PathLocation]\door <> Null Then
                                    If n\Path[n\PathLocation]\door\open = False Then
                                        n\Path[n\PathLocation]\door\open = True
                                        n\Path[n\PathLocation]\door\fastopen = 1
                                        PlaySound2(OpenDoorFastSFX, mainPlayer\cam, n\Path[n\PathLocation]\door\obj)
                                    EndIf
                                EndIf							
                                If dist2 < 0.2 Then n\PathLocation = n\PathLocation + 1
                            EndIf 
                        EndIf
                        
                    Else
                        AnimateNPC(n, 892,972, 0.2)
                        ;Animate2(n\obj, AnimTime(n\obj),892,972, 0.2)
                        
                        n\PathTimer = Max(0, n\PathTimer-FPSfactor)
                        If n\PathTimer=<0 Then
                            If n\Target<>Null Then
                                n\PathStatus = FindPath(n, EntityX(n\Target\Collider),EntityY(n\Target\Collider)+0.2,EntityZ(n\Target\Collider))	
                            Else
                                n\PathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))	
                            EndIf
                            n\PathTimer = 70*5
                        EndIf
                    EndIf
                EndIf
                
                If dist > 32.0 Or EntityY(n\Collider)<-50 Then
                    If Rand(50)=1 Then TeleportCloser(n)
                EndIf
            Else ;play the eating animation if killtimer < 0 
                AnimateNPC(n, Min(27,AnimTime(n\obj)), 193, 0.5)
                
                ;Animate2(n\obj, AnimTime(n\obj), Min(27,AnimTime(n\obj)), 193, 0.5)
            EndIf
            
            
        Case 1,2,3
            If n\Sound = 0 Then
                n\Sound = LoadSound_Strict("SFX\Music\096Angered.ogg")
            Else
                n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider, 10.0, 1.0)
            EndIf
            
            If n\State=1 Then ; get up
                If n\Frame>1085 Then
                    
                    AnimateNPC(n, 1085, 1412, 0.3,False)
                    If n\Frame> 1411.9 Then n\Frame = 307
                    
                    ;Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.3,False)
                    ;If AnimTime(n\obj)=1412 Then SetAnimTime(n\obj, 307)
                Else
                    AnimateNPC(n, 307, 424, 0.3, False)
                    If n\Frame > 423.9 Then n\State = 2 : n\Frame = 892							
                    
                    ;Animate2(n\obj, AnimTime(n\obj),307,424, 0.3, False)
                    ;If AnimTime(n\obj)=424 Then n\State = 2 : SetAnimTime(n\obj, 892)								
                EndIf
            ElseIf n\State=2
                AnimateNPC(n, 833, 972, 0.3, False)
                
                ;Animate2(n\obj, AnimTime(n\obj),833,972, 0.3, False)
                If n\Frame=>972 Then n\State = 3 : n\State2=0
            ElseIf n\State=3
                n\State2 = n\State2+FPSfactor
                If n\State2 > 70*18 Then 
                    AnimateNPC(n, 973, 1001, 0.5, False)
                    ;Animate2(n\obj, AnimTime(n\obj),973,1001, 0.5, False)
                    If n\Frame>1000.9 Then 
                        n\State = 4
                        StopChannel n\SoundChn
                        FreeSound_Strict n\Sound : n\Sound = 0
                    EndIf
                Else
                    AnimateNPC(n, 892,978, 0.3)
                    ;Animate2(n\obj, AnimTime(n\obj),892,978, 0.3)
                EndIf
            EndIf
        Case 5
            If dist < 16.0 Then 
            
                If dist < 4.0 Then
                    GiveAchievement(Achv096)
                EndIf
                    
                If n\Sound = 0 Then
                    n\Sound = LoadSound_Strict("SFX\Music\096.ogg")
                Else
                    n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, n\Collider, 14.0, 1.0)
                EndIf
                
                n\State2=n\State2+FPSfactor
                If n\State2>1000 Then ;walking around
                    If n\State2>1600 Then n\State2=Rand(0,500) : n\Frame = 1652 ;: SetAnimTime(n\obj, 1652)
                    
                    If n\Frame<1652 Then ;idle to walk
                        n\CurrSpeed = CurveValue(n\Speed*0.1,n\CurrSpeed,5.0)
                        AnimateNPC(n, 1638,1652, n\CurrSpeed*45,False)
                        ;Animate2(n\obj, AnimTime(n\obj),1638,1652, n\CurrSpeed*45,False)
                    Else
                        n\CurrSpeed = CurveValue(n\Speed*0.1,n\CurrSpeed,5.0)
                        AnimateNPC(n, 1653,1724, n\CurrSpeed*45) ;walk
                        ;Animate2(n\obj, AnimTime(n\obj),1653,1724, n\CurrSpeed*45) ;walk
                    EndIf
                    
                    If MilliSecs2() > n\State3 Then
                        n\LastSeen=0
                        If EntityVisible(mainPlayer\collider, n\Collider) Then 
                            n\LastSeen=1
                        Else
                            HideEntity n\Collider
                            EntityPick(n\Collider, 1.5)
                            If PickedEntity() <> 0 Then
                                n\Angle = EntityYaw(n\Collider)+Rnd(80,110)
                            EndIf
                            ShowEntity n\Collider
                        EndIf
                        n\State3=MilliSecs2()+3000
                    EndIf
                    
                    If n\LastSeen Then 
                        PointEntity n\obj, mainPlayer\collider
                        RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),130.0),0
                        If dist < 1.5 Then n\State2=0
                    Else
                        RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),50.0),0
                    EndIf
                Else
                    If n\Frame>1638 Then ;walk to idle
                        n\CurrSpeed = CurveValue(n\Speed*0.05,n\CurrSpeed,8.0)	
                        AnimateNPC(n, 1652, 1638, -n\CurrSpeed*45,False)
                        ;Animate2(n\obj, AnimTime(n\obj),1652,1638, -n\CurrSpeed*45,False)
                        
                    Else ;idle
                        n\CurrSpeed = CurveValue(0,n\CurrSpeed,4.0)	
                        AnimateNPC(n, 585, 633, 0.2) ;idle
                        ;Animate2(n\obj, AnimTime(n\obj),585,633, 0.2) ;idle
                    EndIf
                EndIf
                
                angle = WrapAngle(DeltaYaw(n\Collider, mainPlayer\cam));-EntityYaw(n\Collider))
                If (Not NoTarget)
                    If angle<55 Or angle>360-55 Then
                        CameraProject mainPlayer\cam,EntityX(n\Collider), EntityY(mainPlayer\collider)+5.8*0.2-0.25, EntityZ(n\Collider)
                        
                        If ProjectedX()>0 And ProjectedX()<userOptions\screenWidth Then
                            If ProjectedY()>0 And ProjectedY()<userOptions\screenHeight Then
                                If EntityVisible(mainPlayer\collider, n\Collider) Then
                                    If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6)
                                        PlaySound_Strict LoadTempSound("SFX\SCP\096\Triggered.ogg")
                                        
                                        CurrCameraZoom = 10
                                        
                                        n\Frame = 833
                                        ;SetAnimTime n\obj, 833
                                        StopChannel n\SoundChn
                                        FreeSound_Strict n\Sound
                                        n\Sound = 0
                                        n\State = 2
                                    EndIf
                                EndIf									
                            EndIf
                        EndIf
                        
                    EndIf
                EndIf
                
                MoveEntity n\Collider, 0,0,n\CurrSpeed
            EndIf
    End Select
    
    ;ResetEntity(n\Collider)
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.07, EntityZ(n\Collider))
    
    RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), 0
End Function

Function InitializeNPCtype096(n.NPCs)
    n\nvName = "SCP-096"
    n\collider = CreatePivot()
    EntityRadius n\collider, 0.3
    EntityType n\collider, HIT_PLAYER
    n\obj = LoadAnimMesh("GFX\npcs\scp096.b3d")
	n\sounds[2] = LoadSound("SFX\Door\DoorOpenFast.ogg")
    
    n\speed = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "speed") / 100.0)
    
    Local temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "scale") / 3.0)
    ScaleEntity n\obj, temp, temp, temp	
    
    MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
End Function

Function UpdateNPCtype096(n.NPCs)
	Local angle%, i%, pvt%, de.Decals
	
    Select n\state
        Case 0
            If (n\playerDistance < 8.0) Then
                If n\sounds[0] = 0 Then
                    n\sounds[0] = LoadSound("SFX\Music\096.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 8.0, 1.0)
                EndIf
                
                AnimateNPC(n, 1085,1412, 0.1) ;sitting
                ;Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.1) ;sitting
                
                angle = WrapAngle(DeltaYaw(n\collider, mainPlayer\collider));-EntityYaw(n\Collider,True))
                
                If (Not NoTarget)
                    If angle<90 Or angle>270 Then
                        CameraProject mainPlayer\cam,EntityX(n\collider), EntityY(n\collider)+0.25, EntityZ(n\collider)
                        
                        If ProjectedX()>0 And ProjectedX()<userOptions\screenWidth Then
                            If ProjectedY()>0 And ProjectedY()<userOptions\screenHeight Then
                                If EntityVisible(mainPlayer\collider, n\collider) Then
                                    If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6)
                                        PlaySound2 LoadTempSound("SFX\SCP\096\Triggered.ogg")
                                        
                                        mainPlayer\camZoom = 10
                                        
                                        n\frame = 307
                                        ;SetAnimTime n\obj, 307
                                        StopChannel n\soundChannels[0]
                                        FreeSound n\sounds[0]
                                        n\sounds[0] = 0
                                        n\state = 1
                                    EndIf
                                EndIf									
                            EndIf
                        EndIf								
                        
                    EndIf
                EndIf
                
            EndIf
        Case 4
            mainPlayer\camZoom = CurveValue(Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0) * 10.0),mainPlayer\camZoom,8.0)
            
            If n\target = Null Then 
                If n\sounds[0] = 0 Then
                    n\sounds[0] = LoadSound("SFX\SCP\096\Scream.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 7.5, 1.0)
                EndIf
                
                If n\sounds[1] = 0 Then
                    n\sounds[1] = LoadSound("SFX\Music\096Chase.ogg")
                Else
                    If n\soundChannels[1] = 0 Then
                        n\soundChannels[1] = PlaySound(n\sounds[1])
                    Else
                        If (Not IsChannelPlaying(n\soundChannels[1])) Then n\soundChannels[1] = PlaySound(n\sounds[1])
                        ChannelVolume(n\soundChannels[1], Min(Max(8.0-n\playerDistance,0.6),1.0)*userOptions\soundVolume)
                    EndIf
                EndIf
            EndIf
            
            If NoTarget And n\target = Null Then n\state = 5
            
            If (Not mainPlayer\dead) Then
                
                If TimeInPosMilliSecs() > n\state3 Then
                    n\lastSeen=0
                    If n\target=Null Then
                        If EntityVisible(mainPlayer\collider, n\collider) Then n\lastSeen=1
                    Else
                        If EntityVisible(n\target\collider, n\collider) Then n\lastSeen=1
                    EndIf
                    n\state3=TimeInPosMilliSecs()+3000
                EndIf
                
                If n\lastSeen=1 Then
                    n\pathTimer=Max(70*3, n\pathTimer)
                    n\pathStatus=0
                    
                    If n\playerDistance < 2.8 Or n\frame<150 Then 
                        If n\frame>193 Then n\frame = 2.0 ;go to the start of the jump animation
                        
                        AnimateNPC(n, 2, 193, 0.7)
                        ;Animate2(n\obj, AnimTime(n\obj), 2, 193, 0.7)
                        
                        If n\playerDistance > 1.0 Then 
                            n\currSpeed = CurveValue(n\speed*2.0,n\currSpeed,15.0)
                        Else
                            n\currSpeed = 0
                            
                            If n\target=Null Then
                                If (Not mainPlayer\godMode) Then 
                                    PlaySound2 DamageSFX(4)
                                    
                                    pvt = CreatePivot()
                                    mainPlayer\camShake = 30
                                    mainPlayer\blurTimer = 2000
                                    DeathMSG = "A large amount of blood found in [DATA REDACTED]. DNA indentified as Subject D-9341. Most likely [DATA REDACTED] by SCP-096."
                                    Kill(mainPlayer)
                                    ;KillAnim = 1 ;TODO: idk
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
                        
                        If n\target=Null Then
                            PointEntity n\collider, mainPlayer\collider
                        Else
                            PointEntity n\collider, n\target\collider
                        EndIf
                        
                    Else
                        If n\target=Null Then 
                            PointEntity n\obj, mainPlayer\collider
                        Else
                            PointEntity n\obj, n\target\collider
                        EndIf
                        
                        RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 5.0), 0
                        
                        If n\frame>1000 Then n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
                        
                        If n\frame<1058 Then
                            AnimateNPC(n, 892, 1058, n\speed*5, False)	
                            ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*5, False)	
                        Else
                            AnimateNPC(n, 1059, 1074, n\currSpeed*5)	
                            ;Animate2(n\obj, AnimTime(n\obj),1059,1074, n\CurrSpeed*5)	
                        EndIf
                    EndIf
                    
                    RotateEntity n\collider, 0, EntityYaw(n\collider), 0, True
                    MoveEntity n\collider, 0,0,n\currSpeed
                    
                Else
                    If n\pathStatus = 1 Then
                        
                        If n\path[n\pathLocation]=Null Then 
                            If n\pathLocation > 19 Then 
                                n\pathLocation = 0 : n\pathStatus = 0
                            Else
                                n\pathLocation = n\pathLocation + 1
                            EndIf
                        Else
                            PointEntity n\obj, n\path[n\pathLocation]\obj
                            
                            RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 5.0), 0
                            
                            If n\frame>1000 Then n\currSpeed = CurveValue(n\speed*1.5,n\currSpeed,15.0)
                            MoveEntity n\collider, 0,0,n\currSpeed
                            
                            If n\frame<1058 Then
                                AnimateNPC(n, 892,1058, n\speed*8, False)
                                ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*8, False)
                            Else
                                AnimateNPC(n, 1059,1084, n\currSpeed*8)	
                                ;Animate2(n\obj, AnimTime(n\obj),1059,1084, n\CurrSpeed*8)	
                            EndIf
                            
                            dist2# = EntityDistance(n\collider,n\path[n\pathLocation]\obj)
                            If dist2 < 0.4 Then
								;TODO: fix
;                                If n\Path[n\PathLocation]\door <> Null Then
;									If n\Path[n\PathLocation]\door\open = False Then
;										n\Path[n\PathLocation]\door\open = True
;                                        n\Path[n\PathLocation]\door\fastopen = 1
;                                        PlayRangedSound(n\sounds[2], mainPlayer\cam, n\path[n\pathLocation]\door\obj)
;                                    EndIf
;                                EndIf							
                                If dist2 < 0.2 Then n\PathLocation = n\PathLocation + 1

                            EndIf 
                        EndIf
                        
                    Else
                        AnimateNPC(n, 892,972, 0.2)
                        ;Animate2(n\obj, AnimTime(n\obj),892,972, 0.2)
                        
                        n\pathTimer = Max(0, n\pathTimer-timing\tickDuration)
                        If n\pathTimer=<0 Then
                            If n\target<>Null Then
                                n\pathStatus = FindPath(n, EntityX(n\target\collider),EntityY(n\target\collider)+0.2,EntityZ(n\target\collider))	
                            Else
                                n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))	
                            EndIf
                            n\pathTimer = 70*5
                        EndIf
                    EndIf
                EndIf
                
                If n\playerDistance > 32.0 Or EntityY(n\collider)<-50 Then
                    If Rand(50)=1 Then TeleportCloser(n)
                EndIf
            Else ;play the eating animation if killtimer < 0 
                AnimateNPC(n, Min(27,AnimTime(n\obj)), 193, 0.5)
                
                ;Animate2(n\obj, AnimTime(n\obj), Min(27,AnimTime(n\obj)), 193, 0.5)
            EndIf
            
            
        Case 1,2,3
            If n\sounds[0] = 0 Then
                n\sounds[0] = LoadSound("SFX\Music\096Angered.ogg")
            Else
                n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\Collider, 10.0, 1.0)
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
                n\State2 = n\State2+timing\tickDuration
                If n\State2 > 70*18 Then 
                    AnimateNPC(n, 973, 1001, 0.5, False)
                    ;Animate2(n\obj, AnimTime(n\obj),973,1001, 0.5, False)
                    If n\Frame>1000.9 Then 
                        n\State = 4
                        StopChannel n\soundChannels[0]
                        FreeSound n\sounds[0] : n\sounds[0] = 0
                    EndIf
                Else
                    AnimateNPC(n, 892,978, 0.3)
                    ;Animate2(n\obj, AnimTime(n\obj),892,978, 0.3)
                EndIf
            EndIf
        Case 5
            If dist < 16.0 Then 
            
                If n\sounds[0] = 0 Then
                    n\sounds[0] = LoadSound("SFX\Music\096.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\Collider, 14.0, 1.0)
                EndIf
                
                n\State2=n\State2+timing\tickDuration
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
                    
                    If TimeInPosMilliSecs() > n\State3 Then
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
                        n\State3=TimeInPosMilliSecs()+3000
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
                                        PlaySound2 LoadTempSound("SFX\SCP\096\Triggered.ogg")
                                        
                                        mainPlayer\camZoom = 10
                                        
                                        n\Frame = 833
                                        ;SetAnimTime n\obj, 833
                                        StopChannel n\soundChannels[0]
                                        FreeSound n\sounds[0]
                                        n\sounds[0] = 0
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
;~IDEal Editor Parameters:
;~C#Blitz3D
Function InitializeNPCtypeApache(n.NPCs)
    n\nvName = "Human"
    n\gravityMult = 0.0
    n\maxGravity = 0.0
    n\collider = CreatePivot()
    EntityRadius n\collider, 0.2
    n\obj = CopyEntity(ApacheObj);LoadAnimMesh("GFX\apache.b3d")
    
    n\obj2 = CopyEntity(ApacheRotorObj);LoadAnimMesh("GFX\apacherotor.b3d",n\obj)
    EntityParent n\obj2,n\obj
	
	n\sounds[0] = LoadSound("SFX\Character\Apache\Propeller.ogg")
    
    For i = -1 To 1 Step 2
        Local rotor2 = CopyEntity(n\obj2,n\obj2)
        RotateEntity rotor2,0,4.0*i,0
        EntityAlpha rotor2, 0.5
    Next
    
    n\obj3 = LoadAnimMesh("GFX\apacherotor2.b3d",n\obj)
    PositionEntity n\obj3, 0.0, 2.15, -5.48
    
    EntityType n\collider, HIT_APACHE
    EntityRadius n\collider, 3.0
    
    For i = -1 To 1 Step 2
        Local Light1 = CreateLight(2,n\obj)
        ;room\LightDist[i] = range
        LightRange(Light1,2.0)
        LightColor(Light1,255,255,255)
        PositionEntity(Light1, 1.65*i, 1.17, -0.25)
        
        Local lightsprite = CreateSprite(n\obj)
        PositionEntity(lightsprite, 1.65*i, 1.17, 0, -0.25)
        ScaleSprite(lightsprite, 0.13, 0.13)
        EntityTexture(lightsprite, LightSpriteTex(0))
        EntityBlend (lightsprite, 3)
        EntityFX lightsprite, 1+8
    Next
    
    temp# = 0.6
    ScaleEntity n\obj, temp, temp, temp
End Function

Function UpdateNPCtypeApache(n.NPCs)
    Local dist# = EntityDistance(mainPlayer\collider, n\collider)
    If dist<60.0 Then 
        If mainPlayer\currRoom\RoomTemplate\Name = "exit1" Then 
            dist2 = Max(Min(EntityDistance(n\collider, mainPlayer\currRoom\Objects[3])/(8000.0*RoomScale),1.0),0.0)
        Else 
            dist2 = 1.0
        EndIf
        
        n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 25.0, dist2)
    EndIf
    
    n\dropSpeed = 0
    
    Select n\state
        Case 0,1
            TurnEntity(n\obj2,0,20.0*timing\tickDuration,0)
            TurnEntity(n\obj3,20.0*timing\tickDuration,0,0)
            
            If n\state=1 And (Not NoTarget) Then
                If Abs(EntityX(mainPlayer\collider)-EntityX(n\collider))< 30.0 Then
                    If Abs(EntityZ(mainPlayer\collider)-EntityZ(n\collider))<30.0 Then
                        If Abs(EntityY(mainPlayer\collider)-EntityY(n\collider))<20.0 Then
                            If Rand(20)=1 Then 
                                If EntityVisible(mainPlayer\collider, n\collider) Then
                                    n\state = 2
                                    PlayRangedSound(AlarmSFX(2), mainPlayer\cam, n\collider, 50, 1.0)
                                EndIf
                            EndIf									
                        EndIf
                    EndIf
                EndIf							
            EndIf
        Case 2,3 ;player located -> attack
            
            If n\state = 2 Then 
                target = mainPlayer\collider
            ElseIf n\state = 3
                target=CreatePivot()
                PositionEntity target, n\EnemyX, n\EnemyY, n\EnemyZ, True
            EndIf
            
            If NoTarget And n\state = 2 Then n\state = 1
            
            TurnEntity(n\obj2,0,20.0*timing\tickDuration,0)
            TurnEntity(n\obj3,20.0*timing\tickDuration,0,0)
            
            If Abs(EntityX(target)-EntityX(n\collider)) < 55.0 Then
                If Abs(EntityZ(target)-EntityZ(n\collider)) < 55.0 Then
                    If Abs(EntityY(target)-EntityY(n\collider))< 20.0 Then
                        PointEntity n\obj, target
                        RotateEntity n\collider, CurveAngle(Min(WrapAngle(EntityPitch(n\obj)),40.0),EntityPitch(n\collider),40.0), CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),90.0), EntityRoll(n\collider), True
                        PositionEntity(n\collider, EntityX(n\collider), CurveValue(EntityY(target)+8.0,EntityY(n\collider),70.0), EntityZ(n\collider))
                        
                        dist# = Distance(EntityX(target),EntityZ(target),EntityX(n\collider),EntityZ(n\collider))
                        
                        n\currSpeed = CurveValue(Min(dist-6.5,6.5)*0.008, n\currSpeed, 50.0)
                        
                        ;If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(n\collider),EntityZ(n\collider)) > 6.5 Then
                        ;	n\currspeed = CurveValue(0.08,n\currspeed,50.0)
                        ;Else
                        ;	n\currspeed = CurveValue(0.0,n\currspeed,30.0)
                        ;EndIf
                        MoveEntity n\collider, 0,0,n\currSpeed*timing\tickDuration
                        
                        
                        If n\pathTimer = 0 Then
                            n\pathStatus = EntityVisible(n\collider,target)
                            n\pathTimer = Rand(100,200)
                        Else
                            n\pathTimer = Min(n\pathTimer-timing\tickDuration,0.0)
                        EndIf
                        
                        If n\pathStatus = 1 Then ;player visible
                            RotateEntity n\collider, EntityPitch(n\collider), EntityYaw(n\collider), CurveAngle(0, EntityRoll(n\collider),40), True
                            
                            If n\Reload =< 0 Then
                                If dist<20.0 Then
                                    pvt = CreatePivot()
                                    
                                    PositionEntity pvt, EntityX(n\collider),EntityY(n\collider), EntityZ(n\collider)
                                    RotateEntity pvt, EntityPitch(n\collider), EntityYaw(n\collider),EntityRoll(n\collider)
                                    MoveEntity pvt, 0, 8.87*(0.21/9.0), 8.87*(1.7/9.0) ;2.3
                                    PointEntity pvt, target
                                    
                                    If WrapAngle(EntityYaw(pvt)-EntityYaw(n\collider))<10 Then
                                        PlayRangedSound_SM(sndManager\gunshot[1], mainPlayer\cam, n\collider, 20)
                                        
                                        DeathMSG = Chr(34)+"CH-2 to control. Shot down a runaway Class D at Gate B."+Chr(34)
                                        
                                        Shoot( EntityX(pvt),EntityY(pvt), EntityZ(pvt),((10/dist)*(1/dist))*(n\state=2),(n\state=2))
                                        
                                        n\Reload = 5
                                    EndIf
                                    
                                    FreeEntity pvt
                                EndIf
                            EndIf
                        Else 
                            RotateEntity n\collider, EntityPitch(n\collider), EntityYaw(n\collider), CurveAngle(-20, EntityRoll(n\collider),40), True
                        EndIf
                        MoveEntity n\collider, -EntityRoll(n\collider)*0.002,0,0
                        
                        n\Reload=n\Reload-timing\tickDuration
                        
                        
                    EndIf
                EndIf
            EndIf		
            
            If n\state = 3 Then FreeEntity target
        Case 4 ;crash
            If n\state2 < 300 Then
                
                TurnEntity(n\obj2,0,20.0*timing\tickDuration,0)
                TurnEntity(n\obj3,20.0*timing\tickDuration,0,0)
                
                TurnEntity n\collider,0,-timing\tickDuration*7,0;Sin(TimeInPosMilliSecs()/40)*timing\tickDuration
                n\state2=n\state2+timing\tickDuration*0.3
                
                target=CreatePivot()
                PositionEntity target, n\EnemyX, n\EnemyY, n\EnemyZ, True
                
                PointEntity n\obj, target
                MoveEntity n\obj, 0,0,timing\tickDuration*0.001*n\state2
                PositionEntity(n\collider, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                
                If EntityDistance(n\obj, target) <0.3 Then
                    If TempSound2 <> 0 Then FreeSound TempSound2 : TempSound2 = 0
                    TempSound2 = LoadSound("SFX\Character\Apache\Crash"+Rand(1,2)+".ogg")
                    mainPlayer\camShake = Max(mainPlayer\camShake, 3.0)
                    PlaySound2 TempSound2
                    n\state = 5
                EndIf
                
                FreeEntity target
            EndIf
    End Select
    
    PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider), EntityZ(n\collider))
    RotateEntity n\obj, EntityPitch(n\collider), EntityYaw(n\collider), EntityRoll(n\collider), True
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
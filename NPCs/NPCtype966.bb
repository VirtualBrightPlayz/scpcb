Function InitializeNPCtype966(n.NPCs)
    i = 1
    For n2.NPCs = Each NPCs
        If (n\NPCtype = n2\NPCtype) And (n<>n2) Then i=i+1
    Next
    n\NVName = "SCP-966-"+i
    
    n\Collider = CreatePivot()
    EntityRadius n\Collider,0.2
    
    For n2.NPCs = Each NPCs
        If (n\NPCtype = n2\NPCtype) And (n<>n2) Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\npcs\scp-966.b3d")
    EndIf
    
    EntityFX n\obj,1
    
    temp# = GetINIFloat("DATA\NPCs.ini", "SCP-966", "scale")/40.0
    ScaleEntity n\obj, temp, temp, temp		
    
    ;EntityColor n\obj,Rnd(0,50),0,Rnd(50,100)
    
    SetAnimTime n\obj,15.0
    
    EntityType n\Collider,HIT_PLAYER
    
    n\Speed = 0.02
End Function

Function UpdateNPCtype966(n.NPCs)
    dist = EntityDistance(n\Collider,mainPlayer\collider)
    
    If (dist<HideDistance) Then
        
    ;n\state = the "general" state (idle/wander/attack/echo etc)
    ;n\state2 = timer for doing raycasts
        
        prevFrame = n\Frame
        
        If n\Sound > 0 Then
            temp = 0.5
        ;the ambient sound gets louder when the npcs are attacking
            If n\State > 0 Then temp = 1.0	
            
            n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, mainPlayer\cam, 10.0,temp)
        EndIf
        
        temp = Rnd(-1.0,1.0)
        PositionEntity n\obj,EntityX(n\Collider,True),EntityY(n\Collider,True)-0.2,EntityZ(n\Collider,True)
        RotateEntity n\obj,-90.0,EntityYaw(n\Collider),0.0
        
        If (Not IsPlayerWearingTempName(mainPlayer,"nvgoggles")) Then
            HideEntity n\obj
            If dist<1 And n\Reload <= 0 And MsgTimer <= 0 Then
                Select Rand(6)
                    Case 1
                        Msg="You feel something breathing right next to you."
                    Case 2
                        Msg=Chr(34)+"It feels like something's in this room with me."+Chr(34)
                    Case 3
                        Msg="You feel like something is here with you, but you do not see anything."
                    Case 4
                        Msg=Chr(34)+"Is my mind playing tricks on me or is there someone else here?"+Chr(34)
                    Case 5
                        Msg="You feel like something is following you."
                    Case 6
                        Msg="You can feel something near you, but you are unable to see it. Perhaps its time is now."
                End Select
                n\Reload = 20*70
                MsgTimer=8*70
            EndIf
            n\Reload = n\Reload - timing\tickDuration
            
        Else
            ShowEntity n\obj
        EndIf
        
        If n\State3>5*70 Then
        ;n\State = 1
            If n\State3<1000.0 Then
                For n2.NPCs = Each NPCs	
                    If n2\NPCtype = n\NPCtype Then n2\State3=1000.0 
                Next
            EndIf
            
            n\State = Max(n\State,8)
            n\State3 = 1000.0					
            
        EndIf
        
        If mainPlayer\stamina<10 Then 
            n\State3=n\State3+timing\tickDuration
        Else If n\State3 < 900.0
            n\State3=Max(n\State3-timing\tickDuration*0.2,0.0)
        EndIf
        
        If n\State <> 10
            n\LastSeen = 0
        EndIf
        
        Select n\State
            Case 0 ;idle, standing
                If n\Frame>2300.0 Then
                    AnimateNPC(n, 2391, 2416, 1.0, False)	
                    If n\Frame>2415.0 Then SetNPCFrame(n, 201)
                Else
                    AnimateNPC(n, 201, 1015, 1.0, False)
                    
                ;echo/stare/walk around periodically
                    If n\Frame>1014.0 Then 
                        If Rand(3)=1 And dist<4 Then
                            n\State = Rand(1,4)
                        Else
                            n\State = Rand(5,6)								
                        EndIf
                    EndIf
                    
                ;echo if player gets close
                    If dist<2.0 Then 
                        n\State=Rand(1,4)
                    EndIf 							
                EndIf
                
                n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 10.0)
                
                MoveEntity n\Collider,0,0,n\CurrSpeed
                
            Case 1,2 ;echo
                If n\State=1 Then
                    AnimateNPC(n, 1015, 1180, 1.0, False)
                    If n\Frame > 1179.0 Then n\State = 0
                Else
                    AnimateNPC(n, 1180, 1379, 1.0, False)
                    If n\Frame > 1378.0 Then n\State = 0
                EndIf
                
                If n\Frame>1029.0 And prevFrame<=1029.0 Or n\Frame>1203.0 And prevFrame<=1203.0 Then
                    PlaySound2(LoadTempSound("SFX\SCP\966\Echo"+Rand(1,3)+".ogg"), mainPlayer\cam, n\Collider)
                EndIf
                
                angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
                RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),20.0),0.0
                
                If n\State3<900 Then
                    mainPlayer\blurTimer = ((Sin(TimeInPosMilliSecs()/50)+1.0)*200)/dist
                    
                    If dist<16 Then
                        mainPlayer\blinkEffect = Max(mainPlayer\blinkEffect, 1.5)
                        ;BlinkEffectTimer = 1000
                        
                        mainPlayer\staminaEffect = 2.0
                        ;StaminaEffectTimer = 1000
                        
                        If MsgTimer<=0 And mainPlayer\staminaEffect<1.5 Then
                            Select Rand(4)
                                Case 1
                                    Msg = "You feel exhausted."
                                Case 2
                                    Msg = Chr(34)+"Could really go for a nap now..."+Chr(34)
                                Case 3
                                    Msg = Chr(34)+"If I wasn't in this situation I would take a nap somewhere."+Chr(34)
                                Case 4
                                    Msg = "You feel restless."
                            End Select

                            MsgTimer = 7*70
                        EndIf
                    EndIf							
                EndIf
                
            Case 3,4 ;stare at player
                If n\State=3 Then
                    AnimateNPC(n, 1379.0, 1692.0, 1.0, False)
                    
                    If n\Frame>1691.0 Then n\State = 0
                Else
                    AnimateNPC(n, 1692.0, 2156.0, 1.0, False)
                    
                    If n\Frame>2155.0 Then n\State = 0
                EndIf
                
                If n\Frame>1393.0 And prevFrame<=1393.0 Or n\Frame>1589.0 And prevFrame<=1589.0 Or n\Frame>2000.0 And prevFrame<=2000.0 Then
                    PlaySound2(LoadTempSound("SFX\SCP\966\Idle"+Rand(1,3)+".ogg"), mainPlayer\cam, n\Collider)
                EndIf
                
                angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
                RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),20.0),0.0
            Case 5,6,8 ;walking or chasing
                If n\Frame<2343.0 Then ;start walking
                    AnimateNPC(n, 2319, 2343, 0.5, False)
                Else
                    AnimateNPC(n, 2343, 2391, n\CurrSpeed*25.0)
                    
                ;chasing the player
                    If n\State = 8 And dist<32 Then
                        If n\PathTimer <= 0 Then
                            n\PathStatus = FindPath (n, EntityX(mainPlayer\collider,True), EntityY(mainPlayer\collider,True), EntityZ(mainPlayer\collider,True))
                            n\PathTimer = 40*10
                            n\CurrSpeed = 0
                        EndIf
                        n\PathTimer = Max(n\PathTimer-timing\tickDuration,0)
                        
                        If (Not EntityVisible(n\Collider,mainPlayer\collider)) Then
                            If n\PathStatus = 2 Then
                                n\CurrSpeed = 0
                                SetNPCFrame(n,201)
                            ;SetAnimTime n\obj,15
                            ElseIf n\PathStatus = 1
                                If n\Path[n\PathLocation]=Null Then 
                                    If n\PathLocation > 19 Then 
                                        n\PathLocation = 0 : n\PathStatus = 0
                                    Else
                                        n\PathLocation = n\PathLocation + 1
                                    EndIf
                                Else
                                    n\Angle = VectorYaw(EntityX(n\Path[n\PathLocation]\obj,True)-EntityX(n\Collider),0,EntityZ(n\Path[n\PathLocation]\obj,True)-EntityZ(n\Collider))
                    ;RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),10.0),0.0
                                    
                                    dist2 = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                                    
                                    If dist2 < 0.8 Then 
                                        If n\Path[n\PathLocation]\door<>Null Then
                                            If (Not n\Path[n\PathLocation]\door\open) Then UseDoor(n\Path[n\PathLocation]\door,False)
                                        EndIf
                                        If dist < 0.2 Then n\PathLocation = n\PathLocation + 1
                                    EndIf
                                    
                                EndIf
                            ElseIf n\PathStatus = 0
                                n\CurrSpeed = CurveValue(0,n\CurrSpeed,10.0)
                            EndIf
                        Else
                            n\Angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
                            
                            If dist<1.0 Then n\State=10
                            
                        EndIf
                        
                        n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                    Else
                        If TimeInPosMilliSecs() > n\State2 And dist<16.0 Then
                            HideEntity n\Collider
                            EntityPick(n\Collider, 1.5)
                            If PickedEntity() <> 0 Then
                                n\Angle = EntityYaw(n\Collider)+Rnd(80,110)
                            EndIf
                            ShowEntity n\Collider
                            
                            n\State2=TimeInPosMilliSecs()+1000
                            
                            If Rand(5)=1 Then n\State=0
                        EndIf	
                        
                        n\CurrSpeed = CurveValue(n\Speed*0.5, n\CurrSpeed, 20.0)
                        
                    EndIf
                    
                    RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),30.0),0
                    
                    MoveEntity n\Collider,0,0,n\CurrSpeed
                EndIf
            Case 10 ;attack
                If n\LastSeen=0
                    PlaySound2(LoadTempSound("SFX\SCP\966\Echo"+Rand(1,3)+".ogg"), mainPlayer\cam, n\Collider)
                    n\LastSeen = 1
                EndIf
                
                If n\Frame>2300.0 Then
                    AnimateNPC(n, 2391, 2416, 1.0, False)	
                    If n\Frame>2415.0 Then 
                        Select Rand(3)
                            Case 1
                                SetNPCFrame(n, 2160)
                            Case 2
                                SetNPCFrame(n, 2192)
                            Case 3
                                SetNPCFrame(n, 2221)
                        End Select
                        
                    EndIf
                Else
                    If n\Frame <= 2191 Then
                        AnimateNPC(n, 2160, 2191, 0.3, False)
                        If n\Frame > 2190 Then n\State = 8
                    ElseIf n\Frame <= 2220
                        AnimateNPC(n, 2192, 2220, 0.3, False)
                        If n\Frame > 2219 Then n\State = 8
                    ElseIf n\Frame <= 2260
                        AnimateNPC(n, 2221, 2260, 0.3, False)
                        If n\Frame > 2259 Then n\State = 8
                    EndIf
                EndIf
                
                If dist<1.0 Then
                    If n\Frame>2173.0 And prevFrame<=2173.0 Or n\Frame>2203.0 And prevFrame<=2203.0 Or n\Frame>2227.0 And prevFrame<=2227.0 Then
                        PlaySound2(LoadTempSound("SFX\General\Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\Collider)
                        mainPlayer\injuries = mainPlayer\injuries + Rnd(0.5,1.0)								
                    EndIf	
                EndIf
                
                n\Angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
                RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),30.0),0
                
        End Select
    Else
        HideEntity n\obj
        If (Rand(600)=1) Then
            TeleportCloser(n)
        EndIf
    EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
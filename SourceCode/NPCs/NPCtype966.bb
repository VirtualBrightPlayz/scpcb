Function InitializeNPCtype966(n.NPCs)
    Local i% = 1
	Local n2.NPCs
    For n2.NPCs = Each NPCs
        If (n\npcType = n2\npcType) And (n<>n2) Then
			i= i + Rand(1,3)
		EndIf
    Next
    n\nvName = "SCP-966-"+i
    
    n\collider = CreatePivot()
    EntityRadius n\collider,0.2
    
    For n2.NPCs = Each NPCs
        If (n\npcType = n2\npcType) And (n<>n2) Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh("GFX/NPCs/scp966/scp-966.b3d")
    EndIf
    
    EntityFX n\obj,1
    
    Local temp# = GetINIFloat("DATA/NPCs.ini", "SCP-966", "scale")/40.0
    ScaleEntity n\obj, temp, temp, temp		
    
    ;EntityColor n\obj,Rnd(0,50),0,Rnd(50,100)
    
    SetAnimTime n\obj,15.0
    
    EntityType n\collider,HIT_PLAYER
    
    n\speed = 0.02
End Function

Function UpdateNPCtype966(n.NPCs)
	Local prevFrame%, temp%, angle#, dist2#
	Local n2.NPCs
	
    If (n\playerDistance<HideDistance) Then
        
    ;n\state = the "general" state (idle/wander/attack/echo etc)
    ;n\state2 = timer for doing raycasts
        
        prevFrame = n\frame
        
        If n\sounds[0] > 0 Then
            temp = 0.5
        ;the ambient sound gets louder when the npcs are attacking
            If n\state > 0 Then temp = 1.0	
            
            n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, mainPlayer\cam, 10.0,temp)
        EndIf
        
        temp = Rnd(-1.0,1.0)
        PositionEntity n\obj,EntityX(n\collider,True),EntityY(n\collider,True)-0.2,EntityZ(n\collider,True)
        RotateEntity n\obj,-90.0,EntityYaw(n\collider),0.0
        
        If (Not IsPlayerWearingTempName(mainPlayer,"nvgoggles")) Then
            HideEntity n\obj
            If n\playerDistance<1 And n\reload <= 0 And MsgTimer <= 0 Then
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
                n\reload = 20*70
                MsgTimer=8*70
            EndIf
            n\reload = n\reload - timing\tickDuration
            
        Else
            ShowEntity n\obj
        EndIf
        
        If n\state3>5*70 Then
        ;n\state = 1
            If n\state3<1000.0 Then
                For n2.NPCs = Each NPCs	
                    If n2\npcType = n\npcType Then n2\state3=1000.0 
                Next
            EndIf
            
            n\state = Max(n\state,8)
            n\state3 = 1000.0					
            
        EndIf
        
        If mainPlayer\stamina<10 Then 
            n\state3=n\state3+timing\tickDuration
        ElseIf n\state3 < 900.0 Then
            n\state3=Max(n\state3-timing\tickDuration*0.2,0.0)
        EndIf
        
        If n\state <> 10 Then
            n\lastSeen = 0
        EndIf
        
        Select n\state
            Case 0 ;idle, standing
                If n\frame>2300.0 Then
                    AnimateNPC(n, 2391, 2416, 1.0, False)	
                    If n\frame>2415.0 Then SetNPCFrame(n, 201)
                Else
                    AnimateNPC(n, 201, 1015, 1.0, False)
                    
                ;echo/stare/walk around periodically
                    If n\frame>1014.0 Then 
                        If Rand(3)=1 And n\playerDistance<4 Then
                            n\state = Rand(1,4)
                        Else
                            n\state = Rand(5,6)								
                        EndIf
                    EndIf
                    
                ;echo if player gets close
                    If n\playerDistance<2.0 Then 
                        n\state=Rand(1,4)
                    EndIf 							
                EndIf
                
                n\currSpeed = CurveValue(0.0, n\currSpeed, 10.0)
                
                MoveEntity n\collider,0,0,n\currSpeed
                
            Case 1,2 ;echo
                If n\state=1 Then
                    AnimateNPC(n, 1015, 1180, 1.0, False)
                    If n\frame > 1179.0 Then n\state = 0
                Else
                    AnimateNPC(n, 1180, 1379, 1.0, False)
                    If n\frame > 1378.0 Then n\state = 0
                EndIf
                
                If n\frame>1029.0 And prevFrame<=1029.0 Or n\frame>1203.0 And prevFrame<=1203.0 Then
                    PlayRangedSound(LoadTempSound("SFX/SCP/966/Echo"+Rand(1,3)+".ogg"), mainPlayer\cam, n\collider)
                EndIf
                
                angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\collider))
                RotateEntity n\collider,0.0,CurveAngle(angle,EntityYaw(n\collider),20.0),0.0
                
                If n\state3<900 Then
                    mainPlayer\blurTimer = ((Sin(TimeInPosMilliSecs()/50)+1.0)*200)/n\playerDistance
                    
                    If n\playerDistance<16 Then
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
                If n\state=3 Then
                    AnimateNPC(n, 1379.0, 1692.0, 1.0, False)
                    
                    If n\frame>1691.0 Then n\state = 0
                Else
                    AnimateNPC(n, 1692.0, 2156.0, 1.0, False)
                    
                    If n\frame>2155.0 Then n\state = 0
                EndIf
                
                If n\frame>1393.0 And prevFrame<=1393.0 Or n\frame>1589.0 And prevFrame<=1589.0 Or n\frame>2000.0 And prevFrame<=2000.0 Then
                    PlayRangedSound(LoadTempSound("SFX/SCP/966/Idle"+Rand(1,3)+".ogg"), mainPlayer\cam, n\collider)
                EndIf
                
                angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\collider))
                RotateEntity n\collider,0.0,CurveAngle(angle,EntityYaw(n\collider),20.0),0.0
            Case 5,6,8 ;walking or chasing
                If n\frame<2343.0 Then ;start walking
                    AnimateNPC(n, 2319, 2343, 0.5, False)
                Else
                    AnimateNPC(n, 2343, 2391, n\currSpeed*25.0)
                    
                ;chasing the player
                    If n\state = 8 And n\playerDistance<32 Then
                        If n\pathTimer <= 0 Then
                            n\pathStatus = FindPath (n, EntityX(mainPlayer\collider,True), EntityY(mainPlayer\collider,True), EntityZ(mainPlayer\collider,True))
                            n\pathTimer = 40*10
                            n\currSpeed = 0
                        EndIf
                        n\pathTimer = Max(n\pathTimer-timing\tickDuration,0)
                        
                        If (Not EntityVisible(n\collider,mainPlayer\collider)) Then
                            If n\pathStatus = 2 Then
                                n\currSpeed = 0
                                SetNPCFrame(n,201)
                            ;SetAnimTime n\obj,15
                            ElseIf n\pathStatus = 1 Then
                                If n\path[n\pathLocation]=Null Then 
                                    If n\pathLocation > 19 Then 
                                        n\pathLocation = 0 : n\pathStatus = 0
                                    Else
                                        n\pathLocation = n\pathLocation + 1
                                    EndIf
                                Else
                                    n\angle = VectorYaw(EntityX(n\path[n\pathLocation]\obj,True)-EntityX(n\collider),0,EntityZ(n\path[n\pathLocation]\obj,True)-EntityZ(n\collider))
                    ;RotateEntity n\collider,0.0,CurveAngle(angle,EntityYaw(n\collider),10.0),0.0
                                    
                                    dist2 = EntityDistance(n\collider,n\path[n\pathLocation]\obj)
                                    
                                    If dist2 < 0.8 Then 
                                        ;TODO: fix
										;If n\path[n\pathLocation]\door<>Null Then
                                        ;    If (Not n\path[n\pathLocation]\door\open) Then UseDoor(n\path[n\pathLocation]\door,False)
                                        ;EndIf
                                        If n\playerDistance < 0.2 Then n\pathLocation = n\pathLocation + 1
                                    EndIf
                                    
                                EndIf
                            ElseIf n\pathStatus = 0 Then
                                n\currSpeed = CurveValue(0,n\currSpeed,10.0)
                            EndIf
                        Else
                            n\angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\collider))
                            
                            If n\playerDistance<1.0 Then n\state=10
                            
                        EndIf
                        
                        n\currSpeed = CurveValue(n\speed,n\currSpeed,10.0)
                    Else
                        If TimeInPosMilliSecs() > n\state2 And n\playerDistance<16.0 Then
                            HideEntity n\collider
                            EntityPick(n\collider, 1.5)
                            If PickedEntity() <> 0 Then
                                n\angle = EntityYaw(n\collider)+Rnd(80,110)
                            EndIf
                            ShowEntity n\collider
                            
                            n\state2=TimeInPosMilliSecs()+1000
                            
                            If Rand(5)=1 Then n\state=0
                        EndIf	
                        
                        n\currSpeed = CurveValue(n\speed*0.5, n\currSpeed, 20.0)
                        
                    EndIf
                    
                    RotateEntity n\collider, 0, CurveAngle(n\angle,EntityYaw(n\collider),30.0),0
                    
                    MoveEntity n\collider,0,0,n\currSpeed
                EndIf
            Case 10 ;attack
                If n\lastSeen=0 Then
                    PlayRangedSound(LoadTempSound("SFX/SCP/966/Echo"+Rand(1,3)+".ogg"), mainPlayer\cam, n\collider)
                    n\lastSeen = 1
                EndIf
                
                If n\frame>2300.0 Then
                    AnimateNPC(n, 2391, 2416, 1.0, False)	
                    If n\frame>2415.0 Then 
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
                    If n\frame <= 2191 Then
                        AnimateNPC(n, 2160, 2191, 0.3, False)
                        If n\frame > 2190 Then n\state = 8
                    ElseIf n\frame <= 2220 Then
                        AnimateNPC(n, 2192, 2220, 0.3, False)
                        If n\frame > 2219 Then n\state = 8
                    ElseIf n\frame <= 2260 Then
                        AnimateNPC(n, 2221, 2260, 0.3, False)
                        If n\frame > 2259 Then n\state = 8
                    EndIf
                EndIf
                
                If n\playerDistance<1.0 Then
                    If n\frame>2173.0 And prevFrame<=2173.0 Or n\frame>2203.0 And prevFrame<=2203.0 Or n\frame>2227.0 And prevFrame<=2227.0 Then
                        PlayRangedSound(LoadTempSound("SFX/General/Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\collider)
                        mainPlayer\injuries = mainPlayer\injuries + Rnd(0.5,1.0)								
                    EndIf	
                EndIf
                
                n\angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\collider))
                RotateEntity n\collider, 0, CurveAngle(n\angle,EntityYaw(n\collider),30.0),0
                
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
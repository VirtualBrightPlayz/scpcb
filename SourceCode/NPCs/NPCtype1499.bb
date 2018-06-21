Function InitializeNPCtype1499(n.NPCs)
    n\nvName = "Unidentified"
    n\collider = CreatePivot()
    EntityRadius n\collider, 0.2
    EntityType n\collider, HIT_PLAYER
    For n2.NPCs = Each NPCs
        If (n\npcType = n2\NPCtype) And (n<>n2) Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh("GFX/NPCs/scp1499/scp1499.b3d")
    EndIf
    
    n\speed = (GetINIFloat("DATA/NPCs.ini", "SCP-1499-1", "speed") / 100.0) * Rnd(0.9,1.1)
    temp# = (GetINIFloat("DATA/NPCs.ini", "SCP-1499-1", "scale") / 6.0) * Rnd(0.8,1.0)
    
    ScaleEntity n\obj, temp, temp, temp
    
    EntityFX n\obj,1
End Function

Function UpdateNPCtype1499(n.NPCs)
    ;n\state: Current State of the NPC
    ;n\state2: A second state variable (dependend on the current NPC's n\state)
    
    prevFrame# = n\frame
    
    If (Not n\Idle) And EntityDistance(n\collider,mainPlayer\collider)<HideDistance*2 Then
        If n\state = 0 Or n\state = 2 Then
            For n2.NPCs = Each NPCs
                If n2\NPCtype = n\npcType And n2 <> n Then
                    If n2\state <> 0 And n2\state <> 2 Then
                        n\state = 1
                        n\state2 = 0
                        Exit
                    EndIf
                EndIf
            Next
        EndIf
        
        Select n\state
            Case 0
                If n\CurrSpeed = 0.0 Then
                    If n\state2 < 500.0*Rnd(1,3) Then
                        n\CurrSpeed = 0.0
                        n\state2 = n\state2 + timing\tickDuration
                    Else
                        If n\currSpeed = 0.0 Then n\currSpeed = n\currSpeed + 0.0001
                    EndIf
                Else
                    If n\state2 < 10000.0*Rnd(1,3) Then
                        n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                        n\state2 = n\state2 + timing\tickDuration
                    Else
                        n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,50.0)
                    EndIf
                    
                    RotateEntity n\collider,0,CurveAngle(n\Angle,EntityYaw(n\collider),10.0),0
                    
                    If Rand(200) = 1 Then n\Angle = n\Angle + Rnd(-45,45)
                    
                    HideEntity n\collider
                    EntityPick(n\collider, 1.5)
                    If PickedEntity() <> 0 Then
                        n\Angle = EntityYaw(n\collider)+Rnd(80,110)
                    EndIf
                    ShowEntity n\collider
                EndIf
                
                If n\CurrSpeed = 0.0 Then
                    AnimateNPC(n,296,317,0.2)
                Else
                    If (n\ID Mod 2 = 0) Then
                        AnimateNPC(n,1,62,(n\CurrSpeed*28))
                    Else
                        AnimateNPC(n,100,167,(n\CurrSpeed*28))
                    EndIf
                EndIf
                
                ;randomly play the "screaming animation" and revert back to state 0
                If (Rand(5000)=1) Then
                    n\state = 2
                    n\state2 = 0
                    
                    If Not IsChannelPlaying(n\soundChannels[0]) Then
                        dist = EntityDistance(n\collider,mainPlayer\collider)
                        If (dist < 20.0) Then
                            If n\sounds[0] <> 0 Then FreeSound n\sounds[0] : n\sounds[0] = 0
                            n\sounds[0] = LoadSound("SFX/SCP/1499/Idle"+Rand(1,4)+".ogg")
                            n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider, 20.0)
                        EndIf
                    EndIf
                EndIf
                
                If (n\ID Mod 2 = 0) And (Not NoTarget) Then
                    dist = EntityDistance(n\collider,mainPlayer\collider)
                    If dist < 10.0 Then
                        If EntityVisible(n\collider,mainPlayer\collider) Then
                            ;play the "screaming animation"
                            n\state = 2
                            If dist < 5.0 Then
                                If n\sounds[0] <> 0 Then FreeSound n\sounds[0] : n\sounds[0] = 0
                                n\sounds[0] = LoadSound("SFX/SCP/1499/Triggered.ogg")
                                n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider,20.0)
                                
                                n\state2 = 1 ;if player is too close, switch to attack after screaming
                                
                                For n2.NPCs = Each NPCs
                                    ;If n2\NPCtype = n\npcType And n2 <> n And (n\ID Mod 2 = 0) Then
                                    If n2\NPCtype = n\npcType And n2 <> n Then
                                        n2\state = 1
                                        n2\state2 = 0
                                    EndIf
                                Next
                            Else
                                n\state2 = 0 ;otherwise keep idling
                            EndIf
                            
                            n\Frame = 203
                        EndIf
                    EndIf
                EndIf
            Case 1 ;attacking the player
                If NoTarget Then n\state = 0
                
                If mainPlayer\currRoom\RoomTemplate\Name = "dimension1499" Then
                    ;If Music(19)=0 Then Music(19) = LoadSound("SFX/Music/1499Danger.ogg") ;TODO: fix
                    ShouldPlay = 19
                EndIf
                
                PointEntity n\obj,mainPlayer\collider
                RotateEntity n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0),0
                
                dist = EntityDistance(n\collider,mainPlayer\collider)
                
                If n\state2 = 0.0 Then
                    n\CurrSpeed = CurveValue(n\Speed*1.75,n\CurrSpeed,10.0)
                    
                    If (n\ID Mod 2 = 0) Then
                        AnimateNPC(n,1,62,(n\CurrSpeed*28))
                    Else
                        AnimateNPC(n,100,167,(n\CurrSpeed*28))
                    EndIf
                EndIf
                
                If dist < 0.75 Then
                    If (n\ID Mod 2 = 0) Or n\state3 = 1 Then
                        n\state2 = Rand(1,2)
                        n\state = 3
                        If n\state2 = 1 Then
                            SetNPCFrame(n,63)
                        Else
                            SetNPCFrame(n,168)
                        EndIf
                    Else
                        n\state = 4
                    EndIf
                EndIf
            Case 2 ;play the "screaming animation" and switch to n\state2 after it's finished
                n\CurrSpeed = 0.0
                AnimateNPC(n,203,295,0.1,False)
                
                If n\Frame > 294.0 Then
                    n\state = n\state2
                EndIf
            Case 3 ;slashing at the player
                n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,5.0)
                dist = EntityDistance(n\collider,mainPlayer\collider)
                If n\state2 = 1 Then
                    AnimateNPC(n,63,100,0.6,False)
                    If prevFrame < 89 And n\Frame=>89 Then
                        If dist > 0.85 Or Abs(DeltaYaw(n\collider,mainPlayer\collider))>60.0 Then
                            ;Miss
                        Else
                            mainPlayer\injuries = mainPlayer\injuries + Rnd(0.75,1.5)
                            PlayRangedSound(LoadTempSound("SFX/General/Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\collider)
                            If mainPlayer\injuries > 10.0 Then
                                Kill(mainPlayer)
                                If mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499" Then
                                    DeathMSG = "All personnel situated within Evacuation Shelter LC-2 during the breach have been administered "
                                    DeathMSG = DeathMSG + "Class-B amnestics due to Incident 1499-E. The Class D subject involved in the event "
                                    DeathMSG = DeathMSG + "died shortly after being shot by Agent [REDACTED]."
                                Else
                                    DeathMSG = "An unidentified male and a deceased Class D subject were discovered in [REDACTED] by the Nine-Tailed Fox. "
                                    DeathMSG = DeathMSG + "The man was described as highly agitated and seemed to only speak Russian. "
                                    DeathMSG = DeathMSG + "He's been taken into a temporary holding area at [REDACTED] while waiting for a translator to arrive."
                                EndIf
                            EndIf
                        EndIf
                    ElseIf n\Frame => 99 Then
                        n\state2 = 0.0
                        n\state = 1
                    EndIf
                Else
                    AnimateNPC(n,168,202,0.6,False)
                    If prevFrame < 189 And n\Frame=>189 Then
                        If dist > 0.85 Or Abs(DeltaYaw(n\collider,mainPlayer\collider))>60.0 Then
                            ;Miss
                        Else
                            mainPlayer\injuries = mainPlayer\injuries + Rnd(0.75,1.5)
                            PlayRangedSound(LoadTempSound("SFX/General/Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\collider)
                            If mainPlayer\injuries > 10.0 Then
                                Kill(mainPlayer)
                                If mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499" Then
                                    DeathMSG = "All personnel situated within Evacuation Shelter LC-2 during the breach have been administered "
                                    DeathMSG = DeathMSG + "Class-B amnestics due to Incident 1499-E. The Class D subject involved in the event "
                                    DeathMSG = DeathMSG + "died shortly after being shot by Agent [REDACTED]."
                                Else
                                    DeathMSG = "An unidentified male and a deceased Class D subject were discovered in [REDACTED] by the Nine-Tailed Fox. "
                                    DeathMSG = DeathMSG + "The man was described as highly agitated and seemed to only speak Russian. "
                                    DeathMSG = DeathMSG + "He's been taken into a temporary holding area at [REDACTED] while waiting for a translator to arrive."
                                EndIf
                            EndIf
                        EndIf
                    ElseIf n\Frame => 201 Then
                        n\state2 = 0.0
                        n\state = 1
                    EndIf
                EndIf
            Case 4 ;standing in front of the player
                dist = EntityDistance(n\collider,mainPlayer\collider)
                n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,5.0)
                AnimateNPC(n,296,317,0.2)
                
                PointEntity n\obj,mainPlayer\collider
                RotateEntity n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0),0
                
                If dist > 0.85 Then
                    n\state = 1
                EndIf
        End Select
        
        If n\soundChannels[0] <> 0 And IsChannelPlaying(n\soundChannels[0]) Then
            UpdateRangedSoundOrigin(n\soundChannels[0],mainPlayer\cam,n\collider,20.0)
        EndIf
        
        MoveEntity n\collider,0,0,n\CurrSpeed*timing\tickDuration
        
        RotateEntity n\obj,0,EntityYaw(n\collider)-180,0
        PositionEntity n\obj,EntityX(n\collider),EntityY(n\collider)-0.2,EntityZ(n\collider)
        
        ShowEntity n\obj
    Else
        HideEntity n\obj
    EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
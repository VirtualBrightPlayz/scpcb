Function InitializeNPCtype1499(n.NPCs)
    n\NVName = "Unidentified"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    EntityType n\Collider, HIT_PLAYER
    For n2.NPCs = Each NPCs
        If (n\NPCtype = n2\NPCtype) And (n<>n2) Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\npcs\1499-1.b3d")
    EndIf
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-1499-1", "speed") / 100.0) * Rnd(0.9,1.1)
    temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-1499-1", "scale") / 6.0) * Rnd(0.8,1.0)
    
    ScaleEntity n\obj, temp, temp, temp
    
    EntityFX n\obj,1
End Function

Function UpdateNPCtype1499(n.NPCs)
    ;n\State: Current State of the NPC
    ;n\State2: A second state variable (dependend on the current NPC's n\State)
    
    prevFrame# = n\Frame
    
    If (Not n\Idle) And EntityDistance(n\Collider,mainPlayer\collider)<HideDistance*2
        If n\State = 0 Or n\State = 2
            For n2.NPCs = Each NPCs
                If n2\NPCtype = n\NPCtype And n2 <> n
                    If n2\State <> 0 And n2\State <> 2
                        n\State = 1
                        n\State2 = 0
                        Exit
                    EndIf
                EndIf
            Next
        EndIf
        
        Select n\State
            Case 0
                If n\CurrSpeed = 0.0
                    If n\State2 < 500.0*Rnd(1,3)
                        n\CurrSpeed = 0.0
                        n\State2 = n\State2 + FPSfactor
                    Else
                        If n\CurrSpeed = 0.0 Then n\CurrSpeed = n\CurrSpeed + 0.0001
                    EndIf
                Else
                    If n\State2 < 10000.0*Rnd(1,3)
                        n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                        n\State2 = n\State2 + FPSfactor
                    Else
                        n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,50.0)
                    EndIf
                    
                    RotateEntity n\Collider,0,CurveAngle(n\Angle,EntityYaw(n\Collider),10.0),0
                    
                    If Rand(200) = 1 Then n\Angle = n\Angle + Rnd(-45,45)
                    
                    HideEntity n\Collider
                    EntityPick(n\Collider, 1.5)
                    If PickedEntity() <> 0 Then
                        n\Angle = EntityYaw(n\Collider)+Rnd(80,110)
                    EndIf
                    ShowEntity n\Collider
                EndIf
                
                If n\CurrSpeed = 0.0
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
                    n\State = 2
                    n\State2 = 0
                    
                    If Not ChannelPlaying(n\SoundChn) Then
                        dist = EntityDistance(n\Collider,mainPlayer\collider)
                        If (dist < 20.0) Then
                            If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
                            n\Sound = LoadSound_Strict("SFX\SCP\1499\Idle"+Rand(1,4)+".ogg")
                            n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider, 20.0)
                        EndIf
                    EndIf
                EndIf
                
                If (n\ID Mod 2 = 0) And (Not NoTarget) Then
                    dist = EntityDistance(n\Collider,mainPlayer\collider)
                    If dist < 10.0 Then
                        If EntityVisible(n\Collider,mainPlayer\collider) Then
                            ;play the "screaming animation"
                            n\State = 2
                            If dist < 5.0 Then
                                If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
                                n\Sound = LoadSound_Strict("SFX\SCP\1499\Triggered.ogg")
                                n\SoundChn = PlaySound2(n\Sound, mainPlayer\cam, n\Collider,20.0)
                                
                                n\State2 = 1 ;if player is too close, switch to attack after screaming
                                
                                For n2.NPCs = Each NPCs
                                    ;If n2\NPCtype = n\NPCtype And n2 <> n And (n\ID Mod 2 = 0) Then
                                    If n2\NPCtype = n\NPCtype And n2 <> n
                                        n2\State = 1
                                        n2\State2 = 0
                                    EndIf
                                Next
                            Else
                                n\State2 = 0 ;otherwise keep idling
                            EndIf
                            
                            n\Frame = 203
                        EndIf
                    EndIf
                EndIf
            Case 1 ;attacking the player
                If NoTarget Then n\State = 0
                
                If mainPlayer\currRoom\RoomTemplate\Name = "dimension1499"
                    If Music(19)=0 Then Music(19) = LoadSound_Strict("SFX\Music\1499Danger.ogg")
                    ShouldPlay = 19
                EndIf
                
                PointEntity n\obj,mainPlayer\collider
                RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0),0
                
                dist = EntityDistance(n\Collider,mainPlayer\collider)
                
                If n\State2 = 0.0
                    n\CurrSpeed = CurveValue(n\Speed*1.75,n\CurrSpeed,10.0)
                    
                    If (n\ID Mod 2 = 0) Then
                        AnimateNPC(n,1,62,(n\CurrSpeed*28))
                    Else
                        AnimateNPC(n,100,167,(n\CurrSpeed*28))
                    EndIf
                EndIf
                
                If dist < 0.75
                    If (n\ID Mod 2 = 0) Or n\State3 = 1
                        n\State2 = Rand(1,2)
                        n\State = 3
                        If n\State2 = 1
                            SetNPCFrame(n,63)
                        Else
                            SetNPCFrame(n,168)
                        EndIf
                    Else
                        n\State = 4
                    EndIf
                EndIf
            Case 2 ;play the "screaming animation" and switch to n\state2 after it's finished
                n\CurrSpeed = 0.0
                AnimateNPC(n,203,295,0.1,False)
                
                If n\Frame > 294.0 Then
                    n\State = n\State2
                EndIf
            Case 3 ;slashing at the player
                n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,5.0)
                dist = EntityDistance(n\Collider,mainPlayer\collider)
                If n\State2 = 1
                    AnimateNPC(n,63,100,0.6,False)
                    If prevFrame < 89 And n\Frame=>89
                        If dist > 0.85 Or Abs(DeltaYaw(n\Collider,mainPlayer\collider))>60.0
                            ;Miss
                        Else
                            mainPlayer\injuries = mainPlayer\injuries + Rnd(0.75,1.5)
                            PlaySound2(LoadTempSound("SFX\General\Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\Collider)
                            If mainPlayer\injuries > 10.0
                                Kill()
                                If mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499"
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
                    ElseIf n\Frame => 99
                        n\State2 = 0.0
                        n\State = 1
                    EndIf
                Else
                    AnimateNPC(n,168,202,0.6,False)
                    If prevFrame < 189 And n\Frame=>189
                        If dist > 0.85 Or Abs(DeltaYaw(n\Collider,mainPlayer\collider))>60.0
                            ;Miss
                        Else
                            mainPlayer\injuries = mainPlayer\injuries + Rnd(0.75,1.5)
                            PlaySound2(LoadTempSound("SFX\General\Slash"+Rand(1,2)+".ogg"), mainPlayer\cam, n\Collider)
                            If mainPlayer\injuries > 10.0
                                Kill()
                                If mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499"
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
                    ElseIf n\Frame => 201
                        n\State2 = 0.0
                        n\State = 1
                    EndIf
                EndIf
            Case 4 ;standing in front of the player
                dist = EntityDistance(n\Collider,mainPlayer\collider)
                n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,5.0)
                AnimateNPC(n,296,317,0.2)
                
                PointEntity n\obj,mainPlayer\collider
                RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0),0
                
                If dist > 0.85
                    n\State = 1
                EndIf
        End Select
        
        If n\SoundChn <> 0 And ChannelPlaying(n\SoundChn) Then
            UpdateSoundOrigin(n\SoundChn,mainPlayer\cam,n\Collider,20.0)
        EndIf
        
        MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
        
        RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
        PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
        
        ShowEntity n\obj
    Else
        HideEntity n\obj
    EndIf
End Function

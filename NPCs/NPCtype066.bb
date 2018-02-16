Function InitializeNPCtype066(n.NPCs)
    n\NVName = "SCP-066"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    EntityType n\Collider, HIT_PLAYER
    
    n\obj = LoadAnimMesh_Strict("GFX\NPCs\scp-066.b3d")
    temp# = GetINIFloat("DATA\NPCs.ini", "SCP-066", "scale")/2.5
    ScaleEntity n\obj, temp, temp, temp		
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-066", "speed") / 100.0)
End Function

Function UpdateNPCtype066(n.NPCs)
    dist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
    
    Select n\State
        Case 0 
            ;idle: moves around randomly from waypoint to another if the player is far enough
            ;starts staring at the player when the player is close enough
            
            If dist > 20.0 Then
                AnimateNPC(n, 451, 612, 0.2, True)
                ;Animate2(n\obj, AnimTime(n\obj), 451, 612, 0.2, True)
                
                If n\State2 < MilliSecs2() Then
                    For w.waypoints = Each WayPoints
                        If w\door = Null Then
                            If Abs(EntityX(w\obj,True)-EntityX(n\Collider))<4.0 Then
                                If Abs(EntityZ(w\obj,True)-EntityZ(n\Collider))<4.0 Then
                                    PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.3, EntityZ(w\obj,True)
                                    ResetEntity n\Collider
                                    Exit
                                EndIf
                            EndIf
                        EndIf
                    Next
                    n\State2 = MilliSecs2()+5000
                EndIf
            ElseIf dist < 8.0
                n\LastDist = Rnd(1.0, 2.5)
                n\State = 1
            EndIf
        Case 1 ;staring at the player
            
            If n\Frame<451 Then
                angle = WrapAngle(CurveAngle(DeltaYaw(n\Collider, Collider)-180, (AnimTime(n\obj)-2.0)/1.2445, 15.0))
                ;0->360 = 2->450
                SetNPCFrame(n,angle*1.2445+2.0)
                
                ;SetAnimTime(n\obj, angle*1.2445+2.0)							
            Else
                AnimateNPC(n, 636, 646, 0.4, False)
                If n\Frame = 646 Then SetNPCFrame(n,2)
                ;Animate2(n\obj, AnimTime(n\obj), 636, 646, 0.4, False)
                ;If AnimTime(n\obj)=646 Then SetAnimTime (n\obj, 2)
            EndIf
            dist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
            
            If Rand(700)=1 Then PlaySound2(LoadTempSound("SFX\SCP\066\Eric"+Rand(1,3)+".ogg"),Camera, n\Collider, 8.0)
            
            If dist < 1.0+n\LastDist Then n\State = Rand(2,3)
        Case 2 ;roll towards the player and make a sound, and then escape	
            If n\Frame < 647 Then 
                angle = CurveAngle(0, (AnimTime(n\obj)-2.0)/1.2445, 5.0)
                
                If angle < 5 Or angle > 355 Then 
                    SetNPCFrame(n,647)
                Else
                    SetNPCFrame(n,angle*1.2445+2.0)
                EndIf
                ;SetAnimTime(n\obj, angle*1.2445+2.0)
                ;If angle < 5 Or angle > 355 Then SetAnimTime(n\obj, 647)
            Else
                If n\Frame=683 Then 
                    If n\State2 = 0 Then
                        If Rand(2)=1 Then
                            PlaySound2(LoadTempSound("SFX\SCP\066\Eric"+Rand(1,3)+".ogg"),Camera, n\Collider, 8.0)
                        Else
                            PlaySound2(LoadTempSound("SFX\SCP\066\Notes"+Rand(1,6)+".ogg"), Camera, n\Collider, 8.0)
                        EndIf									
                        
                        Select Rand(1,6)
                            Case 1
                                If n\Sound2=0 Then n\Sound2=LoadSound_Strict("SFX\SCP\066\Beethoven.ogg")
                                n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Collider)
                                CameraShake = 10.0
                            Case 2
                                n\State3 = Rand(700,1400)
                            Case 3
                                For d.Doors = Each Doors
                                    If d\locked = False And d\KeyCard = 0 And d\Code = "" Then
                                        If Abs(EntityX(d\frameobj)-EntityX(n\Collider))<16.0 Then
                                            If Abs(EntityZ(d\frameobj)-EntityZ(n\Collider))<16.0 Then
                                                UseDoor(d, False)
                                            EndIf
                                        EndIf
                                    EndIf
                                Next
                            Case 4
                                If PlayerRoom\RoomTemplate\DisableDecals = False Then
                                    CameraShake = 5.0
                                    de.Decals = CreateDecal(1, EntityX(n\Collider), 0.01, EntityZ(n\Collider), 90, Rand(360), 0)
                                    de\Size = 0.3 : UpdateDecals
                                    PlaySound_Strict(LoadTempSound("SFX\General\BodyFall.ogg"))
                                    If Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))<0.8 Then
                                        Injuries = Injuries + Rnd(0.3,0.5)
                                    EndIf
                                EndIf
                        End Select
                    EndIf
                    
                    n\State2 = n\State2+FPSfactor
                    If n\State2>70 Then 
                        n\State = 3
                        n\State2 = 0
                    EndIf
                Else
                    n\CurrSpeed = CurveValue(n\Speed*1.5, n\CurrSpeed, 10.0)
                    PointEntity n\obj, Collider
                    ;angle = CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10);1.0/Max(n\CurrSpeed,0.0001))
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj)-180, EntityYaw(n\Collider), 10), 0
                    
                    AnimateNPC(n, 647, 683, n\CurrSpeed*25, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 647, 683, n\CurrSpeed*25, False)
                    
                    MoveEntity n\Collider, 0,0,-n\CurrSpeed*FPSfactor
                    
                EndIf
            EndIf
        Case 3
            PointEntity n\obj, Collider
            angle = CurveAngle(EntityYaw(n\obj)+n\Angle-180, EntityYaw(n\Collider), 10);1.0/Max(n\CurrSpeed,0.0001))
            RotateEntity n\Collider, 0, angle, 0
            
            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 10.0)
            MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
            
            ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25)
            
            If Rand(100)=1 Then n\Angle = Rnd(-20,20)
            
            n\State2 = n\State2 + FPSfactor
            If n\State2>250 Then 
                AnimateNPC(n, 684, 647, -n\CurrSpeed*25, False)
                ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25, False)
                If n\Frame=647 Then 
                    n\State = 0
                    n\State2=0
                EndIf
            Else
                AnimateNPC(n, 684, 647, -n\CurrSpeed*25)
                
                ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25)
            EndIf
            
    End Select
    
    If n\State > 1 Then
        If n\Sound = 0 Then n\Sound = LoadSound_Strict("SFX\SCP\066\Rolling.ogg")
        If n\SoundChn<>0 Then
            If ChannelPlaying(n\SoundChn) Then
                n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 20)
            EndIf
        Else
            n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider, 20)
        EndIf					
    EndIf
    
    ;If n\SoundChn2<>0 Then
    ;	If ChannelPlaying(n\SoundChn2) Then
    ;		n\SoundChn2 = LoopSound2(n\Sound2, n\SoundChn2, Camera, n\Collider, 20)
    ;		BlurTimer = Max((5.0-dist)*300,0)
    ;	EndIf
    ;EndIf
    
    
    If n\State3 > 0 Then
        n\State3 = n\State3-FPSfactor
        LightVolume = TempLightVolume-TempLightVolume*Min(Max(n\State3/500,0.01),0.6)
        HeartBeatRate = Max(HeartBeatRate, 130)
        HeartBeatVolume = Max(HeartBeatVolume,Min(n\State3/1000,1.0))
    EndIf
    
    If ChannelPlaying(n\SoundChn2)
        UpdateSoundOrigin2(n\SoundChn2,Camera,n\Collider,20)
        BlurTimer = Max((5.0-dist)*300,0)
    EndIf
    
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
    
    RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), 0
End Function

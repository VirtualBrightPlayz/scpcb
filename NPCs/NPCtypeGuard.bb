Function InitializeNPCtypeGuard(n.NPCs)
    n\NVName = "Human"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    ;EntityRadius Collider, 0.15, 0.30
    EntityType n\Collider, HIT_PLAYER
    n\obj = CopyEntity(GuardObj) ;LoadAnimMesh_Strict("GFX\npcs\mtf.b3d")
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "Guard", "speed") / 100.0)
    temp# = (GetINIFloat("DATA\NPCs.ini", "Guard", "scale") / 2.5)
    
    ScaleEntity n\obj, temp, temp, temp
    
    MeshCullBox (n\obj, -MeshWidth(GuardObj), -MeshHeight(GuardObj), -MeshDepth(GuardObj), MeshWidth(GuardObj)*2, MeshHeight(GuardObj)*2, MeshDepth(GuardObj)*2)
End Function

Function UpdateNPCtypeGuard(n.NPCs)
	Local dist#

    prevFrame# = n\Frame
				
    n\BoneToManipulate = ""
    n\BoneToManipulate2 = ""
    n\ManipulateBone = False
    n\ManipulationType = 0
    n\NPCNameInSection = "Guard"
    
    Select n\State
        Case 1 ;aims and shoots at the player
            AnimateNPC(n, 1539, 1553, 0.2, False)
            ;Animate2(n\obj, AnimTime(n\obj), 1539, 1553, 0.2, False)
            
            If KillTimer => 0 Then
                dist = EntityDistance(n\Collider,Collider)
                Local ShootAccuracy# = 0.4+0.5*SelectedDifficulty\aggressiveNPCs
                Local DetectDistance# = 11.0
                
                ;If at Gate B increase his distance so that he can shoot the player from a distance after they are spotted.
                If PlayerRoom\RoomTemplate\Name = "exit1" Then
                    DetectDistance = 21.0
                    ShootAccuracy = 0.0
                    If Rand(1,8-SelectedDifficulty\aggressiveNPCs*4)<2 Then ShootAccuracy = 0.03
                    
                    ;increase accuracy if the player is going slow
                    ShootAccuracy = ShootAccuracy + (0.5 - CurrSpeed*20)
                EndIf
                
                If dist < DetectDistance Then
                    pvt% = CreatePivot()
                    PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
                    PointEntity(pvt, Collider)
                    RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
                    
                    RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
                    
                    PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
                    PointEntity(pvt, Collider)
                    RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
                    
                    If n\Reload = 0 And n\Frame>1550 Then
                        DebugLog "entitypick"
                        EntityPick(pvt, dist)
                        If PickedEntity() = Collider Or n\State3=1 Then
                            Local instaKillPlayer% = False
                            
                            If PlayerRoom\RoomTemplate\Name = "start" Then 
                                DeathMSG = "Subject D-9341. Cause of death: Gunshot wound to the head. The surveillance tapes confirm that the subject was terminated by Agent Ulgrin shortly after the site lockdown was initiated."
                                instaKillPlayer = True
                            ElseIf PlayerRoom\RoomTemplate\Name = "exit1" Then
                                DeathMSG = Chr(34)+"Agent G. to control. Eliminated a Class D escapee in Gate B's courtyard."+Chr(34)
                            Else
                                DeathMSG = ""
                            EndIf
                            
                            PlaySound2(GunshotSFX, Camera, n\Collider, 35)
                            
                            RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
                            PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                            MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
                            
                            PointEntity pvt, Collider
                            Shoot(EntityX(pvt), EntityY(pvt), EntityZ(pvt), ShootAccuracy, False, instaKillPlayer)
                            n\Reload = 7
                        Else
                            n\CurrSpeed = n\Speed
                        End If
                    EndIf
                    
                    FreeEntity(pvt)									
                EndIf
                
                n\ManipulateBone = True
                
                If n\State2 = 10 Then ;Hacky way of applying spine pitch to specific guards.
                    n\BoneToManipulate = "spine"
                    n\ManipulationType = 3
                Else
                    n\BoneToManipulate = "chest"
                    n\BoneToManipulate2 = "head"
                    n\ManipulationType = 1
                EndIf
            Else
                n\State = 0
            EndIf
        Case 2 ;shoots
            AnimateNPC(n, 1539, 1553, 0.35, False)
            DebugLog "shoot"
            ;Animate2(n\obj, AnimTime(n\obj), 1539, 1553, 0.35, False)
            If n\Reload = 0 And n\Frame > 1545 Then 
                PlaySound2(GunshotSFX, Camera, n\Collider, 20)
                p.Particles = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
                PositionEntity(p\pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                RotateEntity(p\pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
                MoveEntity (p\pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
                n\Reload = 7
            End If
        Case 3 ;follows a path
            If n\PathStatus = 2 Then
                n\State = 0
                n\CurrSpeed = 0
            ElseIf n\PathStatus = 1
                If n\Path[n\PathLocation]=Null Then 
                    If n\PathLocation > 19 Then 
                        n\PathLocation = 0 : n\PathStatus = 0
                    Else
                        n\PathLocation = n\PathLocation + 1
                    EndIf
                Else
                    PointEntity n\obj, n\Path[n\PathLocation]\obj
                    
                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
                    
                    AnimateNPC(n, 1614, 1641, n\CurrSpeed*30)
                    ;Animate2(n\obj, AnimTime(n\obj), 1614, 1641, n\CurrSpeed*30)
                    n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
                    
                    MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
                    
                    If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
                        n\PathLocation = n\PathLocation + 1
                    EndIf 
                EndIf
            Else
                n\CurrSpeed = 0
                n\State = 4
            EndIf
        Case 4
            AnimateNPC(n, 923, 1354, 0.2)
            ;Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
            
            If Rand(400) = 1 Then n\Angle = Rnd(-180, 180)
            
            RotateEntity(n\Collider, 0, CurveAngle(n\Angle + Sin(MilliSecs2() / 50) * 2, EntityYaw(n\Collider), 150.0), 0, True)
            
            dist# = EntityDistance(n\Collider, Collider)
            If dist < 15.0 Then
                
                If WrapAngle(EntityYaw(n\Collider)-DeltaYaw(n\Collider, Collider))<90 Then
                    If EntityVisible(pvt,Collider) Then n\State = 1
                EndIf
                
            EndIf
            
        Case 5 ;following a target
            
            RotateEntity n\Collider, 0, CurveAngle(VectorYaw(n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider))+n\Angle, EntityYaw(n\Collider), 20.0), 0
            
            dist# = Distance(EntityX(n\Collider),EntityZ(n\Collider),n\EnemyX,n\EnemyZ)
            
            AnimateNPC(n, 1614, 1641, n\CurrSpeed*30)
            ;Animate2(n\obj, AnimTime(n\obj), 1614, 1641, n\CurrSpeed*30)
            
            If dist > 2.0 Or dist < 1.0  Then
                n\CurrSpeed = CurveValue(n\Speed*Sgn(dist-1.5)*0.75, n\CurrSpeed, 10.0)
            Else
                n\CurrSpeed = CurveValue(0, n\CurrSpeed, 10.0)
            EndIf
            
            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
        Case 7
            AnimateNPC(n, 923, 1354, 0.2)
            ;Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
        Case 8
            
        Case 9
            AnimateNPC(n, 923, 1071, 0.2)
            n\BoneToManipulate = "head"
            n\ManipulateBone = True
            n\ManipulationType = 0
            n\Angle = EntityYaw(n\Collider)
        Case 10
            AnimateNPC(n, 1614, 1641, n\CurrSpeed*30)
            
            n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
            
            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
        Case 11
            If KillTimer => 0 Then
                dist = EntityDistance(n\Collider,Collider)
                
                If dist<11.0 And EntityVisible(n\Collider,Collider) Then
                    pvt% = CreatePivot()
                    PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
                    PointEntity(pvt, Collider)
                    RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
                    
                    RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
                    
                    PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
                    PointEntity(pvt, Collider)
                    RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
                    
                    If EntityVisible(n\Collider,Collider) Then n\State3=1
                    
                    If n\Reload = 0 And n\Frame>1550 Then
                        DebugLog "entitypick"
                        EntityPick(pvt, dist)
                        If PickedEntity() = Collider Or n\State3=1 Then
                            If Abs(DeltaYaw(n\Collider,Collider))<50
                                DeathMSG = ""
                                
                                PlaySound2(GunshotSFX, Camera, n\Collider, 35)
                                
                                RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
                                PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                                MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
                                
                                PointEntity pvt, Collider
                                Shoot(EntityX(pvt),EntityY(pvt),EntityZ(pvt),0.9, False)
                                n\Reload = 7
                            EndIf
                        Else
                            n\CurrSpeed = n\Speed
                        End If
                    EndIf
                    
                    FreeEntity(pvt)
                    
                    If n\Frame < 843 Or n\Frame > 895
                        AnimateNPC(n, 1539, 1553, 0.2, False)
                    Else
                        SetNPCFrame(n, 1553)
                    EndIf
                    
                    n\BoneToManipulate = "chest"
                    n\BoneToManipulate2 = "head"
                    n\ManipulateBone = True
                    n\ManipulationType = 1
                Else
                    If n\PathStatus = 1
                        If n\Path[n\PathLocation]=Null Then 
                            If n\PathLocation > 19 Then 
                                n\PathLocation = 0 : n\PathStatus = 0
                            Else
                                n\PathLocation = n\PathLocation + 1
                            EndIf
                        Else
                            AnimateNPC(n, 843, 895, n\CurrSpeed*36)
                            n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
                            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
                            
                            PointEntity n\obj, n\Path[n\PathLocation]\obj
                            
                            RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
                            
                            If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
                                n\PathLocation = n\PathLocation + 1
                            EndIf
                        EndIf
                    Else
                        If n\PathTimer = 0 Then n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider)+0.5,EntityZ(Collider))
                        
                        Local wayPointCloseToPlayer.WayPoints
                        wayPointCloseToPlayer = Null
                        
                        For wp.WayPoints = Each WayPoints
                            If EntityDistance(wp\obj,Collider)<2.0
                                wayPointCloseToPlayer = wp
                                Exit
                            EndIf
                        Next
                        
                        If wayPointCloseToPlayer<>Null
                            n\PathTimer = 1
                            If EntityVisible(wayPointCloseToPlayer\obj,n\Collider)
                                If Abs(DeltaYaw(n\Collider,wayPointCloseToPlayer\obj))>0
                                    PointEntity n\obj, wayPointCloseToPlayer\obj
                                    RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
                                EndIf
                            EndIf
                        Else
                            n\PathTimer = 0
                        EndIf
                        
                        If n\PathTimer = 1
                            AnimateNPC(n, 843, 895, n\CurrSpeed*36)
                            n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
                            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
                        EndIf
                    EndIf
                    
                    If prevFrame < 893 And n\Frame=>893 Then
                        PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
                    ElseIf prevFrame < 867 And n\Frame=>867
                        PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))
                    EndIf
                EndIf
                
            Else
                n\State = 0
            EndIf
        Case 12
            AnimateNPC(n, 1539, 1553, 0.2, False)
            
            pvt% = CreatePivot()
            PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
            If n\State2 = 1.0
                PointEntity(pvt, Collider)
            Else
                RotateEntity pvt,0,n\Angle,0
            EndIf
            RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
            
            RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
            
            PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
            If n\State2 = 1.0
                PointEntity(pvt, Collider)
            Else
                RotateEntity pvt,0,n\Angle,0
            EndIf
            RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
            
            FreeEntity(pvt)
            
            n\ManipulateBone = True
            n\BoneToManipulate = "chest"
            n\BoneToManipulate2 = "head"
            n\ManipulationType = 1
            
            UpdateSoundOrigin(n\SoundChn,Camera,n\Collider,20)
        Default
            If Rand(400) = 1 Then n\PrevState = Rnd(-30, 30)
            n\PathStatus = 0
            AnimateNPC(n, 923, 1354, 0.2)
            ;Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
            
            RotateEntity(n\Collider, 0, CurveAngle(n\Angle + n\PrevState + Sin(MilliSecs2() / 50) * 2, EntityYaw(n\Collider), 50), 0, True)
    End Select
    
    If n\CurrSpeed > 0.01 Then
        If prevFrame > 1638 And n\Frame<1620 Then
            PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
        ElseIf prevFrame < 1627 And n\Frame=>1627
            PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
        EndIf
    EndIf
    
    
    n\Reload = Max(0, n\Reload - FPSfactor)
    ;RotateEntity(n\Collider, 0, EntityYaw(n\Collider), 0, True)
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
    
    ;RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), 0
    RotateEntity n\obj, -90, EntityYaw(n\Collider), 0
End Function
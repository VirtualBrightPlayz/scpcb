Function InitializeNPCtype372(n.NPCs)
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    n\obj = LoadAnimMesh("GFX/npcs/372.b3d")
    
    temp# = 0.35 / MeshWidth(n\obj)
    ScaleEntity n\obj, temp, temp, temp
End Function

Function UpdateNPCtype372(n.NPCs)
    RN$ = mainPlayer\currRoom\RoomTemplate\Name
    If RN$ <> "pocketdimension" And RN$ <> "dimension1499" Then 
        If n\Idle Then
            HideEntity(n\obj)
            If Rand(50) = 1 And (mainPlayer\blinkTimer < -5 And mainPlayer\blinkTimer > -15) Then
                ShowEntity(n\obj)
                angle# = EntityYaw(mainPlayer\collider)+Rnd(-90,90)
                
                dist = Rnd(1.5, 2.0)
                PositionEntity(n\Collider, EntityX(mainPlayer\collider) + Sin(angle) * dist, EntityY(mainPlayer\collider)+0.2, EntityZ(mainPlayer\collider) + Cos(angle) * dist)
                n\Idle = False
                n\State = Rand(20, 60)
                
                If Rand(300)=1 Then PlayRangedSound(RustleSFX(Rand(0,2)),mainPlayer\cam, n\obj, 8, Rnd(0.0,0.2))
            End If
        Else
            PositionEntity(n\obj, EntityX(n\Collider) + Rnd(-0.005, 0.005), EntityY(n\Collider)+0.3+0.1*Sin(TimeInPosMilliSecs()/2), EntityZ(n\Collider) + Rnd(-0.005, 0.005))
            RotateEntity n\obj, 0, EntityYaw(n\Collider), ((TimeInPosMilliSecs()/5) Mod 360)
            
            AnimateNPC(n, 32, 113, 0.4)
            ;Animate2(n\obj, AnimTime(n\obj), 32, 113, 0.4)
            
            If EntityInView(n\obj, mainPlayer\cam) Then
                If Rand(30)=1 Then 
                    If (Not IsChannelPlaying(n\soundChannels[0])) Then
                        If EntityVisible(mainPlayer\cam, n\obj) Then 
                            n\soundChannels[0] = PlayRangedSound(RustleSFX(Rand(0,2)),mainPlayer\cam, n\obj, 8, 0.3)
                        EndIf
                    EndIf
                EndIf
                
                temp = CreatePivot()
                PositionEntity temp, EntityX(mainPlayer\collider), EntityY(mainPlayer\collider), EntityZ(mainPlayer\collider)
                PointEntity temp, n\Collider
                
                angle =  WrapAngle(EntityYaw(mainPlayer\collider)-EntityYaw(temp))
                If angle < 180 Then
                    RotateEntity n\Collider, 0, EntityYaw(mainPlayer\collider)-80, 0		
                Else
                    RotateEntity n\Collider, 0, EntityYaw(mainPlayer\collider)+80, 0
                EndIf
                FreeEntity temp
                
                MoveEntity n\Collider, 0, 0, 0.03*timing\tickDuration
                
                n\State = n\State-timing\tickDuration
            EndIf
            n\State=n\State-(timing\tickDuration/80.0)
            If n\State <= 0 Then n\Idle = True	
        End If
        
    EndIf
    
    n\DropSpeed = 0
    ResetEntity(n\Collider)
End Function

Function InitializeNPCtype372(n.NPCs)
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    n\obj = LoadAnimMesh_Strict("GFX\npcs\372.b3d")
    
    temp# = 0.35 / MeshWidth(n\obj)
    ScaleEntity n\obj, temp, temp, temp
End Function

Function UpdateNPCtype372(n.NPCs)
    RN$ = PlayerRoom\RoomTemplate\Name
    If RN$ <> "pocketdimension" And RN$ <> "dimension1499" Then 
        If n\Idle Then
            HideEntity(n\obj)
            If Rand(50) = 1 And (BlinkTimer < -5 And BlinkTimer > -15) Then
                ShowEntity(n\obj)
                angle# = EntityYaw(Collider)+Rnd(-90,90)
                
                dist = Rnd(1.5, 2.0)
                PositionEntity(n\Collider, EntityX(Collider) + Sin(angle) * dist, EntityY(Collider)+0.2, EntityZ(Collider) + Cos(angle) * dist)
                n\Idle = False
                n\State = Rand(20, 60)
                
                If Rand(300)=1 Then PlaySound2(RustleSFX(Rand(0,2)),Camera, n\obj, 8, Rnd(0.0,0.2))
            End If
        Else
            PositionEntity(n\obj, EntityX(n\Collider) + Rnd(-0.005, 0.005), EntityY(n\Collider)+0.3+0.1*Sin(MilliSecs2()/2), EntityZ(n\Collider) + Rnd(-0.005, 0.005))
            RotateEntity n\obj, 0, EntityYaw(n\Collider), ((MilliSecs2()/5) Mod 360)
            
            AnimateNPC(n, 32, 113, 0.4)
            ;Animate2(n\obj, AnimTime(n\obj), 32, 113, 0.4)
            
            If EntityInView(n\obj, Camera) Then
                GiveAchievement(Achv372)
                
                If Rand(30)=1 Then 
                    If (Not ChannelPlaying(n\SoundChn)) Then
                        If EntityVisible(Camera, n\obj) Then 
                            n\SoundChn = PlaySound2(RustleSFX(Rand(0,2)),Camera, n\obj, 8, 0.3)
                        EndIf
                    EndIf
                EndIf
                
                temp = CreatePivot()
                PositionEntity temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
                PointEntity temp, n\Collider
                
                angle =  WrapAngle(EntityYaw(Collider)-EntityYaw(temp))
                If angle < 180 Then
                    RotateEntity n\Collider, 0, EntityYaw(Collider)-80, 0		
                Else
                    RotateEntity n\Collider, 0, EntityYaw(Collider)+80, 0
                EndIf
                FreeEntity temp
                
                MoveEntity n\Collider, 0, 0, 0.03*FPSfactor
                
                n\State = n\State-FPSfactor
            EndIf
            n\State=n\State-(FPSfactor/80.0)
            If n\State <= 0 Then n\Idle = True	
        End If
        
    EndIf
    
    n\DropSpeed = 0
    ResetEntity(n\Collider)
End Function

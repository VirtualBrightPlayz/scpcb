Function InitializeNPCtypeD(n.NPCs)
    n\NVName = "Human"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.32
    EntityType n\Collider, HIT_PLAYER
    
    n\obj = CopyEntity(ClassDObj)
    
    temp# = 0.5 / MeshWidth(n\obj)
    ScaleEntity n\obj, temp, temp, temp
    
    n\Speed = 2.0 / 100
    
    MeshCullBox (n\obj, -MeshWidth(ClassDObj), -MeshHeight(ClassDObj), -MeshDepth(ClassDObj), MeshWidth(ClassDObj)*2, MeshHeight(ClassDObj)*2, MeshDepth(ClassDObj)*2)
End Function

Function UpdateNPCtypeD(n.NPCs)
    RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
    
    prevFrame = AnimTime(n\obj)
    
    Select n\State
        Case 0 ;idle
            n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
            Animate2(n\obj, AnimTime(n\obj), 210, 235, 0.1)
        Case 1 ;walking
            If n\State2 = 1.0
                n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
            Else
                n\CurrSpeed = CurveValue(0.015, n\CurrSpeed, 5.0)
            EndIf
            Animate2(n\obj, AnimTime(n\obj), 236, 260, n\CurrSpeed * 18)
        Case 2 ;running
            n\CurrSpeed = CurveValue(0.03, n\CurrSpeed, 5.0)
            Animate2(n\obj, AnimTime(n\obj), 301, 319, n\CurrSpeed * 18)
    End Select
    
    If n\State2 <> 2
        If n\State = 1
            If n\CurrSpeed > 0.01 Then
                If prevFrame < 244 And AnimTime(n\obj)=>244 Then
                    PlaySound2(StepSFX(GetStepSound(n\Collider),0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))						
                ElseIf prevFrame < 256 And AnimTime(n\obj)=>256
                    PlaySound2(StepSFX(GetStepSound(n\Collider),0,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))
                EndIf
            EndIf
        ElseIf n\State = 2
            If n\CurrSpeed > 0.01 Then
                If prevFrame < 309 And AnimTime(n\obj)=>309
                    PlaySound2(StepSFX(GetStepSound(n\Collider),1,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))
                ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301
                    PlaySound2(StepSFX(GetStepSound(n\Collider),1,Rand(0,2)),mainPlayer\cam, n\Collider, 8.0, Rnd(0.3,0.5))
                EndIf
            EndIf
        EndIf
    EndIf
    
    If n\Frame = 19 Or n\Frame = 60
        n\IsDead = True
    EndIf
    If AnimTime(n\obj)=19 Or AnimTime(n\obj)=60
        n\IsDead = True
    EndIf
    
    MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
    
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
    
    RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
End Function

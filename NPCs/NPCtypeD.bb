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
    
    MoveEntity(n\Collider, 0, 0, n\CurrSpeed * timing\tickDuration)
    
    PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
    
    RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
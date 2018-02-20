Const STATEGUARD_IDLE_LOOK%        = 0 ;TODO: Implement.
Const STATEGUARD_MOVE_TO_TARGET%   = 1
Const STATEGUARD_SHOOT_TARGET%     = 2
Const STATEGUARD_DEAD%             = 3

;AnimateNPC(n, 923, 1071, 0.2) ; IDLE STATE

Function InitializeNPCtypeGuard(n.NPCs)
    n\nvName = "Humen"
    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.2)
    EntityType(n\collider, HIT_PLAYER)

    LoadOrCopyMesh("GFX\npcs\guard.b3d")
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "Guard", "speed") / 100.0)

    Local temp# = (GetINIFloat("DATA\NPCs.ini", "Guard", "scale") / 2.5)
    ScaleEntity(n\obj, temp, temp, temp)
    
    MeshCullBox(n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
End Function

Function UpdateNPCtypeGuard(n.NPCs)
	Local dist#

    Local prevFrame# = n\frame
    
    Select n\state
        Case STATEGUARD_MOVE_TO_TARGET
            ;If guard was given a target then use its position.
			If (n\target <> Null) Then
				n\targetX = EntityX(n\target\collider)
				n\targetY = EntityY(n\target\collider)
				n\targetZ = EntityZ(n\target\collider)
            EndIf

            RotateEntity(n\collider, 0, CurveAngle(VectorYaw(n\targetX-EntityX(n\collider), 0, n\targetZ-EntityZ(n\collider))+n\angle, EntityYaw(n\collider), 20.0), 0)
            
            dist# = Distance(EntityX(n\collider), EntityZ(n\collider), n\targetX, n\targetZ)
            
            AnimateNPC(n, 1614, 1641, n\currSpeed * 30)
            
            If dist > 2.0 Or dist < 1.0  Then
                n\currSpeed = CurveValue(n\speed * Sgn(dist - 1.5) * 0.75, n\currSpeed, 10.0)
            Else
                n\currSpeed = CurveValue(0, n\currSpeed, 10.0)
            EndIf

            If n\currSpeed > 0.01 Then
                If (prevFrame > 1638 And n\Frame < 1620) Then
                    PlaySound2(StepSFX(2,0,Rand(0,2)), Camera, n\collider, 8.0, Rnd(0.5, 0.7))						
                ElseIf prevFrame < 1627 And n\Frame=>1627
                    PlaySound2(StepSFX(2,0,Rand(0,2)), Camera, n\collider, 8.0, Rnd(0.5, 0.7))						
                EndIf
            EndIf
            
            MoveEntity(n\collider, 0, 0, n\currSpeed * FPSfactor)

        Case STATEGUARD_SHOOT_TARGET
            ;Raising gun to aim animation.
            AnimateNPC(n, 1539, 1553, 0.2, False)
            
            ;If guard was given a target then use its position.
			If (n\target <> Null) Then
				n\targetX = EntityX(n\target\collider)
				n\targetY = EntityY(n\target\collider)
				n\targetZ = EntityZ(n\target\collider)
            EndIf

            Local pvt% = CreatePivot()
            PositionEntity(pvt, targetX, targetY, targetZ)

            ;TODO: Make relative to target.
            PointEntity(pvt, Collider)

            RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
            
            RotateEntity(n\collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\collider), 10), 0, True)
            
            ;Start shooting once the aiming animation is done.
            If (n\timer < 0 And n\Frame>1550) Then
                PlaySound2(GunshotSFX, Camera, n\Collider, 35)
                
                RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
                PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
                
                PointEntity(pvt, Collider)

                p.Particles = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
                PositionEntity(p\pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
                RotateEntity(p\pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
                MoveEntity (p\pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)

                n\Reload = 7
            EndIf

            FreeEntity(pvt)

            n\timer = n\timer - FPSfactor
    End Select
End Function
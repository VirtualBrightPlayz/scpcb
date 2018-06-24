Function InitializeNPCtypeTentacle(n.NPCs)
    n\nvName = "Unidentified"

    n\collider = CreatePivot()

	Local n2.NPCs
    For n2 = Each NPCs
        If (n\npcType = n2\npcType And n<>n2) Then
            n\obj = CopyEntity(n2\obj)
            Exit
        EndIf
    Next

    If (n\obj = 0) Then
        n\obj = LoadAnimMesh("GFX/NPCs/tentacle/tentacle.b3d")
        ScaleEntity(n\obj, 0.065,0.065,0.065)
    EndIf

	n\sounds[0] = LoadSound("SFX/Room/035Chamber/TentacleSpawn.ogg")
	n\sounds[1] = LoadSound("SFX/Room/035Chamber/TentacleAttack1.ogg")
	n\sounds[2] = LoadSound("SFX/Room/035Chamber/TentacleAttack2.ogg")

    SetAnimTime(n\obj, 283)
End Function

Function UpdateNPCtypeTentacle(n.NPCs)
    If (n\playerDistance < 8.0) Then

        Select n\state
            Case 0 ;spawn

                If (n\frame>283) Then
                    mainPlayer\heartbeatIntensity = Max(CurveValue(1.0, mainPlayer\heartbeatIntensity, 50),mainPlayer\heartbeatIntensity)
                    ;HeartBeatRate = Max(CurveValue(130, HeartBeatRate, 100),HeartBeatRate)

                    PointEntity(n\obj, mainPlayer\collider)
                    RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),25.0), 0)

                    AnimateNPC(n, 283, 389, 0.3, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 283, 389, 0.3, False)

                    If (n\frame>388) Then n\state = 1
                Else
                    If (n\playerDistance < 2.5) Then
                        SetNPCFrame(n, 284)
                        PlaySound2(n\sounds[0])
                    EndIf
                EndIf
                ;spawn 283,389
                ;attack 2, 32
                ;idle 33, 174
            Case 1 ;idle
                PointEntity(n\obj, mainPlayer\collider)
                RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),25.0), 0)

                AnimateNPC(n, 33, 174, 0.3, True)
                ;Animate2(n\obj, AnimTime(n\obj), 33, 174, 0.3, True)
            Case 2

                ;finish the idle animation before playing the attack animation
                If (n\frame>33 And n\frame<174) Then
                    AnimateNPC(n, 33, 174, 2.0, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 33, 174, 2.0, False)
                Else
                    PointEntity(n\obj, mainPlayer\collider)
                    RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),10.0), 0)

                    If (n\frame>33) Then
                        ;SetAnimTime(n\obj,2)
                        n\frame = 2
                        PlaySound2(n\sounds[Rand(1,2)])
                    EndIf
                    AnimateNPC(n, 2, 32, 0.3, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 2, 32, 0.3, False)

                    If (n\frame>=5 And n\frame<6) Then
                        If (n\playerDistance < 1.8) Then
                            If (Abs(DeltaYaw(n\collider, mainPlayer\collider))<20) Then
                                If (IsPlayerWearingTempName(mainPlayer,"hazmatsuit")) Then
                                    mainPlayer\injuries = mainPlayer\injuries+Rnd(0.5)
                                    PlaySound2(LoadTempSound("SFX/General/BodyFall.ogg"))
                                Else
                                    mainPlayer\blurTimer = 100
                                    mainPlayer\injuries = mainPlayer\injuries+Rnd(1.0,1.5)
                                    PlaySound2(DamageSFX(Rand(3,4)))

                                    If (mainPlayer\injuries > 3.0) Then
                                        DeathMSG = Chr(34)+"We will need more than the regular cleaning team to care of this. "
                                        DeathMSG = DeathMSG + "Two large and highly active tentacle-like appendages seem "
                                        DeathMSG = DeathMSG + "to have formed inside the chamber. Their level of aggression is "
                                        DeathMSG = DeathMSG + "unlike anything we've seen before - it looks like they have "
                                        DeathMSG = DeathMSG + "beaten some unfortunate Class D to death at some point during the breach."+Chr(34)
                                        Kill(mainPlayer)
                                    EndIf
                                EndIf

                            EndIf
                        EndIf

                        n\frame = 6
                        ;SetAnimTime(n\obj, 6)
                    ElseIf (n\frame=32) Then
                        n\state = 1
                        n\frame = 173
                        ;SetAnimTime(n\obj, 173)
                    EndIf
                EndIf

        End Select

        PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider), EntityZ(n\collider))
        RotateEntity(n\obj, EntityPitch(n\collider)-90, EntityYaw(n\collider)-180, EntityRoll(n\collider), True)

        n\dropSpeed = 0

        ResetEntity(n\collider)

    EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
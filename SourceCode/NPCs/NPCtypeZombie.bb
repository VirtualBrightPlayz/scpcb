Function InitializeNPCtypeZombie(n.NPCs)
    n\nvName = "Human"
    n\collider = CreatePivot()
    EntityRadius n\collider, 0.2
    EntityType n\collider, HIT_PLAYER

	Local n2.NPCs
    For n2.NPCs = Each NPCs
        If n\npcType = n2\npcType And n<>n2 Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
	
	Local temp#
    If n\obj = 0 Then
        n\obj = LoadAnimMesh("GFX/NPCs/zombie/zombie.b3d")

        temp# = (GetINIFloat("Data/NPCs.ini", "SCP-049-2", "scale") / 2.5)
        ScaleEntity n\obj, temp, temp, temp

        MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
    EndIf

    n\speed = (GetINIFloat("Data/NPCs.ini", "SCP-049-2", "speed") / 100.0)

    SetAnimTime(n\obj, 107)

    n\sounds[0] = LoadSound("SFX/SCP/049/0492Breath.ogg")

    n\hp = 100
End Function

Function UpdateNPCtypeZombie(n.NPCs)
	Local prevFrame# = n\frame
	
    If Abs(EntityY(mainPlayer\collider)-EntityY(n\collider))<4.0 Then
        If (Not n\isDead) Then
            Select n\state
                Case 0
                    AnimateNPC(n, 719, 777, 0.2, False)

                    ;Animate2(n\obj, AnimTime(n\obj), 719, 777, 0.2, False)
                    If n\frame=777 Then
                        If Rand(700)=1 Then
                            If EntityDistance(mainPlayer\collider, n\collider)<5.0 Then
                                n\frame = 719
                                ;SetAnimTime (n\obj, 719)
                                If Rand(3)=1 Then
                                    n\state=1
                                    n\frame = 155
                                    ;SetAnimTime n\obj, 155
                                EndIf
                            EndIf
                        EndIf
                    EndIf
                Case 1 ;stands up
                    If n\frame=>682 Then
                        AnimateNPC(n, 926, 935, 0.3, False)
                        If n\frame = 935 Then n\state = 2

                        ;Animate2(n\obj, AnimTime(n\obj), 926, 935, 0.3, False)
                        ;If AnimTime(n\obj)=935 Then n\state = 2
                    Else
                        AnimateNPC(n, 155, 682, 1.5, False)
                        ;Animate2(n\obj, AnimTime(n\obj), 155, 682, 1.5, False)
                    EndIf
                Case 2 ;following the player
                    If n\state3 < 0 Then ;check if the player is visible every three seconds
                        If EntityDistance(mainPlayer\collider, n\collider)<5.0 Then
                            If EntityVisible(mainPlayer\collider, n\collider) Then n\state2 = 70*5
                        EndIf
                        n\state3=70*3
                    Else
                        n\state3=n\state3-timing\tickDuration
                    EndIf

                    If n\state2 > 0 And (Not NoTarget) Then ;player is visible -> attack
                        n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 6.0, 0.6)

                        n\pathStatus = 0

                        PointEntity n\obj, mainPlayer\collider
                        RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 30.0), 0

                        If n\playerDistance < 0.7 Then
                            n\state = 3
                            If Rand(2)=1 Then
                                n\frame = 2
                                ;SetAnimTime n\obj, 2
                            Else
                                n\frame = 66
                                ;SetAnimTime n\obj, 66
                            EndIf
                        Else
                            n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
                            MoveEntity n\collider, 0, 0, n\currSpeed * timing\tickDuration

                            AnimateNPC(n, 936, 1017, n\currSpeed*60)

                            ;Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\currSpeed*60)

                        EndIf

                        n\state2=n\state2-timing\tickDuration
                    Else
                        If n\pathStatus = 1 Then ;path found
                            If n\path[n\pathLocation]=Null Then
                                If n\pathLocation > 19 Then
                                    n\pathLocation = 0 : n\pathStatus = 0
                                Else
                                    n\pathLocation = n\pathLocation + 1
                                EndIf
                            Else
                                PointEntity n\obj, n\path[n\pathLocation]\obj

                                RotateEntity n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 30.0), 0
                                n\currSpeed = CurveValue(n\speed, n\currSpeed, 20.0)
                                MoveEntity n\collider, 0, 0, n\currSpeed * timing\tickDuration

                                AnimateNPC(n, 936, 1017, n\currSpeed*60)
                                ;Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\currSpeed*60)

                                If EntityDistance(n\collider,n\path[n\pathLocation]\obj) < 0.2 Then
                                    n\pathLocation = n\pathLocation + 1
                                EndIf
                            EndIf
                        Else ;no path to the player, stands still
                            n\currSpeed = 0
                            AnimateNPC(n, 778, 926, 0.1)
                            ;Animate2(n\obj, AnimTime(n\obj), 778, 926, 0.1)

                            n\pathTimer = n\pathTimer-timing\tickDuration
                            If n\pathTimer =< 0 Then
                                n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.1,EntityZ(mainPlayer\collider))
                                n\pathTimer = n\pathTimer+70*5
                            EndIf
                        EndIf
                    EndIf
                Case 3
                    If NoTarget Then n\state = 2
                    If n\frame < 66 Then
                        AnimateNPC(n, 2, 65, 0.7, False)

                        ;Animate2(n\obj, AnimTime(n\obj), 2, 65, 0.7, False)
                        If prevFrame < 23 And n\frame=>23 Then
                            PlaySound2 DamageSFX(Rand(5,8))
                            mainPlayer\injuries = mainPlayer\injuries+Rnd(0.4,1.0)
                            DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
                        ElseIf n\frame=65 Then
                            n\state = 2
                        EndIf
                    Else
                        AnimateNPC(n, 66, 132, 0.7, False)
                        ;Animate2(n\obj, AnimTime(n\obj), 66, 132, 0.7, False)
                        If prevFrame < 90 And n\frame=>90 Then
                            PlaySound2 DamageSFX(Rand(5,8))
                            mainPlayer\injuries = mainPlayer\injuries+Rnd(0.4,1.0)
                            DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
                        ElseIf n\frame=132 Then
                            n\state = 2
                        EndIf
                    EndIf

            End Select
        Else
            AnimateNPC(n, 133, 157, 0.5, False)
        EndIf

        PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider) - 0.2, EntityZ(n\collider))

        RotateEntity n\obj, -90, EntityYaw(n\collider), 0
    EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
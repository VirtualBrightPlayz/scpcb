Function InitializeNPCtype096(n.NPC)
    n\nvName = "SCP-096"
    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.3)
    EntityType(n\collider, HIT_PLAYER)
    n\obj = LoadAnimMesh("GFX/NPCs/scp096/scp096.b3d")
	n\sounds[2] = LoadSound("SFX/Door/DoorOpenFast.ogg")

    n\speed = (GetINIFloat("Data/NPCs.ini", "SCP-096", "speed") / 100.0)

    Local temp# = (GetINIFloat("Data/NPCs.ini", "SCP-096", "scale") / 3.0)
    ScaleEntity(n\obj, temp, temp, temp)

    MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
End Function

Function UpdateNPCtype096(n.NPC)
	Local angle#, i%, pvt%, de.Decal
	Local dist2#

    Select n\state
        Case 0
            If (n\playerDistance < 8.0) Then
                If (n\sounds[0] = 0) Then
                    n\sounds[0] = LoadSound("SFX/Music/096.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 8.0, 1.0)
                EndIf

                AnimateNPC(n, 1085,1412, 0.1) ;sitting
                ;Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.1) ;sitting

                angle = WrapAngle(DeltaYaw(n\collider, mainPlayer\collider));-EntityYaw(n\collider,True))

                If (Not NoTarget) Then
                    If (angle<90 Or angle>270) Then
                        CameraProject(mainPlayer\cam,EntityX(n\collider), EntityY(n\collider)+0.25, EntityZ(n\collider))

                        If (ProjectedX()>0 And ProjectedX()<userOptions\screenWidth) Then
                            If (ProjectedY()>0 And ProjectedY()<userOptions\screenHeight) Then
                                If (EntityVisible(mainPlayer\collider, n\collider)) Then
                                    If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6) Then
                                        PlaySound2(LoadTempSound("SFX/SCP/096/Triggered.ogg"))

                                        mainPlayer\camZoom = 10

                                        n\frame = 307
                                        ;SetAnimTime(n\obj, 307)
                                        StopChannel(n\soundChannels[0])
                                        FreeSound(n\sounds[0])
                                        n\sounds[0] = 0
                                        n\state = 1
                                    EndIf
                                EndIf
                            EndIf
                        EndIf

                    EndIf
                EndIf

            EndIf
        Case 4
            mainPlayer\camZoom = CurveValue(Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0) * 10.0),mainPlayer\camZoom,8.0)

            If (n\target = Null) Then
                If (n\sounds[0] = 0) Then
                    n\sounds[0] = LoadSound("SFX/SCP/096/Scream.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 7.5, 1.0)
                EndIf

                If (n\sounds[1] = 0) Then
                    n\sounds[1] = LoadSound("SFX/Music/096Chase.ogg")
                Else
                    If (n\soundChannels[1] = 0) Then
                        n\soundChannels[1] = PlaySound(n\sounds[1])
                    Else
                        If (Not IsChannelPlaying(n\soundChannels[1])) Then n\soundChannels[1] = PlaySound(n\sounds[1])
                        ChannelVolume(n\soundChannels[1], Min(Max(8.0-n\playerDistance,0.6),1.0)*userOptions\sndVolume)
                    EndIf
                EndIf
            EndIf

            If (NoTarget And n\target = Null) Then n\state = 5

            If (Not mainPlayer\dead) Then

                If (TimeInPosMilliSecs() > n\state3) Then
                    n\lastSeen=0
                    If (n\target=Null) Then
                        If (EntityVisible(mainPlayer\collider, n\collider)) Then n\lastSeen=1
                    Else
                        If (EntityVisible(n\target\collider, n\collider)) Then n\lastSeen=1
                    EndIf
                    n\state3=TimeInPosMilliSecs()+3000
                EndIf

                If (n\lastSeen=1) Then
                    n\pathTimer=Max(70*3, n\pathTimer)
                    n\pathStatus=0

                    If (n\playerDistance < 2.8 Or n\frame<150) Then
                        If (n\frame>193) Then n\frame = 2.0 ;go to the start of the jump animation

                        AnimateNPC(n, 2, 193, 0.7)
                        ;Animate2(n\obj, AnimTime(n\obj), 2, 193, 0.7)

                        If (n\playerDistance > 1.0) Then
                            n\currSpeed = CurveValue(n\speed*2.0,n\currSpeed,15.0)
                        Else
                            n\currSpeed = 0

                            If (n\target=Null) Then
                                If (Not mainPlayer\godMode) Then
                                    PlaySound2(mainPlayer\damageSFX[4])

                                    pvt = CreatePivot()
                                    mainPlayer\camShake = 30
                                    mainPlayer\blurTimer = 2000
                                    DeathMSG = "A large amount of blood found in [DATA REDACTED]. DNA indentified as Subject D-9341. Most likely [DATA REDACTED] by SCP-096."
                                    Kill(mainPlayer)
                                    ;KillAnim = 1 ;TODO: idk
                                    For i = 0 To 6
                                        PositionEntity(pvt, EntityX(mainPlayer\collider)+Rnd(-0.1,0.1),EntityY(mainPlayer\collider)-0.05,EntityZ(mainPlayer\collider)+Rnd(-0.1,0.1))
                                        TurnEntity(pvt, 90, 0, 0)
                                        EntityPick(pvt,0.3)

                                        de = CreateDecal(Rand(DECAL_BLOOD_DROP1, DECAL_BLOOD_DROP2), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
                                        de\size = Rnd(0.2,0.6) : EntityAlpha(de\obj, 1.0) : ScaleSprite(de\obj, de\size, de\size)
                                    Next
                                    FreeEntity(pvt)
                                EndIf
                            EndIf
                        EndIf

                        If (n\target=Null) Then
                            PointEntity(n\collider, mainPlayer\collider)
                        Else
                            PointEntity(n\collider, n\target\collider)
                        EndIf

                    Else
                        If (n\target=Null) Then
                            PointEntity(n\obj, mainPlayer\collider)
                        Else
                            PointEntity(n\obj, n\target\collider)
                        EndIf

                        RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 5.0), 0)

                        If (n\frame>1000) Then n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)

                        If (n\frame<1058) Then
                            AnimateNPC(n, 892, 1058, n\speed*5, False)
                            ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\speed*5, False)
                        Else
                            AnimateNPC(n, 1059, 1074, n\currSpeed*5)
                            ;Animate2(n\obj, AnimTime(n\obj),1059,1074, n\currSpeed*5)
                        EndIf
                    EndIf

                    RotateEntity(n\collider, 0, EntityYaw(n\collider), 0, True)
                    MoveEntity(n\collider, 0,0,n\currSpeed)

                Else
                    If (n\pathStatus = 1) Then

                        If (n\path[n\pathLocation]=Null) Then
                            If (n\pathLocation > 19) Then
                                n\pathLocation = 0 : n\pathStatus = 0
                            Else
                                n\pathLocation = n\pathLocation + 1
                            EndIf
                        Else
                            PointEntity(n\obj, n\path[n\pathLocation]\obj)

                            RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 5.0), 0)

                            If (n\frame>1000) Then n\currSpeed = CurveValue(n\speed*1.5,n\currSpeed,15.0)
                            MoveEntity(n\collider, 0,0,n\currSpeed)

                            If (n\frame<1058) Then
                                AnimateNPC(n, 892,1058, n\speed*8, False)
                                ;Animate2(n\obj, AnimTime(n\obj),892,1058, n\speed*8, False)
                            Else
                                AnimateNPC(n, 1059,1084, n\currSpeed*8)
                                ;Animate2(n\obj, AnimTime(n\obj),1059,1084, n\currSpeed*8)
                            EndIf

                            dist2 = EntityDistance(n\collider,n\path[n\pathLocation]\obj)
                            If (dist2 < 0.4) Then
								;TODO: fix
;                                If (n\path[n\pathLocation]\door <> Null) Then
;									If (n\path[n\pathLocation]\door\open = False) Then
;										n\path[n\pathLocation]\door\open = True
;                                        n\path[n\pathLocation]\door\fastopen = 1
;                                        PlayRangedSound(n\sounds[2], mainPlayer\cam, n\path[n\pathLocation]\door\obj)
;                                    EndIf
;                                EndIf
                                If (dist2 < 0.2) Then n\pathLocation = n\pathLocation + 1

                            EndIf
                        EndIf

                    Else
                        AnimateNPC(n, 892,972, 0.2)
                        ;Animate2(n\obj, AnimTime(n\obj),892,972, 0.2)

                        n\pathTimer = Max(0, n\pathTimer-timing\tickDuration)
                        If (n\pathTimer<=0) Then
                            If (n\target<>Null) Then
                                n\pathStatus = FindPath(n, EntityX(n\target\collider),EntityY(n\target\collider)+0.2,EntityZ(n\target\collider))
                            Else
                                n\pathStatus = FindPath(n, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
                            EndIf
                            n\pathTimer = 70*5
                        EndIf
                    EndIf
                EndIf

                If (n\playerDistance > 32.0 Or EntityY(n\collider)<-50) Then
                    If (Rand(50)=1) Then TeleportCloser(n)
                EndIf
            Else ;play the eating animation if killtimer < 0
                AnimateNPC(n, Min(27,AnimTime(n\obj)), 193, 0.5)

                ;Animate2(n\obj, AnimTime(n\obj), Min(27,AnimTime(n\obj)), 193, 0.5)
            EndIf


        Case 1,2,3
            If (n\sounds[0] = 0) Then
                n\sounds[0] = LoadSound("SFX/Music/096Angered.ogg")
            Else
                n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 10.0, 1.0)
            EndIf

            If (n\state=1) Then ; get up
                If (n\frame>1085) Then

                    AnimateNPC(n, 1085, 1412, 0.3,False)
                    If (n\frame> 1411.9) Then n\frame = 307

                    ;Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.3,False)
                    ;If (AnimTime(n\obj)=1412) Then SetAnimTime(n\obj, 307)
                Else
                    AnimateNPC(n, 307, 424, 0.3, False)
                    If (n\frame > 423.9) Then
						n\state = 2 : n\frame = 892
					EndIf

                    ;Animate2(n\obj, AnimTime(n\obj),307,424, 0.3, False)
                    ;If (AnimTime(n\obj)=424) Then n\state = 2 : SetAnimTime(n\obj, 892)
                EndIf
            ElseIf (n\state=2) Then
                AnimateNPC(n, 833, 972, 0.3, False)

                ;Animate2(n\obj, AnimTime(n\obj),833,972, 0.3, False)
                If (n\frame>=972) Then
					n\state = 3 : n\state2=0
				EndIf
            ElseIf (n\state=3) Then
                n\state2 = n\state2+timing\tickDuration
                If (n\state2 > 70*18) Then
                    AnimateNPC(n, 973, 1001, 0.5, False)
                    ;Animate2(n\obj, AnimTime(n\obj),973,1001, 0.5, False)
                    If (n\frame>1000.9) Then
                        n\state = 4
                        StopChannel(n\soundChannels[0])
                        FreeSound(n\sounds[0])
						n\sounds[0] = 0
                    EndIf
                Else
                    AnimateNPC(n, 892,978, 0.3)
                    ;Animate2(n\obj, AnimTime(n\obj),892,978, 0.3)
                EndIf
            EndIf
        Case 5
            If (n\playerDistance < 16.0) Then

                If (n\sounds[0] = 0) Then
                    n\sounds[0] = LoadSound("SFX/Music/096.ogg")
                Else
                    n\soundChannels[0] = LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 14.0, 1.0)
                EndIf

                n\state2=n\state2+timing\tickDuration
                If (n\state2>1000) Then ;walking around
                    If (n\state2>1600) Then
						n\state2=Rand(0,500) : n\frame = 1652 ;: SetAnimTime(n\obj, 1652)
					EndIf

                    If (n\frame<1652) Then ;idle to walk
                        n\currSpeed = CurveValue(n\speed*0.1,n\currSpeed,5.0)
                        AnimateNPC(n, 1638,1652, n\currSpeed*45,False)
                        ;Animate2(n\obj, AnimTime(n\obj),1638,1652, n\currSpeed*45,False)
                    Else
                        n\currSpeed = CurveValue(n\speed*0.1,n\currSpeed,5.0)
                        AnimateNPC(n, 1653,1724, n\currSpeed*45) ;walk
                        ;Animate2(n\obj, AnimTime(n\obj),1653,1724, n\currSpeed*45) ;walk
                    EndIf

                    If (TimeInPosMilliSecs() > n\state3) Then
                        n\lastSeen=0
                        If (EntityVisible(mainPlayer\collider, n\collider)) Then
                            n\lastSeen=1
                        Else
                            HideEntity(n\collider)
                            EntityPick(n\collider, 1.5)
                            If (PickedEntity() <> 0) Then
                                n\angle = EntityYaw(n\collider)+Rnd(80,110)
                            EndIf
                            ShowEntity(n\collider)
                        EndIf
                        n\state3=TimeInPosMilliSecs()+3000
                    EndIf

                    If (n\lastSeen) Then
                        PointEntity(n\obj, mainPlayer\collider)
                        RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),130.0),0)
                        If (n\playerDistance < 1.5) Then n\state2=0
                    Else
                        RotateEntity(n\collider, 0, CurveAngle(n\angle,EntityYaw(n\collider),50.0),0)
                    EndIf
                Else
                    If (n\frame>1638) Then ;walk to idle
                        n\currSpeed = CurveValue(n\speed*0.05,n\currSpeed,8.0)
                        AnimateNPC(n, 1652, 1638, -n\currSpeed*45,False)
                        ;Animate2(n\obj, AnimTime(n\obj),1652,1638, -n\currSpeed*45,False)

                    Else ;idle
                        n\currSpeed = CurveValue(0,n\currSpeed,4.0)
                        AnimateNPC(n, 585, 633, 0.2) ;idle
                        ;Animate2(n\obj, AnimTime(n\obj),585,633, 0.2) ;idle
                    EndIf
                EndIf

                angle = WrapAngle(DeltaYaw(n\collider, mainPlayer\cam));-EntityYaw(n\collider))
                If (Not NoTarget) Then
                    If (angle<55 Or angle>360-55) Then
                        CameraProject(mainPlayer\cam,EntityX(n\collider), EntityY(mainPlayer\collider)+5.8*0.2-0.25, EntityZ(n\collider))

                        If (ProjectedX()>0 And ProjectedX()<userOptions\screenWidth) Then
                            If (ProjectedY()>0 And ProjectedY()<userOptions\screenHeight) Then
                                If (EntityVisible(mainPlayer\collider, n\collider)) Then
                                    If (mainPlayer\blinkTimer < - 16 Or mainPlayer\blinkTimer > - 6) Then
                                        PlaySound2(LoadTempSound("SFX/SCP/096/Triggered.ogg"))

                                        mainPlayer\camZoom = 10

                                        n\frame = 833
                                        ;SetAnimTime(n\obj, 833)
                                        StopChannel(n\soundChannels[0])
                                        FreeSound(n\sounds[0])
                                        n\sounds[0] = 0
                                        n\state = 2
                                    EndIf
                                EndIf
                            EndIf
                        EndIf

                    EndIf
                EndIf

                MoveEntity(n\collider, 0,0,n\currSpeed)
            EndIf
    End Select

    ;ResetEntity(n\collider)
    PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider)-0.07, EntityZ(n\collider))

    RotateEntity(n\obj, EntityPitch(n\collider), EntityYaw(n\collider), 0)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
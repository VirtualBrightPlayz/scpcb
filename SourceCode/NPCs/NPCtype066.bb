Function InitializeNPCtype066(n.NPCs)
    n\nvName = "SCP-066"
    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.2)
    EntityType(n\collider, HIT_PLAYER)

    n\obj = LoadAnimMesh("GFX/NPCs/scp066/scp-066.b3d")
    Local temp# = GetINIFloat("Data/NPCs.ini", "SCP-066", "scale")/2.5
    ScaleEntity(n\obj, temp, temp, temp)

    n\speed = (GetINIFloat("Data/NPCs.ini", "SCP-066", "speed") / 100.0)
End Function

Function UpdateNPCtype066(n.NPCs)
	Local w.WayPoints, de.Decals, d.Doors
	Local angle#

    Select n\state
        Case 0
            ;idle: moves around randomly from waypoint to another if the player is far enough
            ;starts staring at the player when the player is close enough

            If (n\playerDistance > 20.0) Then
                AnimateNPC(n, 451, 612, 0.2, True)
                ;Animate2(n\obj, AnimTime(n\obj), 451, 612, 0.2, True)

                If (n\state2 < TimeInPosMilliSecs()) Then
                    For w = Each WayPoints
                        ;If (w\door = Null) Then ;TODO: fix?
						If (Abs(EntityX(w\obj,True)-EntityX(n\collider))<4.0) Then
							If (Abs(EntityZ(w\obj,True)-EntityZ(n\collider))<4.0) Then
								PositionEntity(n\collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.3, EntityZ(w\obj,True))
								ResetEntity(n\collider)
								Exit
							EndIf
						EndIf
                        ;EndIf
                    Next
                    n\state2 = TimeInPosMilliSecs()+5000
                EndIf
            ElseIf (n\playerDistance < 8.0) Then
                n\lastDist = Rnd(1.0, 2.5)
                n\state = 1
            EndIf
        Case 1 ;staring at the player

            If (n\frame<451) Then
                angle = WrapAngle(CurveAngle(DeltaYaw(n\collider, mainPlayer\collider)-180, (AnimTime(n\obj)-2.0)/1.2445, 15.0))
                ;0->360 = 2->450
                SetNPCFrame(n,angle*1.2445+2.0)

                ;SetAnimTime(n\obj, angle*1.2445+2.0)
            Else
                AnimateNPC(n, 636, 646, 0.4, False)
                If (n\frame = 646) Then SetNPCFrame(n,2)
                ;Animate2(n\obj, AnimTime(n\obj), 636, 646, 0.4, False)
                ;If (AnimTime(n\obj)=646) Then SetAnimTime(n\obj, 2)
            EndIf

			If (Rand(700)=1) Then PlayRangedSound(LoadTempSound("SFX/SCP/066/Eric"+Str(Rand(1,3))+".ogg"),mainPlayer\cam, n\collider, 8.0)

            If (n\playerDistance < 1.0+n\lastDist) Then n\state = Rand(2,3)
        Case 2 ;roll towards the player and make a sound, and then escape
            If (n\frame < 647) Then
                angle = CurveAngle(0, (AnimTime(n\obj)-2.0)/1.2445, 5.0)

                If (angle < 5 Or angle > 355) Then
                    SetNPCFrame(n,647)
                Else
                    SetNPCFrame(n,angle*1.2445+2.0)
                EndIf
                ;SetAnimTime(n\obj, angle*1.2445+2.0)
                ;If (angle < 5 Or angle > 355) Then SetAnimTime(n\obj, 647)
            Else
                If (n\frame=683) Then
                    If (n\state2 = 0) Then
                        If (Rand(2)=1) Then
                            PlayRangedSound(LoadTempSound("SFX/SCP/066/Eric"+Str(Rand(1,3))+".ogg"),mainPlayer\cam, n\collider, 8.0)
                        Else
                            PlayRangedSound(LoadTempSound("SFX/SCP/066/Notes"+Str(Rand(1,6))+".ogg"), mainPlayer\cam, n\collider, 8.0)
                        EndIf

                        Select Rand(1,6)
                            Case 1
                                If (n\sounds[1]=0) Then n\sounds[1]=LoadSound("SFX/SCP/066/Beethoven.ogg")
                                n\soundChannels[1] = PlayRangedSound(n\sounds[1], mainPlayer\cam, n\collider)
                                mainPlayer\camShake = 10.0
                            Case 2
                                n\state3 = Rand(700,1400)
                            Case 3
                                For d = Each Doors
                                    If (d\locked = False And d\keyCard = 0 And d\code = "") Then
                                        If (Abs(EntityX(d\frameobj)-EntityX(n\collider))<16.0) Then
                                            If (Abs(EntityZ(d\frameobj)-EntityZ(n\collider))<16.0) Then
                                                UseDoor(d, False)
                                            EndIf
                                        EndIf
                                    EndIf
                                Next
                            Case 4
                                If (mainPlayer\currRoom\roomTemplate\disableDecals = False) Then
                                    mainPlayer\camShake = 5.0
                                    de = CreateDecal(1, EntityX(n\collider), 0.01, EntityZ(n\collider), 90, Rand(360), 0)
                                    de\size = 0.3 : UpdateDecals()
                                    PlaySound2(LoadTempSound("SFX/General/BodyFall.ogg"))
                                    If (Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(n\collider),EntityZ(n\collider))<0.8) Then
                                        mainPlayer\injuries = mainPlayer\injuries + Rnd(0.3,0.5)
                                    EndIf
                                EndIf
                        End Select
                    EndIf

                    n\state2 = n\state2+timing\tickDuration
                    If (n\state2>70) Then
                        n\state = 3
                        n\state2 = 0
                    EndIf
                Else
                    n\currSpeed = CurveValue(n\speed*1.5, n\currSpeed, 10.0)
                    PointEntity(n\obj,mainPlayer\collider)
                    ;angle = CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10);1.0/Max(n\currSpeed,0.0001))
                    RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj)-180, EntityYaw(n\collider), 10), 0)

                    AnimateNPC(n, 647, 683, n\currSpeed*25, False)
                    ;Animate2(n\obj, AnimTime(n\obj), 647, 683, n\currSpeed*25, False)

                    MoveEntity(n\collider, 0,0,-n\currSpeed*timing\tickDuration)

                EndIf
            EndIf
        Case 3
            PointEntity(n\obj,mainPlayer\collider)
            angle = CurveAngle(EntityYaw(n\obj)+n\angle-180, EntityYaw(n\collider), 10);1.0/Max(n\currSpeed,0.0001))
            RotateEntity(n\collider, 0, angle, 0)

            n\currSpeed = CurveValue(n\speed, n\currSpeed, 10.0)
            MoveEntity(n\collider, 0,0,n\currSpeed*timing\tickDuration)

            ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25)

            If (Rand(100)=1) Then n\angle = Rnd(-20,20)

            n\state2 = n\state2 + timing\tickDuration
            If (n\state2>250) Then
                AnimateNPC(n, 684, 647, -n\currSpeed*25, False)
                ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25, False)
                If (n\frame=647) Then
                    n\state = 0
                    n\state2=0
                EndIf
            Else
                AnimateNPC(n, 684, 647, -n\currSpeed*25)

                ;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\currSpeed*25)
            EndIf

    End Select

    If (n\state > 1) Then
        If (n\sounds[0] = 0) Then n\sounds[0] = LoadSound("SFX/SCP/066/Rolling.ogg")
        If (n\soundChannels[0]<>0) Then
            If (IsChannelPlaying(n\soundChannels[0])) Then
                n\soundChannels[0]= LoopRangedSound(n\sounds[0], n\soundChannels[0], mainPlayer\cam, n\collider, 20)
            EndIf
        Else
            n\soundChannels[0] = PlayRangedSound(n\sounds[0], mainPlayer\cam, n\collider, 20)
        EndIf
    EndIf

    ;If (n\soundChannels[1]<>0) Then
    ;	If (IsChannelPlaying(n\soundChannels[1])) Then
    ;		n\soundChannels[1] = LoopRangedSound(n\sounds[1], n\soundChannels[1], mainPlayer\cam, n\collider, 20)
    ;		mainPlayer\blurTimer = Max((5.0-dist)*300,0)
    ;	EndIf
    ;EndIf


    If (n\state3 > 0) Then
        n\state3 = n\state3-timing\tickDuration
        ;LightVolume = TempLightVolume-TempLightVolume*Min(Max(n\state3/500,0.01),0.6)
        mainPlayer\heartbeatIntensity = Max(mainPlayer\heartbeatIntensity, 130)
        ;HeartBeatVolume = Max(HeartBeatVolume,Min(n\state3/1000,1.0))
    EndIf

    If (IsChannelPlaying(n\soundChannels[1])) Then
        mainPlayer\blurTimer = Max((5.0-n\playerDistance)*300,0)
    EndIf

    PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider) - 0.2, EntityZ(n\collider))

    RotateEntity(n\obj, EntityPitch(n\collider)-90, EntityYaw(n\collider), 0)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
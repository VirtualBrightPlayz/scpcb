Function InitializeNPCtype860(n.NPCs)
    n\nvName = "Unidentified"

    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.25)
    EntityType(n\collider, HIT_PLAYER)
    n\obj = LoadAnimMesh("GFX/NPCs/scp860/forestmonster.b3d")

    EntityFX(n\obj, 1)

    Local tex% = LoadTexture("GFX/NPCs/scp860/860_eyes.png",1+2)

    n\obj2 = CreateSprite()
    ScaleSprite(n\obj2, 0.1, 0.1)
    EntityTexture(n\obj2, tex)
    FreeTexture(tex)

    EntityFX(n\obj2, 1 + 8)
    EntityBlend(n\obj2, BLEND_ADD)
    SpriteViewMode(n\obj2, 2)

    n\speed = (GetINIFloat("Data/NPCs.ini", "forestmonster", "speed") / 100.0)

    Local temp# = (GetINIFloat("Data/NPCs.ini", "forestmonster", "scale") / 20.0)
    ScaleEntity(n\obj, temp, temp, temp)

    MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
End Function

Function UpdateNPCtype860(n.NPCs)
	Local x%, z%, x2%, z2%, pvt%
	Local prevFrame%, angle#, temp%
	Local fr.Forest

    If (mainPlayer\currRoom\roomTemplate\name = "room860") Then
        fr=mainPlayer\currRoom\fr;Object.Forest(e\room\objects[1])

        Select n\state
            Case 0 ;idle (hidden)

                HideEntity(n\collider)
                HideEntity(n\obj)
                HideEntity(n\obj2)

                n\state2 = 0
                PositionEntity(n\collider, 0, -100, 0)
            Case 1 ;appears briefly behind the trees
                n\dropSpeed = 0

                If (EntityY(n\collider)<= -100) Then
                    TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)

                    x = Int(Floor((TFormedX()*RoomScale+6.0)/12.0))
                    z = Int(Floor((TFormedZ()*RoomScale+6.0)/12.0))

                    TFormPoint(x/RoomScale*12.0,0,z/RoomScale*12.0,fr\forest_Pivot,0)

                    For x2 = Int(Max(x-1,0)) To Int(Min(x+1,gridsize)) Step 2
                        For z2 = Int(Max(z-1,0)) To Int(Min(z+1,gridsize)) Step 2
                            If (fr\grid[(z2*gridsize)+x2]=0) Then

                                TFormPoint((x*12 + (x2-x)*6)/RoomScale,0,(z*12 + (z2-z)*6)/RoomScale,mainPlayer\currRoom\obj,0)

                                PositionEntity(n\collider, TFormedX(), EntityY(fr\forest_Pivot,True)+2.3, TFormedZ())

                                If (EntityInView(n\collider, mainPlayer\cam)) Then
                                    PositionEntity(n\collider, 0, -110, 0)
                                Else ;only spawn the monster outside the player's field of view
                                    x2 = Int(Min(x+1,gridsize))
                                    Exit
                                EndIf

                            EndIf
                        Next
                    Next

                    If (EntityY(n\collider)> -100) Then
                        PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0, 0.5)

                        Select Rand(3)
                            Case 1
                                PointEntity(n\collider,mainPlayer\collider)
                                n\frame = 2
                                ;SetAnimTime(n\obj, 2)
                            Case 2
                                PointEntity(n\collider,mainPlayer\collider)
                                n\frame = 201
                                ;SetAnimTime(n\obj, 201)
                            Case 3
                                PointEntity(n\collider,mainPlayer\collider)
                                TurnEntity(n\collider, 0, 90, 0)
                                n\frame = 299
                                ;SetAnimTime(n\obj, 299)
                        End Select

                        n\state2 = 0
                    EndIf
                Else

                    ShowEntity(n\obj)
                    ShowEntity(n\collider)

                    PositionEntity(n\collider, EntityX(n\collider), EntityY(fr\forest_Pivot,True)+2.3, EntityZ(n\collider))

                    If (n\state2 = 0) Then ;don't start moving until the player is looking
                        If (EntityInView(n\collider, mainPlayer\cam)) Then
                            n\state2 = 1
                            If (Rand(8)=1) Then
                                PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+Str(Rand(0,2))+".ogg"), mainPlayer\cam, n\collider, 20.0)
                            EndIf
                        EndIf
                    Else
                        If (n\frame<=199) Then
                            AnimateNPC(n, 2, 199, 0.5,False)
                            If (n\frame=199) Then
								n\frame = 298 : ;PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0)
							EndIf

                            ;Animate2(n\obj, AnimTime(n\obj), 2, 199, 0.5,False)
                            ;If (AnimTime(n\obj)=199) Then SetAnimTime(n\obj,298) : PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0)
                        ElseIf (n\frame <= 297) Then
                            PointEntity(n\collider,mainPlayer\collider)

                            AnimateNPC(n, 200, 297, 0.5, False)
                            If (n\frame=297) Then
								n\frame=298 : PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0)
							EndIf

                            ;Animate2(n\obj, AnimTime(n\obj), 200, 297, 0.5,False)
                            ;If (AnimTime(n\obj)=297) Then SetAnimTime(n\obj,298) : PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0)
                        Else
                            angle = CurveAngle(GetAngle(EntityX(n\collider),EntityZ(n\collider),EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider)),EntityYaw(n\collider)+90,20.0)

                            RotateEntity(n\collider, 0, angle-90, 0, True)

                            AnimateNPC(n, 298, 316, n\currSpeed*10)

                            ;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\currSpeed*10)

                            n\currSpeed = CurveValue(n\speed, n\currSpeed, 10.0)
                            MoveEntity(n\collider, 0,0,n\currSpeed*timing\tickDuration)

                            If (n\playerDistance>15.0) Then
                                PositionEntity(n\collider, 0,-110,0)
                                n\state = 0
                                n\state2 = 0
                            EndIf
                        EndIf
                    EndIf

                EndIf

                ResetEntity(n\collider)
            Case 2 ;appears on the path and starts to walk towards the player
                ShowEntity(n\obj)
                ShowEntity(n\collider)

                prevFrame = Int(n\frame)

                If (EntityY(n\collider)<= -100) Then
                    TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)
                    x = Int(Floor((TFormedX()*RoomScale+6.0)/12.0))
                    z = Int(Floor((TFormedZ()*RoomScale+6.0)/12.0))

                    For x2 = Int(Max(x-1,0)) To Int(Min(x+1,gridsize))
                        For z2 = Int(Max(z-1,0)) To Int(Min(z+1,gridsize))
                            If (fr\grid[(z2*gridsize)+x2]>0 And (x2<>x Or z2<>z) And (x2=x Or z2=z)) Then

                                TFormPoint((x2*12)/RoomScale,0,(z2*12)/RoomScale,mainPlayer\currRoom\obj,0)

                                ;PositionEntity(n\collider, TFormedX(), EntityY(fr\forest_Pivot,True)+0.5, TFormedZ())
                                PositionEntity(n\collider, TFormedX(), EntityY(fr\forest_Pivot,True)+1.0, TFormedZ())
                                DebugLog(Str(EntityY(fr\forest_Pivot,True)))

                                If (EntityInView(n\collider, mainPlayer\cam)) Then
                                    mainPlayer\blinkTimer=-10
                                Else
                                    x2 = Int(Min(x+1,gridsize))
                                    Exit
                                EndIf
                            EndIf
                        Next
                    Next
                Else

                    angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\collider)+90,80.0)

                    RotateEntity(n\collider, 0, angle-90, 0, True)

                    n\currSpeed = CurveValue(n\speed*0.3, n\currSpeed, 50.0)
                    MoveEntity(n\collider, 0,0,n\currSpeed*timing\tickDuration)

                    AnimateNPC(n, 494, 569, n\currSpeed*25)

                    ;Animate2(n\obj, AnimTime(n\obj), 494, 569, n\currSpeed*25)

                    If (n\state2 = 0) Then
                        If (n\playerDistance<8.0) Then
                            If (EntityInView(n\collider,mainPlayer\cam)) Then
                                PlaySound2(LoadTempSound("SFX/SCP/860/Chase"+Str(Rand(1,2))+".ogg"))

                                PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+Str(Rand(0,2))+".ogg"), mainPlayer\cam, n\collider)
                                n\state2 = 1
                            EndIf
                        EndIf
                    EndIf

                    If (mainPlayer\moveSpeed > 0.03) Then ;the player is running
                        n\state3 = n\state3 + timing\tickDuration
                        If (Rnd(5000)<n\state3) Then
                            temp = True
                            If (n\soundChannels[0] <> 0) Then
                                If (IsChannelPlaying(n\soundChannels[0])) Then temp = False
                            EndIf
                            If (temp) Then
                                n\soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+Str(Rand(0,2))+".ogg"), mainPlayer\cam, n\collider)
                            EndIf
                        EndIf
                    Else
                        n\state3 = Max(n\state3 - timing\tickDuration,0)
                    EndIf

                    If (n\playerDistance<4.5 Or n\state3 > Rnd(200,250)) Then
                        n\soundChannels[0] = PlayRangedSound(LoadTempSound("SFX/SCP/860/Cancer"+Str(Rand(3,5))+".ogg"), mainPlayer\cam, n\collider)
                        n\state = 3
                    EndIf

                    If (n\playerDistance > 16.0) Then
                        n\state = 0
                        n\state2 = 0
                        PositionEntity(n\collider, 0,-110,0)
                    EndIf
                EndIf

                ;535, 568
                If (prevFrame < 533 And n\frame=>533) Or (prevFrame > 568 And n\frame<2) Then
                    PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 15.0, 0.6)
                EndIf

            Case 3 ;runs towards the player and attacks
                ShowEntity(n\obj)
                ShowEntity(n\collider)

                prevFrame = Int(n\frame)

                angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\collider)+90,40.0)

                RotateEntity(n\collider, 0, angle-90, 0, True)

                If (n\sounds[0] = 0) Then n\sounds[0] = LoadSound("SFX/General/Slash1.ogg")
                If (n\sounds[1] = 0) Then n\sounds[1] = LoadSound("SFX/General/Slash2.ogg")

                If (n\playerDistance>1.1 And (Not mainPlayer\dead)) Then
                    n\currSpeed = CurveValue(n\speed*0.8, n\currSpeed, 10.0)

                    AnimateNPC(n, 298, 316, n\currSpeed*10)
                    ;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\currSpeed*10)

                    If (prevFrame < 307 And n\frame=>307) Then
                        PlayRangedSound(sndManager\footstep8601[Rand(0, 2)]\internal, mainPlayer\cam, n\collider, 10.0)
                    EndIf
                Else
                    ;461, 476

                    DeathMSG = ""

                    n\currSpeed = CurveValue(0.0, n\currSpeed, 5.0)

                    AnimateNPC(n, 451,493, 0.5, False)

                    ;Animate2(n\obj, AnimTime(n\obj), 451,493, 0.5, False)
                    If (prevFrame < 461 And n\frame=>461) Then
                        If (Not mainPlayer\dead) Then Kill(mainPlayer)
                        PlaySound2(n\sounds[0])
                    EndIf
                    If (prevFrame < 476 And n\frame=>476) Then PlaySound2(n\sounds[1])
                    If (prevFrame < 486 And n\frame=>486) Then PlaySound2(n\sounds[1])

                EndIf

                MoveEntity(n\collider, 0,0,n\currSpeed*timing\tickDuration)
        End Select

        If (n\state <> 0) Then
            RotateEntity(n\collider, 0, EntityYaw(n\collider), 0, True)

            PositionEntity(n\obj, EntityX(n\collider), EntityY(n\collider)-0.1, EntityZ(n\collider))
            RotateEntity(n\obj, EntityPitch(n\collider)-90, EntityYaw(n\collider), EntityRoll(n\collider), True)

            If (n\playerDistance > 8.0) Then
                ShowEntity(n\obj2)
                EntityAlpha(n\obj2, Min(n\playerDistance-8.0,1.0))

                PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
                RotateEntity(n\obj2, 0, EntityYaw(n\collider) - 180, 0)
                MoveEntity(n\obj2, 0, 30.0*0.025, -33.0*0.025)

                ;render distance is set to 8.5 inside the forest,
                ;so we need to cheat a bit to make the eyes visible if they're further than that
                pvt = CreatePivot()
                PositionEntity(pvt, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam))
                PointEntity(pvt, n\obj2)
                MoveEntity(pvt, 0,0,8.0)
                PositionEntity(n\obj2, EntityX(pvt),EntityY(pvt),EntityZ(pvt))
                FreeEntity(pvt)
            Else
                HideEntity(n\obj2)
            EndIf
        EndIf
    EndIf
End Function

;TODO: Move to 860 creature file.
Function Find860Angle#(n.NPCs, fr.Forest)
	TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)
	Local playerx% = Int(Floor((TFormedX()*RoomScale+6.0)/12.0))
	Local playerz% = Int(Floor((TFormedZ()*RoomScale+6.0)/12.0))
	
	TFormPoint(EntityX(n\collider),EntityY(n\collider),EntityZ(n\collider),0,mainPlayer\currRoom\obj)
	Local x# = (TFormedX()*RoomScale+6.0)/12.0
	Local z# = (TFormedZ()*RoomScale+6.0)/12.0
	
	Local xt% = Int(Floor(x)), zt% = Int(Floor(z))
	
	Local x2%, z2%
	If (xt<>playerx Or zt<>playerz) Then ;the monster is not on the same tile as the player
		For x2 = Int(Max(xt-1,0)) To Int(Min(xt+1,gridsize-1))
			For z2 = Int(Max(zt-1,0)) To Int(Min(zt+1,gridsize-1))
				If (fr\grid[(z2*gridsize)+x2]>0 And (x2<>xt Or z2<>zt) And (x2=xt Or z2=zt)) Then
					
					;tile (x2,z2) is closer to the player than the monsters current tile
					If ((Abs(playerx-x2)+Abs(playerz-z2))<(Abs(playerx-xt)+Abs(playerz-zt))) Then
						Return GetAngle(x-0.5,z-0.5,x2,z2)+EntityYaw(mainPlayer\currRoom\obj)+180
					EndIf
					
				EndIf
			Next
		Next
	Else
		Return GetAngle(EntityX(n\collider),EntityZ(n\collider),EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider))+180
	EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D
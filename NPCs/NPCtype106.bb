Const STATE106_IDLE%            = 0
Const STATE106_RISE%            = 1
Const STATE106_ATTACK%          = 2
Const STATE106_PD%              = 3
Const STATE106_MOVE_TO_TARGET%  = 4
Const STATE106_CONTAINED%       = 5

Type Data106
End Type

Function InitializeNPCtype106(n.NPCs)
    n\nvName = "SCP-106"
    n\collider = CreatePivot()
    EntityRadius(n\Collider, 0.2)
    EntityType(n\Collider, HIT_PLAYER)
    n\obj = LoadAnimMesh("GFX\npcs\106_2.b3d")

    n\gravity = False

    Local temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "scale") / 2.2)		
    ScaleEntity(n\obj, temp, temp, temp)

    Local oldManEyes% = LoadTexture("GFX\npcs\oldmaneyes.jpg")

    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "speed") / 100.0)

    n\obj2 = CreateSprite()
    ScaleSprite(n\obj2, 0.03, 0.03)
    EntityTexture(n\obj2, oldManEyes)
    EntityBlend (n\obj2, BLEND_ADD)
    EntityFX(n\obj2, 1 + 8) ;Full-bright + no fog.
    SpriteViewMode(n\obj2, 2)

    FreeTexture(oldManEyes)

    n\sounds[0] = LoadSound("SFX\SCP\106\Spawn.ogg")
    n\sounds[1] = LoadSound("SFX\SCP\106\Breathing.ogg")
    n\sounds[2] = LoadSound("SFX\SCP\106\Seen.ogg")
    n\sounds[3] = LoadSound("SFX\SCP\106\Corrosion1.ogg")
    n\sounds[4] = LoadSound("SFX\SCP\106\Corrosion2.ogg")
    n\sounds[5] = LoadSound("SFX\SCP\106\Corrosion3.ogg")
    n\sounds[6] = LoadSound("SFX\SCP\106\Laugh.ogg")
    n\sounds[7] = LoadSound("SFX\SCP\106\Catch.ogg")

    n\timer = Rand(22000, 27000)
End Function

Function UpdateNPCtype106(n.NPCs)
    Local dist# = EntityDistance(n\collider, mainPlayer\collider)
    Local dist2#
    Local visible%
    Local prevFrame#

    Select n\state
        Case STATE106_RISE
            If (mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499") Then
                Return
            EndIf

            n\timer = n\timer - (timing\tickDuration * (SelectedDifficulty\aggressiveNPCs * 2))

            ;Check if 106's timer is below 0, if not decrease it.
            If (n\timer >= 0) Then
                n\CurrSpeed = 0
                MoveEntity(n\Collider, 0, ((EntityY(mainPlayer\collider) - 30) - EntityY(n\Collider)) / 200.0, 0)
                n\Frame = 110
            ;Otherwise begin spawning 106.
            ElseIf n\timer >= -10
                If EntityY(n\Collider) < EntityY(mainPlayer\collider) - 20.0 - 0.55 Then
                    If Not mainPlayer\currRoom\RoomTemplate\DisableDecals Then
                        de.Decals = CreateDecal(0, EntityX(mainPlayer\collider), 0.01, EntityZ(mainPlayer\collider), 90, Rand(360), 0)
                        de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals
                    EndIf
                    
                    n\PrevY = EntityY(mainPlayer\collider)
                    
                    SetAnimTime n\obj, 110
                    
                    PlaySound(n\sounds[0])
                End If
                
                ;Corrosion.
                If Rand(500) = 1 Then PlayRangedSound(n\sounds[Rand(3, 5)], mainPlayer\cam, n\Collider)
                ;Breathing
                n\soundChn = LoopRangedSound(n\sounds[1], n\SoundChn, mainPlayer\cam, n\Collider, 8.0, 0.8)
                
                ;Rising.
                If n\timer >= - 10 Then
                    ShouldPlay = 66
                    If (n\Frame < 259) Then
                        PositionEntity n\Collider, EntityX(n\Collider), n\PrevY-0.15, EntityZ(n\Collider)
                        PointEntity n\obj, mainPlayer\collider
                        RotateEntity (n\Collider, 0, CurveValue(EntityYaw(n\obj),EntityYaw(n\Collider),100.0), 0, True)
                        
                        AnimateNPC(n, 110, 259, 0.15, False)
                    EndIf
                EndIf
            ;Switch to attacking state.
            Else
                n\timer = 0
                n\state = STATE106_ATTACK
            EndIf

        Case STATE106_ATTACK
            ;TODO: Set music to play 106 theme?
            If (dist < 8.0 And (Not NoTarget)) Then
                visible = EntityVisible(n\collider, mainPlayer\collider)
            EndIf

            ;Show glowing eyes.
            ;TODO: fix
			;If dist < CameraFogFar*LightVolume*0.6 Then
            ;    HideEntity n\obj2
            ;Else
            ;    ShowEntity n\obj2
            ;    EntityAlpha (n\obj2, Min(dist-CameraFogFar*LightVolume*0.6,1.0))
            ;EndIf

            If (visible) Then
                If EntityInView(n\collider, mainPlayer\cam) Then
                    mainPlayer\blurTimer = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), mainPlayer\blurTimer)
                    mainPlayer\camZoom = Max(mainPlayer\camZoom, (Sin(Float(TimeInPosMilliSecs())/20.0)+1.0) * 20.0 * Max((4.0-dist)/4.0,0))
                    
                    If (TimeInPosMilliSecs() - n\lastSeen > 60000) Then 
                        mainPlayer\camZoom = 40
                        PlaySound(n\sounds[2])
                        n\lastSeen = TimeInPosMilliSecs()
                    EndIf
                EndIf
            EndIf

            n\currSpeed = CurveValue(n\speed, n\currSpeed, 10.0)

            If dist > 0.8 Then
                prevFrame# = n\frame

                ;Walking animation.
                AnimateNPC(n, 284, 333, n\CurrSpeed * 43)
                
                ;Footstep sounds.
                If (prevFrame =< 286 And n\frame > 286) Then
                    PlayRangedSound(Step2SFX(Rand(0,2)),mainPlayer\cam, n\Collider, 6.0, Rnd(0.8,1.0))	
                ElseIf (prevFrame=<311 And n\frame > 311.0)
                    PlayRangedSound(Step2SFX(Rand(0,2)),mainPlayer\cam, n\Collider, 6.0, Rnd(0.8,1.0))
                EndIf

                If (dist > 25.0 Or Visible Or n\pathStatus = 2) Then
                    
                    PointEntity(n\obj, mainPlayer\collider)
                    RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10.0), 0)
                    
                    n\pathTimer = Max(n\pathTimer - timing\tickDuration, 0)
                    If n\pathTimer =< 0 Then
                        n\PathStatus = 0
                    EndIf
                ;Between 0.8 and 25 units.
                Else 
                    ;Pathfind to the player.
                    If n\PathTimer <= 0 Then
                        n\PathStatus = FindPath (n, EntityX(mainPlayer\collider,True), EntityY(mainPlayer\collider,True), EntityZ(mainPlayer\collider,True))
                        n\PathTimer = 70*10
                        n\CurrSpeed = 0
                    Else
                        n\PathTimer = Max(n\PathTimer-timing\tickDuration,0)
                        
                        If n\PathStatus = 2 Then
                            n\CurrSpeed = 0
                        ElseIf n\PathStatus = 1
                            If n\Path[n\PathLocation]=Null Then 
                                If n\PathLocation > 19 Then 
                                    n\PathLocation = 0 : n\PathStatus = 0
                                Else
                                    n\PathLocation = n\PathLocation + 1
                                EndIf
                            Else
                                TranslateEntity n\Collider, 0, ((EntityY(n\Path[n\PathLocation]\obj,True) - 0.11) - EntityY(n\Collider)) / 50.0, 0
                                
                                PointEntity n\obj, n\Path[n\PathLocation]\obj
                                
                                dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
                                
                                RotateEntity(n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), Min(20.0,dist2*10.0)), 0)
                                
                                If dist2 < 0.2 Then n\PathLocation = n\PathLocation + 1
                            EndIf
                        ElseIf n\PathStatus = 0
                            If n\State3=0 Then AnimateNPC(n, 334, 494, 0.3)
                        EndIf
                    EndIf
                EndIf
            Else
                ;Caught.
                If dist > 0.5 Then 
                    n\currSpeed = CurveValue(n\speed * 2.5, n\currSpeed, 10.0)
                Else
                    n\currSpeed = 0
                EndIf
                AnimateNPC(n, 105, 110, 0.15, False)
                
                If (Not mainPlayer\dead) And mainPlayer\fallTimer >= 0 Then
                    PointEntity(n\obj, mainPlayer\collider)
                    RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10.0), 0)									
                    
                    ;TODO: Teleport to pocket dimension.
                    If Ceil(n\Frame) = 110 And (Not mainPlayer\godMode) Then
                        PlaySound(DamageSFX(1))
                        PlaySound(n\sounds[7])

                        PlaySound(n\sounds[6])
                        mainPlayer\fallTimer = Min(-1, mainPlayer\fallTimer)
                        PositionEntity(mainPlayer\head, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), True)
                        ResetEntity(mainPlayer\head)
                        RotateEntity(mainPlayer\head, 0, EntityYaw(mainPlayer\cam) + Rand(-45, 45), 0)
                    EndIf
                EndIf
            EndIf

            MoveEntity(n\collider, 0, 0, n\currSpeed * timing\tickDuration)
            
            If (dist > 48) Then 
                ;Reset state.
                If (Not EntityInView(n\obj,mainPlayer\cam) And Rand(5)=1) Then
                    n\timer = Rand(22000, 27000)
                    n\state = STATE106_RISE
                ;Flank.
                Else
                    ;TODO
                EndIf
            EndIf
    End Select
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D
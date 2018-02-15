Function InitializeNPCtype106(n.NPCs)
    n\NVName = "SCP-106"
    n\Collider = CreatePivot()
    n\GravityMult = 0.0
    n\MaxGravity = 0.0
    EntityRadius n\Collider, 0.2
    EntityType n\Collider, HIT_PLAYER
    n\obj = LoadAnimMesh_Strict("GFX\npcs\106_2.b3d")

    ;If BumpEnabled Then 		
    ;	diff1 = LoadTexture_Strict("GFX\npcs\106_diffuse.png")
    ;	bump1 = LoadTexture_Strict("GFX\npcs\106_normals.png")
    ;	;TextureBlend bump1, FE_BUMP ;USE DOT3
    ;	;TextureBlend di1, FE_SPECULAR0
    ;	
    ;	;EntityTexture n\obj, bump1, 0, 0
    ;	EntityTexture n\obj, diff1, 0, 1
    ;	FreeTexture diff1
    ;	FreeTexture bump1
    ;EndIf

    temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "scale") / 2.2)		
    ScaleEntity n\obj, temp, temp, temp

    Local OldManEyes% = LoadTexture_Strict("GFX\npcs\oldmaneyes.jpg")

    n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "speed") / 100.0)

    n\obj2 = CreateSprite()
    ScaleSprite(n\obj2, 0.03, 0.03)
    EntityTexture(n\obj2, OldManEyes)
    EntityBlend (n\obj2, BLEND_ADD)
    EntityFX(n\obj2, 1 + 8)
    SpriteViewMode(n\obj2, 2)

    FreeTexture OldManEyes%
End Function

Function UpdateNPCtype106(n.NPCs)
    If Contained106 Then
        n\Idle = True
        HideEntity n\obj
        HideEntity n\obj2
        PositionEntity n\obj, 0,500.0,0, True
    Else
        
        dist = EntityDistance(n\Collider, Collider)
        
        Local spawn106% = True
        If PlayerRoom\RoomTemplate\Name$ = "dimension1499" Then spawn106% = False
        For e.Events = Each Events
            If e\EventName = "room860"
                If e\EventState = 1
                    spawn106% = False
                EndIf
                Exit
            EndIf
        Next
        If (Not spawn106%) And n\State <= 0 Then n\State = Rand(22000, 27000)
        
        If (Not n\Idle) And spawn106%
            If n\State <= 0 Then	;attacking	
                If EntityY(n\Collider) < EntityY(Collider) - 20.0 - 0.55 Then
                    If Not PlayerRoom\RoomTemplate\DisableDecals Then
                        de.Decals = CreateDecal(0, EntityX(Collider), 0.01, EntityZ(Collider), 90, Rand(360), 0)
                        de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals
                    EndIf
                    
                    n\PrevY = EntityY(Collider)
                    
                    SetAnimTime n\obj, 110
                    
                    If PlayerRoom\RoomTemplate\Name <> "coffin"
                        PositionEntity(n\Collider, EntityX(Collider), EntityY(Collider) - 15, EntityZ(Collider))
                    EndIf
                    
                    PlaySound_Strict(DecaySFX(0))
                End If
                
                If Rand(500) = 1 Then PlaySound2(OldManSFX(Rand(0, 2)), Camera, n\Collider)
                n\SoundChn = LoopSound2(OldManSFX(4), n\SoundChn, Camera, n\Collider, 8.0, 0.8)
                
                If n\State > - 10 Then
                    ShouldPlay = 66
                    If n\Frame<259 Then
                        PositionEntity n\Collider, EntityX(n\Collider), n\PrevY-0.15, EntityZ(n\Collider)
                        PointEntity n\obj, Collider
                        RotateEntity (n\Collider, 0, CurveValue(EntityYaw(n\obj),EntityYaw(n\Collider),100.0), 0, True)
                        
                        AnimateNPC(n, 110, 259, 0.15, False)
                    Else
                        n\State = -10
                    EndIf
                Else
                    If PlayerRoom\RoomTemplate\Name <> "gatea" Then ShouldPlay = 10
                    
                    Local Visible% = False
                    If dist < 8.0 Then
                        Visible% = EntityVisible(n\Collider, Collider)
                    EndIf
                    
                    If NoTarget Then Visible = False
                    
                    If Visible Then
                        If PlayerRoom\RoomTemplate\Name <> "gatea" Then n\PathTimer = 0
                        If EntityInView(n\Collider, Camera) Then
                            GiveAchievement(Achv106)
                            
                            ;Achievements(Achv106) = True
                            
                            BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
                            CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs2())/20.0)+1.0) * 20.0 * Max((4.0-dist)/4.0,0))
                            
                            If MilliSecs2() - n\LastSeen > 60000 Then 
                                CurrCameraZoom = 40
                                PlaySound_Strict(HorrorSFX(6))
                                n\LastSeen = MilliSecs2()
                            EndIf
                        EndIf
                    Else
                        n\State=n\State-FPSfactor
                    End If
                    
                    If dist > 0.8 Then
                        If (dist > 25.0 Or PlayerRoom\RoomTemplate\Name = "pocketdimension" Or Visible Or n\PathStatus = 2) And PlayerRoom\RoomTemplate\Name <> "gatea" Then 
                            
                            If (dist > 40 Or PlayerRoom\RoomTemplate\Name = "pocketdimension") Then
                                TranslateEntity n\Collider, 0, ((EntityY(Collider) - 0.14) - EntityY(n\Collider)) / 50.0, 0
                            EndIf
                            
                            n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                            
                            PointEntity n\obj, Collider
                            RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
                            
                            If KillTimer >= 0 Then
                                prevFrame# = n\Frame
                                AnimateNPC(n, 284, 333, n\CurrSpeed*43)
                                ;Animate2(n\obj, AnimTime(n\obj), 284, 333, n\CurrSpeed*43)
                                
                                If prevFrame =< 286 And n\Frame>286 Then
                                    PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))	
                                ElseIf prevFrame=<311 And n\Frame>311.0 
                                    PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))
                                EndIf
                            Else 
                                n\CurrSpeed = 0
                            EndIf
                            
                            n\PathTimer = Max(n\PathTimer-FPSfactor,0)
                            If n\PathTimer =< 0 Then n\PathStatus = 0
                        Else 
                            
                            If n\PathTimer <= 0 Then
                                n\PathStatus = FindPath (n, EntityX(Collider,True), EntityY(Collider,True), EntityZ(Collider,True))
                                n\PathTimer = 70*10
                                n\CurrSpeed = 0
                            Else
                                n\PathTimer = Max(n\PathTimer-FPSfactor,0)
                                
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
                                        
                                        RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), Min(20.0,dist2*10.0)), 0
                                        n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                                        
                                        prevFrame# = AnimTime(n\obj)
                                        AnimateNPC(n, 284, 333, n\CurrSpeed*43)
                                        ;Animate2(n\obj, AnimTime(n\obj), 284, 333, n\CurrSpeed*43)
                                        If prevFrame =< 286 And n\Frame>286 Then
                                            PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))	
                                        ElseIf prevFrame=<311 And n\Frame>311.0 
                                            PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))
                                        EndIf
                                        
                                        If dist2 < 0.2 Then n\PathLocation = n\PathLocation + 1
                                    EndIf
                                ElseIf n\PathStatus = 0
                                    If n\State3=0 Then AnimateNPC(n, 334, 494, 0.3)
                                    ;Animate2(n\obj, AnimTime(n\obj), 334, 494, 0.3)
                                    n\CurrSpeed = CurveValue(0,n\CurrSpeed,10.0)
                                EndIf
                            EndIf
                            
                        EndIf
                        
                    ElseIf PlayerRoom\RoomTemplate\Name <> "gatea" And (Not NoTarget) ;dist < 0.8
                        
                        If dist > 0.5 Then 
                            n\CurrSpeed = CurveValue(n\Speed * 2.5,n\CurrSpeed,10.0)
                        Else
                            n\CurrSpeed = 0
                        EndIf
                        AnimateNPC(n, 105, 110, 0.15, False)
                        ;Animate2(n\obj, AnimTime(n\obj), 105, 110, 0.15, False)
                        ;If Floor(AnimTime(n\obj)) = 43 Then SetAnimTime(n\obj, 43)
                        
                        If KillTimer >= 0 And FallTimer >= 0 Then
                            PointEntity n\obj, Collider
                            RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0										
                            
                            If Ceil(n\Frame) = 110 And (Not GodMode) Then
                                PlaySound_Strict(DamageSFX(1))
                                PlaySound_Strict(HorrorSFX(5))											
                                If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
                                    DeathMSG = "Subject D-9341. Body partially decomposed by what is assumed to be SCP-106's "+Chr(34)+"corrosion"+Chr(34)+" effect. Body disposed of via incineration."
                                    Kill()
                                Else
                                    PlaySound_Strict(OldManSFX(3))
                                    FallTimer = Min(-1, FallTimer)
                                    PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
                                    ResetEntity (Head)
                                    RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
                                EndIf
                            EndIf
                        EndIf
                        
                    EndIf
                    
                EndIf 
                
                MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
                
                If n\State <= Rand(-3500, -3000) Then 
                    If Not EntityInView(n\obj,Camera) Then n\State = Rand(22000, 27000)
                EndIf
                
                If FallTimer < -250.0 Then
                    MoveToPocketDimension()
                    n\State = 250 ;make 106 idle for a while
                EndIf
                
            Else ;idling outside the map
                n\CurrSpeed = 0
                MoveEntity n\Collider, 0, ((EntityY(Collider) - 30) - EntityY(n\Collider)) / 200.0, 0
                n\DropSpeed = 0
                n\Frame = 110
                ;SetAnimTime n\obj, 110.0
                
                If (Not PlayerRoom\RoomTemplate\DisableDecals) Then 
                    If (SelectedDifficulty\aggressiveNPCs) Then
                        n\State=n\State-FPSfactor*2
                    Else
                        n\State=n\State-FPSfactor
                    EndIf
                EndIf
            End If
            
            ResetEntity(n\Collider)
            n\DropSpeed = 0
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.15, EntityZ(n\Collider))
            
            RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
            
            PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
            RotateEntity(n\obj2, 0, EntityYaw(n\Collider) - 180, 0)
            MoveEntity(n\obj2, 0, 8.6 * 0.11, -1.5 * 0.11)
            
            If PlayerRoom\RoomTemplate\Name = "pocketdimension" Or PlayerRoom\RoomTemplate\Name = "gatea" Then
                HideEntity n\obj2
            Else
                If dist < CameraFogFar*LightVolume*0.6 Then
                    HideEntity n\obj2
                Else
                    ShowEntity n\obj2
                    EntityAlpha (n\obj2, Min(dist-CameraFogFar*LightVolume*0.6,1.0))
                EndIf
            EndIf						
        Else
            HideEntity n\obj2
        EndIf
        
    EndIf
End Function

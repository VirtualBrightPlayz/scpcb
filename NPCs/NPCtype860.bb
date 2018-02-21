Function InitializeNPCtype860(n.NPCs)
    n\NVName = "Unidentified"
    
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.25
    EntityType n\Collider, HIT_PLAYER
    n\obj = LoadAnimMesh_Strict("GFX\npcs\forestmonster.b3d")
    
    EntityFX(n\obj, 1)
    
    tex = LoadTexture_Strict("GFX\npcs\860_eyes.png",1+2)
    
    n\obj2 = CreateSprite()
    ScaleSprite(n\obj2, 0.1, 0.1)
    EntityTexture(n\obj2, tex)
    FreeTexture tex
    
    EntityFX(n\obj2, 1 + 8)
    EntityBlend(n\obj2, BLEND_ADD)
    SpriteViewMode(n\obj2, 2)
    
    n\Speed = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "speed") / 100.0)
    
    temp# = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "scale") / 20.0)
    ScaleEntity n\obj, temp, temp, temp	
    
    MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
End Function

Function UpdateNPCtype860(n.NPCs)
    If mainPlayer\currRoom\RoomTemplate\Name = "room860" Then
        Local fr.Forest=mainPlayer\currRoom\fr;Object.Forest(e\room\Objects[1])
        
        dist = EntityDistance(mainPlayer\collider,n\Collider)
        
        Select n\State
            Case 0 ;idle (hidden)
                
                HideEntity n\Collider
                HideEntity n\obj
                HideEntity n\obj2
                
                n\State2 = 0
                PositionEntity(n\Collider, 0, -100, 0)
            Case 1 ;appears briefly behind the trees
                n\DropSpeed = 0
                
                If EntityY(n\Collider)<= -100 Then
                    TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)
                    
                    x = Floor((TFormedX()*RoomScale+6.0)/12.0)
                    z = Floor((TFormedZ()*RoomScale+6.0)/12.0)
                    
                    TFormPoint(x/RoomScale*12.0,0,z/RoomScale*12.0,fr\Forest_Pivot,0)
                    
                    For x2 = Max(x-1,0) To Min(x+1,gridsize) Step 2
                        For z2 = Max(z-1,0) To Min(z+1,gridsize) Step 2
                            If fr\grid[(z2*gridsize)+x2]=0 Then
                                
                                TFormPoint((x*12 + (x2-x)*6)/RoomScale,0,(z*12 + (z2-z)*6)/RoomScale,mainPlayer\currRoom\obj,0)
                                
                                PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+2.3, TFormedZ()
                                
                                If EntityInView(n\Collider, mainPlayer\cam) Then
                                    PositionEntity n\Collider, 0, -110, 0
                                Else ;only spawn the monster outside the player's field of view
                                    x2 = Min(x+1,gridsize)
                                    Exit												
                                EndIf
                                
                            EndIf
                        Next
                    Next
                    
                    If EntityY(n\Collider)> -100 Then
                        PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0, 0.5)
                        
                        Select Rand(3)
                            Case 1
                                PointEntity n\Collider, Collider
                                n\Frame = 2
                                ;SetAnimTime(n\obj, 2)
                            Case 2
                                PointEntity n\Collider, Collider
                                n\Frame = 201
                                ;SetAnimTime(n\obj, 201)
                            Case 3
                                PointEntity n\Collider, Collider
                                TurnEntity n\Collider, 0, 90, 0
                                n\Frame = 299
                                ;SetAnimTime(n\obj, 299)
                        End Select
                        
                        n\State2 = 0
                    EndIf
                Else
                    
                    ShowEntity n\obj
                    ShowEntity n\Collider
                    
                    PositionEntity n\Collider, EntityX(n\Collider), EntityY(fr\Forest_Pivot,True)+2.3, EntityZ(n\Collider)
                    
                    If n\State2 = 0 Then ;don't start moving until the player is looking
                        If EntityInView(n\Collider, mainPlayer\cam) Then 
                            n\State2 = 1
                            If Rand(8)=1 Then
                                PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), mainPlayer\cam, n\Collider, 20.0)
                            EndIf										
                        EndIf
                    Else
                        If n\Frame<=199 Then
                            AnimateNPC(n, 2, 199, 0.5,False)
                            If n\Frame=199 Then n\Frame = 298 : PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0)
                            
                            ;Animate2(n\obj, AnimTime(n\obj), 2, 199, 0.5,False)
                            ;If AnimTime(n\obj)=199 Then SetAnimTime(n\obj,298) : PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0)
                        ElseIf n\Frame <= 297
                            PointEntity n\Collider, Collider
                            
                            AnimateNPC(n, 200, 297, 0.5, False)
                            If n\Frame=297 Then n\Frame=298 : PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0)
                            
                            ;Animate2(n\obj, AnimTime(n\obj), 200, 297, 0.5,False)
                            ;If AnimTime(n\obj)=297 Then SetAnimTime(n\obj,298) : PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0)
                        Else
                            angle = CurveAngle(GetAngle(EntityX(n\Collider),EntityZ(n\Collider),EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider)),EntityYaw(n\Collider)+90,20.0)
                            
                            RotateEntity n\Collider, 0, angle-90, 0, True
                            
                            AnimateNPC(n, 298, 316, n\CurrSpeed*10)
                            
                            ;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
                            
                            n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 10.0)
                            MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
                            
                            If dist>15.0 Then
                                PositionEntity n\Collider, 0,-110,0
                                n\State = 0
                                n\State2 = 0
                            EndIf
                        EndIf									
                    EndIf
                    
                EndIf
                
                ResetEntity n\Collider
            Case 2 ;appears on the path and starts to walk towards the player
                ShowEntity n\obj
                ShowEntity n\Collider
                
                prevFrame = n\Frame
                
                If EntityY(n\Collider)<= -100 Then
                    TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)
                    x = Floor((TFormedX()*RoomScale+6.0)/12.0)
                    z = Floor((TFormedZ()*RoomScale+6.0)/12.0)
                    
                    For x2 = Max(x-1,0) To Min(x+1,gridsize)
                        For z2 = Max(z-1,0) To Min(z+1,gridsize)
                            If fr\grid[(z2*gridsize)+x2]>0 And (x2<>x Or z2<>z) And (x2=x Or z2=z) Then
                                
                                TFormPoint((x2*12)/RoomScale,0,(z2*12)/RoomScale,mainPlayer\currRoom\obj,0)
                                
                                ;PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+0.5, TFormedZ()
                                PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+1.0, TFormedZ()
                                DebugLog EntityY(fr\Forest_Pivot,True)
                                
                                If EntityInView(n\Collider, mainPlayer\cam) Then
                                    mainPlayer\blinkTimer=-10
                                Else
                                    x2 = Min(x+1,gridsize)
                                    Exit
                                EndIf
                            EndIf
                        Next
                    Next
                Else
                    
                    angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,80.0)
                    
                    RotateEntity n\Collider, 0, angle-90, 0, True
                    
                    n\CurrSpeed = CurveValue(n\Speed*0.3, n\CurrSpeed, 50.0)
                    MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
                    
                    AnimateNPC(n, 494, 569, n\CurrSpeed*25)
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 494, 569, n\CurrSpeed*25)
                    
                    If n\State2 = 0 Then
                        If dist<8.0 Then
                            If EntityInView(n\Collider,mainPlayer\cam) Then
                                PlaySound_Strict LoadTempSound("SFX\SCP\860\Chase"+Rand(1,2)+".ogg")
                                
                                PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), mainPlayer\cam, n\Collider)	
                                n\State2 = 1
                            EndIf										
                        EndIf
                    EndIf
                    
                    If CurrSpeed > 0.03 Then ;the player is running
                        n\State3 = n\State3 + FPSfactor
                        If Rnd(5000)<n\State3 Then
                            temp = True
                            If n\SoundChn <> 0 Then
                                If ChannelPlaying (n\SoundChn) Then temp = False
                            EndIf
                            If temp Then
                                n\SoundChn = PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), mainPlayer\cam, n\Collider)
                            EndIf
                        EndIf
                    Else
                        n\State3 = Max(n\State3 - FPSfactor,0)
                    EndIf
                    
                    If dist<4.5 Or n\State3 > Rnd(200,250) Then
                        n\SoundChn = PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(3,5)+".ogg"), mainPlayer\cam, n\Collider)
                        n\State = 3
                    EndIf
                    
                    If dist > 16.0 Then
                        n\State = 0
                        n\State2 = 0
                        PositionEntity n\Collider, 0,-110,0
                    EndIf
                EndIf
                
                ;535, 568
                If (prevFrame < 533 And n\Frame=>533) Or (prevFrame > 568 And n\Frame<2) Then
                    PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 15.0, 0.6)
                EndIf
                
            Case 3 ;runs towards the player and attacks
                ShowEntity n\obj
                ShowEntity n\Collider
                
                prevFrame = n\Frame
                
                angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,40.0)
                
                RotateEntity n\Collider, 0, angle-90, 0, True
                
                If n\Sound = 0 Then n\Sound = LoadSound_Strict("SFX\General\Slash1.ogg")
                If n\Sound2 = 0 Then n\Sound2 = LoadSound_Strict("SFX\General\Slash2.ogg")
                
                If dist>1.1 And KillTimer => 0 Then 
                    n\CurrSpeed = CurveValue(n\Speed*0.8, n\CurrSpeed, 10.0)
                    
                    AnimateNPC(n, 298, 316, n\CurrSpeed*10)
                    ;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
                    
                    If (prevFrame < 307 And n\Frame=>307) Then
                        PlaySound2(Step2SFX(Rand(3,5)), mainPlayer\cam, n\Collider, 10.0)
                    EndIf
                Else
                    ;461, 476
                    
                    DeathMSG = ""
                    
                    n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
                    
                    AnimateNPC(n, 451,493, 0.5, False)
                    
                    ;Animate2(n\obj, AnimTime(n\obj), 451,493, 0.5, False)
                    If (prevFrame < 461 And n\Frame=>461) Then 
                        If KillTimer => 0 Then Kill() : KillAnim = 0
                        PlaySound_Strict(n\Sound)
                    EndIf
                    If (prevFrame < 476 And n\Frame=>476) Then PlaySound_Strict(n\Sound2)
                    If (prevFrame < 486 And n\Frame=>486) Then PlaySound_Strict(n\Sound2)
                    
                EndIf
                
                MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
        End Select
        
        If n\State <> 0 Then
            RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
            
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.1, EntityZ(n\Collider))
            RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True
            
            If dist > 8.0 Then
                ShowEntity n\obj2
                EntityAlpha n\obj2, Min(dist-8.0,1.0)
                
                PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
                RotateEntity(n\obj2, 0, EntityYaw(n\Collider) - 180, 0)
                MoveEntity(n\obj2, 0, 30.0*0.025, -33.0*0.025)
                
                ;render distance is set to 8.5 inside the forest,
                ;so we need to cheat a bit to make the eyes visible if they're further than that
                pvt = CreatePivot()
                PositionEntity pvt, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam)
                PointEntity pvt, n\obj2
                MoveEntity pvt, 0,0,8.0
                PositionEntity n\obj2, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
                FreeEntity pvt
            Else
                HideEntity n\obj2
            EndIf
        EndIf
    EndIf
End Function
